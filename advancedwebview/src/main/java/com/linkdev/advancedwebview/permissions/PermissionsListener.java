package com.linkdev.advancedwebview.permissions;

/**
 * Created by Sherif.ElNady on 8/17/2016.
 */
public interface PermissionsListener {
    void onPermissionGranted();

    void onPermissionDenied();
}
