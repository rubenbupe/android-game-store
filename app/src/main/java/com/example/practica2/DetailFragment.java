package com.example.practica2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practica2.Utils.DatabaseHelper;

import java.text.DecimalFormat;

public class DetailFragment extends Fragment {

    private Cursor cursor;

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();

        View v = getView();

        DecimalFormat format = new DecimalFormat("#.##");

        if(v != null){
            ((ImageView)v.findViewById(R.id.gameImage)).setImageResource(
                    cursor.getInt(DatabaseHelper.GameTable.IMAGE_ID));

            ((TextView)v.findViewById(R.id.gameName)).setText(
                    cursor.getString(DatabaseHelper.GameTable.NAME));

            ((TextView)v.findViewById(R.id.gameConsole)).setText(
                    cursor.getString(DatabaseHelper.GameTable.CONSOLE));

            ((TextView)v.findViewById(R.id.gameOriginalPrice)).setText(
                    format.format(cursor.getDouble(
                            DatabaseHelper.GameTable.ORIGINAL_PRICE)) +
                            getResources().getString(R.string.currency));

            ((TextView)v.findViewById(R.id.gamePrice)).setText(
                    format.format(cursor.getDouble(DatabaseHelper.GameTable.PRICE)) +
                    getResources().getString(R.string.currency));

            ((TextView)v.findViewById(R.id.gameDate)).setText(
                    cursor.getString(DatabaseHelper.GameTable.DATE));

            v.findViewById(R.id.gameAddShoppingCart).setOnClickListener(view -> {
                if(mListener != null)
                    mListener.itemAddCartClicked();
            });

            TextView t = ((TextView)v.findViewById(R.id.gameOriginalPrice));
            t.setPaintFlags(t.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        this.cursor.moveToFirst();
    }

    interface Listener {
        void itemAddCartClicked();
    }

    private Listener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mListener = (Listener) context;
    }


}