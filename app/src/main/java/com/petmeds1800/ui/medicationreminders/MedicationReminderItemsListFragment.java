package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.CommerceItems;
import com.petmeds1800.model.entities.MedicationReminderCommerceItem;
import com.petmeds1800.model.entities.OrderList;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.orders.support.DividerItemDecoration;
import com.petmeds1800.util.Utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sdixit on 20-10-2016.
 */

public class MedicationReminderItemsListFragment extends AbstractFragment
        implements MedicationReminderItemListContract.View, Runnable ,EditText.OnEditorActionListener{

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.medicationsreminderitems_recyclerView)
    RecyclerView mMedicationsreminderitemsRecyclerView;

    @BindView(R.id.containerLayout)
    RelativeLayout mContainerLayout;

    @BindView(R.id.itemName_edit)
    EditText mItemNameEdit;

    @BindView(R.id.itemNameLayout)
    TextInputLayout mItemNameLayout;

    @BindView(R.id.selectOrderstxt)
    TextView mSelectOrderstxt;

    MedicationReminderItemListContract.Presenter mOrderPresenter;

    private ArrayList<MedicationReminderCommerceItem> mMedicationReminderCommerceItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    HomeActivity mCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mCallBack = ((HomeActivity) context);
        }
    }

    private void setupCardsRecyclerView() {
        mMedicationsreminderitemsRecyclerView.setVisibility(View.VISIBLE);
        MedicationReminderItemsAdapter medicationReminderItemsAdapter = new MedicationReminderItemsAdapter(
                getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mMedicationsreminderitemsRecyclerView.getChildAdapterPosition(v);
                MedicationReminderCommerceItem medicationReminderCommerceItem = mMedicationReminderCommerceItems
                        .get(position);
                getFragmentManager().popBackStackImmediate();
                mCallBack.setItemDescription(medicationReminderCommerceItem.getProductName(), medicationReminderCommerceItem.getSkuName());


            }
        });
        medicationReminderItemsAdapter.setItems(mMedicationReminderCommerceItems);
        mMedicationsreminderitemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMedicationsreminderitemsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mMedicationsreminderitemsRecyclerView.setHasFixedSize(true);
        mMedicationsreminderitemsRecyclerView.setAdapter(medicationReminderItemsAdapter);


    }

    public static MedicationReminderItemsListFragment newInstance() {
        MedicationReminderItemsListFragment
                medicationReminderListFragment = new MedicationReminderItemsListFragment();
        Bundle args = new Bundle();
        medicationReminderListFragment.setArguments(args);
        return medicationReminderListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medication_reminder_items_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            ((AbstractActivity) getActivity())
                    .setToolBarTitle(getActivity().getString(R.string.items_title));
            ((AbstractActivity) getActivity()).enableBackButton();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mItemNameEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mItemNameEdit.setOnEditorActionListener(this);
        mOrderPresenter = new MedicationReminderItemsListPresentor(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mOrderPresenter.getOrderList();
    }


    @WorkerThread
    public void populateList(List<OrderList> orderList) {
        for (OrderList orderItem : orderList) {
            ArrayList<CommerceItems> commerceItems = orderItem.getCommerceItems();
            for (CommerceItems commerceItem : commerceItems) {
                MedicationReminderCommerceItem medicationReminderCommerceItem = new MedicationReminderCommerceItem();
                medicationReminderCommerceItem.setDescription(orderItem.getDescription());
                medicationReminderCommerceItem.setSubmittedDate(orderItem.getSubmittedDate());
                medicationReminderCommerceItem.setSkuImageUrl(commerceItem.getSkuImageUrl());
                medicationReminderCommerceItem.setProductName(commerceItem.getProductName());
                medicationReminderCommerceItem.setSkuName(commerceItem.getSkuName());
                mMedicationReminderCommerceItems.add(medicationReminderCommerceItem);
            }
        }
        new Handler(Looper.getMainLooper()).post(this);
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(ArrayList<OrderList> list) {
        mMedicationReminderCommerceItems = new ArrayList<MedicationReminderCommerceItem>();
        mProgressBar.setVisibility(View.GONE);
        populateList(list);
    }

    @Override
    public void onError(String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        Utils.displayCrouton(getActivity(), errorMessage.toString(), mContainerLayout);
    }

    @Override
    public void showErrorCrouton(CharSequence message, boolean span) {
        mProgressBar.setVisibility(View.GONE);
        Utils.displayCrouton(getActivity(), message.toString(), mContainerLayout);
    }


    @Override
    public void run() {
        setupCardsRecyclerView();
    }

    @Override
    public void setPresenter(MedicationReminderItemListContract.Presenter presenter) {
        mOrderPresenter = presenter;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            getFragmentManager().popBackStackImmediate();
            mCallBack.setItemDescription(mItemNameEdit.getText().toString(), "");
            hideSoftKeyBoard();
        }
        return false;
    }
}
