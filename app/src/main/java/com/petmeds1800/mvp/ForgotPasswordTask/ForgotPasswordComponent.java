package com.petmeds1800.mvp.ForgotPasswordTask;

import com.petmeds1800.dagger.scopes.ActivityScope;
import com.petmeds1800.dagger.component.AppComponent;
import com.petmeds1800.ui.ForgotPasswordActivity;

import dagger.Component;

/**
 * Created by Digvijay on 8/19/2016.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ForgotPasswordPresenterModule.class)
public interface ForgotPasswordComponent {
    void inject(ForgotPasswordActivity forgotPasswordActivity);
}
