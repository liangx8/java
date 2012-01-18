package com.personal.expense;

import java.util.List;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.personal.expense.entity.Account;
import com.personal.expense.entity.Expense;
import com.personal.expense.sqlite.CursorUtils;
import com.personal.expense.sqlite.ExpenseSQLiteHelper;

public class ExpenseActivity extends ListActivity implements OnClickListener {
	private List<Expense> expenses;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        DataPool.accountFile=settings.getString("account_abbr", Constants.DEFAULT_ACCOUNT);
        DataPool.sqlHelper=new ExpenseSQLiteHelper(this);
        SQLiteDatabase readDb=DataPool.sqlHelper.getReadableDatabase();
        Cursor c=readDb.query("ex_account", null, "account_abbr = ?", new String[]{DataPool.accountFile}, null, null, null);
        Account acc=new Account();
        if(!c.moveToFirst()){
        	SQLiteDatabase writeDb = DataPool.sqlHelper.getWritableDatabase();
        	DatabaseUtils.InsertHelper insertHelper=new DatabaseUtils.InsertHelper(writeDb, "ex_account");
        	ContentValues cv=new ContentValues();
        	cv.put("account_abbr", DataPool.accountFile);
        	cv.put("account_name", DataPool.accountFile);
        	insertHelper.insert(cv);
        	c=readDb.query("ex_account", null, "account_abbr = ?", new String[]{DataPool.accountFile}, null, null, null);
        }
        acc.setId(c.getInt(0));
        acc.setAccountAbbr(c.getString(1));
        acc.setAccountName(c.getString(2));
        c.close();
        c=readDb.query("ex_expense", null, "account_id = ?", new String[]{String.valueOf(acc.getId())}, null, null, "issue_date desc");
        expenses = CursorUtils.populate(c,Expense.entityPopulated());
        c.close();
        Resources res=getResources();
        if(Constants.DEFAULT_ACCOUNT.equals(DataPool.accountFile)){
        	setTitle(res.getString(R.string.default_account));
        } else {
        	setTitle(acc.getAccountName());
        }
        
        View v=findViewById(R.id.textview_empty);
        ListView lv=getListView();
        if(expenses.size()==0){
        	v.setVisibility(View.VISIBLE);
        	lv.setVisibility(View.GONE);
        } else{
        	v.setVisibility(View.GONE);
        	lv.setVisibility(View.VISIBLE);
        }
        setListAdapter(new InnerAdapter(this,expenses));
        Button b=(Button) findViewById(R.id.btn_new_expense);
        b.setOnClickListener(this);
    }
	@Override
    protected void onStop() {
    	super.onStop();
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("account_abbr", DataPool.accountFile);
        editor.commit();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.exp_menu, menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.change_account:
			return true;
		case R.id.quit:
			finish();
			return true;
		default:
		}
		return super.onMenuItemSelected(featureId, item);
	}
	private static class InnerAdapter extends BaseAdapter{
		private final Context parent;
		private final List<Expense> expenses;
		public InnerAdapter(Context ctx,List<Expense> data) {
			parent=ctx;
			expenses=data;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return expenses.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View v, ViewGroup arg2) {
			TextView tv;
			if(v==null){
				tv=new TextView(parent);
			} else {
				tv=(TextView) v;
			}
			tv.setText("有一笔");
			return tv;
		}
	}
	@Override
	public void onClick(View v) {
		// new expense click
		Intent intent=new Intent(this, EditExpenseActivity.class);
		startActivity(intent);
	}
}