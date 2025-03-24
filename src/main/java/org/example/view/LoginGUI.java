package org.example.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import org.example.controller.MovieManager;
import org.example.controller.UserManager;

public class LoginGUI extends Application {
    private static MovieManager movieManager;
    private static UserManager userManager;
    private static String currentUsername;

    public static void setManagers(MovieManager mm, UserManager um) {
        movieManager = mm;
        userManager = um;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Movie Mania - Login");

        // UI Elements
        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        Button btnLogin = new Button("Login");
        Button btnSignUp = new Button("Sign Up");
        Button btnGuestLogin = new Button("Login as Guest");

        Label lblMessage = new Label();

        // Login Button Action
        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            // Check if credentials are for the admin user
            if (username.equals("admin") && password.equals("admin123")) {
                lblMessage.setText("Admin login successful!");
                currentUsername = "admin"; // Set currentUsername to "admin"

                // Open the Admin GUI if credentials are correct
                Platform.runLater(() -> {
                    AdminGUI adminGUI = new AdminGUI();
                    adminGUI.start(new Stage());
                    primaryStage.close();
                });

                // Check if user credentials are valid using UserManager
            } else if (userManager.authenticateUser(username, password)) {
                currentUsername = username;
                lblMessage.setText("Login successful!");

                // Open the User GUI if user credentials are correct
                Platform.runLater(() -> {
                    UserGUI userGUI = new UserGUI();
                    userGUI.setManagers(movieManager, userManager);
                    userGUI.start(new Stage());
                    primaryStage.close();
                });

            } else {
                lblMessage.setText("Invalid credentials. Try again.");
            }
        });

        // Sign Up Button Action
        btnSignUp.setOnAction(e -> {
            // Open sign-up form in a new window
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");

            // UI elements for sign-up form
            Label lblNewUsername = new Label("New Username:");
            TextField txtNewUsername = new TextField();
            Label lblNewPassword = new Label("New Password:");
            PasswordField txtNewPassword = new PasswordField();
            Button btnCreateAccount = new Button("Create Account");
            Label lblSignUpMessage = new Label();

            // Button action for creating account
            btnCreateAccount.setOnAction(event -> {
                String newUsername = txtNewUsername.getText();
                String newPassword = txtNewPassword.getText();

                // Add the new user using UserManager
                if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
                    userManager.saveUserCredentials(newUsername, newPassword);
                    lblSignUpMessage.setText("Account created successfully!");

                    // Optionally, close sign-up window after successful creation
                    signUpStage.close();
                } else {
                    lblSignUpMessage.setText("Please enter both username and password.");
                }
            });

            // Layout for sign-up window
            VBox signUpLayout = new VBox(10, lblNewUsername, txtNewUsername, lblNewPassword, txtNewPassword, btnCreateAccount, lblSignUpMessage);
            signUpLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
            signUpStage.setScene(new Scene(signUpLayout, 300, 200));
            signUpStage.show();
        });

        // Guest Login Button Action
        // Inside the LoginGUI class, modify the guest login button action
        btnGuestLogin.setOnAction(e -> {
            currentUsername = "guest"; // Set currentUsername to "guest"
            lblMessage.setText("Logged in as Guest");

            // Open the Guest GUI and display movies
            Platform.runLater(() -> {
                GuestGUI guestGUI = new GuestGUI();
                guestGUI.setManagers(movieManager, userManager);
                guestGUI.displayMovies(); // Display movies for the guest
            });
        });

        // Layout for the login screen
        VBox layout = new VBox(10, lblUsername, txtUsername, lblPassword, txtPassword, btnLogin, btnSignUp, btnGuestLogin, lblMessage);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 300, 300)); // Increased height to accommodate the new button
        primaryStage.show();
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static boolean isAdmin() {
        return currentUsername != null && currentUsername.equals("admin");
    }

    public static boolean isGuest() {
        return currentUsername != null && currentUsername.equals("guest");
    }
}
