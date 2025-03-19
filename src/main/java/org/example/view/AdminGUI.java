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

/**
 * AdminGUI represents the admin dashboard for Movie Mania.
 * It allows admins to add, delete, update, and view movies.
 */
public class AdminGUI extends Application {
    private MovieManager movieManager;
    private UserManager userManager;

    /**
     * Constructor to initialize the AdminGUI with MovieManager and UserManager.
     */
    public AdminGUI() {
        this.movieManager = new MovieManager();
        this.userManager = new UserManager();
        this.movieManager.readMovies(); // Ensure movies are read from the CSV file
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

    /**
     * Displays the screen for adding a new movie.
     */
    private void showAddMovieScreen() {
        Stage stage = new Stage();
        stage.setTitle("Add Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Input fields for movie details
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
                // Parse input and create a new movie
                int year = Integer.parseInt(txtYear.getText().trim());
                double rating = Double.parseDouble(txtRating.getText().trim());
                Movie newMovie = new Movie(0, txtTitle.getText(), year, txtMainCast.getText(), rating, txtGenre.getText(), txtDescription.getText(), "");
                movieManager.getMovies().add(newMovie);
                movieManager.saveMovies(); // Save the updated list to the CSV file
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid year and rating.");
            }
        });

        layout.getChildren().addAll(txtTitle, txtYear, txtMainCast, txtRating, txtGenre, txtDescription, btnAdd);
        stage.setScene(new Scene(layout, 400, 500));
        stage.show();
    }

    /**
     * Displays the screen for deleting a movie.
     * Uses a ComboBox to select the movie by ID and name.
     */
    private void showDeleteMovieScreen() {
        Stage stage = new Stage();
        stage.setTitle("Delete Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Create a ComboBox to display movie ID and name
        ComboBox<String> movieComboBox = new ComboBox<>();
        for (Movie movie : movieManager.getMovies()) {
            movieComboBox.getItems().add(movie.getMovieId() + ": " + movie.getTitle());
        }

        Button btnDelete = new Button("Delete");

        btnDelete.setOnAction(e -> {
            String selectedMovie = movieComboBox.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                // Extract movie ID from the selected item
                int movieId = Integer.parseInt(selectedMovie.split(":")[0].trim());
                boolean deleted = deleteMovieById(movieId);
                if (deleted) {
                    showAlert("Success", "Movie deleted successfully!");
                } else {
                    showAlert("Error", "Movie not found!");
                }
                stage.close();
            } else {
                showAlert("Error", "Please select a movie to delete.");
            }
        });

        layout.getChildren().addAll(movieComboBox, btnDelete);
        stage.setScene(new Scene(layout, 300, 200));
        stage.show();
    }

    /**
     * Displays the screen for updating a movie.
     * Uses a ComboBox to select the movie by ID and name.
     */
    private void showUpdateMovieScreen() {
        Stage stage = new Stage();
        stage.setTitle("Update Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Create a ComboBox to display movie ID and name
        ComboBox<String> movieComboBox = new ComboBox<>();
        for (Movie movie : movieManager.getMovies()) {
            movieComboBox.getItems().add(movie.getMovieId() + ": " + movie.getTitle());
        }

        // Fields for updating movie details
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

        Button btnUpdate = new Button("Update Movie");

        // When a movie is selected, populate the fields with its details
        movieComboBox.setOnAction(e -> {
            String selectedMovie = movieComboBox.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                // Extract movie ID from the selected item
                int movieId = Integer.parseInt(selectedMovie.split(":")[0].trim());
                Movie movie = movieManager.getMovieById(movieId);
                if (movie != null) {
                    // Populate fields with the selected movie's details
                    txtTitle.setText(movie.getTitle());
                    txtYear.setText(String.valueOf(movie.getYear()));
                    txtMainCast.setText(movie.getMainCast());
                    txtRating.setText(String.valueOf(movie.getRating()));
                    txtGenre.setText(movie.getGenre());
                    txtDescription.setText(movie.getDescription());
                }
            }
        });

        btnUpdate.setOnAction(e -> {
            String selectedMovie = movieComboBox.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                // Extract movie ID from the selected item
                int movieId = Integer.parseInt(selectedMovie.split(":")[0].trim());
                Movie movie = movieManager.getMovieById(movieId);
                if (movie != null) {
                    try {
                        // Update the movie details
                        movie.setTitle(txtTitle.getText());
                        movie.setYear(Integer.parseInt(txtYear.getText().trim()));
                        movie.setMainCast(txtMainCast.getText());
                        movie.setRating(Double.parseDouble(txtRating.getText().trim()));
                        movie.setGenre(txtGenre.getText());
                        movie.setDescription(txtDescription.getText());
                        movieManager.saveMovies();
                        showAlert("Success", "Movie updated successfully!");
                        stage.close();
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid Input", "Please enter valid year and rating.");
                    }
                }
            } else {
                showAlert("Error", "Please select a movie to update.");
            }
        });

        layout.getChildren().addAll(movieComboBox, txtTitle, txtYear, txtMainCast, txtRating, txtGenre, txtDescription, btnUpdate);
        stage.setScene(new Scene(layout, 400, 500));
        stage.show();
    }

    /**
     * Displays a list of all movies.
     */
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

    /**
     * Deletes a movie by its ID.
     *
     * @param movieId The ID of the movie to delete.
     * @return True if the movie was deleted, false otherwise.
     */
    private boolean deleteMovieById(int movieId) {
        List<Movie> movies = movieManager.getMovies();
        return movies.removeIf(movie -> movie.getMovieId() == movieId);
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to display.
     */
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
