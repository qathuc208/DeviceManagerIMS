package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.HistoryAdapter.ViewHolder;
import com.example.items.UnitItem;
import com.example.items.RecentItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import example.com.devicemanagerims.R;

public class UnitAdapter extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;
	private List<UnitItem> all_list = null;
	private ArrayList<UnitItem> arraylist;

	public UnitAdapter(Context context, List<UnitItem> all_list) {
		mContext = context;
		this.all_list = all_list;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<UnitItem>();
		this.arraylist.addAll(all_list);
	}

	public class ViewHolder {

		TextView info;		
	}

	@Override
	public int getCount() {

		return all_list.size();

	}

	@Override
	public Object getItem(int position) {
		return all_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();           
            view = inflater.inflate(R.layout.template_listview_unit, null);
            holder.info = (TextView) view.findViewById(R.id.text_all_content_asneed);            
            
            view.setTag(holder);
            
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        String info= all_list.get(position).getInfo();
        holder.info.setText(info); 
        if(position==0)
        {
        	//holder.info.setTextColor(Color.RED);
        	holder.info.setText(info); 
        }
        else
        {
        	holder.info.setText(info); 
        }      
       
        return view;
	}

}
