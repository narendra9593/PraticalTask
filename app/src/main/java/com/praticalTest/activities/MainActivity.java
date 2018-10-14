package com.praticalTest.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.praticalTest.R;
import com.praticalTest.adapter.ViewPagerAdapter;
import com.praticalTest.customView.CustomViewPager;
import com.praticalTest.entities.Item;
import com.praticalTest.fragment.ItemFragment;
import com.praticalTest.fragment.CartFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 14-10-2018.
 */

public class MainActivity extends AppCompatActivity implements ItemFragment.IAddToCart {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        items = new ArrayList<>();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        viewPager.setSwipeEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void addToCart(Item item) {
        items.add(item);
        String tag = "android:switcher:" + R.id.viewPager + ":" + 1;
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.setText("Cart (" + items.size() + ")");
        CartFragment f = (CartFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.addReceivedDataToCart(items);
    }
}
