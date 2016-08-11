package com.petmeds1800.ui.payment;

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
import android.widget.LinearLayout;

import com.petmeds1800.R;
import com.petmeds1800.model.Card;
import com.petmeds1800.ui.fragments.AbstractFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhinav on 11/8/16.
 */
public class SavedCardsListFragment extends AbstractFragment implements SavedCardsListContract.View, View.OnClickListener {

    @BindView(R.id.noSavedCards_layout)
    LinearLayout mNoSavedCardsLinearLayout;

    @BindView(R.id.savedCards_recyclerView)
    RecyclerView mSavedCardsRecyclerView;

    private SavedCardsListContract.Presenter mPresenter;
    private SavedCardsAdapter mSavedCardsAdapter;
    private MenuItem mAddMenuItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SavedCardsListPresenter(this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.fragment_saved_cards_list, container, false);
        ButterKnife.bind(this, view);

        mSavedCardsAdapter = new SavedCardsAdapter(false,this);
        setupCardsRecyclerView();
        return view;

    }

    private void setupCardsRecyclerView() {
        mSavedCardsRecyclerView.setAdapter(mSavedCardsAdapter);
        mSavedCardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
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
        if(id == R.id.action_add){
            replaceAndAddToBackStack(new AddACardFragment());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNoCardsView() {
        mNoSavedCardsLinearLayout.setVisibility(View.VISIBLE);
        mSavedCardsRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void showCardsListView(List<Card> cardsList) {
        mNoSavedCardsLinearLayout.setVisibility(View.GONE);
        mSavedCardsRecyclerView.setVisibility(View.VISIBLE);
        mSavedCardsAdapter.setData(cardsList);
    }

    @Override
    public void setPresenter(@NonNull SavedCardsListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {

    }
}
