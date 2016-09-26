package com.petmeds1800.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.petmeds1800.R;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;
import com.petmeds1800.ui.shoppingcart.ShoppingCartListContract;
import com.petmeds1800.ui.shoppingcart.presenter.ShoppingCartListPresenter;
import com.petmeds1800.util.ShoppingCartAdapterViewHolder;
import com.petmeds1800.util.ShoppingCartRecyclerViewAdapter;

/**
 * Created by pooja on 8/2/2016.
 */
public class CartFragment extends AbstractFragment implements ShoppingCartListContract.View{

    private RecyclerView containerLayoutItems;
    ShoppingCartListContract.Presenter mPresenter;
    private ShoppingCartRecyclerViewAdapter shoppingCartRecyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ShoppingCartListPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getShoppingCartList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cart,container,false);
        containerLayoutItems = (RecyclerView) view.findViewById(R.id.products_offered_RecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        containerLayoutItems.setLayoutManager(mLayoutManager);
        containerLayoutItems.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public boolean populateShoppingCartResponse(ShoppingCartListResponse shoppingCartListResponse) {
        ShoppingCartListResponse local_ShoppingCartListResponse = shoppingCartListResponse;
        shoppingCartRecyclerViewAdapter = new ShoppingCartRecyclerViewAdapter(local_ShoppingCartListResponse.getShoppingCart().getCommerceItems(),createFooter(((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_offer_code_card, null, false)),getActivity());
        containerLayoutItems.setAdapter(shoppingCartRecyclerViewAdapter);
        return true;
    }

    @Override
    public void onError(String errorMessage) {
    }

    @Override
    public void setPresenter(ShoppingCartListContract.Presenter presenter) {

    }

    private View createFooter(View footerView){
        LinearLayout OfferCodeContainerLayout = (LinearLayout) footerView.findViewById(R.id.cart_each_item_container);
        TextInputLayout CouponCodeLayout = (TextInputLayout) footerView.findViewById(R.id.coupon_code_input_layout);
        Button OrderStatusLAbel = (Button) footerView.findViewById(R.id.order_status_label);
        return footerView;
    }
}
