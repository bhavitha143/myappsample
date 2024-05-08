package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.KeyValueDTO;
import com.bh.realtrack.dto.LCSOSCellDTO;
import com.bh.realtrack.dto.LCVolumeAnalysisDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeActualCostDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeBoxPackingDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeDetailsTransportationDTO;
import com.bh.realtrack.dto.LcVolumeGaugeChartDTO;
import com.bh.realtrack.dto.ShippingReportDTO;
import com.bh.realtrack.dto.ShowExpiryMessageDTO;

/**
 * @author Shweta D. Sawant
 */
public interface IShippingPreservationDAO {

	// Shipping/Preservation Widget

	ShowExpiryMessageDTO showExpiryMessageFlag(String projectId);

	ShowExpiryMessageDTO showExpiryMessageExpiredFlag(String projectId);

	// Logistic Cost Widget

	List<ShippingReportDTO> getShippingReportDetails(String projectId);

	List<KeyValueDTO> getLCSubProjectDropdown(String projectId);

	List<KeyValueDTO> getLCShowByDropdown(String projectId);

	String getLCAgainstVolumeBudgetAsSold(String projectId, String subProject, String showBy, String viewType);

	Map<String, Object> getLCAgainstVolumeSummaryQuarterlyBarChart(String projectId, String subProject, String showBy,
			String viewType);

	Map<String, Object> getLCAgainstVolumeSummaryMonthlyBarChart(String projectId, String subProject, String showBy,
			String viewType);

	LcVolumeGaugeChartDTO getLCAgainstVolumeSummaryGaugeChart(String projectId, String subProject, String showBy,
			String viewType);

	String getLCAgainstVolumeSummaryUpdatedOn(String projectId, String subProject);

	Map<String, Object> getLCVolumeAnalysisQuarterlyGraphSummary(String projectId, String subProject, String viewType);

	Map<String, Object> getLCVolumeAnalysisMonthlyGraphSummary(String projectId, String subProject, String viewType);

	Map<String, Object> getLCVolumeAnalysisVolumePackedPiechartSummary(String projectId, String subProject,
			String viewType);

	Map<String, Object> getLCVolumeAnalysisVolumeShippedPiechartSummary(String projectId, String subProject,
			String viewType);

	String getLCVolumeAnalysisSummaryUpdatedOn(String projectId, String subProject);

	List<LcAgainstVolumeDetailsTransportationDTO> getLCAgainstVolumeTransportationDetails(String projectId,
			String subProject);

	List<LcAgainstVolumeActualCostDetailsDTO> getLCAgainstVolumeActualCostDetails(String projectId, String subProject,
			String viewType, String showBy);

	List<LcAgainstVolumeBoxPackingDetailsDTO> getLCAgainstVolumeBoxPackingDetails(String projectId, String subProject);

	List<LCVolumeAnalysisDetailsDTO> getLCVolumeAnalysisPopupDetails(String projectId, String subProject,
			String viewType, String chartType, String category);

	List<LCVolumeAnalysisDetailsDTO> getLCVolumeAnalysisExcelDetails(String projectId, String subProject);

	String getLCSOSCellData(String projectId);

	int saveLCSOSCellData(LCSOSCellDTO sosCellDTO, String sso);

}
