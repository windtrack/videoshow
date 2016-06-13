package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.entity.Tags;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainPagerAdapter extends BaseExpandableListAdapter {
	private Context paramContext;
	private List<List<AnchorInfo>> list_son;
	private List<Tags> list_father;
	public static ImageLoader mImageLoader = null;
	public static DisplayImageOptions mOptions;

	public MainPagerAdapter() {
		super();
	}

	public MainPagerAdapter(Context paramContext,
			List<List<AnchorInfo>> list_son, List<Tags> list_father) {
		super();
		this.paramContext = paramContext;
		this.list_son = list_son;
		this.list_father = list_father;
		// ImageLoaderConfiguration localImageLoaderConfiguration = new
		// ImageLoaderConfiguration.Builder(
		// paramContext).threadPoolSize(1)
		// .memoryCache(new WeakMemoryCache()).build();
		// mImageLoader = ImageLoader.getInstance();
		// mImageLoader.init(localImageLoaderConfiguration);
		// mOptions = new DisplayImageOptions.Builder()
		// .bitmapConfig(Bitmap.Config.RGB_565)
		// .showStubImage(R.drawable.haibao_loading)
		// .showImageForEmptyUri(R.drawable.haibao_loading)
		// .showImageOnFail(R.drawable.haibao_loading)
		// .showImageOnLoading(R.drawable.haibao_loading).cacheOnDisc()
		// .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.haibao_loading).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(paramContext));
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list_son.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int paramInt1, final int paramInt2,
			boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
		ChildViewHolder childSingleHolder;
		if (paramView == null) {
			childSingleHolder = new ChildViewHolder();
			paramView = View.inflate(paramContext,
					R.layout.livehall_mainpage_list_childitem, null);
			initChildViews(childSingleHolder.left,
					paramView.findViewById(R.id.item_left),
					getHost(paramInt1, paramInt2, 0));
			initChildViews(childSingleHolder.right,
					paramView.findViewById(R.id.item_right),
					getHost(paramInt1, paramInt2, 1));
			paramView.setTag(childSingleHolder);
		} else {
			childSingleHolder = (ChildViewHolder) paramView.getTag();
		}
		initItem(getHost(paramInt1, paramInt2, 0), childSingleHolder.left);
		initItem(getHost(paramInt1, paramInt2, 1), childSingleHolder.right);
		paramView.findViewById(R.id.item_left).setOnClickListener(
				new OnClickListener() {
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						AnchorInfo anchorInfo = getHost(paramInt1, paramInt2, 0);
						Intent localIntent = new Intent(paramContext,
								LiveRoomActivity.class);
						localIntent.putExtra("room_id", anchorInfo.getId());
						paramContext.startActivity(localIntent);
					}
				});
		paramView.findViewById(R.id.item_right).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						AnchorInfo anchorInfo = getHost(paramInt1, paramInt2, 1);
						Intent localIntent = new Intent(paramContext,
								LiveRoomActivity.class);
						localIntent.putExtra("room_id", anchorInfo.getId());
						paramContext.startActivity(localIntent);
					}
				});
		return paramView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		int i = 0;
		if (this.list_son.get(groupPosition).size() % 2 == 0) {
			i = this.list_son.get(groupPosition).size() / 2;
		} else {
			i = (this.list_son.get(groupPosition).size() / 2 + 1);
		}
		return i;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list_father.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list_father.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int paramInt, boolean paramBoolean,
			View paramView, ViewGroup paramViewGroup) {
		// TODO Auto-generated method stub
		GroupViewHolder localGroupViewHolder;
		if (paramView == null) {
			localGroupViewHolder = new GroupViewHolder();
			paramView = View.inflate(this.paramContext,
					R.layout.livehall_mainpage_list_groupitem, null);
			localGroupViewHolder.groupImage = ((ImageView) paramView
					.findViewById(R.id.group_icon));
			localGroupViewHolder.groupText = ((TextView) paramView
					.findViewById(R.id.group_name));
			localGroupViewHolder.groupEnter = ((ImageView) paramView
					.findViewById(R.id.group_enter));
			paramView.setTag(localGroupViewHolder);
		} else {
			localGroupViewHolder = (GroupViewHolder) paramView.getTag();
		}
		localGroupViewHolder.groupText.setText(list_father.get(paramInt)
				.getName());
		int idex = list_father.get(paramInt).getIdx();
		if (idex == 0) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_tuijian);
		} else if (idex == 1) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_yirenzhuanqu);
		} else if (idex == 2) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_nvshen);
		} else if (idex == 3) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_menmenda);
		} else if (idex == 4) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_xiaoqingxin);
		} else if (idex == 5) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_gaoxiao);
		} else if (idex == 6) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_jinbao);
		} else if (idex == 7) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_haoshenyin);
		} else if (idex == 8) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_xinxiu);
		} else if (idex == 9) {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_qita);
		} else {
			localGroupViewHolder.groupImage
					.setImageResource(R.drawable.typeicon_tuijian);
		}

		return paramView;

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

	class ChildSingleHolder {
		public TextView count;
		public ImageView icon;
		public TextView name;
		public View parent;

		private ChildSingleHolder() {
		}
	}

	private final class ChildViewHolder {
		public MainPagerAdapter.ChildSingleHolder left = new MainPagerAdapter.ChildSingleHolder();
		public MainPagerAdapter.ChildSingleHolder right = new MainPagerAdapter.ChildSingleHolder();

		private ChildViewHolder() {

		}
	}

	private AnchorInfo getHost(int paramInt1, int paramInt2, int paramInt3) {
		AnchorInfo localAnchorInfo = null;
		this.list_son.get(paramInt1).size();
		switch (paramInt3) {
		case 0:
			localAnchorInfo = (AnchorInfo) this.list_son.get(paramInt1).get(
					paramInt2 * 2);
			break;
		case 1:
			if (this.list_son.get(paramInt1).size() > 1 + paramInt2 * 2) {
				localAnchorInfo = (AnchorInfo) this.list_son.get(paramInt1)
						.get(paramInt2 * 2 + 1);
			}
			break;
		default:
			break;
		}
		return localAnchorInfo;
	}

	private void initChildViews(ChildSingleHolder paramChildSingleHolder,
			View paramView, AnchorInfo paramAnchorInfo) {
		paramChildSingleHolder.parent = paramView;
		paramChildSingleHolder.icon = ((ImageView) paramView
				.findViewById(R.id.anchor_icon));
		paramChildSingleHolder.name = ((TextView) paramView
				.findViewById(R.id.anchor_name));
		paramChildSingleHolder.count = ((TextView) paramView
				.findViewById(R.id.anchor_count));
	}

	private void initItem(AnchorInfo paramAnchorInfo,
			ChildSingleHolder paramChildSingleHolder) {
		if (paramAnchorInfo == null) {
			paramChildSingleHolder.parent.setVisibility(4);
			return;
		} else {
			paramChildSingleHolder.parent.setVisibility(0);
			paramChildSingleHolder.count
					.setText(paramAnchorInfo.getMaxonline());
			paramChildSingleHolder.name.setText(paramAnchorInfo.getNickname());
			mImageLoader.displayImage(
					APP.POST_URL_ROOT2 + paramAnchorInfo.getPoster_url2(),
					paramChildSingleHolder.icon, mOptions, null);
			RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) paramChildSingleHolder.icon
					.getLayoutParams();
			localLayoutParams.height = getImageHeight();
			paramChildSingleHolder.icon.setLayoutParams(localLayoutParams);
			paramChildSingleHolder.count
					.setCompoundDrawablesWithIntrinsicBounds(
							this.paramContext.getResources().getDrawable(
									R.drawable.icon_zaixianrenshu), null, null,
							null);
		}
	}

	private final class GroupViewHolder {
		public ImageView groupEnter;
		public ImageView groupImage;
		public TextView groupText;

		private GroupViewHolder() {
		}
	}

	private int getImageHeight() {
		return 3 * (((Activity) this.paramContext).getWindowManager()
				.getDefaultDisplay().getWidth() - Utils.dip2px(
				this.paramContext, 15.0F)) / 8;
	}
}
