package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.annotation.RequiresApi;

import com.example.items.RecentItem;

import java.util.ArrayList;

public class DataRecent extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RecentDB";
    private static final int SCHEMA_VERSION = 1;

    private static final String SQL_FOR_ALL_RECENTLY = "SELECT *" + " FROM " + recentlyItemsTable.TABLE_NAME;



    public DataRecent(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataRecent(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DataRecent(Context context, String name, int version, SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    public DataRecent(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataRecent.SQL_QUERY_CREATE_RECENTLY_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertNewRecent(String imei, String type, String content, String time) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(recentlyItemsTable.IMEI, imei);
        contentValues.put(recentlyItemsTable.TYPE, type);
        contentValues.put(recentlyItemsTable.CONTENTS, content);
        contentValues.put(recentlyItemsTable.TIMES, time);

        if (db.insert(recentlyItemsTable.TABLE_NAME, null, contentValues) != -1) {
            status = true;
        }
        return status;
    }

    public ArrayList<RecentItem> getAllRecent() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<RecentItem> recentItems = new ArrayList<RecentItem>();
        Cursor cursor = db.rawQuery(SQL_FOR_ALL_RECENTLY, null);

        if (cursor.moveToLast()) {
            int count = 0;
            do {
                String imei = cursor.getString(cursor.getColumnIndex(recentlyItemsTable.IMEI));
                String type = cursor.getString(cursor.getColumnIndex(recentlyItemsTable.TYPE));
                String content = cursor.getString(cursor.getColumnIndex(recentlyItemsTable.CONTENTS));
                String time = cursor.getString(cursor.getColumnIndex(recentlyItemsTable.TIMES));

                RecentItem recentItem = new RecentItem(imei, type, content, time);

                recentItems.add(recentItem);
                count++;
            } while (cursor.moveToPrevious() && count < 5);
        }

        cursor.close();
        return recentItems;
    }

    public ArrayList<RecentItem> getAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<RecentItem> recentItems = new ArrayList<RecentItem>();
        Cursor cursor = db.rawQuery(SQL_FOR_ALL_RECENTLY, null);

        if (cursor.moveToLast()) {
            do {
                String imei = cursor.getString(cursor.getColumnIndex(recentlyItemsTable.IMEI));
                String type = cursor.getString(cursor.getColumnIndex(recentlyItemsTable.TYPE));
                String content = cursor.getString(cursor.getColumnIndex(recentlyItemsTable.CONTENTS));
                String time = cursor.getString(cursor.getColumnIndex(recentlyItemsTable.TIMES));

                RecentItem recentItem = new RecentItem(imei, type, content, time);

                recentItems.add(recentItem);
            } while (cursor.moveToPrevious());
        }

        cursor.close();
        return recentItems;
    }

    public static class recentlyItemsTable implements BaseColumns {
        public static final String TABLE_NAME = "DETAIL_ITEMS";

        public static final String ID = "ID";
        public static final String IMEI = "IMEI";
        public static final String TYPE = "TYPES";
        public static final String CONTENTS = "CONTENTS_DEVICE";
        public static final String TIMES = "TIMES_DEVICE";
    }

    //CREATE TABLE IF NOT EXISTS
    public static final String SQL_QUERY_CREATE_RECENTLY_ITEMS_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    recentlyItemsTable.TABLE_NAME +
                    " (" +
                    recentlyItemsTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    recentlyItemsTable.IMEI + " TEXT, " +
                    recentlyItemsTable.TYPE + " TEXT, " +
                    recentlyItemsTable.CONTENTS + " TEXT, " +
                    recentlyItemsTable.TIMES + " TEXT " +
                    " );"
            ;
}
