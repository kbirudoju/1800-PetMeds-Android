package com.petmeds1800.ui.checkout.stepfour;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.SavePetVetRequest;
import com.petmeds1800.model.shoppingcart.CommerceItems;
import com.petmeds1800.model.shoppingcart.ShoppingCartListResponse;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.checkout.stepfour.presenter.StepFourRootContract;
import com.petmeds1800.ui.checkout.stepfour.presenter.StepFourRootPresenter;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.util.GeneralPreferencesHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/27/2016.
 */
public class StepFourRootFragment extends AbstractFragment implements View.OnClickListener,StepFourRootContract.View{
    ShoppingCartListResponse shoppingCartObj;
    @BindView(R.id.review_submit_button)
    Button mReviewSubmitButton;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    private StepFourRootContract.Presenter mPresenter;

    public static StepFourRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse) {
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
        StepFourRootFragment fragment = new StepFourRootFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_step_two, container, false);
        ButterKnife.bind(this,view);
        mReviewSubmitButton.setOnClickListener(this);
        PetMedsApplication.getAppComponent().inject(this);
        mPresenter=new StepFourRootPresenter(this);
        replaceStepRootChildFragmentWithTag(PetVetInfoFragment.newInstance(shoppingCartObj), R.id.pet_vet_container, PetVetInfoFragment.class.getSimpleName());
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communication_container);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle =getArguments();
        if(bundle!=null){
            shoppingCartObj=(ShoppingCartListResponse)bundle.getSerializable(CartFragment.SHOPPING_CART);
        }
    }

    @Override
    public void onClick(View v) {
        PetVetInfoFragment fragment = (PetVetInfoFragment) getChildFragmentManager().findFragmentByTag(PetVetInfoFragment.class.getSimpleName());
        fragment.shoppingCart.getCommerceItems().size();
        ArrayList<String> commerceItemIds=new ArrayList<>();
        ArrayList<String> petIds=new ArrayList<>();
        ArrayList<String> vetIds=new ArrayList<>();

        for(CommerceItems commerceItem: fragment.shoppingCart.getCommerceItems()){
            commerceItemIds.add(commerceItem.getCommerceItemId());
            if(commerceItem.getPetId()!=null && !commerceItem.getPetId().isEmpty()) {
                petIds.add(commerceItem.getPetId());
            }
            if(commerceItem.getVetId()!=null && !commerceItem.getVetId().isEmpty()) {
                vetIds.add(commerceItem.getVetId());
            }
        }
        //create request for add pet and vet info to cart
        SavePetVetRequest savePetVetRequest=new SavePetVetRequest(true,fragment.mMailOption,commerceItemIds,petIds,vetIds,mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
       mPresenter.applyPetVetInfo(savePetVetRequest);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess() {
        Snackbar.make(mReviewSubmitButton, "Success", Snackbar.LENGTH_LONG).show();    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void setPresenter(StepFourRootContract.Presenter presenter) {

    }
}
