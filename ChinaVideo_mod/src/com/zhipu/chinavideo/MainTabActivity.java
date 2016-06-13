package com.zhipu.chinavideo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.adapter.TopOnLineAdapter;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.fragment.DiscoveryFragment;
import com.zhipu.chinavideo.fragment.LiveHallFragment;
import com.zhipu.chinavideo.fragment.PersonalCenterFragment;
import com.zhipu.chinavideo.fragment.RankListFragment;
import com.zhipu.chinavideo.fragment.TextFragment;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.BasicActivityfirst;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.GetRedBeansUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;
import com.zhipu.chinavideo.wxapi.WXEntryActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainTabActivity extends BasicActivityfirst implements
		OnClickListener {
	private Class[] fragmentArray;
	private String[] mTextviewArray;
	private int[] mImageViewArray;
	public static MainTabActivity activity = null;
	private LayoutInflater layoutInflater;
	private String tag;
	private LinearLayout takeLookLayout;
	private FragmentTabHost mTabHost;
	private boolean isTypeChoicePopShown;
	private static SharedPreferences preferences;
	private static AnchorInfo anchorInfo;
	private static AnchorInfo Radomanchor_from;
	private static CircularImage suibian_icon;
	public static ImageLoader mImageLoader = null;
	public static DisplayImageOptions mOptions;
	// 退出时间
	private long currentBackPressedTime = 0;
	// 退出间隔
	private static final int BACK_PRESSED_INTERVAL = 2000;
	private static ImageView anchoronline_close;
	private static TextView anchor_online_other;
	private static ViewStub anchoronlinePop;
	private static View anchoronlineView;
	private static Animation anchor_showAnim;
	private static Animation anchor_goneAnim;
	private static List<AnchorInfo> anchorsonline;
	private static View top_online_loading;
	private static ListView anchor_online_listview;
	private static RelativeLayout no_online_layout;
	private static TextView online_num;
	private static RelativeLayout top_online_title;
	private static TextView sbkk_tv;
	private View shouye_loading;
	private int screenWidth = 0;
	private String from_type = "0";
	private String room_id = "888888";
	private static ImageView suibiankankan_center;
//	private RelativeLayout rl_bg_1, rl_bg_2, rl_bg_3, rl_bg_4, rl_bg_5,
//			rl_bg_6, rl_bg_7;
//	private ImageView icon_bg_1, icon_bg_2, icon_bg_3, icon_bg_4, icon_bg_5,
//			icon_bg_6, icon_bg_7;
	
	public RelativeLayout[] rl_bg;
	public ImageView[] icon_bg;
	
	
	private Dialog dialog_re;
	private boolean isqiandao = false;
	// 签到
	LinearLayout qiandao_btn;
	// 个人中心签到
	private boolean qd_grzx = false;
	// 签到
	public static final String qiandao = "qd";
	private TextView tv_wyqd;

	boolean showUpdate ;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		activity = this;
		requestWindowFeature(1);
		setContentView(R.layout.maintab_activity);
		Intent intent = this.getIntent();
		room_id = intent.getStringExtra("room_id");
		from_type = intent.getStringExtra("type");
		suibian_icon = (CircularImage) findViewById(R.id.kankan_image);
		suibiankankan_center = (ImageView) findViewById(R.id.suibiankankan_center);
		IntentFilter filter = new IntentFilter(qiandao);
		registerReceiver(broadcastReceiver1, filter);
		shouye_loading = findViewById(R.id.shouye_loading);
		shouye_loading.setVisibility(View.GONE);
		suibian_icon.setOnClickListener(activity);
		anchorInfo = new AnchorInfo();
		Radomanchor_from = new AnchorInfo();
		initView();
		ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(
				activity).threadPoolSize(1).memoryCache(new WeakMemoryCache())
				.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(localImageLoaderConfiguration);
		mOptions = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showStubImage(R.drawable.haibao_loading)
				.showImageForEmptyUri(R.drawable.haibao_loading)
				.showImageOnFail(R.drawable.haibao_loading)
				.showImageOnLoading(R.drawable.haibao_loading).cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		getScreenWH();
		if (!Utils.isNetworkAvailble(activity)) {
			final AlertDialog localAlertDialog = new AlertDialog.Builder(
					activity).create();
			localAlertDialog.show();
			Window localWindow = localAlertDialog.getWindow();
			localWindow.setContentView(LayoutInflater.from(this).inflate(
					R.layout.network_dialog, null));
			WindowManager.LayoutParams layoutParams = localAlertDialog
					.getWindow().getAttributes();
			int x = (3 * screenWidth / 4);
			layoutParams.width = x;
			layoutParams.height = LayoutParams.WRAP_CONTENT;
			localAlertDialog.getWindow().setAttributes(layoutParams);
			TextView localTextView2 = (TextView) localWindow
					.findViewById(R.id.network_dialog_text);
			Button localButton2 = (Button) localWindow
					.findViewById(R.id.network_dialog_button);
			localTextView2.setText("网络异常,请检查后重新尝试!");
			localTextView2.setVisibility(0);
			localTextView2.setTextSize(15);
			localButton2.setText("确定");
			localButton2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					localAlertDialog.cancel();
					finish();
				}
			});
		} else {
			// 获取用户登录信息
			getUserData();
			// 获取随机的主播
			getRandomAnchor();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		this.layoutInflater = LayoutInflater.from(this);
		this.mTabHost = ((FragmentTabHost) findViewById(R.id.tabhost));
		this.takeLookLayout = ((LinearLayout) findViewById(R.id.suibiankankan));
		this.takeLookLayout
				.setBackgroundResource(R.drawable.maintab_suibiankankan_bg);
		sbkk_tv = (TextView) findViewById(R.id.sbkk_tv);
		this.mTabHost.setup(this, getSupportFragmentManager(),
				R.id.realtabcontent);
		mTabHost.getTabWidget().setDividerDrawable(null);
		mTabHost.getTabWidget().setBackgroundResource(
				R.drawable.bottom_background);
		int i = this.fragmentArray.length;
		for (int j = 0; j < 5; j++) {
			TabHost.TabSpec localTabSpec = this.mTabHost.newTabSpec(
					this.mTextviewArray[j]).setIndicator(getTabItemView(j));
			this.mTabHost.addTab(localTabSpec, this.fragmentArray[j], null);
			if (j == 0) {
				this.tag = localTabSpec.getTag();
			}
		}
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
	}

	public boolean isTypeChoicePopShown() {
		return this.isTypeChoicePopShown;
	}

	public MainTabActivity() {
		Class[] arrayOfClass = new Class[5];
		arrayOfClass[0] = LiveHallFragment.class;
		arrayOfClass[1] = RankListFragment.class;
		arrayOfClass[2] = TextFragment.class;
		arrayOfClass[3] = DiscoveryFragment.class;
		arrayOfClass[4] = PersonalCenterFragment.class;
		this.fragmentArray = arrayOfClass;
		int[] arrayOfInt = new int[5];
		arrayOfInt[0] = R.drawable.maintab_livehall_btn;
		arrayOfInt[1] = R.drawable.maintab_subscribe_btn;
		arrayOfInt[2] = R.drawable.maintab_suibiankankan_bg;
		arrayOfInt[3] = R.drawable.maintab_discovery_btn;
		arrayOfInt[4] = R.drawable.maintab_personalcenter_btn;
		this.mImageViewArray = arrayOfInt;
		String[] arrayOfString = new String[5];
		arrayOfString[0] = "大厅";
		arrayOfString[1] = "排行";
		arrayOfString[2] = "随便看看";
		arrayOfString[3] = "发现";
		arrayOfString[4] = "我";
		this.mTextviewArray = arrayOfString;
		this.isTypeChoicePopShown = false;
	}

	private View getTabItemView(int paramInt) {
		View localView = this.layoutInflater.inflate(
				R.layout.maintab_item_view, null);
		ImageView localImageView = (ImageView) localView
				.findViewById(R.id.imageview);
		localImageView.setImageResource(this.mImageViewArray[paramInt]);
		TextView localTextView = (TextView) localView
				.findViewById(R.id.textview);
		localTextView.setText(this.mTextviewArray[paramInt]);
		localTextView.setTextColor(getResources().getColorStateList(
				R.drawable.maintab_text_color));
		if (paramInt == 2) {
			localView.setBackgroundResource(R.color.white);
			localView.setPadding(30, 0, 30, 0);
			localImageView.setVisibility(4);
			localTextView.setVisibility(4);
			localView.setVisibility(0);
		} else {
			localView.setBackgroundResource(R.color.white);
		}
		if (paramInt == 0)
			localView.setPadding(20, 0, 0, 0);
		if (paramInt == 1)
			localView.setPadding(0, 0, 20, 0);
		if (paramInt == 3)
			localView.setPadding(20, 0, 0, 0);
		if (paramInt == 4)
			localView.setPadding(0, 0, 20, 0);
		return localView;
	}

	public void setTypeChoicePopShown(boolean paramBoolean) {
		this.isTypeChoicePopShown = paramBoolean;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
//		if(showUpdate){
//			return ;
//		}
		
		if (this.isTypeChoicePopShown) {
			((LiveHallFragment) getSupportFragmentManager().findFragmentByTag(
					this.tag)).outTypeChoicePopView();
		} else {
			if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
				currentBackPressedTime = System.currentTimeMillis();
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			} else {
				// 退出
				super.onBackPressed();
			}
		}
	}

	/**
	 * 如果用户之前登录过，直接登录，重新获取用户信息，如果用户没有登录过，作为游客登录，获取用户信息
	 */
	private void getUserData() {
		String deviceToken = preferences.getString(APP.DEVICETOKEN, "");
		String openid = preferences.getString(APP.OPEN_ID, "");
		String token = preferences.getString(APP.TOKEN, "");
		String user = preferences.getString(APP.USER, "");
		String pass = preferences.getString(APP.PASS, "");
		if (preferences.getString(APP.IS_LOGIN, "").equals("true")) {
			// 自动登录
			login("m_user_login", user, pass, deviceToken);
		}
		// 游客登录
		else {
			touristlogin();
		}
	}

	/**
	 * 用户登录接口
	 */
	private void login(final String name, final String user_name,
			final String password, final String deviceToken) {
		Runnable loginrun = new Runnable() {
			public void run() {
				String s = Utils.userself_login(name, user_name, password,
						deviceToken);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int a = jsonObject.getInt("s");
					if (a == 1) {
						JSONObject data = jsonObject.getJSONObject("data");
						String id = data.getString("id");
						String secret = jsonObject.getString("secret");
						String user_name = data.getString("username");
						String password = data.getString("password");
						String usertype = data.getString("user_type");
						String gender = data.getString("gender");
						String pos = data.getString("pos");
						String phone = data.getString("phone");
						Editor editor = preferences.edit();
						editor.putString(APP.IS_LOGIN, "true");
						editor.putString(APP.USER_ID, id);
						editor.putString(APP.SECRET, secret);
						editor.putString(APP.USER, user_name);
						editor.putString(APP.PASS, password);
						editor.putString(APP.PHONE, phone);
						editor.putString(APP.GENDER, gender);
						editor.putString(APP.POS, pos);
						
						editor.putString(APP.USER_TYPE, usertype);
						editor.commit();
						handler.sendEmptyMessage(3);
						// 获取用户信息
						getUserinfo(id, secret);
						// 签到
//						IsSignToday(id, secret);
						qd_grzx = false;
					} else {
						handler.sendEmptyMessage(4);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(loginrun);
	}

	/**
	 * 游客登录
	 */
	private void touristlogin() {
		Runnable touristloginrun = new Runnable() {
			public void run() {
				String s = Utils.tourist_login("pc_guest_login");
				try {
					JSONObject result = new JSONObject(s);
					int i = result.getInt("s");
					if (i == 1) {
						JSONObject data = result.getJSONObject("data");

						String id = data.getString("id");

						String secret = result.getString("secret");

						String nickname = data.getString("nickname");

						JSONObject auth = result.getJSONObject("auth");

						String timestamp = auth.getString("timestamp");
						String openkey = auth.getString("openkey");
						Editor editor = preferences.edit();

						editor.putString(APP.USER_ID, id);

						editor.putString(APP.SECRET, secret);

						editor.putString(APP.TIMESTAMP, timestamp);

						editor.putString(APP.OPENKEY, openkey);

						editor.putString(APP.NICKNAME, nickname);

						editor.commit();
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
		ThreadPoolWrap.getThreadPool().executeTask(touristloginrun);
	}

	/**
	 * 获取用户详细信息
	 * 
	 * @param id
	 * @param key
	 */
	private void getUserinfo(final String id, final String key) {
		// TODO Auto-generated method stub
		Runnable getuserinforun = new Runnable() {
			public void run() {
				String s = Utils.getuserinfo(id, key);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int i = jsonObject.getInt("s");
					if (i == 1) {
						JSONObject data = jsonObject.getJSONObject("data");
						String beans = data.getString("beans");
						String rlevel = data.getString("rlevel");
						String clevel = data.getString("clevel");
						String icon = data.getString("icon");
						String nickname = data.getString("nickname");
						String openkey = data.getString("openkey");
						String shouchong = data.getString("shouchong");
						String time = data.getString("timestamp");
						String cost_beans = data.getString("cost_beans");
						String openid = data.getString("openid");
						// String phone = data.getString("phone");
						String received_beans = data
								.getString("received_beans");
						int viplv = data.getInt("vip_lv");
						int is_stealth = data.getInt("is_stealth");
						Editor editor = preferences.edit();
						editor.putString(APP.BEANS, beans);
						editor.putString(APP.USER_ICON, APP.USER_LOGO_ROOT
								+ icon);
						editor.putString(APP.USER_RLEVEL, rlevel);
						editor.putString(APP.USER_CLEVEL, clevel);
						editor.putString(APP.NICKNAME, nickname);
						editor.putString(APP.OPENKEY, openkey);
						editor.putString(APP.SHOUCHONG, shouchong);
						editor.putString(APP.TIMESTAMP, time);
						// editor.putString(APP.PHONE, phone);
						editor.putString(APP.COST_BEANS, cost_beans);
						editor.putString(APP.RECEIVED_BEANS, received_beans);
						editor.putString(APP.OPEN_ID, openid);
						editor.putInt(APP.VIPLV, viplv);
						editor.putInt(APP.ISSTEALTH, is_stealth);
						editor.commit();
						handler.sendEmptyMessage(5);
					} else {
						handler.sendEmptyMessage(6);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(6);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getuserinforun);
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Log.i("lvjian", "游客登录成功");
				shouye_loading.setVisibility(8);
				if ("1".equals(from_type)) {
					Intent in = new Intent(MainTabActivity.this,
							LiveRoomActivity.class);
					in.putExtra("room_id", room_id);
					startActivity(in);
				}
				break;
			case 2:
				Log.i("lvjian", "游客登录失败或异常");
				shouye_loading.setVisibility(8);
				Utils.showToast(activity, "网络较差,请检查后重新尝试");
				break;
			case 3:
				shouye_loading.setVisibility(8);
				Log.i("lvjian", "用户登录成功");
				break;
			case 4:
				Log.i("lvjian", "用户登录失败或异常");
				shouye_loading.setVisibility(8);
				Utils.showToast(activity, "网络较差,请检查后重新尝试");
				break;
			case 5:
				if ("1".equals(from_type)) {
					Intent in = new Intent(MainTabActivity.this,
							LiveRoomActivity.class);
					in.putExtra("room_id", room_id);
					startActivity(in);
				}
				Log.i("lvjian", "获取用户信息成功");
				break;
			case 6:
				shouye_loading.setVisibility(8);
				Log.i("lvjian", "获取用户信息失败或异常");
				Utils.showToast(activity, "网络较差,请检查后重新尝试");
				break;
			case 7:
				Radomanchor_from = anchorInfo;

				mImageLoader.displayImage(
						APP.POST_URL_ROOT + anchorInfo.getPoster_url(),
						suibian_icon, mOptions, null);
				break;
			case 8:
				Log.i("lvjian", "获取随机主播失败或异常");
				break;
			// 获取我的关注列表
			case 9:
				break;
			// 获取我的关注列表
			case 10:
				break;
			// 签到信息获取成功
			case 11:
				String m = msg.obj.toString();
				showDownloadDialog(m);
				break;
			case 12:
				// 已经签到过了
				String m1 = msg.obj.toString();
				// showDownloadDialog(m1);
				if (qd_grzx) {
					Log.i("lvjian", "------个人签到------");
					if (dialog_re != null) {
						changeView(m1);
						dialog_re.show();
					} else {
						showDownloadDialog(m1);
					}

				} else {
					if (isqiandao) {
						changeView(m1);
					}
				}

				if (qiandao_btn != null) {
					qiandao_btn.setBackgroundColor(MainTabActivity.this
							.getResources().getColor(R.color.qianhui));
					tv_wyqd.setText("今日已签到");
				}
				Log.i("lvjian", "----------今天已经签到过了------------");
				break;
			case 13:
				if (qiandao_btn != null) {
					tv_wyqd.setText("今日已签到");
					qiandao_btn.setBackgroundColor(MainTabActivity.this
							.getResources().getColor(R.color.qianhui));
				}
				IsSignToday(preferences.getString(APP.USER_ID, ""),
						preferences.getString(APP.SECRET, ""));
				Utils.showToast(MainTabActivity.this, "签到成功！");
				break;
			case 14:
				// Utils.showToast(MainTabActivity.this, "签到失败！");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 获取随机的主播
	 */
	private static void getRandomAnchor() {
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
						anchorInfo = gson.fromJson(js.toString(),
								AnchorInfo.class);
						mhandler.sendEmptyMessage(7);
					} else {
						mhandler.sendEmptyMessage(8);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mhandler.sendEmptyMessage(8);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getrandomuserrun);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.kankan_image:
			if (anchorInfo != null) {
				GlobalData.getInstance(activity).mJumpFormAnchorCenter = false ;
				Intent localIntent = new Intent(activity,
						LiveRoomActivity.class);
				localIntent.putExtra("room_id", anchorInfo.getId());
				activity.startActivity(localIntent);
			} else {
				if (Radomanchor_from != null) {
					Intent localIntent = new Intent(activity,
							LiveRoomActivity.class);
					localIntent.putExtra("room_id", Radomanchor_from.getId());
					activity.startActivity(localIntent);
				} else {
					Utils.showToast(activity, "当前没有主播");
				}

			}
			break;
		default:
			break;
		}
	}

	public static void initanchoronline() {
		initOnlinePop();
		if (anchor_showAnim == null) {
			anchor_showAnim = AnimationUtils.loadAnimation(activity,
					R.anim.title_enter);
		}
		anchoronlinePop.startAnimation(anchor_showAnim);
		anchoronlinePop.setVisibility(0);
	}

	public static void outanchoronlineview() {
		if (anchor_goneAnim == null) {
			anchor_goneAnim = AnimationUtils.loadAnimation(activity,
					R.anim.title_enter);
		}
		anchoronlinePop.startAnimation(anchor_goneAnim);
		anchoronlinePop.setVisibility(8);

	}

	public static void initOnlinePop() {
		if (anchoronlineView == null) {
			anchoronlinePop = ((ViewStub) activity
					.findViewById(R.id.anchor_onlineview));
			anchoronlinePop.setLayoutResource(R.layout.hall_anchoronline_view);
			anchoronlineView = activity.anchoronlinePop.inflate();
			anchoronline_close = (ImageView) anchoronlineView
					.findViewById(R.id.anchor_online_close);
			anchor_online_other = (TextView) anchoronlineView
					.findViewById(R.id.anchor_online_other);
			top_online_loading = anchoronlineView
					.findViewById(R.id.top_online_loading);
			anchor_online_listview = (ListView) anchoronlineView
					.findViewById(R.id.anchor_online_listview);
			no_online_layout = (RelativeLayout) anchoronlineView
					.findViewById(R.id.no_online_layout);
			online_num = (TextView) anchoronlineView
					.findViewById(R.id.online_num);

			top_online_title = (RelativeLayout) anchoronlineView
					.findViewById(R.id.top_online_title);
			top_online_title.setOnClickListener(activity);
			top_online_loading.setVisibility(0);
			anchor_online_other.getBackground().setAlpha(100);
			anchoronline_close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					outanchoronlineview();
				}

			});
			anchor_online_other.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					outanchoronlineview();
				}

			});
		}
		SpannableString styledText = setTextColor("正在直播 ", "0", "位");
		online_num.setText(styledText);
		getmyguanzhu();
	}

	private static SpannableString setTextColor(String str1, String str2,
			String str3) {
		String s = str1 + str2 + str3;
		SpannableString styledText = new SpannableString(s);
		styledText.setSpan(new TextAppearanceSpan(activity, R.style.style0),
				str1.length(), (str1.length() + str2.length()),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}

	/**
	 * 登录签到
	 */
	private void SignToday(final String user, final String secr) {
		// TODO Auto-generated method stub
		Runnable signtodayrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.qiandaotoday(user, secr);
					JSONObject obj = new JSONObject(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(signtodayrun);
	}

	/**
	 * 是否签到，获取签到信息
	 */
	public void IsSignToday(final String user, final String secr) {
		// TODO Auto-generated method stub
		Runnable issigntodayrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.getqiandaomsg(user, secr);
					Log.i("lvjian", "是否签到--------------------------------》"
							+ result);
					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("data");
					int today_status = data.getInt("today_status");
					String days = data.getString("days");
					if (today_status == 0) {
						Message me = new Message();
						me.what = 11;
						me.obj = days;
						handler.sendMessage(me);
					} else {
						Message lala = new Message();
						lala.what = 12;
						lala.obj = days;
						handler.sendMessage(lala);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(issigntodayrun);
	}

	/**
	 * 获取我的关注列表
	 * 
	 * @param user_id
	 * @param crumb
	 */
	public static void getmyguanzhu() {
		anchorsonline = new ArrayList<AnchorInfo>();
		Runnable getmyguardrun = new Runnable() {
			public void run() {
				String s = Utils.getsubscribelist(
						preferences.getString(APP.USER_ID, ""),
						preferences.getString(APP.SECRET, ""));
				try {
					JSONObject jsonObject = new JSONObject(s);
					int a = jsonObject.getInt("s");
					if (a == 1) {
						JSONArray ja = jsonObject.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							if ("1".equals(ja.getJSONObject(i).getString(
									"status"))) {
								Gson gson = new Gson();
								AnchorInfo an = gson.fromJson(
										ja.getJSONObject(i).toString(),
										AnchorInfo.class);
								anchorsonline.add(an);
							}
						}
						mhandler.sendEmptyMessage(1);
					} else {
						mhandler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					mhandler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getmyguardrun);
	}

	public static Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				top_online_loading.setVisibility(8);
				if (anchorsonline.size() > 0) {
					if (anchorsonline.size() <= 4) {
						LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) anchor_online_listview
								.getLayoutParams(); // 取控件textView当前的布局参数
						linearParams.height = linearParams.WRAP_CONTENT;// 控件的高强制设成20
						linearParams.width = linearParams.FILL_PARENT;// 控件的宽强制设成30
						anchor_online_listview.setLayoutParams(linearParams);
					} else {
						LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) anchor_online_listview
								.getLayoutParams(); // 取控件textView当前的布局参数
						linearParams.height = 440;// 控件的高强制设成20
						linearParams.width = linearParams.FILL_PARENT;// 控件的宽强制设成30
						anchor_online_listview.setLayoutParams(linearParams);
					}
					SpannableString styledText = setTextColor("正在直播 ",
							anchorsonline.size() + "", "位");
					online_num.setText(styledText);
					TopOnLineAdapter adapter = new TopOnLineAdapter(activity,
							anchorsonline);
					anchor_online_listview.setAdapter(adapter);
					no_online_layout.setVisibility(8);
					anchor_online_listview.setVisibility(0);
				} else {
					no_online_layout.setVisibility(0);
					anchor_online_listview.setVisibility(8);
				}
				break;
			case 2:
				anchor_online_listview.setVisibility(8);
				top_online_loading.setVisibility(8);
				no_online_layout.setVisibility(0);
				break;
			case 3:
				if (anchorInfo != null) {
					// Animation localAnimation = AnimationUtils.loadAnimation(
					// activity, R.anim.livehall_suibiankankan_rotate);
					if (mImageLoader != null && mOptions != null) {
						mImageLoader.displayImage(APP.POST_URL_ROOT
								+ anchorInfo.getPoster_url(), suibian_icon,
								mOptions, null);
					}
					// localAnimation.setInterpolator(new LinearInterpolator());
					// suibian_icon.clearAnimation();
					// suibian_icon.startAnimation(localAnimation);
					if (suibiankankan_center != null) {
						suibiankankan_center.setVisibility(View.VISIBLE);
					}
				} else {
					if (suibiankankan_center != null) {
						suibiankankan_center.setVisibility(View.VISIBLE);
					}
					if (suibian_icon != null) {
						suibian_icon.setImageResource(R.drawable.suibian_bg);
						suibian_icon.clearAnimation();
					}
					if (sbkk_tv != null) {
						sbkk_tv.setVisibility(8);
					}
					getRandomAnchor();
				}
				break;
			case 7:
				if (suibiankankan_center != null) {
					suibiankankan_center.setVisibility(View.VISIBLE);
				}
				Radomanchor_from = anchorInfo;
				// Animation localAnimation = AnimationUtils.loadAnimation(
				// activity, R.anim.livehall_suibiankankan_rotate);
				mImageLoader.displayImage(
						APP.POST_URL_ROOT + anchorInfo.getPoster_url(),
						suibian_icon, mOptions, null);
				// localAnimation.setInterpolator(new LinearInterpolator());
				// suibian_icon.clearAnimation();
				// suibian_icon.startAnimation(localAnimation);
				break;
			case 8:
				if (suibiankankan_center != null) {
					suibiankankan_center.setVisibility(View.VISIBLE);
				}
				Log.i("lvjian", "获取随机主播失败或异常");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 设置当前播放主播
	 * 
	 * @param anchor
	 */
	public static void setAnchor(AnchorInfo anchor) {
		if (anchor != null) {
			anchorInfo = anchor;
			mhandler.sendEmptyMessage(3);
		}
	}

	public static void SetAnchorNull() {
		anchorInfo = null;
		mhandler.sendEmptyMessage(3);
	}

	/**
	 * 获取实际屏幕的宽高
	 */
	public void getScreenWH() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	private void showDownloadDialog(String days) {
		dialog_re = new AlertDialog.Builder(this).create();
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(this).inflate(
				R.layout.dialog_qiandao, null));
		ImageView qiandao_guanbi = (ImageView) localWindow
				.findViewById(R.id.qiandao_guanbi);
		qiandao_btn = (LinearLayout) localWindow.findViewById(R.id.qiandao_btn);
		tv_wyqd = (TextView) localWindow.findViewById(R.id.tv_wyqd);
		
		int[] rlid = {
				R.id.rl_bg_1,
				R.id.rl_bg_2,
				R.id.rl_bg_3,
				R.id.rl_bg_4,
				R.id.rl_bg_5,
				R.id.rl_bg_6,
				R.id.rl_bg_7,
		};
		int[] iconid = {
				R.id.icon_bg_1,
				R.id.icon_bg_2,
				R.id.icon_bg_3,
				R.id.icon_bg_4,
				R.id.icon_bg_5,
				R.id.icon_bg_6,
				R.id.icon_bg_7,
		};
		
		rl_bg = new RelativeLayout[7];
		icon_bg = new ImageView[7] ;
		for(int i=0; i<rl_bg.length; i++){
			rl_bg[i] = (RelativeLayout) localWindow.findViewById(rlid[i]);
			icon_bg[i] = (ImageView) localWindow.findViewById(iconid[i]);
		}
		
		qiandao_guanbi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_re.cancel();
			}
		});
		qiandao_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 点击签到
				QD();
			}
		});
		isqiandao = true;
		changeView(days);
	}

	public void changeView(String days) {
		int today = Integer.parseInt(days) ;
		for(int i=0;i<7;i++){
			if(i<today){
				rl_bg[i].setBackgroundResource(R.drawable.qiandao_dituyiqian);
				icon_bg[i].setVisibility(0);
			}
		}
	}

	/**
	 * 点击今天签到
	 */
	private void QD() {
		final String id = preferences.getString(APP.USER_ID, "");
		final String secret = preferences.getString(APP.SECRET, "");
		Runnable qddayrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.new_qd(id, secret);
					Log.i("lvjian", "签到是否成功--------------------------------》"
							+ result);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						handler.sendEmptyMessage(13);
					} else {
						handler.sendEmptyMessage(14);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(14);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(qddayrun);
	}

	// 点击个人中心签到

	BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
		public void onReceive(Context arg0, Intent intent) {
			Log.i("lvjian", "---------------------接收到消息--------------------");
			qd_grzx = true;
			IsSignToday(preferences.getString(APP.USER_ID, ""),
					preferences.getString(APP.SECRET, ""));
		};
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver1);
	}
	
	public void doFinish(){
		super.onBackPressed();
	}
	
	
	public void setPauseBackButton(){
		showUpdate = true ;
	}
	
}