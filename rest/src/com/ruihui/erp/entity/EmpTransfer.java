package com.ruihui.erp.entity;

import java.util.Date;

import com.ruihui.dao.annotation.Field;
import com.ruihui.dao.annotation.Field.FieldType;
import com.ruihui.dao.annotation.Id;
import com.ruihui.dao.annotation.Table;

@Table(name = "erp_emp_transfer")
public class EmpTransfer {
	private Long id;
	private Long empId;
	private Date joinedDate;
	private Date leftDate;
	private String department;
	private Date createdTime;

	@Id(name = "id")
	public Long getId() {
		return id;
	}

	@Field(name = "id", type = FieldType.SET)
	public void setId(Long id) {
		this.id = id;
	}

	@Field(name = "emp_id", type = FieldType.GET)
	public Long getEmpId() {
		return empId;
	}

	@Field(name = "emp_id", type = FieldType.SET)
	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Field(name = "joined_date", type = FieldType.GET)
	public Date getJoinedDate() {
		return joinedDate;
	}

	@Field(name = "joined_date", type = FieldType.SET)
	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}

	@Field(name = "left_date", type = FieldType.GET)
	public Date getLeftDate() {
		return leftDate;
	}

	@Field(name = "left_date", type = FieldType.SET)
	public void setLeftDate(Date leftDate) {
		this.leftDate = leftDate;
	}

	@Field(name = "department", type = FieldType.GET)
	public String getDepartment() {
		return department;
	}

	@Field(name = "department", type = FieldType.SET)
	public void setDepartment(String department) {
		this.department = department;
	}

	@Field(name = "created_time", type = FieldType.GET)
	public Date getCreatedTime() {
		return createdTime;
	}

	@Field(name = "created_time", type = FieldType.SET)
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
}
