package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;

import java.io.InputStream;
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

    /**
     * Resizes an image to the specified width and height.
     *
     * @param imagePath The path to the image in the resources folder.
     * @param width     The desired width of the image.
     * @param height    The desired height of the image.
     * @return An ImageView object containing the resized image.
     */
    private ImageView resizeImage(String imagePath, int width, int height) {
        try {
            // Ensure image path starts from resources
            InputStream inputStream = getClass().getResourceAsStream("/" + imagePath);
            if (inputStream == null) {
                System.err.println("Image not found: " + imagePath);
                return getDefaultImageView(width, height); // Return default image
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


    /**
     * Displays the movie titles screen with filters and a grid of movie posters.
     */
    public void showMovieTitlesScreen() {
        Stage movieTitlesStage = new Stage();
        movieTitlesStage.setTitle("Movie Titles");

        BorderPane root = new BorderPane();

        // Filter controls
        FlowPane filterPane = new FlowPane();
        filterPane.setHgap(10);
        filterPane.setPadding(new Insets(10));
        filterPane.setAlignment(Pos.CENTER); // Center the filter pane

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

        // Movie grid
        GridPane movieGrid = new GridPane();
        movieGrid.setHgap(20);
        movieGrid.setVgap(20);
        movieGrid.setPadding(new Insets(20));
        movieGrid.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(movieGrid);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        // Caching images
        Map<String, ImageView> imageCache = new HashMap<>();

        // Update movie list based on filters
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

    /**
     * Displays the details screen for a specific movie.
     *
     * @param movie The movie to display details for.
     */
    public void showMovieDetailsScreen(Movie movie) {
        Stage movieDetailsStage = new Stage();
        movieDetailsStage.setTitle(movie.getTitle());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Movie title
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);

        // Movie details
        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(20));
        detailsBox.setStyle("-fx-background-color: #F0F0F0; -fx-border-radius: 10px; -fx-background-radius: 10px;");

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

        // Buttons
        HBox buttonPane = new HBox(20);
        buttonPane.setPadding(new Insets(20));
        buttonPane.setAlignment(Pos.CENTER);

        Button btnBack = new Button("Back to Movie Titles");

        // Only add "Add to Favorites" and "Add to Watch History" buttons if the user is not an admin and not a guest
        if (!LoginGUI.isAdmin() && !LoginGUI.isGuest()) {
            Button btnAddToFavorites = new Button("Add to Favorites");
            Button btnAddToWatchHistory = new Button("Add to Watch History");

            btnAddToFavorites.setOnAction(e -> {
                userManager.addToFavorites(movie.getTitle());
                showAlert("Success", "Added to Favorites!");
            });

            btnAddToWatchHistory.setOnAction(e -> {
                userManager.addToWatchHistory(movie.getTitle());
                showAlert("Success", "Added to Watch History!");
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
}