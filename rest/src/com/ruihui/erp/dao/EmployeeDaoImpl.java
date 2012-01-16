package com.ruihui.erp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ruihui.common.utils.CollectionUtils;
import com.ruihui.common.utils.Combinator;
import com.ruihui.dao.GenericDaoImpl;
import com.ruihui.dao.SqlHelperDao;
import com.ruihui.dao.SqlHelperDaoImpl;
import com.ruihui.dao.exception.MessageException;
import com.ruihui.dao.utils.Page;
import com.ruihui.erp.ErpConstant;
import com.ruihui.erp.entity.EmpTransfer;
import com.ruihui.erp.entity.Employee;
import com.ruihui.erp.vo.VOEmployee;

public class EmployeeDaoImpl extends GenericDaoImpl<Employee> implements EmployeeDao{
	final private Log log=LogFactory.getLog(EmployeeDaoImpl.class);
	final private JdbcTemplate jdbcTemplate;
	final private EmpTransferDao empTransferDao;
	final private SqlHelperDao sqlHelperDao;
	//final private SAXParser parser;
	public EmployeeDaoImpl(JdbcTemplate template) {
		super(template);
		//SAXParserFactory factory=SAXParserFactory.newInstance();
		//parser=factory.newSAXParser();
		jdbcTemplate=template;
		empTransferDao=new EmpTransferDaoImpl(template);
		sqlHelperDao=new SqlHelperDaoImpl(template);
		
	}
	@Override
	protected Class<Employee> entityClass() {
		return Employee.class;
	}
	@Override
	public boolean existCode(String code) {
		int count=jdbcTemplate.queryForInt("select count(1) from erp_employee where emp_code = '"+code + "'");
		return count > 0;
	}
	@Override
	public void join(Employee employee, long joinedDate) throws MessageException {
		boolean deptCodeNotExist=true;
		for(String dept:ErpConstant.DEPARTMENTS){
			if(dept.equals(employee.getDepartment())){
				deptCodeNotExist=false;
				break;
			}
		}
		if(deptCodeNotExist){
			throw new MessageException("部门代码不存在");
		}
		insert(employee);
		EmpTransfer empTran=new EmpTransfer();
		empTran.setEmpId(employee.getId());
		empTran.setDepartment(ErpConstant.ROOT_DEPARTMMENT);
		Date d=new Date(joinedDate);
		empTran.setJoinedDate(d);
		empTransferDao.insert(empTran);
		empTran=new EmpTransfer();
		empTran.setEmpId(employee.getId());
		empTran.setDepartment(employee.getDepartment());
		empTran.setJoinedDate(d);
		empTransferDao.insert(empTran);
	}
	@Override
	public void bindDate(List<Employee> emps,Combinator<Long[],Employee, Object> c) {
		String sql="select et.id,et.emp_id,et.joined_date,et.left_date from erp_emp_transfer et left join erp_employee e on et.emp_id = e.id where et.department = ?";
		log.info(sql);
		List<Map<String,Object>> list=sqlHelperDao.query(sql,new Object[]{ErpConstant.ROOT_DEPARTMMENT}, new RowMapper<Map<String,Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int arg1)
					throws SQLException {
				Map<String,Object> map=CollectionUtils.emptyMap();
				map.put("emp_id", rs.getLong("emp_id"));
				map.put("joined_date", rs.getTimestamp("joined_date"));
				map.put("left_date", rs.getTimestamp("left_date"));
				return map;
			}
		});
		for(Employee e:emps){
			Map<String,Object> map=null;
			for(Map<String,Object> m:list){
				Long id=(Long) m.get("emp_id");
				if(id.equals(e.getId())) {
					map=m;
					break;
				}
			}
			if(map==null) throw new RuntimeException("data of table erp_employee have error!");
			Object d=map.get("left_date");
			Long l;
			if(d==null)
				l=null;
			else
				l=((Date)d).getTime();
			c.combinate(new Long[]{((Date)map.get("joined_date")).getTime(),l},e);
		}
	}
	
	public void loadEmployee(){
		DataPool.employees=Collections.unmodifiableCollection(this.all(null));
	}
	@Override
	public Page page(int start, int limit, String sort, String dir) {
		Page p=new Page();
		p.setTotal(count());
		p.setLimit(limit);
		p.setStart(start);
		StringBuilder sb=new StringBuilder();
		if(sort != null){
			sb.append("order by ");
			sb.append(sort);
		}
		sb.append("limit ");
		sb.append(start);
		sb.append(',');
		sb.append(limit);
		List<VOEmployee> voes=CollectionUtils.emptyList();
		List<Employee> es=list(sb.toString());
		for(Employee e:es){
			VOEmployee voe=new VOEmployee(e);
			voes.add(voe);
		}
		p.setData(voes);
		return p;
	}
}
/*
select et.id,et.emp_id,et.joined_date,et.left_date,et.department,et.created_time,e.`status` 
from erp_emp_transfer et left join erp_employee e on et.emp_id = e.id
where et.department = 'HEAD'
 */
