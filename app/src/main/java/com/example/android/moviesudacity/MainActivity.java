
package com.example.android.moviesudacity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{
    private String MOVIES_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=dbb539c09bc6d9e2e9e6bf360b705e5b";
    private static final int MOVIES_LOADER_ID = 1;

    private MoviesAdapter mAdapter;
    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recView = (RecyclerView) findViewById(R.id.movies_list);
        recView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL ));
        recView.setHasFixedSize(true);

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), MainActivity.this);
        recView.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(MOVIES_LOADER_ID, null, this);
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, MOVIES_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

        if(data != null && !data.isEmpty()){
            mAdapter.swap(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}

