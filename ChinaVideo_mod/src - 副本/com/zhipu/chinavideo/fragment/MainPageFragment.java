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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.AdvertiseAdapter;
import com.zhipu.chinavideo.adapter.MainPagerAdapter;
import com.zhipu.chinavideo.adapter.TypePagerAdapter;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.db.HandlerCmd;
import com.zhipu.chinavideo.entity.Advertise;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.entity.Tags;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.rpc.RpcRoutine;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.UIHandler;
import com.zhipu.chinavideo.util.Utils;
import com.zhipu.chinavideo.util.UIHandler.IHandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

@SuppressLint("ValidFragment")
public class MainPageFragment extends Fragment implements OnClickListener {
	private View rootView;
	private View loadingView;
	private View headView;
	private PullToRefreshListView pullToRefreshListView;
	private ListView expandableListView;
	private List<Advertise> advertise_list;
	private ViewPager viewpager;
	private AdvertiseAdapter advertiseAdapter;
	// private List<Tags> father;
	// private List<List<AnchorInfo>> anchor_list_son;
	private TypePagerAdapter maipager_list_adapter;
	private List<Tags> tags = new ArrayList<Tags>();
	private ViewPager viewPager;
	private LinearLayout dot;
	private List<ImageView> dotList;
	// ViewPager-item视图集合的保存
	private ArrayList<View> views;
	// ViewPager-item临时视图
	private View view1;
	private ImageView[] imageViews;
	private ImageView imageView;
	private ViewGroup group;
	private View mainpager_nowifi;
	private LinearLayout ll_nowifi;
	private int current = 0;
	private Context context;
	private List<AnchorInfo> anchor_list;
	
	public MainPageFragment() {

	}

	public static MainPageFragment getInstance(List<Tags> tags,
			ViewPager viewPager) {
		MainPageFragment mainPageFragment = new MainPageFragment();
		mainPageFragment.tags = tags;
		mainPageFragment.viewPager = viewPager;
		return mainPageFragment;
	}

	// 下拉刷新，重新加载数据
	private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			initMainPageData(2);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (this.rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_mainpage,
					container, false);
			context = getActivity();
			mainpager_nowifi = rootView.findViewById(R.id.mainpager_nowifi);
			ll_nowifi = (LinearLayout) rootView.findViewById(R.id.ll_nowifi);
			ll_nowifi.setOnClickListener(this);
			this.pullToRefreshListView = ((PullToRefreshListView) this.rootView
					.findViewById(R.id.lv_content));
			this.loadingView = this.rootView.findViewById(R.id.loading_layout);
			this.expandableListView = ((ListView) this.pullToRefreshListView
					.getRefreshableView());
			this.headView = inflater.inflate(R.layout.livehall_ad_viewpager,
					null);
			this.viewpager = (ViewPager) this.headView
					.findViewById(R.id.ad_pager);
			group = (ViewGroup) this.headView.findViewById(R.id.dot);
			RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) this.viewpager
					.getLayoutParams();
			localLayoutParams.height = ((int) (5.0F * (((WindowManager) getActivity()
					.getSystemService("window")).getDefaultDisplay().getWidth() / 18.0F)));
			this.viewpager.setLayoutParams(localLayoutParams);
			this.expandableListView.addHeaderView(this.headView);
			this.pullToRefreshListView
					.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
			this.pullToRefreshListView.setDisableScrollingWhileRefreshing(true);
			this.pullToRefreshListView
					.setOnRefreshListener(this.onRefreshListener);
			this.pullToRefreshListView
					.setOnScrollListener(new PauseOnScrollListener(ImageLoader
							.getInstance(), true, false, this.onScrollListener));
			initMainPageData(1);
		}
		return this.rootView;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		handler.destory();
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	/**
	 * 初始化首页列表数据
	 * 
	 * @param paramBoolean
	 * @param i
	 * @param a
	 */
	private void initMainPageData(int a) {
		if (a == 1) {
			loadingView.setVisibility(0);
		} else {
			loadingView.setVisibility(8);
		}
		RpcRoutine.getInstance().addRpc(RpcEvent.GetHallInfo, handler);
//		Runnable initmainpagedatarun = new Runnable() {
//			public void run() {
//				advertise_list = new ArrayList<Advertise>();
//				anchor_list = new ArrayList<AnchorInfo>();
//				String s = Utils.get_mainpageranchorlist();
////				Log.i("lvjian", "首页返回的数据========》" + s);
//				try {
//					JSONObject jsonObject = new JSONObject(s);
//					int i = jsonObject.getInt("s");
//					if (i == 1) {
//						// 解析bannner数据
//						JSONArray obj_ad = jsonObject.getJSONArray("banner");
////						Log.i("lvjian", "obj_ad=============>"+obj_ad.toString());
//						for (int p = 0; p < obj_ad.length(); p++) {
//							Gson gs = new Gson();
//							JSONObject ad_j = obj_ad.getJSONObject(p);
//							Advertise advertise = gs.fromJson(ad_j.toString(),
//									Advertise.class);
//							advertise_list.add(advertise);
//						}
//						// 解析主播列表数据
//						JSONObject data = jsonObject.getJSONObject("data");
//						JSONArray ja = data.getJSONArray("rooms");
//						for (int j = 0; j < ja.length(); j++) {
//							JSONObject jsona = ja.getJSONObject(j);
//							Gson gson = new Gson();
//							AnchorInfo anchorInfo = gson.fromJson(
//									jsona.toString(), AnchorInfo.class);
//							anchor_list.add(anchorInfo);
//						}
//						handler.sendEmptyMessage(3);
//					} else {
//						handler.sendEmptyMessage(4);
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					handler.sendEmptyMessage(3);
//				}
//			}
//		};
//		ThreadPoolWrap.getThreadPool().executeTask(initmainpagedatarun);
	}
	
	private UIHandler handler = new UIHandler(Looper.getMainLooper(), new IHandler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerCmd.HandlerCmd_GetHallInfoList:
				advertise_list = GlobalData.getInstance().getmHallInfo().advertise_list ;
				anchor_list = GlobalData.getInstance().getmHallInfo().anchor_list ;
//				break ;
//			// 获取首页主播列表数据成功
//			case HandlerCmd.HandlerCmd_Success:
				loadingView.setVisibility(8);
				mainpager_nowifi.setVisibility(8);
				pullToRefreshListView.onRefreshComplete();
				// Log.i("lvjian", "getactivity------main---------->"
				// + MainPageFragment.this.getParentFragment()
				// .getActivity());
				advertiseAdapter = new AdvertiseAdapter(advertise_list,
						MainPageFragment.this.getParentFragment().getActivity());
				viewpager.setAdapter(advertiseAdapter);
				viewpager.setOnPageChangeListener(new OnPageChangeListener() {
						
					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub
						for (int i = 0; i < imageViews.length; i++) {
							imageViews[arg0]
									.setBackgroundResource(R.drawable.dot_red);
							if (arg0 != i) {
								imageViews[i]
										.setBackgroundResource(R.drawable.dot_white);
							}
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});
				initCirclePoint();
//				sendEmptyMessage(5);
				maipager_list_adapter = new TypePagerAdapter(
						MainPageFragment.this.getParentFragment().getActivity(),
						anchor_list);
				expandableListView.setAdapter(maipager_list_adapter);
				expandableListView.setVisibility(0);
				loadingView.setVisibility(8);
				break;
			// 请求接口异常
			case HandlerCmd.HandlerCmd_RPC_Failed:
				loadingView.setVisibility(8);
				mainpager_nowifi.setVisibility(0);
				break;
			case 5:
				if (advertise_list.size() == 0) {
					return;
				}
				viewpager.setCurrentItem(current % advertise_list.size());
				msg = handler.obtainMessage();
				msg.what = 5;
				msg.arg1 = current++;
				handler.sendMessageDelayed(msg, 3000);
				break;
			default:
				break;
			}
		}


	});
	
	// handler更新ui
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case HandlerCmd.HandlerCmd_GetHallInfoList:
//				advertise_list = GlobalData.getInstance().getmHallInfo().advertise_list ;
//				anchor_list = GlobalData.getInstance().getmHallInfo().anchor_list ;
////				break ;
////			// 获取首页主播列表数据成功
////			case HandlerCmd.HandlerCmd_Success:
//				loadingView.setVisibility(8);
//				mainpager_nowifi.setVisibility(8);
//				pullToRefreshListView.onRefreshComplete();
//				// Log.i("lvjian", "getactivity------main---------->"
//				// + MainPageFragment.this.getParentFragment()
//				// .getActivity());
//				advertiseAdapter = new AdvertiseAdapter(advertise_list,
//						MainPageFragment.this.getParentFragment().getActivity());
//				viewpager.setAdapter(advertiseAdapter);
//				viewpager.setOnPageChangeListener(new OnPageChangeListener() {
//
//					@Override
//					public void onPageSelected(int arg0) {
//						// TODO Auto-generated method stub
//						for (int i = 0; i < imageViews.length; i++) {
//							imageViews[arg0]
//									.setBackgroundResource(R.drawable.dot_red);
//							if (arg0 != i) {
//								imageViews[i]
//										.setBackgroundResource(R.drawable.dot_white);
//							}
//						}
//					}
//
//					@Override
//					public void onPageScrolled(int arg0, float arg1, int arg2) {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					public void onPageScrollStateChanged(int arg0) {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				initCirclePoint();
////				sendEmptyMessage(5);
//				maipager_list_adapter = new TypePagerAdapter(
//						MainPageFragment.this.getParentFragment().getActivity(),
//						anchor_list);
//				expandableListView.setAdapter(maipager_list_adapter);
//				expandableListView.setVisibility(0);
//				loadingView.setVisibility(8);
//				break;
//			// 请求接口异常
//			case HandlerCmd.HandlerCmd_RPC_Failed:
//				loadingView.setVisibility(8);
//				mainpager_nowifi.setVisibility(0);
//				break;
//			case 5:
//				if (advertise_list.size() == 0) {
//					return;
//				}
//				viewpager.setCurrentItem(current % advertise_list.size());
//				msg = obtainMessage();
//				msg.what = 5;
//				msg.arg1 = current++;
//				sendMessageDelayed(msg, 3000);
//				break;
//			default:
//				break;
//			}
//		};
//	};

	private void initCirclePoint() {
		// 广告大于0
		if (advertise_list.size() > 0) {
			group.removeAllViews();
			imageViews = new ImageView[advertise_list.size()];
			// 广告栏的小圆点图标
			for (int i = 0; i < advertise_list.size(); i++) {
				// 创建一个ImageView, 并设置宽高. 将该对象放入到数组中
				imageView = new ImageView(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				params.setMargins(5, 0, 5, 0);
				imageView.setLayoutParams(params);
				imageViews[i] = imageView;
				// 初始值, 默认第0个选中
				if (i == 0) {
					imageViews[i].setBackgroundResource(R.drawable.dot_red);
				} else {
					imageViews[i].setBackgroundResource(R.drawable.dot_white);
				}
				// 将小圆点放入到布局中
				group.addView(imageViews[i]);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_nowifi:
			mainpager_nowifi.setVisibility(0);
			initMainPageData(1);
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
