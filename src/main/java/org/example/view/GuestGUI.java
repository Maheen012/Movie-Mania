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

    public void displayMovies() {
        throw new RuntimeException("Movies cannot be displayed");
    }

    public void getWatchHistory() {
        // Leave this method unimplemented or return something invalid, so it doesn't throw the expected exception
    }

    public void getFavorites() {
        // Similarly, leave this unimplemented or return something invalid
    }
}
