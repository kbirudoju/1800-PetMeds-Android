package com.petmeds1800.ui.address;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.intent.AddNewEntityIntent;
import com.petmeds1800.model.Address;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.checkout.AddNewEntityActivity;
import com.petmeds1800.ui.checkout.steponerootfragment.StepOneRootFragment;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.payment.AddEditCardFragment;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Abhinav on 11/8/16.
 */
public class AddressSelectionListFragment extends AbstractFragment
        implements SavedAddressListContract.View, View.OnClickListener {

    @BindView(R.id.noSavedAddress_layout)
    LinearLayout mNoSavedAddressLinearLayout;

    @BindView(R.id.savedAddress_recyclerView)
    RecyclerView mSavedAddressRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.containerLayout)
    RelativeLayout mContainerLayout;

    @BindView(R.id.addNewAdrress)
    Button mAddNewAdrress;

    @BindView(R.id.headerText)
    TextView mHeaderText;

    private SavedAddressListContract.Presenter mPresenter;

    private AddressSelectionAdapter mSavedAddressAdapter;

    private MenuItem mAddMenuItem;

    private AddNewEntityActivity stepOneCallBack;

    private HomeActivity mCallback;

    private int mRequestCode;

    private String mShippingAddressId;


    public static AddressSelectionListFragment newInstance(int requestCode, String shippingAddressId) {
        Bundle args = new Bundle();
        args.putInt(AddEditCardFragment.REQUEST_CODE, requestCode);
        args.putString(StepOneRootFragment.SHIPPING_ADDRESS_KEY, shippingAddressId);
        AddressSelectionListFragment fragment = new AddressSelectionListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SavedAddressListPresenter(this);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mRequestCode = bundle.getInt(AddEditCardFragment.REQUEST_CODE);
            mShippingAddressId = bundle.getString(StepOneRootFragment.SHIPPING_ADDRESS_KEY);
        }
        if (mRequestCode == AddEditCardFragment.EDIT_CARD_REQUEST) {
            setHasOptionsMenu(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_address_list, container, false);
        ButterKnife.bind(this, view);
        mAddNewAdrress.setOnClickListener(this);
        if (mRequestCode == StepOneRootFragment.REQUEST_CODE) {
            setUpViews();
        } else {
            ((AbstractActivity) getActivity()).enableBackButton();
            ((AbstractActivity) getActivity())
                    .setToolBarTitle(getContext().getString(R.string.addressSelectionListTitle));
        }
        mSavedAddressAdapter = new AddressSelectionAdapter(false, this, getContext(), mRequestCode);
        setupCardsRecyclerView();
        return view;

    }

    private void setUpViews() {
        mAddNewAdrress.setVisibility(View.VISIBLE);
        mHeaderText.setVisibility(View.VISIBLE);
        mNoSavedAddressLinearLayout.setVisibility(View.GONE);
        mSavedAddressRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (context instanceof HomeActivity) {
                mCallback = (HomeActivity) context;
            }
            if (context instanceof AddNewEntityActivity) {
                stepOneCallBack = (AddNewEntityActivity) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AddACardContract.AddressSelectionListener");
        }

    }

    private void setupCardsRecyclerView() {
        mSavedAddressRecyclerView.setAdapter(mSavedAddressAdapter);
        mSavedAddressRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mRequestCode == StepOneRootFragment.REQUEST_CODE) {
            mSavedAddressRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_a_card, menu);
        mAddMenuItem = menu.findItem(R.id.action_add);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            replaceAccountAndAddToBackStack(new AddEditAddressFragment(), AddEditAddressFragment.class.getName());
        }
        return super.onOptionsItemSelected(item);
    }

    public String getShippingAddressId() {
        return mShippingAddressId;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNoAddressView() {
        mProgressBar.setVisibility(View.GONE);
        if (mRequestCode != StepOneRootFragment.REQUEST_CODE) {
            mNoSavedAddressLinearLayout.setVisibility(View.VISIBLE);
            mSavedAddressRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showAddressListView(List<Address> addressList) {
        mProgressBar.setVisibility(View.GONE);
        mNoSavedAddressLinearLayout.setVisibility(View.GONE);
        mSavedAddressRecyclerView.setVisibility(View.VISIBLE);
        mSavedAddressAdapter.setData(addressList);
    }

    @Override
    public void startAddressUpdate(Address address) {
        // no implementation is required here. But implementation is required in SavedAddressListFragment
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        errorMessage = errorMessage.equals(Utils.TIME_OUT) ? getString(R.string.internet_not_available) : errorMessage;
        mProgressBar.setVisibility(View.GONE);
        Utils.displayCrouton(getActivity(), (String) errorMessage);
    }

    @Override
    public void setPresenter(@NonNull SavedAddressListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    void forwardAddressToActivity(Address address, int requestCode) {
        if (mCallback != null) {
            mCallback.setAddress(address, requestCode);
        }
        if (stepOneCallBack != null) {
            stepOneCallBack.setAddress(address, requestCode);
        }

    }

    void forwardAddressToFragment(Address address) {
        ((StepOneRootFragment) getParentFragment()).setAddress(address);

    }


    public void addNewAddress() {
        startActivity(new AddNewEntityIntent(getActivity(), Constants.ADD_NEW_ADDRESS_REQUEST));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewAdrress:
                addNewAddress();
                break;
        }


    }

}
