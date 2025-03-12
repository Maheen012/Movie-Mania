package org.example.view;

import org.example.controller.UserManager;
import org.example.model.Movie;
import org.example.controller.MovieManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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

    /**
     * Displays the screen with a list of movie titles.
     * Each movie title is represented by a button that, when clicked,
     * shows detailed information about the movie.
     */
    public void showMovieTitlesScreen() {
        // Create the movie titles frame
        JFrame movieTitlesFrame = new JFrame("Movie Titles");
        movieTitlesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieTitlesFrame.setSize(600, 400);
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
            // Create a button for each movie and add it to the panel
            for (Movie movie : movies) {
                JButton btnMovie = new JButton("<html><center>" + movie.getTitle() + "</center></html>");
                btnMovie.setPreferredSize(new Dimension(100, 180));
                btnMovie.setFont(new Font("Arial", Font.BOLD, 15));
                btnMovie.setVerticalTextPosition(SwingConstants.BOTTOM);
                btnMovie.setHorizontalTextPosition(SwingConstants.CENTER);
                btnMovie.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showMovieDetailsScreen(movie); // Show details of the selected movie
                        movieTitlesFrame.dispose(); // Close the movie titles window
                    }
                });
                movieListPanel.add(btnMovie); // Add button to the movie list panel
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
