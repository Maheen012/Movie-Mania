import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.view.UserGUI;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    /**
     * Unit Test: Verifies the initialization of the UserGUI with the welcome label and buttons.
     * This test checks that the basic UI components are correctly initialized.
     */
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

    /**
     * Integration Test: Verifies the functionality of the "View Movies" button.
     * This test checks if the "View Movies" button is displayed and correctly triggers the button's action.
     */
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

    /**
     * System Test: Simulates clicking the "Logout" button and ensures that the application closes after logout.
     * This tests the end-to-end behavior, including UI interaction and application state changes.
     */
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

            // After logout, the application should close completely
            assertFalse(stage.isShowing()); // Stage should be closed
        });
    }

    /**
     * Unit Test: Verifies that the "View Favorites" button is displayed and clickable.
     * This ensures the Favorites button functionality works.
     */
    @Test
    void testViewFavoritesButton() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            userGUI.start(stage);

            javafx.scene.control.Button btnViewFavorites = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            assertNotNull(btnViewFavorites);
            assertEquals("View Favorites", btnViewFavorites.getText());

            // Simulate clicking the "View Favorites" button
            btnViewFavorites.fire();
            // Verify if the favorites screen opens (checking the presence of favorite list view)
            assertNotNull(stage.getScene().lookup(".list-view"));
        });
    }

    /**
     * Unit Test: Verifies that the "View Watch History" button is displayed and clickable.
     * This ensures the Watch History button functionality works.
     */
    @Test
    void testViewWatchHistoryButton() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            userGUI.start(stage);

            javafx.scene.control.Button btnViewWatchHistory = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            assertNotNull(btnViewWatchHistory);
            assertEquals("Watch History", btnViewWatchHistory.getText());

            // Simulate clicking the "View Watch History" button
            btnViewWatchHistory.fire();
            // Verify if the watch history screen opens (checking the presence of watch history list view)
            assertNotNull(stage.getScene().lookup(".list-view"));
        });
    }

    /**
     * Integration Test: Simulate the user has no favorite movies and verifies that the list is empty.
     * This tests that the "Favorites" screen displays an empty list when there are no favorites.
     */
    @Test
    void testEmptyFavoritesList() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            userGUI.start(stage);

            userManager.removeFromFavorites("SomeMovie");  // Ensure no favorites exist

            // Simulate clicking the "View Favorites" button
            javafx.scene.control.Button btnViewFavorites = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            btnViewFavorites.fire();

            // Verify that the favorites list is empty
            ListView<String> favoritesList = (ListView<String>) stage.getScene().lookup(".list-view");
            assertNotNull(favoritesList);
            assertTrue(favoritesList.getItems().isEmpty());
        });
    }

    /**
     * Integration Test: Simulates removing a movie from the "Favorites" list and verifies the removal.
     * This tests the interaction between UI and data for removing items from the favorites list.
     */
    @Test
    void testRemoveFromFavorites() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            userGUI.start(stage);

            // Simulate adding a movie to the favorites list
            userManager.addToFavorites("Favorite Movie");

            // Simulate clicking the "View Favorites" button
            javafx.scene.control.Button btnViewFavorites = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            btnViewFavorites.fire();

            ListView<String> favoritesList = (ListView<String>) stage.getScene().lookup(".list-view");
            assertTrue(favoritesList.getItems().contains("Favorite Movie"));

            // Simulate removing the movie from the favorites list
            javafx.scene.control.Button btnRemove = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            btnRemove.fire();

            // Verify that the movie is removed from the list
            assertFalse(favoritesList.getItems().contains("Favorite Movie"));
        });
    }

    /**
     * Integration Test: Simulates removing a movie from the "Watch History" list and verifies the removal.
     * This tests the interaction between UI and data for removing items from the watch history list.
     */
    @Test
    void testRemoveFromWatchHistory() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            userGUI.start(stage);

            // Simulate adding a movie to the watch history list
            userManager.addToWatchHistory("Watched Movie");

            // Simulate clicking the "View Watch History" button
            javafx.scene.control.Button btnViewWatchHistory = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            btnViewWatchHistory.fire();

            ListView<String> watchHistoryList = (ListView<String>) stage.getScene().lookup(".list-view");
            assertTrue(watchHistoryList.getItems().contains("Watched Movie"));

            // Simulate removing the movie from the watch history list
            javafx.scene.control.Button btnRemove = (javafx.scene.control.Button) stage.getScene().lookup("Button");
            btnRemove.fire();

            // Verify that the movie is removed from the list
            assertFalse(watchHistoryList.getItems().contains("Watched Movie"));
        });
    }
}
