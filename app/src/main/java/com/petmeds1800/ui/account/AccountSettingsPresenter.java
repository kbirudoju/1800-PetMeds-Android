package com.petmeds1800.ui.account;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.Profile;
import com.petmeds1800.model.entities.UpdateAccountSettingsRequest;
import com.petmeds1800.model.entities.UpdateAccountSettingsResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.InputValidationUtil;
import com.petmeds1800.util.RetrofitErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 4/8/16.
 */
public class AccountSettingsPresenter implements AccountSettingsContract.Presenter {

    private static final int PASSWORD_LENGTH = 8;

    @NonNull
    private final AccountSettingsContract.View mView;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    private Context mContext;
    AccountSettingsPresenter(AccountSettingsContract.View settingsView,  Context context) {
        mView = settingsView;
        PetMedsApplication.getAppComponent().inject(this);
        this.mContext=context;
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

        return password.matches(InputValidationUtil.passwordPattern);
    }

    @Override
    public boolean validateConfirmPassword(String password, String confirmPassword) {

        return (TextUtils.equals(password, confirmPassword) ? true : false);
    }

    @Override
    public void findUserData() {

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
                        Log.e("AccountSettings", e.getLocalizedMessage());
                        if(RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection ) {
                            if(mView.isActive())
                            mView.showError(mContext.getString(R.string.no_internet_caps));
                        }else {
                            if(mView.isActive())
                            mView.showError(e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onNext(Profile s) {
                        if (s != null) { //TODO Need to handle errors.As of now there is no status field in the API response but it should be.
                            if (mView.isActive()) {
                                mView.setUserData(s.getUser());
                            }
                        }
                    }
                });

    }

    @Override
    public void saveSettings(final UpdateAccountSettingsRequest updateAccountSettingsRequest) {

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
                        Log.e("AccountSettings", e.getLocalizedMessage());
                        if(RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection ) {
                            if(mView.isActive())
                            mView.showError(mContext.getString(R.string.no_internet_caps));
                        }else {
                            if(mView.isActive())
                            mView.showError(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onNext(UpdateAccountSettingsResponse s) {

                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mPreferencesHelper.setLoginEmail(updateAccountSettingsRequest.getEmail());
                                mPreferencesHelper.setLoginPassword(updateAccountSettingsRequest.getPassword());
                                mView.showSuccess();
                            }
                        } else {
                            if (mView.isActive()) {
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
