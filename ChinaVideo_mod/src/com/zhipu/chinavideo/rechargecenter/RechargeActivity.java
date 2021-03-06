package com.zhipu.chinavideo.rechargecenter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.ffcs.inapppaylib.PayHelper;
import com.ffcs.inapppaylib.bean.Constants;
import com.ffcs.inapppaylib.bean.response.BaseResponse;
import com.ffcs.inapppaylib.bean.response.PayResponse;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.alipayutils.Keys;
import com.zhipu.chinavideo.alipayutils.PayResult;
import com.zhipu.chinavideo.alipayutils.Rsa;
import com.zhipu.chinavideo.entity.PayTypeVo;
import com.zhipu.chinavideo.rechargecenter.CardRechargeActivity;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.OperatorUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;
import com.zhipu.chinavideo.wxapi.MD5;
import com.zhipu.chinavideo.wxapi.WXPayEntryActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RechargeActivity extends Activity implements OnClickListener {
	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;
	private ImageView back;
	private int[] tvIds = { R.id.rc_5, R.id.rc_20, R.id.rc_100, R.id.rc_other };
	private TextView[] tv = new TextView[this.tvIds.length];
	private int[] fangshiIds = { R.id.rc_duanxin5, R.id.rc_duanxin15,
			R.id.rc_zhifubao, R.id.rc_yinlian, R.id.rc_shoujichongzhika,
			R.id.rc_wxchongzhi };
	private LinearLayout[] tv1 = new LinearLayout[this.fangshiIds.length];
	// 确认按钮
	private TextView recharge_sure;
	// 默认充值多少
	private int price = 5;
	// 充值类型
	private int type = 3;
	private String order;
	private SharedPreferences preferences;
	private String user_id;
	private String secret;
	// 订单流水号
	private String tn = "";
	private TextView recharge_name;
	private TextView recharge_beans;
	// 输入金额
	private String input_num = "";
	private TextView other_sum;
	private View view;
	// 电信短信充值
	private PayHelper payHelper;
	private Dialog dialog;
	private Dialog dialog2;
	private Dialog duanxinchongzhi_dialog;
	private String wxorder = "";
	private static final String APP_ID = "wxe3b6571489e8a96d";
	private static final String PARTNER_ID = "1233302101";
	public static final String API_KEY = "73a25016e0c15a6a0f09ca977f7cd8db";
	StringBuffer sb;
	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private boolean jineistrue = true;
	// 房间信息
	private String room_id = "";
	private Timer timer;

	private SharedPreferences share;// 充值选择支付次数排行   alipay:支付宝 wechat：微信
									// phonecard：手机充值卡 phone：手机
									// 分别对应的支付方式type 支付宝：3 微信：6 手机充值卡：5 手机：4

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		Intent intent = this.getIntent();
		if (Utils.isEmpty(intent.getStringExtra("room_id"))) {
			room_id = "";
		} else {
			room_id = intent.getStringExtra("room_id");
		}
		req = new PayReq();
		sb = new StringBuffer();
		msgApi.registerApp(APP_ID);
		IntentFilter filter = new IntentFilter(WXPayEntryActivity.action);
		registerReceiver(broadcastReceiver, filter);
		initPay189();
		this.dialog = Utils.showProgressDialog(this, "获取订单中", true);
		this.dialog2 = Utils.showProgressDialog(this, "正在更新乐币", true);
		this.duanxinchongzhi_dialog = Utils.showProgressDialog(this,
				"充值中，请耐心等待！", true);
		back = (ImageView) findViewById(R.id.title_back);
		back.setOnClickListener(this);
		other_sum = (TextView) findViewById(R.id.rc_other);
		for (int j = 0; j < tvIds.length; j++) {
			this.tv[j] = ((TextView) findViewById(this.tvIds[j]));
			tv[j].setOnClickListener(this);
		}
		for (int i = 0; i < fangshiIds.length; i++) {
			this.tv1[i] = ((LinearLayout) findViewById(this.fangshiIds[i]));
			tv1[i].setOnClickListener(this);
		}
		recharge_sure = (TextView) findViewById(R.id.recharge_sure);
		recharge_sure.setOnClickListener(this);
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		user_id = preferences.getString(APP.USER_ID, "");
		secret = preferences.getString(APP.SECRET, "");
		setinfo();
		initPayType();
		
		share = getSharedPreferences("PAY_TYPE", 0);
		getCommonPayType();//获取用户经常使用的充值方式
	}

	// 从Share中取出用户最常用的支付方式，然后界面上默认
	private void getCommonPayType() {
		List<PayTypeVo> list = new ArrayList<PayTypeVo>();
		list.add(new PayTypeVo(share.getInt("alipay", 0), "alipay", 3));
		list.add(new PayTypeVo(share.getInt("wechat", 0), "wechat", 6));
		list.add(new PayTypeVo(share.getInt("phonecard", 0), "phonecard", 5));
		list.add(new PayTypeVo(share.getInt("phone", 0), "phone", 4));
		Collections.sort(list);// 将List进行排序
		PayTypeVo first = list.get(0);
		if (first.getNum() > 0) {
			type = first.getType();
			switch (first.getType()) {
			case 3:// 设置支付宝回回显示
				changeFanshiBg(R.id.rc_zhifubao);
				break;
			case 4:// 设置手机支付默认选中
				changeFanshiBg(R.id.rc_yinlian);
				break;
			case 5:// 设置手机充值卡默认选中
				changeFanshiBg(R.id.rc_shoujichongzhika);
				break;
			case 6:// 设置微信支付默认选中
				changeFanshiBg(R.id.rc_wxchongzhi);
				break;
			default:
				break;
			}

		}

	}

	// 根据手机是否是电信手机来判断是否显示天翼短信充值
	private void initPayType() {
		LinearLayout payTY5 = (LinearLayout) findViewById(R.id.rc_duanxin5);
		LinearLayout payTy15 = (LinearLayout) findViewById(R.id.rc_duanxin15);
		if (OperatorUtil.isChinaTelecom(RechargeActivity.this)) {// 如果手机是电信手机（显示天翼的5元短信和15元短信）
			payTy15.setVisibility(View.VISIBLE);
			payTY5.setVisibility(View.VISIBLE);
		} else {// 如果手机不是电信手机
			payTy15.setVisibility(View.GONE);
			payTY5.setVisibility(View.GONE);
		}
	}

	private void intoyoumengrecharge() {
		// 友盟统计
		String channel = "";
		if (type == 1) {
			channel = "duanxin5";
		} else if (type == 2) {
			channel = "duanxin10";
		} else if (type == 3) {
			channel = "zhifubao";
		} else if (type == 4) {
			channel = "yinlian";
		} else if (type == 6) {
			channel = "weixin";
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("channel", channel);
		map.put("price", price + "");
		// map.put("user_id", user_id);
		MobclickAgent.onEvent(this, "recharge", map);
		MobclickAgent.onEventValue(this, "money", map, price);
	}

	private void setinfo() {
		recharge_name = (TextView) findViewById(R.id.recharge_name);
		recharge_beans = (TextView) findViewById(R.id.recharge_beans);
		String nickname = preferences.getString(APP.NICKNAME, "");
		String money = preferences.getString(APP.BEANS, "");
		recharge_name.setText(nickname);
		recharge_beans.setText(money + " 乐币");
	}
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.rc_5:
			changeTvBg(R.id.rc_5);
			price = 5;
			jineistrue = true;
			break;
		case R.id.rc_20:
			price = 20;
			jineistrue = true;
			changeTvBg(R.id.rc_20);
			break;
		case R.id.rc_100:
			price = 100;
			jineistrue = true;
			changeTvBg(R.id.rc_100);
			break;
		case R.id.rc_other:
			if (!Utils.isEmpty(other_sum.getText().toString())
					&& !"其它金额".equals(other_sum.getText().toString().trim())) {
				price = Integer.parseInt(other_sum.getText().toString());
			} else {
				jineistrue = false;
			}
			changeTvBg(R.id.rc_other);
			final AlertDialog localAlertDialog = new AlertDialog.Builder(this)
					.create();
			localAlertDialog.show();
			Window localWindow = localAlertDialog.getWindow();
			localWindow.setGravity(Gravity.BOTTOM);
			view = LayoutInflater.from(this).inflate(
					R.layout.recharge_edittext, null);
			localWindow.setContentView(view);
			// 设置大小
			android.view.WindowManager.LayoutParams lp = localWindow
					.getAttributes();
			lp.width = LayoutParams.FILL_PARENT;
			lp.height = LayoutParams.WRAP_CONTENT;
			localWindow.setAttributes(lp);
			localWindow.setWindowAnimations(R.style.mystyle);
			localAlertDialog.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			// //弹出软键盘
			// Utils.ShowKeyboard(view);
			this.getWindow().setGravity(Gravity.BOTTOM);
			final EditText input = (EditText) localWindow
					.findViewById(R.id.edit_shuru);
			TextView edit_input_sure = (TextView) localWindow
					.findViewById(R.id.edit_input_sure);
			// 确定输入
			edit_input_sure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// 如果输入数字为空
					if (Utils.isEmpty(input.getText().toString())) {
						Utils.showToast(RechargeActivity.this, "请输入充值金额");
					} else if (input.getText().toString().length() > 5) {
						Utils.showToast(RechargeActivity.this, "金额过大");
					} else if (Integer.parseInt(input.getText().toString()) < 1) {
						Utils.showToast(RechargeActivity.this, "您输入的金额不合理");
					} else {
						input_num = input.getText().toString();
						localAlertDialog.dismiss();
						handler.sendEmptyMessage(7);
					}
				}
			});
			break;
		case R.id.rc_duanxin5:
			type = 1;
			changeFanshiBg(R.id.rc_duanxin5);
			payHelper.pay(this, "90000055", hand, "1");
			break;
		case R.id.rc_duanxin15:
			type = 2;
			changeFanshiBg(R.id.rc_duanxin15);
			payHelper.pay(this, "90000361", hand, "1");
			break;
		case R.id.rc_zhifubao:
			type = 3;
			changeFanshiBg(R.id.rc_zhifubao);
			break;
		case R.id.rc_yinlian:
			type = 4;
			changeFanshiBg(R.id.rc_yinlian);
			break;
		case R.id.rc_shoujichongzhika:
			type = 5;
			changeFanshiBg(R.id.rc_shoujichongzhika);
			Intent intent = new Intent(RechargeActivity.this,
					CardRechargeActivity.class);
			startActivity(intent);
			break;
		case R.id.rc_wxchongzhi:
			type = 6;
			changeFanshiBg(R.id.rc_wxchongzhi);
			break;
		// 确认
		case R.id.recharge_sure:
			if (jineistrue) {
				if (type == 1) {
				} else if (type == 2) {
				} else if (type == 3) {
					share.edit().putInt("alipay", share.getInt("alipay", 0)+1).commit();
					GetZhiFuBaoOrder();
				} else if (type == 4) {
					share.edit().putInt("phone", share.getInt("phone", 0)+1).commit();
					getyinlianordernumber();
				} else if (type == 5) {
				} else if (type == 6) {
					share.edit().putInt("wechat", share.getInt("wechat", 0)+1).commit();
					getwxorder();
				}
			} else {
				Utils.showToast(RechargeActivity.this, "请输入金额！");
			}
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

	/**
	 * 支付宝充值
	 */
	private void GetZhiFuBaoOrder() {
		dialog.show();
		Runnable getzhifubaoordertrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.getzhifubaoorder(user_id, price + "",
							room_id);

					JSONObject obj = new JSONObject(result);
					int state = obj.getInt("s");
					if (state == 1) {
						order = obj.getString("data");
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getzhifubaoordertrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// laquzhifubao();
				dialog.dismiss();
				// 订单
				String orderInfo = getOrderInfo("商品", "乐币充值", price + "", order);
				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();
				Runnable payRunnable = new Runnable() {

					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(RechargeActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);
						Message msg = new Message();
						msg.what = 1;
						msg.obj = result;
						zhifubaohand.sendMessage(msg);
					}
				};
				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
				break;
			case 2:
				dialog.dismiss();
				Utils.showToast(RechargeActivity.this, "获取订单号失败");
				break;
			case 3:
				Log.i("lvjian", "tn-------------------->" + tn);
				dialog.dismiss();
				String spId = null;
				String sysProvider = null;
				UPPayAssistEx.startPayByJAR(RechargeActivity.this,
						PayActivity.class, spId, sysProvider, tn, "00");
				break;
			case 4:
				dialog.dismiss();
				Utils.showToast(RechargeActivity.this, "获取订单号失败,请重新尝试");
				break;
			// 更新乐币成功
			case 5:
				dialog2.dismiss();
				recharge_beans.setText(preferences.getString(APP.BEANS, "")
						+ " 乐币");
				Log.i("lvjian", "-----------更新乐币成功----------");
				// 充值任务完成，触发充值任务事件
				getupdatetaskreward(preferences.getString(APP.USER_ID, ""),
						preferences.getString(APP.SECRET, ""), "8");
				intoyoumengrecharge();
				break;
			// 更新乐币失败
			case 6:
				dialog2.dismiss();
				Log.i("lvjian", "-----------更新乐币失败----------");
				break;
			// 输入金额
			case 7:
				jineistrue = true;
				price = Integer.parseInt(input_num);
				other_sum.setText(price + "");
				break;
			// 获取微信订单成功
			case 8:
				dialog.dismiss();
				sendPayReq();
				break;
			// 获取微信订单失败
			case 9:
				dialog.dismiss();
				Utils.showToast(RechargeActivity.this, "获取微信订单失败！");
				break;
			// 30秒后调用短信充值订单确认接口，完成给用户加币
			case 10:
				timer.cancel();
				String num = (String) msg.obj;
				Log.i("lvjian", "num------------->" + num);
				smsokupdata(preferences.getString(APP.USER_ID, ""),
						preferences.getString(APP.SECRET, ""), num);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price,
			String order) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + order + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\""
				+ "http://www.0058.com/api/pay/malipay/notify_url.php" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */

	public String sign(String content) {
		return Rsa.sign(content, Keys.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// /**
	// * 拉取支付宝
	// */
	// private void laquzhifubao() {
	// String info = getNewOrderInfo();
	// String sign = Rsa.sign(info, Keys.PRIVATE);
	// sign = URLEncoder.encode(sign);
	// info += "&sign=\"" + sign + "\"&" + getSignType();
	// final String orderInfo = info;
	// new Thread() {
	// public void run() {
	// AliPay alipay = new AliPay(RechargeActivity.this, mHandler);
	// // 设置为沙箱模式，不设置默认为线上环境
	// // alipay.setSandBox(true);
	// String result = alipay.pay(orderInfo);
	// Message msg = new Message();
	// msg.what = RQF_PAY;
	// msg.obj = result;
	// mHandler.sendMessage(msg);
	// }
	// }.start();
	//
	// }
	//
	// private String getSignType() {
	// return "sign_type=\"RSA\"";
	// }
	//
	// private String getNewOrderInfo() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("partner=\"");
	// sb.append(Keys.DEFAULT_PARTNER);
	// sb.append("\"&out_trade_no=\"");
	// sb.append(order);
	// sb.append("\"&subject=\"");
	// sb.append("商品");
	// sb.append("\"&body=\"");
	// sb.append("人民币兑换乐币套餐");
	// sb.append("\"&total_fee=\"");
	// sb.append(price);
	// sb.append("\"&notify_url=\"");
	// // 网址需要做URL编码
	// sb.append(URLEncoder
	// .encode("http://www.0058.com/api/pay/malipay/notify_url.php"));
	// sb.append("\"&service=\"mobile.securitypay.pay");
	// sb.append("\"&_input_charset=\"UTF-8");
	// sb.append("\"&return_url=\"");
	// sb.append(URLEncoder.encode("http://m.alipay.com"));
	// sb.append("\"&payment_type=\"1");
	// sb.append("\"&seller_id=\"");
	// sb.append(Keys.DEFAULT_SELLER);
	// // 如果show_url值为空，可不传
	// // sb.append("\"&show_url=\"");
	// sb.append("\"&it_b_pay=\"1m");
	// sb.append("\"");
	// return new String(sb);
	// }

	// /**
	// * 支付宝支付结果
	// */
	// Handler mHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// Result result = new Result((String) msg.obj);
	// switch (msg.what) {
	// case RQF_PAY:
	// case RQF_LOGIN:
	// Toast.makeText(RechargeActivity.this, result.getResult(),
	// Toast.LENGTH_SHORT).show();
	// // 支付宝支付结果，更新乐币
	// UpdateBeans();
	// break;
	// default:
	// break;
	// }
	// };
	// };

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {

			BaseResponse resp = null;
			switch (msg.what) {
			case Constants.RESULT_VALIDATE_FAILURE:
				Log.i("lvjian", "-------------合法性验证-------------");
				// 合法性验证失败
				resp = (BaseResponse) msg.obj;
				// Toast.makeText(RechargeActivity.this,
				// resp.getRes_code() + ":" + resp.getRes_message(),
				// Toast.LENGTH_SHORT).show();
				Log.i("lvjian",
						"返回码：" + resp.getRes_code() + ":"
								+ resp.getRes_message());
				break;
			case Constants.RESULT_PAY_SUCCESS:
				Log.i("lvjian", "-------------支付成功-------------");
				// 支付成功
				resp = (BaseResponse) msg.obj;
				// Toast.makeText(RechargeActivity.this,
				// resp.getRes_code() + ":" + resp.getRes_message(),
				// Toast.LENGTH_SHORT).show();
				// Log.i("lvjian",
				// "返回码：" + resp.getRes_code() + ":"
				// + resp.getRes_message());
				PayResponse payResp = (PayResponse) msg.obj;
				// Log.i("lvjian", "------payResp.getOrder_no()------------->"
				// + payResp.getOrder_no());
				// Log.i("lvjian",
				// "------app.user_id------------->"
				// + preferences.getString(APP.USER_ID, ""));
				// Log.i("lvjian",
				// "------app.secret------------->"
				// + preferences.getString(APP.SECRET, ""));
				duanxinchongzhi_dialog.show();
				timer(payResp.getOrder_no());
				break;

			case Constants.RESULT_PAY_FAILURE:
				// Log.i("lvjian", "-------------支付失败-------------");
				// 支付失败
				resp = (BaseResponse) msg.obj;
				Toast.makeText(
						RechargeActivity.this,
						resp.getRes_code() + ":" + resp.getRes_message()
								+ "，请重新尝试！", Toast.LENGTH_SHORT).show();
				break;
			case 21:
				duanxinchongzhi_dialog.dismiss();
				Toast.makeText(RechargeActivity.this, "充值成功！", 100).show();
				UpdateBeans();
				break;
			case 22:
				duanxinchongzhi_dialog.dismiss();
				Toast.makeText(RechargeActivity.this, "充值失败,请联系客服！", 100)
						.show();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 用户用短信充值付钱成功后，调用服务器后台验证给他加乐币
	 * 
	 */

	private void smsokupdata(final String uid, final String secret,
			final String ordernumber) {
		Runnable updataduanxin = new Runnable() {
			public void run() {
				try {
					String result = Utils.updateuser_lebi(uid, secret,
							ordernumber);
					// Log.i("lvjian", "order----duanxin--->" + result);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						hand.sendEmptyMessage(21);
					} else {
						// 充值失败
						hand.sendEmptyMessage(22);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// 充值异常
					hand.sendEmptyMessage(22);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(updataduanxin);
	}

	// 获取订单号
	private void getyinlianordernumber() {
		dialog.show();
		Runnable getordernumberrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.getunpayorder(user_id, secret,
							user_id, "", room_id, price);
					// Log.i("lvjian", "order------->" + result);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						tn = obj.getString("data");
						handler.sendEmptyMessage(3);
					} else {
						// 获取订单号失败
						handler.sendEmptyMessage(4);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(4);
					Log.i("lvjian",
							"-------------------获取银联订单号接口异常--------------------");
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getordernumberrun);
	}

	/**
	 * 银联返回数据
	 */
	// 接受银联返回数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			Utils.showToast(RechargeActivity.this, "支付成功！");
			// Log.i("lvjian", "-------------支付成功------------");
			// 银联支付结果成功，更新乐币
			UpdateBeans();
		} else if (str.equalsIgnoreCase("fail")) {
			Utils.showToast(RechargeActivity.this, "支付失败！");
		} else if (str.equalsIgnoreCase("cancel")) {
			Utils.showToast(RechargeActivity.this, "你已取消了本次订单的支付！");
		}
	}
	Runnable updatebeansrun;

	/**
	 * 更新乐币
	 */
	private void UpdateBeans() {
		dialog2.show();
		updatebeansrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.getuserinfo(user_id, secret);
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

						handler.sendEmptyMessage(5);
					} else {
						handler.sendEmptyMessage(6);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(6);
				}
				if (updatebeansrun != null) {
					ThreadPoolWrap.getThreadPool().removeTask(updatebeansrun);
				}

			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(updatebeansrun);
	}

	/**
	 * 初始化短信支付
	 */
	public void initPay189() {
		payHelper = PayHelper.getInstance(this);
		payHelper
				.init("212173170000036141", "9b55dbb589236d134e46d46b648c1575");
		payHelper.settimeout(8000); // 设置超时时间（可不设，默认为8s）
	}

	private void getwxorder() {
		dialog.show();
		Runnable getwx_orderrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.getwx_order(user_id, secret, user_id,
							"", room_id, price + "");
					// Log.i("lvjian", "order----wx--->" + result);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						wxorder = data.getString("prepayid");
						handler.sendEmptyMessage(8);
					} else {
						handler.sendEmptyMessage(9);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(9);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getwx_orderrun);
	}

	private static enum LocalRetCode {
		ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
	}

	private void sendPayReq() {
		nonceStr = genNonceStr();
		timeStamp = genTimeStamp();
		req.appId = APP_ID;
		req.partnerId = PARTNER_ID;
		req.prepayId = wxorder;
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genAppSign(signParams);
		// Log.i("lvjian", "------------>" + genAppSign(signParams));
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		msgApi.sendReq(req);
	}

	private long timeStamp;
	private String nonceStr;

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context arg0, Intent intent) {
			String type = intent.getExtras().getString("type");
			if ("0".equals(type)) {
				UpdateBeans();
				Utils.showToast(RechargeActivity.this, "支付成功！");
			} else if ("-2".equals(type)) {
				Utils.showToast(RechargeActivity.this, "用户取消支付！");
			} else {
				Utils.showToast(RechargeActivity.this, "支付失败！");
			}
		};
	};
	/**
	 * 支付宝结果
	 */
	private Handler zhifubaohand = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 1:

				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(RechargeActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					UpdateBeans();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(RechargeActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(RechargeActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
	
	/**
	 * 充值成功后,改变任务中状态更新
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

	public void timer(final String number) {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				System.out.println("----------------设定要指定任务------------------");
				Message msg = new Message();
				msg.what = 10;
				msg.obj = number;
				handler.sendMessage(msg);
			}
		}, 30000);// 设定指定的时间time,此处为3000毫秒
	}
}
