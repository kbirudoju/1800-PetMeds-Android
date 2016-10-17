package com.petmeds1800.ui.checkout.stepthreefragment;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.PaymentGroup;
import com.petmeds1800.model.shoppingcart.response.PaymentGroups;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.MonthYearPicker;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhinav on 4/10/16.
 */
public class AddGuestCardFragment extends AbstractFragment implements
        GuestStepThreeRootContract.PaymentMethodInteractionListener {

    private static final String PAYMENT_GROUP = "paymentGroup";

    @BindView(R.id.cardNumberLayout)
    TextInputLayout mCardNumberLayout;

    @BindView(R.id.expirationDateInputLayout)
    TextInputLayout mExpirationDateInputLayout;

    @BindView(R.id.cvvInputLayout)
    TextInputLayout mCvvInputLayout;

    @BindView(R.id.cardNumber_edit)
    EditText mCardNumberEdit;

    @BindView(R.id.expirationDate_edit)
    EditText mExpirationDateEdit;

    @BindView(R.id.cvv_edit)
    EditText mCvvEdit;

    @BindView(R.id.addressContainer_layout)
    RelativeLayout mAddressContainerLayout;

    @BindView(R.id.defaultPayment_switch)
    Switch mDefaultPaymentSwitch;

    @BindView(R.id.addressSelection_label)
    TextView mAddressSelectionLabel;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private int mSelectedExpirationMonth;

    private int mSelectedExpirationYear;

    private CardRequest mCardRequest;

    public static AddGuestCardFragment newInstance() {

        Bundle args = new Bundle();

        AddGuestCardFragment fragment = new AddGuestCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddGuestCardFragment newInstance(PaymentGroups paymentGroup) {

        Bundle args = new Bundle();
        args.putSerializable(PAYMENT_GROUP, paymentGroup);
        AddGuestCardFragment fragment = new AddGuestCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        PetMedsApplication.getAppComponent().inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_a_card, container, false);
        ButterKnife.bind(this, view);
        mDefaultPaymentSwitch.setVisibility(View.GONE);
        mAddressContainerLayout.setVisibility(View.GONE);
        mAddressSelectionLabel.setVisibility(View.GONE);
        return view;
    }

    private void populateData() {
        //TODO need to implement onec API flow is finalized for the anonymous user
    }



    @OnClick(R.id.expirationDate_edit)
    void showExpirationDate() {

        final MonthYearPicker myp = new MonthYearPicker(getActivity());
        myp.build(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mExpirationDateEdit.setText(myp.getSelectedMonthShortName() + " " + myp.getSelectedYear());
                //putting the values in the fields so that we can use them directly avoiding the need to calculate it further
                mSelectedExpirationMonth = myp.getSelectedMonth();
                mSelectedExpirationYear = myp.getSelectedYear();
            }
        }, null);

        myp.show();
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public boolean checkAndShowError() {

        boolean invalidCreditCardNumber;
        boolean invalidExpirationDate;
        boolean invalidCvv;

        ///// Negative flow /////

            //card number validation
            if (!Utils.isCreditCardNumberValid(mCardNumberEdit.getText().toString())) {
                mCardNumberLayout.setError(getContext().getString(R.string.invalidCreditCardError));
                invalidCreditCardNumber = true;
            } else {
                mCardNumberLayout.setError(null);
                //following line would help to keep the view size intact
                mCardNumberLayout.setErrorEnabled(false);
                invalidCreditCardNumber = false;
            }

            //cvv validation
            if (!Utils.isCvvValid(mCvvEdit.getText().toString())) {
                mCvvInputLayout.setError(getContext().getString(R.string.invalidCvvNumberError));
                invalidCvv = true;
            } else {
                mCvvInputLayout.setError(null);
                mCvvInputLayout.setErrorEnabled(false);
                invalidCvv = false;
            }

        //expiration date validation is valid for both cases - AddACard and EditACard request
        if (!Utils.isExpirationDateValid(mSelectedExpirationMonth, mSelectedExpirationYear)) {
            mExpirationDateInputLayout.setError(getContext().getString(R.string.invalidExpirationDateError));
            invalidExpirationDate = true;
        } else {
            mExpirationDateInputLayout.setError(null);
            mExpirationDateInputLayout.setErrorEnabled(false);
            invalidExpirationDate = false;
        }

        if (invalidCreditCardNumber || invalidExpirationDate || invalidCvv) {
            return false;
        }
        else {
            //initialize the card object
            mCardRequest = new CardRequest(
                    mCardNumberEdit.getText().toString()
                    , String.valueOf(mSelectedExpirationMonth)
                    , String.valueOf(mSelectedExpirationYear)
                    , "false"
                    , mCvvEdit.getText().toString()
                    , "" //TODO we should attach the addressID once a billing address has been added

                    , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber()); //TODO check if we need to initialize the session confirmation as well

            return true;
        }

    }

    @Override
    public CardRequest getCard() {
        return mCardRequest;
    }
}
