package com.petmeds1800.ui.resetpassword;

import android.app.Fragment;
import android.util.Log;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.CheckResetPasswordTokenRequest;
import com.petmeds1800.model.entities.PasswordResetResponse;
import com.petmeds1800.model.entities.ResetPasswordResponse;
import com.petmeds1800.model.entities.SaveResetPasswordRequest;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.model.entities.SessionConfigRequest;
import com.petmeds1800.model.entities.SignOutResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.InputValidationUtil;
import com.petmeds1800.util.RetrofitErrorHandler;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ResetPasswordPresenter implements ResetPasswordContract.Presenter {

    public static final String TOKEN_EXPIRED_MSG = "expired";
    private ResetPasswordContract.View mView;
    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public ResetPasswordPresenter(ResetPasswordContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void checkResetPasswordLinkValidity(CheckResetPasswordTokenRequest checkResetPasswordTokenRequest) {
        mPetMedsApiService.checkPasswordResetToken(checkResetPasswordTokenRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PasswordResetResponse>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   //error handling would be implemented once we get the details from backend team
                                   Log.e("CheckPasswordLink", e.getMessage());
                                   int errorId = RetrofitErrorHandler.getErrorMessage(e);
                                   if (errorId != 0) {
                                       if (mView.isActive()) {
                                           mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                                       }
                                   }

                               }

                               @Override
                               public void onNext(PasswordResetResponse passwordResetResponse) {
                                   if (passwordResetResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                       if (mView.isActive()) {
                                           mView.onSuccess(passwordResetResponse);
                                       }
                                   } else {
                                       if (mView.isActive()) {
                                           mView.onSuccess(passwordResetResponse);
                                       }
                                   }

                               }
                           }
                );

    }

    @Override
    public void saveResetPasswordDetails(final SaveResetPasswordRequest saveResetPasswordRequest) {
        mPetMedsApiService.getSessionConfirmationNumber()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, SessionConfNumberResponse>() {
                    @Override
                    public SessionConfNumberResponse call(Throwable throwable) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(throwable);
                        if (!(errorId == R.string.noInternetConnection)) {
                            return mPreferencesHelper.getSessionConfirmationResponse();
                        }
                        return null;
                    }
                })
                .flatMap(new ResetPassword(saveResetPasswordRequest))
                .subscribe(new Subscriber<ResetPasswordResponse>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   //error handling would be implemented once we get the details from backend team
                                   Log.e("SaveResetPassword", e.getMessage());
                                   int errorId = RetrofitErrorHandler.getErrorMessage(e);
                                   if (errorId != 0) {
                                       if (mView.isActive()) {
                                           mView.showErrorCrouton(((Fragment) mView).getString(errorId), false);
                                       }
                                   } else {
                                       if (mView.isActive()) {
                                           mView.showErrorCrouton(e.getLocalizedMessage(), false);
                                       }
                                   }

                               }

                               @Override
                               public void onNext(ResetPasswordResponse resetPasswordResponse) {

                                   if (resetPasswordResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                       if (mView.isActive()) {
                                           mView.onSavedResetPasswordDetailsSuccess();
                                       }
                                   } else {
                                       if (mView.isActive()) {
                                           String errorMsg = resetPasswordResponse.getStatus().getErrorMessages().get(0);
                                           if (errorMsg.contains(TOKEN_EXPIRED_MSG)) {
                                               mView.onTokenExpired();
                                           } else {
                                               mView.showErrorCrouton(errorMsg, false);
                                           }
                                       }
                                   }

                               }
                           }
                );


    }

    private class ResetPassword implements Func1<SessionConfNumberResponse, Observable<ResetPasswordResponse>> {
        SaveResetPasswordRequest mSaveResetPasswordRequest;

        ResetPassword(SaveResetPasswordRequest saveResetPasswordRequest) {
            mSaveResetPasswordRequest = saveResetPasswordRequest;
        }

        @Override
        public Observable<ResetPasswordResponse> call(
                SessionConfNumberResponse sessionConfNumberResponse) {
            if (sessionConfNumberResponse != null) {
                String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                com.petmeds1800.util.Log.v("sessionToken", sessionConfNumber);
                if (sessionConfNumber != null) {
                    mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                }
                mSaveResetPasswordRequest.setDynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                return mPetMedsApiService.saveResetPasswordDetails(mSaveResetPasswordRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
            } else {
                return null;
            }
        }
    }

    @Override
    public void start() {

    }

    @Override
    public boolean validatePassword(String password) {
        return password.matches(InputValidationUtil.passwordPattern);
    }

    @Override
    public void signout() {

        mPetMedsApiService.logout(new SessionConfigRequest(mPreferencesHelper.getSessionConfirmationResponse()
                .getSessionConfirmationNumber()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignOutResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        com.petmeds1800.util.Log.e("SignouPresenter", e.getMessage());
                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            com.petmeds1800.util.Log.d("Signout", "retrying after session renew");
                            signout();
                            return;

                        }
                        //error handling would be implemented once we get the details from backend team
                        mView.showErrorCrouton(e.getLocalizedMessage(), false);

                    }

                    @Override
                    public void onNext(SignOutResponse s) {
                        com.petmeds1800.util.Log.d("SignOutResponse", s.toString());
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.onSignOutSuccess(true);
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showErrorCrouton(
                                        s.getStatus().getErrorMessages().size() > 0 ? s.getStatus().getErrorMessages()
                                                .get(0) : null, false);
                            }
                        }

                    }
                });
    }
}
