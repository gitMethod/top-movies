package com.example.android.moviesudacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private ImageView image;
    private TextView title;
    private TextView release;
    private TextView rating;
    private TextView synopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        image = (ImageView) findViewById(R.id.details_image);
        title = (TextView) findViewById(R.id.details_title);
        release = (TextView) findViewById(R.id.details_release);
        rating = (TextView) findViewById(R.id.details_raiting);
        synopsis = (TextView) findViewById(R.id.details_synopsis);

        Intent intent = getIntent();
        Movie clickedMovie = intent.getParcelableExtra("clickedMovie");

        image.setImageBitmap(clickedMovie.getBitmapImg());
        title.setText(clickedMovie.getTitle());
        release.setText(clickedMovie.getRelease());
        rating.setText(Double.toString(clickedMovie.getRating()));
        synopsis.setText(clickedMovie.getSynopsis());
    }
}
