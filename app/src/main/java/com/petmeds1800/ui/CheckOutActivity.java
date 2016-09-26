package com.petmeds1800.ui;

import com.petmeds1800.R;
import com.petmeds1800.ui.checkout.StepOneRootFragment;
import com.petmeds1800.util.FontelloTextView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;

public class CheckOutActivity extends AbstractActivity {

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

    public final static int FIRST_SHIPMENT_CHECKOUT_CIRCLE = 1;

    public final static int SECOND_SHIPMENT_CHECKOUT_CIRCLE = 2;

    public final static int THIRD_SHIPMENT_CHECKOUT_CIRCLE = 3;

    public final static int FOURTH_SHIPMENT_CHECKOUT_CIRCLE = 4;

    public final static int FIVTH_SHIPMENT_CHECKOUT_CIRCLE = 5;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle(getString(R.string.shipment_address));
        enableBackButton();
        replaceCheckOutFragment(StepOneRootFragment.newInstance(), StepOneRootFragment.class.getName());
//        replaceCheckOutFragment(StepTwoRootFragment.newInstance(), StepTwoRootFragment.class.getName());
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
}
