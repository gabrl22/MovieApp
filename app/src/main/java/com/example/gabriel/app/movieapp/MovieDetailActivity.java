package com.example.gabriel.app.movieapp;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView mDetailImageView;
    private TextView mOverviewTextView;
    private RatingBar mRatingBar;
    private TextView mReleasedDateTextView;
    private Toolbar mToolbar;
    private final String imageBaseUrl = "https://image.tmdb.org/t/p/w342";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mToolbar =(Toolbar)findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDetailImageView = (ImageView)findViewById(R.id.detail_image);
        mOverviewTextView = (TextView)findViewById(R.id.overview_textview);
        mRatingBar = (RatingBar)findViewById(R.id.rating_bar);
        mReleasedDateTextView = (TextView)findViewById(R.id.release_date_textview);


        Movie movie = getIntent().getParcelableExtra("MOVIE");


        ((CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar)).setTitle(movie.getMtitle());

        mOverviewTextView.setText(movie.getOverview());
        Picasso.with(this).load(imageBaseUrl + movie.getDetailImageUrl()).into(mDetailImageView);
        mReleasedDateTextView.setText(movie.getReleaseDate());
        mRatingBar.setRating((float)movie.getRating()/2);
    }
}
