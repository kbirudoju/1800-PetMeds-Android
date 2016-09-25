package com.petmeds1800.intent;

import com.petmeds1800.ui.checkout.CheckOutActivity;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

/**
 * Created by Sdixit on 21-09-2016.
 */

public class CheckOutIntent extends Intent {

    public CheckOutIntent(Context mContext, HashMap<String, String> itemsDetail) {
        super(mContext, CheckOutActivity.class);
        this.putExtra(CheckOutActivity.ITEMS_DETAIL, itemsDetail);
    }
}
