package com.petmeds1800.util;

import android.content.Context;
import android.text.Spanned;
import com.petmeds1800.util.Log;
import android.widget.FrameLayout;

import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.ui.fragments.HomeRootFragment;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sarthak on 07-Nov-16.
 */

public class AsyncRenewSessionConfirmationNumber {

    private HomeRootFragment mContext;
    private PetMedsApiService mApiService;
    private GeneralPreferencesHelper mPreferencesHelper;

    public AsyncRenewSessionConfirmationNumber(HomeRootFragment mContext, PetMedsApiService mApiService, GeneralPreferencesHelper mPreferencesHelper) {
        Log.w("AsyncRenewSession", "AsyncRenewSessionConfirmationNumber Constructor Exit");

        this.mContext = mContext;
        this.mApiService = mApiService;
        this.mPreferencesHelper = mPreferencesHelper;

        Log.w("AsyncRenewSession", "AsyncRenewSessionConfirmationNumber Constructor Exit");
    }

    /**
     * Renew Session Confirmation Number for ex issue related to session Expiration
     * Facing 409 Error
     * also 200 OK not feasible
     */
    public void initializeSessionConfirmationNumber() {
        Log.w("AsyncRenewSession", "initializeSessionConfirmationNumber Enter");

        mApiService.getSessionConfirmationNumber()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SessionConfNumberResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        mContext.showErrorCrouton(e.getLocalizedMessage(), false);
                    }

                    @Override
                    public void onNext(SessionConfNumberResponse sessionConfNumberResponse) {
                        if (sessionConfNumberResponse != null) {
                            String sessionConfNumber = sessionConfNumberResponse.getSessionConfirmationNumber();
                            Log.v("sessionToken", sessionConfNumber);
                            if (sessionConfNumber != null) {
                                mPreferencesHelper.saveSessionConfirmationResponse(sessionConfNumberResponse);
                            } else {
                                //TODO Have to change the message
                                mContext.showErrorCrouton("Session number not generated", false);
                            }
                        } else {
                            //TODO Have to change the message
                            mContext.showErrorCrouton("Session response not generated", false);
                        }
                    }
                });
        Log.w("AsyncRenewSession", "initializeSessionConfirmationNumber Exit");
    }
}
