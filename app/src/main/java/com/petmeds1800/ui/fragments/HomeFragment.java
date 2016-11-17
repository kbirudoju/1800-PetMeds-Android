package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;
import com.petmeds1800.intent.BarcodeScannerIntent;
import com.petmeds1800.model.ProductCategory;
import com.petmeds1800.ui.AbstractActivity;
import com.petmeds1800.ui.HomeActivity;
import com.petmeds1800.ui.dashboard.CategoryListFragment;
import com.petmeds1800.ui.dashboard.WidgetListFragment;
import com.petmeds1800.ui.support.HomeFragmentContract;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pooja on 8/2/2016.
 */
public class HomeFragment extends AbstractFragment implements HomeFragmentContract.ProductCategoryInteractionListener {

    @BindView(R.id.home_tabs)
    TabLayout homeTabs;

    @BindView(R.id.home_viewpager)
    ViewPager homeViewPager;

    MenuItem barcodeMenuItem;

    MenuItem searchMenuItem;

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractActivity) getActivity()).disableBackButton();
        ((AbstractActivity)getActivity()).setToolBarTitle((getResources().getStringArray(R.array.tab_title)[0]));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("HomeFragment", "onCreateView>>>>>");
        ButterKnife.bind(this, view);
        setUpViewPager(homeViewPager);
        homeTabs.setupWithViewPager(homeViewPager);
        setHasOptionsMenu(true);
        ((AbstractActivity) getActivity()).getToolbar().setLogo(R.drawable.ic_logo_petmeds_toolbar);
        //start listening for optionsMenuAction
        registerIntent(new IntentFilter(HomeActivity.SETUP_HAS_OPTIONS_MENU_ACTION), getContext());

        return view;
    }

    private void setUpViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new WidgetListFragment(), getActivity().getString(R.string.home_title));
        adapter.addFragment(new CategoryListFragment(), getActivity().getString(R.string.shop_category_title));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void startWebViewFragment(ProductCategory productCategory) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebviewFragment.TITLE_KEY, productCategory.getName());
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.server_endpoint) + productCategory.getUrl());
        replaceHomeFragmentWithBundle(new CommonWebviewFragment(), bundle);
    }

    @Override
    public void replaceWebViewFragment(String url,String title) {
        Bundle bundle = new Bundle();
      //  bundle.putString(CommonWebviewFragment.TITLE_KEY, productCategory.getName());
        bundle.putString(CommonWebviewFragment.URL_KEY, getString(R.string.server_endpoint) + url);
        bundle.putString(CommonWebviewFragment.TITLE_KEY,title);
        replaceHomeFragmentWithBundle(new CommonWebviewFragment(), bundle);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        barcodeMenuItem = menu.findItem(R.id.action_barcode);
        searchMenuItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                barcodeMenuItem.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                barcodeMenuItem.setVisible(true);
                return true;
            }
        });

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setQueryHint(getString(R.string.label_search));
        searchView.setIconifiedByDefault(false);
        EditText searchEdit = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        searchEdit.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.hint_color));

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    String encodedQuery = URLEncoder.encode(query, "utf-8");
                    String url = getString(R.string.server_endpoint) + "/search.jsp?Ns=product.salesvolume%7C1&Ntt="
                            + encodedQuery;
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonWebviewFragment.TITLE_KEY, query);
                    bundle.putString(CommonWebviewFragment.URL_KEY, url);
                    // getToolbar().setLogo(null);
                    MenuItemCompat.collapseActionView(searchMenuItem);
                    replaceFragmentWithBackStack(new CommonWebviewFragment(), bundle, R.id.home_root_fragment_container);

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
        if (menuItem.getItemId() == R.id.action_barcode) {
            startActivity(new BarcodeScannerIntent(getActivity()));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onDestroyView() {
        deregisterIntent(getContext());
        super.onDestroyView();
    }

    @Override
    protected void onReceivedBroadcast(Context context, Intent intent) {
        checkAndSetHasOptionsMenu(intent, HomeRootFragment.class.getName());
        super.onReceivedBroadcast(context, intent);
    }

}


