package com.zhipu.chinavideo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.adapter.AnchorNewAdapter;
import com.zhipu.chinavideo.adapter.ChatAdapter;
import com.zhipu.chinavideo.adapter.ChatGiftAdapter;
import com.zhipu.chinavideo.adapter.RoomTabAdapter;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.db.HandlerCmd;
import com.zhipu.chinavideo.db.MyOpenHelper;
import com.zhipu.chinavideo.entity.ActivityMsg;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.entity.AnchorNews;
import com.zhipu.chinavideo.entity.Guard;
import com.zhipu.chinavideo.entity.Historys;
import com.zhipu.chinavideo.entity.ChatMessage;
import com.zhipu.chinavideo.entity.SiLiao;
import com.zhipu.chinavideo.fragment.MommonCountFragment;
import com.zhipu.chinavideo.fragment.MommonManageFragment;
import com.zhipu.chinavideo.fragment.PubChatFragment;
import com.zhipu.chinavideo.fragment.PriChatFragment;
import com.zhipu.chinavideo.manager.AnchorManager;
import com.zhipu.chinavideo.manager.ChooseSongManager;
import com.zhipu.chinavideo.manager.DownRunwayManager;
import com.zhipu.chinavideo.manager.EditManager;
import com.zhipu.chinavideo.manager.GiftManager;
import com.zhipu.chinavideo.manager.RankListManager;
import com.zhipu.chinavideo.manager.RunwayManager;
import com.zhipu.chinavideo.manager.ShouHuManager;
import com.zhipu.chinavideo.manager.SoundManager;
import com.zhipu.chinavideo.player.LoadStreamInterface;
import com.zhipu.chinavideo.player.MediaPlayerController;
import com.zhipu.chinavideo.player.MediaPlayerException;
import com.zhipu.chinavideo.player.NotRelativeLayoutException;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.rpc.RpcRoutine;
import com.zhipu.chinavideo.socket.BaseClient;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.BasicActivity;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.ConsumpUtil;
import com.zhipu.chinavideo.util.PagerSlidingTabStrip;
import com.zhipu.chinavideo.util.ParseMessage;
import com.zhipu.chinavideo.util.RunTextView;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.UIUtil;
import com.zhipu.chinavideo.util.UMengShare;
import com.zhipu.chinavideo.util.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 直播间界面
 */
public class LiveRoomActivity extends BasicActivity implements OnClickListener {
	// 连接socket的user_id要用房间信息里返回的openid

	private String mMommonUrl;// 财神消息服务器地址
	private int mMommonPort;// 财神消息服务器端口

	private String openid;
	private static RelativeLayout liveroomview;
	private ViewStub input_viewstub;
	private static RelativeLayout intpu_layout;
	private TextView input_et;
	private static EditManager editmanager;
	// 检测用户由游客从房间登录后状态，数据更新状态
	private boolean is_login = false;
	// 房间主播的id
	private static String anchor_id = "";
	private String guanzhu_num = "";// 关注数
	private final int GETGUANZHUN_NUM_OK = 3;// 获取关注数成功
	private final int GETGUANZHUN_NUM_ERROR = 4;// 获取关注数失败
	private final int GUANZHU_OK = 5;// 关注成功
	private final int GUANZHU_ERROR = 6;// 关注失败
	private final int GET_MSG = 7;// 收到Socket消息的Handler

	// 财神相关
	public static final int HANDLE_MOMMOM_CONNECT = 88;// 财神消息

	public static final int C_MOMMON_CNNECT = 7;
	public static final int S_GET_MOMMON_CNNECT = 74;

	public static final int S_GET_MOMMON_START = 10;// 游戏开始

	public static final int C_MOMMON_CLICK = 8;// 点击金币
	public static final int S_GET_MOMMON_CLICK = 9;// 点击金币回馈

	public static final int S_GET_MOMMON_OVER = 11;// 游戏结束

	private static SharedPreferences sharedPreferences;// 本地存储
	private String chat_url;// Socket服务器的地址
	private int port;// Socket消息服务器的端口
	private String leftlive_url = "";// 左边播放器直播的地址的URL
	private String rightlive_url = "";// 右边播放器直播的地址的URL
	private String leftStream = "";// 左边播放器对应的Stream
	private String rightStream = "";// 右边播放器对应的Stream
	private String room_id = "";// 房间编号
	private DisplayMetrics dm;// 设备屏幕信息
	private static int screenWidth;// 屏幕宽度
	private static int screenHeight;// 屏幕高度
	private SQLiteDatabase db;// 本地存储db
	// 房间信息
	private String anchor_icon;
	private String anchor_headicon;
	private String anchor_received_level;
	private static String anchor_name;
	private ImageView room_back;// 返回的ImageView
	private int is_follow = 0;// 是否关注
	private TextView room_guanzhu_num;// 显示房间关注数
	private TextView guanzhu_text;//
	private ImageView guanzhu_icon;// 关注的Icon
	private RelativeLayout room_guanzhu;
	private static ViewPager viewPager;
	private PagerSlidingTabStrip indicator;
	private static RoomTabAdapter roomTabAdapter;
	public static BaseClient client;
	public static BaseClient mommonclient;
	private String timestamp;
	private String openkey;
	private static Context context;
	private ImageView gift_icon;
	private CircularImage input_icon;
	private static GiftManager giftmanager;
	private AnchorManager anchormanager;
	private static RankListManager rankListManager;
	private static ChooseSongManager chooseSongManager;// 点歌界面管理
	private static ShouHuManager shouHuManager;
	private ViewStub gift_viewstub;// 礼物的ViewStub
	private ViewStub anchor_viewstub;// 主播信息的ViewStub
	private ViewStub ranklist_viewstub;
	private ViewStub shouhulist_viewstub;// 守护列表的ViewStub
	private ViewStub chooseSongViewStub;// 点歌界面的ViewStub
	private List<ChatMessage> messages;// 消息源
	private List<ChatMessage> primessages;
	private static ChatAdapter chatadapter;// 消息适配器
	private static ChatAdapter pri_chatadapter;
	private ListView pub_chat_listview;// 消息的ListView
	//
	private ListView pri_chat_listview;
	private RunwayManager mRunwayManager;// 顶部跑马灯管理类
	private DownRunwayManager down_mRunwayManager;// 界面中间跑马灯管理类
	private RunTextView runwayTextView;// 订不跑马灯的View
	private RunTextView down_runwayTextView;// 界面中间跑马灯View
	// 聊天listview
	private static ListView edit_listview;
	// 私聊消息
	private static ListView pri_edit_list;
	private static ImageView red_addone;
	// 当前播放的Anchor
	private AnchorInfo anchor_current;
	private LinearLayout video_more_button;
	private boolean button_is_show = true;
	private ImageView video_setting;
	private ImageView video_shengyin;
	private ImageView video_big;
	private ImageView video_share;
	private ImageView video_stop;
	private boolean is_fullscreen = false;
	private boolean is_Audiomode = false;
	private TextView yinpinmoshi_tv;
	// 主播在线状态
	private String status = "1";
	// 红豆
	// 用户的财富等级
	private static String user_cost_level = "0";
	public static ImageLoader mImageLoader = null;
	public static DisplayImageOptions mOptions;
	// 快速刷礼物按钮
	private ImageView fastsend_gift_img;
	private TextView fastsend_gift_num;
	private RelativeLayout gift_button;
	private ListView chat_gift_item;
	public static ChatGiftAdapter chatGiftAdapter;
	public static List<ChatMessage> giftmessages;
	private Timer timer;
	private Timer timer_run_bottom;
	private Timer timer_run_top;
	private static Timer timershuaping;
	private int is_guard = 0;
	private static boolean shuaping = true;
	// 禁言
	public static boolean is_shutup = true;
	private List<String> shut_up_list = new ArrayList<String>();
	private int mVolume = -1;
	private int mMaxVolume;
	private AudioManager mAudioManager;
	private GestureDetector mGestureDetector;
	// 用户的守护等级和守护类型
	private static String g_lv = "";
	private static String g_type = "";

	private MommonManageFragment mPlaceFragment;
	private MommonCountFragment mMommonCountFragment;

	public boolean isPause;
	public boolean isMommonStart;

	private String loginUserId = "";

	private ListView m_anchorNewsView;// 主播离线动态显示
	private AnchorNewAdapter m_AnchorNewAdapter;
	private ImageView mImageView_ctvlogo;
	private Button mBt_changeRoom;
	boolean isClickChangeAnchor;// 点击换一换

	// 播放器的变量
	private RelativeLayout leftMediaViewCon;// 左侧播放器的容器
	private RelativeLayout rightMediaViewCon;// 右侧播放器的容器
	private ImageView leftTiger;// 左边老虎
	private ImageView rightTiger;// 右边老虎
	private TextView leftInfo;// 左边等待加载提示
	private TextView rightInfo;// 右边等待加载提示
	private MediaPlayerController leftController;// 左边播放器的控制类
	private MediaPlayerController rightController;// 右边播放器的控制类
	private ImageView leftPlayerBg;// 左边播放器的背景
	private ImageView rightPlayerBg;// 右边播放器的背景
	private AnimationDrawable leftanimationDrawable;// 左边视频加载等待的小老虎动画
	private AnimationDrawable rightanimationDrawable;// 右边加载视频等待的小老虎动画
	private boolean IS_LIANMAI = false;// 直播间是否是连麦标志
	private RelativeLayout videoFather;// 播放器的父类容器
	private LinearLayout allPlayerCon;// 播放器的容器
	private boolean isFullScreened = false;// 是否开启过全屏

	private boolean isPlaying;
	private boolean isAnchorLiving;
	private int roomSourceFlag;// 视频源 0 web 1手机

	private boolean useShare ;
	
	private void initview() {
		sharedPreferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		is_login = sharedPreferences.getString(APP.IS_LOGIN, "").equals("true");
		getScreenWH();
		room_back = (ImageView)findViewById(R.id.room_back);
		room_back.setOnClickListener(this);
		viewPager = (ViewPager)findViewById(R.id.room_viewpager);
		indicator = (PagerSlidingTabStrip)findViewById(R.id.room_indicator);
		int a = Utils.dip2px(context, 14.0f);
		indicator.setTabPaddingLeftRight(a);
		room_guanzhu_num = (TextView)findViewById(R.id.room_guanzhu_num);
		guanzhu_text = (TextView)findViewById(R.id.guanzhu_text);
		guanzhu_icon = (ImageView)findViewById(R.id.guanzhu_icon);
		room_guanzhu = (RelativeLayout)findViewById(R.id.room_guanzhu);
		room_guanzhu.setOnClickListener(this);
		// pb = (TextView) findViewById(R.id.progress_bar);
		// video_background = (ImageView) findViewById(R.id.video_background);
		// RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth,
		// screenWidth * 3 / 4);
		// leftPlayerBg.setLayoutParams(params);
		// sf.setLayoutParams(params);
		intpu_layout = (RelativeLayout)findViewById(R.id.input);
		input_et = (TextView)findViewById(R.id.input_et);
		input_et.setOnClickListener(this);
		input_viewstub = (ViewStub)findViewById(R.id.input_viewstub);
		gift_icon = (ImageView)findViewById(R.id.gift_icon);
		gift_icon.setOnClickListener(this);
		gift_button = (RelativeLayout)findViewById(R.id.gift_button);
		gift_button.setOnClickListener(this);
		input_icon = (CircularImage)findViewById(R.id.input_icon);
		input_icon.setOnClickListener(this);
		gift_viewstub = (ViewStub)findViewById(R.id.gift_viewstub);
		anchor_viewstub = (ViewStub)findViewById(R.id.anchor_viewstub);
		ranklist_viewstub = (ViewStub)findViewById(R.id.ranklist_viewstub);
		shouhulist_viewstub = (ViewStub)findViewById(R.id.shouhulist_viewstub);
		chooseSongViewStub = (ViewStub)findViewById(R.id.choose_song_viewstub);
		runwayTextView = (RunTextView)findViewById(R.id.laba_run);
		down_runwayTextView = (RunTextView)findViewById(R.id.xitongxiaoxi);
		runwayTextView.getBackground().setAlpha(150);
		down_runwayTextView.getBackground().setAlpha(150);
		red_addone = (ImageView)findViewById(R.id.red_addone);
		messages = new ArrayList<ChatMessage>();
		primessages = new ArrayList<ChatMessage>();
		giftmessages = new ArrayList<ChatMessage>();
		video_more_button = (LinearLayout)findViewById(R.id.video_more_button);
		video_setting = (ImageView)findViewById(R.id.video_setting);
		video_shengyin = (ImageView)findViewById(R.id.video_shengyin);
		video_big = (ImageView)findViewById(R.id.video_big);
		video_share = (ImageView)findViewById(R.id.video_share);
		
		useShare = true ;
		if(!useShare){
			video_share.setVisibility(View.GONE);
		}
		
		
		video_stop = (ImageView)findViewById(R.id.video_stop);
		video_setting.setOnClickListener(this);
		video_shengyin.setOnClickListener(this);
		video_big.setOnClickListener(this);
		video_share.setOnClickListener(this);
		video_stop.setOnClickListener(this);
		yinpinmoshi_tv = (TextView)findViewById(R.id.yinpinmoshi_tv);
		fastsend_gift_img = (ImageView)findViewById(R.id.fastsend_gift_img);
		fastsend_gift_num = (TextView)findViewById(R.id.fastsend_gift_num);
		mOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.loading_img)
				.cacheInMemory().cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// 设置用户图像
		if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
			String url = sharedPreferences.getString(APP.USER_ICON, "");
			mImageLoader.displayImage(url, input_icon, mOptions);
		}
		isPause = false;
		mImageView_ctvlogo = (ImageView)findViewById(R.id.imageView_ctvlogo);
		loginUserId = sharedPreferences.getString(APP.USER_ID, "");

		m_anchorNewsView = (ListView)findViewById(R.id.listView_anchor_news);
		m_anchorNewsView.setVisibility(View.GONE);
		mBt_changeRoom = (Button)findViewById(R.id.button_changeroom);
		mBt_changeRoom.setVisibility(View.GONE);
		initPlayer();// 初始所有连麦的控件
	}

	// 播放器初始化
	private void initPlayer() {
		leftTiger = (ImageView)findViewById(R.id.left_live_xiaolaohu);
		leftInfo = (TextView)findViewById(R.id.left_progress_bar);
		leftPlayerBg = (ImageView)findViewById(R.id.left_video_background);
		leftTiger.setImageResource(R.drawable.room_tiger_loading);
		leftanimationDrawable = (AnimationDrawable)leftTiger.getDrawable();
		leftanimationDrawable.start();
		rightTiger = (ImageView)findViewById(R.id.right_live_xiaolaohu);
		rightInfo = (TextView)findViewById(R.id.right_progress_bar);
		rightPlayerBg = (ImageView)findViewById(R.id.right_video_background);
		rightTiger.setImageResource(R.drawable.room_tiger_loading);
		rightanimationDrawable = (AnimationDrawable)rightTiger.getDrawable();
		rightanimationDrawable.start();
		leftMediaViewCon = (RelativeLayout)findViewById(R.id.video_left_con);
		rightMediaViewCon = (RelativeLayout)findViewById(R.id.video_right_con);
		rightMediaViewCon.setVisibility(View.GONE);
		videoFather = (RelativeLayout)findViewById(R.id.video_father);
		allPlayerCon = (LinearLayout)findViewById(R.id.all_player_con);

		leftInfo.setText("客官莫急  马上就到");
		if (leftController == null) {
			leftController = new MediaPlayerController(leftMediaViewCon, context,
					new LoadStreamInterface() {// 视频加载状态监听
						@Override
						public void loadSuccess() {
							showAnchorNews(false);
							leftInfo.setVisibility(View.GONE);
							leftMediaViewCon.setVisibility(View.VISIBLE);
							leftPlayerBg.setVisibility(View.GONE);
							leftanimationDrawable.stop();
							leftTiger.setVisibility(View.GONE);
						}

						@Override
						public void loadError() {
							GetAnchorNews();
							leftInfo.setText("视频加载失败啦！！！");
						}
					});
		}

		setVideoViewSize();// 设置播放器的尺寸
	}

	// 播放视频
	private void play() {
		Log.e("unfind", "视频播放的URL：" + (leftlive_url + "/" + leftStream) + "-----------------"
				+ (rightlive_url + "/" + rightStream));
		// 左右视频播放器的等待动画出现
		leftInfo.setVisibility(View.VISIBLE);
		leftMediaViewCon.setVisibility(View.VISIBLE);
		leftPlayerBg.setVisibility(View.VISIBLE);
		leftanimationDrawable.start();
		leftTiger.setVisibility(View.VISIBLE);

		if (Utils.isEmpty(leftlive_url) || Utils.isEmpty(leftStream)) {// 如果视频的播放地址为空 那么隐藏视频播放器
			leftInfo.setText("开始努力加载视频！！！");
		}
		else {// 如果视频流的地址不为空
			leftMediaViewCon.setVisibility(View.VISIBLE);// 设置视频播放器显示
			leftInfo.setText("客官莫急  马上就到");
			if (leftController == null) {
				leftController = new MediaPlayerController(leftMediaViewCon, context,
						new LoadStreamInterface() {// 视频加载状态监听
							@Override
							public void loadSuccess() {
								showAnchorNews(false);
								leftInfo.setVisibility(View.GONE);
								leftMediaViewCon.setVisibility(View.VISIBLE);
								leftPlayerBg.setVisibility(View.GONE);
								leftanimationDrawable.stop();
								leftTiger.setVisibility(View.GONE);
							}

							@Override
							public void loadError() {
								leftInfo.setText("视频加载失败啦！！！");
								GetAnchorNews();
							}
						});
			}
			try {
				leftController.startPlay(leftlive_url + "/" + leftStream);
			}
			catch (MediaPlayerException e) {
				e.printStackTrace();
			}
		}
		if (IS_LIANMAI) {
			// 左右视频播放器的等待动画出现
			rightInfo.setVisibility(View.VISIBLE);
			rightMediaViewCon.setVisibility(View.VISIBLE);
			rightPlayerBg.setVisibility(View.VISIBLE);
			rightanimationDrawable.start();
			rightTiger.setVisibility(View.VISIBLE);
			if (Utils.isEmpty(leftlive_url) || Utils.isEmpty(rightStream)) {
				rightInfo.setText("开始努力加载视频！！！");
			}
			else {
				rightInfo.setText("客官莫急  马上就到");
				rightMediaViewCon.setVisibility(View.VISIBLE);
				if (rightController == null) {
					rightController = new MediaPlayerController(rightMediaViewCon, context,
							new LoadStreamInterface() {// 视频加载状态监听
								@Override
								public void loadSuccess() {
									rightInfo.setVisibility(View.GONE);
									rightMediaViewCon.setVisibility(View.VISIBLE);
									rightPlayerBg.setVisibility(View.GONE);
									rightanimationDrawable.stop();
									rightTiger.setVisibility(View.GONE);
								}

								@Override
								public void loadError() {
									rightInfo.setText("视频加载失败啦！！！");
									rightController.stop();
								}
							});
				}
				try {
					rightController.startPlay(leftlive_url + "/" + rightStream);
				}
				catch (MediaPlayerException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			rightMediaViewCon.setVisibility(View.GONE);
		}
		setVideoViewSize();// 设置播放器的尺寸
	}

	// 当竖屏的时候，设置尺寸
	private void setVideoViewSize() {
		int height = UIUtil.dip2px(context, 270f);
		int width = height * 4 / 3;
		int halfWidth = screenWidth / 2;
		if (IS_LIANMAI) {// 如果是连麦
			try {
				if (leftController != null) {
					leftController.setSize(width, height);
				}
				if (rightController != null) {
					rightController.setSize(width, height);
				}
				if (width > halfWidth) {
					if (leftController != null) {
						leftController.setMargin(-(width - halfWidth) / 2, 0, 0, 0);
					}
					if (rightController != null) {
						rightController.setMargin(0, 0, -(width - halfWidth) / 2, 0);
					}
				}
			}
			catch (NotRelativeLayoutException e) {
				e.printStackTrace();
			}

			LayoutParams leftConlp = leftMediaViewCon.getLayoutParams();
			leftConlp.width = width;
			leftConlp.height = height;
			leftMediaViewCon.setLayoutParams(leftConlp);

			LayoutParams rightConlp = rightMediaViewCon.getLayoutParams();
			rightConlp.width = width;
			rightConlp.height = height;
			rightMediaViewCon.setLayoutParams(rightConlp);

			// 设置播放器的左边的背景的高度
			LayoutParams leftBglp = leftPlayerBg.getLayoutParams();
			leftBglp.height = UIUtil.dip2px(context, 270f);
			leftPlayerBg.setLayoutParams(leftBglp);

			LayoutParams rightBglp = rightPlayerBg.getLayoutParams();
			rightBglp.height = UIUtil.dip2px(context, 270f);
			rightPlayerBg.setLayoutParams(rightBglp);
		}
		else {// 如果不是连麦
			rightMediaViewCon.setVisibility(View.GONE);// 隐藏右边的视频播放器
			// 设置播放器外层容器的高度和宽度
			LayoutParams leftConlp = leftMediaViewCon.getLayoutParams();
			leftConlp.width = screenWidth;
			leftConlp.height = screenWidth * 3 / 4;
			leftMediaViewCon.setLayoutParams(leftConlp);
			// 设置播放器的背景的高度
			LayoutParams leftBglp = leftPlayerBg.getLayoutParams();
			leftBglp.height = screenWidth * 3 / 4;
			leftPlayerBg.setLayoutParams(leftBglp);
			try {
				if (leftController != null) {
					leftController.setSize(screenWidth, screenWidth * 3 / 4);
					leftController.setMargin(0, 0, 0, 0);
				}
			}
			catch (NotRelativeLayoutException e) {
				e.printStackTrace();
			}
		}

		LayoutParams flp = videoFather.getLayoutParams();
		flp.height = screenWidth * 3 / 4;
		videoFather.setLayoutParams(flp);
	}

	// 全屏状态下设置播放器的尺寸
	private void setVideoSizeByFullScreen() {

		ViewGroup.LayoutParams leftLp = leftMediaViewCon.getLayoutParams();
		try {
			if (IS_LIANMAI) {// 如果是连麦状态
				ViewGroup.LayoutParams rightLp = rightMediaViewCon.getLayoutParams();
				leftMediaViewCon.setVisibility(View.VISIBLE);
				rightMediaViewCon.setVisibility(View.VISIBLE);

				int width = screenHeight / 2;
				int height = width * 3 / 4;

				leftLp.width = width;
				rightLp.width = width;

				leftLp.height = screenWidth;
				rightLp.height = screenWidth;

				leftMediaViewCon.setLayoutParams(leftLp);
				rightMediaViewCon.setLayoutParams(rightLp);

				leftController.setSize(width, height);
				rightController.setSize(width, height);

				if (height < screenWidth) {
					leftController.setMargin(0, (screenWidth - height) / 2, 0, 0);
					rightController.setMargin(0, (screenWidth - height) / 2, 0, 0);
				}
				else {
					leftController.setMargin(0, 0, 0, 0);
					rightController.setMargin(0, 0, 0, 0);
				}

			}
			else {
				rightMediaViewCon.setVisibility(View.GONE);
				leftLp.width = screenWidth * 4 / 3;
				leftLp.height = screenWidth;
				leftMediaViewCon.setLayoutParams(leftLp);
				leftController.setSize(screenWidth * 4 / 3, screenWidth);
				leftController.setMargin((screenHeight - (screenWidth * 4 / 3)) / 2, 0, 0, 0);
			}
		}
		catch (NotRelativeLayoutException e) {
			e.printStackTrace();
		}

		LayoutParams flp = videoFather.getLayoutParams();
		flp.height = screenWidth;
		videoFather.setLayoutParams(flp);
	}

	/**
	 * 显示主播动态
	 */
	private void showAnchorNews(boolean flag) {
		if (flag) {
			m_anchorNewsView.setVisibility(View.VISIBLE);
			mBt_changeRoom.setVisibility(View.VISIBLE);
			yinpinmoshi_tv.setVisibility(View.INVISIBLE);
			mBt_changeRoom.setEnabled(true);
			mBt_changeRoom.setOnClickListener(this);
			m_AnchorNewAdapter = new AnchorNewAdapter(this, 0,
					GlobalData.getInstance(context).mAnchorNewsInfo);
			m_AnchorNewAdapter.setmAnchorID(anchor_id);
			m_anchorNewsView.setAdapter(m_AnchorNewAdapter);
			mImageView_ctvlogo.setVisibility(View.INVISIBLE);
			leftMediaViewCon.setVisibility(View.GONE);
			isPlaying = false;

			video_shengyin.setVisibility(View.GONE);
			video_big.setVisibility(View.GONE);
			video_share.setVisibility(View.GONE);
			video_stop.setVisibility(View.VISIBLE);

		}
		else {
			m_anchorNewsView.setVisibility(View.GONE);
			mBt_changeRoom.setVisibility(View.GONE);
			yinpinmoshi_tv.setVisibility(View.GONE);
			leftMediaViewCon.setVisibility(View.VISIBLE);
			mImageView_ctvlogo.setVisibility(View.VISIBLE);
			isPlaying = true;

			video_shengyin.setVisibility(View.VISIBLE);
			video_big.setVisibility(View.VISIBLE);
			video_share.setVisibility(View.VISIBLE);
//			video_share.setVisibility(View.GONE);
			if(!useShare){
				video_share.setVisibility(View.GONE);
			}
			video_stop.setVisibility(View.VISIBLE);
		}
			
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_liveroom);
		shuaping = true;
		liveroomview = (RelativeLayout)findViewById(R.id.liveroomview);
		context = this;
		Intent intent = this.getIntent();
		room_id = intent.getStringExtra("room_id");
		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		initview();
		g_lv = "0";
		g_type = "0";
		isAnchorLiving = false;
		// 获取房间信息
		if (Utils.isEmpty(room_id)) {
			Utils.showToast(LiveRoomActivity.this, "房间不存在！");
		}
		else {
			// 获取视频播放器，首先加载视频
			getNewRoomInfoAndUrl();
		}
	}

	/**
	 * 获取实际屏幕的宽高
	 */
	public void getScreenWH() {
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	/**
	 * 获取视频流地址
	 */
	private void getResultUrl() {
		// RpcRoutine.getInstance().addRpc(RpcEvent.GetRoomClientUrl, handler,
		// sharedPreferences.getString(APP.USER_ID, ""),
		// sharedPreferences.getString(APP.SECRET, ""), room_id);
		Runnable updatebeansrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.getLiveUrl(sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""), room_id);
					Log.e("unfind", "查询房间直播地址返回：" + result);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject dataJson = obj.getJSONObject("data");
						if (dataJson.has("is_lian")) {
							int isLian = dataJson.getInt("is_lian");
							if (dataJson.has("chat_url")) {
								String msgUrl = dataJson.getString("chat_url");
								if (!Utils.isEmpty(msgUrl)) {
									chat_url = msgUrl.split(":")[0];
									port = new Integer(msgUrl.split(":")[1]).intValue();
								}
							}
							leftlive_url = dataJson.getString("live_url");
							leftStream = dataJson.getString("stream");
							rightlive_url = dataJson.getString("live_url");
							if (isLian == 1) {
								IS_LIANMAI = true;
								JSONObject onlineaqJson = dataJson.getJSONObject("onlineq");
								rightlive_url = onlineaqJson.getString("live_url");
								rightStream = onlineaqJson.getString("streamname");
							}
							else {
								IS_LIANMAI = false;
							}
						}

						handler.sendEmptyMessage(1121);// 发送消息
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(updatebeansrun);
	}

	// 开启连麦
	private void openLianMai() {
		rightInfo.setVisibility(View.VISIBLE);
		rightMediaViewCon.setVisibility(View.VISIBLE);
		rightPlayerBg.setVisibility(View.VISIBLE);
		rightanimationDrawable.start();
		rightTiger.setVisibility(View.VISIBLE);
		ViewGroup.LayoutParams rightBgLp = rightPlayerBg.getLayoutParams();
		ViewGroup.LayoutParams rightLp = rightMediaViewCon.getLayoutParams();
		rightBgLp.width = screenHeight / 2;
		rightBgLp.height = screenWidth;
		rightLp.height = screenHeight;
		rightInfo.setText("客官莫急  马上就到");
		try {
			if (rightController == null) {
				rightController = new MediaPlayerController(rightMediaViewCon, context,
						new LoadStreamInterface() {
							public void loadSuccess() {
								rightInfo.setVisibility(View.GONE);
								rightMediaViewCon.setVisibility(View.VISIBLE);
								rightPlayerBg.setVisibility(View.GONE);
								rightanimationDrawable.stop();
								rightTiger.setVisibility(View.GONE);
							}

							@Override
							public void loadError() {
								rightInfo.setText("视频加载失败啦！！！");
								rightController.stop();
							}
						});
			}
			rightController.startPlay(rightlive_url + "/" + rightStream);
		}
		catch (MediaPlayerException e) {
			e.printStackTrace();
		}
		if (is_fullscreen) {// 如果是全屏状态下开启了连麦
			setVideoSizeByFullScreen();// 全屏状态下设置尺寸
		}
		else {
			setVideoViewSize();// 设置屏幕尺寸
		}
	}

	// 关闭连麦
	private void closeLianMai() {
		rightMediaViewCon.setVisibility(View.GONE);
		rightController.stop();
		// 设置左边播放器的容器的尺寸
		ViewGroup.LayoutParams leftLp = leftMediaViewCon.getLayoutParams();
		if (is_fullscreen) {// 如果是全屏状态下关闭了连麦

			int height = screenWidth;
			int widht = height * 4 / 3;

			leftLp.height = screenWidth;
			leftLp.width = screenHeight;
			try {
				if (leftController != null) {
					leftController.setSize(widht, height);
					if (screenHeight > widht) {
						leftController.setMargin((screenHeight - widht) / 2, 0, 0, 0);
					}
					else {
						leftController.setMargin(0, 0, 0, 0);
					}
				}
			}
			catch (NotRelativeLayoutException e) {
				e.printStackTrace();
			}
		}
		else {

			if (isFullScreened) {
				LayoutParams flp = videoFather.getLayoutParams();
				flp.height = screenWidth * 3 / 4;
				videoFather.setLayoutParams(flp);

				LayoutParams allLp = allPlayerCon.getLayoutParams();
				flp.height = screenWidth * 3 / 4;
				allPlayerCon.setLayoutParams(allLp);

				try {
					LayoutParams leftConLp = leftMediaViewCon.getLayoutParams();
					leftConLp.height = screenWidth * 3 / 4;
					leftMediaViewCon.setLayoutParams(leftConLp);
					if (leftController != null) {
						leftController.setSize(screenWidth, screenWidth * 3 / 4);
						leftController.setMargin(0, 0, 0, 0);
					}
				}
				catch (NotRelativeLayoutException e) {
					e.printStackTrace();
				}
			}
			else {
				leftLp.height = screenWidth * 3 / 4;
				leftLp.width = screenWidth;
				try {
					if (leftController != null) {
						int height = screenWidth * 3 / 4;
						leftController.setSize(screenWidth, height);
						leftController.setMargin(0, 0, 0, 0);
					}
				}
				catch (NotRelativeLayoutException e) {
					e.printStackTrace();
				}
			}
		}
		leftMediaViewCon.setLayoutParams(leftLp);
	}

	private Handler handler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			if (isFinishing()) {// 如果用户已经按下了返回键，界面被Kill，那么就不处理逻辑，直接return
				return;
			}
			switch (msg.what) {
				case HandlerCmd.HandlerCmd_NoLivingAnchor:
					noLivingAnchor_dialog("没有在线的主播") ;
					showAnchorNews(true);
					break;
				case HandlerCmd.HandlerCmd_GetStreamSuccess:
					roomSourceFlag = msg.arg1;
					break;
				case HandlerCmd.HandlerCmd_GetStreamFailed:
					roomSourceFlag = -1;
					// Toast.makeText(context, "", duration)
					break;
				case HandlerCmd.HandlerCmd_CallChangeRoom: {
					doChangeRoom();
				}
					break;
				case HandlerCmd.HandlerCmd_GetAnchorNewsSuccess: {
					int h = leftPlayerBg.getHeight();
					ViewGroup.LayoutParams params = m_anchorNewsView.getLayoutParams();
					params.height = (int)h;
					m_anchorNewsView.setLayoutParams(params);
					showAnchorNews(true);
				}
					break;
				case HandlerCmd.HandlerCmd_GetAnchorNewsFailed: {
					int h = leftPlayerBg.getHeight();
					ViewGroup.LayoutParams params = m_anchorNewsView.getLayoutParams();

					float dividerHeight = m_anchorNewsView.getDividerHeight();

					params.height = (int)h - 140;
					m_anchorNewsView.setLayoutParams(params);

					String headUrl = APP.USER_LOGO_ROOT + anchor_headicon;

					GlobalData.getInstance(LiveRoomActivity.this).mAnchorNewsInfo.clear();
					AnchorNews oneNews = new AnchorNews();
					oneNews.setmHeadIconUrl(headUrl);
					oneNews.setmPhotoUrl("null");
					oneNews.setmId(-99999);
					oneNews.setmLikeCount(0);
					oneNews.setmHasLiked(false);
					GlobalData.getInstance(LiveRoomActivity.this).mAnchorNewsInfo.add(oneNews);

					showAnchorNews(true);
					m_AnchorNewAdapter.notifyDataSetChanged();

				}

					break;

				// 获取视频流地址成功

				case HandlerCmd.HandlerCmd_GetRoomUrlSuccess:
					GlobalData gd = GlobalData.getInstance(LiveRoomActivity.this);
					chat_url = gd.getmRoomCLientUrl().chat_url;
					port = gd.getmRoomCLientUrl().port;
					leftlive_url = gd.getmRoomCLientUrl().live_url;
					leftStream = gd.getmRoomCLientUrl().stream;
					play();
					break;
				case 1121:
					play();
					break;
				// 获取房间信息
				case HandlerCmd.HandlerCmd_GetRoomInfoSuccess:

					GlobalData dataGd = GlobalData.getInstance(LiveRoomActivity.this);
					timestamp = dataGd.getmRoomInfo().timestamp;
					openid = dataGd.getmRoomInfo().openid;
					is_guard = dataGd.getmRoomInfo().is_guard;
					openkey = dataGd.getmRoomInfo().openkey;
					is_follow = dataGd.getmRoomInfo().is_follow;
					anchor_name = dataGd.getmRoomInfo().anchor_name;
					anchor_id = dataGd.getmRoomInfo().anchor_id;
					anchor_received_level = dataGd.getmRoomInfo().anchor_received_level;
					anchor_icon = dataGd.getmRoomInfo().anchor_icon;
					anchor_headicon = dataGd.getmRoomInfo().anchor_head_icon;
					status = dataGd.getmRoomInfo().status;
					anchor_current = dataGd.getmRoomInfo().anchor_current;
					mMommonUrl = dataGd.getmRoomInfo().mMommonUrl;
					mMommonPort = dataGd.getmRoomInfo().mMommonPort;

					// 是否显示刷按钮
					if (!Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTNUM, ""))
							&& !Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTPRICE, ""))
							&& !Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTID, ""))
							&& !Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTNUM, ""))
							&& !Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTICON, ""))) {
						gift_button.setVisibility(0);
						fastsend_gift_num
								.setText(sharedPreferences.getString(APP.FAST_GIFTNUM, ""));
						mImageLoader.displayImage(
								APP.GIFT_PATH + sharedPreferences.getString(APP.FAST_GIFTICON, ""),
								fastsend_gift_img, mOptions);
					}
					if (!"1".equals(status)) {
						isAnchorLiving = false;
						leftMediaViewCon.setVisibility(View.GONE);
						rightMediaViewCon.setVisibility(View.GONE);
						// yinpinmoshi_tv.setVisibility(0);
						// yinpinmoshi_tv.setText("主播休息了！");
						mImageView_ctvlogo.setVisibility(View.INVISIBLE);
						GetAnchorNews();
					}
					else {
						isAnchorLiving = true;
						// rl_video.setVisibility(View.VISIBLE);
						// yinpinmoshi_tv.setVisibility(View.INVISIBLE);
						// pb.setVisibility(View.VISIBLE);
						// tiger_animation.setVisibility(View.VISIBLE);
						// video_background.setVisibility(View.VISIBLE);

						mImageView_ctvlogo.setVisibility(View.VISIBLE);
						// rl_video.setVisibility(0);
						yinpinmoshi_tv.setVisibility(8);
						// String msgBody = activityMsg.getMsg();
						// try {
						// JSONObject object = new JSONObject(msgBody);
						// live_url = object.getString("watchPull");
						// stream = object.getString("stream_name");
						// handler.sendEmptyMessage(1121);
						// } catch (JSONException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
					}

					if (anchor_current != null) {
						MainTabActivity.setAnchor(anchor_current);
					}
					// 初始化聊天
					chatadapter = new ChatAdapter(LiveRoomActivity.this, messages);
					// 初始化私聊
					pri_chatadapter = new ChatAdapter(LiveRoomActivity.this, primessages);
					chatGiftAdapter = new ChatGiftAdapter(LiveRoomActivity.this, giftmessages);
					// 初始化标签
					roomTabAdapter = new RoomTabAdapter(getSupportFragmentManager(), room_id,
							anchor_id);
					viewPager.setAdapter(roomTabAdapter);
					indicator.setViewPager(viewPager);
					indicator.setSelectedTextColorResource(R.color.title_bg);
					indicator.setIndicatorColorResource(R.color.title_bg);
					indicator.setTextSize(getResources().getDimensionPixelSize(
							R.dimen.livehall_tab_textsize));
					indicator.setOnPageChangeListener(new OnPageChangeListener() {
						@Override
						public void onPageSelected(int current) {
							// TODO Auto-generated method stub
							if (current == 0) {
								intpu_layout.setVisibility(0);
								// 切换tab更新是否显示gift_button按钮
								if (!Utils.isEmpty(sharedPreferences
										.getString(APP.FAST_GIFTNUM, ""))
										&& !Utils.isEmpty(sharedPreferences.getString(
												APP.FAST_GIFTPRICE, ""))
										&& !Utils.isEmpty(sharedPreferences.getString(
												APP.FAST_GIFTID, ""))
										&& !Utils.isEmpty(sharedPreferences.getString(
												APP.FAST_GIFTNUM, ""))
										&& !Utils.isEmpty(sharedPreferences.getString(
												APP.FAST_GIFTICON, ""))) {
									gift_button.setVisibility(0);
									fastsend_gift_num.setText(sharedPreferences.getString(
											APP.FAST_GIFTNUM, ""));
									mImageLoader.displayImage(
											APP.GIFT_PATH
													+ sharedPreferences.getString(
															APP.FAST_GIFTICON, ""),
											fastsend_gift_img, mOptions);
								}
							}
							else if (current == 1) {
								intpu_layout.setVisibility(0);
							}
							else {
								intpu_layout.setVisibility(8);
								gift_button.setVisibility(8);
							}
						}

						@Override
						public void onPageScrolled(int arg0, float arg1, int arg2) {
						}

						@Override
						public void onPageScrollStateChanged(int arg0) {
						}
					});
					// 设置关注
					if (is_follow != 0) {
						guanzhu_text.setTextColor(getResources().getColor(R.color.huise_zi));
						guanzhu_text.setText("粉丝榜");
						guanzhu_icon.setVisibility(8);
						room_guanzhu_num.setTextColor(getResources().getColor(R.color.title_bg));
						room_guanzhu.setBackgroundResource(R.color.white);
					}
					editmanager = new EditManager();
					editmanager.initEditManager(LiveRoomActivity.this, input_viewstub,
							intpu_layout, room_id, anchor_id, anchor_name);
					giftmanager = GiftManager.getInstance();
					giftmanager.initGiftManager(LiveRoomActivity.this, gift_viewstub, intpu_layout,
							anchor_id, room_id, anchor_name, gift_button, fastsend_gift_img,
							fastsend_gift_num, viewPager, is_guard);
					anchormanager = AnchorManager.getInstance();
					anchormanager.initAnchorManager(LiveRoomActivity.this, anchor_viewstub,
							anchor_id, room_id);
					rankListManager = RankListManager.getInstance();
					rankListManager.initRankListManager(LiveRoomActivity.this, ranklist_viewstub,
							anchor_id, room_id);
					chooseSongManager = ChooseSongManager.getInstance();// 初始化点歌管理类
					chooseSongManager.init(chooseSongViewStub, context, room_id, anchor_id);
					shouHuManager = ShouHuManager.getInstance();
					shouHuManager.initShouHuManager(LiveRoomActivity.this, shouhulist_viewstub,
							anchor_id, room_id);
					pub_chat_listview = PubChatFragment.getPubchatListView();
					pri_chat_listview = PriChatFragment.getPrichatListView();
					// 公聊不为空
					if (pub_chat_listview != null) {
						pub_chat_listview.setAdapter(chatadapter);
					}
					if (pri_chat_listview != null) {
						pri_chat_listview.setAdapter(pri_chatadapter);
					}
					chat_gift_item = PubChatFragment.getChat_gift_item();

					if (chat_gift_item != null) {
						chat_gift_item.setAdapter(chatGiftAdapter);
					}
					intpu_layout.setVisibility(0);
					// 房间信息获取成功后，向历史观看插入这一记录
					mRunwayManager = RunwayManager.getInstance();
					down_mRunwayManager = DownRunwayManager.getInstance();
					mRunwayManager.init(runwayTextView, LiveRoomActivity.this);
					new MyTask().execute();
					down_mRunwayManager.init(down_runwayTextView, LiveRoomActivity.this);
					// 获取房间信息成功后，获取主播红豆数目和主播的关注数目
					get_hearts_and_fans_num();
					// 连接socket
					SocketStart();
					// 连接socket的同时，获取守护信息
					GetShouhu();
					Get_Shut_Up_List();
					break;
				case GETGUANZHUN_NUM_OK:
					room_guanzhu_num.setText(guanzhu_num);
					break;
				case GETGUANZHUN_NUM_ERROR:
					Log.i("lvjian", "---------获取主播红豆和关注数失败或异常-------");
					break;
				case GUANZHU_OK:
					// 动画切换关东状态
					guanzhu_text.setTextColor(getResources().getColor(R.color.huise_zi));
					guanzhu_icon.setVisibility(8);
					guanzhu_text.setText("粉丝榜");
					room_guanzhu_num.setTextColor(getResources().getColor(R.color.title_bg));
					room_guanzhu_num.setText(guanzhu_num);
					room_guanzhu.setBackgroundResource(R.color.white);
					Animation animation = AnimationUtils.loadAnimation(LiveRoomActivity.this,
							R.anim.title_enter);
					room_guanzhu.startAnimation(animation);
					break;
				case GUANZHU_ERROR:
					Log.i("lvjian", "---------关注失败-------");
					break;
				// 接受聊天消息
				case GET_MSG:
					ActivityMsg activityMsg = (ActivityMsg)msg.obj;
					ParseMessage m = new ParseMessage(activityMsg, indicator,
							LiveRoomActivity.this, anchor_name);
					Log.e("unfind", "收到消息：---------" + activityMsg.getTid() + "---------"
							+ activityMsg.getMsg());
					if (activityMsg.getTid() == 53) {// 主播开启连麦
						IS_LIANMAI = true;
						try {
							JSONObject json = new JSONObject(activityMsg.getMsg());
							if (json.has("stream_name")) {
								rightStream = json.getString("stream_name");
							}
						}
						catch (JSONException e) {
							e.printStackTrace();
						}
						openLianMai();// 开启连麦
					}
					if (activityMsg.getTid() == 54) {// 主播关闭连麦
						// Log.e("unfind", "关闭连麦：" + activityMsg.getMsg());
						IS_LIANMAI = false;
						closeLianMai();// 关闭连麦
					}

					if (activityMsg.getTid() == 14) {// 寄语消息
						mRunwayManager.handleMessage(m.getMessage());// 跑马灯显示寄语消息
						if (loginUserId.equals(m.getMessage().getT_id())) {
							// 调用查询金币方法
							UpdateBeans();
						}
						break;
					}
					if (activityMsg.getTid() == 3) {
						boolean flag = sharedPreferences.getString(APP.IS_LOGIN, "").equals("true");
						if (!flag) {
							return;
						}

						String msgBody = activityMsg.getMsg();
						try {
							JSONObject object = new JSONObject(msgBody);

							int giftid = object.getInt("gift_id");

							if (giftid == 1742 || giftid == 1743 || giftid == 1741) {
								SocketMommonStart();
							}

						}
						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					if (activityMsg.getTid() == 230) {
						// Log.i("lvjian", "activityMsg---中奖消息--230--msg---->"
						// + activityMsg.getMsg());
					}

					// 主播上播
					if (activityMsg.getTid() == 27) {

						isAnchorLiving = true;
						leftMediaViewCon.setVisibility(View.VISIBLE);// 显示播放器
						yinpinmoshi_tv.setVisibility(View.GONE);// 隐藏提示
						mImageView_ctvlogo.setVisibility(View.VISIBLE);
						String msgBody = activityMsg.getMsg();
						try {
							JSONObject object = new JSONObject(msgBody);
							leftlive_url = object.getString("watchPull");
							leftStream = object.getString("stream_name");
							IS_LIANMAI = false;// 主播上播的时候，默认的是没有开启连麦的
							handler.sendEmptyMessage(1121);// 播放视频
						}
						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// 主播下播
					if (activityMsg.getTid() == 28) {
						isAnchorLiving = false;
						leftMediaViewCon.setVisibility(View.GONE);
						rightMediaViewCon.setVisibility(View.GONE);
						// yinpinmoshi_tv.setVisibility(0);
						// yinpinmoshi_tv.setText("主播休息了！");
						mImageView_ctvlogo.setVisibility(View.INVISIBLE);
						GetAnchorNews();
					}
					// 广播
					if (activityMsg.getTid() == 36) {
						down_mRunwayManager.handleMessage(m.getMessage());
						if (timer_run_bottom != null) {
							timer_run_bottom.cancel();
						}
						timerruntextbottom();
						return;
					}
					// tid==42和tid==50（是系统消息和抽魔幻卡牌中奖的消息）
					if (activityMsg.getTid() == 42 || activityMsg.getTid() == 50) {
						mRunwayManager.handleMessage(m.getMessage());// 显示系统消息
						// 设置礼物显示定时器
						// if (timer_run_top != null) {
						// timer_run_top.cancel();
						// }
						// timerruntexttop();
						return;
					}

					// 被踢出房间
					if (activityMsg.getTid() == 4) {
						// 如果提的人是用户，就退出直播间
						if (!Utils.isEmpty(m.getMessage().getContent())
								&& "踢".equals(m.getMessage().getContent())) {
							if (leftController != null) {
								leftController.stop();
							}
							if (rightController != null) {
								rightController.stop();
							}
							String name = m.getMessage().getSay();
							repeatlogin_dialog(name);
						}
					}
					// 账号重复登录
					if (activityMsg.getTid() == 10) {
						if (leftController != null) {
							leftController.stop();
						}
						if (rightController != null) {
							rightController.stop();
						}
						repeatlogin_dialog("账号重复登录！");
					}
					// 不为空，并且不为礼物，聊天
					if (m.getMessage() != null) {
						if (m.getMessage().getTid() == 33) {// 礼物消息
							// 如果是幸运礼物中奖了，就刷新
							// Log.i("lvjian","-----消息-中奖内容-------->"+m.getMessage().getTname());
							if (!Utils.isEmpty(m.getMessage().getTname())) {
								messages.add(m.getMessage());
								chatadapter.notifyDataSetChanged();
							}

							if (giftmessages.size() == 0) {
								giftmessages.add(m.getMessage());
								if (chat_gift_item == null) {
									break;
								}
								if (chat_gift_item.getChildAt(0) != null) {
									chatGiftAdapter
											.AppearAnimPosition(chat_gift_item.getChildAt(0));
								}
							}
							else if (giftmessages.size() == 1) {
								// 如果是同一人就移除旧的直接加载新的
								if (giftmessages.get(0).getSname()
										.equals(m.getMessage().getSname())) {
									giftmessages.remove(0);
									giftmessages.add(m.getMessage());
								}
								else {
									giftmessages.add(m.getMessage());
									if (chat_gift_item.getChildAt(1) != null) {
										chatGiftAdapter.AppearAnimPosition(chat_gift_item
												.getChildAt(1));
									}
								}
							}
							else if (giftmessages.size() == 2) {
								// 如果有两个用户刷，新来的用户是其中之一，直接覆盖，如果不是其中之一移除第一个，添加到最后
								if (giftmessages.get(0).getSname()
										.equals(m.getMessage().getSname())) {
									// 名字相同，礼物也相同就累计（替换这个位置的）
									if (giftmessages.get(0).getGift_id()
											.equals(m.getMessage().getGift_id())) {
										giftmessages.set(0, m.getMessage());
									}
									else {
										giftmessages.remove(0);
										giftmessages.add(m.getMessage());
										chatGiftAdapter.AppearAnimPosition(chat_gift_item
												.getChildAt(1));
									}

								}
								else if (giftmessages.get(1).getSname()
										.equals(m.getMessage().getSname())) {
									if (giftmessages.get(1).getGift_id()
											.equals(m.getMessage().getGift_id())) {
										giftmessages.set(1, m.getMessage());
									}
								}
								else {
									giftmessages.remove(0);
									giftmessages.add(m.getMessage());
									chatGiftAdapter
											.AppearAnimPosition(chat_gift_item.getChildAt(1));
								}
							}
							chat_gift_item.setVisibility(0);
							// 设置礼物显示定时器
							if (timer != null) {
								timer.cancel();
							}
							timer();
							// // 设置他的高度
							// if (giftmessages.size() <= 3) {
							// RelativeLayout.LayoutParams layoutparams =
							// (RelativeLayout.LayoutParams) chat_gift_item
							// .getLayoutParams();
							// layoutparams.height = layoutparams.WRAP_CONTENT;
							// chat_gift_item.setLayoutParams(layoutparams);
							// } else {
							// RelativeLayout.LayoutParams layoutparams =
							// (RelativeLayout.LayoutParams) chat_gift_item
							// .getLayoutParams();
							// int a = Utils.dip2px(LiveRoomActivity.this, 100.0F);
							// layoutparams.height = a;
							// chat_gift_item.setLayoutParams(layoutparams);
							// }
							chatGiftAdapter.notifyDataSetChanged();
							/*
							 * if (chat_gift_item != null) { chat_gift_item .setTranscriptMode (ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
							 * chat_gift_item.setCacheColorHint(0); }
							 */
						}
						else {
							// 私聊
							if (activityMsg.getTid() == 1) {
								primessages.add(m.getMessage());
								pri_chatadapter.notifyDataSetChanged();
								if (pri_edit_list != null) {
									pri_edit_list
											.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
									pri_edit_list.setCacheColorHint(0);
								}
								if (pri_chat_listview != null) {
									pri_chat_listview
											.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
									pri_chat_listview.setCacheColorHint(0);
								}
							}
							else {
								messages.add(m.getMessage());
								chatadapter.notifyDataSetChanged();
								if (pub_chat_listview != null) {
									pub_chat_listview
											.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
									pub_chat_listview.setCacheColorHint(0);
								}
								if (edit_listview != null) {
									edit_listview
											.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
									edit_listview.setCacheColorHint(0);
								}
							}
						}
					}
					break;
				case HandlerCmd.HandlerCmd_GetRoomInfoException:
					Utils.showToast(LiveRoomActivity.this, "获取房间信息失败");
					break;
				case HandlerCmd.HandlerCmd_GetRoomInfoFailed:
					String m1 = "";
					m1 = msg.obj.toString();
					repeatlogin_dialog(m1);
					if ("您的账号已过期，请重新登陆！".equals(m1)) {
						loginOut();
					}
					break;
				// socket异常，断开连接（断线重连）
				case HandlerCmd.HandlerCmd_RoomChatClientException:
					SocketStart();
					GetShouhu();
					break;
				case HandlerCmd.HandlerCmd_RoomMommonClientException:
					break;
				// 快速刷礼物
				case 9:
					Utils.showToast(LiveRoomActivity.this, msg.obj.toString());
					break;
				case 10:
					Animation push_left_out = AnimationUtils.loadAnimation(context,
							R.anim.pust_left_out);
					chat_gift_item.startAnimation(push_left_out);
					push_left_out.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							giftmessages.clear();
							chatGiftAdapter.clearMap();
							chat_gift_item.setVisibility(8);
						}
					});
					break;
				// 获取禁言列表成功
				case 11:
					String us_id = sharedPreferences.getString(APP.USER_ID, "");
					if (shut_up_list.size() > 0) {
						for (int i = 0; i < shut_up_list.size(); i++) {
							if (shut_up_list.get(i).equals(us_id)) {
								is_shutup = false;
							}
						}
					}
					break;
				// 获取禁言列表失败
				case 12:
					Log.i("lvjian", "-----获取禁言列表失败-------");
					break;
				// 关闭下面的滚动条
				case 142:
					down_runwayTextView.setVisibility(8);
					break;
				case 143:
					runwayTextView.setVisibility(8);
					break;
				case 20:
					yurenjiezhongjiang(msg.obj.toString(), 0);
					break;
				case 21:
					yurenjiezhongjiang(msg.obj.toString(), 1);
					break;
				case 22:
					yurenjiezhongjiang(msg.obj.toString(), 2);
					break;
				case 23:
					yurenjiezhongjiang(msg.obj.toString(), 3);
					break;
				case 24:
					yurenjiezhongjiang(msg.obj.toString(), 4);
					break;
				default:
					break;
			}
		};
	};

	/**
	 * 更新乐币
	 */
	private void UpdateBeans() {
		Runnable updatebeansrun = new Runnable() {
			public void run() {
				try {
					String result = Utils.getuserinfo(loginUserId,
							sharedPreferences.getString(APP.SECRET, ""));
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						String beans = data.getString("beans");
						Editor editor = sharedPreferences.edit();
						editor.putString(APP.BEANS, beans);
						editor.commit();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(updatebeansrun);
	}

	/**
	 * 异步任务，插入历史观看数据
	 * 
	 * @author Administrator
	 * 
	 */
	class MyTask extends AsyncTask<Void, Void, Void> {
		// 准备处理
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			MyOpenHelper openHelper = new MyOpenHelper(LiveRoomActivity.this);
			db = openHelper.getWritableDatabase();
		}

		// 开始处理
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String time = Utils.getCurrrenDate();
			Historys his = new Historys(APP.POST_URL_ROOT + anchor_icon, anchor_name,
					anchor_received_level, time, anchor_id);
			insertData(his);
			return null;
		}

		// 处理结束
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			closeDataBase();
		}
	}

	// 插入历史观看记录
	private long insertData(Historys his) {
		// 之前有该主播观看记录，先删除记录再加入最新记录
		db.delete(APP.TABLE_NAME, "hisRoom=?", new String[] { his.getRoom_id() });
		ContentValues values = new ContentValues();
		values.put("hisImg", his.getImg_url());
		values.put("hisName", his.getName());
		values.put("hisLv", his.getLv_url());
		values.put("hisTime", his.getTime());
		values.put("hisRoom", his.getRoom_id());
		return db.insert(APP.TABLE_NAME, null, values);
	}

	// 关闭数据库
	private void closeDataBase() {
		if (null != db) {
			db.close();
		}
	}

	// 关闭视频播放器
	private void stopMediaPlayer() {
		if (leftController != null) {
			leftController.stop();
		}
		if (rightController != null) {
			rightController.stop();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isPause = true;
		MobclickAgent.onPause(this);

		if (mPlaceFragment != null && isMommonStart) {
			mPlaceFragment.doPause();
		}

		// 如果是关闭finish
		if (isFinishing()) {
			GlobalData.getInstance(context).mJumpFormAnchorCenter = false;
			SoundManager.getIntance().stopDropMusic();
			stopMediaPlayer();// 停止播放器
			if (client != null) {
				client.disconnect();
				client = null;
			}
			if (mommonclient != null) {
				mommonclient.disconnect();
				mommonclient = null;
			}

			if (editmanager != null) {
				editmanager.exit();
			}
			if (giftmanager != null) {
				giftmanager.exit();
			}
			if (anchormanager != null) {
				anchormanager.exit();
			}
			if (rankListManager != null) {
				rankListManager.exit();
			}
			if (shouHuManager != null) {
				shouHuManager.exit();
			}
			if (chooseSongManager != null) {
				chooseSongManager.exit();
			}
			// AnchorZhuboFragment.closeGetRedBean();
			// 关闭定时器
			if (timer != null) {
				timer.cancel();
			}
			if (timer_run_bottom != null) {
				timer_run_bottom.cancel();
			}
			if (timer_run_top != null) {
				timer_run_top.cancel();
			}
			if (timershuaping != null) {
				timershuaping.cancel();
			}

			// 关闭财神相关
			if (mPlaceFragment != null) {
				mPlaceFragment.doGameOver();
				showMommonView(false);
				showMommonCountView(false);
			}

		}
		else {
			// if ((this.mMediaPlayer != null) && (this.mMediaPlayer.isPlay()))
			// {
			// this.mMediaPlayer.stopPlay();
			// }
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		String roomidFormAnchorCenter = GlobalData.getInstance(context).mCurRoomIdFormAnchorCenter;
		// 当前房间id 和 跳转来的id 是否相同
		if (!Utils.isEmpty(roomidFormAnchorCenter) && !roomidFormAnchorCenter.equals(room_id)
				&& GlobalData.getInstance(context).mJumpFormAnchorCenter) {
			GlobalData.getInstance(context).mJumpFormAnchorCenter = false;
			room_id = roomidFormAnchorCenter;
			GlobalData.getInstance(context).mCurRoomIdFormAnchorCenter = "";
			handler.sendEmptyMessage(HandlerCmd.HandlerCmd_CallChangeRoom);
		}
		isPause = false;
		MobclickAgent.onResume(this);
		if (mPlaceFragment != null && isMommonStart) {
			mPlaceFragment.doResume();
		}
		if (isAnchorLiving) {
			play();// 播放视频(这是防止从其他界面再次进入该界面的时候要重新播放视频)
		}

		// 如果用户登录成功，重新加载房间数据
		if (!is_login && sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
			if (Utils.isEmpty(room_id)) {
				Utils.showToast(LiveRoomActivity.this, "房间不存在！");
			}
			else {

				GetRoomInfo();
			}
			// 设置用户图像
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				String url = sharedPreferences.getString(APP.USER_ICON, "");
				mImageLoader.displayImage(url, input_icon, mOptions);
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mRunwayManager != null) {
			mRunwayManager.recyle();// 回收跑马灯的消息队列资源
		}

		super.onDestroy();
		setContentView(R.layout.activity_null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_changeroom: {
				// 换房间
				getRandomAnchor();
			}
				break;
			case R.id.room_back:
				finish();
				break;
			// 点击关注按钮
			case R.id.room_guanzhu:
				// 缺用户是否登录
				if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
					if (is_follow == 0) {
						FollowAnchor();
					}
				}
				else {
					Utils.recharge(LiveRoomActivity.this);
				}
				break;
			// 输入框
			case R.id.input_et:
				if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
					intpu_layout.setVisibility(8);
					SiLiao siliaoaa = new SiLiao();
					editmanager.showLiveInputView("0", siliaoaa);
					edit_listview = editmanager.getEditListView();
					edit_listview.setAdapter(chatadapter);
					// 私聊
					pri_edit_list = editmanager.getPri_edit_list();
					pri_edit_list.setAdapter(pri_chatadapter);
				}
				else {
					Utils.recharge(LiveRoomActivity.this);
				}
				break;
			// 礼物
			case R.id.gift_icon:
				// 显示礼物列表
				if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
					giftmanager.showgiftView(1, anchor_id, anchor_name);
					intpu_layout.setVisibility(8);
				}
				else {
					Utils.recharge(LiveRoomActivity.this);
				}
				break;
			// 主播信息
			case R.id.input_icon:
				if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
					anchormanager.showanchorView();
					setGuideResId(3);
				}
				else {
					Utils.recharge(LiveRoomActivity.this);
				}
				break;
			// 点击显示返回和设置等按钮
			// case R.id.video_father:
			// if (button_is_show) {
			// video_more_button.setVisibility(0);
			// room_back.setVisibility(0);
			// button_is_show = false;
			// } else {
			// video_more_button.setVisibility(8);
			// room_back.setVisibility(8);
			// button_is_show = true;
			// }
			// break;
			case R.id.video_setting:
				break;
			case R.id.video_shengyin:
				if (is_Audiomode) {
					// rl_video.setVisibility(View.VISIBLE);
					leftMediaViewCon.setVisibility(View.VISIBLE);
					rightMediaViewCon.setVisibility(View.VISIBLE);
					yinpinmoshi_tv.setVisibility(8);
					yinpinmoshi_tv.setText("音频模式");
					is_Audiomode = false;
				}
				else {
					// rl_video.setVisibility(View.INVISIBLE);
					leftMediaViewCon.setVisibility(View.INVISIBLE);
					rightMediaViewCon.setVisibility(View.INVISIBLE);
					is_Audiomode = true;
					yinpinmoshi_tv.setVisibility(0);
				}
				break;
			case R.id.video_big:// 全屏
				if (is_fullscreen) {// 如果是全屏，则退出全屏
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置竖屏
					intpu_layout.setVisibility(View.VISIBLE);// 显示聊天输入框
					video_stop.setVisibility(View.VISIBLE);// 显示停止按钮
					video_share.setVisibility(View.VISIBLE);
					
					if(!useShare){
						video_share.setVisibility(View.GONE);
					}
					
					if (!Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTNUM, ""))
							&& !Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTPRICE, ""))
							&& !Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTID, ""))
							&& !Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTNUM, ""))
							&& !Utils.isEmpty(sharedPreferences.getString(APP.FAST_GIFTICON, ""))) {
						gift_button.setVisibility(View.VISIBLE);
						fastsend_gift_num
								.setText(sharedPreferences.getString(APP.FAST_GIFTNUM, ""));
						mImageLoader.displayImage(
								APP.GIFT_PATH + sharedPreferences.getString(APP.FAST_GIFTICON, ""),
								fastsend_gift_img, mOptions);
					}
					video_shengyin.setVisibility(View.VISIBLE);// 显示音频模式按钮
					video_big.setImageResource(R.drawable.living_video_big);// 设置全屏模式按钮
					room_back.setImageResource(R.drawable.living_video_back);// 返回
					is_fullscreen = false;
					setVideoViewSize();// 设置播放器尺寸
					LayoutParams flp = videoFather.getLayoutParams();
					flp.height = screenWidth * 3 / 4;
					videoFather.setLayoutParams(flp);

					LayoutParams allLp = allPlayerCon.getLayoutParams();
					flp.height = screenWidth * 3 / 4;
					allPlayerCon.setLayoutParams(allLp);

					try {
						LayoutParams leftConLp = leftMediaViewCon.getLayoutParams();
						leftConLp.height = screenWidth * 3 / 4;
						leftMediaViewCon.setLayoutParams(leftConLp);
						if (leftController != null) {
							leftController.setSize(screenWidth, screenWidth * 3 / 4);
						}

						if (IS_LIANMAI) {
							LayoutParams rightConLp = rightMediaViewCon.getLayoutParams();
							rightConLp.height = screenWidth * 3 / 4;
							rightMediaViewCon.setLayoutParams(rightConLp);
							if (rightController != null) {
								rightController.setSize(screenWidth, screenWidth * 3 / 4);
							}

						}

					}
					catch (NotRelativeLayoutException e) {
						e.printStackTrace();
					}
				}
				else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置横屏
					getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
					intpu_layout.setVisibility(View.GONE);// 隐藏聊天输入框
					gift_button.setVisibility(View.GONE);// 隐藏礼物按钮
					video_stop.setVisibility(View.GONE);// 隐藏停止按钮
					video_share.setVisibility(View.GONE);
					video_shengyin.setVisibility(View.GONE);// 隐藏音频模式按钮

					video_big.setImageResource(R.drawable.living_video_small);// 全屏按钮的图片换为退出全屏的图片样式
					room_back.setImageResource(R.drawable.living_video_back);
					if (IS_LIANMAI) {
						ViewGroup.LayoutParams leftLp = leftPlayerBg.getLayoutParams();
						ViewGroup.LayoutParams rightLp = rightPlayerBg.getLayoutParams();
						leftLp.height = screenWidth;
						rightLp.height = screenWidth;
					}
					else {
						ViewGroup.LayoutParams leftLp = leftPlayerBg.getLayoutParams();
						leftLp.height = screenWidth;
					}
					is_fullscreen = true;
					setVideoSizeByFullScreen();// 全屏状态下设置播放器尺寸

					isFullScreened = true;// 代表开启过全屏
				}
				break;
			case R.id.video_stop:
				MainTabActivity.SetAnchorNull();
				finish();
				break;
			case R.id.video_share:// 分享
				callShare();
				break;
			// 刷礼物快捷图标
			case R.id.gift_button:
				if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
					// 判断用户的钱够不够
					String beans = sharedPreferences.getString(APP.BEANS, "0");
					String num = sharedPreferences.getString(APP.FAST_GIFTNUM, "");
					String price = sharedPreferences.getString(APP.FAST_GIFTPRICE, "");
					BigDecimal user_beans = null;
					int giftnum = 0;
					int giftprice = 0;
					if (!Utils.isEmpty(beans)) {
						// user_beans = ConsumpUtil.parseBigDecimal(beans);
					}
					if (!Utils.isEmpty(num)) {
						giftnum = Integer.parseInt(num);
					}
					if (!Utils.isEmpty(price)) {
						giftprice = Integer.parseInt(price);
					}

					boolean canBuy = false;
					if (ConsumpUtil.compare(beans, (giftnum * giftprice) + "")) {
						canBuy = true;
					}

					String uid = sharedPreferences.getString(APP.FAST_GIFTID, "");
					if ("1704".equals(uid) || "1705".equals(uid) || "1702".equals(uid)) {
						if (is_guard == 1) {
							// 判断显示刷礼物按钮
							if (canBuy) {
								FastSendGift();
							}
							else {
								// 用户钱不够
								Utils.Moneynotenough(context, "余额不足!", room_id);
							}
						}
						else {
							tequanliwu("您不是守护，不能赠专属礼物！");
						}
					}
					else {
						// 判断显示刷礼物按钮
						if (canBuy) {
							FastSendGift();
						}
						else {
							// 用户钱不够
							Utils.Moneynotenough(context, "余额不足!", room_id);
						}
					}

				}
				else {
					Utils.recharge(LiveRoomActivity.this);
				}

				break;
			default:
				break;
		}
	}

	/**
	 * 
	 * 获取红豆和关注数量
	 */
	private void get_hearts_and_fans_num() {
		Runnable heartsandfansrun = new Runnable() {
			@Override
			public void run() {
				try {
					String roominfo = Utils.getheartsandfansnum(anchor_id);
					JSONObject room_info = new JSONObject(roominfo);
					int state = room_info.getInt("s");
					JSONObject date = room_info.getJSONObject("data");
					if (state == 1) {
						guanzhu_num = date.getString("fans");
						handler.sendEmptyMessage(GETGUANZHUN_NUM_OK);
					}
					else {
						handler.sendEmptyMessage(GETGUANZHUN_NUM_ERROR);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
					// 异常处理
					handler.sendEmptyMessage(GETGUANZHUN_NUM_ERROR);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(heartsandfansrun);
	}

	/**
	 * 
	 * 获取房间信息
	 */
	private void GetRoomInfo() {

		RpcRoutine.getInstance().addRpc(RpcEvent.GetRoomInfo, handler, LiveRoomActivity.this,
				sharedPreferences.getString(APP.USER_ID, ""),
				sharedPreferences.getString(APP.SECRET, ""), room_id);
	}

	/**
	 * 关注主播
	 */
	private void FollowAnchor() {
		Runnable guanzhurun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.follow(sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""), anchor_id);
					JSONObject data = new JSONObject(result);
					int state = data.getInt("s");
					if (state == 1) {
						guanzhu_num = data.getInt("sum") + "";
						handler.sendEmptyMessage(GUANZHU_OK);
					}
					else {
						handler.sendEmptyMessage(GUANZHU_ERROR);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(GUANZHU_ERROR);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(guanzhurun);
	}

	// 连接聊天服务器
	public void SocketStart() {
		Runnable socketstartrun = new Runnable() {
			@Override
			public void run() {
				try {
					// 连接socket服务器
					if (client != null) {
						client.disconnect();
						client = null;
					}
					client = new BaseClient();
					// client.start(chat_url, port, handler);
					Log.e("unfind", "消息URL:" + chat_url + "-----" + port);
					client.start(chat_url, port, handler, GET_MSG,
							HandlerCmd.HandlerCmd_RoomChatClientException);
					JsonObject data = new JsonObject();
					data.addProperty("userid", openid);
					data.addProperty("roomid", room_id);
					data.addProperty("timestamp", timestamp + "");
					data.addProperty("openkey", openkey);
					data.addProperty("clienttype", 1);
					String message = data.toString();
					byte[] request = Utils.getBytes(message, GET_MSG);
					// byte[] request = Utils.getBytes(message, 7);
					client.sendmsg(request);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(socketstartrun);
	}

	/**
	 * 返回键的监听
	 */
	// 退出时间
	private long currentBackPressedTime = 0;
	// 退出间隔
	private static final int BACK_PRESSED_INTERVAL = 2000;

	@Override
	public void onBackPressed() {

			if (editmanager != null && editmanager.getshowing()) {
				editmanager.goneliveInputview();
				return;
			}
			if (giftmanager != null && giftmanager.getshowing()) {
				giftmanager.gonegiftview();
				return;
			}
			if (anchormanager != null && anchormanager.getshowing()) {
				anchormanager.goneanchortview();
				return;
			}
			if (rankListManager != null && rankListManager.getshowing()) {
				rankListManager.goneranklistview();
				return;
			}
			if (chooseSongManager != null && chooseSongManager.isIsshowing()) {

				chooseSongManager.close();
				return;
			}
			if (shouHuManager != null && shouHuManager.getshowing()) {
				shouHuManager.goneshouhulistview();
				return;
			}

			if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
				currentBackPressedTime = System.currentTimeMillis();
				Toast.makeText(this, "再按一次退出房间", Toast.LENGTH_SHORT).show();
			}
			else {

			this.finish();
		}

	}

	/**
	 * 发送消息
	 * 
	 * @param text
	 */
	public static void sendmsg(String text, EditText edittext, String chat_type, SiLiao siliao) {
		// 用户没有登录
		if (!sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
			Utils.recharge(context);
			return;
		}
		if (Utils.isEmpty(text)) {
			Utils.showToast(context, "说话不能为空哦");
			return;
		}
		user_cost_level = sharedPreferences.getString(APP.USER_CLEVEL, "");
		int cl = 0;
		if (!Utils.isEmpty(user_cost_level)) {
			cl = Integer.parseInt(user_cost_level);
		}
		if (!is_shutup) {
			Utils.showToast(context, "您被禁言了！");
			return;
		}
		// 做个时间定时器 防止刷屏
		if (shuaping) {
			shuaping = false;
			timershuaping();
		}
		else {
			Utils.showToast(context, "您的发言过于频繁！");
			return;
		}
		// 区分私聊和公聊
		if ("1".equals(chat_type)) {
			if (cl < 3) {
				Utils.showToast(context, "三富以下不能私聊！");
				return;
			}
			JsonObject chat = new JsonObject();
			chat.addProperty("t_stealth", "0");
			chat.addProperty("s_stealth", "0");
			chat.addProperty("vip_lv", sharedPreferences.getInt(APP.VIPLV, 0));
			chat.addProperty("g_level", g_lv);
			chat.addProperty("g_type", g_type);
			chat.addProperty("sname", sharedPreferences.getString(APP.NICKNAME, ""));
			chat.addProperty("suid", sharedPreferences.getString(APP.USER_ID, ""));
			if (siliao != null) {
				chat.addProperty("tuid", siliao.getId());
				chat.addProperty("tname", siliao.getName());
			}
			else {
				chat.addProperty("tuid", anchor_id);
				chat.addProperty("tname", anchor_name);
			}

			chat.addProperty("tid", chat_type);
			chat.addProperty("text", text);
			String hello = chat.toString();
			byte[] send = Utils.getBytes(hello, 1);
			edittext.setText("");
			edittext.clearFocus();
			// 发送消息
			client.sendmsg(send);
		}
		else {

			// if (cl < 1 && text.length() > 7) {
			// Utils.showToast(context, "一富不能超过7个字哦!");
			// return;
			// }
			if (text.length() > 30) {
				Utils.showToast(context, "您一次说的话太多了!");
				return;
			}
			JsonObject chat = new JsonObject();
			chat.addProperty("t_stealth", "0");
			chat.addProperty("s_stealth", "0");
			chat.addProperty("vip_lv", sharedPreferences.getInt(APP.VIPLV, 0));
			chat.addProperty("g_level", g_lv);
			chat.addProperty("g_type", g_type);
			chat.addProperty("sname", sharedPreferences.getString(APP.NICKNAME, ""));
			chat.addProperty("suid", sharedPreferences.getString(APP.USER_ID, ""));
			if (siliao != null) {
				chat.addProperty("tuid", siliao.getId());
				chat.addProperty("tname", siliao.getName());
			}
			else {
				chat.addProperty("tuid", "");
				chat.addProperty("tname", "所有人");
			}

			chat.addProperty("tid", chat_type);
			chat.addProperty("text", text);
			String hello = chat.toString();
			byte[] send = Utils.getBytes(hello, 2);
			edittext.setText("");
			edittext.clearFocus();
			// 发送消息
			client.sendmsg(send);
		}

	}

	/**
	 * 赠送红豆的动画
	 */
	public static void startdonghua() {
		final ImageView hondou = new ImageView(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
		hondou.setLayoutParams(params);
		hondou.setImageResource(R.drawable.red_bean);
		liveroomview.addView(hondou);
		TranslateAnimation animation = new TranslateAnimation(screenWidth, (screenWidth * 3 / 8),
				screenHeight, (screenWidth * 1 / 4));
		ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		AlphaAnimation alphaGlow = new AlphaAnimation(0, 1);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(alphaGlow);
		set.addAnimation(scaleAnimation);
		set.addAnimation(animation);
		// 设置动画时间 (作用到每个动画)
		set.setDuration(1000);
		set.setFillAfter(true);
		hondou.startAnimation(set);
		set.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				hondou.clearAnimation();
				hondou.setVisibility(8);
				ScaleAnimation scale = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
						Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(400);
				scale.setFillAfter(true);
				red_addone.setVisibility(0);
				red_addone.startAnimation(scale);
				scale.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						red_addone.clearAnimation();
						red_addone.setVisibility(8);
					}
				});
			}
		});
	}

	/**
	 * 显示礼物
	 * 
	 */
	public static void showgift(String anchor_id, String anchor_name, int canku) {
		if (giftmanager != null) {
			if (canku == 1) {
				giftmanager.showgiftView(3, anchor_id, anchor_name);
			}
			else {
				giftmanager.showgiftView(2, anchor_id, anchor_name);
			}
		}
	}

	public static void showfanlist() {
		if (rankListManager != null) {
			rankListManager.showranklistView();
		}
	}

	public static void showshouhulist() {
		if (shouHuManager != null) {
			shouHuManager.showshouhulistView();
		}
	}

	// 显示点歌 add by zhongxf 2016年4月8日10:52:54
	public static void showChooseSong() {
		if (chooseSongManager != null) {
			chooseSongManager.showViews();
		}
	}

	/**
	 * 横竖屏切换
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 退出全屏
	 */
	private void quitFullScreen() {
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * 快速刷礼物
	 * 
	 * @author lvjian
	 * 
	 */
	private void FastSendGift() {
		Runnable fastsendgiftrun = new Runnable() {
			@Override
			public void run() {
				try {
					String user_id = sharedPreferences.getString(APP.USER_ID, "");
					String secret = sharedPreferences.getString(APP.SECRET, "");
					String gift_id = sharedPreferences.getString(APP.FAST_GIFTID, "");
					String gift_num = sharedPreferences.getString(APP.FAST_GIFTNUM, "");
					String result = Utils.send_present(user_id, secret, anchor_id, gift_id,
							gift_num + "", room_id, "0", is_guard);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						String beans = data.getString("beans");
						Editor editor = sharedPreferences.edit();
						editor.putString(APP.BEANS, beans);
						// 愚人节整蛊活动中奖消息解析
						JSONObject aprilfool_info = data.getJSONObject("aprilfool_info");
						JSONArray sgift = aprilfool_info.getJSONArray("sgift");
						if (sgift.length() > 0) {
							if (sgift.length() == 1) {
								JSONObject one = sgift.getJSONObject(0);
								String name = one.getString("name");
								Log.i("lvjian", "==============愚人节活动===============");
								if ("大智若愚(徽章)".equals(name)) {
									Message m1 = new Message();
									m1.what = 21;
									m1.obj = "获得" + name;
									handler.sendMessage(m1);
								}
								else if ("老爷车座驾（3天）".equals(name)) {
									Message m2 = new Message();
									m2.what = 22;
									m2.obj = "获得" + name;
									handler.sendMessage(m2);
								}
								else {
									Message m3 = new Message();
									m3.what = 23;
									m3.obj = "获得" + name;
									handler.sendMessage(m3);
								}

							}
							else {
								String name_total = "";
								for (int i = 0; i < sgift.length(); i++) {
									JSONObject one1 = sgift.getJSONObject(i);
									name_total = name_total + "," + one1.getString("name");
								}
								Message m4 = new Message();
								m4.what = 24;
								m4.obj = "获得" + name_total;
								handler.sendMessage(m4);
							}
						}
						JSONArray draw = aprilfool_info.getJSONArray("draw");
						if (draw.length() > 0) {
							JSONObject yaoshi = draw.getJSONObject(0);
							int num = yaoshi.getInt("num");
							Message m5 = new Message();
							m5.what = 20;
							m5.obj = "获得宝箱钥匙 X " + num;
							handler.sendMessage(m5);
						}
						// // 保存快速刷礼物
						// editor.putString(APP.FAST_GIFTID, gift_id);
						// editor.putString(APP.FAST_GIFTNUM, gift_num);
						// editor.putString(APP.FAST_GIFTPRICE,
						// sharedPreferences
						// .getString(APP.FAST_GIFTPRICE, ""));
						editor.commit();
					}
					else {
						Message msg = new Message();
						msg.what = 9;
						msg.obj = obj.getString("data");
						handler.sendMessage(msg);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
					Log.i("lvjian", "---------------------------发送礼物异常--------------------------");
					Message msg = new Message();
					msg.what = 9;
					msg.obj = "发送礼物异常";
					handler.sendMessage(msg);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(fastsendgiftrun);
	}

	/**
	 * 显示公聊
	 */
	public static void setcurrentpubchat() {
		if (roomTabAdapter != null && viewPager != null) {
			viewPager.setCurrentItem(0);
		}
	}

	// 第一种方法：设定指定任务task在指定时间time执行 schedule(TimerTask task, Date time)
	public void timer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				System.out.println("----------------设定要指定任务------------------");
				handler.sendEmptyMessage(10);
			}
		}, 4000);// 设定指定的时间time,此处为5000毫秒
	}

	// 下面滚动消息消失
	public void timerruntextbottom() {
		down_runwayTextView.setVisibility(0);
		timer_run_bottom = new Timer();
		timer_run_bottom.schedule(new TimerTask() {
			public void run() {
				handler.sendEmptyMessage(142);
			}
		}, 12000);// 设定指定的时间time,此处为6000毫秒
	}

	// 上面滚动消息消失
	public void timerruntexttop() {
		runwayTextView.setVisibility(0);
		timer_run_top = new Timer();
		timer_run_top.schedule(new TimerTask() {
			public void run() {
				handler.sendEmptyMessage(143);
			}
		}, 12000);// 设定指定的时间time,此处为6000毫秒
	}
	
	public static void timershuaping() {
		timershuaping = new Timer();
		timershuaping.schedule(new TimerTask() {
			public void run() {
				mhandler.sendEmptyMessage(1);
			}
		}, 3000);// 设定指定的时间time,此处为5000毫秒
	}

	private static Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					shuaping = true;
					timershuaping.cancel();
					break;
		
				default:
					break;
			}
		};
	};

	/**
	 * 获取禁言列表
	 * 
	 * @author Jon_V
	 * 
	 */
	private void Get_Shut_Up_List() {
		Runnable getshutuprun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.get_shut_up_list(
							sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""), room_id);
					JSONObject obj = new JSONObject(result);
					int state = obj.getInt("s");
					if (state == 1) {
						JSONArray data = obj.getJSONArray("data");
						if (data.length() > 0) {
							for (int i = 0; i < data.length(); i++) {
								String item = (String)data.get(i);
								String a = item.substring(0, item.indexOf("|"));
								shut_up_list.add(a);
							}
							handler.sendEmptyMessage(11);
						}
					}
					else {
						handler.sendEmptyMessage(12);
					}
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(12);
				}

			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getshutuprun);
	}
	
	/**
	 * 账号重复登录
	 * 
	 * @param msg
	 */
	private void repeatlogin_dialog(String text) {
		final Dialog dialog_re = new AlertDialog.Builder(this).create();
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.repeatlogin_dialog,
				null));
		Button releat_login_queding = (Button)localWindow.findViewById(R.id.releat_login_queding);
		TextView moneynotenoth_title = (TextView)localWindow.findViewById(R.id.moneynotenoth_title);
		moneynotenoth_title.setText(text);
		releat_login_queding.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				dialog_re.cancel();
				LiveRoomActivity.this.finish();
			}
		});
		dialog_re.setCancelable(false);
	}
	/**
	 * 没有在线的主播
	 * 
	 * @param msg
	 */
	private void noLivingAnchor_dialog(String text) {
		final Dialog dialog_re = new AlertDialog.Builder(this).create();
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.repeatlogin_dialog,
				null));
		Button releat_login_queding = (Button)localWindow.findViewById(R.id.releat_login_queding);
		TextView moneynotenoth_title = (TextView)localWindow.findViewById(R.id.moneynotenoth_title);
		moneynotenoth_title.setText(text);
		releat_login_queding.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				dialog_re.cancel();
			}
		});
	}

	/**
	 * 对某某私聊
	 * 
	 */
	public static void showprichat(final SiLiao siliao) {
		final Dialog dialog_re = new AlertDialog.Builder(context).create();
		Window window = dialog_re.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setWindowAnimations(R.style.windowAnimstyle);
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(context).inflate(R.layout.siliao_dialog,
				null));
		TextView a_n = (TextView)localWindow.findViewById(R.id.a_n);
		TextView s_btn = (TextView)localWindow.findViewById(R.id.s_btn);
		a_n.setText(siliao.getName());
		// 和某某某私聊
		s_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_re.cancel();
				if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
					intpu_layout.setVisibility(8);
					editmanager.showLiveInputView("1", siliao);
					edit_listview = editmanager.getEditListView();
					edit_listview.setAdapter(chatadapter);
					// 私聊
					pri_edit_list = editmanager.getPri_edit_list();
					pri_edit_list.setAdapter(pri_chatadapter);
				}
				else {
					Utils.recharge(context);
				}
			}
		});

	}

	/**
	 * 让其不再保存Fragment的状态
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// super.onSaveInstanceState(outState);
	}

	/**
	 * 滑动屏幕调节音量
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			if (!checkIsShowing()) {
				return false;
			}

			if (e1 == null || e2 == null) {
				return false;
			}

			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int)e2.getRawY();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();
			if (mOldY < (3 * windowWidth) / 4) {
				onVolumeSlide((mOldY - y) / windowHeight);
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0) {
				mVolume = 0;
			}
		}
		int index = (int)(percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume) {
			index = mMaxVolume;
		}
		else if (index < 0) {
			index = 0;
		}
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
				AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				endGesture();
				break;
			case MotionEvent.ACTION_DOWN:
				// if(checkIsShowing()){
				if (button_is_show) {
					video_more_button.setVisibility(0);
					room_back.setVisibility(0);
					button_is_show = false;
				}
				else {
					video_more_button.setVisibility(8);
					room_back.setVisibility(8);
					button_is_show = true;
				}
				// }

				break;
			default:
				break;
		}
		return super.onTouchEvent(event);
	}

	private void endGesture() {
		mVolume = -1;
	}

	/**
	 * 守护的详细信息
	 * 
	 * @param msg
	 */
	public static void shoushouhudialog(Guard guard) {
		final Dialog shouhudialog = new AlertDialog.Builder(context).create();
		shouhudialog.show();
		Window localWindow = shouhudialog.getWindow();
		localWindow.setContentView(LayoutInflater.from(context).inflate(
				R.layout.shouhu_dialog_layout, null));
		TextView shenyutianshu = (TextView)localWindow.findViewById(R.id.shenyutianshu);
		CircularImage guard_item_icon_tanchuang = (CircularImage)localWindow
				.findViewById(R.id.guard_item_icon_tanchuang);
		ImageView shouhu_user_caifu = (ImageView)localWindow.findViewById(R.id.shouhu_user_caifu);
		ImageView shouhu_user_shouhu = (ImageView)localWindow.findViewById(R.id.shouhu_user_shouhu);
		TextView shouhu_item_name = (TextView)localWindow.findViewById(R.id.shouhu_item_name);
		shenyutianshu.setText("还有" + guard.getDays_remain() + "天到期");
		shouhu_item_name.setText(guard.getNickname());
		// 设置财富等级图片
		APP.setCost_level(guard.getCost_level(), shouhu_user_caifu, context);
		// 设置守护等级
		String str = APP.USER_BIG_LOGO_ROOT + guard.getIcon();
		mImageLoader.displayImage(str, guard_item_icon_tanchuang, mOptions);
		if ("1".equals(guard.getIs_year())) {
			// 年守护
			if ("1".equals(guard.getType())) {
				// 银守护
				shouhu_user_shouhu.setImageResource(APP.parseIsYearSilverGuardId(guard.getLevel()));
				;
			}
			else {
				// 金守护
				shouhu_user_shouhu.setImageResource(APP.parseIsYearGoldGuardId(guard.getLevel()));
			}
		}
		else {
			// 年守护
			if ("1".equals(guard.getType())) {
				// 银守护
				shouhu_user_shouhu.setImageResource(APP.parseSilverGuardId(guard.getLevel()));
//				shouhu_user_shouhu.setImageResource(APP.parseIsYearSilverGuardId(guard.getLevel()));
			}
			else {
				// 金守护
				shouhu_user_shouhu.setImageResource(APP.parseGoldGuardId(guard.getLevel()));
//				shouhu_user_shouhu.setImageResource(APP.parseIsYearGoldGuardId(guard.getLevel()));
			}
		}
	}

	public static void RushDataSet(int position) {
		giftmessages.remove(position);
		chatGiftAdapter.notifyDataSetChanged();
	}

	private void GetShouhu() {
		Runnable getguardandviprun = new Runnable() {
			@Override
			public void run() {
				try {
					String user_id = sharedPreferences.getString(APP.USER_ID, "");
					String secret = sharedPreferences.getString(APP.SECRET, "");
					String result = Utils.getvipandguard(user_id, secret, anchor_id, room_id);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						// 判断是否是守护
						if (data.has("guard")) {
							JSONObject userinfo = data.getJSONObject("userinfo");
							JSONObject guard = data.getJSONObject("guard");
							// guard_expire = guard.getString("expire");
							// exp = guard.getString("exp");
							// max_exp = guard.getString("max_exp");
							g_lv = guard.getString("lv");
							g_type = guard.getString("type");
							// guard_lv = guard.getString("lv");
							// type = guard.getString("type");
						}
						else {
						}
					}
					else {
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getguardandviprun);
	}

	/**
	 * 注销登录
	 */
	private void loginOut() {
		Runnable outloginrun = new Runnable() {
			public void run() {
				String s = Utils.outlogin();
				try {
					JSONObject jsonObject = new JSONObject(s);
					int i = jsonObject.getInt("s");
					if (i == 1) {
						JSONObject data = jsonObject.getJSONObject("data");
						String id = data.getString("id");
						String secret = jsonObject.getString("secret");
						String nickname = data.getString("nickname");
						String icon = data.getString("icon");
						JSONObject auth = jsonObject.getJSONObject("auth");
						String timestamp = auth.getString("timestamp");
						String openkey = auth.getString("openkey");
						SharedPreferences preferences = getSharedPreferences(APP.MY_SP,
								Context.MODE_PRIVATE);
						Editor editor = preferences.edit();
						editor.putString(APP.IS_LOGIN, "false");
						editor.putString(APP.USER_ID, id);
						editor.putString(APP.USER_ICON, icon);
						editor.putString(APP.SECRET, secret);
						editor.putString(APP.TIMESTAMP, timestamp);
						editor.putString(APP.OPENKEY, openkey);
						editor.putString(APP.NICKNAME, nickname);
						editor.commit();
					}
					else {
					}
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(outloginrun);
	}

	private void tequanliwu(String text) {
		final Dialog dialog_re = new AlertDialog.Builder(context).create();
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(context).inflate(
				R.layout.repeatlogin_dialog, null));
		Button releat_login_queding = (Button)localWindow.findViewById(R.id.releat_login_queding);
		TextView moneynotenoth_title = (TextView)localWindow.findViewById(R.id.moneynotenoth_title);
		moneynotenoth_title.setText(text);
		releat_login_queding.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				dialog_re.cancel();
			}
		});
	}

	boolean mMommonShow;

	@SuppressLint("NewApi")
	public void showMommonView(boolean flag) {
		if (mPlaceFragment == null) {
			return;
		}

		if (flag) {
			if (!mMommonShow) {
				isMommonStart = true;
				mMommonShow = true;
				getFragmentManager().beginTransaction().add(R.id.liveroomview, mPlaceFragment)
						.commit();

			}
		}
		else {
			if (mMommonShow) {
				mMommonShow = false;
				// mPlaceFragment.doFinishTime();
				getFragmentManager().beginTransaction().remove(mPlaceFragment).commit();
			}

		}
	}

	@SuppressLint("NewApi")
	public void showMommonCountView(boolean flag) {
		if (mMommonCountFragment == null) {
			return;
		}
		if (flag) {
			getFragmentManager().beginTransaction().add(R.id.liveroomview, mMommonCountFragment)
					.commit();
		}
		else {
			getFragmentManager().beginTransaction().remove(mMommonCountFragment).commit();
		}

	}

	private Handler mommomHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case HANDLE_MOMMOM_CONNECT: {
					ActivityMsg activityMsg = (ActivityMsg)msg.obj;

					if (activityMsg.getTid() == S_GET_MOMMON_CNNECT) {
						if (mPlaceFragment == null) {
							mPlaceFragment = new MommonManageFragment(LiveRoomActivity.this);
						}
						if (mMommonCountFragment == null) {
							mMommonCountFragment = new MommonCountFragment(LiveRoomActivity.this);
						}

					}
					else if (activityMsg.getTid() == S_GET_MOMMON_START) {
						showMommonView(true);
					}
					else if (activityMsg.getTid() == S_GET_MOMMON_CLICK) {
						String msgBody = activityMsg.getMsg();
						JSONObject object;
						try {
							object = new JSONObject(msgBody);
							int money = object.getInt("money");
							int id = object.getInt("id");
							if (money == -1) {
								money = 0;
							}
							mPlaceFragment.showAddMOney(money, id);
						}
						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else if (activityMsg.getTid() == S_GET_MOMMON_OVER) {
						String msgBody = activityMsg.getMsg();
						JSONObject object;
						try {
							object = new JSONObject(msgBody);
							int money = object.getInt("money");

							String beans = sharedPreferences.getString(APP.BEANS, "");
							Editor editor = sharedPreferences.edit();
							editor.putString(APP.BEANS, money + "");
							editor.commit();
							if (mPlaceFragment != null) {
								mMommonCountFragment.setCurMoney(mPlaceFragment.curCash);
								mPlaceFragment.doGameOver();
								showMommonView(false);
								showMommonCountView(true);
							}
							else {
								Log.i("sjf", "果然出现了");
							}

							isMommonStart = false;
						}
						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (mommonclient != null) {
							mommonclient.disconnect();
							mommonclient = null;
						}
					}

				}
					break;

			}
		}
	};

	// 连接财神服务器
	public void SocketMommonStart() {
		Runnable socketstartrun = new Runnable() {
			@Override
			public void run() {
				try {

					// 连接socket服务器
					if (mommonclient != null) {
						mommonclient.disconnect();
						mommonclient = null;
					}
					mommonclient = new BaseClient();
					// mommonclient.start(mMommonUrl, mMommonPort,
					// mommomHandler);
					mommonclient.start(mMommonUrl, mMommonPort, mommomHandler,
							HANDLE_MOMMOM_CONNECT, HandlerCmd.HandlerCmd_RoomMommonClientException);
					// mommonclient.start("192.168.1.102", 5588, mommomHandler);
					JsonObject data = new JsonObject();
					data.addProperty("userid", openid);
					data.addProperty("roomid", room_id);
					data.addProperty("timestamp", timestamp + "");
					data.addProperty("openkey", openkey);
					data.addProperty("clienttype", 1);
					String message = data.toString();
					byte[] request = Utils.getBytes(message, C_MOMMON_CNNECT);
					mommonclient.sendmsg(request);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(socketstartrun);
	}

	// 连接财神服务器
	public void SocketMommonClickMoney(final int id) {
		Runnable socketClick = new Runnable() {
			@Override
			public void run() {
				try {
					// 连接socket服务器
					if (mommonclient != null) {
						JsonObject data = new JsonObject();
						data.addProperty("type", 1);
						data.addProperty("id", id);
						data.addProperty("roomid", room_id);
						String message = data.toString();
						byte[] request = Utils.getBytes(message, C_MOMMON_CLICK);
						mommonclient.sendmsg(request);
					}
					// ThreadPoolWrap.getThreadPool().removeTask(this);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(socketClick);
	}

	private void yurenjiezhongjiang(String text, int nn) {
		final Dialog dialog_re1 = new AlertDialog.Builder(context).create();
		dialog_re1.show();
		Window localWindow = dialog_re1.getWindow();
		localWindow.setContentView(LayoutInflater.from(context).inflate(R.layout.yurenjie_dialog,
				null));
		ImageView yrj_close = (ImageView)localWindow.findViewById(R.id.yrj_close);
		TextView yrj_content = (TextView)localWindow.findViewById(R.id.yrj_content);
		ImageView yrj_img = (ImageView)localWindow.findViewById(R.id.yrj_img);

		yrj_content.setText(text);
		yrj_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_re1.cancel();
			}
		});
		switch (nn) {
			case 0:
				yrj_img.setImageResource(R.drawable.yr_yaoshi);
				break;
			case 1:
				yrj_img.setImageResource(R.drawable.yr_huizhang);
				break;
			case 2:
				yrj_img.setImageResource(R.drawable.yr_lyc);
				break;
			case 3:
				yrj_img.setImageResource(R.drawable.yr_ql);
				break;
			case 4:
				yrj_img.setImageResource(R.drawable.yr_libao);
				break;

			default:
				break;
		}

	}

	@SuppressWarnings("unused")
	private void GetAnchorNews() {

		// if(true){
		// return ;
		// }

		Runnable getguardandviprun = new Runnable() {
			@Override
			public void run() {
				try {
					String user_id = sharedPreferences.getString(APP.USER_ID, "");
					String secret = sharedPreferences.getString(APP.SECRET, "");
					String result = Utils.addRpcEvent(RpcEvent.GetPhotoWall, user_id, secret,
							anchor_id, 1, 5);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					GlobalData.getInstance(LiveRoomActivity.this).mAnchorNewsInfo.clear();
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						String path = data.getString("image_url");
						int count = data.getInt("count");

						JSONArray photoData = data.getJSONArray("photos");

						for (int i = 0; i < photoData.length(); i++) {
							JSONObject photoInfo = photoData.getJSONObject(i);
							String headUrl = APP.USER_LOGO_ROOT + anchor_headicon;
							String urlPath = photoInfo.getString("uri");
							String photeUrl = APP.POST_URL_HEAD + path + "big_" + urlPath;
							int id = photoInfo.getInt("id");
							boolean isLiked = false;
							if ((photoInfo.getInt("liked") == 1)) {
								isLiked = true;
							}
							else {
								isLiked = false;
							}
							int likeCount = photoInfo.getInt("like_num");

							AnchorNews oneNews = new AnchorNews();
							oneNews.setmHeadIconUrl(headUrl);
							oneNews.setmPhotoUrl(photeUrl);
							oneNews.setmId(id);
							oneNews.setmLikeCount(likeCount);
							oneNews.setmHasLiked(isLiked);
							GlobalData.getInstance(LiveRoomActivity.this).mAnchorNewsInfo
									.add(oneNews);

						}
						handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetAnchorNewsSuccess);
					}
					else {
						handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetAnchorNewsFailed);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetAnchorNewsFailed);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getguardandviprun);
	}

	private boolean checkIsShowing() {

		return isPlaying;
	}

	/**
	 * 获取随机的主播
	 */
	private void getRandomAnchor() {
		showAnchorNews(false);
		mBt_changeRoom.setEnabled(false);
		Runnable getrandomuserrun = new Runnable() {
			public void run() {
				String s = Utils.getrandomuser();
				try {
					JSONObject jsonObject = new JSONObject(s);
					int i = jsonObject.getInt("s");
					if (i == 1) {
						JSONArray data = jsonObject.getJSONArray("data");
						Gson gson = new Gson();
						JSONObject js = data.getJSONObject(0);
						AnchorInfo anchorInfo = gson.fromJson(js.toString(), AnchorInfo.class);
						room_id = anchorInfo.getId();
						handler.sendEmptyMessage(HandlerCmd.HandlerCmd_CallChangeRoom);
					}
					else {
						handler.sendEmptyMessage(HandlerCmd.HandlerCmd_NoLivingAnchor);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(HandlerCmd.HandlerCmd_NoLivingAnchor);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getrandomuserrun);
	}

	/**
	 * 
	 * 描述：通过roomid获取房间最新数据
	 */
	private void getNewRoomInfoAndUrl() {
		getRoomSourceFlag();
		getResultUrl();
		GetRoomInfo();
	}

	/**
	 * 
	 * 描述：通过roomid获取房间最新数据 换房间的时候 清除房间数据
	 */
	private void doChangeRoom() {
		if (messages.size() > 0) {
			messages.clear();
			chatadapter.notifyDataSetChanged();
		}
		if (primessages.size() > 0) {
			primessages.clear();
			pri_chatadapter.notifyDataSetChanged();
		}
		getNewRoomInfoAndUrl();
	}

	/**
	 * 
	 * 描述：获取视频源
	 */
	private void getRoomSourceFlag() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					String result = Utils.addRpcEvent(RpcEvent.GetStreamDir, room_id);
					JSONObject jsonObject = new JSONObject(result);
					int i = jsonObject.getInt("s");
					if (i == 1) {
						int flag = jsonObject.getInt("data");
						Message msg = new Message();
						msg.what = HandlerCmd.HandlerCmd_GetStreamSuccess;
						msg.arg1 = flag;
						handler.sendMessage(msg);
					}
					else {
						handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetStreamFailed);
					}
				}
				catch (Exception e) {
					// TODO: handle exception
					handler.sendEmptyMessage(HandlerCmd.HandlerCmd_GetStreamFailed);
				}
				ThreadPoolWrap.getThreadPool().removeTask(this);
			}
		};

		ThreadPoolWrap.getThreadPool().executeTask(runnable);
	}

	private void callShare() {
		GlobalData dataGd = GlobalData.getInstance(LiveRoomActivity.this);
		String name = dataGd.getmRoomInfo().name;
		String url_poster = dataGd.getmRoomInfo().url_poster;
		String url_touch = dataGd.getmRoomInfo().url_touch;
		String text_share = dataGd.getmRoomInfo().text_share;
		UMengShare.callShare(LiveRoomActivity.this, name, text_share, url_poster, url_touch);
	}
}
