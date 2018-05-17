package com.legolambnon.rejestrpolowu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Toddler on 2017-09-07.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Entries2.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "entries_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "SPECIES";
    public static final String COL_3 = "LENGTH";
    public static final String COL_4 = "WEIGHT";
    public static final String COL_5 = "DATE";
    public static final String COL_6 = "BAIT";
    public static final String COL_7 = "FISHERY";
    public static final String COL_8 = "TIME";
    public static final String COL_9 = "WEATHER";
    public static final String COL_10 = "IMAGES";

    private static final String DATABASE_ALTER_TABLE = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN " + COL_10 + " TEXT";

    //Entries.db

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,SPECIES TEXT,LENGTH DOUBLE,WEIGHT DOUBLE,DATE TEXT,BAIT TEXT,FISHERY TEXT,TIME TEXT,WEATHER TEXT,IMAGES TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(DATABASE_ALTER_TABLE);
        }
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //onCreate(db);

    }

    public boolean insertData(String species, String length, String weight, String date, String bait, String fishery, String time, String weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, species);
        contentValues.put(COL_3, length);
        contentValues.put(COL_4, weight);
        contentValues.put(COL_5, date);
        contentValues.put(COL_6, bait);
        contentValues.put(COL_7, fishery);
        contentValues.put(COL_8, time);
        contentValues.put(COL_9, weather);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertImageUri(String id, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_10, image);
        /*
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
        */
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { id } );
        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getDetails(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM entries_table WHERE ROWID = "+id+" Limit 1", null);
        return res;
    }

    public boolean updateData(String id, String species, String length, String weight, String date, String bait, String fishery, String time, String weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, species);
        contentValues.put(COL_3, length);
        contentValues.put(COL_4, weight);
        contentValues.put(COL_5, date);
        contentValues.put(COL_6, bait);
        contentValues.put(COL_7, fishery);
        contentValues.put(COL_8, time);
        contentValues.put(COL_9, weather);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { id } );
        return true;
    }

    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?" , new String[] {id});
    }
}
