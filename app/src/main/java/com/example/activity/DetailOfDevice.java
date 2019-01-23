package com.example.activity;

import java.io.File;
import java.util.Calendar;

import com.example.database.DataProcessing;
import com.example.database.DataRecent;

import com.example.effect.Slide;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;
import android.net.Uri;

import example.com.devicemanagerims.R;

public class DetailOfDevice extends AppCompatActivity {
        
    TextView tv_name_of_model, tv_owner, tv_day_of_owner, tv_imei, tv_day_of_borrow, tv_day_of_lost, tv_status;
    ImageView img_avatar_of_owner, img_avatar_of_borrower;
    Button btnDeleteDevice;
    
    TextSwitcher text_switch;  
    Context context;
    boolean check=false;
    
    String imei, name_of_model, owner, avatar_of_owner, day_of_owner, is_borrow, borrower,
           avatar_of_borrower, day_of_borrow, is_lost, day_of_lost;
    
   
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.device_item);
        text_switch=(TextSwitcher) findViewById(R.id.tw_intro_device_detail);
        context = DetailOfDevice.this;	
        text_switch.setInAnimation(this, android.R.anim.slide_in_left);
		text_switch.setOutAnimation(this, android.R.anim.slide_out_right);
		
		text_switch.setFactory(new ViewFactory() {
            
            public View makeView() {
                
                TextView myText = new TextView(context);
                myText.setGravity(Gravity.TOP | Gravity.CENTER);
                myText.setTextSize(25);
                myText.setTextColor(Color.WHITE);
                return myText;
            }
        });		
		text_switch.setText("Detail of device");
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
			            	text_switch.setText("Becareful before delete");
			            else
			            	text_switch.setText("Detail of device");
			          }
			        });
			      }
			    } catch (InterruptedException e) {
			    }
			  }
			};

		t.start();	 
        
        // Get the intent from ListViewAdapter
        Intent i = getIntent();        
        
        imei = i.getStringExtra("imei"); 
        name_of_model = i.getStringExtra("name_of_model");  
        owner = i.getStringExtra("owner");  
        avatar_of_owner = i.getStringExtra("avatar_of_owner");  
        day_of_owner = i.getStringExtra("day_of_owner");  
        borrower = i.getStringExtra("borrower");  
        is_borrow = i.getStringExtra("is_borrow");  
        avatar_of_borrower = i.getStringExtra("avatar_of_borrower");  
        day_of_borrow = i.getStringExtra("day_of_borrow");  
        is_lost = i.getStringExtra("is_lost");  
        day_of_lost = i.getStringExtra("day_of_lost");        
       
        
        tv_imei=(TextView) findViewById(R.id.tv_imei);
        tv_name_of_model=(TextView) findViewById(R.id.tv_name_of_model);
        tv_owner=(TextView) findViewById(R.id.tv_owner);
        tv_day_of_owner=(TextView) findViewById(R.id.tv_day_of_owner);        
        tv_day_of_borrow=(TextView) findViewById(R.id.tv_day_of_borrow);
        tv_day_of_lost=(TextView) findViewById(R.id.tv_day_of_lost);
        tv_status=(TextView) findViewById(R.id.tv_status);        
        img_avatar_of_owner=(ImageView) findViewById(R.id.img_avatar_of_owner);
        img_avatar_of_borrower=(ImageView) findViewById(R.id.img_avatar_of_borrower);        
        btnDeleteDevice=(Button) findViewById(R.id.btnDeleteDevice);
        
        //prepare value
        imei=checkString(imei);
        name_of_model=checkString(name_of_model);
        owner=checkString(owner);
        day_of_owner=checkString(day_of_owner);        
        day_of_lost=checkString(day_of_lost);
        
        avatar_of_owner=checkAvatar(avatar_of_owner);
        avatar_of_borrower=checkAvatar(avatar_of_borrower);
       
        //parse value
        tv_imei.setText("Imei: "+imei);        
        tv_name_of_model.setText("Model: "+name_of_model);
        tv_owner.setText(owner);
        tv_day_of_owner.setText("Owner at "+day_of_owner); 
        
        if(is_borrow==null ) 
        {
        	if(borrower==null)
        		tv_day_of_borrow.setText("This device isn't borrowed");        	
        	else
        		tv_day_of_borrow.setText("Last borrow by\n"+borrower);
        }
        else
        	tv_day_of_borrow.setText("Borrow at "+day_of_borrow
					+"\nby "+borrower);
        if(is_lost==null ) 
        {
            tv_status.setText("Status: Available");
            tv_day_of_lost.setText("This device is ok");
        } 
        else
        {
            tv_status.setText("Status: No Available");
            tv_day_of_lost.setText("Lost at "+day_of_lost);
        }
        
        
        //avatar
        img_avatar_of_owner.setImageResource(Integer.parseInt(avatar_of_owner));
        if(avatar_of_borrower.contains(imei))
        {
            BitmapFactory.Options options = new BitmapFactory.Options(); 
            options.inPreferredConfig = Bitmap.Config.ARGB_8888; 
            Bitmap bitmap = BitmapFactory.decodeFile(avatar_of_borrower, options); 
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            img_avatar_of_borrower.setImageBitmap(resized); 
            img_avatar_of_borrower.setRotation(90);
            
            img_avatar_of_borrower.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                   
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + avatar_of_borrower), "image/*");
                    startActivity(intent);
                    
                }
            });
        }   
        btnDeleteDevice.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
               
                AlertDialog.Builder alertFirst = new AlertDialog.Builder(DetailOfDevice.this);
                TextView msg = new TextView(DetailOfDevice.this);
                msg.setText("Will device be deleted?");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                msg.setTextSize(18);
                msg.setTextColor(Color.BLACK);
                alertFirst.setView(msg);                        
                
                alertFirst.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface arg0, int arg1) {                               
                     
                       //delete ava
                       File imagesFolder = new File(Environment.getExternalStorageDirectory(), "DeviceManagement");
                       if(imagesFolder.isDirectory() == false) imagesFolder.mkdirs();
                       File image = new File(imagesFolder, imei+".jpeg"); 
                       image.delete();                
                       
                       DataProcessing database= new DataProcessing(DetailOfDevice.this);
                       boolean status= database.deleteDevice(imei);
                       
                       DataRecent recent= new DataRecent(DetailOfDevice.this);
                       String type="3";
                       String content="Remove "+ name_of_model;                       
                       Calendar c = Calendar.getInstance(); 
                       int year=c.get(Calendar.YEAR);
                       int month=c.get(Calendar.MONTH)+1;
                       int day=c.get(Calendar.DAY_OF_MONTH);                    
                       String day_of_remove=String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
                       int hour= c.get(Calendar.HOUR_OF_DAY);
                       int minute=c.get(Calendar.MINUTE);
                       String time=String.valueOf(hour)+":"+String.valueOf(minute)+" "+day_of_remove;
                       boolean statusRecent= recent.insertNewRecent(imei, type, content, time);
                       
                       if(status==true && statusRecent==true)
                       {
                           AlertDialog.Builder alert = new AlertDialog.Builder(DetailOfDevice.this);
                           TextView msg = new TextView(DetailOfDevice.this);
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
                           AlertDialog.Builder alert = new AlertDialog.Builder(DetailOfDevice.this);
                           TextView msg = new TextView(DetailOfDevice.this);
                           msg.setText("Fail");
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
                   }
                });  
                
                alertFirst.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                        
                    }
                });
                
                AlertDialog alertDialog = alertFirst.create();
                alertDialog.show();
                
            }
        });
       }
    
    public String checkString(String content)
    {
        if(content==null) return "null";
        else
            return content;
    }
    
    public String checkAvatar(String content)
    {
        if(content==null) return String.valueOf(R.drawable.noava);
        else
            return content;
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
