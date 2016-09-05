package com.petmeds1800.ui.pets;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.AddPetRequest;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.CommonDialogFragment;
import com.petmeds1800.ui.fragments.dialog.GenderDialogFragment;
import com.petmeds1800.ui.pets.presenter.AddPetPresenter;
import com.petmeds1800.ui.pets.support.AddPetContract;
import com.petmeds1800.ui.pets.support.UpdateImageUtil;
import com.petmeds1800.util.GeneralPreferencesHelper;
import com.petmeds1800.util.Utils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/23/2016.
 */
public class AddPetFragment extends AbstractFragment implements View.OnClickListener, GenderDialogFragment.GenderSetListener,CommonDialogFragment.ValueSelectedListener,AddPetContract.View {
    private static final int AGE_REQUEST = 1;
    @BindView(R.id.pet_gender_edit)
    EditText mPetGenderText;
    @BindView(R.id.pet_birthday_edit)
    EditText mPetBirthdayText;
    @BindView(R.id.pet_age_edit)
    EditText mPetAgeText;
    @BindView(R.id.pet_picture_edit)
    EditText mPetPictureText;
    UpdateImageUtil updateImageUtil;
    @BindView(R.id.pet_picture_image)
    ImageView mPetImage;
    @BindView(R.id.pet_name_edit)
    EditText mPetNameText;
    @BindView(R.id.owner_name_edit)
    EditText mOwnerNameText;
    @BindView(R.id.pet_type_edit)
    EditText mPetTypeText;
    @BindView(R.id.breed_type_edit)
    EditText mBreedTypeText;

    @BindView(R.id.pet_weight_edit)
    EditText mPetWeight;
    private ArrayList<Integer> medConditionIds;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;

    @BindView(R.id.petNameInputLayout)
    TextInputLayout mPetNameInputLayout;
    @BindView(R.id.ownerNameInputLayout)
    TextInputLayout mOwnerNameInputLayout;
    @BindView(R.id.petTypeInputLayout)
    TextInputLayout mPetTypeInputLayout;
    @BindView(R.id.breedInputLayout)
    TextInputLayout mBreedInputLayout;
    @BindView(R.id.genderInputLayout)
    TextInputLayout mGenderInputLayout;
    @BindView(R.id.ageInputLayout)
    TextInputLayout mAgeInputLayout;
    @BindView(R.id.weightInputLayout)
    TextInputLayout mWeightInputLayout;
    @BindView(R.id.birthdayInputLayout)
    TextInputLayout mBirthdayInputLayout;
    private AddPetContract.Presenter mPresenter;
    long birthdayInMillis;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);
        ButterKnife.bind(this, view);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_add_pet));
        ((AbstractActivity) getActivity()).enableBackButton();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new AddPetPresenter(this);
        setHasOptionsMenu(true);
        PetMedsApplication.getAppComponent().inject(this);

        mPetGenderText.setOnClickListener(this);
        mPetBirthdayText.setOnClickListener(this);
        mPetAgeText.setOnClickListener(this);
        mPetPictureText.setOnClickListener(this);
        updateImageUtil = UpdateImageUtil.getInstance(this);

        //Todo remove this code after api integration
        medConditionIds = new ArrayList<>();
        medConditionIds.add(7);
        medConditionIds.add(21);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pet_gender_edit:
                FragmentManager fm = getFragmentManager();
                GenderDialogFragment dialogFragment = new GenderDialogFragment();
                dialogFragment.setGenderSetListener(this);
                dialogFragment.show(fm);
                break;
            case R.id.pet_birthday_edit:
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(listener).setMaxDate(Calendar.getInstance().getTime())
                        .setInitialDate(new Date()).setIndicatorColor(getActivity().getResources().getColor(R.color.pattern_blue))
                        .build()
                        .show();
                break;
            case R.id.pet_age_edit:
                FragmentManager fragManager = getFragmentManager();
                CommonDialogFragment commonDialogFragment = CommonDialogFragment.newInstance(getActivity().getResources().getStringArray(R.array.age_range), getActivity().getString(R.string.choose_range_txt), AGE_REQUEST);
                commonDialogFragment.setValueSetListener(this);
                commonDialogFragment.show(fragManager);

                break;
            case R.id.pet_picture_edit:
                showImageOptions();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onGenderSet(String gender) {
        mPetGenderText.setText(gender);
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            birthdayInMillis = date.getTime();
            mPetBirthdayText.setText(Utils.changeDateFormat(date.getTime(), "MMM dd, yyyy"));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };

    @Override
    public void onValueSelected(String value , int requestCode) {

        switch (requestCode){
            case AGE_REQUEST:
                mPetAgeText.setText(value);
                break;
        }

    }

    private void showImageOptions() {
        final CharSequence[] items = {
                getString(R.string.takePhotoTxt), getString(R.string.chooseFromGalleryTxt)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.selectPetTitle));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: {
                        updateImageUtil.updateProfilePic(UpdateImageUtil.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                    }
                    break;
                    case 1: {
                        updateImageUtil.updateProfilePic(UpdateImageUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    }
                    break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File mUri;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case UpdateImageUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                    updateImageUtil.onActivityResult(requestCode, resultCode, data);
                    break;
                case UpdateImageUtil.GALLERY_CAPTURE_IMAGE_REQUEST_CODE:
                    updateImageUtil.onActivityResult(requestCode, resultCode, data);
                    break;
                case Crop.REQUEST_CROP:
                    handleCrop(resultCode, data);
                    break;
                default:
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

    protected void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Uri finalUri = Crop.getOutput(result);
            if (finalUri != null) {

                Glide.with(this).load(finalUri.toString()).asBitmap() .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).centerCrop().into(new BitmapImageViewTarget(mPetImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mPetImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_a_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {

            boolean isValidPetName;
            boolean isValidOwnerName;
            boolean isValidPetType;
            boolean isValidBreedType;
            boolean isValidGender;
            boolean isValidWeight;
            boolean isValidAge;

            isValidPetName = checkAndShowError(mPetNameText, mPetNameInputLayout, R.string.error_petname);
            isValidOwnerName = checkAndShowError(mOwnerNameText, mOwnerNameInputLayout, R.string.error_petowner);
            isValidPetType = checkAndShowError(mPetTypeText, mPetTypeInputLayout, R.string.error_pettype);
            isValidBreedType = checkAndShowError(mBreedTypeText, mBreedInputLayout, R.string.error_petbreed);
            isValidGender = checkAndShowError(mPetGenderText, mGenderInputLayout, R.string.error_petgender);
            isValidWeight = checkAndShowError(mPetWeight, mWeightInputLayout, R.string.error_validweight);
            isValidAge = checkAndShowError(mPetAgeText, mAgeInputLayout, R.string.error_petage, mPetBirthdayText);

            if (isValidPetName ||
                    isValidOwnerName ||
                    isValidPetType ||
                    isValidBreedType ||
                    isValidGender ||
                    isValidWeight ||
                    isValidAge)
                return super.onOptionsItemSelected(item);
        }
        //Todo Remove all hardcoded value after api integration
        progressBar.setVisibility(View.VISIBLE);
        AddPetRequest addPetRequest = new AddPetRequest(mPetNameText.getText().toString()
                , mOwnerNameText.getText().toString(),
                mPetTypeText.getText().toString().toLowerCase(),
                mBreedTypeText.getText().toString(),
                mPetGenderText.getText().toString().toLowerCase(),
                mPetWeight.getText().toString(),
                "5",
                Utils.changeDateFormat(birthdayInMillis, "MM/dd/yyyy"),
                "yes",
                "dog two allergy info",
                "Albon",
                "Amoxicillin",
                "",
                "Antirobe",
                medConditionIds,
                "dog two other info",
                "Cefa-Drops",
                "Cephalexin",
                "",
                "Chlorhexidine",
                mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
        mPresenter.addPetData(addPetRequest);


        return super.onOptionsItemSelected(item);
    }

    @Override
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
        return isAdded();
    }

    @Override
    public void onSuccess() {
        progressBar.setVisibility(View.GONE);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void onError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(mPetWeight, errorMessage, Snackbar.LENGTH_LONG).show();

    }

    public boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId, EditText birthdayText) {
        if (auditEditText.getText().toString().isEmpty() && birthdayText.getText().toString().isEmpty()) {
            auditTextInputLayout.setError(getContext().getString(errorStringId));
            return true;
        } else {
            auditTextInputLayout.setError(null);
            auditTextInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    @Override
    public void setPresenter(AddPetContract.Presenter presenter) {

    }


}
