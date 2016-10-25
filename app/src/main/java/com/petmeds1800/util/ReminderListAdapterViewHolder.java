package com.petmeds1800.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.petmeds1800.R;

/**
 * Created by Sarthak on 10/20/2016.
 */

public class ReminderListAdapterViewHolder extends RecyclerView.ViewHolder {

    LinearLayout mPetPlusRemindersContainer;

    public ReminderListAdapterViewHolder(View itemView) {
        super(itemView);

        mPetPlusRemindersContainer = (LinearLayout) itemView.findViewById(R.id.reminder_each_item_container);
    }
}
