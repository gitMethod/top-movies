
package com.example.android.moviesudacity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesudacity.data.MoviesContract;
import com.example.android.moviesudacity.data.MoviesDbHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.moviesudacity.R.id.progressBarParent;

public class MainActivity extends AppCompatActivity implements
         MoviesAdapter.ListItemClickListener {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String MOVIES_URL_POPULAR =
            "https://api.themoviedb.org/3/movie/popular?api_key=dbb539c09bc6d9e2e9e6bf360b705e5b";
    private String MOVIES_URL_RATING =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=dbb539c09bc6d9e2e9e6bf360b705e5b";

    private static final int NETWORK_LOADER_ID = 1;
    private static final int PERSIST_LOADER_ID = 2;
    private MoviesAdapter mAdapter;
    private RecyclerView recView;
    private MoviesList moviesList;
    private boolean showMenu;
    private LoaderManager loaderManager;
    private LoaderManager.LoaderCallbacks<List<Movie>> networkLoader;
    private LoaderManager.LoaderCallbacks<Cursor> persistLoader;
    private MoviesDbHelper moviesDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recView = (RecyclerView) findViewById(R.id.movies_list);
        recView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL ));
        recView.setHasFixedSize(true);

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this, MainActivity.this);
        recView.setAdapter(mAdapter);
        loaderManager = getLoaderManager();

        networkLoader = new LoaderManager.LoaderCallbacks<List<Movie>>() {
            @Override
            public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
                return new MovieLoader(MainActivity.this, args.getString("url"));
            }
            @Override
            public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
                moviesList = new MoviesList(data);
                mAdapter.swap(moviesList.getList());
                loaderVisibility(false);

            }
            @Override
            public void onLoaderReset(Loader<List<Movie>> loader) {
            }
        };

        persistLoader = new LoaderManager.LoaderCallbacks<Cursor>(){

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] projection = {
                        MoviesContract.MoviesEntry._ID,
                        MoviesContract.MoviesEntry.COLUMN_MOVIE_ID
                };

                return new CursorLoader(MainActivity.this,
                        MoviesContract.MoviesEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                ArrayList<String> favoriteIds = new ArrayList<>();
                int columnIndex = data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID);
                while(data.moveToNext()) {
                    favoriteIds.add(data.getString(columnIndex));
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };

        if(savedInstanceState == null){
            Log.e(LOG_TAG, "the instance is null");
            checkInternetAndRun(MOVIES_URL_POPULAR);

        } else {
            Log.e(LOG_TAG, "the instance is no null");
            loaderVisibility(false);
            moviesList = savedInstanceState.getParcelable("savedMovies");
            mAdapter.swap(moviesList.getList());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("savedMovies", moviesList);
    }

    public void checkInternetAndRun(String url){
        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.restartLoader(NETWORK_LOADER_ID, bundle, networkLoader);
            loaderVisibility(true);
        } else {
            TextView progressText = (TextView) findViewById(R.id.progress_text);
            progressText.setText("No internet connection");
        }

    }

    public void loaderVisibility(boolean status) {
        LinearLayout spinner =(LinearLayout)findViewById(progressBarParent);
        View progressBar = findViewById(R.id.progress_bar);
        if(status){
            spinner.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            spinner.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        showMenu = !status;
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
                mAdapter.clear();
                return true;
            case R.id.action_rating:
                checkInternetAndRun(MOVIES_URL_RATING);
                mAdapter.clear();
                return true;
            case R.id.action_favorites:
                Toast.makeText(this, "favorites clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("clickedMovie", movie);
        startActivity(intent);
    }
}

