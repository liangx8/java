package com.personal.expense.entity;

import android.database.Cursor;

import com.personal.expense.sqlite.CursorUtils.EntityPopulated;


public class Account{
	private Integer id;
	private String accountAbbr;
	private String accountName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccountAbbr() {
		return accountAbbr;
	}
	public void setAccountAbbr(String accountAbbr) {
		this.accountAbbr = accountAbbr;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public static EntityPopulated<Account> entityPopulated(){
		return new EntityPopulated<Account>() {
			@Override
			public Account convert(Cursor c) {
				Account entity=new Account();
				int cc=c.getColumnCount();
				for(int i=0;i<cc;i++){
					String cn=c.getColumnName(i);
					if("id".equals(cn)){
						entity.id=c.getInt(i);
					}
					if("account_abbr".equals(cn)){
						entity.accountAbbr=c.getString(i);
					}
					if("account_name".equals(cn)){
						entity.accountName=c.getString(i);
					}
				}
				
				return entity;
			}
		};
	}
}
