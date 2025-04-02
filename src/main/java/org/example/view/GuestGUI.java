package org.example.view;

import javafx.application.Platform;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;

public class GuestGUI {
    private MovieManager movieManager;
    private UserManager userManager;

    public void setManagers(MovieManager movieManager, UserManager userManager) {
        this.movieManager = movieManager;
        this.userManager = userManager;
    }

    /**
     * Displays the movie catalog for guests.
     */
    public void displayMovies() {
        Platform.runLater(() -> {
            MovieViewer movieViewer = new MovieViewer(movieManager, userManager);
            movieViewer.showMovieTitlesScreen();
        });
    }

    /**
     * Guests do not have access to watch history.
     *
     * @throws UnsupportedOperationException if a guest attempts to view watch history.
     */
    public void getWatchHistory() {
        throw new UnsupportedOperationException("Guests cannot access watch history");
    }

    /**
     * Guests do not have access to favorites.
     *
     * @throws UnsupportedOperationException if a guest attempts to access favorites.
     */
    public void getFavorites() {
        throw new UnsupportedOperationException("Guests cannot access favorites");
    }
}
