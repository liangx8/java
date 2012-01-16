package com.ruihui.common.excel;
import org.apache.poi.ss.usermodel.Row;

public interface RowProcess {
	Object oneRow(Row row);
}
