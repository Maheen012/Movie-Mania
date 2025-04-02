import javafx.scene.control.Button;
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
    public void testLoginWithValidCredentials() {
        // Lookup the UI components by fx:id
        TextField txtUsername = lookup("#txtUsername").query();
        PasswordField txtPassword = lookup("#txtPassword").query();
        Button btnLogin = lookup("#btnLogin").query();
        Label lblMessage = lookup("#lblMessage").query();


        // Simulate clicking the login button
        btnLogin.fire(); // Trigger the login button's action

        // Verify the message after login
        assertEquals("Admin login successful!", lblMessage.getText());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        // Lookup the UI components by fx:id
        TextField txtUsername = lookup("#txtUsername").query();
        PasswordField txtPassword = lookup("#txtPassword").query();
        Button btnLogin = lookup("#btnLogin").query();
        Label lblMessage = lookup("#lblMessage").query();



        // Simulate clicking the login button
        btnLogin.fire(); // Trigger the login button's action

        // Verify the message after login attempt
        assertEquals("Invalid credentials. Try again.", lblMessage.getText());
    }

    @Test
    public void testGuestLogin() {
        // Lookup the guest login button and label
        Button btnGuestLogin = lookup("#btnGuestLogin").query();
        Label lblMessage = lookup("#lblMessage").query();

        // Simulate clicking the guest login button
        btnGuestLogin.fire(); // Trigger the guest login button's action

        // Verify the guest login message
        assertEquals("Logged in as Guest", lblMessage.getText());
    }
}
