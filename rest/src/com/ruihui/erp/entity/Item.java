package com.ruihui.erp.entity;

import java.util.Date;

import com.ruihui.dao.annotation.Field;
import com.ruihui.dao.annotation.Id;
import com.ruihui.dao.annotation.Field.FieldType;

public class Item {
	private Long id;
	private String code;
	private String name;
	private Long typeId;
	private String remark;
	private String uom;
	private Date createdTime;
	@Id(name="id")
	public Long getId() {
		return id;
	}
	@Field(name="id",type=FieldType.SET)
	public void setId(Long id) {
		this.id = id;
	}
	@Field(name="code",type=FieldType.GET)
	public String getCode() {
		return code;
	}
	@Field(name="code",type=FieldType.SET)
	public void setCode(String code) {
		this.code = code;
	}
	@Field(name="name",type=FieldType.GET)
	public String getName() {
		return name;
	}
	@Field(name="name",type=FieldType.SET)
	public void setName(String name) {
		this.name = name;
	}
	@Field(name="type_id",type=FieldType.GET)
	public Long getTypeId() {
		return typeId;
	}
	@Field(name="type_id",type=FieldType.SET)
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	@Field(name="remark",type=FieldType.GET)
	public String getRemark() {
		return remark;
	}
	@Field(name="remark",type=FieldType.SET)
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Field(name="uom",type=FieldType.GET)
	public String getUom() {
		return uom;
	}
	@Field(name="uom",type=FieldType.SET)
	public void setUom(String uom) {
		this.uom = uom;
	}
	@Field(name="created_time",type=FieldType.GET)
	public Date getCreatedTime() {
		return createdTime;
	}
	@Field(name="created_time",type=FieldType.SET)
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
}
