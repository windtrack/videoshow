package com.zhipu.chinavideo.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.AnchorCenterActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Rank;
import com.zhipu.chinavideo.entity.StarWeek;
import com.zhipu.chinavideo.util.APP;

public class FengYunAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<List<Rank>> ranks;
	private List<StarWeek> title;
	public DisplayImageOptions mOptions;
	private String mark_name;
	private ImageLoader mImageLoader = null;
	private int[] number = { R.drawable.fans_1, R.drawable.fans_2,
			R.drawable.fans_3, R.drawable.fans_4, R.drawable.fans_5 };
	private int[] rank_color = { R.color.first, R.color.second, R.color.third,
			R.color.fourth, R.color.fourth };

	public FengYunAdapter() {
		super();
	}

	public FengYunAdapter(Context context, List<List<Rank>> ranks,
			List<StarWeek> title, String mark_name) {
		super();
		this.context = context;
		this.ranks = ranks;
		this.title = title;
		this.mark_name = mark_name;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return ranks.get(groupPosition).get(childPosition);
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
		HoldViewSon holdViewSon = null;
		if (convertView == null) {
			holdViewSon = new HoldViewSon();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.rank_item, null);
			holdViewSon.icon = (ImageView) convertView
					.findViewById(R.id.rank_item_img);
			holdViewSon.name = (TextView) convertView
					.findViewById(R.id.rank_item_name);
			holdViewSon.id = (TextView) convertView
					.findViewById(R.id.rank_item_anchorid);
			holdViewSon.level = (ImageView) convertView
					.findViewById(R.id.rank_item_level);
			holdViewSon.number = (ImageView) convertView
					.findViewById(R.id.rank_numbler);
			holdViewSon.fansnum = (TextView) convertView
					.findViewById(R.id.rank_item_fansnum);
			holdViewSon.r_fss=(TextView) convertView.findViewById(R.id.r_fss);
			convertView.setTag(holdViewSon);
		} else {
			holdViewSon = (HoldViewSon) convertView.getTag();
		}
		holdViewSon.r_fss.setText("数量：");
		holdViewSon.name.setText(ranks.get(groupPosition).get(childPosition)
				.getNickname());
		holdViewSon.id.setText(ranks.get(groupPosition).get(childPosition)
				.getUser_id());
		APP.setReceived_level(ranks.get(groupPosition).get(childPosition)
				.getReceived_level(), holdViewSon.level, context);
		String str1 = APP.POST_URL_ROOT2
				+ ranks.get(groupPosition).get(childPosition).getPoster_url2();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.user_icon_default).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str1, holdViewSon.icon, mOptions);
		holdViewSon.number.setImageResource(number[childPosition]);
		holdViewSon.number.setBackgroundResource(rank_color[childPosition]);
		holdViewSon.fansnum.setText(ranks.get(groupPosition).get(childPosition)
				.getGiftnum()+"个");
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, AnchorCenterActivity.class);
				intent.putExtra("id",
						ranks.get(groupPosition).get(childPosition)
								.getUser_id());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return ranks.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return title.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return title.size();
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
		HoldViewfather holderfather = null;
		if (convertView == null) {
			holderfather = new HoldViewfather();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.weekstar_father_item, null);

			holderfather.rank_title = (TextView) convertView
					.findViewById(R.id.fy_rank_title);
			holderfather.rank_content = (TextView) convertView
					.findViewById(R.id.fy_rank_content);
			convertView.setTag(holderfather);
		} else {
			holderfather = (HoldViewfather) convertView.getTag();
		}
		if (mark_name.equals("周星")) {
			holderfather.rank_title.setText("周星：");
		} else if (mark_name.equals("风云")) {
			holderfather.rank_title.setText("风云：");
		}
		String str = "根据主播每周收到的" + title.get(groupPosition).getName() + "进行排行";
		SpannableStringBuilder builder = new SpannableStringBuilder(str);
		builder.setSpan(new TextAppearanceSpan(context, R.style.style2), 4, 8,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(new TextAppearanceSpan(context, R.style.style2), 9,
				(title.get(groupPosition).getName().length() + 9),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holderfather.rank_content.setText(builder);
		// holderfather.rank_content.setText(str);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public class HoldViewfather {
		TextView rank_title;
		TextView rank_content;
	}

	public class HoldViewSon {
		ImageView icon;
		TextView name;
		TextView id;
		ImageView level;
		ImageView number;
		TextView fansnum;
		TextView r_fss;
	}
}
