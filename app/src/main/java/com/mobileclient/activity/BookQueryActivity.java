package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Book;
import com.mobileclient.domain.BookClass;
import com.mobileclient.handler.BookClassListHandler;
import com.mobileclient.service.BookClassService;
import com.mobileclient.service.BookService;
import com.mobileclient.util.HttpUtil; 

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class BookQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明图书条形码输入框
	private EditText ET_barcode;
	// 声明图书名称输入框
	private EditText ET_bookName;
	// 声明图书类别下拉框
	private Spinner spinner_bookClassId;
	 
	
	// 出版日期控件
	private DatePicker dp_publishDate;
	private CheckBox cb_publishDate;
	 

	private ArrayAdapter<String> bookClassId_adapter;
	private static  String[] m  = null;
	private List<BookClass> bookClassList = null; 
	
	/*图书类别管理业务逻辑层*/
	private BookClassService bookClassService = new BookClassService();
	
	/*查询过滤条件保存到这个对象中*/
	private Book queryConditionBook = new Book();
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.book_query);
		 
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_barcode = (EditText) findViewById(R.id.ET_barcode);
		ET_bookName = (EditText) findViewById(R.id.ET_bookName);
		spinner_bookClassId = (Spinner) findViewById(R.id.Spinner_bookClassId); 
		dp_publishDate = (DatePicker) findViewById(R.id.dp_publishDate);
		cb_publishDate = (CheckBox) findViewById(R.id.cb_publishDate);
		
		// 获取所有的图片类别
		bookClassList = bookClassService.GetAllBookClass();
		int bookClassCount = bookClassList.size();
		m = new String[bookClassCount+1];
		m[0] = "不限制";
		for(int i=1;i<=bookClassCount;i++) { 
			m[i] = bookClassList.get(i-1).getBookClassName();
		} 
		
		
		// 将可选内容与ArrayAdapter连接起来
		bookClassId_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, m);

		// 设置图书类别下拉列表的风格
		bookClassId_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中
		spinner_bookClassId.setAdapter(bookClassId_adapter);

		// 添加事件Spinner事件监听
		spinner_bookClassId.setOnItemSelectedListener(new BookClassIdSpinnerSelectedListener());

		// 设置默认值
		spinner_bookClassId.setVisibility(View.VISIBLE);

		/*单击添加图书按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
				  
					/*获取查询参数*/
					queryConditionBook.setBarcode(ET_barcode.getText().toString());
					queryConditionBook.setBookName(ET_bookName.getText().toString()); 
					if(cb_publishDate.isChecked()) {
						/*获取出版日期*/
						Date publishDate = new Date(dp_publishDate.getYear()-1900,dp_publishDate.getMonth(),dp_publishDate.getDayOfMonth());
						queryConditionBook.setPublishDate(new Timestamp(publishDate.getTime()));
					} else {
						queryConditionBook.setPublishDate(null);
					} 
					 
					/*操作完成后返回到图书管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(BookQueryActivity.this, BookListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionBook", queryConditionBook);
					intent.putExtras(bundle);
					startActivity(intent);    
					
					BookQueryActivity.this.finish();
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

		 
		 
 

	// 使用数组形式操作
	class BookClassIdSpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) { 
			if(arg2 != 0)
				queryConditionBook.setBookClassId(bookClassList.get(arg2-1).getBookClassId()); 
			else
				queryConditionBook.setBookClassId(0);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
		} 
	}

}