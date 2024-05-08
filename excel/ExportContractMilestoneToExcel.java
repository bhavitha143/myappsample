/**
 * 
 */
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bh.realtrack.dto.ContractMilestoneDTO;

/**
 * @author Anand Kumar
 *
 */
public class ExportContractMilestoneToExcel {

    public XSSFWorkbook exportContractMilestoneExcel(final XSSFWorkbook workbook,
            final List<ContractMilestoneDTO> contractMilestoneDTOList) {

        Sheet sheet = workbook.createSheet("CONTRACT MILESTONE");
        sheet.createFreezePane(0, 1);
        int rowNum = 1;
        int headerColumnCount = 0;
        int dataColumnCount = 0;
        Row row = null;
        Cell cell = null;

        row = sheet.createRow(0);
        row.setHeightInPoints(60);

        /*cell = row.createCell(0);
        cell.setCellValue(("Batch #"));
        cell.setCellStyle(getCellHeadStyle(workbook));*/

        cell = row.createCell(headerColumnCount);
        cell.setCellValue(("Activity Id"));
        cell.setCellStyle(getCellHeadStyle(workbook));

        headerColumnCount = headerColumnCount + 1;
        cell = row.createCell(headerColumnCount);
        cell.setCellValue(("Activity Name"));
        cell.setCellStyle(getCellHeadStyle(workbook));

        headerColumnCount = headerColumnCount + 1;
        cell = row.createCell(headerColumnCount);
        cell.setCellValue(("Forecast Date"));
        cell.setCellStyle(getCellHeadStyle(workbook));

        headerColumnCount = headerColumnCount + 1;
        cell = row.createCell(headerColumnCount);
        cell.setCellValue(("Contractual Due Date"));
        cell.setCellStyle(getCellHeadStyle(workbook));

        headerColumnCount = headerColumnCount + 1;
        cell = row.createCell(headerColumnCount);
        cell.setCellValue(("Total Float Variance (Days)"));
        cell.setCellStyle(getCellHeadStyle(workbook));

        headerColumnCount = headerColumnCount + 1;
        cell = row.createCell(headerColumnCount);
        cell.setCellValue(("Status"));
        cell.setCellStyle(getCellHeadStyle(workbook));

        headerColumnCount = headerColumnCount + 1;
        cell = row.createCell(headerColumnCount);
        cell.setCellValue(("Previous Forecast Date"));
        cell.setCellStyle(getCellHeadStyle(workbook));

        headerColumnCount = headerColumnCount + 1;
        cell = row.createCell(headerColumnCount);
        cell.setCellValue(("Delta Total Float Variance"));
        cell.setCellStyle(getCellHeadStyle(workbook));

        for (int i = 0; i <= cell.getColumnIndex(); i++) {
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < contractMilestoneDTOList.size(); i++) {
            row = sheet.createRow(rowNum++);

           /* cell = row.createCell(0);
            cell.setCellValue(contractMilestoneDTOList.get(i).getBatchId());
            cell.setCellStyle(getCellBodyStyle(workbook));*/

            dataColumnCount = 0;
            cell = row.createCell(dataColumnCount);
            cell.setCellValue(contractMilestoneDTOList.get(i).getActivityId());
            cell.setCellStyle(getCellBodyStyle(workbook));

            dataColumnCount = dataColumnCount + 1;
            cell = row.createCell(dataColumnCount);
            cell.setCellValue(contractMilestoneDTOList.get(i).getActivityName());
            cell.setCellStyle(getCellBodyStyle(workbook));

            dataColumnCount = dataColumnCount + 1;
            cell = row.createCell(dataColumnCount);
            cell.setCellValue(contractMilestoneDTOList.get(i).getForecastDate());
            cell.setCellStyle(getCellBodyStyle(workbook));

            dataColumnCount = dataColumnCount + 1;
            cell = row.createCell(dataColumnCount);
            cell.setCellValue(contractMilestoneDTOList.get(i).getContractualDueDate());
            cell.setCellStyle(getCellBodyStyle(workbook));

            dataColumnCount = dataColumnCount + 1;
            cell = row.createCell(dataColumnCount);
            cell.setCellValue(contractMilestoneDTOList.get(i).getTotalFloatDays());
            cell.setCellStyle(getCellBodyStyle(workbook));

            dataColumnCount = dataColumnCount + 1;
            cell = row.createCell(dataColumnCount);
            cell.setCellValue(contractMilestoneDTOList.get(i).getGrpTotalFloatDays());
            cell.setCellStyle(getCellBodyStyle(workbook));

            dataColumnCount = dataColumnCount + 1;
            cell = row.createCell(dataColumnCount);
            cell.setCellValue(contractMilestoneDTOList.get(i).getPreForcastDate());
            cell.setCellStyle(getCellBodyStyle(workbook));

            dataColumnCount = dataColumnCount + 1;
            cell = row.createCell(dataColumnCount);
            cell.setCellValue(contractMilestoneDTOList.get(i).getTotalFloatDelta());
            cell.setCellStyle(getCellBodyStyle(workbook));

        }

        Footer footer = sheet.getFooter();
        footer.setCenter("GE Confidential");

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
}
