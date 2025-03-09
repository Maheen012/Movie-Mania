package org.example.view;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * LoginGUI is responsible for handling the user authentication process, including user login, admin login, and sign-up.
 */
public class LoginGUI {
    private boolean isAdmin; // Tracks if the user is an admin
    private boolean isAuthenticated; // Tracks if the user is authenticated

    /**
     * Constructor for the LoginGUI class, initializes authentication status and creates the login GUI.
     */
    public LoginGUI() {
        isAuthenticated = false; // Initialize authentication status to false
        isAdmin = false; // Initialize admin status to false

        // Create the main login/sign-up frame
        JFrame frame = new JFrame("Movie Mania");
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1, 10, 10));

        // Create buttons for user login, admin login, and sign-up
        JButton btnLoginUser = new JButton("Login as User");
        JButton btnLoginAdmin = new JButton("Login as Admin");
        JButton btnSignUp = new JButton("Sign Up");

        // Add buttons to the frame
        frame.add(btnLoginUser);
        frame.add(btnLoginAdmin);
        frame.add(btnSignUp);

        // Add action listener for the "Login as User" button
        btnLoginUser.addActionListener(e -> showLoginDialog(false));

        // Add action listener for the "Login as Admin" button
        btnLoginAdmin.addActionListener(e -> showLoginDialog(true));

        // Add action listener for the "Sign Up" button
        btnSignUp.addActionListener(e -> showSignUpDialog());

        frame.setVisible(true); // Make the frame visible

        // Wait until the user is authenticated
        while (!isAuthenticated) {
            try {
                Thread.sleep(100); // Sleep to avoid busy-waiting
            } catch (InterruptedException ex) {
                ex.printStackTrace(); // Handle interruption
            }
        }
    }

    /**
     * Shows the login dialog for user or admin login based on the isAdminLogin flag.
     *
     * @param isAdminLogin indicates whether to show the admin login dialog
     */
    private void showLoginDialog(boolean isAdminLogin) {
        // Create a new dialog for login, setting the title based on whether it's admin or user login
        JDialog loginDialog = new JDialog();
        loginDialog.setTitle(isAdminLogin ? "Login as Admin" : "Login as User");
        loginDialog.setSize(300, 200);
        loginDialog.setLayout(new GridLayout(3, 2, 10, 10));

        // Create labels and text fields for username and password
        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");

        // Add components to the dialog
        loginDialog.add(lblUsername);
        loginDialog.add(txtUsername);
        loginDialog.add(lblPassword);
        loginDialog.add(txtPassword);
        loginDialog.add(new JLabel());
        loginDialog.add(btnLogin);

        // Add action listener for the login button
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText(); // Get the entered username
            String password = new String(txtPassword.getPassword()); // Get the entered password

            if (isAdminLogin) {
                // Check admin credentials
                if (username.equals("admin") && password.equals("admin123")) {
                    isAdmin = true; // Set admin status to true
                    isAuthenticated = true; // Set authentication status to true
                    loginDialog.dispose(); // Close the login dialog
                } else {
                    // Show error message for invalid admin credentials
                    JOptionPane.showMessageDialog(loginDialog, "Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Check user credentials
                if (authenticateUser(username, password)) {
                    isAdmin = false; // Set admin status to false
                    isAuthenticated = true; // Set authentication status to true
                    loginDialog.dispose(); // Close the login dialog
                } else {
                    // Show error message for invalid user credentials
                    JOptionPane.showMessageDialog(loginDialog, "Invalid user credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginDialog.setVisible(true); // Make the dialog visible
    }

    /**
     * Shows the sign-up dialog for creating a new user.
     */
    private void showSignUpDialog() {
        // Create a new dialog for the sign-up process
        JDialog signUpDialog = new JDialog();
        signUpDialog.setTitle("Sign Up");
        signUpDialog.setSize(300, 200);
        signUpDialog.setLayout(new GridLayout(3, 2, 10, 10));

        // Create labels and text fields for username and password
        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnSignUp = new JButton("Sign Up");

        // Add components to the dialog
        signUpDialog.add(lblUsername);
        signUpDialog.add(txtUsername);
        signUpDialog.add(lblPassword);
        signUpDialog.add(txtPassword);
        signUpDialog.add(new JLabel());
        signUpDialog.add(btnSignUp);

        // Add action listener for the sign-up button
        btnSignUp.addActionListener(e -> {
            String username = txtUsername.getText(); // Get the entered username
            String password = new String(txtPassword.getPassword()); // Get the entered password

            // Check if the username is already taken
            if (isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(signUpDialog, "Username already taken!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (username.isEmpty() || password.isEmpty()) {
                // Check if username or password is empty
                JOptionPane.showMessageDialog(signUpDialog, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Save the new user credentials
                saveUserCredentials(username, password);
                JOptionPane.showMessageDialog(signUpDialog, "Sign up successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                signUpDialog.dispose(); // Close the sign-up dialog
            }
        });

        signUpDialog.setVisible(true); // Make the dialog visible
    }

    /**
     * Gets the file path for the UserPass.csv file.
     *
     * @return the file object representing the UserPass.csv file
     */
    private File getFilePath() {
        // Get the file path from resources
        URL getPathURL = getClass().getClassLoader().getResource("UserPass.csv");
        // Check if UserPass.csv is in resources
        if (getPathURL == null) {
            System.out.println("UserPass file not found in resources. Using the file system.");
        }
        try {
            return new File(getPathURL.toURI()); // Convert URL to File
        } catch (URISyntaxException e) {
            throw new RuntimeException(e); // Handle URI syntax exception
        }
    }

    /**
     * Checks if the logged-in user is an admin.
     *
     * @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Authenticates the user based on the provided username and password.
     *
     * @param username the entered username
     * @param password the entered password
     * @return true if the user is authenticated, false otherwise
     */
    private boolean authenticateUser(String username, String password) {
        File file = getFilePath(); // Get the path to the UserPass.csv file
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] credentials;
            while ((credentials = reader.readNext()) != null) {
                // Check if the username and password match
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true; // Authentication successful
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(); // Handle file reading errors
        }
        return false; // Authentication failed
    }

    /**
     * Checks if a username is already taken by another user.
     *
     * @param username the username to check
     * @return true if the username is taken, false otherwise
     */
    private boolean isUsernameTaken(String username) {
        File file = getFilePath(); // Get the path to the UserPass.csv file
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] credentials;
            while ((credentials = reader.readNext()) != null) {
                // Check if the username already exists
                if (credentials.length == 2 && credentials[0].equals(username)) {
                    return true; // Username is taken
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(); // Handle file reading errors
        }
        return false; // Username is available
    }

    /**
     * Saves user credentials (username and password) to the UserPass.csv file.
     *
     * @param username the username to save
     * @param password the password to save
     */
    public void saveUserCredentials(String username, String password) {
        File userFile = getFilePath();
        if (userFile == null) return;

        // Check if the user file exists to determine if we are appending or writing new
        boolean fileExists = userFile.exists();

        // If the username is already taken, don't append
        if (isUsernameTaken(username)) {
            return; // Return early if the username is already taken
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(userFile, true))) { // true enables append mode
            // Write the header only if the file doesn't already exist
            if (!fileExists) {
                String[] header = {"Username", "Password"};
                writer.writeNext(header);
            }

            // Save the user credentials (username, password)
            String[] userData = {username, password};
            writer.writeNext(userData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
