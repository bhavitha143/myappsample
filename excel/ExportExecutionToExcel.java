/**
 * Shweta Sawant
 */
package com.bh.realtrack.excel;

import java.util.List;

import com.bh.realtrack.dto.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class ExportExecutionToExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (22 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	public SXSSFWorkbook exportExecutionExcel(final SXSSFWorkbook workbook,
			final List<ExecutionDTO> executionDTOList) {

		SXSSFSheet sheet = workbook.createSheet("Execution Details");
		workbook.setCompressTempFiles(true);
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);

		cell = row.createCell(0);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(1);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);
			
		cell = row.createCell(2);
		cell.setCellValue(("SUB PROJECT NAME"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(3);
		cell.setCellValue(("ACTIVITY ID"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(4);
		cell.setCellValue(("ACTIVITY NAME"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(5);
		cell.setCellValue(("PRODUCT LINE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		cell = row.createCell(6);
		cell.setCellValue(("DEPARTMENT"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(7);
		cell.setCellValue(("WEIGHT"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(8);
		cell.setCellValue(("FLOAT"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(9);
		cell.setCellValue(("TOTAL FLOAT DAYS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(10);
		cell.setCellValue(("WEIGHT %"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(11);
		cell.setCellValue(("BASELINE START DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(12);
		cell.setCellValue(("FORECAST START DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(13);
		cell.setCellValue(("ACTUAL START DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(14);
		cell.setCellValue(("DAYS LATE START"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(15);
		cell.setCellValue(("OTD START"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(16);
		cell.setCellValue(("TIMING START"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(17);
		cell.setCellValue(("WBS CBS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(18);
		cell.setCellValue(("ACTIVITY TYPE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(19);
		cell.setCellValue(("ENG LOB CODES"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(20);
		cell.setCellValue(("BASELINE FINISH DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(21);
		cell.setCellValue(("FORECAST FINISH DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(22);
		cell.setCellValue(("ACTUAL FINISH DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(23);
		cell.setCellValue(("DAYS LATE FINISH"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(24);
		cell.setCellValue(("OTD FINISH"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(25);
		cell.setCellValue(("TIMING FINISH"));
		cell.setCellStyle(headStyle);
	
		cell = row.createCell(26);
		cell.setCellValue(("MILESTONE TYPE"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(27);
		cell.setCellValue(("FUNCTION GROUP"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(28);
		cell.setCellValue(("DATA DATE"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}
		for (int i = 0; i < executionDTOList.size(); i++) {

			row = sheet.createRow(rowNum++);

			cell = row.createCell(0);
			cell.setCellValue(executionDTOList.get(i).getMasterProjectName() == null ? ""
					: executionDTOList.get(i).getMasterProjectName());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(1);
			cell.setCellValue(executionDTOList.get(i).getProjectId() == null ? ""
					: executionDTOList.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);
	
			cell = row.createCell(2);
			cell.setCellValue(executionDTOList.get(i).getSubProjectName() == null ? ""
					: executionDTOList.get(i).getSubProjectName());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(3);
			cell.setCellValue(executionDTOList.get(i).getActivityId() == null ? ""
					: executionDTOList.get(i).getActivityId());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(4);
			cell.setCellValue(executionDTOList.get(i).getActivityName() == null ? ""
					: executionDTOList.get(i).getActivityName());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(5);
			cell.setCellValue(executionDTOList.get(i).getProductLine() == null ? ""
					: executionDTOList.get(i).getProductLine());
			cell.setCellStyle(bodyStyle);

			
			cell = row.createCell(6);
			cell.setCellValue(executionDTOList.get(i).getDepartment() == null ? ""
					: executionDTOList.get(i).getDepartment());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(7);
			cell.setCellValue(executionDTOList.get(i).getWeight() == null ? ""
					: executionDTOList.get(i).getWeight());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(8);
			cell.setCellValue(executionDTOList.get(i).getFloatType() == null ? ""
					: executionDTOList.get(i).getFloatType());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(9);
			cell.setCellValue(Double.toString(executionDTOList.get(i)
					.getTotalFloatDays()) == null ? "0" : Double
					.toString(executionDTOList.get(i).getTotalFloatDays()));
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(10);
			cell.setCellValue(executionDTOList.get(i).getWeightpercentage() == null ? ""
					: executionDTOList.get(i).getWeightpercentage());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(11);
			cell.setCellValue(executionDTOList.get(i).getBaselineStartDate() == null ? ""
					: executionDTOList.get(i).getBaselineStartDate());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(12);
			cell.setCellValue(executionDTOList.get(i).getForecastStartDate() == null ? ""
					: executionDTOList.get(i).getForecastStartDate());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(13);
			cell.setCellValue(executionDTOList.get(i).getActualStartDate() == null ? ""
					: executionDTOList.get(i).getActualStartDate());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(14);
			cell.setCellValue(Double.toString(executionDTOList.get(i)
					.getDaysLateStart()) == null ? "0" : Double
					.toString(executionDTOList.get(i).getDaysLateStart()));
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(15);
			cell.setCellValue(executionDTOList.get(i).getOtdStart() == null ? ""
					: executionDTOList.get(i).getOtdStart());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(16);
			cell.setCellValue(executionDTOList.get(i).getTimingStart() == null ? ""
					: executionDTOList.get(i).getTimingStart());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(17);
			cell.setCellValue(executionDTOList.get(i).getWbsCbs() == null ? ""
					: executionDTOList.get(i).getWbsCbs());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(18);
			cell.setCellValue(executionDTOList.get(i).getActivityType() == null ? ""
					: executionDTOList.get(i).getActivityType());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(19);
			cell.setCellValue(executionDTOList.get(i).getEngLobCodes() == null ? ""
					: executionDTOList.get(i).getEngLobCodes());
			cell.setCellStyle(bodyStyle);
			
	        cell = row.createCell(20);
			cell.setCellValue(executionDTOList.get(i).getBaselineFinishDate() == null ? ""
					: executionDTOList.get(i).getBaselineFinishDate());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(21);
			cell.setCellValue(executionDTOList.get(i).getForecastFinishDate() == null ? ""
					: executionDTOList.get(i).getForecastFinishDate());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(22);
			cell.setCellValue(executionDTOList.get(i).getActualFinishDate() == null ? ""
					: executionDTOList.get(i).getActualFinishDate());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(23);
			cell.setCellValue(Double.toString(executionDTOList.get(i)
					.getDaysLateFinish()) == null ? "0" : Double
					.toString(executionDTOList.get(i).getDaysLateFinish()));
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(24);
			cell.setCellValue(executionDTOList.get(i).getOtdFinish() == null ? ""
					: executionDTOList.get(i).getOtdFinish());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(25);
			cell.setCellValue(executionDTOList.get(i).getTimingFinish() == null ? ""
					: executionDTOList.get(i).getTimingFinish());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(26);
			cell.setCellValue(executionDTOList.get(i).getMilestoneType() == null ? ""
					: executionDTOList.get(i).getMilestoneType());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(27);
			cell.setCellValue(executionDTOList.get(i).getFunctionGroup() == null ? ""
					: executionDTOList.get(i).getFunctionGroup());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(28);
			cell.setCellValue(executionDTOList.get(i).getDataDate() == null ? ""
					: executionDTOList.get(i).getDataDate());
			cell.setCellStyle(bodyStyle);


			sheet.setColumnWidth(3, lWidth);// activityName
			sheet.setColumnWidth(7, sWidth);// weight %
			sheet.setColumnWidth(20, mWidth);// wbsCbs
			sheet.setColumnWidth(23, mWidth);// activityType
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

		return workbook;

	}

	public SXSSFWorkbook exportExecutionStartPopupExcel(SXSSFWorkbook workbook, List<ExecutionStartDTO> executionStartDTOList, String sheetName){
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.createFreezePane(0, 1);
		CellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUB PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PRODUCT LINE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DEPARTMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WEIGHT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FLOAT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TOTAL FLOAT DAYS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WEIGHT PERCENTAGE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BASELINE START DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FORECAST START DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL START DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DAYS LATE START"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OTD START"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TIMING START"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WBS CBS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ENG LOB CODES"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DATA DATE"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for(int i = 0; i<executionStartDTOList.size();i++){
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getMasterProjectName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getSubProjectName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getActivityId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getActivityName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getProductLine());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getDepartment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getWeight());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getFloatType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getTotalFloatDays());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getWeightpercentage());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getBaselineStartDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getForecastStartDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getActualStartDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getDaysLateStart());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getOtdStart());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getTimingStart());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getWbsCbs());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getActivityType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getEngLobCodes());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionStartDTOList.get(i).getDataDate());
			cell.setCellStyle(bodyStyle);

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
		return workbook;
	}

	public SXSSFWorkbook exportExecutionFinishPopupExcel(SXSSFWorkbook workbook, List<ExecutionFinishDTO> executionFinishDTOList, String sheetName){
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.createFreezePane(0, 1);
		CellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUB PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PRODUCT LINE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DEPARTMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WEIGHT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FLOAT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TOTAL FLOAT DAYS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WEIGHT PERCENTAGE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BASELINE FINISH DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FORECAST FINISH DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL FINISH DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DAYS LATE FINISH"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OTD FINISH"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TIMING FINISH"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WBS CBS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ENG LOB CODES"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DATA DATE"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for(int i = 0; i<executionFinishDTOList.size();i++){
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getMasterProjectName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getSubProjectName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getActivityId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getActivityName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getProductLine());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getDepartment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getWeight());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getFloatType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getTotalFloatDays());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getWeightpercentage());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getBaselineFinishDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getForecastFinishDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getActualFinishDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getDaysLateFinish());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getOtdFinish());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getTimingFinish());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getWbsCbs());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getActivityType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getEngLobCodes());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionFinishDTOList.get(i).getDataDate());
			cell.setCellStyle(bodyStyle);

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
		return workbook;
	}



	private CellStyle getCellHeadStyle(final SXSSFWorkbook workbook) {
		CellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private void setBorderStyle(final CellStyle headStyle) {
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		headStyle.setBorderTop(BorderStyle.THIN);

	}

	private Font getFontHeader(final SXSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.WHITE.getIndex());
		return font;
	}

	private CellStyle getCellBodyStyle(final SXSSFWorkbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}

	private Font getFontContent(final SXSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	public SXSSFWorkbook exportExecutionNCExcel(final SXSSFWorkbook workbook,
			final List<ExecutionNCDTO> executionNCDTOList, String organizationName) {

		SXSSFSheet sheet = workbook.createSheet("Non - Conformances");
		sheet.createFreezePane(0, 1);
		workbook.setCompressTempFiles(true);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);

		if(organizationName==null){
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("Project"));
			cell.setCellStyle(getCellHeadStyle(workbook));
		}
		else {
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("Sub Project"));
			cell.setCellStyle(getCellHeadStyle(workbook));
		}
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Organization Name"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Criticality"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Origin"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Location"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Sub Location"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("NCR Number"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Creation Date"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("NCR Type"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Last Revision"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Status"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Closure Date"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Aging/Cycle Time"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Customer Feedback"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Part Number"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Pei Code"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Om Description"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Discrepancy"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Defect"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Disposition"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Special Job Id"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Special Job Date"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SRC Refresh Date"));
		cell.setCellStyle(headStyle);
		
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);

		}

		for (int i = 0; i < executionNCDTOList.size(); i++) {

			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			if(organizationName==null){
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(
						executionNCDTOList.get(i).getProject() == null ? "" : executionNCDTOList.get(i).getProject());
				cell.setCellStyle(bodyStyle);
			}
			else {
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(
						executionNCDTOList.get(i).getSubProject() == null ? "" : executionNCDTOList.get(i).getSubProject());
				cell.setCellStyle(bodyStyle);
			}

			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(
					executionNCDTOList.get(i).getOrganizationName() == null ? "" : executionNCDTOList.get(i).getOrganizationName());
			cell.setCellStyle(bodyStyle);


			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getCriticality() == null ? ""
					: executionNCDTOList.get(i).getCriticality());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(
					executionNCDTOList.get(i).getOrigin() == null ? "" : executionNCDTOList.get(i).getOrigin());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getLocation() == null ? ""
					: executionNCDTOList.get(i).getLocation());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getSubLocation() == null ? ""
					: executionNCDTOList.get(i).getSubLocation());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(
					executionNCDTOList.get(i).getNcrNumber() == null ? "" : executionNCDTOList.get(i).getNcrNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getCreationDate() == null ? ""
					: executionNCDTOList.get(i).getCreationDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(
					executionNCDTOList.get(i).getnCRType() == null ? "" : executionNCDTOList.get(i).getnCRType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getLastRevision() == null ? ""
					: executionNCDTOList.get(i).getLastRevision());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(
					executionNCDTOList.get(i).getStatus() == null ? "" : executionNCDTOList.get(i).getStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getClosureDate() == null ? ""
					: executionNCDTOList.get(i).getClosureDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getAging() == null ? "" : executionNCDTOList.get(i).getAging());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getCustomerFeedback() == null ? ""
					: executionNCDTOList.get(i).getCustomerFeedback());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(
					executionNCDTOList.get(i).getPartNumber() == null ? "" : executionNCDTOList.get(i).getPartNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(
					executionNCDTOList.get(i).getPeiCode() == null ? "" : executionNCDTOList.get(i).getPeiCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getOmDescription() == null ? ""
					: executionNCDTOList.get(i).getOmDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getDiscrepancy() == null ? ""
					: executionNCDTOList.get(i).getDiscrepancy());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(
					executionNCDTOList.get(i).getDefect() == null ? "" : executionNCDTOList.get(i).getDefect());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getDisposition() == null ? ""
					: executionNCDTOList.get(i).getDisposition());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getSpecialJobId() == null ? ""
					: executionNCDTOList.get(i).getSpecialJobId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getSpecialJobDate() == null ? ""
					: executionNCDTOList.get(i).getSpecialJobDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(executionNCDTOList.get(i).getSrcRefreshDate() == null ? ""
					: executionNCDTOList.get(i).getSrcRefreshDate());
			cell.setCellStyle(bodyStyle);
			
			

			sheet.setColumnWidth(15, lWidth);
			sheet.setColumnWidth(16, lWidth);
			sheet.setColumnWidth(17, lWidth);
			sheet.setColumnWidth(18, lWidth);
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

		return workbook;

	}

	public SXSSFWorkbook exportVarOrderExcel(final SXSSFWorkbook workbook,
			final List<VarOrderReportDTO> varOrderExcelDataList) {

		SXSSFSheet sheet = workbook.createSheet("Variation Orders");
		workbook.setCompressTempFiles(true);
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);

		cell = row.createCell(0);
		cell.setCellValue(("RECORD NUMBER"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(1);
		cell.setCellValue(("TITLE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(2);
		cell.setCellValue(("DESCRIPTION OF CHANGE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(3);
		cell.setCellValue(("DATE SENT TO CUSTOMER"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(4);
		cell.setCellValue(("AMOUNT"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(5);
		cell.setCellValue(("CURRENCY"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(6);
		cell.setCellValue(("AMOUNT USD"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(7);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);
		
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}
		for (int i = 0; i < varOrderExcelDataList.size(); i++) {

			row = sheet.createRow(rowNum++);

			cell = row.createCell(0);
			cell.setCellValue(varOrderExcelDataList.get(i).getRecordNumber() == null ? ""
					: varOrderExcelDataList.get(i).getRecordNumber());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(1);
			cell.setCellValue(varOrderExcelDataList.get(i).getTitle() == null ? ""
					: varOrderExcelDataList.get(i).getTitle());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(2);
			cell.setCellValue(varOrderExcelDataList.get(i)
					.getChangeDescription() == null ? ""
					: varOrderExcelDataList.get(i).getChangeDescription());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(3);
			cell.setCellValue(varOrderExcelDataList.get(i).getCustomerWantDt() == null ? ""
					: varOrderExcelDataList.get(i).getCustomerWantDt());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(4);
			cell.setCellValue(Double.toString(varOrderExcelDataList.get(i)
					.getVorOrigAmount()) == null ? "0" : Double
					.toString(varOrderExcelDataList.get(i).getVorOrigAmount()));
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(5);
			cell.setCellValue(varOrderExcelDataList.get(i).getCurrency() == null ? ""
					: varOrderExcelDataList.get(i).getCurrency());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(6);
			cell.setCellValue(Double.toString(varOrderExcelDataList.get(i)
					.getVorAmount()) == null ? "0" : Double
					.toString(varOrderExcelDataList.get(i).getVorAmount()));
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(7);
			cell.setCellValue(varOrderExcelDataList.get(i).getStatus() == null ? ""
					: varOrderExcelDataList.get(i).getStatus());
			cell.setCellStyle(bodyStyle);

			sheet.setColumnWidth(5, mWidth);
			sheet.setColumnWidth(11, lWidth);
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("GE Confidential");

		return workbook;
	}

	public SXSSFWorkbook exportVarOrderExcel1(SXSSFWorkbook workbook, List<VarOrderReport1DTO> varOrderExcelDataList1, String role) {

	SXSSFSheet sheet = workbook.createSheet("Variation Orders");
	workbook.setCompressTempFiles(true);
	sheet.createFreezePane(0, 1);
	int rowNum = 1;
	Row row = null;
	Cell cell = null;

	row = sheet.createRow(0);
	row.setHeightInPoints(60);
	CellStyle headStyle = getCellHeadStyle(workbook);

	cell = row.createCell(0);
	cell.setCellValue(("SUBPL"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(1);
	cell.setCellValue(("CUSTOMER"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(2);
	cell.setCellValue(("REGION"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(3);
	cell.setCellValue(("MASTER PROJECT NAME"));
	cell.setCellStyle(headStyle);
	
	cell = row.createCell(4);
	cell.setCellValue(("PROJECT NUMBER"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(5);
	cell.setCellValue(("PROJECT NAME"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(6);
	cell.setCellValue(("RECORD NUMBER"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(7);
	cell.setCellValue(("CHANGE CUSTOMER REF NO"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(8);
	cell.setCellValue(("TITLE"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(9);
	cell.setCellValue(("DESCRIPTION OF CHANGE"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(10);
	cell.setCellValue(("PHASE"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(11);
	cell.setCellValue(("CUST REVIEW DATE"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(12);
	cell.setCellValue(("CUST APPROVAL BY DATE"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(13);
	cell.setCellValue(("VALIDITY"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(14);
	cell.setCellValue(("DAYS TO EXPIRY"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(15);
	cell.setCellValue(("INITIAL COST USD"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(16);
	cell.setCellValue(("INITIAL PRICE"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(17);
	cell.setCellValue(("INITIAL PRICE USD"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(18);
	cell.setCellValue(("FINAL COST USD"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(19);
	cell.setCellValue(("FINAL PRICE"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(20);
	cell.setCellValue(("FINAL PRICE USD"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(21);
	cell.setCellValue(("FINAL CM"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(22);
	cell.setCellValue(("AMOUNT"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(23);
	cell.setCellValue(("CURRENCY"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(24);
	cell.setCellValue(("AMOUNT USD"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(25);
	cell.setCellValue(("PROB X AMOUNT USD"));
	cell.setCellStyle(headStyle);

	cell = row.createCell(26);
	cell.setCellValue(("CHANGE YEAR"));
	cell.setCellStyle(headStyle);
	
	if(role.equalsIgnoreCase("Internal")) {
		
		cell = row.createCell(27);
		cell.setCellValue(("CHANGE BASIS FOR CO"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(28);
		cell.setCellValue(("CHANGE TYPE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(29);
		cell.setCellValue(("CHANGE ORIGIN"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(30);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(31);
		cell.setCellValue(("CUSTOMER VIEW"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(32);
		cell.setCellValue(("INITIAL COST"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(33);
		cell.setCellValue(("INITIAL CM"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(34);
		cell.setCellValue(("FINAL COST"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(35);
		cell.setCellValue(("CHANGE YEAR QTR"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(36);
		cell.setCellValue(("CHANGE LIKELIHOOD"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(37);
		cell.setCellValue(("DATE CHANGE IDENTIFIED"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(38);
		cell.setCellValue(("CREATOR"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(39);
		cell.setCellValue(("CREATION DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(40);
		cell.setCellValue(("QUALITY DOCUMENTATION IMPACT"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(41);
		cell.setCellValue(("FDS TCP IMPACT"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(42);
		cell.setCellValue(("CONTRACT DELIVERY IMPACT"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(43);
		cell.setCellValue(("INTERNAL SCHEDULE IMPACT"));
		cell.setCellStyle(headStyle);

	}

	CellStyle bodyStyle = getCellBodyStyle(workbook);

	for (int i = 0; i <= cell.getColumnIndex(); i++) {
		sheet.setColumnWidth(i, xsWidth);
	}
	for (int i = 0; i < varOrderExcelDataList1.size(); i++) {

		row = sheet.createRow(rowNum++);

		cell = row.createCell(0);
		cell.setCellValue(varOrderExcelDataList1.get(i).getSubPl() == null ? ""
				: varOrderExcelDataList1.get(i).getSubPl());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(1);
		cell.setCellValue(varOrderExcelDataList1.get(i).getCustomer() == null ? ""
				: varOrderExcelDataList1.get(i).getCustomer());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(2);
		cell.setCellValue(varOrderExcelDataList1.get(i).getRegion() == null ? ""
				: varOrderExcelDataList1.get(i).getRegion());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(3);
		cell.setCellValue(varOrderExcelDataList1.get(i).getProjectName() == null ? ""
				: varOrderExcelDataList1.get(i).getProjectName());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(4);
		cell.setCellValue(varOrderExcelDataList1.get(i).getProjectNumber() == null ? ""
				: varOrderExcelDataList1.get(i).getProjectNumber());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(5);
		cell.setCellValue(varOrderExcelDataList1.get(i).getProject_name() == null ? ""
				: varOrderExcelDataList1.get(i).getProject_name());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(6);
		cell.setCellValue(varOrderExcelDataList1.get(i).getRecordNumber() == null ? ""
				: varOrderExcelDataList1.get(i).getRecordNumber());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(7);
		cell.setCellValue(varOrderExcelDataList1.get(i).getVorCustomerRefNo() == null ? ""
				: varOrderExcelDataList1.get(i).getVorCustomerRefNo());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(8);
		cell.setCellValue(varOrderExcelDataList1.get(i).getTitle() == null ? ""
				: varOrderExcelDataList1.get(i).getTitle());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(9);
		cell.setCellValue(varOrderExcelDataList1.get(i).getChangeDescription() == null ? ""
				: varOrderExcelDataList1.get(i).getChangeDescription());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(10);
		cell.setCellValue(varOrderExcelDataList1.get(i).getVorPhase() == null ? ""
				: varOrderExcelDataList1.get(i).getVorPhase());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(11);
		cell.setCellValue(varOrderExcelDataList1.get(i).getCustReviewDate() == null ? ""
				: varOrderExcelDataList1.get(i).getCustReviewDate());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(12);
		cell.setCellValue(varOrderExcelDataList1.get(i).getCustApprovalByDate() == null ? ""
				: varOrderExcelDataList1.get(i).getCustApprovalByDate());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(13);
		cell.setCellValue(varOrderExcelDataList1.get(i).getValidity() == null ? ""
				: varOrderExcelDataList1.get(i).getValidity());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(14);
		cell.setCellValue(varOrderExcelDataList1.get(i).getDaysToExpiry() == null ? ""
				: varOrderExcelDataList1.get(i).getDaysToExpiry());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(15);
		cell.setCellValue(varOrderExcelDataList1.get(i).getInitialCostUsd() == null ? ""
				: varOrderExcelDataList1.get(i).getInitialCostUsd());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(16);
		cell.setCellValue(varOrderExcelDataList1.get(i).getInitialPrice() == null ? ""
				: varOrderExcelDataList1.get(i).getInitialPrice());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(17);
		cell.setCellValue(varOrderExcelDataList1.get(i).getInitialPriceUsd() == null ? ""
				: varOrderExcelDataList1.get(i).getInitialPriceUsd());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(18);
		cell.setCellValue(varOrderExcelDataList1.get(i).getFinalCostUsd() == null ? ""
				: varOrderExcelDataList1.get(i).getFinalCostUsd());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(19);
		cell.setCellValue(varOrderExcelDataList1.get(i).getFinalPrice() == null ? ""
				: varOrderExcelDataList1.get(i).getFinalPrice());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(20);
		cell.setCellValue(varOrderExcelDataList1.get(i).getFinalPriceUsd() == null ? ""
				: varOrderExcelDataList1.get(i).getFinalPriceUsd());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(21);
		cell.setCellValue(varOrderExcelDataList1.get(i).getFinalCm() == null ? ""
				: varOrderExcelDataList1.get(i).getFinalCm());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(22);
		cell.setCellValue(varOrderExcelDataList1.get(i).getVorOrigAmount() == null ? ""
				: varOrderExcelDataList1.get(i).getVorOrigAmount());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(23);
		cell.setCellValue(varOrderExcelDataList1.get(i).getCurrency() == null ? ""
				: varOrderExcelDataList1.get(i).getCurrency());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(24);
		cell.setCellValue(varOrderExcelDataList1.get(i).getVorAmount() == null ? ""
				: varOrderExcelDataList1.get(i).getVorAmount());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(25);
		cell.setCellValue(varOrderExcelDataList1.get(i).getProbAmountUsd() == null ? ""
				: varOrderExcelDataList1.get(i).getProbAmountUsd());
		cell.setCellStyle(bodyStyle);

		cell = row.createCell(26);
		cell.setCellValue(varOrderExcelDataList1.get(i).getVorYear() == null ? ""
				: varOrderExcelDataList1.get(i).getVorYear());
		cell.setCellStyle(bodyStyle);

				if(role.equalsIgnoreCase("Internal")) {
			
			cell = row.createCell(27);
			cell.setCellValue(varOrderExcelDataList1.get(i).getVorBasisForCo() == null ? ""
					: varOrderExcelDataList1.get(i).getVorBasisForCo());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(28);
			cell.setCellValue(varOrderExcelDataList1.get(i).getVorType() == null ? ""
					: varOrderExcelDataList1.get(i).getVorType());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(29);
			cell.setCellValue(varOrderExcelDataList1.get(i).getVorOrigin() == null ? ""
					: varOrderExcelDataList1.get(i).getVorOrigin());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(30);
			cell.setCellValue(varOrderExcelDataList1.get(i).getStatus() == null ? ""
					: varOrderExcelDataList1.get(i).getStatus());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(31);
			cell.setCellValue(varOrderExcelDataList1.get(i).getCustomerView() == null ? ""
					: varOrderExcelDataList1.get(i).getCustomerView());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(32);
			cell.setCellValue(varOrderExcelDataList1.get(i).getInitialCost() == null ? ""
					: varOrderExcelDataList1.get(i).getInitialCost());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(33);
			cell.setCellValue(varOrderExcelDataList1.get(i).getInitialCm() == null ? ""
					: varOrderExcelDataList1.get(i).getInitialCm());
			cell.setCellStyle(bodyStyle);
			
			cell = row.createCell(34);
			cell.setCellValue(varOrderExcelDataList1.get(i).getFinalCost() == null ? ""
					: varOrderExcelDataList1.get(i).getFinalCost());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(35);
			cell.setCellValue(varOrderExcelDataList1.get(i).getVorYearQtr() == null ? ""
					: varOrderExcelDataList1.get(i).getVorYearQtr());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(36);
			cell.setCellValue(varOrderExcelDataList1.get(i).getVorLikelihood() == null ? ""
					: varOrderExcelDataList1.get(i).getVorLikelihood());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(37);
			cell.setCellValue(varOrderExcelDataList1.get(i).getDateChangeIdentified() == null ? ""
					: varOrderExcelDataList1.get(i).getDateChangeIdentified());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(38);
			cell.setCellValue(varOrderExcelDataList1.get(i).getCreator() == null ? ""
					: varOrderExcelDataList1.get(i).getCreator());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(39);
			cell.setCellValue(varOrderExcelDataList1.get(i).getCreationDate() == null ? ""
					: varOrderExcelDataList1.get(i).getCreationDate());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(40);
			cell.setCellValue(varOrderExcelDataList1.get(i).getQualityDocumentationImpact() == null ? ""
					: varOrderExcelDataList1.get(i).getQualityDocumentationImpact());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(41);
			cell.setCellValue(varOrderExcelDataList1.get(i).getFdsTcpImpact() == null ? ""
					: varOrderExcelDataList1.get(i).getFdsTcpImpact());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(42);
			cell.setCellValue(varOrderExcelDataList1.get(i).getContractDeliveryImpact() == null ? ""
					: varOrderExcelDataList1.get(i).getContractDeliveryImpact());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(43);
			cell.setCellValue(varOrderExcelDataList1.get(i).getInternalScheduleImpact() == null ? ""
					: varOrderExcelDataList1.get(i).getInternalScheduleImpact());
			cell.setCellStyle(bodyStyle);

		}

		sheet.setColumnWidth(5, mWidth);
		sheet.setColumnWidth(11, lWidth);
	}
	Footer footer = sheet.getFooter();
	footer.setCenter("GE Confidential");

	return workbook;
}
}
