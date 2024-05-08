package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

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

/**
 * @author Shweta D. Sawant
 */
public interface IMaterialManagementDAO {

	public List<DropDownDTO> getMMReceivingSubProjectFilter(String projectId);

	public List<DropDownDTO> getMMReceivingOrganizationFilter(String projectId);

	public List<DropDownDTO> getMMReceivingLocationFilter(String projectId);

	public MMReceivingSummaryDetailsDTO getMMReceivingCountDetails(String projectId, String subProject,
			String organization, String location);

	public Map<String, Object> getMMReceivingAgingPieChart(String projectId, String subProject, String organization,
			String location);

	public Map<String, Object> getMMReceivingWfAnomalyPieChart(String projectId, String subProject, String organization,
			String location);

	public List<MMReceivingPopupDetailsDTO> getMMReceivingPopupDetails(String projectId, String subProject,
			String organization, String location, String type, String startDate, String endDate, String fiscalWeek);

	public List<MMReceivingChartDetailsDTO> getMMReceivingChartDetails(String projectId, String subProject,
			String organization, String location, String startDate, String endDate, String weekOrMonth);

	public List<MMReceivingPopupDetailsDTO> getMMReceivingDetailsExcel(String projectId, String subProject,
			String organization, String location);

	public String getLastUpdatedDate(String projectId);

	public String checkIfProjectIsModule(String projectId);

	public String getLastUpdatedDate(String projectId, String moveOrderType);

	public List<DropDownDTO> getMMPickingSubProjectFilter(String projectId, String moveOrderType);

	public List<DropDownDTO> getMMPickingSubInvFromFilter(String projectId, String moveOrderType);

	public List<DropDownDTO> getMMPickingSubInvToFilter(String projectId, String moveOrderType);

	public MMPickingSummaryDetailsDTO getMMPickingSummaryDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String moveOrderType);

	public List<MMPickingChartDetailsDTO> getMMPickingChartDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String startDate, String endDate, String moveOrderType);

	public List<MMPickingPopupDetailsDTO> getMMPickingSummaryPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String type);

	List<MMPickingPopupDetailsDTO> getMMPickingChartPopupDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String type, String startDate, String endDate, String fiscalWeek);

	public List<MMPickingPopupDetailsDTO> getMMPickingDetailsExcel(String projectId, String subProject,
			String subInvFrom, String subInvTo);

	public List<MMMoveOrderPopupDetailsDTO> getMMMoveOrderSummaryPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String type);

	List<MMMoveOrderPopupDetailsDTO> getMMMoveOrderChartPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String type, String startDate, String endDate, String fiscalWeek);

	List<MMMoveOrderPopupDetailsDTO> getMMMoveOrderDetailsExcel(String projectId, String subProject, String subInvFrom,
			String subInvTo);

	public String getWfAnoLastUpdatedDate(String projectId);

	public List<DropDownDTO> getWFAnoOwnershipFilter(String projectId);

	public MMWfAnomalySummaryDetailsDTO getWFAnoCountDetails(String projectId, String ownership);

	public Map<String, Object> getWFAnoCycleChartDetails(String projectId, String ownership);

	public Map<String, Object> getWFAnoAgingChartDetails(String projectId, String ownership);

	public List<MMWfAnomalyPopupDetailsDTO> getWFAnoPopupDetails(String projectId, String ownership, String chartType,
			String status);

	public List<MMWfAnomalyPopupDetailsDTO> getWfAnoDetailsExcel(String projectId, String ownership, String chartType,
			String status);

	public List<DropDownDTO> getPackingFDSubProjectFilter(String projectId);

	public List<DropDownDTO> getPackingFDSubInvFromFilter(String projectId);

	public List<DropDownDTO> getPackingFDSubInvToFilter(String projectId);

	public List<DropDownDTO> getPackingFDOwnershipFilter(String projectId);

	public MMPackingFDSummaryDetailsDTO getPackingFDCountDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String ownership);

	public String getPackingFDLastUpdatedDate(String projectId);

	public List<MMPackingFDChartDetailsDTO> getMMPackingFDChartDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String ownership, String startDate, String endDate);

	public List<MMPackingFDPopupDetailsDTO> getMMPackingFDSummaryPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String ownership, String chartType, String status);

	List<MMPackingFDPopupDetailsDTO> getMMPackingFDChartPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String ownership, String chartType, String status, String startDate,
			String endDate, String fiscalWeek);

	public List<MMPackingFDPopupDetailsDTO> getMMPackingFDDetailsExcel(String projectId, String subProject,
			String subInvFrom, String subInvTo, String ownership);

}
