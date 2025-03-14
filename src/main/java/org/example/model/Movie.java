package org.example.model;

/**
 * The Movie class represents a movie with its details.
 */
public class Movie {
    private int movieId; // Unique ID for the movie
    private String title;
    private int year;
    private String mainCast;
    private double rating;
    private String genre;
    private String description;
    private String coverImagePath; // New field for the cover image path


    /**
     * Constructor to initialize a Movie object with all the movie details.
     *
     * @param movieId     The unique ID of the movie.
     * @param title       The title of the movie.
     * @param year        The release year of the movie.
     * @param mainCast    The main cast of the movie.
     * @param rating      The rating of the movie.
     * @param genre       The genre of the movie.
     * @param description A brief description of the movie.
     */
    public Movie(int movieId, String title, int year, String mainCast, double rating, String genre, String description, String coverImagePath) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.mainCast = mainCast;
        this.rating = rating;
        this.genre = genre;
        this.description = description;
        this.coverImagePath = coverImagePath;
    }

    // Getters and Setters
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMainCast() {
        return mainCast;
    }

    public void setMainCast(String mainCast) {
        this.mainCast = mainCast;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }
}
