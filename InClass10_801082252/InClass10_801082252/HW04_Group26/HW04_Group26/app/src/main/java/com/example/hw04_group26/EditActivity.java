package com.example.hw04_group26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    EditText name, description, year, imdb;
    Spinner genre;
    SeekBar rating;
    Button savechanges;
    TextView rating_value;
    int progressChangedValue = 0;
    int r=0,g=0,y;
    String n,d,i,TAG="demo";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Movie selected;
    ArrayList<Movie> movie_list = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Movie");
        setContentView(R.layout.activity_edit);
        name = (EditText)findViewById(R.id.et1_name);
        description = (EditText)findViewById(R.id.et1_description);
        year = (EditText)findViewById(R.id.et1_year);
        imdb = (EditText)findViewById(R.id.et1_imdb);
        genre = (Spinner)findViewById(R.id.sp1_genre);
        rating = (SeekBar)findViewById(R.id.seek1_rating);
        savechanges = (Button)findViewById(R.id.b_save);
        rating_value = (TextView)findViewById(R.id.tv1_rating);
        Intent w = getIntent();
        movie_list = (ArrayList<Movie>) w.getSerializableExtra("Movies");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(adapter);
        genre.setOnItemSelectedListener(new EditActivity.CustomOnItemSelectedListener());

        rating.setMax(5);
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

        Intent in = getIntent();
        selected = (Movie) in.getSerializableExtra("Selected_Movie");
        name.setText(selected.getName());
        description.setText(selected.getDesc());
        year.setText(String.valueOf(selected.getYear()));
        imdb.setText(selected.getImdb());
        rating.setProgress(selected.getRating());
        genre.setSelection(selected.getGenre());
        rating_value.setText(String.valueOf(selected.getRating()));

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                db.collection("movies").document(selected.getName())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

            }
        });

        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                n = name.getText().toString();
                d = description.getText().toString();
                y = Integer.parseInt(year.getText().toString());
                i = imdb.getText().toString();
                if(n.equals("")){
                    name.setError("Enter Movie Name");
                }
                else if (d.equals("")){
                    description.setError("Enter Description");
                } else if(String.valueOf(y)==null){
                    year.setError("Enter year");
                } else if(y > Calendar.getInstance().get(Calendar.YEAR)|| year.getText().toString().length()!=4){
                    year.setError("Enter valid year");
                } else if(i.equals("")){
                    imdb.setError("Enter IMDB link");
                } else if(!URLUtil.isValidUrl(i)){
                    imdb.setError("IMDB Link not valid");
                } else if(g==0){
                    Toast.makeText(EditActivity.this, "Please Select Genre", Toast.LENGTH_SHORT).show();
                }
                else{
                    int flag=0;
                    for(Movie m: movie_list){
                        if(m.getName().equals(n)){
                            Toast.makeText(EditActivity.this, "Movie Already exists!!", Toast.LENGTH_SHORT).show();
                            flag=1;
                        }
                    }
                    if(flag == 0){
                        Movie movie = new Movie(n,d,g,r,y,i);
                        Intent in = new Intent();
                        in.putExtra("EditMovie",movie);
                        setResult(RESULT_OK,in);
                        finish();
                    }
                }
            }
        });
    }
    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            g=i;
            Toast.makeText(adapterView.getContext(),""+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }
}

