package com.example.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import com.example.adapter.UnitAdapter;
import com.example.items.UnitItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import example.com.devicemanagerims.R;

public class MemberActivity extends AppCompatActivity {

	String imei, name_of_model, owner, avatar_of_owner, day_of_owner, is_borrow,
    avatar_of_borrower, day_of_borrow, is_lost, day_of_lost, team;
	Context context;
	TextSwitcher text_switch;
	ListView listview_member;
	UnitAdapter unit_adapter;
	int check = 0;
	Random r = new Random();
	ArrayList<UnitItem> unit_list= new ArrayList<UnitItem>();
	String[] mTestArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//get data
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
        team = i.getStringExtra("team");
		
        setContentView(R.layout.listview_unit);
		text_switch = (TextSwitcher) findViewById(R.id.text_switch_unit);
		context = MemberActivity.this;
		text_switch.setInAnimation(this, android.R.anim.slide_in_left);
		text_switch.setOutAnimation(this, android.R.anim.slide_out_right);

		text_switch.setFactory(new ViewFactory() {

			public View makeView() {

				TextView myText = new TextView(context);
				myText.setGravity(Gravity.TOP | Gravity.CENTER);
				myText.setTextSize(20);
				myText.setTextColor(Color.BLACK);
				return myText;
			}
		});
		text_switch.setText("Chose a member");
		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(3000);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {

								check = r.nextInt(5);
								switch (check) {
								case 0:
									text_switch.setText(team +" Team");
									break;
								case 1:
									text_switch.setText("Chose a member from "+team +" Team");
									break;
								case 2:
									text_switch.setText(team +" Team");
									break;
								case 3:
									text_switch.setText(team +" Team");
									break;
								case 4:
									text_switch
											.setText("");
									break;								

								default:
									break;
								}

							}
						});
					}
				} catch (InterruptedException e) {
				}
			}
		};

		t.start();
		////////////////////////list view
		listview_member= (ListView) findViewById(R.id.listview_unit);	
		switch (team.toLowerCase()) {
		case "call":
			mTestArray = getResources().getStringArray(R.array.call_arrays);
			break;
		case "contact":
			mTestArray = getResources().getStringArray(R.array.contact_arrays);
			break;
		case "gms/3rd party":
			mTestArray = getResources().getStringArray(R.array.gms_3rd_arrays);
			break;
		case "homescreen and setting":
			mTestArray = getResources().getStringArray(R.array.home_setting_arrays);
			break;
		case "media apps":
			mTestArray = getResources().getStringArray(R.array.media_app_arrays);
			break;
		case "messaging":
			mTestArray = getResources().getStringArray(R.array.message_arrays);
			break;
		case "organizer":
			mTestArray = getResources().getStringArray(R.array.organizer_arrays);
			break;
		case "smart manager":
			mTestArray = getResources().getStringArray(R.array.smart_manager_arrays);
			break;
		case "splanner":
			mTestArray = getResources().getStringArray(R.array.splanner_arrays);
			break;

		default:
			break;
		}
		
		ArrayList<String> string_list= new ArrayList<String>();
		for (int in = 0; in < mTestArray.length; in++) {
			String string = mTestArray[in];
			string_list.add(string);		
		}
		Collections.sort(string_list);		
		for (String name : string_list) {
			UnitItem unit= new UnitItem(name);
			unit_list.add(unit);	
		}				
		unit_adapter= new UnitAdapter(context, unit_list);
		listview_member.setAdapter(unit_adapter);
		
		listview_member.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				 String name_borrower= unit_list.get(position).getInfo()+" from "+team+" Team";
				
				 Intent intent = new Intent(context, BorrowDevice.class);
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
                 // Pass team
                 intent.putExtra("borrower",name_borrower);    
                 //start Activity
                 startActivity(intent);  
                 finish();	
                 overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
			}
		});
		
		
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
