package com.zhipu.chinavideo;

import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeedbackActivity extends Activity implements OnClickListener {
	private ImageView back;
	private TextView title_tv;
	private EditText feedback_input;
	private LinearLayout input_btn;
	private SharedPreferences preferences;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		back = (ImageView) findViewById(R.id.title_back);
		title_tv = (TextView) findViewById(R.id.title_text);
		feedback_input = (EditText) findViewById(R.id.feedback_input);
		input_btn = (LinearLayout) findViewById(R.id.input_btn);
		input_btn.setOnClickListener(this);
		title_tv.setText("感谢您的宝贵意见");
		back.setOnClickListener(this);
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		this.dialog = Utils.showProgressDialog(this, "正在提交", true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.input_btn:
			String text = feedback_input.getText().toString();
			if(Utils.isEmpty(text)){
				Utils.showToast(FeedbackActivity.this, "请输入您的宝贵意见！");
			}else{
				Intput(text);
			}
			break;
		default:
			break;
		}
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

	/**
	 * 点击今天签到
	 */
	private void Intput(final String text) {
		dialog.show();
		final String id = preferences.getString(APP.USER_ID, "");
		final String secret = preferences.getString(APP.SECRET, "");
		Runnable intputrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.feedback(id, secret, text);
					Log.i("lvjian", "意见反馈结果----》" + result);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						String msg = obj.getString("msg");
						Message m = new Message();
						m.what = 1;
						m.obj = msg;
						handler.sendMessage(m);
					} else {
						Message m1 = new Message();
						m1.what = 1;
						m1.obj = "反馈失败！";
						handler.sendMessage(m1);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message m1 = new Message();
					m1.what = 1;
					m1.obj = "反馈失败！";
					handler.sendMessage(m1);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(intputrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				String s = msg.obj.toString();
				repeatlogin_dialog(s);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 账号重复登录
	 * 
	 * @param msg
	 */
	private void repeatlogin_dialog(String text) {
		final Dialog dialog_re = new AlertDialog.Builder(this).create();
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(this).inflate(
				R.layout.repeatlogin_dialog, null));
		Button releat_login_queding = (Button) localWindow
				.findViewById(R.id.releat_login_queding);
		TextView moneynotenoth_title = (TextView) localWindow
				.findViewById(R.id.moneynotenoth_title);
		moneynotenoth_title.setText(text);
		releat_login_queding.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				dialog_re.cancel();
				FeedbackActivity.this.finish();
			}
		});
	}

}
