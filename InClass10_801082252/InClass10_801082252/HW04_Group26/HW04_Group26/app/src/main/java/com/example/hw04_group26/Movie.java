package com.example.hw04_group26;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Movie implements Serializable {
        String name;
        String desc;
        int genre;
        int rating;
        int year;
        String imdb;

    public Movie(String name, String desc, int genre, int rating, int year, String imdb) {
        this.name = name;
        this.desc = desc;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", genre=" + genre +
                ", rating=" + rating +
                ", year='" + year + '\'' +
                ", imdb='" + imdb + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getGenre() {
        return genre;
    }

    public int getRating() {
        return rating;
    }

    public int getYear() {
        return year;
    }

    public String getImdb() {
        return imdb;
    }

    public Map toHashmap(){
        Map<String,Object> userMap = new HashMap<>(  );

        userMap.put( "name", this.name );
        userMap.put("desc", this.desc);
        userMap.put("genre", this.genre);
        userMap.put("rating", this.rating);
        userMap.put("year", this.year);
        userMap.put("imdb", this.imdb);
        return userMap;
    }

    public static Comparator<Movie> MovieYearComparator = new Comparator<Movie>() {

        public int compare(Movie m1, Movie m2) {

            int movie1 = m1.getYear();
            int movie2 = m2.getYear();
            return movie1-movie2;

        }};


    public static Comparator<Movie> MovieRatingComparator = new Comparator<Movie>() {

        public int compare(Movie m1, Movie m2) {

            int movie1 = m1.getRating();
            int movie2 = m2.getRating();
            return movie2-movie1;
        }};
}
