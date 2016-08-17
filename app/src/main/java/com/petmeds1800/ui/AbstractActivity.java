package com.petmeds1800.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.petmeds1800.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class AbstractActivity extends AppCompatActivity {

    //CHECKSTYLE:OFF
    @SuppressWarnings("checkstyle:visibilitymodifier")
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //CHECKSTYLE:ON

    private int mSelectedNavItemIndex;

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

        }


    }

    public void setToolBarTitle(String title){
        mToolbar.setTitle(title);
    }




}
