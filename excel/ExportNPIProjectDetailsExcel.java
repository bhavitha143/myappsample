package com.bh.realtrack.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.bh.realtrack.dto.NPIProjectDetailsDTO;



public class ExportNPIProjectDetailsExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (26 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;
	

	public SXSSFWorkbook downloadNPIDetails(SXSSFWorkbook workbook, List<NPIProjectDetailsDTO> excelDetails,
			String sheetname) {
		Sheet sheet = workbook.createSheet(sheetname);
		sheet.createFreezePane(0, 1);
		CellStyle headStyle = ExcelStylesLib.getCellHeadStyleForSXSSF(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(20);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("JOB"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DUMMY CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACT TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DUE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FORECAST DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OWNERSHIP"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY GROUP"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = ExcelStylesLib.getCellBodyStyleForSXSSF(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < excelDetails.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;
			cell = row.createCell(dataColumnCount);
			cell = row.createCell(0);
			cell.setCellValue(excelDetails.get(i).getProjectID());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getJob());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDummyCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getActivityName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getActType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDueDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getActualDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getForecastDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOwnership());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getActivityGroup());
			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
		return workbook;

	}

}
