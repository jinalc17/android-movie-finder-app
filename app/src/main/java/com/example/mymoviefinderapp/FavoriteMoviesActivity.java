package com.example.mymoviefinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteMoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private DatabaseHelper dbHelper;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Notify changes to MainActivity and finish
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        loadFavorites();
    }

    private void loadFavorites() {
        List<Movie> favoriteMovies = dbHelper.getAllFavorites();
        adapter = new MovieAdapter(this, favoriteMovies); // Pass context here
        recyclerView.setAdapter(adapter);

        // Update SharedViewModel with new favorites
        sharedViewModel.setFavoriteMovies(favoriteMovies);
    }
}
