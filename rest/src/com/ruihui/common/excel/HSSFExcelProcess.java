package com.ruihui.common.excel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class HSSFExcelProcess implements ExcelProcess {
	final public static String DATE_FORMAT = "dd/MM/yyyy";
	final private HSSFWorkbook wb;
	private HSSFSheet defaultSheet;
	private HSSFDataFormat format;

	public HSSFExcelProcess(InputStream is) throws IOException {
		wb = new HSSFWorkbook(is);
		int num = wb.getNumberOfSheets();
		if (num > 0)
			defaultSheet = wb.getSheetAt(0);
		else
			defaultSheet = wb.createSheet();
	}

	public HSSFExcelProcess() {
		wb = new HSSFWorkbook();
		defaultSheet = createSheetIfNotExists(wb, 0);
		format = wb.createDataFormat();
	}

	// @Override
	// public void setCellValue(int row, int col, HSSFRichTextString richText) {
	// HSSFCell cell=createCellIfNotExists(defaultSheet,row,col);
	// cell.setCellValue(richText);
	// }
	@Override
	public void setDefaultSheet(String sheetName) {
		defaultSheet = createSheetIfNotExists(wb, sheetName);
	}

	@Override
	public void setDefaultSheet(int sheetIndex) {
		defaultSheet = createSheetIfNotExists(wb, sheetIndex);

	}

	@Override
	public void write(OutputStream out) throws IOException {
		wb.write(out);
	}

	@Override
	public void setCellValue(int row, int col, Date value) {
		HSSFCell cell = createCellIfNotExists(defaultSheet, row, col);
		if (value == null) {
			cell.setCellValue(new HSSFRichTextString());
		} else {
			cell.setCellValue(value);
			cell.getCellStyle().setDataFormat(format.getFormat(DATE_FORMAT));
		}
	}

	private static HSSFCell createCellIfNotExists(HSSFSheet sheet,
			int rowIndex, int colIndex) {
		HSSFRow row = sheet.getRow(rowIndex);
		if (row == null)
			row = sheet.createRow(rowIndex);
		HSSFCell cell = row.getCell(colIndex);
		if (cell == null)
			cell = row.createCell(colIndex);
		return cell;
	}

	private static HSSFSheet createSheetIfNotExists(HSSFWorkbook wb,
			int sheetIndex) {
		int num = wb.getNumberOfSheets();
		if (num <= sheetIndex)
			return wb.createSheet();
		return wb.getSheetAt(sheetIndex);
	}

	private static HSSFSheet createSheetIfNotExists(HSSFWorkbook wb,
			String sheetName) {
		HSSFSheet sheet = wb.getSheet(sheetName);
		if (sheet == null)
			return wb.createSheet(sheetName);
		return sheet;
	}

	@Override
	public void changeDefaultSheetName(String sheetName) {
		int index = wb.getSheetIndex(defaultSheet);
		wb.setSheetName(index, sheetName);
	}

	@Override
	public void setCellValue(int row, int col, String text) {
		HSSFRichTextString richText = new HSSFRichTextString(text);
		HSSFCell cell = createCellIfNotExists(defaultSheet, row, col);
		cell.setCellValue(richText);
	}

	@Override
	public void setCellValue(int row, int col, Integer value) {
		HSSFCell cell = createCellIfNotExists(defaultSheet, row, col);
		if (value == null) {
			cell.setCellValue(new HSSFRichTextString());
		} else {
			cell.setCellValue(value);
		}
	}

	@Override
	public Date getDateValue(int row, int col) {
		HSSFCell cell = createCellIfNotExists(defaultSheet, row, col);
		if (cell == null)
			return null;
		double excelDate = cell.getNumericCellValue();
		if (HSSFDateUtil.isValidExcelDate(excelDate)) {
			Date javaDate = HSSFDateUtil.getJavaDate(excelDate);
			return javaDate;
		} else {
			throw new RuntimeException("cell on (" + row + "," + col
					+ ") is not date format");
		}
	}

	@Override
	public double getNumberValue(int row, int col) {
		HSSFCell cell = createCellIfNotExists(defaultSheet, row, col);
		if (cell == null)
			return .0;
		return cell.getNumericCellValue();
	}

	@Override
	public String getStringValue(int row, int col) {
		HSSFCell cell = createCellIfNotExists(defaultSheet, row, col);
		if (cell == null)
			return null;
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

	private Cell locateCell(int rowIndex, int col) {
		HSSFRow r=defaultSheet.getRow(rowIndex);
		if(r==null)return null;
		return r.getCell(col);
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
