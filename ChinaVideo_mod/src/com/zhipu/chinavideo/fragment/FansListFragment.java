package com.zhipu.chinavideo.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.FansAdapter;
import com.zhipu.chinavideo.entity.Fans;
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
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class FansListFragment extends Fragment {
	private SharedPreferences sharedPreferences;
	private String user_id;
	private String secret;
	private View rootView;
	private static String room_id;
	private static String anchor_id;
	private PullToRefreshExpandableListView pullToRefreshExpandableListView;
	private ExpandableListView fans_lsit;
	private List<String> father;
	private List<List<Fans>> fans;
	private View loading_view;
	private FansAdapter adapter;
	// 下拉刷新，重新加载数据
	private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 下拉刷新
			getLiveFans();

		}

		public void onPullUpToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
		}
	};
	private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
		public void onScroll(AbsListView paramAnonymousAbsListView,
				int paramAnonymousInt1, int paramAnonymousInt2,
				int paramAnonymousInt3) {
		}

		public void onScrollStateChanged(AbsListView paramAnonymousAbsListView,
				int paramAnonymousInt) {
		}
	};

	public FansListFragment() {

	}

	public static FansListFragment getIntance(String r_id, String an_id) {
		FansListFragment fansListFragment = new FansListFragment();
		room_id = r_id;
		anchor_id = an_id;
		return fansListFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_fanslist,
					container, false);
			sharedPreferences = getActivity().getSharedPreferences(APP.MY_SP,
					Context.MODE_PRIVATE);
			user_id = sharedPreferences.getString(APP.USER_ID, "");
			secret = sharedPreferences.getString(APP.SECRET, "");
			this.pullToRefreshExpandableListView = ((PullToRefreshExpandableListView) this.rootView
					.findViewById(R.id.fans_lsit));
			fans_lsit = ((ExpandableListView) this.pullToRefreshExpandableListView
					.getRefreshableView());
			fans_lsit.setGroupIndicator(null);
			// father.add("本场粉丝榜");
			// father.add("本周粉丝榜");
			loading_view = rootView.findViewById(R.id.fans_loading);
			pullToRefreshExpandableListView
					.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
			pullToRefreshExpandableListView
					.setDisableScrollingWhileRefreshing(true);
			pullToRefreshExpandableListView
					.setOnRefreshListener(this.onRefreshListener);
			pullToRefreshExpandableListView
					.setOnScrollListener(new PauseOnScrollListener(ImageLoader
							.getInstance(), true, false, this.onScrollListener));
			getLiveFans();
		}
		return rootView;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	/**
	 * 获取本场直播的粉丝
	 * 
	 * @author lvjian
	 * 
	 */
	private void getLiveFans() {
		fans = new ArrayList<List<Fans>>();
		father = new ArrayList<String>();
		Runnable getlivefansrun = new Runnable() {
			@Override
			public void run() {
				try {
					Gson gson = new Gson();
					String s = Utils.get_live_fans(anchor_id);
					JSONObject obj = new JSONObject(s);
					int state = obj.getInt("s");
					if (state == 1) {
						JSONArray data = obj.getJSONArray("data");
						List<Fans> f = new ArrayList<Fans>();
						for (int i = 0; i < data.length(); i++) {
							JSONObject j = (JSONObject) data.get(i);
							Fans fan = gson.fromJson(j.toString(), Fans.class);
							f.add(fan);
						}
						if (f.size() > 0) {
							fans.add(f);
							father.add("本场粉丝榜");
						}

						handler.sendEmptyMessage(1);
					} else if (state == 0) {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getlivefansrun);

	}

	/**
	 * 获取粉丝周榜
	 */
	private void getFans() {
		Runnable getfansrun = new Runnable() {
			@Override
			public void run() {
				try {
					Gson gson = new Gson();
					String s = Utils.getFans(user_id, secret, room_id, "week");
					JSONObject obj = new JSONObject(s);
					int state = obj.getInt("s");
					if (state == 1) {
						List<Fans> fs = new ArrayList<Fans>();
						JSONArray data = obj.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							JSONObject j = (JSONObject) data.get(i);
							Fans fan = new Fans();
							fan.setCost_level(j.getString("cost_level"));
							fan.setIcon(j.getString("icon"));
							fan.setId(j.getString("user_id"));
							fan.setNickname(j.getString("nickname"));
							fan.setNum(j.getString("num"));
							fs.add(fan);

						}
						if (fs.size() > 0) {
							fans.add(fs);
							father.add("本周粉丝榜");
						}

						handler.sendEmptyMessage(3);
					} else if (state == 0) {
						handler.sendEmptyMessage(4);
					}
				} catch (JSONException e) {
					handler.sendEmptyMessage(4);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getfansrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				getFans();
				break;
			case 2:
				Log.i("lvjian","-----------------获取本场粉丝失败或异常---------------------");
				break;
			case 3:
				pullToRefreshExpandableListView.onRefreshComplete();
				loading_view.setVisibility(8);
				if (father.size() > 0 && fans.size() > 0) {
					adapter = new FansAdapter(father, getActivity(), fans);
					fans_lsit.setAdapter(adapter);
					for (int i = 0; i < adapter.getGroupCount(); i++) {

						fans_lsit.expandGroup(i);

					}
					;
					// 不能收缩
					fans_lsit
							.setOnGroupClickListener(new OnGroupClickListener() {
								@Override
								public boolean onGroupClick(
										ExpandableListView parent, View v,
										int groupPosition, long id) {
									// TODO Auto-generated method stub
									return true;
								}
							});
				}

				break;
			case 4:
				Log.i("lvjian","-----------------获取本周粉丝失败---------------------");
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
