package com.petmeds1800.ui;

import com.petmeds1800.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class AbstractActivity extends AppCompatActivity {

    //CHECKSTYLE:OFF
    @SuppressWarnings("checkstyle:visibilitymodifier")
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //CHECKSTYLE:ON


    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    public void setToolBarTitle(String title) {
        mToolbar.setTitle(title);
    }

    public void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void disableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    public void replaceAndAddToBackStack(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
