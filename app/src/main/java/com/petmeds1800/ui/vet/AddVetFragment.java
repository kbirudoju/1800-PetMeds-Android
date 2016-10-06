package com.petmeds1800.ui.vet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.fragments.AbstractFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 10/3/2016.
 */
public class AddVetFragment extends AbstractFragment implements View.OnClickListener{
    @BindView(R.id.cantFindVetButton)
    TextView mCantFindVetButton;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vet, container, false);
        ButterKnife.bind(this,view);
        mCantFindVetButton.setOnClickListener(this);
        ((AbstractActivity) getActivity()).setToolBarTitle(getActivity().getString(R.string.add_vet_header));
        ((AbstractActivity) getActivity()).enableBackButton();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_settings, menu);
        MenuItem editMenuItem = menu.findItem(R.id.action_edit);
        editMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        //((AddNewEntityActivity)getActivity()).replaceFragment(new CantFindVetFragment(),CantFindVetFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.add_entity_container, new CantFindVetFragment(), CantFindVetFragment.class.getSimpleName());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
       // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
