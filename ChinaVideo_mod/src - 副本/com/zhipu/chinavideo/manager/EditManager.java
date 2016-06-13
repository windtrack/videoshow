package com.zhipu.chinavideo.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.Pop_chatadapter;
import com.zhipu.chinavideo.entity.SiLiao;
import com.zhipu.chinavideo.fragment.PubChatFragment;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.KeyboardLayout;
import com.zhipu.chinavideo.util.MgcUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditManager implements View.OnClickListener {
	private SharedPreferences sharedPreferences;
	private static EditManager instance;
	private View inputview;
	private Context context;
	private RelativeLayout input_layout;
	private ViewStub liveinputStub;
	private TextView edit_tv;
	private ListView edit_listview;
	private ListView pri_edit_list;
	private RelativeLayout edit_rl;
	private Activity localActivity;
	private static EditText edit_shuru;
	private TextView send;
	private ImageView biaoqing_img;
	private ViewStub biaoqing_stub;
	private FaceManager facemanager;
	private CheckBox laba_checkbox;
	private String room_id;
	private boolean is_show = false;
	private KeyboardLayout input_bottom_layout;
	private static String chat_type = "2";
	// 免费广播数目
	private int gb_number = 0;
	private String text = "";
	private TextView tv_laba_number;
	String[] stringArr;
	//
	private RelativeLayout view1, view2;
	private CheckBox qiaoqiaohua_check_btn;
	// 用户列表
	private List<SiLiao> siliaos;
	// 聊天对象
	private PopupWindow chat_pop;
	private Pop_chatadapter pop_chatadapter;
	private TextView qqh_tv2;
	private TextView qqh_tv3;
	// 主播名称
	private String anchor_name;
	private String anchor_id;
	private SiLiao current_siliao;
	private SiLiao suoyouren;
	private SiLiao zhubo;

	private MgcUtil mgc = new MgcUtil();

	public static EditManager getInstance() {
		if (instance == null) {
			instance = new EditManager();
		}
		return instance;
	}

	public void exit() {
		instance = null;
	}

	public void initEditManager(Context context, ViewStub liveinputStub,
			RelativeLayout input_layout, String room_id, String anchor_id,
			String anchor_name) {

		this.context = context;
		this.liveinputStub = liveinputStub;
		this.input_layout = input_layout;
		this.room_id = room_id;
		this.anchor_id = anchor_id;
		this.anchor_name = anchor_name;

		sharedPreferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		current_siliao = new SiLiao();
		siliaos = new ArrayList<SiLiao>();
		suoyouren = new SiLiao();
		suoyouren.setId("");
		suoyouren.setName("所有人");
		current_siliao = suoyouren;
		siliaos.add(suoyouren);
		zhubo = new SiLiao();
		zhubo.setId(anchor_id);
		zhubo.setName(anchor_name);
		siliaos.add(zhubo);
		if (mgc == null) {
			mgc = new MgcUtil();
		}
		mgc.loadMgc(context);
	}

	public void showLiveInputView(String type, SiLiao siliao) {
		PubChatFragment.setchatlistgone();
		is_show = true;
		if (inputview == null) {
			this.liveinputStub.setLayoutResource(R.layout.chat_input);
			this.inputview = this.liveinputStub.inflate();
			findviewbyid();
		} else {
			this.inputview.setVisibility(0);
			if (edit_shuru != null) {
				// 让EditText获取焦点
				edit_shuru.setFocusable(true);
				edit_shuru.setFocusableInTouchMode(true);
				edit_shuru.requestFocus();
			}
		}
		Utils.showSoftInPut(context);
		if (facemanager != null) {
			facemanager.goneFaceView();
		}
		if ("1".equals(type)) {
			if (qiaoqiaohua_check_btn != null) {
				qiaoqiaohua_check_btn.setChecked(true);
			}
			// 如果私聊对象已经存在，把他删除
			if (siliao != null) {
				for (int i = 0; i < siliaos.size(); i++) {
					if (siliao.getName().equals(siliaos.get(i).getName())) {
						siliaos.remove(i);
					}
				}
				siliaos.add(siliao);
			}
			current_siliao = siliao;
		} else {
			if (qiaoqiaohua_check_btn != null
					&& qiaoqiaohua_check_btn.isChecked()) {
				current_siliao = zhubo;
			} else {
				current_siliao = suoyouren;
			}
		}

		qqh_tv2.setText(current_siliao.getName());
		
		if(mgc==null){
			mgc = new MgcUtil();
			mgc.loadMgc(context);
		}
		
	}

	public void goneliveInputview() {
		is_show = false;
		// 如果输入法弹出状态，关闭输入法
		if (Utils.getinputisshow(context, edit_shuru)) {
			Utils.hintKbTwo(context);
		}
		if ((this.inputview != null) && (this.inputview.getVisibility() == 0)) {
			this.inputview.setVisibility(8);
			input_layout.setVisibility(0);
			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.input_exit);
			inputview.startAnimation(animation);
		}
		PubChatFragment.setchatlistshow();
		if(mgc!=null){
			mgc.recyle();
		}
	}

	private void findviewbyid() {
		// TODO Auto-generated method stub
		localActivity = (Activity) this.context;
		// 显示dialog
		edit_listview = (ListView) localActivity.findViewById(R.id.edit_list);
		pri_edit_list = (ListView) localActivity
				.findViewById(R.id.pri_edit_list);
		edit_tv = (TextView) localActivity.findViewById(R.id.edit_tv);
		send = (TextView) localActivity.findViewById(R.id.send);
		edit_rl = (RelativeLayout) localActivity.findViewById(R.id.edit_rl);
		biaoqing_img = (ImageView) localActivity
				.findViewById(R.id.biaoqing_img);
		biaoqing_stub = (ViewStub) localActivity
				.findViewById(R.id.biaoqing_stub);
		laba_checkbox = (CheckBox) localActivity.findViewById(R.id.laba);
		qiaoqiaohua_check_btn = (CheckBox) localActivity
				.findViewById(R.id.qiaoqiaohua_check_btn);
		view1 = (RelativeLayout) localActivity.findViewById(R.id.view_1);
		view2 = (RelativeLayout) localActivity.findViewById(R.id.view_2);
		input_bottom_layout = (KeyboardLayout) localActivity
				.findViewById(R.id.input_layut);
		qqh_tv2 = (TextView) localActivity.findViewById(R.id.qqh_tv2);
		qqh_tv3 = (TextView) localActivity.findViewById(R.id.qqh_tv3);
		qqh_tv3.setOnClickListener(this);
		qqh_tv2.setOnClickListener(this);
		tv_laba_number = (TextView) localActivity
				.findViewById(R.id.tv_laba_number);
		send.setOnClickListener(this);
		edit_tv.setOnClickListener(this);
		biaoqing_img.setOnClickListener(this);
		edit_listview.getBackground().setAlpha(160);
		pri_edit_list.getBackground().setAlpha(160);
		edit_rl.getBackground().setAlpha(160);
		edit_shuru = (EditText) localActivity.findViewById(R.id.edit_shuru);
		edit_shuru.setFocusable(true);
		edit_shuru.setFocusableInTouchMode(true);
		edit_shuru.requestFocus();
		facemanager = new FaceManager();
		facemanager.initFaceManager(context, biaoqing_stub);

		edit_shuru.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (facemanager != null) {
					facemanager.goneFaceView();
				}
				return false;
			}
		});
		laba_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					view1.setVisibility(8);
					view2.setVisibility(0);
					get_gb_number();
				} else {
					view1.setVisibility(0);
					view2.setVisibility(8);
					tv_laba_number.setVisibility(View.GONE);
				}
			}
		});
		qiaoqiaohua_check_btn
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean ischeck) {
						// TODO Auto-generated method stub
						if (ischeck) {
							edit_listview.setVisibility(8);
							pri_edit_list.setVisibility(0);
							chat_type = "1";
							if ("所有人".equals(qqh_tv2.getText().toString()
									.trim())) {
								if (siliaos.size() > 1) {
									current_siliao = siliaos.get(1);
									qqh_tv2.setText(current_siliao.getName());
								}

							}
						} else {
							edit_listview.setVisibility(0);
							pri_edit_list.setVisibility(8);
							chat_type = "2";
						}
					}
				});
		// // 监听关闭输入法
		// input_bottom_layout
		// .setOnSizeChangedListener(new onSizeChangedListener() {
		// @Override
		// public void onChanged(boolean showKeyboard) {
		// // TODO Auto-generated method stub
		// if (showKeyboard) {
		// // Log.i("lvjian",
		// // "--------------------------------显示输入法----------------------");
		// } else {
		// Log.i("lvjian",
		// "--------------------------------隐藏输入法----------------------");
		// if (is_show) {
		// goneliveInputview();
		// }
		// }
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edit_tv:
			// 调用返回键
			((LiveRoomActivity) context).onBackPressed();
			// goneliveInputview();
			break;
		// 发送按钮
		case R.id.send:
			if (laba_checkbox.isChecked()) {
				if (mgc == null) {
					mgc = new MgcUtil();
					mgc.loadMgc(context);
				}
				text = mgc.getmgcarray(edit_shuru.getText().toString());
				if (Utils.isEmpty(text)) {
					Utils.showToast(context, "广播消息不能为空哦！");
				} else {
					// 有免费广播发免费广播，没有免费广播扣钱
					if (gb_number > 0) {
						Send_Gb(text, "1");
					} else {
						Send_Gb(text, "0");
					}
				}
				// 发广播
			} else {
				if (mgc == null) {
					mgc = new MgcUtil();
					mgc.loadMgc(context);
				}

				LiveRoomActivity
						.sendmsg(
								mgc.getmgcarray(edit_shuru.getText().toString()
										.trim()), edit_shuru, chat_type,
								current_siliao);
			}
			edit_shuru.setFocusable(true);
			edit_shuru.setFocusableInTouchMode(true);
			edit_shuru.requestFocus();
			break;
		// 打开表情表情
		case R.id.biaoqing_img:
			if (((Activity) context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
				// 隐藏软键盘
				((Activity) context).getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				facemanager.showfaceView();
			}
			if (Utils.getinputisshow(context, edit_shuru)) {
				// 关闭输入法
				Utils.hintKbTwo(context);
				facemanager.showfaceView();
			} else {
				facemanager.showfaceView();
			}
			break;
		case R.id.qqh_tv2:
			creatPopWindow();
			int[] location = new int[2];
			view1.getLocationOnScreen(location);
			chat_pop.showAtLocation(view1, Gravity.NO_GRAVITY,
					location[0] + 50, location[1] - chat_pop.getHeight());
			break;
		case R.id.qqh_tv3:
			if (qiaoqiaohua_check_btn.isChecked()) {
				qiaoqiaohua_check_btn.setChecked(false);
			} else {
				qiaoqiaohua_check_btn.setChecked(true);
			}
			break;
		default:
			break;
		}
	}

	public static void setEditText(String text) {
		edit_shuru.append(text);
	}

	public ListView getEditListView() {
		if (edit_listview != null) {
			return edit_listview;
		}
		return null;
	}

	public ListView getPri_edit_list() {
		if (pri_edit_list != null) {
			return pri_edit_list;
		}
		return null;
	}

	/**
	 * 发送广播
	 * 
	 * @author Jon_V
	 * 
	 */
	public void Send_Gb(final String text, final String type) {
		Runnable sendgbrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.send_gb(
							sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""),
							room_id, text, type);
					JSONObject obj = new JSONObject(result);
					int state = obj.getInt("s");
					if (state == 1) {
						JSONObject data = obj.getJSONObject("data");
						String msg = data.getString("msg");
						Message message = new Message();
						message.what = 1;
						message.obj = msg;
						handler.sendMessage(message);
						if (type.equals("1")) {
							gb_number = gb_number - 1;
						}
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(sendgbrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (gb_number > 0) {
					tv_laba_number.setVisibility(View.VISIBLE);
					tv_laba_number.setText("免费喇叭 X " + gb_number);
				} else {
					tv_laba_number.setVisibility(View.VISIBLE);
					tv_laba_number.setText("喇叭5万乐币1次");
				}
				String m = msg.obj.toString();
				Utils.showToast(context, m);
				edit_shuru.setText("");
				break;
			case 2:
				Utils.showToast(context, "发送广播失败");
				break;
			case 3:
				if (gb_number > 0) {
					tv_laba_number.setVisibility(View.VISIBLE);
					tv_laba_number.setText("免费喇叭 X " + gb_number);
				} else {
					tv_laba_number.setVisibility(View.VISIBLE);
					tv_laba_number.setText("喇叭5万乐币1次");
				}
				break;
			default:
				break;
			}
		};
	};

	public boolean getshowing() {
		return is_show;
	}

	/**
	 * 获取免费广播数
	 */
	private void get_gb_number() {
		Runnable followrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.getgbnumber(
							sharedPreferences.getString(APP.USER_ID, ""),
							sharedPreferences.getString(APP.SECRET, ""));
					// Log.i("lvjian", "免费广播数目-----------》" + result);
					JSONObject obj = new JSONObject(result);
					int state = obj.getInt("s");
					if (state == 1) {
						gb_number = obj.getInt("data");
					} else {

					}
					handler.sendEmptyMessage(3);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(followrun);
	}

	/**
	 * 创建礼物数量列表
	 */
	private void creatPopWindow() {
		View v = LayoutInflater.from(context).inflate(
				R.layout.gift_num_popwindow, null);
		ListView lv = (ListView) v.findViewById(R.id.gift_num_listView);
		pop_chatadapter = new Pop_chatadapter(context, siliaos);
		lv.setAdapter(pop_chatadapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (siliaos.get(arg2) != null) {
					current_siliao = siliaos.get(arg2);
					qqh_tv2.setText(siliaos.get(arg2).getName());
					if (arg2 == 0) {
						qiaoqiaohua_check_btn.setChecked(false);
					}
				}
				chat_pop.dismiss();
			}
		});
		chat_pop = new PopupWindow(v, 250, 300);
		chat_pop.setOutsideTouchable(true);
		chat_pop.setTouchable(true);
		chat_pop.setFocusable(true);
		chat_pop.setBackgroundDrawable(new ColorDrawable(0));
	}

}
