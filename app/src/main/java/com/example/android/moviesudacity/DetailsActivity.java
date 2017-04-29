package com.example.android.moviesudacity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;


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

        LinearLayout reviewsContainer = (LinearLayout) findViewById(R.id.details_reviews);
        for (final MovieReview review : clickedMovie.getReviews()) {

            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            int marginBottom = (int) (getResources().getDisplayMetrics().density * 24 + 0.5f);
            linearParams.setMargins(0,0,0,marginBottom);
            linearLayout.setLayoutParams(linearParams);
            reviewsContainer.addView(linearLayout);

            TextView authorTv = new TextView(this);
            authorTv.setText(review.getAuthor());
            TextViewCompat.setTextAppearance( authorTv, R.style.TextAppearance_AppCompat_Subhead);
            linearLayout.addView(authorTv);

            TextView contentTv = new TextView(this);
            contentTv.setText(review.getContent().replace("\n\r", ""));
            TextViewCompat.setTextAppearance( contentTv, R.style.CustomBody);
            contentTv.setMaxLines(2);
            contentTv.setEllipsize(TextUtils.TruncateAt.END);
            linearLayout.addView(contentTv);

            View.OnClickListener clickListener = new View.OnClickListener() {
                public void onClick(View v) {
                    new MaterialDialog.Builder(DetailsActivity.this)
                            .title(review.getAuthor())
                            .content(review.getContent())
                            .show();

                }
            };
            linearLayout.setOnClickListener(clickListener);
        }
    }
}
