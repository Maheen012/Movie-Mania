import org.example.controller.MovieManager;
import org.example.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieManagerTest {

    private MovieManager movieManager;

    @BeforeEach
    void setUp() {
        movieManager = new MovieManager();
    }

    @Test
    void testInitialMovieListIsEmpty() {
        assertTrue(movieManager.getMovies().isEmpty(), "Movie list should be initially empty.");
    }

    @Test
    void testAddMovie() {
        Movie movie = new Movie(1, "Inception", 2010, "Leonardo DiCaprio", 8.8, "Sci-Fi", "A mind-bending thriller.", "images/Inception.jpg\n");
        movieManager.getMovies().add(movie);
        assertEquals(1, movieManager.getMovies().size(), "Movie list should contain one movie.");
    }

    @Test
    void testGetMovieById_Found() {
        Movie movie = new Movie(1, "Inception", 2010, "Leonardo DiCaprio", 8.8, "Sci-Fi", "A mind-bending thriller.", "images/Inception.jpg\n" );
        movieManager.getMovies().add(movie);
        assertEquals(movie, movieManager.getMovieById(1), "Should return the correct movie.");
    }

    @Test
    void testGetMovieById_NotFound() {
        assertNull(movieManager.getMovieById(999), "Should return null for a non-existent movie.");
    }

    @Test
    void testGetMovieFilePath_NotNull() {
        assertNotNull(movieManager.getMovieFilePath(), "Movie file path should not be null.");
    }

}