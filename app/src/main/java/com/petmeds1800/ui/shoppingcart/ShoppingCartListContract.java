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
    }

    interface Presenter extends BasePresenter {
        void getGeneralPopulateShoppingCart();
        void getAddItemShoppingCart(AddItemRequestShoppingCart addItemRequestShoppingCart);
        void getRemoveItemShoppingCart(RemoveItemRequestShoppingCart removeItemRequestShoppingCart);
        void getApplyCouponShoppingCart(ApplyCouponRequestShoppingCart applyCouponRequestShoppingCart);
        void getUpdateItemQuantityRequestShoppingCart(UpdateItemQuantityRequestShoppingCart updateItemQuantityRequestShoppingCart);
        void checkoutPayPal(PayPalCheckoutRequest request);
    }
}
