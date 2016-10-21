package com.petmeds1800.ui.checkout;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.ShippingMethod;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Digvijay on 9/26/2016.
 */
public class ShippingMethodsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShippingMethod> mShippingMethodList;

    private OnItemClickListener mOnItemClickListener;

    private int selectedPosition = 0;

    public ShippingMethodsListAdapter(List<ShippingMethod> shippingMethodList) {
        mShippingMethodList = shippingMethodList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_item_shipping_method, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        if (viewHolder != null) {

            if(selectedPosition == position){
                viewHolder.selectionImage.setImageResource(R.drawable.ic_radio_button_on);
            }else{
                viewHolder.selectionImage.setImageResource(R.drawable.ic_radio_button_off);
            }

            final ShippingMethod shippingMethod = mShippingMethodList.get(position);
            viewHolder.shippingText.setText(shippingMethod.getShippingText());
            viewHolder.shippingPrice.setText(shippingMethod.getShippingPrice());
        }
    }

    @Override
    public int getItemCount() {
        return mShippingMethodList == null ? 0 : mShippingMethodList.size();
    }

    public ShippingMethod getItem(int position) {
        return mShippingMethodList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imv_shipping_selection)
        ImageView selectionImage;

        @BindView(R.id.txv_shipping_text)
        TextView shippingText;

        @BindView(R.id.txv_shipping_price)
        TextView shippingPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

}
