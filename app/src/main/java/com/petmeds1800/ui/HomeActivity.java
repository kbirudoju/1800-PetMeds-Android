package com.petmeds1800.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.petmeds1800.R;
import com.petmeds1800.ui.fragments.AccountRootFragment;
import com.petmeds1800.ui.fragments.CartFragment;
import com.petmeds1800.ui.fragments.HomeFragment;
import com.petmeds1800.ui.fragments.LearnFragment;
import com.petmeds1800.ui.support.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AbstractActivity {

    @BindView(R.id.tablayout)
    TabLayout mHomeTab;

    @BindView(R.id.viewpager_fragments)
    ViewPager mViewPager;

    List<Fragment> fragmentList;
    private static final int[] TAB_ICON_UNSELECTED = {R.drawable.ic_menu_home, R.drawable.ic_menu_cart, R.drawable.ic_menu_learn,R.drawable.ic_menu_account};
    private static final int[] TAB_ICON_SELECTED = {R.drawable.ic_menu_home_pressed, R.drawable.ic_menu_cart_pressed, R.drawable.ic_menu_learn_pressed,R.drawable.ic_menu_account_pressed};


    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
            //initialize fragment list
            fragmentList = new ArrayList<Fragment>();
            fragmentList.add(new HomeFragment());
            fragmentList.add(new CartFragment());
            fragmentList.add(new LearnFragment());
            fragmentList.add(new AccountRootFragment());

            mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), fragmentList));
            mHomeTab.setupWithViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(0);

            ViewPager.OnPageChangeListener pageChangeListener= new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setTitle((getResources().getStringArray(R.array.tab_title)[position]));
                    for (int i = 0; i < mHomeTab.getTabCount(); ++i)
                        mHomeTab.getTabAt(i).setIcon(i != position ? TAB_ICON_UNSELECTED[i] : TAB_ICON_SELECTED[i]);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

             };

        //code to set defult first tab selected
        mViewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);
    }





}
