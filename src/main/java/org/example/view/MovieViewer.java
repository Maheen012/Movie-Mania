package org.example.view;

import org.example.controller.UserManager;
import org.example.model.Movie;
import org.example.controller.MovieManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * MovieViewer provides the graphical user interface for displaying movie titles
 * and detailed information about each selected movie.
 */
public class MovieViewer {
    private MovieManager movieManager;
    private UserManager userManager;

    /**
     * Constructor for MovieViewer class.
     * Initializes the viewer with a MovieManager to manage movie data.
     *
     * @param movieManager The MovieManager to handle movie data and actions.
     */
    public MovieViewer(MovieManager movieManager) {
        this.userManager = userManager;
        this.movieManager = movieManager;
    }

    public ImageIcon resizeImage(String imagePath, int targetWidth, int targetHeight) {
        try {
            // Debug: Print the image path
            System.out.println("Loading image from: " + imagePath);

            // Load the original image
            BufferedImage originalImage = ImageIO.read(getClass().getClassLoader().getResource(imagePath));

            // If the image is missing, load a default image
            if (originalImage == null) {
                System.err.println("Failed to load image: " + imagePath + ". Loading default image.");
                originalImage = ImageIO.read(getClass().getClassLoader().getResource("images/default.jpg"));
            }

            // Calculate the aspect ratio
            double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();

            // Calculate the new dimensions while preserving the aspect ratio
            int newWidth = targetWidth;
            int newHeight = (int) (targetWidth / aspectRatio);

            // If the calculated height exceeds the target height, adjust the dimensions
            if (newHeight > targetHeight) {
                newHeight = targetHeight;
                newWidth = (int) (targetHeight * aspectRatio);
            }

            // Resize the image
            Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            // Convert the resized image to an ImageIcon
            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if the image cannot be loaded
        }
    }

    /**
     * Displays the screen with a list of movie titles.
     * Each movie title is represented by a button that, when clicked,
     * shows detailed information about the movie.
     */
    public void showMovieTitlesScreen() {
        // Create the movie titles frame
        JFrame movieTitlesFrame = new JFrame("Movie Titles");
        movieTitlesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieTitlesFrame.setSize(800, 600); // Increased size to accommodate images
        movieTitlesFrame.setLayout(new BorderLayout());

        // Create and add the title label to the top of the window
        JLabel lblMovieTitles = new JLabel("Movies", JLabel.CENTER);
        lblMovieTitles.setFont(new Font("Arial", Font.BOLD, 18));
        movieTitlesFrame.add(lblMovieTitles, BorderLayout.NORTH);

        // Create a panel to display the list of movies
        JPanel movieListPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        movieListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add a scroll pane to the movie list panel for scrolling through the list
        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        movieTitlesFrame.add(scrollPane, BorderLayout.CENTER);

        // Get the list of movies from the MovieManager
        List<Movie> movies = movieManager.getMovies();
        if (movies.isEmpty()) {
            // Show a message if no movies are found
            JOptionPane.showMessageDialog(movieTitlesFrame, "No movies found!");
        } else {
            // Create a panel for each movie and add it to the list panel
            for (Movie movie : movies) {
                JPanel moviePanel = new JPanel(new BorderLayout());
                moviePanel.setPreferredSize(new Dimension(200, 300));

                // Resize the movie cover image
                ImageIcon coverIcon = resizeImage(movie.getCoverImagePath(), 150, 200); // Resize to 150x200 pixels
                JLabel coverLabel = new JLabel(coverIcon);
                moviePanel.add(coverLabel, BorderLayout.CENTER);

                // Add the movie title below the cover
                JLabel titleLabel = new JLabel(movie.getTitle(), JLabel.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
                moviePanel.add(titleLabel, BorderLayout.SOUTH);

                // Add action listener to show movie details when clicked
                moviePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        showMovieDetailsScreen(movie);
                        movieTitlesFrame.dispose();
                    }
                });

                movieListPanel.add(moviePanel);
            }
        }

        // Add a back button to return to the catalogue
        JButton btnBack = new JButton("Back to Catalogue");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movieTitlesFrame.dispose(); // Close the movie titles window
            }
        });
        movieTitlesFrame.add(btnBack, BorderLayout.SOUTH); // Add button to the bottom

        movieTitlesFrame.setVisible(true); // Make the frame visible
    }

    /**
     * Displays a detailed screen for a selected movie.
     * Shows movie details such as year, cast, rating, genre, and description with buttons for favorites
     * and watch history
     *
     * @param movie The movie whose details will be displayed.
     */
    public void showMovieDetailsScreen(Movie movie) {
        userManager = new UserManager();
        // Create movie details frame
        JFrame movieDetailsFrame = new JFrame(movie.getTitle());
        movieDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieDetailsFrame.setSize(500, 400);
        movieDetailsFrame.setLayout(new BorderLayout());

        // Create and add the title label to the top of the window
        JLabel lblMovieDetails = new JLabel(movie.getTitle(), JLabel.CENTER);
        lblMovieDetails.setFont(new Font("Arial", Font.BOLD, 24));
        movieDetailsFrame.add(lblMovieDetails, BorderLayout.NORTH);

        // Create a text area to display the movie details
        JTextArea txtMovieDetails = new JTextArea();
        txtMovieDetails.setEditable(false); // Make the text area read-only
        txtMovieDetails.setLineWrap(true); // Enable line wrapping
        txtMovieDetails.setWrapStyleWord(true); // Wrap at word boundaries
        txtMovieDetails.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMovieDetails.setText(
                "Year: " + movie.getYear() + "\n" +
                        "Main Cast: " + movie.getMainCast() + "\n" +
                        "Rating: " + movie.getRating() + "\n" +
                        "Genre: " + movie.getGenre() + "\n" +
                        "Description: " + movie.getDescription()
        );

        // Add a scroll pane to the text area for scrolling through movie details
        JScrollPane scrollPane = new JScrollPane(txtMovieDetails);
        movieDetailsFrame.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Add "Add to Favorites" button
        JButton btnAddToFavorites = new JButton("Add to Favorites");
        btnAddToFavorites.addActionListener(e -> {
            String movieTitle = movie.getTitle();
            userManager.addToFavorites(movieTitle);
        });
        buttonPanel.add(btnAddToFavorites);

        // Add "Add to Watch History" button
        JButton btnAddToWatchHistory = new JButton("Add to Watch History");
        btnAddToWatchHistory.addActionListener(e -> {
            String movieTitle = movie.getTitle();
            userManager.addToWatchHistory(movieTitle);
        });
        buttonPanel.add(btnAddToWatchHistory);

        // Add "Back to Movie Titles" button
        JButton btnBack = new JButton("Back to Movie Titles");
        btnBack.addActionListener(e -> {
            movieDetailsFrame.dispose();
            showMovieTitlesScreen();
        });
        buttonPanel.add(btnBack);

        // Add the button panel to the south of the frame
        movieDetailsFrame.add(buttonPanel, BorderLayout.SOUTH);

        movieDetailsFrame.setVisible(true);
    }
}
