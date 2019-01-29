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


import com.example.items.DeviceItem;

import java.util.ArrayList;

public class DataProcessing extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DeviceDB";
    private static final int SCHEMA_VERSION = 1;

    private static final String SQL_FOR_TOTAL_DEVICE = "SELECT " + newDeviceItemsTable.IMEI + " FROM " + newDeviceItemsTable.TABLE_NAME;

    private static final String SQL_FOR_BORROW_DEVICE = "SELECT " + newDeviceItemsTable.IMEI + " FROM " + newDeviceItemsTable.TABLE_NAME + " WHERE " + newDeviceItemsTable.IS_BORROW + " = ?";

    private static final String SQL_FOR_LOST_DEVICE = "SELECT " + newDeviceItemsTable.IMEI + " FROM " + newDeviceItemsTable.TABLE_NAME + " WHERE " + newDeviceItemsTable.IS_LOST + " = ?";

    private static final String SQL_FOR_ALL_DEVICE = "SELECT *" + " FROM " + newDeviceItemsTable.TABLE_NAME;

    private static final String SQL_FOR_LIST_OF_NAME_DEVICE = "SELECT " + newDeviceItemsTable.NAME_OF_MODEL + " FROM " + newDeviceItemsTable.TABLE_NAME;

    private static final String SQL_FOR_DETECH_DEVICE = "SELECT *" + " FROM " + newDeviceItemsTable.TABLE_NAME + " WHERE " + newDeviceItemsTable.IMEI + " = ?";


    public DataProcessing(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    public DataProcessing(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataProcessing(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DataProcessing(Context context, String name, int version, SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataProcessing.SQL_QUERY_CREATE_NEW_DEVICE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertNewDevice(String imei, String name_of_model, String owner, String avatar_of_owner, String day_of_owner,
                                   String is_borrow, String borrower, String avatar_of_borrower, String day_of_borrow, String is_lost, String day_of_lost) {
        boolean status = false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(newDeviceItemsTable.IMEI, imei);
        cv.put(newDeviceItemsTable.NAME_OF_MODEL, name_of_model);
        cv.put(newDeviceItemsTable.OWERS, owner);
        cv.put(newDeviceItemsTable.AVARTA_OF_OWERS, avatar_of_owner);
        cv.put(newDeviceItemsTable.DAY_OF_OWNER, day_of_owner);
        cv.put(newDeviceItemsTable.IS_BORROW, is_borrow);
        cv.put(newDeviceItemsTable.BORROWER, borrower);
        cv.put(newDeviceItemsTable.AVATAR_OF_BORROW, avatar_of_borrower);
        cv.put(newDeviceItemsTable.DAY_OF_BORROW, day_of_borrow);
        cv.put(newDeviceItemsTable.IS_LOST, is_lost);
        cv.put(newDeviceItemsTable.DAY_OF_LOST, day_of_lost);

        if (db.insert(newDeviceItemsTable.TABLE_NAME, null, cv) != -1) status = true;

        return status;
    }

    public boolean updateDevice(String imei, String name_of_model, String owner, String avatar_of_owner, String day_of_owner,
                                String is_borrow, String borrower, String avatar_of_borrower, String day_of_borrow, String is_lost, String day_of_lost) {
        boolean status = false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(newDeviceItemsTable.NAME_OF_MODEL, name_of_model);
        cv.put(newDeviceItemsTable.OWERS, owner);
        cv.put(newDeviceItemsTable.AVARTA_OF_OWERS, avatar_of_owner);
        cv.put(newDeviceItemsTable.DAY_OF_OWNER, day_of_owner);
        cv.put(newDeviceItemsTable.IS_BORROW, is_borrow);
        cv.put(newDeviceItemsTable.BORROWER, borrower);
        cv.put(newDeviceItemsTable.AVATAR_OF_BORROW, avatar_of_borrower);
        cv.put(newDeviceItemsTable.DAY_OF_BORROW, day_of_borrow);
        cv.put(newDeviceItemsTable.IS_LOST, is_lost);
        cv.put(newDeviceItemsTable.DAY_OF_LOST, day_of_lost);
        String[] args = {imei};
        if (db.update(newDeviceItemsTable.TABLE_NAME, cv, "imei=?", args) != -1) status = true;
        return status;
    }

    public boolean deleteDevice(String imei) {
        boolean status = false;
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {imei};

        if (db.delete(newDeviceItemsTable.TABLE_NAME, "imei=?", args) != 0)
            status = true;

        return status;
    }

    //////////////////////////GET STATUS OF ALL TABLE/////////////////////////////
    public ArrayList<Float> getStatusOfAllTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Float> listTable = new ArrayList<Float>();

        Cursor deviceTotalCr = db.rawQuery(SQL_FOR_TOTAL_DEVICE, null);
        //total device
        float totalDevices = (float) deviceTotalCr.getCount();
        deviceTotalCr.close();

        //borrow device
        String[] args = {"true"};
        Cursor devicesBorrow = db.rawQuery(SQL_FOR_BORROW_DEVICE, args);
        float totalBorrow = (float) devicesBorrow.getCount();
        devicesBorrow.close();

        //lost_device
        String[] args1 = {"true"};
        Cursor devicesLost = db.rawQuery(SQL_FOR_LOST_DEVICE, args1);
        float totalLost = (float) devicesLost.getCount();
        devicesLost.close();

        float totalAvailable = totalDevices - totalBorrow - totalLost;

        listTable.add(totalAvailable);
        listTable.add(totalBorrow);
        listTable.add(totalLost);
        return listTable;
    }

    //get all device
    public ArrayList<DeviceItem> getAllDevice() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DeviceItem> listDeviceItem = new ArrayList<DeviceItem>();
        Cursor cursor = db.rawQuery(SQL_FOR_ALL_DEVICE, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM DeviceDetail", null);

        if (cursor.moveToFirst()) {
            do {
                String imei = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.IMEI));
                String name_of_model = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.NAME_OF_MODEL));
                String owner = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.OWERS));
                String avatar_of_owner = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.AVARTA_OF_OWERS));
                String day_of_owner = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.DAY_OF_OWNER));
                String is_borrow = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.IS_BORROW));
                String borrower = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.BORROWER));
                String avatar_of_borrower = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.AVATAR_OF_BORROW));
                String day_of_borrow = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.DAY_OF_BORROW));
                String is_lost = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.IS_LOST));
                String day_of_lost = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.DAY_OF_LOST));

                DeviceItem item = new DeviceItem(imei, name_of_model, owner, avatar_of_owner, day_of_owner,
                        is_borrow, borrower, avatar_of_borrower, day_of_borrow, is_lost, day_of_lost);

                listDeviceItem.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listDeviceItem;
    }

    ////////////////////////////GET AUTO DEVICE NAME//////////////////////////
    public ArrayList<String> getListOfNameDevice() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> listOfName = new ArrayList<String>();
        Cursor cursor = db.rawQuery(SQL_FOR_LIST_OF_NAME_DEVICE, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.NAME_OF_MODEL));
                listOfName.add(name);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listOfName;
    }

    //////////////////////////DETECT DEVICE//////////////////////////
    public DeviceItem detectDevice(String imei) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] args = {imei};
        DeviceItem item = null;
        Cursor cursor = db.rawQuery(SQL_FOR_DETECH_DEVICE, args);

        if (cursor.moveToFirst()) {
            do {
                String name_of_model = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.NAME_OF_MODEL));
                String owner = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.OWERS));
                String avatar_of_owner = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.AVARTA_OF_OWERS));
                String day_of_owner = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.DAY_OF_OWNER));
                String is_borrow = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.IS_BORROW));
                String borrower = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.BORROWER));
                String avatar_of_borrower = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.AVATAR_OF_BORROW));
                String day_of_borrow = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.DAY_OF_BORROW));
                String is_lost = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.IS_LOST));
                String day_of_lost = cursor.getString(cursor.getColumnIndex(newDeviceItemsTable.DAY_OF_LOST));

                item = new DeviceItem(imei, name_of_model, owner, avatar_of_owner, day_of_owner, is_borrow, borrower,
                        avatar_of_borrower, day_of_borrow, is_lost, day_of_lost);


            } while (cursor.moveToNext());
        }
        cursor.close();
        return item;
    }

    public static class newDeviceItemsTable implements BaseColumns {
        public static final String TABLE_NAME = "DeviceDetail";

        public static final String IMEI = "IMEI";
        public static final String NAME_OF_MODEL = "NAME_OF_MODEL";
        public static final String OWERS = "OWERS_DEVICE";
        public static final String AVARTA_OF_OWERS = "AVARTA_OF_OWERS";
        public static final String DAY_OF_OWNER = "DAY_OF_OWNER";
        public static final String IS_BORROW = "IS_BORROW";
        public static final String BORROWER = "BORROWER";
        public static final String AVATAR_OF_BORROW = "AVATAR_OF_BORROW";
        public static final String DAY_OF_BORROW = "DAY_OF_BORROW";
        public static final String IS_LOST = "IS_LOST";
        public static final String DAY_OF_LOST = "DAY_OF_LOST";

    }

    public static final String SQL_QUERY_CREATE_NEW_DEVICE_ITEMS_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    newDeviceItemsTable.TABLE_NAME +
                    " (" +
                    newDeviceItemsTable.IMEI + " TEXT PRIMARY KEY , " +
                    newDeviceItemsTable.NAME_OF_MODEL + " TEXT, " +
                    newDeviceItemsTable.OWERS + " TEXT, " +
                    newDeviceItemsTable.AVARTA_OF_OWERS + " TEXT, " +
                    newDeviceItemsTable.DAY_OF_OWNER + " TEXT, " +
                    newDeviceItemsTable.IS_BORROW + " TEXT, " +
                    newDeviceItemsTable.BORROWER + " TEXT, " +
                    newDeviceItemsTable.AVATAR_OF_BORROW + " TEXT, " +
                    newDeviceItemsTable.DAY_OF_BORROW + " TEXT, " +
                    newDeviceItemsTable.IS_LOST + " TEXT, " +
                    newDeviceItemsTable.DAY_OF_LOST + " TEXT " +
                    " );";
}
