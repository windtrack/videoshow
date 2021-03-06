package com.zhipu.chinavideo.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zhipu.chinavideo.FeedbackActivity;
import com.zhipu.chinavideo.MainTabActivity;
import com.zhipu.chinavideo.MobileVerification;
import com.zhipu.chinavideo.MyAnchor_Activity;
import com.zhipu.chinavideo.HistoryActivity;
import com.zhipu.chinavideo.LoginActivity;
import com.zhipu.chinavideo.MyCarActivity;
import com.zhipu.chinavideo.MyInformationActivity;
import com.zhipu.chinavideo.MyPhotoAlbumActivity;
import com.zhipu.chinavideo.QianDaoActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.SettingActivity;
import com.zhipu.chinavideo.Strongbox_Activity;
import com.zhipu.chinavideo.TaskActivity;
import com.zhipu.chinavideo.broadcast.ReceiveInterface;
import com.zhipu.chinavideo.broadcast.SteathBroadCastReceive;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.db.HandlerCmd;
import com.zhipu.chinavideo.rechargecenter.RechargeActivity;
import com.zhipu.chinavideo.roundperson.LocationDialog;
import com.zhipu.chinavideo.roundperson.RoundPersonActivity;
import com.zhipu.chinavideo.roundperson.RoundPersonUtil;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.rpc.RpcRoutine;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import ctv.sdk.Config;
import ctv.sdk.GameBilling;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersonalCenterFragment extends Fragment implements OnClickListener {
	private View rootView;
	private RelativeLayout my_guard;
	private SharedPreferences sharedPreferences;
	private CircularImage mine_icon;
	public ImageLoader mImageLoader = null;
	public DisplayImageOptions mOptions;
	private TextView mine_name;
	private RelativeLayout my_center;
	private ImageView ceshi;
	private RelativeLayout shoujiyanzheng;
	private RelativeLayout task_center;
	private RelativeLayout guli_rl;
	private RelativeLayout chongzhi_rl;
	private RelativeLayout rl_qiandao;
	private RelativeLayout rl_mycar;
	private RelativeLayout baoxianxiang_rl;
	private RelativeLayout roundPerson;// 周边达人的按钮
	private RelativeLayout rl_myPhotoAlbum;//我的相册
	private RelativeLayout rl_feedback;//意见反馈
	
	private LinearLayout histoty;
	// 个人中心信息
	private String taskinfo_current;
	private String taskinfo_total;
	private String sign_days;
	private String online;
	private int vip_lv = 0;
	private String cost_level;
	private ImageView cost_level_img;
	private TextView task_current;
	private TextView anchor_online;
	private LinearLayout myprerogative_rl;
	public static boolean reseticon = false;
	private ImageView ll_line;
	private ImageView ll_linephoto;

	// private RelativeLayout rl_shouchongliwu;
	// private ImageView shouchognliwu_line;
	private LinearLayout l_my_zhubo;
	private ImageView mine_viplevel;
	private ImageView line_dengji;

	private int is_stealth = 0;// 是否是神秘人 0不是神秘人 1是神秘人

	private SteathBroadCastReceive receive;// 广播接收器

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
		getActivity().unregisterReceiver(receive);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_personalcenter,
					container, false);
			ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(
					getActivity()).threadPoolSize(1)
					.memoryCache(new WeakMemoryCache()).build();
			mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(localImageLoaderConfiguration);
			mOptions = new DisplayImageOptions.Builder()
					.bitmapConfig(Bitmap.Config.RGB_565)
					.showStubImage(R.drawable.icon)
					.showImageForEmptyUri(R.drawable.icon)
					.showImageOnFail(R.drawable.icon)
					.showImageOnLoading(R.drawable.icon).cacheOnDisc()
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
			sharedPreferences = getActivity().getSharedPreferences(APP.MY_SP,
					Context.MODE_PRIVATE);
			my_guard = (RelativeLayout) rootView.findViewById(R.id.my_guard);
			mine_icon = (CircularImage) rootView.findViewById(R.id.mine_icon);
			mine_name = (TextView) rootView.findViewById(R.id.mine_name);
			my_center = (RelativeLayout) rootView.findViewById(R.id.my_center);
			guli_rl = (RelativeLayout) rootView.findViewById(R.id.guli_rl);
			histoty = (LinearLayout) rootView.findViewById(R.id.history);
			mine_viplevel = (ImageView) rootView
					.findViewById(R.id.mine_viplevel);
			line_dengji = (ImageView) rootView.findViewById(R.id.line_dengji);
			myprerogative_rl = (LinearLayout) rootView
					.findViewById(R.id.myprerogative);
			myprerogative_rl.setOnClickListener(this);
			cost_level_img = (ImageView) rootView
					.findViewById(R.id.mine_costlevel);
			task_current = (TextView) rootView.findViewById(R.id.task_current);
			anchor_online = (TextView) rootView
					.findViewById(R.id.anchor_online);
			chongzhi_rl = (RelativeLayout) rootView
					.findViewById(R.id.chongzhi_rl);
			rl_qiandao = (RelativeLayout) rootView
					.findViewById(R.id.rl_qiandao);
			rl_mycar = (RelativeLayout) rootView.findViewById(R.id.rl_mycar);
			baoxianxiang_rl = (RelativeLayout) rootView
					.findViewById(R.id.baoxianxiang_rl);
			
			
			rl_myPhotoAlbum  = (RelativeLayout) rootView.findViewById(R.id.rl_photoalbum);
			rl_myPhotoAlbum.setOnClickListener(this);
			
			
			rl_feedback  = (RelativeLayout) rootView.findViewById(R.id.rl_feedback);
			rl_feedback.setOnClickListener(this);
			
			
			ll_linephoto = (ImageView) rootView.findViewById(R.id.ll_line_photo);
			if(GlobalData.getInstance().isAnchor()){
				rl_myPhotoAlbum.setVisibility(View.VISIBLE);
				ll_linephoto.setVisibility(View.VISIBLE);
			}else{
				rl_myPhotoAlbum.setVisibility(View.GONE);
				ll_linephoto.setVisibility(View.GONE);
			}
			
			
			ll_line = (ImageView) rootView.findViewById(R.id.ll_line);
			
	
			// rl_shouchongliwu = (RelativeLayout)
			// rootView.findViewById(R.id.shouchongliwu);
			// shouchognliwu_line = (ImageView)
			// rootView.findViewById(R.id.shouchognliwu_line);
			l_my_zhubo = (LinearLayout) rootView.findViewById(R.id.l_my_zhubo);
			l_my_zhubo.getBackground().setAlpha(153);
			baoxianxiang_rl.setOnClickListener(this);
			rl_mycar.setOnClickListener(this);
			rl_qiandao.setOnClickListener(this);
			chongzhi_rl.setOnClickListener(this);

				
			// 新版隐藏
			// shouchognliwu_line.setVisibility(View.INVISIBLE);
			// rl_shouchongliwu.setVisibility(View.INVISIBLE);

			task_center = (RelativeLayout) rootView
					.findViewById(R.id.task_center);
			shoujiyanzheng = (RelativeLayout) rootView
					.findViewById(R.id.shoujiyanzheng);
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				String url = sharedPreferences.getString(APP.USER_ICON, "");
				mImageLoader.displayImage(url, mine_icon, mOptions);
				mine_name
						.setText(sharedPreferences.getString(APP.NICKNAME, ""));
			}
			my_guard.setOnClickListener(this);
			my_center.setOnClickListener(this);
			task_center.setOnClickListener(this);
			guli_rl.setOnClickListener(this);
			histoty.setOnClickListener(this);
			shoujiyanzheng.setOnClickListener(this);
			ceshi = (ImageView) rootView.findViewById(R.id.ceshi);
			ceshi.setOnClickListener(this);
			String user_id = sharedPreferences.getString(APP.USER_ID, "");
			String user_key = sharedPreferences.getString(APP.SECRET, "");
			roundPerson = (RelativeLayout) rootView
					.findViewById(R.id.round_person);// 周边达人
			roundPerson.setOnClickListener(this);
			initData(user_id, user_key, user_id);
		}
		//进入该Fragment的时候查询一次神秘人
		getUserInfo();// 查询是否是神秘人

		receive = new SteathBroadCastReceive(new ReceiveInterface() {
			@Override
			public void receive() {
				getUserInfo();// 查询用户是否是神秘人
			}
		});
		//注册该广播主要是为了防止用户重新登录之后，获取是否是神秘人
		getActivity().registerReceiver(receive,
				new IntentFilter(SteathBroadCastReceive.action));

		return rootView;
	}

	/**
	 * 查询当前登录用户是否是神秘人
	 */
	private void getUserInfo() {
		Runnable GetStealth = new Runnable() {
			public void run() {
				try {
					String result = Utils.getuserinfo(
							sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""));
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						is_stealth = data.getInt("is_stealth");
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(GetStealth);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			//意见反馈
			case R.id.rl_feedback:
				if(GlobalData.getInstance().checkLogin()){
					startActivity(new Intent(getActivity(),FeedbackActivity.class));
				}else{
					callLogin() ;
				}
				break ;
		//我的相册
		case R.id.rl_photoalbum:
			if(GlobalData.getInstance().checkLogin()){
				Intent in = new Intent(getActivity(), MyPhotoAlbumActivity.class);
				in.putExtra("anchorId", GlobalData.getInstance().getSharedPreferences().getString(APP.USER_ID, ""));
				startActivity(in);
			}else{
				callLogin() ;
			}
			break ;
		case R.id.my_guard:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				Intent in = new Intent(getActivity(), MyAnchor_Activity.class);
				startActivity(in);
			} else {
				callLogin();
			}
			break;
		case R.id.my_center:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				Intent intent = new Intent(getActivity(),
						MyInformationActivity.class);
				startActivity(intent);
			} else {
				callLogin();
			}
			break;

		case R.id.task_center:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				startActivity(new Intent(getActivity(), TaskActivity.class));
			} else {
				callLogin();
			}

			break;
		case R.id.shoujiyanzheng:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				Intent intent1 = new Intent(getActivity(),
						MobileVerification.class);
				startActivityForResult(intent1, 100);
			} else {
				callLogin();
			}
			break;
		case R.id.guli_rl:
			Uri uri = Uri.parse("market://details?id="
					+ getActivity().getPackageName());
			Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent2);
			// Intent intent2=new Intent(getActivity(),
			// DoubleHuoDongActivity.class);
			// startActivity(intent2);
			break;
		case R.id.chongzhi_rl:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				startActivity(new Intent(getActivity(),
						MyInformationActivity.class));
			} else {
				callLogin();
			}
			break;
		case R.id.history:
			startActivity(new Intent(getActivity(), HistoryActivity.class));
			break;
		case R.id.myprerogative:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				startActivity(new Intent(getActivity(), RechargeActivity.class));
				// Intent intent=new Intent(getActivity(),
				// HuoDongActivity.class);
				// intent.putExtra("url","http://192.168.1.176/mohd/mfoolday20160401");
				// intent.putExtra("title","愚人节活动");
				// startActivity(intent);
			} else {
				callLogin();
			}
			break;
		case R.id.ceshi:
			Intent in = new Intent(getActivity(), SettingActivity.class);
			startActivityForResult(in, 100);
			break;
		case R.id.rl_qiandao:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {

				Intent intent = new Intent(MainTabActivity.qiandao);
				getActivity().sendBroadcast(intent);

			} else {
				callLogin();
			}
			break;
		case R.id.rl_mycar:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				startActivity(new Intent(getActivity(), MyCarActivity.class));
			} else {
				callLogin();
			}
			break;
		case R.id.round_person:// 周边达人
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {

				final Intent intent = new Intent(getActivity(),
						RoundPersonActivity.class);
				intent.putExtra("IS_STEALTH", is_stealth);
				boolean flag = sharedPreferences.getBoolean(
						RoundPersonUtil.ROUND_IS_SHOW, true);
				if (flag) {
					final LocationDialog ld = new LocationDialog();
					ld.setOnCancleClickListen(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							ld.closeDialog();
						}
					});

					ld.setOnSureClickListen(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							CheckBox cb = ld.getCheckBox();
							if (cb.isChecked()) {
								sharedPreferences
										.edit()
										.putBoolean(
												RoundPersonUtil.ROUND_IS_SHOW,
												false).commit();
							} else {
								sharedPreferences
										.edit()
										.putBoolean(
												RoundPersonUtil.ROUND_IS_SHOW,
												true).commit();
							}
							startActivity(intent);
							ld.closeDialog();
						}
					});
					ld.show(getActivity());
				} else {
					startActivity(intent);
				}
			} else {
				callLogin();
			}
			break;
		case R.id.baoxianxiang_rl:
			if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
				Intent intent = new Intent(getActivity(),
						Strongbox_Activity.class);
				startActivity(intent);
			} else {
				callLogin();
			}
			break;
		// 领取首充礼包
		// case R.id.shouchongliwu:
		// if (sharedPreferences.getString(APP.IS_LOGIN, "").equals("true")) {
		// ShowMsg();
		// } else {
		// callLogin() ;
		// }
		// break;
		default:
			break;
		}
	}

	private void callLogin() {

		if (Config.Use_3rd_LogIn) {
			GameBilling.getInstance().Login(getActivity(), handler);
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivityForResult(intent, 100);
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (!mine_name.getText().toString()
				.equals(sharedPreferences.getString(APP.NICKNAME, ""))) {
			mine_name.setText(sharedPreferences.getString(APP.NICKNAME, ""));
		}

		if (reseticon) {
			String url = sharedPreferences.getString(APP.USER_ICON, "");
			mImageLoader.displayImage(url, mine_icon, mOptions);
			reseticon = false;
		}
		/**
		 * 判断手机是否验证
		 */
		if (Utils.isEmpty(sharedPreferences.getString(APP.PHONE, ""))) {
			ll_line.setVisibility(0);
			shoujiyanzheng.setVisibility(0);
		} else {
			ll_line.setVisibility(8);
			shoujiyanzheng.setVisibility(8);
		}
		if ("0".equals(sharedPreferences.getString(APP.SHOUCHONG, ""))) {
			// shouchognliwu_line.setVisibility(0);
			// rl_shouchongliwu.setVisibility(0);
		} else {
			// shouchognliwu_line.setVisibility(8);
			// rl_shouchongliwu.setVisibility(8);
		}
		if(GlobalData.getInstance().isAnchor()){
			rl_myPhotoAlbum.setVisibility(View.VISIBLE);
			ll_linephoto.setVisibility(View.VISIBLE);
		}else{
			rl_myPhotoAlbum.setVisibility(View.GONE);
			ll_linephoto.setVisibility(View.GONE);
		}
		// shouchognliwu_line.setVisibility(View.INVISIBLE);
		// rl_shouchongliwu.setVisibility(View.INVISIBLE);
		super.onResume();
	}

	private void setUserInfo() {
		// TODO Auto-generated method stub
		String url = sharedPreferences.getString(APP.USER_ICON, "");
		mImageLoader.displayImage(url, mine_icon, mOptions);
		mine_name.setText(sharedPreferences.getString(APP.NICKNAME, ""));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (20 == resultCode) {
			setUserInfo();
			initData(sharedPreferences.getString(APP.USER_ID, ""),
					sharedPreferences.getString(APP.SECRET, ""),
					sharedPreferences.getString(APP.USER_ID, ""));

		} else if (21 == resultCode) {
			ll_line.setVisibility(8);
			shoujiyanzheng.setVisibility(8);

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initData(final String user_id, final String secret,
			final String tuid) {
		Runnable personinforun = new Runnable() {
			public void run() {
				try {
					String s = Utils.get_personInfo(user_id, secret, tuid);
					JSONObject ss = new JSONObject(s);
					int i = ss.getInt("s");
					if (i == 1) {
						JSONObject jsonObject = ss.getJSONObject("data");
						JSONObject taskinfo = jsonObject
								.getJSONObject("task_info");
						taskinfo_current = taskinfo.getString("current");
						taskinfo_total = taskinfo.getString("total");
						sign_days = jsonObject.getString("sign_days");
						online = jsonObject.getString("online");
						JSONObject userinfo = jsonObject
								.getJSONObject("userinfo");
						vip_lv = userinfo.getInt("vip_lv");
						cost_level = userinfo.getString("cost_level");
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(personinforun);
	}

	// private Handler handler2 = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	// case HandlerCmd.HandlerCmd_CallLoginSuccess:
	// Log.i("lvjian", "用户登录成功");
	//
	// break;
	// case HandlerCmd.HandlerCmd_CallLoginFailed:
	// case HandlerCmd.HandlerCmd_CallLoginException:
	//
	//
	// Log.i("lvjian", "用户登录失败或者异常");
	// if (Utils.isEmpty(GlobalData.getInstance().mLoginErrorInfo)) {
	// Utils.showToast(getActivity(), "网络条件差,请检查网络！");
	// } else {
	// Utils.showToast(getActivity(), GlobalData.getInstance().mLoginErrorInfo);
	// }
	// break;
	// case HandlerCmd.HandlerCmd_GetUserInfoSuccess:
	// Log.i("lvjian", "获取用户信息成功");
	// Toast.makeText(getActivity(), "登录成功", 100).show();
	// getActivity().setResult(20, null);
	// // finish();
	// break;
	// case HandlerCmd.HandlerCmd_GetUserInfoFailed:
	//
	// Log.i("lvjian", "获取用户信息失败或异常");
	// break;
	// default:
	// break;
	// }
	// };
	// };

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HandlerCmd.HandlerCmd_Call3rdLoginSuccess:

				if (GlobalData.getInstance().getmThirdLoginInfo().username != ""
						&& GlobalData.getInstance().getmThirdLoginInfo().userToken != "") {
					String deviceToken = GlobalData.getInstance()
							.getSharedPreferences()
							.getString(APP.DEVICETOKEN, "");
					RpcRoutine.getInstance()
							.addRpc(RpcEvent.Call3rdLogin,
									handler,
									GlobalData.getInstance()
											.getmThirdLoginInfo().username,
									GlobalData.getInstance()
											.getmThirdLoginInfo().userToken,
									50, deviceToken);
					// RpcRoutine.getInstance().addRpc(RpcEvent.CallQQLogin,
					// handler, openid, token,50,deviceToken, wx_qq_channel);
				}

				break;
			case 1:
				cost_level_img.setVisibility(0);
				mine_viplevel.setVisibility(0);
				line_dengji.setVisibility(0);
				APP.setCost_level(cost_level, cost_level_img, getActivity());
				mine_viplevel.setImageResource(APP.parseVIPLevel(vip_lv));

				String str = "(" + taskinfo_current + "/" + taskinfo_total
						+ ")";

				SpannableStringBuilder style1 = new SpannableStringBuilder(str);
				style1.setSpan(new ForegroundColorSpan(Color.RED), 1,
						(1 + taskinfo_current.length()),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				task_current.setText(style1);
				SpannableStringBuilder style = new SpannableStringBuilder(
						"正在直播" + online);
				style.setSpan(new ForegroundColorSpan(Color.RED), 4,
						(4 + online.length()),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				anchor_online.setText(style);
				break;
			case 2:
				task_current.setText("");
				anchor_online.setText("");
				cost_level_img.setVisibility(4);
				mine_viplevel.setVisibility(4);
				line_dengji.setVisibility(4);

				break;
			case 3:
				String m = "";
				m = msg.obj.toString();
				repeatlogin_dialog(m);
				break;
			case 4:
				repeatlogin_dialog("领取异常！");
				break;
			default:
				break;
			}
		};
	};

	private void ShowMsg() {
		final AlertDialog localAlertDialog = new AlertDialog.Builder(
				getActivity()).create();
		localAlertDialog.show();
		Window localWindow = localAlertDialog.getWindow();
		View localView = ((Activity) getActivity()).getLayoutInflater()
				.inflate(R.layout.get_shouchonggift, null);
		Button shouchong_b1 = (Button) localView
				.findViewById(R.id.shouchong_b1);
		TextView moneynotenoth_title = (TextView) localView
				.findViewById(R.id.shouchongmsg);
		Button shouchong_b2 = (Button) localView
				.findViewById(R.id.shouchong_b2);
		shouchong_b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getshouchonggift("1");
				localAlertDialog.dismiss();
			}
		});
		shouchong_b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getshouchonggift("2");
				localAlertDialog.dismiss();
			}
		});
		localWindow.setContentView(localView);
	}

	/**
	 * 领取首充礼物
	 */
	private void getshouchonggift(final String type) {
		// TODO Auto-generated method stub
		Runnable getshouchonggift = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.getshouchonggift(
							sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""), type);
					// Log.i("lvjian", "领取首冲礼包====>" + result);
					JSONObject obj = new JSONObject(result);
					JSONObject data1 = obj.getJSONObject("data");
					String data2 = data1.getString("data");
					Message ms = new Message();
					ms.what = 3;
					ms.obj = data2;
					handler.sendMessage(ms);
					int a = obj.getInt("s");
					if (a == 1) {
					} else {
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getshouchonggift);
	}

	private void repeatlogin_dialog(String text) {
		final Dialog dialog_re = new AlertDialog.Builder(getActivity())
				.create();
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(getActivity()).inflate(
				R.layout.repeatlogin_dialog, null));
		Button releat_login_queding = (Button) localWindow
				.findViewById(R.id.releat_login_queding);
		TextView moneynotenoth_title = (TextView) localWindow
				.findViewById(R.id.moneynotenoth_title);
		moneynotenoth_title.setText(text);
		releat_login_queding.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				dialog_re.cancel();
			}
		});
	}

}
