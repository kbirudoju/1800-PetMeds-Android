package com.petmeds1800.ui;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.ui.fragments.AccountRootFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.HomeFragment;
import com.petmeds1800.ui.fragments.LearnFragment;
import com.petmeds1800.ui.fragments.dialog.FingerprintAuthenticationDialog;
import com.petmeds1800.ui.payment.AddACardContract;
import com.petmeds1800.ui.payment.AddACardFragment;
import com.petmeds1800.ui.support.TabPagerAdapter;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;
import com.petmeds1800.util.Utils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AbstractActivity
        implements AddACardContract.AddressSelectionListener, DialogInterface.OnClickListener {

    @BindView(R.id.tablayout)
    TabLayout mHomeTab;

    @BindView(R.id.viewpager_fragments)
    ViewPager mViewPager;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.container_home)
    RelativeLayout mContainerLayout;

    List<Fragment> fragmentList;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private boolean mIsAuthDialogShown;

    private int mTabIndex;

    private FingerprintAuthenticationDialog mAuthDialog;

    private static final int[] TAB_ICON_UNSELECTED = {R.drawable.ic_menu_home, R.drawable.ic_menu_cart,
            R.drawable.ic_menu_learn, R.drawable.ic_menu_account};

    private static final int[] TAB_ICON_SELECTED = {R.drawable.ic_menu_home_pressed, R.drawable.ic_menu_cart_pressed,
            R.drawable.ic_menu_learn_pressed, R.drawable.ic_menu_account_pressed};

    public void showPushPermissionDailog() {
        AlertDialog alertDialog = Utils.showAlertDailog(this,
                String.format(getString(R.string.notification_title), getString(R.string.application_name)),
                getString(R.string.notification_message), R.style.StyleForNotification)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_allow_button).toUpperCase(), this)
                .setNegativeButton(getString(R.string.dialog_deny_button).toUpperCase(), this)
                .create();
        alertDialog.show();
    }

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        PetMedsApplication.getAppComponent().inject(this);
        if (getIntent().getBooleanExtra("isFromHomeActivity", false)) {
            showPushPermissionDailog();
            if (!RxFingerprint.isHardwareDetected(this)) {
                mPreferencesHelper.setIsFingerPrintEnabled(false);
            } else if (!RxFingerprint.hasEnrolledFingerprints(this)) {
                mPreferencesHelper.setIsFingerPrintEnabled(false);
            } else {
                mPreferencesHelper.setIsFingerPrintEnabled(true);
            }

        }

        Log.d("HomeActivity", ">>>>>>>>>>>");
        //initialize fragment list
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new CartFragment());
        fragmentList.add(new LearnFragment());
        fragmentList.add(new AccountRootFragment());

        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), fragmentList));
        mHomeTab.setupWithViewPager(mViewPager);

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabIndex = position;
                for (int i = 0; i < mHomeTab.getTabCount(); ++i) {
                    mHomeTab.getTabAt(i).setIcon(i != position ? TAB_ICON_UNSELECTED[i] : TAB_ICON_SELECTED[i]);
                }
                if (position == 0 || position == 1 || position == 2) {
                    removeAllFragment();
                }

                if (position == 3) {
                    //TODO: code improvement, We can create constants for the pages
                    showAuthDialog();
                }
                setToolBarTitle((getResources().getStringArray(R.array.tab_title)[position]));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        };

        //code to set default first tab selected
        mViewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsAuthDialogShown && mTabIndex == 3) {
            mAuthDialog.dismiss();
            showAuthDialog();
        }
    }

    @Override
    public void setAddress(Address address) {
        AddACardFragment addCardFragment = (AddACardFragment) getSupportFragmentManager()
                .findFragmentByTag(AddACardFragment.class.getName());
        if (addCardFragment != null) {
            addCardFragment.displayAddress(address);
        } else {
            AddACardFragment newFragment = new AddACardFragment();
            Bundle args = new Bundle();
            args.putSerializable(AddACardFragment.FIRST_ARG, address);
            newFragment.setArguments(args);
            replaceAndAddToBackStack(newFragment, AddACardFragment.class.getName());
        }
    }

    private void showFingerprintDialog() {
        mIsAuthDialogShown = true;
        mAuthDialog = new FingerprintAuthenticationDialog();
        mAuthDialog.setCancelable(false);
        mAuthDialog.show(getSupportFragmentManager(), "FingerprintAuthenticationDialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void removeAllFragment() {
        FragmentManager fm = getSupportFragmentManager();
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    private void showAuthDialog() {
        showProgress();
        mApiService.getSecurityStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecurityStatusResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        int errorId = RetrofitErrorHandler.getErrorMessage(e);
                        if (errorId == R.string.noInternetConnection) {
                            Toast.makeText(HomeActivity.this, getString(R.string.noInternetConnection),
                                    Toast.LENGTH_SHORT).show();
                        }
                        getViewPager().setCurrentItem(0);
                        Log.v("onError", e.getMessage());
                    }

                    @Override
                    public void onNext(SecurityStatusResponse securityStatusResponse) {
                        hideProgress();
                        int securityStatus = securityStatusResponse.getSecurityStatus();
                        //TODO: improvement
                        if (securityStatus == 0 || securityStatus == 2) {
                            showFingerprintDialog();
                        } else if (securityStatus == 4) {
                            //TODO: research more into silent sign in logic
                        }
                    }
                });
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public ViewGroup getContainerView() {
        return mContainerLayout;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mPreferencesHelper.setIsPushNotificationEnableFlag(true);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mPreferencesHelper.setIsPushNotificationEnableFlag(false);
                break;

        }
    }
}
