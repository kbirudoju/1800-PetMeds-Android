package com.petmeds1800.ui.dashboard;

import com.petmeds1800.model.Address;
import com.petmeds1800.model.ProductCategory;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by Abhinav on 11/8/16.
 */
public interface ProductCategoryListContract {


    interface View extends BaseView<Presenter> {

        boolean isActive();
        void startWebView(ProductCategory productCategory);
    }

    interface Presenter extends BasePresenter {

        void getProductCategories();
    }

}
