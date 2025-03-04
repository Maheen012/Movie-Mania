package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MovieManager {

    // Variables for each column in the CSV file
    public String title;
    public String year;
    public String mainCast;
    public String rating;
    public String genre;
    public String description;

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
                // Map CSV columns to variables
                title = nextLine[0];
                year = nextLine[1];
                mainCast = nextLine[2];
                rating = nextLine[3];
                genre = nextLine[4];
                description = nextLine[5];

                // Print the movie details
                printMovieDetails();
            }

            // Close the reader
            reader.close();

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to print movie details
    private void printMovieDetails() {
        System.out.println("Title: " + title
                + ", Year: " + year
                + ", Main Cast: " + mainCast
                + ", Rating: " + rating
                + ", Genre: " + genre
                + ", Description: " + description);
    }

    public static void main(String[] args) {
        String filePath = "movies.csv"; // Path to your CSV file
        MovieManager movieManager = new MovieManager();
        movieManager.readMovies(filePath);
    }
}
