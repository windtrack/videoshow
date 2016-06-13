package com.zhipu.chinavideo.player;

import android.util.Log;

/**
 * @author: zhongxf
 * @Description: 播放器的异常
 * @date: 2016年4月26日
 */
public class MediaPlayerException extends Exception {
	private static final long serialVersionUID = 1L;
	 
	/**
	 *@author： zhongxf
	 *@Description: 播放器没有初始化的异常
	 */
	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		super.printStackTrace();
		Log.e("ChinaVideo", "The VideoView is not initialized");
	}
	

}
