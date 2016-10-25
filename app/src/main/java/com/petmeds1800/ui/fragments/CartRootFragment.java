package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.ui.checkout.confirmcheckout.ConfirmationReceiptRootFragment;
import com.petmeds1800.util.Constants;

/**
 * Created by Sarthak on 9/30/2016.
 */

public class CartRootFragment extends AbstractFragment {

    public static CartRootFragment newInstance(int requestCode) {

        Bundle args = new Bundle();

        CartRootFragment fragment = new CartRootFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_container, container, false);
        replaceCartFragment(new CartFragment());
        return view;
    }


    public void replaceConfirmOrderFragment(CommitOrderResponse commitOrderResponse) {
        if (commitOrderResponse != null) {
            ConfirmationReceiptRootFragment confirmationReceiptRootFragment = new ConfirmationReceiptRootFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.CONFIRMATION_ORDER_RESPONSE, commitOrderResponse);
            confirmationReceiptRootFragment.setArguments(bundle);
            replaceCartFragment(confirmationReceiptRootFragment);
        } else{
            replaceCartFragmentWithTag(new CartFragment(), CartFragment.class.getName());
        }

    }
}
