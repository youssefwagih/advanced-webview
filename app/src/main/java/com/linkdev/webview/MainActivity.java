package com.linkdev.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.linkdev.advancedwebview.webview.ActivityWebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityWebView.startActivity("https://www.google.com.eg/", "title", true, this);
    }
}
