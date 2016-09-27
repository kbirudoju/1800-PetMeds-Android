package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.ui.address.AddEditAddressFragment;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.checkout.steponerootfragment.StepOneRootContract;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.payment.AddEditCardFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 27-09-2016.
 */

public class StepThreeRootFragment extends AbstractFragment {

    @BindView(R.id.newPaymentMethod)
    Button mNewPaymentMethod;

    private StepOneRootContract.Presenter mPresenter;

    public final static int REQUEST_CODE = 6;

    public static StepThreeRootFragment newInstance() {
        StepThreeRootFragment f = new StepThreeRootFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_three_checkout, container, false);
        PetMedsApplication.getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CheckOutActivity) getActivity()).setCheckOutCircleAsSelected(CheckOutActivity.THIRD_SHIPMENT_CHECKOUT_CIRCLE);
        replaceStepRootChildFragment(
                PaymentSelectionListFragment.newInstance(PaymentSelectionListFragment.REQUEST_CODE),
                R.id.creditCardDetailFragment);
        replaceStepRootChildFragment(AddEditAddressFragment.newInstance(null, StepThreeRootFragment.REQUEST_CODE),
                R.id.billingAddressfragment);
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communicationfragment);

    }

    @OnClick(R.id.newPaymentMethod)
    public void onClick() {
        ((CheckOutActivity) getActivity()).replaceCheckOutFragment(
                AddEditCardFragment.newInstance(StepThreeRootFragment.REQUEST_CODE),
                AddEditCardFragment.class.getName(), true);
    }
}
