package com.mobileclient.handler;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;

import com.mobileclient.domain.Book;
import com.mobileclient.domain.BookClass;

 
public class BookListHandler extends DefaultHandler {
	
	private List<Book> bookList = null;
	
	private Book book;
	
	private String tempString;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		if (book != null) {    
            String valueString = new String(ch, start, length);     
            if ("barcode".equals(tempString))     
                // 如果当前解析到的节点是barcode 就要将barcode中的文本节点元素的值得到    
            	book.setBarcode(valueString);
               //bookClass.setBookClassId(new Integer(valueString).intValue());
            else if ("bookName".equals(tempString)) 
            	book.setBookName(valueString); 
            else if("bookClassId".equals(tempString)) 
            	book.setBookClassId(new Integer(valueString).intValue());
            else if("price".equals(tempString)) 
            	book.setPrice(new Float(valueString).floatValue());
            else if("count".equals(tempString))
            	book.setCount(new Integer(valueString).intValue());
            else if("publishDate".equals(tempString)) { 
            	book.setPublishDate(Timestamp.valueOf(valueString));
            } else if("bookImage".equals(tempString))
            	book.setBookImage(valueString); 
        }    
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		 if("Book".equals(localName)&&book!=null)    
	        {    
	            bookList.add(book);    
	            book = null;    
	        }    
	        tempString = null;  
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		bookList = new ArrayList<Book>();
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		// 先判断读到的元素是否是person    
        if ("Book".equals(localName)) {    
            // 如果读到的是bookClass这个元素 就要保存起来，保存在我们创建的那个BookClass的类中 所以我们要new一个BookClass类    
            book = new Book();    
            // attributes是属性。    
            //bookClass.setId(new Integer(attributes.getValue(IDSTRING)));    
        }     
        tempString = localName;    
	}

	public List<Book> getBookList() {
		// TODO Auto-generated method stub
		return this.bookList;
	}

}
