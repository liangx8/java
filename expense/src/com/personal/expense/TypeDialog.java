package com.personal.expense;

import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.personal.expense.entity.ExpType;
import com.personal.expense.sqlite.CursorUtils;
import com.personal.expense.utils.CollectionUtils;

public class TypeDialog extends Dialog{
	private TypeName2Adapter tn2Adapter;
	public TypeDialog(Context context) {
		super(context);
		setContentView(R.layout.type_dialog);
		TextView typeSelectText=(TextView) findViewById(R.id.textview_select_text);
		typeSelectText.setText("");
		Resources res=context.getResources();
		setTitle(res.getString(R.string.pick_expense_type));
		SQLiteDatabase readDb=DataPool.sqlHelper.getReadableDatabase();
		Cursor csr=readDb.query("ex_type", null, null, null, null, null, "type_name1,type_name2");
		List<ExpType> types=CursorUtils.populate(csr, ExpType.entityPopulated());
		ListView lv1=(ListView) findViewById(R.id.list_type_name1);
		
		ListView lv2=(ListView) findViewById(R.id.list_type_name2);
		tn2Adapter=new TypeName2Adapter(context,typeSelectText);
		lv2.setAdapter(tn2Adapter);
		lv1.setAdapter(new TypeName1Adapter(context,types,lv2,typeSelectText));
		Button b=(Button) findViewById(android.R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}
	public ExpType getExpType(){
		return tn2Adapter.chosen;
	}
	private static class TypeName2Adapter extends BaseAdapter{
		private List<ExpType> typeName2;
		private ExpType chosen;
		final private Context parent;
		final private TextView textViewType;
		public TypeName2Adapter(Context ctx,TextView v) {
			parent=ctx;
			textViewType=v;
		}
		public void setTypeName2(List<ExpType> v){
			typeName2=v;
			this.notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return typeName2==null?0:typeName2.size();
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
		public View getView(int index, View view, ViewGroup arg2) {
			Type2Button v;
			if(view == null){
				v=new Type2Button(parent,this);
			} else {
				v=(Type2Button) view;
			}
			v.setExpType(typeName2.get(index));
			return v;
		}
		private void updateChoosen(ExpType type){
			textViewType.setText(type.getTypeName1()+" - "+type.getTypeName2());
			chosen=type;
		}
		
		private static class Type2Button extends Button implements View.OnClickListener{
			private ExpType expType;
			final private TypeName2Adapter tn2Adapter;
			public Type2Button(Context context,TypeName2Adapter adapter) {
				super(context);
				tn2Adapter=adapter;
				setOnClickListener(this);
			}

			@Override
			public void onClick(View arg0) {
				tn2Adapter.updateChoosen(expType);
			}

			public void setExpType(ExpType expType) {
				this.expType = expType;
				setText(expType.getTypeName2());
			}
			
		}
	}
	private static class TypeName1Adapter extends BaseAdapter{
		final private Map<String,List<ExpType>> types;
		final private List<String> name1s;
		final private Context parent;
		final private TypeName2Adapter adapter2;
		final private TextView textViewType;
		public TypeName1Adapter(Context ctx,List<ExpType> data,ListView typeName2,TextView v) {
			textViewType=v;
			adapter2=(TypeName2Adapter) typeName2.getAdapter();
			if(adapter2==null){
				throw new NullPointerException("adapter2 should not null");
			}
			parent=ctx;
			types=CollectionUtils.emptyMap();
			name1s=CollectionUtils.emptyList();
			for(ExpType et:data){
				List<ExpType> subTypes=types.get(et.getTypeName1());
				if(subTypes==null){
					subTypes=CollectionUtils.emptyList();
					types.put(et.getTypeName1(), subTypes);
					name1s.add(et.getTypeName1());
					Log.i("type_name1",et.getTypeName1());
				}
				subTypes.add(et);
			}
			Log.i("type_size",String.valueOf(types.size()));
		}
		@Override
		public int getCount() {
			return types.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			Type1Button btn;
			if(view==null){
				btn=new Type1Button(parent,this);
			} else {
				btn = (Type1Button) view;
			}
			btn.setTypeName1(name1s.get(index));
			return btn;
		}
		void updateTypeName2List(String typeName1){
			Log.i("toast",typeName1);
			adapter2.setTypeName2(types.get(typeName1));
			textViewType.setText(typeName1);
		}
		private static class Type1Button extends Button implements View.OnClickListener{
			private String typeName1;
			final private TypeName1Adapter typeName2;
			public Type1Button(Context context,TypeName1Adapter tn2) {
				super(context);
				typeName2=tn2;
				setOnClickListener(this);
			}

			public void setTypeName1(String n){
				typeName1=n;
				setText(n);
			}

			@Override
			public void onClick(View v) {
				typeName2.updateTypeName2List(typeName1);
			}
			
		}
	}

}
