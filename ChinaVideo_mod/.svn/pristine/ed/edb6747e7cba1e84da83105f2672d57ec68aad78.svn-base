package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.ChooseSongVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * @author: zhongxf
 * @Description: 点歌的ListView的Item
 * @date: 2016年4月8日
 */
public class ChooseSongListViewAdapter extends BaseExpandableListAdapter {

	private List<ChooseSongVo> list;// 数据源的List

	private Context cxt;// 上下文

	public ChooseSongListViewAdapter(Context cxt, List<ChooseSongVo> list) {
		this.list = list;
		this.cxt = cxt;
	}

	static class ViewHolder {
		TextView name;
		TextView info;
	}

	/**
	 * @author： zhongxf
	 * @Description:
	 * @input:
	 * @output:
	 */
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}


	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}


	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	@Override
	public View getGroupView(int position, boolean arg1, View convertView,
			ViewGroup viewGroup) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(cxt).inflate(
					R.layout.choose_song_listview_item, null);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.choose_song_name);
			viewHolder.info = (TextView) convertView
					.findViewById(R.id.choose_song_info);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ChooseSongVo vo = list.get(position);
		if (vo != null) {
			if(vo.isSelected()){
				viewHolder.info.setText(vo.getNickname());
				viewHolder.name.setText(vo.getName());
			}else{
				viewHolder.name.setText(vo.getSinger()+"-"+vo.getName());
			}
		}
		return convertView;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
