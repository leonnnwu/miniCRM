package com.wuliang.TestCRM.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDAO {
	private Context ctx = null;
	protected SQLiteDatabase sd = null;
	
	public BaseDAO(Context ctx){
		this.ctx = ctx;
	}
	
	class DBHelper extends SQLiteOpenHelper{
		
		String sql = "create table contact (_id integer primary key autoincrement, " +
				"client_name text not null, contact_name text not null, tel text not null," +
				"email text, address text);";

		public DBHelper(Context context, String name,CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table contact");
			db.execSQL(sql);
		}
		
	}
	
	public void createDatabase(){
		DBHelper myDBHelper = new DBHelper(this.ctx, "contacts.db", null, 1);
		try{
			sd = myDBHelper.getWritableDatabase();
		}catch(Exception e){
			e.printStackTrace();
			sd = myDBHelper.getReadableDatabase();
		}
	}
	
	public void close(){
		if(sd != null){
			sd.close();
			sd = null;
		}
	}
}
