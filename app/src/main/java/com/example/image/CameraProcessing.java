package com.example.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import com.example.activity.BorrowDevice;
import com.example.activity.DetailOfDevice;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import example.com.devicemanagerims.R;

public class CameraProcessing extends Activity {

    int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=100;
    Button btnCapture, btnOk;
    ImageView img_capture_result;
    Intent imageIntent;
    Uri uriSavedImage;
    static boolean check_if_capture=false;
    
    String imei, name_of_model, owner, avatar_of_owner, day_of_owner, is_borrow,
    avatar_of_borrower, day_of_borrow, is_lost, day_of_lost;
    
    TextSwitcher text_switch;  
    Context context;
    boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_item);
        text_switch=(TextSwitcher) findViewById(R.id.tw_intro_camera);
        context = CameraProcessing.this;	
        text_switch.setInAnimation(this, android.R.anim.fade_in);
		text_switch.setOutAnimation(this, android.R.anim.fade_out);
		
		text_switch.setFactory(new ViewFactory() {
            
            public View makeView() {
                
                TextView myText = new TextView(context);
                myText.setGravity(Gravity.TOP | Gravity.CENTER);
                myText.setTextSize(25);
                myText.setTextColor(Color.WHITE);
                return myText;
            }
        });		
		text_switch.setText("Take picture from borrrower");
		Thread t = new Thread() {

			  @Override
			  public void run() {
			    try {
			      while (!isInterrupted()) {
			        Thread.sleep(4000);
			        runOnUiThread(new Runnable() {
			          @Override
			          public void run() {
			            if(check==true) 
			            	check=false;
			            else 
			            	check=true;			            
			            if(check==true) 
			            	text_switch.setText("Take picture from borrrower");
			            else
			            	text_switch.setText("If want, you can recapture");
			          }
			        });
			      }
			    } catch (InterruptedException e) {
			    }
			  }
			};

		t.start();	 
        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnOk = (Button) findViewById(R.id.btnOK);
        img_capture_result = (ImageView) findViewById(R.id.image_result);
        
        Intent i = getIntent();
        
        imei = i.getStringExtra("imei"); 
        name_of_model = i.getStringExtra("name_of_model");  
        owner = i.getStringExtra("owner");  
        avatar_of_owner = i.getStringExtra("avatar_of_owner");  
        day_of_owner = i.getStringExtra("day_of_owner");  
        is_borrow = i.getStringExtra("is_borrow");  
        avatar_of_borrower = i.getStringExtra("avatar_of_borrower");  
        day_of_borrow = i.getStringExtra("day_of_borrow");  
        is_lost = i.getStringExtra("is_lost");  
        day_of_lost = i.getStringExtra("day_of_lost");
        
      //camera stuff
        imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //folder stuff
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "DeviceManagement");
        if(imagesFolder.isDirectory() == false) imagesFolder.mkdirs();
        File image = new File(imagesFolder, imei+".jpeg");                
        avatar_of_borrower=image.getAbsolutePath();
        image.delete();                
        uriSavedImage = Uri.fromFile(image);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        btnCapture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {                
                
                //camera stuff
                imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //folder stuff
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "DeviceManagement");
                if(imagesFolder.isDirectory() == false) imagesFolder.mkdirs();
                File image = new File(imagesFolder, imei+".jpeg");                
                avatar_of_borrower=image.getAbsolutePath();
                image.delete();                
                uriSavedImage = Uri.fromFile(image);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });        
        
        btnOk.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
               
                if(avatar_of_borrower != null && check_if_capture==true)
                {
                    Intent intent = new Intent(CameraProcessing.this, BorrowDevice.class);                
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
                    Calendar c = Calendar.getInstance(); 
                    int year=c.get(Calendar.YEAR);
                    int month=c.get(Calendar.MONTH)+1;
                    int day=c.get(Calendar.DAY_OF_MONTH);                    
                    day_of_borrow=String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
                    intent.putExtra("day_of_borrow",day_of_borrow);
                    // Pass all data is_lost
                    intent.putExtra("is_lost",is_lost);
                    // Pass all data day_of_lost
                    intent.putExtra("day_of_lost",day_of_lost);    
                    // Pass url of avataar
                    intent.putExtra("avatar_from_camera", avatar_of_borrower);  
                    //start Activity
                    startActivity(intent);   
                    finish();
                }
                else
                {
                    Toast.makeText(CameraProcessing.this, "You have to capture an image", Toast.LENGTH_LONG).show();
                }
                    
            }
        });

    }    
   

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==Activity.RESULT_OK)
        {  
        	check_if_capture=true;
            BitmapFactory.Options options = new BitmapFactory.Options(); 
            options.inPreferredConfig = Bitmap.Config.ARGB_8888; 
            Bitmap bitmap = BitmapFactory.decodeFile(avatar_of_borrower, options); 
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 270, 480, true);
            img_capture_result.setImageBitmap(resized); 
            img_capture_result.setRotation(90);
          
        }        
    }   
}
