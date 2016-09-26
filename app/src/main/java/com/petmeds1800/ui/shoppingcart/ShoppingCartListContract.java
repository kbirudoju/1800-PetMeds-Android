package com.petmeds1800.ui.shoppingcart;

import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sarthak on 9/23/2016.
 */

public interface ShoppingCartListContract {

    interface View extends BaseView<ShoppingCartListContract.Presenter> {
        boolean isActive();
        boolean populateShoppingCartResponse(ShoppingCartListResponse shoppingCartListResponse);
        void onError(String errorMessage);
    }

    interface Presenter extends BasePresenter {
        void getShoppingCartList();
    }
}
