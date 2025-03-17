package org.example.view;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import org.example.controller.MovieManager;
import org.example.controller.UserManager;

/**
 * UserGUI represents the user dashboard for Movie Mania.
 */
public class UserGUI extends Application {
    private MovieManager movieManager;
    private UserManager userManager;


    public UserGUI() {
        // Will be initialized using setManagers()
    }

    public void setManagers(MovieManager mm, UserManager um) {
        this.movieManager = mm;
        this.userManager = um;
    }

    @Override
    public void start(Stage primaryStage) {
        if (movieManager == null || userManager == null) {
            System.out.println("Error: MovieManager and UserManager are not set!");
            return;
        }

        primaryStage.setTitle("Movie Mania - User Catalogue");

        Label lblWelcome = new Label("Welcome to Movie Mania!");
        Button btnViewMovies = new Button("View Movies");
        Button btnViewFavorites = new Button("View Favorites");
        Button btnWatchHistory = new Button("Watch History");
        Button btnLogout = new Button("Logout");

        // Button Actions
        btnViewMovies.setOnAction(e -> new MovieViewer(movieManager, userManager).showMovieTitlesScreen());
        btnViewFavorites.setOnAction(e -> showFavoritesScreen(primaryStage));
        btnWatchHistory.setOnAction(e -> showWatchHistoryScreen(primaryStage));
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            System.exit(0); // Close the app
        });

        // Layout
        VBox layout = new VBox(10, lblWelcome, btnViewMovies, btnViewFavorites, btnWatchHistory, btnLogout);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.show();
    }

    private void showFavoritesScreen(Stage mainStage) {
        Stage favoritesStage = new Stage();
        favoritesStage.setTitle("Favorites");

        ListView<String> favoritesList = new ListView<>();
        favoritesList.getItems().addAll(userManager.getFavoriteMovies(LoginGUI.getCurrentUsername()));

        Button btnBack = new Button("Back");
        btnBack.setOnAction(e -> favoritesStage.close());

        VBox layout = new VBox(10, new Label("Your Favorite Movies"), favoritesList, btnBack);
        layout.setStyle("-fx-padding: 20;");
        favoritesStage.setScene(new Scene(layout, 400, 300));
        favoritesStage.show();
    }

    private void showWatchHistoryScreen(Stage mainStage) {
        Stage watchHistoryStage = new Stage();
        watchHistoryStage.setTitle("Watch History");

        ListView<String> watchHistoryList = new ListView<>();
        watchHistoryList.getItems().addAll(userManager.getWatchHistory(LoginGUI.getCurrentUsername()));

        Button btnBack = new Button("Back");
        btnBack.setOnAction(e -> watchHistoryStage.close());

        VBox layout = new VBox(10, new Label("Your Watch History"), watchHistoryList, btnBack);
        layout.setStyle("-fx-padding: 20;");
        watchHistoryStage.setScene(new Scene(layout, 400, 300));
        watchHistoryStage.show();
    }
}
