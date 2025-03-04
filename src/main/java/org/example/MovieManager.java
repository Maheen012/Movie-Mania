package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieManager {

    // List to store movies
    private List<Movie> movies = new ArrayList<>();

    // Method to read all movie data from the CSV file, skipping the header
    public void readMovies(String filePath) {
        try {
            // Create a CSVReader instance
            CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).build();

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

            // Close the reader
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to print movie details
    public void printMovieDetails() {
        for (Movie movie : movies) {
            System.out.println("Title: " + movie.getTitle()
                    + ", Year: " + movie.getYear()
                    + ", Main Cast: " + movie.getMainCast()
                    + ", Rating: " + movie.getRating()
                    + ", Genre: " + movie.getGenre()
                    + ", Description: " + movie.getDescription());
        }
    }

    // Method to save movies to the CSV file
    public void saveMovies(String filePath) {
        try {
            // Create a CSVWriter instance
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));

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

            // Close the writer
            writer.close();

            System.out.println("Movie data saved to file!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "movies.csv";
        MovieManager movieManager = new MovieManager();

        // Read movies from the file
        movieManager.readMovies(filePath);

        // Example: Create a new movie object and add it to the list
        //Movie newMovie = new Movie("New Movie", 2025, "Actor 1, Actor 2", 9.5, "Action", "An exciting new action movie.");
        //movieManager.movies.add(newMovie); // Add the new movie to the list

        // Save all movies (including the new one) to the file
        //movieManager.saveMovies(filePath);

        // Print movie details after saving the movies
        //movieManager.printMovieDetails();
    }
}
