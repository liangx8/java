package com.personal.expense;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import android.content.res.Resources;
import android.util.Log;

import com.personal.expense.entity.Account;
import com.personal.expense.sqlite.ExpenseSQLiteHelper;
import com.personal.expense.utils.CollectionUtils;
import com.personal.expense.utils.IOUtils;

public class DataPool {
	public static String accountFile=null;
	public static final String sharedPath="expense";
	public static Account current;
	public static List<String> sqlList(Resources res,int fileId){
        InputStream in=res.openRawResource(fileId);
        InputStreamReader r=new InputStreamReader(in, Charset.forName("UTF-8"));
        List<String> sql=CollectionUtils.emptyList();
        String s;
        try {
			while((s=IOUtils.readLine(r))!=null){
				sql.add(s);
			}
			r.close();
			in.close();
		} catch (IOException e) {
			Log.w("!!",e.getMessage());
			throw new RuntimeException(e);
		}
        return sql;
	}
	public static ExpenseSQLiteHelper sqlHelper;
}
