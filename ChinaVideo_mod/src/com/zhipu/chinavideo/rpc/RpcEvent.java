package com.zhipu.chinavideo.rpc;

public enum RpcEvent {
	UpdateBeans("get_userinfo"),
	GetUserInfo("get_userinfo"),
	GetSigin("m_sign"),
	GetHallInfo("m_hall_new"),
	GetRoomClientUrl("resurl"),// 获取视频流和聊天服务器地址
	GetRoomInfo("roominfo"), //获取房间信息
	
	GetShouChongGift("pc_sc_getfbg"), //首冲数据
	
	GetPhotoWall("m_photos"), //主播照片列表
	CallDeletePhoto("m_photo_del"), //删除主播照片
	CallLikePhoto("m_photo_like"), //点赞
	
	CallOpenGift("m_get_hd_gift"), //打包礼包
	
	CallQQLogin("m_qq_login"), //第三方登录
	CallWXLogin("wechat_login"), //第三方登录
	CallUserLogin("m_user_login"), //第三方登录
	Call3rdLogin("m_3rd_login"), //第三方登录
	
	GetChooseSong("m_song_list"),//获取歌单列表和已经点歌单
	ChooseSong("m_diange"),//点歌
	GetPayOrder("m_get_order_no"),//支付订单
	GetMMPayInfo("mm_pay_xiane_info") , //mm支付
	Call3rdOrderid("nudoorder") , //第三方订单
	GetStreamDir("room_soure_flag") ; //获取视频流方向 
	;
	
	public String name;
	private RpcEvent(String name) {
		this.name = name;
	}
}
