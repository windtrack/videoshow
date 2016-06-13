package com.zhipu.chinavideo;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Strongbox_Activity extends Activity implements OnClickListener {
	private ImageView strongbox_back;
	private String safe_beans = "";
	private String user_beans = "";
	private TextView strongbox_usermoney;
	private TextView strongbox_moneycount;
	private TextView strongbox_user_name;
	private String name = "";
	private String icon = "";
	private Drawable draw = null;
	private CircularImage user_img;
	PopupWindow pw;
	// 个人信息
	private RelativeLayout strongbox_myself;

	// 存入保险箱
	private RelativeLayout instrongbox;
	// 从保险箱取出
	private RelativeLayout strongbox_getout;
	// 重置密码
	private RelativeLayout ret_pwd;
	private View view;
	private String user_id = "";
	private String secret = "";
	private String num = "";
	private String instrongbox_msg = "";
	private String getout_et = "";
	private String getout_password = "";
	private String old_pwd = "";
	private String new_pwd1 = "";
	private String new_pwd2 = "";
	private String reset_msg = "";
	private EditText safe_password_reset;
	private EditText safe_password_set;
	private String pwd = "";
	private String pwd2 = "";
	private LinearLayout safe_password_sure;
	private LinearLayout register_no;
	private LinearLayout register_yes;
	private String isregisterok = "";
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private TextView title;
	private SharedPreferences preferencesn;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.strongbox);
		view = findViewById(R.id.strongbox_view);
		preferencesn = this.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		user_id = preferencesn.getString(APP.USER_ID, "");
		secret = preferencesn.getString(APP.SECRET, "");
		name = preferencesn.getString(APP.NICKNAME, "");
		icon = preferencesn.getString(APP.USER_ICON, "");
		register_no = (LinearLayout) findViewById(R.id.register_no);
		register_yes = (LinearLayout) findViewById(R.id.register_yes);
		title = (TextView) findViewById(R.id.title_text);
		dialog = Utils.showProgressDialog(this, "进行中...", true);
		title.setText("保险箱");
		safe_password_set = (EditText) findViewById(R.id.safe_password_set);
		safe_password_reset = (EditText) findViewById(R.id.safe_password_reset);
		safe_password_sure = (LinearLayout) findViewById(R.id.safe_password_sure);
		safe_password_sure.setOnClickListener(this);
		strongbox_back = (ImageView) findViewById(R.id.title_back);
		strongbox_back.setOnClickListener(this);
		strongbox_usermoney = (TextView) findViewById(R.id.strongbox_usermoney);
		strongbox_moneycount = (TextView) findViewById(R.id.strongbox_moneycount);
		strongbox_user_name = (TextView) findViewById(R.id.strongbox_user_name);
		strongbox_user_name.setText(name);
		user_img = (CircularImage) findViewById(R.id.strongbox_img);
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(this));
		mImageLoader.displayImage(icon, user_img, mOptions);
		instrongbox = (RelativeLayout) findViewById(R.id.instrongbox);
		instrongbox.setOnClickListener(this);
		strongbox_getout = (RelativeLayout) findViewById(R.id.strongbox_getout);
		strongbox_getout.setOnClickListener(this);
		strongbox_myself = (RelativeLayout) findViewById(R.id.strongbox_myself);
		strongbox_myself.setOnClickListener(this);
		ret_pwd = (RelativeLayout) findViewById(R.id.ret_pwd);
		ret_pwd.setOnClickListener(this);
		getisregist(user_id, secret);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		// 存入保险箱
		case R.id.instrongbox:
			showPopupWindow(view);
			break;
		// 从保险箱取出
		case R.id.strongbox_getout:
			showgetoutStrongbox(view);
			break;
		// 重置密码
		case R.id.ret_pwd:
			resetpasswordpopupwindow(view);
			break;
		// 个人资料
		case R.id.strongbox_myself:
			// startActivity(new Intent(Strongbox_Activity.this,
			// UserActivity.class));
			break;
		case R.id.safe_password_sure:
			pwd = safe_password_set.getText().toString();
			pwd2 = safe_password_reset.getText().toString();
			registerstrongbox(pwd, pwd2);
			break;
		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				//保存当前乐币
				Editor editor = preferencesn.edit();
				editor.putString(APP.BEANS, user_beans);
				editor.commit();
				strongbox_usermoney.setText(user_beans);
				strongbox_moneycount.setText(safe_beans);
				pw.dismiss();
				break;
			case 2:
				dialog.dismiss();
				Utils.showToast(Strongbox_Activity.this, instrongbox_msg);
				pw.dismiss();
				break;
			case 3:
				Utils.showToast(Strongbox_Activity.this, reset_msg);
				pw.dismiss();
				break;
			case 4:
				strongbox_usermoney.setText(user_beans);
				strongbox_moneycount.setText(safe_beans);
				register_yes.setVisibility(view.VISIBLE);
				register_no.setVisibility(view.GONE);
				break;
			case 5:
				register_yes.setVisibility(view.GONE);
				register_no.setVisibility(view.VISIBLE);
				break;
			case 6:

				Utils.showToast(Strongbox_Activity.this, isregisterok);
				getisregist(user_id, secret);
				break;
			case 7:
				Utils.showToast(Strongbox_Activity.this, isregisterok);
				break;
			default:
				break;
			}
		};
	};

	// 存入保险箱对话框
	public void showPopupWindow(View parent) {
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.instrongbox, null,
				false);
		LinearLayout instrongbox_sure = (LinearLayout) vPopupWindow
				.findViewById(R.id.instrongbox_sure);
		final EditText lb_num = (EditText) vPopupWindow
				.findViewById(R.id.instrongbox_et);
		pw = new PopupWindow(vPopupWindow, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		pw.setBackgroundDrawable(dw);
		// 显示popupWindow对话框
		pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
		vPopupWindow.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = vPopupWindow.findViewById(R.id.rl_strong).getTop();
				int a = vPopupWindow.findViewById(R.id.rl_strong).getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height || y > a) {
						pw.dismiss();
					}
				}
				return true;
			}
		});
		// 存入
		instrongbox_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				num = lb_num.getText().toString();
				inmoneytoStrongbox(num);
			}
		});
	}

	// 取出保险箱
	public void showgetoutStrongbox(View parent) {
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.getoutstrongbox,
				null, false);
		LinearLayout getoutstrongbox_sure = (LinearLayout) vPopupWindow
				.findViewById(R.id.getoutstrongbox_sure);
		final EditText getoutstrongbox_et = (EditText) vPopupWindow
				.findViewById(R.id.getoutstrongbox_et);
		final EditText getoutstrongbox_password = (EditText) vPopupWindow
				.findViewById(R.id.getoutstrongbox_password);
		pw = new PopupWindow(vPopupWindow, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		pw.setBackgroundDrawable(dw);
		// 显示popupWindow对话框
		pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
		vPopupWindow.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = vPopupWindow.findViewById(R.id.rl_strong).getTop();
				int a = vPopupWindow.findViewById(R.id.rl_strong).getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height || y > a) {
						pw.dismiss();
					}
				}
				return true;
			}
		});
		getoutstrongbox_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getout_et = getoutstrongbox_et.getText().toString();
				getout_password = getoutstrongbox_password.getText().toString();
				getoutStrongbox(getout_et, getout_password);
			}
		});
	}

	// 修改保险箱密码
	public void resetpasswordpopupwindow(View parent) {
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.resetpassword,
				null, false);
		LinearLayout reset_password_sure = (LinearLayout) vPopupWindow
				.findViewById(R.id.reset_password_sure);
		final EditText old_password = (EditText) vPopupWindow
				.findViewById(R.id.old_password);
		final EditText new_password = (EditText) vPopupWindow
				.findViewById(R.id.new_password);
		final EditText reset_new_password = (EditText) vPopupWindow
				.findViewById(R.id.reset_new_password);
		pw = new PopupWindow(vPopupWindow, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		pw.setBackgroundDrawable(dw);
		// 显示popupWindow对话框
		pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
		vPopupWindow.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = vPopupWindow.findViewById(R.id.rl_strong).getTop();
				int a = vPopupWindow.findViewById(R.id.rl_strong).getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height || y > a) {
						pw.dismiss();
					}
				}
				return true;
			}
		});
		reset_password_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				old_pwd = old_password.getText().toString();
				new_pwd1 = new_password.getText().toString();
				new_pwd2 = reset_new_password.getText().toString();
				resetpassword(old_pwd, new_pwd1, new_pwd2);
			}
		});
	}

	/**
	 * 存入保险箱
	 * 
	 * @param num
	 */
	private void inmoneytoStrongbox(final String num) {
		dialog.show();
		Runnable inmoney = new Runnable() {
			public void run() {
				String result = Utils.Inmoney(user_id, secret, num);
				try {
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject jb = obj.getJSONObject("data");
						user_beans = jb.getString("useraccount_beans");
						safe_beans = jb.getString("safeaccount_beans");
						mHandler.sendEmptyMessage(1);
					} else {
						instrongbox_msg = obj.getString("data");
						mHandler.sendEmptyMessage(2);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(inmoney);
	}

	/**
	 * 从保险箱取出
	 * 
	 * @param num
	 */
	private void getoutStrongbox(final String num, final String password) {
		// TODO Auto-generated method stub
		dialog.show();
		Runnable inmoney = new Runnable() {
			public void run() {
				String result = Utils.getoutmoney(user_id, secret, num,
						password);
				try {
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject jb = obj.getJSONObject("data");
						user_beans = jb.getString("useraccount_beans");
						safe_beans = jb.getString("safeaccount_beans");
						mHandler.sendEmptyMessage(1);
					} else {
						instrongbox_msg = obj.getString("data");
						mHandler.sendEmptyMessage(2);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(inmoney);
	}

	/**
	 * 修改保险箱密码
	 * 
	 * @param num
	 */
	private void resetpassword(final String oldpwd, final String newpwd1,
			final String newpwd2) {
		// TODO Auto-generated method stub
		Runnable inmoney = new Runnable() {
			public void run() {
				String result = Utils.reset_strongpassword(user_id, secret,
						oldpwd, newpwd1, newpwd2);
				try {
					JSONObject obj = new JSONObject(result);
					reset_msg = obj.getString("data");
					mHandler.sendEmptyMessage(3);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(inmoney);
	}

	/**
	 * 注册保险箱
	 * 
	 * @param num
	 */
	private void registerstrongbox(final String pwd, final String repwd) {
		// TODO Auto-generated method stub
		Runnable inmoney = new Runnable() {
			public void run() {
				String result = Utils.registersafe(user_id, secret, pwd, repwd);
				try {
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					isregisterok = obj.getString("data");
					if (s == 1) {

						mHandler.sendEmptyMessage(6);
					} else {
						mHandler.sendEmptyMessage(7);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(inmoney);
	}

	/**
	 * 判断是否注册过保险箱接口
	 */
	private void getisregist(final String u_id, final String srt) {
		// TODO Auto-generated method stub
		Runnable getroominforun = new Runnable() {
			public void run() {
				try {
					String roominfo = Utils.get_SafeRegister(u_id, srt);
					JSONObject room_info = new JSONObject(roominfo);
					int state = room_info.getInt("s");
					if (state == 1) {
						JSONObject data = room_info.getJSONObject("data");
						int isp = data.getInt("isPass");
						// 判断是否注册
						if (isp == 1) {
							JSONObject user_account = data
									.getJSONObject("user_account");
							user_beans = user_account.getString("beans");
							JSONObject safe_account = data
									.getJSONObject("safe_account");
							safe_beans = safe_account.getString("beans");
							mHandler.sendEmptyMessage(4);
						} else {
							mHandler.sendEmptyMessage(5);
						}

					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getroominforun);
	}
	/**
	 * 友盟统计
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

}