package com.zhipu.chinavideo.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.AnchorCenterActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.RankAdapter.HoldViewSon;
import com.zhipu.chinavideo.entity.Fans;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FansAdapter extends BaseExpandableListAdapter {
	List<String> father;
	Context context;
	private List<List<Fans>> sons;
	private HoldView holdView;
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private int[] fan_draw = { R.drawable.ranklist_1, R.drawable.ranklist_2,
			R.drawable.ranklist_3, R.drawable.ranklist_4, R.drawable.ranklist_5 };

	public FansAdapter(List<String> father, Context context,
			List<List<Fans>> sons) {
		super();
		this.father = father;
		this.context = context;
		this.sons = sons;
		holdView = new HoldView();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return sons.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HoldView holdView = null;
		if (convertView == null) {
			holdView = new HoldView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fans_sons, null);
			holdView.fans_icon = (CircularImage) convertView
					.findViewById(R.id.fan_icon);
			holdView.fans_name = (TextView) convertView
					.findViewById(R.id.fans_names);
			holdView.con_num = (TextView) convertView
					.findViewById(R.id.contribution);
			holdView.mingci_img = (ImageView) convertView
					.findViewById(R.id.fan_mingci);
			holdView.cost_level = (ImageView) convertView
					.findViewById(R.id.fans_cost_level);
			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		holdView.fans_name.setText(sons.get(groupPosition).get(childPosition)
				.getNickname());
		holdView.con_num.setText(sons.get(groupPosition).get(childPosition).getNum()+"乐币");
		holdView.mingci_img.setImageResource(fan_draw[childPosition]);
		APP.setCost_level(sons.get(groupPosition).get(childPosition)
				.getCost_level(), holdView.cost_level, context);
		String str = APP.USER_BIG_LOGO_ROOT
				+ sons.get(groupPosition).get(childPosition).getIcon();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str, holdView.fans_icon, mOptions);
		// 监听排行榜Item点击事件，（富豪榜跳到用户信息，主播榜跳到主播信息）
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, AnchorCenterActivity.class);
				intent.putExtra("id", sons.get(groupPosition)
						.get(childPosition).getId());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return sons.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return father.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return father.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HoldView2 holdView2 = null;
		if (convertView == null) {
			holdView2 = new HoldView2();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fans_father, null);
			holdView2.father_name = (TextView) convertView
					.findViewById(R.id.father_name);
			convertView.setTag(holdView2);
		} else {
			holdView2 = (HoldView2) convertView.getTag();
		}
		holdView2.father_name.setText(father.get(groupPosition));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public class HoldView {
		CircularImage fans_icon;
		TextView fans_name;
		TextView con_num;
		ImageView cost_level;
		ImageView mingci_img;

	}

	public class HoldView2 {
		TextView father_name;
	}
}
