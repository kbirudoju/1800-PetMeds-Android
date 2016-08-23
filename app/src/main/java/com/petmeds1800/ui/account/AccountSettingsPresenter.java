package com.petmeds1800.ui.account;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.Profile;
import com.petmeds1800.model.entities.UpdateAccountSettingsRequest;
import com.petmeds1800.model.entities.UpdateAccountSettingsResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 4/8/16.
 */
public class AccountSettingsPresenter implements AccountSettingsContract.Presenter {

    private static final int PASSWORD_LENGTH = 3;

    @NonNull
    private final AccountSettingsContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    AccountSettingsPresenter(AccountSettingsContract.View settingsView) {
        mView = settingsView;
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public boolean validateName(String name) {
        return !name.isEmpty();
    }

    @Override
    public boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean validatePassword(String password) {
        return password.length() >= PASSWORD_LENGTH;
    }

    @Override
    public void findUserData() {

        //test data
//        User user = new User();
//        user.setFirstName("Abhinav");
//        user.setEmail("aagarwal@dminc.com");
//        user.setPassword("dontknow");

        mPetMedsApiService
                .getAccountSettings(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Profile>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                    }

                    @Override
                    public void onNext(Profile s) {
                        if (s != null) {
                            if (mView.isActive()) {
                                mView.setUserData(s.getProfile());
                            }
                        }
                    }
                });

    }

    @Override
    public void saveSettings(UpdateAccountSettingsRequest updateAccountSettingsRequest) {

        mPetMedsApiService.updateAccountSettings(updateAccountSettingsRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateAccountSettingsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                    }

                    @Override
                    public void onNext(UpdateAccountSettingsResponse s) {

                        if (s.getStatus().equals(API_SUCCESS_CODE)) {
                            if(mView.isActive()){
                                mView.showSuccess();
                            }
                        }
                        else{
                            if(mView.isActive()){
                                mView.showError(s.getStatus().getErrorMessages().get(0));
                            }
                        }
                    }
                });

    }


    @Override
    public void start() {

    }
}
