package com.petmeds1800.ui.support;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by pooja on 8/2/2016.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;

    public TabPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;

    }

    public List<Fragment> getitems() {
        return this.fragmentList;
    }

    public void setItemAtPosition(int position,Fragment fragment) {
        this.fragmentList.set(position,fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}

