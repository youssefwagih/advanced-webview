package com.linkdev.customwebview.web_view_preview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;


/**
 * Created by Youssef.Wagih on 24-April-18.
 */

public abstract class FragmentBase extends Fragment {

    private boolean isTablet = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    protected boolean isTablet() {
        return isTablet;
    }

    protected abstract void initializeViews(View v);

    protected abstract void setListeners();

    /**
     * @param fragment        to be loaded in the container
     * @param containerViewID container holding fragment
     * @param fragmentTag
     * @param fragmentManager
     * @param bundle
     */
    public void addChildFragmentToFragment(Fragment fragment,
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
}
