package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

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

public interface LogisticCostDashboardDAO {

	List<DropDownDTO> getLCPmLeaderDropDown(LCDashboardDTO paramList);

	List<DropDownDTO> getLCSpmDropDown(LCDashboardDTO paramList);

	List<DropDownDTO> getLCFinanceSegmentDropDown(LCDashboardDTO paramList, String projectList);

	List<DropDownDTO> getLCShippingManagerDropDown(LCDashboardDTO paramList, String projectList);

	List<DropDownDTO> getLCRacDateHorizonDropDowns();

	List<DropDownDTO> getLCExWorksHorizonDropDowns();

	String getDefaultProjectList(LCDashboardDTO paramList);

	String getBoxPackingProjectList(LCDashboardBPSDTO paramList, String startDate, String endDate);

	LCSummaryDTO getLCDashboardBoxPackingSummary(LCDashboardBPSDTO paramList, String projectList);

	Map<String, Object> getLCDashboardVolumePackedPieChart(LCDashboardBPSDTO paramList, String projectList);

	String getTransportationProjectList(LCDashboardBPSDTO paramList, String startDate, String endDate);

	LCSummaryDTO getLCDashboardTransportationSummary(LCDashboardBPSDTO paramList, String projectList);

	Map<String, Object> getLCDashboardVolumeShippedPieChart(LCDashboardBPSDTO paramList, String projectList);

	Map<String, Object> getLCDashboardShipmentStatusPieChart(String projectList);

	String getWorkloadProjectList(LCDashboardBPSDTO paramList, String startDate, String endDate);

	List<LCDashboardWorkloadDTO> getLCDashboardWorkloadDetails(LCDashboardBPSDTO paramList, String projectList);

	List<LCDashboardDetailedWorkloadDTO> getLCDashboardDetailedWorkloadDetails(LCDashboardBPSDTO paramList,
			String projectList);

	Map<String, Object> getLCDashboardVolumePackedBySitePieChart(LCDashboardBPSDTO paramList, String projectList);

	List<DropDownDTO> getBPSiteAnalysisSiteDropDown(String projectList);

	List<DropDownDTO> getBPSiteAnalysisPackTypeDropDown(String projectList);

	List<DropDownDTO> getBPSiteAnalysisColorByDropDown();

	DropDownDTO getBPSiteAnalysisDefaultDates();

	String getBPSiteAnalysisSummaryProjectList(LCDashboardBPSiteAnalysisFilterDTO paramList, String startDate,
			String endDate);

	List<LCDashboardBPSiteAnalysisDTO> getBPSiteAnalysisChartSummary(String projectList,
			LCDashboardBPSiteAnalysisFilterDTO paramList, String startDate, String endDate, String chartType,
			String colorBy);

	List<LCDashboardBPSiteAnalysisDetailsDTO> getBPSiteAnalysisDetails(String projectList, String startDate,
			String endDate);

	List<DropDownDTO> getTransportationAnalysisRegionDropDown(String projectList);

	List<DropDownDTO> getTransportationAnalysisIncotermsDropDown(String projectList);

	List<DropDownDTO> getTransportationAnalysisColorByDropDown();

	DropDownDTO getTransportationAnalysisDefaultDates();

	String getLCDashboardTransportationAnalysisSummaryProjectList(LCDashboardTransportationAnalysisFilterDTO paramList,
			String startDate, String endDate);

	List<LCDashboardTransportationAnalysisDTO> getTransportationAnalysisChartSummary(String projectList,
			LCDashboardTransportationAnalysisFilterDTO paramList, String startDate, String endDate, String chartType,
			String colorBy);

	List<LCDashboardTransportationAnalysisDetailsDTO> getTransportationAnalysisDetails(String projectList,
			String startDate, String endDate);

	List<LegendColorDTO> getLCDashboardChartConfiguration(String colorBy);

}
