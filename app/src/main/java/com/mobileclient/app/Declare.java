package com.mobileclient.app;



import android.app.Application;

public class Declare extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub 
		super.onCreate();
		
		CrashHandler crashHandler = CrashHandler.getInstance();    
	    crashHandler.init(getApplicationContext()); 
	}
	private int id;
    private String userName;
    public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	} 
}
