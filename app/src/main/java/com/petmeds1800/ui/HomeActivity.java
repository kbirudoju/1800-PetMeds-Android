package com.petmeds1800.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.ui.fragments.AccountRootFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.HomeRootFragment;
import com.petmeds1800.ui.fragments.LearnFragment;
import com.petmeds1800.ui.fragments.dialog.FingerprintAuthenticationDialog;
import com.petmeds1800.ui.fragments.dialog.ProgressDialog;
import com.petmeds1800.ui.payment.AddACardContract;
import com.petmeds1800.ui.payment.AddEditCardFragment;
import com.petmeds1800.ui.support.TabPagerAdapter;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;
import com.petmeds1800.util.Utils;

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

    @BindView(R.id.container_home)
    RelativeLayout mContainerLayout;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private ProgressDialog mProgressDialog;

    private FingerprintAuthenticationDialog mAuthenticationDialog;

    private static final String IS_FROM_HOME_ACTIVITY = "isFromHomeActivity";

    private int mTabIndex;

    private static final int[] TAB_ICON_UNSELECTED = {R.drawable.ic_menu_home, R.drawable.ic_menu_cart,
            R.drawable.ic_menu_learn, R.drawable.ic_menu_account};

    private static final int[] TAB_ICON_SELECTED = {R.drawable.ic_menu_home_pressed, R.drawable.ic_menu_cart_pressed,
            R.drawable.ic_menu_learn_pressed, R.drawable.ic_menu_account_pressed};

    private String mSearchString;

    private boolean mShowOptionsMenu;

    private HomeRootFragment mHomeRootFragment;
    TabPagerAdapter mAdapter;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mAnalyticsUtil = new AnalyticsUtil();
        PetMedsApplication.getAppComponent().inject(this);

        if (getIntent().getBooleanExtra(IS_FROM_HOME_ACTIVITY, false) && mPreferencesHelper.getIsUserLoggedIn()) {
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
        List<Fragment> fragmentList = new ArrayList<>();
        mHomeRootFragment = new HomeRootFragment();
        fragmentList.add(mHomeRootFragment);
        fragmentList.add(new CartFragment());
        fragmentList.add(new LearnFragment());
        fragmentList.add(new AccountRootFragment());
        mAdapter=new TabPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mAdapter);
        mHomeTab.setupWithViewPager(mViewPager);
        mHomeTab.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int numTab = tab.getPosition();
                       /* if(numTab==0){
                          *//*  HomeRootFragment fragment = (HomeRootFragment) getSupportFragmentManager().findFragmentByTag(HomeRootFragment.class.getSimpleName());
                            fragment.addHomeFragment();*//*
                            FragmentManager fm = getSupportFragmentManager();
                            Fragment fr = mAdapter.getItem(numTab);
                            if(fr instanceof HomeRootFragment){
                                ((HomeRootFragment)fr).addHomeFragment();

                            }
                        }*/
                        Log.d("ontabselected",numTab+">>>>");

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        //TODO: could not understand the purpose of this code so removed. If still needed please justify
                        removeAllFragment();

                    }
                });


        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected",">>>>>>");
                mTabIndex = position;
                for (int i = 0; i < mHomeTab.getTabCount(); ++i) {
                    mHomeTab.getTabAt(i).setIcon(i != position ? TAB_ICON_UNSELECTED[i] : TAB_ICON_SELECTED[i]);
                }


                if (position == 3 && mPreferencesHelper.getIsUserLoggedIn()) {
                    //TODO: code improvement, We can create constants for the pages
                    Log.v("test", "from scroll tab>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    checkLoginStatus();
                }

                if (position == 0) {
                    getToolbar().setLogo(R.drawable.ic_logo_petmeds_toolbar);
                    mShowOptionsMenu = true;
                } else {
                    getToolbar().setLogo(null);
                    mShowOptionsMenu = false;
                }
                invalidateOptionsMenu();
                setToolBarTitle((getResources().getStringArray(R.array.tab_title)[position]));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        };

        //code to set default first tab selected
        mViewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);
        // Instead of initializing it in onResume for many times we initialize it in onCreate
        mProgressDialog = new ProgressDialog();
        mAuthenticationDialog = new FingerprintAuthenticationDialog();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTabIndex == 3 && mPreferencesHelper.getIsUserLoggedIn()) {
            checkLoginStatus();
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgressDialog != null && mProgressDialog.isVisible()) {
            mProgressDialog.dismiss();
        }
        if (mAuthenticationDialog != null && mAuthenticationDialog.isVisible()) {
            mAuthenticationDialog.dismiss();
        }
    }

    @Override
    public void setAddress(Address address, int requestCode) {
        AddEditCardFragment addCardFragment = (AddEditCardFragment) getSupportFragmentManager()
                .findFragmentByTag(AddEditCardFragment.class.getName());
        if (addCardFragment != null) {
            addCardFragment.displayAddress(address);
        } else {
            AddEditCardFragment newFragment = AddEditCardFragment.newInstance(address, requestCode);
            replaceAccountAndAddToBackStack(newFragment, AddEditCardFragment.class.getName());
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showFingerprintDialog() {
        mAuthenticationDialog.setCancelable(false);
        mAuthenticationDialog.show(getSupportFragmentManager(), "FingerprintAuthenticationDialog");
    }

    public void removeAllFragment() {
        Log.d("remove all frgamnet", ">>>>");
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

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

    public AnalyticsUtil getAnalyticsRef() {
        return mAnalyticsUtil;
    }

    private void checkLoginStatus() {
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
        mProgressDialog.setCancelable(false);
        if (!mProgressDialog.isAdded()) {
            mProgressDialog.show(getSupportFragmentManager(), "ProgressDialog");
        }
    }

    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    public ViewGroup getContainerView() {
        return mContainerLayout;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mPreferencesHelper.setIsPushNotificationEnableFlag(true);
                mAnalyticsUtil.trackEvent(getString(R.string.push_notifications_category),
                        getString(R.string.push_notifications_enability),
                        getString(R.string.push_notifications_enable_label));
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mPreferencesHelper.setIsPushNotificationEnableFlag(false);
                mAnalyticsUtil.trackEvent(getString(R.string.push_notifications_category),
                        getString(R.string.push_notifications_disability),
                        getString(R.string.push_notification_disability));
                break;
        }
    }

}
