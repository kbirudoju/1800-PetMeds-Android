package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.MedicationReminderItem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.petmeds1800.util.AlertRecyclerViewAdapter.adpaterPosition;

/**
 * Created by Sdixit on 13-10-2016.
 */

public class MedicationRemindersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;

    MedicationReminderItem mMedicationReminderItem;

    ArrayList<MedicationReminderItem> mMedicationReminderItemArrayList;

    MedicationReminderListener mListener;

    public MedicationRemindersAdapter(Context context) {
        mContext = context;
    }

    public void setItems(ArrayList<MedicationReminderItem> medicationReminderItemArrayList) {
        mMedicationReminderItemArrayList = medicationReminderItemArrayList;
        notifyDataSetChanged();
    }

    public void setListener(MedicationReminderListener listener) {
        mListener = listener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        int resource = R.layout.view_medication_reminder_row;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new MedicationRemindersViewHolder(v);
        return viewHolder;
    }

    public MedicationReminderItem getItemAt(int position) {
        return mMedicationReminderItemArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("position of items :::: ", position + ">>>>" + mMedicationReminderItemArrayList.size());
        final MedicationRemindersViewHolder
                medicationRemindersViewHolder = (MedicationRemindersViewHolder) holder;
        final MedicationReminderItem item = (MedicationReminderItem) getItemAt(position);
        medicationRemindersViewHolder.mPetName.setText(item.getPetName());
        medicationRemindersViewHolder.mReminderDescription.setText(item.getScheduleDescription());
        medicationRemindersViewHolder.mReminderName.setText(item.getReminderName());
        medicationRemindersViewHolder.mReminderMonthEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnClickMedicationEdit(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMedicationReminderItemArrayList.size();
    }

    public static class MedicationRemindersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reminderName)
        TextView mReminderName;

        @BindView(R.id.petName)
        TextView mPetName;

        @BindView(R.id.reminderDescription)
        TextView mReminderDescription;

        @BindView(R.id.reminderMonthEdit)
        ImageButton mReminderMonthEdit;

        public MedicationRemindersViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            adpaterPosition = getAdapterPosition();

        }

    }
}
