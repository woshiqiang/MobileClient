package com.mobileclient.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Book implements Serializable {
	/*条形码*/
	private String barcode; 
	/*图书名称*/
	private String bookName;
	/*图书所在类别*/
	private int bookClassId;
	/*图书价格*/
	private float price;
	/*图书数量*/
	private int count;
	/*出版日期*/
	private Timestamp publishDate;
	/*图书图片*/
	private String bookImage;
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public int getBookClassId() {
		return bookClassId;
	}
	public void setBookClassId(int bookClassId) {
		this.bookClassId = bookClassId;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Timestamp getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Timestamp publishDate2) {
		this.publishDate = publishDate2;
	}
	public String getBookImage() {
		return bookImage;
	}
	public void setBookImage(String bookImage) {
		this.bookImage = bookImage;
	}
}
