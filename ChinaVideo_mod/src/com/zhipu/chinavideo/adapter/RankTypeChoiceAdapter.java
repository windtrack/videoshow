package com.zhipu.chinavideo.adapter;


import com.zhipu.chinavideo.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankTypeChoiceAdapter extends BaseAdapter {
	private int checked;
	private Context mContext;
	private String[] typeName;
    private String[] arrayOfString={"明星","人气","点播","富豪","周星","风云"};
	public RankTypeChoiceAdapter(Context paramContext, int paramInt) {
		this.typeName = arrayOfString;
		this.mContext = paramContext;
		this.checked = paramInt;
	}

	public int getCount() {
		return typeName.length;
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
			localTypeViewHold.layout = (LinearLayout) paramView
					.findViewById(R.id.type_layout);
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
