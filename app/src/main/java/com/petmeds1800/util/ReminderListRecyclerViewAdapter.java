package com.petmeds1800.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.RefillReminderSortingperPet;
import com.petmeds1800.model.refillreminder.response.OrderItems;
import com.petmeds1800.model.refillreminder.response.ReminderMonth;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sarthak on 10/20/2016.
 */

public class ReminderListRecyclerViewAdapter extends RecyclerView.Adapter<ReminderListAdapterViewHolder> {
    private Context mContext;
    private Handler mHandler;
    private ArrayList<RefillReminderSortingperPet> mRefillReminderSortingperPets;

    public ReminderListRecyclerViewAdapter(Context mContext, Handler mHandler, ArrayList<RefillReminderSortingperPet> refillReminderSortingperPets) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mRefillReminderSortingperPets = refillReminderSortingperPets;
    }

    @Override
    public ReminderListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReminderListAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_reminder_list_each_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ReminderListAdapterViewHolder holder, final int position) {

        final View reminderListEachItemSubView = (LayoutInflater.from(mContext)).inflate(R.layout.view_reminder_list_each_sub_item, null);
        ((TextView) reminderListEachItemSubView.findViewById(R.id.text_for_pet_name)).setText(mContext.getString(R.string.for_tag_pet_name) + " " + mRefillReminderSortingperPets.get(position).getPetName());
        LinearLayout reminderEachSubItemActualContainer = (LinearLayout) reminderListEachItemSubView.findViewById(R.id.reminder_actual_item_container);

        Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + mRefillReminderSortingperPets.get(position).getPetImageURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(((ImageView)reminderListEachItemSubView.findViewById(R.id.image_pet))) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ((ImageView)reminderListEachItemSubView.findViewById(R.id.image_pet)).setImageDrawable(circularBitmapDrawable);
            }
        });

        for (int i = 0 ; i < mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().size() ; i++){
            final View v = (LayoutInflater.from(mContext)).inflate(R.layout.view_reminder_linear_layout_each_item, null);

            Glide.with(mContext).load(mContext.getString(R.string.server_endpoint) + mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(i).getListed_orderItem().getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(((ImageView)v.findViewById(R.id.image_medication))) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ((ImageView)v.findViewById(R.id.image_medication)).setImageDrawable(circularBitmapDrawable);
                }
            });

            //*******************************************If NULL is as Reminder Month***********************************

            if (null == mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(i).getListed_orderItem().getReminderMonth()){
                mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(i).getListed_orderItem().setReminderMonth(new ReminderMonth("","",""));
            }

            //*******************************************If NULL is as Reminder Month***********************************

            ((TextView)v.findViewById(R.id.text_medication_description)).setText(mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(i).getListed_orderItem().getSkuDisplayName());
            ((TextView)v.findViewById(R.id.text_medication_month)).setText(mContext.getString(R.string.for_tag_pet_month) + " " + mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(i).getListed_orderItem().getReminderMonth().getName());
            ((TextView)v.findViewById(R.id.text_medication_quantity)).setText(mContext.getString(R.string.for_tag_pet_quantity_remaining) + " " + mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(i).getListed_orderItem().getQuantity());
            ((TextView)v.findViewById(R.id.text_medication_price)).setText(mContext.getString(R.string.for_tag_pet_price) + "" + mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(i).getListed_orderItem().getSellingPrice());
            final int finalI = i;
            ((FrameLayout) v.findViewById(R.id.frame_edit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = Message.obtain(null, Constants.REFILL_LIST_PER_PET_EDIT);
                    Bundle b = new Bundle();
                    b.putString(Constants.REFILL_LIST_PER_PET_SKUID,mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(finalI).getListed_orderItem().getSkuDisplayName());
                    b.putString(Constants.REFILL_LIST_PER_PET_IMAGE_URL,mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(finalI).getListed_orderItem().getImageUrl());
                    b.putString(Constants.REFILL_LIST_PER_PET_PRICE,mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(finalI).getListed_orderItem().getSellingPrice());
                    b.putString(Constants.REFILL_LIST_PER_PET_MONTH,mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(finalI).getListed_orderItem().getReminderMonth().getName());
                    b.putString(Constants.REFILL_LIST_PER_PET_NAME,mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(finalI).getListed_orderItem().getPetName());
                    b.putString(Constants.REFILL_LIST_PER_PET_QUANTITY_REMAINING,mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(finalI).getListed_orderItem().getQuantity());
                    b.putSerializable(Constants.REFILL_LIST_PER_PET_ITEM_ID,mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(finalI).getListed_orderItem().getItemId());
                    b.putSerializable(Constants.REFILL_LIST_PER_PET_ORDER_ID,mRefillReminderSortingperPets.get(position).getRefillReminderSortingPetcompOrderArraylist().get(finalI).getListed_easyRefillReminder().getOrderId());
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

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            reminderEachSubItemActualContainer.addView(v,lp);
        }
        ((LinearLayout)holder.mPetPlusRemindersContainer).addView(reminderListEachItemSubView);
    }

    @Override
    public int getItemCount() {
        return mRefillReminderSortingperPets.size();
    }
}
