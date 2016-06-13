package com.zhipu.chinavideo.util;

/**
 * @author: zhongxf
 * @Description: 将
 * @date: 2016年4月18日
 */
public class ASCIIEncryUtil {
	//將字符串转换成为一个ascII的字符串
	public static String ascII(String str){
		char[] chars = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if(i!=0){
				sb.append("a");
			}
			sb.append((int)chars[i]);
		}
		return sb.toString();
	}
	//从ascII的字符串中解析出来原来字符串
	public static String string(String str){
		String[] ascIIs = str.split("a");
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < ascIIs.length; i++) {
			int ascII = new Integer(ascIIs[i]).intValue();
			sb.append((char)ascII);
		}
		return sb.toString();
		
	}

}
