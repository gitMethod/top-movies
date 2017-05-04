
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesudacity.data.MoviesContract;

import java.util.ArrayList;

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
    private LoaderManager loaderManager;
    private ArrayList<String> moviesIds;

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

        if(savedInstanceState == null){
            runLoader(MOVIES_URL_POPULAR, null);
        } else {
            progressBarVisibility(false);
            moviesList = savedInstanceState.getParcelable("savedMovies");
            mAdapter.swap(moviesList.getList());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("savedMovies", moviesList);
    }

    public void runLoader(String url, ArrayList<String> moviesIds){
        mAdapter.clear();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putStringArrayList("ids", moviesIds);

        if(checkConnection()) {
            loaderManager.restartLoader(NETWORK_LOADER_ID, bundle, networkLoader);
            progressBarVisibility(true);
        }
    }

    public boolean checkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        TextView progressText = (TextView) findViewById(R.id.progress_text);
        progressText.setText("No internet connection");
        progressBarVisibility(true);
        return false;
    }

    public void progressBarVisibility(boolean status) {
        LinearLayout spinner =(LinearLayout)findViewById(progressBarParent);
        View progressBar = findViewById(R.id.progress_bar);
        if(status){
            spinner.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            spinner.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popularity:
                runLoader(MOVIES_URL_POPULAR, null);
                getSupportActionBar().setTitle("Top 25 by popularity");
                return true;
            case R.id.action_rating:
                runLoader(MOVIES_URL_RATING, null);
                getSupportActionBar().setTitle("Top 25 by rating");
                return true;
            case R.id.action_favorites:
                runLoader(null, moviesIds);
                getSupportActionBar().setTitle("My favorites");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie movie, int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        markFavorite(movie);
        intent.putExtra("clickedMovie", movie);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void markFavorite(Movie movie){
        for (int i=0; i<moviesIds.size(); i++){
            if(moviesIds.get(i).equals(movie.getId())){
                movie.setFavorite(true);
                return;
            }
        }
    }

    private LoaderManager.LoaderCallbacks<MoviesList> networkLoader = new LoaderManager.LoaderCallbacks<MoviesList>() {
        @Override
        public Loader<MoviesList> onCreateLoader(int id, Bundle args) {
            String url = args.getString("url");
            ArrayList<String> moviesIds = args.getStringArrayList("ids");
            if( url == null){
                return new MovieLoader(MainActivity.this, moviesIds);
            } else {
                return new MovieLoader(MainActivity.this, url);
            }
        }
        @Override
        public void onLoadFinished(Loader<MoviesList> loader, MoviesList data) {
            moviesList = data;
            mAdapter.swap(moviesList.getList());
            progressBarVisibility(false);
        }
        @Override
        public void onLoaderReset(Loader<MoviesList> loader) {}
    };



    private LoaderManager.LoaderCallbacks<Cursor> persistLoader = new LoaderManager.LoaderCallbacks<Cursor>(){
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
            moviesIds = favoriteIds;
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {}};

    @Override
    protected void onResume() {
        super.onResume();
            loaderManager.restartLoader(PERSIST_LOADER_ID, null, persistLoader);

        if(moviesList != null && moviesList.isDatabaseArray()){
            runLoader(null, moviesIds);
            Toast.makeText(this, "triggered", Toast.LENGTH_SHORT).show();
        }

    }
}

