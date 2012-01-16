package com.ruihui.erp.dao;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.ruihui.dao.GenericDao;
import com.ruihui.erp.entity.EmpTransfer;
@Transactional(readOnly=true)
public interface EmpTransferDao extends GenericDao<EmpTransfer>{

	/**
	 * 职员内部调动或者离职.<br />
	 * 如果department是 ErpConstant.ROOT_DEPARTMMENT, 职员离职
	 * @param empId	职员ID
	 * @param department 调入部门
	 * @param date 日期
	 */
	@Transactional(readOnly=false)
	void transfer(Long empId, String department, Date date);

}
