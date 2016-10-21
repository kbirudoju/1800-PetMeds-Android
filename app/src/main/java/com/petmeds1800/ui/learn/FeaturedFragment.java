package com.petmeds1800.ui.learn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.fragments.LearnFragment;

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
        ButterKnife.bind(this, view);
        showWebViewFragment();
        return view;
    }

    private void showWebViewFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_featured_education));
        replaceFragmentWithBundle(new CommonWebviewFragment(), bundle, R.id.container_webview_fragment);
    }

    @OnClick(R.id.txv_questions)
    public void showAskVet() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_q_and_a_directory));
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_featured_ask_vet));
        ((LearnFragment) getParentFragment()).addAskVetFragment(bundle);
    }
}
