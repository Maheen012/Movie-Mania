package org.example.view;

import org.example.model.Movie;
import org.example.controller.MovieManager;
import org.example.controller.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * AdminGUI class represents the graphical user interface for the admin functionality of the movie management system.
 * It allows admins to add, delete, update, and view movies, as well as log out of the system.
 */
public class AdminGUI {
    private MovieManager movieManager;
    private UserManager userManager;

    /**
     * Constructor for initializing the AdminGUI.
     *
     * @param movieManager The MovieManager instance to manage movie operations.
     */
    public AdminGUI(MovieManager movieManager, UserManager userManager) {
        this.movieManager = movieManager;
        this.userManager = userManager;
        initializeUI();
    }

    /**
     * Initializes the graphical user interface for the admin window.
     * It sets up the window, title, buttons, and their corresponding action listeners.
     */
    private void initializeUI() {
        // Create frame for admin catalogue window
        JFrame frame = new JFrame("Movie Mania - Admin Catalogue");
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // Create and add the title label to the top of the window
        JLabel lblTitle = new JLabel("Movie Mania", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        // Create a panel to hold the buttons for admin actions
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));

        // Create buttons for different admin actions
        JButton btnAddMovie = new JButton("Add Movie");
        JButton btnDeleteMovie = new JButton("Delete Movie");
        JButton btnUpdateMovie = new JButton("Update Movie");
        JButton btnViewMovies = new JButton("View Movies");
        JButton btnLogout = new JButton("Logout");

        // Set up action listeners for each button
        btnAddMovie.addActionListener(e -> showAddMovieScreen());
        btnDeleteMovie.addActionListener(e -> showDeleteMovieScreen());
        btnUpdateMovie.addActionListener(e -> showUpdateMovieScreen());
        btnViewMovies.addActionListener(e -> new MovieViewer(movieManager, userManager).showMovieTitlesScreen());
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current window
                System.exit(0); // Terminate the program
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(btnAddMovie);
        buttonPanel.add(btnDeleteMovie);
        buttonPanel.add(btnUpdateMovie);
        buttonPanel.add(btnViewMovies);
        buttonPanel.add(btnLogout);

        // Add button panel to the center of the window
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Displays the screen to add a new movie.
     * It includes input fields for movie details like title, year, cast, rating, genre, and description.
     */
    private void showAddMovieScreen() {
        // Create the add movie frame
        JFrame addMovieFrame = new JFrame("Add Movie");
        addMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addMovieFrame.setSize(1200, 800); // Set window size
        addMovieFrame.setLayout(new GridLayout(8, 2, 10, 10)); // Added an extra row for movie ID

        // Create input fields for movie details
        JTextField txtMovieId = new JTextField();
        JTextField txtTitle = new JTextField();
        JTextField txtYear = new JTextField();
        JTextField txtMainCast = new JTextField();
        JTextField txtRating = new JTextField();
        JTextField txtGenre = new JTextField();
        JTextArea txtDescription = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtDescription);

        // Add labels and input fields to the window
        addMovieFrame.add(new JLabel("Movie ID:"));
        addMovieFrame.add(txtMovieId);
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

        // Add action listener for the add movie button
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = txtTitle.getText().trim();
                String mainCast = txtMainCast.getText().trim();
                String genre = txtGenre.getText().trim();
                String description = txtDescription.getText().trim();

                // Validate that all fields are filled
                if (title.isEmpty() || mainCast.isEmpty() || genre.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(addMovieFrame, "Please fill in all fields.");
                    return;
                }

                try {
                    // Parse movie ID, year, and rating values
                    int movieId = Integer.parseInt(txtMovieId.getText().trim());
                    int year = Integer.parseInt(txtYear.getText().trim());
                    double rating = Double.parseDouble(txtRating.getText().trim());

                    // Validate year and rating values
                    if (year < 1900 || year > 2100) {
                        JOptionPane.showMessageDialog(addMovieFrame, "Please enter a valid year.");
                        return;
                    }
                    if (rating < 0 || rating > 10) {
                        JOptionPane.showMessageDialog(addMovieFrame, "Please enter a valid rating between 0 and 10.");
                        return;
                    }

                    // Check if the movie ID already exists
                    if (movieManager.getMovieById(movieId) != null) {
                        JOptionPane.showMessageDialog(addMovieFrame, "Movie ID already exists. Please use a unique ID.");
                        return;
                    }

                    // Create a new Movie object and add it to the movie list
                    Movie newMovie = new Movie(movieId, title, year, mainCast, rating, genre, description, "resources/images");
                    movieManager.getMovies().add(newMovie);
                    movieManager.saveMovies(); // Save the updated movie list to CSV

                    // Show success message and close the add movie window
                    JOptionPane.showMessageDialog(addMovieFrame, "Movie added successfully!");
                    addMovieFrame.dispose(); // Close the add movie window
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addMovieFrame, "Invalid input. Please check the fields.");
                }
            }
        });

        addMovieFrame.add(btnAdd); // Add the button to the window
        addMovieFrame.setVisible(true); // Make the window visible
    }

    /**
     * Deletes a movie by its ID.
     *
     * @param movieId The ID of the movie to be deleted.
     * @return true if the movie was found and deleted, false otherwise.
     */
    private boolean deleteMovieById(int movieId) {
        List<Movie> movies = movieManager.getMovies(); // Get list of movies
        // Iterate through the list to find the movie by ID
        for (Movie movie : movies) {
            if (movie.getMovieId() == movieId) {
                // Remove the movie from the list
                movies.remove(movie);
                movieManager.saveMovies(); // Save the updated list to CSV
                return true; // Movie found and deleted
            }
        }
        return false; // Movie not found
    }

    /**
     * Displays the screen to delete a movie by its ID.
     */
    private void showDeleteMovieScreen() {
        // Create the delete movie frame
        JFrame deleteMovieFrame = new JFrame("Delete Movie");
        deleteMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteMovieFrame.setSize(1200, 800);
        deleteMovieFrame.setLayout(new GridLayout(2, 2, 10, 10));

        // Create input field for movie ID
        JTextField txtMovieId = new JTextField();
        deleteMovieFrame.add(new JLabel("Enter Movie ID to Delete:"));
        deleteMovieFrame.add(txtMovieId);

        // Add delete button and action listener
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int movieId = Integer.parseInt(txtMovieId.getText().trim());
                    boolean deleted = deleteMovieById(movieId);
                    if (deleted) {
                        JOptionPane.showMessageDialog(deleteMovieFrame, "Movie deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(deleteMovieFrame, "Movie not found.");
                    }
                    deleteMovieFrame.dispose(); // Close the delete movie window
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(deleteMovieFrame, "Invalid movie ID. Please enter a valid number.");
                }
            }
        });

        deleteMovieFrame.add(btnDelete); // Add the button to the window
        deleteMovieFrame.setVisible(true); // Make the window visible
    }

    /**
     * Displays the screen to update a movie by its ID.
     */
    private void showUpdateMovieScreen() {
        // Create the update movie frame
        JFrame updateMovieFrame = new JFrame("Update Movie");
        updateMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateMovieFrame.setSize(1200, 800);
        updateMovieFrame.setLayout(new GridLayout(2, 2, 10, 10));

        // Create search field to find movie by ID
        JTextField txtMovieId = new JTextField();
        JButton btnSearch = new JButton("Search");

        // Add search label and input field
        updateMovieFrame.add(new JLabel("Enter Movie ID to Update:"));
        updateMovieFrame.add(txtMovieId);
        updateMovieFrame.add(btnSearch);

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int movieId = Integer.parseInt(txtMovieId.getText().trim());
                    Movie movieToUpdate = movieManager.getMovieById(movieId);

                    // If movie is found, proceed to editing screen
                    if (movieToUpdate == null) {
                        JOptionPane.showMessageDialog(updateMovieFrame, "Movie not found!");
                        return;
                    }

                    // Close search window and open the edit window
                    updateMovieFrame.dispose();
                    showEditMovieScreen(movieToUpdate);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(updateMovieFrame, "Invalid movie ID. Please enter a valid number.");
                }
            }
        });

        updateMovieFrame.setVisible(true); // Make the window visible
    }

    /**
     * Displays the screen to edit the details of a movie.
     *
     * @param movieToUpdate The movie to be edited.
     */
    private void showEditMovieScreen(Movie movieToUpdate) {
        // Create the edit movie frame
        JFrame editMovieFrame = new JFrame("Edit Movie - " + movieToUpdate.getTitle());
        editMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editMovieFrame.setSize(1200, 800);
        editMovieFrame.setLayout(new GridLayout(8, 2, 10, 10));

        // Pre-fill input fields with existing movie data
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

        // Button to save changes to the movie
        JButton btnSave = new JButton("Save Changes");
        btnSave.addActionListener(e -> {
            try {
                // Get new values from input fields
                String newTitle = txtTitle.getText().trim();
                int newYear = Integer.parseInt(txtYear.getText().trim());
                String newMainCast = txtMainCast.getText().trim();
                double newRating = Double.parseDouble(txtRating.getText().trim());
                String newGenre = txtGenre.getText().trim();
                String newDescription = txtDescription.getText().trim();

                // Update movie object with new details
                movieToUpdate.setTitle(newTitle);
                movieToUpdate.setYear(newYear);
                movieToUpdate.setMainCast(newMainCast);
                movieToUpdate.setRating(newRating);
                movieToUpdate.setGenre(newGenre);
                movieToUpdate.setDescription(newDescription);

                // Save the updated list to CSV
                movieManager.saveMovies();

                JOptionPane.showMessageDialog(editMovieFrame, "Movie updated successfully!");
                editMovieFrame.dispose(); // Close the edit window
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editMovieFrame, "Invalid input. Please check the fields.");
            }
        });

        // Add save button to the window
        editMovieFrame.add(new JLabel());
        editMovieFrame.add(btnSave);

        editMovieFrame.setVisible(true); // Make the window visible
    }
}

