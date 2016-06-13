package com.zhipu.chinavideo.util;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

public class GetRedBeansUtil {
	private int checkCount = 0;
	private Timer mTimer;
	private TimerTask mTimerTask;

	public static void getRedBeans(final String uid, final String secret,
			final GetBeansListener getbeansListener) {
		Runnable getredheartsrun = new Runnable() {
			@Override
			public void run() {
				String s = "";
				try {
					JSONObject obj = Utils.GetRedhearts(uid, secret);
					if (obj != null) {
						String code = obj.getString("s");
						if (1 == Integer.parseInt(code)) {
							String hearts = obj.getJSONObject("data")
									.getString("hearts");
							String timer = obj.getJSONObject("data").getString(
									"remine_time");
							getbeansListener.hearts(code, hearts, timer);
						
						} else {

							String hearts = obj.getJSONObject("data")
									.getString("hearts");
							getbeansListener.hearts(code, hearts, null);
							
						}
					} else {
						getbeansListener.failure();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getredheartsrun);
	}
	
	/**
	 * @param paramOrderRequest
	 * @param paramInt1
	 * @param paramInt2
	 * @param paramInt3
	 */
	public void getRedBeansTask(final BeansRequest paramOrderRequest,
			int paramInt1, int paramInt2, final int paramInt3) {
		this.mTimerTask = new TimerTask() {
			public void run() {
				GetRedBeansUtil localPayUtil = GetRedBeansUtil.this;
				localPayUtil.checkCount = (1 + localPayUtil.checkCount);
				paramOrderRequest.getRedBeans();
			}
		};
		this.mTimer = new Timer();
		this.mTimer.schedule(this.mTimerTask, paramInt2, paramInt1);
	}

	/**
	 * ֹͣ停止定时器
	 */
	public void stopCheck() {
		if (this.mTimer != null)
			this.mTimer.cancel();
		if (this.mTimerTask != null)
			this.mTimerTask.cancel();
	}

	public static abstract interface BeansRequest {
		public abstract void getRedBeans();

		public abstract void failure();
	}

	public static abstract interface GetBeansListener {
		public abstract void failure();

		public abstract void hearts(String code, String hearts, String timer);
	}

}
