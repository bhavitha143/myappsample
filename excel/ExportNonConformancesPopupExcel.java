package com.bh.realtrack.excel;

import com.bh.realtrack.dto.ManufacturingPopupDetailsDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

public class ExportNonConformancesPopupExcel {
    int sWidth = ((int) (22 * 1.14388)) * 256;



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

    public SXSSFWorkbook getNonConformancesPopupExcel(SXSSFWorkbook workbook, List<ManufacturingPopupDetailsDTO> manufacturingPopupDetailsDTOList, String sheetName) {
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
            cell.setCellValue(("SUB PROJECT"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("ORGANIZATION NAME"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("CRITICALITY"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("ORIGIN"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("LOCATION"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("SUB LOCATION"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("NCR NUMBER"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("CREATION DATE"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("NCR TYPE"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("LAST REVISION"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("STATUS"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("CLOSURE DATE"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("AGING/CYCLE TIME"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("CUSTOMER FEEDBACK"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("CUSTOMER APPROVAL REQUIRED"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("FINAL SPECIFIC RESPONSIBILITY"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("PART NUMBER"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("DISCREPANCY"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("DISCREPANCY COMMENTS"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("DEFECT"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("DISPOSITION"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("SPECIAL JOB ID"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("SPECIAL JOB DATE"));
            cell.setCellStyle(headStyle);

            headerColumnCount = headerColumnCount + 1;
            cell = row.createCell(headerColumnCount);
            cell.setCellValue(("SRC REFRESH DATE"));
            cell.setCellStyle(headStyle);

            CellStyle bodyStyle = getCellBodyStyle(workbook);

            for (int i = 0; i <= cell.getColumnIndex(); i++) {
                sheet.setColumnWidth(i, sWidth);
            }

            for(int i = 0; i<manufacturingPopupDetailsDTOList.size();i++){

                row = sheet.createRow(rowNum++);
                dataColumnCount = 0;

                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getSubProject());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getOrganizationName());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getCriticality());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getOrigin());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getPlantLocation());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getPlantSublocation());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getNcrNumber());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getCreationDate());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getNcrType());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getLastRevision());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getStatus());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getClosureDate());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getAging());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getCustomerFeedback());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getApprovalFlag());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getSpecificResponsibility());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getPartNumber());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getDiscrepancy());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getDiscrepancyComments());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getDefect());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getDispostion());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getSpecialJobId());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getSpecialJobDate());
                cell.setCellStyle(bodyStyle);

                dataColumnCount = dataColumnCount + 1;
                cell = row.createCell(dataColumnCount);
                cell.setCellValue(manufacturingPopupDetailsDTOList.get(i).getSrcRefreshDate());
                cell.setCellStyle(bodyStyle);

            }

            Footer footer = sheet.getFooter();
            footer.setCenter("BH Confidential");
            return workbook;
        }


}
