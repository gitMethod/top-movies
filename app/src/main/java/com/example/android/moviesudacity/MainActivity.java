
package com.example.android.moviesudacity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.moviesudacity.R.id.progressBarParent;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MoviesAdapter.ListItemClickListener {
    private String MOVIES_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=dbb539c09bc6d9e2e9e6bf360b705e5b";
    private static final int MOVIES_LOADER_ID = 1;
    private MoviesAdapter mAdapter;
    private RecyclerView recView;
    private MoviesList moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recView = (RecyclerView) findViewById(R.id.movies_list);
        recView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL ));
        recView.setHasFixedSize(true);

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        recView.setAdapter(mAdapter);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIES_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            TextView progressText = (TextView) findViewById(R.id.progress_text);
            progressText.setText("No internet connection");
        }
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("savedList", moviesList);
        super.onSaveInstanceState(outState);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popularity:
                mAdapter.swap(moviesList.sortByPopularity());
                return true;
            case R.id.action_rating:
                mAdapter.swap(moviesList.sortByRating());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, MOVIES_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

        moviesList = new MoviesList(data);
        mAdapter.swap(moviesList.getList());

        LinearLayout spinner=(LinearLayout)findViewById(progressBarParent);
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

