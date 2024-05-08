package com.bh.realtrack.service;

import java.util.Map;

import com.bh.realtrack.dto.LCDashboardBPSDTO;
import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisFilterDTO;
import com.bh.realtrack.dto.LCDashboardDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisFilterDTO;

public interface LogisticCostDashboardService {

	Map<String, Object> getLCDashboardDropDowns(LCDashboardDTO paramList);

	Map<String, Object> getLCDashboardBoxPackingSummary(LCDashboardBPSDTO paramList);

	Map<String, Object> getLCDashboardTransportationSummary(LCDashboardBPSDTO paramList);

	Map<String, Object> getLCDashboardWorkloadDetails(LCDashboardBPSDTO paramList);

	byte[] downloadLCDashboardWorkloadDetails(LCDashboardBPSDTO paramList);

	Map<String, Object> getLCDashboardBPSiteAnalysisFilters(LCDashboardDTO paramList);

	Map<String, Object> getLCDashboardBPSiteAnalysisSummary(LCDashboardBPSiteAnalysisFilterDTO paramList);

	byte[] downloadLCDashboardBPSiteAnalysisDetails(LCDashboardBPSiteAnalysisFilterDTO paramList);

	Map<String, Object> getLCDashboardTransportationAnalysisFilters(LCDashboardDTO paramList);

	Map<String, Object> getLCDashboardTransportationAnalysisSummary(
			LCDashboardTransportationAnalysisFilterDTO paramList);

	byte[] downloadLCDashboardTransportationAnalysisDetails(LCDashboardTransportationAnalysisFilterDTO paramList);

}
