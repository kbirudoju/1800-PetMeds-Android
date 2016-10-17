package com.petmeds1800.ui.vet.support;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.Vet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/12/2016.
 */
public class VetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    final static int NORMAL_VIEW_TYPE = 1;
    final static int FOOTER_VIEW_TYPE = 2;
    private ArrayList<Vet> vetList;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == NORMAL_VIEW_TYPE) {
            int resource = R.layout.view_vet_list_item;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder = new VetItemViewHolder(v);
        } else {
            int resource = R.layout.view_find_a_vet;
            v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            viewHolder=new FooterViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {

        } else {
            final VetItemViewHolder vetViewHolder = (VetItemViewHolder) holder;
            final Vet vet = (Vet) getItemAt(position);
            vetViewHolder.clinic_name_label.setText(vet.getClinic());
            vetViewHolder.vet_name_label.setText(vet.getName());
            vetViewHolder.phone_number_label.setText(vet.getPhoneNumber());

        }
    }

    public void setData(ArrayList<Vet> vetList) {
        this.vetList = vetList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (vetList == null ) {
            return 0;
        } else {
            return vetList.size()+1;

        }
    }

    @Override
    public int getItemViewType(int position) {
        return ((vetList == null || position >= vetList.size())) ? FOOTER_VIEW_TYPE : NORMAL_VIEW_TYPE;
    }


    public Vet getItemAt(int position) {
        return vetList.get(position);
    }


    public static class VetItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vetNameLabel)
        TextView vet_name_label;
        @BindView(R.id.clinicNameLabel)
        TextView clinic_name_label;
        @BindView(R.id.vetPhoneNumberLabel)
        TextView phone_number_label;
        @BindView(R.id.action_call_view)
        LinearLayout call_action_view;
        @BindView(R.id.edit_action_view)
        LinearLayout edit_action_view;



        public VetItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
    public static class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
