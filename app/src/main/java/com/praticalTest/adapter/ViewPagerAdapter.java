package com.praticalTest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.praticalTest.fragment.ItemFragment;
import com.praticalTest.fragment.CartFragment;

/**
 * Created by Andy on 14-10-2018.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new ItemFragment();
        }
        else if (position == 1)
        {
            fragment = new CartFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Items";
        }
        else if (position == 1)
        {
            title = "Cart";
        }

        return title;
    }
}
