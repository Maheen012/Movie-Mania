package org.example.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;

import java.util.List;

public class AdminGUI extends Application {
    private MovieManager movieManager;
    private UserManager userManager;

    public AdminGUI() {
        this.movieManager = new MovieManager();
        this.userManager = new UserManager();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Movie Mania - Admin Catalogue");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Movie Mania - Admin Panel");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnAddMovie = new Button("Add Movie");
        Button btnDeleteMovie = new Button("Delete Movie");
        Button btnUpdateMovie = new Button("Update Movie");
        Button btnViewMovies = new Button("View Movies");
        Button btnLogout = new Button("Logout");

        // Button Actions
        btnAddMovie.setOnAction(e -> showAddMovieScreen());
        btnDeleteMovie.setOnAction(e -> showDeleteMovieScreen());
        btnUpdateMovie.setOnAction(e -> showUpdateMovieScreen());
        btnViewMovies.setOnAction(e -> showMovieList());
        btnLogout.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(title, btnAddMovie, btnDeleteMovie, btnUpdateMovie, btnViewMovies, btnLogout);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddMovieScreen() {
        Stage stage = new Stage();
        stage.setTitle("Add Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Title");
        TextField txtYear = new TextField();
        txtYear.setPromptText("Year");
        TextField txtMainCast = new TextField();
        txtMainCast.setPromptText("Main Cast");
        TextField txtRating = new TextField();
        txtRating.setPromptText("Rating");
        TextField txtGenre = new TextField();
        txtGenre.setPromptText("Genre");
        TextArea txtDescription = new TextArea();
        txtDescription.setPromptText("Description");

        Button btnAdd = new Button("Add Movie");
        btnAdd.setOnAction(e -> {
            try {
                int year = Integer.parseInt(txtYear.getText().trim());
                double rating = Double.parseDouble(txtRating.getText().trim());
                Movie newMovie = new Movie(0, txtTitle.getText(), year, txtMainCast.getText(), rating, txtGenre.getText(), txtDescription.getText(), "");
                movieManager.getMovies().add(newMovie);
                movieManager.saveMovies();
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid year and rating.");
            }
        });

        layout.getChildren().addAll(txtTitle, txtYear, txtMainCast, txtRating, txtGenre, txtDescription, btnAdd);
        stage.setScene(new Scene(layout, 400, 500));
        stage.show();
    }

    private void showDeleteMovieScreen() {
        Stage stage = new Stage();
        stage.setTitle("Delete Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        TextField txtMovieId = new TextField();
        txtMovieId.setPromptText("Enter Movie ID");
        Button btnDelete = new Button("Delete");

        btnDelete.setOnAction(e -> {
            try {
                int movieId = Integer.parseInt(txtMovieId.getText().trim());
                boolean deleted = deleteMovieById(movieId);
                if (deleted) {
                    showAlert("Success", "Movie deleted successfully!");
                } else {
                    showAlert("Error", "Movie not found!");
                }
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid movie ID.");
            }
        });

        layout.getChildren().addAll(txtMovieId, btnDelete);
        stage.setScene(new Scene(layout, 300, 200));
        stage.show();
    }

    private void showUpdateMovieScreen() {
        showAlert("Feature Coming Soon", "Update feature is under development.");
    }

    private void showMovieList() {
        Stage stage = new Stage();
        stage.setTitle("Movie List");

        ListView<String> movieListView = new ListView<>();
        for (Movie movie : movieManager.getMovies()) {
            movieListView.getItems().add(movie.getTitle() + " (" + movie.getYear() + ")");
        }

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(movieListView);

        stage.setScene(new Scene(layout, 400, 300));
        stage.show();
    }

    private boolean deleteMovieById(int movieId) {
        List<Movie> movies = movieManager.getMovies();
        return movies.removeIf(movie -> movie.getMovieId() == movieId);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
