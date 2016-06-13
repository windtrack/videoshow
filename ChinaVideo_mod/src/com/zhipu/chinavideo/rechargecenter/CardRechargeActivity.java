package com.zhipu.chinavideo.rechargecenter;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.util.APP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CardRechargeActivity extends Activity implements OnClickListener {
	private int[] tvIds = { R.id.card_yidong, R.id.card_liantong,
			R.id.card_dianxin };
	private TextView[] tv = new TextView[this.tvIds.length];
	private int[] fangshiIds = { R.id.card_10, R.id.card_50, R.id.card_100 };
	private TextView[] tv1 = new TextView[this.fangshiIds.length];
	private TextView recharge_name;
	private TextView recharge_beans;
	private SharedPreferences preferences;
	private String user_id;
	private String secret;
	private ImageView title_back;
	private TextView cardrecharge_sure;
    private String cardType="SZX";
    private int chargeMoney=10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cardrecharge);
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		user_id = preferences.getString(APP.USER_ID, "");
		secret = preferences.getString(APP.SECRET, "");
		title_back = (ImageView) findViewById(R.id.title_back);
		cardrecharge_sure=(TextView) findViewById(R.id.cardrecharge_sure);
		title_back.setOnClickListener(this);
		cardrecharge_sure.setOnClickListener(this);
		setinfo();
		for (int j = 0; j < tvIds.length; j++) {
			this.tv[j] = ((TextView) findViewById(this.tvIds[j]));
			tv[j].setOnClickListener(this);
		}
		for (int i = 0; i < fangshiIds.length; i++) {
			this.tv1[i] = ((TextView) findViewById(this.fangshiIds[i]));
			tv1[i].setOnClickListener(this);
		}
	}

	private void setinfo() {
		recharge_name = (TextView) findViewById(R.id.recharge_name);
		recharge_beans = (TextView) findViewById(R.id.recharge_beans);
		String nickname = preferences.getString(APP.NICKNAME, "");
		String money = preferences.getString(APP.BEANS, "");
		recharge_name.setText(nickname);
		recharge_beans.setText(money);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.card_yidong:
			cardType="SZX";
			changeTvBg(R.id.card_yidong);
			break;
		case R.id.card_liantong:
			cardType="UNICOM";
			changeTvBg(R.id.card_liantong);
			break;
		case R.id.card_dianxin:
			cardType="TELECOM";
			changeTvBg(R.id.card_dianxin);
			break;
		case R.id.card_10:
			chargeMoney=10;
			changeFanshiBg(R.id.card_10);
			break;
		case R.id.card_50:
			chargeMoney=50;
			changeFanshiBg(R.id.card_50);
			break;
		case R.id.card_100:
			chargeMoney=100;
			changeFanshiBg(R.id.card_100);
			break;
		case R.id.title_back:
			finish();
			break;
		case R.id.cardrecharge_sure:
			SharedPreferences share = getSharedPreferences("PAY_TYPE", 0);
			share.edit().putInt("phonecard", share.getInt("phonecard", 0)+1).commit();
			Intent intent=new Intent(CardRechargeActivity.this,MobileCardInputActivity.class);
			intent.putExtra("cardType", cardType);
			intent.putExtra("chargeMoney", chargeMoney);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 选中充值类型
	 */
	private void changeTvBg(int type) {
		for (int i = 0; i < tvIds.length; i++) {
			if (type == tvIds[i]) {
				tv[i].setBackgroundResource(R.drawable.bg_recharge_selected);
			} else {
				tv[i].setBackgroundResource(R.drawable.white_cor_bg);
			}
		}
	}

	/**
	 * 选中充值类型
	 */
	private void changeFanshiBg(int type) {
		for (int i = 0; i < fangshiIds.length; i++) {
			if (type == fangshiIds[i]) {
				tv1[i].setBackgroundResource(R.drawable.bg_recharge_selected);
			} else {
				tv1[i].setBackgroundResource(R.drawable.white_cor_bg);
			}
		}
	}
}