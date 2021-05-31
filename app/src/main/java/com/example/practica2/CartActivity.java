package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.practica2.Utils.DatabaseHelper;

public class CartActivity extends AppCompatActivity implements CartListFragment.Listener {

    private Fragment curFragment;
    private DatabaseHelper databaseHelper;

    private CartListFragment cartListFragment;
    private Cursor cartCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_cart);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cartListFragment = new CartListFragment();

        changeFragment(cartListFragment);

        databaseHelper = new DatabaseHelper(this);
        cartCursor = databaseHelper.getShoppingCart();
        cartListFragment.setData(cartCursor);

    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.cartFragmentContainer, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if(this.curFragment != null) {
            transaction.remove(this.curFragment);
        }else
            transaction.disallowAddToBackStack();


        transaction.commit();
        curFragment = fragment;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void gameClicked(int position) {}

    @Override
    public void gameRemoveCartClicked(int id) {
        this.databaseHelper.removeFromCart(id);
        cartCursor = databaseHelper.getShoppingCart();
        cartListFragment.setData(cartCursor);
    }

    @Override
    public void gameRemoveOneCartClicked(int id) {
        this.databaseHelper.removeOneFromCart(id);
        cartCursor = databaseHelper.getShoppingCart();
        cartListFragment.setData(cartCursor);
    }

    @Override
    public void gameAddCartClicked(int id) {
        this.databaseHelper.addToCart(id);
        cartCursor = databaseHelper.getShoppingCart();
        cartListFragment.setData(cartCursor);
    }

    @Override
    public void checkoutClicked() {
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        checkoutFragment.setData(cartCursor);

        changeFragment(checkoutFragment);
    }


}