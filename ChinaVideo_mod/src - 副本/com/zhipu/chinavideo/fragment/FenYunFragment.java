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
import com.zhipu.chinavideo.adapter.FengYunAdapter;
import com.zhipu.chinavideo.entity.Rank;
import com.zhipu.chinavideo.entity.StarWeek;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

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

public class FenYunFragment extends Fragment {
	private View rootView;
	private String mark_name;
	private PullToRefreshExpandableListView pullToRefreshExpandableListView;
	private ExpandableListView fengyun_list;
	private FengYunAdapter adapter;
	private List<StarWeek> father;
	private List<List<Rank>> sons;
	private String result = "";
	private View fengyun_loading_layout;
	// 下拉刷新，重新加载数据
	private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 下拉刷新
			getfengyun("m_cur_month_star_rank_new");
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

	public FenYunFragment() {

	}

	public static FenYunFragment getIntance(String mark_name) {
		FenYunFragment fenYunFragment = new FenYunFragment();
		fenYunFragment.mark_name = mark_name;
		return fenYunFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_fengyun,
					container, false);
			fengyun_loading_layout = rootView
					.findViewById(R.id.fengyun_loading_layout);
			this.pullToRefreshExpandableListView = ((PullToRefreshExpandableListView) this.rootView
					.findViewById(R.id.fengyun_list));
			fengyun_list = ((ExpandableListView) this.pullToRefreshExpandableListView
					.getRefreshableView());
			fengyun_list.setGroupIndicator(null);
			pullToRefreshExpandableListView
					.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
			pullToRefreshExpandableListView
					.setDisableScrollingWhileRefreshing(true);
			pullToRefreshExpandableListView
					.setOnRefreshListener(this.onRefreshListener);
			pullToRefreshExpandableListView
					.setOnScrollListener(new PauseOnScrollListener(ImageLoader
							.getInstance(), true, false, this.onScrollListener));
			getfengyun("m_cur_month_star_rank_new");
			fengyun_loading_layout.setVisibility(0);
		}
		return rootView;
	}

	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	/**
	 * 获取风云榜数据
	 */

	private void getfengyun(final String name) {
		father = new ArrayList<StarWeek>();
		sons = new ArrayList<List<Rank>>();
		// TODO Auto-generated method stub
		Runnable getfengyunrun = new Runnable() {
			public void run() {
				// 如果是周星
				if ("周星".equals(mark_name)) {
					result = Utils.getranklist("rank_weekstar_new", "current");
				} else {
					result = Utils.getbillboard(name);
				}
				try {
					JSONObject obj = new JSONObject(result);
					JSONArray images = obj.getJSONArray("data");
					for (int i = 0; i < images.length(); i++) {
						Gson gson = new Gson();
						JSONObject jo = (JSONObject) images.get(i);
						JSONArray ja = jo.getJSONArray("users");
						List<Rank> ranks = new ArrayList<Rank>();
						Gson gs = new Gson();
						for (int j = 0; j < ja.length(); j++) {
							JSONObject jb = (JSONObject) ja.get(j);
							Rank rank = new Rank();
							rank.setUser_id(jb.getString("id"));
							rank.setIcon(jb.getString("icon"));
							rank.setNickname(jb.getString("nickname"));
							rank.setFans_num(jb.getString("fans_num"));
							rank.setPoster_url2(jb.getString("poster_url2"));
							rank.setReceived_level(jb
									.getString("received_level"));
							rank.setGiftnum(jb.getString("giftnum"));
							ranks.add(rank);
						}
						// 当songs不为0时，才加入显示（避免空指针错误）
						if (ranks.size() > 0) {
							sons.add(ranks);
							father.add(gson.fromJson(jo.toString(),
									StarWeek.class));
						}
					}
					handler.sendEmptyMessage(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getfengyunrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				fengyun_loading_layout.setVisibility(8);
				pullToRefreshExpandableListView.onRefreshComplete();
				adapter = new FengYunAdapter(getActivity(), sons, father,
						mark_name);
				fengyun_list.setAdapter(adapter);
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					fengyun_list.expandGroup(i);
				}
				;
				// 不能收缩
				fengyun_list
						.setOnGroupClickListener(new OnGroupClickListener() {

							@Override
							public boolean onGroupClick(
									ExpandableListView parent, View v,
									int groupPosition, long id) {
								// TODO Auto-generated method stub
								return true;
							}
						});
				break;
			case 2:
				fengyun_loading_layout.setVisibility(8);
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
