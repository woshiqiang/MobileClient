package com.mobileclient.activity;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
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
import com.mobileclient.handler.BookListHandler;
import com.mobileclient.service.BookService;
import com.mobileclient.util.BookClassSimpleAdapter;
import com.mobileclient.util.BookSimpleAdapter;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView.ScaleType;

public class BookListActivity extends Activity {

	BookSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String barcode;

	/* 图书操作业务逻辑层对象 */
	BookService bookService = new BookService();
	
	/*保存查询参数条件的图书对象*/
	private Book queryConditionBook;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置---图书列表");
		} else {
			setTitle("您好：" + username + "   当前位置---图书列表");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionBook = (Book)extras.getSerializable("queryConditionBook");
		 
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();

		try {
			adapter = new BookSimpleAdapter(this, list,
					R.layout.book_list_item, new String[] { "barcode",
							"bookClassId", "bookName", "bookImage" },
					new int[] { R.id.tv_barcode, R.id.tv_bookClassId,
							R.id.tv_bookName, R.id.iv_bookImage });
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// 添加长按点击
		lv.setOnCreateContextMenuListener(bookListItemListener);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
 
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) { 
            	String barcode = list.get(arg2).get("barcode").toString();
            	Intent intent = new Intent();
    			intent.setClass(BookListActivity.this, BookDetailActivity.class);
    			Bundle bundle = new Bundle();
    			bundle.putString("barcode", barcode);
    			intent.putExtras(bundle);
    			startActivity(intent); 
            }
             
        });
	}

	private OnCreateContextMenuListener bookListItemListener = new OnCreateContextMenuListener() {

		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑图书信息"); 
			menu.add(0, 1, 0, "删除图书信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑图书信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取图书条形码
			barcode = list.get(position).get("barcode").toString();
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, BookEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("barcode", barcode);
			intent.putExtras(bundle);
			startActivity(intent);
			BookListActivity.this.finish();
			
		} else if (item.getItemId() == 1) {// 删除图书
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取图书条形码
			HashMap<String, Object> hashMap = (HashMap<String, Object>) list
					.get(position);
			barcode = hashMap.get("barcode").toString();
			dialog();
		}

		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(BookListActivity.this);
		builder.setMessage("确认删除吗？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				String result = bookService.DeleteBook(barcode);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Declare declare = (Declare) getApplicationContext();
			int myid = declare.getId();
			System.out.println("myid          =" + myid);
			/* 查询图书信息 */
			List<Book> bookList = bookService.QueryBook(queryConditionBook);
			for (int i = 0; i < bookList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("barcode", bookList.get(i).getBarcode());
				map.put("bookClassId", bookList.get(i).getBookClassId());
				map.put("bookName", bookList.get(i).getBookName());
				byte[] data = ImageService.getImage(HttpUtil.BASE_URL
						+ bookList.get(i).getBookImage());// 获取图片数据
				
 
				BitmapFactory.Options opts = new BitmapFactory.Options();  
				opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(data, 0, data.length, opts);
				//BitmapFactory.decodeFile(filepath, opts);  
				  
				opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 100*100);  
				opts.inJustDecodeBounds = false;  
				try {  
					//Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
					Bitmap bookImage = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
					map.put("bookImage", bookImage);
					//this.iv_bookImage.setImageBitmap(bm); 
					//this.iv_bookImage.setScaleType(ScaleType.FIT_CENTER); 
				    } catch (OutOfMemoryError err) {  
				}   
				//Bitmap bookImage = BitmapFactory.decodeByteArray(data, 0,data.length);
				
				list.add(map);
			}
		} catch (Exception e) {

			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加图书");
		menu.add(0, 2, 2, "查询图书");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			// 添加图书信息
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, BookAddActivity.class);
			startActivity(intent);
			BookListActivity.this.finish();

		} else if (item.getItemId() == 2) {
			/*查询图书信息*/
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, BookQueryActivity.class);
			startActivity(intent);
			BookListActivity.this.finish();

		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(BookListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			BookListActivity.this.finish();
		}
		return true; 
	}
}