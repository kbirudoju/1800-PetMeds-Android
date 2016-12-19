package com.petmeds1800.ui.checkout.stepfour;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.petmeds1800.R;
import com.petmeds1800.intent.AddNewEntityIntent;
import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.model.shoppingcart.response.CommerceItems;
import com.petmeds1800.model.shoppingcart.response.ShoppingCart;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.checkout.stepfour.presenter.PetVetInfoContract;
import com.petmeds1800.ui.checkout.stepfour.presenter.PetVetInfoPresenter;
import com.petmeds1800.ui.checkout.stepfour.support.PetVetInfoAdapter;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.dialog.CommonDialogFragment;
import com.petmeds1800.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/27/2016.
 */
public class PetVetInfoFragment extends AbstractFragment implements PetVetInfoContract.View,CommonDialogFragment.ValueSelectedListener{

    public static final String ZIPCODE_KEY = "zipcode";

    public static final String PET_KEY = "pet";

    public static final String VET_KEY = "vet";

    private ShoppingCart shoppingCart;

    @BindView(R.id.pet_vet_view)
    RecyclerView mPetVetRecyclerView;

    public PetVetInfoAdapter mAdapter;

    private LinkedHashMap<String, String> mPetList;

    private static final int VET_REQUEST = 1;

    private static final int PET_REQUEST = 2;

    private PetVetInfoContract.Presenter mPresenter;

    private int mPosition;
    // private ArrayList<Vet> mVetList;
    private LinkedHashMap<String,String> mVetList;
    public String mMailOption="N";
    public ArrayList<CommerceItems> mCommerceItem;
    private StepFourRootFragment parentFragment;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;


    public static PetVetInfoFragment newInstance(ShoppingCartListResponse shoppingCartListResponse) {
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
        PetVetInfoFragment fragment = new PetVetInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_vet_information, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new PetVetInfoPresenter(this);
        parentFragment=(StepFourRootFragment)getParentFragment();
        mAdapter= new PetVetInfoAdapter(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = (int) v.getTag();
                switch (v.getId()) {
                    case R.id.pet_name_edit:
                        Log.d("petname", "clicked");
                        if(mPetList!=null && mPetList.size()>0) {
                            showDialog(mPetList, PET_REQUEST, getActivity().getString(R.string.select_pet_title));
                        }else{
                            mProgressBar.setVisibility(View.VISIBLE);
                            mPresenter.getPetListData();
                        }
                        break;
                    case R.id.vet_name_edit:
                        Log.d("vetname", "clicked");
                        if(mVetList!=null && mVetList.size()>0){
                            showDialog(mVetList, VET_REQUEST, getActivity().getString(R.string.select_vet_title));
                        }else{
                            mProgressBar.setVisibility(View.VISIBLE);
                            mPresenter.getVetListData();
                        }
                        break;
                }
            }
        }, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mMailOption="Y";

                }else{
                    mMailOption="N";
                }

                mAdapter.setPrescriptionOption(isChecked);
                mAdapter.notifyDataSetChanged();
            }
        },this);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ShoppingCartListResponse shoppingCartListResponse = (ShoppingCartListResponse)bundle.getSerializable(CartFragment.SHOPPING_CART);
            //Add only those item which is a RxItem
         //  shoppingCartListResponse.getShoppingCart().getCommerceItems().get(0).setIsRxItem(true);
           shoppingCart=shoppingCartListResponse.getShoppingCart();
            mCommerceItem=new ArrayList<CommerceItems>();
            for(CommerceItems commerceItems : shoppingCart.getCommerceItems()){
                if(commerceItems.isRxItem()){
                    mCommerceItem.add(commerceItems);
                }
            }
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setItem();

    }

    private void setupRecyclerView() {
        mPetVetRecyclerView.setAdapter(mAdapter);
        mPetVetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPetVetRecyclerView.setHasFixedSize(true);
        mPetVetRecyclerView.setNestedScrollingEnabled(false);

    }

    private void setItem(){
        if(mCommerceItem!=null && mCommerceItem.size()>0){
            mAdapter.setData(mCommerceItem);
        }
    }

    @Override
    public void setPetList(List<Pets> petList) {
        mProgressBar.setVisibility(View.GONE);
        mPetList = new LinkedHashMap<String, String>();
        for (Pets pet : petList) {
            mPetList.put(pet.getPetId(), pet.getPetName());
        }
        //Add view for add pet with id -1
        mPetList.put("-1", getActivity().getString(R.string.add_new_pet_txt));
        showDialog(mPetList, PET_REQUEST, getActivity().getString(R.string.select_pet_title));


    }

    @Override
    public void onError(String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mPetVetRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setVetList(List<Vet> vetList) {
        mProgressBar.setVisibility(View.GONE);
        mVetList = new LinkedHashMap<String, String>();
        for (Vet vet : vetList) {
            mVetList.put(vet.getId(), vet.getName());
        }

        //Add view for add vet with id -1
        mVetList.put("-1", getActivity().getString(R.string.add_new_vet_txt));
        showDialog(mVetList, VET_REQUEST, getActivity().getString(R.string.select_vet_title));
    }

    @Override
    public void setPresenter(PetVetInfoContract.Presenter presenter) {

    }

    public void showDialog(HashMap<String, String> data, int code, String title) {
        FragmentManager fragManager = getFragmentManager();
        CommonDialogFragment commonDialogFragment = CommonDialogFragment
                .newInstance(data,
                        title, code);
        commonDialogFragment.setValueSetListener(this);
        commonDialogFragment.show(fragManager);
    }

    @Override
    public void onValueSelected(String value, int requestCode) {
        switch (requestCode) {
            case PET_REQUEST:

                if(value.equalsIgnoreCase(getActivity().getString(R.string.add_new_pet_txt))){
                    AddNewEntityIntent addNewEntityIntent = new AddNewEntityIntent(getActivity(), Constants.ADD_NEW_PET_REQUEST);
                    startActivityForResult(addNewEntityIntent, Constants.ADD_NEW_PET_REQUEST);
                }else{
                    mCommerceItem.get(mPosition - 1).setPetName(value);
                    mCommerceItem.get(mPosition - 1).setPetId(getPetVetKey(mPetList, value));
                    parentFragment.isEmpty=false;
                   //mAdapter.notifyItemChanged(mPosition);
                  mAdapter.notifyDataSetChanged();
                }

                break;
            case VET_REQUEST:
                if(value.equalsIgnoreCase(getActivity().getString(R.string.add_new_vet_txt))){
                    AddNewEntityIntent addNewEntityIntent = new AddNewEntityIntent(getActivity(), Constants.ADD_NEW_VET_REQUEST);
                    addNewEntityIntent.putExtra(
                            ZIPCODE_KEY,shoppingCart.getShippingGroups().get(0).getShippingAddress().getPostalCode());
                    startActivityForResult(addNewEntityIntent, Constants.ADD_NEW_VET_REQUEST);

                }else{
                    mCommerceItem.get(mPosition - 1).setVetClinic(value);
                    mCommerceItem.get(mPosition - 1).setVetId(getPetVetKey(mVetList, value));
                    parentFragment.isEmpty=false;
                    //mAdapter.notifyItemChanged(mPosition);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.ADD_NEW_PET_REQUEST)
        {
            if(data!=null) {
                Log.d("onActivityResult", ">>>>");
                Pets pet = (Pets) data.getSerializableExtra(PET_KEY);
                //remove add new pet item and add again to the last position
                mPetList.remove("-1");
                mPetList.put(pet.getPetId(), pet.getPetName());
                mPetList.put("-1", getActivity().getString(R.string.add_new_pet_txt));
                mCommerceItem.get(mPosition - 1).setPetName(pet.getPetName());
                mCommerceItem.get(mPosition-1).setPetId(pet.getPetId());
                parentFragment.isEmpty=false;
               // mAdapter.notifyItemChanged(mPosition);
                mAdapter.notifyDataSetChanged();
            }

        }
        if(requestCode==Constants.ADD_NEW_VET_REQUEST)
        {
            if(data!=null) {
                Log.d("onActivityResult", ">>>>");
                Vet vet = (Vet) data.getSerializableExtra(VET_KEY);
                mVetList.remove("-1");
                mVetList.put(vet.getId(), vet.getName());
                mVetList.put("-1", getActivity().getString(R.string.add_new_vet_txt));
                mCommerceItem.get(mPosition - 1).setVetClinic(vet.getName());
                mCommerceItem.get(mPosition-1).setVetId(vet.getId());
                parentFragment.isEmpty=false;
               // mAdapter.notifyItemChanged(mPosition);
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    private String getPetVetKey(HashMap<String, String> petVetMap, String value) {
        for (String keyset : petVetMap.keySet()) {
            if (petVetMap.get(keyset).equals(value)) {
                return keyset;
            }
        }
        return value;
    }
}
