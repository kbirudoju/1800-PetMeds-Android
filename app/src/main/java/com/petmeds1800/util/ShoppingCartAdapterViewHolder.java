package com.petmeds1800.util;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.petmeds1800.R;

/**
 * Created by Sarthak on 9/26/2016.
 */

public class ShoppingCartAdapterViewHolder extends RecyclerView.ViewHolder {

    ImageView itemImage;
    TextView mItemTitle;
    TextView mItemDescription;
    TextInputLayout mItemQuantityDescription;
    TextView mItemCost;
    FrameLayout mEditItemLayout;
    FrameLayout mDeleteItemLayout;
    Spinner mItemQuantitySpinner;

    public ShoppingCartAdapterViewHolder(View v, int TYPE) {
        super(v);

        if (TYPE == ShoppingCartRecyclerViewAdapter.TYPE_ITEM){
            itemImage = (ImageView) v.findViewById(R.id.item_main_image);
            mItemTitle = (TextView) v.findViewById(R.id.item_main_title);
            mItemDescription = (TextView) v.findViewById(R.id.item_main_desc);
            mItemQuantityDescription = (TextInputLayout) v.findViewById(R.id.item_quantity_input_layout);
            mItemCost = (TextView) v.findViewById(R.id.each_item_selling_price);
            mEditItemLayout = (FrameLayout) v.findViewById(R.id.button_edit);
            mDeleteItemLayout = (FrameLayout) v.findViewById(R.id.button_remove);
            mItemQuantitySpinner = (Spinner) v.findViewById(R.id.spinner_item_quantity);
        }
    }
}
