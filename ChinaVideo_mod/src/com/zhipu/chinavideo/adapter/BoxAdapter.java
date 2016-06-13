package com.zhipu.chinavideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhipu.chinavideo.R;

public class BoxAdapter extends BaseAdapter {
	private LayoutInflater inf;
	/**
	 * zhongxf 在2016年4月8日增加点歌功能
	 * */
	private int[] images = new int[] {R.drawable.paihangtubiao,R.drawable.shouhutubiao,R.drawable.bbx_fanpai,
			 R.drawable.bbx_zuojia,
			R.drawable.bbx_baoxianxiang, R.drawable.bbx_chongzhi,R.drawable.dg_icon };
	private String[] names = new String[] { "榜单", "守护", "翻牌","座驾", "保险箱", "充值","点歌" };
	public BoxAdapter(Context context) {
		this.inf = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return images[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inf.inflate(R.layout.box_item, null);
		}
		ImageView iv = (ImageView) convertView.findViewById(R.id.images);
		TextView tv = (TextView) convertView.findViewById(R.id.textView);
		iv.setBackgroundResource(images[position]);
		tv.setText(names[position]);
		return convertView;
	}
}
