package com.zhipu.chinavideo.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.zhipu.chinavideo.MainTabActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.SearchActivity;
import com.zhipu.chinavideo.adapter.LiveHallTypeChoiceAdapter;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.entity.Tags;
import com.zhipu.chinavideo.util.PagerSlidingTabStrip;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.UpdateManager;
import com.zhipu.chinavideo.util.Utils;

import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LiveHallFragment extends Fragment implements View.OnClickListener {
	private View rootView;
	private TypePagerAdapter adapter;
	private ViewPager viewPager;
	private PagerSlidingTabStrip indicator;
	private RelativeLayout pop_btn;
	private Animation showAnim;
	private LiveHallTypeChoiceAdapter choiceAdapter;
	private GridView typeChoiceGrid;
	private ViewStub typeChoicePop;
	private View typeChoiceView;
	private int popCheckedId = 0;
	private TextView chooseTypeText;
	private Animation goneAnim;
	private Animation xuanzhuandonghua;
	private List<Tags> tags_list;
	// 版本号
	private String lev;
	private LinearLayout ll_nowifi;
	private View livehall_loading;
	private View livehall_nowifi;
	// 获取本地版本号
	private String localversion;
	private int localVersionCode;
	private String aver;
	private int versionCode ;
	private String aurl = "http://down.0058.com/0058_Video.apk";
	private ImageView livehall_pop_btn;
	private View gridview_bg;
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (this.rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_livehall,
					container, false);
			livehall_loading = rootView.findViewById(R.id.livehall_loading);
			livehall_nowifi = rootView.findViewById(R.id.livehall_nowifi);
			this.viewPager = ((ViewPager) this.rootView
					.findViewById(R.id.moretab_viewPager));
			this.indicator = ((PagerSlidingTabStrip) this.rootView
					.findViewById(R.id.moretab_indicator));
			this.pop_btn = (RelativeLayout) this.rootView
					.findViewById(R.id.pop_btn);
			this.chooseTypeText = (TextView) this.rootView
					.findViewById(R.id.livehall_choose_pop_txt);
			livehall_pop_btn = (ImageView) rootView
					.findViewById(R.id.livehall_pop_btn);
			ll_nowifi = (LinearLayout) this.rootView
					.findViewById(R.id.ll_nowifi);
			ll_nowifi.setOnClickListener(this);
			this.pop_btn.setOnClickListener(this);
			tags_list = new ArrayList<Tags>();
			// 获取本地版本号
			localversion = getlocalverson();
			localVersionCode = getlocalversonCode() ;
			
			initTitleBar();
			// 版本升级和头部标签接扣
			gettaglist();
		}
		return rootView;
	}

	public class TypePagerAdapter extends FragmentStatePagerAdapter {
		private String[] TITLES;

		public TypePagerAdapter(FragmentManager fm, List<Tags> tag) {
			super(fm);
			// TODO Auto-generated constructor stub
			String[] arrayOfString = new String[tag.size() + 1];
			arrayOfString[0] = "直播";
			for (int i = 1; i < tag.size() + 1; i++) {
				arrayOfString[i] = tag.get(i - 1).getName();
				tag.get(i - 1).setCurrent_item(i);
			}
			this.TITLES = arrayOfString;
		}

		@Override
		public Fragment getItem(int paramInt) {
			// TODO Auto-generated method stub
			Fragment localObject;
			if (paramInt == 0) {
				localObject = MainPageFragment
						.getInstance(tags_list, viewPager);
			} else {
				localObject = TypePageFragment.getIntance(TITLES[paramInt]);
			}
			return localObject;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return this.TITLES[position];
		}

		public String[] getTITLES() {
			return TITLES;
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		// 下拉选框按钮
		case R.id.pop_btn:
			if (((MainTabActivity) getActivity()).isTypeChoicePopShown()) {
				outTypeChoicePopView();
			} else {
				inTypeChoicePopView();
			}
			break;
		case R.id.ll_nowifi:
			gettaglist();
			livehall_nowifi.setVisibility(8);
			break;
		default:
			break;
		}
	}

	private void initPop() {
		xuanzhuandonghua = AnimationUtils.loadAnimation(getActivity(),
				R.anim.xuanzhuan_titlebutton);
		livehall_pop_btn.startAnimation(xuanzhuandonghua);
		xuanzhuandonghua.setAnimationListener(new AnimationListener() {

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
				livehall_pop_btn.setImageResource(R.drawable.down_top);
			}
		});
		if (this.typeChoiceView == null) {
			this.typeChoicePop = ((ViewStub) getActivity().findViewById(
					R.id.type_choice_pop_stub));
			this.typeChoicePop.setLayoutResource(R.layout.type_choice_pop);
			View localView = this.typeChoicePop.inflate();
			this.typeChoiceView = localView.findViewById(R.id.typegrid_layout);
			this.gridview_bg = localView.findViewById(R.id.gridview_bg);
			this.typeChoiceGrid = ((GridView) localView
					.findViewById(R.id.typegird));
			this.choiceAdapter = new LiveHallTypeChoiceAdapter(getActivity(),
					this.popCheckedId, tags_list);
			this.typeChoiceGrid.setAdapter(this.choiceAdapter);
			this.typeChoiceView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					LiveHallFragment.this.outTypeChoicePopView();
				}
			});
		}
		this.typeChoiceGrid
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(
							AdapterView<?> paramAnonymousAdapterView,
							View paramAnonymousView, int paramAnonymousInt,
							long paramAnonymousLong) {
						LiveHallFragment.this.viewPager
								.setCurrentItem(paramAnonymousInt);
						LiveHallFragment.this.outTypeChoicePopView();
					}
				});
	}

	/**
	 * 显示下拉框
	 */
	private void inTypeChoicePopView() {
		// TODO Auto-generated method stub
		initPop();
		if (this.showAnim == null)
			this.showAnim = AnimationUtils.loadAnimation(getActivity(),
					R.anim.livehall_type_choice_pop_show);
		this.typeChoiceGrid.startAnimation(this.showAnim);
		this.gridview_bg.setVisibility(0);
		this.typeChoiceGrid.setVisibility(0);
		this.typeChoiceView.setVisibility(0);
		this.choiceAdapter.refreshGrid(this.popCheckedId);
		((MainTabActivity) getActivity()).setTypeChoicePopShown(true);
		this.chooseTypeText.setVisibility(0);
	}

	public void outTypeChoicePopView() {
		livehall_pop_btn.startAnimation(xuanzhuandonghua);
		xuanzhuandonghua.setAnimationListener(new AnimationListener() {

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
				livehall_pop_btn.setImageResource(R.drawable.down);
			}
		});
		if (this.goneAnim == null) {

			this.goneAnim = AnimationUtils.loadAnimation(getActivity(),
					R.anim.livehall_type_choice_pop_close);

			this.goneAnim
					.setAnimationListener(new Animation.AnimationListener() {
						public void onAnimationEnd(
								Animation paramAnonymousAnimation) {
							LiveHallFragment.this.typeChoiceGrid
									.setVisibility(8);
						}
						
						public void onAnimationRepeat(
								Animation paramAnonymousAnimation) {
						}
							
						public void onAnimationStart(
								Animation paramAnonymousAnimation) {
							LiveHallFragment.this.typeChoiceGrid
									.setOnItemClickListener(null);
						}
					});
		}
		this.typeChoiceGrid.startAnimation(this.goneAnim);
		this.gridview_bg.setVisibility(8);
		this.typeChoiceView.setVisibility(8);
		((MainTabActivity) getActivity()).setTypeChoicePopShown(false);
		this.chooseTypeText.setVisibility(4);
	}

	/**
	 * 获取标签
	 */
	private void gettaglist() {
		livehall_loading.setVisibility(0);
		Runnable gettaglistrun = new Runnable() {
			public void run() {
				String s = Utils.gettaglistdata();
//				Log.i("lvjian", "获取标签--------------->" + s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int i = jsonObject.getInt("s");
					if (i == 1) {
						JSONObject data = jsonObject.getJSONObject("data");
						aver = data.getString("aver");
						aurl = data.getString("aurl");
						
						if(data.has("androidversioncode")){
							versionCode = data.getInt("androidversioncode");
						}
						
						String updateInfo = data.getString("updateinfo");
						UpdateManager.str_updateInfo = updateInfo ;
						UpdateManager.str_version = "版本号"+ aver ;
						JSONArray ja = data.getJSONArray("tags");
						JSONArray newsarray = data.getJSONArray("news");
						// tab标签
						for (int j = 0; j < ja.length(); j++) {
							JSONObject taginfo = ja.getJSONObject(j);
							Gson gson = new Gson();
							Tags tag = gson.fromJson(taginfo.toString(),
									Tags.class);
							tags_list.add(tag);
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
		ThreadPoolWrap.getThreadPool().executeTask(gettaglistrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 版本更新
//				Log.i("lvjian", "localversion---------------------》"+ localversion);
				// 不为空
				
				try {
					if(localVersionCode<versionCode){
						UpdateManager mUpdateManager = new UpdateManager((MainTabActivity)getActivity(), aurl);
						if(mUpdateManager!=null){
							mUpdateManager.checkUpdateInfo();
						}
						
					}else{
						//自动登录的  打开签到
						if(GlobalData.getInstance(getActivity()).checkLogin()){
							String uid = GlobalData.getInstance(getActivity()).getUid();
							String usc = GlobalData.getInstance(getActivity()).getUSercert();
							MainTabActivity act = ((MainTabActivity)getActivity());
							if(act!=null){
								act.IsSignToday(uid, usc);
							}
						}
					}
				}
				catch (Exception e) {
					// TODO: handle exception
					Log.i("sjf", "version update has happend an error!");
				}
		
				
				livehall_loading.setVisibility(8);
				adapter = new TypePagerAdapter(getChildFragmentManager(),
						tags_list);
				viewPager.setAdapter(adapter);
				indicator.setViewPager(viewPager);
				indicator.setSelectedTextColorResource(R.color.title_bg);
				indicator.setIndicatorColorResource(R.color.title_bg);
				if (isAdded()) {
					indicator.setTextSize(getActivity().getResources()
							.getDimensionPixelSize(
									R.dimen.livehall_tab_textsize));
				}
				indicator.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub
						LiveHallFragment.this.popCheckedId = arg0;
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
				break;
			// 获取tag标签失败或者异常
			case 2:
				livehall_loading.setVisibility(8);
				livehall_nowifi.setVisibility(0);
				break;
			default:
				break;
			}
		};
	};

	private void initTitleBar() {
		ImageView sousuoimg = (ImageView) this.rootView
				.findViewById(R.id.sousuo_img);
		sousuoimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});
		ImageView curren_anchor = (ImageView) this.rootView
				.findViewById(R.id.current_anchor);
		curren_anchor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 弹出在线主播界面
				MainTabActivity.initanchoronline();
			}
		});

	}

	private String getlocalverson() {
		PackageInfo packageInfo;
		try {
			packageInfo = getActivity().getApplicationContext()
					.getPackageManager()
					.getPackageInfo(getActivity().getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "版本号未知";
		}
	}

	private int getlocalversonCode() {
		PackageInfo packageInfo;
		try {
			packageInfo = getActivity().getApplicationContext()
					.getPackageManager()
					.getPackageInfo(getActivity().getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	

}
