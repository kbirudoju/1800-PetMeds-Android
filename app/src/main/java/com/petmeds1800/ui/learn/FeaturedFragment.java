package com.petmeds1800.ui.learn;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.fragments.LearnFragment;
import com.petmeds1800.ui.fragments.LearnRootFragment;
import com.petmeds1800.util.AnalyticsUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.petmeds1800.util.Constants.PUSH_EXTRA_ID;
import static com.petmeds1800.util.Constants.PUSH_SCREEN_TYPE;

/**
 * Created by Digvijay on 10/18/2016.
 */

public class FeaturedFragment extends AbstractFragment {

    private final int TYPE_QUESTION_ANSWER_ALERT = 6;

    private int screenType = 0;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured, container, false);
        screenType = ((HomeActivity) getActivity()).getIntent().getIntExtra(PUSH_SCREEN_TYPE, 0);
        id = ((HomeActivity) getActivity()).getIntent().getStringExtra(PUSH_EXTRA_ID);
        new AnalyticsUtil().trackScreen(getString(R.string.label_education_home_analytics_title));
        ButterKnife.bind(this, view);
        showWebViewFragment();
        setHasOptionsMenu(false);
        if (((HomeActivity) getActivity()).getIntent() != null) {
            navigateOnPush();
        }
        //start listening for optionsMenuAction
        registerIntent(new IntentFilter(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION), getContext());
        return view;
    }

    private void showWebViewFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_featured_education));
        bundle.putBoolean(CommonWebviewFragment.DISABLE_BACK_BUTTON, true);
        replaceFragmentWithBundle(new CommonWebviewFragment(), bundle, R.id.container_webview_fragment);
    }

    @OnClick(R.id.txv_questions)
    public void showAskVet() {
        Log.v("screen Type","inside this show as petvet"+screenType);
        PetMedsApplication.menuItemsClicked = true;
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_q_and_a_directory));
        bundle.putString(CommonWebviewFragment.URL_KEY,
                (screenType == TYPE_QUESTION_ANSWER_ALERT) ? getString(R.string.url_vet_answered)+id
                        : getString(R.string.url_featured_ask_vet));
        ((LearnFragment) getParentFragment()).addAskVetFragment(bundle);
    }

    @Override
    public void onDestroyView() {
        deregisterIntent(getContext());
        super.onDestroyView();
    }

    public void navigateOnPush() {
        //TODO Reminder Id is hardcoded which is done when push payload inplementeid
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (screenType) {
                    case TYPE_QUESTION_ANSWER_ALERT:
                        ((HomeActivity) getActivity()).getIntent().putExtra(PUSH_SCREEN_TYPE, 0);
                        Log.v("screen Type","inside this"+screenType);
                        showAskVet();
                        screenType = 0;
                        break;
                }
            }
        },200);
        Log.v("screen Type","screen Type"+screenType);


    }

    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {
        checkAndSetHasOptionsMenu(intent, LearnRootFragment.class.getName());
        super.onReceivedBroadcast(context, intent);
    }
}
