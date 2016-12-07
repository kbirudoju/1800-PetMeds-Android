package com.petmeds1800.ui.checkout.stepthreefragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.Card;
import com.petmeds1800.model.PayPalCheckoutRequest;
import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.UpdateCardRequest;
import com.petmeds1800.model.shoppingcart.response.PaymentGroups;
import com.petmeds1800.model.shoppingcart.response.ShippingAddress;
import com.petmeds1800.model.shoppingcart.response.ShippingGroups;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.address.AddEditAddressFragment;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhinav on 27-09-2016.
 */

public class GuestStepThreeRootFragment extends AbstractFragment implements GuestStepThreeRootContract.View,CompoundButton.OnCheckedChangeListener, CheckOutActivity.PaypalErrorListener {

    @BindView(R.id.newPaymentMethod)
    Button mNewPaymentMethod;

    public final static int REQUEST_CODE = 8;

    @BindView(R.id.shippingNavigator)
    Button mShippingNavigator;

    @BindView(R.id.stepThreeContainerLayout)
    RelativeLayout mContainerLayout;

    private ShoppingCartListResponse mShoppingCartListResponse;

    private Address mAddress;

    private CheckOutActivity activity;

    private Bundle mBundle;

    private String mStepName;

    private GuestStepThreePresenter mPresenter;

    private Card mCard;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @BindView(R.id.payPalCheckBox)
    CheckBox mPaypalCheckbox;

    private PaymentGroups mPaymentGroup;

    public static GuestStepThreeRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse,
                                                         String stepName) {
        GuestStepThreeRootFragment f = new GuestStepThreeRootFragment();
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
        mShoppingCartListResponse = (ShoppingCartListResponse) getArguments()
                .getSerializable(CartFragment.SHOPPING_CART);
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

        mNewPaymentMethod.setVisibility(View.GONE);

        if (((CheckOutActivity) getActivity()).getApplicableSteps() == 4) {
            mShippingNavigator.setText(getString(R.string.review_submit_navigator_button_title));
        }

        populateAddress();
        populatePaymentGroup();
        mPaypalCheckbox.setOnCheckedChangeListener(this);
        return view;

    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card card) {
        mCard = card;
    }

    public void populateAddress() {
        //we need to perform this unnecessary conversion into Address model as reusable 'AddEditFragment' only knows about Address model
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

    private void populatePaymentGroup() {
        mPaymentGroup = mShoppingCartListResponse.getShoppingCart().getPaymentGroups().get(0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new GuestStepThreePresenter(this);

        //add the creditCardFragment
        //check if a payment method is present
        if(mPaymentGroup == null) {
            replaceStepRootChildFragment(
                    AddGuestCardFragment.newInstance(),
                    R.id.creditCardDetailFragment);
        }
        else {
            replaceStepRootChildFragment(
                    AddGuestCardFragment.newInstance(mPaymentGroup),
                    R.id.creditCardDetailFragment);
        }

        //add the addeditAddressFragment
        replaceStepRootChildFragment(
                AddGuestCardFragment.newInstance(),
                R.id.creditCardDetailFragment);
        replaceStepRootChildFragment(AddEditAddressFragment.newInstance(mAddress, GuestStepThreeRootFragment.REQUEST_CODE),
                R.id.billingAddressfragment);
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communicationfragment);

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccessAddressAddition(AddAddressResponse addAddressResponse) {

    }

    @Override
    public void onSuccessCreditCardAddition(Object response) {
        activity.hideProgress();
    }

    @Override
    public void onError(String errorMessage) {
        activity.hideProgress();
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        activity.hideProgress();
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }


    @OnClick(R.id.shippingNavigator)
    public void onShippingNavigatorClick() {
        //TODO call the presenter to first save the address and then card and then apply both of them
        AddGuestCardFragment addGuestCardFragment = (AddGuestCardFragment) getChildFragmentManager().findFragmentById(R.id.creditCardDetailFragment);

        AddressRequest addressRequest;
        if(addGuestCardFragment != null) {
            if( addGuestCardFragment.checkAndShowError()) {

                AddEditAddressFragment addEditAddressFragment = (AddEditAddressFragment) getChildFragmentManager().findFragmentById(R.id.billingAddressfragment);
                if(addEditAddressFragment != null) {
                    if(addEditAddressFragment.validateFields()) {
                        addEditAddressFragment.initializeGuestAddressRequest();
                        addressRequest = addEditAddressFragment.getAddressRequest();

                        //check if its a addCard request or update card depending on the availablity of the paymentCardKey
                        if(mShoppingCartListResponse.getShoppingCart().getPaymentCardKey() == null ||
                                mShoppingCartListResponse.getShoppingCart().getPaymentCardKey().isEmpty()) {

                            CardRequest cardRequest = addGuestCardFragment.getCard();

                            mPresenter.applyCreditCardPaymentMethod(addressRequest , cardRequest , null);
                            activity.showProgress();
                        }
                        else {
                            //pass the cardKey and get the updated card request
                            UpdateCardRequest updateCardRequest = addGuestCardFragment.getUpdatedCard(mShoppingCartListResponse.getShoppingCart().getPaymentCardKey());

                            mPresenter.applyCreditCardPaymentMethod(addressRequest , null , updateCardRequest);
                            activity.showProgress();
                        }

                    }
                }
            }
        }

    }

    @Override
    public void onSuccessCreditCardPayment(ShoppingCartListResponse response) {
        activity.hideProgress();
        activity.moveToNext(mStepName, response);

    }

    @Override
    public void onSuccess(String url) {
        activity.hideProgress();
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.PAYPAL_DATA, url);
        bundle.putBoolean(CommonWebviewFragment.ISCHECKOUT, true);
        bundle.putString(CommonWebviewFragment.STEPNAME, mStepName);
        ((CheckOutActivity) getActivity())
                .replaceCheckOutFragmentWithBundle(new CommonWebviewFragment(), CommonWebviewFragment.class.getName(),
                        false, bundle);
    }

    @Override
    public void onPayPal(final String errorMsg) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.displayCrouton(getActivity(), errorMsg, mContainerLayout);
                    }
                });
            }
        };
        thread.start();
    }

    @Override
    public void setPresenter(GuestStepThreeRootContract.Presenter presenter) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        activity.showProgress();
        PayPalCheckoutRequest payPalCheckoutRequest = new PayPalCheckoutRequest("checkout");
        mPresenter.checkoutPayPal(payPalCheckoutRequest);
    }

    @Override
    public void onPayPalError(String errorMsg) {
        activity.hideProgress();
        if (errorMsg.isEmpty()) {
            Utils.displayCrouton(getActivity(), getString(R.string.unexpected_error_label), mContainerLayout);

        } else {
            Utils.displayCrouton(getActivity(), errorMsg, mContainerLayout);

        }
    }
}
