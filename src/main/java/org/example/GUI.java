package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI {

    private MovieManager movieManager; // Manages movie data
    private boolean isAdmin = false; // Tracks if the user is an admin or not

    public static void main(String[] args) {
        // Start with the login screen
        LoginGUI login = new LoginGUI();
        boolean isAdmin = login.isAdmin(); // Get role from login

        // Launch the main application
        SwingUtilities.invokeLater(() -> new GUI(isAdmin));
    }

    public GUI(boolean isAdmin) {
        this.isAdmin = isAdmin;
        movieManager = new MovieManager();
        movieManager.readMovies(); // Load movies from CSV file

        JFrame frame = new JFrame("Movie Mania - " + (isAdmin ? "Admin Catalogue" : "User Catalogue"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the window is closed
        frame.setSize(400, 300); // Set window size
        frame.setLayout(new BorderLayout()); // Use BorderLayout for the main layout

        // Create and add the title label to the top of the window
        JLabel lblTitle = new JLabel("Movie Mania", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24)); // Set font and size
        frame.add(lblTitle, BorderLayout.NORTH); // Add label to the top (NORTH) of the window

        // Create a panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Check if the user is an admin or a regular user
        if (isAdmin) {
            // Admin Catalogue Buttons
            JButton btnAddMovie = new JButton("Add Movie");
            JButton btnDeleteMovie = new JButton("Delete Movie");
            JButton btnUpdateMovie = new JButton("Update Movie");
            JButton btnViewMovies = new JButton("View Movies");
            JButton btnSignOut = new JButton("Logout");

            // Add buttons to the panel
            buttonPanel.add(btnAddMovie);
            buttonPanel.add(btnDeleteMovie);
            buttonPanel.add(btnUpdateMovie);
            buttonPanel.add(btnViewMovies);
            buttonPanel.add(btnSignOut);

            // Add Movie Functionality
            btnAddMovie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { showAddMovieScreen(); }
            });

            // Delete Movie Functionality
            btnDeleteMovie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Prompt the admin to enter the movie title
                    String movieTitle = JOptionPane.showInputDialog(frame, "Enter the title of the movie to delete:");
                    if (movieTitle != null && !movieTitle.trim().isEmpty()) {
                        boolean isDeleted = deleteMovieByTitle(movieTitle.trim()); // Delete the movie
                        if (isDeleted) {
                            JOptionPane.showMessageDialog(frame, "Movie deleted successfully!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Movie not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "No movies to be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Update Movie Button
            btnUpdateMovie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showUpdateMovieScreen();
                }
            });

            // View Movies Functionality
            btnViewMovies.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { showMovieTitlesScreen(); }
            });

            // Sign Out Functionality
            btnSignOut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose(); // Close the current window
                    System.exit(0); // Completely terminate the program
                }
            });
        } else if(!isAdmin){
            // User Catalogue Buttons
            JButton btnViewMovies = new JButton("View Movies");
            JButton btnViewFavorites = new JButton("View Favorites");
            JButton btnWatchHistory = new JButton("Watch History");
            JButton btnLogout = new JButton("Logout");

            // Add buttons to the panel
            buttonPanel.add(btnViewMovies);
            buttonPanel.add(btnViewFavorites);
            buttonPanel.add(btnWatchHistory);
            buttonPanel.add(btnLogout);

            // View Movies Functionality
            btnViewMovies.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { showMovieTitlesScreen(); }
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
                    frame.dispose(); // Close the current window
                    System.exit(0);// leaves the program when you sign out
                }
            });

        }

        // Add the button panel to the center of the window
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Method to delete a movie by title
    private boolean deleteMovieByTitle(String title) {
        List<Movie> movies = movieManager.getMovies(); // Get the list of movies
        // Search for the movie by title
        for (Movie movie : movies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                // Remove the movie from the list
                movies.remove(movie);
                // Save the updated list to the CSV file
                movieManager.saveMovies();
                return true; // Movie found and deleted
            }
        }
        return false; // Movie not found
    }

    // Method to show the screen for adding a new movie
    private void showAddMovieScreen() {
        JFrame addMovieFrame = new JFrame("Add Movie");
        addMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addMovieFrame.setSize(400, 400); // Set window size
        addMovieFrame.setLayout(new GridLayout(7, 2, 10, 10));

        // Create text fields and text area for movie details
        JTextField txtTitle = new JTextField();
        JTextField txtYear = new JTextField();
        JTextField txtMainCast = new JTextField();
        JTextField txtRating = new JTextField();
        JTextField txtGenre = new JTextField();
        JTextArea txtDescription = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtDescription);

        // Add labels and text fields to the window
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

        // Add button to submit the new movie
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get input values from text fields
                    String title = txtTitle.getText();
                    int year = Integer.parseInt(txtYear.getText());
                    String mainCast = txtMainCast.getText();
                    double rating = Double.parseDouble(txtRating.getText());
                    String genre = txtGenre.getText();
                    String description = txtDescription.getText();

                    // Create a new Movie object and add it to the list
                    Movie newMovie = new Movie(title, year, mainCast, rating, genre, description);
                    movieManager.getMovies().add(newMovie);
                    movieManager.saveMovies(); // Save the updated list to CSV

                    // Show success message and close the add movie window
                    JOptionPane.showMessageDialog(addMovieFrame, "Movie added successfully!");
                    addMovieFrame.dispose();
                } catch (NumberFormatException ex) {
                    // Show error message if input is invalid
                    JOptionPane.showMessageDialog(addMovieFrame, "Invalid input. Please check the fields.");
                }
            }
        });

        addMovieFrame.add(btnAdd); // Add the button to the window
        addMovieFrame.setVisible(true);
    }

    private void showUpdateMovieScreen() {
        JFrame updateMovieFrame = new JFrame("Update Movie");
        updateMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateMovieFrame.setSize(400, 400);
        updateMovieFrame.setLayout(new GridLayout(3, 2, 10, 10));

        // Input field to search for the movie
        JTextField txtSearchTitle = new JTextField();
        JButton btnSearch = new JButton("Search");

        // Take input from user for the movie they want to update
        updateMovieFrame.add(new JLabel("Enter Title to Update:"));
        updateMovieFrame.add(txtSearchTitle);
        updateMovieFrame.add(btnSearch);

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTitle = txtSearchTitle.getText().trim();

                // Check is the user input field is empty
                if (searchTitle.isEmpty()) {
                    JOptionPane.showMessageDialog(updateMovieFrame, "Please enter a title.");
                    return;
                }

                Movie movieToUpdate = null;

                // Look for the movie in the list
                for (Movie movie : movieManager.getMovies()) {
                    if (movie.getTitle().equalsIgnoreCase(searchTitle)) {
                        movieToUpdate = movie;
                        break;
                    }
                }

                if (movieToUpdate == null) {
                    JOptionPane.showMessageDialog(updateMovieFrame, "Movie not found!");
                    return;
                }

                // Close the search frame and open the update screen
                updateMovieFrame.dispose();
                showEditMovieScreen(movieToUpdate);
            }
        });

        updateMovieFrame.setVisible(true);
    }

    private void showEditMovieScreen(Movie movieToUpdate) {
        JFrame editMovieFrame = new JFrame("Edit Movie - " + movieToUpdate.getTitle());
        editMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editMovieFrame.setSize(400, 500);
        editMovieFrame.setLayout(new GridLayout(8, 2, 10, 10));

        // Pre-filled input fields with existing data
        JTextField txtTitle = new JTextField(movieToUpdate.getTitle());
        JTextField txtYear = new JTextField(String.valueOf(movieToUpdate.getYear()));
        JTextField txtMainCast = new JTextField(movieToUpdate.getMainCast());
        JTextField txtRating = new JTextField(String.valueOf(movieToUpdate.getRating()));
        JTextField txtGenre = new JTextField(movieToUpdate.getGenre());
        JTextArea txtDescription = new JTextArea(movieToUpdate.getDescription());
        JScrollPane scrollPane = new JScrollPane(txtDescription);

        // Add labels and text fields
        editMovieFrame.add(new JLabel("Title:"));
        editMovieFrame.add(txtTitle);
        editMovieFrame.add(new JLabel("Year:"));
        editMovieFrame.add(txtYear);
        editMovieFrame.add(new JLabel("Main Cast:"));
        editMovieFrame.add(txtMainCast);
        editMovieFrame.add(new JLabel("Rating:"));
        editMovieFrame.add(txtRating);
        editMovieFrame.add(new JLabel("Genre:"));
        editMovieFrame.add(txtGenre);
        editMovieFrame.add(new JLabel("Description:"));
        editMovieFrame.add(scrollPane);

        // Button to save changes
        JButton btnSave = new JButton("Save Changes");
        btnSave.addActionListener(e -> {
            try {
                // Get new values from the input fields
                String newTitle = txtTitle.getText().trim();
                int newYear = Integer.parseInt(txtYear.getText().trim());
                String newMainCast = txtMainCast.getText().trim();
                double newRating = Double.parseDouble(txtRating.getText().trim());
                String newGenre = txtGenre.getText().trim();
                String newDescription = txtDescription.getText().trim();

                // Update the movie object using setters
                movieToUpdate.setTitle(newTitle);
                movieToUpdate.setYear(newYear);
                movieToUpdate.setMainCast(newMainCast);
                movieToUpdate.setRating(newRating);
                movieToUpdate.setGenre(newGenre);
                movieToUpdate.setDescription(newDescription);

                // Save the updated movie list to CSV
                movieManager.saveMovies();

                JOptionPane.showMessageDialog(editMovieFrame, "Movie updated successfully!");
                editMovieFrame.dispose(); // Close the edit movie screen
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editMovieFrame, "Invalid input. Please check the fields.");
            }
        });

        editMovieFrame.add(new JLabel());
        editMovieFrame.add(btnSave);

        editMovieFrame.setVisible(true);
    }



    // Method to show the screen with a list of movie titles
    private void showMovieTitlesScreen() {
        JFrame movieTitlesFrame = new JFrame("Movie Titles");
        movieTitlesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieTitlesFrame.setSize(600, 400);
        movieTitlesFrame.setLayout(new BorderLayout());

        // Create and add the title label to the top of the window
        JLabel lblMovieTitles = new JLabel("Movies", JLabel.CENTER);
        lblMovieTitles.setFont(new Font("Arial", Font.BOLD, 18)); // Set font and size
        movieTitlesFrame.add(lblMovieTitles, BorderLayout.NORTH); // Add label to the top (NORTH) of the window

        // Create a panel to display the list of movies
        JPanel movieListPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        movieListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add a scroll pane to the movie list panel
        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        movieTitlesFrame.add(scrollPane, BorderLayout.CENTER);

        // Get the list of movies from the MovieManager
        List<Movie> movies = movieManager.getMovies();
        if (movies.isEmpty()) {
            // Show message if no movies are found
            JOptionPane.showMessageDialog(movieTitlesFrame, "No movies found!");
        } else {
            // Create a button for each movie and add it to the panel
            for (Movie movie : movies) {
                JButton btnMovie = new JButton("<html><center>" + movie.getTitle() + "</center></html>");
                btnMovie.setPreferredSize(new Dimension(100, 180)); // Set button size
                btnMovie.setFont(new Font("Arial", Font.BOLD, 15)); // Set font and size
                btnMovie.setVerticalTextPosition(SwingConstants.BOTTOM); // Position text at the bottom
                btnMovie.setHorizontalTextPosition(SwingConstants.CENTER); // Center the text
                btnMovie.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showMovieDetailsScreen(movie); // Show details of the selected movie
                        movieTitlesFrame.dispose();
                    }
                });
                movieListPanel.add(btnMovie);
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
        movieTitlesFrame.add(btnBack, BorderLayout.SOUTH); // Add button to the bottom (SOUTH) of the window

        movieTitlesFrame.setVisible(true);
    }

    // Method to show the screen with details of a selected movie
    private void showMovieDetailsScreen(Movie movie) {
        JFrame movieDetailsFrame = new JFrame(movie.getTitle());
        movieDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieDetailsFrame.setSize(500, 400);
        movieDetailsFrame.setLayout(new BorderLayout());

        // Create and add the title label to the top of the window
        JLabel lblMovieDetails = new JLabel(movie.getTitle(), JLabel.CENTER);
        lblMovieDetails.setFont(new Font("Arial", Font.BOLD, 24)); // Set font and size
        movieDetailsFrame.add(lblMovieDetails, BorderLayout.NORTH); // Add label to the top (NORTH) of the window

        // Create a text area to display movie details
        JTextArea txtMovieDetails = new JTextArea();
        txtMovieDetails.setEditable(false); // Make the text area read-only
        txtMovieDetails.setLineWrap(true); // Enable line wrapping
        txtMovieDetails.setWrapStyleWord(true); // Wrap at word boundaries
        txtMovieDetails.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font and size
        txtMovieDetails.setText(
                "Year: " + movie.getYear() + "\n" +
                        "Main Cast: " + movie.getMainCast() + "\n" +
                        "Rating: " + movie.getRating() + "\n" +
                        "Genre: " + movie.getGenre() + "\n" +
                        "Description: " + movie.getDescription()
        );

        // Add a scroll pane to the text area
        JScrollPane scrollPane = new JScrollPane(txtMovieDetails);
        movieDetailsFrame.add(scrollPane, BorderLayout.CENTER);

        // Add a back button to return to the movie titles screen
        JButton btnBack = new JButton("Back to Movie Titles");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movieDetailsFrame.dispose(); // Close the movie details window
                showMovieTitlesScreen();
            }
        });
        movieDetailsFrame.add(btnBack, BorderLayout.SOUTH); // Add button to the bottom (SOUTH) of the window

        movieDetailsFrame.setVisible(true);
    }
}
