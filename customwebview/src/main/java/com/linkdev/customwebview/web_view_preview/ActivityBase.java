package com.linkdev.customwebview.web_view_preview;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.linkdev.customwebview.R;

/**
 * Created by Youssef.Wagih on 24-April-18.
 */

public abstract class ActivityBase extends AppCompatActivity {
    private Toolbar myToolbar;

    private boolean isTablet = false;
    protected boolean IsLandscape = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void setToolbar(Toolbar toolbar, String title, boolean showUpButton, int arrowColorId, boolean withElevation) {
        myToolbar = toolbar;
        myToolbar.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && withElevation) {
          //  myToolbar.setElevation(getResources().getDimension(R.dimen.padding_small));
        }
        setSupportActionBar(myToolbar);


        if (showUpButton) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_ab_back_mtrl_am_alpha);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(upArrow);
                upArrow.setColorFilter(ContextCompat.getColor(this, arrowColorId), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    protected void setToolbar(Toolbar toolbar, String title, boolean showUpButton,
                              @DrawableRes int backIconDrawable, int arrowColorId, boolean withElevation) {
        myToolbar = toolbar;
        myToolbar.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && withElevation) {
           // myToolbar.setElevation(getResources().getDimension(R.dimen.padding_small));
        }
        setSupportActionBar(myToolbar);


        if (showUpButton) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                Drawable upArrow = ContextCompat.getDrawable(this, backIconDrawable);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(upArrow);
                upArrow.setColorFilter(ContextCompat.getColor(this, arrowColorId), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    /**
     * @param fragment        to be loaded in the container
     * @param containerViewID container holding fragment
     * @param fragmentTag
     * @param fragmentManager
     * @param bundle
     */
    public void addFragmentToActivity(android.support.v4.app.Fragment fragment,
                                      int containerViewID,
                                      String fragmentTag,
                                      boolean addToBackStack,
                                      FragmentManager fragmentManager,
                                      Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (addToBackStack)
            fragmentTransaction.replace(containerViewID, fragment, fragmentTag).addToBackStack(null).commit();
        else
            fragmentTransaction.replace(containerViewID, fragment, fragmentTag).commit();
    }

    /**
     * @param fragment        to be loaded in the container
     * @param containerViewID container holding fragment
     * @param fragmentManager
     * @param bundle
     */
    public void addFragmentToActivity(android.support.v4.app.Fragment fragment,
                                      int containerViewID,
                                      boolean addToBackStack,
                                      FragmentManager fragmentManager,
                                      Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (addToBackStack)
            fragmentTransaction.replace(containerViewID, fragment).addToBackStack(null).commit();
        else
            fragmentTransaction.replace(containerViewID, fragment).commit();
    }

    public void hideToolbar() {
        if (myToolbar != null)
            myToolbar.setVisibility(View.GONE);
    }

    public void setToolbarTitle(String title) {
        if (myToolbar != null)
            myToolbar.setTitle(title);
    }

    public void setToolbarSubTitle(String subTitle) {
        if (myToolbar != null) {
            myToolbar.setSubtitle(subTitle);
        }

    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setOrientationBasedOnDeviceType() {
        if (!isTablet)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    protected abstract void initializeViews();

    protected abstract void setListeners();

    protected abstract void loadFragment();

    protected boolean isTablet() {
        return isTablet;
    }
}
