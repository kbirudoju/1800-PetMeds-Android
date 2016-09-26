package com.petmeds1800.ui.checkout;

import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.entities.ShippingMethod;
import com.petmeds1800.model.entities.ShippingMethodsResponse;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ShippingMethodsListAdapter mListAdapter;

    protected List<ShippingMethod> mShippingMethodList = new ArrayList<>();

    private StepTwoContract.Presenter mPresenter;

    @Inject
    PetMedsApiService mApiService;

    public static StepTwoRootFragment newInstance() {
        StepTwoRootFragment f = new StepTwoRootFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
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
    }

    private void updateRecyclerView(List<ShippingMethod> shippingMethodList) {
        if (shippingMethodList != null) {
            mShippingMethodList.clear();
            mShippingMethodList.addAll(shippingMethodList);
            mListAdapter.notifyDataSetChanged();
        }
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
    public void setPresenter(StepTwoContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
