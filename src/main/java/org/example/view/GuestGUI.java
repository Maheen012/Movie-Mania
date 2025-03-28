package org.example.view;

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
        // Use the MovieViewer to display the movie catalog
        MovieViewer movieViewer = new MovieViewer(movieManager, userManager);
        movieViewer.showMovieTitlesScreen();
    }

    /**
     * Guests do not have access to watch history.
     */
    public void getWatchHistory() {
        System.out.println("Guests do not have access to watch history.");
    }

    /**
     * Guests do not have access to favorites.
     */
    public void getFavorites() {
        System.out.println("Guests do not have access to favorites.");
    }
}