package com.example.android.moviesudacity.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.moviesudacity";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    private MoviesContract(){}

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);
        public final static String TABLE_NAME = "movies";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_MOVIE_ID ="id";
    }
}
