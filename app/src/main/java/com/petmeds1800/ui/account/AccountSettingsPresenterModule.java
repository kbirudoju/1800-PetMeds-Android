package com.petmeds1800.ui.account;

import dagger.Provides;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.UserModel;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Abhinav on 8/8/16.
 */

@Module
public class AccountSettingsPresenterModule {

    private final AccountSettingsContract.View mView;

    public AccountSettingsPresenterModule(AccountSettingsContract.View view){
        mView = view;
    }

    @Provides
    AccountSettingsContract.View providesAccountSettingsView() {
        return mView;
    }

    @Provides
    UserModel provideUserModel(PetMedsApiService apiService){
        return new UserModel(apiService);
    }

    @Provides
    AccountSettingsContract.Presenter provideAccountSettingsPresenter(UserModel userModel) {
        return new AccountSettingsPresenter(userModel,mView);
    }


}
