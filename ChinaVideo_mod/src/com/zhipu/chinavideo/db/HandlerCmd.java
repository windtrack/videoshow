package com.zhipu.chinavideo.db;

public class HandlerCmd {

	public static final int HandlerCmd_RPC_Success = 10001;//接受数据成功
	
	public static final int HandlerCmd_RPC_Failed = 10002; //接受数据失败
	
	
	public static final int HandlerCmd_RPC_Exception = 10003; //rpc解析异常
	
	public static final int HandlerCmd_GetHallInfoList = 10005;//获取大厅列表数据
	public static final int HandlerCmd_LoadFailed = 100005;//获取大厅列表数据
	
	public static final int HandlerCmd_GetRoomUrlSuccess = 10006 ;//获取房间聊天地址
	
	public static final int HandlerCmd_GetRoomInfoSuccess = 10007 ;//获取房间信息
	
	public static final int HandlerCmd_GetRoomInfoFailed = 10008 ;//获取房间信息失败
	
	public static final int HandlerCmd_GetRoomInfoException = 10009 ;//获取房间信息异常
	

	
	public static final int HandlerCmd_RoomChatClientException = 10010 ;
	
	public static final int HandlerCmd_RoomMommonClientException = 10011 ;
	
	public static final int HandlerCmd_CallLoginSuccess = 10012;
	public static final int HandlerCmd_CallLoginFailed = 10013;
	public static final int HandlerCmd_CallLoginException = 10014;
	
	public static final int HandlerCmd_GetUserInfoSuccess = 10015;
	public static final int HandlerCmd_GetUserInfoFailed = 10016;
	
	
	public static final int HandlerCmd_GetShowChongGiftSuccess = 10017;
	public static final int HandlerCmd_GetShowChongGiftFiled_hasGet = 10018;
	public static final int HandlerCmd_GetShowChongGiftFiled_worng = 10019;
	public static final int HandlerCmd_GetShowChongGiftFiled_ToPay = 10020;
	public static final int HandlerCmd_GetShowChongGiftFiled_PayLittle = 10021;
	
	
	
	public static final int HandlerCmd_GetAnchorNewsSuccess = 10023 ;//
	public static final int HandlerCmd_GetAnchorNewsFailed = 10024 ;//
	
	public static final int HandlerCmd_CallChangeRoom = 10025 ;
	
	public static final int HandlerCmd_GetStreamSuccess = 10026 ;//获取视频源
	public static final int HandlerCmd_GetStreamFailed = 10027 ;
	public static final int HandlerCmd_NoLivingAnchor = 10028 ;
	
	public static final int HandlerCmd_Get3rdOrderId = 20011 ;
	public static final int HandlerCmd_Get3rdOrderIdError = 20012 ;
	
	public static final int HandlerCmd_Call3rdLoginSuccess = 20013 ;
	public static final int HandlerCmd_Call3rdLogOutSuccess = 20014 ;
	
	

	
	
	
	
}
