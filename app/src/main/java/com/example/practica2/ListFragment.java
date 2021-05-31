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

import com.example.practica2.Adapters.ItemRecyclerAdapter;
import com.example.practica2.Utils.DatabaseHelper;

public class ListFragment extends Fragment implements ItemRecyclerAdapter.RecyclerViewClickListener {

    public static final int SECTION_NEW = 0;
    public static final int SECTION_DISCOUNTS = 1;
    public static final int SECTION_XBOX = 2;
    public static final int SECTION_PS4 = 3;

    private Cursor cursor;
    private int mode;
    private ItemRecyclerAdapter mAdapter;

    private ListFragment(){};

    public ListFragment(int mode) {
        this.mode = mode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list, container, false);
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
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            switch (this.mode){
                case SECTION_NEW:
                    cursor = dbHelper.getNews();
                    break;
                case SECTION_DISCOUNTS:
                    cursor = dbHelper.getDeals();
                    break;
                case SECTION_PS4:
                    cursor = dbHelper.getPs4();
                    break;
                case SECTION_XBOX:
                    cursor = dbHelper.getXbox();
                    break;
            }
            mAdapter = new ItemRecyclerAdapter(getActivity(),
                    this,  cursor);
            recyclerView.setAdapter(mAdapter);

        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        onItemClicked(position);
    }

    @Override
    public void recyclerViewAddClicked(View v, int position) {onItemAddClicked(position);}

    static interface Listener {
        void gameClicked(int position);
        void gameAddCartClicked(int position);
    }

    private MainFragment.Listener mListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mListener = (MainFragment.Listener) context;
    }

    public void onItemClicked(int position){
        if(mListener != null){
            mListener.gameClicked(position);
        }
    }

    public void onItemAddClicked(int position){
        if(mListener != null){
            mListener.gameAddCartClicked(position);
        }
    }
}