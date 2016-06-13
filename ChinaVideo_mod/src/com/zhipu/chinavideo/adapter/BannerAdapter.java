package com.zhipu.chinavideo.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Advertise;
import com.zhipu.chinavideo.util.APP;

public class BannerAdapter extends PagerAdapter implements OnClickListener {
	private Context context;
	private List<Advertise> banner;
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;

	public BannerAdapter(Context context, List<Advertise> banner) {
		this.context = context;
		this.banner = banner;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return banner.size();
	}

	@Override
	public boolean isViewFromObject(View v, Object obj) {
		// TODO Auto-generated method stub
		return v == obj;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final Advertise advertise = banner.get(position);
		View view = LayoutInflater.from(context).inflate(
				R.layout.banner_pager_image, null);
		ImageView imageView = (ImageView) view;
		String str1 = APP.USER_LOGO_ROOT + advertise.getImg();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str1, imageView, mOptions);
		imageView.setTag(position);
		imageView.setOnClickListener(this);
		container.addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		container.removeView((View) object);
		// super.destroyItem(container, position, object);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int i = Integer.parseInt(v.getTag().toString());
		for (int j = 0; j < banner.size(); j++) {
			if (i == j) {
				// Map<String, String> map = banner.get(j);
				// String room_id = map.get("room_id");
				// Intent intent = new Intent();
				//
				// context.startActivity(intent);
			}
		}

		// if ("0".equals(v.getTag().toString())) {
		//
		// Intent intent = new Intent();
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.setData(Uri.parse("http://www.baidu.com/"));
		// context.startActivity(intent);
		//
		// }
		// if ("1".equals(v.getTag().toString())) {
		//
		// Intent intent = new Intent();
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.setData(Uri.parse("http://www.sina.com.cn/"));
		// context.startActivity(intent);
		//
		// }

	}

}
