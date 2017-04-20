
package com.example.android.moviesudacity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MoviesAdapter.ListItemClickListener {
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

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), MainActivity.this, this);
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

        LinearLayout spinner=(LinearLayout)findViewById(R.id.progressBarParent);
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public void onListItemClick(Movie movie) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("clickedMovie", movie);
        startActivity(intent);
    }
}

