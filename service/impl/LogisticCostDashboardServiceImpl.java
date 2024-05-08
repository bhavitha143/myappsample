package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.LogisticCostDashboardDAO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.LCDashboardBPSDTO;
import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisDTO;
import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisDetailsDTO;
import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisFilterDTO;
import com.bh.realtrack.dto.LCDashboardDTO;
import com.bh.realtrack.dto.LCDashboardDetailedWorkloadDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisDetailsDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisFilterDTO;
import com.bh.realtrack.dto.LCDashboardWorkloadDTO;
import com.bh.realtrack.dto.LCSummaryDTO;
import com.bh.realtrack.dto.LegendColorDTO;
import com.bh.realtrack.excel.ExportLCDashboardDetailsExcel;
import com.bh.realtrack.service.ICommonService;
import com.bh.realtrack.service.LogisticCostDashboardService;

@Service
public class LogisticCostDashboardServiceImpl implements LogisticCostDashboardService {

	private static Logger log = LoggerFactory.getLogger(LogisticCostDashboardServiceImpl.class.getName());

	@Autowired
	private LogisticCostDashboardDAO logisticDAO;

	@Autowired
	private ICommonService commonService;

	@Override
	public Map<String, Object> getLCDashboardDropDowns(LCDashboardDTO paramList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> dropDown1 = new ArrayList<DropDownDTO>();
		List<DropDownDTO> dropDown2 = new ArrayList<DropDownDTO>();
		List<DropDownDTO> dropDown3 = new ArrayList<DropDownDTO>();
		List<DropDownDTO> dropDown4 = new ArrayList<DropDownDTO>();
		List<DropDownDTO> dropDown5 = new ArrayList<DropDownDTO>();
		List<DropDownDTO> dropDown6 = new ArrayList<DropDownDTO>();
		String projectList = "", defaultStr = "";
		String favProjects = commonService.fetchFavProjects();
		if (null == favProjects) {
			favProjects = "0";
		}
		try {
			paramList.setFavProjects(favProjects);
			projectList = logisticDAO.getDefaultProjectList(paramList);
			dropDown1 = logisticDAO.getLCPmLeaderDropDown(paramList);
			dropDown2 = logisticDAO.getLCSpmDropDown(paramList);
			if (projectList != "0") {
				dropDown3 = logisticDAO.getLCFinanceSegmentDropDown(paramList, projectList);
				dropDown4 = logisticDAO.getLCShippingManagerDropDown(paramList, projectList);
			}
			dropDown5 = logisticDAO.getLCRacDateHorizonDropDowns();
			dropDown6 = logisticDAO.getLCExWorksHorizonDropDowns();
			defaultStr = getDefaultValues();
			responseMap.put("pmLeader", dropDown1);
			responseMap.put("spm", dropDown2);
			responseMap.put("financialSegment", dropDown3);
			responseMap.put("shippingManager", dropDown4);
			responseMap.put("racDateHorizon", dropDown5);
			responseMap.put("exWorksHorizon", dropDown6);
			responseMap.put("racDateHorizonDefault", defaultStr);
			responseMap.put("exWorksHorizonDefault", defaultStr);
		} catch (Exception e) {
			log.error("getLCDashboardDropDowns(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	private String getDefaultValues() {
		LocalDate lDate = LocalDate.now();
		String defaultStr = "";
		defaultStr = String.valueOf(lDate.getYear());
		return defaultStr;
	}

	public static Map<String, String> getDates(String dateFilter) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM/yyyy");
		Map<String, String> dateMap = new HashMap<String, String>();
		String year = "", quarter = "", last3Yrs = "", startDate = "", endDate = "";
		Date date, date1;
		Calendar cal = Calendar.getInstance();
		LocalDate lDate = LocalDate.now();
		if (null != dateFilter && !dateFilter.isEmpty() && !dateFilter.equalsIgnoreCase("")) {
			if (dateFilter.contains("Q")) {
				String[] selectedQuarterArr = dateFilter.split("-");
				year = "20" + selectedQuarterArr[0];
				quarter = selectedQuarterArr[1];
			} else if (!dateFilter.contains("Q") && dateFilter.length() == 4) {
				startDate = "Jan/" + dateFilter;
				endDate = "Dec/" + dateFilter;
			} else if (dateFilter.equalsIgnoreCase("Last 3 Years")) {
				year = String.valueOf(lDate.getYear());
				last3Yrs = String.valueOf(lDate.minusYears(2).getYear());
				startDate = "Jan/" + last3Yrs;
				endDate = "Dec/" + year;
			} else {
				quarter = String.valueOf(lDate.get(IsoFields.QUARTER_OF_YEAR) + "Q");
				year = String.valueOf(lDate.getYear());
			}
			if (quarter.equalsIgnoreCase("1Q")) {
				startDate = "Jan/" + year;
				endDate = "Mar" + "/" + year;
			} else if (quarter.equalsIgnoreCase("2Q")) {
				startDate = "Apr/" + year;
				endDate = "Jun" + "/" + year;
			} else if (quarter.equalsIgnoreCase("3Q")) {
				startDate = "Jul/" + year;
				endDate = "Sep" + "/" + year;
			} else if (quarter.equalsIgnoreCase("4Q")) {
				startDate = "Oct/" + year;
				endDate = "Dec" + "/" + year;
			}
		}
		date = format2.parse(startDate);
		date1 = format2.parse(endDate);
		cal.setTime(date1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		startDate = format1.format(date);
		endDate = format1.format(cal.getTime());
		dateMap.put("startDate", startDate);
		dateMap.put("endDate", endDate);
		return dateMap;
	}

	@Override
	public Map<String, Object> getLCDashboardBoxPackingSummary(LCDashboardBPSDTO paramList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> volumePackedPieChart = new HashMap<String, Object>();
		Map<String, Object> volumePackedBySitePieChart = new HashMap<String, Object>();
		LCSummaryDTO summaryDTO = new LCSummaryDTO();
		String favProjects = commonService.fetchFavProjects();
		String projectList = "", startDate = "", endDate = "";
		if (null == favProjects) {
			favProjects = "0";
		}
		try {
			paramList.setFavProjects(favProjects);
			startDate = getDateFormat(paramList.getStartDate());
			endDate = getDateFormat(paramList.getEndDate());
			log.info("startDate : " + startDate + " :::: endDate : " + endDate);
			projectList = logisticDAO.getBoxPackingProjectList(paramList, startDate, endDate);
			if (projectList != "0") {
				summaryDTO = logisticDAO.getLCDashboardBoxPackingSummary(paramList, projectList);
				volumePackedPieChart = logisticDAO.getLCDashboardVolumePackedPieChart(paramList, projectList);
				volumePackedBySitePieChart = logisticDAO.getLCDashboardVolumePackedBySitePieChart(paramList,
						projectList);
			}
			responseMap.put("summary", summaryDTO);
			responseMap.put("volumeShippedPieChart", volumePackedPieChart);
			responseMap.put("volumePackedBySitePieChart", volumePackedBySitePieChart);
			responseMap.put("updatedOn", "");
		} catch (Exception e) {
			log.error("getLCDashboardBoxPackingSummary(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLCDashboardTransportationSummary(LCDashboardBPSDTO paramList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> volumeShippedPieChart = new HashMap<String, Object>();
		Map<String, Object> shipmentStatusPieChart = new HashMap<String, Object>();
		LCSummaryDTO summaryDTO = new LCSummaryDTO();
		String favProjects = commonService.fetchFavProjects();
		String projectList = "", startDate = "", endDate = "";
		if (null == favProjects) {
			favProjects = "0";
		}
		try {
			paramList.setFavProjects(favProjects);
			startDate = getDateFormat(paramList.getStartDate());
			endDate = getDateFormat(paramList.getEndDate());
			log.info("startDate : " + startDate + " :::: endDate : " + endDate);
			projectList = logisticDAO.getTransportationProjectList(paramList, startDate, endDate);
			if (projectList != "0") {
				summaryDTO = logisticDAO.getLCDashboardTransportationSummary(paramList, projectList);
				volumeShippedPieChart = logisticDAO.getLCDashboardVolumeShippedPieChart(paramList, projectList);
				shipmentStatusPieChart = logisticDAO.getLCDashboardShipmentStatusPieChart(projectList);
			}
			responseMap.put("summary", summaryDTO);
			responseMap.put("volumePackedPieChart", volumeShippedPieChart);
			responseMap.put("volumeShipmentStatusPieChart", shipmentStatusPieChart);
			responseMap.put("updatedOn", "");
		} catch (Exception e) {
			log.error("getLCDashboardTransportationSummary(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	private String getDateFormat(String inputDate) {
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/YYYY");
		if (null != inputDate) {
			Date date = null;
			try {
				date = format1.parse(inputDate);
				inputDate = format2.format(date).toUpperCase();
			} catch (ParseException e) {
				log.error("getDateFormat() ::" + e.getMessage());
			}
		}

		return inputDate;
	}

	@Override
	public Map<String, Object> getLCDashboardWorkloadDetails(LCDashboardBPSDTO paramList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<LCDashboardWorkloadDTO> workLoadList = new ArrayList<LCDashboardWorkloadDTO>();
		String favProjects = commonService.fetchFavProjects();
		String projectList = "", startDate = "", endDate = "";
		if (null == favProjects) {
			favProjects = "0";
		}
		try {
			paramList.setFavProjects(favProjects);
			projectList = logisticDAO.getWorkloadProjectList(paramList, startDate, endDate);
			if (projectList != "0") {
				workLoadList = logisticDAO.getLCDashboardWorkloadDetails(paramList, projectList);
			}
			responseMap.put("lcWorkloadDetails", workLoadList);
		} catch (Exception e) {
			log.error("getLCDashboardWorkloadDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadLCDashboardWorkloadDetails(LCDashboardBPSDTO paramList) {
		List<LCDashboardWorkloadDTO> workLoadList = new ArrayList<LCDashboardWorkloadDTO>();
		List<LCDashboardDetailedWorkloadDTO> detailedWorkloadList = new ArrayList<LCDashboardDetailedWorkloadDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExportLCDashboardDetailsExcel excelDTO = new ExportLCDashboardDetailsExcel();
		byte[] excelData = null;
		String favProjects = commonService.fetchFavProjects();
		String projectList = "", startDate = "", endDate = "";
		if (null == favProjects) {
			favProjects = "0";
		}
		try {
			paramList.setFavProjects(favProjects);
			projectList = logisticDAO.getWorkloadProjectList(paramList, startDate, endDate);

			if (projectList != "0") {
				workLoadList = logisticDAO.getLCDashboardWorkloadDetails(paramList, projectList);
			}
			log.info("Creating Workload Details Sheet with " + workLoadList.size() + " rows.");
			excelDTO.downloadLCDashboardWorkloadDetails(workbook, workLoadList);

			if (projectList != "0") {
				detailedWorkloadList = logisticDAO.getLCDashboardDetailedWorkloadDetails(paramList, projectList);
			}
			log.info("Creating Detailed Workload Details Sheet with " + detailedWorkloadList.size() + " rows.");
			excelDTO.downloadLCDashboardDetailedWorkloadDetails(workbook, detailedWorkloadList);

			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading LC Dashboard workload excel file :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading LC Dashboard workload excel file :: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getLCDashboardBPSiteAnalysisFilters(LCDashboardDTO paramList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> siteList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> packTypeList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> colorByList = new ArrayList<DropDownDTO>();
		DropDownDTO defaultDates = new DropDownDTO();
		String projectList = "", favProjects = "0";
		try {
			favProjects = commonService.fetchFavProjects();
			if (null == favProjects) {
				favProjects = "0";
			}
			paramList.setFavProjects(favProjects);
			projectList = logisticDAO.getDefaultProjectList(paramList);
			if (projectList != "0") {
				siteList = logisticDAO.getBPSiteAnalysisSiteDropDown(projectList);
				packTypeList = logisticDAO.getBPSiteAnalysisPackTypeDropDown(projectList);
				colorByList = logisticDAO.getBPSiteAnalysisColorByDropDown();
				defaultDates = logisticDAO.getBPSiteAnalysisDefaultDates();
			}
			responseMap.put("site", siteList);
			responseMap.put("packType", packTypeList);
			responseMap.put("colorBy", colorByList);
			responseMap.put("defaultColorBy", "SITE");
			responseMap.put("startDate", defaultDates.getKey());
			responseMap.put("endDate", defaultDates.getVal());
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardServiceImpl :: getLCDashboardBPSiteAnalysisFilters() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLCDashboardBPSiteAnalysisSummary(LCDashboardBPSiteAnalysisFilterDTO paramList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<LCDashboardBPSiteAnalysisDTO> list = new ArrayList<LCDashboardBPSiteAnalysisDTO>();
		List<LegendColorDTO> pieChartConfigList = new ArrayList<LegendColorDTO>();
		String projectList = "", startDate = "", endDate = "", chartType = "", colorBy = "";
		try {
			String favProjects = commonService.fetchFavProjects();
			if (null == favProjects) {
				favProjects = "0";
			}
			paramList.setFavProjects(favProjects);
			startDate = getDateFormat(paramList.getStartDate());
			endDate = getDateFormat(paramList.getEndDate());
			chartType = paramList.getChartType();
			colorBy = paramList.getColorBy();
			log.info("LogisticCostDashboardServiceImpl :: getLCDashboardBoxPackingSiteAnalysis() :: startDate : "
					+ startDate + " :::: endDate : " + endDate + " :::: chartType : " + chartType + " :::: colorBy : "
					+ colorBy);
			projectList = logisticDAO.getBPSiteAnalysisSummaryProjectList(paramList, startDate, endDate);
			if (projectList != "0") {
				list = logisticDAO.getBPSiteAnalysisChartSummary(projectList, paramList, startDate, endDate, chartType,
						colorBy);
				pieChartConfigList = logisticDAO.getLCDashboardChartConfiguration(colorBy);
			}
			responseMap.put("list", list);
			responseMap.put("config", pieChartConfigList);
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardServiceImpl :: getLCDashboardBoxPackingSiteAnalysis() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadLCDashboardBPSiteAnalysisDetails(LCDashboardBPSiteAnalysisFilterDTO paramList) {
		List<LCDashboardBPSiteAnalysisDetailsDTO> list = new ArrayList<LCDashboardBPSiteAnalysisDetailsDTO>();
		ExportLCDashboardDetailsExcel excelDTO = new ExportLCDashboardDetailsExcel();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		String projectList = "", startDate = "", endDate = "";
		try {
			String favProjects = commonService.fetchFavProjects();
			if (null == favProjects) {
				favProjects = "0";
			}
			paramList.setFavProjects(favProjects);
			startDate = getDateFormat(paramList.getStartDate());
			endDate = getDateFormat(paramList.getEndDate());
			projectList = logisticDAO.getBPSiteAnalysisSummaryProjectList(paramList, startDate, endDate);
			if (projectList != "0") {
				list = logisticDAO.getBPSiteAnalysisDetails(projectList, startDate, endDate);
			}
			log.info("Creating B&P Site Analysis Sheet with " + list.size() + " rows.");
			excelDTO.downloadLCDashboardBPSiteAnalysisDetails(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardServiceImpl :: downloadLCDashboardBPSiteAnalysisDetails() :: "
					+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error(
						"Exception in LogisticCostDashboardServiceImpl :: downloadLCDashboardBPSiteAnalysisDetails() :: "
								+ e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getLCDashboardTransportationAnalysisFilters(LCDashboardDTO paramList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> regionList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> incotermsList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> colorByList = new ArrayList<DropDownDTO>();
		DropDownDTO defaultDates = new DropDownDTO();
		String projectList = "", favProjects = "0";
		try {
			favProjects = commonService.fetchFavProjects();
			if (null == favProjects) {
				favProjects = "0";
			}
			paramList.setFavProjects(favProjects);
			projectList = logisticDAO.getDefaultProjectList(paramList);
			if (projectList != "0") {
				regionList = logisticDAO.getTransportationAnalysisRegionDropDown(projectList);
				incotermsList = logisticDAO.getTransportationAnalysisIncotermsDropDown(projectList);
				colorByList = logisticDAO.getTransportationAnalysisColorByDropDown();
				defaultDates = logisticDAO.getTransportationAnalysisDefaultDates();
			}
			responseMap.put("region", regionList);
			responseMap.put("incoterms", incotermsList);
			responseMap.put("colorBy", colorByList);
			responseMap.put("defaultColorBy", "REGION");
			responseMap.put("startDate", defaultDates.getKey());
			responseMap.put("endDate", defaultDates.getVal());
		} catch (Exception e) {
			log.error(
					"Exception in LogisticCostDashboardServiceImpl :: getLCDashboardTransportationAnalysisFilters() :: "
							+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLCDashboardTransportationAnalysisSummary(
			LCDashboardTransportationAnalysisFilterDTO paramList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<LCDashboardTransportationAnalysisDTO> list = new ArrayList<LCDashboardTransportationAnalysisDTO>();
		List<LegendColorDTO> pieChartConfigList = new ArrayList<LegendColorDTO>();
		String projectList = "", startDate = "", endDate = "", chartType = "", colorBy = "";
		try {
			String favProjects = commonService.fetchFavProjects();
			if (null == favProjects) {
				favProjects = "0";
			}
			paramList.setFavProjects(favProjects);
			startDate = getDateFormat(paramList.getStartDate());
			endDate = getDateFormat(paramList.getEndDate());
			chartType = paramList.getChartType();
			colorBy = paramList.getColorBy();
			log.info("LogisticCostDashboardServiceImpl :: getLCDashboardTransportationAnalysisSummary() :: startDate : "
					+ startDate + " :::: endDate : " + endDate + " :::: chartType : " + chartType + " :::: colorBy : "
					+ colorBy);
			projectList = logisticDAO.getLCDashboardTransportationAnalysisSummaryProjectList(paramList, startDate,
					endDate);
			if (projectList != "0") {
				list = logisticDAO.getTransportationAnalysisChartSummary(projectList, paramList, startDate, endDate,
						chartType, colorBy);
				pieChartConfigList = logisticDAO.getLCDashboardChartConfiguration(colorBy);
			}
			responseMap.put("list", list);
			responseMap.put("config", pieChartConfigList);
		} catch (Exception e) {
			log.error(
					"Exception in LogisticCostDashboardServiceImpl :: getLCDashboardTransportationAnalysisSummary() :: "
							+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadLCDashboardTransportationAnalysisDetails(
			LCDashboardTransportationAnalysisFilterDTO paramList) {
		List<LCDashboardTransportationAnalysisDetailsDTO> list = new ArrayList<LCDashboardTransportationAnalysisDetailsDTO>();
		ExportLCDashboardDetailsExcel excelDTO = new ExportLCDashboardDetailsExcel();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		String projectList = "", startDate = "", endDate = "";
		try {
			String favProjects = commonService.fetchFavProjects();
			if (null == favProjects) {
				favProjects = "0";
			}
			paramList.setFavProjects(favProjects);
			startDate = getDateFormat(paramList.getStartDate());
			endDate = getDateFormat(paramList.getEndDate());
			projectList = logisticDAO.getLCDashboardTransportationAnalysisSummaryProjectList(paramList, startDate,
					endDate);
			if (projectList != "0") {
				list = logisticDAO.getTransportationAnalysisDetails(projectList, startDate, endDate);
			}
			log.info("Creating Transportation Analysis Sheet with " + list.size() + " rows.");
			excelDTO.downloadLCDashboardTransportationAnalysisDetails(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error(
					"Exception in LogisticCostDashboardServiceImpl :: downloadLCDashboardTransportationAnalysisDetails() :: "
							+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error(
						"Exception in LogisticCostDashboardServiceImpl :: downloadLCDashboardTransportationAnalysisDetails() :: "
								+ e.getMessage());
			}
		}
		return excelData;
	}

}
