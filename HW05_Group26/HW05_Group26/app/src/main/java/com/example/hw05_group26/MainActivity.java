package com.example.hw05_group26;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    ArrayList<Source> result = new ArrayList<>();
    String API_KEY = "a1cc783ff80d40a4a25b057edf948ce4";
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News App");
        pd = new ProgressDialog(this);
        lv = findViewById(R.id.listView);


        if(isConnected()){
            String url = "https://newsapi.org/v2/sources?apiKey="+API_KEY;
            new GetSourcesTask().execute(url);

        }else{
            Toast.makeText(getApplicationContext(),"No Internet Connection!!", Toast.LENGTH_SHORT).show();
        }


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



    private class GetSourcesTask extends AsyncTask<String,Void, ArrayList<Source>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading Sources...");
            pd.show();
        }

        @Override
        protected ArrayList<Source> doInBackground(String... params) {
            HttpURLConnection connection = null;
            Log.d("demo", params[0]);
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray newssource = root.getJSONArray("sources");
                    for (int i=0;i<newssource.length();i++) {
                        JSONObject sourceJson = newssource.getJSONObject(i);
                        Source sourcedata = new Source();
                        if(sourceJson.getString("id") != null ) {
                            sourcedata.id = sourceJson.getString("id");
                        }else {
                            sourcedata.id = "";
                        }
                        if(sourceJson.getString("name") != null ) {
                            sourcedata.name = sourceJson.getString("name");
                        }else {
                            sourcedata.name = "";
                        }
                        result.add(sourcedata);
                    }
                }
            } catch (IOException e) {
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
        protected void onPostExecute(ArrayList<Source> sources) {
            super.onPostExecute(sources);
            pd.dismiss();
            SourceAdapter sourceadapter = new SourceAdapter(getBaseContext(),R.layout.source_item,result);
            lv.setAdapter(sourceadapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent newActivityintent = new Intent(MainActivity.this,NewsActivity.class);
                    newActivityintent.putExtra("NEW_MOVIE_ID", result.get(i).getId());
                    newActivityintent.putExtra("NEW_MOVIE_NAME", result.get(i).getName());
                    startActivityForResult(newActivityintent,100);
                }
            });
        }
    }
}
