package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IMaterialManagementDAO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.MMMoveOrderPopupDetailsDTO;
import com.bh.realtrack.dto.MMPackingFDChartDetailsDTO;
import com.bh.realtrack.dto.MMPackingFDPopupDetailsDTO;
import com.bh.realtrack.dto.MMPackingFDSummaryDetailsDTO;
import com.bh.realtrack.dto.MMPickingChartDetailsDTO;
import com.bh.realtrack.dto.MMPickingPopupDetailsDTO;
import com.bh.realtrack.dto.MMPickingSummaryDetailsDTO;
import com.bh.realtrack.dto.MMReceivingChartDetailsDTO;
import com.bh.realtrack.dto.MMReceivingPopupDetailsDTO;
import com.bh.realtrack.dto.MMReceivingSummaryDetailsDTO;
import com.bh.realtrack.dto.MMWfAnomalyPopupDetailsDTO;
import com.bh.realtrack.dto.MMWfAnomalySummaryDetailsDTO;
import com.bh.realtrack.excel.ExportMaterialManagementExcel;
import com.bh.realtrack.service.IMaterialManagementService;
import com.bh.realtrack.util.MaterialManagementConstants;

/**
 * @author Shweta D. Sawant
 */
@Service
public class MaterialManagementServiceImpl implements IMaterialManagementService {

	@Autowired
	private IMaterialManagementDAO iMaterialManagementDAO;
	private static final Logger log = LoggerFactory.getLogger(MaterialManagementServiceImpl.class.getName());

	@Override
	public Map<String, Object> getMMReceivingDropdownDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> subProjectFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> organizationFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> locationFilter = new ArrayList<DropDownDTO>();
		try {
			subProjectFilter = iMaterialManagementDAO.getMMReceivingSubProjectFilter(projectId);
			organizationFilter = iMaterialManagementDAO.getMMReceivingOrganizationFilter(projectId);
			locationFilter = iMaterialManagementDAO.getMMReceivingLocationFilter(projectId);
			responseMap.put("subProject", subProjectFilter);
			responseMap.put("organization", organizationFilter);
			responseMap.put("location", locationFilter);
			responseMap.put("startDate", getDefaultDatesForQuarter("Start"));
			responseMap.put("endDate", getDefaultDatesForQuarter("End"));
		} catch (Exception e) {
			log.error("getMMReceivingDropdownDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMReceivingSummaryDetails(String projectId, String subProject, String organization,
			String location) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String updateOnDate = "";
		MMReceivingSummaryDetailsDTO summaryDTO = new MMReceivingSummaryDetailsDTO();
		try {
			summaryDTO = iMaterialManagementDAO.getMMReceivingCountDetails(projectId, subProject, organization,
					location);
			updateOnDate = iMaterialManagementDAO.getLastUpdatedDate(projectId);
			responseMap.put("totalMaterialNeeded", summaryDTO.getTotal());
			responseMap.put("materialsArrived", summaryDTO.getMaterialArrived());
			responseMap.put("onInventory", summaryDTO.getOnInventory());
			responseMap.put("onReceivingArea", summaryDTO.getOnReceivingArea());
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getMMReceivingSummaryDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMReceivingPieChartDetails(String projectId, String subProject, String organization,
			String location) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			Map<String, Object> agingPieChart = new HashMap<String, Object>();
			Map<String, Object> wfAnomalyPieChart = new HashMap<String, Object>();
			agingPieChart = iMaterialManagementDAO.getMMReceivingAgingPieChart(projectId, subProject, organization,
					location);
			wfAnomalyPieChart = iMaterialManagementDAO.getMMReceivingWfAnomalyPieChart(projectId, subProject,
					organization, location);
			responseMap.put("agingChart", agingPieChart);
			responseMap.put("wfAnomalyChart", wfAnomalyPieChart);
		} catch (Exception e) {
			log.error("getMMReceivingPieChartDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMReceivingDetailsPopupData(String projectId, String subProject, String organization,
			String location, String type, String startDate, String endDate, String fiscalWeek) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<MMReceivingPopupDetailsDTO> receivingDetailsPopup = new ArrayList<MMReceivingPopupDetailsDTO>();
		try {
			if (type.equalsIgnoreCase("CYCLE_TIME_CHART_WEEKLY")) {
				dateMap = getDateMap(startDate, endDate);
			}
			receivingDetailsPopup = iMaterialManagementDAO.getMMReceivingPopupDetails(projectId, subProject,
					organization, location, type, dateMap.get("startDate"), dateMap.get("endDate"), fiscalWeek);
			responseMap.put("popup", receivingDetailsPopup);
		} catch (Exception e) {
			log.error("getMMReceivingDetailsPopupData(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMReceivingChartDetails(String projectId, String subProject, String organization,
			String location, String startDate, String endDate, String weekOrMonth) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<MMReceivingChartDetailsDTO> chartDetails = new ArrayList<MMReceivingChartDetailsDTO>();
		try {
			dateMap = getDateMap(startDate, endDate);
			chartDetails = iMaterialManagementDAO.getMMReceivingChartDetails(projectId, subProject, organization,
					location, dateMap.get("startDate"), dateMap.get("endDate"), weekOrMonth);
			responseMap.put("chart", chartDetails);
		} catch (Exception e) {
			log.error("getMMReceivingChartDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] getMMReceivingDetailsExcel(String projectId, String subProject, String organization,
			String location) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportMaterialManagementExcel exportMMReceivingDetailsExcel = new ExportMaterialManagementExcel();
		List<MMReceivingPopupDetailsDTO> excelDetails = new ArrayList<MMReceivingPopupDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			excelDetails = iMaterialManagementDAO.getMMReceivingDetailsExcel(projectId, subProject, organization,
					location);
			log.info("Creating Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = exportMMReceivingDetailsExcel.exportMMReceivingDetailsExcel(workbook, excelDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : Receiving - Excel file :: "
					+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Material Management : Receiving - Excel file :: "
						+ e.getMessage());
			}
		}
		return excelData;
	}

	public static Map<String, String> getDateMap(String startDate, String endDate) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat("MMM-yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		Date startDt = null, endDt = null;
		Map<String, String> dateMap = new HashMap<String, String>();
		if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
			startDt = inputFormat.parse(startDate);
			endDt = inputFormat.parse(endDate);
			cal.setTime(endDt);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
			throw new Exception("No Date selected: ");
		}
		dateMap.put("startDate", outputFormat.format(startDt));
		dateMap.put("endDate", outputFormat.format(cal.getTime()));
		return dateMap;
	}

	public static String getDefaultDatesForQuarter(String flag) {
		SimpleDateFormat outputFormat = new SimpleDateFormat("MMM-yyyy");
		Calendar cal = Calendar.getInstance();
		try {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			if (flag.equalsIgnoreCase("Start")) {
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
			} else if (flag.equalsIgnoreCase("End")) {
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2);
			}
		} catch (Exception e) {
			log.error("getDefaultDatesForQuarter(): Exception occurred : " + e.getMessage());
		}
		return outputFormat.format(cal.getTime()).toString();
	}

	@Override
	public Map<String, Object> getMMCheckApplicableFlag(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String flag = "", message = "";
		try {
			flag = iMaterialManagementDAO.checkIfProjectIsModule(projectId);
			responseMap.put("flag", flag);
			if (flag.equalsIgnoreCase("Not Applicable")) {
				message = "This section is only applicable for module projects";
			}
			responseMap.put("message", message);
		} catch (Exception e) {
			log.error("getMMCheckApplicableFlag(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMPickingDropdownDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> subProjectFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> subInvFromFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> subInvToFilter = new ArrayList<DropDownDTO>();
		try {
			subProjectFilter = iMaterialManagementDAO.getMMPickingSubProjectFilter(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_PICKING);
			subInvFromFilter = iMaterialManagementDAO.getMMPickingSubInvFromFilter(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_PICKING);
			subInvToFilter = iMaterialManagementDAO.getMMPickingSubInvToFilter(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_PICKING);
			responseMap.put("startDate", getDefaultDatesForQuarter("Start"));
			responseMap.put("endDate", getDefaultDatesForQuarter("End"));
			responseMap.put("subProject", subProjectFilter);
			responseMap.put("subInvFrom", subInvFromFilter);
			responseMap.put("subInvTo", subInvToFilter);
		} catch (Exception e) {
			log.error("getMMPickingDropdownDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMPickingSummaryDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String updateOnDate = "";
		MMPickingSummaryDetailsDTO summaryDTO = new MMPickingSummaryDetailsDTO();
		try {
			summaryDTO = iMaterialManagementDAO.getMMPickingSummaryDetails(projectId, subProject, subInvFrom, subInvTo,
					MaterialManagementConstants.MOVE_ORDER_TYPE_PICKING);
			updateOnDate = iMaterialManagementDAO.getLastUpdatedDate(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_PICKING);
			responseMap.put("summary", summaryDTO);
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getMMPickingSummaryDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMPickingBacklogTrendChartDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String startDate, String endDate) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		String updateOnDate = "";
		List<MMPickingChartDetailsDTO> chartDetails = new ArrayList<MMPickingChartDetailsDTO>();
		try {
			dateMap = getDateMap(startDate, endDate);
			chartDetails = iMaterialManagementDAO.getMMPickingChartDetails(projectId, subProject, subInvFrom, subInvTo,
					dateMap.get("startDate"), dateMap.get("endDate"),
					MaterialManagementConstants.MOVE_ORDER_TYPE_PICKING);
			updateOnDate = iMaterialManagementDAO.getLastUpdatedDate(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_PICKING);
			responseMap.put("chart", chartDetails);
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getMMPickingBacklogTrendChartDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMPickingPopupDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String type, String startDate, String endDate, String fiscalWeek) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<MMPickingPopupDetailsDTO> pickingDetailsPopup = new ArrayList<MMPickingPopupDetailsDTO>();
		try {
			if (type.equalsIgnoreCase("BAR_CHART_RED") || type.equalsIgnoreCase("BAR_CHART_GREEN")) {
				dateMap = getDateMap(startDate, endDate);
				pickingDetailsPopup = iMaterialManagementDAO.getMMPickingChartPopupDetails(projectId, subProject,
						subInvFrom, subInvTo, type, dateMap.get("startDate"), dateMap.get("endDate"), fiscalWeek);
			} else if (type.equalsIgnoreCase("TOT_MOVE_ORDER") || type.equalsIgnoreCase("ISSUED")
					|| type.equalsIgnoreCase("LOADED") || type.equalsIgnoreCase("DROPPED")) {
				pickingDetailsPopup = iMaterialManagementDAO.getMMPickingSummaryPopupDetails(projectId, subProject,
						subInvFrom, subInvTo, type);
			}
			responseMap.put("popup", pickingDetailsPopup);
		} catch (Exception e) {
			log.error("getMMPickingPopupDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] exportMMPickingDetailsExcel(String projectId, String subProject, String subInvFrom, String subInvTo) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportMaterialManagementExcel exportMMPickingDetailsExcel = new ExportMaterialManagementExcel();
		List<MMPickingPopupDetailsDTO> excelDetails = new ArrayList<MMPickingPopupDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			excelDetails = iMaterialManagementDAO.getMMPickingDetailsExcel(projectId, subProject, subInvFrom, subInvTo);
			log.info("Creating Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = exportMMPickingDetailsExcel.exportMMPickingDetailsExcel(workbook, excelDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : Picking Tab - Excel file :: "
					+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Material Management : Picking Tab - Excel file :: "
						+ e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getMMMoveOrderDropdownDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> subProjectFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> subInvFromFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> subInvToFilter = new ArrayList<DropDownDTO>();
		try {
			subProjectFilter = iMaterialManagementDAO.getMMPickingSubProjectFilter(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_MOVE_ORDER_TRANSFER);
			subInvFromFilter = iMaterialManagementDAO.getMMPickingSubInvFromFilter(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_MOVE_ORDER_TRANSFER);
			subInvToFilter = iMaterialManagementDAO.getMMPickingSubInvToFilter(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_MOVE_ORDER_TRANSFER);
			responseMap.put("startDate", getDefaultDatesForQuarter("Start"));
			responseMap.put("endDate", getDefaultDatesForQuarter("End"));
			responseMap.put("subProject", subProjectFilter);
			responseMap.put("subInvFrom", subInvFromFilter);
			responseMap.put("subInvTo", subInvToFilter);
		} catch (Exception e) {
			log.error("getMMMoveOrderDropdownDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMMoveOrderSummaryDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String updateOnDate = "";
		MMPickingSummaryDetailsDTO summaryDTO = new MMPickingSummaryDetailsDTO();
		try {
			summaryDTO = iMaterialManagementDAO.getMMPickingSummaryDetails(projectId, subProject, subInvFrom, subInvTo,
					MaterialManagementConstants.MOVE_ORDER_TYPE_MOVE_ORDER_TRANSFER);
			updateOnDate = iMaterialManagementDAO.getLastUpdatedDate(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_MOVE_ORDER_TRANSFER);
			responseMap.put("summary", summaryDTO);
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getMMMoveOrderSummaryDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMMoveOrderBacklogTrendChartDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String startDate, String endDate) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		String updateOnDate = "";
		List<MMPickingChartDetailsDTO> chartDetails = new ArrayList<MMPickingChartDetailsDTO>();
		try {
			dateMap = getDateMap(startDate, endDate);
			chartDetails = iMaterialManagementDAO.getMMPickingChartDetails(projectId, subProject, subInvFrom, subInvTo,
					dateMap.get("startDate"), dateMap.get("endDate"),
					MaterialManagementConstants.MOVE_ORDER_TYPE_MOVE_ORDER_TRANSFER);
			updateOnDate = iMaterialManagementDAO.getLastUpdatedDate(projectId,
					MaterialManagementConstants.MOVE_ORDER_TYPE_MOVE_ORDER_TRANSFER);
			responseMap.put("chart", chartDetails);
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getMMMoveOrderBacklogTrendChartDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMMoveOrderPopupDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String type, String startDate, String endDate, String fiscalWeek) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<MMMoveOrderPopupDetailsDTO> pickingDetailsPopup = new ArrayList<MMMoveOrderPopupDetailsDTO>();
		try {
			if (type.equalsIgnoreCase("BAR_CHART_RED") || type.equalsIgnoreCase("BAR_CHART_GREEN")) {
				dateMap = getDateMap(startDate, endDate);
				pickingDetailsPopup = iMaterialManagementDAO.getMMMoveOrderChartPopupDetails(projectId, subProject,
						subInvFrom, subInvTo, type, dateMap.get("startDate"), dateMap.get("endDate"), fiscalWeek);
			} else if (type.equalsIgnoreCase("TOT_MOVE_ORDER") || type.equalsIgnoreCase("ISSUED")
					|| type.equalsIgnoreCase("LOADED") || type.equalsIgnoreCase("DROPPED")) {
				pickingDetailsPopup = iMaterialManagementDAO.getMMMoveOrderSummaryPopupDetails(projectId, subProject,
						subInvFrom, subInvTo, type);
			}
			responseMap.put("popup", pickingDetailsPopup);
		} catch (Exception e) {
			log.error("getMMMoveOrderPopupDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] exportMMMoveOrderDetailsExcel(String projectId, String subProject, String subInvFrom,
			String subInvTo) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportMaterialManagementExcel exportMMMoveOrderDetailsExcel = new ExportMaterialManagementExcel();
		List<MMMoveOrderPopupDetailsDTO> excelDetails = new ArrayList<MMMoveOrderPopupDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			excelDetails = iMaterialManagementDAO.getMMMoveOrderDetailsExcel(projectId, subProject, subInvFrom,
					subInvTo);
			log.info("Creating Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = exportMMMoveOrderDetailsExcel.exportMMMoveOrderDetailsExcel(workbook, excelDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : Move Order Transfer Tab - Excel file :: "
					+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error(
						"Error occured while downloading Material Management : Move Order Transfer Tab - Excel file :: "
								+ e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getWFAnoDropDownDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> ownershipFilter = new ArrayList<DropDownDTO>();
		try {
			ownershipFilter = iMaterialManagementDAO.getWFAnoOwnershipFilter(projectId);
			responseMap.put("ownership", ownershipFilter);
		} catch (Exception e) {
			log.error("getWFAnoDropDownDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getWFAnoSummaryDetails(String projectId, String ownership) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String updateOnDate = "";
		MMWfAnomalySummaryDetailsDTO summaryDTO = new MMWfAnomalySummaryDetailsDTO();
		try {
			summaryDTO = iMaterialManagementDAO.getWFAnoCountDetails(projectId, ownership);
			updateOnDate = iMaterialManagementDAO.getWfAnoLastUpdatedDate(projectId);
			responseMap.put("total", summaryDTO.getTotal());
			responseMap.put("open", summaryDTO.getOpen());
			responseMap.put("closed", summaryDTO.getClosed());
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getWFAnoSummaryDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getWFAnoChartDetails(String projectId, String ownership) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			Map<String, Object> cycleChart = new HashMap<String, Object>();
			Map<String, Object> agingChart = new HashMap<String, Object>();
			cycleChart = iMaterialManagementDAO.getWFAnoCycleChartDetails(projectId, ownership);
			agingChart = iMaterialManagementDAO.getWFAnoAgingChartDetails(projectId, ownership);
			responseMap.put("cycleChart", cycleChart);
			responseMap.put("agingChart", agingChart);
		} catch (Exception e) {
			log.error("getWFAnoChartDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getWFAnoPopupDetails(String projectId, String ownership, String chartType,
			String status) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<MMWfAnomalyPopupDetailsDTO> wfAnoDetailsPopup = new ArrayList<MMWfAnomalyPopupDetailsDTO>();
		List<MMWfAnomalyPopupDetailsDTO> openDetails = new ArrayList<MMWfAnomalyPopupDetailsDTO>();
		List<MMWfAnomalyPopupDetailsDTO> closedDetails = new ArrayList<MMWfAnomalyPopupDetailsDTO>();
		try {
			if (chartType.equalsIgnoreCase("SUMMARY") && status.equalsIgnoreCase("TOTAL")) {
				openDetails = iMaterialManagementDAO.getWfAnoDetailsExcel(projectId, ownership, chartType, "OPEN");
				closedDetails = iMaterialManagementDAO.getWfAnoDetailsExcel(projectId, ownership, chartType, "CLOSED");
				if (null != openDetails && openDetails.size() != 0) {
					wfAnoDetailsPopup.addAll(openDetails);
				}
				if (null != closedDetails && closedDetails.size() != 0) {
					wfAnoDetailsPopup.addAll(closedDetails);
				}
			} else {
				wfAnoDetailsPopup = iMaterialManagementDAO.getWFAnoPopupDetails(projectId, ownership, chartType,
						status);
			}
			responseMap.put("popup", wfAnoDetailsPopup);
		} catch (Exception e) {
			log.error("getWFAnoPopupDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] exportWFAnoExcelDetailsExcel(String projectId, String ownership) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportMaterialManagementExcel exportWfAnoDetailsExcel = new ExportMaterialManagementExcel();
		List<MMWfAnomalyPopupDetailsDTO> excelDetails = new ArrayList<MMWfAnomalyPopupDetailsDTO>();
		List<MMWfAnomalyPopupDetailsDTO> openDetails = new ArrayList<MMWfAnomalyPopupDetailsDTO>();
		List<MMWfAnomalyPopupDetailsDTO> closedDetails = new ArrayList<MMWfAnomalyPopupDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			openDetails = iMaterialManagementDAO.getWfAnoDetailsExcel(projectId, ownership, "EXCEL", "OPEN");
			closedDetails = iMaterialManagementDAO.getWfAnoDetailsExcel(projectId, ownership, "EXCEL", "CLOSED");
			if (null != openDetails && openDetails.size() != 0) {
				excelDetails.addAll(openDetails);
			}
			if (null != closedDetails && closedDetails.size() != 0) {
				excelDetails.addAll(closedDetails);
			}
			log.info("Creating Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = exportWfAnoDetailsExcel.exportWFAnoDetailsExcel(workbook, excelDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : WF Anomaly Tab - Excel file :: "
					+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Material Management : WF Anomaly Tab - Excel file :: "
						+ e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getPackingFDDropdownDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> subProjectFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> subInvFromFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> subInvToFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> ownershipFilter = new ArrayList<DropDownDTO>();
		try {
			subProjectFilter = iMaterialManagementDAO.getPackingFDSubProjectFilter(projectId);
			subInvFromFilter = iMaterialManagementDAO.getPackingFDSubInvFromFilter(projectId);
			subInvToFilter = iMaterialManagementDAO.getPackingFDSubInvToFilter(projectId);
			ownershipFilter = iMaterialManagementDAO.getPackingFDOwnershipFilter(projectId);
			responseMap.put("startDate", getDefaultDatesForQuarter("Start"));
			responseMap.put("endDate", getDefaultDatesForQuarter("End"));
			responseMap.put("subProject", subProjectFilter);
			responseMap.put("subInvFrom", subInvFromFilter);
			responseMap.put("subInvTo", subInvToFilter);
			responseMap.put("ownership", ownershipFilter);
		} catch (Exception e) {
			log.error("getPackingFDDropdownDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMPackingFDSummaryDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String ownership) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String updateOnDate = "";
		MMPackingFDSummaryDetailsDTO summaryDTO = new MMPackingFDSummaryDetailsDTO();
		try {
			summaryDTO = iMaterialManagementDAO.getPackingFDCountDetails(projectId, subProject, subInvFrom, subInvTo,
					ownership);
			updateOnDate = iMaterialManagementDAO.getPackingFDLastUpdatedDate(projectId);
			responseMap.put("totalMoveOrder", summaryDTO.getTotalMoveOrder());
			responseMap.put("issued", summaryDTO.getIssued());
			responseMap.put("loaded", summaryDTO.getLoaded());
			responseMap.put("dropped", summaryDTO.getDropped());
			responseMap.put("packed", summaryDTO.getPacked());
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getMMPackingFDSummaryDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMPackingFDChartDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String ownership, String startDate, String endDate) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		String updateOnDate = "";
		List<MMPackingFDChartDetailsDTO> chartDetails = new ArrayList<MMPackingFDChartDetailsDTO>();
		try {
			dateMap = getDateMap(startDate, endDate);
			chartDetails = iMaterialManagementDAO.getMMPackingFDChartDetails(projectId, subProject, subInvFrom,
					subInvTo, ownership, dateMap.get("startDate"), dateMap.get("endDate"));
			updateOnDate = iMaterialManagementDAO.getPackingFDLastUpdatedDate(projectId);
			responseMap.put("chart", chartDetails);
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getMMPackingFDChartDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMMPackingFDPopupDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String ownership, String chartType, String status, String startDate, String endDate,
			String fiscalWeek) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<MMPackingFDPopupDetailsDTO> packingDetailsPopup = new ArrayList<MMPackingFDPopupDetailsDTO>();
		try {
			if (chartType.equalsIgnoreCase("BAR_CHART")) {
				dateMap = getDateMap(startDate, endDate);
				packingDetailsPopup = iMaterialManagementDAO.getMMPackingFDChartPopupDetails(projectId, subProject,
						subInvFrom, subInvTo, ownership, chartType, status, dateMap.get("startDate"),
						dateMap.get("endDate"), fiscalWeek);
			} else if (chartType.equalsIgnoreCase("SUMMARY")) {
				packingDetailsPopup = iMaterialManagementDAO.getMMPackingFDSummaryPopupDetails(projectId, subProject,
						subInvFrom, subInvTo, ownership, chartType, status);
			}
			responseMap.put("popup", packingDetailsPopup);
		} catch (Exception e) {
			log.error("getMMPackingFDPopupDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] exportMMPackingFDDetailsExcel(String projectId, String subProject, String subInvFrom, String subInvTo,
			String ownership) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportMaterialManagementExcel exportMMPackingDetailsExcel = new ExportMaterialManagementExcel();
		List<MMPackingFDPopupDetailsDTO> excelDetails = new ArrayList<MMPackingFDPopupDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			excelDetails = iMaterialManagementDAO.getMMPackingFDDetailsExcel(projectId, subProject, subInvFrom,
					subInvTo, ownership);
			log.info("Creating Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = exportMMPackingDetailsExcel.exportMMPackingFDDetailsExcel(workbook, excelDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : Packing Tab - Excel file :: "
					+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Material Management : Packing Tab - Excel file :: "
						+ e.getMessage());
			}
		}
		return excelData;
	}

}
