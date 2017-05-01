package com.example.android.moviesudacity.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.moviesudacity";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_REVIEWS= "reviews";

    private MoviesContract(){}

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);
        public final static String TABLE_NAME = "movies";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_MOVIE_TITLE ="title";
        public final static String COLUMN_MOVIE_OVERVIEW ="overview";
        public final static String COLUMN_MOVIE_VOTE_AVERAGE ="voteAverage";
        public final static String COLUMN_MOVIE_RELEASE_DATE ="release";
        public final static String COLUMN_MOVIE_POSTER_PATH ="posterPath";
        public final static String COLUMN_MOVIE_BACKDROP_PATH ="backdropPath";
        public final static String COLUMN_MOVIE_ID ="id";
    }

    /*public static final class VideosEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VIDEOS);
        public final static String TABLE_NAME = "videos";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_VIDEO_KEY ="key";
        public final static String COLUMN_VIDEO_NAME ="name";
        public final static String COLUMN_VIDEO_SIZE ="size";
        public final static String COLUMN_FK_MOVIE_ID ="movieId";
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REVIEWS);
        public final static String TABLE_NAME = "reviews";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_REVIEW_AUTHOR ="author";
        public final static String COLUMN_REVIEW_CONTENT ="content";
        public final static String COLUMN_FK_MOVIE_ID ="movieId";
    }*/
}
