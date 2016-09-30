package com.petmeds1800.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.petmeds1800.R;
import com.petmeds1800.intent.AddNewEntityIntent;
import com.petmeds1800.model.entities.Pets;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.checkout.stepfour.presenter.PetVetInfoContract.PetSelectionListener;
import com.petmeds1800.ui.pets.AddPetFragment;
import com.petmeds1800.util.Constants;

/**
 * Created by pooja on 9/29/2016.
 */
public class AddNewEntityActivity extends AbstractActivity implements PetSelectionListener{
    private int mRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestCode=getIntent().getIntExtra(AddNewEntityIntent.REQUEST_CODE,0);
        switch(mRequestCode){
            case Constants.ADD_NEW_PET_REQUEST:
                getSupportFragmentManager().addOnBackStackChangedListener(getListener());
                Bundle bundle= new Bundle();
                bundle.putBoolean("isEditable",false);
                replaceFragmentWithArgument(new AddPetFragment(), AddPetFragment.class.getSimpleName(), bundle);
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_entity;
    }

    public void replaceFragmentWithArgument(Fragment fragment, String tag,Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.add_entity_container, fragment, tag);
        fragment.setArguments(bundle);
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
        intent.putExtra("pet",pet);
        setResult(2,intent);
        finish();//finishing activity
    }
}
