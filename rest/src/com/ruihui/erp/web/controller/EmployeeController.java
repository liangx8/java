package com.ruihui.erp.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ruihui.common.text.TextObject;
import com.ruihui.common.text.TextObjectUtils;
import com.ruihui.common.utils.Combinator;
import com.ruihui.common.utils.XMLBody;
import com.ruihui.dao.exception.MessageException;
import com.ruihui.dao.utils.Order;
import com.ruihui.dao.utils.Page;
import com.ruihui.erp.dao.EmpTransferDao;
import com.ruihui.erp.dao.EmployeeDao;
import com.ruihui.erp.entity.Employee;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {
	final private EmployeeDao employeeDao;
	final private EmpTransferDao empTransferDao;
	@Autowired
	public EmployeeController(EmployeeDao dao,EmpTransferDao dao1) {
		employeeDao=dao;
		empTransferDao=dao1;
	}

	@ExceptionHandler(MessageException.class)
	public ModelAndView exceptionHandler(MessageException ex,HttpServletRequest request){
		return Utils.exceptionReturn(ex, request.getRequestURI());
		//return Utils.failureTextObject(ex.getMessage());
	}
	/**
	 * 参数格式为 <pre>
	 * &lt;employee attribute1="value" attribute2="value" attribute3="value" /&gt;</pre>
	 * @param xmlData
	 * @return
	 * @throws MessageException 
	 */
	@RequestMapping(value = "join", method = RequestMethod.POST)
	public TextObject join(@RequestBody byte[] xmlData) throws MessageException {
		XMLBody<Employee> body=new XMLBody<Employee>(xmlData,"employee",Employee.class);
		Employee employee=body.getObject();
		long joinedDate=Long.parseLong(body.parameter("joined-date"));

		TextObject result=Utils.successTextobject();
		if(employeeDao.existCode(employee.getEmpCode())){
			throw new MessageException("职员编号已存在");
		} else {
			employeeDao.join(employee,joinedDate);
		}
		return result;
	}
	@RequestMapping(value = "transfer", method = RequestMethod.POST)
	public TextObject transfer(@RequestBody byte[] xmlData){
		XMLBody<Object> t=new XMLBody<Object>(xmlData);
		Long empId=Long.parseLong(t.parameter("emp-id"));
		Long transferDate=Long.parseLong(t.parameter("transfer-date"));
		String department=t.parameter("department");
		empTransferDao.transfer(empId,department,new Date(transferDate));
		return Utils.successTextobject();
	}
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public TextObject update(@RequestBody byte[] xmlData) throws MessageException {
		XMLBody<Employee> t=new XMLBody<Employee>(xmlData, "employee", Employee.class);
		Employee e=t.getObject();
		if(e.getId()==null){
			throw new MessageException("必须提供ID参数");
		}
		employeeDao.update(e);
		return Utils.successTextobject();
	}
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public TextObject list(@RequestBody byte[] xmlData) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<Employee> emps = employeeDao.all(new Order("desc","emp_name").append("asc","birth_date"));
		final TextObject o=Utils.successTextobject();
		employeeDao.bindDate(emps,new Combinator<Long[],Employee,Object>(){
			@Override
			public Object combinate(Long[] d, Employee i2) {
				try {
					TextObject one=TextObjectUtils.toTextObject(i2);
					one.addAttribute("joined-date", d[0]);
					one.addAttribute("left-date", d[1]==null?0:d[1]);
					o.appendChild(one);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return null;
			}});
		return o;
	}
	@RequestMapping(value = "fetch", method = RequestMethod.POST)
	public TextObject employee(@RequestBody byte[] xmlData) throws MessageException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		XMLBody<Object> t=new XMLBody<Object>(xmlData);
		String empCode = t.parameter("emp_code");
		if(empCode == null){
			throw new MessageException("请输入工号");
		}
		if(employeeDao.existCode(empCode)){
			Employee e=employeeDao.unique("where emp_code = '"+empCode + "'");
			TextObject result = Utils.successTextobject();
			return result.appendChild(TextObjectUtils.toTextObject(e));
		} else {
			throw new MessageException("工号不存在!");
		}
	}
	/**
	 * 
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @return
	 */
	@RequestMapping(value="page",method=RequestMethod.GET)
	public ModelMap page(@RequestParam(value="start",defaultValue="0",required=false)int start,@RequestParam(value="limit",defaultValue="10",required=false)int limit,@RequestParam(value="sort",required=false)String sort,@RequestParam(value="dir",required=false)String dir){
		Page page =employeeDao.page(start,limit,sort,dir);

		ModelMap mm=new ModelMap();
		mm.addAttribute("page", page);
		return mm;
	}
}
