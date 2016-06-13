package com.zhipu.chinavideo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.GiftAdapter;
import com.zhipu.chinavideo.entity.Gift;
import com.zhipu.chinavideo.util.APP;

public class GiftFragment extends Fragment {
	private GiftAdapter adapter;
	private Context context;
	private List<Gift> gs = new ArrayList<Gift>();
	private List<Gift> gifts = new ArrayList<Gift>();
	private GridView gift_gridview;
	private View rootView;
	private int position = 0;
	private SharedPreferences preferences;
	private Editor editor;

	public GiftFragment() {
		super();
	}

	public static GiftFragment getIntance(Context context, List<Gift> gs,
			int position) {
		GiftFragment giftFragment = new GiftFragment();
		giftFragment.context = context;
		giftFragment.gs = gs;
		giftFragment.position = position;
		giftFragment.preferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		giftFragment.editor = giftFragment.preferences.edit();
		return giftFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_gift, container,
					false);
			gift_gridview = (GridView) rootView
					.findViewById(R.id.gift_gridview);
			for (int i = 0; i < gs.size(); i++) {
				if (i >= (8 * position) && i < (8 * (position + 1))) {
					gifts.add(gs.get(i));
				}
			}
			adapter = new GiftAdapter(gifts, context, position);
			gift_gridview.setAdapter(adapter);
			gift_gridview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					adapter.setLocation(arg2);
					adapter.notifyDataSetChanged();
					// 保存赠送礼物是否是仓库中的礼物
					editor.putString(APP.GIFT_ID_CURRENT, gifts.get(arg2)
							.getId());
					editor.putString(APP.GIFT_FROM_CURRENT, "0");
					// 保存刷礼物价格
					editor.putString(APP.GIFT_PRICE_CURREN, gifts.get(arg2)
							.getPrice());
					editor.putString(APP.GIFT_ICON_CUTTENT, gifts.get(arg2)
							.getIcon());
					editor.commit();
				}
			});
		}
		return rootView;
	}

	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null) {
			localViewGroup.removeView(this.rootView);
		}
	}

	public void resetFragmentData(List<Gift> slist) {
		 GiftFragment.getIntance(context, slist, position);
	}

}
