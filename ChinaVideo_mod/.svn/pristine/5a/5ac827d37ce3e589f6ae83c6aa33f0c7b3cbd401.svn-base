package com.zhipu.chinavideo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;

public class TopOnLineAdapter extends BaseAdapter {
	private Context context;
	private List<AnchorInfo> list = new ArrayList<AnchorInfo>();
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
    
	public TopOnLineAdapter(Context context, List<AnchorInfo> list) {
		super();
		this.context = context;
		this.list = list;
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
					R.layout.top_online_item, null);
			holder.cir = (ImageView) convertView
					.findViewById(R.id.top_online_anchor_icon);
			holder.name = (TextView) convertView
					.findViewById(R.id.top_online_anchor_name);
			holder.num = (TextView) convertView
					.findViewById(R.id.top_online_anchor_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.num.setText(list.get(position).getMaxonline());
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
				Intent in = new Intent(context, LiveRoomActivity.class);
				in.putExtra("room_id", list.get(position).getId());
				context.startActivity(in);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView cir;
		TextView name;
		TextView num;
	}

}
