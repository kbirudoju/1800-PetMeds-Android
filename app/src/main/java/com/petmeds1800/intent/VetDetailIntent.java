package com.petmeds1800.intent;

import android.content.Context;
import android.content.Intent;

import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.vet.VetDetailActivtiy;

/**
 * Created by pooja on 10/21/2016.
 */
public class VetDetailIntent extends Intent {
    public static final String REQUEST_CODE = "vet_info";


    public VetDetailIntent(Context mContext,VetList vetList) {
        super(mContext, VetDetailActivtiy.class);
        putExtra(REQUEST_CODE, vetList);
    }
}
