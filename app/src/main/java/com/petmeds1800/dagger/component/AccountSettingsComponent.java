package com.petmeds1800.dagger.component;

import com.petmeds1800.dagger.ActivityScope;
import com.petmeds1800.dagger.module.AppComponent;
import com.petmeds1800.ui.account.AccountSettingsFragment;
import com.petmeds1800.ui.account.AccountSettingsPresenterModule;

import dagger.Component;

/**
 * Created by Abhinav on 8/8/16.
 */
@ActivityScope
@Component(dependencies = AppComponent.class , modules = AccountSettingsPresenterModule.class)
public interface AccountSettingsComponent {

    void inject(AccountSettingsFragment accountSettingsFragment);

}
