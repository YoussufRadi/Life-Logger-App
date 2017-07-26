package com.winterproject.youssufradi.life_logger.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.winterproject.youssufradi.life_logger.Event.EventFragment;
import com.winterproject.youssufradi.life_logger.Log.LoggerFragment;

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EventFragment.displaymood = true;
                EventFragment tab1 = new EventFragment();
                return tab1;
            case 1:
                LoggerFragment.displaymood = true;
                LoggerFragment tab2 = new LoggerFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
