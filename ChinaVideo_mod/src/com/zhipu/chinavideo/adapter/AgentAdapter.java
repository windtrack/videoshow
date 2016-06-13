package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Agent;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AgentAdapter extends BaseAdapter {
	private List<Agent> agents;
	private Context context;

	public AgentAdapter(List<Agent> agents, Context context) {
		super();
		this.agents = agents;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return agents.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return agents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		if (convertView == null) {
			v = LayoutInflater.from(context).inflate(R.layout.agent_item, null);
		} else {
			v = convertView;
		}
		TextView tv = (TextView) v.findViewById(R.id.agent_name);
		String nickname = agents.get(position).getNickname();
		if (nickname == null) {
			tv.setText("");
		} else {
			tv.setText(nickname);
		}
		return v;

	}

}
