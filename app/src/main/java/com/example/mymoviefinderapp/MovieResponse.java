package com.example.mymoviefinderapp;

import java.util.List;

public class MovieResponse {
    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String imdbID;
    private String imdbRating;
    private String Metascore;
    private String imdbVotes;
    private String BoxOffice;
    private String Production;
    private String Website;
    private String Poster;
    private List<Rating> Ratings; // Assuming this is a list of ratings
    private List<Movie> Search;

    // Getters and setters
    public List<Movie> getSearch() { return Search; }
    public void setSearch(List<Movie> search) { Search = search; }
    public String getTitle() { return Title; }
    public void setTitle(String title) { Title = title; }

    public String getYear() { return Year; }
    public void setYear(String year) { Year = year; }

    public String getRated() { return Rated; }
    public void setRated(String rated) { Rated = rated; }

    public String getReleased() { return Released; }
    public void setReleased(String released) { Released = released; }

    public String getRuntime() { return Runtime; }
    public void setRuntime(String runtime) { Runtime = runtime; }

    public String getGenre() { return Genre; }
    public void setGenre(String genre) { Genre = genre; }

    public String getDirector() { return Director; }
    public void setDirector(String director) { Director = director; }

    public String getWriter() { return Writer; }
    public void setWriter(String writer) { Writer = writer; }

    public String getActors() { return Actors; }
    public void setActors(String actors) { Actors = actors; }

    public String getPlot() { return Plot; }
    public void setPlot(String plot) { Plot = plot; }

    public String getLanguage() { return Language; }
    public void setLanguage(String language) { Language = language; }

    public String getCountry() { return Country; }
    public void setCountry(String country) { Country = country; }

    public String getAwards() { return Awards; }
    public void setAwards(String awards) { Awards = awards; }

    public String getImdbID() { return imdbID; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }

    public String getImdbRating() { return imdbRating; }
    public void setImdbRating(String imdbRating) { this.imdbRating = imdbRating; }

    public String getMetascore() { return Metascore; }
    public void setMetascore(String metascore) { Metascore = metascore; }

    public String getImdbVotes() { return imdbVotes; }
    public void setImdbVotes(String imdbVotes) { imdbVotes = imdbVotes; }

    public String getBoxOffice() { return BoxOffice; }
    public void setBoxOffice(String boxOffice) { BoxOffice = boxOffice; }

    public String getProduction() { return Production; }
    public void setProduction(String production) { Production = production; }

    public String getWebsite() { return Website; }
    public void setWebsite(String website) { Website = website; }

    public String getPoster() { return Poster; }
    public void setPoster(String poster) { Poster = poster; }

    public List<Rating> getRatings() { return Ratings; }
    public void setRatings(List<Rating> ratings) { Ratings = ratings; }

    // Inner class to represent a Rating
    public static class Rating {
        private String Source;
        private String Value;

        // Getters and setters
        public String getSource() { return Source; }
        public void setSource(String source) { Source = source; }

        public String getValue() { return Value; }
        public void setValue(String value) { Value = value; }
    }
}

