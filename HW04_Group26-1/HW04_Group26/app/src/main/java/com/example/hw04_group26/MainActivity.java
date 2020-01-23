//Group Number 26 Old Number 17
//Gokul Nithin Kumar Rajakumar
//Satish Kumar Balasubramaniam

package com.example.hw04_group26;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button add,edit,delete,year,rating;
    ArrayList<Movie> movie_list = new ArrayList<Movie>();
    ArrayAdapter<String> adapter;
    Movie selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Favorite Movies");
        setContentView(R.layout.activity_main);
        add = (Button)findViewById(R.id.b_add);
        edit = (Button)findViewById(R.id.b_edit);
        delete = (Button)findViewById(R.id.b_delete);
        year = (Button)findViewById(R.id.b_year);
        rating = (Button)findViewById(R.id.b_rating);
        adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.select_dialog_item);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,AddActivity.class);
                i.putExtra("Movies",movie_list);
                startActivityForResult(i,1001);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getCount()==0){
                    Toast.makeText(MainActivity.this, "Movies List Empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Pick a Movie");
                    dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            for (int ia=0;ia<movie_list.size();ia++){
                                if(movie_list.get(ia).getName().equals(adapter.getItem(i))){
//                                    int index = movie_list.indexOf();
                                    selected = movie_list.get(ia);
                                    movie_list.remove(selected);
                                    adapter.remove(selected.getName());
                                }
                            }
                            Intent q =new Intent(MainActivity.this,EditActivity.class);
                            q.putExtra("Selected_Movie",selected);
                            q.putExtra("Movies",movie_list);
                            startActivityForResult(q,1002);
                        }
                    });
                    dialog.show();
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getCount()==0){
                    Toast.makeText(MainActivity.this, "Movies List Empty! ", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Pick a Movie");
                    dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (Movie m: movie_list){
                                if(m.getName().equals(adapter.getItem(i))){
                                    int index = movie_list.indexOf(m);
                                    selected = movie_list.get(index);
                                    Toast.makeText(MainActivity.this, selected.getName()+" has been deleted successfully", Toast.LENGTH_SHORT).show();
                                    movie_list.remove(selected);
                                    adapter.remove(selected.getName());
                                }
                            }
                        }
                    });
                    dialog.show();
                }

            }
        });

        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setAction("com.example.hw04_group26.intent.action.FILTER_BY_YEAR");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("Movies",movie_list);
                startActivity(intent);
            }
        });
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setAction("com.example.hw04_group26.intent.action.FILTER_BY_RATING");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("Movies",movie_list);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1001){
            if(resultCode==RESULT_OK && data.getExtras().containsKey("NewMovie")){
                Movie movie = (Movie) data.getExtras().getSerializable("NewMovie");
                movie_list.add(movie);
                adapter.add(movie.getName());
            }
        }
        if(requestCode == 1002){
            if(resultCode == RESULT_OK && data.getExtras().containsKey("EditMovie")){
                Movie movie = (Movie) data.getExtras().getSerializable("EditMovie");
                movie_list.add(movie);
                adapter.add(movie.getName());
            }
        }
    }
}
