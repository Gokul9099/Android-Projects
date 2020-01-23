//Group 26
//Gokul Nithin Kumar Rajakumar (801082252)
//Satish Kumar Balasubramanian (801114629)

package com.example.group26_inclass06;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
TextView category, title, published, description,outof;
Button go;
ImageView image,prev,next;
ProgressDialog pd,pd_image;
String url_link;
String ApiKey= "a1cc783ff80d40a4a25b057edf948ce4";
int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        category = (TextView)findViewById(R.id.tv_category);
        title = (TextView)findViewById(R.id.tv_title);
        published = (TextView)findViewById(R.id.tv_published);
        description = (TextView)findViewById(R.id.tv_description);
        outof = (TextView)findViewById(R.id.tv_outof);
        image = (ImageView)findViewById(R.id.im_image);
        prev = (ImageView)findViewById(R.id.im_prev);
        next = (ImageView)findViewById(R.id.im_next);
        go = (Button)findViewById(R.id.b_go);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Loading..");
        pd_image = new ProgressDialog(MainActivity.this);
        pd_image.setMessage("Loading Image..");

        prev.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        image.setImageResource(android.R.color.transparent);

        final String [] categories = {"business", "entertainment", "general", "health", "science", "sports", "technology"};
        description.setMovementMethod(new ScrollingMovementMethod());

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
//                    Toast.makeText(MainActivity.this, "Internet Connection", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder build1 = new AlertDialog.Builder(MainActivity.this);
                    build1.setTitle("Select Category");
                    build1.setCancelable(true);

                    build1.setItems(categories, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(MainActivity.this, categories[i], Toast.LENGTH_SHORT).show();
                            category.setText(categories[i]);
                            url_link = "https://newsapi.org/v2/top-headlines?category="+categories[i]+"&apiKey="+ApiKey;
                            new GetSimpleAsync().execute(url_link);
                        }
                    });
                    build1.show();

                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
    private class GetSimpleAsync extends AsyncTask<String, Void, ArrayList<Article>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            count=0;
            pd.show();
        }

        @Override
        protected ArrayList<Article> doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            ArrayList<Article> result = new ArrayList<Article>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray articles = root.getJSONArray("articles");
                    for (int i=0;i<articles.length();i++) {
                        JSONObject articleJson = articles.getJSONObject(i);
                        Article article = new Article();
                        article.title = articleJson.getString("title");
                        article.description = articleJson.getString("description");
                        article.urlToImage = articleJson.getString("urlToImage");
                        article.publishedAt = articleJson.getString("publishedAt");
                        result.add(article);
                    }
                }
            }
            catch (MalformedURLException e) {
//                Log.d("demo","MalformedURLException");
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.d("demo","IOException1");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<Article> result) {
            pd.dismiss();
            if (result != null) {
                if(result.size()>1){
                    prev.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                }

                set_details(result.get(0),result.size());
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(count>0){
                            --count;
                        }
                        else{
                            count=result.size()-1;
                        }
                        set_details(result.get(count),result.size());
                    }
                });

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(count< result.size()-1){
                            ++count;
                        }
                        else{
                            count=0;
                        }
                        set_details(result.get(count),result.size());
                    }
                });

            } else {
                reset_details();
                Toast.makeText(MainActivity.this, "No News Found!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void reset_details(){
        title.setText("");
        published.setText("");
        description.setText("");
        outof.setText("");
        image.setImageResource(android.R.color.transparent);
    }
    public  void set_details(Article result, int length){
        title.setText(result.getTitle());
        published.setText(result.getPublishedAt());
        if(result.getDescription()=="null"){
            description.setText("");
            Toast.makeText(this, "Description Not Available!", Toast.LENGTH_SHORT).show();
        }else{
            description.setText(result.getDescription());
        }
        outof.setText(count+1 +" out of "+ length);
        new GetImageAsync().execute(result.getUrlToImage());
//        Picasso.get().load(result.getUrlToImage()).into(image);
    }

    private class GetImageAsync extends AsyncTask<String, Void, Void> {
        Bitmap bitmap = null;

        public GetImageAsync() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd_image.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            bitmap = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd_image.dismiss();
            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            }
            else{
                image.setImageResource(android.R.color.transparent);
                Toast.makeText(MainActivity.this, "Image not Available!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
