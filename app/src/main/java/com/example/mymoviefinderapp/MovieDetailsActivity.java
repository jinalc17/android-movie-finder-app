package com.example.mymoviefinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mymoviefinderapp.databinding.ActivityMovieDetailsBinding;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MovieDetailsActivity extends AppCompatActivity {

    private ActivityMovieDetailsBinding binding;
    private OkHttpClient client;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up back button
        binding.backButton.setOnClickListener(v -> onBackPressed());

        // Get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("MOVIE_TITLE");

        // Initialize OkHttpClient with logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        gson = new Gson();
        fetchMovieDetails(title);
    }

    private void fetchMovieDetails(String title) {
        String query = title.trim();
        if (!query.isEmpty()) {
            String url = "https://www.omdbapi.com/?apikey=6a34ea07&t=" + query + "&type=movie";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(MovieDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        MovieResponse movieResponse = gson.fromJson(jsonResponse, MovieResponse.class);

                        runOnUiThread(() -> {
                            if (movieResponse != null) {
                                setMovieDetails(movieResponse);
                            } else {
                                Toast.makeText(MovieDetailsActivity.this, "No movies found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(MovieDetailsActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        }
    }

    private void setMovieDetails(MovieResponse movieResponse) {
        binding.titleTextView.setText(movieResponse.getTitle());
        binding.yearTextView.setText("Year: " + movieResponse.getYear());
        binding.ratedTextView.setText("Rated: " + movieResponse.getRated());
        binding.releasedTextView.setText("Released: " + movieResponse.getReleased());
        binding.runtimeTextView.setText("Runtime: " + movieResponse.getRuntime());
        binding.genreTextView.setText("Genre: " + movieResponse.getGenre());
        binding.directorTextView.setText("Director: " + movieResponse.getDirector());
        binding.writerTextView.setText("Writer: " + movieResponse.getWriter());
        binding.actorsTextView.setText("Actors: " + movieResponse.getActors());
        binding.plotTextView.setText("Plot: " + movieResponse.getPlot());
        binding.languageTextView.setText("Language: " + movieResponse.getLanguage());
        binding.countryTextView.setText("Country: " + movieResponse.getCountry());
        binding.awardsTextView.setText("Awards: " + movieResponse.getAwards());
        binding.imdbIdTextView.setText("IMDb ID: " + movieResponse.getImdbID());
        binding.ratingsTextView.setText("IMDb Rating: " + movieResponse.getImdbRating() +
                "\nRotten Tomatoes: " + movieResponse.getRatings().get(1).getValue());
        binding.metascoreTextView.setText("Meta Score: " + movieResponse.getMetascore());
        binding.imdbVotesTextView.setText("IMDb Votes: " + movieResponse.getImdbVotes());
        binding.boxOfficeTextView.setText("Box Office: " + movieResponse.getBoxOffice());
        binding.productionTextView.setText("Production: " + movieResponse.getProduction());
        binding.websiteTextView.setText("Website: " + movieResponse.getWebsite());

        // Load the poster image using Glide
        Glide.with(this).load(movieResponse.getPoster()).into(binding.posterImageView);
    }
}
