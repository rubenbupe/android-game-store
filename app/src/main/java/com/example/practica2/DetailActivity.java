package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.practica2.Utils.DatabaseHelper;

public class DetailActivity extends AppCompatActivity implements DetailFragment.Listener {

    private DetailFragment mDetailFragment;
    private Cursor cursor;
    private DatabaseHelper databaseHelper;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        id = b.getInt("GAME_ID");
        Log.d("id", String.valueOf(id));

        databaseHelper = new DatabaseHelper(this);
        cursor = databaseHelper.getGame(id);

        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);

        fragment.setCursor(cursor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.detail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    public void itemAddCartClicked() {
        this.databaseHelper.addToCart(id);
    }
}