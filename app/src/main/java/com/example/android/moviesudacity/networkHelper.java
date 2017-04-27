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
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class networkHelper {
    private static final String LOG_TAG = networkHelper.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE_IMAGE_SIZE = "w185/";
    private static final String BASE_BACKDROP_SIZE = "w500/";

    private static List<Movie> extractMoviesInfo(String url, Context context){
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Movie> movies = new ArrayList<>();

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
                                String imageUrl = currentMovie.getString("poster_path");
                                String wholeUrlImage = BASE_IMAGE_URL + BASE_IMAGE_SIZE + imageUrl;
                                String backDrop = currentMovie.getString("backdrop_path");
                                String wholeUrlBackdrop = BASE_IMAGE_URL + BASE_BACKDROP_SIZE + backDrop;
                                String title = currentMovie.getString("title");
                                String synopsis = currentMovie.getString("overview");
                                double rating = currentMovie.getDouble("vote_average");
                                String release = currentMovie.getString("release_date");
                                String id = currentMovie.getString("id");

                                Movie movie = new Movie(title, synopsis, rating, release, wholeUrlImage, wholeUrlBackdrop, id);
                                movies.add(movie);
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
        return movies;
    }

    private static List<Movie> appendReviewsVideos(List<Movie> list, Context context){
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Movie> movies = list;

        RequestQueue queue = Volley.newRequestQueue(context);

        for (int i=0; i<list.size(); i++) {
            final Movie movie = list.get(i);
            String movieId = movie.getId();
            String url =
                    "https://api.themoviedb.org/3/movie/"+movieId+
                            "?api_key=dbb539c09bc6d9e2e9e6bf360b705e5b&append_to_response=reviews,videos";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response){
                            try {
                                JSONObject jsonReviewObject = response.getJSONObject("reviews");
                                JSONArray movieReviewsArray = jsonReviewObject.getJSONArray("results");
                                for (int i = 0; i < movieReviewsArray.length(); i++) {
                                    JSONObject currentReview = movieReviewsArray.getJSONObject(i);
                                    String author = currentReview.getString("author");
                                    String content = currentReview.getString("content");
                                    movie.getReviews().put(author, content);
                                }

                                JSONObject jsonPreviewsObject = response.getJSONObject("videos");
                                JSONArray moviePreviewsArray = jsonPreviewsObject.getJSONArray("results");
                                for (int i = 0; i < moviePreviewsArray.length(); i++) {
                                    JSONObject currentVideo = moviePreviewsArray.getJSONObject(i);
                                    String id = currentVideo.getString("id");
                                    String key = currentVideo.getString("key");
                                    movie.getPreviews().put(key, id);
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
            Log.e(LOG_TAG, "got the infoooooooooooooooooo");
        }
        return movies;
    }

    private static void cachedJsonImages(List<Movie> list, Context context){


        final CountDownLatch latch = new CountDownLatch(list.size() * 2);
        for (int i=0; i<list.size(); i++){
            final Movie movie = list.get(i);

            Picasso.with(context).load(movie.getImgUrl()).fetch(new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    latch.countDown();
                }
                @Override
                public void onError() {
                    Log.d(LOG_TAG, "error fetching image");
                }
            });

            Picasso.with(context).load(movie.getBackDrop()).fetch(new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    latch.countDown();
                }
                @Override
                public void onError() {
                    Log.e(LOG_TAG, "error fetching image");
                }
            });

            for (Map.Entry<String, String> entry : movie.getPreviews().entrySet()) {
                Picasso.with(context).load("http://img.youtube.com/vi/"+entry.getKey()+"/0.jpg")
                        .fetch(new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(LOG_TAG, "fetching image");
                    }
                    @Override
                    public void onError() {
                        Log.d(LOG_TAG, "error fetching image");
                    }
                });
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<Movie> moviesData(String requestUrl, Context context) {

        List<Movie> movies = extractMoviesInfo(requestUrl, context);
        List<Movie> moviesComplete = appendReviewsVideos( movies, context);
        cachedJsonImages(moviesComplete, context);
        Log.e(LOG_TAG, "not waiting for anyone");
        return moviesComplete;
    }




}

