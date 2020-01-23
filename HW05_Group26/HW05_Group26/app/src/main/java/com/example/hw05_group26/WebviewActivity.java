package com.example.hw05_group26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("NEWS_IMAGE_URL")) {
                if (isConnected()) {
                    WebView w = (WebView) findViewById(R.id.webview);
                    w.getSettings().setJavaScriptEnabled(true);
                    w.loadUrl(getIntent().getExtras().getString("NEWS_IMAGE_URL"));
                    w.setWebViewClient(new WebViewClient());
                }
            }
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
}
