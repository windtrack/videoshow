package com.zhipu.chinavideo.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhipu.chinavideo.GuardActivity;
import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.FansAdapter;
import com.zhipu.chinavideo.adapter.GuardAdapter;
import com.zhipu.chinavideo.entity.Fans;
import com.zhipu.chinavideo.entity.Guard;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

public class ShouHuManager implements View.OnClickListener {
	private boolean isshowing = false;
	private Context context;
	private ViewStub viewstub_shouhu;
	private static ShouHuManager instance;
	private String anchor_id;
	private String room_id;
	private View shouview;
	private SharedPreferences sharedPreferences;
	private FansAdapter adapter;
	private String user_id;
	private String secret;
	private View room_ranklist_top;
	private GridView shouhu_lsit;
	private List<Guard> guards;
	private int sum = 0;
	private int online_num = 0;
	private GuardAdapter guardAdapter;
	private View room_guardlist_top;
	private TextView kaitongshouhu_btn;
	private TextView shouhu_list_num;

	public static ShouHuManager getInstance() {
		if (instance == null)
			instance = new ShouHuManager();
		return instance;
	}

	public void exit() {
		instance = null;
	}

	public void initShouHuManager(Context context, ViewStub viewstub_shouhu,
			String anchor_id, String room_id) {
		this.context = context;
		this.viewstub_shouhu = viewstub_shouhu;
		this.anchor_id = anchor_id;
		this.room_id = room_id;
		sharedPreferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString(APP.USER_ID, "");
		secret = sharedPreferences.getString(APP.SECRET, "");

	}

	public void showshouhulistView() {
		isshowing = true;
		if (shouview == null) {
			this.viewstub_shouhu.setLayoutResource(R.layout.shouhumanager);
			this.shouview = this.viewstub_shouhu.inflate();
			initViews();
		} else {
			this.shouview.setVisibility(0);
		}
	}

	public void goneshouhulistview() {
		isshowing = false;
		if ((this.shouview != null) && (this.shouview.getVisibility() == 0)) {
			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.input_exit);
			shouview.startAnimation(animation);
			this.shouview.setVisibility(8);
		}
	}

	private void initViews() {
		shouhu_lsit = (GridView) shouview.findViewById(R.id.shouhu_lsit);
		room_guardlist_top = shouview.findViewById(R.id.room_guardlist_top);
		kaitongshouhu_btn = (TextView) shouview
				.findViewById(R.id.kaitongshouhu_btn);
		shouhu_list_num = (TextView) shouview
				.findViewById(R.id.shouhu_list_num);
		kaitongshouhu_btn.setOnClickListener(this);
		room_guardlist_top.setOnClickListener(this);
		guards = new ArrayList<Guard>();
		guardAdapter = new GuardAdapter(guards, context);
		shouhu_lsit.setAdapter(guardAdapter);
		shouhu_lsit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LiveRoomActivity.shoushouhudialog(guards.get(arg2));
			}
		});
		getshouhulist();

	}

	public boolean getshowing() {
		return isshowing;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.room_guardlist_top:
			goneshouhulistview();
			break;
		case R.id.kaitongshouhu_btn:
			Intent in = new Intent(context, GuardActivity.class);
			in.putExtra("anchor_id", anchor_id);
			in.putExtra("room_id", room_id);
			context.startActivity(in);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取本场直播的粉丝
	 * 
	 * @author lvjian
	 * 
	 */
	private void getshouhulist() {

		Runnable getlivefansrun = new Runnable() {
			@Override
			public void run() {
				try {
					Gson gson = new Gson();
					String s = Utils.getguardlistnew(anchor_id, room_id);
					Log.i("lvjian", "获取守护列表-------------》" + s);
					JSONObject obj = new JSONObject(s);
					int state = obj.getInt("s");
					JSONObject data = obj.getJSONObject("data");
					if (state == 1) {
						sum = data.getInt("sum");
						online_num = data.getInt("online_num");
						JSONArray data1 = data.getJSONArray("data");
						for (int i = 0; i < data1.length(); i++) {
							JSONObject j = (JSONObject) data1.get(i);
							Guard guard = gson.fromJson(j.toString(),
									Guard.class);
							guards.add(guard);
						}
						handler.sendEmptyMessage(1);
					} else if (state == 0) {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getlivefansrun);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				Log.i("lvjian", "guards-------->" + guards);
				guardAdapter.notifyDataSetChanged();
				shouhu_list_num.setText("TA的守护" + "(" + online_num + "/" + sum
						+ ")");
				break;
			case 2:
				Log.i("lvjian",
						"-----------------获取本场粉丝失败或异常---------------------");
				break;

			default:
				break;
			}
		};
	};

}
