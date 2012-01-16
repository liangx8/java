package com.ruihui.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ruihui.dao.GenericDaoImpl;
import com.ruihui.erp.ErpConstant;
import com.ruihui.erp.entity.EmpTransfer;

public class EmpTransferDaoImpl extends GenericDaoImpl<EmpTransfer> implements
		EmpTransferDao {
	private JdbcTemplate jdbcTemplate;
	private Log log=LogFactory.getLog(EmpTransferDaoImpl.class);
	protected EmpTransferDaoImpl(JdbcTemplate template) {
		super(template);
		jdbcTemplate=template;
	}

	@Override
	protected Class<EmpTransfer> entityClass() {
		return EmpTransfer.class;
	}

	@Override
	public void transfer(Long empId, String department, Date date) {
		List<EmpTransfer> list=list("where emp_id = ? and left_date is null", new Object[]{empId});
		// only two records were expected!
		switch(list.size()){
		case 0:
			return;
		case 2:
			if(ErpConstant.ROOT_DEPARTMMENT.equals(department)){
				//离职
				for(EmpTransfer et:list){
					et.setLeftDate(date);
					update(et);
					employeeStatus(empId,ErpConstant.EMPLOYEE_STATUS_LEFT);
				}
			} else {
				// 调动
				for(EmpTransfer et:list){
					if(!ErpConstant.ROOT_DEPARTMMENT.equals(et.getDepartment())){
						if(et.getDepartment().equals(department)){
							// 相同的部门.无效的操作.
							return;
						}
						et.setLeftDate(date);
						update(et);
						EmpTransfer et1=new EmpTransfer();
						et1.setDepartment(department);
						et1.setEmpId(empId);
						et1.setJoinedDate(date);
						insert(et1);
						employeeDepartment(empId,department);
					}
				}
			}
			break;
		default:
			throw new RuntimeException("员工在职数据有错误,表:erp_emp_transfer,id:"+empId);
		}
	}

	private void employeeDepartment(Long empId, String department) {
		String sql="update erp_employee set department = ? where id = ?";
		log.info(sql);
		log.info(String.format("[%s,%d]", department,empId));
		jdbcTemplate.update(sql, department,empId);
		
	}

	private void employeeStatus(Long empId,int status) {
		String sql="update erp_employee set status = ? where id = ?";
		log.info(sql);
		log.info(String.format("[%d,%d]", status,empId));
		jdbcTemplate.update(sql, status,empId);
	}


}
