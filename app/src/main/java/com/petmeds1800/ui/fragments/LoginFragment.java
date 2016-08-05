package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;
import com.petmeds1800.mvp.LoginTask.LoginContract;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Digvijay on 8/3/2016.
 */
public class LoginFragment extends AbstractFragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUsernameError() {

    }

    @Override
    public void setPasswordError() {

    }

    @Override
    public void navigateToHome() {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
//        mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }
}
