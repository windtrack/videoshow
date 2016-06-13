package com.zhipu.chinavideo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.adapter.SearchAdapter;
import com.zhipu.chinavideo.adapter.SearchHotAdapter;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnClickListener {
	private TextView search_btn;
	private EditText search_input;
	private List<AnchorInfo> anchors;
	private View loadingview;
	private ListView search_list;
	private SearchAdapter searchAdapter;
	private TextView search_tv;
	private ListView hot_search_list;
	private ListView history_search_list;
	private List<String> hot_search_data;
	private List<String> history_search_data;
	private TextView search_quxiao_btn;
	private SharedPreferences sharedPreferences;
	private SearchHotAdapter searchHotAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		sharedPreferences = getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		search_btn = (TextView) findViewById(R.id.search_btn);
		search_input = (EditText) findViewById(R.id.search_input);
		loadingview = findViewById(R.id.serch_loading);
		search_list = (ListView) findViewById(R.id.search_list);
		search_tv = (TextView) findViewById(R.id.search_tv);
		search_quxiao_btn = (TextView) findViewById(R.id.search_quxiao_btn);
		search_quxiao_btn.setOnClickListener(this);
		hot_search_list = (ListView) findViewById(R.id.hot_search_list);
		history_search_list = (ListView) findViewById(R.id.history_search_list);
		search_btn.setOnClickListener(this);
		hot_search_data = new ArrayList<String>();
		history_search_data = new ArrayList<String>();
		gethotsearch();
		search_input.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (search_input.getText().toString().length() > 0) {
					search_btn.setVisibility(0);
					search_quxiao_btn.setVisibility(8);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		String save_history = sharedPreferences.getString(APP.HISTORY, "");
		String[] hisArrays = save_history.split(",");
		for (int i = 0; i < hisArrays.length; i++) {
			history_search_data.add(hisArrays[i]);
		}
		searchHotAdapter = new SearchHotAdapter(history_search_data,
				SearchActivity.this);
		history_search_list.setAdapter(searchHotAdapter);
		history_search_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				loadingview.setVisibility(0);
				getsearchdata(history_search_data.get(arg2));
				search_input.setText(history_search_data.get(arg2));
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_quxiao_btn:
			finish();
			break;
		case R.id.search_btn:
			InputMethodManager localInputMethodManager = (InputMethodManager) this
					.getSystemService("input_method");
			if (localInputMethodManager.isActive()) {
				localInputMethodManager.hideSoftInputFromWindow(
						v.getWindowToken(), 0);
			}
			if ("".equals(search_input.getText().toString().trim())) {
				Utils.showToast(SearchActivity.this, "搜索关键字不能为空噢 ！");
			} else {
				getsearchdata(search_input.getText().toString().trim());
				loadingview.setVisibility(0);
				Save();
			}
			break;
		default:
			break;
		}
	}

	private void getsearchdata(final String text) {
		Runnable getsearchdatarun = new Runnable() {
			public void run() {
				String s = Utils.getsearchdata(text);
				Log.i("lvjian", "搜索关键字======================》》" + text);
				Log.i("lvjian", "搜索结果======================》》" + s);
				anchors = new ArrayList<AnchorInfo>();
				try {
					JSONObject jsonObject = new JSONObject(s);
					int a = jsonObject.getInt("s");
					if (a == 1) {
						JSONArray ja = jsonObject.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							AnchorInfo anchor = new AnchorInfo();
							Gson gson = new Gson();
							anchor = gson.fromJson(ja.getJSONObject(i)
									.toString(), AnchorInfo.class);
							anchors.add(anchor);
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
		ThreadPoolWrap.getThreadPool().executeTask(getsearchdatarun);
	}

	private void gethotsearch() {
		Runnable gethotsearch = new Runnable() {
			public void run() {
				String s = Utils.gethotsearch();
				Log.i("lvjian", "s====热门搜索列表======》"+s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int a = jsonObject.getInt("s");
					if (a == 1) {
						JSONArray ja = jsonObject.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							hot_search_data.add(ja.getString(i));
						}
						handler.sendEmptyMessage(3);
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
		ThreadPoolWrap.getThreadPool().executeTask(gethotsearch);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				loadingview.setVisibility(8);
				searchAdapter = new SearchAdapter(SearchActivity.this, anchors);
				search_list.setAdapter(searchAdapter);
				search_list.setVisibility(0);
				search_tv.setVisibility(0);
				String t = search_input.getText().toString().trim();
				String v = anchors.size() + "";
				String strs = "搜索" + t + ",共检索到" + anchors.size() + "条记录";
				SpannableStringBuilder style = new SpannableStringBuilder(strs);
				style.setSpan(new ForegroundColorSpan(Color.RED), 2,
						(2 + t.length()), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				style.setSpan(new ForegroundColorSpan(Color.RED), (2 + t
						.length() + 5),
						(2 + t.length() + 5 + v.trim().length()),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				search_tv.setText(style);
				break;
			case 2:
				Log.i("lvjian", "搜索结果失败或异常");
				loadingview.setVisibility(8);
				break;
			// 热门搜索接口返回成功
			case 3:
				Hotadapter hotadapter = new Hotadapter();
				hot_search_list.setAdapter(hotadapter);
				hot_search_list
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								try {
									JSONObject jsonObject = new JSONObject(
											hot_search_data.get(arg2));
									String search_key = jsonObject
											.getString("key");
									loadingview.setVisibility(0);
									getsearchdata(search_key);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});
				Log.i("lvjian", "热门搜索返回成功");
				break;
			// 热门搜索接口返回失败
			case 4:
				Log.i("lvjian", "热门搜索返回失败");
				break;
			default:
				break;
			}
		};
	};

	class Hotadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return hot_search_data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return hot_search_data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(SearchActivity.this).inflate(
						R.layout.hot_search_item, null);
			}
			TextView hot_search_tv = (TextView) convertView
					.findViewById(R.id.hot_search_tv);
			try {
				JSONObject jsonObject = new JSONObject(
						hot_search_data.get(arg0));
				String text = jsonObject.getString("title");
				hot_search_tv.setText(text);
				String search_title = jsonObject.getString("key");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

	}

	private void Save() {
		String text = search_input.getText().toString();
		String save_Str = sharedPreferences.getString(APP.HISTORY, "");
		String[] hisArrays = save_Str.split(",");
		for (int i = 0; i < hisArrays.length; i++) {
			if (hisArrays[i].equals(text)) {
				return;
			}
		}
		StringBuilder sb = new StringBuilder(save_Str);
		sb.append(text + ",");
		sharedPreferences.edit().putString(APP.HISTORY, sb.toString()).commit();
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
