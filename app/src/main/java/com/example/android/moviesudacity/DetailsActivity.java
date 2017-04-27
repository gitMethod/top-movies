package com.example.android.moviesudacity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Map;


public class DetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
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


        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        int id = -1;
        for (final Map.Entry<String, String> entry : clickedMovie.getPreviews().entrySet()) {

            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.setPadding(4, 4, 4, 4);
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams
                    (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            frameLayout.setId(id +=1);
            layout.addView(frameLayout);
            Log.e(LOG_TAG, "the id is " + frameLayout.getId());

            ImageView imageView = new ImageView(this);
            Picasso.with(DetailsActivity.this).load("http://img.youtube.com/vi/"+entry.getKey()+"/mqdefault.jpg").into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            frameLayout.addView(imageView);

            ImageView imageViewOverlay = new ImageView(this);

            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams
                            (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            imageViewOverlay.setLayoutParams(layoutParams);

            imageViewOverlay.setImageResource(R.drawable.youtube_play);
            imageViewOverlay.setScaleType(ImageView.ScaleType.FIT_XY);
            frameLayout.addView(imageViewOverlay);

            View.OnClickListener clickListener = new View.OnClickListener() {

                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+entry.getKey())));
                }
            };

            imageView.setOnClickListener(clickListener);
        }


    }
}
