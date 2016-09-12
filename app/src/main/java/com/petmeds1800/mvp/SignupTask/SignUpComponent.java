package com.petmeds1800.mvp.SignupTask;

import com.petmeds1800.dagger.component.AppComponent;
import com.petmeds1800.dagger.scopes.ActivityScope;
import com.petmeds1800.ui.SignUpActivity;

import dagger.Component;

/**
 * Created by Digvijay on 9/9/2016.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SignUpPresenterModule.class)
public interface SignUpComponent {
    void inject(SignUpActivity signUpActivity);
}
