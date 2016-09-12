package com.petmeds1800.ui.fragments;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.account.AccountSettingsFragment;
import com.petmeds1800.ui.account.SignOutContract;
import com.petmeds1800.ui.account.SignOutPresenter;
import com.petmeds1800.ui.address.SavedAddressListFragment;
import com.petmeds1800.ui.orders.MyOrderFragment;
import com.petmeds1800.ui.payment.SavedCardsListFragment;
import com.petmeds1800.ui.pets.PetListFragment;
import com.petmeds1800.ui.refillreminder.ReminderListFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import android.content.DialogInterface;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/2/2016.
 */
public class AccountFragment extends AbstractFragment
        implements View.OnClickListener, Switch.OnCheckedChangeListener, DialogInterface.OnClickListener,
        SignOutContract.View {

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

    private final static int FROM_NOTIFICATION = 1;

    private final static int FROM_SIGNOUT_OPTION = 2;

    private final static int FROM_FINGERPRINT_OPTION = 3;

    private final static int IS_HARDWARE_DETECTED = 4;

    private final static int HAS_ENROLLED_FINGERPRINTS = 5;

    private final static int HAS_ENABILITY = 6;

    @BindView(R.id.signOut)
    TextView signOut;

    private int fromWhichAlert = 0;


    @BindView(R.id.refill_reminder_label)
    TextView mRefillReminderLabel;
    private SignOutContract.Presenter mPresenter;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_account));
        ((AbstractActivity) getActivity()).disableBackButton();
        myOrderView.setOnClickListener(this);
        mAccountSettings.setOnClickListener(this);
        mManagePaymentLabel.setOnClickListener(this);
        mManageAddressLabel.setOnClickListener(this);
        mPetsLabel.setOnClickListener(this);
        signOut.setOnClickListener(this);
        fillWindow();
    }

    private void fillWindow() {
        if (mPreferencesHelper.getIsPushNotificationEnableFlag()) {
            notificationStatus.setChecked(true);
        }
        if (mPreferencesHelper.getIsFingerPrintEnabled()) {
            fingerPrintStatus.setChecked(true);
        } else {
            fingerPrintStatus.setChecked(false);
        }
        notificationStatus.setOnCheckedChangeListener(this);
        fingerPrintStatus.setOnCheckedChangeListener(this);

    }

    private void showDailogForFingerprintAvailability(String title, String msg) {
        AlertDialog alertDialog = Utils.showAlertDailog(getActivity(), title, msg, R.style.StyleForNotification)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_ok_button).toUpperCase(), null)
                .create();
        alertDialog.show();
    }

    private void showDailogForSignOut(String title, String msg) {
        fromWhichAlert = FROM_SIGNOUT_OPTION;
        AlertDialog alertDialog = Utils.showAlertDailog(getActivity(), title, msg, R.style.StyleForNotification)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_logout_button).toUpperCase(), this)
                .setNegativeButton(getString(R.string.dialog_cancel_button).toUpperCase(), this)
                .create();
        alertDialog.show();
    }

    private int checkStatus() {
        if (!RxFingerprint.isHardwareDetected(getActivity())) {
            return IS_HARDWARE_DETECTED;
        }
        if (!RxFingerprint.hasEnrolledFingerprints(getActivity())) {
            return HAS_ENROLLED_FINGERPRINTS;
        }
        return HAS_ENABILITY;
       // mRefillReminderLabel.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        PetMedsApplication.getAppComponent().inject(this);

        mPresenter = new SignOutPresenter(this);
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
            case R.id.signOut:
                showDailogForSignOut(getString(R.string.logout_alert_title), null);
                break;
            case R.id.refill_reminder_label:
                replaceAndAddToBackStack(new ReminderListFragment(), ReminderListFragment.class.getName());
                break;
            default:
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.notificationStatus:
                fromWhichAlert = FROM_NOTIFICATION;
                mPreferencesHelper.setIsPushNotificationEnableFlag(isChecked);
                break;
            case R.id.fingerPrintStatus:
                if (buttonView.isPressed()) {
                    int status = checkStatus();
                    if (status == IS_HARDWARE_DETECTED) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDailogForFingerprintAvailability(
                                        getString(R.string.fingerprint_hardware_support_title),
                                        getString(R.string.fingerprint_hardware_support_message));
                                fingerPrintStatus.setChecked(false);
                            }
                        }, 300);
                        mPreferencesHelper.setIsFingerPrintEnabled(false);
                        return;
                    }
                    if (status == HAS_ENROLLED_FINGERPRINTS) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDailogForFingerprintAvailability(getString(R.string.fingerprint_enrollment_title),
                                        getString(R.string.fingerprint_enrollment_message));
                                fingerPrintStatus.setChecked(false);
                            }
                        }, 300);
                        mPreferencesHelper.setIsFingerPrintEnabled(false);
                        return;
                    }
                }
                mPreferencesHelper.setIsFingerPrintEnabled(isChecked);
                break;

        }


    }

    public void performOperation(int fromWhichAlert, boolean isPositive) {
        switch (fromWhichAlert) {
            case FROM_SIGNOUT_OPTION:
                if (isPositive) {
                    ((HomeActivity)getActivity()).showProgress();
                    mPresenter.sendDataToServer(
                            mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                }
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

    @Override
    public void onSuccess() {
        ((HomeActivity)getActivity()).hideProgress();
        replaceFragment(new SignOutFragment());
    }

    @Override
    public void onError(String errorMessage) {
        ((HomeActivity)getActivity()).hideProgress();
        Snackbar.make(getView().findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(SignOutContract.Presenter presenter) {

    }
}
