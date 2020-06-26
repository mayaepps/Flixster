package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String VIDEOS_API_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=";

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView tvPopularity;
    ImageView ivBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // unwrap the movie passed in through intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        // get and then set the title, overview, and vote average
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        tvPopularity = findViewById(R.id.tvPopularity);
        ivBackdrop = findViewById(R.id.ivBackdrop);

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvPopularity.setText("Popularity: " + movie.getPopularity());
        //number of stars (out of 5) is half of voteAverage (out of 10)
        float voteAverage = movie.getVoteAverage().floatValue();
        float stars = voteAverage > 0 ? voteAverage / 2.0f : 0;
        rbVoteAverage.setRating(stars);

        Glide.with(this).load(movie.getBackdropPath()).placeholder(Movie.BACKDROP_PLACEHOLDER).into(ivBackdrop);

        String fullApiKey = String.format(VIDEOS_API_URL + getString(R.string.movie_database_api_key), movie.getId());


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(fullApiKey, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                try {
                    JSONArray result = json.jsonObject.getJSONArray("results");
                    final String key = result.getJSONObject(0).getString("key");

                    ivBackdrop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);

                            i.putExtra("youtubeKey", key);
                            startActivity(i);
                        }
                    });

                } catch (JSONException e) {
                    Log.e("MovieDetailsActivity", "Hit JSON exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("MovieDetailsActivity", "onFailure: status code " + statusCode);
            }
        });

    }


}