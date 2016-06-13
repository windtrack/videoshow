package com.zhipu.chinavideo;

import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FogretPwdActivity extends Activity implements OnClickListener {
	private int time = 180;
	private EditText phone_number;
	private EditText yanzhengma;
	private EditText newpassword;
	private String phone, yanzheng, password;
	private TextView queding;
	private ImageView login_back;
	private TextView send_code;
	private LinearLayout sure;
	String data = "";
	String data1 = "";
	private TextView title_tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fogretpassword);
		phone_number = (EditText) findViewById(R.id.phone_number);
		yanzhengma = (EditText) findViewById(R.id.yanzhengma);
		newpassword = (EditText) findViewById(R.id.newpassword);
		queding = (TextView) findViewById(R.id.queding);
		login_back = (ImageView) findViewById(R.id.title_back);
		send_code = (TextView) findViewById(R.id.send_code);
		sure = (LinearLayout) findViewById(R.id.sure);
		title_tv = (TextView) findViewById(R.id.title_text);
		title_tv.setText("找回密码");
		queding.setOnClickListener(this);
		login_back.setOnClickListener(this);
		send_code.setOnClickListener(this);
		sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 发送手机验证码
		case R.id.send_code:
			phone = phone_number.getText().toString();
			if (Utils.isEmpty(phone)) {
				Toast.makeText(this, "手机号不能为空", 100).show();
			} else {
				getyanzhengcode(phone);
			}
			break;
		case R.id.sure:
			phone = phone_number.getText().toString();
			yanzheng = yanzhengma.getText().toString();
			password = newpassword.getText().toString();
			if (Utils.isEmpty(phone) || Utils.isEmpty(yanzheng)
					|| Utils.isEmpty(password)) {
				Toast.makeText(this, "还有信息没填", 100).show();
			} else {
				resetpassword(phone, yanzheng, password);
			}
			break;
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 重置密码
	 * 
	 * @param phone2
	 * @param yanzheng2
	 * @param password2
	 */
	private void resetpassword(final String phone2, final String yanzheng2,
			final String password2) {
		// TODO Auto-generated method stub
		Runnable resetpasswordrun = new Runnable() {
			public void run() {
				String s = Utils.resetnewpassword(phone2, yanzheng2, password2);
				Log.i("lvjian", "重置密码====》" + s);
				try {
					JSONObject obj = new JSONObject(s);
					data1 = obj.getString("data");
					String s2 = obj.getString("s");
					if (s2.equals("1")) {
						mHandler.sendEmptyMessage(3);
					} else {
						mHandler.sendEmptyMessage(33);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(resetpasswordrun);
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
				String s = Utils.getyanzheng(phone);
				Log.i("lvjian", "获取验证码====》" + s);
				try {
					JSONObject obj = new JSONObject(s);
					data = obj.getString("data");
					String s1 = obj.getString("s");
					if (s1.equals("1")) {
						mHandler.sendEmptyMessage(1);
					} else {
						mHandler.sendEmptyMessage(11);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getyanzhengcoderun);
	}

	private Handler mHandler = new Handler() {
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
			case 11:
				Utils.showToast(FogretPwdActivity.this, data);
				break;
			case 2:
				time = 180;
				send_code.setClickable(true);
				send_code.setText("获取验证码");
				break;
			case 3:
				Utils.showToast(FogretPwdActivity.this, data1);
				Intent intent = new Intent();
				intent.putExtra("username", phone);
				intent.putExtra("password", password);
				setResult(20, intent);
				finish();
				break;
			case 33:
				Utils.showToast(FogretPwdActivity.this, data1);
				break;
			default:
				break;
			}
		};
	};

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
