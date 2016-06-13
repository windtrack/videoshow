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
import com.zhipu.chinavideo.adapter.Disc_Adapter;
import com.zhipu.chinavideo.entity.News;
import com.zhipu.chinavideo.entity.Tags;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DiscoveryFragment extends Fragment {
	private View rootView;
	private TextView title_tv;
	private List<News> news;
	private ListView disc_list;
	private Disc_Adapter adapter;
	private View disc_loading;

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null) {
			localViewGroup.removeView(this.rootView);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_discovery, container,
					false);
			news = new ArrayList<News>();
			adapter = new Disc_Adapter(news, getActivity());
			initTitleBar();
			disc_list = (ListView) rootView.findViewById(R.id.disc_list);
			disc_loading = rootView.findViewById(R.id.disc_loading);
			disc_list.setAdapter(adapter);
			gettaglist();
		}

		return rootView;
	}

	private void initTitleBar() {
		ImageView sousuoimg = (ImageView) this.rootView
				.findViewById(R.id.sousuo_img);
		title_tv = (TextView) this.rootView.findViewById(R.id.title_tv);
		title_tv.setText("发现");
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
				// TODO Auto-generated method stub
				MainTabActivity.initanchoronline();
			}
		});
	}

	/**
	 * 获取标签
	 */
	private void gettaglist() {
		disc_loading.setVisibility(0);
		Runnable gettaglistrun = new Runnable() {
			public void run() {
				String s = Utils.gettaglistdata();
//				Log.i("lvjian", "发现----------》" + s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int i = jsonObject.getInt("s");
					if (i == 1) {
						JSONObject data = jsonObject.getJSONObject("data");
						String aver = data.getString("aver");
						JSONArray ja = data.getJSONArray("tags");
						JSONArray newsarray = data.getJSONArray("news");
						// 发现
						for (int k = 0; k < newsarray.length(); k++) {
							JSONObject newsinfo = newsarray.getJSONObject(k);
							Gson gson = new Gson();
							News newss = gson.fromJson(newsinfo.toString(),
									News.class);
							news.add(newss);
						}
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(gettaglistrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				disc_loading.setVisibility(8);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				disc_loading.setVisibility(8);
				break;
			case 3:
				disc_loading.setVisibility(8);
				break;

			default:
				break;
			}
		};
	};
}
