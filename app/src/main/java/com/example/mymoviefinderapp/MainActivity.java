package com.example.mymoviefinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private EditText searchField;
    private Button searchButton;
    private Button favoritesButton;
    private OkHttpClient client;
    private Gson gson;
    private SharedViewModel sharedViewModel;
    private ActivityResultLauncher<Intent> favoritesActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchButton);
        favoritesButton = findViewById(R.id.favoritesButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieAdapter = new MovieAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(movieAdapter);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        gson = new Gson();
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        sharedViewModel.getFavoriteMovies().observe(this, favoriteMovies -> {
            if (movieAdapter != null) {
                movieAdapter.updateMovies(favoriteMovies);
            }
        });

        favoritesActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Refresh the movie list or favorites
                refreshFavorites();
            }
        });

        favoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoriteMoviesActivity.class);
            favoritesActivityLauncher.launch(intent);
        });

        searchButton.setOnClickListener(v -> searchMovies());
    }

    private void searchMovies() {
        String query = searchField.getText().toString().trim();
        if (!query.isEmpty()) {
            String url = "https://www.omdbapi.com/?apikey=6a34ea07&s=" + query + "&type=movie";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        MovieResponse movieResponse = gson.fromJson(jsonResponse, MovieResponse.class);

                        runOnUiThread(() -> {
                            if (movieResponse != null && movieResponse.getSearch() != null) {
                                movieAdapter.updateMovies(movieResponse.getSearch());
                                refreshFavorites(); // Refresh favorites
                            } else {
                                Toast.makeText(MainActivity.this, "No movies found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        }
    }

    private void refreshFavorites() {
        List<Movie> favoriteMovies = sharedViewModel.getFavoriteMovies().getValue();
        if (favoriteMovies != null) {
            movieAdapter.updateMovies(favoriteMovies);
        }
    }
}
