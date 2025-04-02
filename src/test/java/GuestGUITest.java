import static org.junit.jupiter.api.Assertions.*;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.view.GuestGUI;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        // Initialize the managers and GuestGUI
        movieManager = new MovieManager();
        userManager = new UserManager();
        guestGUI = new GuestGUI();
        guestGUI.setManagers(movieManager, userManager);
    }

    @Test
    void testGuestCanViewMovies() {
        // Guest should be able to view the movie catalog without exceptions
        assertDoesNotThrow(() -> guestGUI.displayMovies(),
                "GuestGUI should be able to display movies without errors");
    }

    @Test
    void testGuestCannotAccessWatchHistory() {
        // Guest should not have access to watch history
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            guestGUI.getWatchHistory();
        }, "GuestGUI should not allow access to watch history");

        assertEquals("Guests cannot access watch history", exception.getMessage());
    }

    @Test
    void testGuestCannotAccessFavorites() {
        // Guest should not have access to favorites list
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            guestGUI.getFavorites();
        }, "GuestGUI should not allow access to favorites");

        assertEquals("Guests cannot access favorites", exception.getMessage());
    }
}
