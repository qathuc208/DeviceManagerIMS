package com.example.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.activity.DetailOfDevice;
import com.example.effect.CircleImageView;
import com.example.items.DeviceItem;

import android.app.Activity;
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

public class AvailableAdapter extends BaseAdapter {
    
    Context mContext;
    LayoutInflater inflater;
    private List<DeviceItem> available_list = null;
    private ArrayList<DeviceItem> arraylist;

    public AvailableAdapter(Context context, List<DeviceItem> available_list) {
        mContext = context;
        this.available_list = available_list;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<DeviceItem>();
        this.arraylist.addAll(available_list);
    }
    
    public class ViewHolder {
       
        TextView day_of_owner;
        de.hdodenhof.circleimageview.CircleImageView avatar_of_owner;
    }
    
    @Override
    public int getCount() {
        
        return available_list.size();
    }

    @Override
    public Object getItem(int position) {
        return available_list.get(position);
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
            view = inflater.inflate(R.layout.template_listview, null);
            // Locate the TextViews
            holder.day_of_owner = (TextView) view.findViewById(R.id.text_all_info_asneed);           
            // Locate the ImageView
            holder.avatar_of_owner = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.ava_template);
            holder.avatar_of_owner.setBorderWidth(5);
            holder.avatar_of_owner.setBorderColorResource(R.color.color_available);
           
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        String info= "Model: "+available_list.get(position).getName_of_model()+"\n"+"Create at: "+available_list.get(position).getDay_of_owner();
        holder.day_of_owner.setText(info); 
        // Set the results into ImageView
        holder.avatar_of_owner.setImageResource(Integer.parseInt(available_list.get(position).getAvatar_of_owner()));
        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to DeviceDetail Class
                Intent intent = new Intent(mContext, DetailOfDevice.class);
                // Pass all data imei
                intent.putExtra("imei", (available_list.get(position).getImei()));
                // Pass all data name_of_model
                intent.putExtra("name_of_model",(available_list.get(position).getName_of_model()));
                // Pass all data owner
                intent.putExtra("owner",(available_list.get(position).getOwner()));
                // Pass all data avatar_of_owner
                intent.putExtra("avatar_of_owner",(available_list.get(position).getAvatar_of_owner()));
                // Pass all data day_of_owner
                intent.putExtra("day_of_owner",(available_list.get(position).getDay_of_owner()));
                // Pass all data is_borrow
                intent.putExtra("is_borrow",(available_list.get(position).getIs_borrow()));
                // Pass all data borrower
                intent.putExtra("borrower",(available_list.get(position).getBorrower()));
                // Pass all data avatar_of_borrower
                intent.putExtra("avatar_of_borrower",(available_list.get(position).getAvatar_of_borrower()));
                // Pass all data day_of_borrow
                intent.putExtra("day_of_borrow",(available_list.get(position).getDay_of_borrow()));
                // Pass all data is_lost
                intent.putExtra("is_lost",(available_list.get(position).getIs_lost()));
                // Pass all data day_of_lost
                intent.putExtra("day_of_lost",(available_list.get(position).getDay_of_lost()));             
                
                //start Activity
                Activity activity = (Activity) mContext;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        available_list.clear();
        if (charText.length() == 0) {
            available_list.addAll(arraylist);
        }
        else {            
            
                for (DeviceItem item : arraylist) {
                    if (item.getName_of_model().toLowerCase(Locale.getDefault()).contains(charText)
                        ||item.getOwner().toLowerCase(Locale.getDefault()).contains(charText)) {
                        
                        if(available_list.contains(item) == false)
                            available_list.add(item);
                    }
                }                 
        }
        notifyDataSetChanged();
    }

}
