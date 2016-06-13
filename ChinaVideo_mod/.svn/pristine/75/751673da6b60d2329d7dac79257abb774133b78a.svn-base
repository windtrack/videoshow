package com.zhipu.chinavideo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author: zhongxf
 * @Description: 广播查询神秘人
 * @date: 2016年4月20日
 */
public class SteathBroadCastReceive extends BroadcastReceiver {
	
	private ReceiveInterface ri ;
	
	public SteathBroadCastReceive(ReceiveInterface ri){
		super();
		this.ri = ri;
		
	}

	public final static String action = "cn.com.chinavideo.GET_STEATH";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if(action.equals(arg1.getAction())){
			ri.receive();
		}
	}
	
}
