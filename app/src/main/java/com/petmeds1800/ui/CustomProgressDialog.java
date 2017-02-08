package com.petmeds1800.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.petmeds1800.R;

public class CustomProgressDialog extends ProgressDialog {
    private Context mContext;

    private CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loading_gif);
        ImageView mLoadingGif = (ImageView) findViewById(R.id.loading_gif_image);
        Glide.with(mContext).load(Uri.parse("file:///android_asset/Loading.gif")).asGif().into(mLoadingGif);
    }

    public static ProgressDialog getInstance(Context context) {
        CustomProgressDialog dialog = new CustomProgressDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
}
