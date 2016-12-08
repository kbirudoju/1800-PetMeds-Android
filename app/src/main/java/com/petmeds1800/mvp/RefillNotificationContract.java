package com.petmeds1800.mvp;

import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;

/**
 * Created by pooja on 12/8/2016.
 */
public interface RefillNotificationContract {
    interface View extends BaseView<Presenter> {

        void onSecurityStatusSuccess();
        void onSecurityStatusError();
        void onShoppingCartSuccess(ShoppingCartListResponse response);
        void onShoppingCartError(String errorMsg);
        void onHomeWidgetError(String errorMsg);
    }

    interface Presenter extends BasePresenter {

        void checkSecurityStatus();

    }
}
