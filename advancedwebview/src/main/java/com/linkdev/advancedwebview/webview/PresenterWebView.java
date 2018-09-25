package com.linkdev.advancedwebview.webview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;

import com.linkdev.advancedwebview.base.PresenterBase;
import com.linkdev.advancedwebview.R;
import com.linkdev.advancedwebview.Utilities;
import com.linkdev.advancedwebview.WebViewHelper;

import java.util.Arrays;
import java.util.List;

import static com.linkdev.advancedwebview.Constants.WEB_VIEW_ENABLE_SHARE_PARAM;
import static com.linkdev.advancedwebview.Constants.WEB_VIEW_TITLE_PARAM;
import static com.linkdev.advancedwebview.Constants.WEB_VIEW_URL_PARAM;

/**
 * Created by Youssef.Wagih on 24-Apr-18.
 */

class PresenterWebView extends PresenterBase {
    private ViewWebView viewWebView;
    private Context context;

    private String url;
    private boolean enableShare;

    PresenterWebView(Context context, ViewWebView viewWebView) {
        this.viewWebView = viewWebView;
        this.context = context;
    }

    void loadWebURL(Bundle bundle) {
        if (bundle != null) {
            url = bundle.getString(WEB_VIEW_URL_PARAM);
            String title = bundle.getString(WEB_VIEW_TITLE_PARAM);
            enableShare = bundle.getBoolean(WEB_VIEW_ENABLE_SHARE_PARAM);

            if (Utilities.CheckIfApplicationIsConnected(context)) {
                viewWebView.showProgress(true);
                if (!TextUtils.isEmpty(url))
                    viewWebView.startWebView(title, url);
                else
                    viewWebView.onError(context.getResources().getString(R.string.something_went_wrong),
                            null, R.drawable.ic_ab_back_mtrl_am_alpha);
            } else
                viewWebView.onError(context.getString(R.string.error_no_internet_connection_title),
                        null, R.drawable.no_internet_connection);
        } else
            viewWebView.onError(context.getResources().getString(R.string.something_went_wrong),
                    null, R.drawable.ic_ab_back_mtrl_am_alpha);
    }

    boolean isShareEnabled() {
        return enableShare;
    }

    AlertDialog uploadFile(WebChromeClient.FileChooserParams fileChooserParams,
                           View.OnClickListener galleryClickListener,
                           View.OnClickListener pdfClickListener,
                           FragmentWebView.UploadDismissed uploadDismissed) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<String> acceptTypes = Arrays.asList(fileChooserParams.getAcceptTypes());

            if (acceptTypes.contains(".pdf") || acceptTypes.contains(".txt") || acceptTypes.contains(".docx"))
                pdfClickListener.onClick(null);
            else if (acceptTypes.contains(".png") || acceptTypes.contains(".jpeg") || acceptTypes.contains(".jpg"))
                galleryClickListener.onClick(null);
            else
                return WebViewHelper.showChooserDialog(context, pdfClickListener, galleryClickListener, uploadDismissed);
        } else
            return WebViewHelper.showChooserDialog(context, pdfClickListener, galleryClickListener, uploadDismissed);
        return null;
    }

    void shareItem() {
        if (!TextUtils.isEmpty(url))
            Utilities.shareItemUrl(context, url);
        else
            viewWebView.showMessage(context.getString(R.string.something_went_wrong));
    }
}
