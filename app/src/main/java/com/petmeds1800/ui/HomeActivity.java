package com.petmeds1800.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.LoremFragment;
import com.petmeds1800.util.PermissionUtils;

public class HomeActivity extends AbstractMenuActivity {

    private static final String TEST_STRING_KEY = "TEST_STRING_KEY";

    private String myTestString = "initial_value";

    private LoremFragment mCurrentFragment;

    public String getMyTestString() {
        return myTestString;
    }

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Uncomment these lines to start using bugsense
//        if(!BuildConfig.DEBUG) {
//            BugSenseHandler.initAndStartSession(HomeActivity.this, getResources().getString(R.string.bugsense_api_key));
//        }
        setContentView(R.layout.activity_home);

        if (savedInstanceState != null) {
            myTestString = savedInstanceState.getString(TEST_STRING_KEY, "default");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEST_STRING_KEY, "saving_state");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PermissionUtils.STORAGE_PERMISSION_REQUEST_CODE:
                    mCurrentFragment.saveLorem();
                    break;
            }
        }
    }

    @Override
    public void onFragmentSelected(Fragment fragment) {
        super.onFragmentSelected(fragment);
        mCurrentFragment = (LoremFragment) fragment;
    }
}
