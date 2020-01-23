//Group Number 26 Old Number 17
//Gokul Nithin Kumar Rajakumar
//Satish Kumar Balasubramaniam

package com.example.group17_inclass05;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    TextView keyword;
    Button go;
    ImageView image;
    ImageButton next;
    ImageButton prev;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyword = (TextView)findViewById(R.id.tv_keyword);
        go = (Button)findViewById(R.id.b_go);
        image = (ImageView)findViewById(R.id.im_image);
        setTitle("MainActivity");
        next = (ImageButton)findViewById(R.id.imb_next);
        prev = (ImageButton)findViewById(R.id.imb_prev);
        progress = (ProgressBar)findViewById(R.id.pb_progress);
        image.setImageDrawable(null);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected()){
                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                    new GetKeywordAsync(image,prev,next,progress).execute("http://dev.theappsdr.com/apis/photos/keywords.php");
                }
                else{
                    Toast.makeText(MainActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
    private class GetKeywordAsync extends AsyncTask<String, Void, String[]> {
        ImageView image;
        ImageButton next;
        ImageButton prev;
        ProgressBar progress;

        public GetKeywordAsync(ImageView image, ImageButton next, ImageButton prev, ProgressBar progress) {
            this.image = image;
            this.next = next;
            this.prev = prev;
            this.progress = progress;
        }

        String [] keywords_list=null;
        @Override
        protected String[] doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                    keywords_list = result.split(";");
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return keywords_list;
        }



        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            final String[] keywords_list=strings;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose item");

            builder.setItems(keywords_list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    keyword.setText(keywords_list[i]);
//                    String URL="http://dev.theappsdr.com/apis/photos/index.php?keyword="+keywords_list[i];
                    new GetImagesAsync(image,prev,next,progress).execute(keywords_list[i]);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    private class GetImagesAsync extends AsyncTask<String,Void, String[]>{
        ImageView image;
        ImageButton next;
        ImageButton prev;
        ProgressBar progress;
        int counter=0;

        public GetImagesAsync(ImageView image, ImageButton next, ImageButton prev, ProgressBar progress) {
            this.image = image;
            this.next = next;
            this.prev = prev;
            this.progress = progress;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {
            HttpURLConnection connection = null;

            String result = null;
            String [] image_list = null;
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = null;

            try {

                String strUrl = "http://dev.theappsdr.com/apis/photos/index.php?keyword=" + strings[0];
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line+" ");
                    }
                    result = stringBuilder.toString();
                    image_list = result.split(" ");


                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return image_list;

        }

        @Override
        protected void onPostExecute(final String[] strings) {
            super.onPostExecute(strings);
            progress.setVisibility(View.INVISIBLE);
            new SetImageAsync(image,progress).execute(strings[counter]);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(counter == strings.length-1){
                        counter =0;
                    }
                    else{
                        ++counter;
                    }
                    new SetImageAsync(image,progress).execute(strings[counter]);
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(counter == 0){
                        counter = strings.length-1;
                    }
                    else{
                        --counter;
                    }
                    new SetImageAsync(image,progress).execute(strings[counter]);
                }
            });


        }
    }

}
