package com.zhipu.chinavideo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.adapter.HisItemAdapter;
import com.zhipu.chinavideo.callback.HisListener;
import com.zhipu.chinavideo.db.MyOpenHelper;
import com.zhipu.chinavideo.entity.Historys;
import com.zhipu.chinavideo.util.APP;

public class HistoryActivity extends Activity implements OnItemClickListener,
		OnClickListener, HisListener {

	private SQLiteDatabase db;

	private List<Historys> list = new ArrayList<Historys>();

	private HisItemAdapter adapter;

	private ListView listView;

	private LinearLayout conview;

	private RelativeLayout empty_view;

	private LinearLayout his_listView;
	private int imagePosition = 0;// 缩略图的位置
	private List<String> urls = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		ImageView back_btn = (ImageView) findViewById(R.id.history_back);
		back_btn.setOnClickListener(this);
		LinearLayout del = (LinearLayout) findViewById(R.id.history_deleteall);
		del.setOnClickListener(this);
		conview = (LinearLayout) findViewById(R.id.history_conview);
		empty_view = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.history_empty, null);
		his_listView = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.history_listview, null);
		adapter = new HisItemAdapter(this, list, this);
		listView = (ListView) his_listView.findViewById(R.id.history_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		MyOpenHelper openHelper = new MyOpenHelper(this, APP.DB_NAME);
		db = openHelper.getWritableDatabase();
		list = findAllByAndroid(db);
		for (int i = 0; i < list.size(); i++) {
			urls.add(list.get(i).getImg_url());
		}
		mHandler.sendEmptyMessage(1);
	}

	private void upView() {
		// TODO Auto-generated method stub
		if (list.size() < 1) {
			conview.removeAllViews();
			conview.addView(empty_view);
		}
		if (list.size() >= 1) {
			conview.removeAllViews();
			adapter = new HisItemAdapter(this, list, this);
			listView.setAdapter(adapter);
			conview.addView(his_listView);
		}
	}

	// 查找数据
	private List<Historys> findAllByAndroid(SQLiteDatabase db) {
		List<Historys> list2 = new ArrayList<Historys>();
		Cursor cursor = db.query(APP.TABLE_NAME, null, null, null, null, null,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String img_url = cursor.getString(cursor.getColumnIndex("hisImg"));
			String name = cursor.getString(cursor.getColumnIndex("hisName"));
			String lv_url = cursor.getString(cursor.getColumnIndex("hisLv"));
			String time = cursor.getString(cursor.getColumnIndex("hisTime"));
			String rome_id = cursor.getString(cursor.getColumnIndex("hisRoom"));
			Historys his = new Historys(img_url, name, lv_url, time, rome_id);
			list2.add(his);
			cursor.moveToNext();
		}

		cursor.close(); // 一定要关闭Cursor对象

		return list2;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Historys his = list.get(position);
		Intent intent = new Intent();
		intent.putExtra("id", his.getRoom_id());
		intent.setClass(this, AnchorCenterActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.history_back) {
			onBackPressed();
		}

		if (v.getId() == R.id.history_deleteall) {

			db.delete(APP.TABLE_NAME, null, null);

			conview.removeAllViews();

			conview.addView(empty_view);
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				upView();
				break;
			default:
				break;
			}
		}
	};

	private int delete(String name) {
		return db.delete(APP.TABLE_NAME, "hisTime=?", new String[] { name });
	}

	@Override
	public void deleteItem(String name) {
		// TODO Auto-generated method stub
		list.clear();
		delete(name);
		list = findAllByAndroid(db);
		urls.clear();
		for (int i = 0; i < list.size(); i++) {
			urls.add(list.get(i).getImg_url());
		}
		mHandler.sendEmptyMessage(1);
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
