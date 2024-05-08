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

import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisDetailsDTO;
import com.bh.realtrack.dto.LCDashboardDetailedWorkloadDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisDetailsDTO;
import com.bh.realtrack.dto.LCDashboardWorkloadDTO;

public class ExportLCDashboardDetailsExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (26 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	private CellStyle getCellBodyStyle(XSSFWorkbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}

	private Font getFontContent(XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	private CellStyle getCellHeadStyle(XSSFWorkbook workbook) {
		CellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private Font getFontHeader(XSSFWorkbook workbook) {
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

	private CellStyle getCellDateStyle(final XSSFWorkbook workbook) {
		CreationHelper creationHelper = workbook.getCreationHelper();
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		bodyStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MMM-yyyy"));
		return bodyStyle;
	}

	public void downloadLCDashboardWorkloadDetails(XSSFWorkbook workbook, List<LCDashboardWorkloadDTO> list)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet sheet = workbook.createSheet("Workload Details");
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
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INSTALLATION COUNTRY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OPPORTUNITY ID"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUSINESS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CUSTOMER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SOW"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT PHASE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SHIPPING MANAGER"));
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
		cell.setCellValue(("RAC DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P BDG AS SOLD (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P LAST BUDGET (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P ACTUAL COST (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P RATE (€/M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P VOLUME OTR (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME PACKED (M3)"));
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
		cell.setCellValue(("UNPACKED (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FIRST BOX EXPIRATION DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("READY FOR SHIPMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FIRST EXWORKS DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION BUDGET AS SOLD (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION LAST BDG (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION ACT COST (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION RATE (€/M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME SHIPPED (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME MSD (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SHIPPED WOOD (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SHIPPED THERMO (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SHIPPED UNPACKED (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PLP CODE-INTERNAL CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PLP CODE-VDR"));
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
			cell.setCellValue(list.get(i).getSegment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRegion());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getInstallationCountry());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getOpptyId());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBusiness());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCustomerName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSow());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCurrentStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPm());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSm());
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
			if (null != list.get(i).getRacDate() && !list.get(i).getRacDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getRacDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpBudgetAsSold() && !list.get(i).getBpBudgetAsSold().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpBudgetAsSold()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpLastBudget() && !list.get(i).getBpLastBudget().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpLastBudget()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpActualCost() && !list.get(i).getBpActualCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpActualCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpRate() && !list.get(i).getBpRate().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpRate()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpVolumeOTR() && !list.get(i).getBpVolumeOTR().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpVolumeOTR()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolumePacked() && !list.get(i).getVolumePacked().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolumePacked()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolumePackedLastMonth()
					&& !list.get(i).getVolumePackedLastMonth().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolumePackedLastMonth()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getWood() && !list.get(i).getWood().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getWood()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getThermo() && !list.get(i).getThermo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getThermo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getUnpacked() && !list.get(i).getUnpacked().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getUnpacked()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getFirstExpirationDate()
					&& !list.get(i).getFirstExpirationDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getFirstExpirationDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getReadyForShipment() && !list.get(i).getReadyForShipment().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getReadyForShipment());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getFirstExWorksDate() && !list.get(i).getFirstExWorksDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getFirstExWorksDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTransportationBudgetAsSold()
					&& !list.get(i).getTransportationBudgetAsSold().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTransportationBudgetAsSold()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTransportationLastBudget()
					&& !list.get(i).getTransportationLastBudget().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTransportationLastBudget()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTransportationActualCost()
					&& !list.get(i).getTransportationActualCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTransportationActualCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTransportationRate()
					&& !list.get(i).getTransportationRate().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTransportationRate()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolumeShipped() && !list.get(i).getVolumeShipped().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolumeShipped()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolumeMSD() && !list.get(i).getVolumeMSD().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolumeMSD()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getShippedWood() && !list.get(i).getShippedWood().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getShippedWood()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getShippedThermo() && !list.get(i).getShippedThermo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getShippedThermo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getShippedUnpacked()
					&& !list.get(i).getShippedUnpacked().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getShippedUnpacked()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPlpCodeInternalDoc());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPlpCodeVdr());
			cell.setCellStyle(bodyStyle);
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

	public void downloadLCDashboardDetailedWorkloadDetails(XSSFWorkbook workbook,
			List<LCDashboardDetailedWorkloadDTO> list) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet sheet = workbook.createSheet("Detailed Workload");
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
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INSTALLATION COUNTRY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OPPPORTUNITY ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CUSTOMER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("UNIT ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MACHINE TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT PHASE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SHIPPING MANAGER"));
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
		cell.setCellValue(("RAC DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P BDG AS SOLD (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P LAST BUDGET (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P ACTUAL COST (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P RATE (€/M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("B&P VOLUME OTR (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME PACKED (M3)"));
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
		cell.setCellValue(("UNPACKED (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FIRST BOX EXPIRATION DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("READY FOR SHIPMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FIRST EXWORKS DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION BUDGET AS SOLD (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION LAST BDG (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION ACT COST (€)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TRANSPORTATION RATE (€/M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME SHIPPED (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VOLUME MSD (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SHIPPED WOOD (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SHIPPED THERMO (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SHIPPED UNPACKED (M3)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PLP CODE - INTERNAL DOC"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PLP CODE - VDR"));
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
			cell.setCellValue(list.get(i).getSegment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRegion());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getInstallationCountry());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getOpportunityId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCustomerName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getUnitId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getMachineType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCurrentStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPm());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSm());
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
			if (null != list.get(i).getRacDate() && !list.get(i).getRacDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getRacDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpBudgetAsSold() && !list.get(i).getBpBudgetAsSold().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpBudgetAsSold()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpLastBudget() && !list.get(i).getBpLastBudget().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpLastBudget()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpActualCost() && !list.get(i).getBpActualCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpActualCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpRate() && !list.get(i).getBpRate().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpRate()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBpVolumeOTR() && !list.get(i).getBpVolumeOTR().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBpVolumeOTR()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolumePacked() && !list.get(i).getVolumePacked().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolumePacked()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolumePackedLastMonth()
					&& !list.get(i).getVolumePackedLastMonth().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolumePackedLastMonth()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getWood() && !list.get(i).getWood().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getWood()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getThermo() && !list.get(i).getThermo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getThermo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getUnpacked() && !list.get(i).getUnpacked().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getUnpacked()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getFirstBoxExpirationDate()
					&& !list.get(i).getFirstBoxExpirationDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getFirstBoxExpirationDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getReadyForShipment() && !list.get(i).getReadyForShipment().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getReadyForShipment());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getFirstExWorksDate() && !list.get(i).getFirstExWorksDate().equalsIgnoreCase("")) {
				Date dt = format.parse(list.get(i).getFirstExWorksDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTransportationBudgetAsSold()
					&& !list.get(i).getTransportationBudgetAsSold().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTransportationBudgetAsSold()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTransportationLastBudget()
					&& !list.get(i).getTransportationLastBudget().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTransportationLastBudget()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTransportationActualCost()
					&& !list.get(i).getTransportationActualCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTransportationActualCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTransportationRate()
					&& !list.get(i).getTransportationRate().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTransportationRate()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolumeShipped() && !list.get(i).getVolumeShipped().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolumeShipped()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolumeMSD() && !list.get(i).getVolumeMSD().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolumeMSD()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getShippedWood() && !list.get(i).getShippedWood().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getShippedWood()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getShippedThermo() && !list.get(i).getShippedThermo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getShippedThermo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getShippedUnpacked()
					&& !list.get(i).getShippedUnpacked().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getShippedUnpacked()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getInternalDoc());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVdr());
			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

	public void downloadLCDashboardBPSiteAnalysisDetails(XSSFWorkbook workbook,
			List<LCDashboardBPSiteAnalysisDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("B&P SITE ANALYSIS");
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
		cell.setCellValue("MONTH/YEAR");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("SITE");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("INCOTERMS");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("THERMO (M3)");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("WOOD (M3)");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("UNPACKED (M3)");
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getMonthYear());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSite());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getIncoterms());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getThermo() && !list.get(i).getThermo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getThermo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getWood());
			if (null != list.get(i).getWood() && !list.get(i).getWood().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getWood()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getUnPacked() && !list.get(i).getUnPacked().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getUnPacked()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

	public void downloadLCDashboardTransportationAnalysisDetails(XSSFWorkbook workbook,
			List<LCDashboardTransportationAnalysisDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("TRANSPORTATION ANALYSIS");
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
		cell.setCellValue("MOTHER JOB");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("SUBPROJECT");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("REGION");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("INCOTERMS");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("VOL SHIPPED");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("WEIGHT");
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue("TOTAL SPEND (€)");
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getMotherJob());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSubProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRegion());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getIncoterms());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getVolShipped() && !list.get(i).getVolShipped().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getVolShipped()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getWeight() && !list.get(i).getWeight().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getWeight()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTotalSpend() && !list.get(i).getTotalSpend().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTotalSpend()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

}
