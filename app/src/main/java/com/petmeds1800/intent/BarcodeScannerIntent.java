package com.petmeds1800.intent;

import com.petmeds1800.ui.BarcodeScanResultsActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Digvijay on 9/20/2016.
 */
public class BarcodeScannerIntent extends Intent {

    public BarcodeScannerIntent(final Context context){
        super(context, BarcodeScanResultsActivity.class);
    }
}
