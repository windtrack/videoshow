package com.zhipu.chinavideo;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

public class RegisterActivity extends Activity implements OnClickListener {
	// 渠道
	private String channel = "安智CPI";
	// 注册，上传服务器渠道
	private EditText phonum;
	private EditText keynum;
	private EditText rannum;
	private TextView sign_get;
	private int time = 180;
	private String user_num;
	private String user_key;
	private String user_random;
	private ProgressDialog progressDialog;
	private ImageView sign_back;
	private TextView title_text;
	private String data = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		sign_back = (ImageView) findViewById(R.id.title_back);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("手机注册");
		LinearLayout sign_back_s = (LinearLayout) findViewById(R.id.sign_back_s);
		LinearLayout sign_next = (LinearLayout) findViewById(R.id.sign_next);
		sign_get = (TextView) findViewById(R.id.sign_get);
		phonum = (EditText) findViewById(R.id.sign_pho_edit);
		keynum = (EditText) findViewById(R.id.sign_key_edit);
		rannum = (EditText) findViewById(R.id.sign_random_edit);
		sign_back.setOnClickListener(this);
		sign_back_s.setOnClickListener(this);
		sign_next.setOnClickListener(this);
		sign_get.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.sign_back_s:
			onBackPressed();
			break;
		case R.id.sign_next:
			user_num = phonum.getText().toString();
			user_key = keynum.getText().toString();
			user_random = rannum.getText().toString();
			if ("".equals(user_num)) {
				Utils.showToast(this, "请输入合法的手机号！");
				return;
			} else {
				if ("".equals(user_key)) {
					Utils.showToast(this, "请输入密码！");
					return;
				} else {
					initProgressDialog();
					progressDialog.setCancelable(false);
					sign();
				}
			}

			break;
		case R.id.sign_get:
			user_num = phonum.getText().toString();
			user_key = keynum.getText().toString();
			user_random = rannum.getText().toString();
			if ("".equals(user_num)) {
				Utils.showToast(this, "请输入合法的手机号！");
				return;
			} else {
				getRandomNum();
			}
			break;
		default:
			break;
		}
	}

	private void sign() {
		// TODO Auto-generated method stub
		Runnable getregisterrun = new Runnable() {
			public void run() {
				String s = Utils.register(user_num, user_key, user_random,
						channel);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int i = jsonObject.getInt("s");
					data = jsonObject.getString("data");
					if (i == 1) {
						mHandler.sendEmptyMessage(3);
					} else {
						mHandler.sendEmptyMessage(5);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getregisterrun);
	}

	private void getRandomNum() {
		// TODO Auto-generated method stub
		Runnable getrandomnumrun = new Runnable() {
			public void run() {
				String s = Utils.getrandomnum(user_num);
				Log.i("lvjian", "验证码：---》"+s)  ;
				try {
					JSONObject jsonObject = new JSONObject(s);
					int i = jsonObject.getInt("s");
					data = jsonObject.getString("data");
					if (i == 1) {
						mHandler.sendEmptyMessage(1);
					} else {
						mHandler.sendEmptyMessage(4);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getrandomnumrun);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				sign_get.setClickable(false);
				sign_get.setText(time + "秒");
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
				sign_get.setClickable(true);
				sign_get.setText("获取验证码");
				break;
			case 3:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				Utils.showToast(RegisterActivity.this, data);
				Intent intent = new Intent();
				intent.putExtra("username", user_num);
				intent.putExtra("password", user_key);
				setResult(20, intent);
				finish();
				break;
			case 4:
				Utils.showToast(RegisterActivity.this, data);
				break;
			case 5:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				Utils.showToast(RegisterActivity.this, data);
				break;

			default:
				break;
			}
		}
	};

	private void initProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("注册中，请稍后···");
		progressDialog.show();
	}
}
