package com.petmeds1800.ui.fragments.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.model.PetNameSelectionList;
import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.ui.medicationreminders.AddPetNameListener;
import com.petmeds1800.ui.pets.presenter.PetListPresenter;
import com.petmeds1800.ui.pets.support.PetListContract;
import com.petmeds1800.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.petmeds1800.util.Constants.PET_NAME;

/**
 * Created by Sarthak on 10/14/2016.
 */

public class PetNameDialogFragment extends DialogFragment implements PetListContract.View, View.OnClickListener {

    private static final String NO_PET = "No Pet";

    @BindView(R.id.cancel_button)
    Button mCancelButton;

    @BindView(R.id.save_button)
    Button mSaveButton;

    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;

    @BindView(R.id.pet_name_list)
    ListView mPetNameList;

    private View rootView;

    private ArrayList<PetNameSelectionList> listofpets = new ArrayList<>();

    private PetListContract.Presenter mPresenter;

    private Dialog mDialog;

    private String mPetName;

    private AddPetNameListener mAddPetNameListener;

    public static PetNameDialogFragment newInstance(String petName) {
        PetNameDialogFragment petNameDialogFragment = new PetNameDialogFragment();
        if (petName != null) {
            Bundle args = new Bundle();
            args.putString(Constants.PET_NAME, petName);
            petNameDialogFragment.setArguments(args);
        }
        return petNameDialogFragment;
    }

    public void setAddPetNameListener(AddPetNameListener addPetNameListener) {
        mAddPetNameListener = addPetNameListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return mDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_pet_name_layout, container, false);
        mPresenter = new PetListPresenter(this,getActivity());
        mPetName = NO_PET;
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }

    @OnClick({R.id.cancel_button, R.id.save_button})
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_button:
                mDialog.dismiss();
                break;
            case R.id.save_button:
                Intent intent = new Intent();
                intent.putExtra(PET_NAME, mPetName);
                getTargetFragment()
                        .onActivityResult(Constants.DIALOG_REMINDER_PET_NAME_REQUEST, Activity.RESULT_OK, intent);
                mDialog.dismiss();
                break;
        }
    }

    public class CustomAdapter extends ArrayAdapter<PetNameSelectionList> {

        private Context context;

        private ArrayList<PetNameSelectionList> listOfValues;

        public CustomAdapter(Context c, ArrayList<PetNameSelectionList> values) {
            super(c, R.layout.list_pet_name_each_item, values);
            this.context = c;
            this.listOfValues = values;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            final View rowView = inflater.inflate(R.layout.list_pet_name_each_item, parent, false);
            LinearLayout petNameContainer = (LinearLayout) rowView.findViewById(R.id.each_row_pet_name_parent);
            ((TextView) petNameContainer.findViewById(R.id.tv_Main_Pet_Name))
                    .setText(listOfValues.get(position).getPetDetails().getPetName());

            if (listOfValues.get(position).isSelected()) {
                setWidgetProperties(rowView, true, ContextCompat.getColor(getActivity(), R.color.black));
            } else {
                setWidgetProperties(rowView, false, ContextCompat.getColor(getActivity(), R.color.petmeds_blue));
            }

            petNameContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!((RadioButton) rowView.findViewById(R.id.rb_Choice)).isChecked()) {
                        for (int i = 0; i < listofpets.size(); i++) {
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

        private void setWidgetProperties(View rowView, boolean checked, int color) {
            ((RadioButton) rowView.findViewById(R.id.rb_Choice)).setChecked(checked);
            ((TextView) rowView.findViewById(R.id.tv_Main_Pet_Name))
                    .setTextColor(color);
        }
    }

    @Override
    public void updatePetList(List<Pets> petList) {
        String petName = null;
        mProgressbar.setVisibility(View.GONE);
        mPetNameList.setVisibility(View.VISIBLE);
        if (getArguments() != null) {
            petName = getArguments().getString(Constants.PET_NAME);
        }
        for (int i = 0; i < petList.size(); i++) {
            if (petName != null && petList.get(i).getPetName().equalsIgnoreCase(petName)) {
                mPetName = petName;
                listofpets.add(new PetNameSelectionList(petList.get(i), true));
            } else {
                listofpets.add(new PetNameSelectionList(petList.get(i), false));
            }
        }
        Pets pets = new Pets();
        pets.setPetName(NO_PET);
        if (petName == null) {
            listofpets.add(0, new PetNameSelectionList(pets, true));
        }
        CustomAdapter petNameSelectionAdapter = new CustomAdapter(getActivity(), listofpets);
        mPetNameList.setAdapter(petNameSelectionAdapter);
        View footerView;
        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.dialog_pet_name_list_footer, null, false);
        footerView.findViewById(R.id.add_new_pet_footer_parent).setOnClickListener(this);
        mPetNameList.addFooterView(footerView);

    }

    @Override
    public void onError(String errorMessage) {
        mProgressbar.setVisibility(View.GONE);
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
        mDialog.dismiss();
        mAddPetNameListener.openAddPetScreen();
    }

    public void radioButtonClicked(Pets pets) {
        mPetName = pets.getPetName();
    }
}
