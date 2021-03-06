package com.petmeds1800.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.petmeds1800.model.ProductCategory;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.HomeRootFragment;
import com.petmeds1800.ui.support.HomeFragmentContract;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/13/2016.
 */
public class CategoryListFragment extends AbstractFragment implements ProductCategoryListContract.View {

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.containerLayout)
    RelativeLayout mContainerLayout;

    @BindView(R.id.productCategories_recyclerView)
    RecyclerView mProductsCategoriesRecyclerView;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    private ProductCategoryAdapter mProductCategoryAdapter;

    private HomeFragmentContract.ProductCategoryInteractionListener mProductCategoryInteractionListener;

    private ProductCategoryPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ProductCategoryPresenter(this);
        onAttachFragment(getParentFragment());

        PetMedsApplication.getAppComponent().inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        ButterKnife.bind(this, view);
        mProductCategoryAdapter = new ProductCategoryAdapter(this, getContext());

        //check if we have a securityStatus lock
        if(! mPreferencesHelper.shouldWaitForSecurityStatus()){
            showProgress();
            mPresenter.getProductCategories();
        }

        setHasOptionsMenu(true);
        //start listening for optionsMenuAction
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION);
        intentFilter.addAction(Constants.SECURITY_STATUS_RECEIVED);
        registerIntent(intentFilter, getContext());

        return view;
    }


    public void onAttachFragment(Fragment fragment) {
        try {
            mProductCategoryInteractionListener = (HomeFragmentContract.ProductCategoryInteractionListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString() + "must implement ProductCategoryInteractionListener");
        }
    }

    private void setupCardsRecyclerView() {
        mProductsCategoriesRecyclerView.setAdapter(mProductCategoryAdapter);
        mProductsCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    public boolean isActive() {
        return isAdded();
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

    @Override
    public void showRetryView() {
        super.showErrorLayout();
    }

    @Override
    public void hideRetryView() {
        super.hideErrorLayout();
    }

    @Override
    protected void onRetryButtonClicked(View view) {
        super.onRetryButtonClicked(view);
        hideRetryView();
        if (mPresenter != null){
            mPresenter.getProductCategories();
        }
    }

    @Override
    public void populateCategoryList(ArrayList<ProductCategory> productCategoryList) {
        hideRetryView();
        mProductCategoryAdapter.setData(productCategoryList);
        setupCardsRecyclerView();
    }

    @Override
    public void startWebView(ProductCategory productCategory) {
        if (mProductCategoryInteractionListener != null) {
            mProductCategoryInteractionListener.startWebViewFragment(productCategory);
        }
    }

    @Override
    public void setPresenter(ProductCategoryListContract.Presenter presenter) {

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
            mPresenter.getProductCategories();
        }

        super.onReceivedBroadcast(context, intent);
    }
}
