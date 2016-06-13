package com.zhipu.chinavideo.db;


import com.zhipu.chinavideo.util.APP;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper{
	

	public MyOpenHelper(Context context) {
		
		super(context, APP.DB_NAME, null, APP.DB_VERSION);
	
	}
	
    public MyOpenHelper(Context context ,String db_name) {
		
		super(context, db_name, null, APP.DB_VERSION);
	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql = "create table " + APP.TABLE_NAME+ "("
				+ "hisId integer PRIMARY KEY AUTOINCREMENT,"
				+ "hisImg varchar(255) not null,"
				+ "hisName varchar(50) not null,"
				+ "hisLv varchar(255) not null,"
				+ "hisTime varchar(255) not null,"
				+ "hisRoom varchar(255) not null"
				+ ")";
		
		db.execSQL(sql);

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
	
}
