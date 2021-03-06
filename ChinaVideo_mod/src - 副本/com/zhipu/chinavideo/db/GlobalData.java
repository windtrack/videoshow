package com.zhipu.chinavideo.db;

import java.util.ArrayList;
import java.util.List;


import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.entity.Advertise;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.entity.AnchorNews;
import com.zhipu.chinavideo.entity.OnePhoto;
import com.zhipu.chinavideo.util.APP;

public class GlobalData{

	
	private static GlobalData instance ;
	public static GlobalData getInstance(){
		return instance ;
	}
	
	private SharedPreferences preferences;
	private Editor edit ;
	
	private ImageLoader mImageLoader ;
	
	public ImageLoader getmImageLoader() {
		return mImageLoader;
	}
	
	public GlobalData(Context context){
		preferences = context.getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		edit = preferences.edit() ;
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}
	public static void init(Context context){
		
		instance = new GlobalData(context);
		
		//内外网切换时清除缓存数据  sunjinfang 2016-4-28 
		int isDemo =  instance.getSharedPreferences().getInt(APP.CheckDemo, APP.IS_DEMO) ;
		if(isDemo != APP.IS_DEMO){
			instance.getEditor().clear();
		}
		instance.getEditor().putInt(APP.CheckDemo, APP.IS_DEMO) ;
		instance.getEditor().commit();
	}
	
	public SharedPreferences getSharedPreferences(){
		return preferences ;
	}
	
	public String getUid(){
		return preferences.getString(APP.USER_ID, "");
	}
	public String getUSercert(){
		return preferences.getString(APP.SECRET, "");
	}
	
	public String getUtime(){
		return preferences.getString(APP.TIMESTAMP, "");
	}
	
	public Editor getEditor(){
		return edit ;
	}
	
	public void commit(){
		 edit.commit() ;
	}
	
	public boolean checkLogin(){
		if (preferences.getString(APP.IS_LOGIN, "").equals("true")) {
			return true ;
		} else {
			return false ;
		}
	}
	public boolean isAnchor(){
		if (preferences.getString(APP.USER_TYPE, "").equals("anchor")) {
			return true ;
		} else {
			return false ;
		}
	}
	
	
	//大厅数据
	public class HallInfoList{
		public List<AnchorInfo> anchor_list = new ArrayList<AnchorInfo>();
		public List<Advertise> advertise_list = new ArrayList<Advertise>();
		
		public void clear(){
			anchor_list.clear();
			advertise_list.clear();
		}
	}
	HallInfoList  mHallInfo = new HallInfoList();
	
	public HallInfoList getmHallInfo() {
		return mHallInfo;
	}
	
	
	//房间聊天与视频地址
	public class RoomClientUrl{
		public String chat_url ;
		public int port ;
		public String live_url ;
		public String stream ; 
	}
	RoomClientUrl  mRoomCLientUrl = new RoomClientUrl();
	public RoomClientUrl getmRoomCLientUrl() {
		return mRoomCLientUrl;
	}
	
	
	
	public class RoomInfo{
		public String timestamp ;
		public String openid;
		public int is_guard ;
		public String openkey ;
		public int is_follow ;
		public String anchor_name ;
		public String anchor_id ;
		public String anchor_received_level ;
		public String anchor_icon;
		public String status ;
		public AnchorInfo anchor_current;
		public String mMommonUrl ;
		public int mMommonPort ;
	}
	
	RoomInfo  mRoomInfo = new RoomInfo();
	public RoomInfo getmRoomInfo() {
		return mRoomInfo;
	}
	
	
	//第三方支付订单
	public String m3rdOrderId;
	
	
	public class ThirdLoginInfo{
		public  String userToken;//保存用户的唯一标识
		public  String username;//保存用户的用户名（仅在需要的时候进行显示，不作为用户的唯一标识）
	}
	
	ThirdLoginInfo mThirdLoginInfo = new ThirdLoginInfo();
	public ThirdLoginInfo getmThirdLoginInfo() {
		return mThirdLoginInfo;
	}

	public void setmThirdLoginInfo(ThirdLoginInfo mThirdLoginInfo) {
		this.mThirdLoginInfo = mThirdLoginInfo;
	}
	
	//登录失败的提示
	public String mLoginErrorInfo;
	
	//领取首冲的提示
	public String mGetShouchongTips ;;
	
	public Vector<OnePhoto> mPhotoAblumImgPath = new Vector<OnePhoto>() ;//主播相册数据
	public Vector<AnchorNews> mAnchorNewsInfo = new Vector<AnchorNews>() ; //主播离线动态数据
	
	
}
