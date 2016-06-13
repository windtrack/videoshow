package com.zhipu.chinavideo.cityseleced;

import java.util.ArrayList;
import java.util.List;

import com.zhipu.chinavideo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProvinceListActivity extends Activity {

	ListView lv;
	Context mcontext;
	String provinces[][];// 存放省数据 0_id 1_name
	int provinceCount = 0;// 读取数据库中省的数量
	private ImageView img_back;
	private TextView title_tv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provincelist);
		mcontext = this;
		lv = (ListView) findViewById(R.id.listview_province);
		img_back = (ImageView) findViewById(R.id.title_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title_tv = (TextView) findViewById(R.id.title_text);
		title_tv.setText("选择所在地");
		// 开始
		MyDatabase myDB = new MyDatabase(this);
		Cursor cProvinces = myDB.getProvinces();
		provinceCount = cProvinces.getCount();
		provinces = new String[provinceCount][2];

		for (int j = 0; j < provinceCount; j++) {
			provinces[j][0] = cProvinces.getString(0);
			provinces[j][1] = cProvinces.getString(1);
			cProvinces.moveToNext();
		}

//		lv.setAdapter(new ArrayAdapter<String>(this,
//				android.R.layout.simple_expandable_list_item_1, getData()));
		lv.setAdapter(new ArrayAdapter<String>(this,
				R.layout.text_item, getData()));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// Toast.makeText(mcontext,p[position], 1).show();
				Intent i = new Intent(mcontext, CityListActivity.class);
				i.putExtra("provinceid", provinces[position][0]);
				i.putExtra("provincename", provinces[position][1]);
				startActivityForResult(i, 1);
				// int version = Integer.valueOf(android.os.Build.VERSION.SDK);
				// if (version >= 5) {
				// overridePendingTransition(android.R.anim.slide_in_left,
				// android.R.anim.fade_out);
				// }
			}
		});
	}

	protected void onActivityResult(int resultCode, int re, Intent data) {
		String cityname;
		if (data == null) {
			return;
		}
		cityname = data.getStringExtra("cityname");
		returnResult(cityname);
	}

	void returnResult(String sdata) {
		Intent result = new Intent();
		result.putExtra("cityname", sdata);
		setResult(21, result);
		finish();
	}

	public List<String> getData() {

		List<String> ls = new ArrayList<String>();
		ls = asList(provinces);
		return ls;
	}

	// 字符创数组转化list
	public List<String> asList(String s[][]) {
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < provinceCount; i++) {
			if (s[i][1] != null)
				l.add(s[i][1]);
		}
		return l;
	}
}
