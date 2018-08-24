package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.activity.BookAddActivity.BookClassIdSpinnerSelectedListener;
import com.mobileclient.domain.Book;
import com.mobileclient.domain.BookClass;
import com.mobileclient.service.BookClassService;
import com.mobileclient.service.BookService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;

public class BookDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明图书条形码控件
	private TextView TV_barcode;
	// 声明图书名称控件
	private TextView TV_bookName;
	// 声明图书类别控件
	private TextView TV_bookClassId;
	// 图书价格控件
	private TextView TV_price;
	// 图书库存控件
	private TextView TV_count;

	// 出版日期控件
	private TextView TV_publishDate;
	// 声明图书图片框
	private ImageView iv_bookImage;
	  
	/* 要保存的图书信息 */
	Book book = new Book(); 
	
	/* 图书管理业务逻辑层 */
	private BookService bookService = new BookService();

	/* 图书类别管理业务逻辑层 */
	private BookClassService bookClassService = new BookClassService();

	private String barcode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看图书信息");
		// 设置当前Activity界面布局
		setContentView(R.layout.book_detail);
		// 通过findViewById方法实例化组件
		try {
			btnReturn = (Button) findViewById(R.id.btnReturn);
			TV_barcode = (TextView) findViewById(R.id.TV_barcode);
			TV_bookName = (TextView) findViewById(R.id.TV_bookName);
			TV_bookClassId = (TextView) findViewById(R.id.TV_bookClassId);
			TV_price = (TextView) findViewById(R.id.TV_price);
			TV_count = (TextView) findViewById(R.id.TV_count);
			TV_publishDate = (TextView) findViewById(R.id.TV_publishDate); 
			iv_bookImage = (ImageView) findViewById(R.id.iv_bookImage); 
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
 
		Bundle extras = this.getIntent().getExtras();
		barcode = extras.getString("barcode");

		initViewDate();

		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BookDetailActivity.this.finish();
			}
		}); 
	}

	/* 初始化显示编辑界面的数据 */
 
	private void initViewDate() {
	    book = bookService.GetBook(barcode); 
		this.TV_barcode.setText(barcode);
		this.TV_bookName.setText(book.getBookName());
		BookClass bookClass = bookClassService.GetBookClass(book.getBookClassId());
		this.TV_bookClassId.setText(bookClass.getBookClassName());
		 
		this.TV_price.setText(book.getPrice() + "");
		this.TV_count.setText(book.getCount() + "");
		Date publishDate = new Date(book.getPublishDate().getTime());
		String publishDateStr = (publishDate.getYear() + 1900) + "-" + (publishDate.getMonth()+1) + "-" + publishDate.getDate();
		this.TV_publishDate.setText(publishDateStr);

		byte[] data = null;
		try {
			// 获取图片数据
			data = ImageService.getImage(HttpUtil.BASE_URL + book.getBookImage());
			Bitmap bookImage = BitmapFactory.decodeByteArray(data, 0,
					data.length);
			this.iv_bookImage.setImageBitmap(bookImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	 

}