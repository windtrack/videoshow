package com.zhipu.chinavideo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.SiLiao;

public class Pop_chatadapter extends BaseAdapter {
	private List<SiLiao> list;
	private LayoutInflater inf;

	public Pop_chatadapter(Context context, List<SiLiao> list) {
		this.list = list;
		this.inf = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = inf.inflate(R.layout.gift_num_popwindow_item, null);
		} else {
			v = convertView;
		}
		TextView tv = (TextView) v.findViewById(R.id.textView);
		String s = list.get(position).getName();
		if (s == null) {
		} else {
			tv.setText(s);
		}
		return v;
	}

}
