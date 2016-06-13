package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.DoubleAnchor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DoubleAdapter extends BaseAdapter {
	private List<DoubleAnchor> doubleanchors;
	private Context context;
	private int[] number = { R.drawable.fans_1, R.drawable.fans_2,
			R.drawable.fans_3, R.drawable.fans_4, R.drawable.fans_5,
			R.drawable.fans_6, R.drawable.fans_7, R.drawable.fans_8,
			R.drawable.fans_9, R.drawable.fans_10 };

	private String type;

	public DoubleAdapter(List<DoubleAnchor> doubleanchors, Context context,
			String type) {
		super();
		this.doubleanchors = doubleanchors;
		this.context = context;
		this.type = type;
	}

	public DoubleAdapter() {
		super();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return doubleanchors.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return doubleanchors.get(position);
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
					R.layout.double_rank_item, null);
			holder.hg_name = (TextView) convertView.findViewById(R.id.hg_name);
			holder.hg_num = (TextView) convertView.findViewById(R.id.hg_num);
			holder.hg_rank = (ImageView) convertView.findViewById(R.id.hg_rank);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.hg_name.setText(doubleanchors.get(position).getTitle());
		holder.hg_num.setText(doubleanchors.get(position).getNum());
		if (position < number.length) {
			holder.hg_rank.setImageResource(number[position]);
		}
		int[] background1 = {
				context.getResources().getColor(R.color.x_doublerank_l),
				context.getResources().getColor(R.color.x_doublerank_h) };
		int[] background2 = {
				context.getResources().getColor(R.color.w_doublerank_l),
				context.getResources().getColor(R.color.w_doublerank_h) };
		int[] background3 = {
				context.getResources().getColor(R.color.h_doublerank_l),
				context.getResources().getColor(R.color.h_doublerank_h) };
		if ("1".equals(type)) {
			int a = position % background1.length;
			convertView.setBackgroundColor(background1[a]);
		} else if ("2".equals(type)) {
			int a = position % background1.length;
			convertView.setBackgroundColor(background2[a]);
		} else if ("3".equals(type)) {
			int a = position % background1.length;
			convertView.setBackgroundColor(background3[a]);
		} else {
		}
		;

		return convertView;
	}

	class ViewHolder {
		TextView hg_num;
		TextView hg_name;
		ImageView hg_rank;
	}
}
