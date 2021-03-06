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
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Bitmap;

public class TypePagerAdapter extends BaseAdapter {
	private Context context;
	private List<AnchorInfo> anchorList;
	public static ImageLoader mImageLoader = null;
	public static DisplayImageOptions mOptions;

	public TypePagerAdapter() {
		super();
	}

	public TypePagerAdapter(Context context, List<AnchorInfo> anchorList) {
		
	
		super();

		this.context = context;
		this.anchorList = anchorList;
		// ImageLoaderConfiguration localImageLoaderConfiguration = new
		// ImageLoaderConfiguration.Builder(
		// context).threadPoolSize(1).memoryCache(new WeakMemoryCache())
		// .build();
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
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int i = 0;
		if (this.anchorList.size() % 2 == 0) {
			i = this.anchorList.size() / 2;
		} else {
			i = (this.anchorList.size() / 2 + 1);
		}
		return i;

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return anchorList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int paramInt, View paramView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ChildViewHolder childSingleHolder;
		if (paramView == null) {
			childSingleHolder = new ChildViewHolder();
			paramView = View.inflate(context,
					R.layout.livehall_mainpage_list_childitem, null);
			initChildViews(childSingleHolder.left,
					paramView.findViewById(R.id.item_left),
					getHost(paramInt, 0));
			initChildViews(childSingleHolder.right,
					paramView.findViewById(R.id.item_right),
					getHost(paramInt, 1));
			paramView.setTag(childSingleHolder);
		} else {
			childSingleHolder = (ChildViewHolder) paramView.getTag();
		}

		initItem(getHost(paramInt, 0), childSingleHolder.left);
		initItem(getHost(paramInt, 1), childSingleHolder.right);
		// 跳转到房间
		paramView.findViewById(R.id.item_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						AnchorInfo anchorInfo = getHost(paramInt, 0);
						Intent localIntent = new Intent(context,
								LiveRoomActivity.class);
						localIntent.putExtra("room_id", anchorInfo.getId());
						context.startActivity(localIntent);
					}
				});
		paramView.findViewById(R.id.item_right).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						AnchorInfo anchorInfo = getHost(paramInt, 1);
						Intent localIntent = new Intent(context,
								LiveRoomActivity.class);
						localIntent.putExtra("room_id", anchorInfo.getId());
						context.startActivity(localIntent);
					}
				});
		return paramView;
	}

	private class ChildSingleHolder {
		public TextView count;
		public ImageView icon;
		public TextView name;
		public View parent;
		public ImageView anchor_not_online;

		private ChildSingleHolder() {
		}
	}

	private final class ChildViewHolder {
		public TypePagerAdapter.ChildSingleHolder left = new TypePagerAdapter.ChildSingleHolder();
		public TypePagerAdapter.ChildSingleHolder right = new TypePagerAdapter.ChildSingleHolder();

		private ChildViewHolder() {

		}
	}

	private AnchorInfo getHost(int paramInt1, int paramInt2) {
		AnchorInfo localAnchorInfo = null;
		while (true) {
			switch (paramInt2) {
			case 0:
				localAnchorInfo = (AnchorInfo) this.anchorList
						.get(paramInt1 * 2);
				break;
			case 1:
				if (this.anchorList.size() > 1 + paramInt1 * 2) {
					localAnchorInfo = (AnchorInfo) this.anchorList
							.get(paramInt1 * 2 + 1);
				}
				break;
			default:
				break;
			}
			return localAnchorInfo;
		}
	}

	private void initChildViews(ChildSingleHolder paramChildSingleHolder,
			View paramView, AnchorInfo paramAnchorInfo) {
		paramChildSingleHolder.parent = paramView;
		paramChildSingleHolder.icon = ((ImageView) paramView
				.findViewById(R.id.anchor_icon));
		paramChildSingleHolder.name = ((TextView) paramView
				.findViewById(R.id.anchor_name));
		paramChildSingleHolder.anchor_not_online = (ImageView) paramView
				.findViewById(R.id.anchor_not_online);
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
			if ("1".equals(paramAnchorInfo.getStatus())) {
				paramChildSingleHolder.count.setVisibility(0);
				paramChildSingleHolder.anchor_not_online.setVisibility(8);
				paramChildSingleHolder.count.setText(paramAnchorInfo
						.getMaxonline());
			} else {
				paramChildSingleHolder.count.setVisibility(8);
				paramChildSingleHolder.anchor_not_online.setVisibility(0);
			}

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
							this.context.getResources().getDrawable(
									R.drawable.icon_zaixianrenshu), null, null,
							null);
		}
	}

	private int getImageHeight() {
		return 3 * (((Activity) this.context).getWindowManager()
				.getDefaultDisplay().getWidth() - Utils.dip2px(this.context,
				15.0F)) / 8;
	}
}
