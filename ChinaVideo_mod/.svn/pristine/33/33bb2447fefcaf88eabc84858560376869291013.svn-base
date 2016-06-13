package com.zhipu.chinavideo.adapter;

import com.zhipu.chinavideo.fragment.AudienceFragment;
import com.zhipu.chinavideo.fragment.BoxFragment;
import com.zhipu.chinavideo.fragment.FansListFragment;
import com.zhipu.chinavideo.fragment.PriChatFragment;
import com.zhipu.chinavideo.fragment.PubChatFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.ListView;

public class RoomTabAdapter extends FragmentPagerAdapter {
	private String r_id;
	private String a_id;
	private String[] TITLES = { "公聊", "私聊", "观众", "百宝箱" };

	public RoomTabAdapter(FragmentManager fm, String room_id, String anchor_id) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.r_id = room_id;
		this.a_id = anchor_id;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return PubChatFragment.getIntance();
		} else if (position == 1) {
			return PriChatFragment.getIntance();
		} else if (position == 2) {
			return AudienceFragment.getIntance(r_id, a_id);
		} else {
			return BoxFragment.getIntance(a_id, r_id);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return TITLES.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return this.TITLES[position];
	}

	public String[] getTITLES() {
		return TITLES;
	}

}
