package com.example.midterm_801082252;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {
    TextView citycountry, temp,max_temp,min_temp,desc,humidity,wind;
    Weather result;
    ArrayList<Forecast> resultss = new ArrayList<Forecast>();
    Button forecast;
    ImageView im_city;
    Weather weatherdata = new Weather();
    RecyclerView recyclerViews;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
City chosenCity;
String city,country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_weather );
        citycountry = (TextView)findViewById(R.id.tv_city);
        temp = (TextView)findViewById(R.id.tv_temp);
        max_temp = (TextView)findViewById(R.id.tv_temp_max);
        min_temp = (TextView)findViewById(R.id.tv_temp_min);
        desc = (TextView)findViewById(R.id.tv_desc);
        humidity = (TextView)findViewById(R.id.tv_humidity);
        forecast = (Button)findViewById( R.id.b_forecast );
        wind = (TextView)findViewById(R.id.tv_wind);
        im_city = (ImageView)findViewById(R.id.im_weather);



        forecast.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()){
                    String urla = "http://api.openweathermap.org/data/2.5/forecast?q="+city+","+country+"&apiKey=2b61944621b39423ac158b07164a389c";
                    new GetForecastAsync().execute(urla);
                }else{
                    Toast.makeText(getApplicationContext(),"No Internet Connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        } );


        if(getIntent().getExtras().containsKey("CITY_KEY")){
            chosenCity = (City) getIntent().getExtras().getSerializable("CITY_KEY");
            city=chosenCity.getCity();
            country = chosenCity.getCountry();
            setTitle("Current Weather");
            citycountry.setText(city+","+country);
//            Toast.makeText(this, ""+city, Toast.LENGTH_SHORT).show();
            if(isConnected()){
                String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+","+country+"&apikey=1048b53d170151e2a43a078076321909";
                new GetSimpleAsync().execute(url);
            }else{
                Toast.makeText(getApplicationContext(),"No Internet Connection!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
    private class GetSimpleAsync extends AsyncTask<String,Void, Weather> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Weather doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                Log.d("demo",params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    Log.d("demo",root+"");
                    JSONObject mains = root.getJSONObject("main");
                    if(mains.getString("temp") != null ) {
                        weatherdata.temp = mains.getString("temp");
                    }else {
                        weatherdata.temp = "";
                    }
                    if(mains.getString("temp_min") != null ) {
                        weatherdata.temp_min = mains.getString("temp_min");
                    }else {
                        weatherdata.temp_min = "";
                    }
                    if(mains.getString("temp_max") != null ) {
                        weatherdata.temp_max = mains.getString("temp_max");
                    }else {
                        weatherdata.temp_max = "";
                    }
                    if(mains.getString("humidity") != null ) {
                        weatherdata.humidity = mains.getString("humidity");
                    }else {
                        weatherdata.humidity = "";
                    }
                    JSONArray weather_list = root.getJSONArray("weather");
                    JSONObject weatherJson = weather_list.getJSONObject(0);
                    if(weatherJson.getString("description") != null ) {
                        weatherdata.description = weatherJson.getString("description");
                    }else {
                        weatherdata.description = "";
                    }
                    if(weatherJson.getString("icon") != null ) {
                        weatherdata.icon = weatherJson.getString("icon");
                    }else {
                        weatherdata.icon = "";
                    }
                    JSONObject winds = root.getJSONObject("wind");
                    if(winds.getString("speed") != null ) {
                        weatherdata.speed = winds.getString("speed");
                    }else {
                        weatherdata.speed = "";
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
            return weatherdata;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            Picasso.get().load("http://openweathermap.org/img/wn/"+weather.icon+"@2x.png").into(im_city);
            temp.setText("Temperature: " + weather.temp + "F");
            max_temp.setText("Temperature Max: " + weather.temp_max + "F");
            min_temp.setText("Temperature Min: " + weather.temp_min + "F" );
            desc.setText("Description: " + weather.description );
            humidity.setText("Humidity: " + weather.humidity + "%");
            wind.setText("Wind Speed: " + weather.speed + "miles/hr" );
        }
    }





    private class GetForecastAsync extends AsyncTask<String,Void, ArrayList<Forecast>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Forecast> doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray results_list = root.getJSONArray("list");
                    for (int i=0;i<results_list.length();i++) {
                        Forecast forecastdata = new Forecast();
                        JSONObject resultJson = results_list.getJSONObject(i);
                        JSONArray weather_list = resultJson.getJSONArray("weather");
                        JSONObject wJson = weather_list.getJSONObject(0);

                        if(wJson.getString("description") != "null" ) {
                            forecastdata.description = wJson.getString("description");
                        }else {
                            forecastdata.description = "";
                        }
                        if(wJson.getString("icon") != "null" ) {
                            forecastdata.icon = wJson.getString("icon");
                        }else {
                            forecastdata.icon = "";
                        }

                        JSONObject mJson = resultJson.getJSONObject("main");
                        if(mJson.getString("temp") != "null" ) {
                            forecastdata.temp = mJson.getString("temp");
                        }else {
                            forecastdata.temp = "";
                        }
                        if(mJson.getString("humidity") != "null" ) {
                            forecastdata.humidity = mJson.getString("humidity");
                        }else {
                            forecastdata.humidity = "";
                        }
                        JSONObject sJson = resultJson.getJSONObject("sys");
                        if(sJson.getString("dt_txt") != null ) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date date = format.parse(sJson.getString("dt_txt"));
                                PrettyTime p  = new PrettyTime();
                                String datetime= (String) p.format(date);
                                forecastdata.dt_txt = datetime;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            forecastdata.dt_txt = "";
                        }
                        Log.d( "demooo",forecastdata+"" );
                        resultss.add(forecastdata);

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
            return resultss;
        }

        @Override
        protected void onPostExecute(ArrayList<Forecast> forecasts) {
            super.onPostExecute(forecasts);
            mAdapter = new Recycler(resultss);
            recyclerViews.setAdapter(mAdapter);
        }
    }

}
