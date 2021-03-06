package com.petmeds1800.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.petmeds1800.util.Log;
import android.view.MenuItem;

import com.petmeds1800.R;
import com.petmeds1800.intent.AddNewEntityIntent;
import com.petmeds1800.model.Address;
import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.model.entities.Vet;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.address.AddEditAddressFragment;
import com.petmeds1800.ui.checkout.stepfour.PetVetInfoFragment;
import com.petmeds1800.ui.checkout.stepfour.presenter.PetVetInfoContract.PetSelectionListener;
import com.petmeds1800.ui.checkout.steponerootfragment.StepOneRootFragment;
import com.petmeds1800.ui.checkout.stepthreefragment.StepThreeRootFragment;
import com.petmeds1800.ui.payment.AddACardContract;
import com.petmeds1800.ui.payment.AddEditCardFragment;
import com.petmeds1800.ui.pets.AddPetFragment;
import com.petmeds1800.ui.vet.AddVetFragment;
import com.petmeds1800.ui.vet.CantFindVetContract;
import com.petmeds1800.util.Constants;

/**
 * Created by pooja on 9/29/2016.
 */

public class AddNewEntityActivity extends AbstractActivity implements PetSelectionListener,AddACardContract.AddressSelectionListener ,CantFindVetContract.VetSelectionListener{
    private int mRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestCode = getIntent().getIntExtra(AddNewEntityIntent.REQUEST_CODE, 0);
        switch (mRequestCode) {
            case Constants.ADD_NEW_PET_REQUEST:
                getSupportFragmentManager().addOnBackStackChangedListener(getListener());
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEditable", false);
                replaceFragmentWithArgument(new AddPetFragment(), AddPetFragment.class.getSimpleName(), bundle);
                break;
            case Constants.ADD_NEW_ADDRESS_REQUEST:
                replaceFragment(
                        AddEditAddressFragment.newInstance(null, StepOneRootFragment.REQUEST_CODE),
                        AddEditAddressFragment.class.getName());
                break;
            case Constants.ADD_NEW_PAYMENT_METHOD:
                replaceFragment(
                        AddEditCardFragment.newInstance(StepThreeRootFragment.LOGGED_IN_REQUEST_CODE),
                        AddEditCardFragment.class.getName());
                break;
            case Constants.ADD_NEW_VET_REQUEST:
               Log.d("zipcode is", getIntent().getStringExtra(PetVetInfoFragment.ZIPCODE_KEY));
                String zipcode=getIntent().getStringExtra(PetVetInfoFragment.ZIPCODE_KEY);
                Bundle codeBundle = new Bundle();
                codeBundle.putString(PetVetInfoFragment.ZIPCODE_KEY, zipcode);
                getSupportFragmentManager().addOnBackStackChangedListener(getListener());
                replaceFragmentWithArgument(new AddVetFragment(), AddVetFragment.class.getSimpleName(),codeBundle);
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_entity;
    }

    public void replaceFragmentWithArgument(Fragment fragment, String tag, Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.add_entity_container, fragment, tag);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.add_entity_container, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    public void replaceFragmentWithBackStack(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.add_entity_container, fragment, tag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
/*Method will be called whenver there is changes in backstack.If the backstack is zero then finish the activity*/
    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount == 0) {
                        finish();
                    }

                }
            }
        };
        return result;
    }

    @Override
    public void setPet(Pets pet) {

        Intent intent=new Intent();
        intent.putExtra(PetVetInfoFragment.PET_KEY,pet);
        setResult(Constants.ADD_NEW_PET_REQUEST,intent);
        finish();//finishing activity
    }

    @Override
    public void setVet(Vet vet) {
        Intent intent=new Intent();
        intent.putExtra(PetVetInfoFragment.VET_KEY,vet);
        setResult(Constants.ADD_NEW_VET_REQUEST,intent);
        finish();//finishing activity
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void setAddress(Address address, int requestCode) {
        AddEditCardFragment addCardFragment = (AddEditCardFragment) getSupportFragmentManager()
                .findFragmentByTag(AddEditCardFragment.class.getName());
        if (addCardFragment != null) {
            addCardFragment.displayAddress(address);
        } else {
            AddEditCardFragment newFragment = AddEditCardFragment.newInstance(address, requestCode);
            replaceAccountAndAddToBackStack(newFragment, AddEditCardFragment.class.getName());
        }
    }
}
