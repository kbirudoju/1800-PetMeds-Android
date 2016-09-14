package com.petmeds1800.intent;

import com.petmeds1800.ui.SignUpActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Digvijay on 9/12/2016.
 */
public class SignUpIntent extends Intent {

    public SignUpIntent(final Context context){
        super(context, SignUpActivity.class);
    }
}
