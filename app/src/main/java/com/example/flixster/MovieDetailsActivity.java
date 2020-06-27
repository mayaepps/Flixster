package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
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

        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        // unwrap the movie passed in through intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        // initialize all the views using view binding
        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;
        tvPopularity = binding.tvPopularity;
        ivBackdrop = binding.ivBackdrop;

        //set all views using this.movie
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvPopularity.setText("Popularity: " + movie.getPopularity());
        float voteAverage = movie.getVoteAverage().floatValue(); //number of stars (out of 5) is half of voteAverage (out of 10)
        float stars = voteAverage > 0 ? voteAverage / 2.0f : 0;
        rbVoteAverage.setRating(stars);

        //load the clickable backdrop
        Glide.with(this).load(movie.getBackdropPath()).placeholder(Movie.BACKDROP_PLACEHOLDER).into(ivBackdrop);

        String fullApiKey = String.format(VIDEOS_API_URL + getString(R.string.movie_database_api_key), movie.getId());

        //call the movie database API and get the key for the YouTube video
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(fullApiKey,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode,Headers headers,JsonHttpResponseHandler.JSON json){
                try{
                    //parse the array to get the key
                    JSONArray result=json.jsonObject.getJSONArray("results");
                    final String key=result.getJSONObject(0).getString("key");

                    //when backdrop is clicked, start the video
                    ivBackdrop.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){

                            // the intent to go to the MovieTrailerActivity with the key from the API call to the MovieTrailerActivity
                            Intent i=new Intent(MovieDetailsActivity.this,MovieTrailerActivity.class);
                            i.putExtra("youtubeKey",key);

                            startActivity(i);
                        }
                    });

                }catch(JSONException e){
                    Toast.makeText(MovieDetailsActivity.this, "No video avaliable", Toast.LENGTH_LONG).show();
                    Log.e("MovieDetailsActivity","Hit JSON exception",e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("MovieDetailsActivity", "onFailure: status code " + statusCode);
            }
        });

    }

}