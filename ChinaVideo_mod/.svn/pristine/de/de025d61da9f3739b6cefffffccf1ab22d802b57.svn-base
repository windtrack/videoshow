package com.zhipu.chinavideo.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.GuardViewerAdapter;
import com.zhipu.chinavideo.adapter.ViewerAdapter;
import com.zhipu.chinavideo.entity.GuardViews;
import com.zhipu.chinavideo.entity.Viewers;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;
/**
 *直播间界面的观众列表的Fragment 
 */
public class AudienceFragment extends Fragment implements OnClickListener {
	private PullToRefreshExpandableListView pullToRefreshExpandableListView;
	private static SharedPreferences sharedPreferences;
	private View rootView;
	private static String room_id;
	private String anchor_id;
	private String user_id;
	private String secret;
	private List<Viewers> viewerList = new ArrayList<Viewers>();
	private List<GuardViews> guard_list = new ArrayList<GuardViews>();
	private List<Viewers> online_helper_list = new ArrayList<Viewers>();
	private List<String> online_helperId_list = new ArrayList<String>();
	private ViewerAdapter adapter;
	private ExpandableListView listview;
	private View audience_loading;
	private int i = 1;
	// 守护
	private GuardViewerAdapter guard_adapter;
	private ExpandableListView guard_listview;
	// 管理
	private ViewerAdapter manager_adapter;
	private ExpandableListView manager_listview;
	private CheckedTextView ctv_guard_list;
	private CheckedTextView ctv_viewer_list;
	private CheckedTextView ctv_manager_list;
	// 守护人数
	private String guard_sun = "1";
	private static Context context;
	private static Dialog dialog;
	// 下拉刷新，重新加载数据
	private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 下拉刷新
			viewerList.clear();
			adapter.notifyDataSetChanged();
			getAudienceList(1);
			i = 1;
		}

		public void onPullUpToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 下拉加载
			getAudienceList(i);
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

	public AudienceFragment() {

	}

	public static AudienceFragment getIntance(String r_id, String anchor_id) {
		AudienceFragment audienceFragment = new AudienceFragment();
		audienceFragment.room_id = r_id;
		audienceFragment.anchor_id = anchor_id;
		return audienceFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_audience,
					container, false);
			context = getActivity();
			sharedPreferences = getActivity().getSharedPreferences(APP.MY_SP,
					Context.MODE_PRIVATE);
			user_id = sharedPreferences.getString(APP.USER_ID, "");
			secret = sharedPreferences.getString(APP.SECRET, "");
			dialog = Utils.showProgressDialog(getActivity(), "", true);
			audience_loading = rootView.findViewById(R.id.audience_loading);
			ctv_guard_list = (CheckedTextView) rootView
					.findViewById(R.id.ctv_guard_list);
			ctv_viewer_list = (CheckedTextView) rootView
					.findViewById(R.id.ctv_viewer_list);
			ctv_manager_list = (CheckedTextView) rootView
					.findViewById(R.id.ctv_manager_list);
			ctv_guard_list.setOnClickListener(this);
			ctv_viewer_list.setOnClickListener(this);
			ctv_manager_list.setOnClickListener(this);
			manager_listview = (ExpandableListView) rootView
					.findViewById(R.id.manager_list);
			guard_listview = (ExpandableListView) rootView
					.findViewById(R.id.guard_list);
			this.pullToRefreshExpandableListView = ((PullToRefreshExpandableListView) this.rootView
					.findViewById(R.id.audience_lsit));
			listview = ((ExpandableListView) this.pullToRefreshExpandableListView
					.getRefreshableView());
			listview.setGroupIndicator(null);
			pullToRefreshExpandableListView
					.setMode(PullToRefreshBase.Mode.BOTH);
			pullToRefreshExpandableListView
					.setDisableScrollingWhileRefreshing(true);
			pullToRefreshExpandableListView
					.setOnRefreshListener(this.onRefreshListener);
			pullToRefreshExpandableListView
					.setOnScrollListener(new PauseOnScrollListener(ImageLoader
							.getInstance(), true, false, this.onScrollListener));
			guard_list = new ArrayList<GuardViews>();
			online_helper_list = new ArrayList<Viewers>();
			viewerList = new ArrayList<Viewers>();
			manager_adapter = new ViewerAdapter(getActivity(),
					online_helper_list);
			manager_listview.setAdapter(manager_adapter);
			adapter = new ViewerAdapter(getActivity(), viewerList);
			listview.setAdapter(adapter);
			getAudienceList(i);
			getGuardlist();
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
	 * 获取观众列表接口数据
	 * 
	 * @author lvjian
	 * 
	 */
	private void getAudienceList(final int i) {
		Runnable getaudiencesrun = new Runnable() {
			@Override
			public void run() {
				try {
					Gson gson = new Gson();
					String s = Utils.get_room_viewer(user_id, secret, room_id,
							i, 10);
					JSONObject obj = new JSONObject(s);
					int state = obj.getInt("s");
					if (state == 1) {
						JSONObject data = obj.getJSONObject("data");
						JSONArray ja = data.getJSONArray("viewers");
						JSONArray helper_ids = data.getJSONArray("helper_ids");
						JSONArray helper_online = data
								.getJSONArray("helpers_info");
						if (helper_online.length() > 0) {
							for (int i = 0; i < helper_ids.length(); i++) {
								String helper_id = helper_ids.getString(i);
								online_helperId_list.add(helper_id);
							}
						}
						for (int i = 0; i < ja.length(); i++) {
							JSONObject el = ja.getJSONObject(i);
							Viewers viewers = gson.fromJson(el.toString(),
									Viewers.class);
							viewerList.add(viewers);
						}
						boolean has = false;
						for (String sid : online_helperId_list) {
							for (Viewers viewer : viewerList) {
								if (viewer.getId().equals(sid)) {
									for (Viewers vv : online_helper_list) {
										if (vv.getId().equals(sid)) {
											has = true;
										}
									}
									if (!has) {
										online_helper_list.add(viewer);
									}
								}
							}
						}
						;
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getaudiencesrun);
	}

	/**
	 * 获取守护列表
	 * 
	 * @author lvjian
	 * 
	 */
	private void getGuardlist() {
		Runnable getguardlistrun = new Runnable() {
			@Override
			public void run() {
				try {
					String s = Utils.getguardlist(anchor_id, room_id);
					JSONObject obj = new JSONObject(s);
					int state = obj.getInt("s");
					if (state == 1) {
						Gson gson = new Gson();
						JSONObject data = obj.getJSONObject("data");
						JSONArray ja = data.getJSONArray("viewers");
						guard_sun = data.getString("sum");
						for (int i = 0; i < ja.length(); i++) {
							JSONObject el = ja.getJSONObject(i);
							GuardViews viewers = gson.fromJson(el.toString(),
									GuardViews.class);
							guard_list.add(viewers);
						}
						handler.sendEmptyMessage(3);
					} else {
						handler.sendEmptyMessage(4);
					}
				} catch (JSONException e) {
					handler.sendEmptyMessage(4);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getguardlistrun);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:

				if (online_helper_list != null) {
					ctv_manager_list.setText("管理" + "("
							+ online_helper_list.size() + ")");
				}
				manager_adapter.notifyDataSetChanged();
				i++;
				pullToRefreshExpandableListView.onRefreshComplete();
				audience_loading.setVisibility(8);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Log.i("lvjian", "---------------获取观众列表失败-----------------");
				break;
			case 3:
				if (guard_list.size() > 0) {
					ctv_guard_list.setText("守护" + "(" + guard_sun + ")");
					guard_adapter = new GuardViewerAdapter(getActivity(),
							guard_list);
					guard_listview.setAdapter(guard_adapter);
				}
				break;
			case 4:
				Log.i("lvjian", "---------------获取守护列表失败-----------------");
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int zise = getResources().getColor(R.color.title_bg);
		int hese = getResources().getColor(R.color.huise_zi);
		switch (v.getId()) {
		case R.id.ctv_guard_list:
			ctv_guard_list.setTextColor(zise);
			ctv_viewer_list.setTextColor(hese);
			ctv_manager_list.setTextColor(hese);
			guard_listview.setVisibility(View.VISIBLE);
			manager_listview.setVisibility(View.GONE);
			pullToRefreshExpandableListView.setVisibility(View.GONE);
			break;
		case R.id.ctv_viewer_list:
			ctv_guard_list.setTextColor(hese);
			ctv_viewer_list.setTextColor(zise);
			ctv_manager_list.setTextColor(hese);
			guard_listview.setVisibility(View.GONE);
			manager_listview.setVisibility(View.GONE);
			pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
			break;
		case R.id.ctv_manager_list:
			ctv_guard_list.setTextColor(hese);
			ctv_viewer_list.setTextColor(hese);
			ctv_manager_list.setTextColor(zise);
			manager_listview.setVisibility(View.VISIBLE);
			guard_listview.setVisibility(View.GONE);
			pullToRefreshExpandableListView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * 禁言或解禁
	 */
	public static void Shut_up(final String shut_id, final String type) {
		dialog.show();
		Runnable shutuprun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.shut_up(
							sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""),
							room_id, shut_id, type);
//					Log.i("lvjian", "result--------------禁言/解禁结果------------->"+ result);
					JSONObject obj = new JSONObject(result);
					int state = obj.getInt("s");
					Message msg = new Message();
					msg.what = 1;
					if (obj.has("data")) {
						String data = obj.getString("data");
						msg.obj = data;
					} else {
						msg.obj = "";
					}
					mhandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mhandler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(shutuprun);
	}

	private static Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				String data = msg.obj.toString();
				if (!Utils.isEmpty(data)) {
					Utils.showToast(context, data);
				}
				break;
			case 2:
				dialog.dismiss();
				Utils.showToast(context, "接口异常！");
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
