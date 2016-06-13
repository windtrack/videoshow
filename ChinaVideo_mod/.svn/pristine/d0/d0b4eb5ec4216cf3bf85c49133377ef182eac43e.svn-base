package com.zhipu.chinavideo.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @author: zhongxf
 * @Description: 手机手机获取运营商
 * @date: 2016年4月11日
 */
public class OperatorUtil {

	private static String getIMSI(Context cxt) {
		TelephonyManager telephonyManager = (TelephonyManager) cxt
				.getSystemService(Context.TELEPHONY_SERVICE);
		String IMSI; // 返回唯一的用户ID;就是这张卡的编号
		IMSI = telephonyManager.getSubscriberId();
		return IMSI;
	}

	/**
	 * 判断是否是中国电信
	 */
	public static boolean isChinaTelecom(Context cxt) {
		String IMSI = getIMSI(cxt);
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。其中
		if (IMSI != null && IMSI.startsWith("46003")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是中国移动
	 */
	public static boolean isChinaMobile(Context cxt) {
		String IMSI = getIMSI(cxt);
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。其中
		if (IMSI != null
				&& (IMSI.startsWith("46000") || IMSI.startsWith("46002"))) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是中国联通
	 */
	public static boolean isChinaUnicom(Context cxt) {
		String IMSI = getIMSI(cxt);
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。其中
		if (IMSI != null && IMSI.startsWith("46001")) {
			return true;
		}
		return false;
	}

}
