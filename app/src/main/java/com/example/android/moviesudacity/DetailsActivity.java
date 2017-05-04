package com.example.android.moviesudacity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.moviesudacity.data.MoviesContract.MoviesEntry;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private ImageView image;
    private ImageView backDrop;
    private TextView title;
    private TextView release;
    private TextView rating;
    private TextView synopsis;
    private FloatingActionButton fab;
    private Movie clickedMovie;

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
        fab = (FloatingActionButton) findViewById(R.id.details_fab);

        Intent intent = getIntent();
        clickedMovie = intent.getParcelableExtra("clickedMovie");

        Picasso.with(DetailsActivity.this).load(clickedMovie.getPosterPath()).into(image);
        Picasso.with(DetailsActivity.this).load(clickedMovie.getBackdropPath()).into(backDrop);
        title.setText(clickedMovie.getTitle());
        release.setText(clickedMovie.getReleaseDate());
        rating.setText(Double.toString(clickedMovie.getVoteAverage()));
        synopsis.setText(clickedMovie.getOverview());
        updateFavIcon();

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(clickedMovie.isFavorite()){
                    deleteFavorite();
                } else {
                    insertFavorite();
                }
            }
        });

        generateTrailers();
        generateReviews();
    }

    private void updateFavIcon(){
        if(clickedMovie.isFavorite()){
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_white));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_white));
        }
    }

    private void insertFavorite(){
        ContentValues values = new ContentValues();
        values.put(MoviesEntry.COLUMN_MOVIE_ID, clickedMovie.getId());
        getContentResolver().insert(MoviesEntry.CONTENT_URI, values);
        clickedMovie.setFavorite(true);
        updateFavIcon();
    }

    private void deleteFavorite(){
        int rowsDeleted = getContentResolver().delete(MoviesEntry.CONTENT_URI,"id=?", new String[]{clickedMovie.getId()});
        if (rowsDeleted == 0) {
            Toast.makeText(this, "Delete movie failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Delete movie success", Toast.LENGTH_SHORT).show();
        }
        clickedMovie.setFavorite(false);
        updateFavIcon();
    }


    private void generateTrailers(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        for (final MovieTrailer trailer : clickedMovie.getTrailers()) {
            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.setPadding(0, 0, 16, 8);
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams
                    (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT));
            layout.addView(frameLayout);

            ImageView imageView = new ImageView(this);
            Picasso.with(DetailsActivity.this).load("http://img.youtube.com/vi/"+trailer.getKey()+"/0.jpg").into(imageView);
            frameLayout.addView(imageView);

            ImageView imageViewOverlay = new ImageView(this);
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams
                            (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            imageViewOverlay.setLayoutParams(layoutParams);
            imageViewOverlay.setImageResource(R.drawable.youtube_play);
            frameLayout.addView(imageViewOverlay);

            View.OnClickListener clickListener = new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+trailer.getKey())));
                }
            };
            imageView.setOnClickListener(clickListener);
        }

    }

    private void generateReviews(){
        LinearLayout reviewsContainer = (LinearLayout) findViewById(R.id.details_reviews);

        if (clickedMovie.getReviews().isEmpty()){
            TextView txvEmpty = new TextView(this);
            txvEmpty.setText("No review found for this movie");
            TextViewCompat.setTextAppearance(txvEmpty, R.style.CustomBody);
            reviewsContainer.addView(txvEmpty);
        }

        for (final MovieReview review : clickedMovie.getReviews()) {
            ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.review_layout, null, false);
            TextView title = (TextView) constraintLayout.getChildAt(0);
            title.setText(review.getAuthor());
            TextView content = (TextView) constraintLayout.getChildAt(1);
            content.setText(review.getContent().replace("\n\r", ""));
            reviewsContainer.addView(constraintLayout);

            View.OnClickListener clickListener = new View.OnClickListener() {
                public void onClick(View v) {
                    new MaterialDialog.Builder(DetailsActivity.this)
                            .title(review.getAuthor())
                            .content(review.getContent())
                            .show();
                }
            };
            constraintLayout.setOnClickListener(clickListener);
        }

    }
}
