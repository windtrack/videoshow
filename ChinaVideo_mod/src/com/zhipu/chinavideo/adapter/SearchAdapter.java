package com.zhipu.chinavideo.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.AnchorCenterActivity;
import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;

public class SearchAdapter extends BaseAdapter {

	private Context context;
	private List<AnchorInfo> list;
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;

	public SearchAdapter(Context context, List<AnchorInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public void setmOptions(DisplayImageOptions mOptions) {
		this.mOptions = mOptions;
	}

	public void setmImageLoader(ImageLoader mImageLoader) {
		this.mImageLoader = mImageLoader;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.searchlist_item, null);
			holder.cir = (CircularImage) convertView
					.findViewById(R.id.anchor_icon);
			holder.name = (TextView) convertView.findViewById(R.id.anchor_name);
			holder.isplay = (ImageView) convertView.findViewById(R.id.isplay);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.size() == 0) {
			return convertView;
		}
		if (list.get(position).getStatus().equals("1")) {
			holder.isplay.setVisibility(0);
		} else {
			holder.isplay.setVisibility(8);
		}
		holder.name.setText(list.get(position).getNickname());
		String str1 = APP.USER_LOGO_ROOT + list.get(position).getIcon();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str1, holder.cir, mOptions);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(context, AnchorCenterActivity.class);
				in.putExtra("id", list.get(position).getAnchor_id());
				context.startActivity(in);
			}
		});
		
		return convertView;

	}
	
	public void setRushData(List<AnchorInfo> list){
		this.list = list;
		Log.i("sumao", "setRushData");
		notifyDataSetChanged();
	}
	class ViewHolder {
		CircularImage cir;
		TextView name;
		ImageView isplay;
	}

}
