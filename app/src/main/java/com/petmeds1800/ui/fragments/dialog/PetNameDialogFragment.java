package com.petmeds1800.ui.fragments.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.petmeds1800.R;
import com.petmeds1800.model.PetNameSelectionList;
import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.ui.pets.presenter.PetListPresenter;
import com.petmeds1800.ui.pets.support.PetListContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarthak on 10/14/2016.
 */

public class PetNameDialogFragment extends DialogFragment implements PetListContract.View, View.OnClickListener{

    private View rootView;
    private ListView mPetNameList;
    private ArrayList<PetNameSelectionList> listofpets = new ArrayList<>();
    private CustomAdapter mPetNameSelectionAdapter;
    private PetListContract.Presenter mPresenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_pet_name_layout, container, false);
        mPetNameList = (ListView) rootView.findViewById(R.id.pet_name_list);
        mPresenter = new PetListPresenter(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }

    public class CustomAdapter extends ArrayAdapter<PetNameSelectionList> {
        private Context context;
        private ArrayList<PetNameSelectionList> listOfValues;

        public CustomAdapter (Context c,ArrayList<PetNameSelectionList> values) {
            super(c,R.layout.list_pet_name_each_item,values);
            this.context = c;
            this.listOfValues = values;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            final View rowView = inflater.inflate(R.layout.list_pet_name_each_item, parent, false);
            LinearLayout petNameContainer = (LinearLayout) rowView.findViewById(R.id.each_row_pet_name_parent);
            ((TextView)petNameContainer.findViewById(R.id.tv_Main_Pet_Name)).setText(listOfValues.get(position).getPetDetails().getPetName());

            if (listOfValues.get(position).isSelected()){
                ((RadioButton)rowView.findViewById(R.id.rb_Choice)).setChecked(true);
                ((TextView)rowView.findViewById(R.id.tv_Main_Pet_Name)).setTextColor(getResources().getColor(R.color.black));
            } else {
                ((RadioButton)rowView.findViewById(R.id.rb_Choice)).setChecked(false);
                ((TextView)rowView.findViewById(R.id.tv_Main_Pet_Name)).setTextColor(getResources().getColor(R.color.petmeds_blue));
            }

            petNameContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!((RadioButton)rowView.findViewById(R.id.rb_Choice)).isChecked()){
                        for (int i = 0 ; i < listofpets.size() ; i++){
                            listOfValues.get(i).setSelected(false);
                        }
                        listOfValues.get(position).setSelected(true);
                        notifyDataSetChanged();
                        radioButtonClicked(listOfValues.get(position).getPetDetails());
                    }
                }
            });
            return rowView;
        }
    }

    @Override
    public void updatePetList(List<Pets> petList) {
        for (int i = 0 ; i < petList.size() ; i++){
            listofpets.add(new PetNameSelectionList(petList.get(i),false));
        }
        Pets pets = new Pets();
        pets.setPetName("No Pet");
        listofpets.add(0,new PetNameSelectionList(pets,true));

        mPetNameSelectionAdapter = new CustomAdapter (getActivity(), listofpets);
        mPetNameList.setAdapter(mPetNameSelectionAdapter);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pet_name_list_footer, null, false);
        footerView.findViewById(R.id.add_new_pet_footer_parent).setOnClickListener(this);
        mPetNameList.addFooterView(footerView);

    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(PetListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(),"Footer Clicked", Toast.LENGTH_LONG).show();
    }

    public void radioButtonClicked(Pets pets){
        Toast.makeText(getActivity(),"Pet Clicked : " + pets.getPetName() + " with pet id : " +  pets.getPetId(), Toast.LENGTH_LONG).show();
    }
}
