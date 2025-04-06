import org.example.controller.MovieManager;
import org.example.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieManagerTest {

    // Unit Tests
    @Test
    void unitTest_getMoviesInitiallyEmpty() {
        MovieManager manager = new MovieManager();
        assertTrue(manager.getMovies().isEmpty());
    }

    @Test
    void unitTest_getMovieByIdNotFound() {
        MovieManager manager = new MovieManager();
        assertNull(manager.getMovieById(999));
    }

    @Test
    void unitTest_addAndRetrieveMovie() {
        MovieManager manager = new MovieManager();
        Movie testMovie = new Movie(1, "Test", 2023, "Cast", 8.0, "Genre", "Desc", "img.jpg");
        manager.getMovies().add(testMovie);

        assertEquals(testMovie, manager.getMovieById(1));
    }

    // Integration Tests
    @TempDir
    Path tempDir;
    private File testCsvFile;

    @BeforeEach
    void setupIntegrationTest() throws IOException {
        testCsvFile = tempDir.resolve("movies.csv").toFile();
        String header = "Movie ID,Title,Year,Main Cast,Rating,Genre,Description,Cover Image Path\n";
        String data = "1,Inception,2010,Leonardo DiCaprio,8.8,Sci-Fi,A thief who steals corporate secrets,inception.jpg\n";
        Files.write(testCsvFile.toPath(), (header + data).getBytes());
    }

    @Test
    void integrationTest_readAndSaveMovies() throws IOException {
        MovieManager manager = new MovieManager() {
            @Override
            public File getMovieFilePath() {
                return testCsvFile;
            }
        };

        manager.readMovies();
        assertEquals(1, manager.getMovies().size());

        manager.getMovies().add(new Movie(2, "New Movie", 2023, "Cast", 7.5, "Genre", "Desc", "img.jpg"));
        manager.saveMovies();

        // Verify by reading again
        MovieManager newManager = new MovieManager() {
            @Override
            public File getMovieFilePath() {
                return testCsvFile;
            }
        };
        newManager.readMovies();
        assertEquals(2, newManager.getMovies().size());
    }

    // System Tests
    @Test
    void systemTest_fullWorkflow() {
        // Setup - use actual file from resources
        MovieManager manager = new MovieManager();
        manager.readMovies();
        int initialCount = manager.getMovies().size();

        // Create and save
        Movie newMovie = new Movie(999, "System Test", 2023, "Test Cast", 7.0, "Test", "Test", "test.jpg");
        manager.getMovies().add(newMovie);
        manager.saveMovies();

        // Verify
        MovieManager verifyManager = new MovieManager();
        verifyManager.readMovies();
        assertEquals(initialCount + 1, verifyManager.getMovies().size());
        assertEquals("System Test", verifyManager.getMovieById(999).getTitle());

        // Cleanup
        assertTrue(verifyManager.deleteMovieById(999));
    }

    @Test
    void systemTest_fileOperations() {
        MovieManager manager = new MovieManager();
        File movieFile = manager.getMovieFilePath();

        assertNotNull(movieFile);
        assertTrue(movieFile.exists());
        assertTrue(movieFile.canRead());
        assertTrue(movieFile.canWrite());
    }
}
