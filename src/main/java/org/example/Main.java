package org.example;

import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.view.LoginGUI;

import javafx.application.Application;
import org.example.view.MovieViewer;

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

        // Start the JavaFX application
        LoginGUI.setManagers(movieManager, userManager);
        Application.launch(LoginGUI.class, args);


        MovieViewer movieViewer = new MovieViewer(movieManager, userManager);
        movieViewer.showMovieTitlesScreen();
    }
}
