/**
 * 
 */
package com.bh.realtrack.excel;

import java.util.Arrays;
import java.util.Iterator;

import javax.ws.rs.ServerErrorException;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.realtrack.dto.KickOffDTO;
import com.bh.realtrack.enumerators.ErrorCode;

/**
 * @author Anand Kumar
 *
 */
public class ExportKickOffToExcel {
	private static Logger log = LoggerFactory.getLogger(ExportKickOffToExcel.class.getName());

	public XSSFWorkbook exportKickOffExcel(final XSSFWorkbook workbook, final KickOffDTO kickOffDTO) {
		JSONObject jsonObject = null;
		JSONArray jsonArrayColumns = null;
		JSONArray jsonArrayRows = null;

		try {
			jsonObject = new JSONObject(kickOffDTO.getMatrix());
			jsonArrayColumns = jsonObject.getJSONArray("columns");
			jsonArrayRows = jsonObject.getJSONArray("rows");
			JSONObject matrixColumns = (JSONObject) jsonArrayColumns.get(0);
			JSONArray matrixRows = (JSONArray) jsonArrayRows.get(0);
			Iterator<String> itrColumns = matrixColumns.keys();
			Sheet sheet = workbook.createSheet("KICK OFF");
			mergeCellKickOffExcel(workbook, sheet);
			sheet.setDisplayGridlines(false);

			int rowNum = 4;
			Row row = null;
			Cell cell = null;
			row = sheet.createRow(rowNum);
			row.setHeightInPoints(40);
			int columnKeys[] = keysArray(matrixColumns.length(), itrColumns);

			for (int i = 0; i < columnKeys.length; i++) {
				String key = String.valueOf(columnKeys[i]);
				int temp = Integer.parseInt(key);
				int cellIndex = temp + 2;
				cell = row.createCell(cellIndex);
				cell.setCellValue(matrixColumns.getString(key));
				if (cellIndex == 2) {
					cell.setCellStyle(getCellHeadGreyStyle(workbook));
				} else {
					cell.setCellStyle(getCellHeadStyle(workbook));
				}
				sheet.setColumnWidth(cellIndex, 5000);

			}

			for (int i = 0; i < matrixRows.length(); i++) {
				JSONObject matrixRow = (JSONObject) matrixRows.get(i);
				Iterator<String> itrRows = matrixRow.keys();
				int rowKeys[] = keysArray(matrixRow.length(), itrRows);
				row = sheet.createRow(++rowNum);
				row.setHeightInPoints(25);
				if (rowNum == 5) {
					cell = row.createCell(i + 1);
					cell.setCellValue("BH" + " " + "\u2193");
				}
				for (int k = 0; k < rowKeys.length; k++) {
					String key = String.valueOf(rowKeys[k]);
					int temp = Integer.parseInt(key);
					int cellIndex = temp + 2;
					cell = row.createCell(cellIndex);
					cell.setCellValue(matrixRow.getString(key));
					if (cellIndex == 2) {
						cell.setCellStyle(getCellHeadGreenStyle(workbook));
					} else {
						cell.setCellStyle(getCellBodyStyle(workbook));
					}
					sheet.setColumnWidth(cellIndex, 5000);

				}
			}
		} catch (JSONException e) {
			log.error("something went wrong while export kick off excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		}
		return workbook;
	}

	private int[] keysArray(int length, Iterator<String> itr) {
		int keys[] = new int[length];
		for (int i = 0; i < length; i++) {
			keys[i] = Integer.parseInt(itr.next());
		}
		Arrays.sort(keys);
		return keys;

	}
	
	private CellStyle getCellHeadStyle(final XSSFWorkbook workbook) {
		XSSFCellStyle headStyle = workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 148; // red
		rgb[1] = (byte) 148; // green
		rgb[2] = (byte) 148; // blue
		XSSFColor color = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(color);
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}
	

	private CellStyle getCellHeadGreyStyle(final XSSFWorkbook workbook) {
		XSSFCellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private CellStyle getCellHeadGreenStyle(final XSSFWorkbook workbook) {
		XSSFCellStyle headStyle = workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 0; // red
		rgb[1] = (byte) 165; // green
		rgb[2] = (byte) 184; // blue
		XSSFColor color = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(color);
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private void setBorderStyle(final XSSFCellStyle headStyle) {
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		headStyle.setBorderTop(BorderStyle.THIN);
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 192; // red
		rgb[1] = (byte) 192; // green
		rgb[2] = (byte) 192; // blue
		XSSFColor color = new XSSFColor(rgb);
		headStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		headStyle.setBottomBorderColor(color);
		headStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		headStyle.setTopBorderColor(color);
		headStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		headStyle.setLeftBorderColor(color);
		headStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		headStyle.setRightBorderColor(color);
	}

	private Font getFontHeader(final XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 8);
		font.setFontName("Arial");
		font.setColor(IndexedColors.WHITE.getIndex());
		return font;
	}

	private CellStyle getCellBodyStyle(final XSSFWorkbook workbook) {
		XSSFCellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.setWrapText(true);
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}
	

	private Font getFontContent(final XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("Arial");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	private void mergeCellKickOffExcel(XSSFWorkbook workbook, Sheet sheet) {
		Row rowHeading = sheet.createRow(1);
		Row rowArrow = sheet.createRow(2);
		CellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setAlignment(HorizontalAlignment.CENTER);
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 15);
		font.setFontName("Arial");
		font.setColor(IndexedColors.BLACK.getIndex());
		style.setFont(font);
		Cell cell = null;
		cell = rowHeading.createCell(1);
		cell.setCellValue("Communication Matrix");

		cell = rowArrow.createCell(3);
		cell.setCellValue("Customer" + " " + "\u2192");
	}
	
	
}
