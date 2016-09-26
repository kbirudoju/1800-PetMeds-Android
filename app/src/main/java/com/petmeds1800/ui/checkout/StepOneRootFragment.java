package com.petmeds1800.ui.checkout;

import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.ui.CheckOutActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 21-09-2016.
 */

public class StepOneRootFragment extends AbstractFragment {

    public final static int REQUEST_CODE = 2;

    @BindView(R.id.shippingNavigator)
    Button mShippingNavigator;

    Address mAddress;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static StepOneRootFragment newInstance() {
        StepOneRootFragment f = new StepOneRootFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        ((CheckOutActivity) getActivity()).setCheckOutCircleAsSelected(CheckOutActivity.FIRST_SHIPMENT_CHECKOUT_CIRCLE);
//        replaceStepRootChildFragment(AddressSelectionListFragment.newInstance(REQUEST_CODE), R.id.detailFragment);
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communicationfragment);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setAddress(Address address) {
        mAddress = address;
    }

    public Address getAddress() {
        return mAddress;
    }

    @OnClick(R.id.shippingNavigator)
    public void onClick() {
        if (mAddress != null) {
            Log.v("test", ":::::::::::::::::::" + mAddress.getAddressId());
        }

    }
}
