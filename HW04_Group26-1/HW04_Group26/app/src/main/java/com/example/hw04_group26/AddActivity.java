package com.example.hw04_group26;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    EditText name, description, year, imdb;
    Spinner genre;
    SeekBar rating;
    Button addmovie;
    TextView rating_value;
    int progressChangedValue = 0;
    int r=0,g=0,y;
    String n,d,i;
    ArrayList<Movie> movie_list = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Add Movie");
        name = (EditText)findViewById(R.id.et_name);
        description = (EditText)findViewById(R.id.et_description);
        year = (EditText)findViewById(R.id.et_year);
        imdb = (EditText)findViewById(R.id.et_imdb);
        genre = (Spinner)findViewById(R.id.sp_genre);
        rating = (SeekBar)findViewById(R.id.seek_rating);
        addmovie = (Button)findViewById(R.id.b_addmovie);
        rating_value = (TextView)findViewById(R.id.tv_rating);
        g=0;r=0;

        Intent w = getIntent();
        movie_list = (ArrayList<Movie>) w.getSerializableExtra("Movies");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(adapter);
        genre.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        rating.setMax(5);
        rating.setProgress(0);
        rating_value.setText("0");
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChangedValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                r = progressChangedValue;

                rating_value.setText(String.valueOf(progressChangedValue));
            }
        });

        addmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    n = name.getText().toString();
                    d = description.getText().toString();
                    y = Integer.parseInt(year.getText().toString());
                    i = imdb.getText().toString();
                    if (n.equals("")) {
                        name.setError("Enter Movie Name");
                    } else if (d.equals("")) {
                        description.setError("Enter Description");
                    } else if (y > Calendar.getInstance().get(Calendar.YEAR)|| year.getText().toString().length()!=4) {
                        year.setError("Enter valid year");
                    } else if (i.equals("")) {
                        imdb.setError("Enter IMDB link");
                    }
                    else if(!URLUtil.isValidUrl(i)){
                        imdb.setError("IMDB Link not valid");
                    } else if (g == 0) {
                        Toast.makeText(AddActivity.this, "Please Select Genre", Toast.LENGTH_SHORT).show();
                    } else {
                        int flag = 0;
                        for (Movie m : movie_list) {
                            if (m.getName().equals(n)) {
                                Toast.makeText(AddActivity.this, "Movie Already exists!!", Toast.LENGTH_SHORT).show();
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            Movie movie = new Movie(n, d, g, r, y, i);
                            Intent in = new Intent();
                            in.putExtra("NewMovie", movie);
                            setResult(RESULT_OK, in);
                            finish();
                        }
                    }
                }
                catch (NumberFormatException e)
                {
                    year.setError("Enter year");
                }

            }
        });

    }

    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {



        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            g = i ;
            Toast.makeText(adapterView.getContext(),""+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }
}
