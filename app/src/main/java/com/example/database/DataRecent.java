package com.example.database;

import java.util.ArrayList;

import com.example.items.DeviceItem;
import com.example.items.RecentItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataRecent extends SQLiteOpenHelper {

	
	private static final String DATABASE_NAME = "RecentDB";
	private static final int SCHEMA_VERSION = 1;

	public DataRecent(Context context) {

		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE RecentDetail (ID INTEGER PRIMARY KEY AUTOINCREMENT, imei TEXT, type TEXT, content TEXT, time TEXT);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean insertNewRecent(String imei, String type, String content, String time) 
	{
		boolean status=false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("imei", imei);
		cv.put("type", type);
		cv.put("content", content);
		cv.put("time", time);		
		
		if( db.insert("RecentDetail", null, cv) != -1) status=true;
		
		return status;
	}
	
	public ArrayList<RecentItem> getAllRecent() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<RecentItem> listRecentItem = new ArrayList<RecentItem>();
        Cursor cursor = db.rawQuery("SELECT * FROM RecentDetail", null);

        if (cursor.moveToLast()) {
        	int count=0;
            do {
                String imei = cursor.getString(cursor.getColumnIndex("imei"));
                String type = cursor.getString(cursor.getColumnIndex("type"));                
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));                
                
                RecentItem item= new RecentItem(imei, type, content, time);
                
                listRecentItem.add(item);
                count++;
            } 
            while (cursor.moveToPrevious() && count < 5);
        }
        cursor.close();
        return listRecentItem;
        }
	
	public ArrayList<RecentItem> getAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<RecentItem> listRecentItem = new ArrayList<RecentItem>();
        Cursor cursor = db.rawQuery("SELECT * FROM RecentDetail", null);

        if (cursor.moveToLast()) {
        	
            do {
                String imei = cursor.getString(cursor.getColumnIndex("imei"));
                String type = cursor.getString(cursor.getColumnIndex("type"));                
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));                
                
                RecentItem item= new RecentItem(imei, type, content, time);
                
                listRecentItem.add(item);               
            } 
            while (cursor.moveToPrevious());
        }
        cursor.close();
        return listRecentItem;
        }
	
	
	
	}
