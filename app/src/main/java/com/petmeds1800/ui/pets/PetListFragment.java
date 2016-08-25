package com.petmeds1800.ui.pets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.pets.presenter.PetListPresenter;
import com.petmeds1800.ui.pets.support.PetListAdapter;
import com.petmeds1800.ui.pets.support.PetListContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/22/2016.
 */
public class PetListFragment extends AbstractFragment implements PetListContract.View {
    @BindView(R.id.pet_list_view)
    RecyclerView mPetRecyclerView;
    private PetListContract.Presenter mPresenter;
    private PetListAdapter mPetListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_pet_list,null);
        ButterKnife.bind(this, view);
        mPresenter = new PetListPresenter(this);
        mPetListAdapter=new PetListAdapter(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             replaceAndAddToBackStack(new AddPetFragment(),AddPetFragment.class.getName());
            }
        });
        return view;
    }

    private void setPetRecyclerView(){
        mPetRecyclerView.setAdapter(mPetListAdapter);
        mPetRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));       // 2 represts number of column
        mPetRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPetRecyclerView();
        mPresenter.start();
    }

    @Override
    public void updatePetList(List<Pets> petList) {
        mPetListAdapter.setData(petList);
    }

    @Override
    public void setPresenter(PetListContract.Presenter presenter) {
        mPresenter = presenter;

    }
}
