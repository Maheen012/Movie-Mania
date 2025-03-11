package org.example.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.example.view.LoginGUI;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * UserManager handles user authentication, registration, and storing user data in a CSV file.
 */
public class UserManager {
    private static final String USER_CSV_FILE = "UserPass.csv";
    private static final String FAVORITES_CSV_FILE = "UserFavorites.csv";

    /**
     * Gets the file path for the UserPass.csv file from resources or file system.
     *
     * @return the file object representing the UserPass.csv file
     */
    private File getUserFilePath() {
        // Get the file path from resources
        URL getPathURL = getClass().getClassLoader().getResource(USER_CSV_FILE);

        // Check if UserPass.csv is in resources
        if (getPathURL == null) {
            System.out.println("UserPass file not found in resources. Using the file system.");
            return new File(USER_CSV_FILE); // Fall back to file system if not found in resources
        }

        try {
            return new File(getPathURL.toURI()); // Convert URL to File if found in resources
        } catch (URISyntaxException e) {
            throw new RuntimeException(e); // Handle URI syntax exception
        }
    }

    /**
     * Gets the path to the UserFavorites.csv file from resources or file system.
     *
     * @return the file object representing the UserFavorites.csv file
     */
    private File getFavoritesFilePath() {
        // Get the file path from resources
        URL getPathURL = getClass().getClassLoader().getResource(FAVORITES_CSV_FILE);

        // Check if UserFavorites.csv is in resources
        if (getPathURL == null) {
            System.out.println("UserFavorites file not found in resources. Using the file system.");
            return new File(FAVORITES_CSV_FILE); // Fall back to file system if not found in resources
        }

        try {
            return new File(getPathURL.toURI()); // Convert URL to File if found in resources
        } catch (URISyntaxException e) {
            throw new RuntimeException(e); // Handle URI syntax exception
        }
    }

    /**
     * Authenticates a user based on the provided username and password.
     *
     * @param username the entered username
     * @param password the entered password
     * @return true if the user is authenticated, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        File file = getUserFilePath();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] credentials;
            while ((credentials = reader.readNext()) != null) {
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true; // Authentication successful
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if a username is already taken.
     *
     * @param username the username to check
     * @return true if the username is taken, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        File file = getUserFilePath();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] credentials;
            while ((credentials = reader.readNext()) != null) {
                if (credentials.length == 2 && credentials[0].equals(username)) {
                    return true; // Username is taken
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Saves a new user's credentials (username and password) to the CSV file.
     *
     * @param username the username to save
     * @param password the password to save
     */
    public void saveUserCredentials(String username, String password) {
        File userFile = getUserFilePath();
        if (userFile == null) return;

        boolean fileExists = userFile.exists();

        if (isUsernameTaken(username)) {
            return; // Don't save if the username is already taken
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(userFile, true))) {
            if (!fileExists) {
                String[] header = {"Username", "Password"};
                writer.writeNext(header);  // Writing header if file doesn't exist
            }

            String[] userData = {username, password};
            writer.writeNext(userData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a movie to the user's favorites list or creates a new list if the user doesn't have one.
     *
     * @param movieTitle the title of the movie to add
     */
    /**
     * Adds a movie to the user's favorites list and updates the CSV file.
     *
     * @param movieTitle the title of the movie to add
     */
    public void addToFavorites(String movieTitle) {
        String username = LoginGUI.getCurrentUsername(); // Get the current logged-in user's username
        File file = getFavoritesFilePath();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> allUsers = reader.readAll();
            boolean userFound = false;

            // Iterate through all users to find the current user
            for (int i = 0; i < allUsers.size(); i++) {
                String[] userRow = allUsers.get(i);
                if (userRow[0].equals(username)) {
                    // User found, update their favorites
                    List<String> favorites = new ArrayList<>();
                    favorites.add(userRow[0]); // First element is the username

                    // Add the current favorites (if any) from the row (ignoring the username column)
                    for (int j = 1; j < userRow.length; j++) {
                        favorites.add(userRow[j]);
                    }

                    // Check if the movie is already in the favorites
                    if (favorites.contains(movieTitle)) {
                        // Show a message that the movie is already in favorites
                        JOptionPane.showMessageDialog(null, "This movie is already in your favorites!");
                        return; // Exit the method
                    } else {
                        favorites.add(movieTitle); // Add the movie to the list
                    }

                    // Update the user's row in the allUsers list
                    allUsers.set(i, favorites.toArray(new String[0]));
                    userFound = true;
                    break;
                }
            }

            // If the user was not found, create a new row for them
            if (!userFound) {
                List<String> favorites = new ArrayList<>();
                favorites.add(username); // Add the username
                favorites.add(movieTitle); // Add the movie title
                allUsers.add(favorites.toArray(new String[0]));
            }

            // Write the updated list of all users back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                writer.writeAll(allUsers); // Write all rows back to the CSV
            } catch (IOException e) {
                e.getMessage();
            }

        } catch (IOException | CsvException e) {
            e.getMessage();
        }
    }

    /**
     * Retrieves the list of favorite movies for a user.
     *
     * @param username the username of the user
     * @return a list of movie titles
     */
    public List<String> getFavoriteMovies(String username) {
        File file = getFavoritesFilePath();
        List<String> favorites = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row[0].equals(username)) {
                    for (int i = 1; i < row.length; i++) {
                        favorites.add(row[i]); // Add movie titles to favorites list
                    }
                    return favorites;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return favorites; // Return empty list if no favorites found
    }

}


