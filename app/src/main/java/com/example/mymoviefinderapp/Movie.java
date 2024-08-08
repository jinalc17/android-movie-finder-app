package com.example.mymoviefinderapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Movie {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

    public Movie(String title, String year, String poster) {
        this.Title = title;
        this.Year = year;
        this.Poster = poster;
    }

    // Getters and setters
    public String getTitle() { return Title; }
    public void setTitle(String title) { Title = title; }

    public String getYear() { return Year; }
    public void setYear(String year) { Year = year; }

    public String getImdbID() { return imdbID; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }

    public String getType() { return Type; }
    public void setType(String type) { Type = type; }

    public String getPoster() { return Poster; }
    public void setPoster(String poster) { Poster = poster; }
}
