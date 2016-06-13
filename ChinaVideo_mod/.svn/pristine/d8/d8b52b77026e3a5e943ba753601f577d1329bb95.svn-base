package com.zhipu.chinavideo.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.SearchActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchHotAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;

	public SearchHotAdapter() {
		super();
	}

	public SearchHotAdapter(List<String> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hot_search_item, null);
		}
		TextView hot_search_tv = (TextView) convertView
				.findViewById(R.id.hot_search_tv);
		hot_search_tv.setText(list.get(arg0));

		return convertView;
	}

}
