package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginGUI {
    private boolean isAdmin;
    private boolean isAuthenticated;
    private static final String CSV_FILE = "Movie-Mania/src/main/resources/UserPass.csv"; // File to store credentials

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

                if (isUsernameTaken(username)) {
                    JOptionPane.showMessageDialog(frame, "Username already taken!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
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

    public boolean isAdmin() {
        return isAdmin;
    }

    private boolean authenticateUser(String username, String password) {
        // Read the CSV file and check for user credentials
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isUsernameTaken(String username) {
        // Read the CSV file and check if the username already exists
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveUserCredentials(String username, String password) {
        // Append the new user credentials to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
