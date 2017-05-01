package com.example.android.moviesudacity.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MoviesProvider extends ContentProvider{
    public static final String LOG_TAG = MoviesProvider.class.getSimpleName();
    private MoviesDbHelper mDbHelper;
    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;
    private static final UriMatcher sUriMatcher = buildMatcher();

    public static UriMatcher buildMatcher(){
        UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES +"/#", MOVIE_ID);
        return sUriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                cursor = database.query(MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_ID:
                selection = MoviesContract.MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                return insert(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private  Uri insertMovie(Uri uri, ContentValues values){
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        long id = sqLiteDatabase.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
