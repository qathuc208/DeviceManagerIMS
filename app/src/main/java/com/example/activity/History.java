package com.example.activity;

import java.util.ArrayList;

import com.example.adapter.HistoryAdapter;
import com.example.database.DataRecent;
import com.example.items.RecentItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ViewSwitcher.ViewFactory;

import example.com.devicemanagerims.R;

public class History extends AppCompatActivity {

	ListView listview_history;
	ArrayList<RecentItem> all_history_list = new ArrayList<RecentItem>();
	Context context;
	DataRecent data_recent;
	HistoryAdapter adapter_history;
	TextSwitcher text_switch;
	boolean check=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);       
		setContentView(R.layout.listview_history);
		listview_history = (ListView) findViewById(R.id.listview_history);
		text_switch=(TextSwitcher) findViewById(R.id.text_switch);		
		context = History.this;	
		
		text_switch.setInAnimation(context, android.R.anim.slide_in_left);
		text_switch.setOutAnimation(context, android.R.anim.slide_out_right);
		
		text_switch.setFactory(new ViewFactory() {
            
            public View makeView() {
                
                TextView myText = new TextView(History.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER);
                myText.setTextSize(25);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });		
		text_switch.setText("This is history");
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
			            	text_switch.setText("History");
			            else
			            	text_switch.setText("You can find anything");
			          }
			        });
			      }
			    } catch (InterruptedException e) {
			    }
			  }
			};

		t.start();		
		
		data_recent = new DataRecent(context);
		all_history_list = data_recent.getAllHistory();
		adapter_history = new HistoryAdapter(context, all_history_list);
		listview_history.setAdapter(adapter_history);
		
		listview_history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				String imei=all_history_list.get(position).getImei();
				
				 AlertDialog.Builder alert = new AlertDialog.Builder(History.this);
	                TextView msg = new TextView(History.this);
	                msg.setText("IMEI: "+imei);
	                msg.setPadding(10, 10, 10, 10);
	                msg.setGravity(Gravity.CENTER);
	                msg.setTextSize(18);
	                msg.setTextColor(Color.BLACK);
	                alert.setView(msg);                        
	                
	                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                   @Override
	                   public void onClick(DialogInterface arg0, int arg1) {                               
	                     	                                            
	                   }
	                });  
	                
	                AlertDialog alertDialog = alert.create();
	                alertDialog.show();
				
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
