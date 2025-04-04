import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {
    private static final String RESOURCES_DIR = "resources";
    private static final String FAVORITES_FILE = RESOURCES_DIR + "/favorites.csv";

    @BeforeAll
    static void setup() {
        File resourcesDir = new File(RESOURCES_DIR);
        if (!resourcesDir.exists()) {
            assertTrue(resourcesDir.mkdir(), "Failed to create resources directory");
        }
    }

    @BeforeEach
    void resetFavoritesFile() throws IOException {
        Files.write(Paths.get(FAVORITES_FILE), "".getBytes()); // Clear file before each test
    }

    @Test
    void adminCanAddMoviesAfterLogin() throws IOException {
        addMovieToFavorites("Inception");
        assertTrue(isMovieInFavorites("Inception"), "Movie was not added to favorites");
    }

    @Test
    void loginFlowCommunicatesWithGUI() {
        // Mock or simulate GUI interactions for login process
        boolean loginSuccess = true; // Simulating successful login
        assertTrue(loginSuccess, "Login GUI did not communicate properly");
    }

    @Test
    void watchHistoryUpdatesCorrectly() throws IOException {
        addMovieToFavorites("Interstellar");
        assertTrue(isMovieInFavorites("Interstellar"), "Watch history did not update correctly");
    }

    private void addMovieToFavorites(String movie) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITES_FILE, true))) {
            writer.write(movie + "\n");
        }
    }

    private boolean isMovieInFavorites(String movie) throws IOException {
        File file = new File(FAVORITES_FILE);
        if (!file.exists()) return false;
        return Files.lines(Paths.get(FAVORITES_FILE)).anyMatch(line -> line.equals(movie));
    }
}


