package com.linkdev.advancedwebview.webview;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkdev.advancedwebview.Constants;
import com.linkdev.advancedwebview.R;
import com.linkdev.advancedwebview.WebViewHelper;
import com.linkdev.advancedwebview.permissions.PermissionsHandlerFragment;
import com.linkdev.advancedwebview.permissions.PermissionsListener;

import java.io.File;

import static com.linkdev.advancedwebview.Constants.WEB_VIEW_ENABLE_SHARE_PARAM;
import static com.linkdev.advancedwebview.Constants.WEB_VIEW_URL_PARAM;
import static com.linkdev.advancedwebview.Utilities.checkAndOpenAvailableApp;
import static com.linkdev.advancedwebview.WebViewHelper.FILE_REQUEST_CODE;
import static com.linkdev.advancedwebview.WebViewHelper.GALLERY_REQUEST_CODE;
import static com.linkdev.advancedwebview.WebViewHelper.getGalleryIntent;

//import com.nbsp.materialfilepicker.ui.FilePickerActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentWebView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentWebView extends PermissionsHandlerFragment implements ViewWebView, PermissionsListener {
    private PresenterWebView presenterWebView;

    private RelativeLayout progressBar;
    private WebView webView;
    private AlertDialog alertDialog;
    private CardView cardEmptyState;
    private ImageView ivEmptyState;
    private TextView txtEmptyStateTitle;
    private TextView txtEmptyStateDescription;

    private WebChromeClient.FileChooserParams mfileChooserParams;
    private ValueCallback<Uri[]> mFilePathCallback;
    private Context context;

    public FragmentWebView() {
        // Required empty public constructor
    }

    public static FragmentWebView newInstance(String url, String title, boolean enableShare) {
        FragmentWebView fragment = new FragmentWebView();
        Bundle args = new Bundle();
        args.putString(WEB_VIEW_URL_PARAM, url);
        args.putString(Constants.WEB_VIEW_TITLE_PARAM, title);
        args.putBoolean(WEB_VIEW_ENABLE_SHARE_PARAM, enableShare);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenterWebView = new PresenterWebView(context, this);
        presenterWebView.loadWebURL(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        initializeViews(view);
        return view;
    }

    @Override
    public void onError(String errorMessage, String errorMessageDescription, int emptyDrawableId) {
        showProgress(false);
        webView.setVisibility(View.GONE);
        cardEmptyState.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(errorMessageDescription)) {
            txtEmptyStateDescription.setVisibility(View.VISIBLE);
            txtEmptyStateDescription.setText(errorMessageDescription);
        } else
            txtEmptyStateDescription.setVisibility(View.GONE);

        ivEmptyState.setImageResource(emptyDrawableId);
        txtEmptyStateTitle.setText(errorMessage);
    }

    @Override
    public void startWebView(String title, String url) {

        // this setting to enable javascript in webview
        webView.getSettings().setJavaScriptEnabled(true);

        // these settings to allow access files from device
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);

        // these settings to allow caching and storage in webview
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setAppCacheEnabled(false);

        webView.setWebViewClient(webViewClientListener);
        // upload file
        webView.setWebChromeClient(webChromeClient);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            WebView.setWebContentsDebuggingEnabled(true);

        webView.setVisibility(View.VISIBLE);

        WebView.setWebContentsDebuggingEnabled(true);

        webView.loadUrl(url);
    }

    @Override
    protected void initializeViews(View v) {
        webView = (WebView) v.findViewById(R.id.webView);
        progressBar = v.findViewById(R.id.progressBar);

        cardEmptyState = (CardView) v.findViewById(R.id.cardEmptyState);
        ivEmptyState = (ImageView) v.findViewById(R.id.ivEmptyState);
        txtEmptyStateTitle = (TextView) v.findViewById(R.id.txtEmptyStateTitle);
        txtEmptyStateDescription = (TextView) v.findViewById(R.id.txtEmptyStateDescription);

        setHasOptionsMenu(true);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void showProgress(boolean show) {
        if (progressBar != null) {
            if (show)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onDestroy() {
        // clear cookies
        clearCookies();

        super.onDestroy();
    }

    private void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().removeSessionCookies(null);
        }
    }

    private void showWebView() {
        webView.setVisibility(View.VISIBLE);
        showProgress(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode != GALLERY_REQUEST_CODE && requestCode != FILE_REQUEST_CODE) || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        boolean pdf = false;
        Uri[] results = null;
        String dataString = null;
        if (data != null) {
            switch (requestCode) {
                case FILE_REQUEST_CODE:
                    //   dataString = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    pdf = true;
                    break;
                case GALLERY_REQUEST_CODE:
                    dataString = data.getDataString();
                    break;
            }
            if (dataString != null) {
                try {
                    results = new Uri[]{Uri.parse(dataString)};
                    if (pdf) {
                        String path = results[0].getPath();

                        File file = new File(path);
                        Uri uri = Uri.fromFile(file);
                        results = new Uri[]{uri};
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else {
            setFileCallBackToNull();
        }
//        if (requestCode == WebViewFragment.INPUT_FILE_REQUEST_CODE) {
//            if (null == mFilePathCallback) return;
//            Uri result = data == null || resultCode != RESULT_OK ? null
//                    : data.getData();
//            mFilePathCallback.onReceiveValue(new Uri[]{result});
//            mFilePathCallback = null;
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        MenuItem shareMenuItem = menu.findItem(R.id.action_share);

        // check if share enabled show share menu item
        shareMenuItem.setVisible(presenterWebView.isShareEnabled());

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*        switch (item.getItemId()) {
            case R.id.action_share:
                presenterWebView.shareItem();
                break;
            default:
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPermissionGranted() {
        alertDialog = presenterWebView.uploadFile(mfileChooserParams, galleryClickListener,
                fileClickListener, uploadDismissed);
    }


    @Override
    public void onPermissionDenied() {

    }

    public void onBackKeyPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else {
            if (getActivity() != null)
                getActivity().finish();
        }
    }

    public interface UploadDismissed {
        void onDismiss();
    }

    private void setFileCallBackToNull() {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }

    private UploadDismissed uploadDismissed = new UploadDismissed() {
        @Override
        public void onDismiss() {
            setFileCallBackToNull();
        }
    };

    private WebViewClient webViewClientListener = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            showWebView();
        }

        @Override
        public void onReceivedHttpError(
                WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            showWebView();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            showWebView();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                checkAndOpenAvailableApp(context, intent);
                return true;
            } else if (url.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                checkAndOpenAvailableApp(context, intent);
                return true;
            }
            return false;
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams) {

            mfileChooserParams = fileChooserParams;
            if (mFilePathCallback != null)
                mFilePathCallback.onReceiveValue(null);

            mFilePathCallback = filePathCallback;

            int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
                checkPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            else
                alertDialog = presenterWebView.uploadFile(fileChooserParams, galleryClickListener,
                        fileClickListener, uploadDismissed);

            return true;
        }
    };

    private View.OnClickListener galleryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (alertDialog != null)
                alertDialog.hide();

            startActivityForResult(getGalleryIntent(), GALLERY_REQUEST_CODE);
        }
    };

    private View.OnClickListener fileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (alertDialog != null)
                alertDialog.hide();

            WebViewHelper.openFilePicker(FragmentWebView.this);
        }
    };
}
