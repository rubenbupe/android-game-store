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
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.practica2.Adapters.ItemRecyclerAdapter;
import com.example.practica2.Utils.DatabaseHelper;

public class MainFragment extends Fragment implements ItemRecyclerAdapter.RecyclerViewClickListener {

    private ItemRecyclerAdapter mAdapter;
    private Cursor cursor;

    public MainFragment(){

    }

    @Override
    public void onStart() {
        super.onStart();

        View v = getView();

        if(v != null){
            RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.itemsList);
            Spinner spinner = (Spinner) v.findViewById(R.id.mainSpinner);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            cursor = dbHelper.getNews();

            mAdapter = new ItemRecyclerAdapter(getActivity(),
                    this,  cursor, 3);
            recyclerView.setAdapter(mAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int option = (int) spinner.getSelectedItemId();
                    switch(option){
                        case 0:
                            mAdapter.setData(dbHelper.getNews());
                            break;
                        case 1:
                            mAdapter.setData(dbHelper.getDeals());
                            break;
                        case 2:
                            mAdapter.setData(dbHelper.getXbox());
                            break;
                        case 3:
                            mAdapter.setData(dbHelper.getPs4());
                            break;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
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

    private Listener mListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mListener = (Listener) context;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }



}