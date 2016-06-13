package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.unionpay.mobile.android.widgets.ad;
import com.zhipu.chinavideo.FirstPayActivity;
import com.zhipu.chinavideo.HuoDongActivity;
import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Advertise;
import com.zhipu.chinavideo.util.APP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.renderscript.Int2;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.graphics.Bitmap;

public class AdvertiseAdapter extends PagerAdapter {
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private Context context;
	private List<Advertise> list;

	public AdvertiseAdapter(List<Advertise> paramList, Context paramContext) {
		this.list = paramList;
		this.context = paramContext;
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.view_page_1).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null && list.size() > 0) {
			return list.size();
		} else {
			return 0;
		}
	}

	@Override
	public boolean isViewFromObject(View paramView, Object paramObject) {
		// TODO Auto-generated method stub
		return paramView == paramObject;
	}

	@Override
	public Object instantiateItem(ViewGroup paramViewGroup, final int paramInt) {
		final Advertise advertise = list.get(paramInt);
		View localView = ((Activity) this.context).getLayoutInflater().inflate(
				R.layout.livehall_ad_pageitem, paramViewGroup, false);
		ImageView localImageView = (ImageView) localView
				.findViewById(R.id.ad_img);
		mImageLoader.displayImage(list.get(paramInt).getImg(), localImageView,
				mOptions);
		paramViewGroup.addView(localView);
		localImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ("2".equals(advertise.getAct())) {
					// 跳转到url
						Intent intent = new Intent(context, HuoDongActivity.class);
						intent.putExtra("url", advertise.getUrl());
						intent.putExtra("title", "活动");
						context.startActivity(intent);
					
					
				} else if ("1".equals(advertise.getAct())) {
					// 跳转到房间
					Intent localIntent = new Intent(context,
							LiveRoomActivity.class);
					localIntent.putExtra("room_id", advertise.getRoom_id());
					context.startActivity(localIntent);
				} else if ("11".equals(advertise.getAct())) {
					// Intent intent = new Intent(context,
					// NianDuShengDianActivity.class);
					// context.startActivity(intent);
				} else  if ("100".equals(advertise.getAct())) {
					Intent intent = new Intent(context, FirstPayActivity.class);
					context.startActivity(intent);
				}else{
					
				}
			}
		});
		return localView;
	}

	@Override
	public void destroyItem(ViewGroup paramViewGroup, int paramInt,
			Object paramObject) {
		paramViewGroup.removeView((View) paramObject);
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}
}
