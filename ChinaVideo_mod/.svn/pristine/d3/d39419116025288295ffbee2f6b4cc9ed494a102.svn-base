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
import com.zhipu.chinavideo.adapter.RankAdapter;
import com.zhipu.chinavideo.entity.Rank;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.annotation.SuppressLint;
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

@SuppressLint({ "ValidFragment" })
public class RankTypeFragment extends Fragment {
	private String rank_name = "";
	private View rootView;
	private String mark_name;
	private List<Rank> rankdata;
	private List<Rank> rankweek;
	private List<Rank> rankmonth;
	private List<List<Rank>> rankall;
	private List<String> title;
	private RankAdapter rankadapter;
	private PullToRefreshExpandableListView pullToRefreshExpandableListView;
	private ExpandableListView rank_lsit;
	private View loading;
	// 下拉刷新，重新加载数据
	private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 下拉刷新
			getrandata(rank_name);
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

	public RankTypeFragment() {

	}

	public static RankTypeFragment getIntance(String mark_name) {
		RankTypeFragment rankTypeFragment = new RankTypeFragment();
		rankTypeFragment.mark_name = mark_name;
		return rankTypeFragment;
	}

	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_rank_type,
					container, false);
			loading = rootView.findViewById(R.id.rank_typepage_loading_layout);
			this.pullToRefreshExpandableListView = ((PullToRefreshExpandableListView) this.rootView
					.findViewById(R.id.rank_list));
			rank_lsit = ((ExpandableListView) this.pullToRefreshExpandableListView
					.getRefreshableView());
			rank_lsit.setGroupIndicator(null);
			pullToRefreshExpandableListView
					.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
			pullToRefreshExpandableListView
					.setDisableScrollingWhileRefreshing(true);
			pullToRefreshExpandableListView
					.setOnRefreshListener(this.onRefreshListener);
			pullToRefreshExpandableListView
					.setOnScrollListener(new PauseOnScrollListener(ImageLoader
							.getInstance(), true, false, this.onScrollListener));
			if (!Utils.isEmpty(mark_name)) {
				if (mark_name.equals("明星")) {
					rank_name = "rank_star_new";//明星排行榜新接口
				} else if (mark_name.equals("富豪")) {
					rank_name = "rank_rich_new";//富豪排行榜新接口
				} else if (mark_name.equals("点播")) {
					rank_name = "rank_song_new";
				} else if (mark_name.equals("人气")) {
					rank_name = "rank_rhearts_new";
				} 
				getrandata(rank_name);
				loading.setVisibility(0);
			}
		}
		return rootView;
	}

	/**
	 * 获取日排行榜数据
	 * 
	 */
	private void getrandata(final String name) {
		rankdata = new ArrayList<Rank>();
		rankweek = new ArrayList<Rank>();
		rankmonth = new ArrayList<Rank>();
		rankall = new ArrayList<List<Rank>>();
		title = new ArrayList<String>();
		Runnable getrankdatarun = new Runnable() {
			public void run() {
				String s = Utils.getranklist(name, "day");
				try {
					JSONObject obj = new JSONObject(s);
					JSONArray d_data = obj.getJSONArray("data");
					Gson gson = new Gson();
					for (int i = 0; i < d_data.length(); i++) {
						// 只取前五名值
						if (i < 5) {
							rankdata.add(gson.fromJson(d_data.getJSONObject(i)
									.toString(), Rank.class));
						}
					}
					handler.sendEmptyMessage(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getrankdatarun);
	}

	/**
	 * 获取周排行榜数据
	 * 
	 */
	private void getranweek(final String name) {
		Runnable getrankweekrun = new Runnable() {
			public void run() {
				String s = Utils.getranklist(name, "week");
				try {
					JSONObject obj = new JSONObject(s);
					JSONArray w_data = obj.getJSONArray("data");
					Gson gson = new Gson();
					for (int i = 0; i < w_data.length(); i++) {
						// 只取前五名值
						if (i < 5) {
							rankweek.add(gson.fromJson(w_data.getJSONObject(i)
									.toString(), Rank.class));
						}
					}

					handler.sendEmptyMessage(2);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getrankweekrun);
	}

	/**
	 * 获取月排行榜数据
	 * 
	 */
	private void getranmonth(final String name) {
		Runnable getrankmonthrun = new Runnable() {
			public void run() {
				String s = Utils.getranklist(name, "month");
				try {
					JSONObject obj = new JSONObject(s);
					JSONArray m_data = obj.getJSONArray("data");
					Gson gson = new Gson();
					for (int i = 0; i < m_data.length(); i++) {
						// 只取前五名值
						if (i < 5) {
							rankmonth.add(gson.fromJson(m_data.getJSONObject(i)
									.toString(), Rank.class));
						}
					}

					handler.sendEmptyMessage(3);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getrankmonthrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (rankdata.size() > 0) {
					title.add("日");
					rankall.add(rankdata);
				}
				getranweek(rank_name);
				break;
			case 2:
				if (rankweek.size() > 0) {
					title.add("周");
					rankall.add(rankweek);
				}
				getranmonth(rank_name);
				break;
			case 3:
				loading.setVisibility(8);
				if (rankmonth.size() > 0) {
					title.add("月");
					rankall.add(rankmonth);
				}
				rankadapter = new RankAdapter(getActivity(), rankall, title,mark_name);
				pullToRefreshExpandableListView.onRefreshComplete();
				rank_lsit.setAdapter(rankadapter);
				for (int i = 0; i < rankadapter.getGroupCount(); i++) {
					rank_lsit.expandGroup(i);
				}
				;
				// 不能收缩
				rank_lsit.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// TODO Auto-generated method stub
						return true;
					}
				});
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
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
}
