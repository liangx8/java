package com.ruihui.erp.web.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.ruihui.common.excel.ExcelProcess;
import com.ruihui.common.excel.ExcelReportModel;
import com.ruihui.common.excel.HSSFExcelProcess;
import com.ruihui.common.text.TextObject;
import com.ruihui.common.text.TextObjectUtils;
import com.ruihui.common.utils.Converter;
import com.ruihui.common.utils.XMLBody;
import com.ruihui.dao.exception.MessageException;
import com.ruihui.erp.ErpConstant;
import com.ruihui.erp.dao.MeatTicketDao;
import com.ruihui.erp.entity.Employee;
import com.ruihui.erp.entity.MeatTicket;
import com.ruihui.erp.vo.ViewObjectMeatReport;
import com.ruihui.erp.vo.ViewObjectMeatTicket;
import com.ruihui.web.mvc.view.DownloadModel;

@Controller
@RequestMapping(value = "/meat")
public class MeatController {
	final private MeatTicketDao meatTicketDao;
	@Autowired
	public MeatController(MeatTicketDao dao) {
		meatTicketDao=dao;
	}
	@ExceptionHandler(MessageException.class)
	public ModelAndView exceptionHandler(MessageException ex,HttpServletRequest request){
		return Utils.exceptionReturn(ex, request.getRequestURI());
	}
	@RequestMapping(value = "ticket", method = RequestMethod.POST)
	public TextObject ticket(@RequestBody byte[] xmlData)  {
		XMLBody<MeatTicket> body=new XMLBody<MeatTicket>(xmlData,"meatticket",MeatTicket.class);
		MeatTicket mt=body.getObject();
		if(mt.getTicketDate() == null || mt.getAmpm() == null){
			
		}
		String strValue=body.parameter("emp-id");
		final TextObject result=Utils.successTextobject();
		meatTicketDao.printingRecord(strValue,mt,new Converter<MeatTicket, MeatTicket>() {
			@Override
			public MeatTicket covert(MeatTicket input)  {
				try {
					result.appendChild(TextObjectUtils.toTextObject(input));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
		return result;
	}
	@RequestMapping(value = "check_emp", method = RequestMethod.POST)
	public TextObject checkEmpCode(@RequestBody byte[] xmlData) throws MessageException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		TextObject result=Utils.successTextobject();
		XMLBody<Object> t=new XMLBody<Object>(xmlData);
		String empCode = t.parameter("emp_code");
		if(empCode == null){
			throw new MessageException("请输入工号");
		}
		Date issueDate= new Date(Long.parseLong(t.parameter("ticket_date")));
		Employee e=meatTicketDao.availabledEmployee(empCode,issueDate);
		return result.appendChild(TextObjectUtils.toTextObject(e));
	}
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public ModelMap list(@RequestParam("from")String from,@RequestParam("to")String to) throws ParseException{
		SimpleDateFormat df=Utils.dateFormat("yyyyMMdd");
		Date start=df.parse(from);
		Date end=df.parse(to);
		List<ViewObjectMeatTicket> list=meatTicketDao.list("where ticket_date between ? and ?", new Object[]{start,end},ViewObjectMeatTicket.class);
		ModelMap m=new ModelMap();
		m.addAttribute("list", list);
		return m;
	}
	@RequestMapping(value = "input", method = RequestMethod.POST)
	public ModelMap input(@RequestParam(value="ticket_id",required=false)Long[] ticketId){
		ModelMap m=new ModelMap();
		if(ticketId == null){
			m.addAttribute("message", "<h1 style=\"color: red\">没有数据录入</h1>");
		} else {
			meatTicketDao.markUsed(ticketId);
			
			m.addAttribute("message", "<h1>数据更新成功</h1>");
		}
		return m;
	}
	@RequestMapping(value = "statistic", method = RequestMethod.POST)
	public ModelMap statistic(@RequestParam("from")String from,@RequestParam("to")String to) throws ParseException{
		SimpleDateFormat df=Utils.dateFormat("yyyyMMdd");
		Date start=df.parse(from);
		Date end=df.parse(to);
		List<ViewObjectMeatReport> mr=meatTicketDao.meatReport(start, end);
		ModelMap m=new ModelMap();
		m.addAttribute("report", mr);
		m.addAttribute("from", from);
		m.addAttribute("to", to);
		return m;
	}
	@RequestMapping(value = "report", method = RequestMethod.POST)
	public DownloadModel report(@RequestParam("from")String from,@RequestParam("to")String to) throws ParseException, IOException{
		SimpleDateFormat df=Utils.dateFormat("yyyyMMdd");
		Date start=df.parse(from);
		Date end=df.parse(to);
		List<ViewObjectMeatReport> mr=meatTicketDao.meatReport(start, end);
		MeatTicketExcelReport xls=new MeatTicketExcelReport(mr,start,end);
		xls.run();
		DownloadModel model=new DownloadModel(xls.asInputStream());
		model.setContentType(ExcelProcess.WEB_EXCEL_TYPE_STRING);
		model.setFilename("meat_report.xls");
		return model;
	}
}
class MeatTicketExcelReport extends ExcelReportModel{
	final private ExcelProcess xls;
	final private List<ViewObjectMeatReport> data;
	final private Date from,to;
	public MeatTicketExcelReport(List<ViewObjectMeatReport> mr,Date start,Date end) {
		xls=new HSSFExcelProcess();
		data=mr;
		from=start;
		to=end;
	}
	@Override
	protected ExcelProcess getExcelProcess() {
		return xls;
	}
	public void run(){
		xls.setCellValue(0, 0, "餐票统计");
		xls.setCellValue(1, 0, from);
		xls.setCellValue(1, 1, to);
		xls.setCellValue(2, 0, "工号");
		xls.setCellValue(2, 1, "姓名");
		xls.setCellValue(2, 2, "餐次");
		xls.setCellValue(2, 3, "就餐历史");
		
		for(int i=0;i<data.size();i++){
			xls.setCellValue(i+3, 0, data.get(i).getEmpCode());
			xls.setCellValue(i+3, 1, data.get(i).getEmpName());
			xls.setCellValue(i+3, 2, data.get(i).getCount());
			List<MeatTicket> mt=data.get(i).getHistory();
			for(int j=0;j<mt.size();j++){
				xls.setCellValue(i+3, j*2+3, mt.get(j).getTicketDate());
				if(mt.get(j).getAmpm().intValue()==ErpConstant.MEAT_LUNCH)
					xls.setCellValue(i+3, j*2+4, "AM");
				if(mt.get(j).getAmpm().intValue()==ErpConstant.MEAT_SUPPER)
					xls.setCellValue(i+3, j*2+4, "PM");
			}
		}
	}
}