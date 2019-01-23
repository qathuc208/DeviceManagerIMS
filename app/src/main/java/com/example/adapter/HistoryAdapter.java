package com.example.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.activity.DetailOfDevice;
import com.example.adapter.AllDeviceAdapter.ViewHolder;
import com.example.effect.CircleImageView;
import com.example.items.RecentItem;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import example.com.devicemanagerims.R;

public class HistoryAdapter extends BaseAdapter{

	 	Context mContext;
	    LayoutInflater inflater;
	    private List<RecentItem> all_list = null;
	    private ArrayList<RecentItem> arraylist;

	    public HistoryAdapter(Context context, List<RecentItem> all_list) {
	        mContext = context;
	        this.all_list = all_list;
	        inflater = LayoutInflater.from(mContext);
	        this.arraylist = new ArrayList<RecentItem>();
	        this.arraylist.addAll(all_list);
	    }
	    
	    public class ViewHolder {
	       
	        TextView content;
	        ImageView type_image;
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
	        // TODO Auto-generated method stub
	        return position;
	    }

	    public View getView(final int position, View view, ViewGroup parent) {
	    	
	        final ViewHolder holder;
	        if (view == null) {
	            holder = new ViewHolder();
	            view = inflater.inflate(R.layout.template_listview_history, null);
	            // Locate the TextViews
	            holder.content = (TextView) view.findViewById(R.id.text_all_content_asneed);           
	            // Locate the ImageView
	            holder.type_image = (ImageView) view.findViewById(R.id.type_image);    	            
	            
	            view.setTag(holder);
	        } else {
	            holder = (ViewHolder) view.getTag();
	        }
	        // Set the results into TextViews
	        String info= all_list.get(position).getContent()+ " at " +all_list.get(position).getTime();
	        holder.content.setText(info); 
	        // Set the results into ImageView
	        String type=all_list.get(position).getType();
	        switch (Integer.parseInt(type)) {
			case 1:
				holder.type_image.setImageResource(R.drawable.recent_add);
				break;
			case 2:
				holder.type_image.setImageResource(R.drawable.recent_borrow);
				break;
			case 3:
				holder.type_image.setImageResource(R.drawable.recent_delete);
				break;
			case 4:
				holder.type_image.setImageResource(R.drawable.recent_lost);
				break;
			case 5:
				holder.type_image.setImageResource(R.drawable.recent_return);
				break;
			case 6:
				holder.type_image.setImageResource(R.drawable.recent_revert);
				break;
			default:
				break;
			}	        
	        
	        return view;
	    }    

}
