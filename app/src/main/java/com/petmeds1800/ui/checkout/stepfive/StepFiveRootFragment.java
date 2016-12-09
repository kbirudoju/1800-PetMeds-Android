package com.petmeds1800.ui.checkout.stepfive;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.HomeIntent;
import com.petmeds1800.model.entities.CheckoutSteps;
import com.petmeds1800.model.entities.CommitOrderRequest;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.model.entities.Item;
import com.petmeds1800.model.entities.Order;
import com.petmeds1800.model.entities.OrderReviewSubmitResponse;
import com.petmeds1800.model.entities.PaymentMethod;
import com.petmeds1800.model.entities.ShippingReviewMethod;
import com.petmeds1800.model.shoppingcart.response.ShippingAddress;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.dialog.CommonDialogFragment;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 28-09-2016.
 */

public class StepFiveRootFragment extends AbstractFragment
        implements StepFiveRootContract.View, ReviewSubmitAdapter.OpenDailogListener {


    @BindView(R.id.shippingAddressDetailsText)
    TextView mShippingAddressDetailsText;

    @BindView(R.id.shippingMethodDetailsText)
    TextView mShippingMethodDetailsText;

    @BindView(R.id.paymentMethodDetailsText)
    TextView mPaymentMethodDetailsText;

    @BindView(R.id.petVetDetailsText)
    TextView mPetVetDetailsText;

    @BindView(R.id.ordersList)
    RecyclerView mOrdersRecyclerView;

    @BindView(R.id.editCardButton)
    Button mEditCardButton;

    @BindView(R.id.subTotalValue)
    TextView mSubTotalValue;

    @BindView(R.id.offerCodeValue)
    TextView mOfferCodeValue;

    @BindView(R.id.shippingValue)
    TextView mShippingValue;

    @BindView(R.id.taxesValue)
    TextView mTaxesValue;

    @BindView(R.id.totalValue)
    TextView mTotalValue;

    @BindView(R.id.shippingNavigator)
    Button mShippingNavigator;

    @BindView(R.id.stepFiveRootContainer)
    RelativeLayout mContainerLayout;

    @BindView(R.id.shippingAddressEdit)
    ImageButton mShippingAddressEdit;

    @BindView(R.id.shippingMethodEdit)
    ImageButton mShippingMethodEdit;

    @BindView(R.id.paymentMethodEdit)
    ImageButton mPaymentMethodEdit;

    @BindView(R.id.petVetEdit)
    ImageButton mPetVetEdit;

    @BindView(R.id.subTotal)
    TextView mSubTotal;

    @BindView(R.id.shippingMethodContainer)
    RelativeLayout mShippingMethodContainer;

    @BindView(R.id.paymentMethodText)
    TextView mPaymentMethodText;

    @BindView(R.id.paymentMethodContainer)
    RelativeLayout mPaymentMethodContainer;

    @BindView(R.id.petVetText)
    TextView mPetVetText;

    @BindView(R.id.petVetContainer)
    RelativeLayout mPetVetContainer;

    @BindView(R.id.itemsheaderText)
    TextView mItemsheaderText;

    @BindView(R.id.communicationfragment)
    FrameLayout mCommunicationfragment;

    @BindView(R.id.scrollerView)
    ScrollView mScrollerView;

    @BindView(R.id.offerCode)
    TextView mOfferCode;

    @BindView(R.id.shipping)
    TextView mShipping;

    @BindView(R.id.taxes)
    TextView mTaxes;

    @BindView(R.id.total)
    TextView mTotal;

    @BindView(R.id.bottomContainer)
    RelativeLayout mBottomContainer;

    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;


    private String mStepName;

    private CheckOutActivity activity;

    private StepFiveRootContract.Presenter mPresenter;


    private Order mOrder;


    public static final String DOLLAR_SIGN = "$";

    public static final String MINUS_SIGN = "-";

    private static final String BLANK_SPACE = " ";

    private static final String FORWARD_SLASH = "/";

    private static final int ADD_ONE_MONTH = 1;

    private static final int SIZE_THREE = 3;

    private static final int SIZE_FOUR = 4;

    private static final int SIZE_FIVE = 5;

    private ShippingAddress mShippingAddress;

    private ShippingReviewMethod mShippingMethod;

    private PaymentMethod mPaymentMethod;

    private ShoppingCartListResponse mShoppingCartListResponse;

    private CheckoutSteps mCheckoutSteps;

    private ArrayList<String> mApplicableSteps;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private ArrayList<String> commerceItemIds;

    private ArrayList<Integer> reminderMonths;

    private ReviewSubmitAdapter mReviewSubmitAdapter;

    private final int ADD_ONE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AnalyticsUtil().trackScreen(getString(R.string.label_order_review_analytics_title));
        commerceItemIds = new ArrayList<String>();
        reminderMonths = new ArrayList<Integer>();
        mShoppingCartListResponse = (ShoppingCartListResponse) getArguments()
                .getSerializable(CartFragment.SHOPPING_CART);
        mStepName = getArguments().getString(CheckOutActivity.STEP_NAME);
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communicationfragment);
    }


    public static StepFiveRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse, String stepName) {
        StepFiveRootFragment f = new StepFiveRootFragment();
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
        View view = inflater.inflate(R.layout.fragment_step_five_checkout, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
        mPresenter = new StepFiveRootPresentor(this);
        mPresenter.getOrderReviewDetails(
                mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (CheckOutActivity) getActivity();
        if (savedInstanceState == null) {
            activity.setToolBarTitle(getString(R.string.review_submit_order_title));
            activity.setLastCompletedSteps(mStepName);
            activity.setActiveStep(mStepName);
        }
    }

    private void setupCardsRecyclerView(ArrayList<Item> items) {
        mReviewSubmitAdapter = new ReviewSubmitAdapter(getContext());
        mReviewSubmitAdapter.setOpenDailogListener(this);
        mReviewSubmitAdapter.setItems(items);
        mOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrdersRecyclerView.setNestedScrollingEnabled(false);
        mOrdersRecyclerView.setAdapter(mReviewSubmitAdapter);
    }

    private void markAsUnselected(int initial, int lastStep) {

        for (int index = initial; index <= lastStep; index++) {
            activity.setCheckOutCircleAsUnSelectedAndUnDone(index);
        }

    }

    @OnClick({R.id.shippingAddressEdit, R.id.shippingMethodEdit, R.id.paymentMethodEdit, R.id.petVetEdit})
    public void onClick(View view) {
        if (mApplicableSteps != null) {
            switch (view.getId()) {
                case R.id.shippingAddressEdit:
                    markAsUnselected(CheckOutActivity.STEP_ONE, CheckOutActivity.SUBMIT_N_REVIEW);
                    activity.startNextStep(mApplicableSteps.get(CheckOutActivity.STEP_ONE),
                            mShoppingCartListResponse);
                    break;
                case R.id.shippingMethodEdit:
                    markAsUnselected(CheckOutActivity.STEP_TWO, CheckOutActivity.SUBMIT_N_REVIEW);
                    activity.startNextStep(mApplicableSteps.get(CheckOutActivity.STEP_TWO),
                            mShoppingCartListResponse);
                    break;
                case R.id.paymentMethodEdit:
                    markAsUnselected(CheckOutActivity.STEP_THREE, CheckOutActivity.SUBMIT_N_REVIEW);
                    activity.startNextStep(mApplicableSteps.get(CheckOutActivity.STEP_THREE),
                            mShoppingCartListResponse, true);
                    break;
                case R.id.petVetEdit:
                    //determine whether payment was done using paypal as it would help us to decide whether to consider
                    if (mApplicableSteps.size() == SIZE_FOUR && !mPaymentMethod.getPaymentType()
                            .equalsIgnoreCase(getString(R.string.label_credit_card))) {
                        //petvet step is at index 2
                        markAsUnselected(CheckOutActivity.STEP_THREE, CheckOutActivity.SUBMIT_N_REVIEW);
                        activity.startNextStep(mApplicableSteps.get(CheckOutActivity.STEP_THREE),
                                mShoppingCartListResponse);
                    } else { // petvet step is at index 3
                        markAsUnselected(CheckOutActivity.STEP_FOUR, CheckOutActivity.SUBMIT_N_REVIEW);
                        activity.startNextStep(mApplicableSteps.get(CheckOutActivity.STEP_FOUR),
                                mShoppingCartListResponse);
                    }

                    break;
            }
        }

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void populateOrderReviewDetails(OrderReviewSubmitResponse response) {

        mProgressbar.setVisibility(View.GONE);
        CheckoutSteps checkoutSteps = response.getCheckoutSteps();
        Order mOrder = response.getOrder();

        if (checkoutSteps != null) {
            mApplicableSteps = checkoutSteps.getApplicableSteps();
        }

        if (mOrder != null) {
            Locale locale = getResources().getConfiguration().locale;
            mSubTotalValue.setText(DOLLAR_SIGN + String.format(locale, "%.2f", mOrder.getOrderSubTotal()));
            mOfferCodeValue.setText(MINUS_SIGN + DOLLAR_SIGN + String.format(locale, "%.2f", mOrder.getDiscount()));
            mShippingValue.setText(DOLLAR_SIGN + String.format(locale, "%.2f", mOrder.getShippingTotal()));
            mTaxesValue.setText(DOLLAR_SIGN + String.format(locale, "%.2f", mOrder.getTaxTotal()));
            mTotalValue.setText(DOLLAR_SIGN + String.format(locale, "%.2f", mOrder.getOrderTotal()));
            setupCardsRecyclerView((ArrayList<Item>) mOrder.getItems());
            mSubTotal.setText(
                    String.format(getString(R.string.subtotal_title), String.valueOf(mOrder.getItems().size())));
            mShippingAddress = mOrder.getShippingAddress();
            mShippingMethod = mOrder.getShippingMethod();
            mPaymentMethod = mOrder.getPaymentMethod();

            if (mShippingAddress != null) {
                mShippingAddressDetailsText.setText(
                        mShippingAddress.getAddress1() + BLANK_SPACE + mShippingAddress.getAddress2() + BLANK_SPACE
                                + mShippingAddress
                                .getState() + BLANK_SPACE + mShippingAddress.getPostalCode());
            }
            if (mShippingMethod != null) {
                mShippingMethodDetailsText.setText(mShippingMethod.getDescription());
            }
            if (mPaymentMethod != null) {
                if (mPaymentMethod.getPaymentType().equalsIgnoreCase(getString(R.string.label_credit_card))) {
                    mPaymentMethodDetailsText.setText(
                            mPaymentMethod.getCardType() + BLANK_SPACE + mPaymentMethod.getCardNumber() + BLANK_SPACE
                                    + mPaymentMethod.getExpirationMonth() + FORWARD_SLASH + mPaymentMethod
                                    .getExpirationyear());
                } else {

                    mPaymentMethodDetailsText.setText(R.string.label_paypal);
                    mPaymentMethodEdit.setVisibility(View.GONE);
                }


            }

        }

        if (mApplicableSteps != null) {

            if ((mApplicableSteps.size() == SIZE_THREE)) {
                mPetVetContainer.setVisibility(View.GONE);
            } else if (mApplicableSteps.size() == SIZE_FOUR && mPaymentMethod.getPaymentType()
                    .equalsIgnoreCase(getString(R.string.label_credit_card))) {
                mPetVetContainer.setVisibility(View.GONE);
            } else {
                mPresenter.populatePetVetInfo((ArrayList<Item>) mOrder.getItems(), mApplicableSteps);
            }
        }

        mBottomContainer.setVisibility(View.VISIBLE);
        mScrollerView.setVisibility(View.VISIBLE);


    }


    @Override
    public void onError(String errorMessage) {
        if (mProgressbar.getVisibility() == View.VISIBLE) {
            mProgressbar.setVisibility(View.GONE);
        }
        activity.hideProgress();
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        activity.hideProgress();
        if (mProgressbar.getVisibility() == View.VISIBLE) {
            mProgressbar.setVisibility(View.GONE);
        }
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }

    @Override
    public void navigateOnOrderConfirmation(CommitOrderResponse response) {
        activity.hideProgress();
        activity.finish();
        //TODO: add integration of ConfirmationReceiptRootFragment here
        Intent intent = new HomeIntent(getActivity());
        intent.putExtra(Constants.CONFIRMATION_ORDER_RESPONSE, response);
        startActivity(intent);

    }

    @Override
    public void setPetVetInfo(String petVetInfo) {
        if (TextUtils.isEmpty(petVetInfo)) {
            mPetVetContainer.setVisibility(View.GONE);
        } else {
            mPetVetDetailsText.setText(petVetInfo);
        }
    }

    @Override
    public void setPresenter(StepFiveRootContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.editCardButton)
    public void onClick() {
        if (!getActivity().isFinishing()) {
            getActivity().finish();
        }

    }

    @OnClick(R.id.shippingNavigator)
    public void onSubmitOrderClick() {
        activity.showProgress();
        CommitOrderRequest commitOrderRequest = new CommitOrderRequest();
        commitOrderRequest
                .set_dynSessConf(mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        commitOrderRequest.setCommerceItemIds(mReviewSubmitAdapter.getCommerceItemsIdList());
        commitOrderRequest.setReminderMonths(mReviewSubmitAdapter.getReorderMonthsIdList());
        mPresenter.submitComittedOrderDetails(commitOrderRequest,
                mShoppingCartListResponse.getShoppingCart().getShoppingCartId());
    }

    @Override
    public void openDailog(String[] data, int code, String title, int defaultValue, final TextView textview,
            final int selectedPosition, final ArrayList<String> reorderMonthsList) {

        FragmentManager fragManager = getFragmentManager();
        CommonDialogFragment commonDialogFragment = CommonDialogFragment
                .newInstance(data,
                        title, code, defaultValue);
        commonDialogFragment.setValueSetListener(new CommonDialogFragment.ValueSelectedListener() {
            @Override
            public void onValueSelected(String value, int requestCode) {
                textview.setText(value);
                int monthsId = new ArrayList<String>(
                        Arrays.asList(getActivity().getResources().getStringArray(R.array.month_names)))
                        .indexOf(value);
                reorderMonthsList.set(selectedPosition, String.valueOf(monthsId + ADD_ONE));

            }
        });
        commonDialogFragment.show(fragManager);
    }
}
