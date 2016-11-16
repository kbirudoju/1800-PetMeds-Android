package com.petmeds1800.ui.vet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.checkout.stepfour.PetVetInfoFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.vet.support.VetListAdapter;
import com.petmeds1800.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/12/2016.
 */
public class VetListFragment extends AbstractFragment implements VetListContract.View,View.OnClickListener{
    @BindView(R.id.vet_recycler_view)
    RecyclerView mVetRecyclerView;

    private VetListAdapter mVetListAdapter;
    private VetListContract.Presenter mPresenter;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.noVet_layout)
    LinearLayout noVetView;
    private ArrayList <Vet> mVetList;
    @BindView(R.id.addVet_button)
    Button mAddVetButton;

    @BindView(R.id.container_layout)
    RelativeLayout mContainer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_vet_list,null);
        mPresenter=new VetListPresenter(this);
        mVetList=new ArrayList<>();
        ButterKnife.bind(this,view);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_my_vets));
        ((AbstractActivity) getActivity()).enableBackButton();
        mAddVetButton.setOnClickListener(this);
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
                    if(v.getTag() instanceof Vet) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("vet_info", (Vet) v.getTag());
                        replaceAccountFragmentWithBundle(new EditVetFragment(), bundle);
                    }else{
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + v.getTag()));
                        startActivity(intent);

                    }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {

      getUserDefaultZipCode();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(ArrayList<Vet> vetList) {
        mVetList=vetList;
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
        Utils.displayCrouton(getActivity(), errorMessage, mContainer);
        //Snackbar.make(mVetRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onZipCodeSuccess(String postalCode) {
        progressBar.setVisibility(View.GONE);
        Bundle codeBundle = new Bundle();
        codeBundle.putString(PetVetInfoFragment.ZIPCODE_KEY, postalCode);
        replaceAccountFragmentWithBundleTag(new AddVetFragment(), AddVetFragment.class.getSimpleName(), codeBundle);

    }

    @Override
    public void onZipCodeError() {
        progressBar.setVisibility(View.GONE);
        Utils.displayCrouton(getActivity(), getString(R.string.add_zip_code_msg), mContainer);
    }

    @Override
    public void setPresenter(VetListContract.Presenter presenter) {

    }

    public void setVet(Vet vet) {
        mVetList.add(vet);
        mVetListAdapter.setData(mVetList);
    }

    @Override
    public void onClick(View v) {
        getUserDefaultZipCode();

    }
    private void getUserDefaultZipCode(){
        progressBar.setVisibility(View.VISIBLE);
        mPresenter.getZipCode();
    }
}
