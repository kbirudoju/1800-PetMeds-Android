package com.petmeds1800.util;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.AlertDailogMultipleChoice;
import com.petmeds1800.model.entities.NameValueData;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Sdixit on 14-09-2016.
 */

public class AlertRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;

    private ArrayList<AlertDailogMultipleChoice> list;

    public AlertRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public static int adpaterPosition = 0;

    public void setListData(ArrayList<AlertDailogMultipleChoice> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        int resource = R.layout.alert_multi_choice;
        v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        viewHolder = new AlertRecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AlertRecyclerViewHolder alertRecyclerViewHolder = (AlertRecyclerViewHolder) holder;
        final AlertDailogMultipleChoice item = (AlertDailogMultipleChoice) getItemAt(position);
        alertRecyclerViewHolder.mMultichoiceText.setText(item.getName());
        alertRecyclerViewHolder.mMultiChoiceCheckBox.setOnCheckedChangeListener(null);
        alertRecyclerViewHolder.mMultiChoiceCheckBox.setChecked(item.isChecked());
        alertRecyclerViewHolder.mMultichoiceText
                .setTextColor((item.isChecked()) ? mContext.getResources().getColor(R.color.radio_text_selected)
                        : mContext.getResources().getColor(R.color.petmeds_blue));
        alertRecyclerViewHolder.mMultiChoiceCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isPressed()) {
                            if (list.size() > 0) {
                                list.get(position).setChecked(isChecked);
                            }
                            alertRecyclerViewHolder.mMultichoiceText
                                    .setTextColor(
                                            (isChecked) ? mContext.getResources().getColor(R.color.radio_text_selected)
                                                    : mContext.getResources().getColor(R.color.petmeds_blue));
                        }
                    }
                });


    }

    public static int getPositionAtAdapter() {
        return adpaterPosition;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public AlertDailogMultipleChoice getItemAt(int position) {
        return list.get(position);
    }

    public ArrayList<AlertDailogMultipleChoice> getItems() {
        return list;
    }

    public ArrayList<String> getCheckedItemsValue() {
        ArrayList<String> list = new ArrayList<String>();
        for (AlertDailogMultipleChoice item : getItems()) {
            if (item.isChecked()) {
                list.add(item.getValue());
            }
        }
        return list;
    }

    public ArrayList<String> getCheckedItemsName() {
        ArrayList<String> list = new ArrayList<String>();
        for (AlertDailogMultipleChoice item : getItems()) {
            if (item.isChecked()) {
                list.add(item.getName());
            }
        }
        return list;
    }

    public ArrayList<NameValueData> getCheckedItems() {
        ArrayList<NameValueData> list = new ArrayList<NameValueData>();
        for (AlertDailogMultipleChoice item : getItems()) {
            if (item.isChecked()) {
                NameValueData data = new NameValueData();
                data.setName(item.getName());
                data.setValue(item.getValue());
                list.add(data);
            }
        }
        return list;
    }

    public static class AlertRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.multichoiceText)
        TextView mMultichoiceText;

        @BindView(R.id.multiChoiceCheckBox)
        CheckBox mMultiChoiceCheckBox;

        public AlertRecyclerViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            adpaterPosition = getAdapterPosition();

        }

    }
}
