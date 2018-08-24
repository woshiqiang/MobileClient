package com.mobileclient.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;
import com.mobileclient.domain.BookClass;

 
public class BookClassListHandler extends DefaultHandler {
	
	private List<BookClass> bookClassList = null;
	
	private BookClass bookClass;
	
	private String tempString;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		if (bookClass != null) {    
            String valueString = new String(ch, start, length);    
            if ("bookClassId".equals(tempString)) {     
                // 如果当前解析到的节点是bookClassId 就要将bookClassId中的文本节点元素的值得到    
                bookClass.setBookClassId(new Integer(valueString).intValue());
            } else if ("bookClassName".equals(tempString)) {
            	bookClass.setBookClassName(valueString);  
            }    
        }    
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		 if("BookClass".equals(localName)&&bookClass!=null)    
	        {    
	            bookClassList.add(bookClass);    
	            bookClass = null;    
	        }    
	        tempString = null;  
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		bookClassList = new ArrayList<BookClass>();
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		// 先判断读到的元素是否是person    
        if ("BookClass".equals(localName)) {    
            // 如果读到的是bookClass这个元素 就要保存起来，保存在我们创建的那个BookClass的类中 所以我们要new一个BookClass类    
            bookClass = new BookClass();    
            // attributes是属性。    
            //bookClass.setId(new Integer(attributes.getValue(IDSTRING)));    
        }     
        tempString = localName;    
	}

	public List<BookClass> getBookClassList() {
		// TODO Auto-generated method stub
		return this.bookClassList;
	}

}
