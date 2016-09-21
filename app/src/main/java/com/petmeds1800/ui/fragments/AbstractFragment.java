package com.petmeds1800.ui.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.petmeds1800.R;

import java.util.HashMap;

public abstract class AbstractFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 1001;

    private PermissionRequested permissionRequested;

    public void replaceAccountAndAddToBackStack(Fragment fragment, String tag) {
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.account_root_fragment_container, fragment, tag);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    void replaceAccountFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.account_root_fragment_container, fragment);
        transaction.commit();

    }


    void replaceHomeFragment(Fragment fragment,String tag) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.home_root_fragment_container, fragment, tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

    }

    void replaceHomeFragmentWithBundle(Fragment fragment, Bundle bundle) {
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.home_root_fragment_container, fragment);
        fragment.setArguments(bundle);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void replaceAccountFragmentWithBundle(Fragment fragment, Bundle bundle) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.account_root_fragment_container, fragment);
        fragment.setArguments(bundle);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }
    public void replaceStepRootChildFragment(Fragment fragment,int containerId) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(containerId, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
    }

    public void addOrReplaceFragmentWithBackStack(Fragment fragment, Bundle bundle) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (fragmentManager.getBackStackEntryCount() == 0) {
                fragment.setArguments(bundle);
                transaction.replace(R.id.home_root_fragment_container, fragment);
                transaction.addToBackStack(fragment.getClass().getSimpleName());
                transaction.commit();
            } else {
                fragment.setArguments(bundle);
                fragmentManager.popBackStack();
                transaction.replace(R.id.home_root_fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    public void popBackStack() {
        getFragmentManager().popBackStack();
    }

    public void popBackStackImmediate() {
        getFragmentManager().popBackStackImmediate();
    }

    public void checkRequiredPermission(String[] requestedPermissions, PermissionRequested permissionRequested) {
        this.permissionRequested = permissionRequested;
        requestPermissions(requestedPermissions, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        HashMap<String, Boolean> deniedPermission = new HashMap<String, Boolean>();
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
                                deniedPermission.put(permissions[i], true);
                            } else {
                                deniedPermission.put(permissions[i], false);
                            }
                        }
                    }
                    if (deniedPermission.size() == 0) {
                        permissionRequested.onPermissionGranted();
                    } else if (permissionRequested != null) {
                        permissionRequested.onPermissionDenied(deniedPermission);
                    }
                }
                return;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void hideSoftKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hideSoftKeyBoard();
        return super.onOptionsItemSelected(item);
    }

    public interface PermissionRequested {

        void onPermissionGranted();

        void onPermissionDenied(HashMap<String, Boolean> deniedPermissions);

        //void onPermanentlyDenied(List<String> permissionsDenied);
    }

}
