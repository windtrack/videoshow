package com.zhipu.chinavideo.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.GuardActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.MyProgress;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AnchorShouhuFragment extends Fragment implements OnClickListener {
	private SharedPreferences sharedPreferences;
	private RelativeLayout layout1;
	private LinearLayout layout2;
	private View loadinglayout;
	private View rootView;
	private TextView kaitong_guard;
	private String nick_name;
	private String user_icon;
	private String guard_expire;
	private String exp;
	private String max_exp;
	private String guard_lv;
	private CircularImage anchor_shouhu_icon;
	private TextView anchor_guard_name;
	private ImageView anchor_shouhu_level;
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private TextView guard_endtime;
	private String room_id;
	private String anchor_id;
	private TextView shouhu_xufei;
	private String type = "1";
	private ImageView anchor_shouhu_level_left;
	private ImageView anchor_shouhu_level_right;
	private MyProgress anchor_shouhu_level_progress;

	public AnchorShouhuFragment() {
		super();
	}

	public static AnchorShouhuFragment getIntance() {
		AnchorShouhuFragment mainTab02 = new AnchorShouhuFragment();
		return mainTab02;
	}

	public void initAnchorShouhuFragment(String room_id, String anchor_id) {
		this.room_id = room_id;
		this.anchor_id = anchor_id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (this.rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_shouhu_anchor,
					container, false);
			loadinglayout = rootView.findViewById(R.id.shouhu_loading);
			sharedPreferences = getActivity().getSharedPreferences(APP.MY_SP,
					Context.MODE_PRIVATE);
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("chinavideo_shouhu"); // 为BroadcastReceiver指定action，即要监听的消息名字。
			getActivity().registerReceiver(dynamicReceiver, intentFilter); // 注册监听
			getguardandvip();
		}
		return rootView;
	}

	private void initview(int a) {
		// TODO Auto-generated method stub
		layout1 = (RelativeLayout) rootView
				.findViewById(R.id.not_shouhu_layout);
		layout2 = (LinearLayout) rootView.findViewById(R.id.is_shouhu_layout);
		guard_endtime = (TextView) rootView.findViewById(R.id.guard_endtime);
		kaitong_guard = (TextView) rootView.findViewById(R.id.kaitong_guard);
		shouhu_xufei = (TextView) rootView.findViewById(R.id.shouhu_xufei);
		anchor_shouhu_level_left = (ImageView) rootView
				.findViewById(R.id.anchor_shouhu_level_left);
		anchor_shouhu_level_right = (ImageView) rootView
				.findViewById(R.id.anchor_shouhu_level_right);
		kaitong_guard.setOnClickListener(this);
		shouhu_xufei.setOnClickListener(this);
		anchor_shouhu_icon = (CircularImage) rootView
				.findViewById(R.id.anchor_shouhu_icon);
		anchor_guard_name = (TextView) rootView
				.findViewById(R.id.anchor_guard_name);
		anchor_shouhu_level = (ImageView) rootView
				.findViewById(R.id.anchor_shouhu_level);
		anchor_shouhu_level_progress = (MyProgress) rootView
				.findViewById(R.id.anchor_shouhu_level_progress);
		// 是守护
		if (a == 1) {
			layout1.setVisibility(8);
			layout2.setVisibility(0);
			// 银守护
			if ("1".equals(type)) {
				anchor_shouhu_level.setImageResource(APP
						.parseSilverGuardId(guard_lv));
				anchor_shouhu_level_left.setImageResource(APP
						.parseSilverGuardId(guard_lv));
				anchor_shouhu_level_right.setImageResource(APP
						.parseSilverGuardId((Integer.parseInt(guard_lv) + 1)
								+ ""));
			} else if ("2".equals(type)) {
				anchor_shouhu_level.setImageResource(APP
						.parseGoldGuardId(guard_lv));
				anchor_shouhu_level_left.setImageResource(APP
						.parseGoldGuardId(guard_lv));
				anchor_shouhu_level_right
						.setImageResource(APP.parseGoldGuardId((Integer
								.parseInt(guard_lv) + 1) + ""));
			} else {

			}
			mOptions = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.loading_img).cacheInMemory()
					.cacheOnDisc().build();
			mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(ImageLoaderConfiguration
					.createDefault(getActivity()));
			mImageLoader.displayImage(APP.USER_BIG_LOGO_ROOT + user_icon,
					anchor_shouhu_icon, mOptions);
			anchor_guard_name.setText(nick_name);
			guard_endtime.setText("有效期至:" + guard_expire);
			anchor_shouhu_level_progress.setMax(Integer.parseInt(max_exp));
			anchor_shouhu_level_progress.setProgress(Integer.parseInt(exp));

			// 不是守护
		} else {
			layout1.setVisibility(0);
			layout2.setVisibility(8);
		}

	}

	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.kaitong_guard:
			Intent in = new Intent(getActivity(), GuardActivity.class);
			in.putExtra("anchor_id", anchor_id);
			in.putExtra("room_id", room_id);
			getActivity().startActivity(in);
			break;
		case R.id.shouhu_xufei:
			Intent xufeiintent = new Intent(getActivity(), GuardActivity.class);
			xufeiintent.putExtra("anchor_id", anchor_id);
			xufeiintent.putExtra("room_id", room_id);
			getActivity().startActivity(xufeiintent);
			break;
		default:
			break;
		}
	}

	private void getguardandvip() {
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
						// 判断是否是守护
						if (data.has("guard")) {
							JSONObject userinfo = data
									.getJSONObject("userinfo");
							nick_name = userinfo.getString("nickname");
							user_icon = userinfo.getString("icon");
							JSONObject guard = data.getJSONObject("guard");
							guard_expire = guard.getString("expire");
							exp = guard.getString("exp");
							max_exp = guard.getString("max_exp");
							guard_lv = guard.getString("lv");
							type = guard.getString("type");
							handler.sendEmptyMessage(1);
						} else {
							handler.sendEmptyMessage(2);
						}
					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getguardandviprun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:

				initview(1);
				loadinglayout.setVisibility(8);
				break;
			case 2:
				initview(2);
				loadinglayout.setVisibility(8);
				break;
			case 3:
				Utils.showToast(getActivity(), "请求失败，请重试");
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(dynamicReceiver);

	}

	private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("chinavideo_shouhu")) {
				loadinglayout.setVisibility(0);
				getguardandvip();
			}
		}
	};

}
