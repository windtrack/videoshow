package com.zhipu.chinavideo.util;


import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class PayUtil {
	private int checkCount = 0;
	private Timer mTimer;
	private TimerTask mTimerTask;
	public static void checkOrder(final String uid, final String ordernumber,
			final CheckOrderListener paramCheckOrderListener) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					String uri = APP.PATH_NEIWANG;
					MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
							new DefaultHttpClient(), uri);
					client.setConnectionTimeout(10000);
					client.setSoTimeout(10000);
					JSONObject result = client.callJSONObject("m_q_order_no",
							uid, ordernumber);
//					Log.i("lvjian", "------检查订单-------->"+result);
					int state = result.getInt("s");
					if (state == 1) {
						// 此处先不考虑换密码，用户名
						JSONObject data = result.getJSONObject("data");
						if (data.getInt("status") == 1) {
							paramCheckOrderListener.success();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					paramCheckOrderListener.failure();
				}
				return null;
			}

		}.execute();

	}

	/**
	 * 查询电信短信订单
	 * 
	 * @param uid
	 * @param secret
	 * @param ordernumber
	 * @param paramCheckOrderListener
	 */
	public static void SMSCheckOrder(final String uid, final String secret,
			final String ordernumber,
			final CheckOrderListener paramCheckOrderListener) {
		Log.i("RechargeActivity", "SMSRecharge");
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					String uri = APP.PATH_NEIWANG;
					MyJSONRPCHttpClient client = new MyJSONRPCHttpClient(
							new DefaultHttpClient(), uri);
					client.setConnectionTimeout(10000);
					client.setSoTimeout(10000);
					JSONObject result = client.callJSONObject("sms_recharge",
							uid, secret, ordernumber);
					int state = result.getInt("s");
					if (state == 1) {
						// 此处先不考虑换密码，用户名
						paramCheckOrderListener.success();
					} else {
						// 如果返回结果不成功，要接着请求
						paramCheckOrderListener.failure();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					paramCheckOrderListener.failure();
				}

				return null;
			}

		}.execute();

	}

	/**
	 * 定时检查订单的状态
	 * 
	 * @param paramOrderRequest
	 * @param paramInt1
	 * @param paramInt2
	 * @param paramInt3
	 */
	public void SMScheckOrderTask(final OrderRequest paramOrderRequest,
			int paramInt1, int paramInt2, final int paramInt3) {
		this.mTimerTask = new TimerTask() {
			public void run() {
				PayUtil localPayUtil = PayUtil.this;
				localPayUtil.checkCount = (1 + localPayUtil.checkCount);
				if (PayUtil.this.checkCount < paramInt3) {
					paramOrderRequest.SMSCheckOrder();
				} else {
					stopCheck();
					paramOrderRequest.failure();
				}
			}
		};
		this.mTimer = new Timer();
		this.mTimer.schedule(this.mTimerTask, paramInt2, paramInt1);
	}

	/**
	 * 定时检查订单的状态
	 * 
	 * @param paramOrderRequest
	 * @param paramInt1
	 * @param paramInt2
	 * @param paramInt3
	 */
	public void checkOrderTask(final OrderRequest paramOrderRequest,
			int paramInt1, int paramInt2, final int paramInt3) {
		this.mTimerTask = new TimerTask() {
			public void run() {
				PayUtil localPayUtil = PayUtil.this;
				localPayUtil.checkCount = (1 + localPayUtil.checkCount);
				if (PayUtil.this.checkCount < paramInt3) {
					paramOrderRequest.checkOrder();
				} else {
					stopCheck();
					paramOrderRequest.failure();
				}
			}
		};
		this.mTimer = new Timer();
		this.mTimer.schedule(this.mTimerTask, paramInt2, paramInt1);
	}

	/**
	 * 停止定时器
	 */
	public void stopCheck() {
		if (this.mTimer != null)
			this.mTimer.cancel();
		if (this.mTimerTask != null)
			this.mTimerTask.cancel();
		this.mTimer = null;
		this.mTimerTask = null;
	}

	public static abstract interface OrderRequest {
		public abstract void checkOrder();

		public abstract void SMSCheckOrder();

		public abstract void failure();
	}

	public static abstract interface CheckOrderListener {
		public abstract void failure();

		public abstract void success();
	}

}
