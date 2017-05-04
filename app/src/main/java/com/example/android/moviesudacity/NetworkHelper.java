package com.example.android.moviesudacity;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class NetworkHelper {
    private static final String LOG_TAG = NetworkHelper.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE_IMAGE_SIZE = "w185/";
    private static final String BASE_BACKDROP_SIZE = "w500/";

    private static List<String> extractMoviesIds(String url, Context context){
        final CountDownLatch latch = new CountDownLatch(1);
        final List<String> moviesIds = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray movieInfoArray;
                        try {
                            movieInfoArray = response.getJSONArray("results");
                            for (int i=0; i<movieInfoArray.length(); i++){
                                JSONObject currentMovie = movieInfoArray.getJSONObject(i);
                                String id = currentMovie.getString("id");
                                moviesIds.add(id);
                            }
                            latch.countDown();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(LOG_TAG, "error parsing json response");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "Problem getting the movies JSON", error);
                    }
                });
        queue.add(jsObjRequest);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return moviesIds;
    }

    private static List<Movie> extractMoviesInfo(List<String> list, Context context){
        final CountDownLatch latch = new CountDownLatch(list.size());
        final List<Movie> movies = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);

        for (int i=0; i<list.size(); i++) {
            String movieId = list.get(i);
            String url = "https://api.themoviedb.org/3/movie/"+movieId+
                            "?api_key=dbb539c09bc6d9e2e9e6bf360b705e5b&append_to_response=reviews,videos";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response){
                            try {
                                String posterPath = response.getString("poster_path");
                                String wholeUrlImage = BASE_IMAGE_URL + BASE_IMAGE_SIZE + posterPath;
                                String backDropPath = response.getString("backdrop_path");
                                String wholeUrlBackdrop = BASE_IMAGE_URL + BASE_BACKDROP_SIZE + backDropPath;
                                String title = response.getString("title");
                                String synopsis = response.getString("overview");
                                double rating = response.getDouble("vote_average");
                                String release = response.getString("release_date");
                                String id = response.getString("id");

                                Movie movie = new Movie(title, synopsis, rating, release, wholeUrlImage, wholeUrlBackdrop, id);
                                movies.add(movie);

                                JSONObject jsonReviewObject = response.getJSONObject("reviews");
                                JSONArray movieReviewsArray = jsonReviewObject.getJSONArray("results");
                                for (int i = 0; i < movieReviewsArray.length(); i++) {
                                    JSONObject currentReview = movieReviewsArray.getJSONObject(i);
                                    String author = currentReview.getString("author");
                                    String content = currentReview.getString("content");
                                    movie.getReviews().add(new MovieReview(author, content));
                                }

                                JSONObject jsonPreviewsObject = response.getJSONObject("videos");
                                JSONArray moviePreviewsArray = jsonPreviewsObject.getJSONArray("results");
                                for (int i = 0; i < moviePreviewsArray.length(); i++) {
                                    JSONObject currentVideo = moviePreviewsArray.getJSONObject(i);
                                    String key = currentVideo.getString("key");
                                    String name = currentVideo.getString("name");
                                    String size = currentVideo.getString("id");
                                    movie.getTrailers().add(new MovieTrailer(key,name,size));
                                }
                                latch.countDown();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(LOG_TAG, "error parsing json response");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, "Problem getting the movies JSON", error);
                        }
                    });
            queue.add(jsObjRequest);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private static void cacheJsonImages(List<Movie> list, Context context){
        final CountDownLatch latch = new CountDownLatch(list.size());
        for (int i=0; i<list.size(); i++){
            final Movie movie = list.get(i);

            Picasso.with(context).load(movie.getPosterPath()).fetch(new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    latch.countDown();
                }
                @Override
                public void onError() {
                    Log.d(LOG_TAG, "error fetching image");
                }
            });
            Picasso.with(context).load(movie.getBackdropPath()).fetch();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static MoviesList networkMovies (String requestUrl, Context context) {
        List<String> moviesIds = extractMoviesIds(requestUrl, context);
        List<Movie> movies = extractMoviesInfo(moviesIds, context);
        cacheJsonImages(movies, context);

        return new MoviesList(movies, false);
    }

    public static MoviesList favoritesMovies (ArrayList<String> moviesIds, Context context) {
        List<Movie> movies = extractMoviesInfo(moviesIds, context);
        setFavorite(movies);
        cacheJsonImages(movies, context);
        return new MoviesList(movies, true);
    }

    private static void setFavorite(List<Movie> list){
        for (Movie movie: list){
            movie.setFavorite(true);
        }
    }




}

