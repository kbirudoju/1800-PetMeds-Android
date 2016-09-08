package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Digvijay on 9/1/2016.
 */
public class IntroFragment extends Fragment {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.view_indicator)
    CirclePageIndicator mPageIndicator;

    @BindView(R.id.video_view)
    VideoView mVideoView;

    private OnIntroFinishedListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_intro, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupVideo();
        mViewPager.setAdapter(new IntroPagerAdapter(getFragmentManager()));
        mPageIndicator.setViewPager(mViewPager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnIntroFinishedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    getActivity().toString() + " must implement " + OnIntroFinishedListener.class.getSimpleName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    @OnClick(R.id.button_get_started)
    public void onGetStartedClick() {
        if (mListener != null) {
            mListener.onIntroFragmentFinished();
        }
    }

    void setupVideo() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mVideoView
                .getLayoutParams();
        params.width = metrics.widthPixels;
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
    }

    public interface OnIntroFinishedListener {

        void onIntroFragmentFinished();
    }

    static class IntroPagerAdapter extends FragmentPagerAdapter {

        private static Fragment[] mFragments = new Fragment[]{
                IntroPageFragment.newInstance(R.drawable.ic_intro_shop, R.string.title_shop,
                        R.string.description_shop),
                IntroPageFragment.newInstance(R.drawable.ic_intro_medicationreminder,
                        R.string.title_medication_reminder,
                        R.string.description_medication_reminder),
                IntroPageFragment.newInstance(R.drawable.ic_intro_find_a_vet, R.string.title_find_a_pet,
                        R.string.description_find_a_pet),
                IntroPageFragment.newInstance(R.drawable.ic_intro_education, R.string.title_education,
                        R.string.description_education),
                IntroPageFragment.newInstance(R.drawable.ic_intro_ez_fill, R.string.title_ez_refill,
                        R.string.description_ez_refill)
        };

        public IntroPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragments != null) {
                return mFragments[position];
            }
            return null;
        }

        @Override
        public int getCount() {
            if (mFragments != null) {
                return mFragments.length;
            }
            return 0;
        }
    }
}
