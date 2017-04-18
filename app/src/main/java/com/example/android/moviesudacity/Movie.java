package com.example.android.moviesudacity;

public class Movie {
    private String title;
    private String synopsis;
    private double rating;
    private String release;
    private String posterUrl;

    public Movie(String title, String synopsis, double rating, String release, String posterUrl) {
        this.title = title;
        this.synopsis = synopsis;
        this.rating = rating;
        this.release = release;
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getRating() {
        return rating;
    }

    public String getRelease() {
        return release;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
}
