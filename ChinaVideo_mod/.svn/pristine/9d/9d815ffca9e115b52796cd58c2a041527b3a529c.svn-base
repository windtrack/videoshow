package com.zhipu.chinavideo.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.AnchorInformationActivity;
import com.zhipu.chinavideo.MyPrerogativeActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AnchorGuizuFragment extends Fragment implements OnClickListener {
	private SharedPreferences sharedPreferences;
	private View rootView;
	private String is_vip = "0";
	private String user_icon;
	private String nick_name;
	private String vip_lv="1";
	private String endtime;
	private CircularImage anchor_vip_icon;
	private TextView anchor_vip_name;
	private ImageView anchor_vip_level;
	private TextView vip_endtime;
	private String cur_rcash;
	private String next_cash;
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private String room_id;
	private String anchor_id;
	private View guizu_loading;
	private RelativeLayout not_guizu_layout;
	private LinearLayout is_vip_layout;
	private TextView shouhu_xufei;
	private TextView kaitong_guizu;

	public AnchorGuizuFragment() {
		super();
	}

	public static AnchorGuizuFragment getIntance() {
		AnchorGuizuFragment mainTab03 = new AnchorGuizuFragment();
		return mainTab03;
	}

	public void initAnchorGuizuFragment(String room_id, String anchor_id) {
		this.room_id = room_id;
		this.anchor_id = anchor_id;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (this.rootView == null) {
			sharedPreferences = getActivity().getSharedPreferences(APP.MY_SP,
					Context.MODE_PRIVATE);
			this.rootView = inflater.inflate(R.layout.fragment_anchorguizu,
					container, false);
			guizu_loading = rootView.findViewById(R.id.guizu_loading);
			guizu_loading.setVisibility(0);
			getguardandvip();

		}
		return rootView;
	}

	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	private void initView(int i) {
		not_guizu_layout = (RelativeLayout) rootView
				.findViewById(R.id.not_guizu_layout);
		is_vip_layout = (LinearLayout) rootView
				.findViewById(R.id.is_vip_layout);
		if (i == 1) {
			not_guizu_layout.setVisibility(8);
			is_vip_layout.setVisibility(0);
		} else {
			not_guizu_layout.setVisibility(0);
			is_vip_layout.setVisibility(8);
		}
		shouhu_xufei = (TextView) rootView.findViewById(R.id.shouhu_xufei);
		kaitong_guizu = (TextView) rootView.findViewById(R.id.kaitong_guizu);
		kaitong_guizu.setOnClickListener(this);
		shouhu_xufei.setOnClickListener(this);
		anchor_vip_icon = (CircularImage) rootView
				.findViewById(R.id.anchor_vip_icon);
		anchor_vip_name = (TextView) rootView
				.findViewById(R.id.anchor_vip_name);
		anchor_vip_level = (ImageView) rootView
				.findViewById(R.id.anchor_vip_level);
		vip_endtime = (TextView) rootView.findViewById(R.id.vip_endtime);
		anchor_vip_name.setText(nick_name);
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader
				.init(ImageLoaderConfiguration.createDefault(getActivity()));
		mImageLoader.displayImage(APP.USER_BIG_LOGO_ROOT + user_icon,
				anchor_vip_icon, mOptions);
		anchor_vip_level.setImageResource(APP.parseVIPLevel(Integer
				.parseInt(vip_lv)));
		vip_endtime.setText("有效期至:" + endtime);
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
						// 判断是否是VIP
						if (data.has("vip")) {
							JSONObject userinfo = data
									.getJSONObject("userinfo");
							nick_name = userinfo.getString("nickname");
							user_icon = userinfo.getString("icon");
							JSONObject vip = data.getJSONObject("vip");
							endtime = vip.getString("expire");
							JSONObject info = vip.getJSONObject("info");
							cur_rcash = info.getString("cur_rcash");
							next_cash = info.getString("next_cash");
							vip_lv = info.getString("lv");
							handler.sendEmptyMessage(1);
						} else {
							handler.sendEmptyMessage(2);
						}
						// 判断是否是vip
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
				initView(1);
				guizu_loading.setVisibility(8);
				break;
			case 2:
				initView(2);
				guizu_loading.setVisibility(8);
				break;
			case 3:
				guizu_loading.setVisibility(8);
				Utils.showToast(getActivity(), "获取数据失败");
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shouhu_xufei:
			Intent intentxufei = new Intent(getActivity(),
					MyPrerogativeActivity.class);
			getActivity().startActivity(intentxufei);
			break;
		case R.id.kaitong_guizu:
			Intent intentgoumai = new Intent(getActivity(),
					MyPrerogativeActivity.class);
			getActivity().startActivity(intentgoumai);
			break;
		default:
			break;
		}
	}
}
