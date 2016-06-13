package com.zhipu.chinavideo.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ExpandableListView.OnGroupClickListener;

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

public class RankListManager implements View.OnClickListener {
	private boolean isshowing = false;
	private Context context;
	private ViewStub viewstub_ranklist;
	private static RankListManager instance;
	private String anchor_id;
	private String room_id;
	private View ranklistview;
	private SharedPreferences sharedPreferences;
	private PullToRefreshExpandableListView pullToRefreshExpandableListView;
	private ExpandableListView fans_lsit;
	private List<String> father;
	private List<List<Fans>> fans;
	private FansAdapter adapter;
	private String user_id;
	private String secret;
	private View room_ranklist_top;
	// private View loading_view;
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

	public static RankListManager getInstance() {
		if (instance == null)
			instance = new RankListManager();
		return instance;
	}

	public void exit() {
		instance = null;
	}

	public void initRankListManager(Context context,
			ViewStub viewstub_ranklist, String anchor_id, String room_id) {
		this.context = context;
		this.viewstub_ranklist = viewstub_ranklist;
		this.anchor_id = anchor_id;
		this.room_id = room_id;
		sharedPreferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString(APP.USER_ID, "");
		secret = sharedPreferences.getString(APP.SECRET, "");

	}

	public void showranklistView() {
		isshowing = true;
		if (ranklistview == null) {
			this.viewstub_ranklist.setLayoutResource(R.layout.rank_list_layout);
			this.ranklistview = this.viewstub_ranklist.inflate();
			initViews();
		} else {
			this.ranklistview.setVisibility(0);
		}
	}

	public void goneranklistview() {
		isshowing = false;
		if ((this.ranklistview != null)
				&& (this.ranklistview.getVisibility() == 0)) {
			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.input_exit);
			ranklistview.startAnimation(animation);
			this.ranklistview.setVisibility(8);
		}
	}

	private void initViews() {
		this.pullToRefreshExpandableListView = ((PullToRefreshExpandableListView) this.ranklistview
				.findViewById(R.id.pull_fans_lsit));
		fans_lsit = ((ExpandableListView) this.pullToRefreshExpandableListView
				.getRefreshableView());
		fans_lsit.setGroupIndicator(null);
		// father.add("本场粉丝榜");
		// father.add("本周粉丝榜");
		pullToRefreshExpandableListView
				.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
		pullToRefreshExpandableListView
				.setDisableScrollingWhileRefreshing(true);
		pullToRefreshExpandableListView
				.setOnRefreshListener(this.onRefreshListener);
		pullToRefreshExpandableListView
				.setOnScrollListener(new PauseOnScrollListener(ImageLoader
						.getInstance(), true, false, this.onScrollListener));
		room_ranklist_top = ranklistview.findViewById(R.id.room_ranklist_top);
		room_ranklist_top.setOnClickListener(this);
		// loading_view = ranklistview.findViewById(R.id.pull_fans_lsit_f);
		getLiveFans();

	}

	public boolean getshowing() {
		return isshowing;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.room_ranklist_top:
			goneranklistview();
			break;

		default:
			break;
		}
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
				Log.i("lvjian",
						"-----------------获取本场粉丝失败或异常---------------------");
				break;
			case 3:
				pullToRefreshExpandableListView.onRefreshComplete();
				// loading_view.setVisibility(8);
				if (father.size() > 0 && fans.size() > 0) {
					adapter = new FansAdapter(father, context, fans);
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
				Log.i("lvjian",
						"-----------------获取本周粉丝失败---------------------");
				break;
			default:
				break;
			}
		};
	};

}
