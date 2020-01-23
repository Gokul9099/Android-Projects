package com.example.midterm_801082252;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    ArrayList<City> result = new ArrayList<City>();
    ArrayAdapter<City> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        setTitle("Select City");
        listview = (ListView)findViewById(R.id.listView);
        new GetSimpleAsync().execute();
    }

    private class GetSimpleAsync extends AsyncTask<String,Void, ArrayList<City>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<City> doInBackground(String... params) {
            try {
                JSONObject root = new JSONObject(loadJSONFromAsset(this));
                JSONArray data_list = root.getJSONArray("data");
                    for (int i=0;i<data_list.length();i++) {
                        JSONObject dataJson = data_list.getJSONObject(i);
                        City citydata = new City();
                        if(dataJson.getString("city") != null ) {
                            citydata.city= dataJson.getString("city");
                        }else {
                            citydata.city = "";
                        }
                        if(dataJson.getString("country") != null ) {
                            citydata.country = dataJson.getString("country");
                        }else {
                            citydata.country = "";
                        }
                        result.add(citydata);
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<City> cities) {
            super.onPostExecute(cities);
            adapter = new ArrayAdapter<City>(MainActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1,result);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText( MainActivity.this, result.get(position)+"", Toast.LENGTH_SHORT ).show();
                    City chosenSource =  result.get(position);
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("CITY_KEY", chosenSource );
                    startActivity(intent);
                }
            });
        }
    }

    public String loadJSONFromAsset(GetSimpleAsync context) {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.cities);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}


