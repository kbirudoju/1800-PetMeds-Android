package com.petmeds1800.ui.vet;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.model.AddVetRequest;
import com.petmeds1800.model.VetList;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.checkout.AddNewEntityActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;
import com.petmeds1800.ui.vet.support.VetListSuggestionAdapter;
import com.petmeds1800.util.AnalyticsUtil;
import com.petmeds1800.util.GeneralPreferencesHelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/3/2016.
 */
public class AddVetFragment extends AbstractFragment implements View.OnClickListener,CantFindVetContract.View{
    @BindView(R.id.cantFindVetButton)
    TextView mCantFindVetButton;

    @BindView(R.id.vetTextView)
    AutoCompleteTextView mVetTextView;
    private VetList vetList;
    @Inject
    GeneralPreferencesHelper mPreferencesHelper;
    private CantFindVetContract.Presenter mPresenter;
    private Activity mCallback;
    private String mZipCode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vet, container, false);
        new AnalyticsUtil().trackScreen(getString(R.string.label_add_vet_analytics_title));
        ButterKnife.bind(this,view);
        mCantFindVetButton.setOnClickListener(this);
        PetMedsApplication.getAppComponent().inject(this);
        mPresenter=new CantFindVetPresenter(this);

        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.add_vet_header));
        ((AbstractActivity) getActivity()).enableBackButton();
        mZipCode=getArguments().getString("zipcode");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_settings, menu);
        MenuItem editMenuItem = menu.findItem(R.id.action_edit);
        MenuItem doneMenuItem= menu.findItem(R.id.action_done);
        doneMenuItem.setVisible(true);
        editMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            if(mVetTextView.getText().toString()!=null && !mVetTextView.getText().toString().isEmpty()) {
                if (vetList != null) {
                    try {
                        ((AbstractActivity)getActivity()).startLoadingGif(getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String vetName = vetList.getClinic();
                    String vetClinic = vetList.getName();
                    String vetPhone = vetList.getPhone();
                    AddVetRequest addVetRequest = new AddVetRequest(vetName, vetClinic, vetPhone
                            , mPreferencesHelper.getSessionConfirmationResponse().getSessionConfirmationNumber());
                    mPresenter.addVetData(addVetRequest);
                }
            }else{
                mVetTextView.setError(getActivity().getString(R.string.empty_vet_error));

            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVetTextView.setAdapter(new VetListSuggestionAdapter(getActivity(), mZipCode));
        // Click Listener for result items
        mVetTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                vetList = (VetList) adapterView.getItemAtPosition(position);
                String vetDetail = vetList.getName() + '\n' + vetList.getAddress();
                mVetTextView.setText(vetDetail);
                hideSoftKeyBoard();
            }
        });
    }

    @Override
    public void onClick(View v) {
        //((AddNewEntityActivity)getActivity()).replaceFragment(new CantFindVetFragment(),CantFindVetFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if(mCallback instanceof AddNewEntityActivity) {
            fragmentTransaction.replace(R.id.add_entity_container, new CantFindVetFragment(), CantFindVetFragment.class.getSimpleName());
        }else if(mCallback instanceof HomeActivity){
            fragmentTransaction.replace(R.id.account_root_fragment_container, new CantFindVetFragment(), CantFindVetFragment.class.getSimpleName());

        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean checkAndShowError(EditText auditEditText, TextInputLayout auditTextInputLayout, int errorStringId) {
        return false;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onSuccess(Vet vet) {
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();

       if(mCallback instanceof AddNewEntityActivity) {
           ((AddNewEntityActivity) mCallback).setVet(vet);
       }else if(mCallback instanceof HomeActivity){
           VetListFragment vetListFragment= (VetListFragment) ((HomeActivity) mCallback).getSupportFragmentManager().findFragmentByTag(VetListFragment.class.getName());
           vetListFragment.setVet(vet);
       }
    }

    @Override
    public void onError(String errorMessage) {
        Snackbar.make(mVetTextView, errorMessage, Snackbar.LENGTH_LONG).show();
        try {
            ((AbstractActivity)getActivity()).stopLoadingGif(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(CantFindVetContract.Presenter presenter) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (context instanceof AddNewEntityActivity) {
                mCallback = (AddNewEntityActivity) context;
            }else if(context instanceof HomeActivity){
                mCallback = (HomeActivity) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
            );
        }

    }



}
