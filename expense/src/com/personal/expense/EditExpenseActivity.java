package com.personal.expense;

import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.personal.expense.entity.ExpType;
import com.personal.expense.entity.Expense;

public class EditExpenseActivity extends Activity {
	private final static int DATE_PICKER_DIALOG=1;
	private final static int TYPE_DIALOG=2;
	private Expense expense;
	private Button typePicking,expenseTime;
	private boolean isCancel=false;
	private DialogInterface.OnCancelListener cancelListener= new DialogInterface.OnCancelListener(){
		@Override
		public void onCancel(DialogInterface dialog) {
			isCancel=true;
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_expense);
		expenseTime=(Button) findViewById(R.id.expense_time);
		expense = new Expense();
		long issueDate=getIntent().getLongExtra("issue_date", new Date().getTime());
		expense.setIssueDate(new Date(issueDate));
		expenseTime.setText(expense.getIssueDate().toLocaleString());
		expenseTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_PICKER_DIALOG);
				
			}
		});
		typePicking = (Button) findViewById(R.id.btn_type_picking);
		typePicking.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(TYPE_DIALOG);
			}
		});
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg;
		switch(id){
		case TYPE_DIALOG:
			dlg=new TypeDialog(this);
			dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface itfc) {
					if(!isCancel){
						TypeDialog d=(TypeDialog) itfc;
						ExpType t=d.getExpType();
						if(t!=null){
							typePicking.setText(t.getTypeName1()+" - "+t.getTypeName2());
							expense.setTypeId(t.getId());
						}
					}
					Log.i("dismiss",itfc.getClass().getName());
				}
			});
			dlg.setOnCancelListener(cancelListener);
			break;
		case DATE_PICKER_DIALOG:
			dlg=new TimePickerDialog(this);
			dlg.setOnCancelListener(cancelListener);
			break;
		default:
			dlg=super.onCreateDialog(id);
		}
		return dlg;
	}
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id){
		case DATE_PICKER_DIALOG:
			TimePickerDialog dlg=(TimePickerDialog) dialog;
			dlg.drawScreen(expense.getIssueDate());
			break;
		}
		super.onPrepareDialog(id, dialog);
	}
	private static class TimePickerDialog extends Dialog{
		private final Context parent;
		private DatePicker dp;
		private TimePicker tp;
		public TimePickerDialog(Context ctx) {
			super(ctx);
			parent=ctx;
		}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.time_picker_dialog);
			Resources res=parent.getResources();
			String title=res.getString(R.string.time_picker_title);
			setTitle(title);
			dp= (DatePicker) TimePickerDialog.this.findViewById(R.id.date_picker1);
			tp= (TimePicker) TimePickerDialog.this.findViewById(R.id.time_picker1);
			Button b=(Button) findViewById(android.R.id.button1);
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
		public void drawScreen(Date issueDate) {
			dp.updateDate(issueDate.getYear()+1900, issueDate.getMonth(),issueDate.getDate());
			tp.setCurrentHour(issueDate.getHours());
			tp.setCurrentMinute(issueDate.getMinutes());
			//dp.set
			//tv.setText(issueDate.toGMTString());
		}
		
	}
}
