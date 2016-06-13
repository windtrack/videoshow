package com.zhipu.chinavideo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhipu.chinavideo.AnchorInformationActivity;
import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.MyInformationActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.util.APP;

public class FileFragment extends Fragment implements OnClickListener {
	private View rootView;
	private SharedPreferences sharedPreferences;
	private String user_id;
	private String secret;
	private RelativeLayout zhubo_file_rl;
	// 主播信息
	private String icon = "";
	private String nickname = "";
	private String room_id = "";
	private String received_level = "";
	private String cost_level = "";
	private String id = "";
	private String username = "";
	private ImageView file_anchor_level;
	private TextView file_room_id;
	private RelativeLayout rl_anchor_room;

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	public FileFragment() {
		super();
	}

	public static FileFragment getIntance(String icon, String nickname,
			String received_level, String cost_level, String id,
			String username, String room_id) {
		FileFragment fileFragment = new FileFragment();
		fileFragment.icon = icon;
		fileFragment.nickname = nickname;
		fileFragment.received_level = received_level;
		fileFragment.cost_level = cost_level;
		fileFragment.id = id;
		fileFragment.room_id = room_id;
		fileFragment.username = username;
		return fileFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_file, container,
					false);
			sharedPreferences = getActivity().getSharedPreferences(APP.MY_SP,
					Context.MODE_PRIVATE);
			user_id = sharedPreferences.getString(APP.USER_ID, "");
			secret = sharedPreferences.getString(APP.SECRET, "");
			zhubo_file_rl = (RelativeLayout) rootView
					.findViewById(R.id.zhubo_file_rl);
			rl_anchor_room = (RelativeLayout) rootView
					.findViewById(R.id.rl_anchor_room);
			rl_anchor_room.setOnClickListener(this);
			zhubo_file_rl.setOnClickListener(this);
			file_anchor_level = (ImageView) rootView
					.findViewById(R.id.file_anchor_level);
			APP.setCost_level(cost_level, file_anchor_level, getActivity());
			file_room_id = (TextView) rootView.findViewById(R.id.file_room_id);
			file_room_id.setText(room_id);
		}
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zhubo_file_rl:
			Intent intent = new Intent(getActivity(),
					AnchorInformationActivity.class);
			intent.putExtra("icon", icon);
			intent.putExtra("nickname", nickname);
			intent.putExtra("received_level", received_level);
			intent.putExtra("cost_level", cost_level);
			intent.putExtra("id", id);
			intent.putExtra("username", username);
			getActivity().startActivity(intent);
			break;
		case R.id.rl_anchor_room:
			Intent in = new Intent(getActivity(), LiveRoomActivity.class);
			in.putExtra("room_id", room_id);
			getActivity().startActivity(in);
			break;
		default:
			break;
		}
	}

}
