package com.petmeds1800.ui.dashboard;

import com.petmeds1800.R;
import com.petmeds1800.model.ProductCategory;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.petmeds1800.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhinav on 8/4/2016.
 */
public class ProductCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;

    CategoryListFragment mCategoryListFragment;

    private List<ProductCategory> mProductCategories;


    public ProductCategoryAdapter(CategoryListFragment categoryListFragment, Context context) {
        this.mCategoryListFragment = categoryListFragment;
        this.mContext = context;

    }

    public void clearData() {
        if (mProductCategories != null) {
            mProductCategories.clear();
            notifyDataSetChanged();
        }
    }


    public void setData(List<ProductCategory> productCategories) {
        this.mProductCategories = productCategories;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;

        int resource = R.layout.view_product_category;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new CategoryViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("position is", position + ">>>>" + mProductCategories.size());

        CategoryViewHolder orderViewHolder = (CategoryViewHolder) holder;
        final ProductCategory productCategory = getItemAt(position);

        orderViewHolder.mCategoryLabel.setText(productCategory.getName());
        orderViewHolder.mCategoryLabel.setTag(position);

        orderViewHolder.mCategoryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductCategory productCategory = getItemAt((Integer) v.getTag());

                mCategoryListFragment.startWebView(productCategory);

            }
        });

    }

    public ProductCategory getItemAt(int position) {
        return mProductCategories.get(position);
    }

    @Override
    public int getItemCount() {
        if (mProductCategories == null) {
            return 0;
        } else {
            return mProductCategories.size();
        }

    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categoryLabel)
        TextView mCategoryLabel;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
