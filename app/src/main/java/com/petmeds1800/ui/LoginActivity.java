package com.petmeds1800.ui;

import com.petmeds1800.R;
import com.petmeds1800.mvp.LoginTask.DaggerLoginComponent;
import com.petmeds1800.mvp.LoginTask.LoginPresenter;
import com.petmeds1800.mvp.LoginTask.LoginPresenterModule;
import com.petmeds1800.ui.fragments.LoginFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

/**
 * Created by Digvijay on 8/3/2016.
 */
public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupComponent();

    }

    private void setupComponent() {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_login);
        DaggerLoginComponent.builder()
                .loginPresenterModule(new LoginPresenterModule(loginFragment))
                .build().inject(this);
    }
}
