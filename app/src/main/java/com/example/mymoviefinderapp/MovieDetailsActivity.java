package com.example.mymoviefinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        binding.backButton.setOnClickListener(v -> {
            // Notify changes to MainActivity and finish
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });

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
        assert title != null;
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
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(MovieDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
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
                        runOnUiThread(() -> Toast.makeText(MovieDetailsActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show());
                    }
                }
            });
        }
    }

    private void setMovieDetails(MovieResponse movieResponse) {
        binding.titleTextView.setText(movieResponse.getTitle());
        binding.yearTextView.setText(String.format("%s%s", getString(R.string.year), movieResponse.getYear()));
        binding.ratedTextView.setText(String.format("%s%s", getString(R.string.rated), movieResponse.getRated()));
        binding.releasedTextView.setText(String.format("%s%s", getString(R.string.released), movieResponse.getReleased()));
        binding.runtimeTextView.setText(String.format("%s%s", getString(R.string.runtime), movieResponse.getRuntime()));
        binding.genreTextView.setText(String.format("%s%s", getString(R.string.genre), movieResponse.getGenre()));
        binding.directorTextView.setText(String.format("%s%s", getString(R.string.director), movieResponse.getDirector()));
        binding.writerTextView.setText(String.format("%s%s", getString(R.string.writer), movieResponse.getWriter()));
        binding.actorsTextView.setText(String.format("%s%s", getString(R.string.actors), movieResponse.getActors()));
        binding.plotTextView.setText(String.format("%s%s", getString(R.string.plot), movieResponse.getPlot()));
        binding.languageTextView.setText(String.format("%s%s", getString(R.string.language), movieResponse.getLanguage()));
        binding.countryTextView.setText(String.format("%s%s", getString(R.string.country), movieResponse.getCountry()));
        binding.awardsTextView.setText(String.format("%s%s", getString(R.string.awards), movieResponse.getAwards()));
        binding.imdbIdTextView.setText(String.format("%s%s", getString(R.string.imdb_id), movieResponse.getImdbID()));
        binding.ratingsTextView.setText(String.format("%s%s%s%s", getString(R.string.imdb_rating), movieResponse.getImdbRating(), getString(R.string.nrotten_tomatoes), movieResponse.getRatings().get(1).getValue()));
        binding.metascoreTextView.setText(String.format("%s%s", getString(R.string.meta_score), movieResponse.getMetascore()));
        binding.imdbVotesTextView.setText(String.format("%s%s", getString(R.string.imdb_votes), movieResponse.getImdbVotes()));
        binding.boxOfficeTextView.setText(String.format("%s%s", getString(R.string.box_office), movieResponse.getBoxOffice()));
        binding.productionTextView.setText(String.format("%s%s", getString(R.string.production), movieResponse.getProduction()));
        binding.websiteTextView.setText(String.format("%s%s", getString(R.string.website), movieResponse.getWebsite()));

        // Load the poster image using Glide
        Glide.with(this).load(movieResponse.getPoster()).into(binding.posterImageView);
    }
}
