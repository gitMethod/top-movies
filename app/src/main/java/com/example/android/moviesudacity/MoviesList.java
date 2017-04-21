package com.example.android.moviesudacity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MoviesList implements Parcelable{
    private List<Movie> list;

    public MoviesList(List<Movie> list) {
        this.list = list;
    }

    public List<Movie> getList() {
        return list;
    }

    public List<Movie> sortByPopularity(){
        Collections.sort(list, new Comparator<Movie>() {
            @Override
            public int compare(Movie c1, Movie c2) {
                return Double.compare(c2.getPopularity(), c1.getPopularity());
            }
        });
        return list;
    }

    public List<Movie> sortByRating(){
        Collections.sort(list, new Comparator<Movie>() {
            @Override
            public int compare(Movie c1, Movie c2) {
                return Double.compare(c2.getRating(), c1.getRating());
            }
        });
        return list;
    }

    protected MoviesList(Parcel in) {
        list = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Creator<MoviesList> CREATOR = new Creator<MoviesList>() {
        @Override
        public MoviesList createFromParcel(Parcel in) {
            return new MoviesList(in);
        }

        @Override
        public MoviesList[] newArray(int size) {
            return new MoviesList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
    }
}


