package com.petmeds1800.ui;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.ConfirmationReceiptFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;

import butterknife.OnClick;

/**
 * Created by Digvijay on 9/28/2016.
 */
public class ConfirmationReceiptActivity extends AbstractActivity implements
        ConfirmationReceiptFragment.OnShareButtonClickListener {

    private final int STORAGE_ACCESS_REQUEST_CODE = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle(getString(R.string.label_confirmation_and_receipt));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_confirmation_receipt;
    }

    private void initStoragePermissionsWrapper() {
        int hasStorageAccessPermission = ContextCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasStorageAccessPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_ACCESS_REQUEST_CODE);
            return;
        }
        showShareOptions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_ACCESS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    showShareOptions();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Storage access denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showShareOptions() {
        ConfirmationReceiptFragment fragment = (ConfirmationReceiptFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_receipt);
        File pdfFile = fragment.generatePdf();
        if (pdfFile != null) {
            fragment.shareFile(pdfFile, "receipt");
        }
    }

    @Override
    public void onShareButtonClick() {
        initStoragePermissionsWrapper();
    }

    @OnClick(R.id.btn_start_new_order)
    public void startNewOrder(){

    }
}
