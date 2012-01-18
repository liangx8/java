package com.personal.expense.entity;

import java.util.Date;

import android.database.Cursor;

import com.personal.expense.sqlite.CursorUtils.EntityPopulated;

public class Expense {
	private Integer id;
	private Date issueDate;
	private Integer typeId;
	private Integer accountId;
	private String remark;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public static EntityPopulated<Expense> entityPopulated(){
		return new EntityPopulated<Expense>(){

			@Override
			public Expense convert(Cursor c) {
				Expense entity=new Expense();
				int cc=c.getColumnCount();
				for(int i=0;i<cc;i++){
					String cn=c.getColumnName(i);
					if("id".equals(cn)){
						entity.id=c.getInt(i);
					}
					if("issue_date".equals(cn)){
						entity.issueDate=new Date(c.getInt(i));
					}
					if("type_id".equals(cn)){
						entity.typeId=c.getInt(i);
					}
					if("account_id".equals(cn)){
						entity.accountId=c.getInt(i);
					}
					if("remark".equals(cn)){
						entity.remark=c.getString(i);
					}
				}
				return entity;
			}
			
		};
	}
}
