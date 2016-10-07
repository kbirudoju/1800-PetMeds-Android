package com.petmeds1800.ui.dashboard.presenter;

import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

import java.util.List;

/**
 * Created by pooja on 9/15/2016.
 */
public interface WidgetContract {
    interface View extends BaseView<Presenter>{
        void updateWidgetData(List<Object> widgetData);
        boolean isActive();
        void onSuccess(List<Object> widgetListData);
        void onError(String errorMessage);
        void addToCartSuccess();
    }

    interface Presenter extends BasePresenter {
        void getWidgetListData();
        void addToCart(AddToCartRequest addToCartRequest);

    }
}
