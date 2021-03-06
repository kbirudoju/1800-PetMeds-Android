package com.petmeds1800.ui.payment;

import com.petmeds1800.R;
import com.petmeds1800.model.Card;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.fragments.dialog.FingerprintAuthenticationDialog;
import com.petmeds1800.util.Constants;
import com.petmeds1800.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhinav on 11/8/16.
 */
public class SavedCardsListFragment extends AbstractFragment
        implements SavedCardsListContract.View {

    @BindView(R.id.noSavedCards_layout)
    LinearLayout mNoSavedCardsLinearLayout;

    @BindView(R.id.savedCards_recyclerView)
    RecyclerView mSavedCardsRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.addCard_button)
    Button mAddCardButton;

    @BindView(R.id.savedCardsContainerLayout)
    RelativeLayout mContainerLayout;

    private SavedCardsListContract.Presenter mPresenter;

    private SavedCardsAdapter mSavedCardsAdapter;

    private MenuItem mAddMenuItem;

    private static final String LOGGED_IN = "logged in";
    private static final String FINGERPRINT_AUTHENTICATION_DIALOG = "FingerprintAuthenticationDialog";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SavedCardsListPresenter(this);
        setHasOptionsMenu(true);
        registerIntent(new IntentFilter(Constants.KEY_AUTHENTICATION_SUCCESS), getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_cards_list, container, false);
        ButterKnife.bind(this, view);
        ((AbstractActivity) getActivity()).setToolBarTitle(getContext().getString(R.string.savedCardsListTitle));
        mSavedCardsAdapter = new SavedCardsAdapter(false, this, getContext());
        setupCardsRecyclerView();
        return view;

    }

    @Override
    protected boolean showErrorLayout() {
        return super.showErrorLayout();
    }

    @Override
    protected boolean hideErrorLayout() {
        return super.hideErrorLayout();
    }

    @Override
    protected void onRetryButtonClicked(View view) {
        hideErrorLayout();
        mProgressBar.setVisibility(View.VISIBLE);
        super.onRetryButtonClicked(view);
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    private void setupCardsRecyclerView() {
        mSavedCardsRecyclerView.setAdapter(mSavedCardsAdapter);
        mSavedCardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).enableBackButton();
        hideErrorLayout();
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
        if (id == R.id.action_add) {
            replaceAccountAndAddToBackStack(new AddEditCardFragment(), AddEditCardFragment.class.getName());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNoCardsView() {

        mProgressBar.setVisibility(View.GONE);
        mNoSavedCardsLinearLayout.setVisibility(View.VISIBLE);
        mSavedCardsRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void showCardsListView(List<Card> cardsList) {
        mProgressBar.setVisibility(View.GONE);
        mNoSavedCardsLinearLayout.setVisibility(View.GONE);
        mSavedCardsRecyclerView.setVisibility(View.VISIBLE);
        mSavedCardsAdapter.setData(cardsList);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        showErrorLayout();
        mProgressBar.setVisibility(View.GONE);
        errorMessage = errorMessage.equals(Utils.TIME_OUT) ? getString(R.string.internet_not_available) : errorMessage;
        Utils.displayCrouton(getActivity(), errorMessage, mContainerLayout);

    }

    @Override
    public void setPresenter(@NonNull SavedCardsListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.addCard_button)
    void startAddCardFragment() {
        replaceAccountAndAddToBackStack(new AddEditCardFragment(), AddEditCardFragment.class.getName());
    }

    @Override
    public void startCardUpdate(Card card) {
        replaceAccountAndAddToBackStack(
                AddEditCardFragment.newInstance(card, AddEditCardFragment.EDIT_CARD_REQUEST),
                AddEditCardFragment.class.getName());
    }

    @Override
    public void showCroutanMessage(String errorMessage) {
        Utils.displayCrouton(getActivity(), errorMessage.toString(), mContainerLayout);
        if (errorMessage.contains(LOGGED_IN)) {
            FingerprintAuthenticationDialog mAuthenticationDialog = new FingerprintAuthenticationDialog();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.SHOW_SOFT_LOGIN_DAILOG, true);
            mAuthenticationDialog.setArguments(bundle);
            if (!mAuthenticationDialog.isAdded()) {
                mAuthenticationDialog.setCancelable(false);
                mAuthenticationDialog.show(getActivity().getSupportFragmentManager(), FINGERPRINT_AUTHENTICATION_DIALOG);
            }
        }
    }

    @Override
    public void onDestroy() {
        deregisterIntent(getActivity());
        super.onDestroy();
    }
    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {
        mPresenter.start();
        super.onReceivedBroadcast(context, intent);
    }
}
