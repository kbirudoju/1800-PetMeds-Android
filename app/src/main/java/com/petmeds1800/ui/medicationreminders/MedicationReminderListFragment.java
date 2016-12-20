package com.petmeds1800.ui.medicationreminders;

import com.petmeds1800.R;
import com.petmeds1800.model.entities.MedicationReminderItem;
import com.petmeds1800.model.entities.MedicationReminderListResponse;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.util.Utils;

import android.os.Bundle;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sdixit on 13-10-2016.
 */

public class MedicationReminderListFragment extends AbstractFragment
        implements MedicationReminderListener, MedicationReminderListContract.View {


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.medicationsreminder_recyclerView)
    RecyclerView mMedicationsreminderRecyclerView;

    @BindView(R.id.medicationReminderContainerLayout)
    RelativeLayout mContainerLayout;

    MedicationRemindersAdapter mMedicationRemindersAdapter;

    MedicationReminderListContract.Presenter mMedicationReminderListPresentor;

    @BindView(R.id.emptyMedicationReminders)
    LinearLayout mEmptyMedicationReminders;

    @BindView(R.id.addReminder_button)
    Button mAddReminderButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupCardsRecyclerView(ArrayList<MedicationReminderItem> items) {
        mMedicationsreminderRecyclerView.setVisibility(View.VISIBLE);
        mMedicationRemindersAdapter = new MedicationRemindersAdapter(getActivity());
        mMedicationRemindersAdapter.setListener(this);
        mMedicationRemindersAdapter.setItems(items);
        mMedicationsreminderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMedicationsreminderRecyclerView.setAdapter(mMedicationRemindersAdapter);

    }

    public static MedicationReminderListFragment newInstance() {
        MedicationReminderListFragment
                medicationReminderListFragment = new MedicationReminderListFragment();
        Bundle args = new Bundle();
        medicationReminderListFragment.setArguments(args);
        return medicationReminderListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medication_reminder_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            ((AbstractActivity) getActivity())
                    .setToolBarTitle(getActivity().getString(R.string.medicationreminders_title));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mMedicationReminderListPresentor = new MedicationReminderListPresentor(this);
        mMedicationReminderListPresentor.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).enableBackButton();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_a_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                replaceAccountAndAddToBackStack(AddEditMedicationRemindersFragment.newInstance(false, (MedicationReminderItem)null),
                        AddEditMedicationRemindersFragment.class.getName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnClickMedicationEdit(MedicationReminderItem item) {
        replaceAccountAndAddToBackStack(AddEditMedicationRemindersFragment.newInstance(true, item),
                AddEditMedicationRemindersFragment.class.getName());

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(MedicationReminderListResponse response) {
        mProgressBar.setVisibility(View.GONE);
        if (response.getMedicationReminderList() != null && response.getMedicationReminderList().size() > 0) {
            setupCardsRecyclerView(response.getMedicationReminderList());
        } else {
            mEmptyMedicationReminders.setVisibility(View.VISIBLE);
        }
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
    public void showRetryView() {
        super.showErrorLayout();
    }

    @Override
    public void hideRetryView() {
        super.hideErrorLayout();

    }

    @Override
    protected void onRetryButtonClicked(View view) {
        super.onRetryButtonClicked(view);
        mProgressBar.setVisibility(View.VISIBLE);
        hideRetryView();
        mMedicationReminderListPresentor.start();
    }

    @Override
    public void setPresenter(MedicationReminderListContract.Presenter presenter) {
        mMedicationReminderListPresentor = presenter;
    }

    @OnClick(R.id.addReminder_button)
    public void onClick() {
        replaceAccountAndAddToBackStack(AddEditMedicationRemindersFragment.newInstance(false, (MedicationReminderItem) null),
                AddEditMedicationRemindersFragment.class.getName());
    }
}
