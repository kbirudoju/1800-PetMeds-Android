package com.petmeds1800.ui;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.mvp.SignupTask.DaggerSignUpComponent;
import com.petmeds1800.mvp.SignupTask.SignUpPresenter;
import com.petmeds1800.mvp.SignupTask.SignUpPresenterModule;
import com.petmeds1800.ui.fragments.SignUpFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Digvijay on 9/9/2016.
 */
public class SignUpActivity extends AbstractActivity{

    @Inject
    SignUpPresenter mSignUpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setToolBarTitle(getString(R.string.label_sign_up));
        enableBackButton();
        setupComponent();
    }

    private void setupComponent(){
        SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_sign_up);
        DaggerSignUpComponent.builder()
                .appComponent(PetMedsApplication.getAppComponent())
                .signUpPresenterModule(new SignUpPresenterModule(signUpFragment))
                .build().inject(this);
    }
}
