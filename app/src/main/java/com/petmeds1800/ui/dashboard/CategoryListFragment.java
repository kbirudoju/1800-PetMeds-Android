package com.petmeds1800.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.model.ProductCategory;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.support.HomeFragmentContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/13/2016.
 */
public class CategoryListFragment extends AbstractFragment implements ProductCategoryListContract.View{

    @BindView(R.id.productCategories_recyclerView)
    RecyclerView mProductsCategoriesRecyclerView;

    ArrayList<ProductCategory> mProductCategories;

    private ProductCategoryAdapter mProductCategoryAdapter;

    private HomeFragmentContract.ProductCategoryInteractionListener mProductCategoryInteractionListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onAttachFragment(getParentFragment());

        //TODO Need to invoke actuall API once we have a clarification from backend
        //intializa test data
        mProductCategories = new ArrayList<>();
        mProductCategories.add(new ProductCategory("Supplies", "/Supplies-cat17.html"));
        mProductCategories.add(new ProductCategory("Food", "/Food-cat78.html"));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_category_list,container,false);
        ButterKnife.bind(this, view);
        mProductCategoryAdapter = new ProductCategoryAdapter(this, getContext());
        mProductCategoryAdapter.setData(mProductCategories);

        setupCardsRecyclerView();


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
    public boolean isActive() {
        return isAdded();
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
