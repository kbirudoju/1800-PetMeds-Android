package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Digvijay on 9/1/2016.
 */
public class IntroFragment extends Fragment {

    @BindView(R.id.videoview)
    VideoView mVideoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_intro, container, false);
        ButterKnife.bind(this, view);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.looping_bg;
        startVideo();
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.start();
        return view;
    }

    void startVideo(){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        mVideoView.setLayoutParams(params);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.looping_bg;
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        mVideoView.start();
    }
}
