package com.example.mymoviefinderapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private DatabaseHelper dbHelper;

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.movieList = movieList != null ? movieList : new ArrayList<>();
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
            intent.putExtra("MOVIE_TITLE", movie.getTitle());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public void updateMovies(List<Movie> newMovies) {
        movieList = newMovies;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView yearTextView;
        private ImageView posterImageView;
        private Button favoriteButton;

        private SharedViewModel sharedViewModel;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            sharedViewModel = new SharedViewModel();
        }

        public void bind(Movie movie) {
            titleTextView.setText(movie.getTitle());
            yearTextView.setText(movie.getYear());
            Glide.with(posterImageView.getContext()).load(movie.getPoster()).into(posterImageView);

            updateFavoriteButton(movie);

            favoriteButton.setOnClickListener(v -> {
                if (isFavorite(movie)) {
                    dbHelper.removeFavorite(movie.getTitle());
                    sharedViewModel.removeFavorite(movie); // Update SharedViewModel
                } else {
                    dbHelper.addFavorite(movie);
                    sharedViewModel.addFavorite(movie); // Update SharedViewModel
                }
                // Only update the button text to reflect the change
                updateFavoriteButton(movie);
            });

        }

        private void updateFavoriteButton(Movie movie) {
            if (isFavorite(movie)) {
                favoriteButton.setText("Remove from Favorites");
            } else {
                favoriteButton.setText("Add to Favorites");
            }
        }

        private boolean isFavorite(Movie movie) {
            List<Movie> favorites = dbHelper.getAllFavorites();
            for (Movie favorite : favorites) {
                if (favorite.getTitle().equals(movie.getTitle())) {
                    return true;
                }
            }
            return false;
        }
    }
}
