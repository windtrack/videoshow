package com.zhipu.chinavideo.cityseleced;


import java.util.ArrayList;
import java.util.List;

import com.zhipu.chinavideo.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class CityListActivity extends Activity{
    private ImageView title_back;
    private TextView title_tv;
	
	String citys[][];
	int cityCount=0;
	ListView lv;
	private TextView title_text;
	private ImageView title_img;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provincelist);
        Intent i=getIntent();
        String provinceid=i.getStringExtra("provinceid");
        lv=(ListView)findViewById(R.id.listview_province);
        title_back=(ImageView) findViewById(R.id.title_back);
        title_tv=(TextView) findViewById(R.id.title_text);
        title_tv.setText("选择所在地");
        title_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        MyDatabase myDB = new MyDatabase(this);
        Cursor cCity = myDB.getCities(provinceid);
        cityCount=cCity.getCount();
        if(cityCount==0)
        {
        	returnResult(i.getStringExtra("provincename"));
        	
        }
        citys=new String[cityCount][2];
        
        for(int j=0;j<cityCount;j++)
        {
        	citys[j][0]=cCity.getString(0);
        	citys[j][1]=cCity.getString(1);	
        	cCity.moveToNext();
        }
        
        lv.setAdapter(new ArrayAdapter<String>(this,R.layout.text_item,getData()));
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				returnResult(citys[position][1]);
			}
		});
        
    }
	
	void returnResult(String sdata) {
		Intent result = new Intent();
		result.putExtra("cityname", sdata);
		setResult(21, result);
		finish();
	}

	public List<String> getData() {

		List<String> ls=new ArrayList<String>();
		ls=asList(citys);
		return ls;
	}
	
	//字符创数组转化list
	public List<String>  asList(String s[][]){
		List<String> l=new ArrayList<String>();
		for(int i=0;i<cityCount;i++)
		{
			if(s[i][1]!=null)
				l.add(s[i][1]);
		}
		return l;	
	}
}
