//package com.zhipu.chinavideo.db;
//
//import com.zhipu.chinavideo.MyInformationActivity;
//import com.zhipu.chinavideo.util.APP;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//
//public class GameData {
//
//	public static GameData instance ;
//	
//	public static GameData getInstance(){
//	
//		return instance ;
//	}
//	private SharedPreferences preferences;
//	private Editor edit ;
//	public GameData(Context context){
//		preferences = context.getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
//		edit = preferences.edit() ;
//	}
//	
//	public static void init(Context context){
//		instance = new GameData(context);
//	}
//	
//	public SharedPreferences getSharedPreferences(){
//		return preferences ;
//	}
//	
//	public Editor getEditor(){
//		return edit ;
//	}
//	
//	public void commit(){
//		 edit.commit() ;
//	}
//	
//	public boolean checkLogin(){
//		if (preferences.getString(APP.IS_LOGIN, "").equals("true")) {
//			return true ;
//		} else {
//			return false ;
//		}
//	}
//	
////	public String getString(String key,String defValue){
////		
////		return preferences.getString(key, defValue) ;
////	}
////	public int getInt(String key,int defValue){
////		
////		return preferences.getInt(key, defValue);
////	}
////	
////	public boolean getBoolean(String key,boolean defValue){
////		
////		return preferences.getBoolean(key, defValue);
////	}
////	public Long getBoolean(String key,Long defValue){
////		
////		return preferences.getLong(key, defValue);
////	}
////	
////	public Float getFloat(String key,Float defValue){
////		
////		return preferences.getFloat(key, defValue);
////	}
//	
//
//	
//	
//	
//	
//}
