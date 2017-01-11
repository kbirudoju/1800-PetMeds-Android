package com.petmeds1800.ui.support;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Log;
import com.petmeds1800.util.RetrofitErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 21/12/16.
 */
public class SecurityLockPresenter implements HomeActivityContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    HomeActivityContract.View mView;

    private Subscription mSubscription;

    public SecurityLockPresenter(HomeActivityContract.View mView) {
        this.mView = mView;
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getSecurityStatusFirst() {

        mSubscription = mPetMedsApiService.getSecurityStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecurityStatusResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        //error handling would be implemented once we get the details from backend team
                        Log.e("securityStatus", e.getMessage());
                        //check if we need to retry as a consequence of 409 conflict
                        if (e instanceof SecurityException) {
                            Log.d("securityStatus", "retrying after session renew");
                            getSecurityStatusFirst();

                            return;

                        } else if (RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection
                                || RetrofitErrorHandler.getErrorMessage(e) == R.string.connectionTimeout) {
                            if (mView.isActive()) {

                                mView.showNonCancelableDialog(
                                        ((AbstractActivity) mView).getString(RetrofitErrorHandler.getErrorMessage(e)));
                                mView.hideProgress();
                            }
                        } else {
                            if (mView.isActive()) {

                                mView.showNonCancelableDialog(e.getLocalizedMessage());
                                mView.hideProgress();
                            }
                        }
                    }

                    @Override
                    public void onNext(SecurityStatusResponse securityStatusResponse) {

                        //we should now unblock the UI in-order to make subsequent network calls

                        if (securityStatusResponse != null) {
                            Log.d("securityStatus", securityStatusResponse.getSecurityStatus() + "");

                            //clear the lock to wait for security status
                            mPreferencesHelper.setWaitForSecurityStatus(false);
                            if (mView.isActive()) {
                                mView.hideProgress();
                                mView.moveAhead();
                            }
                        }
                    }
                });

    }

    @Override
    public void unsubscribeSubscription() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void start() {

    }
}
