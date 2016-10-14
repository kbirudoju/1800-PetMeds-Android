package com.petmeds1800.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.dialog.LoadingGIFDialogFragment;
import com.petmeds1800.util.AnalyticsUtil;

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

    public void replaceFragmentWithStateLoss(Fragment fragment, int containerId) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(containerId, fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * Starts Paws animation located in assets. Blocks UI
     * For updates to Cart Fragment in case of Update Quantity or apply coupon
     * @param mContext
     * @throws Exception
     */
    public void startLoadingGif(Context mContext) throws Exception{

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                LoadingGIFDialogFragment frag = new LoadingGIFDialogFragment();
                frag.show(fragmentTransaction, LoadingGIFDialogFragment.class.getSimpleName());
            }
        },100);
    }

    /**
     * Stops Paws animation located in assets. Removed Blocks to UI
     * For updates to Cart Fragment in case of Update Quantity or apply coupon
     * @param mContext
     * @throws Exception
     */
    public void stopLoadingGif(Context mContext) throws Exception{

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment prev = getSupportFragmentManager().findFragmentByTag(LoadingGIFDialogFragment.class.getSimpleName());
                if (prev != null) {
                    LoadingGIFDialogFragment df = (LoadingGIFDialogFragment) prev;
                    df.dismiss();
                }
            }
        },1000);
    }
}
