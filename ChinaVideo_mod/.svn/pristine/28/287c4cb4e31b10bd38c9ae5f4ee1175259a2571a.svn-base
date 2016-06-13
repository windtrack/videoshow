package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.AnchorInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SubscribeListAdapter extends BaseAdapter {
	private List<AnchorInfo> list;
	private Context context;

	public SubscribeListAdapter(List<AnchorInfo> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	public SubscribeListAdapter() {
		super();
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.subscribelist_item, null);
			holder.anchor_icon = (ImageView) convertView
					.findViewById(R.id.anchor_list_icon);
			holder.anchor_name = (TextView) convertView
					.findViewById(R.id.anchor_list_name);
			holder.anchor_id = (TextView) convertView
					.findViewById(R.id.anchor_list_id);
			holder.subscribe_button = (Button) convertView
					.findViewById(R.id.anchor_list_subscribe);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.anchor_name.setText(list.get(position).getNickname());
		holder.anchor_id.setText(list.get(position).getAnchor_id());
		return convertView;
	}

	class ViewHolder {
		ImageView anchor_icon;
		TextView anchor_name;
		TextView anchor_id;
		Button subscribe_button;
	}
}
