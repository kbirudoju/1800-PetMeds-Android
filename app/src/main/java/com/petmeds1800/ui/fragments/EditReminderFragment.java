package com.petmeds1800.ui.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.petmeds1800.R;
import com.petmeds1800.model.refillreminder.request.RemoveRefillReminderRequest;
import com.petmeds1800.model.refillreminder.request.UpdateRefillReminderRequest;
import com.petmeds1800.model.refillreminder.response.MonthSelectListResponse;
import com.petmeds1800.model.shoppingcart.response.Status;
import com.petmeds1800.ui.fragments.dialog.CommonDialogFragment;
import com.petmeds1800.ui.refillreminder.EditReminderContract;
import com.petmeds1800.ui.refillreminder.ReminderListContract;
import com.petmeds1800.ui.refillreminder.presenter.EditReminderPresenter;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.petmeds1800.R.id.image_medication;
import static com.petmeds1800.R.id.pet_month_layout;
import static com.petmeds1800.R.id.pet_name_layout;
import static com.petmeds1800.R.id.price_input_layout;
import static com.petmeds1800.R.id.quantity_remaining_layout;
import static com.petmeds1800.R.id.remove_reminder_button;
import static com.petmeds1800.R.id.text_medication_description;
import static com.petmeds1800.util.Constants.HIDE_PROGRESSBAR_OR_ANIMATION;
import static com.petmeds1800.util.Constants.SHOW_PROGRESSBAR_OR_ANIMATION;
import static com.petmeds1800.util.Utils.toggleGIFAnimantionVisibility;
import static com.petmeds1800.util.Utils.toggleProgressDialogVisibility;

/**
 * Created by Sarthak on 22-Oct-16.
 */

public class EditReminderFragment extends AbstractFragment implements View.OnTouchListener, View.OnClickListener, EditReminderContract.View{

    @BindView(price_input_layout)
    TextInputLayout mEditPrice;

    @BindView(quantity_remaining_layout)
    TextInputLayout mEditQuantityRemaining;

    @BindView(pet_name_layout)
    TextInputLayout mEditPetName;

    @BindView(pet_month_layout)
    TextInputLayout mEditReminderMonth;

    @BindView(image_medication)
    ImageView mImageMedication;

    @BindView(text_medication_description)
    TextView mDescriptionMedication;

    @BindView(remove_reminder_button)
    Button mRemoveReminderButton;


    private String mOrderID;
    private String mItemID;
    private EditReminderContract.Presenter mPresenter;
    private ProgressBar mProgressBar;
    private String mReminderMonthID;
    private int CALANDER_SELECTION_CODE = 7012;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_reminder, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        ButterKnife.bind(this, view);
        return view;
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
            if (null == mReminderMonthID || mEditReminderMonth.getEditText().toString().equalsIgnoreCase(getArguments().getString(Constants.REFILL_LIST_PER_PET_MONTH))){
                getActivity().onBackPressed();
            } else {
                callReminderAPI(new UpdateRefillReminderRequest(null,mOrderID,mItemID,mReminderMonthID));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValues();
        mEditReminderMonth.getEditText().setOnTouchListener(this);
        mRemoveReminderButton.setOnClickListener(this);
    }

    private void initValues(){
        if (getArguments() != null){
            if (getArguments().containsKey(Constants.REFILL_LIST_PER_PET_PRICE) && !getArguments().getString(Constants.REFILL_LIST_PER_PET_PRICE).isEmpty()){
                mEditPrice.getEditText().setText(getArguments().getString(Constants.REFILL_LIST_PER_PET_PRICE));
            }
            if (getArguments().containsKey(Constants.REFILL_LIST_PER_PET_SKUID) && !getArguments().getString(Constants.REFILL_LIST_PER_PET_SKUID).isEmpty()){
                mDescriptionMedication.setText(getArguments().getString(Constants.REFILL_LIST_PER_PET_SKUID));
            }
            if (getArguments().containsKey(Constants.REFILL_LIST_PER_PET_IMAGE_URL) && !getArguments().getString(Constants.REFILL_LIST_PER_PET_IMAGE_URL).isEmpty()){
                Glide.with(getActivity()).load(getActivity().getString(R.string.server_endpoint) + getArguments().getString(Constants.REFILL_LIST_PER_PET_IMAGE_URL)).asBitmap().centerCrop().into(new BitmapImageViewTarget(mImageMedication) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        (mImageMedication).setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
            if (getArguments().containsKey(Constants.REFILL_LIST_PER_PET_MONTH) && !getArguments().getString(Constants.REFILL_LIST_PER_PET_MONTH).isEmpty()){
                mEditReminderMonth.getEditText().setText(getArguments().getString(Constants.REFILL_LIST_PER_PET_MONTH));
            }

            if (getArguments().containsKey(Constants.REFILL_LIST_PER_PET_NAME) && !getArguments().getString(Constants.REFILL_LIST_PER_PET_NAME).isEmpty()){
                mEditPetName.getEditText().setText(getArguments().getString(Constants.REFILL_LIST_PER_PET_NAME));
            }
            if (getArguments().containsKey(Constants.REFILL_LIST_PER_PET_QUANTITY_REMAINING) && !getArguments().getString(Constants.REFILL_LIST_PER_PET_QUANTITY_REMAINING).isEmpty()){
                mEditQuantityRemaining.getEditText().setText(getArguments().getString(Constants.REFILL_LIST_PER_PET_QUANTITY_REMAINING));
            }
            if (getArguments().containsKey(Constants.REFILL_LIST_PER_PET_ITEM_ID) && !getArguments().getString(Constants.REFILL_LIST_PER_PET_ITEM_ID).isEmpty()){
                mItemID = (getArguments().getString(Constants.REFILL_LIST_PER_PET_ITEM_ID));
            }
            if (getArguments().containsKey(Constants.REFILL_LIST_PER_PET_ORDER_ID) && !getArguments().getString(Constants.REFILL_LIST_PER_PET_ORDER_ID).isEmpty()){
                mOrderID = (getArguments().getString(Constants.REFILL_LIST_PER_PET_ORDER_ID));
            }
        }
    }

    @Override
    public void onDestroyView() {
        if(null != mEditReminderMonth){
            mEditReminderMonth.setOnTouchListener(null);
            mEditReminderMonth = null;
        }
        if(null != mRemoveReminderButton){
            mRemoveReminderButton.setOnClickListener(null);
            mRemoveReminderButton = null;
        }

        toggleProgressDialogVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,mProgressBar);
        toggleGIFAnimantionVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,getActivity());
        super.onDestroyView();
    }

    private void callReminderAPI(Object object){
        if (mPresenter == null){
            mPresenter = new EditReminderPresenter(this);
        }
        if (object == null)
        {
            toggleProgressDialogVisibility(SHOW_PROGRESSBAR_OR_ANIMATION,mProgressBar);
            mPresenter.getGeneralPopulateRefillReminderMonthList();
        } else
        if (object instanceof RemoveRefillReminderRequest){
            toggleGIFAnimantionVisibility(SHOW_PROGRESSBAR_OR_ANIMATION,getActivity());
            mPresenter.getRemoveRefillReminder((RemoveRefillReminderRequest)object);
        } else
        if (object instanceof UpdateRefillReminderRequest){
            toggleGIFAnimantionVisibility(SHOW_PROGRESSBAR_OR_ANIMATION,getActivity());
            mPresenter.getUpdateRefillReminder((UpdateRefillReminderRequest)object);
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public boolean postGeneralPopulateRefillReminderMonthList(final MonthSelectListResponse monthSelectListResponse) {

        final ArrayList<String> monthNames = new ArrayList<>();
        int currentMonthIndex = 0;
        for(int i = 0 ; i < monthSelectListResponse.getMonthList().size() ; i++){
            monthNames.add(monthSelectListResponse.getMonthList().get(i).getName());
            if (mEditReminderMonth.getEditText().getText().toString().equalsIgnoreCase(monthSelectListResponse.getMonthList().get(i).getName())){
                currentMonthIndex = Integer.parseInt(monthSelectListResponse.getMonthList().get(i).getValue())-1;
            }
        }

        FragmentManager fragManager = getFragmentManager();
        CommonDialogFragment commonDialogFragment = CommonDialogFragment.newInstance(monthNames.toArray(new String[monthNames.size()]),getString(R.string.set_reminder_date), CALANDER_SELECTION_CODE, currentMonthIndex);
        commonDialogFragment.setValueSetListener(new CommonDialogFragment.ValueSelectedListener() {
            @Override
            public void onValueSelected(String value, int requestCode) {
                if (requestCode == CALANDER_SELECTION_CODE)
                    mEditReminderMonth.getEditText().setText(value);
                    for(int i = 0 ; i < monthSelectListResponse.getMonthList().size() ; i++){
                       if (monthSelectListResponse.getMonthList().get(i).getName().equalsIgnoreCase(value)){
                           mReminderMonthID = monthSelectListResponse.getMonthList().get(i).getValue();
                           break;
                       }
                    }
            }
        });

        toggleProgressDialogVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,mProgressBar);
        toggleGIFAnimantionVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,getActivity());

        commonDialogFragment.show(fragManager);
        return false;
    }

    @Override
    public boolean onError(String errorMessage) {
        toggleProgressDialogVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,mProgressBar);
        toggleGIFAnimantionVisibility(HIDE_PROGRESSBAR_OR_ANIMATION,getActivity());
        return false;
    }

    @Override
    public boolean onSuccessRemoveorUpdate(Status status) {
        getActivity().onBackPressed();
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if ((v.getId() == R.id.pet_month_layout || v.getId() == R.id.edit_pet_month) && event.getAction() == MotionEvent.ACTION_UP){
            callReminderAPI(null);
            return true;
        }
        return false;
    }

    @Override
    public void setPresenter(EditReminderContract.Presenter presenter) {
            mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        AlertDialog alertDialog = Utils.showAlertDailog(getActivity(),getString(R.string.areYouSure),getString(R.string.confirm_reminder_deletion_message), R.style.StyleForNotification)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialogDeleteButton).toUpperCase(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callReminderAPI(new RemoveRefillReminderRequest(null,mOrderID,mItemID));
                    }
                })
                .setNegativeButton(getString(R.string.cancelTextOnDialog).toUpperCase(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }
}
