package com.petmeds1800.ui.checkout;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.CheckoutSteps;
import com.petmeds1800.model.entities.StepState;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.util.FontelloTextView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class CheckOutActivity extends AbstractActivity
        implements CheckoutActivityContract.View, CheckoutActivityContract.StepsFragmentInteractionListener {

    public static final String ITEMS_DETAIL = "itemsDetail";

    public final static int FIRST_SHIPMENT_CHECKOUT_CIRCLE = 1;

    public final static int SECOND_SHIPMENT_CHECKOUT_CIRCLE = 2;

    public final static int THIRD_SHIPMENT_CHECKOUT_CIRCLE = 3;

    public final static int FOURTH_SHIPMENT_CHECKOUT_CIRCLE = 4;

    public final static int FIVTH_SHIPMENT_CHECKOUT_CIRCLE = 5;

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

    private ArrayList<String> mApplicableSteps;

    private StepState mStepStates;

    private boolean mIsDestroyed = false;

    private CheckoutActivityPresenter mCheckoutPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle(getString(R.string.shipment_address));

        enableBackButton();

        mCheckoutPresenter = new CheckoutActivityPresenter(this);

        //check if we have the valid items in the shopping cart
        if (getIntent() != null) {
            HashMap<String, String> itemDetails = (HashMap<String, String>) getIntent().getSerializableExtra(
                    ITEMS_DETAIL);
            if (itemDetails != null && itemDetails.size() > 0) {
                //show the progress
                mCheckoutPresenter.initializeCheckout(itemDetails);
            }
        }

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

    public void setCheckOutCircleAsSelected(int code) {
        switch (code) {
            case FIRST_SHIPMENT_CHECKOUT_CIRCLE:
                mFirstShipmentAdressButton.setTextColor(Color.WHITE);
                mFirstShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_blue_circle_drawable));
                break;
            case SECOND_SHIPMENT_CHECKOUT_CIRCLE:
                mSecondShipmentAdressButton.setTextColor(Color.WHITE);
                mSecondShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_blue_circle_drawable));
                break;
            case THIRD_SHIPMENT_CHECKOUT_CIRCLE:
                mThirdShipmentAdressButton.setTextColor(Color.WHITE);
                mThirdShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_blue_circle_drawable));
                break;
            case FOURTH_SHIPMENT_CHECKOUT_CIRCLE:
                mFourthShipmentAdressButton.setTextColor(Color.WHITE);
                mFourthShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_blue_circle_drawable));
                break;
            case FIVTH_SHIPMENT_CHECKOUT_CIRCLE:
                mFifthShipmentAdressButton.setTextColor(Color.WHITE);
                mFifthShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_blue_circle_drawable));
                break;
        }
    }

    public void setCheckOutCircleAsDone(int code) {
        switch (code) {
            case FIRST_SHIPMENT_CHECKOUT_CIRCLE:
                mFirstShipmentAdressButton.setVisibility(View.GONE);
                mFirstShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case SECOND_SHIPMENT_CHECKOUT_CIRCLE:
                mSecondShipmentAdressButton.setVisibility(View.GONE);
                mSecondShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case THIRD_SHIPMENT_CHECKOUT_CIRCLE:
                mThirdShipmentAdressButton.setVisibility(View.GONE);
                mThirdShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case FOURTH_SHIPMENT_CHECKOUT_CIRCLE:
                mFourthShipmentAdressButton.setVisibility(View.GONE);
                mFourthShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case FIVTH_SHIPMENT_CHECKOUT_CIRCLE:
                mFifthShipmentAdressButton.setVisibility(View.GONE);
                mFifthShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void replaceCheckOutFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_one_root_fragment, fragment, tag);
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

    }

    @Override
    public void setCheckoutSteps(CheckoutSteps checkoutSteps) {
        mApplicableSteps = checkoutSteps.getApplicableSteps();
        mStepStates = checkoutSteps.getStepState();
    }

    @Override
    public void startNextStep(String nextStepCode) {

        switch (mApplicableSteps.indexOf(nextStepCode)) {

            case 0: //step 1 "Select Shipping Address"
                replaceCheckOutFragment(StepOneRootFragment.newInstance(), StepOneRootFragment.class.getName());

                break;

            case 1: //step 2 "Select Shipping method"
                replaceCheckOutFragment(StepTwoRootFragment.newInstance(), StepTwoRootFragment.class.getName());

                break;

            case 2: //step 3 "Select Payment method"
                //TODO start the Slect Payment method step. Following is just a temporary adjustment
                replaceCheckOutFragment(StepOneRootFragment.newInstance(), StepOneRootFragment.class.getName());

                break;

            case 3: //step 4 "Select Pet/Vet" or "Do order review" depending on the number of steps
                if (mApplicableSteps.size() == 4) {
                    //TODO start the Order review step
                } else if (mApplicableSteps.size() == 5) {
                    //TODO start the PETVet step
                }

                break;

            case 4: //step 5 "Do order review"
                //TODO start the Order review step

                break;
        }

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
    public void setLastCompletedSteps(String lastCompletedStep) {

    }

    @Override
    public void setActiveStep(String activeStep) {

    }

    @Override
    public void moveToNext(String currentStep) {

    }
}
