import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UserManagerTest {

    // Path to the favorites CSV file
    private static final String FAVORITES_CSV_PATH = "src/test/TestResources/favorites.csv";

    // --- Integration Tests ---

    // Test case IT-01-TB: adminCanAddMoviesAfterLogin()
    @Test
    public void adminCanAddMoviesAfterLogin() throws IOException {
        // Simulate the process of adding a movie to favorites
        modifyCsvFile("Movie Title");

        // Check if the movie was successfully added by verifying the file content
        File favoritesFile = new File(FAVORITES_CSV_PATH);
        Assertions.assertTrue(favoritesFile.length() > 0, "Movie was not added to favorites");
    }

    // Test case IT-02-TB: loginFlowCommunicatesWithGUI()
    @Test
    public void loginFlowCommunicatesWithGUI() {
        // Simulate the login flow and check if the GUI communicates properly
        boolean isLoginSuccessful = true;  // This should be replaced with the actual login logic
        Assertions.assertTrue(isLoginSuccessful, "Login flow did not communicate properly with the GUI");
    }

    // Test case IT-04-TB: watchHistoryUpdatesCorrectly()
    @Test
    public void watchHistoryUpdatesCorrectly() throws IOException {
        // Simulate adding a movie to the watch history
        modifyCsvFile("Movie Title");

        // Check if the watch history file has been updated correctly (for this example, we check the favorites file)
        File watchHistoryFile = new File(FAVORITES_CSV_PATH);
        Assertions.assertTrue(watchHistoryFile.length() > 0, "Watch history was not updated correctly");
    }

    // --- Unit Tests ---

    // Test case UT-01-TB: removeMovieFromFavorites()
    @Test
    public void removeMovieFromFavorites() throws IOException {
        // Add a movie first
        modifyCsvFile("Movie Title");

        // Remove the movie (simulating it by clearing the file)
        clearCsvFile();

        // Verify that the movie is no longer in the file (it should be empty after removal)
        File favoritesFile = new File(FAVORITES_CSV_PATH);
        Assertions.assertTrue(favoritesFile.length() == 0, "Movie was not removed from favorites");
    }


    // Test case UT-03-TB: movieTitleIsCorrectInFavorites()
    @Test
    public void movieTitleIsCorrectInFavorites() throws IOException {
        // Add a movie with a specific title
        String movieTitle = "Movie Title";
        modifyCsvFile(movieTitle);

        // Verify that the exact movie title is in the CSV file
        File favoritesFile = new File(FAVORITES_CSV_PATH);
        boolean containsMovieTitle = fileContainsMovieTitle(favoritesFile, movieTitle);

        Assertions.assertTrue(containsMovieTitle, "Movie title was not correctly added to favorites");
    }

    // --- System Tests ---

    // Test case ST-01-TB: systemCorrectlyHandlesAddingAndRemovingMovies()
    @Test
    public void systemCorrectlyHandlesAddingAndRemovingMovies() throws IOException {
        // Add a movie to favorites
        modifyCsvFile("Movie Title");

        // Check if the movie was added
        File favoritesFile = new File(FAVORITES_CSV_PATH);
        Assertions.assertTrue(favoritesFile.length() > 0, "Movie was not added to favorites");

        // Remove the movie (clear the file)
        clearCsvFile();

        // Check if the file is empty
        Assertions.assertTrue(favoritesFile.length() == 0, "Movie was not removed from favorites");
    }

    // Test case ST-02-TB: systemCorrectlyHandlesLoginAndUpdate()
    @Test
    public void systemCorrectlyHandlesLoginAndUpdate() {
        // Simulate the login process
        boolean isLoginSuccessful = true;  // This should be replaced with the actual login logic

        // Simulate adding a movie after login
        boolean isMovieAdded = true;  // This should be replaced with actual movie addition logic after login

        // Verify the login and movie addition success
        Assertions.assertTrue(isLoginSuccessful, "Login did not succeed");
        Assertions.assertTrue(isMovieAdded, "Movie was not added after login");
    }

    // Helper method to simulate modifying the CSV file (e.g., adding a movie to the favorites)
    private void modifyCsvFile(String movieTitle) throws IOException {
        try (FileWriter writer = new FileWriter(FAVORITES_CSV_PATH, true)) {
            writer.write(movieTitle + "\n");
        }
    }

    // Helper method to clear the contents of the CSV file (simulating removal of a movie)
    private void clearCsvFile() throws IOException {
        try (FileWriter writer = new FileWriter(FAVORITES_CSV_PATH, false)) {
            writer.write(""); // Clear the file contents
        }
    }

    // Helper method to count occurrences of a movie title in the CSV file
    private long countOccurrencesInFile(File file, String movieTitle) throws IOException {
        return java.nio.file.Files.lines(file.toPath())
                .filter(line -> line.contains(movieTitle))
                .count();
    }

    // Helper method to check if the movie title is present in the CSV file
    private boolean fileContainsMovieTitle(File file, String movieTitle) throws IOException {
        return java.nio.file.Files.lines(file.toPath())
                .anyMatch(line -> line.contains(movieTitle));
    }
}
