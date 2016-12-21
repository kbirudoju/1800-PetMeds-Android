package com.petmeds1800.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.R;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.learn.FeaturedFragment;
import com.petmeds1800.ui.learn.MedConditionsFragment;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
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

    MenuItem mAboutMenuItem, mSearchMenuItem;
    int screenType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(PetMedsApplication.menuItemsClicked ? true : false);
        PetMedsApplication.menuItemsClicked = false;
        setUpViewPager(mLearnViewPager);
        mLearnTabs.setupWithViewPager(mLearnViewPager);
        //start listening for optionsMenuAction
        registerIntent(new IntentFilter(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION), getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //we should set the title only if current selected tab is not the first home tab
        if (((HomeActivity) getActivity()).getCurrentSelectedTab() == 2) {
            ((AbstractActivity) getActivity()).setToolBarTitle((getResources().getStringArray(R.array.tab_title)[2]));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_learn, menu);
        mAboutMenuItem = menu.findItem(R.id.action_about);
        mSearchMenuItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mAboutMenuItem.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mAboutMenuItem.setVisible(true);
                return true;
            }
        });

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        searchView.setQueryHint(getString(R.string.label_search));
        searchView.setIconifiedByDefault(false);
        EditText searchEdit = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        searchEdit.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.serach_hint_color));
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchEdit, R.drawable.search_cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }


        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    PetMedsApplication.menuItemsClicked = true;
                    String encodedQuery = URLEncoder.encode(query, "utf-8");
                    String url = getString(R.string.url_search_education) + encodedQuery;
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, query);
                    bundle.putString(CommonWebviewFragment.URL_KEY, url);
                    // getToolbar().setLogo(null);
                    MenuItemCompat.collapseActionView(mSearchMenuItem);
                    replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.container_fragment_learn);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    //Toast.makeText(HomeActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_about) {
            PetMedsApplication.menuItemsClicked = true;
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebviewFragment.TITLE_KEY, getString(R.string.label_about));
            bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.url_learn_about));
            replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.container_fragment_learn);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void setUpViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        FeaturedFragment featuredFragment = new FeaturedFragment();
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

    public void addAskVetFragment(Bundle bundle) {
        replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.container_fragment_learn);
    }

    @Override
    public void onDestroyView() {
        deregisterIntent(getContext());
        super.onDestroyView();
    }

    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {
        if (intent.getAction() == HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION) {
            checkAndSetHasOptionsMenu(intent, LearnRootFragment.class.getName());
        }
        super.onReceivedBroadcast(context, intent);
    }
}
