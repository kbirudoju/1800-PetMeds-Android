package com.petmeds1800.intent;

import com.petmeds1800.ui.IntroActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Digvijay on 9/1/2016.
 */
public class IntroIntent extends Intent {

    public IntroIntent(Context context){
        super(context, IntroActivity.class);
    }
}
