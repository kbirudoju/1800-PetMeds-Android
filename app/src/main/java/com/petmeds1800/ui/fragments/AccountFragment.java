package com.petmeds1800.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.account.AccountSettingsFragment;
import com.petmeds1800.ui.address.SavedAddressListFragment;
import com.petmeds1800.ui.orders.MyOrderFragment;
import com.petmeds1800.ui.payment.SavedCardsListFragment;
import com.petmeds1800.ui.pets.PetListFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/2/2016.
 */
public class AccountFragment extends AbstractFragment implements View.OnClickListener, Switch.OnCheckedChangeListener, DialogInterface.OnClickListener {

    @BindView(R.id.myOrder)
    TextView myOrderView;

    @BindView(R.id.accountSettings_label)
    TextView mAccountSettings;

    @BindView(R.id.managePayment_label)
    TextView mManagePaymentLabel;

    @BindView(R.id.manageAddress_label)
    TextView mManageAddressLabel;

    @BindView(R.id.my_pets_label)
    TextView mPetsLabel;
    @BindView(R.id.fingerPrintStatus)
    Switch fingerPrintStatus;
    @BindView(R.id.notificationStatus)
    Switch notificationStatus;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    private boolean fromFillWindow;
    private final static int FROM_NOTIFICATION = 1;
    private final static int FROM_SIGNOUT_OPTION = 2;
    private final static int FROM_FINGERPRINT_OPTION = 3;
    private int fromWhichAlert = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myOrderView.setOnClickListener(this);
        mAccountSettings.setOnClickListener(this);
        mManagePaymentLabel.setOnClickListener(this);
        mManageAddressLabel.setOnClickListener(this);
        mPetsLabel.setOnClickListener(this);
        notificationStatus.setOnCheckedChangeListener(this);
        fillWindow();
    }

    public void fillWindow() {
        if (mPreferencesHelper.getIsPushNotificationEnableFlag()) {
            fromFillWindow = true;
            notificationStatus.setChecked(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        PetMedsApplication.getAppComponent().inject(this);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_account));
        ((AbstractActivity) getActivity()).disableBackButton();
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myOrder:
                replaceAndAddToBackStack(new MyOrderFragment(), MyOrderFragment.class.getName());
                break;

            case R.id.accountSettings_label:
                replaceAndAddToBackStack(new AccountSettingsFragment(), AccountSettingsFragment.class.getName());
                break;

            case R.id.managePayment_label:
                replaceAndAddToBackStack(new SavedCardsListFragment(), SavedCardsListFragment.class.getName());
                break;

            case R.id.manageAddress_label:
                replaceAndAddToBackStack(new SavedAddressListFragment(), SavedAddressListFragment.class.getName());
                break;
            case R.id.my_pets_label:
                replaceAndAddToBackStack(new PetListFragment(), PetListFragment.class.getName());
                break;
            default:
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (fromFillWindow) {
            fromFillWindow = false;
            return;
        }
        switch (buttonView.getId()) {
            case R.id.notificationStatus:
                fromWhichAlert = FROM_NOTIFICATION;
                mPreferencesHelper.setIsPushNotificationEnableFlag(isChecked);
                break;
            case R.id.fingerPrintStatus:
                fromWhichAlert = FROM_SIGNOUT_OPTION;
                break;

        }


    }

    public void performOperation(int fromWhichAlert, boolean isEnable) {
        switch (fromWhichAlert) {
            case FROM_FINGERPRINT_OPTION:
                break;
        }
        fromWhichAlert = 0;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                performOperation(fromWhichAlert, true);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                performOperation(fromWhichAlert, false);
                break;

        }
    }
}
