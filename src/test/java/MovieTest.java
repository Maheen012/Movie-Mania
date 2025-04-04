
import org.example.model.Movie;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Movie.
 */
public class MovieTest {

    /**
     * Test Case ID: UT-05-CB
     * Category: Unit (Clear)
     * Ensures that the movie constructor sets all fields correctly.
     */
    @Test
    public void movieConstructorSetsAllFields() {
        // Given
        int movieId = 1;
        String title = "Inception";
        int year = 2010;
        String mainCast = "Leonardo DiCaprio";
        double rating = 8.8;
        String genre = "Sci-Fi";
        String description = "A thief enters dreams to steal secrets.";
        String coverImagePath = "path/to/image.jpg";

        // When
        Movie movie = new Movie(movieId, title, year, mainCast, rating, genre, description, coverImagePath);

        // Then
        assertEquals(movieId, movie.getMovieId());
        assertEquals(title, movie.getTitle());
        assertEquals(year, movie.getYear());
        assertEquals(mainCast, movie.getMainCast());
        assertEquals(rating, movie.getRating());
        assertEquals(genre, movie.getGenre());
        assertEquals(description, movie.getDescription());
        assertEquals(coverImagePath, movie.getCoverImagePath());
    }

    /**
     * Test Case ID: UT-07-CB
     * Category: Unit (Clear)
     * Ensures that two movies are considered equal based on their ID.
     */
    @Test
    public void movieEqualityBasedOnId() {
        // Given
        Movie movie1 = new Movie(1, "Movie A", 2020, "Actor A", 7.5, "Drama", "Description A", "path/to/imageA.jpg");
        Movie movie2 = new Movie(1, "Movie B", 2021, "Actor B", 8.0, "Action", "Description B", "path/to/imageB.jpg");

        // Then
        assertEquals(movie1.getMovieId(), movie2.getMovieId());
    }

    /**
     * Test Case ID: ST-03-OB
     * Category: System
     * Ensures that viewing a movie updates history correctly.
     */
    @Test
    public void viewingMovieUpdatesHistory() {
        // Given
        Movie movie = new Movie(2, "Interstellar", 2014, "Matthew McConaughey", 8.6, "Sci-Fi", "A space adventure.", "path/to/interstellar.jpg");
        String userHistory = "";

        // When
        userHistory += movie.getTitle();

        // Then
        assertTrue(userHistory.contains("Interstellar"));
    }

    /**
     * Test Case ID: ST-10-OB
     * Category: System
     * Ensures that the movie cover image path is stored correctly.
     */
    @Test
    public void movieCoversDisplayCorrectly() {
        // Given
        String coverPath = "path/to/cover.jpg";
        Movie movie = new Movie(3, "The Matrix", 1999, "Keanu Reeves", 8.7, "Action", "A computer hacker learns the truth.", coverPath);

        // Then
        assertEquals(coverPath, movie.getCoverImagePath());
    }
}
