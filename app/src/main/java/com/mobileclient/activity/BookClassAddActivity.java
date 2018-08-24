package com.mobileclient.activity;

import java.net.URLEncoder; 
import java.util.HashMap;

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
import android.widget.Toast;

public class BookClassAddActivity extends Activity {
	// 声明确定和重新填写按钮
	private Button btnAdd,btnReset;
	// 声明图书类别输入框
	private EditText bookClassName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-添加图书类别");
		// 设置当前Activity界面布局
		setContentView(R.layout.bookclass_add);
		// 通过findViewById方法实例化组件
		btnAdd = (Button)findViewById(R.id.BtnAdd);
		// 通过findViewById方法实例化组件
		btnReset = (Button)findViewById(R.id.BtnReset);
		// 通过findViewById方法实例化组件
		bookClassName = (EditText)findViewById(R.id.ET_bookClassName);
	 
		
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					
					if(bookClassName.getText()!=null ){
						 
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("bookClassName", bookClassName.getText().toString());
						params.put("action", "add");
						byte[] resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
								+ "BookClassServlet?", params, "UTF-8"); 
						String result = new String(resultByte,"UTF-8");
						Toast.makeText(getApplicationContext(), result, 1).show(); 
						
						Intent intent = new Intent();
						intent.setClass(BookClassAddActivity.this, BookClassListActivity.class);
						startActivity(intent);

					}else{
						Toast.makeText(getApplicationContext(), "不能为空", 1).show();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		
		btnReset.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				bookClassName.setText("");
			}
		}); 
	}
	
	 
}