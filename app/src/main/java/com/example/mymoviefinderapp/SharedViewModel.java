package com.example.mymoviefinderapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Movie>> favoriteMovies = new MutableLiveData<>();

    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(List<Movie> movies) {
        favoriteMovies.setValue(movies);
    }

    public void addFavorite(Movie movie) {
        List<Movie> currentFavorites = favoriteMovies.getValue();
        if (currentFavorites != null) {
            currentFavorites.add(movie);
            setFavoriteMovies(currentFavorites);
        }
    }

    public void removeFavorite(Movie movie) {
        List<Movie> currentFavorites = favoriteMovies.getValue();
        if (currentFavorites != null) {
            currentFavorites.remove(movie);
            setFavoriteMovies(currentFavorites);
        }
    }
}
