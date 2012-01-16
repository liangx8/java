package com.ruihui.erp.dao;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ruihui.common.utils.Converter;
import com.ruihui.dao.GenericDao;
import com.ruihui.dao.exception.MessageException;
import com.ruihui.erp.entity.Employee;
import com.ruihui.erp.entity.MeatTicket;
import com.ruihui.erp.vo.ViewObjectMeatReport;
@Transactional(readOnly=true)
public interface MeatTicketDao extends GenericDao<MeatTicket> {

	@Transactional(readOnly=false)
	void printingRecord(String strValue, MeatTicket mt,Converter<MeatTicket, MeatTicket> converter);

	/**
	 * 标记餐票已经被使用
	 * @param ticketId
	 */
	@Transactional(readOnly=false)
	void markUsed(Long[] ticketId);

	/**
	 * 餐费报告
	 * @param start
	 * @param end
	 * @return
	 */
	List<ViewObjectMeatReport> meatReport(Date start, Date end);

	Employee availabledEmployee(String empCode, Date issueDate) throws MessageException;

}
