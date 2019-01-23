package com.example.activity;

import java.util.ArrayList;
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

public class TeamActivity extends AppCompatActivity {

	String imei, name_of_model, owner, avatar_of_owner, day_of_owner, is_borrow,
    avatar_of_borrower, day_of_borrow, is_lost, day_of_lost;
	Context context;
	TextSwitcher text_switch;
	ListView listview_team;
	UnitAdapter unit_adapter;
	int check = 0;
	Random r = new Random();
	ArrayList<UnitItem> unit_list= new ArrayList<UnitItem>();

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
		
        setContentView(R.layout.listview_unit);
		text_switch = (TextSwitcher) findViewById(R.id.text_switch_unit);
		context = TeamActivity.this;
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
		text_switch.setText("Chose a team");
		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(3000);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {

								check = r.nextInt(10);
								switch (check) {
								case 0:
									text_switch.setText("Call Team");
									break;
								case 1:
									text_switch
											.setText("Homescreen & Settings Team");
									break;
								case 2:
									text_switch.setText("GMS/3rd Party Team");
									break;
								case 3:
									text_switch.setText("Contact Team");
									break;
								case 4:
									text_switch.setText("Splanner Team");
									break;	
								case 5:
									text_switch.setText("Messaging Team");
									break;		
								case 6:
									text_switch.setText("Organizer Team");
									break;		
								case 7:
									text_switch.setText("Smart Manager Team");
									break;		
								case 8:
									text_switch.setText("Media Apps Team");
									break;		
								case 9:
									text_switch.setText("");
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
		listview_team= (ListView) findViewById(R.id.listview_unit);		
		String[] mTestArray = getResources().getStringArray(R.array.team_arrays);
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
		listview_team.setAdapter(unit_adapter);
		
		listview_team.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				 String team= unit_list.get(position).getInfo();
				
				 Intent intent = new Intent(context, MemberActivity.class);
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
                 // Pass team
                 intent.putExtra("team",team);    
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
                intent.putExtra("day_of_borrow",day_of_borrow);
                // Pass all data is_lost
                intent.putExtra("is_lost",is_lost);
                // Pass all data day_of_lost
                intent.putExtra("day_of_lost",day_of_lost);                
                //start Activity
                startActivity(intent);  
                //finish();	
                overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
		        finish();
		        overridePendingTransition(R.anim.slide_out_right_to_left, R.anim.slide_in_right_to_left);
		    }
		    return super.onKeyDown(keyCode, event);
		}
}
