import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;
import org.example.view.MovieViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class MovieViewerTest {

    private MovieViewer movieViewer;
    private MovieManager movieManager;
    private UserManager userManager;
    private Stage primaryStage;

    @Start
    private void start(Stage stage) {
        primaryStage = stage; // Store the stage for later use in tests
        movieManager = new MovieManager();
        userManager = new UserManager();
        movieViewer = new MovieViewer(movieManager, userManager);

        // Set a scene to avoid issues with JavaFX components
        primaryStage.setScene(new Scene(new Pane(), 800, 600));
        primaryStage.show();
    }

    @Test
    void testShowMovieTitlesScreen() {
        assertDoesNotThrow(() -> Platform.runLater(() -> movieViewer.showMovieTitlesScreen()),
                "Should not throw an exception when showing movie titles screen.");
    }

    @Test
    void testShowMovieDetailsScreen() {
        Movie movie = new Movie(1, "Inception", 2010, "Leonardo DiCaprio", 8.8, "Sci-Fi", "A mind-bending thriller.", "images/Inception.jpg");

        assertDoesNotThrow(() -> Platform.runLater(() -> movieViewer.showMovieDetailsScreen(movie)),
                "Should not throw an exception when showing movie details screen.");
    }

    @Test
    void testShowAlert() {
        assertDoesNotThrow(() -> Platform.runLater(() -> movieViewer.showAlert("Test Title", "Test Message")),
                "Should not throw an exception when showing an alert.");
    }
}
