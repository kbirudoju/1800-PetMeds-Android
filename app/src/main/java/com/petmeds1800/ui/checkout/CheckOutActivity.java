package com.petmeds1800.ui.checkout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.CheckoutSteps;
import com.petmeds1800.model.entities.StepState;
import com.petmeds1800.model.shoppingcart.response.CommerceItems;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.checkout.stepfive.StepFiveRootFragment;
import com.petmeds1800.ui.checkout.stepfour.StepFourRootFragment;
import com.petmeds1800.ui.checkout.steponerootfragment.StepOneRootFragment;
import com.petmeds1800.ui.checkout.stepthreefragment.StepThreeRootFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.dialog.ProgressDialog;
import com.petmeds1800.util.FontelloTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;


public class CheckOutActivity extends AbstractActivity
        implements CheckoutActivityContract.View, CheckoutActivityContract.StepsFragmentInteractionListener {

    public static final String ITEMS_DETAIL = "itemsDetail";

    public final static int SHIPPMENT_ADDRESS = 0;

    public final static int SHIPPMENT_METHOD = 1;

    public final static int PAYMENT_METHOD = 2;

    public final static int PET_VET_INFORMATION = 3;

    public final static int SUBMIT_N_REVIEW = 4;

    public static final String STEP_NAME = "stepName";

    @BindView(R.id.firstShipmentAdressButton)
    Button mFirstShipmentAdressButton;

    @BindView(R.id.secondShipmentAdressButton)
    Button mSecondShipmentAdressButton;

    @BindView(R.id.thirdShipmentAdressButton)
    Button mThirdShipmentAdressButton;

    @BindView(R.id.fourthShipmentAdressButton)
    Button mFourthShipmentAdressButton;

    @BindView(R.id.fifthShipmentAdressButton)
    Button mFifthShipmentAdressButton;

    @BindView(R.id.firstShipmentAdressFontText)
    FontelloTextView mFirstShipmentAdressFontText;

    @BindView(R.id.secondShipmentAdressFontText)
    FontelloTextView mSecondShipmentAdressFontText;

    @BindView(R.id.thirdShipmentAdressFontText)
    FontelloTextView mThirdShipmentAdressFontText;

    @BindView(R.id.fourthShipmentAdressFontText)
    FontelloTextView mFourthShipmentAdressFontText;

    @BindView(R.id.fifthShipmentAdressFontText)
    FontelloTextView mFifthShipmentAdressFontText;

    @BindView(R.id.fourthContainer)
    LinearLayout mFourthContainer;

    @BindView(R.id.shipmentOrdersNumberLayout)
    RelativeLayout mCheckOutBoxContainer;

    private ArrayList<String> mApplicableSteps;

    private StepState mStepStates;

    private boolean mIsDestroyed = false;

    private CheckoutActivityPresenter mCheckoutPresenter;

    private ShoppingCartListResponse mShoppingCartListResponse;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle(getString(R.string.shipment_address));
        mProgressDialog = new ProgressDialog();
        mShoppingCartListResponse = (ShoppingCartListResponse) getIntent()
                .getSerializableExtra(CartFragment.SHOPPING_CART);
        enableBackButton();

        mCheckoutPresenter = new CheckoutActivityPresenter(this);

        //check if we have the valid items in the shopping cart
        if (getIntent() != null && mShoppingCartListResponse != null) {
            //TODO need to make this work on backgound thread.Consider moving it to the computational RX
            //generate a mapping of itemsID and item quantity. We need to do this becoz of backend tem reluctance of changing their code

            ArrayList<CommerceItems> commerceItems = (mShoppingCartListResponse.getShoppingCart() != null)
                    ? mShoppingCartListResponse.getShoppingCart().getCommerceItems() : null;
            HashMap<String, String> itemDetails = new HashMap<>();
            if (commerceItems != null) {
                for (CommerceItems commerceItem : commerceItems) {
                    itemDetails.put(commerceItem.getCommerceItemId(), commerceItem.getQuantity());
                }
            }
            if (itemDetails != null && itemDetails.size() > 0) {
                //show the progress
                mCheckoutPresenter.initializeCheckout(itemDetails);
            }
        }


    }


    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        mIsDestroyed = true;
        super.onDestroy();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_check_out;
    }


    public void setCheckOutCircleAsUnSelectedAndUnDone(int code) {
        switch (code) {
            case SHIPPMENT_ADDRESS:
                mFirstShipmentAdressButton.setVisibility(View.VISIBLE);
                mFirstShipmentAdressFontText.setVisibility(View.GONE);
                mFirstShipmentAdressButton.setTextColor(ContextCompat.getColor(this, R.color.petmeds_blue));
                mFirstShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_white_circle_drawable));
                break;
            case SHIPPMENT_METHOD:
                mSecondShipmentAdressButton.setVisibility(View.VISIBLE);
                mSecondShipmentAdressFontText.setVisibility(View.GONE);
                mSecondShipmentAdressButton.setTextColor(ContextCompat.getColor(this, R.color.petmeds_blue));
                mSecondShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_white_circle_drawable));
                break;
            case PAYMENT_METHOD:
                mThirdShipmentAdressButton.setVisibility(View.VISIBLE);
                mThirdShipmentAdressFontText.setVisibility(View.GONE);
                mThirdShipmentAdressButton.setTextColor(ContextCompat.getColor(this, R.color.petmeds_blue));
                mThirdShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_white_circle_drawable));
                break;
            case PET_VET_INFORMATION:
                mFourthShipmentAdressButton.setVisibility(View.VISIBLE);
                mFourthShipmentAdressFontText.setVisibility(View.GONE);
                mFourthShipmentAdressButton.setTextColor(ContextCompat.getColor(this, R.color.petmeds_blue));
                mFourthShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_white_circle_drawable));
                break;
            case SUBMIT_N_REVIEW:
                mFifthShipmentAdressButton.setVisibility(View.VISIBLE);
                mFifthShipmentAdressFontText.setVisibility(View.GONE);
                mFifthShipmentAdressButton.setTextColor(ContextCompat.getColor(this, R.color.petmeds_blue));
                mFifthShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_white_circle_drawable));
                break;
        }
    }

    private void setCheckOutCircleAsDone(int code) {
        switch (code) {
            case SHIPPMENT_ADDRESS:
                mFirstShipmentAdressButton.setVisibility(View.GONE);
                mFirstShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case SHIPPMENT_METHOD:
                mSecondShipmentAdressButton.setVisibility(View.GONE);
                mSecondShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case PAYMENT_METHOD:
                mThirdShipmentAdressButton.setVisibility(View.GONE);
                mThirdShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case PET_VET_INFORMATION:
                mFourthShipmentAdressButton.setVisibility(View.GONE);
                mFourthShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case SUBMIT_N_REVIEW:
                mFifthShipmentAdressButton.setVisibility(View.GONE);
                mFifthShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void replaceCheckOutFragment(Fragment fragment, String tag, boolean isBackStackEnable) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_one_root_fragment, fragment, tag);
        if (isBackStackEnable) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        mProgressDialog.setCancelable(false);
        if (!mProgressDialog.isAdded()) {
            mProgressDialog.show(getSupportFragmentManager(), "ProgressDialog");
        }
    }

    @Override
    public void setCheckoutSteps(CheckoutSteps checkoutSteps) {
        mApplicableSteps = checkoutSteps.getApplicableSteps();
        mCheckOutBoxContainer.setVisibility(View.VISIBLE);
        if (mApplicableSteps.size() == 4) {
            hideStepContainer();
        }
        mStepStates = checkoutSteps.getStepState();
    }

    private void hideStepContainer() {
        mFourthContainer.setVisibility(View.GONE);
        mFifthShipmentAdressButton.setText(getResources().getString(R.string.numeric_four));

    }

    @Override
    public void startNextStep(String stepName) {

        switch (mApplicableSteps.indexOf(stepName)) {

            case 0: //step 1 "Select Shipping Address"
                replaceCheckOutFragment(StepOneRootFragment.newInstance(mShoppingCartListResponse, stepName),
                        StepOneRootFragment.class.getName(), false);
                break;

            case 1: //step 2 "Select Shipping method"
                replaceCheckOutFragment(StepTwoRootFragment.newInstance(mShoppingCartListResponse, stepName),
                        StepTwoRootFragment.class.getName(), false);

                break;

            case 2: //step 3 "Select Payment method"
                //TODO start the Slect Payment method step. Following is just a temporary adjustment
                replaceCheckOutFragment(StepThreeRootFragment
                                .newInstance(mShoppingCartListResponse, stepName),
                        StepThreeRootFragment.class.getName(),
                        false);

                break;

            case 3: //step 4 "Select Pet/Vet" or "Do order review" depending on the number of steps

                if (mApplicableSteps.size() == 5) {
                    //TODO start the Order review step
                    replaceCheckOutFragment(
                            StepFourRootFragment.newInstance(mShoppingCartListResponse, stepName),
                            StepFourRootFragment.class.getName(), false);
                } else if (mApplicableSteps.size() == 4) {
                    //TODO start the PETVet step
                  replaceCheckOutFragment(
                            StepFiveRootFragment.newInstance(mShoppingCartListResponse, stepName),
                            StepFiveRootFragment.class.getName(), false);

                }

                break;

            case 4: //step 5 "Do order review"
                //TODO start the Order review step
                replaceCheckOutFragment(StepFiveRootFragment.newInstance(mShoppingCartListResponse, stepName),
                        StepFiveRootFragment.class.getName(), false);
                break;
        }
    }

    @Override
    public void startNextStep(String stepName, ShoppingCartListResponse shoppingCartListResponse) {
        mShoppingCartListResponse = shoppingCartListResponse;
        startNextStep(stepName);

    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {

    }

    @Override
    public void showErrorInMiddle(String errorMessage) {

    }

    @Override
    public boolean isActive() {
        return !mIsDestroyed;  //TODO Need to check if it works for an activity
    }

    @Override
    public void setPresenter(CheckoutActivityContract.Presenter presenter) {

    }

    @Override
    public void setLastCompletedSteps(String currentStep) {
        int currentStepIndex = mApplicableSteps.indexOf(currentStep);
        for (int index = 0; index < currentStepIndex; index++) {
            setCheckOutCircleAsDone(index);
        }
    }

    @Override
    public void setActiveStep(String activeStep) {

        switch (mApplicableSteps.indexOf(activeStep)) {

            case SHIPPMENT_ADDRESS:
                mFirstShipmentAdressButton.setTextColor(Color.WHITE);
                mFirstShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(),
                                        R.drawable.check_out_blue_circle_drawable));
                break;
            case SHIPPMENT_METHOD:
                mSecondShipmentAdressButton.setTextColor(Color.WHITE);
                mSecondShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(),
                                        R.drawable.check_out_blue_circle_drawable));
                break;
            case PAYMENT_METHOD:
                mThirdShipmentAdressButton.setTextColor(Color.WHITE);
                mThirdShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(),
                                        R.drawable.check_out_blue_circle_drawable));
                break;
            case PET_VET_INFORMATION:
                mFourthShipmentAdressButton.setTextColor(Color.WHITE);
                mFourthShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(),
                                        R.drawable.check_out_blue_circle_drawable));
                if (mApplicableSteps.size() == 5) {
                    break;
                }
            case SUBMIT_N_REVIEW:
                mFifthShipmentAdressButton.setTextColor(Color.WHITE);
                mFifthShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(),
                                        R.drawable.check_out_blue_circle_drawable));
                break;
        }
    }

    @Override
    public void moveToNext(String currentStep, ShoppingCartListResponse updatedShoppingCartListResponse) {
        mShoppingCartListResponse = updatedShoppingCartListResponse;
        int lastCompletedStep = mApplicableSteps.indexOf(currentStep);
        //check of current step is the last known step under applicable steps
        if (mApplicableSteps.indexOf(currentStep) == mApplicableSteps.size() - 1) {
            //TODO finish the checkout activity
        } else {
            startNextStep(mApplicableSteps.get(lastCompletedStep + 1));
        }

    }
}
