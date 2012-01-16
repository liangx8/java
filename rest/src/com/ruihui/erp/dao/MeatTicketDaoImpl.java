package com.ruihui.erp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ruihui.common.utils.Converter;
import com.ruihui.dao.GenericDaoImpl;
import com.ruihui.dao.SqlHelperDao;
import com.ruihui.dao.SqlHelperDaoImpl;
import com.ruihui.dao.exception.MessageException;
import com.ruihui.erp.ErpConstant;
import com.ruihui.erp.entity.Employee;
import com.ruihui.erp.entity.MeatTicket;
import com.ruihui.erp.vo.ViewObjectMeatReport;

public class MeatTicketDaoImpl extends GenericDaoImpl<MeatTicket> implements
		MeatTicketDao {
	private Log log=LogFactory.getLog(MeatTicketDaoImpl.class);
	final private SqlHelperDao sqlHelperDao;
	final private EmployeeDao employeeDao;
	protected MeatTicketDaoImpl(JdbcTemplate template) {
		super(template);
		sqlHelperDao = new SqlHelperDaoImpl(template);
		employeeDao = new EmployeeDaoImpl(template);
	}

	@Override
	protected Class<MeatTicket> entityClass() {
		return MeatTicket.class;
	}

	@Override
	public void printingRecord(String strValue, MeatTicket mt,
			Converter<MeatTicket, MeatTicket> converter) {
		String empIds[]=strValue.split(",");
		for(int i=0;i<empIds.length;i++){
			Long empId=Long.parseLong(empIds[i]);
			mt.setId(null);
			mt.setUsed(null);
			mt.setEmpId(empId);
			insert(mt);
			mt.setUsed(0);
			converter.covert(mt);
		}
	}

	@Override
	public void markUsed(Long[] ticketId) {
		String strId=StringUtils.join(ticketId,",");
		String sql=String.format("update erp_meat_ticket set used = 1 where id in (%s)", strId);
		log.info(sql);
		sqlHelperDao.executeUpdate(sql);
		
	}

	@Override
	public List<ViewObjectMeatReport> meatReport(Date start, Date end) {
		List<ViewObjectMeatReport> report;
		String sql="select emp_id,count(emp_id) from erp_meat_ticket where used = 1 and ticket_date between ? and ? group by emp_id";
		report=sqlHelperDao.query(sql, new Object[]{start,end},new RowMapper<ViewObjectMeatReport>(){
			@Override
			public ViewObjectMeatReport mapRow(ResultSet rs, int arg1)throws SQLException {
				ViewObjectMeatReport mr=new ViewObjectMeatReport();
				mr.setEmpId(rs.getLong(1));
				mr.setCount(rs.getInt(2));
				return mr;
			}});
		for(ViewObjectMeatReport mr:report){
			mr.setHistory(list("where used = 1 and emp_id = ? and ticket_date between ? and ?",new Object[]{mr.getEmpId(),start,end}));
			
		}
		return report;
	}

	@Override
	public Employee availabledEmployee(String empCode, Date issueDate) throws MessageException {
		final Employee e=employeeDao.unique("where emp_code = '"+empCode+"'");
		if(ErpConstant.EMPLOYEE_STATUS_LEFT == e.getStatus().intValue()){
			throw new MessageException("职员"+e.getEmpName()+"已经离职");
		}
		return e;
	}


}
