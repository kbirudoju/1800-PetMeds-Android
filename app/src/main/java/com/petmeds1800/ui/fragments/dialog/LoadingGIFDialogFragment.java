package com.petmeds1800.ui.fragments.dialog;

import com.bumptech.glide.Glide;
import com.petmeds1800.R;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Sarthak on 10/4/2016.
 */

public class LoadingGIFDialogFragment extends DialogFragment {

    ImageView mLoadingGif;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
            Glide.with(getActivity()).load(Uri.parse("file:///android_asset/Loading.gif")).asGif().into(mLoadingGif);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_loading_gif, container, false);
        mLoadingGif = (ImageView) root.findViewById(R.id.loading_gif_image);
        return root;
    }
}
