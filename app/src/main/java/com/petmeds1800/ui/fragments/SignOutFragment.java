package com.petmeds1800.ui.fragments;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.LoginIntent;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.CookieJar;

/**
 * Created by Sdixit on 08-09-2016.
 */

public class SignOutFragment extends AbstractFragment {

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Inject
    CookieJar mCookieJar;

    @BindView(R.id.log_in_button)
    Button mLogInButton;

    @BindView(R.id.sign_up_button)
    Button mSignUpButton;

    @BindView(R.id.topImageView)
    ImageView mTopImageView;

    @BindView(R.id.middleTextView)
    TextView mMiddleTextView;

    private static final String IS_FROM_HOME_ACTIVITY = "isFromHomeActivity";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        PetMedsApplication.getAppComponent().inject(this);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_account));
        mPreferencesHelper.clearGeneralPreferencesData();
        ((PersistentCookieJar)mCookieJar).clear();
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.log_in_button)
    public void onClick() {
        startActivity(new LoginIntent(getActivity()));
        getActivity().finish();
    }


}
