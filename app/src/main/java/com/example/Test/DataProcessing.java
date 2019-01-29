package com.example.Test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.items.DeviceItem;

import java.util.ArrayList;

public class DataProcessing extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "DeviceDB";
	private static final int SCHEMA_VERSION = 1;

	public DataProcessing(Context context) {

		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE DeviceDetail (imei TEXT PRIMARY KEY, name_of_model TEXT, owner TEXT, avatar_of_owner TEXT, " +
				    "day_of_owner TEXT, is_borrow TEXT, borrower TEXT, avatar_of_borrower TEXT,day_of_borrow TEXT, is_lost TEXT, day_of_lost TEXT);");		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	//////////////////////////INSERT/////////////////////////////
	
	public boolean insertNewDevice(String imei, String name_of_model, String owner, String avatar_of_owner, String day_of_owner,
	                            String is_borrow, String borrower, String avatar_of_borrower,String day_of_borrow, String is_lost, String day_of_lost) 
	{
	    boolean status=false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("imei", imei);
		cv.put("name_of_model", name_of_model);
		cv.put("owner", owner);
		cv.put("avatar_of_owner", avatar_of_owner);
		cv.put("day_of_owner", day_of_owner);       
		cv.put("is_borrow", is_borrow);  
		cv.put("borrower", borrower);
		cv.put("avatar_of_borrower", avatar_of_borrower);   
		cv.put("day_of_borrow", day_of_borrow); 
		cv.put("is_lost", is_lost);       
        cv.put("day_of_lost", day_of_lost);          
		  
		if( db.insert("DeviceDetail", null, cv) != -1) status=true;
		
		return status;
	}
	
	//////////////////////////UPDATE/////////////////////////////	
	public boolean updateDevice(String imei, String name_of_model, String owner, String avatar_of_owner, String day_of_owner,
                             String is_borrow, String borrower, String avatar_of_borrower, String day_of_borrow, String is_lost, String day_of_lost)  
	{
	    boolean status=false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name_of_model", name_of_model);
        cv.put("owner", owner);
        cv.put("avatar_of_owner", avatar_of_owner);
        cv.put("day_of_owner", day_of_owner);       
        cv.put("is_borrow", is_borrow);   
        cv.put("borrower", borrower);  
        cv.put("avatar_of_borrower", avatar_of_borrower);
        cv.put("day_of_borrow", day_of_borrow);
        cv.put("is_lost", is_lost);       
        cv.put("day_of_lost", day_of_lost);       
        String[] args = { imei };
        if( db.update("DeviceDetail", cv, "imei=?", args) != -1) status=true;
        return status;
    }
	///////////////////////////////DELETE////////////////////////////
	public boolean deleteDevice(String imei)  
       {
        boolean status=false;
            SQLiteDatabase db = getWritableDatabase();                       
            String[] args = { imei };
            
            if( db.delete("DeviceDetail", "imei=?", args) != 0) status=true;
            
        return status;
        }
	
	//////////////////////////GET STATUS OF ALL TABLE/////////////////////////////  
	public ArrayList<Float>  getStatusOfAllTable()	{
	    
	    SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Float> listTable = new ArrayList<Float>();
      
        //total device
        Cursor devicesTotal = db.rawQuery("SELECT imei FROM DeviceDetail", null);
        float totalDevices=(float) devicesTotal.getCount();         
        devicesTotal.close();
        //borrow device
        String[] args = { "true" };           
        Cursor devicesBorrow = db.rawQuery("SELECT imei FROM DeviceDetail WHERE is_borrow = ?", args);
        float totalBorrow=(float) devicesBorrow.getCount(); 
        devicesBorrow.close();     
        //lost_device
        String[] args1 = { "true" };           
        Cursor devicesLost = db.rawQuery("SELECT imei FROM DeviceDetail WHERE is_lost = ?", args1);
        float totalLost=(float) devicesLost.getCount(); 
        devicesLost.close(); 
        
        float totalAvailable=totalDevices-totalBorrow-totalLost;
        
        listTable.add(totalAvailable);
        listTable.add(totalBorrow);
        listTable.add(totalLost);
        return listTable;
	}
	//////////////////////////GET_ALL_FROM_TABLES/////////////////////////////  
	//get all device
	public ArrayList<DeviceItem> getAllDevice() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DeviceItem> listDeviceItem = new ArrayList<DeviceItem>();
        Cursor cursor = db.rawQuery("SELECT * FROM DeviceDetail", null);

        if (cursor.moveToFirst()) {
            do {
                String imei = cursor.getString(cursor.getColumnIndex("imei"));
                String name_of_model = cursor.getString(cursor.getColumnIndex("name_of_model"));                
                String owner = cursor.getString(cursor.getColumnIndex("owner"));
                String avatar_of_owner = cursor.getString(cursor.getColumnIndex("avatar_of_owner"));
                String day_of_owner = cursor.getString(cursor.getColumnIndex("day_of_owner"));                
                String is_borrow = cursor.getString(cursor.getColumnIndex("is_borrow"));
                String borrower = cursor.getString(cursor.getColumnIndex("borrower"));
                String avatar_of_borrower = cursor.getString(cursor.getColumnIndex("avatar_of_borrower"));
                String day_of_borrow = cursor.getString(cursor.getColumnIndex("day_of_borrow"));
                String is_lost = cursor.getString(cursor.getColumnIndex("is_lost"));                
                String day_of_lost = cursor.getString(cursor.getColumnIndex("day_of_lost"));
                
                DeviceItem item= new DeviceItem(imei, name_of_model, owner, avatar_of_owner, day_of_owner, 
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
           Cursor cursor = db.rawQuery("SELECT name_of_model FROM DeviceDetail", null);

           if (cursor.moveToFirst()) {
               do {                    
                   String name= cursor.getString(cursor.getColumnIndex("name_of_model"));  
                   listOfName.add(name);                 
                    
               } while (cursor.moveToNext());
           }
           cursor.close();
           return listOfName;
       } 
	   
	  //////////////////////////DETECT DEVICE//////////////////////////
	   public DeviceItem detectDevice(String imei) {
           SQLiteDatabase db = this.getWritableDatabase();
           
           String[] args = { imei };     
           DeviceItem item=null;
           Cursor cursor = db.rawQuery("SELECT * FROM DeviceDetail WHERE imei= ?", args);

           if (cursor.moveToFirst()) {
               do { 
                   String name_of_model = cursor.getString(cursor.getColumnIndex("name_of_model"));                
                   String owner = cursor.getString(cursor.getColumnIndex("owner"));
                   String avatar_of_owner = cursor.getString(cursor.getColumnIndex("avatar_of_owner"));
                   String day_of_owner = cursor.getString(cursor.getColumnIndex("day_of_owner"));                
                   String is_borrow = cursor.getString(cursor.getColumnIndex("is_borrow"));
                   String borrower = cursor.getString(cursor.getColumnIndex("borrower"));
                   String avatar_of_borrower = cursor.getString(cursor.getColumnIndex("avatar_of_borrower"));                 
                   String day_of_borrow = cursor.getString(cursor.getColumnIndex("day_of_borrow"));
                   String is_lost = cursor.getString(cursor.getColumnIndex("is_lost"));                
                   String day_of_lost = cursor.getString(cursor.getColumnIndex("day_of_lost"));
                   
                   item= new DeviceItem(imei, name_of_model, owner, avatar_of_owner, day_of_owner, is_borrow, borrower, 
                		   avatar_of_borrower, day_of_borrow, is_lost, day_of_lost);
                   
                    
               } while (cursor.moveToNext());
           }
           cursor.close();
           return item;
       } 
	   
}
