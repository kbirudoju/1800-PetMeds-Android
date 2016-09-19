package com.petmeds1800.ui;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.ui.fragments.AccountRootFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
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

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
                //TODO: could not understand the purpose of this code so removed. If still needed please justify
//                if (position == 0 || position == 1 || position == 2) {
//                    removeAllFragment();
//                }

                if (position == 3 && mPreferencesHelper.getIsUserLoggedIn()) {
                    //TODO: code improvement, We can create constants for the pages
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
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressDialog = new ProgressDialog();
        mAuthenticationDialog = new FingerprintAuthenticationDialog();
        if (mTabIndex == 3 && mPreferencesHelper.getIsUserLoggedIn()) {
            checkLoginStatus();
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgressDialog.isVisible()) {
            mProgressDialog.dismiss();
        }
        if (mAuthenticationDialog.isVisible()) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        disableBackButton();

        final MenuItem barcodeMenuItem = menu.findItem(R.id.action_barcode);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        if (mShowOptionsMenu) {
            barcodeMenuItem.setVisible(true);
            searchMenuItem.setVisible(true);
        } else {
            barcodeMenuItem.setVisible(false);
            searchMenuItem.setVisible(false);
        }
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                barcodeMenuItem.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                barcodeMenuItem.setVisible(true);
                return true;
            }
        });

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setQueryHint(getString(R.string.label_search));
        searchView.setIconifiedByDefault(false);
        EditText searchEdit = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(ContextCompat.getColor(this, R.color.white));
        searchEdit.setHintTextColor(ContextCompat.getColor(this, R.color.hint_color));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    String encodedQuery = URLEncoder.encode(query, "utf-8");
                    String url = getString(R.string.server_endpoint) + "/search.jsp?Ns=product.salesvolume%7C1&Ntt="
                            + encodedQuery;
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, query);
                    bundle.putString(CommonWebviewFragment.URL_KEY, url);
                    getToolbar().setLogo(null);
                    MenuItemCompat.collapseActionView(searchMenuItem);
                    mHomeRootFragment.addOrReplaceFragmentWithBackStack(new CommonWebviewFragment(), bundle);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchText) {
                return false;
            }
        });

        // if prev search string, restore it here
        if (!TextUtils.isEmpty(mSearchString)) {
            searchView.setQuery(mSearchString, true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mHomeRootFragment.popBackStack();
            setUpToolbar();
        }
//        if (item.getItemId() == R.id.action_search) {
//
//        } else if (item.getItemId() == R.id.action_barcode) {
//
//        }
        return false;
    }

    @Override
    public void onBackPressed() {
        setUpToolbar();
        super.onBackPressed();
    }

    private void setUpToolbar() {
        //we will check if the current selected item is the home tab, we will setup the action bar
        if ( mViewPager.getCurrentItem() == 0 ) {
            invalidateOptionsMenu();
            getToolbar().setTitle(null);
            getToolbar().setLogo(R.drawable.ic_logo_petmeds_toolbar);
        }

    }

    private void showFingerprintDialog() {
        mAuthenticationDialog.setCancelable(false);
        mAuthenticationDialog.show(getSupportFragmentManager(), "FingerprintAuthenticationDialog");
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
        mProgressDialog.show(getSupportFragmentManager(), "ProgressDialog");
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
