package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public String loadJSONFromRaw(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("trip.json");
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadJSONFromRaw(MapsActivity.this);
        ArrayList<Points> cityList = new ArrayList<>();
        List<Marker> markersList = new ArrayList<Marker>();


        String cityJson = loadJSONFromRaw(this);
        JSONObject root = null;
        try {
            root = new JSONObject(cityJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray cityArray = null;
        try {
            cityArray = root.getJSONArray("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < cityArray.length(); i++) {
            try {
                JSONObject sourceJson = cityArray.getJSONObject(i);
                Points cityObject = new Points();
                if(sourceJson.getString("latitude") != null ) {
                    cityObject.Lat = Double.parseDouble(sourceJson.getString("latitude"));
                }else {
                    cityObject.Lat = 0.0;
                }
                if(sourceJson.getString("longitude") != null ) {
                    cityObject.Long = Double.parseDouble(sourceJson.getString("longitude"));
                }else {
                    cityObject.Long = 0.0;
                }
                cityList.add(cityObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayList<LatLng> LatLongArray = new ArrayList<LatLng>();
        final PolylineOptions polylineoptions = new PolylineOptions();
        markersList.add(mMap.addMarker(new MarkerOptions().position(new LatLng(cityList.get(0).getLat(), cityList.get(0).getLong())).title("Marker First")));

        markersList.add(mMap.addMarker(new MarkerOptions().position(new LatLng(cityList.get(cityList.size()-1).getLat(), cityList.get(cityList.size()-1).getLong())).title("Marker Last")));
        for(int i = 0 ; i < cityList.size() ; i++){
            LatLng lg = new LatLng(cityList.get(i).getLat(),cityList.get(i).getLong());
            LatLongArray.add(lg);
            polylineoptions.add(lg);

        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng m : LatLongArray) {
            builder.include(m);
        }
        int padding = 50;
        LatLngBounds bounds = builder.build();
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                mMap.animateCamera(cu);
                mMap.addPolyline(polylineoptions);

            }
        });

    }
}
