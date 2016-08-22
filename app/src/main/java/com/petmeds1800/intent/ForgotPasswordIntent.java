package com.petmeds1800.intent;

import com.petmeds1800.ui.ForgotPasswordActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Digvijay on 8/22/2016.
 */
public class ForgotPasswordIntent extends Intent {

    public ForgotPasswordIntent(final Context context){
        super(context, ForgotPasswordActivity.class);
    }
}
