
package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;


public class LoginGUI {
    private boolean isAdmin;
    private boolean isAuthenticated;

    public LoginGUI() {
        isAuthenticated = false;
        isAdmin = false;

        JFrame frame = new JFrame("Login / Sign Up");
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();

        JButton btnLoginUser = new JButton("Login as User");
        JButton btnLoginAdmin = new JButton("Login as Admin");
        JButton btnSignUp = new JButton("Sign Up");

        frame.add(lblUsername);
        frame.add(txtUsername);
        frame.add(lblPassword);
        frame.add(txtPassword);
        frame.add(btnLoginUser);
        frame.add(btnLoginAdmin);
        frame.add(new JLabel()); // Empty space
        frame.add(btnSignUp);

        btnLoginUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                if (authenticateUser(username, password)) {
                    isAdmin = false;
                    isAuthenticated = true;
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid user credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLoginAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                if (username.equals("admin") && password.equals("admin123")) {
                    isAdmin = true;
                    isAuthenticated = true;
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                // Check if username is already taken
                if (isUsernameTaken(username)) {
                    JOptionPane.showMessageDialog(frame, "Username already taken!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Save the new user credentials to the CSV file
                    saveUserCredentials(username, password);
                    JOptionPane.showMessageDialog(frame, "Sign up successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        frame.setVisible(true);

        // Wait until login is successful
        while (!isAuthenticated) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private File getFilePath() {
        // Load the UserPass.csv directly from the classpath (this is a special way of loading resource files)
        URL getPathURL = getClass().getClassLoader().getResource("UserPass.csv");

        // Check if movies.csv was found
        if (getPathURL == null) {
            System.out.println("UserPass file not found in resources. Using the file system.");
        }

        // Convert the URL to a File object if found in the classpath
        try {
            return new File(getPathURL.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    private boolean authenticateUser(String username, String password) {
        // Read the CSV file and check for user credentials
        File file = getFilePath();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] credentials;
            while ((credentials = reader.readNext()) != null) {
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isUsernameTaken(String username) {
        // Read the CSV file and check if the username already exists
        File file = getFilePath();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] credentials;
            while ((credentials = reader.readNext()) != null) {
                if (credentials.length == 2 && credentials[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Save new user credentials to UserPass.csv
    private void saveUserCredentials(String username, String password) {
        File file = getFilePath();
        if (file == null) return;

        // Create a User object
        User newUser = new User(username, password);

        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))) {
            // If the file is empty, write the header
            if (file.length() == 0) {
                writer.writeNext(new String[]{"Username", "Password"});
            }

            // Write the new user's credentials to the file
            String[] userData = {newUser.getUsername(), newUser.getPassword()};
            writer.writeNext(userData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
