package com.example.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.activity.DetailOfDevice;
import com.example.activity.LostDevice;
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

public class BorrowAdapter extends BaseAdapter {
    
    Context mContext;
    LayoutInflater inflater;
    private List<DeviceItem> borrow_list = null;
    private ArrayList<DeviceItem> arraylist;

    public BorrowAdapter(Context context, List<DeviceItem> borrow_list) {
        mContext = context;
        this.borrow_list = borrow_list;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<DeviceItem>();
        this.arraylist.addAll(borrow_list);
    }
    
    public class ViewHolder {
       
        TextView day_of_owner;
        de.hdodenhof.circleimageview.CircleImageView avatar_of_borrower;
    }
    
    @Override
    public int getCount() {
        
        return borrow_list.size();
    }

    @Override
    public Object getItem(int position) {
        return borrow_list.get(position);
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
            holder.avatar_of_borrower = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.ava_template);
            holder.avatar_of_borrower.setBorderWidth(5);
            holder.avatar_of_borrower.setBorderColorResource(R.color.color_borrow);
          
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        String info= "Model: "+borrow_list.get(position).getName_of_model()+"\n"+"Borrow at: "+borrow_list.get(position).getDay_of_borrow();
        holder.day_of_owner.setText(info); 
        // Set the results into ImageView
        holder.avatar_of_borrower.setImageResource(Integer.parseInt(borrow_list.get(position).getAvatar_of_owner()));
        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to DeviceDetail Class
                Intent intent = new Intent(mContext, LostDevice.class);
                // Pass all data imei
                intent.putExtra("imei", (borrow_list.get(position).getImei()));
                // Pass all data name_of_model
                intent.putExtra("name_of_model",(borrow_list.get(position).getName_of_model()));
                // Pass all data owner
                intent.putExtra("owner",(borrow_list.get(position).getOwner()));
                // Pass all data avatar_of_owner
                intent.putExtra("avatar_of_owner",(borrow_list.get(position).getAvatar_of_owner()));
                // Pass all data day_of_owner
                intent.putExtra("day_of_owner",(borrow_list.get(position).getDay_of_owner()));
                // Pass all data is_borrow
                intent.putExtra("is_borrow",(borrow_list.get(position).getIs_borrow()));
                // Pass all data borrower
                intent.putExtra("borrower",(borrow_list.get(position).getBorrower()));
                // Pass all data avatar_of_borrower
                intent.putExtra("avatar_of_borrower",(borrow_list.get(position).getAvatar_of_borrower()));
                // Pass all data day_of_borrow
                intent.putExtra("day_of_borrow",(borrow_list.get(position).getDay_of_borrow()));
                // Pass all data is_lost
                intent.putExtra("is_lost",(borrow_list.get(position).getIs_lost()));
                // Pass all data day_of_lost
                intent.putExtra("day_of_lost",(borrow_list.get(position).getDay_of_lost()));             
                
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
        borrow_list.clear();
        if (charText.length() == 0) {
            borrow_list.addAll(arraylist);
        }
        else {            
            
                for (DeviceItem item : arraylist) {
                    if (item.getName_of_model().toLowerCase(Locale.getDefault()).contains(charText)
                        ||item.getOwner().toLowerCase(Locale.getDefault()).contains(charText)) {
                        
                        if(borrow_list.contains(item) == false)
                            borrow_list.add(item);
                    }
                }                 
        }
        notifyDataSetChanged();
    }

}
