package ctv.sdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhipu.chinavideo.db.HandlerCmd;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.rpc.RpcRoutine;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

public class GameBilling {
//	String uid, String uname,String orderId, String itemid,String itemname,String itemprice,
	
	public static GameBilling instance ;
	
	public static GameBilling getInstance(){
		
		if(instance == null){
			instance = new GameBilling();
		}
		return instance ;
	}
	
	private static String userToken;//保存用户的唯一标识
	private static String username;//保存用户的用户名（仅在需要的时候进行显示，不作为用户的唯一标识）
	
	private  Activity mContext;
	private  Handler mHandler;
	
	public static String curBillingPrice; 
	public static String curBillingName;
	public static String curBillingOrderid;
	public static String curBillingProid;
	public static String curBillingUid;
	public static String curBillingUname;
	/**
	 * 
	 * @param uid 用户id
	 * @param uname用户名称
	 * @param orderId订单
	 * @param itemid道具id
	 * @param itemname道具名称
	 * @param itemprice道具价格
	 */
	public static void sendBilling(String uid, String uname,String orderId, String itemid,String itemname,String itemprice){

		curBillingOrderid = orderId;
		curBillingPrice = itemprice;
		curBillingName = itemname;
		curBillingProid = itemid;
		curBillingUid = uid;
		curBillingUname = uname;
		
//		instance.doPay();
	}
	
	public  void doPay(Activity act){
		
	}
	
	public  void Login(Activity act,Handler handle){
		
	}
	
	public  void LogOut(Activity act){
		
	}
	
	public  void doExit(Activity act){
		
	}

	public void init(Activity act) {
		
	}

	public void getOrderID(Context context, Handler handler, String user_id,String id, String room_id) {
		// TODO Auto-generated method stub
		RpcRoutine.getInstance().addRpc(RpcEvent.Call3rdOrderid, handler, user_id,"2",room_id);
	}
	
	
	private void callLoginSuccess(){
		mHandler.sendEmptyMessage(HandlerCmd.HandlerCmd_Call3rdLoginSuccess);
	}
	
	private void callLogOutSuccess(){
		mHandler.sendEmptyMessage(HandlerCmd.HandlerCmd_Call3rdLoginSuccess);
	}
	
}
