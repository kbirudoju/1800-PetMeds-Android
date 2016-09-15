package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.dashboard.CategoryListFragment;
import com.petmeds1800.ui.dashboard.WidgetListFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/2/2016.
 */
public class HomeFragment extends AbstractFragment{
  private Adapter adapter;
    @BindView(R.id.home_tabs)
    TabLayout homeTabs;

    @BindView(R.id.home_viewpager)
    ViewPager homeViewPager;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).disableBackButton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this, view);
        Log.d("Visible Fragment", "HomeFragment");
        (( AbstractActivity) getActivity()).setToolBarTitle("Home");
        setUpViewPager(homeViewPager);
        homeTabs.setupWithViewPager(homeViewPager);
        return view;
    }

    private void setUpViewPager(ViewPager viewPager){
        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new WidgetListFragment(), getActivity().getString(R.string.home_title));
        adapter.addFragment(new CategoryListFragment(), getActivity().getString(R.string.shop_category_title));
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
}


