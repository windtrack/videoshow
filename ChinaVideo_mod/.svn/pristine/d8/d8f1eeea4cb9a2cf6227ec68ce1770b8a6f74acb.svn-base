package com.zhipu.chinavideo.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.callback.HisListener;
import com.zhipu.chinavideo.entity.Historys;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;

public class HisItemAdapter extends BaseAdapter implements OnClickListener {
	private Context context;
	private List<Historys> list;
	private HisListener listener;
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;

	public HisItemAdapter(Context context, List<Historys> list,
			HisListener listener) {
		super();
		this.context = context;
		this.list = list;
		this.listener = listener;
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
					R.layout.history_item, null);
			holder.cir = (CircularImage) convertView
					.findViewById(R.id.history_item_img);
			holder.nick_name=(TextView) convertView.findViewById(R.id.history_item_nickname);
			holder.level=(ImageView) convertView.findViewById(R.id.history_item_levelimg);
			holder.time = (TextView) convertView
					.findViewById(R.id.history_item_time);
			holder.del = (ImageView) convertView
					.findViewById(R.id.history_item_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.del.setOnClickListener(this);
		holder.nick_name.setText(list.get(position).getName());
		holder.del.setTag(list.get(position).getTime());
		
		APP.setReceived_level(list.get(position).getLv_url(), holder.level,
				context);
		holder.time.setText("房间号:"+list.get(position).getRoom_id());
		String str1 =  list.get(position).getImg_url();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str1, holder.cir, mOptions);
		return convertView;
	}

	class ViewHolder {
		CircularImage cir;
		TextView nick_name;
        ImageView level;
		TextView time;
		ImageView del;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.history_item_del) {
			listener.deleteItem(v.getTag().toString());
		}
	}
}
