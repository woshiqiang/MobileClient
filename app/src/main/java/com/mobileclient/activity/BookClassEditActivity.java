package com.mobileclient.activity;

import java.net.URLEncoder; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.BookClass;
import com.mobileclient.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BookClassEditActivity extends Activity {
	// 声明确定和重新填写按钮
	private Button btnUpdate,btnCancle;
	//声明图书类别编号TextView
	private TextView tv_bookClassId;
	// 声明图书类别输入框
	private EditText et_bookClassName;
	
	private String bookClassId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置标题
		setTitle("手机客户端-修改图书类别");
		// 设置当前Activity界面布局
		setContentView(R.layout.bookclass_edit);
		// 通过findViewById方法实例化组件
		btnUpdate = (Button)findViewById(R.id.BtnUpdate);
		// 通过findViewById方法实例化组件
		btnCancle = (Button)findViewById(R.id.BtnCancle);
		// 通过findViewById方法实例化组件
		tv_bookClassId = (TextView)findViewById(R.id.TV_bookClassId);
		// 通过findViewById方法实例化组件
		et_bookClassName = (EditText)findViewById(R.id.ET_bookClassName);
	 
		
		Bundle extras = this.getIntent().getExtras();
		bookClassId = extras.getString("bookClassId");
		     
		
		initViewDate();
		
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					
					if(et_bookClassName.getText()!=null ){ 
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("bookClassId", bookClassId);
						params.put("bookClassName", et_bookClassName.getText().toString());
						params.put("action", "update");
						byte[] resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
								+ "BookClassServlet?", params, "UTF-8"); 
						String result = new String(resultByte,"UTF-8");
						Toast.makeText(getApplicationContext(), result, 1).show(); 
						
						Intent intent = new Intent();
						intent.setClass(BookClassEditActivity.this, BookClassListActivity.class);
						startActivity(intent);
						finish();

					}else{
						Toast.makeText(getApplicationContext(), "不能为空", 1).show();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		
		btnCancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		}); 
	}
	
	
	/*初始化显示编辑界面的数据*/
	private void initViewDate() { 
		
		List<BookClass> bookClassList = new ArrayList<BookClass>();
 
		//根据图书类别编号查询类别信息
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("bookClassId", bookClassId); 
		params.put("action", "updateQuery");
		try {
			byte[] resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
					+ "BookClassServlet?", params, "UTF-8");
			String result = new String(resultByte,"UTF-8");
			
			JSONArray array = new JSONArray(result);  
		    int length = array.length();  
		    for(int i=0;i<length;i++){  
		            JSONObject object = array.getJSONObject(i);
		            BookClass bookClass = new BookClass();
		            bookClass.setBookClassId(object.getInt("bookClassId"));
		            bookClass.setBookClassName(object.getString("bookClassName"));
		            bookClassList.add(bookClass);      
		    }
		    
		    this.tv_bookClassId.setText(String.valueOf(bookClassList.get(0).getBookClassId()));
		    this.et_bookClassName.setText(bookClassList.get(0).getBookClassName());
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	 
}