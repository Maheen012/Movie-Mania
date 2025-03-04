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
}
