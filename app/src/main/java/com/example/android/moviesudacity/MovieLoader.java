package com.example.android.moviesudacity;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>>{

    private static final String LOG_TAG = MovieLoader.class.getName();
    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return NetworkHelper.moviesData(mUrl, getContext());
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
