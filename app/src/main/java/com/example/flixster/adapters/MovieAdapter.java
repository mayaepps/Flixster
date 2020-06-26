package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflates a layout from XML and returns it inside the viewholder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movie_view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movie_view);
    }

    // Populates data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the movie at the position passed in
        Movie movie = movies.get(position);
        // Bind the movie data into the ViewHolder
        holder.bind(movie);

    }

    // Returns the total count of the items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //representation of a row/movie in the recycler view (can't be static)
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            //set the onClickListener to this row
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            int placeholderImage;

            //if phone is in landscape, set imageUrl and placehodlerImage to be back drop images
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                placeholderImage = Movie.BACKDROP_PLACEHOLDER;
            } // else, the imageUrl and placeholder are the poster images
            else {
                imageUrl = movie.getPosterPath();
                placeholderImage = Movie.POSTER_PLACEHOLDER;
            }

            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            Glide.with(context).load(imageUrl).placeholder(placeholderImage)
                    .transform( new RoundedCornersTransformation(radius, margin)).into(ivPoster);

        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position exists
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position just clicked on
                Movie clickedMovie = movies.get(position);
                // create intent for the new movie details activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the clickedMovie using parceler with its name as the key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(clickedMovie));
                //then start the activity
                context.startActivity(intent);

            }
        }
    }
}
