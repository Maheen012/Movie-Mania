package org.example.view;

import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * UserGUI represents the graphical user interface for the user in the Movie Mania system.
 * It provides buttons to view movies, view favorites, watch history, and logout.
 */
public class UserGUI {
    private MovieManager movieManager;
    private UserManager userManager;

    /**
     * Constructor for the UserGUI class.
     * Initializes the GUI with MovieManager and UserManager objects.
     *
     * @param movieManager The MovieManager that handles movie data and actions.
     * @param userManager The UserManager that handles user data, favorites, and authentication.
     */
    public UserGUI(MovieManager movieManager, UserManager userManager) {
        this.movieManager = movieManager;
        this.userManager = userManager;
        initializeUI();
    }

    /**
     * Initializes the User GUI with necessary UI components such as buttons and labels.
     * This method sets up the window layout and action listeners for each button.
     */
    private void initializeUI() {
        // Create frame for user catalogue window
        JFrame frame = new JFrame("Movie Mania - User Catalogue");
        frame.setSize(400, 350);
        frame.setLayout(new BorderLayout());

        // Create and add the title label to the top of the window
        JLabel lblTitle = new JLabel("Movie Mania", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        // Create a panel to hold buttons and set up the layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Buttons for user functionality
        JButton btnViewMovies = new JButton("View Movies");
        JButton btnViewFavorites = new JButton("View Favorites");
        JButton btnWatchHistory = new JButton("Watch History");
        JButton btnLogout = new JButton("Logout");

        // Add action listeners to buttons
        btnViewMovies.addActionListener(e -> new MovieViewer(movieManager).showMovieTitlesScreen());
        btnViewFavorites.addActionListener(e -> showFavoritesScreen(frame));

        // Placeholder for the Watch History button functionality
        btnWatchHistory.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Watch history functionality not implemented yet.");
        });

        // Logout functionality
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current window
                System.exit(0); // Completely terminate the program
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(btnViewMovies);
        buttonPanel.add(btnViewFavorites);
        buttonPanel.add(btnWatchHistory);
        buttonPanel.add(btnLogout);

        // Add the button panel to the center of the window
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Displays the user's favorite movies in a new frame.
     *
     * @param mainFrame The main catalog frame to be shown again after closing the favorites screen.
     */
    public void showFavoritesScreen(JFrame mainFrame) {
        String currentUsername = LoginGUI.getCurrentUsername(); // Get the logged-in username
        List<String> favoriteMovies = userManager.getFavoriteMovies(currentUsername); // Get favorite movies

        if (favoriteMovies.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No favorites added yet.");
            return;
        }

        JFrame favoritesFrame = new JFrame("Favorites");
        favoritesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        favoritesFrame.setSize(600, 500);
        favoritesFrame.setLayout(new BorderLayout());

        JLabel lblFavorites = new JLabel("Your Favorite Movies", JLabel.CENTER);
        lblFavorites.setFont(new Font("Arial", Font.BOLD, 18));
        favoritesFrame.add(lblFavorites, BorderLayout.NORTH);

        JPanel favoritesPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        favoritesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(favoritesPanel);
        favoritesFrame.add(scrollPane, BorderLayout.CENTER);

        // Loop through each movie in the user's favorites and add them as buttons
        for (String movieTitle : favoriteMovies) {
            Movie movie = movieManager.getMovieByTitle(movieTitle);

            if (movie != null) {
                JButton btnFavorite = new JButton("<html><center>" + movie.getTitle() + "</center></html>");
                btnFavorite.setPreferredSize(new Dimension(100, 180));
                btnFavorite.setFont(new Font("Arial", Font.BOLD, 15));
                btnFavorite.setVerticalTextPosition(SwingConstants.BOTTOM);
                btnFavorite.setHorizontalTextPosition(SwingConstants.CENTER);
                btnFavorite.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new MovieViewer(movieManager).showMovieDetailsScreen(movie); // Show the movie details screen
                        favoritesFrame.dispose(); // Close the favorites screen after selecting a movie
                    }
                });
                favoritesPanel.add(btnFavorite); // Add the button for the favorite movie
            }
        }

        // Create the "Go Back to Main Page" button
        JButton btnGoBack = new JButton("Back to Catalogue");
        btnGoBack.setFont(new Font("Arial", Font.BOLD, 15));
        btnGoBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                favoritesFrame.dispose(); // Close the favorites frame
                mainFrame.setVisible(true); // Make the main frame visible again
            }
        });

        // Add the "Go Back" button to the bottom of the window
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnGoBack);
        favoritesFrame.add(bottomPanel, BorderLayout.SOUTH);

        favoritesFrame.setVisible(true); // Show the favorites frame
    }

}
