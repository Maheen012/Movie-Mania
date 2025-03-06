package org.example;

public class Movie {
    private String title;
    private int year;
    private String mainCast;
    private double rating;
    private String genre;
    private String description;

    public Movie(String title, int year, String mainCast, double rating, String genre, String description) {
        this.title = title;
        this.year = year;
        this.mainCast = mainCast;
        this.rating = rating;
        this.genre = genre;
        this.description = description;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMainCast(String mainCast) {
        this.mainCast = mainCast;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

