package com.praticalTest.fragment;
/**
 * Created by Andy on 14-10-2018.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.praticalTest.R;
import com.praticalTest.adapter.CartListAdapter;
import com.praticalTest.entities.Item;

import java.util.List;


public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartListAdapter mAdapter;
    private Context mContext;
    private TextView tvNodata;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tvNodata = (TextView) view.findViewById(R.id.tvNodata);
        recyclerView.setVisibility(View.GONE);
        tvNodata.setVisibility(View.VISIBLE);
    }


    public void addReceivedDataToCart(List<Item> items) {
        if (items.size() > 0) {
            // making http call and fetching menu json
            tvNodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            mAdapter = new CartListAdapter(mContext, items);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        } else {
            tvNodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}