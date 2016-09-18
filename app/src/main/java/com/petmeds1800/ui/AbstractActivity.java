package com.petmeds1800.ui;

import com.petmeds1800.R;
import com.petmeds1800.util.AnalyticsUtil;

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
    AnalyticsUtil mAnalyticsUtil;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    protected abstract int getLayoutResource();

    public void setToolBarTitle(String title) {
        mToolbar.setTitle(title);
    }

    public void enableBackButton() {
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void disableBackButton() {
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    public void replaceAccountAndAddToBackStack(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.account_root_fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

}
