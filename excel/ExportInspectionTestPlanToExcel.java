package com.bh.realtrack.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bh.realtrack.dto.InspectionTestPlanDTO;
import com.bh.realtrack.dto.MCCDetailsDTO;

public class ExportInspectionTestPlanToExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (22 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	public XSSFWorkbook exportInspectionTestPlanExcel(final XSSFWorkbook workbook,
			final List<InspectionTestPlanDTO> inspectionTestPlanDTOList) throws ParseException {

		Sheet sheet = workbook.createSheet("INSPECTION TEST PLAN");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(20);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);
		CellStyle dateBodyStyle = getCellDateStyle(workbook);

		cell = row.createCell(0);
		cell.setCellValue(("OM Description"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(1);
		cell.setCellValue(("Qcp Page Name"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(2);
		cell.setCellValue(("Costing Project"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(3);
		cell.setCellValue(("Functional Unit"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(4);
		cell.setCellValue(("Functional Unit Description"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(5);
		cell.setCellValue(("PEI Code"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(6);
		cell.setCellValue(("Long Dummy Code"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(7);
		cell.setCellValue(("Item Code"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(8);
		cell.setCellValue(("Item Description"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(9);
		cell.setCellValue(("QCP Doc"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(10);
		cell.setCellValue(("Rev QCP"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(11);
		cell.setCellValue(("Requirement ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(12);
		cell.setCellValue(("Requirement Description"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(13);
		cell.setCellValue(("Status"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(14);
		cell.setCellValue(("Ref Docs"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(15);
		cell.setCellValue(("Procedure"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(16);
		cell.setCellValue(("Acceptance"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(17);
		cell.setCellValue(("BH"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(18);
		cell.setCellValue(("Customer"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(19);
		cell.setCellValue(("End User"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(20);
		cell.setCellValue(("TP"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(21);
		cell.setCellValue(("Supplier"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(22);
		cell.setCellValue(("Supplier Location"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(23);
		cell.setCellValue(("Sub Supplier Location"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(24);
		cell.setCellValue(("PO/WIP #"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(25);
		cell.setCellValue(("PO Line"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(26);
		cell.setCellValue(("Expected Inspection Start Date"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(27);
		cell.setCellValue(("Customer Notification Number"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(28);
		cell.setCellValue(("Notification Number"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(29);
		cell.setCellValue(("Notification Status"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(30);
		cell.setCellValue(("Notification Revision"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(31);
		cell.setCellValue(("Inspection Date"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(32);
		cell.setCellValue(("Inspection Duration"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(33);
		cell.setCellValue(("Inspection Type"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(34);
		cell.setCellValue(("Notification To Customer"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(35);
		cell.setCellValue(("Rc1 Reference"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(36);
		cell.setCellValue(("Test Results"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(37);
		cell.setCellValue(("Inspection Notes"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(38);
		cell.setCellValue(("Usr Expected Inspection Start Date"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}
		for (InspectionTestPlanDTO inspectionDTO : inspectionTestPlanDTOList) {
			row = sheet.createRow(rowNum++);

			cell = row.createCell(0);
			cell.setCellValue(inspectionDTO.getOmDescription());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(1);
			cell.setCellValue(inspectionDTO.getQcpPageName());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(2);
			cell.setCellValue(inspectionDTO.getCostingProject());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(3);
			cell.setCellValue(inspectionDTO.getFunctionalUnit());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(4);
			cell.setCellValue(inspectionDTO.getFunctionalUnitDescription());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(5);
			cell.setCellValue(inspectionDTO.getPeiCode());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(6);
			cell.setCellValue(inspectionDTO.getLongDummyCode());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(7);
			cell.setCellValue(inspectionDTO.getItemCode());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(8);
			cell.setCellValue(inspectionDTO.getItemDescription());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(9);
			cell.setCellValue(inspectionDTO.getQcpDoc());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(10);
			cell.setCellValue(inspectionDTO.getRevQCP());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(11);
			cell.setCellValue(inspectionDTO.getRequirementId());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(12);
			cell.setCellValue(inspectionDTO.getRequirementDescription());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(13);
			cell.setCellValue(inspectionDTO.getStatus());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(14);
			cell.setCellValue(inspectionDTO.getRefDocs());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(15);
			cell.setCellValue(inspectionDTO.getProcedure());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(16);
			cell.setCellValue(inspectionDTO.getAcceptance());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(17);
			cell.setCellValue(inspectionDTO.getGe());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(18);
			cell.setCellValue(inspectionDTO.getCustomer());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(19);
			cell.setCellValue(inspectionDTO.getEndUser());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(20);
			cell.setCellValue(inspectionDTO.getTp());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(21);
			cell.setCellValue(inspectionDTO.getSupplier());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(22);
			cell.setCellValue(inspectionDTO.getSupplierLocation());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(23);
			cell.setCellValue(inspectionDTO.getSubSupplierLocation());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(24);
			cell.setCellValue(inspectionDTO.getPoWip());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(25);
			cell.setCellValue(inspectionDTO.getPoLine());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(26);
			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			if (null != inspectionDTO.getExpectedInspectionStartDate()
					&& !inspectionDTO.getExpectedInspectionStartDate().equalsIgnoreCase("")) {
				Date dt = format.parse(inspectionDTO.getExpectedInspectionStartDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			cell = row.createCell(27);
			cell.setCellValue(inspectionDTO.getCustNotificationNumber());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(28);
			cell.setCellValue(inspectionDTO.getNotificationNumber());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(29);
			cell.setCellValue(inspectionDTO.getNotificationStatus());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(30);
			cell.setCellValue(inspectionDTO.getNotificationRevision());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(31);
			if (null != inspectionDTO.getInspectionDate() && !inspectionDTO.getInspectionDate().equalsIgnoreCase("")) {
				Date dt = format.parse(inspectionDTO.getInspectionDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			cell = row.createCell(32);
			cell.setCellValue(inspectionDTO.getInspectionDuration());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(33);
			cell.setCellValue(inspectionDTO.getInspectionType());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(34);
			cell.setCellValue(inspectionDTO.getNotificationToCustomer());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(35);
			cell.setCellValue(inspectionDTO.getRc1Reference());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(36);
			cell.setCellValue(inspectionDTO.getTestResult());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(37);
			cell.setCellValue(inspectionDTO.getInspectionNotes());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(38);
			if (null != inspectionDTO.getUsrExpectedInspectionStartDt()
					&& !inspectionDTO.getUsrExpectedInspectionStartDt().equalsIgnoreCase("")) {
				Date dt = format.parse(inspectionDTO.getUsrExpectedInspectionStartDt());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("GE Confidential");

		return workbook;
	}

	public XSSFWorkbook exportCustomerInspectionTestPlanExcel(final XSSFWorkbook workbook,
			final List<InspectionTestPlanDTO> inspectionTestPlanDTOList) throws ParseException {

		Sheet sheet = workbook.createSheet("INSPECTION TEST PLAN");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(20);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);
		CellStyle dateBodyStyle = getCellDateStyle(workbook);

		cell = row.createCell(0);
		cell.setCellValue(("Qcp Page Name"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(1);
		cell.setCellValue(("Costing Project"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(2);
		cell.setCellValue(("QCP Doc"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(3);
		cell.setCellValue(("Rev QCP"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(4);
		cell.setCellValue(("Item Description"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(5);
		cell.setCellValue(("Requirement ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(6);
		cell.setCellValue(("Requirement Description"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(7);
		cell.setCellValue(("Ref Docs"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(8);
		cell.setCellValue(("Procedure"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(9);
		cell.setCellValue(("Acceptance"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(10);
		cell.setCellValue(("BH"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(11);
		cell.setCellValue(("Customer"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(12);
		cell.setCellValue(("End User"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(13);
		cell.setCellValue(("TP"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(14);
		cell.setCellValue(("Supplier"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(15);
		cell.setCellValue(("Supplier Location"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(16);
		cell.setCellValue(("Sub Supplier Location"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(17);
		cell.setCellValue(("Expected Inspection Start Date"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(18);
		cell.setCellValue(("Customer Notification Number"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(19);
		cell.setCellValue(("Notification Status"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(20);
		cell.setCellValue(("Notification Revision"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(21);
		cell.setCellValue(("Inspection Date"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}
		for (InspectionTestPlanDTO customerInspection : inspectionTestPlanDTOList) {
			row = sheet.createRow(rowNum++);

			cell = row.createCell(0);
			cell.setCellValue(customerInspection.getQcpPageName());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(1);
			cell.setCellValue(customerInspection.getCostingProject());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(2);
			cell.setCellValue(customerInspection.getQcpDoc());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(3);
			cell.setCellValue(customerInspection.getRevQCP());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(4);
			cell.setCellValue(customerInspection.getItemDescription());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(5);
			cell.setCellValue(customerInspection.getRequirementId());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(6);
			cell.setCellValue(customerInspection.getRequirementDescription());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(7);
			cell.setCellValue(customerInspection.getRefDocs());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(8);
			cell.setCellValue(customerInspection.getProcedure());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(9);
			cell.setCellValue(customerInspection.getAcceptance());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(10);
			cell.setCellValue(customerInspection.getGe());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(11);
			cell.setCellValue(customerInspection.getCustomer());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(12);
			cell.setCellValue(customerInspection.getEndUser());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(13);
			cell.setCellValue(customerInspection.getTp());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(14);
			cell.setCellValue(customerInspection.getSupplier());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(15);
			cell.setCellValue(customerInspection.getSupplierLocation());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(16);
			cell.setCellValue(customerInspection.getSubSupplierLocation());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(17);
			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			if (null != customerInspection.getExpectedInspectionStartDate()
					&& !customerInspection.getExpectedInspectionStartDate().equalsIgnoreCase("")) {
				Date dt = format.parse(customerInspection.getExpectedInspectionStartDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			cell = row.createCell(18);
			cell.setCellValue(customerInspection.getCustNotificationNumber());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(19);
			cell.setCellValue(customerInspection.getNotificationStatus());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(20);
			cell.setCellValue(customerInspection.getNotificationRevision());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(21);
			if (null != customerInspection.getInspectionDate()
					&& !customerInspection.getInspectionDate().equalsIgnoreCase("")) {
				Date dt = format.parse(customerInspection.getInspectionDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("GE Confidential");

		return workbook;
	}

	public XSSFWorkbook exportMCCDetailsExcel(XSSFWorkbook workbook, List<MCCDetailsDTO> excelDetails)
			throws ParseException {
		Sheet sheet = workbook.createSheet("MCC Details");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(20);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);
		CellStyle dateBodyStyle = getCellDateStyle(workbook);

		cell = row.createCell(0);
		cell.setCellValue(("Job"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(1);
		cell.setCellValue(("Module"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(2);
		cell.setCellValue(("Module Desc"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(3);
		cell.setCellValue(("System"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(4);
		cell.setCellValue(("System Desc"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(5);
		cell.setCellValue(("Sub System"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(6);
		cell.setCellValue(("Sub System Desc"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(7);
		cell.setCellValue(("Discipline"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(8);
		cell.setCellValue(("Tag"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(9);
		cell.setCellValue(("Tag Desc"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(10);
		cell.setCellValue(("ITR"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(11);
		cell.setCellValue(("ITR Desc"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(12);
		cell.setCellValue(("Status"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}
		for (MCCDetailsDTO mccDetails : excelDetails) {
			row = sheet.createRow(rowNum++);

			cell = row.createCell(0);
			cell.setCellValue(mccDetails.getJob());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(1);
			cell.setCellValue(mccDetails.getModule());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(2);
			cell.setCellValue(mccDetails.getModuleDesc());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(3);
			cell.setCellValue(mccDetails.getSystem());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(4);
			cell.setCellValue(mccDetails.getSysDesc());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(5);
			cell.setCellValue(mccDetails.getSubSystem());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(6);
			cell.setCellValue(mccDetails.getSubSysDesc());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(7);
			cell.setCellValue(mccDetails.getDiscipline());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(8);
			cell.setCellValue(mccDetails.getTag());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(9);
			cell.setCellValue(mccDetails.getTagDesc());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(10);
			cell.setCellValue(mccDetails.getItr());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(11);
			cell.setCellValue(mccDetails.getItrDesc());
			cell.setCellStyle(bodyStyle);

			cell = row.createCell(12);
			cell.setCellValue(mccDetails.getStatus());
			cell.setCellStyle(bodyStyle);

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

		return workbook;
	}

	private CellStyle getCellHeadStyle(final XSSFWorkbook workbook) {
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

	private Font getFontHeader(final XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.WHITE.getIndex());
		return font;
	}

	private CellStyle getCellBodyStyle(final XSSFWorkbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}

	private Font getFontContent(final XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	private CellStyle getCellDateStyle(final XSSFWorkbook workbook) {
		CreationHelper creationHelper = workbook.getCreationHelper();
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		bodyStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MMM-yyyy"));
		return bodyStyle;
	}

}
