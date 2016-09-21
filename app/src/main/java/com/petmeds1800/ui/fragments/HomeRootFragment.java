package com.petmeds1800.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petmeds1800.R;

/**
 * Created by pooja on 8/4/2016.
 */
public class HomeRootFragment extends AbstractFragment {
    private String mSearchString;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);
        addHomeFragment();
        return view;
    }



    public void addHomeFragment(){
        replaceHomeFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home,  menu);
        final MenuItem barcodeMenuItem = menu.findItem(R.id.action_barcode);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        barcodeMenuItem.setVisible(true);
        searchMenuItem.setVisible(true);

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
                    addOrReplaceFragmentWithBackStack(new CommonWebviewFragment(), bundle);

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

        // if prev search string, restore it here
        if (!TextUtils.isEmpty(mSearchString)) {
            searchView.setQuery(mSearchString, true);
        }




        super.onCreateOptionsMenu(menu, inflater);
    }
    public void refershMenu(){
        ((AbstractActivity)getActivity()).setToolBarTitle("");
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/
    //
}
