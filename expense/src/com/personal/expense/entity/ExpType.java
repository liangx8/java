package com.personal.expense.entity;

import java.util.Date;

import android.database.Cursor;

import com.personal.expense.sqlite.CursorUtils.EntityPopulated;

public class ExpType {
	private Integer id;
	private String typeName1;
	private String typeName2;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeName1() {
		return typeName1;
	}
	public void setTypeName1(String typeName1) {
		this.typeName1 = typeName1;
	}
	public String getTypeName2() {
		return typeName2;
	}
	public void setTypeName2(String typeName2) {
		this.typeName2 = typeName2;
	}
	public static EntityPopulated<ExpType> entityPopulated(){
		return new EntityPopulated<ExpType>(){

			@Override
			public ExpType convert(Cursor c) {
				ExpType entity=new ExpType();
				int cc=c.getColumnCount();
				for(int i=0;i<cc;i++){
					String cn=c.getColumnName(i);
					if("id".equals(cn)){
						entity.id=c.getInt(i);
					}
					if("type_name1".equals(cn)){
						entity.typeName1=c.getString(i);
					}
					if("type_name2".equals(cn)){
						entity.typeName2=c.getString(i);
					}
				}
				return entity;
			}
			
		};
	}
}
