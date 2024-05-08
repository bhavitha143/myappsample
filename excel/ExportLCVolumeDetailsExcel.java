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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.bh.realtrack.dto.LCVolumeAnalysisDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeActualCostDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeBoxPackingDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeDetailsTransportationDTO;
import com.bh.realtrack.dto.ShippingReportDTO;

public class ExportLCVolumeDetailsExcel {

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

	public void exportLCAgainstVolumeTransportationDetailsExcel(SXSSFWorkbook workbook,
			List<LcAgainstVolumeDetailsTransportationDTO> list) {
		Sheet sheet = workbook.createSheet("Transportation Details");
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
		cell.setCellValue(("SUB PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INSTALLATION COUNTRY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("RAC DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("READY FOR SHIPMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FREIGHT TERMS & TITLE TRANSFER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INCOTERMS CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION BDG (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION LAST BDG (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION ACT (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("% TRANSP BDG USED"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME OTR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME SHIPPED"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME SHIPPED %"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSubProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getInstallationCountry());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRacDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getShipDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getFreightTermsTitleTransfer());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getIncotermsCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getTransportationBdg());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getTransportationLastBdg());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getTransportationAct());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPerTranspBdgUsed());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVolumeOtr());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVolumeShipped());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVolumeShippedPer());
			cell.setCellStyle(bodyStyle);
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportLCAgainstVolumeBoxPackingDetailsExcel(SXSSFWorkbook workbook,
			List<LcAgainstVolumeBoxPackingDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("Box And Packing Details");
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
		cell.setCellValue(("SUB PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INSTALLATION COUNTRY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("RAC DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("READY FOR SHIPMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P BDG (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P LAST BDG (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P ACT (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PRODUCT RATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME OTR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME PACKED"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME PACKED %"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME PACKED LAST MONTH (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WOOD (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("THERMO (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P NOT NEEDED (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FIRST BOX EXPIRATION DATE"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSubProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getInstallationCountry());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRacDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getReadyForShipment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBpBdg());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBpLastBdg());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBpAct());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProductRate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVolumeOtr());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVolumePacked());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVolumePackedPer());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVolumePackedLastMonth());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getWood());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getThermo());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBpNotNeeded());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getExpirationDate());
			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportLCAgainstVolumeActualCostDetailsExcel(SXSSFWorkbook workbook,
			List<LcAgainstVolumeActualCostDetailsDTO> list) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet sheet = workbook.createSheet("Actual Cost Details");
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
		cell.setCellValue(("JOB"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DESTINATION ORGANIZATION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("RDI DESTINATION TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VENDOR NUM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VENDOR NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO APPROVAL STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO BUYER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO RELEASE BUYER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO RELEASE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO SHIPMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO LINE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO DISTRIBUTION LINE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO ITEM CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO ITEM DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO LINE CREATION DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("NEED BY DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROMISED DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO DISTRIBUTION QTY ORDERED"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("UNIT PRICE CURR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CURR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO EUR CONVERSION RATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO UNIT VALUE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO DISTRIBUTION VALUE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO VALUE RECEIVED"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO DISTRIBUTION BILLED"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO DISTRIBUTION TO BILL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACCOUNT NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("NOTE TO RECEIVER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ORG ID"));
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
			cell.setCellValue(list.get(i).getJob());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getDestinationOrganization());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRdiDestinationType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVendorNum());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVendorName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoApprovalStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoBuyerName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoReleaseBuyerName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoRelease());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoShipment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoLine());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoDistributionLine());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoItemCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoItemDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getPoLineCreationDate()
					&& !list.get(i).getPoLineCreationDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getPoLineCreationDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getNeedByDate() && !list.get(i).getNeedByDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getNeedByDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getPromisedDate() && !list.get(i).getPromisedDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getPromisedDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoDistributionQtyOrdered());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getUnitPriceCurr());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCurr());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoEurConversionRate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoUnitValue());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoDistributionValue());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoValueReceived());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoDistributionBilled());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPoDistributionToBill());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getAccountNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getNoteToReceiver());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getOrgId());
			cell.setCellStyle(bodyStyle);
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportLCVolumeAnalysisDetailsExcel(SXSSFWorkbook workbook, List<LCVolumeAnalysisDetailsDTO> list)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet sheet = workbook.createSheet("Summary Details");
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
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUB PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PEI DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX CLOSURE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PACK TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BARRIER BAG"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STORAGE COSE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PACKING ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("NET WEIGHT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("GROSS WEIGHT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LENGTH"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WIDTH"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("HEIGHT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ARRIVAL DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ARRIVAL PLACE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPIRATION DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CONFIRM DATE"));
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
			cell.setCellValue(list.get(i).getProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSubProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSso());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBoxId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBoxDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPeiDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBoxClosureDate() && !list.get(i).getBoxClosureDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getBoxClosureDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPackType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBarrierBag());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getStorageCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPackagingId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getNetWeight());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getGrossWeight());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getLength());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getWidth());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getHeight());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVolume());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getArrivalDate() && !list.get(i).getArrivalDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getArrivalDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getArrivalPlace());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getExpirationDate() && !list.get(i).getExpirationDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getExpirationDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getConfirmDate() && !list.get(i).getConfirmDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getConfirmDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

	public void exportShippingReportDetailsExcel(SXSSFWorkbook workbook, List<ShippingReportDTO> list)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet sheet = workbook.createSheet("Shipping Details");
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
		cell.setCellValue(("SALES ORDER TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SALES ORDER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CUSTOMER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("JOB NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX CLOSURE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX PLACE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX LENGTH"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX WIDTH"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX HEIGHT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("GROSS WEIGHT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("NET WEIGHT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PACK TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PACKING IDENTIFICATION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STORAGE DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PACKING EXPIRATION DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXW DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PERCIEVED EXW DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PEI DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BARRIER BAG"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FIRST BOX CLOSURE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX VALUE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ADDITIONAL COO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ARGOL ATTRIBUTE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BEACON NUMBER"));
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
			cell.setCellValue(list.get(i).getSalesOrderType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSalesOrder());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCustomerName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getJobNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPm());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBox());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBoxStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBoxClosureDate() && !list.get(i).getBoxClosureDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getBoxClosureDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBoxPlace());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBoxLength());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBoxWidth());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBoxHeight());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getGrossWeight());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getNetWeight());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPackType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPackingIdentification());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getStoragedescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getPackingExpirationDate()
					&& !list.get(i).getPackingExpirationDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getPackingExpirationDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getExwDate() && !list.get(i).getExwDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getExwDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getPercievedExwDate() && !list.get(i).getPercievedExwDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getPercievedExwDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPeiDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBarrierBag());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getFirstBoxClosureDate()
					&& !list.get(i).getFirstBoxClosureDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getFirstBoxClosureDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBoxValue());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getAdditionalCoo());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getArgolAttribute());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBeaconNumber());
			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

}
