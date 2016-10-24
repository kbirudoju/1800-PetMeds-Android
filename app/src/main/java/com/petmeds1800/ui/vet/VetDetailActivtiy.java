package com.petmeds1800.ui.vet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.petmeds1800.R;
import com.petmeds1800.intent.VetDetailIntent;
import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.AbstractActivity;

/**
 * Created by pooja on 10/21/2016.
 */
public class VetDetailActivtiy extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

       VetList vetInfo=(VetList) getIntent().getSerializableExtra(VetDetailIntent.REQUEST_CODE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment= new VetDetailFragment();
        fragmentTransaction.replace(R.id.vet_detail_container, fragment, VetDetailFragment.class.getSimpleName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("vet_detail",vetInfo);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_vet_detail;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
