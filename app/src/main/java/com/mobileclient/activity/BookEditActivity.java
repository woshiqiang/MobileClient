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

public class BookEditActivity extends Activity {
	// 声明更新按钮
	private Button btnUpdate;
	// 声明图书条形码输入框
	private TextView TV_barcode;
	// 声明图书名称输入框
	private EditText ET_bookName;
	// 声明图书类别下拉框
	private Spinner spinner_bookClassId;
	// 图书价格文本框
	private EditText ET_price;
	// 图书库存文本框
	private EditText ET_count;

	// 出版日期控件
	private DatePicker dp_publishDate;
	// 声明图书图片框
	private ImageView iv_bookImage;
	private Button btn_bookImage;
	private int REQ_CODE_CAMERA = 2;
	protected int REQ_CODE_SELECT_IMAGE = 1;

	private ArrayAdapter<String> bookClassId_adapter;
	private static String[] m = null;
	private List<BookClass> bookClassList = null;

	/* 要保存的图书信息 */
	Book book = new Book();
	protected String carmera_path;

	/* 图书管理业务逻辑层 */
	private BookService bookService = new BookService();

	/* 图书类别管理业务逻辑层 */
	private BookClassService bookClassService = new BookClassService();

	private String barcode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-修改图书信息");
		// 设置当前Activity界面布局
		setContentView(R.layout.book_edit);
		// 通过findViewById方法实例化组件
		try {
			btnUpdate = (Button) findViewById(R.id.BtnUpdate);
			TV_barcode = (TextView) findViewById(R.id.TV_barcode);
			ET_bookName = (EditText) findViewById(R.id.ET_bookName);
			spinner_bookClassId = (Spinner) findViewById(R.id.Spinner_bookClassId);
			ET_price = (EditText) findViewById(R.id.ET_price);
			ET_count = (EditText) findViewById(R.id.ET_count);
			dp_publishDate = (DatePicker) this
					.findViewById(R.id.dp_publishDate);

			iv_bookImage = (ImageView) findViewById(R.id.iv_bookImage);
			btn_bookImage = (Button) findViewById(R.id.btn_bookImage);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		// 获取所有的图片类别
		bookClassList = bookClassService.GetAllBookClass();
		int bookClassCount = bookClassList.size();
		m = new String[bookClassCount];
		for (int i = 0; i < bookClassCount; i++) {
			m[i] = bookClassList.get(i).getBookClassName();
		}

		// 将可选内容与ArrayAdapter连接起来
		bookClassId_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m);

		// 设置图书类别下拉列表的风格
		bookClassId_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中
		spinner_bookClassId.setAdapter(bookClassId_adapter);

		// 添加事件Spinner事件监听
		spinner_bookClassId
				.setOnItemSelectedListener(new BookClassIdSpinnerSelectedListener());

		// 设置默认值
		spinner_bookClassId.setVisibility(View.VISIBLE);

		Bundle extras = this.getIntent().getExtras();
		barcode = extras.getString("barcode");

		initViewDate();

		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {

					/* 验证获取图书名称 */
					if (ET_bookName.getText().toString().equals("")) {
						Toast.makeText(BookEditActivity.this, "图书名称输入不能为空!",
								Toast.LENGTH_LONG).show();
						ET_bookName.setFocusable(true);
						ET_bookName.requestFocus();
						return;
					}
					book.setBookName(ET_bookName.getText().toString());

					/* 验证获取图书价格 */
					if (ET_price.getText().toString().equals("")) {
						Toast.makeText(BookEditActivity.this, "图书价格输入不能为空!",
								Toast.LENGTH_LONG).show();
						ET_price.setFocusable(true);
						ET_price.requestFocus();
						return;
					}
					book.setPrice(Float.parseFloat(ET_price.getText()
							.toString()));

					/* 验证获取图书库存 */
					if (ET_count.getText().toString().equals("")) {
						Toast.makeText(BookEditActivity.this, "图书库存输入不能为空!",
								Toast.LENGTH_LONG).show();
						ET_count.setFocusable(true);
						ET_count.requestFocus();
						return;
					}
					book.setCount(Integer.parseInt(ET_count.getText()
							.toString()));

					/* 获取出版日期 */
					Date publishDate = new Date(dp_publishDate.getYear()-1900,
							dp_publishDate.getMonth(), dp_publishDate
									.getDayOfMonth());
					book.setPublishDate(new Timestamp(publishDate.getTime()));

					
					if (!book.getBookImage().startsWith("upload/")) {
						// 如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						BookEditActivity.this.setTitle("正在上传图片，稍等...");
						String bookImage = HttpUtil.uploadFile(book
								.getBookImage());
						BookEditActivity.this.setTitle("图片上传完毕！");
						book.setBookImage(bookImage);
					}

					/* 调用业务逻辑层上传图书信息 */
					BookEditActivity.this.setTitle("正在更新图书信息，稍等...");
					String result = bookService.UpdateBook(book);
					Toast.makeText(getApplicationContext(), result, 1).show(); 

					/* 操作完成后返回到图书管理界面 */
					Intent intent = new Intent();
					intent.setClass(BookEditActivity.this,
							BookListActivity.class);
					startActivity(intent);
					
					BookEditActivity.this.finish();

				} catch (Exception e) {
				}
			}
		});

		/* 单击图片显示控件时进行图片的选择 */
		iv_bookImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(BookEditActivity.this, photoListActivity.class);
				startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

			}

		});

		btn_bookImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Date date = new Date();
				carmera_path = HttpUtil.FILE_PATH + "/carmera_bookImage.bmp";
				File out = new File(carmera_path);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
				startActivityForResult(intent, REQ_CODE_CAMERA);
			}

		});

	}

	/* 初始化显示编辑界面的数据 */
	private void initViewDate() {
	    book = bookService.GetBook(barcode);
		this.TV_barcode.setText(barcode);
		this.ET_bookName.setText(book.getBookName());
		for (int i = 0; i < bookClassList.size(); i++) {
			if (book.getBookClassId() == bookClassList.get(i).getBookClassId()) {
				this.spinner_bookClassId.setSelection(i);
				break;
			}
		}
		this.ET_price.setText(book.getPrice() + "");
		this.ET_count.setText(book.getCount() + "");
		Date publishDate = new Date(book.getPublishDate().getTime());
		this.dp_publishDate.init(publishDate.getYear() + 1900,
				publishDate.getMonth(), publishDate.getDate(), null);

		byte[] data = null;
		try {
			// 获取图片数据
			data = ImageService.getImage(HttpUtil.BASE_URL
					+ book.getBookImage());
			Bitmap bookImage = BitmapFactory.decodeByteArray(data, 0,
					data.length);
			this.iv_bookImage.setImageBitmap(bookImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA  && resultCode == Activity.RESULT_OK) { 
			carmera_path = HttpUtil.FILE_PATH + "/carmera_bookImage.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();  
			opts.inJustDecodeBounds = true;   
			BitmapFactory.decodeFile(carmera_path, opts);  
			  
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);  
			opts.inJustDecodeBounds = false;  
			try {  
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts); 
				String jpgFileName = "carmera_bookImage.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

				this.iv_bookImage.setImageBitmap(booImageBm); 
				this.iv_bookImage.setScaleType(ScaleType.FIT_CENTER);
				this.book.setBookImage(jpgFileName);
			} catch (OutOfMemoryError err) {  
			}   
		}
 
		if(requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) { 
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();  
			opts.inJustDecodeBounds = true;  
			BitmapFactory.decodeFile(filepath, opts);  
			  
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);  
			opts.inJustDecodeBounds = false;  
			try {  
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_bookImage.setImageBitmap(bm); 
				this.iv_bookImage.setScaleType(ScaleType.FIT_CENTER); 
			    } catch (OutOfMemoryError err) {  
			}  
			 
			book.setBookImage(filename); 
		}
	}
	
	
	// 选择图书类别信息
	class BookClassIdSpinnerSelectedListener implements OnItemSelectedListener { 
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) { 
			book.setBookClassId(bookClassList.get(arg2).getBookClassId()); 
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

}