package com.zhipu.chinavideo.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.ChooseSongAdapter;
import com.zhipu.chinavideo.adapter.ChooseSongListViewAdapter;
import com.zhipu.chinavideo.entity.ChooseSongVo;
import com.zhipu.chinavideo.fragment.ChooseSongTextDialog;
import com.zhipu.chinavideo.rechargecenter.RechargeActivity;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ConsumpUtil;
import com.zhipu.chinavideo.util.MgcUtil;
import com.zhipu.chinavideo.util.PagerSlidingTabStrip;
import com.zhipu.chinavideo.util.ShowMsgDialog;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author: zhongxf
 * @Description: 点歌的View管理
 * @date: 2016年4月8日
 */
public class ChooseSongManager implements OnClickListener {
	private boolean isshowing = false;
	private static ChooseSongManager instance;// 点歌的管理对象
	private ViewStub viewStub;// 显示点歌界面的ViewStub
	private Context cxt;// 上下文
	private View rootView;// 点歌界面的根View
	private View closeView;// 关闭ViewStub的占位View
	private PagerSlidingTabStrip pst;// 滑动的
	private ViewPager viewPager;// 滑动切换的ViewPager
	private List<View> viewList;// ViewPager的View数据源
	private List<ChooseSongVo> songList;// 点播的歌曲列表
	private List<ChooseSongVo> selectedList;// 已点播的歌曲列表
	private int songPage = 1;// 歌单的
	private int selectPage = 1;
	private int pageSize = 9;
	private ChooseSongListViewAdapter songAdapter;// 点歌单的适配器
	private ChooseSongListViewAdapter selectAdapter;// 已点歌单的适配器
	private PullToRefreshExpandableListView songListView;
	private PullToRefreshExpandableListView selectListView;
	private ExpandableListView songRefreshListView;
	private ExpandableListView selectRefreshListView;
	private View songLoadingView;// 请求等待
	private View selectLoadView;// 请求等待
	private TextView songInfo;// 点歌单显示无数据提示
	private TextView selectInfo;// 已点歌显示无数据提示
	private List<ChooseSongVo> allSongList;// 从服务器加载所有的歌单的数据的集合
	private List<ChooseSongVo> allSelectList;// 从服务器加载所有的已点歌的数据的集合
	private String roomId;// 房间的编号

	private SharedPreferences share;
	private TextView priceInfo;
	private TextView checkedSongName;
	private TextView checkedSongInfo;
	private String anchor_id = "";// 主播的ID
	private int anchor_level = 1;// 主播的等级 1.代表红星主播 2.代表钻石主播 3.代表皇冠主播
	
	private MgcUtil mgc = new MgcUtil();//敏感词工具类

	private ChooseSongManager() {

	}

	public boolean isIsshowing() {
		return isshowing;
	}

	public static ChooseSongManager getInstance() {
		if (instance == null) {
			instance = new ChooseSongManager();
		}
		return instance;
	}

	public void exit() {
		instance = null;
	}

	public void init(ViewStub viewStub, Context cxt, String roomId,
			String anchor_id) {
		this.cxt = cxt;
		this.viewStub = viewStub;
		this.roomId = roomId;
		this.anchor_id = anchor_id;
		share = cxt.getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		getAnthor();// 获取主播信息
	}

	// 获取主播的信息
	private void getAnthor() {
		Runnable getanchordatarun = new Runnable() {
			public void run() {
				try {
					String s = Utils.getanchorfile(
							share.getString(APP.USER_ID, ""), APP.SECRET,
							anchor_id);
					JSONObject result = new JSONObject(s);
					int code = result.getInt("s");
					JSONObject data = result.getJSONObject("data");
					if (code == 1) {
						JSONObject anchorInfo = data.getJSONObject("userinfo");
						String levelStr = anchorInfo.getString("cost_level");
						int level = new Integer(levelStr).intValue();
						if (level <= 10) {
							anchor_level = 1;
						} else if (level > 10 && level <= 15) {
							anchor_level = 2;
						} else if (level > 15) {
							anchor_level = 3;
						}

					} else {
					}
				} catch (Exception e) {
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getanchordatarun);
	}

	// 显示点歌界面
	public void showViews() {
		isshowing = true;
		if (rootView == null) {
			this.viewStub.setLayoutResource(R.layout.fragment_choose_song);
			rootView = this.viewStub.inflate();
			initViews();
		} else {
			this.rootView.setVisibility(View.VISIBLE);
		}
		if(mgc==null){
			mgc = new MgcUtil();
		}
		mgc.loadMgc(cxt);
		getData();// 获取点歌信息
	}

	// 判断用户的余额是否够
	private boolean isCanChooseSong() {
		String moneyStr = share.getString(APP.BEANS, "0");

		if (!Utils.isEmpty(moneyStr)) {
			if (anchor_level == 1) {
				return ConsumpUtil.compareTo(moneyStr,
						ConsumpUtil.RED_STAR_PRICE) >= 0;
			}
			if (anchor_level == 2) {
				return ConsumpUtil.compareTo(moneyStr,
						ConsumpUtil.DIAMONDS_PRICE) >= 0;
			}
			if (anchor_level == 3) {
				return ConsumpUtil.compareTo(moneyStr, ConsumpUtil.CROWN_PRICE) >= 0;
			}
		}
		return false;
	}

	// 初始化界面
	private void initViews() {
		viewList = new ArrayList<View>();
		closeView = rootView.findViewById(R.id.room_ranklist_top);
		closeView.setOnClickListener(this);
		pst = (PagerSlidingTabStrip) rootView.findViewById(R.id.dg_indicator);
		int a = Utils.dip2px(cxt, 14.0f);
		pst.setTabPaddingLeftRight(a);
		viewPager = (ViewPager) rootView.findViewById(R.id.dg_viewpager);
		viewPager.setAdapter(new ChooseSongAdapter(viewList));
		pst.setViewPager(viewPager);
		pst.setSelectedTextColorResource(R.color.title_bg);
		pst.setIndicatorColorResource(R.color.title_bg);
		pst.setTextSize(cxt.getResources().getDimensionPixelSize(
				R.dimen.livehall_tab_textsize));

		songList = new ArrayList<ChooseSongVo>();
		selectedList = new ArrayList<ChooseSongVo>();
		songAdapter = new ChooseSongListViewAdapter(cxt, songList);
		selectAdapter = new ChooseSongListViewAdapter(cxt, selectedList);

		View songView = LayoutInflater.from(cxt).inflate(
				R.layout.choose_song_view, null);
		View selectView = LayoutInflater.from(cxt).inflate(
				R.layout.choose_song_view, null);

		songLoadingView = songView.findViewById(R.id.choose_song_loading);
		selectLoadView = selectView.findViewById(R.id.choose_song_loading);

		songListView = (PullToRefreshExpandableListView) songView
				.findViewById(R.id.choose_song_list);
		selectListView = (PullToRefreshExpandableListView) selectView
				.findViewById(R.id.choose_song_list);

		songListView.setMode(PullToRefreshBase.Mode.BOTH);
		songListView.setOnRefreshListener(songRefreshListener);
		songListView.setDisableScrollingWhileRefreshing(true);
		songListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, false, this.songScrollListener));

		selectListView.setMode(PullToRefreshBase.Mode.BOTH);
		selectListView.setOnRefreshListener(selectRefreshListener);
		selectListView.setDisableScrollingWhileRefreshing(true);
		selectListView.setOnScrollListener(new PauseOnScrollListener(
				ImageLoader.getInstance(), true, false,
				this.selectScrollListener));

		songRefreshListView = songListView.getRefreshableView();
		selectRefreshListView = selectListView.getRefreshableView();
		songRefreshListView.setGroupIndicator(null);
		selectRefreshListView.setGroupIndicator(null);
		songRefreshListView.setAdapter(songAdapter);
		selectRefreshListView.setAdapter(selectAdapter);
		songRefreshListView.setOnGroupClickListener(songItemListen);

		viewList.add(songView);
		viewList.add(selectView);
		viewPager.getAdapter().notifyDataSetChanged();

		songInfo = (TextView) songView.findViewById(R.id.choose_song_info);
		selectInfo = (TextView) selectView.findViewById(R.id.choose_song_info);

		allSongList = new ArrayList<ChooseSongVo>();
		allSelectList = new ArrayList<ChooseSongVo>();

		priceInfo = (TextView) rootView
				.findViewById(R.id.choose_song_price_info);
		String content = cxt.getString(R.string.dg_info);
		SpannableString styledText = new SpannableString(content);
		styledText.setSpan(new TextAppearanceSpan(cxt, R.style.orange_words),
				42, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		priceInfo.setText(styledText, TextView.BufferType.SPANNABLE);
		
	}

	private final static int REQUEST_SONG_SUCCESS = 0;// 获取点歌单的标志
	private final static int REQUEST_SELCET_SUCCESS = 1;// 获取已点歌单子的标志
	private final static int REQEUST_ERROE = 2;// 接口获取数据出错了
	private final static int CHOOSE_SONG_ERROE = 3;// 接口获取数据出错了
	private final static int RECHARGE_FLAG = 4;// 点歌的时候乐币不足
	private final static int CHOOSE_SONG_SUCCESS = 5;// 点歌成功
	// 线程间通信的Handler
	private Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case REQUEST_SONG_SUCCESS:
				songLoadingView.setVisibility(View.GONE);
				songListView.onRefreshComplete();
				songAdapter.notifyDataSetChanged();
				if (songList.size() > 0) {
					songListView.setVisibility(View.VISIBLE);
					songInfo.setVisibility(View.GONE);
				} else {
					songListView.setVisibility(View.GONE);
					songInfo.setVisibility(View.VISIBLE);
				}
				break;
			case REQUEST_SELCET_SUCCESS:
				selectLoadView.setVisibility(View.GONE);
				selectListView.onRefreshComplete();
				selectAdapter.notifyDataSetChanged();

				if (selectedList.size() > 0) {
					selectListView.setVisibility(View.VISIBLE);
					selectInfo.setVisibility(View.GONE);
				} else {
					selectListView.setVisibility(View.GONE);
					selectInfo.setVisibility(View.VISIBLE);
				}
				break;
			case REQEUST_ERROE:
				songLoadingView.setVisibility(View.GONE);
				selectLoadView.setVisibility(View.GONE);
				songListView.setVisibility(View.GONE);
				selectListView.setVisibility(View.GONE);
				songInfo.setVisibility(View.VISIBLE);
				selectInfo.setVisibility(View.VISIBLE);
				break;
			case CHOOSE_SONG_ERROE:
				String errorInfo = (String) msg.obj;
				Toast.makeText(cxt, errorInfo, Toast.LENGTH_LONG).show();
				break;
			case RECHARGE_FLAG:
				final ShowMsgDialog dia = new ShowMsgDialog();
				dia.setSureBtnText(cxt.getResources().getString(
						R.string.recharge));
				dia.setSureBtn(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dia.closeDialog();
						((Activity) cxt).startActivity(new Intent(cxt,
								RechargeActivity.class));
					}
				});
				dia.setCancleBtn(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dia.closeDialog();
					}
				});
				dia.show(cxt, cxt.getResources().getString(R.string.dg_error),
						true);
				break;
			case CHOOSE_SONG_SUCCESS:
				Toast.makeText(cxt, "点歌成功", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

		};

	};

	// 请求点歌接口
	private void chooseSong(final String text, final ChooseSongVo vo) {
		Runnable chooseSong = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String res = Utils.addRpcEvent(RpcEvent.ChooseSong,
						share.getString(APP.USER_ID, ""),
						share.getString(APP.SECRET, ""), roomId, vo.getId(),
						"1", text);

				try {
					JSONObject resJson = new JSONObject(res);
					int s = resJson.getInt("s");
					if (s == 1) {
						mHandler.sendEmptyMessage(CHOOSE_SONG_SUCCESS);
					} else if (s == 0) {
						mHandler.sendEmptyMessage(RECHARGE_FLAG);
					} else {
						Message msg = new Message();
						msg.what = CHOOSE_SONG_ERROE;
						msg.obj = resJson.getString("msg");
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(chooseSong);
	}

	// 一打开这个界面从服务器获取所有的数据
	private void getData() {
		songPage = 1;
		selectPage = 1;
		Runnable getSong = new Runnable() {
			@Override
			public void run() {
				String res = Utils.addRpcEvent(RpcEvent.GetChooseSong,
						share.getString(APP.USER_ID, ""),
						share.getString(APP.SECRET, ""), roomId);// 调用接口返回数据
				try {
					JSONObject resJson = new JSONObject(res);
					int s = resJson.getInt("s");
					if (s == 1) {
						JSONObject dataJson = resJson.getJSONObject("data");
						JSONArray selectArr = dataJson.getJSONArray("list");
						JSONArray songArr = dataJson.getJSONArray("song_menu");
						// 解析歌单列表
						for (int i = 0; i < songArr.length(); i++) {
							ChooseSongVo vo = new ChooseSongVo();
							JSONObject json = songArr.getJSONObject(i);
							if (json.has("id")) {
								vo.setId(json.getString("id"));
							}
							if (json.has("song_id")) {
								vo.setSongId(json.getString("song_id"));
							}
							if (json.has("singer")) {
								vo.setSinger(json.getString("singer"));
							}
							if (json.has("created_at")) {
								vo.setDate(json.getString("created_at"));
							}
							if (json.has("name")) {
								vo.setName(json.getString("name"));
							}
							allSongList.add(vo);
						}
						// 解析已经点歌曲列表
						for (int j = 0; j < selectArr.length(); j++) {

							ChooseSongVo vo = new ChooseSongVo();
							JSONObject json = selectArr.getJSONObject(j);
							if (json.has("id")) {
								vo.setId(json.getString("id"));
							}
							if (json.has("to_user_id")) {
								vo.setToUserId(json.getString("to_user_id"));
							}
							if (json.has("user_id")) {
								vo.setUserId(json.getString("user_id"));
							}
							if (json.has("song_singer")) {
								vo.setSinger(json.getString("song_singer"));
							}
							if (json.has("created_at")) {
								vo.setDate(json.getString("created_at"));
							}
							if (json.has("song_name")) {
								vo.setName(json.getString("song_name"));
							}
							if (json.has("nickname")) {
								vo.setNickname(json.getString("nickname"));
							}
							vo.setSelected(true);
							allSelectList.add(vo);

						}

						getSelectData();
						getSongData();

					} else {
						mHandler.sendEmptyMessage(REQEUST_ERROE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				getSongData();// 显示点歌单数据
				getSelectData();// 显示已点歌数据
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getSong);
	}

	// 加载点歌单数据到界面上
	private void getSongData() {
		if (songPage > 9) {
			songListView.onRefreshComplete();
			return;
		}
		int showNum = songPage * pageSize;
		songList.clear();
		if (showNum > allSongList.size()) {
			for (int i = 0; i < allSongList.size(); i++) {
				songList.add(allSongList.get(i));
			}
		} else {
			for (int i = 0; i < showNum; i++) {
				songList.add(allSongList.get(i));
			}
		}
		mHandler.sendEmptyMessage(REQUEST_SONG_SUCCESS);
	}

	// 加载已点歌单数据到界面上
	private void getSelectData() {
		if (selectPage > 9) {
			selectListView.onRefreshComplete();
			return;
		}
		selectedList.clear();
		int showNum = selectPage * pageSize;
		if (showNum > allSelectList.size()) {
			for (int i = 0; i < allSelectList.size(); i++) {
				selectedList.add(allSelectList.get(i));
			}
		} else {
			for (int i = 0; i < showNum; i++) {
				selectedList.add(allSelectList.get(i));
			}
		}
		mHandler.sendEmptyMessage(REQUEST_SELCET_SUCCESS);
	}

	
	// 点歌单的点击监听
	private OnGroupClickListener songItemListen = new OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView listView, View itemView,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			checkedSongName = (TextView) itemView
					.findViewById(R.id.choose_song_name);
			checkedSongInfo = (TextView) itemView
					.findViewById(R.id.choose_song_info);
			checkedSongName.setTextColor(cxt.getResources().getColor(
					R.color.second));
			checkedSongInfo.setTextColor(cxt.getResources().getColor(
					R.color.second));

			final ChooseSongVo vo = songList.get(arg2);

			final InputMethodManager imm = (InputMethodManager) cxt
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (isCanChooseSong()) {
				final ChooseSongTextDialog cst = new ChooseSongTextDialog();
				cst.setOnCancleClickListen(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						checkedSongName.setTextColor(cxt.getResources()
								.getColor(R.color.disc_word));
						checkedSongInfo.setTextColor(cxt.getResources()
								.getColor(R.color.disc_word));
						imm.hideSoftInputFromWindow(cst.getTxtInput()
								.getWindowToken(), 0);
						cst.closeDialog();

					}

					private Object Activity(Context cxt) {
						// TODO Auto-generated method stub
						return null;
					}
				});
				cst.setOnSureClickListen(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						checkedSongName.setTextColor(cxt.getResources()
								.getColor(R.color.disc_word));
						checkedSongInfo.setTextColor(cxt.getResources()
								.getColor(R.color.disc_word));

						EditText textInput = cst.getTxtInput();
						String txt = textInput.getText().toString();
						if (txt != null && !"".equals(txt)) {
							imm.hideSoftInputFromWindow(cst.getTxtInput()
									.getWindowToken(), 0);
							cst.closeDialog();
							txt = mgc.getmgcarray(txt);
							chooseSong(txt, vo);
						} else {// 点击确定按钮，并且没有填写寄语的时候（在寄语输入框提示用户）
							checkedSongName.setTextColor(cxt.getResources()
									.getColor(R.color.disc_word));
							checkedSongInfo.setTextColor(cxt.getResources()
									.getColor(R.color.disc_word));
							chooseSong(
									cxt.getResources().getString(
											R.string.dg_default_txt), vo);

							imm.hideSoftInputFromWindow(cst.getTxtInput()
									.getWindowToken(), 0);
							cst.closeDialog();
						}

					}

				});
				cst.show(cxt);

			} else {// 用户本地余额不够
				final ShowMsgDialog sd = new ShowMsgDialog();
				sd.setSureBtnText("充值");
				sd.setCancleBtn(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						checkedSongName.setTextColor(cxt.getResources()
								.getColor(R.color.disc_word));
						checkedSongInfo.setTextColor(cxt.getResources()
								.getColor(R.color.disc_word));
						sd.closeDialog();
					}
				});
				sd.setSureBtn(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						checkedSongName.setTextColor(cxt.getResources()
								.getColor(R.color.disc_word));
						checkedSongInfo.setTextColor(cxt.getResources()
								.getColor(R.color.disc_word));
						sd.closeDialog();
						((Activity) cxt).startActivity(new Intent(cxt,
								RechargeActivity.class));
					}
				});
				sd.show(cxt, cxt.getResources().getString(R.string.dg_error),
						true);
			}

			return false;
		}
	};
	private String[] stringArr;// 敏感词库

	// 下拉刷新，重新加载数据
	@SuppressWarnings("rawtypes")
	private PullToRefreshBase.OnRefreshListener2 songRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 下拉刷新
			songPage = 1;
			getSongData();
		}

		public void onPullUpToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 上拉加载
			songPage = songPage + 1;
			getSongData();
		}
	};
	private AbsListView.OnScrollListener songScrollListener = new AbsListView.OnScrollListener() {
		public void onScroll(AbsListView paramAnonymousAbsListView,
				int paramAnonymousInt1, int paramAnonymousInt2,
				int paramAnonymousInt3) {
		}

		public void onScrollStateChanged(AbsListView paramAnonymousAbsListView,
				int paramAnonymousInt) {
		}
	};

	// 下拉刷新，重新加载数据
	@SuppressWarnings("rawtypes")
	private PullToRefreshBase.OnRefreshListener2 selectRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
		public void onPullDownToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 下拉刷新
			selectPage = 1;
			selectedList.clear();
			getSelectData();
		}

		public void onPullUpToRefresh(
				PullToRefreshBase paramAnonymousPullToRefreshBase) {
			// 上拉加载
			selectPage = selectPage + 1;
			getSelectData();
		}
	};
	private AbsListView.OnScrollListener selectScrollListener = new AbsListView.OnScrollListener() {
		public void onScroll(AbsListView paramAnonymousAbsListView,
				int paramAnonymousInt1, int paramAnonymousInt2,
				int paramAnonymousInt3) {
		}

		public void onScrollStateChanged(AbsListView paramAnonymousAbsListView,
				int paramAnonymousInt) {
		}
	};

	// 关闭点歌界面
	public void close() {
		isshowing = false;
		if ((this.rootView != null) && (this.rootView.getVisibility() == 0)) {
			Animation animation = AnimationUtils.loadAnimation(cxt,
					R.anim.input_exit);
			rootView.startAnimation(animation);
			this.rootView.setVisibility(8);
			if(mgc!=null){
				mgc.recyle();//敏感词数据回收
			}
		}
	}

	/**
	 * @author： zhongxf
	 * @Description: 点击监听
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.room_ranklist_top:
			close();
			break;
		default:
			break;
		}

	}

}