package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.AnchorCenterActivity;
import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.GuardViews;
import com.zhipu.chinavideo.fragment.AudienceFragment;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class GuardViewerAdapter extends BaseExpandableListAdapter {
	private SharedPreferences sharedPreferences;
	private Context context;
	private List<GuardViews> viewers;
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;

	public GuardViewerAdapter(Context context, List<GuardViews> viewers) {
		super();
		this.context = context;
		this.viewers = viewers;
		sharedPreferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		// TODO Auto-generated method stub
		ChildViewHolder holder;
		if (convertView == null) {
			holder = new ChildViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.viewer_child, null);
			holder.pub = (CheckedTextView) convertView.findViewById(R.id.pub);
			holder.pri = (CheckedTextView) convertView.findViewById(R.id.pri);
			holder.files = (CheckedTextView) convertView
					.findViewById(R.id.files);
			holder.gift = (CheckedTextView) convertView.findViewById(R.id.gift);
			holder.limit = (CheckedTextView) convertView
					.findViewById(R.id.limit);
			holder.unlimit = (CheckedTextView) convertView
					.findViewById(R.id.unlimit);
			holder.kickout = (CheckedTextView) convertView
					.findViewById(R.id.kickout);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		holder.pri.setText("@TA");
		// 档案
		holder.files.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if (viewers.get(groupPosition).getIs_stealth() == 1) {
				// Utils.showToast(context, "不能查看神秘人资料！");
				// } else {
				Intent intent = new Intent(context, AnchorCenterActivity.class);
				intent.putExtra("id",
						 viewers.get(groupPosition).getId());
				context.startActivity(intent);
				// }
			}
		});
		// 禁言
		holder.limit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AudienceFragment.Shut_up(viewers.get(groupPosition).getId(),
						"1");
			}
		});
		// 解禁
		holder.unlimit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AudienceFragment.Shut_up(viewers.get(groupPosition).getId(),
						"0");
			}
		});
		// 礼物
		holder.gift.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (sharedPreferences.getString(APP.IS_LOGIN, "")
						.equals("true")) {
					//用户不能给自己送礼物
					if(sharedPreferences.getString(APP.USER_ID, "").equals(viewers.get(groupPosition)
							.getId())){
						Utils.showToast(context, "不能给自己送礼物哦！");
					}else{
						// 显示礼物
						LiveRoomActivity.showgift(viewers.get(groupPosition)
								.getId(), viewers.get(groupPosition).getNickname(),
								0);
					}
				} else {
					Utils.recharge(context);
				}
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return viewers.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return viewers.size();
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
		HoldView holdView = null;
		if (convertView == null) {
			holdView = new HoldView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.viewers_item, null);
			holdView.viewer_icon = (CircularImage) convertView
					.findViewById(R.id.viewer_icon);
			holdView.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holdView.vip_level = (ImageView) convertView
					.findViewById(R.id.vip_level);
			holdView.gif_level = (ImageView) convertView
					.findViewById(R.id.gif_level);
			holdView.guard_level = (ImageView) convertView
					.findViewById(R.id.guard_level);
			holdView.iv_jiantou = (ImageView) convertView
					.findViewById(R.id.iv_jiantou);
			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		GuardViews viewer = viewers.get(groupPosition);
		holdView.vip_level.setVisibility(View.GONE);
		holdView.gif_level.setVisibility(View.GONE);
		final String url = APP.USER_BIG_LOGO_ROOT + viewer.getIcon();
		if ("".equals(url) || null == url) {
			holdView.viewer_icon.setImageResource(R.drawable.loading_img);
		} else {
			// 加载图片
			mOptions = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.loading_img).cacheInMemory()
					.cacheOnDisc().build();
			mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
			mImageLoader.displayImage(url, holdView.viewer_icon, mOptions);
		}
		String nickname = viewer.getNickname();
		holdView.tv_name.setText(nickname);
		String level = viewer.getLevel();
		String type = viewer.getType();
		String is_year = viewer.getIs_year();
		if ("".equals(level) || level == null) {
			holdView.guard_level.setVisibility(View.GONE);
		} else {
			// 判断是否是年守护
			if ("1".equals(is_year)) {
				// 如果type==1是银守护，type==2是金守护
				if ("1".equals(type)) {
					int guard_g_level = APP.parseIsYearSilverGuardId(level);
					holdView.guard_level.setBackgroundResource(guard_g_level);
					holdView.guard_level.setVisibility(View.VISIBLE);
				} else {
					int guard_g_level = APP.parseIsYearGoldGuardId(level);
					holdView.guard_level.setBackgroundResource(guard_g_level);
					holdView.guard_level.setVisibility(View.VISIBLE);
				}
			} else {
				// 如果type==1是银守护，type==2是金守护
				if ("1".equals(type)) {
					int guard_g_level = APP.parseSilverGuardId(level);
					holdView.guard_level.setBackgroundResource(guard_g_level);
					holdView.guard_level.setVisibility(View.VISIBLE);
				} else {
					int guard_g_level = APP.parseGoldGuardId(level);
					holdView.guard_level.setBackgroundResource(guard_g_level);
					holdView.guard_level.setVisibility(View.VISIBLE);
				}
			}
		}
		if (!isExpanded) {
			holdView.iv_jiantou.setBackgroundResource(R.drawable.arrow_right);
		} else {
			holdView.iv_jiantou.setBackgroundResource(R.drawable.down);
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

	public static class HoldView {
		CircularImage viewer_icon;
		TextView tv_name;
		ImageView vip_level;
		ImageView gif_level;
		ImageView guard_level;
		ImageView iv_jiantou;

	}

	public class ChildViewHolder {
		CheckedTextView pub;
		CheckedTextView pri;
		CheckedTextView files;
		CheckedTextView gift;
		CheckedTextView limit;
		CheckedTextView unlimit;
		CheckedTextView kickout;
	}

}
