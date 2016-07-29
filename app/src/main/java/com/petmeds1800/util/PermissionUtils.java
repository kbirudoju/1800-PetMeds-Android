package com.petmeds1800.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.petmeds1800.ui.fragments.dialog.BaseDialogFragment;
import com.petmeds1800.ui.fragments.dialog.NoTitleOkDialogFragment;

/**
 * Created by laetitia on 1/5/16.
 */
public class PermissionUtils extends ContextWrapper {

    public static final int STORAGE_PERMISSION_REQUEST_CODE = 0;

    public PermissionUtils(Context base) {
        super(base);
    }

    public boolean isPermissionGranted(@NonNull String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void askUserForPermission(final FragmentActivity activity, final String permission, final int permissionCode, String dialogMessage) {
        final int permissionState = ContextCompat.checkSelfPermission(this, permission);
        if (permissionState != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionRationaleDialog(activity, dialogMessage, new BaseDialogFragment.DialogButtonsListener() {
                    @Override
                    public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
                        ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionCode);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionCode);
            }
        }
    }

    private void showPermissionRationaleDialog(FragmentActivity activity, String message, BaseDialogFragment.DialogButtonsListener okListener) {
        NoTitleOkDialogFragment dialog = NoTitleOkDialogFragment.newInstance(message);
        dialog.setPositiveListener(okListener);
        dialog.show(activity.getSupportFragmentManager());
    }
}
