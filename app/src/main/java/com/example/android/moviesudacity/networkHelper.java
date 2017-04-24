package com.example.android.moviesudacity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class networkHelper {
    private static final String LOG_TAG = networkHelper.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE_IMAGE_SIZE = "w185/";
    private static final String BASE_BACKDROP_SIZE = "w500/";

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
        Log.e(LOG_TAG, "Problem retrieving the movies JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Movie> extractFeatureFromJson(String movieJSON) {
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }
        final List<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            JSONArray movieInfoArray = baseJsonResponse.getJSONArray("results");

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

                Movie movie = new Movie(title, synopsis, rating, release, wholeUrlImage, wholeUrlBackdrop);
                movies.add(movie);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movies JSON results", e);
        }
        return movies;
    }

    public static void cachedJsonImg(List<Movie> list, Context context){
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
                    Log.d(LOG_TAG, "error fetching image");
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<Movie> moviesData(String requestUrl, Context context) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Movie> movies = extractFeatureFromJson(jsonResponse);
        cachedJsonImg(movies, context);

        return movies;
    }
}

