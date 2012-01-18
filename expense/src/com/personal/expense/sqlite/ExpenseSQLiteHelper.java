package com.personal.expense.sqlite;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.personal.expense.DataPool;
import com.personal.expense.R;

public class ExpenseSQLiteHelper extends SQLiteOpenHelper {
	private static final String DB_NAME="sqlitedb";
	private static final int DB_VERSION = 1;
	private final Resources res;
	public ExpenseSQLiteHelper(Context ctx) {
		super(ctx,DB_NAME,null,DB_VERSION);
		res=ctx.getResources();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		List<String> sqlList=DataPool.sqlList(res,R.raw.table_define_sql);
		for(String sql:sqlList){
			if(sql.startsWith("--"))continue;
			if(sql.trim().length()==0)continue;
			Log.d("debug", sql);
			db.execSQL(sql);
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Cursor c=db.rawQuery("select count(1) from ex_type", null);
		c.moveToNext();
		int count=c.getInt(0);
		c.close();
		if(count==0){
			List<String> sqlList=DataPool.sqlList(res, R.raw.init_type_sql);
			for(String s:sqlList){
				if(s.startsWith("--"))continue;
				if(s.trim().length()==0)continue;
				Log.i("sql",s);
				db.execSQL(s);
			}
		}
	}
}
