package com.zhipu.chinavideo.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.MyAnchorAdapter;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyAnchorFragment extends Fragment implements OnClickListener {
	public static String state ="1";
	private View rootView;
	private ListView listview;
	private MyAnchorAdapter adapter;
	private List<AnchorInfo> list;
	private List<AnchorInfo> tmplist;
	private int sign;//加载的标志
	private SharedPreferences sharedPreferences;
	private String user_id;
	private String secret;
	private View myanchor_loading;
	private View myanchor_nowifi;
	private LinearLayout ll_nowifi;
	private TextView yichang_tv;

	public MyAnchorFragment() {
		super();
	}

	public static MyAnchorFragment getIntance(int paramInt) {
		MyAnchorFragment subscribeListFragment = new MyAnchorFragment();
		subscribeListFragment.sign = paramInt;
		return subscribeListFragment;
	}

	@Override
	public void onResume() {
		if (state.equals("2")) {
			initData();
		}
		super.onResume();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_myanchorlist,
					container, false);
			sharedPreferences = getActivity().getSharedPreferences(APP.MY_SP,
					Context.MODE_PRIVATE);
			user_id = sharedPreferences.getString(APP.USER_ID, "");
			secret = sharedPreferences.getString(APP.SECRET, "");
			this.listview = (ListView) this.rootView
					.findViewById(R.id.subcribe_list);
			myanchor_loading = rootView.findViewById(R.id.myanchor_loading);
			myanchor_nowifi = rootView.findViewById(R.id.myanchor_nowifi);
			ll_nowifi = (LinearLayout) rootView.findViewById(R.id.ll_nowifi);
			yichang_tv = (TextView) rootView.findViewById(R.id.yichang_tv);
			yichang_tv.setText("空空如也");
			ll_nowifi.setOnClickListener(this);
			initData();
			
			
		}
		return rootView;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				state ="1";
				myanchor_loading.setVisibility(8);
				if (list.size() == 0) {
					myanchor_nowifi.setVisibility(0);
				}
				if (adapter != null) {
					adapter = null;
				} 
				adapter = new MyAnchorAdapter(getActivity(), list,sign);
				listview.setAdapter(adapter);
				break;
			case 2:
				myanchor_loading.setVisibility(8);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 获取我的守护列表
	 * 
	 * @param user_id
	 * @param crumb
	 */
	private void initData() {
		myanchor_loading.setVisibility(0);
		list = new ArrayList<AnchorInfo>();
		Runnable getmyguardrun = new Runnable() {
			String s;
			public void run() {
				if (sign == 0) {
					s = Utils.getmyguard(user_id, secret);
				} else if (sign == 1) {
					s = Utils.getsubscribelist(user_id, secret);
				} else {
					s = Utils.getdingyuelist(user_id, secret);
				}
				try {
					JSONObject jsonObject = new JSONObject(s);
					Log.i("sumao", "s:"+s);
					int a = jsonObject.getInt("s");
					if (a == 1) {
						JSONArray ja = jsonObject.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							Gson gson = new Gson();
							AnchorInfo an = gson.fromJson(ja.getJSONObject(i)
									.toString(), AnchorInfo.class);
							Log.i("sumao", "an:"+an.toString());
							if (!list.contains(an)) {
								list.add(an);
							}
						}
						Log.i("sumao", "list"+list.size());
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
		ThreadPoolWrap.getThreadPool().executeTask(getmyguardrun);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_nowifi:
			initData();
			break;

		default:
			break;
		}
	}

}
