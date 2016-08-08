package com.petmeds1800.ui.support;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by pooja on 8/2/2016.
 */
public class TabPagerAdapter  extends FragmentStatePagerAdapter {
    public String[] pagersTitle;
    List<Fragment> fragmentList;

    public TabPagerAdapter(FragmentManager fm,List<Fragment> fragmentList,String[] pagersTitle) {
        super(fm);
        this.fragmentList=fragmentList;
        this.pagersTitle=pagersTitle;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return pagersTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagersTitle[position];
    }
}

