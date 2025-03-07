package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieManager {
    private List<Movie> movies;

    public MovieManager() {
        this.movies = new ArrayList<>();
    }

    // Method to get the path of the movies.csv file
    public File getMovieFilePath() {
        // Load the movies.csv directly from the classpath (this is a special way of loading resource files)
        URL getPathURL = getClass().getClassLoader().getResource("movies.csv");

        // Check if movies.csv was found
        if (getPathURL == null) {
            System.out.println("Movies file not found in resources.");
            return null;
        }

        // Convert the URL to a File
        try {
            return new File(getPathURL.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to read movies from the CSV file
    public void readMovies() {
        File movieFile = getMovieFilePath();
        if (movieFile == null) return;

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(movieFile)).build()) {
            // Skip the header
            reader.readNext();

            // Read all remaining lines into a list
            List<String[]> myEntries = reader.readAll();

            // Process each line from the file
            for (String[] nextLine : myEntries) {
                // Create a Movie object from the CSV line
                String title = nextLine[0];
                int year = Integer.parseInt(nextLine[1]);
                String mainCast = nextLine[2];
                double rating = Double.parseDouble(nextLine[3]);
                String genre = nextLine[4];
                String description = nextLine[5];

                // Create a new Movie object and add it to the list
                Movie movie = new Movie(title, year, mainCast, rating, genre, description);
                movies.add(movie);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    // Method to save movies to the CSV file
    public void saveMovies() {
        File movieFile = getMovieFilePath();
        if (movieFile == null) return;

        try (CSVWriter writer = new CSVWriter(new FileWriter(movieFile))) {
            // Write the header
            String[] header = {"Title", "Year", "Main Cast", "Rating", "Genre", "Description"};
            writer.writeNext(header);

            // Write each movie's data to the file
            for (Movie movie : movies) {
                String[] movieData = {
                        movie.getTitle(),
                        String.valueOf(movie.getYear()),
                        movie.getMainCast(),
                        String.valueOf(movie.getRating()),
                        movie.getGenre(),
                        movie.getDescription()
                };

                // Write the movie data to the file
                writer.writeNext(movieData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to get the list of movies
    public List<Movie> getMovies() {
        return movies;
    }

}

