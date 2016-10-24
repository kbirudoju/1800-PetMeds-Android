package com.petmeds1800.ui.vet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.UpdateVetRequest;
import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.NoTitleOkDialogFragment;
import com.petmeds1800.ui.vet.presenter.VetDetailPresenter;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/18/2016.
 */
public class VetDetailFragment extends AbstractFragment implements View.OnClickListener,VetDetailContract.View,NoTitleOkDialogFragment.DialogButtonsListener{
    private VetList mVetDetail;
    @BindView(R.id.clinicNameLabel)

    TextView mClinicNameLabel;
    @BindView(R.id.addressLine1_label)

    TextView mAddressLine1label;
    @BindView(R.id.addressLine2_label)

    TextView mAddressLine2Label;
    @BindView(R.id.phone_number_label)

    TextView mPhoneNumberLabel;
    @BindView(R.id.distanceLabel)

    TextView mDistanceLabel;
    @BindView(R.id.map_view)

    ImageView mMapImage;
    @BindView(R.id.request_referral_button)
    Button mRequestReferralButton;

    @BindView(R.id.user_name_edit)
    EditText mVetNameEdit;


    @BindView(R.id.phone_number_edit)
    EditText mPhoneNumberEdit;

    @BindView(R.id.phoneInputLayout)
    TextInputLayout mPhoneNumberInputLayout;

    @BindView(R.id.userNameInputLayout)
    TextInputLayout mUserNameInputLayout;

    @BindView(R.id.containerLayout)
    LinearLayout mContainerLayout;

    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    private VetDetailContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_vet_detail, null);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(false);
        mRequestReferralButton.setOnClickListener(this);
        mMapImage.setOnClickListener(this);
        mPresenter=new VetDetailPresenter(this);
        PetMedsApplication.getAppComponent().inject(this);
        return view;
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item=menu.findItem(R.id.action_map);
        item.setVisible(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVetDetail=(VetList)getArguments().getSerializable("vet_detail");
        ((AbstractActivity) getActivity()).enableBackButton();
        if(mVetDetail!=null){
            ((AbstractActivity) getActivity()).setToolBarTitle(getString(R.string.near_vet_txt) + mVetDetail.getZip());
            mClinicNameLabel.setText(mVetDetail.getClinic());
            mAddressLine1label.setText(mVetDetail.getAddress());
            mAddressLine2Label.setText(mVetDetail.getCity()+" , "+mVetDetail.getState());
            mPhoneNumberLabel.setText(getString(R.string.phone_title_txt)+mVetDetail.getPhone());
            mDistanceLabel.setText(mVetDetail.getDistanceFromZip() + " " + getString(R.string.miles_txt) + " " + getString(R.string.from_txt) + " " + mVetDetail.getZip());

            String latVet = String.valueOf(mVetDetail.getLatitude());
            String lngVet =  String.valueOf(mVetDetail.getLongitude());
            String url = "http://maps.google.com/maps/api/staticmap?center=" + latVet + "," + lngVet + "&zoom=10&size=200x200&markers=color:blue%7Clabel:S%7C11211%7C11206%7C11222&sensor=false";

            Glide.with(this).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(mMapImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);

                    mMapImage.setImageDrawable(circularBitmapDrawable);
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.request_referral_button:

                boolean isValidVetName;
                boolean isValidPhoneNumber;

                isValidVetName = checkAndShowError(mVetNameEdit, mUserNameInputLayout, R.string.error_petname);
                isValidPhoneNumber = checkAndShowError(mPhoneNumberEdit, mPhoneNumberInputLayout, R.string.error_phone_required);

                if (isValidVetName ||
                        isValidPhoneNumber
                        ) {
                    Utils.displayCrouton(getActivity(), getString(R.string.errorMsgForEmail), mContainerLayout);
                    return ;
                }


                UpdateVetRequest updateVetRequest=new UpdateVetRequest(mVetDetail.getId(),mPhoneNumberEdit.getText().toString(),mVetNameEdit.getText().toString(),mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());

                try {
                    ((AbstractActivity) getActivity()).startLoadingGif(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPresenter.requestReferral(updateVetRequest);
                break;
            case R.id.map_view:
                if(mVetDetail!=null){
                    double latitude = mVetDetail.getLatitude();
                    double longitude = mVetDetail.getLongitude();
                    String label = mVetDetail.getClinic();
                    String uriBegin = "geo:" + latitude + "," + longitude;
                    String query = latitude + "," + longitude + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    getActivity(). startActivity(intent);

                }
                break;

        }
    }


    public boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId) {
        if (auditEditText.getText().toString().isEmpty()) {
            auditTextInputLayout.setError(getContext().getString(errorStringId));
            return true;
        } else {
            auditTextInputLayout.setError(null);
            auditTextInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onSuccess() {
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        NoTitleOkDialogFragment dialogFragment = NoTitleOkDialogFragment.newInstance(String.format(getString(R.string.requestReferralMsg),mVetDetail.getClinic()));
      dialogFragment.setPositiveListener(this);
      dialogFragment.show(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onError(String errorMessage) {
        Utils.displayCrouton(getActivity(), errorMessage, mContainerLayout);
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setPresenter(VetDetailContract.Presenter presenter) {

    }

    @Override
    public void onDialogButtonClick(DialogFragment dialog, String buttonName) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }
}
