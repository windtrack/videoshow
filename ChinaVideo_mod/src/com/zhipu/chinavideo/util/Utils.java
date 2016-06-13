package com.zhipu.chinavideo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.alexd.jsonrpc.JSONRPCException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.zhipu.chinavideo.LoginActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.RegisterActivity;
import com.zhipu.chinavideo.rechargecenter.RechargeActivity;
import com.zhipu.chinavideo.rpc.RpcEvent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {
	/**
	 * toast显示
	 * 
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * dip装换成px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转换成dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 */
	public static boolean isEmpty(String str) {
		// str=str.trim();
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrrenDate() {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String s = fmt.format(System.currentTimeMillis());
		return s;
	}

	public static String getTime(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		try {
			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 13);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				baos.flush();
				baos.close();
				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 显示输入法
	 */
	public static void showSoftInPut(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 关闭软键盘
	 */
	// 此方法只是关闭软键盘
	public static void hintKbTwo(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && ((Activity) context).getCurrentFocus() != null) {
			if (((Activity) context).getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(((Activity) context)
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 获取输入法的状态
	 */
	public static boolean getinputisshow(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (imm.hideSoftInputFromWindow(editText.getWindowToken(), 0)) {
			imm.showSoftInput(editText, 0);
			// 软键盘已弹出
			return true;
		} else {
			// 软键盘未弹出
			return false;
		}

	}

	/**
	 * 拼接数组
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] getBytes(String body, int tid) {
		// 包长
		byte[] package_length = intToByteArray(body.getBytes().length + 12);
		byte[] message_package = new byte[body.getBytes().length + 12];
		// 方法
		byte[] id = intToByteArray(tid);
		// 包体长度
		byte[] body_length = intToByteArray(body.getBytes().length);
		// 包体
		byte[] body_bytes = body.getBytes();

		System.arraycopy(package_length, 0, message_package, 0, 4);
		System.arraycopy(id, 0, message_package, 4, 4);
		System.arraycopy(body_length, 0, message_package, 8, 4);
		System.arraycopy(body_bytes, 0, message_package, 12,
				body.getBytes().length);
		return message_package;
	}

	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	/**
	 * udicode字符串转汉字
	 * 
	 * @param str
	 * @return
	 */
	public static String uncodeParser(String str) {
		String v = "'" + str + "'";
		try {
			String result = new JSONTokener(v).nextValue().toString();
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 注册登录dialog
	 */
	public static void recharge(final Context paramContext) {
		final AlertDialog localAlertDialog = new AlertDialog.Builder(
				paramContext).create();
		localAlertDialog.show();
		Window localWindow = localAlertDialog.getWindow();
		localWindow.setGravity(Gravity.BOTTOM);
		localWindow.setContentView(LayoutInflater.from(paramContext).inflate(
				R.layout.dialog_logining, null));
		// 设置大小
		android.view.WindowManager.LayoutParams lp = localWindow
				.getAttributes();
		lp.width = LayoutParams.FILL_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		localWindow.setAttributes(lp);
		localWindow.setWindowAnimations(R.style.mystyle); // 添加动画
		TextView dialog_register = (TextView) localWindow
				.findViewById(R.id.dialog_register);
		TextView dialog_login = (TextView) localWindow
				.findViewById(R.id.dialog_login);
		ImageView wx_btn_room = (ImageView) localWindow
				.findViewById(R.id.wx_btn_room);
		ImageView qq_btn_room = (ImageView) localWindow
				.findViewById(R.id.qq_btn_room);
		dialog_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				paramContext.startActivity(new Intent(paramContext,
						RegisterActivity.class));
				localAlertDialog.dismiss();
			}
		});
		dialog_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				paramContext.startActivity(new Intent(paramContext,
						LoginActivity.class));
				localAlertDialog.dismiss();
			}
		});
		// 微信登录
		wx_btn_room.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(paramContext, LoginActivity.class);
				in.putExtra("type", "1");
				paramContext.startActivity(in);
				localAlertDialog.dismiss();
			}
		});
		// qq登录
		qq_btn_room.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(paramContext, LoginActivity.class);
				in.putExtra("type", "2");
				paramContext.startActivity(in);
				localAlertDialog.dismiss();
			}
		});
	}

	/**
	 * 乐币不足，请充值dialog
	 * 
	 * @param context
	 * @return
	 */
	public static void Moneynotenough(final Context paramContext, String text,
			final String room_id) {
		final AlertDialog localAlertDialog = new AlertDialog.Builder(
				paramContext).create();
		localAlertDialog.show();
		Window localWindow = localAlertDialog.getWindow();
		localWindow.setGravity(Gravity.CENTER);
		localWindow.setContentView(LayoutInflater.from(paramContext).inflate(
				R.layout.dialog_layout, null));
		// 设置大小
		android.view.WindowManager.LayoutParams lp = localWindow
				.getAttributes();
		int w = GetWidth(paramContext);
		lp.width = (3 * w / 4);
		lp.height = LayoutParams.WRAP_CONTENT;
		localWindow.setAttributes(lp);
		TextView moneynotenoth_title = (TextView) localWindow
				.findViewById(R.id.moneynotenoth_title);
		TextView dia_title_tv = (TextView) localWindow
				.findViewById(R.id.dia_title_tv);
		dia_title_tv.setText("赠送礼物");
		moneynotenoth_title.setText("您的账户余额不足,请充值");
		Button quxiao = (Button) localWindow.findViewById(R.id.quxiao);
		quxiao.setText("充值");
		quxiao.setTextSize(16);
		Button queding = (Button) localWindow.findViewById(R.id.queding);
		queding.setText("取消");
		queding.setTextSize(16);
		quxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(paramContext, RechargeActivity.class);
				intent.putExtra("room_id", room_id);
				paramContext.startActivity(intent);
				localAlertDialog.dismiss();
			}
		});
		queding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				localAlertDialog.dismiss();
			}
		});
	}

	public static boolean isNetworkAvailble(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService("connectivity");

			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if ((info != null) && (info.isConnected())) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					} else {
						return false;
					}
				}
			}
		} catch (Exception e) {
			Log.i("lvjian", "获取网络异常！");
			return false;
		}
		return false;
	}

	public static Dialog showProgressDialog(Activity paramActivity,
			String paramString, boolean paramBoolean) {
		Dialog localDialog = new Dialog(paramActivity, R.style.loading_dialog);
		localDialog.setCanceledOnTouchOutside(paramBoolean);
		localDialog.setCancelable(paramBoolean);
		View localView = LayoutInflater.from(paramActivity).inflate(
				R.layout.dialog_loading, null);
		localDialog.setContentView(localView);
		((TextView) localView.findViewById(R.id.tipTextView))
				.setText(paramString);
		((ImageView) localView.findViewById(R.id.load_img))
				.startAnimation(AnimationUtils.loadAnimation(paramActivity,
						R.anim.loading_animation));
		return localDialog;
	}

	/**
	 * 聊天弹出
	 */
	public static void showChatToPop(View paramView, int paramInt2,
			Context context) {
		View localView = LayoutInflater.from(context).inflate(
				R.layout.chat_list_top, null);
		final ListView localListView = (ListView) localView
				.findViewById(R.id.chat_top_listview);
		final PopupWindow localPopupWindow = new PopupWindow(localView);
		localPopupWindow.setOutsideTouchable(true);
		localPopupWindow.setTouchable(true);
		localPopupWindow.setFocusable(true);
		localPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
		localPopupWindow.showAtLocation(paramView, 83, 0, paramInt2);

	}

	/**
	 * 获取主播列表的数据（tag标签列表数据）
	 * 
	 * @param i
	 * @param a
	 * @return
	 */
	public static String get_anchorlist(String name) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_rooms_by_tag_new",
					name, 1, 12);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取主页主播列表的数据
	 * 
	 * @return
	 */
//	public static String get_mainpageranchorlist() {
//		String s = "";
//		try {
//			String uri = APP.PATH_NEIWANG;
//			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
//					new DefaultHttpClient(), uri);
//			client.setConnectionTimeout(5000);
//			client.setSoTimeout(5000);
//			JSONObject result = client.callJSONObject("m_hall_new");
//			s = result.toString();
//		} catch (JSONRPCException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return s;
//	}

	/**
	 * 获取tag标签接口数据
	 */
	public static String gettaglistdata() {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_hall_config_new");
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取关注列表数据
	 */
	public static String getsubscribelist(String user_id, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_following", user_id,
					secret, 1, 50);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取订阅列表数据
	 */
	public static String getdingyuelist(String user_id, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("my_sub_list", user_id,
					secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 登录接口
	 */
	public static String user_login(String name, String user_name,
			String password, String deviceToken) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject(name, user_name,
					password, 50, deviceToken);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 微信和qq登录渠道，channel
	 */
	public static String wx_qq_user_login(String name, String user_name,
			String password, String deviceToken, String channel) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			Log.i("lvjian", "user_name----->" + user_name);
			Log.i("lvjian", "password----->" + password);
			Log.i("lvjian", "deviceToken----->" + deviceToken);
			Log.i("lvjian", "channel----->" + channel);
			JSONObject result = client.callJSONObject(name, user_name,
					password, 50, deviceToken, channel);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 用户自动登录接口
	 */
	public static String userself_login(String name, String user_name,
			String password, String deviceToken) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject(name, user_name,
					password, 50, deviceToken, 1);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 游客登录
	 */
	public static String tourist_login(String name) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject(name);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	/**
	 * 进入房间之后，获取登录直播地址
	 */
	public static String getLiveUrl(String userId, String key,String roomId) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("resurl", userId, key,roomId);
			s = result.toString();
		} catch (JSONRPCException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 用户登录完成，获取用户详细信息
	 */
	public static String getuserinfo(String id, String key) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("get_userinfo", id, key,
					1, 1);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 用户登录完成，获取用户详细信息
	 */
	public static String getrandomuser() {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_rand_room");
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取我的守护列表
	 */
	public static String getmyguard(String user_id, String crumb) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_my_guards", user_id,
					crumb);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	// 获取视频流和聊天服务器地址
	public static String get_result_url(String id, String secret, String roomid) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("resurl", id, secret,
					roomid);
			s = result.toString();
		} catch (JSONRPCException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 注销登录接口
	 */
	public static String outlogin() {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_guest_login", 2);
			s = result.toString();
		} catch (JSONRPCException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 修改昵称
	 */
	public static String resetnicheng(String user_id, String sercet, String text) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_update_nickname",
					user_id, sercet, text);
			s = result.toString();
		} catch (JSONRPCException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取排行榜列表
	 * 
	 * @return
	 */
	public static String getranklist(String name, String time) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject(name, time, "20");
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 获取风云版数据（上月）
	 * 
	 */
	public static String getbillboard(String name) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject(name);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 获取搜索结果接口
	 * 
	 */
	public static String getsearchdata(String text) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("search", text);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取任务列表
	 * 
	 */
	public static String gettasklist(String userId, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			// JSONObject result = client.callJSONObject("m_task_list", userId);
			JSONObject result = client.callJSONObject("get_0058_task_list",
					userId, secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 做完一个任务，更新任务完成状态
	 * 
	 */
	public static String update_task_reward(String userId, String secret,
			String task_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client1 = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client1.setConnectionTimeout(5000);
			client1.setSoTimeout(5000);
			JSONObject result = client1.callJSONObject("m_update_task", userId,
					secret, task_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 领取任务奖励
	 * 
	 */
	public static String gettask_reward(String userId, String secret,
			String task_id, String client, String room_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client1 = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client1.setConnectionTimeout(5000);
			client1.setSoTimeout(5000);
			JSONObject result = client1.callJSONObject("m_get_task_reward",
					userId, secret, task_id, client, room_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取验证码接口
	 * 
	 */
	public static String getrandomnum(String user_num) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client1 = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client1.setConnectionTimeout(5000);
			client1.setSoTimeout(5000);
			JSONObject result = client1.callJSONObject("m_get_registercode",
					user_num);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 注册接口
	 * 
	 */
	public static String register(String user_num, String user_key,
			String user_random, String channel) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client1 = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client1.setConnectionTimeout(5000);
			client1.setSoTimeout(5000);
			JSONObject result = client1.callJSONObject("m_reg", user_num,
					user_key, user_random, channel);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 重置密码
	 * 
	 * @return
	 */
	public static String resetnewpassword(String phone, String yanzhengma,
			String newpassword) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("reset_pwd_by_phone",
					phone, yanzhengma, newpassword);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取找回密码验证码
	 * 
	 * @return
	 */
	public static String getyanzheng(String phone) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("forgot_pwd_sendcode",
					phone);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 获取代理列表
	 * 
	 * @param userid
	 * @param secret
	 * @return
	 */
	public static String get_Agent_list(String uid, String crumb) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("get_agent_list", uid,
					crumb);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	// 获取房间信息
	public static String get_room_Info(String userid, String secret,
			String roomid) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("roominfo", userid,
					secret, roomid);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取个人中心（我的详细数据）
	 * 
	 * @param userid
	 * @param secret
	 * @param roomid
	 * @return
	 */
	public static String get_personInfo(String userid, String secret,
			String tuid) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_profile", userid,
					secret, tuid);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 更新个人信息接口
	 * 
	 */
	public static String update_personmsg(String user_id, String secret,
			String data) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("update_profile",
					user_id, secret, data);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取主播红豆和关注数量
	 * 
	 */
	public static String getheartsandfansnum(String anchor_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject(
					"m_get_hearts_and_fans_num", anchor_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取粉丝周榜
	 */
	public static String getFans(String id, String secret, String room_id,
			String type) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(6 * 10000);
			client.setSoTimeout(6 * 10000);
			JSONObject result = client.callJSONObject("fansTopN", id, secret,
					room_id, type, 5);
			s = result.toString();
		} catch (JSONRPCException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取本场粉丝榜
	 */
	public static String get_live_fans(String anchor_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("livefans", anchor_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取支付宝订单
	 */
	public static String getzhifubaoorder(String user_id, String money,
			String room_id) {
		Log.i("lvjian", "room_id------------->" + room_id);
		Log.i("lvjian", "money---------------->" + money);
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_get_order_no",
					user_id, user_id, "malipay", money, "", room_id, "");
			s = result.toString();
		} catch (JSONRPCException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取银联订单号
	 * 
	 * @param userid
	 * @param secret
	 * @return
	 */
	public static String getunpayorder(String uid, String crumb,
			String op_user_id, String agent_id, String room_id, double fee) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("mupacp_get_trade_no",
					uid, crumb, op_user_id, agent_id, room_id, fee);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			Log.i("lvjian", "获取银联订单号接口异常");
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 关注主播
	 */
	public static String follow(String userId, String secret, String tartgetId) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("follow", userId, secret,
					tartgetId);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 取消关注
	 * 
	 * @author sumao
	 * @param userId
	 *            用户Id
	 * @param secret
	 *            session
	 * @param tartgetId
	 *            被取消的主播Id
	 * @return
	 */
	public static String cancelfollow(String userId, String secret,
			String tartgetId) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("cancelfollow", userId,
					secret, tartgetId);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取观众列表和管理列表
	 * 
	 * @param id
	 * @param key
	 * @param roomid
	 * @param page_id
	 * @param page_number
	 * @return
	 */
	public static String get_room_viewer(String id, String key, String roomid,
			int page_id, int page_number) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_get_roomviewer_list",
					id, key, roomid, page_id, page_number);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 订阅主播
	 */
	public static String SubscibeAnchor(String userId, String secret,
			String anchorid) {
		String s = "";
		String uri = APP.PATH_NEIWANG;
		try {
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(10000);
			client.setSoTimeout(10000);
			JSONObject subscibeanchor;
			subscibeanchor = client.callJSONObject("m_sub", userId, anchorid,
					secret);
			s = subscibeanchor.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取礼物列表
	 * 
	 * @param userid
	 * @param secret
	 * @return
	 */
	public static String get_present_list(String userid, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_get_gifts_list_new",
					userid, secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * @author Jon_V 赠送礼物
	 * @param userid
	 *            用户id
	 * @param secret
	 *            密钥
	 * @param tuids
	 *            被赠送者id
	 * @param gift_id
	 *            礼物id
	 * @param gift_num
	 *            礼物数量
	 * @param room_id
	 *            房间id
	 * @return
	 */
	public static String send_present(String userid, String secret,
			String tuids, String gift_id, String gift_num, String room_id,
			String from_backpack, int is_guard) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_send_gifts", userid,
					secret, tuids, gift_id, gift_num, room_id, from_backpack,
					is_guard);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取主播详细信息接口
	 * 
	 * @param userid
	 * @param secret
	 * @return
	 */
	public static String getanchorfile(String userid, String secret,
			String t_user_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_profile", userid,
					secret, t_user_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 热门搜索
	 * 
	 * @param userid
	 * @param secret
	 * @return
	 */
	public static String gethotsearch() {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("hot_search");
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 我的座驾
	 */
	public static String getmycar(String userid, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("my_mounts", userid,
					secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 房间内贵族和守护接口
	 */
	public static String getvipandguard(String userid, String secret,
			String anchor_id, String room_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("room_stuffinfo", userid,
					secret, anchor_id, room_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 守护列表
	 */
	public static String getguardlist(String anchor_id, String room_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("room_guards2",
					anchor_id, room_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 守护列表new
	 */
	public static String getguardlistnew(String anchor_id, String room_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("room_guards2_new",
					anchor_id, room_id, 1);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 签到信息
	 * 
	 */
	public static String qiandao(String userId, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_sign_list", userId,
					secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 签到
	 * 
	 */
	public static String qiandaotoday(String userId, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_sign", userId, secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 新的签到信息
	 * 
	 */
	public static String getqiandaomsg(String userId, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("sign_daily_info",
					userId, secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 新的点击签到接口
	 * 
	 */
	public static String new_qd(String userId, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("sign_daily", userId,
					secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 领取礼物
	 * 
	 */
	public static String qiandaogetpresent(String userId, String secret,
			String type) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_get_sign_reward",
					userId, secret, type);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取座驾列表信息
	 * 
	 * @return
	 */
	public static String getTheCarData() {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("mount_list");
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			System.out.println("-----------获取座驾列表信息异常-------------");
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 购买座驾
	 * 
	 * @return
	 */
	public static String BuyTheCar(String user_id, String secret,
			String mount_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_buy_mount", user_id,
					secret, mount_id, "1", user_id, "0");
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 切换座驾
	 */
	public static String ChangeCar(String user_id, String secret,
			String mount_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_change_mount",
					user_id, secret, mount_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 不显示进场动画
	 */
	public static String nocar(String user_id, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("mount_unbind", user_id,
					secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 意见反馈
	 * 
	 * @return
	 */
	public static String feedback(String user_id, String secret, String text) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("appfeedback", user_id,
					secret, text);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			System.out.println("-----------获取座驾列表信息异常-------------");
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 启用页广告图片地址
	 * 
	 */
	public static String getImageUrl() {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_open_config");
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			System.out.println("-----------获取座驾列表信息异常-------------");
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 充值卡充值
	 * 
	 * @return
	 */
	public static String CardRecharge(String user_id, String money) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_get_order_no",
					user_id, user_id, "myeepay", money, "", "0", "");
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 检查充值卡订单
	 * 
	 * @return
	 */
	public static String CheckOrder(String user_id, String ordernumber) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_q_order_no", user_id,
					ordernumber);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 短信充值成功付款后，验证订单号更新乐币接口
	 */
	public static String updateuser_lebi(String uid, String secret,
			String ordernumber) {
		Log.i("lvjian", "userid------------>" + uid);
		Log.i("lvjian", "secret------------>" + secret);
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			// String uri="http://223.4.144.110:5800/api/rpc/pc.php";
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("sms_recharge", uid,
					secret, ordernumber);
			s = result.toString();
			Log.i("lvjian", "中国-------》" + result);
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			Log.i("lvjian", e.toString());
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 直播间开通 购买守护
	 * 
	 */
	public static String buyguard(String userId, String secret, String room_id,
			String anchor_id, String months, String type) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_buy_guards", userId,
					secret, room_id, anchor_id, months, type);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 魔幻卡牌接口
	 * 
	 */
	public static String GetMagicCard(String user_id, String secret,
			String type, String room_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_poker_draw", user_id,
					secret, type, room_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 存入保险箱
	 * 
	 * @return
	 */
	public static String Inmoney(String user_id, String seart, String num) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_save_safe", user_id,
					seart, num);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 从保险箱取出
	 * 
	 * @return
	 */
	public static String getoutmoney(String user_id, String seart, String num,
			String password) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_decrease_safe",
					user_id, seart, num, password);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 修改保险箱密码
	 * 
	 * @return
	 */
	public static String reset_strongpassword(String user_id, String seart,
			String oldpwd, String newpwd1, String newpwd2) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_safeacc_alterpwd",
					user_id, seart, oldpwd, newpwd1, newpwd2);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return s;

	}

	/**
	 * 注册保险箱
	 * 
	 * @return
	 */
	public static String registersafe(String user_id, String seart, String pwd,
			String pwd2) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_safeacc_register",
					user_id, seart, pwd, pwd2);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return s;

	}

	// 判断是否注册过保险箱
	public static String get_SafeRegister(String userid, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_get_safe_info",
					userid, secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取仓库礼物列表
	 * 
	 * @param userid
	 * @param secret
	 * @return
	 */
	public static String get_canku_list(String userid, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_get_bags", userid,
					secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 领取财富等级礼包
	 */
	public static String getcaifudengjigift(String userid, String secret,
			String lv) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			Log.i("lvjian", "userid------------>" + userid);
			Log.i("lvjian", "secret------------>" + secret);
			JSONObject result = client.callJSONObject("pc_rich_getfbg", userid,
					secret, lv, "1");
			// 1代表android
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 发送豆子
	 * 
	 * @param userid
	 * @param secret
	 * @param anchorid
	 * @param roomid
	 * @return
	 */
	public static String send_hearts(String userid, String secret,
			String anchorid, String roomid, String from_backpack, String num) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_send_hearts", userid,
					secret, anchorid, roomid, from_backpack, num);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	public static void hiddenKeyBoard(Context paramContext) {
		if (paramContext == null) {
			return;
		}
		try {
			((InputMethodManager) paramContext.getSystemService("input_method"))
					.hideSoftInputFromWindow(((Activity) paramContext)
							.getCurrentFocus().getWindowToken(), 2);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 发送广播
	 * 
	 * @param userid
	 * @param secret
	 * @param room_id
	 * @param text
	 * @return
	 */
	public static String send_gb(String userid, String secret, String room_id,
			String text, String type) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_sendmessage", userid,
					secret, room_id, text, type);
			s = result.toString();
			Log.i("lvjian", "广播--------------->" + s);
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 禁言和解禁
	 * 
	 * @param userid
	 * @param secret
	 * @param room_id
	 * @param tuid
	 * @param type
	 * @return
	 */
	public static String shut_up(String userid, String secret, String room_id,
			String tuid, String type) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_shut_up", userid,
					secret, room_id, tuid, "300", type);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 每一分钟请求红豆接口
	 * 
	 */
	public static JSONObject GetRedhearts(String userid, String secret) {
		JSONObject s = null;
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_add_hearts", userid,
					secret, "1");
			s = result;
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	public static int GetWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取微信订单
	 * 
	 */
	public static String getwx_order(String userId, String secret,
			String op_user_id, String agent_id, String room_id, String fee) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client1 = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client1.setConnectionTimeout(5000);
			client1.setSoTimeout(5000);
			JSONObject result = client1.callJSONObject("m_wechat_order",
					userId, secret, op_user_id, agent_id, room_id, fee);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("lvjian", "获取微信订单异常");
		}
		return s;
	}

	/**
	 * 获取禁言列表
	 * 
	 * @param userid
	 * @param secret
	 * @param room_id
	 * @return
	 */

	public static String get_shut_up_list(String userid, String secret,
			String room_id) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_get_shutup_list",
					userid, secret, room_id);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/***
	 * 判断 String 是否是 int
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isInteger(String input) {
		Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
		return mer.find();
	}

	/**
	 * 获取免费广播数目
	 * 
	 * @return
	 */
	public static String getgbnumber(String userId, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("m_get_free_horns",
					userId, secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 手机号码验证接口
	 */
	public static String sjyz(String userid, String crumb, String phone,
			String yzm) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("vmobile", userid, crumb,
					phone, yzm);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 手机号码验证接口
	 */
	public static String sjyz_getyanzhengma(String userid, String crumb,
			String phone) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("get_vmobile_code",
					userid, crumb, phone);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 双十一领取礼物
	 */
	public static String getdoublegift(String userid, String crumb, String type) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_hd_ggifbag", userid,
					crumb, type);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 领取首充礼物
	 */
	public static String getshouchonggift(String userid, String crumb,
			String type) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject("pc_sc_getfbg", userid,
					crumb, type);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	


	/**
	 * 年度盛典特别奖排行
	 */
	public static String gettebiejiangrank(String method) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject(method);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 领取抢票
	 */
	public static String getndsd_qiangpiao(String user_id, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			Log.i("lvjian", "user_id------------>" + user_id);
			Log.i("lvjian", "secret------------>" + secret);
			JSONObject result = client.callJSONObject("active_celedraw",
					user_id, secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 年度盛典
	 */
	public static String getndsd_msg(String user_id, String secret) {
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			Log.i("lvjian", "user_id------------>" + user_id);
			Log.i("lvjian", "secret------------>" + secret);
			JSONObject result = client.callJSONObject("active_cele_info",
					user_id, secret);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	//发送rpc请求
	public static String addRpcEvent(RpcEvent event,Object ... params){
		String s = "";
		try {
			String uri = APP.PATH_NEIWANG;
			MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
					new DefaultHttpClient(), uri);
			client.setConnectionTimeout(5000);
			client.setSoTimeout(5000);
			JSONObject result = client.callJSONObject(event.name, params);
			s = result.toString();
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	
	@SuppressLint("NewApi")
	public static String getDiskPhotoWallCacheDir(Context context) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return (cachePath + File.separator + "photowall");
	}
	
	
	
/**
 * sjf
 * 
 * 显示图片的中间区域
 * @param bitmap
 * @param edgeLength
 * @return
 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap) {
		
		int edgeLength = 0;
		int edgeLength_show = 0;
		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		edgeLength = widthOrg >= heightOrg ? heightOrg : widthOrg;
		edgeLength_show = edgeLength;
		float scaleNormal = 1;
		int real_w = 0 ;
		int real_h = 0 ;
		if(scaleNormal>1.0){
			real_w = edgeLength;
			real_h =(int) (edgeLength/scaleNormal) ;
		}else{
			real_w = (int) (edgeLength*scaleNormal);
			real_h =edgeLength ;
		}
		// 从图中截取正中间的正方形部分。
		int xTopLeft = (widthOrg - real_w) / 2;
		int yTopLeft = (heightOrg - real_h) / 2;

		try {
			result = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft,real_w-1, real_h-1);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 指定显示的宽高
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, float width,float height) {

		
		int edgeLength = 0;
		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		edgeLength = widthOrg >= heightOrg ? heightOrg : widthOrg;

		float scaleNormal = width*1.0f / height;
		
		int real_w = 0 ;
		int real_h = 0 ;
		if(scaleNormal>1.0){
			real_w = edgeLength;
			real_h =(int) (edgeLength/scaleNormal) ;
		}else{
			real_w = (int) (edgeLength*scaleNormal);
			real_h =edgeLength ;
		}
		// 从图中截取正中间的正方形部分。
		int xTopLeft = (widthOrg - real_w) / 2;
		int yTopLeft = (heightOrg - real_h) / 2;
		try {
			result = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft,real_w-1, real_h-1);
		} catch (Exception e) {
			return null;
		}
		return result;


	}

	/**
	 * 判断手机是否有SD卡。
	 * 
	 * @return 有SD卡返回true，没有返回false。
	 */
	private boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
}