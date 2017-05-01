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

        /*String SQL_CREATE_VIDEO_TABLE =  "CREATE TABLE " + VideosEntry.TABLE_NAME + " ("
                + VideosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VideosEntry.COLUMN_VIDEO_KEY + " TEXT NOT NULL, "
                + VideosEntry.COLUMN_VIDEO_NAME + " TEXT, "
                + VideosEntry.COLUMN_VIDEO_SIZE + " TEXT, "
                + VideosEntry.COLUMN_FK_MOVIE_ID + " INTEGER ); ";

        String SQL_CREATE_REVIEW_TABLE =  "CREATE TABLE " + ReviewEntry.TABLE_NAME + " ("
                + ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReviewEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, "
                + ReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT, "
                + ReviewEntry.COLUMN_FK_MOVIE_ID + " INTEGER ); ";*/

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        //db.execSQL(SQL_CREATE_VIDEO_TABLE);
        //db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
