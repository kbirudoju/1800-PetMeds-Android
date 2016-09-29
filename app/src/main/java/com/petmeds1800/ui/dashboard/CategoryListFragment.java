package com.petmeds1800.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.ProductCategory;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.support.HomeFragmentContract;
import com.petmeds1800.util.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pooja on 9/13/2016.
 */
public class CategoryListFragment extends AbstractFragment implements ProductCategoryListContract.View{


    @BindView(R.id.error_layout)
    LinearLayout mErrorLayout;

    @BindView(R.id.error_Text)
    TextView mErrorLabel;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.containerLayout)
    RelativeLayout mContainerLayout;

    @BindView(R.id.productCategories_recyclerView)
    RecyclerView mProductsCategoriesRecyclerView;

    private ProductCategoryAdapter mProductCategoryAdapter;

    private HomeFragmentContract.ProductCategoryInteractionListener mProductCategoryInteractionListener;

    private ProductCategoryPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ProductCategoryPresenter(this , getContext());
        onAttachFragment(getParentFragment());

        PetMedsApplication.getAppComponent().inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_category_list,container,false);
        ButterKnife.bind(this, view);
        mProductCategoryAdapter = new ProductCategoryAdapter(this, getContext());

        showProgress();
        mPresenter.getProductCategories();

        return view;
    }


    void onAttachFragment(Fragment fragment) {
        try {
             mProductCategoryInteractionListener = (HomeFragmentContract.ProductCategoryInteractionListener) fragment;
        }
        catch(ClassCastException e) {
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
        }
        Utils.displayCrouton(getActivity(), (String) message, mContainerLayout);
    }

    @Override
    public void showRetryView(String errorMessage) {
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorLabel.setText(errorMessage);
    }

    @OnClick(R.id.retry_Button)
    public void retry() {
        mErrorLayout.setVisibility(View.GONE);
        showProgress();
        mPresenter.getProductCategories();
    }

    @Override
    public void populateCategoryList(ArrayList<ProductCategory> productCategoryList) {
        mProductsCategoriesRecyclerView.setVisibility(View.VISIBLE);
        mProductCategoryAdapter.setData(productCategoryList);
        setupCardsRecyclerView();
    }

    @Override
    public void startWebView(ProductCategory productCategory) {
        if(mProductCategoryInteractionListener != null) {
            mProductCategoryInteractionListener.startWebViewFragment(productCategory);
        }
    }

    @Override
    public void setPresenter(ProductCategoryListContract.Presenter presenter) {

    }
}
