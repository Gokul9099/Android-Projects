package com.example.hw04_group26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MovieYearActivity extends AppCompatActivity {
    TextView name,description,genre,rating,year,imdb;
    ImageView first,prev,next,last;
    Button finish;
    ArrayList<Movie> movie_list = new ArrayList<Movie>();
    String[] genre_array = {"Select","Action","Animation","Comedy","Documentary","Family","Horror","Crime","Others"};
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_year);
        setTitle("Movies by Year");
        name = (TextView)findViewById(R.id.tvy_name);
        description = (TextView)findViewById(R.id.tvy_description);
        genre = (TextView)findViewById(R.id.tvy_genre);
        rating = (TextView)findViewById(R.id.tvy_rating);
        imdb = (TextView)findViewById(R.id.tvy_imdb);
        year = (TextView)findViewById(R.id.tvy_year);
        first = (ImageView)findViewById(R.id.imy_first);
        prev = (ImageView)findViewById(R.id.imy_prev);
        next = (ImageView)findViewById(R.id.imy_next);
        last = (ImageView)findViewById(R.id.imy_last);
        finish = (Button)findViewById(R.id.by_finish);

        Intent w = getIntent();
        movie_list = (ArrayList<Movie>) w.getSerializableExtra("Movies");

        Collections.sort(movie_list,Movie.MovieYearComparator);
        get_details(count);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=0;
                get_details(count);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count < movie_list.size()-1){
                    ++count;
                    get_details(count);
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count > 0){
                    --count;
                    get_details(count);
                }
            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = movie_list.size()-1;
                get_details(count);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void get_details(int c){
        Movie movie = movie_list.get(c);
        name.setText(movie.getName());
        description.setText(movie.getDesc());
        description.setMovementMethod(new ScrollingMovementMethod());
        genre.setText(genre_array[movie.getGenre()]);
        rating.setText(movie.getRating()+" / 5");
        year.setText(String.valueOf(movie.getYear()));
        imdb.setText(movie.getImdb());
    }
}
