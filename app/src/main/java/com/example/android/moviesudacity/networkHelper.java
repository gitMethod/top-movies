package com.example.android.moviesudacity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

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

public class networkHelper {
    private static final String LOG_TAG = networkHelper.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE_IMAGE_SIZE = "w185/";

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
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            JSONArray movieArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject currentMovie = movieArray.getJSONObject(i);
                String imageUrl = currentMovie.getString("poster_path");
                String wholeUrl = BASE_IMAGE_URL + BASE_IMAGE_SIZE + imageUrl;
                Bitmap bitmap = getBitmapFromURL(wholeUrl);
                String title = currentMovie.getString("title");
                String synopsis = currentMovie.getString("overview");
                double rating = currentMovie.getDouble("vote_average");
                String release = currentMovie.getString("release_date");
                Movie movie = new Movie(title, synopsis, rating, release, bitmap);
                movies.add(movie);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return movies;
    }

    public static List<Movie> moviesData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Movie> movies = extractFeatureFromJson(jsonResponse);

        return movies;
    }

    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie picture", e);
            return null;
        }
    }
}

