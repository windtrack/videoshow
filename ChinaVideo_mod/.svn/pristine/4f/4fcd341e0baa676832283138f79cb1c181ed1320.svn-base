package com.zhipu.chinavideo.application;

import android.app.Application;

/**
 * @author: zhongxf
 * @Description: 秀场App的自定义Application
 * @date: 2016年5月5日
 */
public class ChinaVideoApplication extends Application {
	/**
	 *@author： zhongxf
	 *@Description: 
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ChinaVideoExHandler crashHanlder = ChinaVideoExHandler.getInstance();
		crashHanlder.init(getApplicationContext());
	}
}
