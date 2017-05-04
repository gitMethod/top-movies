package com.example.android.moviesudacity;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class MovieLoader extends AsyncTaskLoader<MoviesList>{

    private static final String LOG_TAG = MovieLoader.class.getName();
    private String mUrl;
    private ArrayList<String> moviesIds;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    public MovieLoader(Context context, ArrayList<String> moviesIds) {
        super(context);
        this.moviesIds = moviesIds;
    }

    @Override
    public MoviesList loadInBackground() {
        if (mUrl == null) {
            Log.e(LOG_TAG, "calling favorites");
            return NetworkHelper.favoritesMovies(moviesIds, getContext());
        }
        Log.e(LOG_TAG, "calling network");
        return NetworkHelper.networkMovies(mUrl, getContext());
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
