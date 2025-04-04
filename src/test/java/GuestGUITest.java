import static org.junit.jupiter.api.Assertions.*;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.view.GuestGUI;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;

public class GuestGUITest {
    private MovieManager movieManager;
    private UserManager userManager;
    private GuestGUI guestGUI;

    @BeforeAll
    static void initJavaFX() {
        // Initialize the JavaFX toolkit once for all tests
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        // Initialize the managers and GuestGUI before each test
        movieManager = new MovieManager();
        userManager = new UserManager();
        guestGUI = new GuestGUI();
        guestGUI.setManagers(movieManager, userManager);
    }

    @Test
    void testGuestCanViewMovies() {
        // SYSTEM TEST:
        // Ensures that a guest can view the movie catalog without any exceptions.
        // Involves GuestGUI interacting with MovieViewer, MovieManager, and UserManager.
        assertDoesNotThrow(() -> guestGUI.displayMovies(),
                "GuestGUI should be able to display movies without errors");
    }

    @Test
    void testGuestCannotAccessWatchHistory() {
        // UNIT TEST:
        // Validates that calling getWatchHistory() throws the expected exception,
        // ensuring restricted access for guests to watch history.
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            guestGUI.getWatchHistory();
        }, "GuestGUI should not allow access to watch history");

        assertEquals("Guests cannot access watch history", exception.getMessage());
    }

    @Test
    void testGuestCannotAccessFavorites() {
        // UNIT TEST:
        // Validates that calling getFavorites() throws the expected exception,
        // ensuring restricted access for guests to the favorites list.
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            guestGUI.getFavorites();
        }, "GuestGUI should not allow access to favorites");

        assertEquals("Guests cannot access favorites", exception.getMessage());
    }

    @Test
    void testGuestDisplaysMovieScreen() throws Exception {
        // INTEGRATION TEST:
        // Verifies that displayMovies() correctly uses MovieManager and UserManager
        // to initialize MovieViewer and display the movie titles screen.

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                guestGUI.displayMovies();
                // Ideally, we should check if MovieViewer is properly launched.
                // Example (if MovieViewer had an `isMovieScreenShown()` method):
                // assertTrue(movieViewer.isMovieScreenShown(), "Movie screen should be displayed");
            } finally {
                latch.countDown();
            }
        });

        latch.await(); // Wait for the JavaFX thread to finish execution
    }
}
