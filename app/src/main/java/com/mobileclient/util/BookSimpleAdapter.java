package com.mobileclient.util;

 
import java.util.HashMap;
import java.util.List;   
import java.util.Map;   

import com.mobileclient.activity.R;
 

 
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;   
import android.view.View;   
import android.view.ViewGroup;   
import android.widget.Checkable;   
import android.widget.ImageView;   
import android.widget.SimpleAdapter;   
import android.widget.TextView;   
  
public class BookSimpleAdapter extends SimpleAdapter {   
	 
    private int[] mTo;   
    private String[] mFrom;   
    
    private List<? extends Map<String, ?>> mData;   
    
    private LayoutInflater mInflater;  
    HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    Context context = null;
    int index=0;
    public BookSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {   
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
		
		if (convertView == null) {
			System.out.println("contextcontext  "+context);
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.book_list_item, null);
			holder = new ViewHolder();
			 
			 
			try {
				
				holder.tv_barcode = (TextView)convertView.findViewById(R.id.tv_barcode); 
				holder.tv_bookClassId = (TextView)convertView.findViewById(R.id.tv_bookClassId);
				holder.tv_bookName = (TextView)convertView.findViewById(R.id.tv_bookName);
				holder.iv_bookImage = (ImageView)convertView.findViewById(R.id.iv_bookImage);
			
			}
			catch(Exception ex) {
				System.out.print(ex.toString());
			}
			
		 
			final int p = position;
			System.out.println("pppppppppppppppppp   "+p); 
			 
			convertView.setTag(holder);
		}else{
			 
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.tv_barcode.setText(mData.get(position).get("barcode").toString());
		
		String url = HttpUtil.BASE_URL + "BookClassServlet?action=GetBookClassName&bookClassId=" + mData.get(position).get("bookClassId").toString();
		String bookClassName = HttpUtil.queryStringForPost(url); 
		
		holder.tv_bookClassId.setText(bookClassName);
		holder.tv_bookName.setText(mData.get(position).get("bookName").toString());
		holder.iv_bookImage.setImageBitmap((Bitmap)mData.get(position).get("bookImage"));

		index++;
		System.out.println("indexindexindexindexindexindexindex   "+index);
		return convertView;  
    }   
    
    static class ViewHolder{
    	 
    	TextView tv_barcode;  
    	TextView tv_bookClassId;
    	TextView tv_bookName;
    	ImageView iv_bookImage;
    	
    }

   
}  
