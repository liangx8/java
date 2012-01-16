package com.ruihui.erp.vo;

import com.ruihui.erp.dao.DataPool;
import com.ruihui.erp.entity.Employee;
import com.ruihui.erp.entity.MeatTicket;

public class ViewObjectMeatTicket extends MeatTicket {
	static private Integer AM=1;
	static private Integer PM=2;
	static private Integer USED=1;
	public String getEmpName(){
		Long empId=getEmpId();
		for(Employee e:DataPool.employees){
			if(empId.equals(e.getId()))
				return e.getEmpName();
		}
		return "";
	}
	public String getAmpmName(){
		Integer ap=getAmpm();
		if(AM.equals(ap)) return "上午";
		if(PM.equals(ap)) return "下午";
		return "";
	}
	public String getUsedName(){
		Integer used=getUsed();
		if(USED.equals(used)){
			return "已用";
		}
		return "未用";
	}
}
