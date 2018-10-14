package com.praticalTest.fragment;
/**
 * Created by Andy on 14-10-2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.praticalTest.MyApplication;
import com.praticalTest.R;
import com.praticalTest.adapter.CartListAdapter;
import com.praticalTest.entities.Item;
import com.praticalTest.helper.RecyclerItemTouchHelper;
import com.praticalTest.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ItemFragment extends Fragment implements
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = ItemFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView tvNodata;
    private List<Item> cartList;
    private CartListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private Context mContext;
    private IAddToCart iAddToCart;
    // url to fetch json
    private static final String URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            iAddToCart = (IAddToCart) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    private void initView(View view) {
        tvNodata=(TextView)view.findViewById(R.id.tvNodata);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        cartList = new ArrayList<>();
        mAdapter = new CartListAdapter(mContext, cartList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        addSwipeHelper();
    }

    private void addSwipeHelper() {
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper
                (0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        if (InternetConnection.checkConnection(mContext)) {
            // making http call and fetching menu json
            tvNodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            prepareCart();
        } else {
            Toast.makeText(mContext, "Please check your Internet connection.",
                    Toast.LENGTH_LONG).show();
            tvNodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
                    viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
    }

    /**
     * method make volley network call and parses json
     */
    private void prepareCart() {
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response: " + response.toString());
                        // Do something with response
                        // Process the JSON
                        try {
                            // Get the JSON array
                            JSONArray array = response.getJSONArray("rows");

                            List<Item> items = new Gson().fromJson(array.toString(), new TypeToken<List<Item>>() {
                            }.getType());

                            // adding items to cart list
                            cartList.clear();
                            cartList.addAll(items);

                            // refreshing recycler view
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Log.d(TAG, "Error: " + error.getMessage());
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cartList.get(viewHolder.getAdapterPosition()).getName();

            // add in cart
            Item deletedItem = cartList.get(viewHolder.getAdapterPosition());
            iAddToCart.addToCart(deletedItem);

            // remove the item from Item recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
        }

    }

    public interface IAddToCart {
        void addToCart(Item item);
    }

}