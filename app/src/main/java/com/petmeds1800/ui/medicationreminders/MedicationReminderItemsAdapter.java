package com.petmeds1800.ui.medicationreminders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.CommerceItems;
import com.petmeds1800.model.entities.MedicationReminderCommerceItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sdixit on 20-10-2016.
 */

public class MedicationReminderItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;

    private View.OnClickListener onClickListener;

    private ArrayList<MedicationReminderCommerceItem> mCommerceItemsList;

    private final String ORDER_TEXT = "Order #";

    private final String HYPHEN_TEXT = " - ";

    public MedicationReminderItemsAdapter(Context context, View.OnClickListener onClickListener) {
        mContext = context;
        this.onClickListener = onClickListener;

    }

    public void setItems(ArrayList<MedicationReminderCommerceItem> commerceItemsList) {
        mCommerceItemsList = commerceItemsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        int resource = R.layout.view_medication_reminder_items;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new MedicationReminderItemsAdapter.MedicationReminderItemsHolder(v);
        v.setOnClickListener(onClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MedicationReminderItemsAdapter.MedicationReminderItemsHolder medicationReminderItemsHolder
                = (MedicationReminderItemsAdapter.MedicationReminderItemsHolder) holder;
        final MedicationReminderCommerceItem item = (MedicationReminderCommerceItem) getItemAt(position);
        medicationReminderItemsHolder.mProductNameLabel.setText(item.getProductName());
        medicationReminderItemsHolder.mItemDescription.setText(item.getSkuName());
        medicationReminderItemsHolder.mOrderDetails
                .setText(ORDER_TEXT + item.getDescription() + HYPHEN_TEXT + item.getSubmittedDate());
        Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + item.getSkuImageUrl()).asBitmap().
                centerCrop().into(new BitmapImageViewTarget(medicationReminderItemsHolder.mProductImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                medicationReminderItemsHolder.mProductImage.setImageDrawable(circularBitmapDrawable);
            }
        });

    }

    public CommerceItems getItemAt(int position) {
        return mCommerceItemsList.get(position);
    }

    @Override
    public int getItemCount() {
        return mCommerceItemsList.size();
    }

    public static class MedicationReminderItemsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image)
        ImageView mProductImage;

        @BindView(R.id.product_name_label)
        TextView mProductNameLabel;

        @BindView(R.id.item_description)
        TextView mItemDescription;

        @BindView(R.id.orderDetails)
        TextView mOrderDetails;

        public MedicationReminderItemsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

}
