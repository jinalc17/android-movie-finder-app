package com.example.mymoviefinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MovieDetailsActivity extends AppCompatActivity {
    private OkHttpClient client;
    private Gson gson;
    private TextView titleTextView;
    private TextView yearTextView;
    private TextView ratedTextView;
    private TextView releasedTextView;
    private TextView runtimeTextView;
    private TextView genreTextView;
    private TextView directorTextView;
    private TextView writerTextView;
    private TextView actorsTextView;
    private TextView plotTextView;
    private TextView languageTextView;
    private TextView countryTextView;
    private TextView awardsTextView;
    private TextView imdbIdTextView;
    private TextView ratingsTextView;
    private TextView metascoreTextView;
    private TextView imdbVotesTextView;
    private TextView boxOfficeTextView;
    private TextView productionTextView;
    private TextView websiteTextView;
    private ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        yearTextView = findViewById(R.id.yearTextView);
        ratedTextView = findViewById(R.id.ratedTextView);
        releasedTextView = findViewById(R.id.releasedTextView);
        runtimeTextView = findViewById(R.id.runtimeTextView);
        genreTextView = findViewById(R.id.genreTextView);
        directorTextView = findViewById(R.id.directorTextView);
        writerTextView = findViewById(R.id.writerTextView);
        actorsTextView = findViewById(R.id.actorsTextView);
        plotTextView = findViewById(R.id.plotTextView);
        languageTextView = findViewById(R.id.languageTextView);
        countryTextView = findViewById(R.id.countryTextView);
        awardsTextView = findViewById(R.id.awardsTextView);
        imdbIdTextView = findViewById(R.id.imdbIdTextView);
        ratingsTextView = findViewById(R.id.ratingsTextView);
        metascoreTextView = findViewById(R.id.metascoreTextView);
        imdbVotesTextView = findViewById(R.id.imdbVotesTextView);
        boxOfficeTextView = findViewById(R.id.boxOfficeTextView);
        productionTextView = findViewById(R.id.productionTextView);
        websiteTextView = findViewById(R.id.websiteTextView);
        posterImageView = findViewById(R.id.posterImageView);

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
        String query = title.toString().trim();
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
        titleTextView.setText(movieResponse.getTitle());
        yearTextView.setText("Year: " +movieResponse.getYear());
        ratedTextView.setText("Rated: " +movieResponse.getRated());
        releasedTextView.setText("Released: " +movieResponse.getReleased());
        runtimeTextView.setText("Runtime: " +movieResponse.getRuntime());
        genreTextView.setText("Genre: " +movieResponse.getGenre());
        directorTextView.setText("Director: " +movieResponse.getDirector());
        writerTextView.setText("Writer: " +movieResponse.getWriter());
        actorsTextView.setText("Actors: " +movieResponse.getActors());
        plotTextView.setText("Plot: " +movieResponse.getPlot());
        languageTextView.setText("Language: " +movieResponse.getLanguage());
        countryTextView.setText("Country: " +movieResponse.getCountry());
        awardsTextView.setText("Awards: " +movieResponse.getAwards());
        imdbIdTextView.setText("imdbId: " +movieResponse.getImdbID());
        ratingsTextView.setText("IMDb Rating: " + movieResponse.getImdbRating() + "\nRotten Tomatoes: " + movieResponse.getRatings().get(1).getValue());
        metascoreTextView.setText("Meta Score: " +movieResponse.getMetascore());
        imdbVotesTextView.setText("IMDb Votes: " +movieResponse.getImdbVotes());
        boxOfficeTextView.setText("Box Office: " +movieResponse.getBoxOffice());
        productionTextView.setText("Production: " +movieResponse.getProduction());
        websiteTextView.setText("Website: " +movieResponse.getWebsite());

        // Load the poster image using Glide
        Glide.with(this).load(movieResponse.getPoster()).into(posterImageView);
    }
}
