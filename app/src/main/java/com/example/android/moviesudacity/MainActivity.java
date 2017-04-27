
package com.example.android.moviesudacity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private String MOVIES_URL_POPULAR =
            "https://api.themoviedb.org/3/movie/popular?api_key=dbb539c09bc6d9e2e9e6bf360b705e5b";
    private String MOVIES_URL_RATING =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=dbb539c09bc6d9e2e9e6bf360b705e5b";

    private static final int MOVIES_LOADER_ID = 1;
    private MoviesAdapter mAdapter;
    private RecyclerView recView;
    private MoviesList moviesList;
    private boolean showMenu;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recView = (RecyclerView) findViewById(R.id.movies_list);
        recView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL ));
        recView.setHasFixedSize(true);

        loaderManager = getLoaderManager();

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this, MainActivity.this);
        recView.setAdapter(mAdapter);

        checkInternetAndRun(MOVIES_URL_POPULAR);
    }

    public void checkInternetAndRun(String url){
        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        mAdapter.clear();
        LinearLayout spinner=(LinearLayout)findViewById(progressBarParent);
        spinner.setVisibility(View.VISIBLE);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.restartLoader(MOVIES_LOADER_ID, bundle, this);
        } else {
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            TextView progressText = (TextView) findViewById(R.id.progress_text);
            progressText.setText("No internet connection");
        }
        showMenu = false;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if(!showMenu){
            menu.findItem(R.id.action_rating).setVisible(false);
            menu.findItem(R.id.action_popularity).setVisible(false);
        } else {
            menu.findItem(R.id.action_rating).setVisible(true);
            menu.findItem(R.id.action_popularity).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popularity:
                checkInternetAndRun(MOVIES_URL_POPULAR);
                return true;
            case R.id.action_rating:
                checkInternetAndRun(MOVIES_URL_RATING);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, args.getString("url"));
    }


    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        moviesList = new MoviesList(data);
        mAdapter.swap(moviesList.getList());

        LinearLayout spinner=(LinearLayout)findViewById(progressBarParent);
        spinner.setVisibility(View.GONE);

        showMenu = true;
        invalidateOptionsMenu();
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

