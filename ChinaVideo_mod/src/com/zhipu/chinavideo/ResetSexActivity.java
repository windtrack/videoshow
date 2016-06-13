package com.zhipu.chinavideo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.entity.AnchorInfo;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResetSexActivity extends Activity implements OnClickListener {
	private ImageView back;
	private TextView title_tv;
	private RelativeLayout nan;
	private RelativeLayout nv;
	private ImageView nan_img;
	private ImageView nv_img;
	private SharedPreferences sharedPreferences;
	private String user_id;
	private String secret;
	private String xingbie="男";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetsex);
		sharedPreferences = getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString(APP.USER_ID, "");
		secret = sharedPreferences.getString(APP.SECRET, "");
		back = (ImageView) findViewById(R.id.title_back);
		back.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.title_text);
		title_tv.setText("请选择性别");
		nan = (RelativeLayout) findViewById(R.id.nan);
		nv = (RelativeLayout) findViewById(R.id.nv);
		nan.setOnClickListener(this);
		nv.setOnClickListener(this);
		Intent intent = this.getIntent();
		nan_img = (ImageView) findViewById(R.id.nan_img);
		nv_img = (ImageView) findViewById(R.id.nv_img);
		String select_sex = intent.getStringExtra("select_sex");
		if (select_sex.equals("男")) {
			nv_img.setVisibility(8);
		} else {
			nan_img.setVisibility(8);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.nan:
			xingbie="男";
			StringBuffer sb = new StringBuffer("");
			sb.append("{\"gender\":\"");
			sb.append("0");
			sb.append("\"");
			sb.append("}");
			Log.i("lvjian", "s------------------>" + sb.toString());
			setSex(sb.toString());
			break;
		case R.id.nv:
			xingbie="女";
			StringBuffer sb1 = new StringBuffer("");
			sb1.append("{\"gender\":\"");
			sb1.append("1");
			sb1.append("\"");
			sb1.append("}");
			Log.i("lvjian", "s------------------>" + sb1.toString());
			setSex(sb1.toString());
			break;
		default:
			break;
		}
	}

	private void setSex(final String data) {
		Runnable setsexrun = new Runnable() {
			public void run() {
				String s = Utils.update_personmsg(user_id, secret, data);
				Log.i("lvjian", "data1111111111111111==>" + s);

				try {
					JSONObject jsonObject = new JSONObject(s);
					int a = jsonObject.getInt("s");
					if (a == 1) {
						handler.sendEmptyMessage(1);

					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(setsexrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Intent in = new Intent();
				in.putExtra("sex", xingbie);
				setResult(20, in);
				Editor editor = sharedPreferences.edit();
				if(xingbie.equals("男")){
					editor.putString(APP.GENDER, "0");
				}else if(xingbie.equals("女")){
					editor.putString(APP.GENDER, "1");
				}else{
					if(xingbie.equals("男")){
						editor.putString(APP.GENDER, "2");
					}
				}
				editor.commit();
				finish();
				break;
			case 2:
				Utils.showToast(ResetSexActivity.this, "修改性别失败或异常");
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
