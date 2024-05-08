package com.bh.realtrack.service;

import java.util.Map;

import com.bh.realtrack.dto.LCSOSCellDTO;
import com.bh.realtrack.dto.LogisticCostDropdownDTO;
import com.bh.realtrack.dto.ShowExpiryMessageDTO;

/**
 * @author shwsawan
 *
 */
public interface IShippingPreservationService {

	// Shipping/Preservation Widget

	ShowExpiryMessageDTO showExpiryMessageFlag(String projectId, String status);

	byte[] downloadShippingReportDetails(String projectId);

	// Logistic Cost Widget

	LogisticCostDropdownDTO getLogisticCostDropdowns(String projectId);

	Map<String, Object> getLCAgainstVolumeSummary(String projectId, String subProject, String showBy, String viewType);

	Map<String, Object> getLCVolumeAnalysisSummary(String projectId, String subProject, String viewType);

	Map<String, Object> getLCAgainstVolumeDetailsPopup(String projectId, String subProject, String showBy,
			String viewType, String selectedPeriod);

	Map<String, Object> getLCVolumeAnalysisDetailsPopup(String projectId, String subProject, String viewType,
			String chartType, String category);

	byte[] downloadLCAgainstVolumeDetails(String projectId, String subProject, String showBy, String viewType);

	byte[] downloadLCVolumeAnalysisDetails(String projectId, String subProject, String viewType);

	Map<String, Object> getLCSOSCellData(String projectId);

	Map<String, Object> saveLCSOSCellData(LCSOSCellDTO sosCellDTO);

}