package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.controller.NPIProjectDetailsController;
import com.bh.realtrack.dao.INPIProjectDetailsDAO;
import com.bh.realtrack.dao.impl.NPIProjectDetailsDAOImpl;
import com.bh.realtrack.dto.NPIDetailsPopupDataResponse;
import com.bh.realtrack.dto.NPIDropdownResponseDTO;
import com.bh.realtrack.dto.NPIOtdTrendDTO;
import com.bh.realtrack.dto.NPIProjectDetailsDTO;
import com.bh.realtrack.dto.NPISummaryDetailsResponseDTO;
import com.bh.realtrack.excel.ExportNPIProjectDetailsExcel;
import com.bh.realtrack.service.INPIProjectDetailsService;

@Service
public class NPIProjectDetailsServiceImpl implements INPIProjectDetailsService {

	@Autowired
	INPIProjectDetailsDAO npiProjectDetailsDao;
	private static final Logger log = LoggerFactory.getLogger(NPIProjectDetailsServiceImpl.class);


	@Override
	public NPIDropdownResponseDTO getNPIDropDownDetails(String projectId) throws Exception {
		return npiProjectDetailsDao.getNPIDropDownDetails(projectId);
	}

	@Override
	public NPISummaryDetailsResponseDTO getNPISummaryDetails(String projectId, List<String> subProjects,
			List<String> owners, List<String> activityGroups) throws Exception {
		return npiProjectDetailsDao.getNPISummaryDetails(projectId, subProjects, owners, activityGroups);
	}

	@Override
	public Map<String, Object> getNPIDetailsPopupData(String projectId, String chartType, String status, String fwWeek,
			List<String> subProjects, List<String> owners, List<String> activityGroups) throws Exception {

		Map<String, Object> responseMap = new HashMap<String, Object>();

		if (chartType.equalsIgnoreCase("BAR") && (status.equalsIgnoreCase("BACKLOG") || status.equalsIgnoreCase("TOGO")
				|| status.equalsIgnoreCase("RELEASED") || status.equalsIgnoreCase("NOTPLANNED") )) {
			String fiscalWeek = npiProjectDetailsDao.getFiscalWeek();
			if (fiscalWeek != null && !fiscalWeek.isEmpty()) {
				if (!fwWeek.isEmpty() && fwWeek != null) {
					if (fiscalWeek.equalsIgnoreCase(fwWeek)) {
						List<NPIDetailsPopupDataResponse> popUpDataDetails = npiProjectDetailsDao
								.getNPIDetailsPopupData(projectId, chartType, status, fwWeek, subProjects, owners,
										activityGroups);
						responseMap.put("popupDetails", popUpDataDetails);
						responseMap.put("showErrorMsg", "N");
						return responseMap;
					} else {
						responseMap.put("popupDetails", null);
						responseMap.put("showErrorMsg", "Y");
						return responseMap;
					}

				} else {
					log.error("Fiscal week in input is empty or null" + fwWeek);
				}
			} else {
				log.error("Fiscal week received from backend is empty" + fiscalWeek);
			}
		} else {
			List<NPIDetailsPopupDataResponse> popUpDataDetails = npiProjectDetailsDao.getNPIDetailsPopupData(projectId,
					chartType, status, fwWeek, subProjects, owners, activityGroups);
			responseMap.put("popupDetails", popUpDataDetails);
			responseMap.put("showErrorMsg", "N");
			return responseMap;
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getNPIOTDTrendDetails(String projectId, List<String> subProject, List<String> owner,
			List<String> activityGroup, String startDate, String endDate) throws Exception {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<NPIOtdTrendDTO> otdTrendDetails = npiProjectDetailsDao.getNPIOTDTrendDetails(projectId, subProject, owner,
				activityGroup, startDate, endDate);
		String lastUpdatedOn = npiProjectDetailsDao.getUpdatedOnForChartData(projectId);
		responseMap.put("chartData", otdTrendDetails);
		responseMap.put("updatedOn", lastUpdatedOn);
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] getNPIDetailsExcel(String projectId, String subProject, String owner, String activityGroup) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportNPIProjectDetailsExcel downloadNPIDetails = new ExportNPIProjectDetailsExcel();
		List<NPIProjectDetailsDTO> excelDetails = new ArrayList<NPIProjectDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			excelDetails = npiProjectDetailsDao.getNPIDetailsExcel(projectId, subProject, owner, activityGroup);
			List<NPIProjectDetailsDTO> bomExcelDetails = excelDetails.stream()
					.filter(dto -> "BOM".equals(dto.getActivityGroupDisplay())).collect(Collectors.toList());
			log.info("Creating BOM Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = downloadNPIDetails.downloadNPIDetails(workbook, bomExcelDetails, "BOM");
			
			List<NPIProjectDetailsDTO> intDocExcelDetails = excelDetails.stream()
					.filter(dto -> "Internal Doc".equals(dto.getActivityGroupDisplay())).collect(Collectors.toList());
			log.info("Creating Internal Doc Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = downloadNPIDetails.downloadNPIDetails(workbook, intDocExcelDetails, "Internal Doc");
			
			List<NPIProjectDetailsDTO> phaseAExcelDetails = excelDetails.stream()
					.filter(dto -> "Phase A".equals(dto.getActivityGroupDisplay())).collect(Collectors.toList());
			log.info("Creating Phase A Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = downloadNPIDetails.downloadNPIDetails(workbook, phaseAExcelDetails, "Phase A");
			
			List<NPIProjectDetailsDTO> phaseCExcelDetails = excelDetails.stream()
					.filter(dto -> "Phase C".equals(dto.getActivityGroupDisplay())).collect(Collectors.toList());
			log.info("Creating Phase C Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = downloadNPIDetails.downloadNPIDetails(workbook, phaseCExcelDetails, "Phase C");

			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading NPI Project Details" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading NPI Project Details " + e.getMessage());
			}
		}
		return excelData;
	}

}
