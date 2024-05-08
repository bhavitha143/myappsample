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
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.bh.realtrack.dto.MaterialListDetailsDTO;

public class ExportMaterialListDetailsDTO {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (26 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	public void exportMaterialListLiveDetailsExcel(SXSSFWorkbook workbook, List<MaterialListDetailsDTO> list,
			String viewName) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet sheet = workbook.createSheet("Procurement " + viewName);
		sheet.createFreezePane(0, 1);
		CellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(20);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue("TRAIN");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("JOB");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("COMPONENT CODE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("COMPONENT DESCRIPTION");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("ACTIVITY ID");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("ITEM DESCRIPTION");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("BL EARLY PO ISSUE DATE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("BL LATE PO ISSUE DATE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("ACTUAL PO ISSUE DATE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("PO NUMBER");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("SUPPLIER NAME");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("NEED DELIVERY DATE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("CONTRACTUAL DATE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("EXPECTED DELIVERY DATE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("ACTUAL DELIVERY DATE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("STATUS");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("DUMMY CODE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("REAL CODE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("MAIN ITEM");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("INTERNAL ITEM");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("DE NAME");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("BUYER NAME");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("SFM NAME");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("TECH ALIGN WF ID");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("TECH ALIGN WF LINK");
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);
		CellStyle dateBodyStyle = getCellDateStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);

			dataColumnCount = 0;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getTrain());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSubProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getComponentCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getComponentDesc());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getActivityId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getPlannedPoIssueDate()
					&& !list.get(i).getPlannedPoIssueDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getPlannedPoIssueDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBlLatePoIssueDt() && !list.get(i).getBlLatePoIssueDt().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getBlLatePoIssueDt());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getActualPoIssueDate()
					&& !list.get(i).getActualPoIssueDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getActualPoIssueDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSupplierName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getNeedPoDeliveryDate()
					&& !list.get(i).getNeedPoDeliveryDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getNeedPoDeliveryDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getContractualPoDeliveryDate()
					&& !list.get(i).getContractualPoDeliveryDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getContractualPoDeliveryDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getExpectedPoDeliveryDate()
					&& !list.get(i).getExpectedPoDeliveryDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getExpectedPoDeliveryDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getActualPoDeliveryDate()
					&& !list.get(i).getActualPoDeliveryDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getActualPoDeliveryDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemStatus());
			cell.setCellStyle(getCellBodyStyle2(workbook, list.get(i).getItemStatus()));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getDummyCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRealCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getMainItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getInternalItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getDeName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBuyerName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSfmName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getWorkflowId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getTaWorkflowLink());
			cell.setCellStyle(bodyStyle);
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

	public void exportMaterialListCustomDetailsExcel(SXSSFWorkbook workbook, List<MaterialListDetailsDTO> list,
			String viewName) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet sheet = workbook.createSheet("Procurement " + viewName);
		String trainNo = "", jobNumber = "", bomPeiDescription = "";
		int rowNum = 1, headerColumnCount = 0, dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);
		CellStyle dateBodyStyle = getCellDateStyle(workbook);

		row = sheet.createRow(0);
		row.setHeightInPoints(40);
		sheet.createFreezePane(0, 1);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue("ITEM DESCRIPTION");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BL EARLY PO ISSUE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BL LATE PO ISSUE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL PO ISSUE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUPPLIER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("NEED DELIVERY DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CONTRACTUAL DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPECTED DELIVERY DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL DELIVERY DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			dataColumnCount = 0;

			if (!(trainNo.equalsIgnoreCase(list.get(i).getTrain()))) {
				if (!trainNo.equalsIgnoreCase("")) {
					row = sheet.createRow(rowNum++);
				}
				trainNo = list.get(i).getTrain();
				row = sheet.createRow(rowNum++);
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(trainNo);
				cell.setCellStyle(getCellHeadStyle2(workbook));
				bomPeiDescription = "";
			}

			if (!(jobNumber.equalsIgnoreCase(list.get(i).getSubProject()))) {
				jobNumber = list.get(i).getSubProject();
				row = sheet.createRow(rowNum++);
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(jobNumber);
				cell.setCellStyle(getCellHeadStyle3(workbook));
			}

			if (!(bomPeiDescription.equalsIgnoreCase(list.get(i).getComponentDesc()))) {
				if (!bomPeiDescription.equalsIgnoreCase("")) {
					row = sheet.createRow(rowNum++);
				}
				row = sheet.createRow(rowNum++);
				bomPeiDescription = list.get(i).getComponentDesc();
				sheet.setColumnWidth(dataColumnCount, lWidth);
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(bomPeiDescription);
				cell.setCellStyle(getCellHeadStyle4(workbook));
			}

			row = sheet.createRow(rowNum++);
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getPlannedPoIssueDate()
					&& !list.get(i).getPlannedPoIssueDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getPlannedPoIssueDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBlLatePoIssueDt() && !list.get(i).getBlLatePoIssueDt().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getBlLatePoIssueDt());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getActualPoIssueDate()
					&& !list.get(i).getActualPoIssueDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getActualPoIssueDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSupplierName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getNeedPoDeliveryDate()
					&& !list.get(i).getNeedPoDeliveryDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getNeedPoDeliveryDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getContractualPoDeliveryDate()
					&& !list.get(i).getContractualPoDeliveryDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getContractualPoDeliveryDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getExpectedPoDeliveryDate()
					&& !list.get(i).getExpectedPoDeliveryDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getExpectedPoDeliveryDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getActualPoDeliveryDate()
					&& !list.get(i).getActualPoDeliveryDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getActualPoDeliveryDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemStatus());
			cell.setCellStyle(getCellBodyStyle2(workbook, list.get(i).getItemStatus()));
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

	private CellStyle getCellHeadStyle(final SXSSFWorkbook workbook) {
		XSSFCellStyle headStyle = (XSSFCellStyle) workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 0; // red
		rgb[1] = (byte) 32; // green
		rgb[2] = (byte) 96; // blue
		XSSFColor xssfColor = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(xssfColor);
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		headStyle.setWrapText(true);
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		return headStyle;
	}

	private CellStyle getCellHeadStyle2(final SXSSFWorkbook workbook) {
		XSSFCellStyle headStyle = (XSSFCellStyle) workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 0; // red
		rgb[1] = (byte) 128; // green
		rgb[2] = (byte) 0; // blue
		XSSFColor xssfColor = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(xssfColor);
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader2(workbook));
		headStyle.setWrapText(true);
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		return headStyle;
	}

	private CellStyle getCellHeadStyle3(final SXSSFWorkbook workbook) {
		XSSFCellStyle headStyle = (XSSFCellStyle) workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 90; // red
		rgb[1] = (byte) 191; // green
		rgb[2] = (byte) 248; // blue
		XSSFColor xssfColor = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(xssfColor);
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader2(workbook));
		headStyle.setWrapText(true);
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		return headStyle;
	}

	private CellStyle getCellHeadStyle4(final SXSSFWorkbook workbook) {
		XSSFCellStyle headStyle = (XSSFCellStyle) workbook.createCellStyle();
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader2(workbook));
		headStyle.setWrapText(true);
		headStyle.setAlignment(HorizontalAlignment.CENTER);
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
		font.setFontHeightInPoints((short) 12);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.WHITE.getIndex());
		return font;
	}

	private Font getFontHeader2(final SXSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLUE.getIndex());
		return font;
	}

	private CellStyle getCellBodyStyle(final SXSSFWorkbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.setWrapText(true);
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}

	private CellStyle getCellBodyStyle2(final SXSSFWorkbook workbook, String status) {
		XSSFCellStyle bodyStyle = (XSSFCellStyle) workbook.createCellStyle();
		XSSFColor xssfColor = getItemStatusColor(status);
		bodyStyle.setFillForegroundColor(xssfColor);
		bodyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		setBorderStyle(bodyStyle);
		bodyStyle.setWrapText(true);
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}

	private Font getFontContent(final SXSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	private CellStyle getCellDateStyle(final SXSSFWorkbook workbook) {
		CreationHelper creationHelper = workbook.getCreationHelper();
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		bodyStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MMM-yyyy"));
		return bodyStyle;
	}

	private XSSFColor getItemStatusColor(String status) {
		String color = "";
		switch (status) {
		case "Arrived":
			color = "BH_GREEN";
			break;
		case "Expected on time":
			color = "BH_LIGHT_GREEN";
			break;
		case "PO to be issued - on time":
			color = "BH_LIGHT_GREEN";
			break;
		case "PO partially issued - Expected Late - take action":
			color = "BH_ROSE_500";
			break;
		case "PO partially issued - Expected Late & expired - take action":
			color = "BH_ROSE_500";
			break;
		case "PO partially issued - Expected expired - take action":
			color = "BH_ROSE_500";
			break;
		case "PO partially issued - Expected on time":
			color = "BH_GOLD";
			break;
		case "PO Not Issued and delay is +4 weeks":
			color = "BH_ROSE_500";
			break;
		case "Expected Late":
			color = "BH_ROSE_500";
			break;
		case "Expected Late & expired - to check":
			color = "BH_ROSE_500";
			break;
		case "PO Not Issued and Need in the past":
			color = "BH_ROSE_500";
			break;
		case "PO Not Issued and overdue vs Late BL":
			color = "BH_ROSE_500";
			break;
		case "Need in delay & no Expected - to check":
			color = "BH_ROSE_500";
			break;
		case "Expected Late & expired":
			color = "BH_ROSE_500";
			break;
		case "Expected expired - to check":
			color = "BH_GOLD";
			break;
		case "No Need Delivery - to check":
			color = "BH_GOLD";
			break;
		case "Need in the future, no Expected - to check":
			color = "BH_GOLD";
			break;
		case "PO Not Issued - take action":
			color = "BH_GOLD";
			break;
		case "Expected expired":
			color = "BH_GOLD";
			break;
		case "Soft Pegging":
			color = "BH_GREEN";
			break;
		case "Not Issued and Need date in the past":
			color = "BH_GOLD";
			break;
		case "No Need date – to check":
			color = "BH_GOLD";
			break;
		default:
			// white
		}
		byte[] rgb = new byte[3];
		if (color.equalsIgnoreCase("BH_GREEN")) {
			rgb[0] = (byte) 1; // red
			rgb[1] = (byte) 131; // green
			rgb[2] = (byte) 116; // blue
		} else if (color.equalsIgnoreCase("BH_LIGHT_GREEN")) {
			rgb[0] = (byte) 2; // red
			rgb[1] = (byte) 188; // green
			rgb[2] = (byte) 148; // blue
		} else if (color.equalsIgnoreCase("BH_GOLD")) {
			rgb[0] = (byte) 253; // red
			rgb[1] = (byte) 183; // green
			rgb[2] = (byte) 20; // blue
		} else if (color.equalsIgnoreCase("BH_ROSE_500")) {
			rgb[0] = (byte) 225; // red
			rgb[1] = (byte) 45; // green
			rgb[2] = (byte) 57; // blue
		} else {
			rgb[0] = (byte) 255; // red
			rgb[1] = (byte) 255; // green
			rgb[2] = (byte) 255; // blue
		}
		XSSFColor xssfColor = new XSSFColor(rgb);
		return xssfColor;
	}
}
