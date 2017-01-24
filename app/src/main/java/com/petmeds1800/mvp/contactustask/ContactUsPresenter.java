package com.petmeds1800.mvp.contactustask;

import android.support.annotation.NonNull;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.ContactUsResponse;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pooja on 1/24/2017.
 */
public class ContactUsPresenter  implements ContactUsContract.Presenter {

    @Inject
    PetMedsApiService mPetMedsApiService;

    private ContactUsContract.View mView;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public ContactUsPresenter(@NonNull ContactUsContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    @Override
    public void getContactData() {
        mPetMedsApiService
                .getContactDetail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContactUsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());

                    }

                    @Override
                    public void onNext(ContactUsResponse contactUsResponse) {
                        if (contactUsResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.setContactData(contactUsResponse.getContactUs());
                            }
                        } else {
                            if (mView.isActive()) {
                                mView.onError(contactUsResponse.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });
    }



    @Override
    public void start() {
getContactData();
    }


}

