package com.petmeds1800.ui.pets;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.pets.presenter.PetListPresenter;
import com.petmeds1800.ui.pets.support.PetListAdapter;
import com.petmeds1800.ui.pets.support.PetListContract;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/22/2016.
 */
public class PetListFragment extends AbstractFragment implements PetListContract.View, OnClickListener {

    @BindView(R.id.pet_list_view)
    RecyclerView mPetRecyclerView;

    private PetListContract.Presenter mPresenter;

    private PetListAdapter mPetListAdapter;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.noPet_layout)
    LinearLayout noPetLayout;

    @BindView(R.id.addPet_button)
    Button mAddPetButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_list, null);
        ButterKnife.bind(this, view);
        mPresenter = new PetListPresenter(this);
        mPetListAdapter = new PetListAdapter(getActivity(), new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mPetRecyclerView.getChildAdapterPosition(v);
                Bundle bundle = new Bundle();
                if (position > -1 && mPetListAdapter.getItemViewType(position) == PetListAdapter.NORMAL_VIEW_TYPE) {
                    Pets pet = mPetListAdapter.getItemAt(position);
                    bundle.putSerializable("pet", pet);
                    bundle.putBoolean("isEditable", true);
                    replaceAccountFragmentWithBundle(new AddPetFragment(), bundle);

                } else {
                    bundle.putBoolean("isEditable", false);
                    replaceAccountFragmentWithBundle(new AddPetFragment(), bundle);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_pet_profiles));
        ((AbstractActivity) getActivity()).enableBackButton();
    }

    private void setPetRecyclerView() {
        mPetRecyclerView.setAdapter(mPetListAdapter);
        mPetRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));       // 2 represts number of column
        mPetRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mAddPetButton.setOnClickListener(this);
        setPetRecyclerView();
        mPresenter.start();
    }

    @Override
    public void updatePetList(List<Pets> petList) {
        progressBar.setVisibility(View.GONE);
        if (petList.size() > 0) {
            noPetLayout.setVisibility(View.GONE);
            mPetRecyclerView.setVisibility(View.VISIBLE);
            mPetListAdapter.setData(petList);


        } else {
            mPetRecyclerView.setVisibility(View.GONE);
            noPetLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(mPetRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(PetListContract.Presenter presenter) {
        mPresenter = presenter;

    }

    public void addPet() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEditable", false);
        replaceAccountFragmentWithBundle(new AddPetFragment(), bundle);
    }

    @Override
    public void onClick(View v) {
        addPet();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_a_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                addPet();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
