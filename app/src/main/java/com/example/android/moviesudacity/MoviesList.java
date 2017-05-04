package com.example.android.moviesudacity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MoviesList implements Parcelable{
    private boolean databaseArray;
    private List<Movie> list;

    public MoviesList(List<Movie> list, boolean databaseArray) {
        this.list = list;
        this.databaseArray = databaseArray;
    }

    public List<Movie> getList() {
        return list;
    }

    public boolean isDatabaseArray() {
        return databaseArray;
    }

    public void setDatabaseArray(boolean databaseArray) {
        this.databaseArray = databaseArray;
    }

    protected MoviesList(Parcel in) {
        list = in.createTypedArrayList(Movie.CREATOR);
        databaseArray = in.readInt() != 0;
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
        dest.writeInt(databaseArray ? 1 : 0);
    }
}


