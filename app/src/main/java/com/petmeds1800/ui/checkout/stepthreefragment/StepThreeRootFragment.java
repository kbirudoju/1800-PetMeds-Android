package com.petmeds1800.ui.checkout.stepthreefragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.Card;
import com.petmeds1800.model.entities.CreditCardPaymentMethodRequest;
import com.petmeds1800.model.shoppingcart.response.ShippingAddress;
import com.petmeds1800.model.shoppingcart.response.ShippingGroups;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.address.AddEditAddressFragment;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.payment.AddEditCardFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 27-09-2016.
 */

public class StepThreeRootFragment extends AbstractFragment implements StepThreeRootContract.View {

    @BindView(R.id.newPaymentMethod)
    Button mNewPaymentMethod;

    public final static int REQUEST_CODE = 6;

    @BindView(R.id.shippingNavigator)
    Button mShippingNavigator;

    private ShoppingCartListResponse mShoppingCartListResponse;

    private Address mAddress;

    private CheckOutActivity activity;

    private Bundle mBundle;

    private String mStepName;

    private StepThreeRootContract.Presenter mPresenter;

    private Card mCard;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    public static StepThreeRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse,
            String stepName) {
        StepThreeRootFragment f = new StepThreeRootFragment();
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
        args.putString(CheckOutActivity.STEP_NAME, stepName);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CheckOutActivity) {
            activity = (CheckOutActivity) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.setToolBarTitle(getString(R.string.payment_method_header));
        mShoppingCartListResponse = (ShoppingCartListResponse) getArguments().getSerializable(CartFragment.SHOPPING_CART);
        mStepName = getArguments().getString(CheckOutActivity.STEP_NAME);
        activity.setLastCompletedSteps(mStepName);
        activity.setActiveStep(mStepName);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_three_checkout, container, false);
        PetMedsApplication.getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        populateAddress();
        return view;

    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card card) {
        mCard = card;
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
        mPresenter = new StepThreeRootPresentor(this);
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
        activity.replaceCheckOutFragment(
                AddEditCardFragment.newInstance(StepThreeRootFragment.REQUEST_CODE),
                AddEditCardFragment.class.getName(), true);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccessCreditCardPayment(ShoppingCartListResponse response) {
        activity.hideProgress();
        activity.moveToNext(mStepName, response);

    }

    @Override
    public void onError(String errorMessage) {
        activity.hideProgress();
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        activity.hideProgress();
    }

    @Override
    public void setPresenter(StepThreeRootContract.Presenter presenter) {

    }

    @OnClick(R.id.shippingNavigator)
    public void onShippingNavigatorClick() {
        activity.showProgress();
        Card card;
        card = getCard();
        if (card != null) {
            CreditCardPaymentMethodRequest request = new CreditCardPaymentMethodRequest();
            request.setCardId(card.getId());
            request.setBillingAddressId(card.getBillingAddress().getRepositoryId());
            request.setDynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
            mPresenter.applyCreditCardPaymentMethod(request);
        }

    }
}
