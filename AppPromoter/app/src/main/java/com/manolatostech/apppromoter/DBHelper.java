package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 22/1/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AppPromoter.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table IF NOT EXISTS user" +
                        "(id integer, user text, pass text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(int id, String user, String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("user", user);
        contentValues.put("pass", pass);
        db.insert("user", null, contentValues);
    }

    public void updatePass(long id, String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pass", pass);
        db.update("user", contentValues, "id = " + id, null );
    }

    public int delete(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("user",
                "id = " + id,
                null);
    }
}