package com.example.flixster.models;

import com.example.flixster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    public static final int POSTER_PLACEHOLDER = R.drawable.flicks_movie_placeholder;
    public static final int BACKDROP_PLACEHOLDER = R.drawable.flicks_backdrop_placeholder;

    String backdropPath;
    String posterPath;
    String title;
    String overview;

    public Movie(JSONObject json) throws JSONException {
        posterPath = json.getString("poster_path");
        backdropPath = json.getString("backdrop_path");
        title = json.getString("title");
        overview = json.getString("overview");

    }

    public static List<Movie> fromJSONArray(JSONArray movieJSONArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJSONArray.length(); i++) {
            movies.add(new Movie(movieJSONArray.getJSONObject(i)));
        }

        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }


    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

}
