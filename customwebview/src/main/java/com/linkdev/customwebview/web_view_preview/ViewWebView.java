package com.linkdev.customwebview.web_view_preview;

import android.support.annotation.DrawableRes;

/**
 * Created by Youssef.Wagih on 24-Apr-18.
 */

interface ViewWebView extends ViewBase {
    void onError(String errorMessage, String errorMessageDescription, @DrawableRes int emptyDrawableId);

    void startWebView(String title, String url);
}
