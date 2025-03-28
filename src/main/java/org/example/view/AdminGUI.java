package org.example.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        btnViewMovies.setOnAction(e -> {
            Platform.runLater(() -> {
                MovieViewer movieViewer = new MovieViewer(movieManager, userManager);
                movieViewer.showMovieTitlesScreen();
            });
        });
        btnLogout.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(title, btnAddMovie, btnDeleteMovie, btnUpdateMovie, btnViewMovies, btnLogout);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
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
                boolean deleted = movieManager.deleteMovieById(movieId);

                if (deleted) {
                    showAlert("Success", "Movie deleted successfully!");

                    // Update the ComboBox to reflect the removal
                    movieComboBox.getItems().clear();
                    for (Movie movie : movieManager.getMovies()) {
                        movieComboBox.getItems().add(movie.getMovieId() + ": " + movie.getTitle());
                    }
                } else {
                    showAlert("Error", "Movie not found!");
                }
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

        // Image upload
        Label lblImage = new Label("Upload Cover Image:");
        Button btnUploadImage = new Button("Choose Image");
        Label lblImagePath = new Label("No image selected");
        btnUploadImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                // Save the image to the resources/images folder
                String imageName = movieComboBox.getValue().split(":")[0].trim() + "_" + selectedFile.getName();
                String destinationPath = "target/classes/images/" + imageName;
                try {
                    Files.copy(selectedFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                    lblImagePath.setText("images/" + imageName); // Store relative path
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Failed to upload image.");
                }
            }
        });

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
                    lblImagePath.setText(movie.getCoverImagePath()); // Set current image path
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
                        movie.setCoverImagePath(lblImagePath.getText()); // Update image path
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

        layout.getChildren().addAll(movieComboBox, txtTitle, txtYear, txtMainCast, txtRating, txtGenre, txtDescription,
                lblImage, btnUploadImage, lblImagePath, btnUpdate);
        stage.setScene(new Scene(layout, 400, 500));
        stage.show();
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
    private void showAddMovieScreen() {
        Stage stage = new Stage();
        stage.setTitle("Add Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Find the next available movie ID
        int nextMovieId = movieManager.getMovies().stream()
                .mapToInt(Movie::getMovieId)
                .max()
                .orElse(0) + 1;

        // Input fields for movie details
        TextField txtID = new TextField(String.valueOf(nextMovieId));
        txtID.setEditable(false);  // Make it read-only since it's auto-generated
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

        // Image upload
        Label lblImage = new Label("Upload Cover Image:");
        Button btnUploadImage = new Button("Choose Image");
        Label lblImagePath = new Label("No image selected");
        btnUploadImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                // Save the image to the resources/images folder
                String imageName = nextMovieId + "_" + selectedFile.getName();
                String destinationPath = "target/classes/images/" + imageName;
                try {
                    Files.copy(selectedFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                    lblImagePath.setText("images/" + imageName); // Store relative path
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Failed to upload image.");
                }
            }
        });

        Button btnAdd = new Button("Add Movie");
        btnAdd.setOnAction(e -> {
            try {
                // Parse input and create a new movie
                int year = Integer.parseInt(txtYear.getText().trim());
                double rating = Double.parseDouble(txtRating.getText().trim());

                Movie newMovie = new Movie(
                        nextMovieId,
                        txtTitle.getText(),
                        year,
                        txtMainCast.getText(),
                        rating,
                        txtGenre.getText(),
                        txtDescription.getText(),
                        lblImagePath.getText() // Set the image path
                );

                movieManager.getMovies().add(newMovie);
                movieManager.saveMovies(); // Save the updated list to the CSV file
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid year and rating.");
            }
        });

        layout.getChildren().addAll(txtID, txtTitle, txtYear, txtMainCast, txtRating, txtGenre, txtDescription,
                lblImage, btnUploadImage, lblImagePath, btnAdd);

        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
        stage.show();
    }
}
