package com.petmeds1800.intent;

import com.petmeds1800.ui.checkout.CheckOutActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Sdixit on 21-09-2016.
 */

public class CheckOutIntent extends Intent {

    public CheckOutIntent(Context mContext) {
        super(mContext, CheckOutActivity.class);
    }
}
