package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.petmeds1800.R;

public abstract class AbstractFragment extends Fragment {

    public void replaceAndAddToBackStack(Fragment fragment) {
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.fragment_container, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

   public  void replaceFragmentWithBundle(Fragment fragment, Bundle bundle){
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.fragment_container, fragment);
        fragment.setArguments(bundle);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void popBackStack(){
        getFragmentManager().popBackStack();
    }

}
