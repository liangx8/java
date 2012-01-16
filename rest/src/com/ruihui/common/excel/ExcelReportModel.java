package com.ruihui.common.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public abstract class ExcelReportModel {
	
	abstract protected ExcelProcess getExcelProcess();
	/**
	 * Transfer Workbook as InputStream object.
	 * @return
	 */
	public InputStream asInputStream()throws IOException {
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		getExcelProcess().write(out);
		ByteArrayInputStream is=new ByteArrayInputStream(out.toByteArray());
		out.close();
		return is;
	}
	public void write(OutputStream os)throws IOException {
		getExcelProcess().write(os);
	}
}