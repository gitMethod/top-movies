package com.example.android.moviesudacity;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable{
    private String key;
    private String name;
    private String size;

    public MovieTrailer(String key, String name, String size) {
        this.key = key;
        this.name = name;
        this.size = size;
    }

    protected MovieTrailer(Parcel in) {
        key = in.readString();
        name = in.readString();
        size = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(size);
    }
}
