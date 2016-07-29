package com.petmeds1800.intent;

import android.content.Context;
import android.content.Intent;

import com.petmeds1800.ui.HomeActivity;

public class HomeIntent extends Intent {

    public HomeIntent(final Context context) {
        super(context, HomeActivity.class);
    }

}
