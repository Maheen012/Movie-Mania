package org.example.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

/**
 * AdminGUI represents the admin dashboard for Movie Mania.
 * It allows admins to add and view movies (with update/delete functionality in MovieViewer).
 */
public class AdminGUI extends Application {
    private MovieManager movieManager;
    private UserManager userManager;

    public AdminGUI() {
        this.movieManager = new MovieManager();
        this.userManager = new UserManager();
        this.movieManager.readMovies();
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
        Button btnViewMovies = new Button("View Movies");
        Button btnLogout = new Button("Logout");

        btnAddMovie.setOnAction(e -> showAddMovieScreen());
        btnViewMovies.setOnAction(e -> {
            Platform.runLater(() -> {
                MovieViewer movieViewer = new MovieViewer(movieManager, userManager);
                movieViewer.showMovieTitlesScreen();
            });
        });
        btnLogout.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(title, btnAddMovie, btnViewMovies, btnLogout);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddMovieScreen() {
        Stage stage = new Stage();
        stage.setTitle("Add Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        int nextMovieId = movieManager.getMovies().stream()
                .mapToInt(Movie::getMovieId)
                .max()
                .orElse(0) + 1;

        TextField txtID = new TextField(String.valueOf(nextMovieId));
        txtID.setEditable(false);
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
                String imageName = nextMovieId + "_" + selectedFile.getName();
                String destinationPath = "target/classes/images/" + imageName;
                try {
                    Files.copy(selectedFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                    lblImagePath.setText("images/" + imageName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Failed to upload image.");
                }
            }
        });

        Button btnAdd = new Button("Add Movie");
        btnAdd.setOnAction(e -> {
            try {
                Movie newMovie = new Movie(
                        nextMovieId,
                        txtTitle.getText(),
                        Integer.parseInt(txtYear.getText().trim()),
                        txtMainCast.getText(),
                        Double.parseDouble(txtRating.getText().trim()),
                        txtGenre.getText(),
                        txtDescription.getText(),
                        lblImagePath.getText()
                );

                movieManager.getMovies().add(newMovie);
                movieManager.saveMovies();
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
