package com.petmeds1800.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.shoppingcart.response.CommerceItems;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sarthak on 9/26/2016.
 */

public class ShoppingCartRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingCartAdapterViewHolder> {

    public static final int TYPE_FOOTER = 0;
    public static final int TYPE_ITEM = 1;

    private ArrayList<CommerceItems> commerceItemsesCollection;
    private View itemFooter;
    private Context mContext;
    private Handler mHandler;

    public ShoppingCartRecyclerViewAdapter(ArrayList<CommerceItems> commerceItemsesCollection, View itemFooter, Context context, Handler handler) {
        this.commerceItemsesCollection = commerceItemsesCollection;
        this.itemFooter = itemFooter;
        this.mContext = context;
        this.mHandler = handler;
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
    public void onBindViewHolder(final ShoppingCartAdapterViewHolder holder, final int position) {

        if (isFooter(position)) {
            return;
        }

        holder.itemTitle.setText(commerceItemsesCollection.get(position).getProductDisplayName());
        holder.itemDescription.setText(commerceItemsesCollection.get(position).getSkuDisplayName());
        holder.itemQuantityDescription.getEditText().setText(commerceItemsesCollection.get(position).getQuantity());
        holder.itemCost.setText(Float.toString(commerceItemsesCollection.get(position).getSellingPrice()));

        Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + commerceItemsesCollection.get(position).getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.itemImage) {

            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.itemImage.setImageDrawable(circularBitmapDrawable);
            }
        });

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = Message.obtain(null,Constants.DELETE_ITEM_REQUEST_SHOPPINGCART);
                Bundle b = new Bundle();
                b.putString(Constants.COMMERCE_ITEM_ID,commerceItemsesCollection.get(position).getCommerceItemId());
                msg.setData(b);

                try {
                    mHandler.sendMessage(msg);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = Message.obtain(null,Constants.CLICK_ITEM_UPDATE_SHOPPINGCART);
                Bundle b = new Bundle();
                b.putString(CommonWebviewFragment.TITLE_KEY,commerceItemsesCollection.get(position).getProductDisplayName());
                b.putString(CommonWebviewFragment.URL_KEY,mContext.getString(R.string.server_endpoint)+commerceItemsesCollection.get(position).getProductPageUrl()+"&review=write");
                msg.setData(b);

                try {
                    mHandler.sendMessage(msg);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
