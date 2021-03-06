package com.zhipu.chinavideo.roundperson;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.zhipu.chinavideo.AnchorCenterActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ASCIIEncryUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author: zhongxf
 * @Description: 周边达人的Activity
 * @date: 2016年4月13日
 */
public class RoundPersonActivity extends Activity {

	private ImageView backBtn;// 返回按钮
	private TextView title;// 标题

	private PullToRefreshExpandableListView listView;// 下拉刷新的ListView
	private ExpandableListView refreshListView;// ListView
	private List<RoundPersonVo> list;// 数据源
	private RoundPersonAdapter adapter;// 适配器

	private String userid;// 用户编号
	private String info;// 用户的信息字符串
	private SharedPreferences share;// 本地存储的对象
	private MapUtil mu;
	private View loadingView;
	
	private Button checkBtn;//筛选按钮
	private Button noDataBtn;//没有数据按钮
	
	private ImageView topImg;
	private Bitmap playerIcon;
	private Bitmap zhuboIcon;
	private int is_stealth = 0;//是否是神秘人
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_round_person);
		
		Intent intent = getIntent();
		is_stealth = intent.getIntExtra("IS_STEALTH", 0);
		
		backBtn = (ImageView) findViewById(R.id.title_back);
		backBtn.setOnClickListener(listen);
		title = (TextView) findViewById(R.id.title_text);
		title.setText(getResources().getString(R.string.round_person));
		listView = (PullToRefreshExpandableListView) findViewById(R.id.round_person_lsit);
		refreshListView = listView.getRefreshableView();
		refreshListView.setGroupIndicator(null);
		listView.setMode(PullToRefreshBase.Mode.BOTH);//只有下拉刷新
		listView.setDisableScrollingWhileRefreshing(true);
		list = new ArrayList<RoundPersonVo>();
		adapter = new RoundPersonAdapter(list, RoundPersonActivity.this);
		refreshListView.setAdapter(adapter);// 设置显示数据的适配器
		refreshListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RoundPersonActivity.this,AnchorCenterActivity.class);
				intent.putExtra("id", list.get(arg2).getId());
				startActivity(intent);
				return false;
			}
		});
		noDataBtn = (Button) findViewById(R.id.no_data);
		noDataBtn.setOnClickListener(listen);
		loadingView = findViewById(R.id.get_round_person_view);
		share = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		String url = share.getString(APP.USER_ICON, "");
		userid = share.getString(APP.USER_ID, "");
		SDKInitializer.initialize(getApplicationContext());//初始化百度地图SDK
		String type = share.getString(APP.USER_TYPE, "user");//user:普通用户   anchor:主播
		String ut = "p";
		if("anchor".equals(type)){
			ut = "z";
		}else{
			ut = "p";
		}
		
		info = share.getString(APP.NICKNAME, "")+","+ut+","+getPicName(url,userid);
		// 启动地图定位
		mu = MapUtil.getInstance(RoundPersonActivity.this, userid, ASCIIEncryUtil.ascII(info),loadingView,noDataBtn,is_stealth);//初始化并定位
		mu.upload();//上送位置数据到百度服务器
		getData();// 获取周边达人的数据
		checkBtn = (Button) findViewById(R.id.shaixuan_btn);
		checkBtn.setOnClickListener(listen);
		
		topImg = (ImageView) findViewById(R.id.icon);
		playerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.player_icon);
		zhuboIcon = BitmapFactory.decodeResource(getResources(), R.drawable.zhubo_icon);
		String t = share.getString(RoundPersonUtil.ROUND_TYPE_KEY, RoundPersonUtil.ALL_PERSON);
		if(RoundPersonUtil.ONLY_PLAYER.equals(t)){
			topImg.setVisibility(View.VISIBLE);
			topImg.setImageBitmap(playerIcon);
		}else if(RoundPersonUtil.ONLY_ZHUBO.equals(t)){
			topImg.setVisibility(View.VISIBLE);
			topImg.setImageBitmap(zhuboIcon);
		}else{
			topImg.setVisibility(View.GONE);
		}
	}
	
	
	//根据头像地址的URL获取头像的名字
	private String getPicName(String url,String userId){
		String name = "";
		if(url.indexOf(".jpg")>0||url.indexOf(".png")>0){
			String[] urls = url.split("/");
			String allName = urls[urls.length-1];
			name = allName.substring(userId.length(),allName.length());
		}else{
			name = "n";
		}
		return name; 
	}
	// 根据页码和分页数获取数据
	private void getData() {
		mu.search(list,adapter,listView);
	}

	private final static int REQUEST_DATA_SUCCESS = 1;// 发送数据请求成功
	// Handler
	private Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case REQUEST_DATA_SUCCESS:
				listView.onRefreshComplete();// 隐藏View
				refreshListView.expandGroup(0);
				adapter.notifyDataSetChanged();// 通知界面修改
				break;
			default:
				break;
			}
		};
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mu.onDestroy();
		
		if(zhuboIcon!=null){
			zhuboIcon.recycle();
		}
		if(playerIcon!=null){
			playerIcon.recycle();
		}
		
		super.onDestroy();
	}
	private SelectPopWindow menuWindow;
	//显示筛选条件的底部弹出窗
	private void showBottom(){
		menuWindow = new SelectPopWindow(RoundPersonActivity.this, listen,is_stealth);  
        //显示窗口  
        menuWindow.showAtLocation(RoundPersonActivity.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置 
	}
	
	// 整个界面的点击监听
	private OnClickListener listen = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.title_back:// 头部返回按钮
				finish();
				break;
			case R.id.shaixuan_btn:
				showBottom();
				break;
			case R.id.only_zhubo://只看主播
				mu.clearShow();
				share.edit().putString(RoundPersonUtil.ROUND_TYPE_KEY, RoundPersonUtil.ONLY_ZHUBO).commit();
				mu.onlyShowZB();
				menuWindow.dismiss();
				topImg.setVisibility(View.VISIBLE);
				topImg.setImageBitmap(zhuboIcon);
				break;
			case R.id.only_player://只看玩家
				mu.clearShow();
				share.edit().putString(RoundPersonUtil.ROUND_TYPE_KEY, RoundPersonUtil.ONLY_PLAYER).commit();
				mu.onlyShowPlayer();
				menuWindow.dismiss();
				topImg.setVisibility(View.VISIBLE);
				topImg.setImageBitmap(playerIcon);
				break;
			case R.id.only_all://查看全部
				mu.clearShow();
				share.edit().putString(RoundPersonUtil.ROUND_TYPE_KEY, RoundPersonUtil.ALL_PERSON).commit();
				mu.showAll();
				menuWindow.dismiss();
				topImg.setVisibility(View.GONE);
				break;
			case R.id.clear_location://清除位置信息
				menuWindow.dismiss();
				if(is_stealth==1){
					finish();
				}else{
					mu.clearInfo();
				}
				break;
			case R.id.no_data:
				noDataBtn.setVisibility(View.GONE);
				loadingView.setVisibility(View.VISIBLE);
				mu.search(list, adapter, listView);
				break;
			default:
				break;
			}
		}
	};
	
}
