package com.ruihui.erp.entity;

import java.util.Date;

import com.ruihui.dao.annotation.Field;
import com.ruihui.dao.annotation.Field.FieldType;
import com.ruihui.dao.annotation.Id;
import com.ruihui.dao.annotation.Table;
@Table(name="erp_code_master")
public class CodeMaster {
	private Long id;
	private String category;
	private String abbr;
	private String fullDesc;
	private Date createdTime;
	@Id(name="id")
	public Long getId() {
		return id;
	}
	@Field(name="id",type=FieldType.SET)
	public void setId(Long id) {
		this.id = id;
	}
	@Field(name="category",type=FieldType.GET)
	public String getCategory() {
		return category;
	}
	@Field(name="category",type=FieldType.SET)
	public void setCategory(String category) {
		this.category = category;
	}
	@Field(name="abbr",type=FieldType.GET)
	public String getAbbr() {
		return abbr;
	}
	@Field(name="abbr",type=FieldType.SET)
	public void setAbbr(String addr) {
		this.abbr = addr;
	}
	@Field(name="full_desc",type=FieldType.GET)
	public String getFullDesc() {
		return fullDesc;
	}
	@Field(name="full_desc",type=FieldType.SET)
	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
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
