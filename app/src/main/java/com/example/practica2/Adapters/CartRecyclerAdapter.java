package com.example.practica2.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica2.R;
import com.example.practica2.Utils.DatabaseHelper;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.MyViewHolder>{

    //private ArrayList<GameData> mData;
    private Integer maxItems;
    private Context mContext;
    private static RecyclerViewClickListener itemListener;
    private Cursor items;


    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int id);
        void recyclerViewRemoveClicked(View v, int id);
        void recyclerViewRemoveOneClicked(View v, int id);
        void recyclerViewAddClicked(View v, int id);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = (LinearLayout)itemView;
            linearLayout.setOnClickListener(this);
            ImageView imageViewRemove = (ImageView) itemView.findViewById(R.id.gameRemoveShoppingCart);
            imageViewRemove.setOnClickListener(this);

            ImageView imageViewRemoveOne = (ImageView) itemView.findViewById(R.id.gameRemoveOneShoppingCart);
            imageViewRemoveOne.setOnClickListener(this);

            ImageView imageViewAdd = (ImageView) itemView.findViewById(R.id.gameAddShoppingCart);
            imageViewAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.gameRemoveShoppingCart)
                itemListener.recyclerViewRemoveClicked(v, getGameId(getAdapterPosition()));
            else if(v.getId() == R.id.gameRemoveOneShoppingCart)
                itemListener.recyclerViewRemoveOneClicked(v, getGameId(getAdapterPosition()));
            else if(v.getId() == R.id.gameAddShoppingCart)
                itemListener.recyclerViewAddClicked(v, getGameId(getAdapterPosition()));
            else
                itemListener.recyclerViewListClicked(v, getGameId(getAdapterPosition()));

        }
    }

    private int getGameId(int position) {
        if (items != null) {
            if (items.moveToPosition(position)) {
                return items.getInt(DatabaseHelper.GameTable.ID);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public void setData(Cursor cursor){
        this.items = cursor;
        this.notifyDataSetChanged();
    }

    public CartRecyclerAdapter(Context context, RecyclerViewClickListener itemListener,
                               Cursor cursor) {
        this(context, itemListener, cursor, null);
    }

    public CartRecyclerAdapter(Context context, RecyclerViewClickListener itemListener,
                               Cursor cursor, Integer maxItems) {
        mContext=context;
        this.items = cursor;
        CartRecyclerAdapter.itemListener = itemListener;
        this.maxItems = maxItems;
    }

    @Override
    public CartRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cart_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        items.moveToPosition(pos);

        ((TextView)holder.linearLayout.findViewById(R.id.gameName)).setText(
                items.getString(DatabaseHelper.GameTable.NAME)
        );
        ((TextView)holder.linearLayout.findViewById(R.id.gamePrice)).setText(
                ( String.valueOf(items.getString(DatabaseHelper.GameTable.PRICE)) + "€")
        );
        ((TextView)holder.linearLayout.findViewById(R.id.gameCount)).setText(
                ( String.valueOf(items.getString(DatabaseHelper.GameTable.CART_COUNT)) + " uds.")
        );
    }

    @Override
    public int getItemCount() {
        if(items != null) {
            return items.getCount();
        }
        return 0;
    }

    public Cursor getCursor() {
        return this.items;
    }

}



