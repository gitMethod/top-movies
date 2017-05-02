package com.example.android.moviesudacity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.moviesudacity.data.MoviesContract.MoviesEntry;

public class MoviesDbHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = MoviesDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MOVIE_TABLE =  "CREATE TABLE " + MoviesEntry.TABLE_NAME + " ("
                + MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, "
                + MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " INTEGER, "
                + MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT, "
                + MoviesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, "
                + MoviesEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT, "
                + MoviesEntry.COLUMN_MOVIE_ID + " TEXT );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
