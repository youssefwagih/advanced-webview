package com.linkdev.advancedwebview.webview;

import android.support.annotation.DrawableRes;

import com.linkdev.advancedwebview.base.ViewBase;

/**
 * Created by Youssef.Wagih on 24-Apr-18.
 */

interface ViewWebView extends ViewBase {
    void onError(String errorMessage, String errorMessageDescription, @DrawableRes int emptyDrawableId);

    void startWebView(String title, String url);
}
