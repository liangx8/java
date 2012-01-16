package com.ruihui.erp.vo;

import com.ruihui.erp.ErpConstant;
import com.ruihui.erp.entity.Employee;

public class VOEmployee extends Employee {
	public VOEmployee(Employee e) {
		setBirthDate(e.getBirthDate());
		setCreatedTime(e.getCreatedTime());
		setDepartment(e.getDepartment());
		setEmpCode(e.getEmpCode());
		setEmpName(e.getEmpName());
		setGender(e.getGender());
		setId(e.getId());
		setPersonId(e.getPersonId());
		setRemark(e.getRemark());
		setStatus(e.getStatus());
	}
	public String getStrDepartment(){
		String dept=getDepartment();
		for(int i=0;i<ErpConstant.DEPARTMENTS.length;i++){
			if(ErpConstant.DEPARTMENTS[i].equals(dept)){
				return ErpConstant.DEPT_DESCRIPTION[i];
			}
		}
		return "";
	}
	public String getStrGender(){
		Integer g=getGender();
		if(ErpConstant.GENDER_FEMALE.equals(g))
			return "女";
		if(ErpConstant.GENDER_MALE.equals(g))
			return "男";
		return "未输入";
	}
}
