package com.ruihui.common.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public interface ExcelProcess {
	final static String WEB_EXCEL_TYPE_STRING="application/vnd.ms-excel";

//	public void setCellValue(int row,int col, HSSFRichTextString richText);
	public void setCellValue(int row,int col, String text);
	public void setDefaultSheet(String sheetName);
	public void setDefaultSheet(int sheetIndex);
	public void write(OutputStream out)throws IOException;
	public void setCellValue(int row, int col, Date value);
	public void changeDefaultSheetName(String sheetName);
	public void setCellValue(int row, int col, Integer value);
	
	public Date getDateValue(int row,int col);
	public String getStringValue(int row,int col);
	public double getNumberValue(int row,int col);

	public void eachRow(RowProcess rowProcess);
	
	public void eachCell(CellProcess cellProcess);
	public void shiftRows(int startRow,int endRow, int n);
	
	public void setCellStyle(int row,int col,String fmt);
	
	
}