package com.example.android.moviesudacity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {
    private String title;
    private String synopsis;
    private double rating;
    private String release;
    private String imgUrl;
    private String backDrop;
    private String id;

    private ArrayList<MovieReview> reviews = new ArrayList<MovieReview>();
    private ArrayList<MovieTrailer> trailers = new ArrayList<MovieTrailer>();

    public Movie(String title, String synopsis, double rating, String release, String imgUrl, String backDrop, String id) {
        this.title = title;
        this.synopsis = synopsis;
        this.rating = rating;
        this.release = release;
        this.imgUrl = imgUrl;
        this.backDrop = backDrop;
        this.id = id;
        //this.reviews = new ArrayList<MovieReview>();
        //this.trailers = new ArrayList<MovieTrailer>();
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

    public String getImgUrl() {
        return imgUrl;
    }

    public String getBackDrop() {
        return backDrop;
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
        dest.writeString(synopsis);
        dest.writeDouble(rating);
        dest.writeString(release);
        dest.writeString(imgUrl);
        dest.writeString(backDrop);
        dest.writeString(id);
        dest.writeTypedList(reviews);
        dest.writeTypedList(trailers);
    }

    protected Movie(Parcel in) {
        title = in.readString();
        synopsis = in.readString();
        rating = in.readDouble();
        release = in.readString();
        imgUrl = in.readString();
        backDrop = in.readString();
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
