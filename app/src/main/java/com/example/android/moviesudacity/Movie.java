package com.example.android.moviesudacity;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String synopsis;
    private double rating;
    private double popularity;
    private String release;
    private String imgUrl;

    public Movie(String title, String synopsis, double rating, double popularity, String release, String imgUrl) {
        this.title = title;
        this.synopsis = synopsis;
        this.rating = rating;
        this.popularity = popularity;
        this.release = release;
        this.imgUrl = imgUrl;
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

    public double getPopularity() {
        return popularity;
    }

    public String getRelease() {
        return release;
    }

    public String getImagUrl() {
        return imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeDouble(rating);
        dest.writeDouble(popularity);
        dest.writeString(release);
        dest.writeString(imgUrl);
    }

    protected Movie(Parcel in) {
        title = in.readString();
        synopsis = in.readString();
        rating = in.readDouble();
        popularity = in.readDouble();
        release = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
