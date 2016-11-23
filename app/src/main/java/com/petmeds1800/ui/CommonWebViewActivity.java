package com.petmeds1800.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.CommonWebviewFragment;

/**
 * Created by Digvijay on 10/28/2016.
 */

public class CommonWebViewActivity extends AbstractActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonWebviewFragment webViewFragment = new CommonWebviewFragment();
        webViewFragment.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_web_view_fragment, webViewFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
