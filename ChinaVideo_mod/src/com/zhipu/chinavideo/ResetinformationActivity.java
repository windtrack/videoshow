package com.zhipu.chinavideo;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ResetinformationActivity extends Activity implements
		OnClickListener {
	private ImageView back;
	// private TextView title;
	private String title_str;
	private TextView baocun;
	private SharedPreferences sharedPreferences;
	private EditText reset_nicheng;
	private String nicheng;
	private String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetiformation);
		sharedPreferences = getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		back = (ImageView) findViewById(R.id.back_img);
		back.setOnClickListener(this);
		// title = (TextView) findViewById(R.id.title_tv);
		Intent intent = this.getIntent();
		title_str = intent.getStringExtra("title");
		// title.setText(title_str);
		baocun = (TextView) findViewById(R.id.baocun);
		reset_nicheng = (EditText) findViewById(R.id.reset_nicheng);
		reset_nicheng.setText(title_str);
		Selection.setSelection(reset_nicheng.getText(), title_str.length());
		baocun.setOnClickListener(this);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager m = (InputMethodManager) reset_nicheng
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 300);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_img:
			finish();
			break;
		case R.id.baocun:
			// 昵称修改
			nicheng = reset_nicheng.getText().toString().trim();
			if ("".equals(nicheng) || nicheng == null) {
				Utils.showToast(ResetinformationActivity.this, "请输入昵称");
			} else {
				resetnicheng(sharedPreferences.getString(APP.USER_ID, ""),
						sharedPreferences.getString(APP.SECRET, ""), nicheng);
			}
			break;
		default:
			break;
		}
	}

	private void resetnicheng(final String user_id, final String sercet,
			final String text) {
		Runnable resetnicheng = new Runnable() {
			public void run() {
				String s = Utils.resetnicheng(user_id, sercet, text);
				Log.i("lvjian", "s-------------->" + s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int i = jsonObject.getInt("s");
					data = jsonObject.getString("data");
					if (i == 1) {
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(resetnicheng);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				getupdatetaskreward(
						sharedPreferences.getString(APP.USER_ID, ""),
						sharedPreferences.getString(APP.SECRET, ""), "3");
				Utils.showToast(ResetinformationActivity.this, data);
				Editor editor = sharedPreferences.edit();
				editor.putString(APP.NICKNAME, nicheng);
				editor.commit();
				setResult(22);
				finish();
				break;
			case 2:
				Utils.showToast(ResetinformationActivity.this, data);
				break;
			case 3:
				Log.i("lvjian", "修改昵称接口异常");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 修改昵称后,改变任务中状态更新
	 */
	private void getupdatetaskreward(final String userId, final String secret,
			final String task_id) {
		// TODO Auto-generated method stub
		Runnable resetnichengtask = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.update_task_reward(userId, secret,
							task_id);
					JSONObject obj = new JSONObject(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(resetnichengtask);
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
