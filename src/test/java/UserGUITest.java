import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.view.UserGUI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

class UserGUITest {

    private MovieManager movieManager;
    private UserManager userManager;
    private UserGUI userGUI;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit once for all tests
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Initialize the managers and UserGUI
        movieManager = new MovieManager();
        userManager = new UserManager();
        userGUI = new UserGUI();
        userGUI.setManagers(movieManager, userManager); // Set managers
    }

    @Test
    void testUserGUI() {
        // Simulate user login
        Platform.runLater(() -> {
            Stage stage = new Stage();
            userGUI.start(stage);

            // Verify that the welcome label is present
            assertNotNull(stage.getScene().lookup(".label"));
            assertEquals("Welcome to Movie Mania!", ((javafx.scene.control.Label) stage.getScene().lookup(".label")).getText());

            // Verify the presence of the buttons
            assertNotNull(stage.getScene().lookup("Button"));
        });
    }

    @Test
    void testViewMoviesButton() {
        // Simulate clicking the "View Movies" button
        Platform.runLater(() -> {
            Stage stage = new Stage();
            userGUI.start(stage);

            // Simulate button click
            javafx.scene.control.Button btnViewMovies = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            assertNotNull(btnViewMovies);
            assertEquals("View Movies", btnViewMovies.getText());

            // Simulate clicking the button (in this test, it will just open a new window)
            btnViewMovies.fire();
        });
    }

    @Test
    void testLogoutButton() {
        // Simulate clicking the "Logout" button
        Platform.runLater(() -> {
            Stage stage = new Stage();
            userGUI.start(stage);

            javafx.scene.control.Button btnLogout = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            assertNotNull(btnLogout);
            assertEquals("Logout", btnLogout.getText());

            // Simulate clicking the "Logout" button
            btnLogout.fire();

            // After logout, the stage should close
            assertTrue(stage.isIconified());
        });
    }
}
