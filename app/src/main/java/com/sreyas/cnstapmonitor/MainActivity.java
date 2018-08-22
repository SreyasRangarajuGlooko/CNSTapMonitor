package com.sreyas.cnstapmonitor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sreyas.cnstapmonitor.Graph.GraphFragment;
import com.sreyas.cnstapmonitor.ManageData.ManageDataFragment;
import com.sreyas.cnstapmonitor.Models.Analytics;
import com.sreyas.cnstapmonitor.Models.TapDataListener;
import com.sreyas.cnstapmonitor.Tap.TapFragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TapDataListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Analytics.initialize(this);
        tabAdapter = new TabAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    TapFragment tapFragment = (TapFragment) tabAdapter.getFragment(0);
                    if (tapFragment != null) {
                        tapFragment.resetTap();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onTapDataChanged() {
        tabAdapter.notifyDataSetChanged();
    }

    class TabAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[]{"Tap", "Graph", "Data"};
        private HashMap<Integer, Fragment> fragments = new HashMap<>();

        TabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new TapFragment();
                    fragments.put(0, fragment);
                    break;
                case 1:
                    fragment = new GraphFragment();
                    fragments.put(1, fragment);
                    break;
                case 2:
                    fragment = new ManageDataFragment();
                    fragments.put(2, fragment);
                    break;
                default:
                    fragment = null;
            }
            return fragment;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if(object instanceof TapFragment){
                return POSITION_UNCHANGED;
            }
            return POSITION_NONE;
        }

        public Fragment getFragment(Integer position) {
            return fragments.get(position);
        }
    }
}
