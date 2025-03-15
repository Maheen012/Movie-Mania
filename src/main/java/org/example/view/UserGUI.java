package org.example.view;

import org.example.controller.MovieManager;
import org.example.controller.UserManager;
import org.example.model.Movie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
        frame.setSize(1200, 800);
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

        // Add action listeners for buttons
        btnViewMovies.addActionListener(e -> new MovieViewer(movieManager, userManager).showMovieTitlesScreen());
        btnViewFavorites.addActionListener(e -> showFavoritesScreen(frame));
        btnWatchHistory.addActionListener(e -> showWatchHistoryScreen(frame));

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
     * The user can click on movie titles to view their details or select multiple movies to remove from the favorites.
     *
     * @param mainFrame The main catalog frame to be shown again after closing the favorites screen.
     */
    public void showFavoritesScreen(JFrame mainFrame) {
        // Get the current logged-in username
        String currentUsername = LoginGUI.getCurrentUsername();

        // Get the list of favorite movies for the current user
        List<String> favoriteMovies = userManager.getFavoriteMovies(currentUsername);

        // Check if there are no favorite movies
//        if (favoriteMovies.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "No favorites added yet.");
//            return;
//        }

        // Create the favorites frame
        JFrame favoritesFrame = new JFrame("Favorites");
        favoritesFrame.setSize(1200, 800);
        favoritesFrame.setLayout(new BorderLayout());

        // Create and add the header label
        JLabel lblFavorites = new JLabel("Your Favorite Movies", JLabel.CENTER);
        lblFavorites.setFont(new Font("Arial", Font.BOLD, 18));
        favoritesFrame.add(lblFavorites, BorderLayout.NORTH);

        // Create the panel to display the favorite movies with checkboxes
        JPanel favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
        List<JCheckBox> checkboxes = new ArrayList<>();  // List to hold checkboxes for each movie

        // Add a checkbox for each favorite movie
        for (String movieTitle : favoriteMovies) {
            JCheckBox movieCheckbox = new JCheckBox(movieTitle);
            checkboxes.add(movieCheckbox);
            favoritesPanel.add(movieCheckbox);
        }

        // Wrap the favorites panel with a scroll pane to handle overflow
        JScrollPane scrollPane = new JScrollPane(favoritesPanel);
        favoritesFrame.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the "Remove from Favs" button
        JPanel bottomPanel = new JPanel();

        // Add the "Remove from Favs" button to the bottom panel
        JButton btnRemoveFromFavs = new JButton("Remove from Favorites");
        btnRemoveFromFavs.addActionListener(e -> {
            List<String> selectedMovies = new ArrayList<>();

            // Add selected movie titles to the list
            for (JCheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    selectedMovies.add(checkbox.getText());
                }
            }

            // If no movies are selected, show a message
            if (selectedMovies.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No movies selected.");
                return;
            }

            // Remove selected movies from favorites and update the CSV
            for (String movieTitle : selectedMovies) {
                userManager.removeFromFavorites(movieTitle);
            }

            JOptionPane.showMessageDialog(null, "Selected movies removed from favorites.");
            favoritesFrame.dispose();  // Close the current favorites frame
            showFavoritesScreen(mainFrame);  // Refresh the favorites screen to reflect changes
        });
        bottomPanel.add(btnRemoveFromFavs);

        // Add the "Back to Catalogue" button
        JButton btnGoBack = new JButton("Back to Catalogue");
        btnGoBack.addActionListener(e -> {
            favoritesFrame.dispose();
            mainFrame.setVisible(true);
        });
        bottomPanel.add(btnGoBack);

        // Add the bottom panel with the buttons to the frame
        favoritesFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Display the favorites frame
        favoritesFrame.setVisible(true);
    }

    /**
     * Displays the user's watch history in a new frame.
     * The user can click on movie titles to view their details or select multiple movies to remove from the watch history.
     *
     * @param mainFrame The main catalog frame to be shown again after closing the watch history screen.
     */
    public void showWatchHistoryScreen(JFrame mainFrame) {
        // Get the current logged-in username
        String currentUsername = LoginGUI.getCurrentUsername();

        // Get the list of watch history movies for the current user
        List<String> watchHistoryMovies = userManager.getWatchHistory(currentUsername);

        // Create the watch history frame
        JFrame watchHistoryFrame = new JFrame("Watch History");
        watchHistoryFrame.setSize(1200, 800);
        watchHistoryFrame.setLayout(new BorderLayout());

        // Create and add the header label
        JLabel lblWatchHistory = new JLabel("Your Watch History", JLabel.CENTER);
        lblWatchHistory.setFont(new Font("Arial", Font.BOLD, 18));
        watchHistoryFrame.add(lblWatchHistory, BorderLayout.NORTH);

        // Create the panel to display the watch history movies with checkboxes
        JPanel watchHistoryPanel = new JPanel();
        watchHistoryPanel.setLayout(new BoxLayout(watchHistoryPanel, BoxLayout.Y_AXIS));  // Vertical layout
        List<JCheckBox> checkboxes = new ArrayList<>();  // List to hold checkboxes for each movie

        // Add a checkbox for each watch history movie
        for (String movieTitle : watchHistoryMovies) {
            JCheckBox movieCheckbox = new JCheckBox(movieTitle);
            checkboxes.add(movieCheckbox);
            watchHistoryPanel.add(movieCheckbox);
        }

        // Wrap the watch history panel with a scroll pane to handle overflow
        JScrollPane scrollPane = new JScrollPane(watchHistoryPanel);
        watchHistoryFrame.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the "Remove from History" button
        JPanel bottomPanel = new JPanel();

        // Add the "Remove from History" button to the bottom panel
        JButton btnRemoveFromHistory = new JButton("Remove from History");
        btnRemoveFromHistory.addActionListener(e -> {
            List<String> selectedMovies = new ArrayList<>();

            // Add selected movie titles to the list
            for (JCheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    selectedMovies.add(checkbox.getText());
                }
            }

            // If no movies are selected, show a message
            if (selectedMovies.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No movies selected.");
                return;
            }

            // Remove selected movies from watch history and update the CSV
            for (String movieTitle : selectedMovies) {
                userManager.removeFromWatchHistory(movieTitle);
            }

            JOptionPane.showMessageDialog(null, "Selected movies removed from watch history.");
            watchHistoryFrame.dispose();  // Close the current watch history frame
            showWatchHistoryScreen(mainFrame);  // Refresh the watch history screen to reflect changes
        });
        bottomPanel.add(btnRemoveFromHistory);

        // Add the "Back to Catalogue" button
        JButton btnGoBack = new JButton("Back to Catalogue");
        btnGoBack.addActionListener(e -> {
            watchHistoryFrame.dispose();
            mainFrame.setVisible(true);
        });
        bottomPanel.add(btnGoBack);

        // Add the bottom panel with the buttons to the frame
        watchHistoryFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Display the watch history frame
        watchHistoryFrame.setVisible(true);
    }

}
