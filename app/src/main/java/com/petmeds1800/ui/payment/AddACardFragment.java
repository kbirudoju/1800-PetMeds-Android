package com.petmeds1800.ui.payment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.petmeds1800.R;
import com.petmeds1800.model.Card;
import com.petmeds1800.ui.fragments.AbstractFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhinav on 13/8/16.
 */
public class AddACardFragment extends AbstractFragment implements AddACardContract.View {


    @BindView(R.id.nameOnCard_edit)
    EditText mNameOnCardEdit;
    @BindView(R.id.cardNumber_edit)
    EditText mCardNumberEdit;
    @BindView(R.id.expirationDate_edit)
    EditText mExpirationDateEdit;
    @BindView(R.id.cvv_edit)
    EditText mCvvEdit;
    @BindView(R.id.defaultPayment_switch)
    Switch mDefaultPaymentSwitch;

    private AddACardContract.Presenter mPresenter;
    private AlertDialog mAlertDialog;

    private static final int DISMISS_APPROVAL_DIALOG = 1;
    private static final long APPROVAL_DIALOG_DURATION = 1000;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddACardPresenter(this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_a_card, container, false);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {

            String nameOnCard = mNameOnCardEdit.getText().toString();
            String cardNumber = mCardNumberEdit.getText().toString();
            String expirationDate = mExpirationDateEdit.getText().toString();
            String cvv = mCvvEdit.getText().toString();
            boolean isDefaultPayment = mDefaultPaymentSwitch.isChecked();

            Card card = new Card(nameOnCard, cardNumber, expirationDate, isDefaultPayment);
            mPresenter.saveCard(card);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void paymentMethodApproved() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.cardSavedInAccount).setCancelable(false);
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
        mHandler.sendEmptyMessageDelayed(DISMISS_APPROVAL_DIALOG,APPROVAL_DIALOG_DURATION);
    }

    @Override
    public void paymentMethodDisapproved() {

    }

    @Override
    public void setPresenter(AddACardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == DISMISS_APPROVAL_DIALOG){
                mAlertDialog.dismiss();
                popBackStack();
            }
            return false;
        }
    });
}
