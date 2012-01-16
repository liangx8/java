package com.ruihui.erp.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ruihui.common.text.TextObject;
import com.ruihui.common.text.TextObjectUtils;
import com.ruihui.common.utils.XMLBody;
import com.ruihui.dao.exception.MessageException;
import com.ruihui.erp.dao.EmpTransferDao;
import com.ruihui.erp.entity.EmpTransfer;

@Controller
@RequestMapping(value = "/transfer")
public class EmpTransferController {
	final private EmpTransferDao empTransferDao;
	@Autowired
	public EmpTransferController(EmpTransferDao dao) {
		empTransferDao=dao;
	}
	@ExceptionHandler(MessageException.class)
	public ModelAndView exceptionHandler(MessageException ex,HttpServletRequest request){
		return Utils.exceptionReturn(ex, request.getRequestURI());
	}
	@RequestMapping(value = "history", method = RequestMethod.POST)
	public TextObject history(@RequestBody byte[] xmlData) throws MessageException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		XMLBody<Object> body=new XMLBody<Object>(xmlData);
		String empId=body.parameter("emp-id");
		List<EmpTransfer> trans;
		if(empId==null){
			throw new MessageException("必须指定职员ID");
		} else {
			trans=empTransferDao.list("where emp_id="+empId+" order by joined_date");
		}
		TextObject result=Utils.successTextobject();
		for(EmpTransfer t:trans){
			result.appendChild(TextObjectUtils.toTextObject(t));
		}
		return result;
	}
}
