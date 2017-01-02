package com.petmeds1800.ui.support;

import com.petmeds1800.model.ProductCategory;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Abhinav on 11/8/16.
 */
public interface HomeFragmentContract {


    interface View extends BaseView<Presenter> {

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }

    interface ProductCategoryInteractionListener {
        void startWebViewFragment(ProductCategory productCategory);
        void replaceWebViewFragment(String url,String title);
        void replaceBannerView(String url);
    }

}
