package com.petmeds1800.ui.fragments;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.mvp.SignupTask.SignUpContract;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Digvijay on 9/9/2016.
 */
public class SignUpFragment extends AbstractFragment implements SignUpContract.View {

    @Inject
    PetMedsApiService mApiService;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setEmailError(String errorString) {

    }

    @Override
    public void setPasswordError(String errorString) {

    }

    @Override
    public void setNameError(String errorString, int viewId) {

    }

    @Override
    public void setAddressError(String errorString, int viewId) {

    }

    @Override
    public void setPostalCodeError(String errorString) {

    }

    @Override
    public void setPhoneError(String errorString) {

    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {

    }

    @Override
    public void navigateToHome() {

    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {

    }
}
