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
import com.ruihui.dao.utils.Order;
import com.ruihui.erp.dao.CodeMasterDao;
import com.ruihui.erp.entity.CodeMaster;

@Controller
@RequestMapping(value = "/code")
public class CodeMasterController {
	final private CodeMasterDao codeMasterDao;
	@Autowired
	public CodeMasterController(CodeMasterDao dao) {
		codeMasterDao=dao;
	}
	@ExceptionHandler(MessageException.class)
	public ModelAndView exceptionHandler(MessageException ex,HttpServletRequest request){
		return Utils.exceptionReturn(ex, request.getRequestURI());
	}
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public TextObject list(@RequestBody byte[] xmlData) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, MessageException{
		XMLBody<Object> body=new XMLBody<Object>(xmlData);
		String category=body.parameter("category");
		List<CodeMaster> cms;
		if(category==null){
			cms=codeMasterDao.all(new Order("asc","category"));
		} else {
			cms=codeMasterDao.list("where category = '"+category+"'");
		}

		TextObject result=Utils.successTextobject();
		for(CodeMaster cm:cms){
			result.appendChild(TextObjectUtils.toTextObject(cm));
		}
		return result;
	}
}
