package com.petmeds1800.ui.checkout.confirmcheckout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.Item;
import com.petmeds1800.util.Log;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Digvijay on 10/13/2016.
 */

public class ReceiptItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> mItemList;

    private Context mContext;
    DecimalFormat df = new DecimalFormat("0.00");

    public ReceiptItemsListAdapter(List<Item> itemList, Context context) {
        mItemList = itemList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_item_order_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReceiptItemsListAdapter.ViewHolder viewHolder = (ReceiptItemsListAdapter.ViewHolder) holder;
        Log.e("position is",position+"onBindViewHolder");
        if (viewHolder != null) {
            Log.e("position is",position+"onBindViewHolder");
            final Item item = mItemList.get(position);
            Picasso.with(mContext).load(item.getItemImageURL()).into(viewHolder.itemImage);
            viewHolder.itemName.setText(item.getProductName());
            viewHolder.itemDescription.setText(item.getSkuName());
            viewHolder.itemPrice.setText(String.valueOf(df.format(item.getPrice())));
            viewHolder.itemQuantity.setText(mContext.getString(R.string.quantity_formatter, item.getItemQuantity()));
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_item)
        ImageView itemImage;

        @BindView(R.id.txv_item_name)
        TextView itemName;

        @BindView(R.id.txv_item_description)
        TextView itemDescription;

        @BindView(R.id.txv_item_price)
        TextView itemPrice;

        @BindView(R.id.txv_item_quantity)
        TextView itemQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
