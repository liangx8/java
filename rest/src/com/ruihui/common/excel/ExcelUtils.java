package com.ruihui.common.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class ExcelUtils {
	public static final Object getObjectValue(Cell c){
		switch (c.getCellType()) {
		case Cell.CELL_TYPE_FORMULA:
		case Cell.CELL_TYPE_STRING:
			return c.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			if(DateUtil.isCellInternalDateFormatted(c))
				return c.getDateCellValue();
			else
				return c.getNumericCellValue();
		case Cell.CELL_TYPE_BOOLEAN:
			return c.getBooleanCellValue();
		case Cell.CELL_TYPE_BLANK:
			return "";
		}
		throw new RuntimeException("Unknow type in ("+c.getRowIndex()+","+c.getColumnIndex()+")");
	}
}
