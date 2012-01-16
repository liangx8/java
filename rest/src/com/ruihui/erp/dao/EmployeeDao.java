package com.ruihui.erp.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ruihui.common.utils.Combinator;
import com.ruihui.dao.GenericDao;
import com.ruihui.dao.exception.MessageException;
import com.ruihui.dao.utils.Page;
import com.ruihui.erp.entity.Employee;
@Transactional(readOnly=true)
public interface EmployeeDao extends GenericDao<Employee> {
	boolean existCode(String code);

	/**
	 * 
	 * @param employee 新职员信息
	 * @param departmentCode 加入部门
	 * @param joinedDate 加入日期
	 */
	@Transactional(readOnly=false)
	void join(Employee employee, long joinedDate) throws MessageException;
	/**
	 * 为每个employee组合入职日期,使用回调的设计是为了把拆分 employee 成为xml的业务放到action层
	 * @param emps
	 * @param combinator
	 */
	void bindDate(List<Employee> emps,Combinator<Long[],Employee,Object> combinator);
	@Transactional(readOnly=false)
	void update(Employee e);
	Page page(int start, int limit, String sort, String dir);
}
