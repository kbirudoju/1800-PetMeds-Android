package com.petmeds1800.ui.checkout;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.CheckoutSteps;
import com.petmeds1800.model.entities.StepState;
import com.petmeds1800.model.shoppingcart.response.ShoppingCart;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.checkout.stepfive.StepFiveRootFragment;
import com.petmeds1800.ui.checkout.stepfour.StepFourRootFragment;
import com.petmeds1800.ui.checkout.steponerootfragment.GuestStepOneRootFragment;
import com.petmeds1800.ui.checkout.steponerootfragment.StepOneRootFragment;
import com.petmeds1800.ui.checkout.stepthreefragment.GuestStepThreeRootFragment;
import com.petmeds1800.ui.checkout.stepthreefragment.StepThreeRootFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.util.FontelloTextView;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import com.petmeds1800.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;


public class CheckOutActivity extends AbstractActivity

        implements CheckoutActivityContract.View, CheckoutActivityContract.StepsFragmentInteractionListener,
        CommonWebviewFragment.OnPaymentCompletedListener {

    public static final String ITEMS_DETAIL = "itemsDetail";

    public final static int STEP_ONE = 0;

    public final static int STEP_TWO = 1;

    public final static int STEP_THREE = 2;

    public final static int STEP_FOUR = 3;

    public final static int SUBMIT_N_REVIEW = 4;

    public static final String STEP_NAME = "stepName";

    private static final String NAVIGATE_TO_CART = "navigateToCart";

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

    @BindView(R.id.thirdContainer)
    LinearLayout mThirdContainer;

    @BindView(R.id.activity_check_out)
    RelativeLayout mRootContainer;

    private ArrayList<String> mApplicableSteps;

    private StepState mStepStates;

    private boolean mIsDestroyed = false;

    private CheckoutActivityPresenter mCheckoutPresenter;

    private ShoppingCartListResponse mShoppingCartListResponse;

    boolean mIsReviewOn = false;

    @Inject
    PetMedsApiService mPetMedsApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private CheckoutSteps mCheckoutSteps;

    private int mSecurityStatus;

    PaypalErrorListener paypalErrorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShoppingCartListResponse = (ShoppingCartListResponse) getIntent()
                .getSerializableExtra(CartFragment.SHOPPING_CART);
        mCheckoutSteps = (CheckoutSteps) getIntent().getSerializableExtra(CartFragment.CHECKOUT_STEPS);
        enableBackButton();

        PetMedsApplication.getAppComponent().inject(this);

        mCheckoutPresenter = new CheckoutActivityPresenter(this);

        //first set the mSecurityStatus to know if a user is logged-in or not
        mCheckoutPresenter.checkSecurityStatus();

    }

    @Override
    public void initializeSteps(HashMap<String, String> itemDetails) {
        if (mCheckoutSteps != null) {
            setCheckoutSteps(mCheckoutSteps);
            startNextStep(mCheckoutSteps.getStepState().getNextCheckoutStep());
        } else {
            if (itemDetails != null && itemDetails.size() > 0) {
                mCheckoutPresenter.initializeCheckout(itemDetails);
            }
        }
    }


    @Override
    public void hideProgress() {
        try {
            stopLoadingGif(this);
        } catch (Exception e) {
            e.printStackTrace();
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


    public void setCheckOutCircleAsUnSelectedAndUnDone(int code) {
        switch (code) {
            case STEP_ONE:
                mFirstShipmentAdressButton.setVisibility(View.VISIBLE);
                mFirstShipmentAdressFontText.setVisibility(View.GONE);
                mFirstShipmentAdressButton.setTextColor(ContextCompat.getColor(this, R.color.petmeds_blue));
                mFirstShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_white_circle_drawable));
                break;
            case STEP_TWO:
                mSecondShipmentAdressButton.setVisibility(View.VISIBLE);
                mSecondShipmentAdressFontText.setVisibility(View.GONE);
                mSecondShipmentAdressButton.setTextColor(ContextCompat.getColor(this, R.color.petmeds_blue));
                mSecondShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_white_circle_drawable));
                break;
            case STEP_THREE:
                mThirdShipmentAdressButton.setVisibility(View.VISIBLE);
                mThirdShipmentAdressFontText.setVisibility(View.GONE);
                mThirdShipmentAdressButton.setTextColor(ContextCompat.getColor(this, R.color.petmeds_blue));
                mThirdShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.check_out_white_circle_drawable));
                break;
            case STEP_FOUR:
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
            case STEP_ONE:
                mFirstShipmentAdressButton.setVisibility(View.GONE);
                mFirstShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case STEP_TWO:
                mSecondShipmentAdressButton.setVisibility(View.GONE);
                mSecondShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case STEP_THREE:
                mThirdShipmentAdressButton.setVisibility(View.GONE);
                mThirdShipmentAdressFontText.setVisibility(View.VISIBLE);
                break;
            case STEP_FOUR:
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


    public void replaceCheckOutFragmentWithBundle(Fragment fragment, String tag, boolean isBackStackEnable,
            Bundle bundle) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_one_root_fragment, fragment, tag);
        if (isBackStackEnable) {
            fragmentTransaction.addToBackStack(null);
        }
        fragment.setArguments(bundle);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            upIntent.putExtra(NAVIGATE_TO_CART, true);
            //create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        try {
            startLoadingGif(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCheckoutSteps(CheckoutSteps checkoutSteps) {
        mApplicableSteps = checkoutSteps.getApplicableSteps();
        mCheckOutBoxContainer.setVisibility(View.VISIBLE);
        if (mApplicableSteps != null && mApplicableSteps.size() == 4) {
            hideStepFiveContainer(getResources().getString(R.string.numeric_four));
        } else if (mApplicableSteps != null && mApplicableSteps.size() == 3) {
            hideStepContainers();
        }
        mStepStates = checkoutSteps.getStepState();
    }

    @Override
    public void updateShoppingCartInShoppingCartListResponse(ShoppingCart shoppingCart) {
        mShoppingCartListResponse.setShoppingCart(shoppingCart);
        //update the item count as well since we dont have the itemCount field in the initCheckoutAPI response but number of items could change
        mShoppingCartListResponse.setItemCount(shoppingCart.getCommerceItems().size());
    }

    //Hide in case of four steps
    private void hideStepFiveContainer(String textString) {
        mFourthContainer.setVisibility(View.GONE);
        mFifthShipmentAdressButton.setText(textString);

    }

    //Hide in case of five steps

    private void hideStepContainers() {
        hideStepFiveContainer(getResources().getString(R.string.numeric_3));
        mThirdContainer.setVisibility(View.GONE);
    }

    @Override
    public void startNextStep(String stepName) {

        switch (mApplicableSteps.indexOf(stepName)) {

            case 0: //step 1 "Select Shipping Address"
                //check if the user is logged-in
                if (mSecurityStatus == 4 || mSecurityStatus == 5 || mSecurityStatus == 2) {
                    replaceCheckOutFragment(StepOneRootFragment.newInstance(mShoppingCartListResponse, stepName),
                            StepOneRootFragment.class.getName(), false);
                } else {
                    replaceCheckOutFragment(GuestStepOneRootFragment.newInstance(mShoppingCartListResponse, stepName),
                            GuestStepOneRootFragment.class.getName(), false);
                }

                break;

            case 1: //step 2 "Select Shipping method"
                replaceCheckOutFragment(StepTwoRootFragment.newInstance(mShoppingCartListResponse, stepName),
                        StepTwoRootFragment.class.getName(), false);

                break;

            case 2: //step 3 "Select Payment method"
                if (mApplicableSteps.size() == 3) {
                    replaceCheckOutFragment(StepFiveRootFragment.newInstance(mShoppingCartListResponse, stepName),
                            StepFiveRootFragment.class.getName(), false);
                }else if(mApplicableSteps.size()==4 && stepName.equals("PETVET")){
                    replaceCheckOutFragment(
                            StepFourRootFragment.newInstance(mShoppingCartListResponse, stepName),
                            StepFourRootFragment.class.getName(), false);
                }
                else if (mSecurityStatus == 4 || mSecurityStatus == 5) {
                    replaceCheckOutFragment(StepThreeRootFragment
                                    .newInstance(mShoppingCartListResponse, stepName,
                                            StepThreeRootFragment.LOGGED_IN_REQUEST_CODE),
                            StepThreeRootFragment.class.getName(),
                            false);
                }
                else { //guest user adding payment for first time
                    replaceCheckOutFragment(GuestStepThreeRootFragment
                                    .newInstance(mShoppingCartListResponse, stepName,mSecurityStatus),
                            GuestStepThreeRootFragment.class.getName(),
                            false);
                }

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
    public RelativeLayout getContainerView(){

        return mRootContainer;
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

            case STEP_ONE:
                mFirstShipmentAdressButton.setTextColor(Color.WHITE);
                mFirstShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(),
                                        R.drawable.check_out_blue_circle_drawable));
                break;
            case STEP_TWO:
                mSecondShipmentAdressButton.setTextColor(Color.WHITE);
                mSecondShipmentAdressButton
                        .setBackground(ContextCompat
                                .getDrawable(getApplicationContext(),
                                        R.drawable.check_out_blue_circle_drawable));
                break;
            case STEP_THREE:
                if (mApplicableSteps.size() == 4 || mApplicableSteps.size() == 5) {
                    mThirdShipmentAdressButton.setTextColor(Color.WHITE);
                    mThirdShipmentAdressButton
                            .setBackground(ContextCompat
                                    .getDrawable(getApplicationContext(),
                                            R.drawable.check_out_blue_circle_drawable));
                    break;
                }
            case STEP_FOUR:
                if (mApplicableSteps.size() == 5) {
                    mFourthShipmentAdressButton.setTextColor(Color.WHITE);
                    mFourthShipmentAdressButton
                            .setBackground(ContextCompat
                                    .getDrawable(getApplicationContext(),
                                            R.drawable.check_out_blue_circle_drawable));

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
            //need to finish the activity
        } else {
            startNextStep(mApplicableSteps.get(lastCompletedStep + 1));
        }
    }

    @Override
    public void startNextStep(String stepName, ShoppingCartListResponse shoppingCartListResponse, boolean isReviewOn) {
        mIsReviewOn = isReviewOn;
        startNextStep(stepName, shoppingCartListResponse);
    }

    @Override
    public void setSecurityStatus(int securityStatus) {
        mSecurityStatus = securityStatus;

        //check if we have the valid items in the shopping cart
        if (getIntent() != null && mShoppingCartListResponse != null) {
            //generate a mapping of itemsID and item quantity. We need to do this becoz of backend tem reluctance of changing their code
            mCheckoutPresenter.mapItemIdWithQuantity(mShoppingCartListResponse);
        }

    }

    @Override
    public void onPaymentCompleted(ShoppingCartListResponse paypalResponse) {

    }

    public int getApplicableSteps() {
        return mApplicableSteps.size();
    }

    public String getNextApplicableSteps() {
        if (mShoppingCartListResponse.getCheckoutSteps() != null) {
            return mShoppingCartListResponse.getCheckoutSteps().getStepState().getNextCheckoutStep();
        }
        return null;
    }

    @Override
    public void onCheckoutPaymentCompleted(ShoppingCartListResponse paypalResponse, String stepName) {
   /*  CommonWebviewFragment fragment= (CommonWebviewFragment)getSupportFragmentManager().findFragmentByTag(CommonWebviewFragment.class.getName());
     fragment.removeFragment();
*/        if (paypalResponse.getShoppingCart() != null) {

         moveToNext(stepName, paypalResponse);
            Log.d("response in cart",
                    paypalResponse.getCheckoutSteps().getApplicableSteps() + ">>>" + paypalResponse.getCheckoutSteps()
                            .getStepState().getNextCheckoutStep() + "stepname" + stepName);

        } else {
            Log.d("response in cart", paypalResponse.getStatus().getErrorMessages().get(0));
            if (paypalErrorListener != null) {
                paypalErrorListener.onPayPal(paypalResponse.getStatus().getErrorMessages().get(0));

            }
        }
    }

    public void addListener(StepThreeRootFragment stepThreeRootFragment) {
        paypalErrorListener = stepThreeRootFragment;
    }

    public interface PaypalErrorListener {
             void onPayPal(String errorMsg);
        }

    }


