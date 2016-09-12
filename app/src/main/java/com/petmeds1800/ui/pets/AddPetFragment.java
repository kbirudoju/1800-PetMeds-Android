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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.entities.AddPetRequest;
import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.model.entities.RemovePetRequest;
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

import static com.petmeds1800.R.id.fromGallery;

/**
 * Created by pooja on 8/23/2016.
 */
public class AddPetFragment extends AbstractFragment implements View.OnClickListener, GenderDialogFragment.GenderSetListener, CommonDialogFragment.ValueSelectedListener, AddPetContract.View {
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
    View dialoglayout;
    AlertDialog alertDailogForPicture;
    private boolean isEditable;
    @BindView(R.id.pet_image_layout)
    RelativeLayout editPetImageView;
    @BindView(R.id.add_pet_image_layout)
    RelativeLayout addPetImageView;
    private MenuItem mEditMenuItem;
    private MenuItem mDoneMenuItem;
    private Pets mPet;
    @BindView(R.id.remove_pet_button)
    Button removePetButton;
    @BindView(R.id.edit_pet_image)
    ImageView mEditPetImage;

    public static final int GENDER_REQUEST_CODE=1;
    public static final int REMOVE_PET_REQUEST_CODE=2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);
        ButterKnife.bind(this, view);
        isEditable= getArguments().getBoolean("isEditable");
        mPet = (Pets) getArguments().getSerializable("pet");

        if(isEditable){
            ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_pet_profiles));
            editPetImageView.setVisibility(view.VISIBLE);
            addPetImageView.setVisibility(view.GONE);
            removePetButton.setVisibility(View.VISIBLE);
            setPetData(mPet);
            enableViews(false);

        }else{

            ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.title_add_pet));
            addPetImageView.setVisibility(view.VISIBLE);
            editPetImageView.setVisibility(view.GONE);
            removePetButton.setVisibility(View.GONE);
            enableViews(true);

        }

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
        removePetButton.setOnClickListener(this);
        mEditPetImage.setOnClickListener(this);
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
                Bundle bundle =new Bundle();
                bundle.putStringArray("options",getResources().getStringArray(R.array.gender_array));
                bundle.putString("title", getString(R.string.choose_gender_label));
                bundle.putString("message", "");
                bundle.putString("ok",getString(R.string.dialog_ok_button));
                bundle.putString("cancel",getString(R.string.dialog_cancel_button));
                dialogFragment.setTargetFragment(this, GENDER_REQUEST_CODE);
                dialogFragment.setGenderSetListener(this);
                dialogFragment.setArguments(bundle);
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
            case R.id.takePhoto:
                updateImageUtil.updateProfilePic(UpdateImageUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                alertDailogForPicture.dismiss();
                break;
            case fromGallery:
                updateImageUtil.updateProfilePic(UpdateImageUtil.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                alertDailogForPicture.dismiss();
            case R.id.remove_pet_button:
                FragmentManager fragmentManager = getFragmentManager();
                Bundle removePetBundle =new Bundle();
                removePetBundle.putStringArray("options", getResources().getStringArray(R.array.remove_pet_option));
                removePetBundle.putString("title", getString(R.string.remove_pet_title));
                removePetBundle.putString("message", getString(R.string.remove_pet_message));
                removePetBundle.putString("ok",getString(R.string.label_fingerprint_continue));
                removePetBundle.putString("cancel", getString(R.string.cancelTextOnDialog));
                GenderDialogFragment removePetDialog = new GenderDialogFragment();
                removePetDialog.setTargetFragment(this,REMOVE_PET_REQUEST_CODE);
                removePetDialog.setGenderSetListener(this);
                removePetDialog.setArguments(removePetBundle);
                removePetDialog.show(fragmentManager);
                break;
            case R.id.edit_pet_image:
                showImageOptions();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onGenderSet(GenderDialogFragment fragment ,String gender,int key) {
        if(fragment.getTargetRequestCode()==GENDER_REQUEST_CODE){
            mPetGenderText.setText(gender);
        }else if(fragment.getTargetRequestCode()==REMOVE_PET_REQUEST_CODE){
            RemovePetRequest request= new RemovePetRequest(mPet.getPetId(),String.valueOf(key), mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
            mPresenter.removePet(request);

        }
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
    public void onValueSelected(String value, int requestCode) {

        switch (requestCode) {
            case AGE_REQUEST:
                mPetAgeText.setText(value);
                break;
        }

    }

    private void showImageOptions() {
        dialoglayout = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_for_picture, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.selectPetTitle));
        builder.setView(dialoglayout);
        alertDailogForPicture = builder.create();
        alertDailogForPicture.show();
        TextView takePhoto = (TextView) dialoglayout.findViewById(R.id.takePhoto);
        TextView fromGallery = (TextView) dialoglayout.findViewById(R.id.fromGallery);
        takePhoto.setTextColor(getActivity().getResources().getColor(R.color.petmeds_blue));
        fromGallery.setTextColor(getActivity().getResources().getColor(R.color.petmeds_blue));
        takePhoto.setOnClickListener(this);
        fromGallery.setOnClickListener(this);
       /* builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 1: {
                        updateImageUtil.updateProfilePic(UpdateImageUtil.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                    }
                    break;
                    case 0: {
                        updateImageUtil.updateProfilePic(UpdateImageUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    }
                    break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();*/
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

                if(!isEditable) {
                    Glide.with(this).load(finalUri.toString()).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).centerCrop().into(new BitmapImageViewTarget(mPetImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mPetImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }else{
                    Glide.with(this).load(finalUri.toString()).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).centerCrop().into(new BitmapImageViewTarget(mEditPetImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mEditPetImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_settings, menu);
        mEditMenuItem = menu.findItem(R.id.action_edit);
        mDoneMenuItem = menu.findItem(R.id.action_done);
        if(isEditable){
            enableEditAction();
        }else{
            enableDoneAction();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_edit){
            enableDoneAction();
            enableViews(true);
            return super.onOptionsItemSelected(item);
        }
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
        if(isEditable){
            AddPetRequest addPetRequest = new AddPetRequest(mPet.getPetId(),mPetNameText.getText().toString()
                    , mOwnerNameText.getText().toString(),
                    mPetTypeText.getText().toString().toLowerCase(),
                    mBreedTypeText.getText().toString(),
                    mPetGenderText.getText().toString().toLowerCase(),
                    mPetWeight.getText().toString(),
                    "2",
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
            mPresenter.updatePetData(addPetRequest);
        }else {
            AddPetRequest addPetRequest = new AddPetRequest(mPetNameText.getText().toString()
                    , mOwnerNameText.getText().toString(),
                    mPetTypeText.getText().toString().toLowerCase(),
                    mBreedTypeText.getText().toString(),
                    mPetGenderText.getText().toString().toLowerCase(),
                    mPetWeight.getText().toString(),
                    "2",
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
        }

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

    private void enableDoneAction() {
        mDoneMenuItem.setVisible(true);
        mEditMenuItem.setVisible(false);
    }
    private void enableEditAction() {
        mDoneMenuItem.setVisible(false);
        mEditMenuItem.setVisible(true);
    }

    private void enableViews(boolean isEnable) {
        mPetNameText.setEnabled(isEnable);
        mOwnerNameText.setEnabled(isEnable);
        mPetTypeText.setEnabled(isEnable);
        mBreedTypeText.setEnabled(isEnable);
        mPetGenderText.setEnabled(isEnable);
        mPetAgeText.setEnabled(isEnable);
        mPetWeight.setEnabled(isEnable);
        mPetBirthdayText.setEnabled(isEnable);
        removePetButton.setEnabled(isEnable);
        mEditPetImage.setEnabled(isEnable);
        mPetAgeText.setEnabled(isEnable);
    }

    private void setPetData(Pets pet){
        if (pet != null) {
            mPetNameText.setText(pet.getPetName());
            mOwnerNameText.setText(pet.getOwnerName());
            mPetTypeText.setText(pet.getPetType());
            mBreedTypeText.setText(pet.getBreedType());
            mPetGenderText.setText(pet.getGender());
            mPetWeight.setText(pet.getWeight());
            mPetBirthdayText.setText(pet.getBirthday());
            mPetAgeText.setText(pet.getPetAge().getName());
            Glide.with(getActivity()).load(getActivity().getString(R.string.server_endpoint) + pet.getPictureURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(mEditPetImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mEditPetImage.setImageDrawable(circularBitmapDrawable);
                }
            });

        }
    }
}
