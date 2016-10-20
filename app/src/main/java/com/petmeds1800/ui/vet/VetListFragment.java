package com.petmeds1800.ui.vet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.vet.support.VetListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/12/2016.
 */
public class VetListFragment extends AbstractFragment implements VetListContract.View{
    @BindView(R.id.vet_recycler_view)
    RecyclerView mVetRecyclerView;

    private VetListAdapter mVetListAdapter;
    private VetListContract.Presenter mPresenter;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.noVet_layout)
    LinearLayout noVetView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_vet_list,null);
        mPresenter=new VetListPresenter(this);
        ButterKnife.bind(this,view);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_my_vets));
        ((AbstractActivity) getActivity()).enableBackButton();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVetListAdapter=new VetListAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag",v.getTag()+">>>>");
                if(v.getTag()!=null){
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("vet_info",(Vet)v.getTag());
                    replaceAccountFragmentWithBundle(new EditVetFragment(), bundle);
                }else{
                    replaceAccountAndAddToBackStack(new FindAVetFragment(), FindAVetFragment.class.getName());

                }

            }
        });
        setHasOptionsMenu(true);
        setUpVetList();
        mPresenter.getVetListData();
    }


    private void setUpVetList() {

        mVetRecyclerView.setAdapter(mVetListAdapter);
        mVetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVetRecyclerView.setHasFixedSize(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_a_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(ArrayList<Vet> vetList) {
        progressBar.setVisibility(View.GONE);
        if(vetList!=null && vetList.size()==0){
            noVetView.setVisibility(View.VISIBLE);
            mVetRecyclerView.setVisibility(View.GONE);
        }else{
            noVetView.setVisibility(View.GONE);
            mVetRecyclerView.setVisibility(View.VISIBLE);
            mVetListAdapter.setData(vetList);
        }

    }

    @Override
    public void onError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(mVetRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(VetListContract.Presenter presenter) {

    }
}
