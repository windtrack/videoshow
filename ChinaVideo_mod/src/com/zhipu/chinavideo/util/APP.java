package com.zhipu.chinavideo.util;

import com.zhipu.chinavideo.R;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class APP extends Application {
	public static final int IS_DEMO = 1  ; //0内网 1外网 2外网测试 
	
//	if(IS_DEMO == 0){
//	public static  String PATH_NEIWANG = "http://60.174.249.98:8002/api/rpc/pc.php";
//	public static  String GIFT_PATH = "http://60.174.249.98:8002/static/img/gift/mobile/";
//	public static  String DISC_PATH = "http://60.174.249.98:8002/static/img/app/faxian/";// 发现测试服图片地址
//	public static  String CAR_PATH =  "http://60.174.249.98:8002/static/img/mount/mobile/sit_";// 发现测试服图片地址
//	public static  String URL_RULE = "http://60.174.249.98:8000/about/rules.html";
//	public static String URL_Head = "http://60.174.249.98:8000/share/app/" ; //分享地址 头
	
//}else if(IS_DEMO == 1){
	public static  String PATH_NEIWANG = "http://mobile.0058.com/api/rpc/pc.php";
	public static  String GIFT_PATH = "http://static.0058.com/static/img/gift/mobile/";
	public static  String DISC_PATH = "http://static.0058.com/static/img/app/faxian/";//
	public static  String CAR_PATH = "http://static.0058.com/static/img/mount/mobile/sit_";
	public static  String URL_RULE = "http://www.0058.com/about/rules.html";
	public static String URL_Head = "http://www.0058.com/share/app/" ; //分享地址 头
//}else if(IS_DEMO == 2){
//	public static  String PATH_NEIWANG = "http://test.0058.com/api/rpc/pc.php";
//	public static  String GIFT_PATH = "http://static.0058.com/static/img/gift/mobile/";
//	public static  String DISC_PATH = "http://static.0058.com/static/img/app/faxian/";//
//	public static  String CAR_PATH = "http://static.0058.com/static/img/mount/mobile/sit_";
//	public static  String URL_RULE = "http://www.0058.com/about/rules.html";
//	public static String URL_Head = "http://test.0058.com/share/app/" ; //分享地址 头
	
	public static void init() {
		
			
	};
	
	public static  String URL_CHAT = "192.168.1.201";
	public static int localVersion = 0;// 本地安装版本
	public static String versionName = "1.0";// 本地安装版本
	public static final String PATH = "http://www.0058.com/api/rpc/pc.php";
	public static final String POST_URL_HEAD = "http://static.0058.com/" ;
	public static final String POST_URL_ROOT = "http://static.0058.com/poster_url/";
	// 新的海报图片
	public static final String POST_URL_ROOT2 = "http://static.0058.com/poster_url/w480/";
	public static final String USER_LOGO_ROOT = "http://static.0058.com/xtuserlogo/45x45/";
	public static final String USER_BIG_LOGO_ROOT = "http://static.0058.com/xtuserlogo/200x200/";
	public static final String CAR_PATH1 = "http://static.0058.com/static/img/mount/mobile/";
	
	public static final String TAG = "ChinaVideo";
	
	
	// SharedPreferences保存的数据
	public static final String CheckDemo = "isdemo";
	
	public static final String MY_SP = "mydata";
	public static final String IS_LOGIN = "login";
	public static final String LOGIN_STYLE = "login_Style";
	public static final String DEVICETOKEN = "device_token";
	public static final String USER = "user";
	public static final String PASS = "pass";
	public static final String OPEN_ID = "openid";
	public static final String TOKEN = "access_token";
	public static final String TIMESTAMP = "timestamp";
	public static final String OPENKEY = "openkey";
	public static final String NICKNAME = "nickname";
	public static final String USER_ID = "id";
	public static final String SECRET = "secret";
	public static final String BEANS = "beans";
	public static final String USER_ICON = "icon";
	public static final String USER_RLEVEL = "rlevel";
	public static final String USER_CLEVEL = "clevel";
	public static final String COST_BEANS = "cost";
	public static final String RECEIVED_BEANS = "received";
	public static final String VIPLV = "vip_lv";
	public static final String ISSTEALTH = "is_stealth";
	public static final String GENDER = "man";
	public static final String POS = "weizhi";
	public static final String USER_TYPE = "user_type" ;
	public static final String IP = "ip";
	public static final String GIFT_ID_CURRENT = "1001";
	public static final String GIFT_FROM_CURRENT = "0";
	public static final String GIFT_PRICE_CURREN = "giftprice";
	public static final String GIFT_ICON_CUTTENT = "gifticon";
	public static final String HISTORY = "history";
	public static final String LASTTIME_USERNAME = "lasttimeusername";
	public static final String LASTTIME_PASSWORD = "lasttimepassword";
	public static final String PHONE = "phone";
	// 首冲
	public static final String SHOUCHONG = "shouchong";
	// 最近观看数据库
	public static final String DB_NAME = "history_db";
	public static final String TABLE_NAME = "t1_his";
	public static final int DB_VERSION = 1;
	// 快速刷礼物
	public static final String FAST_GIFTNUM = "giftnum";
	public static final String FAST_GIFTPRICE = "price";
	public static final String FAST_GIFTID = "100001";
	public static final String FAST_GIFTICON = "fast_gift_icon";
	// 是否关闭系统通知消息
	public static final String IS_CLOSEMSG = "is_closemsg";
    //红豆新手引导
	public static final String HD_FIRST="hdfirst";
	// 敏感词过滤

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initVersion();
	}

	public void initVersion() {
		try {
			PackageInfo packageInfo = getApplicationContext()
					.getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置明星等级图片
	 * 
	 * @param str
	 * @param v
	 * @param context
	 */
	public static void setReceived_level(String str, ImageView v,
			Context context) {
		// TODO Auto-generated method stub

		Bitmap received_img = null;
		if ("0".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr0);

		} else if ("1".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr1);

		} else if ("2".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr2);

		} else if ("3".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr3);

		} else if ("4".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr4);

		} else if ("5".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr5);

		} else if ("6".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr6);

		} else if ("7".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr7);

		} else if ("8".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr8);

		} else if ("9".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr9);

		} else if ("10".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr10);

		} else if ("11".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr11);

		} else if ("12".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr12);

		} else if ("13".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr13);

		} else if ("14".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr14);

		} else if ("15".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr15);

		} else if ("16".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr16);

		} else if ("17".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr17);

		} else if ("18".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr18);

		} else if ("19".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr19);

		} else if ("20".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr20);

		} else if ("21".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr21);

		} else if ("22".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr22);

		} else if ("23".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr23);

		} else if ("24".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr24);

		} else if ("25".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr25);

		} else if ("26".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr26);

		} else if ("27".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr27);

		} else if ("28".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr28);

		} else if ("29".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr29);

		} else if ("30".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr30);

		} else if ("31".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr31);

		} else if ("32".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr32);

		} else if ("33".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr33);

		} else if ("34".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr34);

		} else if ("35".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr35);

		} else {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pr0);

		}

		v.setImageBitmap(received_img);

	}

	/**
	 * 设置财富等级图片
	 * 
	 * @param str
	 * @param v
	 * @param context
	 */
	public static void setCost_level(String str, ImageView v, Context context) {
		// TODO Auto-generated method stub

		Bitmap received_img = null;
		if ("0".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_0);

		} else if ("1".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_1);

		} else if ("2".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_2);

		} else if ("3".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_3);

		} else if ("4".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_4);

		} else if ("5".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_5);

		} else if ("6".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_6);

		} else if ("7".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_7);

		} else if ("8".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_8);

		} else if ("9".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_9);

		} else if ("10".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_10);

		} else if ("11".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_11);

		} else if ("12".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_12);

		} else if ("13".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_13);

		} else if ("14".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_14);

		} else if ("15".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_15);

		} else if ("16".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_16);

		} else if ("17".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_17);

		} else if ("18".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_18);

		} else if ("19".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_19);

		} else if ("20".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_20);

		} else if ("21".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_21);

		} else if ("22".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_22);

		} else if ("23".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_23);

		} else if ("24".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_24);

		} else if ("25".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_25);

		} else if ("26".equals(str)) {

			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_26);

		} else {
			received_img = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.level_0);
		}

		v.setImageBitmap(received_img);

	}

	/**
	 * 获取财富等级经验数目
	 * 
	 * @param level
	 * @return
	 */
	public static Long parseCostnum(String str) {
		Long costnum = 0l;
		if ("0".equals(str)) {

			costnum = 0l;

		} else if ("1".equals(str)) {

			costnum = 100000l;
		} else if ("2".equals(str)) {

			costnum = 500000l;

		} else if ("3".equals(str)) {

			costnum = 1500000l;

		} else if ("4".equals(str)) {

			costnum = 3000000l;

		} else if ("5".equals(str)) {

			costnum = 6000000l;
		} else if ("6".equals(str)) {

			costnum = 10000000l;

		} else if ("7".equals(str)) {

			costnum = 20000000l;

		} else if ("8".equals(str)) {

			costnum = 40000000l;

		} else if ("9".equals(str)) {

			costnum = 65000000l;

		} else if ("10".equals(str)) {

			costnum = 100000000l;

		} else if ("11".equals(str)) {

			costnum = 150000000l;
		} else if ("12".equals(str)) {

			costnum = 200000000l;

		} else if ("13".equals(str)) {

			costnum = 250000000l;

		} else if ("14".equals(str)) {

			costnum = 350000000l;

		} else if ("15".equals(str)) {

			costnum = 500000000l;

		} else if ("16".equals(str)) {

			costnum = 700000000l;

		} else if ("17".equals(str)) {

			costnum = 1000000000l;

		} else if ("18".equals(str)) {

			costnum = 1400000000l;

		} else if ("19".equals(str)) {

			costnum = 1900000000l;

		} else if ("20".equals(str)) {

			costnum = 2500000000l;
		} else if ("21".equals(str)) {

			costnum = 3200000000l;

		} else if ("22".equals(str)) {

			costnum = 4000000000l;

		} else if ("23".equals(str)) {

			costnum = 5000000000l;

		} else if ("24".equals(str)) {

			costnum = 6300000000l;

		} else if ("25".equals(str)) {

			costnum = 8000000000l;

		} else if ("26".equals(str)) {
			costnum = 12800000000l;

		} else {
			costnum = 0l;
		}
		return costnum;
	}

	/**
	 * 获取主播经验
	 * 
	 * @param level
	 * @return
	 */
	public static Long parsereceivenum(String str) {
		Long costnum = 0l;
		if ("0".equals(str)) {

			costnum = 0l;

		} else if ("1".equals(str)) {

			costnum = 100000l;
		} else if ("2".equals(str)) {

			costnum = 300000l;

		} else if ("3".equals(str)) {

			costnum = 600000l;

		} else if ("4".equals(str)) {

			costnum = 1000000l;

		} else if ("5".equals(str)) {

			costnum = 2000000l;
		} else if ("6".equals(str)) {

			costnum = 3500000l;

		} else if ("7".equals(str)) {

			costnum = 5500000l;

		} else if ("8".equals(str)) {

			costnum = 8000000l;

		} else if ("9".equals(str)) {

			costnum = 11000000l;

		} else if ("10".equals(str)) {

			costnum = 15000000l;

		} else if ("11".equals(str)) {

			costnum = 20000000l;
		} else if ("12".equals(str)) {

			costnum = 40000000l;

		} else if ("13".equals(str)) {

			costnum = 70000000l;

		} else if ("14".equals(str)) {

			costnum = 110000000l;

		} else if ("15".equals(str)) {

			costnum = 160000000l;

		} else if ("16".equals(str)) {

			costnum = 240000000l;

		} else if ("17".equals(str)) {

			costnum = 300000000l;

		} else if ("18".equals(str)) {

			costnum = 400000000l;

		} else if ("19".equals(str)) {

			costnum = 600000000l;

		} else if ("20".equals(str)) {

			costnum = 1000000000l;
		} else if ("21".equals(str)) {

			costnum = 1600000000l;

		} else if ("22".equals(str)) {

			costnum = 2400000000l;

		} else if ("23".equals(str)) {

			costnum = 3600000000l;

		} else if ("24".equals(str)) {

			costnum = 5000000000l;

		} else if ("25".equals(str)) {

			costnum = 6600000000l;

		} else if ("26".equals(str)) {
			costnum = 8600000000l;

		} else if ("27".equals(str)) {

			costnum = 11200000000l;

		} else if ("28".equals(str)) {

			costnum = 14000000000l;

		} else if ("29".equals(str)) {
			costnum = 17000000000l;

		} else if ("30".equals(str)) {
			costnum = 20200000000l;

		} else if ("31".equals(str)) {
			costnum = 23600000000l;

		} else if ("32".equals(str)) {
			costnum = 27200000000l;

		} else if ("33".equals(str)) {
			costnum = 32000000000l;

		} else if ("34".equals(str)) {
			costnum = 37000000000l;

		} else if ("35".equals(str)) {
			costnum = 44000000000l;

		} else {
			costnum = 0l;
		}
		return costnum;
	}

	public static int parseVIPLevel(int level) {
		int vip_resource = 0;
		switch (level) {
		case 0:
			vip_resource = R.drawable.v0;
			break;
		case 1:
			vip_resource = R.drawable.v1;
			break;
		case 2:
			vip_resource = R.drawable.v2;
			break;
		case 3:
			vip_resource = R.drawable.v3;
			break;
		case 4:
			vip_resource = R.drawable.v4;
			break;
		case 5:
			vip_resource = R.drawable.v5;
			break;
		case 6:
			vip_resource = R.drawable.v6;
			break;
		case 7:
			vip_resource = R.drawable.v7;
			break;
		case 8:
			vip_resource = R.drawable.v8;
			break;
		case 9:
			vip_resource = R.drawable.v9;
			break;
		}
		return vip_resource;
	}

	/**
	 * 金守护
	 * 
	 * @param level
	 * @return
	 */
	public static int parseGoldGuardId(String level) {
		if ("1".equals(level)) {
			return R.drawable.g1;
		} else if ("2".equals(level)) {
			return R.drawable.g2;
		} else if ("3".equals(level)) {
			return R.drawable.g3;
		} else if ("4".equals(level)) {
			return R.drawable.g4;
		} else if ("5".equals(level)) {
			return R.drawable.g5;
		} else if ("6".equals(level)) {
			return R.drawable.g6;
		} else if ("7".equals(level)) {
			return R.drawable.g7;
		} else if ("8".equals(level)) {
			return R.drawable.c8;
		} else if ("9".equals(level)) {
			return R.drawable.c9;
		} else if ("10".equals(level)) {
			return R.drawable.c10;
		} else if ("11".equals(level)) {
			return R.drawable.c11;
		} else if ("12".equals(level)) {
			return R.drawable.c12;
		} else if ("13".equals(level)) {
			return R.drawable.c13;
		} else if ("14".equals(level)) {
			return R.drawable.c14;
		} else if ("15".equals(level)) {
			return R.drawable.c15;
		} else if ("16".equals(level)) {
			return R.drawable.c16;
		} else if ("17".equals(level)) {
			return R.drawable.c17;
		} else if ("18".equals(level)) {
			return R.drawable.c18;
		} else if ("19".equals(level)) {
			return R.drawable.c19;
		} else {
			return R.drawable.c20;
		}
	}

	/**
	 * 金守护(年守护)
	 * 
	 * @param level
	 * @return
	 */
	public static int parseIsYearGoldGuardId(String level) {
		if ("1".equals(level)) {
			return R.drawable.gn1;
		} else if ("2".equals(level)) {
			return R.drawable.gn2;
		} else if ("3".equals(level)) {
			return R.drawable.gn3;
		} else if ("4".equals(level)) {
			return R.drawable.gn4;
		} else if ("5".equals(level)) {
			return R.drawable.gn5;
		} else if ("6".equals(level)) {
			return R.drawable.gn6;
		} else if ("7".equals(level)) {
			return R.drawable.gn7;
		} else if ("8".equals(level)) {
			return R.drawable.cn8;
		} else if ("9".equals(level)) {
			return R.drawable.cn9;
		} else if ("10".equals(level)) {
			return R.drawable.cn10;
		} else if ("11".equals(level)) {
			return R.drawable.cn11;
		} else if ("12".equals(level)) {
			return R.drawable.cn12;
		} else if ("13".equals(level)) {
			return R.drawable.cn13;
		} else if ("14".equals(level)) {
			return R.drawable.cn14;
		} else if ("15".equals(level)) {
			return R.drawable.cn15;
		} else if ("16".equals(level)) {
			return R.drawable.cn16;
		} else if ("17".equals(level)) {
			return R.drawable.cn17;
		} else if ("18".equals(level)) {
			return R.drawable.cn18;
		} else if ("19".equals(level)) {
			return R.drawable.cn19;
		} else {
			return R.drawable.cn20;
		}
	}

	/**
	 * 银守护(年守护)
	 * 
	 * @param level
	 * @return
	 */
	public static int parseIsYearSilverGuardId(String level) {
		if ("1".equals(level)) {
			return R.drawable.sn1;
		} else if ("2".equals(level)) {
			return R.drawable.sn2;
		} else if ("3".equals(level)) {
			return R.drawable.sn3;
		} else if ("4".equals(level)) {
			return R.drawable.sn4;
		} else if ("5".equals(level)) {
			return R.drawable.sn5;
		} else if ("6".equals(level)) {
			return R.drawable.sn6;
		} else if ("7".equals(level)) {
			return R.drawable.sn7;
		} else if ("8".equals(level)) {
			return R.drawable.cn8;
		} else if ("9".equals(level)) {
			return R.drawable.cn9;
		} else if ("10".equals(level)) {
			return R.drawable.cn10;
		} else if ("11".equals(level)) {
			return R.drawable.cn11;
		} else if ("12".equals(level)) {
			return R.drawable.cn12;
		} else if ("13".equals(level)) {
			return R.drawable.cn13;
		} else if ("14".equals(level)) {
			return R.drawable.cn14;
		} else if ("15".equals(level)) {
			return R.drawable.cn15;
		} else if ("16".equals(level)) {
			return R.drawable.cn16;
		} else if ("17".equals(level)) {
			return R.drawable.cn17;
		} else if ("18".equals(level)) {
			return R.drawable.cn18;
		} else if ("19".equals(level)) {
			return R.drawable.cn19;
		} else {
			return R.drawable.cn20;
		}

	}

	/**
	 * 银守护
	 * 
	 * @param level
	 * @return
	 */
	public static int parseSilverGuardId(String level) {
		if ("1".equals(level)) {
			return R.drawable.s1;
		} else if ("2".equals(level)) {
			return R.drawable.s2;
		} else if ("3".equals(level)) {
			return R.drawable.s3;
		} else if ("4".equals(level)) {
			return R.drawable.s4;
		} else if ("5".equals(level)) {
			return R.drawable.s5;
		} else if ("6".equals(level)) {
			return R.drawable.s6;
		} else if ("7".equals(level)) {
			return R.drawable.s7;
		} else if ("8".equals(level)) {
			return R.drawable.c8;
		} else if ("9".equals(level)) {
			return R.drawable.c9;
		} else if ("10".equals(level)) {
			return R.drawable.c10;
		} else if ("11".equals(level)) {
			return R.drawable.c11;
		} else if ("12".equals(level)) {
			return R.drawable.c12;
		} else if ("13".equals(level)) {
			return R.drawable.c13;
		} else if ("14".equals(level)) {
			return R.drawable.c14;
		} else if ("15".equals(level)) {
			return R.drawable.c15;
		} else if ("16".equals(level)) {
			return R.drawable.c16;
		} else if ("17".equals(level)) {
			return R.drawable.c17;
		} else if ("18".equals(level)) {
			return R.drawable.c18;
		} else if ("19".equals(level)) {
			return R.drawable.c19;
		} else {
			return R.drawable.c20;
		}

	}

}
