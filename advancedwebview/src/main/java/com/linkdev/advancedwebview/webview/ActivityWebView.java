package com.linkdev.advancedwebview.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.linkdev.advancedwebview.R;
import com.linkdev.advancedwebview.base.ActivityBase;

import static com.linkdev.advancedwebview.Constants.WEB_VIEW_ENABLE_SHARE_EXTRA;
import static com.linkdev.advancedwebview.Constants.WEB_VIEW_TITLE_ITEM_EXTRA;
import static com.linkdev.advancedwebview.Constants.WEB_VIEW_URL_ITEM_EXTRA;


public class ActivityWebView extends ActivityBase {
    private Toolbar toolbar;
    private FragmentWebView fragmentWebView;

    public static void startActivity(String url, String title, boolean enableShare, Context context) {
        Intent intent = new Intent(context.getApplicationContext(), ActivityWebView.class);
        intent.putExtra(WEB_VIEW_TITLE_ITEM_EXTRA, title);
        intent.putExtra(WEB_VIEW_URL_ITEM_EXTRA, url);
        intent.putExtra(WEB_VIEW_ENABLE_SHARE_EXTRA, enableShare);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_toolbar_fragment);

        initializeViews();
        setListeners();

        if (getIntent() != null) {
            loadFragment();
        }
    }

    @Override
    protected void initializeViews() {
        ImageView ivLogoToolbar;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void setListeners() {
    }

    @Override
    protected void loadFragment() {
        String url = getIntent().getStringExtra(WEB_VIEW_URL_ITEM_EXTRA);
        String title = getIntent().getStringExtra(WEB_VIEW_TITLE_ITEM_EXTRA);
        boolean enableShare = getIntent().getBooleanExtra(WEB_VIEW_ENABLE_SHARE_EXTRA, false);

        setToolbar(toolbar, title, true, android.R.color.black, true);

        fragmentWebView = FragmentWebView.newInstance(url, title, enableShare);
        addFragmentToActivity(fragmentWebView,
                R.id.frmFragmentContainer,
                "",
                false,
                getSupportFragmentManager(),
                null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        goBackIfAvailable();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            goBackIfAvailable();

        return true;
    }

    private void goBackIfAvailable() {
        if (fragmentWebView != null)
            fragmentWebView.onBackKeyPressed();
    }
}
