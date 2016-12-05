package com.petmeds1800.ui.dashboard;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.ProductCategoryListResponse;
import com.petmeds1800.util.RetrofitErrorHandler;

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

    @Inject
    PetMedsApiService mPetMedsApiService;


    ProductCategoryPresenter(ProductCategoryListContract.View view) {
        mView = view;
        mView.setPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
    }

    @Override
    public void getProductCategories() {
        mView.showProgress();
        mPetMedsApiService.getProductCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductCategoryListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //hide the progress
                        mView.hideProgress();
                        //error handling would be implemented once we get the details from backend team
                        Log.e("GetProductCategory", e.getMessage());
                        if (mView.isActive()) {
                            int errorId = RetrofitErrorHandler.getErrorMessage(e);
                            //show the retry view
                            if (errorId == R.string.noInternetConnection || errorId == R.string.connectionTimeout) {
                                mView.showRetryView();
                            }else{
                                mView.showErrorCrouton(e.getMessage(), true);
                            }
                        }

                    }

                    @Override
                    public void onNext(ProductCategoryListResponse s) {
                        mView.hideProgress();
                        if(mView.isActive()){
                            if (s.getStatus().getCode().equals(API_SUCCESS_CODE)) {
                                mView.populateCategoryList(s.getCategoryLinks());
                            } else {
                                Log.d("GetProductCategoryList", s.getStatus().getErrorMessages().get(0));
                                mView.showRetryView();
                            }
                        }
                    }
                });

    }

    @Override
    public void start() {

    }
}
