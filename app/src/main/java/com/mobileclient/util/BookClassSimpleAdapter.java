package com.mobileclient.util;

 
import java.util.HashMap;
import java.util.List;   
import java.util.Map;   

import com.mobileclient.activity.R;
 

 
import android.content.Context;
import android.view.LayoutInflater;   
import android.view.View;   
import android.view.ViewGroup;   
import android.widget.Checkable;   
import android.widget.ImageView;   
import android.widget.SimpleAdapter;   
import android.widget.TextView;   
  
public class BookClassSimpleAdapter extends SimpleAdapter {   
	 
    private int[] mTo;   
    private String[] mFrom;   
    
    private List<? extends Map<String, ?>> mData;   
    
    private LayoutInflater mInflater;  
    HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    Context context = null;
    int index=0;
    public BookClassSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {   
        super(context, data, resource, from, to);   
       
        mTo = to;   
        mFrom = from;   
        mData = data;   
         
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        this.context= context;
    }  
    
    public View getView(int position, View convertView, ViewGroup parent) {   
    	View view;
		ViewHolder holder = null;
		
		System.out.println("getView " + position + " " + convertView);
		if(convertView == null) { 
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.bookclass_list_item, null);
			System.out.println("address: " + convertView);
			holder = new ViewHolder(); 
			holder.tv_bookClassId = (TextView)convertView.findViewById(R.id.tv_bookClassId);
			holder.tv_bookClassName = (TextView)convertView.findViewById(R.id.tv_bookClassName);
            convertView.setTag(holder); 
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.tv_bookClassId.setText(mData.get(position).get("bookClassId").toString());
		holder.tv_bookClassName.setText(mData.get(position).get("bookClassName").toString());
		
		
		return convertView;
		/*
		if (map.get(position) == null) {
			System.out.println("contextcontext  "+context);
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mInflater.inflate(R.layout.bookclass_list_item, null);
			holder = new ViewHolder();
			 
			holder.tv_bookClassId = (TextView)view.findViewById(R.id.tv_bookClassId);
			holder.tv_bookClassName = (TextView)view.findViewById(R.id.tv_bookClassName);
		 
			final int p = position;
			System.out.println("pppppppppppppppppp   "+p);
			map.put(position, view);
			 
			view.setTag(holder);
		}else{
			view = map.get(position);
			holder = (ViewHolder)view.getTag();
		}
		
		 
		holder.tv_bookClassId.setText(mData.get(position).get("bookClassId").toString());
		holder.tv_bookClassName.setText(mData.get(position).get("bookClassName").toString());
		 
		index++;
		System.out.println("indexindexindexindexindexindexindex   "+index);
		return view;  
		*/
		
		
    }   
    
    static class ViewHolder{
    	 
    	TextView tv_bookClassId;
    	TextView tv_bookClassName;
    }

   
}  
