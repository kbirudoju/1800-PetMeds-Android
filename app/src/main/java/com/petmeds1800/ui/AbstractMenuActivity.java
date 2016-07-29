package com.petmeds1800.ui;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.LoremFragment;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class AbstractMenuActivity extends AppCompatActivity {

    private static final String STATE_NAV_ITEM_ID = "navItemId";
    //CHECKSTYLE:OFF
    @SuppressWarnings("checkstyle:visibilitymodifier")
    @BindView(R.id.view_drawer)
    DrawerLayout mDrawerLayout;

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
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                    mDrawerLayout, mToolbar, R.string.label_drawer_open,
                    R.string.label_drawer_close);
            mDrawerLayout.setDrawerListener(toggle);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
        }

        if (savedInstanceState == null) {
            mSelectedNavItemIndex = 0;
        } else {
            mSelectedNavItemIndex = savedInstanceState.getInt(STATE_NAV_ITEM_ID);
        }

        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.navigation_item_1:
                                mSelectedNavItemIndex = 0;
                                break;
                            case R.id.navigation_item_2:
                                mSelectedNavItemIndex = 1;
                                break;
                            case R.id.navigation_item_3:
                                mSelectedNavItemIndex = 2;
                                break;
                            case R.id.navigation_item_4:
                                mSelectedNavItemIndex = 3;
                                break;
                            case R.id.navigation_item_5:
                                mSelectedNavItemIndex = 4;
                                break;
                        }
                        menuItem.setChecked(true);
                        onFragmentSelected(LoremFragment.newInstance(mSelectedNavItemIndex + 1));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        navigationView.getMenu().getItem(mSelectedNavItemIndex).setChecked(true);
        onFragmentSelected(LoremFragment.newInstance(mSelectedNavItemIndex + 1));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_NAV_ITEM_ID, mSelectedNavItemIndex);
    }

    public void onFragmentSelected(final Fragment fragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        //noinspection ResourceType
        fragmentManager.beginTransaction()
                .replace(R.id.view_content, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
