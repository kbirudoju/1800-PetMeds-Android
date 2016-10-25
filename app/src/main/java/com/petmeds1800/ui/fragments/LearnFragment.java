package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;
import com.petmeds1800.ui.learn.FeaturedFragment;
import com.petmeds1800.ui.learn.MedConditionsFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/2/2016.
 */
public class LearnFragment extends AbstractFragment {

    @BindView(R.id.learn_tabs)
    TabLayout mLearnTabs;

    @BindView(R.id.learn_viewpager)
    ViewPager mLearnViewPager;

    MenuItem mAboutMenuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        ButterKnife.bind(this, view);
       // setHasOptionsMenu(true);
        setUpViewPager(mLearnViewPager);
        mLearnTabs.setupWithViewPager(mLearnViewPager);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_learn, menu);
        mAboutMenuItem = menu.findItem(R.id.action_barcode);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUpViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new FeaturedFragment(), getActivity().getString(R.string.title_featured));
        adapter.addFragment(new MedConditionsFragment(), getActivity().getString(R.string.title_conditions));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public void addAskVetFragment(Bundle bundle){
        addOrReplaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.container_fragment_learn);
    }
}
