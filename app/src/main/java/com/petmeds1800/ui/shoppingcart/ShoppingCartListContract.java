package com.petmeds1800.ui.shoppingcart;

import com.petmeds1800.model.PayPalCheckoutRequest;
import com.petmeds1800.model.shoppingcart.request.AddItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.ApplyCouponRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.RemoveItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.UpdateItemQuantityRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.BasePresenter;
import com.petmeds1800.mvp.BaseView;

/**
 * Created by Sarthak on 9/23/2016.
 */

public interface ShoppingCartListContract {

    interface View extends BaseView<ShoppingCartListContract.Presenter> {
        boolean isActive();
        boolean postGeneralPopulateShoppingCart(ShoppingCartListResponse shoppingCartListResponse);
        boolean onError(String errorMessage, String simpleName);
        void onSuccess(String url);
        void onPayPalError(String errorMsg);
        void showRetryView();
        void hideRetryView();
    }

    interface Presenter extends BasePresenter {
        void getGeneralPopulateShoppingCart(boolean isRepeat);
        void getAddItemShoppingCart(AddItemRequestShoppingCart addItemRequestShoppingCart,boolean isRepeat);
        void getRemoveItemShoppingCart(RemoveItemRequestShoppingCart removeItemRequestShoppingCart,boolean isRepeat);
        void getApplyCouponShoppingCart(ApplyCouponRequestShoppingCart applyCouponRequestShoppingCart,boolean isRepeat);
        void getUpdateItemQuantityRequestShoppingCart(UpdateItemQuantityRequestShoppingCart updateItemQuantityRequestShoppingCart,boolean isRepeat);
        void checkoutPayPal(PayPalCheckoutRequest request);
    }
}
