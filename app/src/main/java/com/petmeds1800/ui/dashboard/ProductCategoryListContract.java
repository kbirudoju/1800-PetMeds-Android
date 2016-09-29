package com.petmeds1800.ui.dashboard;

import com.petmeds1800.model.Address;
import com.petmeds1800.model.ProductCategory;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhinav on 11/8/16.
 */
public interface ProductCategoryListContract {


    interface View extends BaseView<Presenter> {

        void showProgress();
        void hideProgress();
        boolean isActive();

        void showErrorCrouton(CharSequence message, boolean span);

        void showRetryView(String errorMessage);
        void populateCategoryList(ArrayList<ProductCategory> productCategoryList);
        void startWebView(ProductCategory productCategory);
    }

    interface Presenter extends BasePresenter {

        void getProductCategories();
    }

}
