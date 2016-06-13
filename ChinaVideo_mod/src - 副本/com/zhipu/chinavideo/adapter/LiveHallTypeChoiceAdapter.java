package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Tags;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LiveHallTypeChoiceAdapter extends BaseAdapter {
	private final int TYPE_COUNT = 5;
	private int checked;
	private Context mContext;
	private String[] typeName;
	private List<Tags> tag_list;

	public LiveHallTypeChoiceAdapter(Context paramContext, int paramInt,
			List<Tags> tag_list) {
		this.tag_list = tag_list;
		String[] arrayOfString = new String[tag_list.size() + 1];
		arrayOfString[0] = "直播";
		for (int i = 1; i < tag_list.size() + 1; i++) {
			arrayOfString[i] = tag_list.get(i - 1).getName();
		}
		this.typeName = arrayOfString;
		this.mContext = paramContext;
		this.checked = paramInt;
	}

	public int getCount() {
		return tag_list.size() + 1;
	}

	public Object getItem(int paramInt) {
		return Integer.valueOf(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		TypeViewHold localTypeViewHold;
		if (paramView == null) {
			localTypeViewHold = new TypeViewHold();
			paramView = View.inflate(this.mContext,
					R.layout.type_choice_pop_item, null);
			localTypeViewHold.layout = ((LinearLayout) paramView
					.findViewById(R.id.type_layout));
			localTypeViewHold.typeText = ((TextView) paramView
					.findViewById(R.id.type_name));
			paramView.setTag(localTypeViewHold);
		} else {
			localTypeViewHold = (TypeViewHold) paramView.getTag();
		}
		if (paramInt != this.checked) {
			localTypeViewHold.typeText.setText(this.typeName[paramInt]);
			localTypeViewHold.typeText
			.setBackgroundResource(R.drawable.typechange_selecor);
			localTypeViewHold.typeText.setTextColor(this.mContext
					.getResources().getColor(R.color.bg_dialog));
		} else {
			localTypeViewHold.typeText.setText(this.typeName[paramInt]);
			localTypeViewHold.typeText
			.setBackgroundResource(R.drawable.typechoice_press_btn);
			localTypeViewHold.typeText.setTextColor(this.mContext
					.getResources().getColor(R.color.white));
		}
		return paramView;
	}

	public void refreshGrid(int paramInt) {
		this.checked = paramInt;
		notifyDataSetChanged();
	}

	private final class TypeViewHold {
		public LinearLayout layout;
		public TextView typeText;

		private TypeViewHold() {
		}
	}

}