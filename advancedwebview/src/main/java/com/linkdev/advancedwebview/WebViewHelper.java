package com.linkdev.advancedwebview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

//import com.nbsp.materialfilepicker.MaterialFilePicker;

import com.linkdev.advancedwebview.webview.FragmentWebView;

/**
 * Created by Ahmed.Ezz on 6/12/2017.
 */

public class WebViewHelper {

    public static final int FILE_REQUEST_CODE = 0;
    public static final int GALLERY_REQUEST_CODE = 1;

    public static AlertDialog showChooserDialog(Context context, View.OnClickListener mfileClickListener,
                                         View.OnClickListener mgalleryClickListener,
                                         final FragmentWebView.UploadDismissed uploadDismissed) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        View promptsView = ((Activity) context).getLayoutInflater().inflate(R.layout.select_attach_dialog, null);

        // set prompts.xml to alert dialog builder
        alertDialogBuilder.setView(promptsView);
        final LinearLayout gallery = (LinearLayout) promptsView
                .findViewById(R.id.linear_gallery);

        final LinearLayout lnrPdf = (LinearLayout) promptsView
                .findViewById(R.id.linear_pdf);

        lnrPdf.setOnClickListener(mfileClickListener);
        gallery.setOnClickListener(mgalleryClickListener);

        alertDialogBuilder
                .setCancelable(true)
                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (uploadDismissed != null)
                            uploadDismissed.onDismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

        return alertDialog;
    }

    public static void openFilePicker(android.support.v4.app.Fragment fragment) {
/*        MaterialFilePicker materialFilePicker = new MaterialFilePicker()
                .withSupportFragment(fragment)
                .withRequestCode(FILE_REQUEST_CODE)
                .withFilter(Pattern.compile(".*\\.(txt|pdf|docx)$")) // Filtering files and directories by file name using regexp
                // Set directories filterable (false by default)
                .withHiddenFiles(true); // Show hidden files and folders
        materialFilePicker.start();*/
    }

    public static Intent getGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }

}
