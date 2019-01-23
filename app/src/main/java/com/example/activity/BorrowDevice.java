package com.example.activity;
import java.util.Calendar;

import com.example.database.DataProcessing;
import com.example.database.DataRecent;
import com.example.effect.Slide;
import com.example.image.CameraProcessing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import example.com.devicemanagerims.R;

public class BorrowDevice extends AppCompatActivity {
    
    
    TextView tv_name_of_model, tv_owner, tv_day_of_owner, tv_imei, tv_day_of_borrow, tv_day_of_lost, tv_status;
    ImageView img_avatar_of_owner, img_avatar_of_borrower;
    Button btnBorrowDevice;        
    String imei, name_of_model, owner, avatar_of_owner, day_of_owner, is_borrow, borrower,
           avatar_of_borrower, day_of_borrow, is_lost, day_of_lost;
    String avatar_from_camera =null;
    TextSwitcher text_switch;  
    Context context;
    boolean check=false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
              
        setContentView(R.layout.borrow_item);
        text_switch=(TextSwitcher) findViewById(R.id.tw_intro_device_detail);
        context = BorrowDevice.this;	
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
		text_switch.setText("Device can be borrowed");
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
			            	text_switch.setText("Becareful before borrow");
			            else
			            	text_switch.setText("Device can be borrowed");
			          }
			        });
			      }
			    } catch (InterruptedException e) {
			    }
			  }
			};

		t.start();	 
       
        tv_imei=(TextView) findViewById(R.id.tv_imei);
        tv_name_of_model=(TextView) findViewById(R.id.tv_name_of_model);
        tv_owner=(TextView) findViewById(R.id.tv_owner);
        tv_day_of_owner=(TextView) findViewById(R.id.tv_day_of_owner);        
        tv_day_of_borrow=(TextView) findViewById(R.id.tv_day_of_borrow);
        tv_day_of_lost=(TextView) findViewById(R.id.tv_day_of_lost);
        tv_status=(TextView) findViewById(R.id.tv_status);
        
        img_avatar_of_owner=(ImageView) findViewById(R.id.img_avatar_of_owner);
        img_avatar_of_borrower=(ImageView) findViewById(R.id.img_avatar_of_borrower);        
        btnBorrowDevice=(Button) findViewById(R.id.btnBorrowDevice);
        
       
        
        // Get the intent from ListViewAdapter
        Intent i = getIntent();
        
        imei = i.getStringExtra("imei"); 
        name_of_model = i.getStringExtra("name_of_model");  
        owner = i.getStringExtra("owner");  
        avatar_of_owner = i.getStringExtra("avatar_of_owner");  
        day_of_owner = i.getStringExtra("day_of_owner");  
        is_borrow = i.getStringExtra("is_borrow"); 
        borrower = i.getStringExtra("borrower");
        avatar_of_borrower = i.getStringExtra("avatar_of_borrower");  
        day_of_borrow = i.getStringExtra("day_of_borrow");  
        is_lost = i.getStringExtra("is_lost");  
        day_of_lost = i.getStringExtra("day_of_lost");  
        
        //get url from CameraProcessing
        //avatar_from_camera =i.getStringExtra("avatar_from_camera");
        //get borrower;
        borrower = i.getStringExtra("borrower"); 
        if(borrower != null)
        {           
            btnBorrowDevice.setText("Back");
            avatar_of_borrower=avatar_from_camera;
            is_borrow="true";
            //get day
            Calendar c = Calendar.getInstance(); 
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH)+1;
            int day=c.get(Calendar.DAY_OF_MONTH);                    
            String day_of_borrow=String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
            
            DataProcessing database= new DataProcessing(BorrowDevice.this);
            boolean status= database.updateDevice(imei, name_of_model, owner, avatar_of_owner, day_of_owner, 
                    is_borrow, borrower, avatar_of_borrower, day_of_borrow, is_lost, day_of_lost);
            
            DataRecent recent= new DataRecent(BorrowDevice.this);
            String type="2";
            String content="Borrow "+ name_of_model;
            int hour= c.get(Calendar.HOUR_OF_DAY);
            int minute=c.get(Calendar.MINUTE);
            String time=String.valueOf(hour)+":"+String.valueOf(minute)+" "+day_of_borrow;
            boolean statusRecent= recent.insertNewRecent(imei, type, content, time);
            
            if(status==true && statusRecent==true)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(BorrowDevice.this);
                TextView msg = new TextView(BorrowDevice.this);
                msg.setText("Success");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                msg.setTextSize(18);
                msg.setTextColor(Color.BLACK);
                alert.setView(msg);                        
                
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface arg0, int arg1) {                               
                     
                     //finish();                             
                   }
                });  
                
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(BorrowDevice.this);
                TextView msg = new TextView(BorrowDevice.this);
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
                   }
                });  
                
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }                           
        }    
       
        //prepare value
        imei=checkString(imei);
        name_of_model=checkString(name_of_model);
        owner=checkString(owner);
        day_of_owner=checkString(day_of_owner);        
        day_of_lost=checkString(day_of_lost);
        day_of_borrow=checkString(day_of_borrow);
        
        avatar_of_owner=checkAvatar(avatar_of_owner);
        avatar_of_borrower=checkAvatar(avatar_of_borrower);
       
        //parse value
        tv_imei.setText("imei: "+imei);        
        tv_name_of_model.setText("Model: "+name_of_model);
        tv_owner.setText(owner);
        tv_day_of_owner.setText("Owner at "+day_of_owner);         
        tv_day_of_lost.setText("Lost at "+day_of_lost);
        tv_day_of_borrow.setText("Borrow at "+day_of_borrow
        						+"\nby "+borrower);
        
        if(is_borrow==null) tv_day_of_borrow.setText("This device isn't borrowed");
        if(is_lost==null ) 
            {
                tv_status.setText("Status: Available");
                tv_day_of_lost.setText("This device is ok");
            }        
        
        //avatar
        img_avatar_of_owner.setImageResource(Integer.parseInt(avatar_of_owner));
        if(avatar_of_borrower.contains(imei))
        {
            BitmapFactory.Options options = new BitmapFactory.Options(); 
            options.inPreferredConfig = Bitmap.Config.ARGB_8888; 
            Bitmap bitmap = BitmapFactory.decodeFile(avatar_of_borrower, options); 
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
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
        
        
        btnBorrowDevice.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
               
                if(btnBorrowDevice.getText().toString().contains("a"))//if user done in taking image              	
                {
                	finish();
                	overridePendingTransition(R.anim.slide_out_right_to_left, R.anim.slide_in_right_to_left);
                }                
                else
                {
                	AlertDialog.Builder alertFirst = new AlertDialog.Builder(BorrowDevice.this);
                    TextView msg = new TextView(BorrowDevice.this);
                    msg.setText("Will device be borrowed?");
                    msg.setPadding(10, 10, 10, 10);
                    msg.setGravity(Gravity.CENTER);
                    msg.setTextSize(18);
                    msg.setTextColor(Color.BLACK);
                    alertFirst.setView(msg);                        
                    
                    alertFirst.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface arg0, int arg1) {                               
                         
//                           Intent intent = new Intent(BorrowDevice.this, CameraProcessing.class);
//                           // Pass all data imei
//                           intent.putExtra("imei", imei);
//                           // Pass all data name_of_model
//                           intent.putExtra("name_of_model",name_of_model);
//                           // Pass all data owner
//                           intent.putExtra("owner",owner);
//                           // Pass all data avatar_of_owner
//                           intent.putExtra("avatar_of_owner",avatar_of_owner);
//                           // Pass all data day_of_owner
//                           intent.putExtra("day_of_owner",day_of_owner);
//                           // Pass all data is_borrow
//                           intent.putExtra("is_borrow",is_borrow);
//                           // Pass all data avatar_of_borrower
//                           intent.putExtra("avatar_of_borrower",avatar_of_borrower);
//                           // Pass all data day_of_borrow
//                           intent.putExtra("day_of_borrow",day_of_borrow);
//                           // Pass all data is_lost
//                           intent.putExtra("is_lost",is_lost);
//                           // Pass all data day_of_lost
//                           intent.putExtra("day_of_lost",day_of_lost);    
//                           //start Activity
//                           startActivity(intent);  
                    	   Intent intent = new Intent(BorrowDevice.this, TeamActivity.class);
                           // Pass all data imei
                           intent.putExtra("imei", imei);
                           // Pass all data name_of_model
                           intent.putExtra("name_of_model",name_of_model);
                           // Pass all data owner
                           intent.putExtra("owner",owner);
                           // Pass all data avatar_of_owner
                           intent.putExtra("avatar_of_owner",avatar_of_owner);
                           // Pass all data day_of_owner
                           intent.putExtra("day_of_owner",day_of_owner);
                           // Pass all data is_borrow
                           intent.putExtra("is_borrow",is_borrow);
                           // Pass all data avatar_of_borrower
                           intent.putExtra("avatar_of_borrower",avatar_of_borrower);
                           // Pass all data day_of_borrow
                           intent.putExtra("day_of_borrow",day_of_borrow);
                           // Pass all data is_lost
                           intent.putExtra("is_lost",is_lost);
                           // Pass all data day_of_lost
                           intent.putExtra("day_of_lost",day_of_lost);    
                           //start Activity
                           startActivity(intent);  
                           finish();
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

