package org.example.view;

import org.example.controller.MovieManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * UserGUI represents the graphical user interface for the user in the Movie Mania system.
 * It provides buttons to view movies, view favorites, watch history, and logout.
 */
public class UserGUI {
    private MovieManager movieManager;

    /**
     * Constructor for the UserGUI class.
     * Initializes the GUI with a MovieManager object to handle movie data.
     *
     * @param movieManager The MovieManager that handles movie data and actions.
     */
    public UserGUI(MovieManager movieManager) {
        this.movieManager = movieManager;
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

        // Placeholder for the View Favorites button functionality
        btnViewFavorites.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Favorites functionality not implemented yet.");
        });

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
}
