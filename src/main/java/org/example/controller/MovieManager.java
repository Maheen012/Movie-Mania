package org.example.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.example.model.Movie;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * MovieManager is responsible for managing movie data.
 */
public class MovieManager {
    private List<Movie> movies; // List to store movie objects

    /**
     * Constructor to initialize the MovieManager with an empty list of movies.
     */
    public MovieManager() {
        this.movies = new ArrayList<>();
    }

    /**
     * Returns the file path of the movies.csv file.
     * The file is loaded from the classpath (i.e., from resources).
     *
     * @return The file path of the movies.csv file or null if not found.
     */
    public File getMovieFilePath() {
        // Load the movies.csv directly from the classpath
        URL getPathURL = getClass().getClassLoader().getResource("movies.csv");

        // Check if the file was found in the resources folder
        if (getPathURL == null) {
            System.out.println("Movies file not found in resources.");
            return null;
        }

        // Convert the URL to a File object
        try {
            return new File(getPathURL.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e); // Throw an exception if URI conversion fails
        }
    }

    /**
     * Reads movie data from the movies.csv file and populates the list of movies.
     */
    public void readMovies() {
        File movieFile = getMovieFilePath();
        if (movieFile == null) return; // Return if the file is not found

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(movieFile)).build()) {
            // Skip the header row
            reader.readNext();

            // Read all remaining lines from the file into a list
            List<String[]> myEntries = reader.readAll();

            // Process each line from the CSV and create Movie objects
            for (String[] nextLine : myEntries) {
                int movieId = Integer.parseInt(nextLine[0]); // Parse movie ID
                String title = nextLine[1];
                int year = Integer.parseInt(nextLine[2]); // Parse year
                String mainCast = nextLine[3];
                double rating = Double.parseDouble(nextLine[4]); // Parse rating
                String genre = nextLine[5];
                String description = nextLine[6];
                String coverImagePath = nextLine[7];

                // Create a Movie object and add it to the list
                Movie movie = new Movie(movieId, title, year, mainCast, rating, genre, description,coverImagePath);
                movies.add(movie);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace(); // Print any exceptions encountered
        }
    }

    /**
     * Saves the list of movies back to the movies.csv file.
     */
    public void saveMovies() {
        File movieFile = getMovieFilePath();
        if (movieFile == null) return; // Return if the file is not found

        try (CSVWriter writer = new CSVWriter(new FileWriter(movieFile))) {
            // Write the header row to the CSV file
            String[] header = {"Movie ID", "Title", "Year", "Main Cast", "Rating", "Genre", "Description"};
            writer.writeNext(header);

            // Write each movie's data to the CSV file
            for (Movie movie : movies) {
                String[] movieData = {
                        String.valueOf(movie.getMovieId()), // Convert movie ID to string
                        movie.getTitle(),
                        String.valueOf(movie.getYear()), // Convert year to string
                        movie.getMainCast(),
                        String.valueOf(movie.getRating()), // Convert rating to string
                        movie.getGenre(),
                        movie.getDescription()
                };

                // Write the movie data to the file
                writer.writeNext(movieData);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Print any exceptions encountered
        }
    }

    /**
     * Returns the list of movies.
     *
     * @return A list of Movie objects.
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     * Retrieves a movie by its ID.
     *
     * @param movieId The ID of the movie.
     * @return The Movie object if found, or null if not found.
     */
    public Movie getMovieById(int movieId) {
        for (Movie movie : movies) {
            if (movie.getMovieId() == movieId) {
                return movie;
            }
        }
        return null;
    }
}


