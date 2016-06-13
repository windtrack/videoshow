package com.zhipu.chinavideo.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.fragment.AnchorZhuboFragment;
import com.zhipu.chinavideo.fragment.AnchorShouhuFragment;
import com.zhipu.chinavideo.fragment.AnchorGuizuFragment;
import com.zhipu.chinavideo.fragment.Giftfragment_hongdou;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.GetRedBeansUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

public class AnchorManager implements View.OnClickListener {
	private boolean isshowing = false;
	private FragmentActivity localActivity;
	private Context context;
	private ViewStub viewstub_anchor;
	private static AnchorManager instance;
	private String anchor_id;
	private String room_id;
	private View anchorview;
	/**
	 * 底部四个按钮
	 */
	private LinearLayout mTabBtnFrd;
	private LinearLayout mTabBtnAddress;
	private LinearLayout mTabBtnSettings;
	private TextView zhubo_anchor;
	private TextView shouhu_anchor;
	private TextView guizu_anchor;
	private AnchorShouhuFragment mTab02;
	private AnchorZhuboFragment mTab01;
	private AnchorGuizuFragment mTab03;
	private View room_anchor_top;
	
	public static GetRedBeansUtil getRedBeansUtil;
	private SharedPreferences sharedPreferences;
	private int timer = 180;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	public static AnchorManager getInstance() {
		if (instance == null)
			instance = new AnchorManager();
		return instance;
	}

	public void exit() {
		instance = null;
	}

	public void initAnchorManager(Context context, ViewStub viewstub_anchor,
			String anchor_id, String room_id) {
		this.context = context;
		this.viewstub_anchor = viewstub_anchor;
		this.anchor_id = anchor_id;
		this.room_id = room_id;
		sharedPreferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		getRedBeans();
	}

	public void showanchorView() {
		isshowing = true;
		if (anchorview == null) {
			localActivity = (FragmentActivity) this.context;
			viewstub_anchor.setLayoutResource(R.layout.anchor_layout);
			anchorview = viewstub_anchor.inflate();
			initViews();
		} else {
			this.anchorview.setVisibility(0);
		}
	}

	public void goneanchortview() {
		isshowing = false;
		if ((this.anchorview != null) && (this.anchorview.getVisibility() == 0)) {
			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.input_exit);
			anchorview.startAnimation(animation);
			this.anchorview.setVisibility(8);
		}
	}
	
	/**
	 * 每三分钟获取红豆数量
	 * 
	 * @author Jon_V
	 * 
	 */
	private void getRedBeans() {
		Log.i("sumao", "manager()...getRedBeans()....");
		getRedBeansUtil = new GetRedBeansUtil();
		this.getRedBeansUtil.getRedBeansTask(
				new GetRedBeansUtil.BeansRequest() {
					public void getRedBeans() {
						final String user_id = sharedPreferences.getString(
								APP.USER_ID, "");
						final String secret = sharedPreferences.getString(
								APP.SECRET, "");
						GetRedBeansUtil.getRedBeans(user_id, secret,
								new GetRedBeansUtil.GetBeansListener() {
									// 异常处理
									public void failure() {
										handler.sendEmptyMessage(6);
									}

									@Override
									public void hearts(String code,
											String hearts,String time) {
										Log.i("sumao", "Manager:code:"+code+",hearts:"+hearts+",timer:"+timer);
										if(1 == Integer.parseInt(code)) {
											Message msg = Message.obtain();
											msg.arg1 = Integer.parseInt(time);
											msg.what = 1;
											handler.sendMessage(msg);
										}else{
											AnchorManager.closeGetRedBean();
										}
									}
								});
					}

					// 异常处理
					public void failure() {
						handler.sendEmptyMessage(6);
					}
				}, 180000, 0, 20);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.i("sumao", "case 1");
				timer = msg.arg1;
				Message localmsg = Message.obtain();
				localmsg.what = 8;
				localmsg.arg1 = timer--;
				if (timer > 0) {
					sendMessageDelayed(localmsg, 1000);
				} else if (timer == 0) {
					sendEmptyMessage(2);
				}
				break;
			case 2:
//				getRedBeans();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.room_anchor_top:
			goneanchortview();
			break;
		case R.id.id_tab_bottom_friend:
			setTabSelection(0);
			break;
		case R.id.id_tab_bottom_contact:
			setTabSelection(1);
			break;
		case R.id.id_tab_bottom_setting:
			setTabSelection(2);
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	private void initViews() {
		// 获取vip和守护信息接口
		mTabBtnFrd = (LinearLayout) anchorview
				.findViewById(R.id.id_tab_bottom_friend);
		mTabBtnAddress = (LinearLayout) anchorview
				.findViewById(R.id.id_tab_bottom_contact);
		mTabBtnSettings = (LinearLayout) anchorview
				.findViewById(R.id.id_tab_bottom_setting);
		zhubo_anchor = (TextView) anchorview.findViewById(R.id.zhubo_anchor);
		shouhu_anchor = (TextView) anchorview.findViewById(R.id.shouhu_anchor);
		guizu_anchor = (TextView) anchorview.findViewById(R.id.guizu_anchor);
		room_anchor_top = anchorview.findViewById(R.id.room_anchor_top);
		mTabBtnFrd.setOnClickListener(this);
		mTabBtnAddress.setOnClickListener(this);
		mTabBtnSettings.setOnClickListener(this);
		room_anchor_top.setOnClickListener(this);
		fragmentManager = localActivity.getFragmentManager();
		setTabSelection(0);
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 */
	@SuppressLint("NewApi")
	private void setTabSelection(int index) {
		// 重置按钮
		resetBtn();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
					.setImageResource(R.drawable.icon_zhubo_on);
			zhubo_anchor.setTextColor(context.getResources().getColor(
					R.color.gitf_btn));
			if (mTab01 == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mTab01 = AnchorZhuboFragment.getIntance();
				mTab01.initAnchorManager(anchor_id, room_id);
				transaction.add(R.id.id_content, mTab01);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mTab01);
			}
			break;
		case 1:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			((ImageButton) mTabBtnAddress
					.findViewById(R.id.btn_tab_bottom_contact))
					.setImageResource(R.drawable.icon_shouhu_on);
			shouhu_anchor.setTextColor(context.getResources().getColor(
					R.color.gitf_btn));
			if (mTab02 == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mTab02 = AnchorShouhuFragment.getIntance();
				mTab02.initAnchorShouhuFragment(room_id, anchor_id);
				transaction.add(R.id.id_content, mTab02);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mTab02);
			}
			break;
		case 2:
			// 当点击了动态tab时，改变控件的图片和文字颜色
			((ImageButton) mTabBtnSettings
					.findViewById(R.id.btn_tab_bottom_setting))
					.setImageResource(R.drawable.icon_guizu_on);
			guizu_anchor.setTextColor(context.getResources().getColor(
					R.color.gitf_btn));
			if (mTab03 == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				mTab03 = AnchorGuizuFragment.getIntance();
				mTab03.initAnchorGuizuFragment(room_id, anchor_id);
				transaction.add(R.id.id_content, mTab03);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				transaction.show(mTab03);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	@SuppressLint("NewApi")
	private void hideFragments(FragmentTransaction transaction) {
		if (mTab01 != null) {
			transaction.hide(mTab01);
		}
		if (mTab02 != null) {
			transaction.hide(mTab02);
		}
		if (mTab03 != null) {
			transaction.hide(mTab03);
		}
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void resetBtn() {
		((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
				.setImageResource(R.drawable.icon_zhubo);
		((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
				.setImageResource(R.drawable.icon_shouhu);
		((ImageButton) mTabBtnSettings
				.findViewById(R.id.btn_tab_bottom_setting))
				.setImageResource(R.drawable.icon_guizu);
		zhubo_anchor.setTextColor(context.getResources().getColor(
				R.color.item_textcolor));
		shouhu_anchor.setTextColor(context.getResources().getColor(

		R.color.item_textcolor));
		guizu_anchor.setTextColor(context.getResources().getColor(
				R.color.item_textcolor));
	}

	public boolean getshowing() {
		return isshowing;
	}
	
	public static void closeGetRedBean() {
		if (getRedBeansUtil != null) {
			getRedBeansUtil.stopCheck();
		}
	}
}
