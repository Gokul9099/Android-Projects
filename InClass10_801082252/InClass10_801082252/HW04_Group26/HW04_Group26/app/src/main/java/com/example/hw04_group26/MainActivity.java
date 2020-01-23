//Group Number 26 Old Number 17
//Gokul Nithin Kumar Rajakumar
//Satish Kumar Balasubramaniam

package com.example.hw04_group26;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button add,edit,delete,year,rating;
    int count = 0;
    ArrayList<Movie> movie_list = new ArrayList<Movie>();
    ArrayAdapter<String> adapter;
    Movie selected;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "demo";
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
                db.collection("movies")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    adapter.clear();
                                    final QuerySnapshot querySnapshot = task.getResult();
                                    count = querySnapshot.size();
                                    for (DocumentSnapshot document: querySnapshot.getDocuments()) {
                                        adapter.add( document.getId() );
                                    }
                                    if(adapter.getCount()==0){
                                        Toast.makeText( MainActivity.this, "Movie List Empty!", Toast.LENGTH_SHORT ).show();
                                    }
                                    else {


                                        AlertDialog.Builder dialog = new AlertDialog.Builder( MainActivity.this );
                                        dialog.setTitle( "Pick a Movie" );
                                        dialog.setAdapter( adapter, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, final int i) {
                                                //                                        System.out.println(document.getId());
                                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                                    if (document.getId().equals( adapter.getItem( i ) )) {
                                                        runOnUiThread( new Runnable() {

                                                            @Override
                                                            public void run() {
                                                                DocumentReference docRef = db.collection( "movies" ).document( adapter.getItem( i ) );
                                                                docRef.get().addOnSuccessListener( new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        int genre = 0, rating = 0, year = 0;
                                                                        String desc = documentSnapshot.getString( "desc" );
                                                                        String imdb = documentSnapshot.getString( "imdb" );
                                                                        String name = documentSnapshot.getString( "name" );
                                                                        try {
                                                                            genre = (int) (long) documentSnapshot.getData().get( "genre" );
                                                                            rating = (int) (long) documentSnapshot.getData().get( "rating" );
                                                                            year = (int) (long) documentSnapshot.getData().get( "year" );
                                                                        } catch (ClassCastException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        selected = new Movie( name, desc, genre, rating, year, imdb );
                                                                    }
                                                                } );
                                                            }
                                                        } );
                                                    }
                                                }
                                                for (int ia = 0; ia < movie_list.size(); ia++) {
                                                    if (movie_list.get( ia ).getName().equals( adapter.getItem( i ) )) {
                                                        selected = movie_list.get( ia );
                                                        movie_list.remove( selected );
                                                        adapter.remove( adapter.getItem( i ) );
                                                    }
                                                }

                                                Log.d( TAG, "Selected: " + selected );
//


                                                Intent q = new Intent( MainActivity.this, EditActivity.class );
                                                q.putExtra( "Selected_Movie", selected );
                                                q.putExtra( "Movies", movie_list );
                                                startActivityForResult( q, 1002 );

                                            }

                                        } );
                                        dialog.show();
//                                    }


                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

//                if(adapter.getCount()==0){
//                    Toast.makeText(MainActivity.this, "Movies List Empty!", Toast.LENGTH_SHORT).show();
//                }


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        db.collection("movies")
                                .get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    adapter.clear();
                                    final QuerySnapshot querySnapshot = task.getResult();
                                    count = querySnapshot.size();
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                     adapter.add( document.getId() );}
                                }
                            }
                        });
                    }
                });
                if (adapter.getCount()==0){
                    Toast.makeText(MainActivity.this, "Movies List Empty! ", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Pick a Movie");
                    dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, final int i) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    db.collection( "movies" ).document( adapter.getItem( i ) )
                                            .delete()
                                            .addOnSuccessListener( new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d( TAG, "DocumentSnapshot successfully deleted!" );
                                                    Toast.makeText( MainActivity.this, selected.getName() + " has been deleted successfully", Toast.LENGTH_SHORT ).show();
                                                    adapter.remove( selected.getName() );
                                                }
                                            }
                                            )
                                            .addOnFailureListener( new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w( TAG, "Error deleting document", e );
                                                }
                                            } );
                                }
                            });

//                            for (int ia=0;ia<movie_list.size();ia++){
//                                if(movie_list.get(ia).getName().equals(adapter.getItem(i))){
//                                    int index = movie_list.indexOf(m);
//                                    selected = movie_list.get(ia);
//                                    movie_list.remove( selected );
//
//
//                                }
//                            }
                        }
                    });
                    dialog.show();
                }


            }
        });

        year.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        db.collection("movies")
                                .orderBy( "year", Query.Direction.ASCENDING)
                                .get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    adapter.clear();
                                    final QuerySnapshot querySnapshot = task.getResult();
                                    count = querySnapshot.size();
                                    movie_list.clear();
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        int genre=0,rating=0,year=0;
                                        String desc = document.getString("desc");
                                        String imdb = document.getString("imdb");
                                        String name = document.getString("name");
                                        try{
                                            genre = (int)(long)document.getData().get( "genre" );
                                            rating = (int)(long)document.getData().get( "rating" );
                                            year = (int)(long) document.getData().get( "year" );
                                        }catch (ClassCastException e){
                                            e.printStackTrace();
                                        }
                                        movie_list.add(new Movie(name,desc,genre,rating,year,imdb ));
                                    }
                                }
                            }
                        });

                    }
                });
                if(movie_list.size()==0){
                    Toast.makeText( MainActivity.this, "Movie List Empty!", Toast.LENGTH_SHORT ).show();
                }
                else{
                    Intent intent =new Intent();
                    intent.setAction("com.example.hw04_group26.intent.action.FILTER_BY_YEAR");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.putExtra("Movies",movie_list);
                    startActivity(intent);
                }

            }
        });
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        db.collection("movies")
                                .orderBy( "rating", Query.Direction.DESCENDING)
                                .get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    adapter.clear();
                                    final QuerySnapshot querySnapshot = task.getResult();
                                    count = querySnapshot.size();
                                    movie_list.clear();
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        int genre=0,rating=0,year=0;
                                        String desc = document.getString("desc");
                                        String imdb = document.getString("imdb");
                                        String name = document.getString("name");
                                        try{
                                            genre = (int)(long)document.getData().get( "genre" );
                                            rating = (int)(long)document.getData().get( "rating" );
                                            year = (int)(long) document.getData().get( "year" );
                                        }catch (ClassCastException e){
                                            e.printStackTrace();
                                        }
                                        movie_list.add(new Movie(name,desc,genre,rating,year,imdb ));
                                    }
                                }
                            }
                        });

                    }
                });
                if(movie_list.size()==0){
                    Toast.makeText( MainActivity.this, "Movie List Empty!", Toast.LENGTH_SHORT ).show();
                }
                else{
                    Intent intent =new Intent();
                    intent.setAction("com.example.hw04_group26.intent.action.FILTER_BY_RATING");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.putExtra("Movies",movie_list);
                    startActivity(intent);
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1001){
            if(resultCode==RESULT_OK && data.getExtras().containsKey("NewMovie")){
                Movie movie = (Movie) data.getExtras().getSerializable("NewMovie");
//                Map<String, Object> movies = new HashMap<>();
                Map<String,Object> movies = movie.toHashmap();
//                movies.put("name", movie.getName());
//                movies.put("year", movie.getYear());
//                movies.put("genre", movie.getGenre());
//                movies.put("rating", movie.getRating());
//                movies.put("desc", movie.getDesc());
//                movies.put("imdb", movie.getImdb());
                Log.d( TAG,"Movies:  " + movies );
                db.collection( "movies" ).document(movie.getName())
                        .set(movies)
                        .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document added");
                    }
                })
                        .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
                movie_list.add(movie);
                adapter.add(movie.getName());
            }
        }
        if(requestCode == 1002){
            if(resultCode == RESULT_OK && data.getExtras().containsKey("EditMovie")){
                Movie movie = (Movie) data.getExtras().getSerializable("EditMovie");
                Map<String, Object> movies = new HashMap<>();

                movies.put("name", movie.getName());
                movies.put("year", movie.getYear());
                movies.put("genre", movie.getGenre());
                movies.put("rating", movie.getRating());
                movies.put("desc", movie.getDesc());
                movies.put("imdb", movie.getImdb());
                Log.d( TAG,"Movies:  " + movies );
                db.collection( "movies" ).document(movie.getName())
                        .set(movies).addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document added");
                    }
                }).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
                movie_list.add(movie);
                adapter.add(movie.getName());
            }
        }
    }
}
