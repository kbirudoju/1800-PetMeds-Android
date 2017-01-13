package com.petmeds1800.ui.checkout.stepfive;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.Item;
import com.petmeds1800.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sdixit on 29-09-2016.
 */

public class ReviewSubmitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;

    private static final int REQUEST_MONTHS = 1;

    private static int adpaterPosition = 0;

    private static int FIRST_INDEX = 1;

    List<Item> mItems;

    private TextView tempLabelForReminderMonth;

    private OpenDailogListener mOpenDailogListener;

    public ReviewSubmitAdapter(Context context) {
        this.mContext = context;
    }

    public ArrayList<String> getCommerceItemsIdList() {
        return commerceItemsIdList;
    }

    private ArrayList<String> commerceItemsIdList = new ArrayList<>();

    public ArrayList<String> getReorderMonthsIdList() {
        return reorderMonthsIdList;
    }

    private ArrayList<String> reorderMonthsIdList = new ArrayList<>();

    private String commerceItemId = null;

    public void setItems(ArrayList<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public interface OpenDailogListener {

        void openDailog(String data[], int code, String title, int defaultValue, TextView textview,
                int selectedPosition, ArrayList<String> reorderMonthsList);

    }

    public void setOpenDailogListener(OpenDailogListener openDailogListener) {
        this.mOpenDailogListener = openDailogListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        int resource = R.layout.view_order_detail_product_row;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        v.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.white));
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
        params.setMargins(0, 2, 0, 0);
        v.setLayoutParams(params);
        viewHolder = new ReviewSubmitViewHolder(v);
        return viewHolder;
    }

    public Item getItemAt(int position) {
        return mItems.get(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("position of items :::: ", position + ">>>>" + mItems.size());
        final ReviewSubmitViewHolder reviewSubmitViewHolder = (ReviewSubmitViewHolder) holder;
        final Item item = (Item) getItemAt(position);
        if (item.isHasUnitLife()) {
            commerceItemId = item.getCommerceItemId();
            commerceItemsIdList.add(commerceItemId);
            reorderMonthsIdList.add(item.getReOrderReminderMonth());
            reviewSubmitViewHolder.mRefillReminderContainer.setVisibility(View.VISIBLE);
            reviewSubmitViewHolder.mReminderMonth.setText(
                    mContext.getResources().getStringArray(R.array.month_names)[Integer
                            .parseInt(item.getReOrderReminderMonth()) - FIRST_INDEX]);

        }

        reviewSubmitViewHolder.mReminderMonthEdit.setTag(commerceItemId);
        reviewSubmitViewHolder.mReminderMonthEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagValue = (String) v.getTag();
                int selectedPosition = commerceItemsIdList.indexOf(tagValue);
                mOpenDailogListener
                        .openDailog(mContext.getResources().getStringArray(R.array.month_names), REQUEST_MONTHS,
                                mContext.getString(R.string.reminder_title), Integer
                                        .parseInt(item.getReOrderReminderMonth()),
                                reviewSubmitViewHolder.mReminderMonth, selectedPosition, reorderMonthsIdList);
            }
        });

        reviewSubmitViewHolder.mProductNameLabel.setText(item.getProductName());
        reviewSubmitViewHolder.mQuantityLabel.setText(String.valueOf(item.getItemQuantity()));
        reviewSubmitViewHolder.mItemDescription.setVisibility(View.VISIBLE);
        reviewSubmitViewHolder.mItemDescription.setText(item.getSkuName());
        reviewSubmitViewHolder.mProductPriceLabel
                .setText(StepFiveRootFragment.DOLLAR_SIGN + String.format(mContext.getResources().getConfiguration().locale ,"%.2f", item.getSellingPrice()));
        Glide.with(mContext).load(item.getItemImageURL()).asBitmap()
                .centerCrop().into(new BitmapImageViewTarget(reviewSubmitViewHolder.mProductImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                reviewSubmitViewHolder.mProductImage.setImageDrawable(circularBitmapDrawable);
            }
        });


    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    public static class ReviewSubmitViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image)
        ImageView mProductImage;

        @BindView(R.id.product_name_label)
        TextView mProductNameLabel;

        @BindView(R.id.item_description)
        TextView mItemDescription;

        @BindView(R.id.quantity_label)
        TextView mQuantityLabel;

        @BindView(R.id.product_price_label)
        TextView mProductPriceLabel;

        @BindView(R.id.reminderMonth)
        TextView mReminderMonth;

        @BindView(R.id.reminderMonthEdit)
        ImageButton mReminderMonthEdit;

        @BindView(R.id.refillReminderContainer)
        RelativeLayout mRefillReminderContainer;

        public ReviewSubmitViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            adpaterPosition = getAdapterPosition();

        }

    }
}
