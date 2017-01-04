package com.petmeds1800.ui.address;

import android.content.Context;
import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.MySavedAddress;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Log;
import com.petmeds1800.util.RetrofitErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 11/8/16.
 */
public class SavedAddressListPresenter implements SavedAddressListContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private SavedAddressListContract.View mView;

    private Context mContext;

    public SavedAddressListPresenter(@NonNull SavedAddressListContract.View view, Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getSavedAddress() {

        mPetMedsApiService
                .getSavedAddress(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MySavedAddress>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //notify about the error.It could be any type of error while getting data from the API
                        Log.e(SavedAddressListPresenter.class.getName(), e.getMessage());
                        if (mView.isActive()) {
                            if(RetrofitErrorHandler.getErrorMessage(e) == R.string.noInternetConnection ) {
                                mView.showErrorMessage(mContext.getString(R.string.no_internet_caps));
                            }else {
                                mView.showErrorMessage(e.getLocalizedMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(MySavedAddress s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)
                                && s.getProfileAddresses() != null && s.getProfileAddresses().size() > 0) {

                            if (mView.isActive()) {
                                mView.showAddressListView(s.getProfileAddresses());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.showNoAddressView();
                            }
                        }
                    }
                });

    }

    @Override
    public void start() {
        getSavedAddress();
    }
}
