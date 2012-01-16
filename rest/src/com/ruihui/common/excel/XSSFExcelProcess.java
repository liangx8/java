package com.ruihui.common.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XSSFExcelProcess implements ExcelProcess {
	final XSSFWorkbook wb;
	private XSSFSheet defaultSheet;
	private DataFormat format;
	public XSSFExcelProcess(InputStream is) throws IOException{
		wb = new XSSFWorkbook(is);
		int num=wb.getNumberOfSheets();
		if(num>0)
			defaultSheet=wb.getSheetAt(wb.getActiveSheetIndex());
		else
			defaultSheet=wb.createSheet();
		format=wb.createDataFormat();
	}
	public XSSFExcelProcess() {
		wb=new XSSFWorkbook();
		defaultSheet=wb.createSheet();
	}

	@Override
	public void setCellValue(int row, int col, String text) {
		XSSFRichTextString richText=new XSSFRichTextString(text);
		XSSFCell cell=createCellIfNotExists(defaultSheet,row,col);
		cell.setCellValue(richText);
	}

	private static XSSFCell createCellIfNotExists(XSSFSheet sheet, int rowIndex,int colIndex) {
		XSSFRow row=sheet.getRow(rowIndex);
		if(row==null)row=sheet.createRow(rowIndex);
		XSSFCell cell=row.getCell(colIndex);
		if(cell==null)cell=row.createCell(colIndex);
		return cell;
	}
	@Override
	public void setDefaultSheet(String sheetName) {
		defaultSheet=createSheetIfNotExists(wb,sheetName);
	}

	private static XSSFSheet createSheetIfNotExists(XSSFWorkbook wb, String sheetName) {
		XSSFSheet sheet=wb.getSheet(sheetName);
		if(sheet==null){
			return wb.createSheet(sheetName);
		}
		return sheet;
	}
	@Override
	public void setDefaultSheet(int sheetIndex) {
		defaultSheet=createSheetIfNotExists(wb,sheetIndex);
	}

	private static XSSFSheet createSheetIfNotExists(XSSFWorkbook wb, int sheetIndex) {
		int num=wb.getNumberOfSheets();
		if(num<=sheetIndex)
			return wb.createSheet();
		return wb.getSheetAt(sheetIndex);
	}
	@Override
	public void write(OutputStream out) throws IOException {
		wb.write(out);
	}

	@Override
	public void setCellValue(int row, int col, Date value) {
		XSSFCell cell=createCellIfNotExists(defaultSheet, row, col);
		
		if(value==null){
			cell.setCellValue(new XSSFRichTextString());
		} else {
			cell.setCellValue(value);
//			cell.getCellStyle().setDataFormat(format.getFormat(DATE_FORMAT));
		}

	}

	@Override
	public void changeDefaultSheetName(String sheetName) {
		int index=wb.getSheetIndex(defaultSheet);
		wb.setSheetName(index, sheetName);
	}

	@Override
	public void setCellValue(int row, int col, Integer value) {
		XSSFCell cell=createCellIfNotExists(defaultSheet, row, col);
		if(value==null){
			cell.setCellValue(new XSSFRichTextString());
		} else {
			cell.setCellValue(value);
		}
	}
	@Override
	public Date getDateValue(int row, int col) {
		XSSFCell cell=createCellIfNotExists(defaultSheet, row, col);
		if(cell==null)return null;
		return cell.getDateCellValue();
	}
	@Override
	public double getNumberValue(int row, int col) {
		XSSFCell cell=createCellIfNotExists(defaultSheet, row, col);
		if(cell==null)return 0;
		return cell.getNumericCellValue();
	}
	@Override
	public String getStringValue(int row, int col) {
		XSSFCell cell=createCellIfNotExists(defaultSheet, row, col);
		if(cell==null)return null;
		return cell.getStringCellValue();
	}
	@Override
	public void eachRow(RowProcess rowProcess) {
		Iterator<Row> rowIt = defaultSheet.iterator();
		while (rowIt.hasNext()) {
			rowProcess.oneRow(rowIt.next());
		}
	}
	@Override
	public void shiftRows(int startRow, int endRow, int n) {
		defaultSheet.shiftRows(startRow, endRow, n);
	}
	@Override
	public void setCellStyle(int row, int col, String fmt) {
		Cell cell=locateCell(row,col);
		if(cell==null)return;
		cell.getCellStyle().setDataFormat(format.getFormat(fmt));
	}
	private Cell locateCell(int rowIndex, int colIndex) {
		XSSFRow row=defaultSheet.getRow(rowIndex);
		if(row==null)return null;
		return row.getCell(colIndex);
	}
	@Override
	public void eachCell(CellProcess cellProcess) {
		Iterator<Row> itRow=defaultSheet.iterator();
		while(itRow.hasNext()){
			Row row=itRow.next();
			Iterator<Cell> itCell=row.iterator();
			while(itCell.hasNext()) cellProcess.eachCell(itCell.next());
		}
		
	}

}
