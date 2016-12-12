package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.LoginIntent;
import com.petmeds1800.intent.SignUpIntent;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 08-09-2016.
 */

public class SignOutFragment extends AbstractFragment {

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @BindView(R.id.topImageView)
    ImageView mTopImageView;

    @BindView(R.id.middleTextView)
    TextView mMiddleTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        PetMedsApplication.getAppComponent().inject(this);
        mPreferencesHelper.setIsUserLoggedIn(false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //we should set the title only if current selected tab is not the first home tab
        if(((HomeActivity)getActivity()).getCurrentSelectedTab() == 3) {
            ((AbstractActivity)getActivity()).setToolBarTitle(getResources().getStringArray(R.array.tab_title)[3]);
        }
    }

    @OnClick(R.id.log_in_button)
    public void onClick() {
        startActivity(new LoginIntent(getActivity()));
        getActivity().finish();
    }

    @OnClick(R.id.sign_up_button)
    public void onSignUpClick() {
        startActivity(new SignUpIntent(getActivity()));
    }
}
