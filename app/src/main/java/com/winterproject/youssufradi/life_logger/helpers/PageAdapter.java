package com.winterproject.youssufradi.life_logger.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import com.winterproject.youssufradi.life_logger.Event.EventFragment;
import com.winterproject.youssufradi.life_logger.Log.LoggerFragment;

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public static EventFragment tab1;
    public static LoggerFragment tab2;
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EventFragment.displaymood = true;
                tab1 = new EventFragment();
                return tab1;
            case 1:
                LoggerFragment.displaymood = true;
                tab2 = new LoggerFragment();
                return tab2;
            case 2:
                LoggerFragment.displaymood = true;
                LoggerFragment tab3 = new LoggerFragment();
            return tab3;
        default:
          return null;
        }
    }


    SparseArray<View> views = new SparseArray<View>();

//    @Override
//    public Object instantiateItem(View container, int position) {
//        View root = <build your view here>;
//        ((ViewPager) container).addView(root);
//        views.put(position, root);
//        return root;
//    }

    @Override
    public void destroyItem(View collection, int position, Object o) {
        View view = (View)o;
        ((ViewPager) collection).removeView(view);
        views.remove(position);
        view = null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
