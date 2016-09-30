package com.petmeds1800.intent;

import android.content.Context;
import android.content.Intent;

import com.petmeds1800.ui.checkout.AddNewEntityActivity;

/**
 * Created by pooja on 9/29/2016.
 */
public class AddNewEntityIntent extends Intent {


    public static final String REQUEST_CODE = "request_code";


    public AddNewEntityIntent(Context mContext,int requestCode) {
        super(mContext, AddNewEntityActivity.class);
        putExtra(REQUEST_CODE,requestCode);
    }
}
