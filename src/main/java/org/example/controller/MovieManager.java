package org.example.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.example.model.Movie;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class MovieManager {
    public List<Movie> movies;

    public MovieManager() {
        this.movies = new ArrayList<>();
    }

    /**
     * Reads movie data from the movies.csv file and populates the list of movies.
     */
    public void readMovies() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("movies.csv")) {
            if (input == null) {
                System.out.println("movies.csv not found in resources.");
                return;
            }

            // Create a temp file to use with CSVReader (which expects a FileReader)
            File tempFile = File.createTempFile("movies", ".csv");
            tempFile.deleteOnExit();
            Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            try (CSVReader reader = new CSVReaderBuilder(new FileReader(tempFile)).withSkipLines(1).build()) {
                List<String[]> myEntries = reader.readAll();

                for (String[] nextLine : myEntries) {
                    int movieId = Integer.parseInt(nextLine[0]);
                    String title = nextLine[1];
                    int year = Integer.parseInt(nextLine[2]);
                    String mainCast = nextLine[3];
                    double rating = Double.parseDouble(nextLine[4]);
                    String genre = nextLine[5];
                    String description = nextLine[6];
                    String coverImagePath = nextLine[7];

                    Movie movie = new Movie(movieId, title, year, mainCast, rating, genre, description, coverImagePath);
                    movies.add(movie);
                }
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the list of movies back to the movies.csv file.
     * Only works if movies.csv is extracted or accessible in a writeable location.
     */
    public void saveMovies() {
        // Save to a file relative to working directory instead of trying to overwrite internal resource
        File movieFile = new File("movies.csv");

        try (CSVWriter writer = new CSVWriter(new FileWriter(movieFile))) {
            String[] header = {"Movie ID", "Title", "Year", "Main Cast", "Rating", "Genre", "Description", "Cover Image Path"};
            writer.writeNext(header);

            for (Movie movie : movies) {
                String[] movieData = {
                        String.valueOf(movie.getMovieId()),
                        movie.getTitle(),
                        String.valueOf(movie.getYear()),
                        movie.getMainCast(),
                        String.valueOf(movie.getRating()),
                        movie.getGenre(),
                        movie.getDescription(),
                        movie.getCoverImagePath()
                };
                writer.writeNext(movieData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Movie getMovieById(int movieId) {
        for (Movie movie : movies) {
            if (movie.getMovieId() == movieId) {
                return movie;
            }
        }
        return null;
    }

    public boolean deleteMovieById(int movieId) {
        for (Movie movie : movies) {
            if (movie.getMovieId() == movieId) {
                movies.remove(movie);
                saveMovies();
                return true;
            }
        }
        return false;
    }
}
