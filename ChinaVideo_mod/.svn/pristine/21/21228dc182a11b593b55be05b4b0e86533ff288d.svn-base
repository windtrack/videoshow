package com.zhipu.chinavideo.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.manager.EditManager;
import com.zhipu.chinavideo.util.SmilyParse;

public class FaceFragment extends Fragment {
	private FaceAdapter adapter;
	private Context context;
	private GridView face_gridview;
	private TextView tv_ceshi;
	private View rootView;
	List<Map<String, ?>> frag_data = new ArrayList<Map<String, ?>>();
	public FaceFragment() {
		super();
	}

	public static FaceFragment getIntance(Context context,List<Map<String, ?>> map) {
		FaceFragment faceFragment = new FaceFragment();
		faceFragment.context = context;
		faceFragment.frag_data=map;
		return faceFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_face, container,
					false);
			face_gridview = (GridView) rootView
					.findViewById(R.id.face_gridview);
			adapter = new FaceAdapter();
			face_gridview.setAdapter(adapter);
		}
		return rootView;
	}

	class FaceAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return frag_data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return frag_data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.face_item, null);
			}
			ImageView face_icon = (ImageView) convertView
					.findViewById(R.id.face_icon);

			Drawable d = getResources().getDrawable(
					((Integer) frag_data.get(position).get("icon")));
			face_icon.setImageDrawable(d);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String text = (String) frag_data.get(position)
							.get("text");
					EditManager.setEditText(text);
				}
			});
			return convertView;
		}
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

}
