package com.zhipu.chinavideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QianDaoActivity extends Activity implements OnClickListener {
	private Dialog dialog;
	private ImageView title_back;
	private TextView title_text;
	private SharedPreferences preferences;
	private String user_id = "";
	private String secret = "";
	// 签到总天数
	private String signed_total_day;
	// 签到礼物是否领取
	private boolean tv1_isget = false;
	private boolean tv2_isget = false;
	private boolean tv3_isget = false;
	private boolean tv4_isget = false;
	private ImageView sign_ok_one;
	private ImageView sign_ok_two;
	private ImageView sign_ok_three;
	private ImageView sign_ok_four;
	private Button qiandao_linqu1;
	private Button qiandao_linqu2;
	private Button qiandao_linqu3;
	private Button qiandao_linqu4;
	private int total = 0;
	private TextView qiandao_totalday;
	private int gift_type = 1;
	private View qiandao_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qiandao);
		initview();
		initdata();
	}

	private void initview() {
		// TODO Auto-generated method stub
		title_back = (ImageView) findViewById(R.id.title_back);
		title_text = (TextView) findViewById(R.id.title_text);
		title_back.setOnClickListener(this);
		title_text.setText("签到");
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		user_id = preferences.getString(APP.USER_ID, "");
		secret = preferences.getString(APP.SECRET, "");
		sign_ok_one = (ImageView) findViewById(R.id.sing_ok_one);
		sign_ok_two = (ImageView) findViewById(R.id.sing_ok_two);
		sign_ok_three = (ImageView) findViewById(R.id.sing_ok_three);
		sign_ok_four = (ImageView) findViewById(R.id.sing_ok_four);
		qiandao_linqu1 = (Button) findViewById(R.id.qiandao_linqu1);
		qiandao_linqu2 = (Button) findViewById(R.id.qiandao_linqu2);
		qiandao_linqu3 = (Button) findViewById(R.id.qiandao_linqu3);
		qiandao_linqu4 = (Button) findViewById(R.id.qiandao_linqu4);
		qiandao_linqu1.setOnClickListener(this);
		qiandao_linqu2.setOnClickListener(this);
		qiandao_linqu3.setOnClickListener(this);
		qiandao_linqu4.setOnClickListener(this);
		qiandao_totalday = (TextView) findViewById(R.id.qiandao_totalday);
		qiandao_loading = findViewById(R.id.qiandao_loading);
		dialog = Utils.showProgressDialog(this, "领取中", true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.qiandao_linqu1:
			if (!tv1_isget && total >= 3) {
				gift_type = 1;
				GetQDpresent("1");
			}
			break;
		case R.id.qiandao_linqu2:
			if (!tv2_isget && total >= 8) {
				gift_type = 2;
				GetQDpresent("2");
			}
			break;
		case R.id.qiandao_linqu3:
			if (!tv3_isget && total >= 15) {
				gift_type = 3;
				GetQDpresent("3");
			}
			break;
		case R.id.qiandao_linqu4:
			if (!tv4_isget && total >= 25) {
				gift_type = 4;
				GetQDpresent("4");
			}
			break;
		default:
			break;
		}
	}

	private void initdata() {
		qiandao_loading.setVisibility(0);
		Runnable qiandaorun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.qiandao(user_id, secret);
					JSONObject obj = new JSONObject(result);
					int a = obj.getInt("s");
					JSONObject oj = obj.getJSONObject("data");
					signed_total_day = oj.getString("signed_total_day");
					JSONArray jj = oj.getJSONArray("has_rewarded");
					for (int i = 0; i < jj.length(); i++) {
						if (jj.getString(i).equals("1")) {
							tv1_isget = true;
						}
						if (jj.getString(i).equals("2")) {
							tv2_isget = true;
						}
						if (jj.getString(i).equals("3")) {
							tv3_isget = true;
						}
						if (jj.getString(i).equals("4")) {
							tv4_isget = true;
						}
					}
					handler.sendEmptyMessage(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(qiandaorun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				qiandao_loading.setVisibility(8);
				qiandao_totalday.setText(signed_total_day);
				total = Integer.parseInt(signed_total_day);
				if (total >= 3) {
					sign_ok_one.setVisibility(0);
					qiandao_linqu1.setVisibility(0);
					// 如果已经领取
					if (tv1_isget) {
						qiandao_linqu1.setText("已领取");
						qiandao_linqu1
								.setBackgroundResource(R.drawable.getok_button_shape);
					} else {
						qiandao_linqu1.setText("领取");
						qiandao_linqu1
								.setBackgroundResource(R.drawable.button_bg_shape);
					}
				}
				if (total >= 8) {
					sign_ok_two.setVisibility(0);
					qiandao_linqu2.setVisibility(0);
					if (tv2_isget) {
						qiandao_linqu2.setText("已领取");
						qiandao_linqu2
								.setBackgroundResource(R.drawable.getok_button_shape);
					} else {
						qiandao_linqu2.setText("领取");
						qiandao_linqu2
								.setBackgroundResource(R.drawable.button_bg_shape);
					}
				}
				if (total >= 15) {
					sign_ok_three.setVisibility(0);
					qiandao_linqu3.setVisibility(0);
					if (tv3_isget) {
						qiandao_linqu3.setText("已领取");
						qiandao_linqu3
								.setBackgroundResource(R.drawable.getok_button_shape);
					} else {
						qiandao_linqu3.setText("领  取");
						qiandao_linqu3
								.setBackgroundResource(R.drawable.button_bg_shape);
					}
				}
				if (total >= 25) {
					sign_ok_four.setVisibility(0);
					qiandao_linqu4.setVisibility(0);
					if (tv4_isget) {
						qiandao_linqu4.setText("已领取");
						qiandao_linqu4
								.setBackgroundResource(R.drawable.getok_button_shape);
					} else {
						qiandao_linqu4.setText("领取");
						qiandao_linqu4
								.setBackgroundResource(R.drawable.button_bg_shape);
					}
				}
				break;
			case 2:
				qiandao_loading.setVisibility(8);
				Utils.showToast(QianDaoActivity.this, "数据异常");
				break;
			// 领取签到礼物成功
			case 3:
				dialog.dismiss();
				Utils.showToast(QianDaoActivity.this, "领取成功");
				if (gift_type == 1) {
					qiandao_linqu1
							.setBackgroundResource(R.drawable.getok_button_shape);
				} else if (gift_type == 2) {
					qiandao_linqu2
							.setBackgroundResource(R.drawable.getok_button_shape);
				} else if (gift_type == 3) {
					qiandao_linqu3
							.setBackgroundResource(R.drawable.getok_button_shape);
				} else if (gift_type == 4) {
					qiandao_linqu4
							.setBackgroundResource(R.drawable.getok_button_shape);
				} else {

				}
				break;
			// 领取签到礼物失败
			case 4:
				dialog.dismiss();
				String data = msg.obj.toString();
				if (Utils.isEmpty(data)) {
					Utils.showToast(QianDaoActivity.this, "领取失败");
				} else {
					Utils.showToast(QianDaoActivity.this, data);

				}
				break;
			case 5:
				dialog.dismiss();
				Utils.showToast(QianDaoActivity.this, "数据接口异常");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 点击领取签到礼物
	 */
	private void GetQDpresent(final String type) {
		// TODO Auto-generated method stub
		dialog.show();
		Runnable getpresentrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.qiandaogetpresent(user_id, secret,
							type);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						handler.sendEmptyMessage(3);
					} else {
						String str = obj.getString("data");
						Message msg = new Message();
						msg.what = 4;
						if (!Utils.isEmpty(str)) {
							msg.obj = str;
						} else {
							msg.obj = "领取失败";
						}
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(5);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getpresentrun);
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
