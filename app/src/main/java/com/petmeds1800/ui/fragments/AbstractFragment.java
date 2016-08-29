package com.petmeds1800.ui.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.petmeds1800.R;

import java.util.HashMap;

public abstract class AbstractFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 1001;
    private PermissionRequested permissionRequested;

    public void replaceAndAddToBackStack(Fragment fragment , String tag) {
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.fragment_container, fragment, tag);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

   public  void replaceFragmentWithBundle(Fragment fragment, Bundle bundle){
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.fragment_container, fragment);
        fragment.setArguments(bundle);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void popBackStack(){
        getFragmentManager().popBackStack();
    }

    public void popBackStackImmediate(){
        getFragmentManager().popBackStackImmediate();
    }


    public void checkRequiredPermission(String[] requestedPermissions, PermissionRequested permissionRequested) {
        this.permissionRequested = permissionRequested;
        requestPermissions(requestedPermissions, PERMISSIONS_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                    if (deniedPermission.size() == 0)
                        permissionRequested.onPermissionGranted();
                    else if (permissionRequested != null) {
                        permissionRequested.onPermissionDenied(deniedPermission);
                    }
                }
                return;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public interface PermissionRequested {
        void onPermissionGranted();

        void onPermissionDenied(HashMap<String, Boolean> deniedPermissions);

        //void onPermanentlyDenied(List<String> permissionsDenied);
    }

}
