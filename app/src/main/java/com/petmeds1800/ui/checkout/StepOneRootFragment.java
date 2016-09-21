package com.petmeds1800.ui.checkout;

import com.petmeds1800.R;
import com.petmeds1800.ui.CheckOutActivity;
import com.petmeds1800.ui.address.AddressSelectionListFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sdixit on 21-09-2016.
 */

public class StepOneRootFragment extends AbstractFragment {

    public final static int REQUEST_CODE = 2;

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
        replaceStepRootChildFragment(AddressSelectionListFragment.newInstance(REQUEST_CODE), R.id.detailFragment);
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communicationfragment);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
