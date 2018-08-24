package com.mobileclient.activity;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.BookClass;
import com.mobileclient.handler.BookClassListHandler;
import com.mobileclient.service.BookService;
import com.mobileclient.util.BookClassSimpleAdapter;
import com.mobileclient.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BookClassListActivity extends Activity {

	BookClassSimpleAdapter adapter;
	ListView lv;
	EditText et;
	String question;
	List<Map<String, Object>> list;
	String bookClassId;

	List<Integer> listItemID = new ArrayList<Integer>();
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookclasslist);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置---图书类别列表");
		} else {
			setTitle("您好：" + username + "   当前位置---图书类别列表");
		}
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		adapter = new BookClassSimpleAdapter(this, list,
				R.layout.bookclass_list_item, new String[] { "bookClassId",
						"bookClassName" }, new int[] { R.id.tv_bookClassId,
						R.id.tv_bookClassName });
		lv.setAdapter(adapter);
		// 添加长按点击
		lv.setOnCreateContextMenuListener(bookClassListItemListener);
	}

	private OnCreateContextMenuListener bookClassListItemListener = new OnCreateContextMenuListener() {

		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "删除图书类别");
			menu.add(0, 1, 0, "编辑图书类别");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {// 删除图书类别
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取图书类别编号
			HashMap<String, Object> hashMap = (HashMap<String, Object>) getDatas()
					.get(position);
			bookClassId = hashMap.get("bookClassId").toString();

			dialog();
		} else if (item.getItemId() == 1) {
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取图书类别编号
			bookClassId = (getDatas().get(position).get("bookClassId").toString());
			Intent intent = new Intent();
			intent.setClass(BookClassListActivity.this,
					BookClassEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("bookClassId", bookClassId);
			intent.putExtras(bundle);
			startActivity(intent);
		}

		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(BookClassListActivity.this);
		builder.setMessage("确认删除吗？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				HashMap<String, String> params = new HashMap<String, String>();
				params.put("bookClassId", bookClassId);
				params.put("action", "delete");
				byte[] resultByte;
				try {
					resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
							+ "BookClassServlet?", params, "UTF-8");
					String result = new String(resultByte, "UTF-8");
					Toast.makeText(getApplicationContext(), result, 1).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
			String urlString = HttpUtil.BASE_URL
					+ "BookClassServlet?action=query";
			URL url = new URL(urlString);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			BookClassListHandler bookClassListHander = new BookClassListHandler();
			xr.setContentHandler(bookClassListHander);

			InputStreamReader isr = new InputStreamReader(url.openStream(),
					"UTF-8");

			InputSource is = new InputSource(isr);

			xr.parse(is);

			List<BookClass> bookClassList = bookClassListHander
					.getBookClassList();

			// 查询返回结果
			// HttpUtil.sendPostRequest(url, null, "UTF-8");
			// String result = HttpUtil.queryStringForPost(url);
			for (int i = 0; i < bookClassList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bookClassId", bookClassList.get(i).getBookClassId());
				map.put("bookClassName", bookClassList.get(i)
						.getBookClassName());
				list.add(map);
			}
		} catch (Exception e) {

			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "添加图书类别");
		menu.add(0, 2, 2, "返回主界面");

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			// 添加图书类别
			Intent intent = new Intent();
			intent.setClass(BookClassListActivity.this,
					BookClassAddActivity.class);
			startActivity(intent);

		} else if (item.getItemId() == 2) {
			Intent intent = new Intent();
			intent.setClass(BookClassListActivity.this, MainMenuActivity.class);
			startActivity(intent);
		}
		return true;

	}
}