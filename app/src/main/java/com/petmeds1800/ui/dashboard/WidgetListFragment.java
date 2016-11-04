package com.petmeds1800.ui.dashboard;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.entities.PetItemList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.dashboard.presenter.WidgetContract;
import com.petmeds1800.ui.dashboard.presenter.WidgetPresenter;
import com.petmeds1800.ui.dashboard.support.WidgetListAdapter;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.HomeRootFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.ui.fragments.HomeFragment;

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

    private WidgetListAdapter mWidgetListAdapter;
    private WidgetContract.Presenter mPresenter;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_widget_list, container, false);
        ButterKnife.bind(this, view);
        mWidgetListAdapter = new WidgetListAdapter(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        mPresenter=new WidgetPresenter(this);

        setHasOptionsMenu(true);
        //start listening for optionsMenuAction
        registerIntent(new IntentFilter(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION), getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
        setUpWidgetList();
        mPresenter.start();
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
        mProgressBar.setVisibility(View.GONE);
        updateWidgetData(widgetData);
    }

    @Override
    public void onError(String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mWidgetRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAddCartError(String errorMessage) {
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Snackbar.make(mWidgetRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void addToCartSuccess() {
        try {
            ((AbstractActivity) getActivity()).stopLoadingGif(getActivity());
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
        checkAndSetHasOptionsMenu(intent , HomeRootFragment.class.getName());
        super.onReceivedBroadcast(context, intent);
    }
}
