package com.zhipu.chinavideo;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MobileVerification extends Activity implements OnClickListener {
	private ImageView title_back;
	private TextView title_text;
	private EditText phone_number;
	private EditText yanzhengma;
	private String phone;
	private String yzm;
	private LinearLayout yz_sure;
	private TextView send_code;
	private String data;
	private int time = 180;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobileverification);
		title_back = (ImageView) findViewById(R.id.title_back);
		title_back.setOnClickListener(this);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("手机验证");
		phone_number = (EditText) findViewById(R.id.phone_number);
		yanzhengma = (EditText) findViewById(R.id.yanzhengma);
		yz_sure = (LinearLayout) findViewById(R.id.yz_sure);
		yz_sure.setOnClickListener(this);
		send_code = (TextView) findViewById(R.id.send_code);
		send_code.setOnClickListener(this);
		sharedPreferences = getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.yz_sure:
			phone = phone_number.getText().toString().trim();
			yzm = yanzhengma.getText().toString().trim();
			if (Utils.isEmpty(phone) || Utils.isEmpty(yzm)) {
				Utils.showToast(MobileVerification.this, "请输入手机号和验证码！");
			} else {
				shoujiyanzheng(phone, yzm);
			}
			break;
		case R.id.send_code:
			phone = phone_number.getText().toString().trim();
			if (Utils.isEmpty(phone)) {
				Utils.showToast(MobileVerification.this, "请输入手机号！");
			} else {
				getyanzhengcode(phone);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param phone
	 */
	private void getyanzhengcode(final String phone) {
		// TODO Auto-generated method stub
		Runnable getyanzhengcoderun = new Runnable() {
			public void run() {
				String s = Utils.sjyz_getyanzhengma(sharedPreferences.getString(APP.USER_ID, ""),
						sharedPreferences.getString(APP.SECRET, ""),phone);
				Log.i("lvjian", "获取验证码====》" + s);
				try {
					JSONObject obj = new JSONObject(s);
					data = obj.getString("data");
					String s1 = obj.getString("s");
					if (s1.equals("1")) {
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getyanzhengcoderun);
	}

	/**
	 * 手机验证
	 * 
	 * @param phone
	 */
	private void shoujiyanzheng(final String phone, final String yzm) {
		// TODO Auto-generated method stub
		Runnable shoujiyanzhengrun = new Runnable() {
			public void run() {
				String s = Utils
						.sjyz(sharedPreferences.getString(APP.USER_ID, ""),
								sharedPreferences.getString(APP.SECRET, ""),
								phone, yzm);
				Log.i("lvjian", "获取验证码====》" + s);
				try {
					JSONObject obj = new JSONObject(s);
					String s1 = obj.getString("s");
					String data = obj.getString("data");
					if (s1.equals("1")) {
						Message msg = new Message();
						msg.what = 4;
						msg.obj = data;
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = 5;
						msg.obj = data;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(shoujiyanzhengrun);
	}

	/**
	 * handler
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				send_code.setClickable(false);
				send_code.setText(time + "秒");
				msg = obtainMessage();
				msg.what = 1;
				msg.arg1 = time--;
				if (time >= 0) {
					sendMessageDelayed(msg, 1000);
				} else {
					sendEmptyMessage(2);
				}
				break;
			case 2:
				time = 180;
				send_code.setClickable(true);
				send_code.setText("获取验证码");
				break;
			case 3:
				Utils.showToast(MobileVerification.this, data);
				break;
			// 完成
			case 4:
				Log.i("lvjian", "--------验证成功----------");
				String m = msg.obj.toString();
				Utils.showToast(MobileVerification.this, m);
				setResult(21, null);
				finish();
				break;
			case 5:
				Log.i("lvjian", "--------验证失败----------");
				String m1 = msg.obj.toString();
				Utils.showToast(MobileVerification.this, m1);
				break;
			default:
				break;
			}
		};
	};
}
