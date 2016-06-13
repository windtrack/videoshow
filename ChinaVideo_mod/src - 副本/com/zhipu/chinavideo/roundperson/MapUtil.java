package com.zhipu.chinavideo.roundperson;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarNearbySearchSortType;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.radar.RadarUploadInfoCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ASCIIEncryUtil;


/**
 * @author: zhongxf
 * @Description: 周边达人工具类
 * @date: 2016年4月15日
 */
public class MapUtil implements RadarUploadInfoCallback, RadarSearchListener, BDLocationListener {

	// 定位相关
	LocationClient mLocClient;// 定位的client
	private int pageIndex = 0;// 当前的搜索页
	private int curPage = 0;//
	private int totalPage = 0;// 查询附近的人的总页
	private LatLng pt = null;// 定位的经纬度对象

	// 周边雷达相关
	RadarNearbyResult listResult = null;// 返回的周边的人的列表
	private String userID = "";// 上传的位置的UserId
	private String userComment = "";// 备注信息（用户的信息的JSON串）
	private Context context;// 上下文路径
	private static MapUtil mu;

	private boolean isLocSucc = false;

	private Handler mHandler;

	private List<RoundPersonVo> list;// 正在显示数据的List
	private RoundPersonAdapter adapter;// 适配器
	private PullToRefreshExpandableListView listView;// 显示的ListView
	private List<RoundPersonVo> allList;// 装所有数据的List

	private String type = "all";// 加载数据 all:查看全部 player:只看玩家 zhubo:只看主播


	private View loadingView;// 等待加载的View
	private Button noDataBtn;// 无数据提示的Button

	private int radius = 100000;// 搜索附近的人的半径

	public ClearLocationDialog clearDia;
	private int isStealthPerson = 0;

	// 清除正在显示的数据源
	public void clearShow() {
		list.clear();
	}

	// 只显示主播
	public void onlyShowZB() {
		type = RoundPersonUtil.ONLY_ZHUBO;
		for (int i = 0; i < allList.size(); i++) {
			RoundPersonVo vo = allList.get(i);
			if ("z".equals(vo.getType())) {
				list.add(vo);
			}
		}
		adapter.notifyDataSetChanged();
		listView.onRefreshComplete();

		if (list.size() <= 0) {
			noDataBtn.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
		else {
			noDataBtn.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}

	}

	// 只显示玩家
	public void onlyShowPlayer() {
		type = RoundPersonUtil.ONLY_PLAYER;
		for (int i = 0; i < allList.size(); i++) {
			RoundPersonVo vo = allList.get(i);
			if ("p".equals(vo.getType())) {
				list.add(vo);
			}
		}
		adapter.notifyDataSetChanged();
		listView.onRefreshComplete();

		if (list.size() <= 0) {
			noDataBtn.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
		else {
			noDataBtn.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
	}

	// 显示全部
	public void showAll() {
		type = RoundPersonUtil.ALL_PERSON;
		for (int i = 0; i < allList.size(); i++) {
			list.add(allList.get(i));
		}

		adapter.notifyDataSetChanged();
		listView.onRefreshComplete();

		if (list.size() <= 0) {
			noDataBtn.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
		else {
			noDataBtn.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
	}


	// 获取地图的单例类
	public static MapUtil getInstance(Context cxt, String userId, String info, View loadView,
			Button noDataButton, int isStealth) {
		if (mu == null) {
			mu = new MapUtil(cxt, userId, info, loadView, noDataButton, isStealth);
		}
		return mu;
	}

	private MapUtil(Context cxt, String userID, String info, View loadView, Button noDataBtn,
			int isStealth) {
		this.context = cxt;
		this.userID = userID;
		allList = new ArrayList<RoundPersonVo>();
		this.loadingView = loadView;
		this.noDataBtn = noDataBtn;
		this.userComment = info;
		this.isStealthPerson = isStealth;
		this.type = cxt.getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE).getString(
				RoundPersonUtil.ROUND_TYPE_KEY, RoundPersonUtil.ALL_PERSON);
//		Log.e("上送的用户信息", "=====" + userID + "---" + userComment);
		clearDia = new ClearLocationDialog();
		clearDia.setOnSureClickListen(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				clearDia.closeDialog();
				((Activity)context).finish();
			}
		});
		init(cxt);// 初始化地图
		mHandler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				switch (msg.what) {
					case 1://上送位置
						if (isLocSucc) {
							if (pt == null) {
//								Log.e("unfnd", "未能获取位置");
								return;
							}
							if (isStealthPerson != 1) {
								RadarUploadInfo info = new RadarUploadInfo();
								info.comments = userComment;
								info.pt = pt;
								RadarSearchManager.getInstance().uploadInfoRequest(info);
							}
							else {// 如果用户是神秘人
								clearInfo();// 清除位置信息
							}
							// RadarSearchManager.getInstance().startUploadAuto(mu,
							// 5000);
						}
						else {
							mHandler.sendEmptyMessageDelayed(1, 1000);
						}
						break;

					case 2://搜索周边达人
						if (isLocSucc) {
							if (pt == null) {
								Toast.makeText(context, "未能成功获取位置", Toast.LENGTH_LONG).show();
								return;
							}
							searchRequest(pageIndex);
						}
						else {
							mHandler.sendEmptyMessageDelayed(2, 1000);
						}
						break;

					default:
						break;
				}

			}
		};
	}

	// 初始化地图
	private void init(Context cxt) {
		// 周边雷达设置监听
		RadarSearchManager.getInstance().addNearbyInfoListener(this);
		// 周边雷达设置用户，id为空默认是设备标识
		RadarSearchManager.getInstance().setUserID(userID);
		// 定位初始化
		mLocClient = new LocationClient(cxt);
		mLocClient.registerLocationListener(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000);// 定位的间隔时间
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	// 上传位置数据到百度服务器
	public void upload() {
		mHandler.sendEmptyMessage(1);
	}

	/**
	 * @author： zhongxf
	 * @Description:清楚当前的雷达信息
	 */
	public void clearInfo() {
		RadarSearchManager.getInstance().clearUserInfo();
	}

	// 搜索周边的人
	public void search(List<RoundPersonVo> list, RoundPersonAdapter adapter,
			PullToRefreshExpandableListView listView) {
		this.list = list;
		this.adapter = adapter;
		this.listView = listView;
		this.listView.setOnRefreshListener(onRefreshListener);
		allList.clear();
		list.clear();
		mHandler.sendEmptyMessage(2);
	}

	// 下拉刷新，重新加载数据
	@SuppressWarnings("rawtypes")
	private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 下拉刷新
			allList.clear();
			list.clear();
			pageIndex=0;
			mHandler.sendEmptyMessage(2);
		}

		public void onPullUpToRefresh(PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 上拉加载
//			loadMore();// 上拉加载更多
			pageIndex = pageIndex+1;
			if(pageIndex>=totalPage){
				listView.onRefreshComplete();
			}else{
				mHandler.sendEmptyMessage(2);
			}
			
		}
	};

	// 所有周边的人
	private void searchRequest(int index) {
//		Log.e("unfind", "--------------"+index);
		RadarNearbySearchOption option = new RadarNearbySearchOption().centerPt(pt).pageNum(index).radius(radius)
				.sortType(RadarNearbySearchSortType.distance_from_far_to_near);
		RadarSearchManager.getInstance().nearbyInfoRequest(option);
	}

	/**
	 * @author： zhongxf
	 * @Description:
	 */
	@Override
	public void onReceiveLocation(BDLocation arg0) {
		if (arg0 == null) {
			return;
		}
		pt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
		isLocSucc = true;
	}

	/**
	 * @author： zhongxf
	 * @Description:
	 */
	@Override
	public void onGetClearInfoState(RadarSearchError error) {
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// 清除成功
			if (isStealthPerson != 1) {
				clearDia.show(context);
			}

		}
		else {
			if (isStealthPerson != 1) {
				Toast.makeText(context, "清除位置失败", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * @author： zhongxf
	 */
	@Override
	public void onGetNearbyInfoList(RadarNearbyResult result, RadarSearchError error) {
		loadingView.setVisibility(View.GONE);
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// 获取成功
			listResult = result;
//			Log.e("unfind", "搜索附近的人返回："+listResult.infoList.size()+"--------"+result.pageIndex+"--------------"+result.pageNum);
			curPage = result.pageIndex;
			totalPage = result.pageNum;
			// 处理数据
			list.clear();
			if(pageIndex==0){
				allList.clear();
			}
			for (int i = 0; i < listResult.infoList.size(); i++) {
				RoundPersonVo vo = new RoundPersonVo();
				String userId = listResult.infoList.get(i).userID;

				vo.setId(userId);
				String info = listResult.infoList.get(i).comments;
				String userInfos = ASCIIEncryUtil.string(info);
				String[] strs = userInfos.split(",");
				if (strs != null && strs.length >= 2) {
					vo.setName(strs[0]);
					vo.setType(strs[1]);
					if (strs.length > 2) {
						if ("n".equals(strs[2])) {
							vo.setImgUrl(null);
						}
						else {
							vo.setImgUrl(APP.USER_LOGO_ROOT + userId + strs[2]);
						}
					}
					else {
						vo.setImgUrl(null);
					}
					vo.setDis(listResult.infoList.get(i).distance);
					allList.add(vo);
				}
			}
			if (allList.size() == 0) {
				listView.setVisibility(View.GONE);
				noDataBtn.setVisibility(View.VISIBLE);
				return;
			}
			if (RoundPersonUtil.ALL_PERSON.equals(type)) {// 查看全部
				showAll();
			}
			else if (RoundPersonUtil.ONLY_PLAYER.equals(type)) {// 只看玩家
				onlyShowPlayer();
			}
			else if (RoundPersonUtil.ONLY_ZHUBO.equals(type)) {
				onlyShowZB();
			}
		}
		else {
			// 获取失败
//			curPage = 0;
//			totalPage = 0;
			listView.setVisibility(View.GONE);
			noDataBtn.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * @author： zhongxf
	 * @Description: 一次上传的回调
	 */
	@Override
	public void onGetUploadState(RadarSearchError error) {
		// TODO Auto-generated method stub、
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// 上传成功
		}
		else {
			// 上传失败
		}
	}

	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 释放周边雷达相关
		RadarSearchManager.getInstance().removeNearbyInfoListener(this);
		// RadarSearchManager.getInstance().clearUserInfo();
		RadarSearchManager.getInstance().destroy();
		mu = null;
	}

	/**
	 * @author： zhongxf
	 * @Description: 自动上传位置信息的回调
	 */
	@Override
	public RadarUploadInfo onUploadInfoCallback() {
		// TODO Auto-generated method stub
		RadarUploadInfo info = new RadarUploadInfo();
		info.comments = userComment;
		info.pt = pt;
		return info;
	}

}
