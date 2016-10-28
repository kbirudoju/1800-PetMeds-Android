package com.petmeds1800.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.petmeds1800.R;

/**
 * Created by Digvijay on 9/20/2016.
 */
public class BarcodeScannerActivity extends AbstractActivity {

    private CaptureManager mCapture;

    private CompoundBarcodeView mBarcodeScannerView;

    private Bundle mSavedInstanceState;

    private final int CAMERA_ACCESS_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        setToolBarTitle(getString(R.string.label_scan_barcode));
        enableBackButton();

        mBarcodeScannerView = (CompoundBarcodeView) findViewById(R.id.zxing_barcode_scanner);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        TextView statusTxv = mBarcodeScannerView.getStatusView();
        statusTxv.setLayoutParams(params);
        statusTxv.setPadding(50, 50, 50, 50);
        statusTxv.setGravity(Gravity.CENTER_VERTICAL);
        statusTxv.setText(R.string.label_place_barcode_within);
        statusTxv.setTextColor(ContextCompat.getColor(this, R.color.white));
        statusTxv.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_color));

        initBarcodeScannerWrapper();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_barcode_scanner;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCapture != null) {
            mCapture.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCapture != null) {
            mCapture.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCapture != null) {
            mCapture.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCapture != null) {
            mCapture.onSaveInstanceState(outState);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeScannerView.onKeyDown(keyCode, event) || super
                .onKeyDown(keyCode, event);
    }

    private void initBarcodeScanner() {
        mCapture = new CaptureManager(this, mBarcodeScannerView);
        mCapture.initializeFromIntent(getIntent(), mSavedInstanceState);
        mCapture.decode();
    }

    private void initBarcodeScannerWrapper() {
        int hasCameraAccessPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (hasCameraAccessPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_ACCESS_REQUEST_CODE);
            return;
        }
        initBarcodeScanner();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_ACCESS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initBarcodeScanner();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Camera access denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
