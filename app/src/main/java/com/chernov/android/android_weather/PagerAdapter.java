package com.chernov.android.android_weather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    // get fragment for attach and tab count
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment tab1 = new TabFragment(position);
                return tab1;
            case 1:
                TabFragment tab2 = new TabFragment(position);
                return tab2;
            case 2:
                TabFragment tab3 = new TabFragment(position);
                return tab3;
            case 3:
                TabFragment tab4 = new TabFragment(position);
                return tab4;
            case 4:
                TabFragment tab5 = new TabFragment(position);
                return tab5;
            case 5:
                TabFragment tab6 = new TabFragment(position);
                return tab6;
            case 6:
                TabFragment tab7 = new TabFragment(position);
                return tab7;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}