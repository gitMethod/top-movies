package com.example.android.moviesudacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private ImageView image;
    private ImageView backDrop;
    private TextView title;
    private TextView release;
    private TextView rating;
    private TextView synopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        image = (ImageView) findViewById(R.id.details_image);
        backDrop = (ImageView) findViewById(R.id.details_backdrop);
        title = (TextView) findViewById(R.id.details_title);
        release = (TextView) findViewById(R.id.details_release);
        rating = (TextView) findViewById(R.id.details_rating);
        synopsis = (TextView) findViewById(R.id.details_synopsis);

        Intent intent = getIntent();
        Movie clickedMovie = intent.getParcelableExtra("clickedMovie");

        Picasso.with(DetailsActivity.this).load(clickedMovie.getImgUrl()).into(image);
        Picasso.with(DetailsActivity.this).load(clickedMovie.getBackDrop()).into(backDrop);
        title.setText(clickedMovie.getTitle());
        release.setText(clickedMovie.getRelease());
        rating.setText(Double.toString(clickedMovie.getRating()));
        synopsis.setText(clickedMovie.getSynopsis());
    }
}
