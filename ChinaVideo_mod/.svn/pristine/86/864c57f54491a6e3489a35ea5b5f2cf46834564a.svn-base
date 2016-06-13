package com.zhipu.chinavideo.roundperson;

import java.util.List;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.util.CircularImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author: zhongxf
 * @Description: 周边达人的适配器
 * @date: 2016年4月13日
 */
public class RoundPersonAdapter extends BaseExpandableListAdapter {
	private List<RoundPersonVo> list;
	private Context cxt;
	private ImageLoader mImageLoader = null;

	private Bitmap faceIcon;
	private Bitmap playerIcon;
	private Bitmap zhuboIcon;

	public RoundPersonAdapter(List<RoundPersonVo> list, Context cxt) {
		this.list = list;
		this.cxt = cxt;
		ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(
				cxt).threadPoolSize(1).memoryCache(new WeakMemoryCache())
				.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(localImageLoaderConfiguration);

		faceIcon = BitmapFactory.decodeResource(cxt.getResources(),
				R.drawable.icon);
		playerIcon = BitmapFactory.decodeResource(cxt.getResources(),
				R.drawable.player_icon);
		zhuboIcon = BitmapFactory.decodeResource(cxt.getResources(),
				R.drawable.zhubo_icon);
	}

	@Override
	public Object getChild(int arg0, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getChildView(int arg0, int position, boolean arg2,
			View convertView, ViewGroup viewgroup) {

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
					R.layout.round_person_item, null);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.round_person_item_name);
			viewHolder.img = (CircularImage) convertView
					.findViewById(R.id.mine_icon);
			viewHolder.dis = (TextView) convertView
					.findViewById(R.id.round_person_dis);
			viewHolder.flagIcon = (ImageView) convertView
					.findViewById(R.id.flag_icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RoundPersonVo vo = list.get(position);
		if (vo != null) {
			viewHolder.name.setText(vo.getName());
			if (vo.getImgUrl() != null && !"".equals(vo.getImgUrl())) {
				mImageLoader.displayImage(vo.getImgUrl(), viewHolder.img);
			} else {
				viewHolder.img.setImageBitmap(faceIcon);
			}
			int dis = vo.getDis();
			if(dis<1000){
				viewHolder.dis.setText(vo.getDis() + "米");
			}else{
				viewHolder.dis.setText(vo.getDis()/1000 + "公里");
			}
			
			
			if ("z".equals(vo.getType())) {
				viewHolder.flagIcon.setImageBitmap(zhuboIcon);
			} else {
				viewHolder.flagIcon.setImageBitmap(playerIcon);
			}
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	static class ViewHolder {
		TextView name;
		CircularImage img;
		TextView dis;
		ImageView flagIcon;
	}

}
