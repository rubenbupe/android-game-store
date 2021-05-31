package com.example.practica2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practica2.Adapters.CartRecyclerAdapter;
import com.example.practica2.Adapters.ItemRecyclerAdapter;
import com.example.practica2.Utils.DatabaseHelper;

public class CartListFragment extends Fragment implements CartRecyclerAdapter.RecyclerViewClickListener {

    private Cursor cursor;
    private CartRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_cart_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View v = getView();

        if(v != null){
            RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.itemsList);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            ;


            mAdapter = new CartRecyclerAdapter(getActivity(),
                    this,  cursor);
            recyclerView.setAdapter(mAdapter);
            
            v.findViewById(R.id.checkoutCart).setOnClickListener(view -> {
                if(mListener != null)
                    mListener.checkoutClicked();
            });

            if(cursor.getCount() == 0) {
                setEmptyMode(v);
            }
        }
    }

    public void setData(Cursor cursor){
        this.cursor = cursor;
        if(mAdapter != null){
            mAdapter.setData(cursor);
            if(cursor.getCount() == 0) {
                setEmptyMode(getView());
            }
        }

    }

    private void setEmptyMode(View v){
        v.findViewById(R.id.checkoutCart).setVisibility(View.INVISIBLE);
        v.findViewById(R.id.noItemsText).setVisibility(View.VISIBLE);
    }

    @Override
    public void recyclerViewListClicked(View v, int id) {
        onItemClicked(id);
    }

    @Override
    public void recyclerViewRemoveClicked(View v, int id) {
        onItemRemoveClicked(id);
    }

    @Override
    public void recyclerViewRemoveOneClicked(View v, int id) {
        onItemRemoveOneClicked(id);
    }

    @Override
    public void recyclerViewAddClicked(View v, int id) {
        onItemAddClicked(id);
    }

    static interface Listener {
        void gameClicked(int position);
        void gameRemoveCartClicked(int position);
        void gameRemoveOneCartClicked(int position);
        void gameAddCartClicked(int position);
        void checkoutClicked();
    }

    private Listener mListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mListener = (Listener) context;
    }

    public void onItemClicked(int id){
        if(mListener != null){
            mListener.gameClicked(id);
        }
    }

    public void onItemRemoveClicked(int id){
        if(mListener != null){
            mListener.gameRemoveCartClicked(id);
        }
    }

    public void onItemRemoveOneClicked(int id){
        if(mListener != null){
            mListener.gameRemoveOneCartClicked(id);
        }
    }

    public void onItemAddClicked(int id){
        if(mListener != null){
            mListener.gameAddCartClicked(id);
        }
    }
}