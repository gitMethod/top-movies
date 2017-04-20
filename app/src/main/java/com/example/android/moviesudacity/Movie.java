package com.example.android.moviesudacity;

import android.graphics.Bitmap;

public class Movie {
    private String title;
    private String synopsis;
    private double rating;
    private String release;
    private Bitmap bitmapImg;

    public Movie(String title, String synopsis, double rating, String release, Bitmap bitmapImg) {
        this.title = title;
        this.synopsis = synopsis;
        this.rating = rating;
        this.release = release;
        this.bitmapImg = bitmapImg;
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

    public Bitmap getBitmapImg() {
        return bitmapImg;
    }
}
