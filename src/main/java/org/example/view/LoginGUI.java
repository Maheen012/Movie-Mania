package org.example.view;

import org.example.controller.UserManager;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JButton with rounded edges for a modern look.
 */
class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - 2;
        g2.drawString(getText(), x, y);
        g2.dispose();
        super.paintComponent(g);
    }

}

/**
 * LoginGUI is responsible for handling the user authentication process, including user login, admin login, and sign-up.
 */
public class LoginGUI {
    private boolean isAdmin;
    private boolean isAuthenticated;
    private UserManager userManager;
    private static String currentUsername;


    /**
     * Sets the username of the currently logged-in user.
     *
     * @param username The username to be set as the current user.
     */
    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    /**
     * Gets the username of the currently logged-in user.
     *
     * @return the current username.
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Checks if the logged-in user is an admin.
     *
     * @return true if the user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    public void waitForLogin() {
        while (!isAuthenticated) {
            try {
                Thread.sleep(100); // Pause execution until the user logs in
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



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
        JButton btnLoginUser = new RoundedButton("Login as User");
        JButton btnLoginAdmin = new RoundedButton("Login as Admin");
        JButton btnSignUp = new RoundedButton("Sign Up");

        // Set button colors for improved UI
        btnLoginUser.setBackground(new Color(30, 144, 255));
        btnLoginUser.setForeground(Color.WHITE);
        btnLoginAdmin.setBackground(new Color(220, 20, 60));
        btnLoginAdmin.setForeground(Color.WHITE);
        btnSignUp.setBackground(new Color(34, 139, 34));
        btnSignUp.setForeground(Color.WHITE);

        // Add buttons to the frame
        frame.add(btnLoginUser);
        frame.add(btnLoginAdmin);
        frame.add(btnSignUp);

        // Add action listeners to handle button clicks
        btnLoginUser.addActionListener(e -> showLoginDialog(false));
        btnLoginAdmin.addActionListener(e -> showLoginDialog(true));
        btnSignUp.addActionListener(e -> showSignUpDialog());

        frame.setVisible(true); // Make the frame visible
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
        JButton btnLogin = new RoundedButton("Login");
        btnLogin.setBackground(new Color(30, 144, 255));
        btnLogin.setForeground(Color.WHITE);

        // Add components to the login dialog
        loginDialog.add(lblUsername);
        loginDialog.add(txtUsername);
        loginDialog.add(lblPassword);
        loginDialog.add(txtPassword);
        loginDialog.add(new JLabel());
        loginDialog.add(btnLogin);

        // Handle login logic
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
        JButton btnSignUp = new RoundedButton("Sign Up");
        btnSignUp.setBackground(new Color(34, 139, 34));
        btnSignUp.setForeground(Color.WHITE);

        // Add components to the sign-up dialog
        signUpDialog.add(lblUsername);
        signUpDialog.add(txtUsername);
        signUpDialog.add(lblPassword);
        signUpDialog.add(txtPassword);
        signUpDialog.add(new JLabel());
        signUpDialog.add(btnSignUp);

        // Handle sign-up logic
        btnSignUp.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (userManager.isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(signUpDialog, "Username already taken!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(signUpDialog, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                userManager.saveUserCredentials(username, password);
                JOptionPane.showMessageDialog(signUpDialog, "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                signUpDialog.dispose();
            }
        });

        signUpDialog.setVisible(true);
    }
}
