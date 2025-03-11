package org.example.view;

import org.example.controller.UserManager;

import javax.swing.*;
import java.awt.*;

/**
 * LoginGUI is responsible for handling the user authentication process, including user login, admin login, and sign-up.
 */
public class LoginGUI {
    private boolean isAdmin;
    private boolean isAuthenticated;
    private UserManager userManager;
    private static String currentUsername;

    /**
     * Constructor for the LoginGUI class, initializes authentication status and creates the login GUI.
     */
    public LoginGUI() {
        userManager = new UserManager();
        isAuthenticated = false;
        isAdmin = false;

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
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Shows the login dialog for user or admin login based on the isAdminLogin flag.
     *
     * @param isAdminLogin indicates whether to show the admin login dialog
     */
    private void showLoginDialog(boolean isAdminLogin) {
        JDialog loginDialog = new JDialog();
        loginDialog.setTitle(isAdminLogin ? "Login as Admin" : "Login as User");
        loginDialog.setSize(300, 200);
        loginDialog.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");

        loginDialog.add(lblUsername);
        loginDialog.add(txtUsername);
        loginDialog.add(lblPassword);
        loginDialog.add(txtPassword);
        loginDialog.add(new JLabel());
        loginDialog.add(btnLogin);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (isAdminLogin) {
                if (username.equals("admin") && password.equals("admin123")) {
                    isAdmin = true;
                    isAuthenticated = true;
                    loginDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginDialog, "Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (userManager.authenticateUser(username, password)) {
                    isAdmin = false;
                    isAuthenticated = true;
                    setCurrentUsername(username);
                    loginDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginDialog, "Invalid user credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginDialog.setVisible(true);
    }

    /**
     * Shows the sign-up dialog for creating a new user.
     */
    private void showSignUpDialog() {
        JDialog signUpDialog = new JDialog();
        signUpDialog.setTitle("Sign Up");
        signUpDialog.setSize(300, 200);
        signUpDialog.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnSignUp = new JButton("Sign Up");

        signUpDialog.add(lblUsername);
        signUpDialog.add(txtUsername);
        signUpDialog.add(lblPassword);
        signUpDialog.add(txtPassword);
        signUpDialog.add(new JLabel());
        signUpDialog.add(btnSignUp);

        btnSignUp.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (userManager.isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(signUpDialog, "Username already taken!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(signUpDialog, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                userManager.saveUserCredentials(username, password);
                JOptionPane.showMessageDialog(signUpDialog, "Sign up successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                signUpDialog.dispose();
            }
        });

        signUpDialog.setVisible(true);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets the username of the currently logged-in user.
     * This should be called when a user successfully logs in.
     */
    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    /**
     * Gets the username of the currently logged-in user.
     * @return the username of the current user
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }
}
