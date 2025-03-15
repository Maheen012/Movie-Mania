package org.example;

import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.view.AdminGUI;
import org.example.view.LoginGUI;
import org.example.view.UserGUI;

/**
 * The Main class is the entry point of the Movie Mania application.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize MovieManager and UserManager
        MovieManager movieManager = new MovieManager();
        UserManager userManager = new UserManager();

        // Load movies from CSV
        movieManager.readMovies();

        // Start with the login screen
        LoginGUI login = new LoginGUI();
        login.waitForLogin(); // Wait here until the user logs in

        // Launch the appropriate GUI based on user role
        if (login.isAdmin()) {
            new AdminGUI(movieManager, userManager);
        } else {
            new UserGUI(movieManager, userManager);
        }
    }
}

