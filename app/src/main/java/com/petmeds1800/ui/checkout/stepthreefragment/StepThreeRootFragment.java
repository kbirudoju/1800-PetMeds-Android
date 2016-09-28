package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.shoppingcart.ShippingAddress;
import com.petmeds1800.model.shoppingcart.ShippingGroups;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;
import com.petmeds1800.ui.address.AddEditAddressFragment;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.checkout.steponerootfragment.StepOneRootContract;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.payment.AddEditCardFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

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

    private ShoppingCartListResponse mShoppingCartListResponse;

    private Address mAddress;

    public static StepThreeRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse) {
        StepThreeRootFragment f = new StepThreeRootFragment();
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
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
        Bundle bundle = getArguments();
        mShoppingCartListResponse = (ShoppingCartListResponse) bundle.getSerializable(CartFragment.SHOPPING_CART);
        populateAddress();
        return view;

    }

    public void populateAddress() {

        ArrayList<ShippingGroups> shippingGroupses = mShoppingCartListResponse.getShoppingCart().getShippingGroups();
        ShippingAddress shippingAddress = null;
        mAddress = new Address();
        if (shippingGroupses.size() > 0) {
            shippingAddress = shippingGroupses.get(0).getShippingAddress();
            mAddress.setAddress1(shippingAddress.getAddress1());
            mAddress.setAddress2(shippingAddress.getAddress2());
            mAddress.setCity(shippingAddress.getCity());
            mAddress.setState(shippingAddress.getState());
            mAddress.setCountry(shippingAddress.getCountry());
            mAddress.setFirstName(shippingAddress.getFirstName());
            mAddress.setLastName(shippingAddress.getLastName());
            mAddress.setPhoneNumber(shippingAddress.getPhoneNumber());
            mAddress.setPostalCode(shippingAddress.getPostalCode());
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CheckOutActivity) getActivity()).setCheckOutCircleAsSelected(CheckOutActivity.THIRD_SHIPMENT_CHECKOUT_CIRCLE);
        replaceStepRootChildFragment(
                PaymentSelectionListFragment.newInstance(PaymentSelectionListFragment.REQUEST_CODE),
                R.id.creditCardDetailFragment);
        replaceStepRootChildFragment(AddEditAddressFragment.newInstance(mAddress, StepThreeRootFragment.REQUEST_CODE),
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
