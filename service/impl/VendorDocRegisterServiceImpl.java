package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IVendorDocRegisterDAO;
import com.bh.realtrack.dto.VDRStatisticsOTDDetailsDTO;
import com.bh.realtrack.excel.ExportStatisticsOTDDetailsExcel;
import com.bh.realtrack.service.IVendorDocRegisterService;

@Service
public class VendorDocRegisterServiceImpl implements IVendorDocRegisterService {

	private static final Logger log = LoggerFactory.getLogger(VendorDocRegisterServiceImpl.class);

	@Autowired
	IVendorDocRegisterDAO iVendorDocRegisterDAO;

	@Override
	public Map<String, Object> getStatisticsMileStoneFilter(String projectId, String subProjectNo,
			String subProductLine, String docOwner, String docType, String docLevel) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		try {
			list.add("VDR 1st");
			responseMap.put("data", list);
		} catch (Exception e) {
			log.error("getStatisticsMileStoneFilter(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getStatisticsOTDChartData(String projectId, String subProjectNo, String subProductLine,
			String docOwner, String docType, String docLevel, String milestoneCode) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Integer> otdMap = new HashMap<String, Integer>();
		String lastUpdateDate = "";
		int compLate = 0, compOnTime = 0, overdue = 0, vorOTD = 0, otdTotal = 0;
		try {
			otdMap = iVendorDocRegisterDAO.getStatisticsOTDDetails(projectId, subProjectNo, docOwner, docType);
			compLate = null != otdMap.get("Completed Late") ? otdMap.get("Completed Late") : 0;
			compOnTime = null != otdMap.get("Completed On Time") ? otdMap.get("Completed On Time") : 0;
			overdue = null != otdMap.get("Overdue") ? otdMap.get("Overdue") : 0;
			if (compLate != 0 || compOnTime != 0 || overdue != 0) {
				otdTotal = compLate + compOnTime + overdue;
				vorOTD = (int) Math.round(((double) (compOnTime) / (double) otdTotal) * 100);
			}
			lastUpdateDate = iVendorDocRegisterDAO.getStatisticsOTDLastUpdateDate(projectId);
			dataMap.put("otd", otdMap);
			dataMap.put("otdTotal", otdTotal);
			dataMap.put("vorOTD", vorOTD);
			dataMap.put("lastUpdateDate", lastUpdateDate);
			responseMap.put("data", dataMap);
		} catch (Exception e) {
			log.error("getStatisticsOTDChartData(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getStatisticsOTDChartPopupDetails(String projectId, String subProjectNo,
			String subProductLine, String docOwner, String docType, String docLevel, String milestoneCode,
			String milestoneStatus) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<VDRStatisticsOTDDetailsDTO> list = new ArrayList<VDRStatisticsOTDDetailsDTO>();
		try {
			list = iVendorDocRegisterDAO.getStatisticsOTDChartPopupDetails(projectId, subProjectNo, docOwner, docType,
					milestoneStatus);
			responseMap.put("data", list);
		} catch (Exception e) {
			log.error("getStatisticsOTDChartPopupDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadStatisticsOTDExcelDetails(String projectId, String subProjectNo, String subProductLine,
			String docOwner, String docType, String docLevel, String milestoneCode) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportStatisticsOTDDetailsExcel excelDTO = new ExportStatisticsOTDDetailsExcel();
		List<VDRStatisticsOTDDetailsDTO> list = new ArrayList<VDRStatisticsOTDDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			list = iVendorDocRegisterDAO.downloadStatisticsOTDExcelDetails(projectId, subProjectNo, docOwner, docType);
			log.info("Creating  Statistics OTD Excel Details Sheet with " + list.size() + " rows.");
			excelDTO.exportStatisticsOTDDetailsExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Statistics OTD Excel Details:: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Statistics OTD Excel Details:: " + e.getMessage());
			}
		}
		return excelData;
	}
}
