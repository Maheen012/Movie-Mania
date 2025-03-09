package org.example;

import org.example.controller.MovieManager;
import org.example.view.AdminGUI;
import org.example.view.LoginGUI;
import org.example.view.UserGUI;

/**
 * The Main class is the entry point of the Movie Mania application.
 */
public class Main {
    public static void main(String[] args) {
        // Start with the login screen
        LoginGUI login = new LoginGUI();
        boolean isAdmin = login.isAdmin(); // Get role from login

        // Initialize MovieManager
        MovieManager movieManager = new MovieManager();
        movieManager.readMovies(); // Load movies from CSV

        // Launch the appropriate GUI based on user role
        if (isAdmin) {
            new AdminGUI(movieManager); // Pass MovieManager to AdminGUI
        } else {
            new UserGUI(movieManager); // Pass MovieManager to UserGUI
        }
    }
}