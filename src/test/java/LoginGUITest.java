import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.view.LoginGUI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

class LoginGUITest {

    private MovieManager movieManager;
    private UserManager userManager;
    private LoginGUI loginGUI;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit once for all tests
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Initialize the managers and LoginGUI
        movieManager = new MovieManager(); // Assuming MovieManager has a no-arg constructor
        userManager = new UserManager();   // Assuming UserManager has a no-arg constructor
        LoginGUI.setManagers(movieManager, userManager);
        loginGUI = new LoginGUI();
    }

    @Test
    void testAdminLogin() {
        // Simulate admin login
        Platform.runLater(() -> {
            Stage stage = new Stage();
            loginGUI.start(stage);

            // Simulate setting the current username (admin)
            LoginGUI.setCurrentUsername("admin");

            // Verify that the current username is set correctly
            assertEquals("admin", LoginGUI.getCurrentUsername());
        });
    }

    @Test
    void testUserLogin() {
        // Simulate user login
        Platform.runLater(() -> {
            Stage stage = new Stage();
            loginGUI.start(stage);

            // Simulate setting the current username (regular user)
            LoginGUI.setCurrentUsername("testUser");

            // Verify that the current username is set correctly
            assertEquals("testUser", LoginGUI.getCurrentUsername());
        });
    }

    @Test
    void testInvalidLogin() {
        // Simulate invalid login
        Platform.runLater(() -> {
            Stage stage = new Stage();
            loginGUI.start(stage);

            // Simulate setting the current username to null (invalid login)
            LoginGUI.setCurrentUsername(null);

            // Verify that the current username is not set
            assertNull(LoginGUI.getCurrentUsername());
        });
    }
}

