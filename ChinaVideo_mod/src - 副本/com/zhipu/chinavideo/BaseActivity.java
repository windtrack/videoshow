package com.zhipu.chinavideo;

import com.zhipu.chinavideo.util.UIHandler;
import com.zhipu.chinavideo.util.UIHandler.IHandler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/**
 *
 * @author sunjinfang
 * @version 2016年5月4日
 */
public abstract class BaseActivity extends Activity {

	boolean isFinish;
	protected  UIHandler handler = new UIHandler(Looper.getMainLooper());
	protected abstract void handler(Message msg);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 handler.setHandler(new IHandler() {
	            public void handleMessage(Message msg) {
	            	if(isFinish){
	            		return ;
	            	}
	                handler(msg);//有消息就提交给子类实现的方法
	            }
	        });   
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isFinish = true;
	}



}
