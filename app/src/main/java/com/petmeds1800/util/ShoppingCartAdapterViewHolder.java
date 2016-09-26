package com.petmeds1800.util;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.petmeds1800.R;

/**
 * Created by Sarthak on 9/26/2016.
 */

public class ShoppingCartAdapterViewHolder extends RecyclerView.ViewHolder {

    ImageView itemImage;
    TextView itemTitle;
    TextView itemDescription;
    TextInputLayout itemQuantityDescription;
    TextView itemCost;
    Button editItem;
    Button deleteItem;

    public ShoppingCartAdapterViewHolder(View v, int TYPE) {
        super(v);

        if (TYPE == ShoppingCartRecyclerViewAdapter.TYPE_ITEM){
            itemImage = (ImageView) v.findViewById(R.id.item_main_image);
            itemTitle = (TextView) v.findViewById(R.id.item_main_title);
            itemDescription = (TextView) v.findViewById(R.id.item_main_desc);
            itemQuantityDescription = (TextInputLayout) v.findViewById(R.id.item_quantity_input_layout);
            itemCost = (TextView) v.findViewById(R.id.each_item_cost_txt);
            editItem = (Button) v.findViewById(R.id.button_edit);
            deleteItem = (Button) v.findViewById(R.id.button_remove);
        }
    }
}
