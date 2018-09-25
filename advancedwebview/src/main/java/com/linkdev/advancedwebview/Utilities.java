package com.linkdev.advancedwebview;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

public class Utilities {


    public static boolean isStringEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || TextUtils.isEmpty(str.toString().trim()) || "null".equalsIgnoreCase(str.toString());
    }

    public static void openWebPage(Context context, String url) throws NullPointerException {
        if (!isStringEmpty(url)) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "no available browser", Toast.LENGTH_LONG).show();
            }
        } else {
            throw new NullPointerException();
        }
    }

    public static void checkAndOpenAvailableApp(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
        else
            Toast.makeText(context, "no available app", Toast.LENGTH_LONG).show();
    }

    public static Boolean CheckIfApplicationIsConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            return activeNetInfo != null && activeNetInfo.isAvailable()
                    && activeNetInfo.isConnected();
        } else {
            return false;
        }
    }

    public static void shareItemUrl(Context context, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }


}
