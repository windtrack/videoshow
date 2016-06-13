package com.zhipu.chinavideo.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/**
 * @author: zhongxf
 * @Description: TODO
 * @date: 2016年4月20日
 */
public class MgcUtil {

	private String[] stringArr;

	
	public void loadMgc(Context cxt){
		if(stringArr==null){
			getmgc(cxt);
		}
	}

	// 获取敏感词库
	public void getmgc(Context cxt) {
		try {
			// Return an AssetManager instance for your application's package
			InputStream is = cxt.getAssets().open("mgct.txt");
			int size = is.available();
			// Read the entire asset into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			// Convert the buffer into a string.
			String text = new String(buffer, "utf-8");
			stringArr = text.split(";");
		} catch (IOException e) {
			// Should never happen!
			throw new RuntimeException(e);
		}
	}

	// 过滤敏感词
	public String getmgcarray(String t) {
		if (stringArr!=null && stringArr.length > 0) {
			for (int i = 0; i < stringArr.length; i++) {
				if (t.contains(stringArr[i])) {
					if (!Utils.isEmpty(stringArr[i])) {
						t = t.replaceAll(stringArr[i], "*");
					}
				}
			}
		}
		return t;
	}
	public void recyle(){
		stringArr = null;
	}
}
