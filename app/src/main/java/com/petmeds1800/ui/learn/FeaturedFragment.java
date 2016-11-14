package com.petmeds1800.ui.learn;

import com.petmeds1800.R;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.fragments.LearnFragment;
import com.petmeds1800.ui.fragments.LearnRootFragment;
import com.petmeds1800.util.AnalyticsUtil;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Digvijay on 10/18/2016.
 */

public class FeaturedFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured, container, false);
        new AnalyticsUtil().trackScreen(getString(R.string.label_education_home_analytics_title));
        ButterKnife.bind(this, view);
        showWebViewFragment();
        setHasOptionsMenu(false);
        //start listening for optionsMenuAction
        registerIntent(new IntentFilter(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION), getContext());
        return view;
    }

    private void showWebViewFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_featured_education));
        bundle.putBoolean(CommonWebviewFragment.DISABLE_BACK_BUTTON , true);
        replaceFragmentWithBundle(new CommonWebviewFragment(), bundle, R.id.container_webview_fragment);
    }

    @OnClick(R.id.txv_questions)
    public void showAskVet() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_q_and_a_directory));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_featured_ask_vet));
        ((LearnFragment) getParentFragment()).addAskVetFragment(bundle);
    }

    @Override
    public void onDestroyView() {
        deregisterIntent(getContext());
        super.onDestroyView();
    }

    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {
        checkAndSetHasOptionsMenu(intent , LearnRootFragment.class.getName());
        super.onReceivedBroadcast(context, intent);
    }
}
