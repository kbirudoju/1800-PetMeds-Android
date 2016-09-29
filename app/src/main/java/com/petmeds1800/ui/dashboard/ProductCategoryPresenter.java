package com.petmeds1800.ui.dashboard;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.ProductCategoryListResponse;
import com.petmeds1800.util.RetrofitErrorHandler;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Abhinav on 29/9/16.
 */
public class ProductCategoryPresenter implements ProductCategoryListContract.Presenter {

    private final ProductCategoryListContract.View mView;

    private final Context mContext;

    @Inject
    PetMedsApiService mPetMedsApiService;


    ProductCategoryPresenter(ProductCategoryListContract.View view, Context context) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getProductCategories() {
        mPetMedsApiService.getProductCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductCategoryListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //error handling would be implemented once we get the details from backend team
                        Log.e("GetProductCategory", e.getMessage());

                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if (mView.isActive()) {
                            //hide the progress
                            mView.hideProgress();
                            //show the retry view
                            if (errorId == R.string.noInternetConnection || errorId == R.string.connectionTimeout) {
                                mView.showRetryView(mContext.getString(errorId));
                            }

                            mView.showErrorCrouton(e.getMessage(), false);
                        }

                    }

                    @Override
                    public void onNext(ProductCategoryListResponse s) {
                        if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                            if (mView.isActive()) {
                                mView.hideProgress();
                                mView.populateCategoryList(s.getCategoryLinks());
                            }
                        } else {
                            Log.d("GetProductCategoryList", s.getStatus().getErrorMessages().get(0));
                            if (mView.isActive()) {
                                mView.hideProgress();
                                mView.showRetryView(s.getStatus().getErrorMessages().get(0));
                            }
                        }

                    }
                });

    }

    @Override
    public void start() {

    }
}
