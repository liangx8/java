package com.ruihui.erp.vo;

import java.util.List;

import com.ruihui.erp.dao.DataPool;
import com.ruihui.erp.entity.Employee;
import com.ruihui.erp.entity.MeatTicket;


public class ViewObjectMeatReport {
	private long empId;
	private int count;
	private List<MeatTicket> history;
	public long getEmpId() {
		return empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<MeatTicket> getHistory() {
		return history;
	}
	public void setHistory(List<MeatTicket> history) {
		this.history = history;
	}
	public String getEmpName(){
		for(Employee e:DataPool.employees){
			if(empId==e.getId().longValue())
				return e.getEmpName();
		}
		return "";
		
	}
	public String getEmpCode() {
		for(Employee e:DataPool.employees){
			if(empId==e.getId().longValue())
				return e.getEmpCode();
		}
		return "";
	}
	
}
