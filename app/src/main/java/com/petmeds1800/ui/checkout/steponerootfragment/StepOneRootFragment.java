package com.petmeds1800.ui.checkout.steponerootfragment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.SavedShippingAddressRequest;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.address.AddressSelectionListFragment;
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

    public final static String SHIPPING_ADDRESS_KEY = "shippingAddressId";

    @BindView(R.id.shippingNavigator)
    Button mShippingNavigator;

    @BindView(R.id.containerLayout)
    RelativeLayout mContainerLayout;

    private Address mAddress;

    private StepOneRootContract.Presenter mPresenter;

    private String mStepName;

    private String mshippingAddressId;


    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private ShoppingCartListResponse mShoppingCartListResponse;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStepName = getArguments().getString(CheckOutActivity.STEP_NAME);
        mShoppingCartListResponse = (ShoppingCartListResponse) getArguments()
                .getSerializable(CartFragment.SHOPPING_CART);
    }

    public static StepOneRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse, String stepName) {
        StepOneRootFragment f = new StepOneRootFragment();
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
        args.putString(CheckOutActivity.STEP_NAME, stepName);
        f.setArguments(args);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new StepOneRootPresentor(this);
        PetMedsApplication.getAppComponent().inject(this);
        if (mShoppingCartListResponse != null) {
            mshippingAddressId = mShoppingCartListResponse.getShoppingCart().getShippingAddressId();
        }
        replaceStepRootChildFragment(AddressSelectionListFragment.newInstance(REQUEST_CODE, mshippingAddressId),
                R.id.detailFragment);
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communicationfragment);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            ((CheckOutActivity) getActivity()).setActiveStep(mStepName);
            ((CheckOutActivity) getActivity()).setToolBarTitle(getString(R.string.shipment_address));
        }
    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        mAddress = address;
    }

    @OnClick(R.id.shippingNavigator)
    public void onClick() {
        ((CheckOutActivity) getActivity()).showProgress();
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
    public void onSuccess(ShoppingCartListResponse response) {
        ((CheckOutActivity) getActivity()).hideProgress();
        ((CheckOutActivity) getActivity()).moveToNext(mStepName, response);
    }


    @Override
    public void onError(String errorMessage) {
        ((CheckOutActivity) getActivity()).hideProgress();

    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        ((CheckOutActivity) getActivity()).hideProgress();
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }


    @Override
    public void setPresenter(StepOneRootContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
