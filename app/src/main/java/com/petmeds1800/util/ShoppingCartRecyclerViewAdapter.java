package com.petmeds1800.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.shoppingcart.CommerceItems;

import java.util.ArrayList;

/**
 * Created by Sarthak on 9/26/2016.
 */

public class ShoppingCartRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingCartAdapterViewHolder> {

    public static final int TYPE_FOOTER = 0;
    public static final int TYPE_ITEM = 1;

    ArrayList<CommerceItems> commerceItemsesCollection;
    View itemFooter;
    Context mContext;

    public ShoppingCartRecyclerViewAdapter(ArrayList<CommerceItems> commerceItemsesCollection, View itemFooter, Context context) {
        this.commerceItemsesCollection = commerceItemsesCollection;
        this.itemFooter = itemFooter;
        this.mContext = context;
    }

    @Override
    public ShoppingCartAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchItemView = null;

        if (viewType == TYPE_ITEM) {
            searchItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_each_item_layout, parent, false);
        } else if (viewType == TYPE_FOOTER) {
            searchItemView = itemFooter;
        }
        return new ShoppingCartAdapterViewHolder(searchItemView,viewType);
    }

    @Override
    public void onBindViewHolder(final ShoppingCartAdapterViewHolder holder, int position) {

        if (isFooter(position)) {
            return;
        }



        CommerceItems local_CommerceItems = commerceItemsesCollection.get(position);
        Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + local_CommerceItems.getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.itemImage) {

            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.itemImage.setImageDrawable(circularBitmapDrawable);
            }
        });

    }

    @Override
    public int getItemCount() {
        return commerceItemsesCollection.size()+1;
    }

    public boolean isFooter(int position) {
        return position == commerceItemsesCollection.size();
    }


    @Override
    public int getItemViewType(int position) {
        return isFooter(position) ?
                TYPE_FOOTER : TYPE_ITEM;
    }
}
