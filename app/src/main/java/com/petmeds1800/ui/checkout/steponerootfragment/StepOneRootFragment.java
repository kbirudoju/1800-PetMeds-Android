package com.petmeds1800.ui.checkout.steponerootfragment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.SavedShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;
import com.petmeds1800.ui.address.AddressSelectionListFragment;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 21-09-2016.
 */

public class StepOneRootFragment extends AbstractFragment implements StepOneRootContract.View {

    public final static int REQUEST_CODE = 2;

    @BindView(R.id.shippingNavigator)
    Button mShippingNavigator;

    @BindView(R.id.containerLayout)
    RelativeLayout mContainerLayout;

    private Address mAddress;

    private StepOneRootContract.Presenter mPresenter;


    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static StepOneRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse) {
        StepOneRootFragment f = new StepOneRootFragment();
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
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

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new StepOneRootPresentor(this);
        PetMedsApplication.getAppComponent().inject(this);

    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        mAddress = address;
    }

    @OnClick(R.id.shippingNavigator)
    public void onClick() {
        if (mAddress != null) {
            mPresenter.saveShippingAddress(
                    new SavedShippingAddressRequest(
                            mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber(),
                            mAddress.getAddressId()));
        }

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }



    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }


    @Override
    public void setPresenter(StepOneRootContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
