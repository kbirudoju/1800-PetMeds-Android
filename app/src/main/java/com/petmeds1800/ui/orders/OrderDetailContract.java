package com.petmeds1800.ui.orders;

import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.ReOrderRequest;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by pooja on 10/5/2016.
 */
public interface OrderDetailContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();
        void onError(String errorMessage);
        void onSuccess();
        void addToCartSuccess();
        void onCancelSuccess();

    }

    interface Presenter extends BasePresenter {
        void reOrder(ReOrderRequest reOrderRequest);
        void cancelOrder(ReOrderRequest reOrderRequest);
        void addToCart(AddToCartRequest addToCartRequest);
    }
}
