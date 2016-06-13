package com.zhipu.chinavideo;

import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ConsumpUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GuardActivity extends Activity implements OnClickListener {
	private TextView title_text;
	private ImageView title_back;
	private TextView buy_shouhu_sure;
	private String room_id;
	private String anchor_id;
	private SharedPreferences sharedPreferences;
	private Dialog dialog;
	private ImageView img_jinshouhu;
	private ImageView img_yinshouhu;
	// 1是银守护，2是金守护
	private String type = "2";
	private TextView shouhu_price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guard);
		Intent localIntent = getIntent();
		room_id = localIntent.getStringExtra("room_id");
		anchor_id = localIntent.getStringExtra("anchor_id");
		Log.i("lvjian", "room_id-0000000000>"+room_id);
		Log.i("lvjian", "anchor_id-0000000000>"+anchor_id);
		sharedPreferences = getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		this.dialog = Utils.showProgressDialog(this, "正在购买", true);
		initview();
	}

	private void initview() {
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("守护");
		title_back = (ImageView) findViewById(R.id.title_back);
		title_back.setOnClickListener(this);
		buy_shouhu_sure = (TextView) findViewById(R.id.buy_shouhu_sure);
		buy_shouhu_sure.setOnClickListener(this);
		img_jinshouhu = (ImageView) findViewById(R.id.img_jinshouhu);
		img_jinshouhu.setOnClickListener(this);
		img_yinshouhu = (ImageView) findViewById(R.id.img_yinshouhu);
		img_yinshouhu.setOnClickListener(this);
		shouhu_price = (TextView) findViewById(R.id.shouhu_price);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.buy_shouhu_sure:
			String beans = sharedPreferences.getString(APP.BEANS, "");
			if (!Utils.isEmpty(beans)) {
				if ("2".equals(type)) {
					
					if(ConsumpUtil.compare(beans, "880000")){
						buyguard();
					} else {
						Utils.Moneynotenough(GuardActivity.this, "余额不足！", "");
					}
				} else {
					if(ConsumpUtil.compare(beans, "280000")){
						buyguard();
					} else {
						Utils.Moneynotenough(GuardActivity.this, "余额不足！", "");
					}
				}
			}

			break;
		// 金守护
		case R.id.img_jinshouhu:
			type = "2";
			img_yinshouhu.setImageResource(R.drawable.yin_shouhu_nomal);
			img_jinshouhu.setImageResource(R.drawable.gold_shouhu_on);
			shouhu_price.setText("88万/月");
			break;
		// 银守护
		case R.id.img_yinshouhu:
			type = "1";
			img_jinshouhu.setImageResource(R.drawable.gold_shouhu_nomal);
			img_yinshouhu.setImageResource(R.drawable.yin_shouhu_on);
			shouhu_price.setText("28万/月");
			break;
		default:
			break;
		}
	}

	private void buyguard() {
		this.dialog.show();
		Runnable buyguardrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.buyguard(
							sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""),
							room_id, anchor_id, "1", type);
					JSONObject obj = new JSONObject(result);
					int state = obj.getInt("s");
					if (state == 1) {
						handler.sendEmptyMessage(1);
					} else {
						Message msg = new Message();
						msg.what = 2;
						msg.obj = obj.getString("data");
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(buyguardrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// 购买成功，更新界面
				Intent intent = new Intent();
				intent.setAction("chinavideo_shouhu");
				sendBroadcast(intent);
				dialog.dismiss();
				finish();
				break;
			case 2:
				String str = msg.obj.toString();
				dialog.dismiss();
				Utils.showToast(GuardActivity.this, str);
				break;
			case 3:
				dialog.dismiss();
				Utils.showToast(GuardActivity.this, "数据异常，请稍后重试");
				break;
			default:
				break;
			}
		};
	};

}
