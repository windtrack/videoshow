package com.zhipu.chinavideo.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.TypePagerAdapter;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.UIHandler;
import com.zhipu.chinavideo.util.Utils;
import com.zhipu.chinavideo.util.UIHandler.IHandler;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class TypePageFragment extends Fragment implements OnClickListener {
	private View rootView;
	private PullToRefreshListView pullToRefreshListView;
	private View loadingView;
	private ListView listView;
	private TypePagerAdapter typePagerAdapter;
	private View loadingview;
	// 皇冠，钻石，明星，新星标记
	private String mark_name;
	private List<AnchorInfo> anchor_list;
	private View typepager_nowifi;
	private LinearLayout ll_nowifi;
	private TextView yichang_tv;

	public TypePageFragment() {
		
	}

	public static TypePageFragment getIntance(String mark_name) {
		TypePageFragment typePageFragment = new TypePageFragment();
		typePageFragment.mark_name = mark_name;
		return typePageFragment;
	}

	private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			initTypePageData(mark_name, 2);
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

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (this.rootView == null) {
			this.rootView = paramLayoutInflater.inflate(
					R.layout.fragment_typepage, container, false);
			typepager_nowifi = rootView.findViewById(R.id.typepager_nowifi);
			ll_nowifi = (LinearLayout) rootView.findViewById(R.id.ll_nowifi);
			ll_nowifi.setOnClickListener(this);
			yichang_tv = (TextView) rootView.findViewById(R.id.yichang_tv);
			this.pullToRefreshListView = ((PullToRefreshListView) this.rootView
					.findViewById(R.id.typepage_lv_content));
			this.loadingView = this.rootView
					.findViewById(R.id.typepage_loading_layout);
			this.listView = ((ListView) this.pullToRefreshListView
					.getRefreshableView());
			loadingview = rootView.findViewById(R.id.typepage_loading_layout);
			this.pullToRefreshListView
					.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
			this.pullToRefreshListView.setDisableScrollingWhileRefreshing(true);
			this.pullToRefreshListView
					.setOnRefreshListener(this.onRefreshListener);
			this.pullToRefreshListView
					.setOnScrollListener(new PauseOnScrollListener(ImageLoader
							.getInstance(), true, false, this.onScrollListener));
			loadingView.setVisibility(0);
			initTypePageData(mark_name, 1);
		}
		return this.rootView;
	}

	/**
	 * 初始化数据
	 * 
	 * @param paramBoolean
	 * @param i
	 * @param a
	 */
	private void initTypePageData(final String name, int a) {
		if (name == null) {
			return;
		} else {
			if (a == 1) {
				loadingview.setVisibility(0);
			} else {
				loadingview.setVisibility(8);
			}

			Runnable inittypepagedatarun = new Runnable() {
				public void run() {
					String s = Utils.get_anchorlist(name);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(s);
						anchor_list = new ArrayList<AnchorInfo>();
						int i = jsonObject.getInt("s");
						if (i == 1) {
							// 解析主播列表数据
							JSONArray ja = jsonObject.getJSONArray("data");
							for (int j = 0; j < ja.length(); j++) {
								JSONObject ai = ja.getJSONObject(j);
								Gson gson = new Gson();
								AnchorInfo anchorInfo = gson.fromJson(
										ai.toString(), AnchorInfo.class);
								anchor_list.add(anchorInfo);
							}
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
			ThreadPoolWrap.getThreadPool().executeTask(inittypepagedatarun);
		}

	}

	public void onDestroyView() {
		super.onDestroyView();
		handler.destory();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
		
	
		
	}
	

	
	private UIHandler handler = new UIHandler(Looper.getMainLooper(),new IHandler() {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case 1:
					if (anchor_list.size() > 0) {
						pullToRefreshListView.onRefreshComplete();
						typePagerAdapter = new TypePagerAdapter(
								TypePageFragment.this.getParentFragment()
										.getActivity(), anchor_list);
						listView.setAdapter(typePagerAdapter);
					} else {
						typepager_nowifi.setVisibility(0);
						yichang_tv.setText("空空如也...");
					}
					loadingview.setVisibility(8);
					break;
				case 2:
					yichang_tv.setText("接口异常");
					loadingview.setVisibility(8);
					typepager_nowifi.setVisibility(0);
					break;
				default:
					break;
				}
		}
	});

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_nowifi:
			typepager_nowifi.setVisibility(8);
			initTypePageData(mark_name, 1);
			break;

		default:
			break;
		}
	}

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
