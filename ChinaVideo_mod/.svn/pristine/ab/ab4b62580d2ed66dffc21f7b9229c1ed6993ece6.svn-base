package com.zhipu.chinavideo.rechargecenter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Tasks;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.HttpUtils;
import com.zhipu.chinavideo.util.MyJSONRPCHttpClient;
import com.zhipu.chinavideo.util.PayUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MobileCardInputActivity extends Activity implements
		OnClickListener {
	private ImageView iv_back;
	private TextView title;
	private TextView card_type;
	private TextView card_money;
	private String cardNum;
	private String cardPwd;
	private String cardType;
	private int chargeMoney;
	private EditText etCard;
	private EditText etPwd;
	private Dialog mDialog;
	private String ordernumber = "";
	private PayUtil payUtil;
	private ProgressDialog progressDialog;
	private SharedPreferences preferences;
	private String user_id;
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 向php服务器提交订单失败
			case 1:
				if (mDialog.isShowing()){
					mDialog.dismiss();
				}
				Log.i("lvjian", "---------------提交订单成功---------------");
				break;
			case 2:
				if (mDialog.isShowing()){
					mDialog.dismiss();
				}
				Log.i("lvjian", "---------------提交订单失败---------------");
				break;
			case 5:
				mDialog.dismiss();
				Log.i("lvjian", "---------------获取订单失败---------------");
				break;
			case 6:
				Log.i("lvjian", "---------------获取订单成功---------------");
				break;
			case 7:
				Utils.showToast(MobileCardInputActivity.this, "充值成功！");
				Log.i("lvjian", "---------------更新乐币成功---------------");
				break;
			case 8:
				Log.i("lvjian", "---------------更新乐币失败---------------");
				break;
				//检查订单成功，更新乐币
			case 9:
				UpdateBeans();
				break;
				//检查订单失败
			case 10:
				Utils.showToast(MobileCardInputActivity.this, "充值失败,请检查卡号密码重新尝试");
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_input_mobile_card_layout);
		// 返回
		iv_back = (ImageView) findViewById(R.id.title_back);
		iv_back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title_text);
		title.setText("手机充值卡");
		card_type = ((TextView) findViewById(R.id.card_type));
		card_money = ((TextView) findViewById(R.id.card_money));
		this.etCard = ((EditText) findViewById(R.id.et_card_num));
		this.etPwd = ((EditText) findViewById(R.id.et_card_pwd));
		Intent localIntent = getIntent();
		cardType = localIntent.getStringExtra("cardType");
		chargeMoney = localIntent.getIntExtra("chargeMoney", 0);
		// 初始化通过上级接收过来的数据
		if (cardType.equals("SZX")) {
			card_type.setText("中国移动充值卡");
		} else if (cardType.equals("UNICOM")) {
			card_type.setText("中国联通充值卡");
		} else if (cardType.equals("TELECOM")) {
			card_type.setText("中国电信充值卡");
		}
		card_money.setText(chargeMoney + "元");
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		user_id = preferences.getString(APP.USER_ID, "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回上个界面
		case R.id.title_back:
			this.finish();
			break;
		case R.id.bt_ok:
			this.cardNum = this.etCard.getText().toString().trim();
			this.cardPwd = this.etPwd.getText().toString().trim();
			if (this.cardNum.length() == 0) {
				Utils.showToast(this, "请输入充值卡号");
			} else if (this.cardPwd.length() == 0) {
				Utils.showToast(this, "请输入充值卡密码");
			} else {
				mobileCard();
			}
			break;

		}
	}

	private void mobileCard() {
		this.mDialog = Utils.showProgressDialog(this, "正在充值", true);
		this.mDialog.show();

		Runnable carrechargerun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.CardRecharge(user_id, chargeMoney
							+ "");
					JSONObject obj = new JSONObject(result);
					Log.i("lvjian", "result--s----cardorder--->>" + result);
					int a = obj.getInt("s");
					if (a == 1) {
						ordernumber = obj.getString("data");
						// 调用服务器php，向远程服务器提交订单
						doRechargeTask(ordernumber);
						handler.sendEmptyMessage(6);
					} else {
						handler.sendEmptyMessage(5);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(5);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(carrechargerun);
	}

	public void doRechargeTask(String orderid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("order_no", orderid);
		params.put("cur_uid", user_id);
		params.put("op_uid", user_id);
		params.put("pay_channel_coding", cardType);
		params.put("card_no", etCard.getText().toString().trim());
		params.put("card_pwd", etPwd.getText().toString().trim());
		params.put("card_amt", chargeMoney + "");
		params.put("total_fee", chargeMoney + "");
		String result = HttpUtils.submitPostData(params, "utf-8");
		Log.i("lvjian", "result==========================>" + result);
		try {
			JSONObject dataJson = new JSONObject(result);
			int state = dataJson.getInt("s");
			// 提交订单成功
			if (state == 1) {
				handler.sendEmptyMessage(1);
				checkOrder();
			} else {
				handler.sendEmptyMessage(2);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(2);
		}
	}

	private void checkOrder() {
		Runnable checkorderrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.CheckOrder(user_id, ordernumber);
					Log.i("lvjian", "-------------checkorder----------->"
							+ result);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						String status = data.getString("status");
						if("1".equals(status)){
							handler.sendEmptyMessage(9);
						}else{
							handler.sendEmptyMessage(10);
						}
					} else {
						handler.sendEmptyMessage(10);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(10);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(checkorderrun);
	}

	protected void onDestroy() {
		if (this.mDialog != null)
			this.mDialog.dismiss();

		super.onDestroy();
	}

	/**
	 * 更新乐币
	 */
	private void UpdateBeans() {
		Runnable updatebeansrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.getuserinfo(user_id,
							preferences.getString(APP.SECRET, ""));
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						String beans = data.getString("beans");
						String rlevel = data.getString("rlevel");
						String clevel = data.getString("clevel");
						String icon = data.getString("icon");
						String nickname = data.getString("nickname");
						String openkey = data.getString("openkey");
						String time = data.getString("timestamp");
						String cost_beans = data.getString("cost_beans");
						String received_beans = data
								.getString("received_beans");
						int viplv = data.getInt("vip_lv");
						int is_stealth = data.getInt("is_stealth");
						Editor editor = preferences.edit();
						editor.putString(APP.BEANS, beans);
						editor.putString(APP.USER_ICON, APP.USER_LOGO_ROOT
								+ icon);
						editor.putString(APP.USER_RLEVEL, rlevel);
						editor.putString(APP.USER_CLEVEL, clevel);
						editor.putString(APP.NICKNAME, nickname);
						editor.putString(APP.OPENKEY, openkey);
						editor.putString(APP.TIMESTAMP, time);
						editor.putString(APP.COST_BEANS, cost_beans);
						editor.putString(APP.RECEIVED_BEANS, received_beans);
						editor.putInt(APP.VIPLV, viplv);
						editor.putInt(APP.ISSTEALTH, is_stealth);
						editor.commit();
						handler.sendEmptyMessage(7);
					} else {
						handler.sendEmptyMessage(8);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(8);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(updatebeansrun);
	}
}
