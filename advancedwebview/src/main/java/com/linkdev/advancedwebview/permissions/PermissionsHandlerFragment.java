package com.linkdev.advancedwebview.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;


import com.linkdev.advancedwebview.base.FragmentBase;

import java.util.ArrayList;

/**
 * Created by Sherif.ElNady on 8/17/2016.
 */
public abstract class PermissionsHandlerFragment extends FragmentBase {
    private PermissionsListener mListener = (PermissionsListener) this;

    //==============================================================================================
    private static final int MY_PERMISSIONS_REQUEST = 1;

    protected void checkPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, MY_PERMISSIONS_REQUEST);
        } else {
            mListener.onPermissionGranted();
        }
    }

    protected void checkPermissions(Context context, String... permissions) {
        ArrayList<String> notGrantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions.add(permission);
            }
        }
        if (notGrantedPermissions.size() == 0) {
            mListener.onPermissionGranted();
        } else {
            requestPermissions(notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]), MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                boolean permissionsGranted = true;
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            permissionsGranted = false;
                            break;
                        }
                    }
                }
                if (permissionsGranted)
                    mListener.onPermissionGranted();
                else
                    mListener.onPermissionDenied();

            }
        }
    }

}
