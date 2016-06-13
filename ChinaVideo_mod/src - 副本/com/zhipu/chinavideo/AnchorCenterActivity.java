package com.zhipu.chinavideo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.fragment.MyAnchorFragment;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AnchorCenterActivity extends FragmentActivity implements
		OnClickListener {



	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	// 主播信息
	private String room_state = "4";
	private String icon = "";
	private String nickName = "";
	private String received_level = "", received_beans;
	private String cost_level = "", cost_beans;
	private String anchorId = "";
	private String room_id;
	private String user_type;
	private String gender;
	private String pos;
	private String followed_by;
	private String fansNum;
	private SharedPreferences preferences;
	private String userId;
	private String secret;
	private boolean isAnchor ;
	private String[] m_photoPath ;
	
	// 明星等级图标和文字
	private ImageView anchor_mxdj, anchor_mxdj2;
	private TextView anchor_recevel_cha;
	private ProgressBar anchor_recevel_progressbar;
	// 财富等级图标和文字
	private ImageView anchor_cfdj, anchor_cfdj2;
	private TextView anchor_cost_cha;
	private ProgressBar anchor_cost_progressbar;
	// 界面按钮和输入框
	private ImageView anchor_icon_back, guanzhu_icon, anchor_sex,
			to_anchor_home;
	private CircularImage anchor_icon;
	private TextView fans_num, guanzhu_text, anchor_name, anchor_id,
			anchor_addr, anchor_status;
	private RelativeLayout guanzhu_btn;
	private LinearLayout anchor_home;
	private LinearLayout fans_layour, status_layout;
	private View anchor_loading;
	private List<AnchorInfo> list;

	//新增图片展示
	private ImageView imageView_morePhoto;
	private ImageView[] imageViewShowPhoto;
	private String[] str_photoUrl ;
	
	
	private ImageView imageView_noPhoto;
	LinearLayout line_ShowPhoto ;
	
	LinearLayout  line_personShow ;
	
	
	Map<String, Bitmap> m_allshowMap;
	
	private static final int M_Handler_GetPhotoListSuccess = 7 ;
	private static final int M_Handler_GetPhotoListFailed = 8 ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anchorcenter);
		Intent intent = this.getIntent();
		anchorId = intent.getStringExtra("id");
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(this));
		mOptions = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showStubImage(R.drawable.loading_img)
				.showImageForEmptyUri(R.drawable.loading_img)
				.showImageOnFail(R.drawable.loading_img)
				.showImageOnLoading(R.drawable.loading_img).cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		;
		m_allshowMap = new HashMap<String, Bitmap>();
		initView();
		getAnchorInformation();
	}

	private void initView() {
		anchor_loading = findViewById(R.id.anchor_loading);
		anchor_icon_back = (ImageView) findViewById(R.id.anchor_icon_back);
		anchor_icon_back.setOnClickListener(this);
		guanzhu_icon = (ImageView) findViewById(R.id.guanzhu_icon);
		anchor_sex = (ImageView) findViewById(R.id.anchor_sex);
		to_anchor_home = (ImageView) findViewById(R.id.to_anchor_home);
		anchor_icon = (CircularImage) findViewById(R.id.anchor_icon);
		fans_num = (TextView) findViewById(R.id.fance_num);
		guanzhu_text = (TextView) findViewById(R.id.guanzhu_text);
		anchor_name = (TextView) findViewById(R.id.anchor_name);
		anchor_id = (TextView) findViewById(R.id.anchor_id);
		anchor_addr = (TextView) findViewById(R.id.anchor_addr);
		anchor_status = (TextView) findViewById(R.id.anchor_status);
		anchor_mxdj = (ImageView) findViewById(R.id.anchor_mxdj);
		anchor_mxdj2 = (ImageView) findViewById(R.id.anchor_mxdj2);
		anchor_cfdj = (ImageView) findViewById(R.id.anchor_cfdj);
		anchor_cfdj2 = (ImageView) findViewById(R.id.anchor_cfdj2);
		anchor_recevel_cha = (TextView) findViewById(R.id.anchor_recevel_cha);
		anchor_cost_cha = (TextView) findViewById(R.id.anchor_cost_cha);
		anchor_recevel_progressbar = (ProgressBar) findViewById(R.id.anchor_recevel_progressbar);
		anchor_cost_progressbar = (ProgressBar) findViewById(R.id.anchor_cost_progressbar);
		guanzhu_btn = (RelativeLayout) findViewById(R.id.guanzhu_btn);
		guanzhu_btn.setOnClickListener(AnchorCenterActivity.this);
		fans_layour = (LinearLayout) findViewById(R.id.anchor_fans_layour);
		status_layout = (LinearLayout) findViewById(R.id.anchor_status_layout);
		anchor_home = (LinearLayout) findViewById(R.id.anchor_home);
		anchor_home.setOnClickListener(this);
		
		imageView_morePhoto = (ImageView)findViewById(R.id.imageView_morePhoto) ;
		imageView_morePhoto.setOnClickListener(this);
		
		
		final int[] img_photoid = {
				R.id.anchor_showphoto0,
				R.id.anchor_showphoto1,
				R.id.anchor_showphoto2,
				} ;
		imageViewShowPhoto = new ImageView[img_photoid.length] ;
		for(int i=0; i<imageViewShowPhoto.length; i++){
			imageViewShowPhoto[i] = (ImageView)findViewById(img_photoid[i]) ;
		}
		
		imageView_noPhoto = (ImageView)findViewById(R.id.imageview_noPhoto) ;
		line_ShowPhoto = (LinearLayout)findViewById(R.id.anchor_showphotoablum) ;
		imageView_noPhoto.setVisibility(View.VISIBLE);
		line_ShowPhoto.setVisibility(View.GONE);
		
		
		line_personShow = (LinearLayout)findViewById(R.id.anchor_show) ;
		line_personShow.setOnClickListener(this);
		
	}

	private void getAnchorInformation() {
		userId = preferences.getString(APP.USER_ID, "");
		secret = preferences.getString(APP.SECRET, "");
		initAnchorData();
		GetPhotoList();
	}

	/**
	 * 获取详细资料信息
	 */
	private void initAnchorData() {
		Runnable getanchordatarun = new Runnable() {
			public void run() {
				try {
					String s = Utils.getanchorfile(userId, secret, anchorId);
					JSONObject result = new JSONObject(s);
					int code = result.getInt("s");
					JSONObject data = result.getJSONObject("data");
					if (code == 1) {
						
					
						
						if (data.has("room_status")) {
							room_state = data.getString("room_status");
						}
						// 是否关注
						if (data.has("followed_by")) {
							followed_by = data.getString("followed_by");
						}

						if (data.has("room_id")) {
							room_id = data.getString("room_id");
						}
						JSONObject anchorInfo = data.getJSONObject("userinfo");
						user_type = anchorInfo.getString("user_type");
						
						if (anchorInfo.has("user_type")) {
							String user_type = anchorInfo.getString("user_type");
							if(user_type.equals("anchor")){
								isAnchor = true ;
							}else{
								isAnchor = false ;
							}
						}
						
						
						icon = anchorInfo.getString("icon");
						if (anchorInfo.has("nickname")) {
							nickName = anchorInfo.getString("nickname");
						}
						if (anchorInfo.has("gender")) {
							gender = anchorInfo.getString("gender");
						}
						if (anchorInfo.has("pos")) {
							pos = anchorInfo.getString("pos");
						}
						cost_level = anchorInfo.getString("cost_level");
						cost_beans = anchorInfo.getString("cost_beans");
						received_level = anchorInfo.getString("received_level");
						received_beans = anchorInfo.getString("received_beans");
//						pos = anchorInfo.getString("pos");
//						m_photoPath
						
						// 主播照片信息 sjf  2016-4-19 14:20  
						
						
						if (data.has("photoinfo")) {
							
							
							JSONObject photoinfo = data.getJSONObject("photoinfo") ;
							
							int hasPhoto = photoinfo.getInt("s");
							
							
							if(hasPhoto ==1 ){
								JSONObject photodata = photoinfo.getJSONObject("data") ;
								
								if (photodata.has("photos")) {
									JSONArray photoArray = photodata.getJSONArray("photos") ;
									int photoCount = photoArray.length() ;
									
									String path = photodata.getString("image_url");
									
									str_photoUrl = new String[photoCount] ;
									for(int i=0; i<photoCount; i++){
										JSONObject photoInfo = photoArray.getJSONObject(i);
										String urlPath = photoInfo.getString("uri");
										str_photoUrl[i] =  APP.POST_URL_HEAD + path + "big_"+ urlPath;
									}
								}
							}
							
							
							
						}
						
						
					
						
						
						
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getanchordatarun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String notice = null;
			switch (msg.what) {
			case 1:
				anchor_loading.setVisibility(8);
				mImageLoader.displayImage(APP.USER_LOGO_ROOT + icon,
						anchor_icon, mOptions);
				anchor_id.setText(anchorId);
				anchor_name.setText(nickName);
				if (pos == null || pos.equals("") || pos.equals("null")) {
					anchor_addr.setText("未知");
				} else {
					anchor_addr.setText(pos);
				}
				if ("0".equals(gender)) {
					anchor_sex.setVisibility(4);
				} else if ("1".equals(gender)) {
					anchor_sex.setImageResource(R.drawable.man);
				} else if ("2".equals(gender)) {
					anchor_sex.setImageResource(R.drawable.female);
				}
				if ("anchor".equals(user_type)) {
					getFansNum();
					if ("1".equals(room_state)) {
						anchor_status.setText("正在直播...");
					} else {
						anchor_status.setText("未开播");
					}

					if ("1".equals(followed_by)) {
						guanzhu_text.setText("取消关注");
					} else {
						guanzhu_text.setText("关注");
					}
				} else if ("user".equals(user_type)) {
					fans_layour.setVisibility(8);
					status_layout.setVisibility(8);
					anchor_home.setVisibility(8);
				}else{
					
				}
				APP.setReceived_level(received_level, anchor_mxdj,
						AnchorCenterActivity.this);
				String rr = (Integer.parseInt(received_level) + 1) + "";
				APP.setReceived_level(rr, anchor_mxdj2,
						AnchorCenterActivity.this);
				Long recevelbeans = Long.parseLong(received_beans);
				Long currrecevel = APP.parsereceivenum(rr);
				Long frontrecevel = APP.parsereceivenum(received_level);
				int recevel_pg = (int) (((recevelbeans - frontrecevel) * 100) / (currrecevel - frontrecevel));
				anchor_recevel_progressbar.setMax(100);
				anchor_recevel_progressbar.setProgress(recevel_pg);
				String recevel_cha = (currrecevel - recevelbeans) + "";
				anchor_recevel_cha.setText("还差" + recevel_cha + "升级");
				String cc = (Integer.parseInt(cost_level) + 1) + "";
				APP.setCost_level(cost_level, anchor_cfdj,
						AnchorCenterActivity.this);
				APP.setCost_level(cc, anchor_cfdj2, AnchorCenterActivity.this);
				Long costbeans = Long.parseLong(cost_beans);
				Long currcost = APP.parseCostnum(cc);
				Long frontcost = APP.parseCostnum(cost_level);
				int cost_pg = (int) (((costbeans - frontcost) * 100) / (currcost - frontcost));
				anchor_cost_progressbar.setMax(100);
				anchor_cost_progressbar.setProgress(cost_pg);
				String cost_cha = (currcost - costbeans) + "";
				anchor_cost_cha.setText("还差" + cost_cha + "升级");
				
				initAnchorPhoto();
				
				if(isAnchor){
					line_personShow.setVisibility(View.VISIBLE);
					anchor_home.setVisibility(View.VISIBLE);
				}else{
					line_personShow.setVisibility(View.GONE);
					anchor_home.setVisibility(View.GONE);
				}
				
				
				break;
			case 2:

				break;
			case 3:
				if (msg.arg2 == 0) {
					Utils.showToast(AnchorCenterActivity.this, "关注成功！");
					followed_by = "1";
					guanzhu_text.setText("取消关注");
					fans_num.setText(msg.arg1 + "");
				} else if (msg.arg2 == 1) {
					Utils.showToast(AnchorCenterActivity.this, "取消关注成功！");
					followed_by = "0";
					guanzhu_text.setText("关注");
					MyAnchorFragment.state ="2";
				}
				break;
			case 4:
				notice = (String) msg.obj;
				if (notice == null|| notice.equals("")|| notice.length()==0) {
					Utils.showToast(AnchorCenterActivity.this, "关注失败！请检查是否登录!");
				} else {
					Utils.showToast(AnchorCenterActivity.this, notice);
				}

				break;
			case 5:
				fans_num.setText(fansNum);
				break;
			case 6:
				Utils.showToast(AnchorCenterActivity.this, "获取粉丝数量失败！");
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.anchor_show:
		{
			if(str_photoUrl!=null){
				Intent in = new Intent(this, MyPhotoAlbumActivity.class);
				in.putExtra("anchorId", anchorId);
				this.startActivity(in);
			}
		}
			break ;
		case R.id.anchor_icon_back:
			finish();
			break;
		case R.id.guanzhu_btn:
			if("0".equals(followed_by)){
				FollowAnchor();
			}else if("1".equals(followed_by)){
				CancelFollowAnchor();
			}
			break;
		case R.id.anchor_home:
		{
			Intent in = new Intent(this, LiveRoomActivity.class);
			in.putExtra("room_id", room_id);
			this.startActivity(in);
		}
			
			break;
		case R.id.anchor_icon:
			break;
		case R.id.imageView_morePhoto:
		{
			Intent in = new Intent(this, MyPhotoAlbumActivity.class);
			in.putExtra("anchorId", anchorId);
			this.startActivity(in);
		}
			break ;
		default:
			break;
		}
	}

	protected void initAnchorPhoto() {
		
		if(str_photoUrl!=null){
			
			imageView_noPhoto.setVisibility(View.GONE);
			imageView_morePhoto.setVisibility(View.VISIBLE);
			line_ShowPhoto.setVisibility(View.VISIBLE);
			
			for(int i=0; i<3; i++){
				if(i<str_photoUrl.length){
					imageViewShowPhoto[i].setVisibility(View.VISIBLE);
					final ImageView view = imageViewShowPhoto[i] ;
					mImageLoader.displayImage(str_photoUrl[i],imageViewShowPhoto[i], mOptions, null);
				}else{
					imageViewShowPhoto[i].setVisibility(View.INVISIBLE);
				}

			}
				
		}else{
			imageView_noPhoto.setVisibility(View.VISIBLE);
			line_ShowPhoto.setVisibility(View.GONE);
			imageView_morePhoto.setVisibility(View.GONE);
		}
		
		
	}

	/**
	 * 关注主播
	 */
	private void FollowAnchor() {
		Runnable guanzhurun = new Runnable() {
			@Override
			public void run() {
				try {
					String s = Utils.follow(userId, secret, anchorId);
					Log.i("sumao", "FollowAnchor userId:" + userId + "secret"
							+ secret + "anchorId" + anchorId + "s" + s);
					JSONObject result = new JSONObject(s);
					int code = result.getInt("s");
					if (code == 1) {
						Message msg = Message.obtain();
						msg.what = 3;
						msg.arg1 = result.getInt("sum");
						msg.arg2 = 0;
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain();
						msg.what = 4;
						msg.obj = result.getString("data");
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(guanzhurun);
	}

	/**
	 * 取消关注主播
	 */
	private void CancelFollowAnchor() {
		Runnable guanzhurun = new Runnable() {
			@Override
			public void run() {
				try {
					String s = Utils.cancelfollow(userId, secret, anchorId);

					Log.i("sumao", "CancelFollowAnchor userId:" + userId
							+ "secret" + secret + "anchorId" + anchorId + "s"
							+ s);
					JSONObject result = new JSONObject(s);
					int code = result.getInt("s");
					if (code == 1) {
						Message msg = Message.obtain();
						msg.what = 3;
						msg.arg2 = 1;
						handler.sendMessage(msg);
						getFansNum();
					} else {
						Message msg = Message.obtain();
						msg.what = 4;
						msg.obj = result.getString("data");
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(guanzhurun);
	}

	/**
	 * 获取主播红豆数量和粉丝数量
	 */
	private void getFansNum() {
		Runnable heartsandfansrun = new Runnable() {
			@Override
			public void run() {
				try {
					String s = Utils.getheartsandfansnum(anchorId);
					JSONObject result = new JSONObject(s);
					int code = result.getInt("s");
					JSONObject data = result.getJSONObject("data");
					if (code == 1) {
						fansNum = data.getString("fans");
						handler.sendEmptyMessage(5);
					} else {
						handler.sendEmptyMessage(6);
					}
				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(heartsandfansrun);
	}

	private void getAnchorList() {
		list = new ArrayList<AnchorInfo>();
		Runnable getmyguardrun = new Runnable() {
			public void run() {
				String s = Utils.getsubscribelist(userId, secret);
				Log.i("sumao", "s"+s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int a = jsonObject.getInt("s");
					if (a == 1) {
						JSONArray ja = jsonObject.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							Gson gson = new Gson();
							AnchorInfo an = gson.fromJson(ja.getJSONObject(i)
									.toString(), AnchorInfo.class);
							list.add(an);
							Log.i("sumao", "an"+an.toString());
						}
					} else {
					}
				} catch (JSONException e) {
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getmyguardrun);
	}
	
	
	
	
	/**
	 * 关注主播
	 */
	private void GetPhotoList() {
//		Runnable guanzhurun = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					String s = Utils.follow(userId, secret, anchorId);
//					Log.i("sumao", "FollowAnchor userId:" + userId + "secret"
//							+ secret + "anchorId" + anchorId + "s" + s);
//					JSONObject result = new JSONObject(s);
//					int code = result.getInt("s");
//					if (code == 1) {
//						Message msg = Message.obtain();
//						msg.what = 3;
//						msg.arg1 = result.getInt("sum");
//						msg.arg2 = 0;
//						handler.sendMessage(msg);
//					} else {
//						Message msg = Message.obtain();
//						msg.what = 4;
//						msg.obj = result.getString("data");
//						handler.sendMessage(msg);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(4);
//				}
//			}
//		};
//		ThreadPoolWrap.getThreadPool().executeTask(guanzhurun);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		setContentView(R.layout.activity_null);
		for(int i=0 ;i<imageViewShowPhoto.length; i++){
			if(imageViewShowPhoto[i]!=null){
				imageViewShowPhoto[i] = null ;
			}
		}
		imageViewShowPhoto = null ;
		
		m_allshowMap.clear();
		m_allshowMap = null ;
	}
}
