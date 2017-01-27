package com.petmeds1800.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.AddRecentlyItemToCart;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.entities.Category;
import com.petmeds1800.model.entities.PetItemList;
import com.petmeds1800.model.entities.Products;
import com.petmeds1800.model.entities.RecentlyOrdered;
import com.petmeds1800.model.entities.RecommendedProducts;
import com.petmeds1800.model.entities.SalePitch;
import com.petmeds1800.model.entities.WhatsNextCategory;
import com.petmeds1800.model.entities.WidgetData;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.dashboard.presenter.WidgetContract;
import com.petmeds1800.ui.dashboard.presenter.WidgetPresenter;
import com.petmeds1800.ui.dashboard.support.WidgetListAdapter;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.fragments.HomeFragment;
import com.petmeds1800.ui.fragments.HomeRootFragment;
import com.petmeds1800.ui.support.HomeFragmentContract;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Log;
import com.petmeds1800.util.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/13/2016.
 */
public class WidgetListFragment extends AbstractFragment implements WidgetContract.View {
    @BindView(R.id.widget_recycler_view)
    RecyclerView mWidgetRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.containerLayoutWidget)
    RelativeLayout mContainerLayout;

    private WidgetListAdapter mWidgetListAdapter;
    private WidgetContract.Presenter mPresenter;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private HomeFragmentContract.ProductCategoryInteractionListener mWidgetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_widget_list, container, false);
        ButterKnife.bind(this, view);
        mWidgetListAdapter = new WidgetListAdapter(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() instanceof PetItemList) {
                    PetItemList petItem = (PetItemList) v.getTag();
                    String productId = petItem.getSku().getParentProduct().getProductId();
                    String skuId = petItem.getSku().getSkuId();
                    int quantity = petItem.getRefillQuantity();
                    try {
                        ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AddToCartRequest addToCartRequest = new AddToCartRequest(skuId, productId, quantity, mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                    mPresenter.addToCart(addToCartRequest);
                    Log.d("cart detail", petItem.getRefillQuantity() + ">>>" + petItem.getSku().getParentProduct().getProductId() + ">>>" + petItem.getSku().getSkuId());
                }else if(v.getTag() instanceof RecentlyOrdered){
                    RecentlyOrdered recentlyOrdered = (RecentlyOrdered) v.getTag();
                    String productId = recentlyOrdered.getParentProduct().getProductId();
                    String skuId = recentlyOrdered.getSkuId();
                    String quantity = recentlyOrdered.getPurchaseItemId();
                    Log.d("minqty", ">>>>>" + quantity);
                    try {
                        ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AddRecentlyItemToCart addRecentlyItemToCart = new AddRecentlyItemToCart(skuId, productId, quantity, mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                    mPresenter.addRecentlyItemToCart(addRecentlyItemToCart);
                }
                else if(v.getTag() instanceof Category){
                 Category category =(Category)v.getTag();
                  ((HomeFragmentContract.ProductCategoryInteractionListener) getParentFragment()).replaceWebViewFragment(category.getCategoryPageUrl(), getString(R.string.recommendation_title));


                }else if(v.getTag() instanceof WhatsNextCategory){
                    WhatsNextCategory whatsNextCategory = (WhatsNextCategory) v.getTag();
                    ((HomeFragmentContract.ProductCategoryInteractionListener) getParentFragment()).replaceWebViewFragment(whatsNextCategory.getCategoryPageUrl(), getString(R.string.whats_next_title));

                }else if(v.getTag() instanceof WidgetData){
                    WidgetData widgetData = (WidgetData) v.getTag();
                    ((HomeFragmentContract.ProductCategoryInteractionListener) getParentFragment()).replaceWebViewFragment(widgetData.getLearnMoreUrl(), getString(R.string.tip_title));

                }else if(v.getTag() instanceof SalePitch){
                    SalePitch salePitch=(SalePitch)v.getTag();
                    ((HomeFragmentContract.ProductCategoryInteractionListener) getParentFragment()).replaceWebViewFragment(salePitch.getLinkUrl(), "");

                }else if(v.getTag() instanceof RecommendedProducts){
                    RecommendedProducts recommendedProducts=(RecommendedProducts)v.getTag();
                    ((HomeFragmentContract.ProductCategoryInteractionListener) getParentFragment()).replaceWebViewFragment(recommendedProducts.getProductPageUrl(), recommendedProducts.getDisplayName());

                }else if(v.getTag() instanceof Products){
                    Products shoppingProducts=(Products)v.getTag();
                    ((HomeFragmentContract.ProductCategoryInteractionListener) getParentFragment()).replaceWebViewFragment(shoppingProducts.getProductPageUrl(), shoppingProducts.getDisplayName());

                }else if(v.getTag() instanceof String){
                    String productPageUrl=(String)v.getTag();
                    ((HomeFragmentContract.ProductCategoryInteractionListener) getParentFragment()).replaceWebViewFragment(productPageUrl,getString(R.string.recently_ordered_title));

                }
            }
        });

        mPresenter=new WidgetPresenter(this);

        setHasOptionsMenu(true);
        //start listening for optionsMenuAction
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION);
        intentFilter.addAction(Constants.SECURITY_STATUS_RECEIVED);
        registerIntent(intentFilter, getContext());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
        setUpWidgetList();
        //check if we have a securityStatus lock
        if(! mPreferencesHelper.shouldWaitForSecurityStatus()) {
            mPresenter.start();
        }
    }



    private void setUpWidgetList() {
        mWidgetRecyclerView.setAdapter(mWidgetListAdapter);
        mWidgetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWidgetRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void updateWidgetData(List<Object> widgetData) {
        mWidgetListAdapter.setData(widgetData);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(List<Object> widgetData) {
        hideRetryView();
        updateWidgetData(widgetData);
    }

    @Override
    public void onAddCartError(String errorMessage) {
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.displayCrouton(getActivity(), errorMessage, mContainerLayout);
      //  Snackbar.make(mWidgetRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void addToCartSuccess() {
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
            ((HomeActivity)getActivity()).updateCartMenuItemCount();
          ((HomeActivity) getActivity()).getViewPager().setCurrentItem(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startWebView(Bundle bundle) {
        HomeFragment homeFragment = (HomeFragment) getParentFragment();
        homeFragment.replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.home_root_fragment_container);
    }

    @Override
    public void setPresenter(WidgetContract.Presenter presenter) {

    }

    @Override
    public void onDestroyView() {
        deregisterIntent(getContext());
        super.onDestroyView();
    }

    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {
        checkAndSetHasOptionsMenu(intent, HomeRootFragment.class.getName());
        //call the API if we have released the security status lock
        if(intent.getAction().equals(Constants.SECURITY_STATUS_RECEIVED)){
            showProgress();
            mPresenter.start();
        }
        super.onReceivedBroadcast(context, intent);
    }

    @Override
    protected boolean showErrorLayout() {
        return super.showErrorLayout();
    }

    @Override
    protected boolean hideErrorLayout() {
        return super.hideErrorLayout();
    }

    @Override
    protected void onRetryButtonClicked(View view) {
        super.onRetryButtonClicked(view);
        hideRetryView();
        if (mPresenter != null){
            mPresenter.start();
        }
    }

    @Override
    public void showRetryView() {
        showErrorLayout();
    }

    @Override
    public void hideRetryView() {
        hideErrorLayout();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        message = message.equals(Utils.TIME_OUT) ? getString(R.string.internet_not_available) : message;
        mProgressBar.setVisibility(View.GONE);
        if (span) {
            Utils.displayCrouton(getActivity(), (Spanned) message, mContainerLayout);
        } else {
            Utils.displayCrouton(getActivity(), (String) message, mContainerLayout);
        }
    }

    public void openWebViewOnBanner(String url){
        ((HomeFragmentContract.ProductCategoryInteractionListener) getParentFragment()).replaceBannerView(url);

    }
}
