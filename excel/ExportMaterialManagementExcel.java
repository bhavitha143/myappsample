package com.bh.realtrack.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.bh.realtrack.dto.MMMoveOrderPopupDetailsDTO;
import com.bh.realtrack.dto.MMPackingFDPopupDetailsDTO;
import com.bh.realtrack.dto.MMPickingPopupDetailsDTO;
import com.bh.realtrack.dto.MMReceivingPopupDetailsDTO;
import com.bh.realtrack.dto.MMWfAnomalyPopupDetailsDTO;

public class ExportMaterialManagementExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (26 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	public SXSSFWorkbook exportMMReceivingDetailsExcel(final SXSSFWorkbook workbook,
			List<MMReceivingPopupDetailsDTO> excelDetails) {
		Sheet sheet = workbook.createSheet("Material Management - Receiving Details");
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
		cell.setCellValue(("COSTING PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PEI"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PEI DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ORDERED ITEM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("QTY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FATHER CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OWNER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUPPLY STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ASCP ORDER NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VENDOR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROMISED DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO CHILD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SPA"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUYER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FULFILLMENT RESPONSIBILITY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INV ORG"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PLANT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO FATHER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CONTRACT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO HEADER NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO LINE NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MAX ARRIVAL DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("RECEIPT DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DELIVER DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WF ANOMALY NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WF ANOMALY CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WF ANOMALY CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WF ANOMALY OPEN DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WF ANOMALY CLOSE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OWNERSHIP"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < excelDetails.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;
			cell = row.createCell(dataColumnCount);
			cell = row.createCell(0);
			cell.setCellValue(excelDetails.get(i).getCostingProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOm());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPei());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPeiDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOrderedItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getQty());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getFatherCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOwner());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSupplyStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAscpOrderNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getVendor());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPromisedDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoChild());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSpa());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getBuyer());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getFulfillmentResponsibility());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getInvOrg());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPlant());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoFather());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getContract());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoHeaderNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoLineNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMaxArrivalDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getReceiptDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDeliverDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getWfAnomalyNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAnomalyCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAnomalyCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAnomalyOpenDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAnomalyCloseDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOwnership());
			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
		return workbook;
	}

	public SXSSFWorkbook exportMMPickingDetailsExcel(final SXSSFWorkbook workbook,
			List<MMPickingPopupDetailsDTO> excelDetails) {
		Sheet sheet = workbook.createSheet("Material Management - Picking Details");
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
		cell.setCellValue(("ITEM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER LINE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LOAD DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DROP DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("AGING (DROP DT VS MO ISSUE DT)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("QUANTITY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ORGANIZATION CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUBINVENTORY FROM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUBINVENTORY TO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO LOADER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LOADER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO DROPPER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DROPPER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LPN"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WIP"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < excelDetails.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;
			cell = row.createCell(dataColumnCount);
			cell = row.createCell(0);
			cell.setCellValue(excelDetails.get(i).getJob());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderLine());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getLoadDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDropDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAgingDropDtVsMoIssueDt());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getQuantity());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOrganizationCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSubInventoryFrom());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSubInventoryTo());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSsoLoader());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getLoaderName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSsoDropper());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDropperName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getLpn());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getWip());
			cell.setCellStyle(bodyStyle);

			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
		return workbook;
	}

	public SXSSFWorkbook exportMMMoveOrderDetailsExcel(SXSSFWorkbook workbook,
			List<MMMoveOrderPopupDetailsDTO> excelDetails) {
		Sheet sheet = workbook.createSheet("Material Management - Move Order Transfer Details");
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
		cell.setCellValue(("ITEM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER LINE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LOAD DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DROP DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("AGING (LOAD DT VS MO ISSUE DT)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("AGING (DROP DT VS LOAD DT)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("QUANTITY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ORGANIZATION CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUBINVENTORY FROM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUBINVENTORY TO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO LOADER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LOADER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO DROPPER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DROPPER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LPN"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WIP"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO LINE"));
		cell.setCellStyle(headStyle);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < excelDetails.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;
			cell = row.createCell(dataColumnCount);
			cell = row.createCell(0);
			cell.setCellValue(excelDetails.get(i).getJob());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderLine());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getLoadDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDropDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAgingLoadDtVsMoIssueDt());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAgingDropDtVsLoadDt());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getQuantity());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOrganizationCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSubInventoryFrom());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSubInventoryTo());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSsoLoader());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getLoaderName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSsoDropper());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDropperName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getLpn());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getWip());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSsoNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSsoLine());
			cell.setCellStyle(bodyStyle);
			cell.setCellStyle(bodyStyle);

		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
		return workbook;
	}

	public SXSSFWorkbook exportWFAnoDetailsExcel(SXSSFWorkbook workbook,
			List<MMWfAnomalyPopupDetailsDTO> excelDetails) {
		Sheet sheet = workbook.createSheet("Material Management - Move Order Transfer Details");
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
		cell.setCellValue(("WF ANOMALY NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ANOMALY CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WF IMPUTATION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CREATION DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CLOSURE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OWNERSHIP"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PLANT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INV. ORG."));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DEPARTMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROCESS STEP"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MATERIAL ORIGIN"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WAREHOUSE ORIGIN"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO LINE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO RELEASE/SHIPPING"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUPPLIER CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUPPLIER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MATERIAL LOCATION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SALES ORDER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OWNER ANOMALY TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("JOB"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < excelDetails.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;
			cell = row.createCell(dataColumnCount);
			cell = row.createCell(0);
			cell.setCellValue(excelDetails.get(i).getWfAnomalyNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAnomalyCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getWfImputation());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getCreationDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getClosureDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOwnership());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPlant());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getInvOrg());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDepartment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getProcessStep());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMaterialOrigin());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getWarehouseOrigin());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPo());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoLine());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoReleaseShipping());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSupplierCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSupplierName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMaterialLocation());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSalesOrder());
			cell.setCellStyle(bodyStyle);
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrder());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOwnerAnomalyType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getJob());
			cell.setCellStyle(bodyStyle);
			cell.setCellStyle(bodyStyle);
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
		return workbook;
	}

	public SXSSFWorkbook exportMMPackingFDDetailsExcel(SXSSFWorkbook workbook,
			List<MMPackingFDPopupDetailsDTO> excelDetails) {
		Sheet sheet = workbook.createSheet("Material Management - Packing Details");
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
		cell.setCellValue(("COSTING PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PEI"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PEI DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ORDERED ITEM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("QTY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FATHER CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OWNER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUPPLY STATUS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ASCP ORDER NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VENDOR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROMISED DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO CHILD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SPA"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUYER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FULFILLMENT RESPONSIBILITY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INV ORG"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PLANT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO FATHER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CONTRACT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO HEADER NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO LINE NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MAX ARRIVAL DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MOVE ORDER DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OUTERMOST BOX"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX CLOSURE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPIRATION DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BOX PLACE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LOADED TIME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO LOADER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LOADER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DROP OFF TIME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SSO DROPPER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DROPPER NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FROM SUBINVENTORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FROM LOCATOR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO LOCATOR"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}
		for (int i = 0; i < excelDetails.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;
			cell = row.createCell(dataColumnCount);
			cell = row.createCell(0);
			cell.setCellValue(excelDetails.get(i).getCostingProject());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOm());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPei());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPeiDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOrderedItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getQty());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getFatherCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOwner());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSupplyStatus());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getAscpOrderNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getVendor());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPromisedDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoChild());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSpa());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getBuyer());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getFulfillmentResponsibility());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getInvOrg());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPlant());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoFather());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getContract());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoHeaderNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getPoLineNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMaxArrivalDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrder());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getMoveOrderDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getOutermostBox());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getBoxClosureDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getExpirationDate());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getBoxPlace());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getLoadedTime());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSsoLoader());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getLoaderName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDropOffTime());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getSsoDropper());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getDropperName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getFromSubInventory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getFromLocator());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(excelDetails.get(i).getToLocater());
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

}
