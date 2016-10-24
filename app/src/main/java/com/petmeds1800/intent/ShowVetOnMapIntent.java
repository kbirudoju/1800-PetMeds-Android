package com.petmeds1800.intent;

import android.content.Context;
import android.content.Intent;

import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.vet.ShowVetOnMapActivity;

import java.util.ArrayList;

/**
 * Created by pooja on 10/21/2016.
 */
public class ShowVetOnMapIntent extends Intent {


    public static final String REQUEST_CODE = "request_code";
    public static final String ZIP_CODE = "zip_code";


    public ShowVetOnMapIntent(Context mContext,ArrayList<VetList> vetList,String zipCode) {
        super(mContext, ShowVetOnMapActivity.class);
        putExtra(REQUEST_CODE, vetList);
        putExtra(ZIP_CODE,zipCode);
    }
}
