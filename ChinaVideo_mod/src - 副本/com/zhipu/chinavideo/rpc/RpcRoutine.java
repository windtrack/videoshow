package com.zhipu.chinavideo.rpc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.alexd.jsonrpc.JSONRPCException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.db.HandlerCmd;
import com.zhipu.chinavideo.entity.Advertise;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.MyJSONRPCHttpClient;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

public class RpcRoutine {

	Runnable runnable ;
	Handler mhandler ;
	public static RpcRoutine instance ;
	
	
	
	public static RpcRoutine getInstance(){
		if(instance == null){
			instance = new RpcRoutine() ;
		}
		return instance ;
	}
	
	/**
	 * 根据需求穿字段
	 * @param event
	 * @param handler
	 * @param params
	 */
	public  void addRpc( final RpcEvent event, final Handler handler, final Object ... params) {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(new DefaultHttpClient(), APP.PATH_NEIWANG);
					client.setConnectionTimeout(5000);
					client.setSoTimeout(5000);
					JSONObject result = client.callJSONObject(event.name, params);
					String msg = result.toString();
					JSONObject obj = new JSONObject(msg);
					doReslut(event, handler,obj);
				} catch (Exception e) {
					e.printStackTrace();
					doException(event,handler) ;
					Log.e("sjf", "rpc eeror id==" +event);
				}
				ThreadPoolWrap.getThreadPool().removeTask(this);
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(runnable);
	}
	
	
	
	
	
	protected void doException(RpcEvent event, Handler handler) {
		switch (event) {
		case GetUserInfo:
			{
				handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetUserInfoFailed);
			}
			break;
		case GetHallInfo:
			break ;
		case GetRoomClientUrl:
			break ;
		case GetRoomInfo:
			{
				handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetRoomInfoException);
			}
			break ;
		case Call3rdOrderid:
			{
				handler.sendEmptyMessage(HandlerCmd.HandlerCmd_Get3rdOrderIdError);
			}
			break ;
			
		case CallQQLogin:
		case CallWXLogin:
		case CallUserLogin:
		case Call3rdLogin:
		{
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_CallLoginException);
		}
		break ;
		case GetShouChongGift:
			
			break ;
			

		default:
			break;
	}
		
	}

	public void doReslut( RpcEvent event, Handler handler,JSONObject obj) throws JSONException{
		
		switch (event) {
			case UpdateBeans:
				updateBeans(handler,obj) ;
				break ;
			case GetUserInfo:
				getUserInfo(event,handler,obj) ;
				break;
			case GetSigin:
				break;
			case GetHallInfo:
				getHallListInfo(handler,obj);
				break ;
			case GetRoomClientUrl:
				getRoomClientUrl(handler,obj) ;
				break ;
			case GetRoomInfo:
				getRoomInfo(handler,obj) ;
				break ;
			case Call3rdOrderid:
				get3rdOrderId(handler,obj) ;
				break ;
			case CallQQLogin:
			case CallWXLogin:
			case CallUserLogin:
			case Call3rdLogin:
				getLoginReslut(event,handler,obj) ;
				break ;
			case GetShouChongGift:
				getShouChongFigt(handler,obj) ;
				break ;
			default:
				break;
		}
	}
	
	
	private void getShouChongFigt(Handler handler, JSONObject obj) throws JSONException {
		int state = obj.getInt("s");
//		{"s":1,"data":{"s":-1,"data":"您还未充值哦，快去充值吧!"}}
		
		JSONObject data = obj.getJSONObject("data") ;
		
		GlobalData.getInstance().mGetShouchongTips = data.getString("data") ;
		int s = data.getInt("s") ;
		if(s==0){
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetShowChongGiftFiled_hasGet);
		}else if(s==1){
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetShowChongGiftSuccess);
		}else if(s==-1){
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetShowChongGiftFiled_ToPay);
		}else if(s==-2){
			//去充值
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetShowChongGiftFiled_ToPay);
		}else if(s==-3){
			//点错了
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetShowChongGiftFiled_worng);
		}else if(s==-4){
			//点错了
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetShowChongGiftFiled_worng);
		}else if(s==-5){
			//点错了
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetShowChongGiftFiled_PayLittle);
		}
		
	}

	private void getUserInfo(RpcEvent event, Handler handler, JSONObject jsonObject) throws JSONException {
		int i = jsonObject.getInt("s");
		if (i == 1) {
			JSONObject data = jsonObject.getJSONObject("data");
			String beans = data.getString("beans");
			String rlevel = data.getString("rlevel");
			String clevel = data.getString("clevel");
			String icon = data.getString("icon");
			String nickname = data.getString("nickname");
			String openkey = data.getString("openkey");
			String shouchong = data.getString("shouchong");
			String time = data.getString("timestamp");
			String cost_beans = data.getString("cost_beans");
			String received_beans = data
					.getString("received_beans");
			String openid = data.getString("openid");
			int viplv = data.getInt("vip_lv");
			int is_stealth = data.getInt("is_stealth");
			Editor editor = GlobalData.getInstance().getEditor();
			editor.putString(APP.BEANS, beans);
			editor.putString(APP.USER_ICON, APP.USER_LOGO_ROOT
					+ icon);
			editor.putString(APP.USER_RLEVEL, rlevel);
			editor.putString(APP.USER_CLEVEL, clevel);
			editor.putString(APP.NICKNAME, nickname);
			editor.putString(APP.OPENKEY, openkey);
			editor.putString(APP.TIMESTAMP, time);
			editor.putString(APP.COST_BEANS, cost_beans);
			editor.putString(APP.RECEIVED_BEANS, received_beans);
			editor.putString(APP.OPEN_ID, openid);
			editor.putString(APP.SHOUCHONG, shouchong);
			editor.putInt(APP.VIPLV, viplv);
			editor.putInt(APP.ISSTEALTH, is_stealth);
			editor.commit();
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetUserInfoSuccess);
		} else {
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetUserInfoFailed);
		}
		
	}

	private void getLoginReslut(RpcEvent event,Handler handler, JSONObject jsonObject) throws JSONException {
//		Log.e("unfind", "登录成功返回数据："+jsonObject.toString());
		int a = jsonObject.getInt("s");
		if (a == 1) {
			JSONObject data = jsonObject.getJSONObject("data");
			String id = data.getString("id");
			String secret = jsonObject.getString("secret");
			String user_name = data.getString("username");
			String password = data.getString("password");
			String gender = data.getString("gender");
			String pos = data.getString("pos");
			String phone = data.getString("phone");
			String type = data.getString("user_type");
			String userType = data.getString("user_type");
			
			Editor editor = GlobalData.getInstance().getEditor();
			editor.putString(APP.IS_LOGIN, "true");
			editor.putString(APP.USER_ID, id);
			editor.putString(APP.SECRET, secret);
			editor.putString(APP.USER, user_name);
			editor.putString(APP.PASS, password);
			editor.putString(APP.GENDER, gender);
			editor.putString(APP.POS, pos);
			editor.putString(APP.PHONE, phone);	
			editor.putString(APP.USER_TYPE, type);
			if (!event.name.equals("m_qq_login")
					&& !event.name.equals("wechat_login")) {
				editor.putString(APP.LASTTIME_USERNAME, GlobalData.getInstance().getmThirdLoginInfo().username);
				editor.putString(APP.LASTTIME_PASSWORD, GlobalData.getInstance().getmThirdLoginInfo().userToken);
			}
			editor.commit();
			// 获取用户信息
			RpcRoutine.getInstance().addRpc(RpcEvent.GetUserInfo, handler, id,secret,1,1);
			RpcRoutine.getInstance().addRpc(RpcEvent.GetSigin, handler, id,secret);
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_CallLoginSuccess);
		} else {
			GlobalData.getInstance().mLoginErrorInfo  = jsonObject.getString("data");
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_CallLoginFailed);
		}
		
	}

	private void get3rdOrderId(Handler handler, JSONObject obj) throws JSONException {
		int state = obj.getInt("s");
		if (state == 1) {
			GlobalData.getInstance().m3rdOrderId = obj.getString("data");
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_Get3rdOrderId);
		}else{
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_Get3rdOrderIdError);
		}
		
		
		
	}

	private void getRoomInfo(Handler handler, JSONObject room_info) throws JSONException {
		int state = room_info.getInt("s");
		if (state == 1) {
			JSONObject data = room_info.getJSONObject("data");
			GlobalData.getInstance().getmRoomInfo().timestamp = data.getString("timestamp");
			GlobalData.getInstance().getmRoomInfo().openid = data.getString("openid");
			GlobalData.getInstance().getmRoomInfo().is_guard = data.getInt("is_guard");
			GlobalData.getInstance().getmRoomInfo().openkey = data.getString("openkey");
			GlobalData.getInstance().getmRoomInfo().is_follow = data.getInt("is_followed");
			JSONObject anchor = data.getJSONObject("anchor");
			GlobalData.getInstance().getmRoomInfo().anchor_name = anchor.getString("nickname");
			GlobalData.getInstance().getmRoomInfo().anchor_id = anchor.getString("id");
			GlobalData.getInstance().getmRoomInfo().anchor_received_level = anchor
					.getString("received_level");
			JSONObject room = data.getJSONObject("room");
			GlobalData.getInstance().getmRoomInfo().anchor_icon = room.getString("poster_url");
			GlobalData.getInstance().getmRoomInfo().status = room.getString("status");
			Gson gson = new Gson();
			GlobalData.getInstance().getmRoomInfo().anchor_current = gson.fromJson(room.toString(),
					AnchorInfo.class);

			JSONObject mgmsvever = data.getJSONObject("gserver");

			GlobalData.getInstance().getmRoomInfo().mMommonUrl = mgmsvever.getString("ip");
			GlobalData.getInstance().getmRoomInfo().mMommonPort = mgmsvever.getInt("port");
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetRoomInfoSuccess);
		} else {
			// 如果获取用户信息失败，弹出什么原因
			String data = "";
			data = room_info.getString("data");
			Message m = new Message();
			m.what = HandlerCmd.HandlerCmd_GetRoomInfoFailed;
			m.obj = data;
			handler.sendMessage(m);
			
			// handler.sendEmptyMessage(GETROOMINFO_ERROR);
		}
		
	}

	private void getRoomClientUrl(Handler handler, JSONObject obj) throws JSONException {
		int s = obj.getInt("s");
		if(s ==1){
			JSONObject data = obj.getJSONObject("data");
			String chaturl = data.getString("chat_url");
			GlobalData.getInstance().getmRoomCLientUrl().chat_url = chaturl.split(":")[0];
			GlobalData.getInstance().getmRoomCLientUrl().port = Integer.parseInt(chaturl.split(":")[1]);
			GlobalData.getInstance().getmRoomCLientUrl().live_url = data.getString("live_url");
			GlobalData.getInstance().getmRoomCLientUrl().stream = data.getString("stream");
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetRoomUrlSuccess);
		}else{
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_RPC_Failed);
		}
	
	}
	
	private void getHallListInfo(Handler handler, JSONObject obj) throws JSONException {
		
		int s = obj.getInt("s");
		
		if(s ==1){
			GlobalData.getInstance().getmHallInfo().clear();
			// 解析bannner数据
			JSONArray obj_ad = obj.getJSONArray("banner");
			for (int p = 0; p < obj_ad.length(); p++) {
				Gson gs = new Gson();
				JSONObject ad_j = obj_ad.getJSONObject(p);
				Advertise advertise = gs.fromJson(ad_j.toString(),Advertise.class);
				GlobalData.getInstance().getmHallInfo().advertise_list.add(advertise);
			}
			// 解析主播列表数据
			JSONObject data = obj.getJSONObject("data");
			JSONArray ja = data.getJSONArray("rooms");
			for (int j = 0; j < ja.length(); j++) {
				JSONObject jsona = ja.getJSONObject(j);
				Gson gson = new Gson();
				AnchorInfo anchorInfo = gson.fromJson(jsona.toString(), AnchorInfo.class);
				GlobalData.getInstance().getmHallInfo().anchor_list.add(anchorInfo);
			}
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetHallInfoList);
		}else{
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_RPC_Failed);
		}
		
		

	}
	
	

	public void updateBeans(final Handler handler,JSONObject obj) throws JSONException{

		JSONObject data = obj.getJSONObject("data");
		String beans = data.getString("beans");
		String rlevel = data.getString("rlevel");
		String clevel = data.getString("clevel");
		String icon = data.getString("icon");
		String nickname = data.getString("nickname");
		String openkey = data.getString("openkey");
		String shouchong = data.getString("shouchong");
		String time = data.getString("timestamp");
		String cost_beans = data.getString("cost_beans");
		String received_beans = data.getString("received_beans");
		String openid = data.getString("openid");
		int viplv = data.getInt("vip_lv");
		int is_stealth = data.getInt("is_stealth");
		Editor editor = GlobalData.getInstance().getEditor();
		editor.putString(APP.BEANS, beans);
		editor.putString(APP.USER_ICON, APP.USER_LOGO_ROOT + icon);
		editor.putString(APP.USER_RLEVEL, rlevel);
		editor.putString(APP.USER_CLEVEL, clevel);
		editor.putString(APP.NICKNAME, nickname);
		editor.putString(APP.OPENKEY, openkey);
		editor.putString(APP.TIMESTAMP, time);
		editor.putString(APP.COST_BEANS, cost_beans);
		editor.putString(APP.RECEIVED_BEANS, received_beans);
		editor.putString(APP.OPEN_ID, openid);
		editor.putString(APP.SHOUCHONG, shouchong);
		editor.putInt(APP.VIPLV, viplv);
		editor.putInt(APP.ISSTEALTH, is_stealth);
		editor.commit();
	}
	
	
	
	
}
