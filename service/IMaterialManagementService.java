package com.bh.realtrack.service;

import java.util.Map;

/**
 * @author Shweta D. Sawant
 */
public interface IMaterialManagementService {

	public Map<String, Object> getMMReceivingDropdownDetails(String projectId);

	public Map<String, Object> getMMReceivingSummaryDetails(String projectId, String subProject, String organization,
			String location);

	public Map<String, Object> getMMReceivingPieChartDetails(String projectId, String subProject, String organization,
			String location);

	public Map<String, Object> getMMReceivingDetailsPopupData(String projectId, String subProject, String organization,
			String location, String type, String startDate, String endDate, String fiscalWeek);

	public Map<String, Object> getMMReceivingChartDetails(String projectId, String subProject, String organization,
			String location, String startDate, String endDate, String weekOrMonth);

	public byte[] getMMReceivingDetailsExcel(String projectId, String subProject, String organization, String location);

	public Map<String, Object> getMMCheckApplicableFlag(String projectId);

	public Map<String, Object> getMMPickingDropdownDetails(String projectId);

	public Map<String, Object> getMMPickingSummaryDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo);

	public Map<String, Object> getMMPickingBacklogTrendChartDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String startDate, String endDate);

	public Map<String, Object> getMMPickingPopupDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String type, String startDate, String endDate, String fiscalWeek);

	public byte[] exportMMPickingDetailsExcel(String projectId, String subProject, String subInvFrom, String subInvTo);

	public Map<String, Object> getMMMoveOrderDropdownDetails(String projectId);

	public Map<String, Object> getMMMoveOrderSummaryDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo);

	public Map<String, Object> getMMMoveOrderBacklogTrendChartDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String startDate, String endDate);

	public Map<String, Object> getMMMoveOrderPopupDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String type, String startDate, String endDate, String fiscalWeek);

	public byte[] exportMMMoveOrderDetailsExcel(String projectId, String subProject, String subInvFrom,
			String subInvTo);

	public Map<String, Object> getWFAnoDropDownDetails(String projectId);

	public Map<String, Object> getWFAnoSummaryDetails(String projectId, String ownership);

	public Map<String, Object> getWFAnoChartDetails(String projectId, String ownership);

	public Map<String, Object> getWFAnoPopupDetails(String projectId, String ownership, String chartType,
			String status);

	public byte[] exportWFAnoExcelDetailsExcel(String projectId, String ownership);

	public Map<String, Object> getPackingFDDropdownDetails(String projectId);

	public Map<String, Object> getMMPackingFDSummaryDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String ownership);

	public Map<String, Object> getMMPackingFDChartDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String ownership, String startDate, String endDate);

	public Map<String, Object> getMMPackingFDPopupDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String ownership, String chartType, String status, String startDate, String endDate,
			String fiscalWeek);

	public byte[] exportMMPackingFDDetailsExcel(String projectId, String subProject, String subInvFrom, String subInvTo,
			String ownership);

}
