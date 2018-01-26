package com.sreyas.cnstapmonitor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.sreyas.cnstapmonitor.TapView.TapFragment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getFragmentManager());
        viewPager.setAdapter(tabAdapter);
    }

    public void redrawGraph(){
        GraphFragment graphFragment = (GraphFragment) tabAdapter.getFragment(1);
        if(graphFragment != null){
            graphFragment.drawGraph();
        }
    }

    class TabAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "Tap", "Graph", "Data" };
        private HashMap<Integer,Fragment> fragments = new HashMap<>();

        TabAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public int getCount(){
            return 3;
        }

        @Override
        public Fragment getItem(int position){
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
        public void destroyItem (ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragments.remove(position);
        }

        public Fragment getFragment(Integer position){
            return fragments.get(position);
        }
    }
}
