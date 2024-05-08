package com.bh.realtrack.excel;

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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.bh.realtrack.dto.IdentikitProjectScopeTableDetailsDTO;

public class ExportIdentikitDetailsExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (26 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	private CellStyle getCellBodyStyle(SXSSFWorkbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}

	private Font getFontContent(SXSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	private CellStyle getCellHeadStyle(SXSSFWorkbook workbook) {
		CellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private Font getFontHeader(SXSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.WHITE.getIndex());
		return font;
	}

	private void setBorderStyle(CellStyle headStyle) {
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		headStyle.setBorderTop(BorderStyle.THIN);

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

	public void exportIdentikitProjectScopeDetailsExcel(SXSSFWorkbook workbook,
			List<IdentikitProjectScopeTableDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("Identikit Project Scope Table Details");
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
		cell.setCellValue(("JOB NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PRODUCT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PRODUCT FAMILY"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getJobNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProduct());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProductFamily());
			cell.setCellStyle(bodyStyle);
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

}
