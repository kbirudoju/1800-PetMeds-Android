package com.petmeds1800.ui.learn;

import android.content.Context;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.PetEducationCategoriesResponse;
import com.petmeds1800.util.RetrofitErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Digvijay on 10/20/2016.
 */

public class MedConditionsListPresenter implements MedConditionsListContract.Presenter {

    private final MedConditionsListContract.View mView;

    private final Context mContext;

    @Inject
    PetMedsApiService medsApiService;

    public MedConditionsListPresenter(MedConditionsListContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getConditionsList() {

        medsApiService.getPetEducationCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PetEducationCategoriesResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        //hide the progress
                        mView.hideProgress();
                        //show the retry view
                        if (errorId == R.string.noInternetConnection || errorId == R.string.connectionTimeout) {
                            mView.showRetryView(mContext.getString(errorId));
                        }
                        mView.showErrorCrouton(e.getMessage(), false);
                    }

                    @Override
                    public void onNext(PetEducationCategoriesResponse categoriesResponse) {
                        if (categoriesResponse.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            mView.hideProgress();
                            mView.populateConditionsListView(categoriesResponse.getCategories());
                        } else {
                            mView.hideProgress();
                            mView.showErrorCrouton(categoriesResponse.getStatus().getErrorMessages().get(0), false);
                        }
                    }
                });
    }

    @Override
    public void start() {

    }
}
