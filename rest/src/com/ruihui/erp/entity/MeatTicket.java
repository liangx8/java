package com.ruihui.erp.entity;

import java.util.Date;

import com.ruihui.dao.annotation.Field;
import com.ruihui.dao.annotation.Field.FieldType;
import com.ruihui.dao.annotation.Id;
import com.ruihui.dao.annotation.Table;

@Table(name = "erp_meat_ticket")
public class MeatTicket {
	private Long id;
	private Long empId;
	private Date ticketDate;
	private Integer ampm;
	private Integer used;
	private Date createdTime;
	@Id(name="id")
	public Long getId() {
		return id;
	}
	@Field(name="id",type=FieldType.SET)
	public void setId(Long id) {
		this.id = id;
	}
	@Field(name="emp_id",type=FieldType.GET)
	public Long getEmpId() {
		return empId;
	}
	@Field(name="emp_id",type=FieldType.SET)
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	@Field(name="ticket_date",type=FieldType.GET)
	public Date getTicketDate() {
		return ticketDate;
	}
	@Field(name="ticket_date",type=FieldType.SET)
	public void setTicketDate(Date ticketDate) {
		this.ticketDate = ticketDate;
	}
	@Field(name="ampm",type=FieldType.GET)
	public Integer getAmpm() {
		return ampm;
	}
	@Field(name="ampm",type=FieldType.SET)
	public void setAmpm(Integer ampm) {
		this.ampm = ampm;
	}
	@Field(name="used",type=FieldType.GET)
	public Integer getUsed() {
		return used;
	}
	@Field(name="used",type=FieldType.SET)
	public void setUsed(Integer used) {
		this.used = used;
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
