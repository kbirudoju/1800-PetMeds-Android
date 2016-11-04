package com.petmeds1800.ui.checkout.stepfour;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.SavePetVetRequest;
import com.petmeds1800.model.shoppingcart.response.CommerceItems;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.ui.checkout.CheckOutActivity;
import com.petmeds1800.ui.checkout.CommunicationFragment;
import com.petmeds1800.ui.checkout.stepfour.presenter.StepFourRootContract;
import com.petmeds1800.ui.checkout.stepfour.presenter.StepFourRootPresenter;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 9/27/2016.
 */

public class StepFourRootFragment extends AbstractFragment implements View.OnClickListener, StepFourRootContract.View {

    ShoppingCartListResponse shoppingCartObj;

    @BindView(R.id.review_submit_button)
    Button mReviewSubmitButton;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @BindView(R.id.containerLayout)
    RelativeLayout mContainerLayout;

    private StepFourRootContract.Presenter mPresenter;

    private CheckOutActivity activity;

    private String mStepName;

    public boolean isEmpty = false;

    public static StepFourRootFragment newInstance(ShoppingCartListResponse shoppingCartListResponse, String stepName) {
        Bundle args = new Bundle();
        args.putSerializable(CartFragment.SHOPPING_CART, shoppingCartListResponse);
        args.putString(CheckOutActivity.STEP_NAME, stepName);
        StepFourRootFragment fragment = new StepFourRootFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_step_two, container, false);
        ButterKnife.bind(this, view);
        mReviewSubmitButton.setOnClickListener(this);
        PetMedsApplication.getAppComponent().inject(this);
        mPresenter = new StepFourRootPresenter(this);
        replaceStepRootChildFragmentWithTag(PetVetInfoFragment.newInstance(shoppingCartObj), R.id.pet_vet_container,
                PetVetInfoFragment.class.getSimpleName());
        replaceStepRootChildFragment(CommunicationFragment.newInstance(CommunicationFragment.REQUEST_CODE_VALUE),
                R.id.communication_container);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (CheckOutActivity) getActivity();
        if (savedInstanceState == null) {
            activity.setToolBarTitle(getString(R.string.label_pet_vet_title));
            activity.setLastCompletedSteps(mStepName);
            activity.setActiveStep(mStepName);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AnalyticsUtil().trackScreen(getString(R.string.label_pet_vet_title));
        Bundle bundle = getArguments();
        if (bundle != null) {
            shoppingCartObj = (ShoppingCartListResponse) bundle.getSerializable(CartFragment.SHOPPING_CART);
            //temporary line for testing
           /* shoppingCartObj.getShoppingCart().getCommerceItems().get(0).setIsRxItem(true);
            shoppingCartObj.getShoppingCart().getCommerceItems().get(2).setIsRxItem(true);*/
            mStepName = getArguments().getString(CheckOutActivity.STEP_NAME);
        }
    }

    @Override
    public void onClick(View v) {

        PetVetInfoFragment fragment = (PetVetInfoFragment) getChildFragmentManager()
                .findFragmentByTag(PetVetInfoFragment.class.getSimpleName());
        ArrayList<String> commerceItemIds = new ArrayList<>();
        ArrayList<String> petIds = new ArrayList<>();
        ArrayList<String> vetIds = new ArrayList<>();
        for (CommerceItems commerceItem : fragment.mCommerceItem) {
            if (commerceItem.getVetClinic() != null && commerceItem.getPetName() != null) {
                commerceItemIds.add(commerceItem.getCommerceItemId());
                petIds.add(commerceItem.getPetId());
                vetIds.add(commerceItem.getVetId());
            } else {
                isEmpty = true;
                fragment.mAdapter.notifyDataSetChanged();
                break;
            }
        }
        if (!isEmpty) {
            activity.showProgress();
            //create request for add pet and vet info to cart
            SavePetVetRequest savePetVetRequest = new SavePetVetRequest(true, fragment.mMailOption, commerceItemIds,
                    petIds,
                    vetIds, mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
            mPresenter.applyPetVetInfo(savePetVetRequest);
        }
    }



    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(ShoppingCartListResponse response) {
        Snackbar.make(mReviewSubmitButton, "Success", Snackbar.LENGTH_LONG).show();
        activity.hideProgress();
        activity.moveToNext(mStepName, response);


    }

    @Override
    public void onError(String errorMessage) {
      showErrorCrouton(errorMessage,false);

    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        activity.hideProgress();
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }

    @Override
    public void setPresenter(StepFourRootContract.Presenter presenter) {

    }
}
