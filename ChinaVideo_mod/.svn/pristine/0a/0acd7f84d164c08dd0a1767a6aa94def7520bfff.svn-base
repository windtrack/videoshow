package com.zhipu.chinavideo.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.manager.AnchorManager;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.GetRedBeansUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AnchorZhuboFragment extends Fragment {
	private String anchor_id;
	private String room_id;
	private View rootView;
	private CircularImage room_anchor_icon;
	private TextView room_anchor_name;
	private ImageView room_anchor_level;
	private ImageView room_anchor_nextlevel;
	// 主播信息
	private String anchor_name;
	private String anchor_icon;
	private String anchor_received_level;
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private SharedPreferences sharedPreferences;
	private long next_beans;
	private long curr_beans;
	private int lv;
	private int next_lv;
	private long beans;
	private View anchorinfo_loading;
	private ProgressBar anchor_rl_progress;
	private TextView an_tv_two;
	private TextView zhubohongdou_num;
	private String hearts = "1111";

	private ImageView sendhong_btn;
	private static TextView hongdou_hasnum;
	public static GetRedBeansUtil getRedBeansUtil;
	private String redbeans = "0";
	private int timer = 180;
	private static TextView hongdoutimer;
	private static TextView hongdoutimer_before;
	private static TextView hongdoutimer_after;
	public static boolean Threadstate = true;

	public AnchorZhuboFragment() {
		super();
	}

	public static AnchorZhuboFragment getIntance() {
		AnchorZhuboFragment mainTab01 = new AnchorZhuboFragment();
		return mainTab01;
	}

	public void initAnchorManager(String anchor_id, String room_id) {
		this.anchor_id = anchor_id;
		this.room_id = room_id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_zhubo_anchor,
					container, false);
			sharedPreferences = getActivity().getSharedPreferences(APP.MY_SP,
					Context.MODE_PRIVATE);
			initviews();
			setListener();
			getguardandvip();
			get_hearts_and_fans_num();
			AnchorManager.closeGetRedBean();
			getUserRedBeans();
		}
		return rootView;
	}

	/**
	 * 每三分钟获取一次红豆
	 */

	private void getUserRedBeans() {
		Threadstate = true;
		getRedBeansUtil = new GetRedBeansUtil();
		this.getRedBeansUtil.getRedBeansTask(
				new GetRedBeansUtil.BeansRequest() {
					public void getRedBeans() {
						final String user_id = sharedPreferences.getString(
								APP.USER_ID, "");
						final String secret = sharedPreferences.getString(
								APP.SECRET, "");
						GetRedBeansUtil.getRedBeans(user_id, secret,
								new GetRedBeansUtil.GetBeansListener() {
									// 异常处理
									public void failure() {
										handler.sendEmptyMessage(6);
									}

									@Override
									public void hearts(String code,
											String hearts, String time) {
										if (1 == Integer.parseInt(code)) {
											Message msg = Message.obtain();
											timer = Integer.parseInt(time);
											msg.obj = hearts;
											msg.what = 4;
											handler.sendMessage(msg);
										} else if (2 == Integer.parseInt(code)) {
											Message msg = Message.obtain();
											msg.obj = hearts;
											msg.what = 8;
											handler.sendMessage(msg);
										} else if (3 == Integer.parseInt(code)) {
											Message msg = Message.obtain();
											msg.obj = hearts;
											msg.what = 9;
											handler.sendMessage(msg);
										} else if (4 == Integer.parseInt(code)) {
											Message msg = Message.obtain();
											msg.obj = hearts;
											msg.what = 9;
											handler.sendMessage(msg);
										}
									}
								});
					}

					// 异常处理
					public void failure() {
						handler.sendEmptyMessage(6);
					}
				}, 180000, 0, 20);
	}

	private void setListener() {
		rootView.findViewById(R.id.sendhong_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						SendHearts("1", "0");
					}
				});

	}

	/**
	 * 
	 * @author Jon_V 赠送红豆
	 */
	private void SendHearts(final String num, final String from_backpack) {
		Runnable sendheartrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.send_hearts(
							sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""),
							anchor_id, room_id, from_backpack, num);
//					Log.i("lvjian", "送红豆返回结果--------------》" + result);
					JSONObject obj = new JSONObject(result);
					int state = obj.getInt("s");
					if (state == 1) {
						JSONObject data = obj.getJSONObject("data");
						String hearts = data.getString("hearts");
						Message msg = new Message();
						msg.obj = hearts;
						msg.what = 7;
						handler.sendMessage(msg);
					} else {
						String message = "";
						if (obj.has("msg")) {
							message = obj.getString("msg");
						} else {
							message = obj.getString("data");
						}
						Message msg = new Message();
						msg.what = 8;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(sendheartrun);
	}

	private void initviews() {
		room_anchor_icon = (CircularImage) rootView
				.findViewById(R.id.liveroom_anchor_icon);
		room_anchor_name = (TextView) rootView
				.findViewById(R.id.liveroom_anchor_name);
		room_anchor_level = (ImageView) rootView
				.findViewById(R.id.room_anchor_level);
		anchorinfo_loading = rootView.findViewById(R.id.anchorinfo_loading);
		room_anchor_nextlevel = (ImageView) rootView
				.findViewById(R.id.room_anchor_nextlevel);
		anchor_rl_progress = (ProgressBar) rootView
				.findViewById(R.id.anchor_rl_progress);
		an_tv_two = (TextView) rootView.findViewById(R.id.an_tv_two);
		zhubohongdou_num = (TextView) rootView
				.findViewById(R.id.zhubohongdou_num);
		sendhong_btn = (ImageView) rootView.findViewById(R.id.sendhong_btn);
		hongdou_hasnum = (TextView) rootView.findViewById(R.id.hongdou_hasnum);
		hongdoutimer = (TextView) rootView.findViewById(R.id.hongdoutimer);
		hongdoutimer_before = (TextView) rootView
				.findViewById(R.id.hongdoutimer_before);
		hongdoutimer_after = (TextView) rootView
				.findViewById(R.id.hongdoutimer_after);
	}

	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	/**
	 * 获取主播信息
	 */
	private void getguardandvip() {
		anchorinfo_loading.setVisibility(0);
		Runnable getguardandviprun = new Runnable() {
			@Override
			public void run() {
				try {
					String user_id = sharedPreferences.getString(APP.USER_ID,
							"");
					String secret = sharedPreferences.getString(APP.SECRET, "");
					String result = Utils.getvipandguard(user_id, secret,
							anchor_id, room_id);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						JSONObject anchorinfo = data
								.getJSONObject("anchorinfo");
						anchor_icon = anchorinfo.getString("icon");
						anchor_name = anchorinfo.getString("nickname");
						anchor_received_level = anchorinfo
								.getString("received_level");
						JSONObject r = anchorinfo.getJSONObject("r");
						next_beans = r.getLong("next_beans");
						lv = r.getInt("lv");
						curr_beans = r.getLong("curr_beans");
						next_lv = r.getInt("next_lv");
						// beans = Integer.parseInt(r.getString("beans"));
						beans = Long.valueOf(r.getString("beans"));
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getguardandviprun);
	}

	/**
	 * 获取主播红豆数量和粉丝数量
	 */
	private void get_hearts_and_fans_num() {
		Runnable heartsandfansrun = new Runnable() {
			@Override
			public void run() {
				try {
					String roominfo = Utils.getheartsandfansnum(anchor_id);
					JSONObject room_info = new JSONObject(roominfo);
					int state = room_info.getInt("s");
					JSONObject date = room_info.getJSONObject("data");
					if (state == 1) {
						hearts = date.getString("hearts");
						handler.sendEmptyMessage(3);
					} else {
					}
				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(heartsandfansrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				anchorinfo_loading.setVisibility(8);
				room_anchor_name.setText(anchor_name);
				APP.setReceived_level(anchor_received_level, room_anchor_level,
						getActivity());
				APP.setReceived_level(next_lv + "", room_anchor_nextlevel,
						getActivity());
				mOptions = new DisplayImageOptions.Builder()
						.showStubImage(R.drawable.loading_img).cacheInMemory()
						.cacheOnDisc().build();
				mImageLoader = ImageLoader.getInstance();
				mImageLoader.init(ImageLoaderConfiguration
						.createDefault(getActivity()));
				mImageLoader.displayImage(APP.USER_LOGO_ROOT + anchor_icon,
						room_anchor_icon, mOptions);
				anchor_rl_progress
						.setMax((int) ((next_beans - curr_beans) / 10000));
				anchor_rl_progress
						.setProgress((int) ((beans - curr_beans) / 10000));
				an_tv_two.setText((next_beans - beans) + "");

				break;
			case 2:
				anchorinfo_loading.setVisibility(8);
				Utils.showToast(getActivity(), "获取主播信息失败！");
				break;
			case 3:
				zhubohongdou_num.setText(hearts);
				break;
			case 4:
				hongdoutimer_before.setVisibility(0);
				hongdoutimer_after.setVisibility(0);
				hongdou_hasnum.setText((CharSequence) msg.obj);
				sendEmptyMessage(5);
				break;
			case 5:
				if (Threadstate) {

					if (timer < 0) {
						timer = 180;
					}

					hongdoutimer.setText(timer + "s");
					Message localmsg = Message.obtain();
					localmsg.what = 5;
					localmsg.arg1 = timer--;
					if (timer > 0) {
						sendMessageDelayed(localmsg, 1000);
					} else if (timer == 0) {
						getUserRedBeans();
					}
				} else {
					sendEmptyMessage(6);
				}
				break;
			case 6:
				break;
			case 7:
				hongdou_hasnum.setText((CharSequence) msg.obj);
				break;
			case 8:
				boolean result = ((String) msg.obj).matches("[0-9]+");
				if (!result) {
					// hongdou_hasnum.setText("0");
					Utils.showToast(getActivity(), (String) msg.obj);
				} else {
					AnchorZhuboFragment.setRedBeansNum(msg.obj.toString());
					AnchorZhuboFragment.setNotice("您今天的红豆领取已达上限！");
				}
				break;
			case 9:
				AnchorZhuboFragment.setRedBeansNum(msg.obj.toString());
				AnchorZhuboFragment.setNotice("今天服务器红豆领取已结束!");
				break;

			default:
				break;
			}
		};
	};

	public static void setRedBeansNum(String num) {
		if (hongdou_hasnum == null) {
			return;
		} else {
			hongdou_hasnum.setText(num);
		}
	}

	public static void setTimer(String timer) {
		if (hongdoutimer == null) {
			return;
		} else {
			hongdoutimer.setText(timer + "s");
		}
	}

	public static void setNotice(String notice) {
		if (hongdoutimer == null) {
			return;
		} else {
			hongdoutimer_before.setVisibility(8);
			hongdoutimer_after.setVisibility(8);
			hongdoutimer.setText(notice);
		}
	}

	public static void closeGetRedBean() {
		if (AnchorZhuboFragment.getRedBeansUtil != null) {
			AnchorZhuboFragment.getRedBeansUtil.stopCheck();
			Threadstate = false;
		}
	}

}
