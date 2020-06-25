package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
//    Set the title and overview from the movie
//    Set the RatingBar value by diving Movie.getVoteAverage by 2.0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // unwrap the movie passed in through intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Unwrapped movie '%s'", movie.getTitle()));

        // get and then set the title, overview, and vote average
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        //number of stars (out of 5) is half of voteAverage (out of 10)
        float voteAverage = movie.getVoteAverage().floatValue();
        float stars = voteAverage > 0 ? voteAverage / 2.0f : 0;
        rbVoteAverage.setRating(stars);

    }
}