package com.example.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.example.adapter.AvailableAdapter;
import com.example.database.DataProcessing;
import com.example.items.DeviceItem;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import example.com.devicemanagerims.R;

public class AvailableActivity extends AppCompatActivity {

    ListView list_view_available;
    TextView tv_sum_of_list_view;
    AvailableAdapter adapter;   
    DataProcessing database;
    
    private MenuItem mSearchMenu;
    private SearchView mSearchView = null;
    private EditText mSearchPlate = null;
    
    private int totalElement=0;
    
    ArrayList<DeviceItem> all_list= new ArrayList<DeviceItem>();
    ArrayList<DeviceItem> available_list= new ArrayList<DeviceItem>();
    
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.listview_available);
       
        ActionBar bar = getActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_available)));
        getSupportActionBar().setTitle(R.string.actionbar_available);
        
        list_view_available=(ListView) findViewById(R.id.listview);
        tv_sum_of_list_view=(TextView) findViewById(R.id.tv_sum_of_element_list_view);        
        
        context=AvailableActivity.this;
        
        database= new DataProcessing(context);
        
        all_list= database.getAllDevice();  
        for (DeviceItem item : all_list) {
            
            if(item.getIs_borrow() == null && item.getIs_lost() == null)
            {
                if(available_list.contains(item) == false) available_list.add(item);
            }            
        }   
        
        adapter= new AvailableAdapter(context, available_list);
        
        totalElement=available_list.size();  
        
        list_view_available.setAdapter(adapter);   
        
        if(totalElement==0) tv_sum_of_list_view.setText("No data to display");
        else
            tv_sum_of_list_view.setText("Total has "+totalElement+" devices in list");   
        
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        mSearchMenu = menu.findItem(R.id.action_websearch);
        mSearchView = (SearchView) mSearchMenu.getActionView();
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mSearchView.setLayoutParams(p);
        mSearchView.setIconified(false);
        int searchPlateId = mSearchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        mSearchView.findViewById(searchPlateId).setBackground(getDrawable(R.drawable.edit_text_bg));
        int searchTextId = mSearchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        mSearchPlate = (EditText) mSearchView.findViewById(searchTextId);
        mSearchPlate.setTextColor(getResources().getColor(R.color.blue_light));
        mSearchPlate.setHint(R.string.search_bar);        
        mSearchPlate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = mSearchPlate.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
        
        mSearchView.clearFocus();
        return super.onPrepareOptionsMenu(menu);
    }    
    
    @Override
    protected void onResume() {
       
        all_list= database.getAllDevice();  
        available_list.clear();
        
        for (DeviceItem item : all_list) {
            
            if(item.getIs_borrow() == null && item.getIs_lost() == null)
            {
                if(available_list.contains(item) == false) available_list.add(item);
            }
            
        }
        adapter= new AvailableAdapter(context, available_list);
        
        list_view_available.setAdapter(adapter); 
        
        totalElement=available_list.size();  
        
        list_view_available.setAdapter(adapter);   
        
        if(totalElement==0) 
            {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT , RelativeLayout.TRUE);
                params.setMargins(0, 300, 0, 0);
                tv_sum_of_list_view.setLayoutParams(params);
                tv_sum_of_list_view.setText("No data to display");
            }
        else
            tv_sum_of_list_view.setText("Total has "+totalElement+" devices in list"); 
        
        super.onResume();
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
