package com.petmeds1800.ui;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.intent.AddUpdateMedicationRemindersIntent;
import com.petmeds1800.intent.CheckOutIntent;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.model.entities.MedicationReminderItem;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.mvp.RefillNotificationContract;
import com.petmeds1800.mvp.RefillNotificationPresenter;
import com.petmeds1800.ui.fragments.AccountRootFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.CartRootFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.fragments.HomeFragment;
import com.petmeds1800.ui.fragments.HomeRootFragment;
import com.petmeds1800.ui.fragments.LearnRootFragment;
import com.petmeds1800.ui.fragments.dialog.FingerprintAuthenticationDialog;
import com.petmeds1800.ui.fragments.dialog.ProgressDialog;
import com.petmeds1800.ui.medicationreminders.AddEditMedicationRemindersFragment;
import com.petmeds1800.ui.medicationreminders.MedicationReminderItemListContract;
import com.petmeds1800.ui.payment.AddACardContract;
import com.petmeds1800.ui.payment.AddEditCardFragment;
import com.petmeds1800.ui.pets.AddPetFragment;
import com.petmeds1800.ui.support.TabPagerAdapter;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.AsyncUpdateShoppingCartIconCountThread;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.RetrofitErrorHandler;
import com.petmeds1800.util.Utils;
import com.urbanairship.UAirship;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
        implements AddACardContract.AddressSelectionListener,
        MedicationReminderItemListContract.AddEditMedicationReminderListener, DialogInterface.OnClickListener,
        CommonWebviewFragment.OnPaymentCompletedListener, RefillNotificationContract.View {

    public static final String SETUP_HAS_OPTIONS_MENU_ACTION = "setupHasOptionsMenuAction";

    public static final String FRAGMENT_NAME_KEY = "fragmentName";

    private static final int OFFSCREEN_PAGE_LIMIT = 3;

    private static final String SCREEN_TYPE = "screenType";

    private static final String NAVIGATE_TO_CART = "navigateToCart";

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

    TabPagerAdapter mAdapter;

    ArrayList<View> mTabLayoutArray = new ArrayList<>();

    private CommitOrderResponse commitOrderResponse;

    private CartRootFragment mCartRootFragment;

    //TODO chnage when push payload is appropriate

    private final int TYPE_MEDICATION_REMINDER__ALERT = 1;

    private final int TYPE_OFFER__ALERT = 2;

    private final int TYPE_ORDER_SHIPPED__ALERT = 3;

    private final int TYPE_PRESCRIPTION_REFILL_DUE_ALERT = 4;

    private final int TYPE_PRESCRIPTION_ORDERED_RECALL_ALERT = 5;

    private final int TYPE_QUESTION_ANSWER_ALERT = 6;

    private final int TYPE_VET_VERIFY_RX_ALERT = 7;

    private int screenType;

    private boolean submitPressed;

    private AccountRootFragment mAccountRootFragment;

    private RefillNotificationContract.Presenter mPresenter;

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        Log.v("on new intent fired", "on new intent fired");
        super.onNewIntent(intent);
        setIntent(intent);
        replaceCommitOrderFragment();

    }

    private void replaceCommitOrderFragment() {
        commitOrderResponse = (CommitOrderResponse) getIntent().getSerializableExtra(
                Constants.CONFIRMATION_ORDER_RESPONSE);
        if (commitOrderResponse != null) {
            mCartRootFragment = ((CartRootFragment) mAdapter.getItem(1));
            mCartRootFragment.replaceConfirmOrderFragment(commitOrderResponse);
        }
    }

    //Call to replace receipt fragment on page listener
    public void replaceFragmentOnConfirmReceipt() {
        if (commitOrderResponse != null) {
            commitOrderResponse = null;
            mCartRootFragment.replaceConfirmOrderFragment(commitOrderResponse);

        }
    }


    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().setLogo(R.drawable.ic_logo_petmeds_toolbar);
        ButterKnife.bind(this);
        mAnalyticsUtil = new AnalyticsUtil();

        mPresenter = new RefillNotificationPresenter(this, HomeActivity.this);

        PetMedsApplication.getAppComponent().inject(this);
        //Perform operation on the first time loading of home screen
        performOperationOnFirstLoad();
        //initialize fragment list
        List<Fragment> fragmentList = new ArrayList<>();
        HomeRootFragment mHomeRootFragment = new HomeRootFragment();
        mCartRootFragment = new CartRootFragment();
        fragmentList.add(mHomeRootFragment);
        fragmentList.add(mCartRootFragment);
        fragmentList.add(new LearnRootFragment());
        mAccountRootFragment = new AccountRootFragment();
        fragmentList.add(mAccountRootFragment);
        mAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mAdapter);
        //we need to set the offset to 3 otherwise options menu cant be managed.
        mViewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        mHomeTab.setupWithViewPager(mViewPager);
        for (int i = 0; i < mHomeTab.getTabCount(); i++) {
            View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.custom_tab_image_overlay, null, false);
            mTabLayoutArray.add(i, v);
            mHomeTab.getTabAt(i).setCustomView(mTabLayoutArray.get(i));
        }
        if (getIntent() != null) {
            screenType = getIntent().getIntExtra(SCREEN_TYPE, 0);
        }
        mHomeTab.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
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
                if (position != 1) {
                    replaceFragmentOnConfirmReceipt();
                }
                Log.d("onPageSelected", ">>>>>>");
                mTabIndex = position;
                for (int i = 0; i < mHomeTab.getTabCount(); ++i) {
                    ((ImageView) (mHomeTab.getTabAt(i).getCustomView()).findViewById(R.id.tab_default_image))
                            .setImageResource(i != position ? TAB_ICON_UNSELECTED[i] : TAB_ICON_SELECTED[i]);

                }

                if (position == 3 && mPreferencesHelper.getIsUserLoggedIn()) {
                    //TODO: code improvement, We can create constants for the pages
                    Log.v("test", "from scroll tab>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                    if (screenType == 0 || screenType == TYPE_PRESCRIPTION_ORDERED_RECALL_ALERT) {
                        checkLoginStatus();
                    }
                }
                //Send analytics
                sendAnalytics(position);
                if (position == 0) {
                    getToolbar().setLogo(R.drawable.ic_logo_petmeds_toolbar);
                    screenType = getIntent().getIntExtra("screenType", 0);

                    //send local broadcast to select the WidgetsTab by default.PETU-40
                    Intent intent = new Intent(HomeFragment.SELECT_WIDGET_TAB);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                } else {
                    getToolbar().setLogo(null);
                }
                invalidateOptionsMenu(position);
                setToolBarTitle((getResources().getStringArray(R.array.tab_title)[position]));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        };
        //code to replace fragment when commit order response comes in anonymous case
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceCommitOrderFragment();
                if (commitOrderResponse != null) {
                    mViewPager.setCurrentItem(1);
                }
            }
        }, 500);
        //To scroll to cart fragment in case of anonymous checkout process
        if (getIntent() != null && getIntent().getBooleanExtra(NAVIGATE_TO_CART, false)) {
            getIntent().putExtra(NAVIGATE_TO_CART, false);
            mViewPager.setCurrentItem(1);
        }
        pageChangeListener.onPageSelected(0);
        invalidateOptionsMenu(0);

        // Instead of initializing it in onResume for many times we initialize it in onCreate
        mProgressDialog = new ProgressDialog();
        mAuthenticationDialog = new FingerprintAuthenticationDialog();
        mViewPager.addOnPageChangeListener(pageChangeListener);
        navigateOnReceivedNotification(screenType);

        IntentFilter intentFilter = new IntentFilter(Constants.INTENT_FILTER_REFILL_NOTIFICATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLoginReceiver, intentFilter);

    }

    private void sendAnalytics(int position) {
        switch (position) {
            case 0:
                mAnalyticsUtil.trackScreen(getString(R.string.home_title));
                break;
            case 1:
                mAnalyticsUtil.trackScreen(getString(R.string.cart_title));
                break;
            case 3:
                mAnalyticsUtil.trackScreen(getString(R.string.title_account));
                break;

        }
    }

    private void navigateOnReceivedNotification(final int screenType) {
        //Delay applied in order to get rid of Illegal argument exception as fingerprintauth dailog
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (screenType) {
                    case TYPE_MEDICATION_REMINDER__ALERT:
                    case TYPE_ORDER_SHIPPED__ALERT:
                    case TYPE_VET_VERIFY_RX_ALERT:
                    case TYPE_PRESCRIPTION_ORDERED_RECALL_ALERT:
                        mViewPager.setCurrentItem(3);
                        break;
                    case TYPE_OFFER__ALERT:
                    case TYPE_PRESCRIPTION_REFILL_DUE_ALERT:
                        showProgress();
                        mPresenter.checkSecurityStatus();
                        break;
                    case TYPE_QUESTION_ANSWER_ALERT:
                        mViewPager.setCurrentItem(2);
                        HomeActivity.this.screenType = 0;
                        break;


                }
            }
        }, 200);


    }


    private void performOperationOnFirstLoad() {
        if (!RxFingerprint.isHardwareDetected(this)) {
            mPreferencesHelper.setIsFingerPrintEnabled(false);
        } else if (!RxFingerprint.hasEnrolledFingerprints(this)) {
            mPreferencesHelper.setIsFingerPrintEnabled(false);
        }
        if (getIntent().getBooleanExtra(IS_FROM_HOME_ACTIVITY, false) && mPreferencesHelper.getIsUserLoggedIn()) {
            showPushPermissionDailog();
            startService(new AddUpdateMedicationRemindersIntent(this, false));
        }
    }

    public void invalidateOptionsMenu(int position) {
        Fragment fragment = mAdapter.getItem(position);
        Intent intent = new Intent(SETUP_HAS_OPTIONS_MENU_ACTION);
        intent.putExtra(FRAGMENT_NAME_KEY, fragment.getClass().getName());

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        invalidateOptionsMenu();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (submitPressed) {
            // Pop back stack when in app notification dailog arrives
            this.getSupportFragmentManager().popBackStackImmediate("ProgressDialog", 0);
        }
        if (mTabIndex == 3 && mPreferencesHelper.getIsUserLoggedIn()) {
            if (screenType == 0 || screenType == TYPE_PRESCRIPTION_ORDERED_RECALL_ALERT) {
                checkLoginStatus();
            }
            // checkLoginStatus();
        }

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
        if (!mAuthenticationDialog.isAdded()) {
            mAuthenticationDialog.setCancelable(false);
            mAuthenticationDialog.show(getSupportFragmentManager(), "FingerprintAuthenticationDialog");
        }
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
                        Log.i("security status:", securityStatus + "");
                        //TODO: improvement
                        if (securityStatus == 0) {  // We need to treat security status 2 same as 0 as all the API on AccountSection stopped working if we would treat 2 same as 4
                            showFingerprintDialog();
                        } else if (securityStatus == 4 || securityStatus
                                == 5 || securityStatus == 2) { //As per backend team, security status 4 or 5 should be treated similarly
                            //TODO: research more into silent sign in logic
                            mAccountRootFragment.showAccountFragment();
                        }
                    }
                });
    }

    public void showProgress() {
        if (!mProgressDialog.isAdded()) {
            mProgressDialog.setCancelable(false);
            mProgressDialog.show(getSupportFragmentManager(), "ProgressDialog");
            submitPressed = true;
        }
    }

    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    public ViewGroup getContainerView() {
        return mContainerLayout;
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        super.onApplyThemeResource(theme, resid, first);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AddPetFragment fragment = (AddPetFragment) getSupportFragmentManager()
                .findFragmentByTag(AddPetFragment.class.getName());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                setPushNotificationEnability(true);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                setPushNotificationEnability(false);
                break;
        }
    }

    private void setPushNotificationEnability(boolean value) {
        mPreferencesHelper.setIsPushNotificationEnableFlag(value);
        UAirship.shared().getPushManager().setUserNotificationsEnabled(value);
    }


    @Override
    public void setItemDescription(String productName, String description) {
        AddEditMedicationRemindersFragment addEditMedicationRemindersFragment
                = (AddEditMedicationRemindersFragment) getSupportFragmentManager()
                .findFragmentByTag(AddEditMedicationRemindersFragment.class.getName());
        if (addEditMedicationRemindersFragment != null) {
            addEditMedicationRemindersFragment.displayItemText(productName, description);
        } else {
            replaceAccountAndAddToBackStack(
                    AddEditMedicationRemindersFragment.newInstance(false, (MedicationReminderItem) null),
                    AddEditMedicationRemindersFragment.class.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Saving the state of the progress dailog
        outState.putBoolean("submitPressed", submitPressed);
    }

    /**
     * For updating BOTH contents of Shopping Cart and Tab Layout Quantity. Internally calls updateCartMenuItemCount
     * called when contents are altered in Shopping Cart from Outside {@link CartFragment}
     */
    public void updateCartTabItemCount() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new AsyncUpdateShoppingCartIconCountThread(mApiService, mPreferencesHelper, HomeActivityMessageHandler)
                        .startAsyncUpdateShoppingCartIconCountThread();
            }
        };
        new Handler().postDelayed(runnable, 1);
    }

    /**
     * For updating the TAB LAyout count for Shopping Cart ONLY. Does not call other APIs internally Only TAB layout
     * count changes. NOT content of ShoppingCart iteslf
     */
    public void updateCartMenuItemCount() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(new Intent(Constants.KEY_CART_FRAGMENT_INTENT_FILTER));
            }
        };
        new Handler().postDelayed(runnable, 1);
    }

    public final Handler HomeActivityMessageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == Constants.KEY_COMPLETED_ASYN_COUNT_FETCH) {
                if (msg.getData().getBoolean(Constants.KEY_SHOPPING_CART_ASYNC_SUCCESS)) {
                    mTabLayoutArray.get(1).findViewById(R.id.tab_text_over_image_container).setVisibility(View.VISIBLE);
                    if ((Integer.parseInt(msg.getData().getString(Constants.KEY_SHOPPING_CART_ICON_VALUE))) > 0) {
                        ((TextView) mTabLayoutArray.get(1).findViewById(R.id.tab_text_over_image))
                                .setBackgroundResource(R.drawable.ball_red);
                        ((TextView) mTabLayoutArray.get(1).findViewById(R.id.tab_text_over_image))
                                .setTextColor(Color.WHITE);
                    } else {
                        ((TextView) mTabLayoutArray.get(1).findViewById(R.id.tab_text_over_image))
                                .setBackgroundResource(R.drawable.ball_white);
                        ((TextView) mTabLayoutArray.get(1).findViewById(R.id.tab_text_over_image))
                                .setTextColor(getResources().getColor(R.color.petmeds_blue));
                    }
                    ((TextView) mTabLayoutArray.get(1).findViewById(R.id.tab_text_over_image))
                            .setText(msg.getData().getString(Constants.KEY_SHOPPING_CART_ICON_VALUE));
                } else {
                    Snackbar.make(mContainerLayout, msg.getData().getString(Constants.KEY_SHOPPING_CART_ICON_VALUE),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        }
    };

    /**
     * Move View Pager Externaly via programtically assigning selection number
     */
    public void scrollViewPager(int pageNo,boolean navigateToShopCategory) {
        mViewPager.setCurrentItem(pageNo);
        //Sending broadcast in order to scroll to shop by category
        if(navigateToShopCategory) {
            Intent intent = new Intent(Constants.SCROLL_TO_SHOP_CATEGORIES);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    public int getCurrentSelectedTab() {
        if (mViewPager != null) {
            return mViewPager.getCurrentItem();
        }

        return -1;
    }


    @Override
    public void onPaymentCompleted(ShoppingCartListResponse paypalResponse) {
        CartFragment cartFragment = (CartFragment) getSupportFragmentManager()
                .findFragmentByTag(CartFragment.class.getSimpleName());
        if (cartFragment != null) {
            cartFragment.startCheckoutAfterPayment(paypalResponse);
        }
    }

    @Override
    public void onCheckoutPaymentCompleted(ShoppingCartListResponse paypalResponse, String stepName) {

    }

    @Override
    public void onSecurityStatusSuccess() {

    }

    @Override
    public void onSecurityStatusError() {
        hideProgress();
        FingerprintAuthenticationDialog mAuthenticationDialog = new FingerprintAuthenticationDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_REFILL_NOTIFICATION, true);
        mAuthenticationDialog.setArguments(bundle);
        if (!mAuthenticationDialog.isAdded()) {
            mAuthenticationDialog.setCancelable(false);
            mAuthenticationDialog
                    .show(getSupportFragmentManager(), "FingerprintAuthenticationDialog");
        }
    }

    @Override
    public void onShoppingCartSuccess(ShoppingCartListResponse response) {
        CheckOutIntent checkOutIntent = new CheckOutIntent(HomeActivity.this);
        checkOutIntent.putExtra(CartFragment.SHOPPING_CART, response);
        checkOutIntent.putExtra(CartFragment.CHECKOUT_STEPS, response.getCheckoutSteps());
        startActivity(checkOutIntent);
    }

    @Override
    public void onShoppingCartError(String errorMsg) {
        hideProgress();
        Utils.displayCrouton(this, errorMsg, mContainerLayout);
    }

    @Override
    public void onHomeWidgetError(String errorMsg) {
        hideProgress();
        Utils.displayCrouton(this, errorMsg, mContainerLayout);
    }

    @Override
    public void setPresenter(RefillNotificationContract.Presenter presenter) {

    }

    private BroadcastReceiver mLoginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent;
            showProgress();
            mPresenter.checkSecurityStatus();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLoginReceiver);
    }
}



