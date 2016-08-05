package com.petmeds1800.intent;

import com.petmeds1800.ui.LoginActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Digvijay on 8/3/2016.
 */
public class LoginIntent extends Intent{

    public LoginIntent(Context context) {
        super(context, LoginActivity.class);
    }
}
