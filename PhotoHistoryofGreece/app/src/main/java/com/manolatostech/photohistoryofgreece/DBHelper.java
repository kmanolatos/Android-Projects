package com.manolatostech.photohistoryofgreece;

/**
 * Created by kmanolatos on 22/1/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PhotoHistory.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table IF NOT EXISTS myData" +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, imageName text, latitude real, longitude real, informationId int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(String imageName, Double latitude, Double longitude, long informationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("imageName", imageName);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("informationId", informationId);
        long insertedId = db.insert("myData", null, contentValues);
        return insertedId;
    }

    public ArrayList<DataModel> getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> model = new ArrayList<DataModel>();
        Cursor res = db.rawQuery("select * from myData order by id desc", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            DataModel temp = new DataModel();
            temp.id = res.getInt(0);
            temp.imageName = res.getString(1);
            temp.latitude = res.getDouble(2);
            temp.longitude = res.getDouble(3);
            temp.informationId = res.getInt(4);
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public int delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("myData",
                "id = " + id,
                null);
    }
}