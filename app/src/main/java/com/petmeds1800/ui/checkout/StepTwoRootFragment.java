package com.petmeds1800.ui.checkout;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.ShippingMethod;
import com.petmeds1800.model.entities.ShippingMethodsRequest;
import com.petmeds1800.model.entities.ShippingMethodsResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Digvijay on 9/23/2016.
 */
public class StepTwoRootFragment extends AbstractFragment
        implements StepTwoContract.View, ShippingMethodsListAdapter.OnItemClickListener {

    @BindView(R.id.recycler_shipping_methods)
    RecyclerView mRecyclerShippingMethods;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.shippingNavigator)
    Button mShippingNavigator;


    private ShippingMethodsListAdapter mListAdapter;

    protected List<ShippingMethod> mShippingMethodList = new ArrayList<>();

    private StepTwoContract.Presenter mPresenter;

    @Inject
    PetMedsApiService mApiService;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    ShippingMethod mShippingMethod;

    private String mStepName;

    private static final int INITIAL_INDEX = 0;

    private CheckOutActivity mCheckOutActivity;

    private ShoppingCartListResponse mShoppingCartListResponse;

    public static StepTwoRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse, String stepName) {
        StepTwoRootFragment f = new StepTwoRootFragment();
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
        args.putString(CheckOutActivity.STEP_NAME, stepName);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new StepTwoPresenter(this);
        mShoppingCartListResponse = (ShoppingCartListResponse) getArguments()
                .getSerializable(CartFragment.SHOPPING_CART);
        mStepName = getArguments().getString(CheckOutActivity.STEP_NAME);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CheckOutActivity) {
            mCheckOutActivity = (CheckOutActivity) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCheckOutActivity = (CheckOutActivity) getActivity();
        if (savedInstanceState == null) {
            mCheckOutActivity.setToolBarTitle(getString(R.string.shipping_method_title));
            mCheckOutActivity.setLastCompletedSteps(mStepName);
            mCheckOutActivity.setActiveStep(mStepName);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping_methods, container, false);
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communicationfragment);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PetMedsApplication.getAppComponent().inject(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerShippingMethods.setLayoutManager(layoutManager);
        mListAdapter = new ShippingMethodsListAdapter(mShippingMethodList);
        mListAdapter.setOnItemClickListener(this);
        mRecyclerShippingMethods.setAdapter(mListAdapter);
        mPresenter.populateShippingMethodsListRecycler();
    }

    @OnClick(R.id.txv_view_shipping_options)
    public void showShippingOptions() {
        mPresenter.showShippingOptions();
    }

    @Override
    public void onItemClick(int position) {
        mShippingMethod = mShippingMethodList.get(position);

    }

    private void updateRecyclerView(List<ShippingMethod> shippingMethodList) {
        if (shippingMethodList != null) {
            mShippingMethodList.clear();
            mShippingMethodList.addAll(shippingMethodList);
            if (shippingMethodList.size() > INITIAL_INDEX) {
                mShippingMethod = mShippingMethodList.get(INITIAL_INDEX);
            }
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
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

    }

    @Override
    public void onSuccessShippingOptions(String htmlResponse) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_shipping_options));
        bundle.putString(CommonWebviewFragment.HTML_DATA, htmlResponse);
        CommonWebviewFragment webViewFragment = new CommonWebviewFragment();
        webViewFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, webViewFragment);
        transaction.addToBackStack(webViewFragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onErrorShippingOptions() {

    }

    @Override
    public void onSuccessShippingMethods(ShippingMethodsResponse shippingMethodsResponse) {
        updateRecyclerView(shippingMethodsResponse.getShippingMethods());
    }

    @Override
    public void onErrorShippingMethods() {

    }

    @Override
    public void onSuccessShippingMethodsApplied(ShoppingCartListResponse shoppingCartListResponse) {
        ((CheckOutActivity) getActivity()).hideProgress();
        ((CheckOutActivity) getActivity()).moveToNext(mStepName, shoppingCartListResponse);

    }

    @Override
    public void onErrorShippingMethodsApplied(String error) {
        ((CheckOutActivity) getActivity()).hideProgress();
    }

    @Override
    public void setPresenter(StepTwoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.shippingNavigator)
    public void onClick() {
        ((CheckOutActivity) getActivity()).showProgress();
        if (mShippingMethod != null) {
            mPresenter.applyShippingMethods(new ShippingMethodsRequest(mShippingMethod.getShippingMethod(),
                    mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber()));
        }
    }
}
