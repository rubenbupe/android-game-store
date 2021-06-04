package com.example.practica2.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.practica2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "PracticaFinalDB";
    private static final int DBVERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    public interface GameTable {
        int ID = 0;
        int NAME = 1;
        int CONSOLE = 2;
        int ORIGINAL_PRICE = 3;
        int PRICE = 4;
        int IMAGE_ID = 5;
        int DATE = 6;
        int CART_COUNT = 7;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Game ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "console TEXT, "
                + "original_price REAL, "
                + "price REAL, "
                + "image_id INTEGER,"
                + "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "cart_count INTEGER DEFAULT 0);"
        );


        // Añadimos los datos de prueba
        addGame (sqLiteDatabase, "Game 1","XBOX", 10.99f, 30, getRandomId(), "2021-01-23");
        addGame (sqLiteDatabase, "Game 2","PS4", 14.99f, 20,getRandomId(), "2021-03-23");
        addGame (sqLiteDatabase, "Game 3","PS4", 15f, 50,getRandomId(), "2020-11-13");
        addGame (sqLiteDatabase, "Game 4","XBOX", 15f, 30,getRandomId(), "2021-02-27");
        addGame (sqLiteDatabase, "Game 5","PS4", 15f, 60,getRandomId(), "2021-02-19");

        addGame (sqLiteDatabase, "Game 6","XBOX", 12.99f, 12.99f, getRandomId(), "2021-05-23");
        addGame (sqLiteDatabase, "Game 7","PS4", 5.99f, 5.99f,getRandomId(), "2021-05-23");
        addGame (sqLiteDatabase, "Game 8","XBOX", 10f, 11f,getRandomId(), "2021-05-13");
        addGame (sqLiteDatabase, "Game 9","XBOX", 13f, 14.99f,getRandomId(), "2021-05-27");
        addGame (sqLiteDatabase, "Game 10","PS4", 20.99f, 21.99f,getRandomId(), "2021-05-21");

//        addGame (sqLiteDatabase, "Game 11","XBOX", 10.99f, 30, getRandomId(), "2021-01-23");
//        addGame (sqLiteDatabase, "Game 12","PS4", 14.99f, 20,getRandomId(), "2021-03-23");
//        addGame (sqLiteDatabase, "Game 13","XBOX", 15f, 50,getRandomId(), "2020-11-13");
//        addGame (sqLiteDatabase, "Game 14","XBOX", 15f, 30,getRandomId(), "2021-02-27");
//        addGame (sqLiteDatabase, "Game 15","PS4", 15f, 60,getRandomId(), "2021-02-19");
//
//        addGame (sqLiteDatabase, "Game 16","XBOX", 10.99f, 30, getRandomId(), "2021-01-23");
//        addGame (sqLiteDatabase, "Game 17","PS4", 14.99f, 20,getRandomId(), "2021-03-23");
//        addGame (sqLiteDatabase, "Game 18","XBOX", 15f, 50,getRandomId(), "2020-11-13");
//        addGame (sqLiteDatabase, "Game 19","XBOX", 15f, 30,getRandomId(), "2021-02-27");
//        addGame (sqLiteDatabase, "Game 20","PS4", 15f, 60,getRandomId(), "2021-02-19");
    }

    private int getRandomId(){
        double randomN = Math.random();
        return (randomN <= 0.5) ?
                R.drawable.cover1 :
                R.drawable.cover2;

    }

    public static void addGame (SQLiteDatabase db, String name, String console, float price, float originalPrice,
                                 int imageID, String release) {
        ContentValues gamesData = new ContentValues();
        gamesData.put("name", name);
        gamesData.put("console", console);
        gamesData.put("price", price);
        gamesData.put("original_price", originalPrice);
        gamesData.put("image_id", imageID);
        gamesData.put("date", release);
        db.insert("Game", null, gamesData);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor getGame(int id){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Game",
                new String[] {"id", "name", "console", "original_price", "price", "image_id",
                        "date", "cart_count"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        return cursor;

    }


    public Cursor getNews(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateOutput = format.format(date);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Game",
                new String[] {"id", "name", "console", "original_price", "price", "image_id",
                        "date", "cart_count"},
                "date >= ?",
                new String[]{dateOutput},
                null, null, "date desc");
        return cursor;

    }

    public Cursor getDeals(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Game",
                new String[] {"id", "name", "console", "original_price", "price", "image_id",
                        "date", "cart_count", "(original_price - price) as discount"},
                "original_price != price",
                null,
                null, null, "discount desc");
        return cursor;

    }

    public Cursor getXbox(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Game",
                new String[] {"id", "name", "console", "original_price", "price", "image_id",
                        "date", "cart_count"},
                "console == ?",
                new String[] {"XBOX"},
                null, null, null);
        return cursor;

    }

    public Cursor getPs4(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Game",
                new String[] {"id", "name", "console", "original_price", "price", "image_id",
                        "date", "cart_count"},
                "console == ?",
                new String[] {"PS4"},
                null, null, null);
        return cursor;

    }

    public Cursor getShoppingCart(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Game",
                new String[] {"id", "name", "console", "original_price", "price", "image_id",
                        "date", "cart_count"},
                "cart_count > 0",
                null,
                null, null, null);

        return cursor;
    }

    public void addToCart(int id){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE Game SET cart_count = cart_count + 1 WHERE id = " + id);
    }

    public void removeFromCart(int id){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE Game SET cart_count = 0 WHERE id = " + id);
    }

    public void removeOneFromCart(int id){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE Game SET cart_count = cart_count -1 WHERE cart_count > 0 and id = " + id);
    }
}
