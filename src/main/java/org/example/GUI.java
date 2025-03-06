package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI {

    private MovieManager movieManager;
    private boolean isAdmin;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI(true)); // Start with admin view
    }

    public GUI(boolean isAdmin) {
        this.isAdmin = isAdmin;
        movieManager = new MovieManager();
        movieManager.readMovies("movies.csv");

        JFrame frame = new JFrame("Movie Mania - " + (isAdmin ? "Admin Catalogue" : "User Catalogue"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Movie Mania", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        if (isAdmin) {
            // Admin Catalogue Buttons
            JButton btnAddMovie = new JButton("Add Movie");
            JButton btnDeleteMovie = new JButton("Delete Movie");
            JButton btnUpdateMovie = new JButton("Update Movie");
            JButton btnSignOut = new JButton("Sign Out");

            buttonPanel.add(btnAddMovie);
            buttonPanel.add(btnDeleteMovie);
            buttonPanel.add(btnUpdateMovie);
            buttonPanel.add(btnSignOut);

            // Add Movie Functionality
            btnAddMovie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAddMovieScreen();
                }
            });

            // Delete Movie Button (Unimplemented)
            btnDeleteMovie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Placeholder for future implementation
                    
                }
            });

            // Update Movie Button (Unimplemented)
            btnUpdateMovie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Placeholder for future implementation
                    
                }
            });

            // Sign Out Functionality
            btnSignOut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    new GUI(false); // Switch to user catalogue
                }
            });
        } else {
            // User Catalogue Buttons
            JButton btnViewMovies = new JButton("View Movies");
            JButton btnViewFavorites = new JButton("View Favorites");
            JButton btnWatchHistory = new JButton("Watch History");
            JButton btnLogout = new JButton("Logout");

            buttonPanel.add(btnViewMovies);
            buttonPanel.add(btnViewFavorites);
            buttonPanel.add(btnWatchHistory);
            buttonPanel.add(btnLogout);

            // View Movies Functionality
            btnViewMovies.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMovieTitlesScreen();
                }
            });

            // View Favorites Button (Unimplemented)
            btnViewFavorites.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Placeholder for future implementation
                    
                }
            });

            // Watch History Button (Unimplemented)
            btnWatchHistory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Placeholder for future implementation
                    
                }
            });

            // Logout Functionality
            btnLogout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    new GUI(true); // Switch to admin catalogue
                }
            });
        }

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void showAddMovieScreen() {
        JFrame addMovieFrame = new JFrame("Add Movie");
        addMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addMovieFrame.setSize(400, 400);
        addMovieFrame.setLayout(new GridLayout(7, 2, 10, 10));

        JTextField txtTitle = new JTextField();
        JTextField txtYear = new JTextField();
        JTextField txtMainCast = new JTextField();
        JTextField txtRating = new JTextField();
        JTextField txtGenre = new JTextField();
        JTextArea txtDescription = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtDescription);

        addMovieFrame.add(new JLabel("Title:"));
        addMovieFrame.add(txtTitle);
        addMovieFrame.add(new JLabel("Year:"));
        addMovieFrame.add(txtYear);
        addMovieFrame.add(new JLabel("Main Cast:"));
        addMovieFrame.add(txtMainCast);
        addMovieFrame.add(new JLabel("Rating:"));
        addMovieFrame.add(txtRating);
        addMovieFrame.add(new JLabel("Genre:"));
        addMovieFrame.add(txtGenre);
        addMovieFrame.add(new JLabel("Description:"));
        addMovieFrame.add(scrollPane);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = txtTitle.getText();
                    int year = Integer.parseInt(txtYear.getText());
                    String mainCast = txtMainCast.getText();
                    double rating = Double.parseDouble(txtRating.getText());
                    String genre = txtGenre.getText();
                    String description = txtDescription.getText();

                    Movie newMovie = new Movie(title, year, mainCast, rating, genre, description);
                    movieManager.getMovies().add(newMovie);
                    movieManager.saveMovies("movies.csv");

                    JOptionPane.showMessageDialog(addMovieFrame, "Movie added successfully!");
                    addMovieFrame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addMovieFrame, "Invalid input. Please check the fields.");
                }
            }
        });

        addMovieFrame.add(btnAdd);
        addMovieFrame.setVisible(true);
    }

    private void showMovieTitlesScreen() {
        JFrame movieTitlesFrame = new JFrame("Movie Titles");
        movieTitlesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieTitlesFrame.setSize(600, 400);
        movieTitlesFrame.setLayout(new BorderLayout());

        JLabel lblMovieTitles = new JLabel("Movies", JLabel.CENTER);
        lblMovieTitles.setFont(new Font("Arial", Font.BOLD, 18));
        movieTitlesFrame.add(lblMovieTitles, BorderLayout.NORTH);

        JPanel movieListPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        movieListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        movieTitlesFrame.add(scrollPane, BorderLayout.CENTER);

        List<Movie> movies = movieManager.getMovies();
        if (movies.isEmpty()) {
            JOptionPane.showMessageDialog(movieTitlesFrame, "No movies found!");
        } else {
            for (Movie movie : movies) {
                JButton btnMovie = new JButton("<html><center>" + movie.getTitle() + "</center></html>");
                btnMovie.setPreferredSize(new Dimension(100, 180));
                btnMovie.setFont(new Font("Arial", Font.BOLD, 15));
                btnMovie.setVerticalTextPosition(SwingConstants.BOTTOM);
                btnMovie.setHorizontalTextPosition(SwingConstants.CENTER);
                btnMovie.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showMovieDetailsScreen(movie);
                        movieTitlesFrame.dispose();
                    }
                });
                movieListPanel.add(btnMovie);
            }
        }

        JButton btnBack = new JButton("Back to Catalogue");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movieTitlesFrame.dispose();
            }
        });
        movieTitlesFrame.add(btnBack, BorderLayout.SOUTH);

        movieTitlesFrame.setVisible(true);
    }

    private void showMovieDetailsScreen(Movie movie) {
        JFrame movieDetailsFrame = new JFrame(movie.getTitle());
        movieDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieDetailsFrame.setSize(500, 400);
        movieDetailsFrame.setLayout(new BorderLayout());

        JLabel lblMovieDetails = new JLabel(movie.getTitle(), JLabel.CENTER);
        lblMovieDetails.setFont(new Font("Arial", Font.BOLD, 24));
        movieDetailsFrame.add(lblMovieDetails, BorderLayout.NORTH);

        JTextArea txtMovieDetails = new JTextArea();
        txtMovieDetails.setEditable(false);
        txtMovieDetails.setLineWrap(true);
        txtMovieDetails.setWrapStyleWord(true);
        txtMovieDetails.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMovieDetails.setText(
                "Year: " + movie.getYear() + "\n" +
                        "Main Cast: " + movie.getMainCast() + "\n" +
                        "Rating: " + movie.getRating() + "\n" +
                        "Genre: " + movie.getGenre() + "\n" +
                        "Description: " + movie.getDescription()
        );

        JScrollPane scrollPane = new JScrollPane(txtMovieDetails);
        movieDetailsFrame.add(scrollPane, BorderLayout.CENTER);

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
}
