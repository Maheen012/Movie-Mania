package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI {

    private MovieManager movieManager;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }

    public GUI() {
        movieManager = new MovieManager();
        movieManager.readMovies("movies.csv");

        JFrame frame = new JFrame("Movie Mania - Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Movie Mania", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        JButton btnSearch = new JButton("View Movies");
        JButton btnViewFavorites = new JButton("View Favorites");
        JButton btnWatchHistory = new JButton("Watch History");
        JButton btnLogout = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnViewFavorites);
        buttonPanel.add(btnWatchHistory);
        buttonPanel.add(btnLogout);
        frame.add(buttonPanel, BorderLayout.CENTER);

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMovieTitlesScreen();
            }
        });

        btnViewFavorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "View Favorites clicked");
            }
        });

        btnWatchHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWatchHistoryScreen();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Logged Out");
            }
        });

        frame.setVisible(true);
    }

    private void showMovieTitlesScreen() {
        JFrame movieTitlesFrame = new JFrame("Movie Titles");
        movieTitlesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieTitlesFrame.setSize(600, 400); // Adjusted size for better visibility
        movieTitlesFrame.setLayout(new BorderLayout());

        JLabel lblMovieTitles = new JLabel("Movies", JLabel.CENTER);
        lblMovieTitles.setFont(new Font("Arial", Font.BOLD, 18));
        movieTitlesFrame.add(lblMovieTitles, BorderLayout.NORTH);

        // Create a panel with a GridLayout (3 columns, dynamic rows)
        JPanel movieListPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 0 means dynamic rows, 3 columns, 10px horizontal and vertical gaps
        movieListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        movieTitlesFrame.add(scrollPane, BorderLayout.CENTER);

        List<Movie> movies = movieManager.getMovies();
        for (Movie movie : movies) {
            JButton btnMovie = new JButton("<html><center>" + movie.getTitle() + "</center></html>"); // Use HTML to center text
            btnMovie.setPreferredSize(new Dimension(100, 180)); // Shorter width, longer height (like a movie cover)
            btnMovie.setFont(new Font("Arial", Font.BOLD, 15)); // Adjust font size to fit the button
            btnMovie.setVerticalTextPosition(SwingConstants.BOTTOM); // Place text at the bottom
            btnMovie.setHorizontalTextPosition(SwingConstants.CENTER); // Center text horizontally
            btnMovie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMovieDetailsScreen(movie);
                    movieTitlesFrame.dispose(); // Close the movie titles screen when a movie is selected
                }
            });
            movieListPanel.add(btnMovie);
        }

        JButton btnBack = new JButton("Back to Catalog");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movieTitlesFrame.dispose(); // Close the movie titles screen
                new GUI(); // Reopen the main catalog screen
            }
        });
        movieTitlesFrame.add(btnBack, BorderLayout.SOUTH);

        movieTitlesFrame.setVisible(true);
    }


    private void showMovieDetailsScreen(Movie movie) {
        JFrame movieDetailsFrame = new JFrame(movie.getTitle());
        movieDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieDetailsFrame.setSize(500, 400); // Adjusted size for better visibility
        movieDetailsFrame.setLayout(new BorderLayout());

        JLabel lblMovieDetails = new JLabel(movie.getTitle(), JLabel.CENTER);
        lblMovieDetails.setFont(new Font("Arial", Font.BOLD, 24));
        movieDetailsFrame.add(lblMovieDetails, BorderLayout.NORTH);

        // Create a JTextArea to display movie details
        JTextArea txtMovieDetails = new JTextArea();
        txtMovieDetails.setEditable(false);
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

        // Wrap the JTextArea in a JScrollPane for vertical scrolling
        JScrollPane scrollPane = new JScrollPane(txtMovieDetails);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling
        movieDetailsFrame.add(scrollPane, BorderLayout.CENTER);

        // Back button to return to the movie titles screen
        JButton btnBack = new JButton("Back to Movie Titles");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movieDetailsFrame.dispose();
                showMovieTitlesScreen();
            }
        });
        movieDetailsFrame.add(btnBack, BorderLayout.SOUTH);

        movieDetailsFrame.setVisible(true);
    }

    private void showWatchHistoryScreen() {
        JFrame watchHistoryFrame = new JFrame("Watch History");
        watchHistoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        watchHistoryFrame.setSize(400, 300);
        watchHistoryFrame.setLayout(new BorderLayout());

        JLabel lblWatchHistory = new JLabel("Your Watch History", JLabel.CENTER);
        lblWatchHistory.setFont(new Font("Arial", Font.BOLD, 24));
        watchHistoryFrame.add(lblWatchHistory, BorderLayout.NORTH);

        JButton btnBack = new JButton("Back to Catalog");
        watchHistoryFrame.add(btnBack, BorderLayout.CENTER);

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                watchHistoryFrame.dispose();
                new GUI();
            }
        });

        watchHistoryFrame.setVisible(true);
    }
}
