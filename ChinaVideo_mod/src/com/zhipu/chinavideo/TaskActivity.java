package com.zhipu.chinavideo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.adapter.TaskAdapter;
import com.zhipu.chinavideo.entity.Tasks;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskActivity extends Activity implements OnClickListener {
	private ListView task_list;
	private ImageView back;
	private View loding_view;
	private String user_id = "0";
	private TaskAdapter adapter;
	private String secret;
	private TextView title;
	private SharedPreferences preferences;
	private List<Tasks> task_lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskactivity);
		task_lists = new ArrayList<Tasks>();
		task_list = (ListView) findViewById(R.id.task_list);
		adapter = new TaskAdapter(TaskActivity.this, task_lists);
		task_list.setAdapter(adapter);
		back = (ImageView) findViewById(R.id.title_back);
		loding_view = findViewById(R.id.task_loading);
		title = (TextView) findViewById(R.id.title_text);
		title.setText("任务中心");
		back.setOnClickListener(this);
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		user_id = preferences.getString(APP.USER_ID, "");
		secret = preferences.getString(APP.SECRET, "");
		if (user_id == null || "".equals(user_id)) {
			user_id = "0";
		}
		// 触发礼物事件
		getupdatetaskreward(user_id, secret, "1");// 新人报道
		getupdatetaskreward(user_id, secret, "5");// 送出1颗红豆
		getupdatetaskreward(user_id, secret, "4");// 送出一朵玫瑰
		getTaskList();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取任务列表
	 */
	private void getTaskList() {
		// TODO Auto-generated method stub
		Runnable taskrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.gettasklist(user_id, secret);
//					Log.i("lvjian", "taskresult---------------------->"
//							+ result);
					JSONObject obj = new JSONObject(result);
					int a = obj.getInt("s");
					if (a == 1) {
						JSONArray data = obj.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							Gson gson = new Gson();
							Tasks task = gson.fromJson(data.getJSONObject(i)
									.toString(), Tasks.class);
							task_lists.add(task);
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
		ThreadPoolWrap.getThreadPool().executeTask(taskrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				loding_view.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Utils.showToast(TaskActivity.this, "获取任务列表失败！");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 修改昵称后,改变任务中状态更新
	 */
	private void getupdatetaskreward(final String userId, final String secret,
			final String task_id) {
		// TODO Auto-generated method stub
		Runnable resetnichengtask = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.update_task_reward(userId, secret,
							task_id);
					JSONObject obj = new JSONObject(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(resetnichengtask);
	}

	/**
	 * 友盟统计
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
