package com.example.group17_inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetImageAsync extends AsyncTask<String, Void, Bitmap> {
    ImageView image;
    Bitmap bitmap = null;
    ProgressBar progress;

    public SetImageAsync(ImageView image, ProgressBar progress) {
        this.image = image;
        this.progress = progress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress.setVisibility(View.INVISIBLE);
        image.setImageDrawable(null);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpURLConnection connection = null;
        bitmap = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Close open connection
            if (connection != null) {
                connection.disconnect();
            }

        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        progress.setVisibility(View.INVISIBLE);
        if (bitmap != null && image != null) {
            image.setImageBitmap(bitmap);
        }

    }
}
