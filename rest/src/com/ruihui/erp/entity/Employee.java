package com.ruihui.erp.entity;

import java.util.Date;

import com.ruihui.dao.annotation.Field;
import com.ruihui.dao.annotation.Field.FieldType;
import com.ruihui.dao.annotation.Id;
import com.ruihui.dao.annotation.Table;

@Table(name="erp_employee")
public class Employee {
	private Long id;
	private String empCode;
	private String empName;
	private Date birthDate;
	private Integer gender;
	private String department;
	private Integer status;
	private String personId;
	private String remark;
	private Date createdTime;
	@Id(name="id")
	public Long getId() {
		return id;
	}
	@Field(name="id",type=FieldType.SET)
	public void setId(Long id) {
		this.id = id;
	}
	@Field(name="emp_code",type=FieldType.GET)
	public String getEmpCode() {
		return empCode;
	}
	@Field(name="emp_code",type=FieldType.SET)
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	@Field(name="emp_name",type=FieldType.GET)
	public String getEmpName() {
		return empName;
	}
	@Field(name="emp_name",type=FieldType.SET)
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	@Field(name="birth_date",type=FieldType.GET)
	public Date getBirthDate() {
		return birthDate;
	}
	@Field(name="birth_date",type=FieldType.SET)
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	@Field(name="gender",type=FieldType.GET)
	public Integer getGender() {
		return gender;
	}
	@Field(name="gender",type=FieldType.SET)
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	@Field(name="department",type=FieldType.GET)
	public String getDepartment() {
		return department;
	}
	@Field(name="department",type=FieldType.SET)
	public void setDepartment(String department) {
		this.department = department;
	}
	@Field(name="status",type=FieldType.GET)
	public Integer getStatus() {
		return status;
	}
	@Field(name="status",type=FieldType.SET)
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Field(name="created_time",type=FieldType.GET)
	public Date getCreatedTime() {
		return createdTime;
	}
	@Field(name="created_time",type=FieldType.SET)
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@Field(name="person_id",type=FieldType.GET)
	public String getPersonId() {
		return personId;
	}
	@Field(name="person_id",type=FieldType.SET)
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	@Field(name="remark",type=FieldType.GET)
	public String getRemark() {
		return remark;
	}
	@Field(name="remark",type=FieldType.SET)
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
