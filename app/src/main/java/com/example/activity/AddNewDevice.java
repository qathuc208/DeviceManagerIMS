package com.example.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.example.database.DataProcessing;
import com.example.database.DataRecent;
import com.example.effect.Slide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import example.com.devicemanagerims.R;

public class AddNewDevice extends AppCompatActivity{
    
    
    EditText edtNewDeviceImei, edtNewDeviceStatus;
    AutoCompleteTextView auto_name_device;
    Spinner spinnerNewDeviceOwner;
    Button btnAddNewDevice;    
    ArrayList<String> list_of_names_uppercase= new ArrayList<String>();  
    ArrayList<String> list_of_names_lowercase= new ArrayList<String>();  
    
    TextSwitcher text_switch;  
    Context context;
    boolean check=false;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);        
        ///////fullscreen        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);        
        
        setContentView(R.layout.add_new_device);
        text_switch=(TextSwitcher) findViewById(R.id.tw_title_add_device);
        context = AddNewDevice.this;	
        text_switch.setInAnimation(this, android.R.anim.slide_in_left);
		text_switch.setOutAnimation(this, android.R.anim.slide_out_right);
		
		text_switch.setFactory(new ViewFactory() {
            
            public View makeView() {
                
                TextView myText = new TextView(context);
                myText.setGravity(Gravity.TOP | Gravity.CENTER);
                myText.setTextSize(25);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });		
		text_switch.setText("You can add new device");
		Thread t = new Thread() {

			  @Override
			  public void run() {
			    try {
			      while (!isInterrupted()) {
			        Thread.sleep(3000);
			        runOnUiThread(new Runnable() {
			          @Override
			          public void run() {
			            if(check==true) 
			            	check=false;
			            else 
			            	check=true;			            
			            if(check==true) 
			            	text_switch.setText("Please fill all field");
			            else
			            	text_switch.setText("You can add new device");
			          }
			        });
			      }
			    } catch (InterruptedException e) {
			    }
			  }
			};

		t.start();	 
        edtNewDeviceImei=(EditText) findViewById(R.id.edtNewDeviceImei);
        auto_name_device=(AutoCompleteTextView) findViewById(R.id.AutoNewDeviceName);       
        spinnerNewDeviceOwner=(Spinner) findViewById(R.id.spinnerNewDeviceOwner);
        btnAddNewDevice=(Button) findViewById(R.id.btnAddNewDevice);
        //get IMEI
        
        Intent i= getIntent();
        String imei=i.getStringExtra("imei");
        
        edtNewDeviceImei.setText(imei);
       
        
        ///auto text view name of device
        //load name of devices from database
        DataProcessing database= new DataProcessing(AddNewDevice.this); 
        
        
        list_of_names_uppercase=database.getListOfNameDevice();   
        for (String element : list_of_names_uppercase) {
            
            list_of_names_lowercase.add(element.toLowerCase());
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list_of_names_lowercase);        
        auto_name_device.setAdapter(adapter); 
        
        auto_name_device.requestFocus();
        
        ////////////////////add button///////////////
        btnAddNewDevice.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {

                String owner=spinnerNewDeviceOwner.getSelectedItem().toString();
                String imei=edtNewDeviceImei.getText().toString();
                String name_of_model=auto_name_device.getText().toString().toUpperCase();
                
                if(imei.length() == 0 || name_of_model.length() == 0 || owner.length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "You have to fill all of field", Toast.LENGTH_LONG).show();
                }
                else
                {
                    DataProcessing newDevice= new DataProcessing(AddNewDevice.this);
                    Calendar c = Calendar.getInstance(); 
                    int year=c.get(Calendar.YEAR);
                    int month=c.get(Calendar.MONTH)+1;
                    int day=c.get(Calendar.DAY_OF_MONTH);                    
                    String day_of_owner=String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
                    String avatar_of_owner=getAvatarTeam(owner);
                    String is_borrow=null;
                    String borrower=null;
                    String avatar_of_borrower=null;
                    String day_of_borrow=null;
                    String is_lost=null;
                    String day_of_lost=null;
                    
                    boolean statusDevice= newDevice.insertNewDevice(imei, name_of_model, owner, avatar_of_owner, day_of_owner, 
                    		is_borrow, borrower, avatar_of_borrower, day_of_borrow, is_lost, day_of_lost);
                    
                    DataRecent recent= new DataRecent(AddNewDevice.this);
                    String type="1";
                    String content="Add "+ name_of_model;
                    int hour= c.get(Calendar.HOUR_OF_DAY);
                    int minute=c.get(Calendar.MINUTE);
                    String time=String.valueOf(hour)+":"+String.valueOf(minute)+" "+day_of_owner;
                    boolean statusRecent= recent.insertNewRecent(imei, type, content, time);
                    
                    if(statusDevice==true && statusRecent==true)
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(AddNewDevice.this);
                        TextView msg = new TextView(AddNewDevice.this);
                        msg.setText("Success");
                        msg.setPadding(10, 10, 10, 10);
                        msg.setGravity(Gravity.CENTER);
                        msg.setTextSize(18);
                        msg.setTextColor(Color.BLACK);
                        alert.setView(msg);                        
                        
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface arg0, int arg1) {                               
                             
                             finish(); 
                             overridePendingTransition(R.anim.slide_out_right_to_left, R.anim.slide_in_right_to_left);
                           }
                        });  
                        
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                    else
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(AddNewDevice.this);
                        TextView msg = new TextView(AddNewDevice.this);
                        msg.setText("Fail because existing device at database");
                        msg.setPadding(10, 10, 10, 10);
                        msg.setGravity(Gravity.CENTER);
                        msg.setTextSize(18);
                        msg.setTextColor(Color.BLACK);
                        alert.setView(msg);        
                        
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface arg0, int arg1) {                               
                             
                             finish();                             
                           }
                        });  
                        
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                        
                    
                }
                
            }
        });
        
    }  
    
    public String getAvatarTeam(String owner)
    {    
        int url_avatar=-1;
        switch (owner) {
        case "Duong Anh Son (duong.son)":
            url_avatar= R.drawable.ava1;            
            break;
        case "Nguyen Kieu Hung (kieu.hung)":
            url_avatar= R.drawable.ava2;
            break;
        case "Pham Van Hai (hai.pv)":
            url_avatar= R.drawable.ava1;
            break;
        case "Tran Van Tung (tran.vtung)":
            url_avatar= R.drawable.ava4;
            break;
        case "Tran Thien Phuc (phuc.tt)":
            url_avatar= R.drawable.ava5;
            break;
        case "Pham Ngoc Thinh (thinh.pv)":
            url_avatar= R.drawable.ava6;
            break;
        case "Nguyen Thi Thanh Dung (dung.ntt)":
            url_avatar= R.drawable.ava7;
            break;
        case "Tran Van Tai (tai.tv)":
            url_avatar= R.drawable.ava8;
            break;
        case "Nguyen Mau Thuc (thuc.nm)":
            url_avatar= R.drawable.ava9;
            break;
        case "Ngo Thanh Vinh (vinh.nt3)":
            url_avatar= R.drawable.ava10;
            break;

        default:
            break;
        }        
        
        return String.valueOf(url_avatar);
    }  
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	        finish();
	        overridePendingTransition(R.anim.slide_out_right_to_left, R.anim.slide_in_right_to_left);
	    }
	    return super.onKeyDown(keyCode, event);
	}

}