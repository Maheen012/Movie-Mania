package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;

import java.io.InputStream;
import java.util.List;
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

        Label genreLabel = new Label("Genre:");
        ComboBox<String> genreComboBox = new ComboBox<>();
        genreComboBox.getItems().addAll("All", "Action", "Drama", "Comedy", "Horror", "Sci-Fi", "Thriller");
        genreComboBox.setValue("All");

        Label yearLabel = new Label("Year:");
        TextField yearField = new TextField();
        yearField.setPromptText("Year");

        Label ratingLabel = new Label("Rating:");
        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating");

        Label searchLabel = new Label("Title:");
        TextField searchField = new TextField();
        searchField.setPromptText("Search by Title");

        Button searchButton = new Button("Search");

        filterPane.getChildren().addAll(genreLabel, genreComboBox, yearLabel, yearField, ratingLabel, ratingField, searchLabel, searchField, searchButton);
        root.setTop(filterPane);

        // Movie grid
        GridPane movieGrid = new GridPane();
        movieGrid.setHgap(10);
        movieGrid.setVgap(10);
        movieGrid.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(movieGrid);
        root.setCenter(scrollPane);

        // Update movie list based on filters
        Runnable updateMovieList = () -> {
            movieGrid.getChildren().clear();
            List<Movie> movies = movieManager.getMovies();

            // Apply filters
            String selectedGenre = genreComboBox.getValue();
            String yearText = yearField.getText();
            String ratingText = ratingField.getText();
            String searchText = searchField.getText().toLowerCase();

            List<Movie> filteredMovies = movies.stream()
                    .filter(movie -> selectedGenre.equals("All") || movie.getGenre().toLowerCase().contains(selectedGenre.toLowerCase()))
                    .filter(movie -> yearText.isEmpty() || String.valueOf(movie.getYear()).equals(yearText))
                    .filter(movie -> ratingText.isEmpty() || String.valueOf(movie.getRating()).equals(ratingText))
                    .filter(movie -> searchText.isEmpty() || movie.getTitle().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());

            for (int i = 0; i < filteredMovies.size(); i++) {
                Movie movie = filteredMovies.get(i);

                // Check for empty or null image paths
                if (movie.getCoverImagePath() == null || movie.getCoverImagePath().isEmpty()) {
                    System.err.println("Empty or null image path for movie: " + movie.getTitle());
                    continue; // Skip this movie
                }

                VBox movieBox = new VBox(5);
                movieBox.setPadding(new Insets(5));
                movieBox.getChildren().add(resizeImage(movie.getCoverImagePath(), 150, 200));
                Label titleLabel = new Label(movie.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold;");
                movieBox.getChildren().add(titleLabel);
                movieBox.setOnMouseClicked(event -> {
                    showMovieDetailsScreen(movie);
                    movieTitlesStage.close();
                });
                movieGrid.add(movieBox, i % 3, i / 3);
            }
        };

        // Add event handlers for filters
        searchButton.setOnAction(e -> updateMovieList.run());
        searchField.setOnAction(e -> updateMovieList.run());

        // Initial movie list update
        updateMovieList.run();

        // Back button
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

        // Movie title
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        root.setTop(titleLabel);

        // Movie details
        TextArea detailsArea = new TextArea(
                "Year: " + movie.getYear() + "\n" +
                        "Main Cast: " + movie.getMainCast() + "\n" +
                        "Rating: " + movie.getRating() + "\n" +
                        "Genre: " + movie.getGenre() + "\n" +
                        "Description: " + movie.getDescription()
        );
        detailsArea.setEditable(false);
        root.setCenter(detailsArea);

        // Buttons
        FlowPane buttonPane = new FlowPane(10, 10);
        buttonPane.setPadding(new Insets(10));

        Button btnAddToFavorites = new Button("Add to Favorites");
        btnAddToFavorites.setOnAction(e -> userManager.addToFavorites(movie.getTitle()));

        Button btnAddToWatchHistory = new Button("Add to Watch History");
        btnAddToWatchHistory.setOnAction(e -> userManager.addToWatchHistory(movie.getTitle()));

        Button btnBack = new Button("Back to Movie Titles");
        btnBack.setOnAction(e -> {
            movieDetailsStage.close();
            showMovieTitlesScreen();
        });

        buttonPane.getChildren().addAll(btnAddToFavorites, btnAddToWatchHistory, btnBack);
        root.setBottom(buttonPane);

        Scene scene = new Scene(root, 1200, 800);
        movieDetailsStage.setScene(scene);
        movieDetailsStage.show();
    }
}