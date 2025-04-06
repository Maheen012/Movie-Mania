import javafx.stage.Window;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.view.AdminGUI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminGUITest {

    private MovieManager movieManager;
    private UserManager userManager;
    private AdminGUI adminGUI;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit once for all tests
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Initialize the managers and AdminGUI
        movieManager = new MovieManager();
        userManager = new UserManager();
        adminGUI = new AdminGUI();
    }

    /**
     * Unit Test: Verifies the initialization of the AdminGUI with the title label and buttons.
     * This test checks that the basic UI components are correctly initialized.
     */
    @Test
    void testAdminGUIInitialization() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            adminGUI.start(stage);

            // Verify that the title label is present
            assertNotNull(stage.getScene().lookup(".label"));
            assertEquals("Movie Mania - Admin Panel",
                    ((javafx.scene.control.Label) stage.getScene().lookup(".label")).getText());

            // Verify the presence of the buttons
            Button[] buttons = stage.getScene().getRoot().getChildrenUnmodifiable()
                    .stream()
                    .filter(node -> node instanceof Button)
                    .toArray(Button[]::new);

            assertEquals(3, buttons.length);
            assertEquals("Add Movie", buttons[0].getText());
            assertEquals("View Movies", buttons[1].getText());
            assertEquals("Logout", buttons[2].getText());
        });
    }

    /**
     * Integration Test: Verifies the functionality of the "Add Movie" button.
     * This test checks if the "Add Movie" button opens the movie addition form.
     */
    @Test
    void testAddMovieButton() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            adminGUI.start(stage);

            Button btnAddMovie = (Button) stage.getScene().lookup("#Add Movie");
            assertNotNull(btnAddMovie);
            assertEquals("Add Movie", btnAddMovie.getText());

            // Simulate clicking the button
            btnAddMovie.fire();

            // Verify the add movie form is shown by checking for title field
            Stage addMovieStage = (Stage) Stage.getWindows().stream()
                    .filter(Window::isShowing)
                    .filter(w -> w != stage)
                    .findFirst()
                    .orElse(null);

            assertNotNull(addMovieStage);
            assertNotNull(addMovieStage.getScene().lookup("#Title"));
        });
    }

    /**
     * System Test: Simulates clicking the "Logout" button and ensures that the application closes.
     */
    @Test
    void testLogoutButton() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            adminGUI.start(stage);

            Button btnLogout = (Button) stage.getScene().lookup("#Logout");
            assertNotNull(btnLogout);
            assertEquals("Logout", btnLogout.getText());

            // Simulate clicking the "Logout" button
            btnLogout.fire();

            // Verify stage is closed
            assertFalse(stage.isShowing());
        });
    }

    /**
     * Integration Test: Verifies the movie addition form contains all required fields.
     */
    @Test
    void testAddMovieFormFields() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            adminGUI.start(stage);

            // Open add movie form
            Button btnAddMovie = (Button) stage.getScene().lookup("#Add Movie");
            btnAddMovie.fire();

            Stage addMovieStage = (Stage) Stage.getWindows().stream()
                    .filter(Window::isShowing)
                    .filter(w -> w != stage)
                    .findFirst()
                    .orElse(null);

            // Verify all form fields are present
            assertNotNull(addMovieStage.getScene().lookup("#Title"));
            assertNotNull(addMovieStage.getScene().lookup("#Year"));
            assertNotNull(addMovieStage.getScene().lookup("#Main Cast"));
            assertNotNull(addMovieStage.getScene().lookup("#Rating"));
            assertNotNull(addMovieStage.getScene().lookup("#Genre"));
            assertNotNull(addMovieStage.getScene().lookup("#Description"));
            assertNotNull(addMovieStage.getScene().lookup("#Choose Image"));
        });
    }

    /**
     * Integration Test: Verifies that a new movie can be added through the form.
     */
    @Test
    void testAddMovieFunctionality() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            adminGUI.start(stage);
            int initialMovieCount = movieManager.getMovies().size();

            // Open add movie form
            Button btnAddMovie = (Button) stage.getScene().lookup("#Add Movie");
            btnAddMovie.fire();

            Stage addMovieStage = (Stage) Stage.getWindows().stream()
                    .filter(Window::isShowing)
                    .filter(w -> w != stage)
                    .findFirst()
                    .orElse(null);

            // Fill in form data
            TextField titleField = (TextField) addMovieStage.getScene().lookup("#Title");
            titleField.setText("Test Movie");

            TextField yearField = (TextField) addMovieStage.getScene().lookup("#Year");
            yearField.setText("2023");

            // Click Add button
            Button btnAdd = (Button) addMovieStage.getScene().lookup("#Add Movie");
            btnAdd.fire();

            // Verify movie was added
            assertEquals(initialMovieCount + 1, movieManager.getMovies().size());
            Movie addedMovie = movieManager.getMovies().get(movieManager.getMovies().size() - 1);
            assertEquals("Test Movie", addedMovie.getTitle());
            assertEquals(2023, addedMovie.getYear());

            // Clean up
            movieManager.getMovies().remove(addedMovie);
            movieManager.saveMovies();
        });
    }

    /**
     * Integration Test: Verifies invalid input handling in the add movie form.
     */
    @Test
    void testInvalidInputHandling() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            adminGUI.start(stage);

            // Open add movie form
            Button btnAddMovie = (Button) stage.getScene().lookup("#Add Movie");
            btnAddMovie.fire();

            Stage addMovieStage = (Stage) Stage.getWindows().stream()
                    .filter(Window::isShowing)
                    .filter(w -> w != stage)
                    .findFirst()
                    .orElse(null);

            // Enter invalid data
            TextField yearField = (TextField) addMovieStage.getScene().lookup("#Year");
            yearField.setText("invalid");

            TextField ratingField = (TextField) addMovieStage.getScene().lookup("#Rating");
            ratingField.setText("invalid");

            // Click Add button
            Button btnAdd = (Button) addMovieStage.getScene().lookup("#Add Movie");
            btnAdd.fire();

            // Verify error alert is shown (can't directly verify alert content without TestFX)
            // Just verify the movie wasn't added
            assertEquals(0, movieManager.getMovies().stream()
                    .filter(m -> m.getTitle().equals("Test Movie"))
                    .count());
        });
    }

    /**
     * Integration Test: Verifies the "View Movies" button opens the movie viewer.
     */
    @Test
    void testViewMoviesButton() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            adminGUI.start(stage);

            Button btnViewMovies = (Button) stage.getScene().lookup("#View Movies");
            assertNotNull(btnViewMovies);
            assertEquals("View Movies", btnViewMovies.getText());

            // Simulate clicking the button
            btnViewMovies.fire();

            // Verify movie viewer is shown by checking for back button
            Stage movieViewerStage = (Stage) Stage.getWindows().stream()
                    .filter(Window::isShowing)
                    .filter(w -> w != stage)
                    .findFirst()
                    .orElse(null);

            assertNotNull(movieViewerStage);
            assertNotNull(movieViewerStage.getScene().lookup("#Back to Catalogue"));
        });
    }
}
