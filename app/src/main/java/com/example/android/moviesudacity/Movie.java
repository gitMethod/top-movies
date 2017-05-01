package com.example.android.moviesudacity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {
    private String title;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
    private String id;

    private ArrayList<MovieReview> reviews = new ArrayList<MovieReview>();
    private ArrayList<MovieTrailer> trailers = new ArrayList<MovieTrailer>();

    public Movie(String title, String overview, double voteAverage, String releaseDate, String posterPath, String backdropPath, String id) {
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.id = id;
        //this.reviews = new ArrayList<MovieReview>();
        //this.trailers = new ArrayList<MovieTrailer>();
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getId() {
        return id;
    }

    public ArrayList<MovieReview> getReviews() {
        return reviews;
    }

    public ArrayList<MovieTrailer> getTrailers() {
        return trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(id);
        dest.writeTypedList(reviews);
        dest.writeTypedList(trailers);
    }

    protected Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        id = in.readString();
        in.readTypedList(reviews, MovieReview.CREATOR );
        in.readTypedList(trailers, MovieTrailer.CREATOR );
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
