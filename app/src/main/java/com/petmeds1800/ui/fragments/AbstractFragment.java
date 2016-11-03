package com.petmeds1800.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.petmeds1800.R;
import com.petmeds1800.ui.HomeActivity;

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

    void replaceCartFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.cart_root_fragment_container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    void replaceCartFragmentWithTag(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.cart_root_fragment_container, fragment, tag);
        transaction.commit();
    }

    void replaceCartFragmentWithBundle(Fragment fragment,String tag,Bundle  bundle) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.cart_root_fragment_container, fragment,tag);
        fragment.setArguments(bundle);
        transaction.commit();
    }
    void replaceCartFragmentWithBackStack(Fragment fragment,String tag,Bundle  bundle) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.cart_root_fragment_container, fragment,tag);
        transaction.addToBackStack(null);
        fragment.setArguments(bundle);
        transaction.commit();
    }

    void replaceHomeFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.home_root_fragment_container, fragment, tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    void replaceFragment(Fragment fragment, String tag, int containerId) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    //TODO: can be replaced with replaceFragmentWithBundle
    protected void replaceHomeFragmentWithBundle(Fragment fragment, Bundle bundle) {
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.home_root_fragment_container, fragment);
        fragment.setArguments(bundle);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    //TODO: can be replaced with replaceFragmentWithBundle
    public void replaceAccountFragmentWithBundle(Fragment fragment, Bundle bundle) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.account_root_fragment_container, fragment);
        fragment.setArguments(bundle);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void replaceAccountFragmentWithBundleTag(Fragment fragment, String tag, Bundle bundle) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.account_root_fragment_container, fragment, tag);
        fragment.setArguments(bundle);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();

    }

    public void addAccountFragmentWithBundle(Fragment fragment, Bundle bundle) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.add(R.id.account_root_fragment_container, fragment);
    }

    public void replaceFragmentWithBundle(Fragment fragment, Bundle bundle, int containerId) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(containerId, fragment);
        fragment.setArguments(bundle);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void replaceStepRootChildFragment(Fragment fragment, int containerId) {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        trans.replace(containerId, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
    }

    public void replaceStepRootChildFragmentWithTag(Fragment fragment, int containerId, String tag) {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        trans.replace(containerId, fragment, tag);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
    }

    public void addStepRootChildFragment(Fragment fragment, int containerId) {
        FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
        trans.replace(containerId, fragment);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void replaceFragmentWithBackStack(Fragment fragment, Bundle bundle, int containerId) {
        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(containerId, fragment);
            transaction.addToBackStack(fragment.getClass().getSimpleName());
            transaction.commit();
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

    public void hideSoftKeyBoard() {
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

    /**
     * Register Intent to receive BroadCast in the onReceivedBroadCast Method
     *
     * @param intentFilter
     * @param context
     * @return
     */
    protected boolean registerIntent(IntentFilter intentFilter, Context context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(abstractBroadcastReceiver, intentFilter);
        return true;
    }

    /**
     * De Register Intent to stop receiving broadcasts in onBroadCastReceived Method
     * Should run in onDestroyView atleasr
     *
     * @param context
     * @return
     */
    protected boolean deregisterIntent(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(abstractBroadcastReceiver);
        return true;
    }

    /**
     * OverRide this method in Fragment to receive broadcasts for the intent filtered registered in registerIntent
     *
     * @param context
     * @param intent
     */
    protected void onReceivedBroadcast(Context context, Intent intent) {
    }

    /**
     * Implicit Implementation, by default initialized in AbstractFragment
     * comprehensive method to receive broadcasts without multiple handling of Receiver instances
     */
    BroadcastReceiver abstractBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onReceivedBroadcast(context, intent);
        }
    };

    /**
     * This method would check if the provided intent and the rootFragment have the similar names.
     * If yes- setHasOptionsMenu as true
     * else - setHasOptionsMenu as false
     *
     * @param intent
     * @param rootFragmentName
     */
    protected void checkAndSetHasOptionsMenu(Intent intent, String rootFragmentName) {
        if (intent.getAction().equals(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION)) {
            String pareamentFragmentName = intent.getStringExtra(HomeActivity.FRAGMENT_NAME_KEY);
            if (pareamentFragmentName != null && pareamentFragmentName.equals(rootFragmentName)) {
                setHasOptionsMenu(true);
            } else {
                setHasOptionsMenu(false);
            }
        }
    }
}
