package com.zhipu.chinavideo.fragment;

import com.zhipu.chinavideo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AnchorPhotoFragment extends Fragment {
	private View rootView;

	public AnchorPhotoFragment() {
		super();
	}

	public static AnchorPhotoFragment getIntance() {
		AnchorPhotoFragment anchorPhotoFragment = new AnchorPhotoFragment();
		return anchorPhotoFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_anchorphoto,
					container, false);
		}
		return rootView;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}
}
