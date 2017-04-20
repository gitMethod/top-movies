package com.example.android.moviesudacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private ImageView image = (ImageView) findViewById(R.id.details_image);
    private TextView title = (TextView) findViewById(R.id.details_title);
    private TextView release = (TextView) findViewById(R.id.details_release);
    private TextView rating = (TextView) findViewById(R.id.details_raiting);
    private TextView synopsis = (TextView) findViewById(R.id.details_synopsis);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int index = intent.getIntExtra("indexPosition", 0);
    }
}
