package com.mobileclient.service;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.widget.Toast;

import com.mobileclient.domain.Book;
import com.mobileclient.domain.BookClass;
import com.mobileclient.handler.BookListHandler;
import com.mobileclient.util.HttpUtil;

/*图书信息管理业务逻辑层*/
public class BookService {

	/* 添加图书信息 */
	public String AddBook(Book book) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barcode", book.getBarcode().toString());
		params.put("bookClassId", book.getBookClassId() + "");
		params.put("bookImage", book.getBookImage());
		params.put("bookName", book.getBookName());
		params.put("count", book.getCount() + "");
		params.put("price", book.getPrice() + "");
		params.put("publishDate", book.getPublishDate().toString());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
					+ "BookServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/* 查询图书信息 */
	public List<Book> QueryBook(Book queryConditionBook) throws Exception {
		String urlString = HttpUtil.BASE_URL + "BookServlet?action=query";
		if(queryConditionBook != null) {
			urlString += "&barcode=" + queryConditionBook.getBarcode() + "";
			urlString += "&bookName=" + URLEncoder.encode(queryConditionBook.getBookName(), "UTF-8") + "";
			urlString += "&bookClassId=" + queryConditionBook.getBookClassId();
			if(queryConditionBook.getPublishDate() != null) {
				urlString += "&publishDate=" + URLEncoder.encode(queryConditionBook.getPublishDate().toString(), "UTF-8");
			}
					
		}
		
		URL url = new URL(urlString);

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		BookListHandler bookListHander = new BookListHandler();
		xr.setContentHandler(bookListHander);

		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");

		InputSource is = new InputSource(isr);

		xr.parse(is);

		List<Book> bookList = bookListHander.getBookList();

		return bookList;
	}

	/* 更新图书信息 */
	public String UpdateBook(Book book) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barcode", book.getBarcode().toString());
		params.put("bookClassId", book.getBookClassId() + "");
		params.put("bookImage", book.getBookImage());
		params.put("bookName", book.getBookName());
		params.put("count", book.getCount() + "");
		params.put("price", book.getPrice() + "");
		params.put("publishDate", book.getPublishDate().toString());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
					+ "BookServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/* 删除图书信息 */
	public String DeleteBook(String barcode) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barcode", barcode);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
					+ "BookServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "图书信息删除失败!";
		}
	}

	/* 根据图书条形码获取图书对象 */
	public Book GetBook(String barcode)  {
		List<Book> bookList = new ArrayList<Book>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barcode", barcode);
		params.put("action", "updateQuery");
	 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			byte[] resultByte;
			try {
				resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL
						+ "BookServlet?", params, "UTF-8");
				String result = new String(resultByte, "UTF-8");
				JSONArray array = new JSONArray(result);
				int length = array.length();
				for (int i = 0; i < length; i++) {
					JSONObject object = array.getJSONObject(i);
					Book book = new Book();
					book.setBarcode(barcode);
					book.setBookClassId(object.getInt("bookClassId"));
					book.setBookImage(object.getString("bookImage"));
					book.setBookName(object.getString("bookName"));
					book.setCount(object.getInt("count"));
					book.setPrice((float) object.getDouble("price"));
					String publishDateString = object.getString("publishDate");
					book.setPublishDate(Timestamp.valueOf(publishDateString));
					bookList.add(book);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int size = bookList.size();
			if(size>0) return bookList.get(0); 
			else return null; 
	}
}
