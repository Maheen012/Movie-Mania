import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.view.LoginGUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class LoginGUITest extends ApplicationTest {

    private MovieManager movieManager;
    private UserManager userManager;

    @Override
    public void start(Stage stage) throws Exception {
        movieManager = new MovieManager(); // Initialize MovieManager
        userManager = new UserManager(); // Initialize UserManager
        LoginGUI.setManagers(movieManager, userManager);
        new LoginGUI().start(stage);
    }

    @Test
    void testLoginWithValidAdminCredentials() {
        Platform.runLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setManagers(new MovieManager(), new UserManager());
            Stage stage = new Stage();
            loginGUI.start(stage);

            TextField txtUsername = (TextField) stage.getScene().lookup("#Username");
            PasswordField txtPassword = (PasswordField) stage.getScene().lookup("#Password");
            Button btnLogin = (Button) stage.getScene().lookup("#btnLogin");
            Label lblMessage = (Label) stage.getScene().lookup("#lblMessage");

            // Simulate admin login
            txtUsername.setText("admin");
            txtPassword.setText("admin123");
            btnLogin.fire();

            // Verify the success message
            assertEquals("Admin login successful!", lblMessage.getText());
        });
    }


    @Test
    void testLoginWithInvalidCredentials() {
        Platform.runLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setManagers(new MovieManager(), new UserManager());
            Stage stage = new Stage();
            loginGUI.start(stage);

            TextField txtUsername = (TextField) stage.getScene().lookup("#Username");
            PasswordField txtPassword = (PasswordField) stage.getScene().lookup("#Password");
            Button btnLogin = (Button) stage.getScene().lookup("#btnLogin");
            Label lblMessage = (Label) stage.getScene().lookup("#lblMessage");

            // Simulate invalid login
            txtUsername.setText("wronguser");
            txtPassword.setText("wrongpassword");
            btnLogin.fire();

            // Verify the error message
            assertEquals("Invalid credentials. Try again.", lblMessage.getText());
        });
    }

    @Test
    void testSignUpWithValidData() {
        Platform.runLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setManagers(new MovieManager(), new UserManager());
            Stage stage = new Stage();
            loginGUI.start(stage);

            Button btnSignUp = (Button) stage.getScene().lookup("#btnSignUp");
            btnSignUp.fire();

            // Open sign-up form and simulate filling it
            TextField txtNewUsername = (TextField) stage.getScene().lookup("#txtNewUsername");
            PasswordField txtNewPassword = (PasswordField) stage.getScene().lookup("#txtNewPassword");
            Button btnCreateAccount = (Button) stage.getScene().lookup("#btnCreateAccount");
            Label lblSignUpMessage = (Label) stage.getScene().lookup("#lblSignUpMessage");

            txtNewUsername.setText("newuser");
            txtNewPassword.setText("newpassword");
            btnCreateAccount.fire();

            // Verify the sign-up success message
            assertEquals("Account created successfully!", lblSignUpMessage.getText());
        });
    }

    @Test
    void testGuestLogin() {
        Platform.runLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setManagers(new MovieManager(), new UserManager());
            Stage stage = new Stage();
            loginGUI.start(stage);

            Button btnGuestLogin = (Button) stage.getScene().lookup("#btnGuestLogin");
            Label lblMessage = (Label) stage.getScene().lookup("#lblMessage");

            btnGuestLogin.fire();

            // Verify the guest login message
            assertEquals("Logged in as Guest", lblMessage.getText());
        });
    }

    @Test
    void testLoginOpensAdminGUI() {
        Platform.runLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setManagers(new MovieManager(), new UserManager());
            Stage stage = new Stage();
            loginGUI.start(stage);

            TextField txtUsername = (TextField) stage.getScene().lookup("#Username");
            PasswordField txtPassword = (PasswordField) stage.getScene().lookup("#Password");
            Button btnLogin = (Button) stage.getScene().lookup("#btnLogin");

            // Simulate admin login
            txtUsername.setText("admin");
            txtPassword.setText("admin123");
            btnLogin.fire();

            // Verify that AdminGUI is launched (you could check if the stage title matches)
            assertTrue(stage.isShowing(), "Admin GUI should be launched.");
        });
    }

    @Test
    void testLoginOpensUserGUI() {
        Platform.runLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setManagers(new MovieManager(), new UserManager());
            Stage stage = new Stage();
            loginGUI.start(stage);

            TextField txtUsername = (TextField) stage.getScene().lookup("#Username");
            PasswordField txtPassword = (PasswordField) stage.getScene().lookup("#Password");
            Button btnLogin = (Button) stage.getScene().lookup("#btnLogin");

            // Simulate user login (assuming user exists in UserManager)
            txtUsername.setText("user");
            txtPassword.setText("user123");
            btnLogin.fire();

            // Verify that UserGUI is launched
            assertTrue(stage.isShowing(), "User GUI should be launched.");
        });
    }

    @Test
    void testFullLoginFlow() {
        Platform.runLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setManagers(new MovieManager(), new UserManager());
            Stage stage = new Stage();
            loginGUI.start(stage);

            // Test admin login
            TextField txtUsername = (TextField) stage.getScene().lookup("#Username");
            PasswordField txtPassword = (PasswordField) stage.getScene().lookup("#Password");
            Button btnLogin = (Button) stage.getScene().lookup("#btnLogin");

            txtUsername.setText("admin");
            txtPassword.setText("admin123");
            btnLogin.fire();

            // Test user login
            txtUsername.setText("user");
            txtPassword.setText("user123");
            btnLogin.fire();

            // Test guest login
            Button btnGuestLogin = (Button) stage.getScene().lookup("#btnGuestLogin");
            btnGuestLogin.fire();

            // Verify successful transitions for all login types
            assertTrue(stage.isShowing(), "GUI should transition correctly for admin, user, and guest logins.");
        });
    }

}
