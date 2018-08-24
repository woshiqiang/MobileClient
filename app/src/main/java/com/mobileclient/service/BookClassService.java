package com.mobileclient.service;

import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.mobileclient.domain.Book;
import com.mobileclient.domain.BookClass;
import com.mobileclient.handler.BookClassListHandler;
import com.mobileclient.util.HttpUtil;

/*图书管理业务逻辑层*/
public class BookClassService {
 
	public List<BookClass> GetAllBookClass() {

		List<BookClass> bookClassList = null;
		try {
			String urlString = HttpUtil.BASE_URL + "BookClassServlet?action=query";
			URL url = new URL(urlString);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			BookClassListHandler bookClassListHander = new BookClassListHandler();
			xr.setContentHandler(bookClassListHander);
			InputStreamReader isr = new InputStreamReader(url.openStream(),"UTF-8");
			InputSource is = new InputSource(isr);
			xr.parse(is);
			bookClassList = bookClassListHander.getBookClassList();
		} catch (Exception ex) {
		} 
		return bookClassList;
	}
	
	/* 根据图书类别编号获取图书类别信息*/
	public BookClass GetBookClass(int bookClassId)  {
		List<BookClass> bookClassList = new ArrayList<BookClass>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("bookClassId", bookClassId+"");
		params.put("action", "updateQuery"); 
 
			byte[] resultByte;
			try {
				resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
						+ "BookClassServlet?", params, "UTF-8");
				String result = new String(resultByte, "UTF-8");
				JSONArray array = new JSONArray(result);
				int length = array.length();
				for (int i = 0; i < length; i++) {
					JSONObject object = array.getJSONObject(i);
					BookClass bookClass = new BookClass();
					bookClass.setBookClassId(bookClassId);
					bookClass.setBookClassName(object.getString("bookClassName")); 
					bookClassList.add(bookClass);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int size = bookClassList.size();
			if(size>0) return bookClassList.get(0); 
			else return null; 
	}
}
