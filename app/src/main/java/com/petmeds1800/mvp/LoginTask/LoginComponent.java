package com.petmeds1800.mvp.LoginTask;

import com.petmeds1800.dagger.ActivityScope;
import com.petmeds1800.dagger.module.AppComponent;
import com.petmeds1800.ui.LoginActivity;

import dagger.Component;

/**
 * Created by Digvijay on 8/4/2016.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = LoginPresenterModule.class)
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
