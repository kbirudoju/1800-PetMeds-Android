package com.petmeds1800.ui.fragments;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.intent.CheckOutIntent;
import com.petmeds1800.model.PayPalCheckoutRequest;
import com.petmeds1800.model.shoppingcart.request.AddItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.ApplyCouponRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.RemoveItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.UpdateItemQuantityRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.shoppingcart.ShoppingCartListContract;
import com.petmeds1800.ui.shoppingcart.presenter.ShoppingCartListPresenter;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.ShoppingCartRecyclerViewAdapter;
import com.petmeds1800.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import static com.petmeds1800.util.Constants.HIDE_PROGRESSBAR_OR_ANIMATION;
import static com.petmeds1800.util.Constants.SHOW_PROGRESSBAR_OR_ANIMATION;
import static com.petmeds1800.util.Utils.toggleGIFAnimantionVisibility;
import static com.petmeds1800.util.Utils.toggleProgressDialogVisibility;

/**
 * Created by pooja on 8/2/2016.
 */
public class CartFragment extends AbstractFragment implements ShoppingCartListContract.View, View.OnClickListener {

    public static final String SHOPPING_CART = "shoppingCart";

    public static final String CHECKOUT_STEPS = "checkoutSteps";

    public static int sPreviousScrollPosition = 0;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private RecyclerView mContainerLayoutItems;

    private ShoppingCartListContract.Presenter mPresenter;

    private LinearLayout mTotalCheckOutContainer;

    private LinearLayout mItemListtContainer;

    private LinearLayout mEmptyCheckoutContainer;

    private TextInputLayout mCouponCodeLayout;

    private LinearLayout mOfferCodeContainerLayout;

    private ProgressBar mProgressBar;

    private ShoppingCartRecyclerViewAdapter mShoppingCartRecyclerViewAdapter;

    private ShoppingCartListResponse mShoppingCartListResponse;

    private NestedScrollView mCartScrollViewContainer;


    public final Handler CartFragmentMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == Constants.DELETE_ITEM_REQUEST_SHOPPINGCART) {
                callmShoppingCartAPI(
                        new RemoveItemRequestShoppingCart(msg.getData().get(Constants.COMMERCE_ITEM_ID).toString(),
                                null));
            } else if (msg.what == Constants.UPDATE_ITEM_QUANTITY_SHOPPINGCART) {
                callmShoppingCartAPI(new UpdateItemQuantityRequestShoppingCart(
                        ((HashMap<String, String>) msg.getData().getSerializable(Constants.QUANTITY_MAP))));
            } else if (msg.what == Constants.CLICK_ITEM_UPDATE_SHOPPINGCART) {
                CommonWebviewFragment commonWebviewFragment = new CommonWebviewFragment();
                commonWebviewFragment.setArguments(msg.getData());
                addStepRootChildFragment(commonWebviewFragment, R.id.cart_root_fragment_container);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        PetMedsApplication.getAppComponent().inject(this);
        mContainerLayoutItems = (RecyclerView) view.findViewById(R.id.products_offered_RecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mContainerLayoutItems.setLayoutManager(mLayoutManager);
        mContainerLayoutItems.setItemAnimator(new DefaultItemAnimator());
        mContainerLayoutItems.setNestedScrollingEnabled(false);
        mCartScrollViewContainer = (NestedScrollView) view.findViewById(R.id.cartScrollViewContainer);
        mTotalCheckOutContainer = (LinearLayout) view.findViewById(R.id.total_checkout_container);
        mItemListtContainer = (LinearLayout) view.findViewById(R.id.item_list_container);
        mEmptyCheckoutContainer = (LinearLayout) view.findViewById(R.id.order_empty_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        ImageButton payPalCheckoutButton = (ImageButton) view.findViewById(R.id.button_paypal_checkout);
        Button continueShoppingButton = (Button) view.findViewById(R.id.continueShipping);
        setHasOptionsMenu(false);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.KEY_CART_FRAGMENT_INTENT_FILTER);
        intentFilter.addAction(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION);
        registerIntent(intentFilter, getActivity());

        payPalCheckoutButton.setOnClickListener(this);
        continueShoppingButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Button) mEmptyCheckoutContainer.findViewById(R.id.editCard_button))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity) getActivity()).scrollViewPager(0,true);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        callmShoppingCartAPI(null);
        //we should set the title only if current selected tab is not the first home tab
        if (((HomeActivity) getActivity()).getCurrentSelectedTab() == 1) {
            ((AbstractActivity) getActivity()).setToolBarTitle((getResources().getStringArray(R.array.tab_title)[1]));
        }
    }

    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {

        if (intent.getAction().equals(Constants.KEY_CART_FRAGMENT_INTENT_FILTER)) {
            callmShoppingCartAPI(null);
        } else if (intent.getAction().equals(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION)) {
            checkAndSetHasOptionsMenu(intent, CartRootFragment.class.getName());
        }
        super.onReceivedBroadcast(context, intent);

    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deregisterIntent(getActivity());
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public boolean postGeneralPopulateShoppingCart(ShoppingCartListResponse shoppingCartListResponse) {

        boolean response = false;
        UpdateValueThreadResponse();
        hideErrorLayout();
        if (null != shoppingCartListResponse && shoppingCartListResponse.getItemCount() > 0) {
            response = initializeShoppingCartPage(shoppingCartListResponse);
            toggleVisibilityShoppingList(false);
        } else if (null == shoppingCartListResponse || shoppingCartListResponse.getItemCount() == 0) {
            toggleVisibilityShoppingList(true);
        }

        toggleProgressDialogVisibility(HIDE_PROGRESSBAR_OR_ANIMATION, mProgressBar);
        toggleGIFAnimantionVisibility(HIDE_PROGRESSBAR_OR_ANIMATION, getActivity());

        return response;
    }

    @Override
    public boolean onError(String errorMessage, String simpleName) {
        toggleProgressDialogVisibility(HIDE_PROGRESSBAR_OR_ANIMATION, mProgressBar);
        toggleGIFAnimantionVisibility(HIDE_PROGRESSBAR_OR_ANIMATION, getActivity());

        if (errorMessage != null && !errorMessage.isEmpty()) {
            if (simpleName != null && simpleName
                    .equalsIgnoreCase(ApplyCouponRequestShoppingCart.class.getSimpleName())) {
                mCouponCodeLayout.setError(errorMessage);
                mOfferCodeContainerLayout.findViewById(R.id.order_status_label).setVisibility(View.GONE);
            } else if (simpleName != null && simpleName
                    .equalsIgnoreCase(UpdateItemQuantityRequestShoppingCart.class.getSimpleName())) {

                if (null != mShoppingCartListResponse && null != mContainerLayoutItems) {
                    mContainerLayoutItems.setAdapter(null);
                    mShoppingCartRecyclerViewAdapter = new ShoppingCartRecyclerViewAdapter(
                            mShoppingCartListResponse.getShoppingCart().getCommerceItems(), createFooter(
                            ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                    .inflate(R.layout.view_offer_code_card, null, false), mShoppingCartListResponse),
                            getActivity(), CartFragmentMessageHandler);
                    mContainerLayoutItems.setAdapter(mShoppingCartRecyclerViewAdapter);
                }

                Utils.displayCrouton(getActivity(), (String) errorMessage, mItemListtContainer);
            } else {
                Utils.displayCrouton(getActivity(), (String) errorMessage, mItemListtContainer);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setPresenter(ShoppingCartListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onSuccess(String url) {
        mProgressBar.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.PAYPAL_DATA, url);
        bundle.putBoolean(CommonWebviewFragment.ISCHECKOUT, false);
        replaceCartFragmentWithBackStack(new CommonWebviewFragment(), CommonWebviewFragment.class.getName(), bundle);
       /* CommonWebviewFragment webViewFragment = new CommonWebviewFragment();
        webViewFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.cart_root_fragment_container, webViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();*/
    }

    @Override
    public void onPayPalError(String errorMsg) {
        if (errorMsg.isEmpty()) {
            Utils.displayCrouton(getActivity(), getString(R.string.unexpected_error_label), mItemListtContainer);

        } else {
            Utils.displayCrouton(getActivity(), errorMsg, mItemListtContainer);

        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {
        hideAllShoppingListView();
        showErrorLayout();
    }

    private View createFooter(final View footerView, ShoppingCartListResponse shoppingCartListResponse) {
        mOfferCodeContainerLayout = (LinearLayout) footerView.findViewById(R.id.cart_each_item_container);
        mCouponCodeLayout = (TextInputLayout) footerView.findViewById(R.id.coupon_code_input_layout);
        Button OrderStatusLAbel = (Button) footerView.findViewById(R.id.order_status_label);

        OrderStatusLAbel.setVisibility(View.GONE);
        ((EditText) mCouponCodeLayout.getEditText()).setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_GO) {
                    callmShoppingCartAPI(new ApplyCouponRequestShoppingCart(
                            ((EditText) mCouponCodeLayout.getEditText()).getText().toString().trim(), null));
                    return true;
                }
                return false;
            }
        });
        ((EditText) mCouponCodeLayout.getEditText()).setImeOptions(EditorInfo.IME_ACTION_GO);

        ((EditText) mCouponCodeLayout.getEditText()).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (((EditText) mCouponCodeLayout.getEditText()).getRight()
                            - ((EditText) mCouponCodeLayout.getEditText()).getCompoundDrawables()[DRAWABLE_RIGHT]
                            .getBounds().width())) {
                        callmShoppingCartAPI(new ApplyCouponRequestShoppingCart(
                                ((EditText) mCouponCodeLayout.getEditText()).getText().toString().trim(), null));

                        return true;
                    }
                }
                return false;
            }
        });

        if (null != shoppingCartListResponse && null != shoppingCartListResponse.getShoppingCart().getCoupon()
                && !shoppingCartListResponse.getShoppingCart().getCoupon().isEmpty()) {
            mCouponCodeLayout.getEditText().setText(shoppingCartListResponse.getShoppingCart().getCoupon());
            OrderStatusLAbel.setVisibility(View.VISIBLE);
        }
        return footerView;
    }

    void startCheckoutProcess(ShoppingCartListResponse shoppingCartListResponse) {
        //start the checkoutActitiy
        CheckOutIntent checkOutIntent = new CheckOutIntent(getContext());
        checkOutIntent.putExtra(SHOPPING_CART, shoppingCartListResponse);
        startActivity(checkOutIntent);
    }

    private boolean initializeShoppingCartPage(final ShoppingCartListResponse shoppingCartListResponse) {
        mShoppingCartListResponse = shoppingCartListResponse;
        mShoppingCartRecyclerViewAdapter = new ShoppingCartRecyclerViewAdapter(
                shoppingCartListResponse.getShoppingCart().getCommerceItems(), createFooter(
                ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.view_offer_code_card, null, false), shoppingCartListResponse), getActivity(),
                CartFragmentMessageHandler);
        mContainerLayoutItems.setAdapter(mShoppingCartRecyclerViewAdapter);

        if (sPreviousScrollPosition > 0) {
            mContainerLayoutItems.scrollToPosition(sPreviousScrollPosition);
            sPreviousScrollPosition = 0;
        }

        Locale locale = getResources().getConfiguration().locale;
        if (null != shoppingCartListResponse.getShoppingCart()) {
            ((TextView) (mTotalCheckOutContainer.findViewById(R.id.items_total_amt_txt))).setText(
                    getActivity().getResources().getString(R.string.dollar_placeholder) + String
                            .format(locale, "%.2f", shoppingCartListResponse.getShoppingCart().getItemsTotal()));
            ((TextView) (mTotalCheckOutContainer.findViewById(R.id.subtotal_value_txt))).setText(
                    getActivity().getResources().getString(R.string.dollar_placeholder) + String
                            .format(locale, "%.2f", shoppingCartListResponse.getShoppingCart().getSubTotal()));

            ((Button) mTotalCheckOutContainer.findViewById(R.id.button_checkout))
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (null != shoppingCartListResponse.getShoppingCart()) {
                                startCheckoutProcess(shoppingCartListResponse);
                            }
                        }
                    });

        } else {
            ((TextView) (mTotalCheckOutContainer.findViewById(R.id.items_total_amt_txt))).setText("-");
            ((TextView) (mTotalCheckOutContainer.findViewById(R.id.subtotal_value_txt))).setText("-");
        }

        if (null != shoppingCartListResponse.getShoppingCart()) {
            ((TextView) mTotalCheckOutContainer.findViewById(R.id.offer_code_value_txt)).setText(
                    getActivity().getResources().getString(R.string.dollar_placeholder) + String
                            .format(locale, "%.2f", shoppingCartListResponse.getShoppingCart().getDiscountAmount()));
        } else {
            ((TextView) mTotalCheckOutContainer.findViewById(R.id.offer_code_value_txt)).setText("-");
        }

        if (shoppingCartListResponse.getShippingMessageInfo().getIsFreeShipping()) {
            mTotalCheckOutContainer.findViewById(R.id.shipping_description_layout).setVisibility(View.VISIBLE);
            ((TextView) (mTotalCheckOutContainer.findViewById(R.id.standard_shipping_txt))).setText(
                    getActivity().getResources().getString(R.string.your_order_qualifies_for_free_standard_shipping));
        } else if (!shoppingCartListResponse.getShippingMessageInfo().getIsFreeShipping()
                && null != shoppingCartListResponse.getShippingMessageInfo().getMessage()
                && shoppingCartListResponse.getShippingMessageInfo().getMessage().length() > 0) {
            mTotalCheckOutContainer.findViewById(R.id.shipping_description_layout).setVisibility(View.VISIBLE);
            ((TextView) (mTotalCheckOutContainer.findViewById(R.id.standard_shipping_txt)))
                    .setText(shoppingCartListResponse.getShippingMessageInfo().getMessage().trim());
        } else {
            ((TextView) (mTotalCheckOutContainer.findViewById(R.id.standard_shipping_txt))).setText("-");
            mTotalCheckOutContainer.findViewById(R.id.shipping_description_layout).setVisibility(View.GONE);
        }
        return true;
    }

    private void toggleVisibilityShoppingList(boolean isEmpty) {
        if (!isEmpty) {
            mCartScrollViewContainer.setVisibility(View.VISIBLE);
            mEmptyCheckoutContainer.setVisibility(View.GONE);
        } else {
            mCartScrollViewContainer.setVisibility(View.GONE);
            mEmptyCheckoutContainer.setVisibility(View.VISIBLE);
        }
    }

    private void hideAllShoppingListView() {
        mCartScrollViewContainer.setVisibility(View.GONE);
        mEmptyCheckoutContainer.setVisibility(View.GONE);
    }

    private void callmShoppingCartAPI(Object object) {

        if (mPresenter == null) {
            mPresenter = new ShoppingCartListPresenter(this);
        }
        if (object == null) {
            toggleProgressDialogVisibility(SHOW_PROGRESSBAR_OR_ANIMATION, mProgressBar);
            mPresenter.getGeneralPopulateShoppingCart(false);
        } else if (object instanceof AddItemRequestShoppingCart) {
            mPresenter.getAddItemShoppingCart((AddItemRequestShoppingCart) (object), false);
        } else if (object instanceof RemoveItemRequestShoppingCart) {
            toggleGIFAnimantionVisibility(SHOW_PROGRESSBAR_OR_ANIMATION, getActivity());
            mPresenter.getRemoveItemShoppingCart((RemoveItemRequestShoppingCart) (object), false);
        } else if (object instanceof ApplyCouponRequestShoppingCart) {
            toggleGIFAnimantionVisibility(SHOW_PROGRESSBAR_OR_ANIMATION, getActivity());
            mPresenter.getApplyCouponShoppingCart((ApplyCouponRequestShoppingCart) (object), false);
        } else if (object instanceof UpdateItemQuantityRequestShoppingCart) {
            toggleGIFAnimantionVisibility(SHOW_PROGRESSBAR_OR_ANIMATION, getActivity());
            mPresenter
                    .getUpdateItemQuantityRequestShoppingCart((UpdateItemQuantityRequestShoppingCart) (object), false);
        }
    }

    public void UpdateValueThreadResponse() {
        try {
            ((HomeActivity) getActivity()).updateCartTabItemCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_paypal_checkout:
                mProgressBar.setVisibility(View.VISIBLE);
                PayPalCheckoutRequest payPalCheckoutRequest = new PayPalCheckoutRequest("cart");
                mPresenter.checkoutPayPal(payPalCheckoutRequest);
                break;
            case R.id.continueShipping:
                ((HomeActivity) getActivity()).scrollViewPager(0,false);
                break;
        }

    }

    public void startCheckoutAfterPayment(final ShoppingCartListResponse response) {
        if (response.getShoppingCart() != null) {
            //start the checkoutActitiy
            CheckOutIntent checkOutIntent = new CheckOutIntent(getContext());
            checkOutIntent.putExtra(SHOPPING_CART, response);
            checkOutIntent.putExtra(CHECKOUT_STEPS, response.getCheckoutSteps());
            startActivity(checkOutIntent);
            Log.d("response in cart",
                    response.getCheckoutSteps().getApplicableSteps() + ">>>" + response.getCheckoutSteps()
                            .getStepState().getNextCheckoutStep());

        } else {
            Log.d("response in cart", response.getStatus().getErrorMessages().get(0));
            final String errormsg = response.getStatus().getErrorMessages().get(0);

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.displayCrouton(getActivity(), errormsg, mItemListtContainer);
                        }
                    });
                }
            };
            thread.start();
        }
    }

    @Override
    protected void onRetryButtonClicked(View view) {
        super.onRetryButtonClicked(view);
        callmShoppingCartAPI(null);
    }
}