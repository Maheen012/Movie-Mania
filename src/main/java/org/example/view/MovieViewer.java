package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieViewer {
    private MovieManager movieManager;
    private UserManager userManager;

    public MovieViewer(MovieManager movieManager, UserManager userManager) {
        this.movieManager = movieManager;
        this.userManager = userManager;
    }

    private ImageView resizeImage(String imagePath, int width, int height) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + imagePath);
            if (inputStream == null) {
                System.err.println("Image not found: " + imagePath);
                return getDefaultImageView(width, height);
            }
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            return imageView;
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultImageView(width, height);
        }
    }

    private ImageView getDefaultImageView(int width, int height) {
        ImageView defaultImageView = new ImageView(new Image(getClass().getResourceAsStream("/default-cover.jpg")));
        defaultImageView.setFitWidth(width);
        defaultImageView.setFitHeight(height);
        return defaultImageView;
    }

    public void showMovieTitlesScreen() {
        Stage movieTitlesStage = new Stage();
        movieTitlesStage.setTitle("Movie Titles");

        BorderPane root = new BorderPane();

        FlowPane filterPane = new FlowPane();
        filterPane.setHgap(10);
        filterPane.setPadding(new Insets(10));
        filterPane.setAlignment(Pos.CENTER);

        Label genreLabel = new Label("Genre:");
        ComboBox<String> genreComboBox = new ComboBox<>();
        genreComboBox.getItems().addAll("All", "Action", "Drama", "Comedy", "Horror", "Sci-Fi", "Thriller");
        genreComboBox.setValue("All");

        Label yearLabel = new Label("Year:");
        ComboBox<String> yearComboBox = new ComboBox<>();
        yearComboBox.getItems().add("All Years");
        for (int year = 2025; year >= 1900; year--) {
            yearComboBox.getItems().add(String.valueOf(year));
        }
        yearComboBox.setValue("All Years");

        Label ratingLabel = new Label("Rating:");
        Spinner<Double> ratingSpinner = new Spinner<>(0.0, 10.0, 0.0, 0.1);
        ratingSpinner.setEditable(true);

        Label searchLabel = new Label("Title:");
        TextField searchField = new TextField();
        searchField.setPromptText("Search by Title");

        Button searchButton = new Button("Search");

        filterPane.getChildren().addAll(genreLabel, genreComboBox, yearLabel, yearComboBox, ratingLabel, ratingSpinner, searchLabel, searchField, searchButton);
        root.setTop(filterPane);

        GridPane movieGrid = new GridPane();
        movieGrid.setHgap(20);
        movieGrid.setVgap(20);
        movieGrid.setPadding(new Insets(20));
        movieGrid.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(movieGrid);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        Map<String, ImageView> imageCache = new HashMap<>();

        Runnable updateMovieList = () -> {
            imageCache.clear();
            movieGrid.getChildren().clear();
            List<Movie> movies = movieManager.getMovies();

            String selectedGenre = genreComboBox.getValue();
            String selectedYear = yearComboBox.getValue();
            double selectedRating = ratingSpinner.getValue();
            String searchText = searchField.getText().toLowerCase();

            List<Movie> filteredMovies = movies.stream()
                    .filter(movie -> selectedGenre.equals("All") || movie.getGenre().toLowerCase().contains(selectedGenre.toLowerCase()))
                    .filter(movie -> selectedYear.equals("All Years") || Integer.toString(movie.getYear()).equals(selectedYear))
                    .filter(movie -> movie.getRating() >= selectedRating)
                    .filter(movie -> searchText.isEmpty() || movie.getTitle().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());

            double windowWidth = movieTitlesStage.getWidth() - 40;
            int columnCount = Math.max((int) (windowWidth / 180), 1);

            for (int i = 0; i < filteredMovies.size(); i++) {
                Movie movie = filteredMovies.get(i);
                if (movie.getCoverImagePath() == null || movie.getCoverImagePath().isEmpty()) {
                    System.err.println("Empty or null image path for movie: " + movie.getTitle());
                    continue;
                }

                VBox movieBox = new VBox(5);
                movieBox.setPadding(new Insets(5));
                movieBox.setAlignment(Pos.CENTER);
                movieBox.setStyle("-fx-background-color: #F0F0F0; -fx-border-radius: 10px; -fx-background-radius: 10px;");

                ImageView imageView = imageCache.computeIfAbsent(movie.getCoverImagePath(), key -> resizeImage(movie.getCoverImagePath(), 150, 200));
                movieBox.getChildren().add(imageView);

                Label titleLabel = new Label(movie.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold;");
                movieBox.getChildren().add(titleLabel);

                movieBox.setOnMouseClicked(event -> {
                    showMovieDetailsScreen(movie);
                    movieTitlesStage.close();
                });

                movieGrid.add(movieBox, i % columnCount, i / columnCount);
            }
        };

        searchButton.setOnAction(e -> updateMovieList.run());
        searchField.setOnAction(e -> updateMovieList.run());
        movieTitlesStage.widthProperty().addListener((obs, oldVal, newVal) -> updateMovieList.run());

        updateMovieList.run();

        Button backButton = new Button("Back to Catalogue");
        backButton.setOnAction(e -> movieTitlesStage.close());
        root.setBottom(backButton);

        Scene scene = new Scene(root, 1200, 800);
        movieTitlesStage.setScene(scene);
        movieTitlesStage.show();
    }

    public void showMovieDetailsScreen(Movie movie) {
        Stage movieDetailsStage = new Stage();
        movieDetailsStage.setTitle(movie.getTitle());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);

        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(20));
        detailsBox.setStyle("-fx-background-color: #F0F0F0; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        ImageView imageView = resizeImage(movie.getCoverImagePath(), 300, 400);
        detailsBox.getChildren().add(imageView);

        Label detailsLabel = new Label(
                "Year: " + movie.getYear() + "\n" +
                        "Main Cast: " + movie.getMainCast() + "\n" +
                        "Rating: " + movie.getRating() + "\n" +
                        "Genre: " + movie.getGenre() + "\n" +
                        "Description: " + movie.getDescription()
        );
        detailsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495E;");
        detailsLabel.setWrapText(true);

        detailsBox.getChildren().add(detailsLabel);
        root.setCenter(detailsBox);

        HBox buttonPane = new HBox(20);
        buttonPane.setPadding(new Insets(20));
        buttonPane.setAlignment(Pos.CENTER);

        Button btnBack = new Button("Back to Movie Titles");

        if (LoginGUI.isAdmin()) {
            Button btnUpdate = new Button("Update Movie");
            Button btnDelete = new Button("Delete Movie");

            btnUpdate.setOnAction(e -> {
                movieDetailsStage.close();
                showUpdateMovieScreen(movie);
            });

            btnDelete.setOnAction(e -> {
                boolean deleted = movieManager.deleteMovieById(movie.getMovieId());
                if (deleted) {
                    showAlert("Success", "Movie deleted successfully!");
                    movieDetailsStage.close();
                } else {
                    showAlert("Error", "Failed to delete movie.");
                }
            });

            buttonPane.getChildren().addAll(btnUpdate, btnDelete);
        } else if (!LoginGUI.isGuest()) {
            Button btnAddToFavorites = new Button("Add to Favorites");
            Button btnAddToWatchHistory = new Button("Add to Watch History");

            btnAddToFavorites.setOnAction(e -> {
                userManager.addToFavorites(movie.getTitle());
            });

            btnAddToWatchHistory.setOnAction(e -> {
                userManager.addToWatchHistory(movie.getTitle());
            });

            buttonPane.getChildren().addAll(btnAddToFavorites, btnAddToWatchHistory);
        }

        btnBack.setOnAction(e -> {
            movieDetailsStage.close();
            showMovieTitlesScreen();
        });

        buttonPane.getChildren().add(btnBack);
        root.setBottom(buttonPane);

        Scene scene = new Scene(root, 1200, 800);
        movieDetailsStage.setScene(scene);
        movieDetailsStage.show();
    }

    private void showUpdateMovieScreen(Movie movie) {
        Stage stage = new Stage();
        stage.setTitle("Update Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        TextField txtTitle = new TextField(movie.getTitle());
        txtTitle.setPromptText("Title");
        TextField txtYear = new TextField(String.valueOf(movie.getYear()));
        txtYear.setPromptText("Year");
        TextField txtMainCast = new TextField(movie.getMainCast());
        txtMainCast.setPromptText("Main Cast");
        TextField txtRating = new TextField(String.valueOf(movie.getRating()));
        txtRating.setPromptText("Rating");
        TextField txtGenre = new TextField(movie.getGenre());
        txtGenre.setPromptText("Genre");
        TextArea txtDescription = new TextArea(movie.getDescription());
        txtDescription.setPromptText("Description");

        Label lblImage = new Label("Current Cover Image: " + movie.getCoverImagePath());
        Button btnUploadImage = new Button("Change Image");
        Label lblImagePath = new Label(movie.getCoverImagePath());
        btnUploadImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                String imageName = movie.getMovieId() + "_" + selectedFile.getName();
                String destinationPath = "target/classes/images/" + imageName;
                try {
                    Files.copy(selectedFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                    lblImagePath.setText("images/" + imageName);
                    lblImage.setText("New Cover Image: " + imageName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Failed to upload image.");
                }
            }
        });

        Button btnUpdate = new Button("Update Movie");
        btnUpdate.setOnAction(e -> {
            try {
                movie.setTitle(txtTitle.getText());
                movie.setYear(Integer.parseInt(txtYear.getText().trim()));
                movie.setMainCast(txtMainCast.getText());
                movie.setRating(Double.parseDouble(txtRating.getText().trim()));
                movie.setGenre(txtGenre.getText());
                movie.setDescription(txtDescription.getText());
                movie.setCoverImagePath(lblImagePath.getText());

                movieManager.saveMovies();
                showAlert("Success", "Movie updated successfully!");
                stage.close();
                showMovieDetailsScreen(movie);
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid year and rating.");
            }
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> stage.close());

        HBox buttonBox = new HBox(10, btnUpdate, btnCancel);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(txtTitle, txtYear, txtMainCast, txtRating, txtGenre, txtDescription,
                lblImage, btnUploadImage, buttonBox);

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
}
