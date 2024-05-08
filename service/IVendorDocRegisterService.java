package com.bh.realtrack.service;

import java.util.Map;

public interface IVendorDocRegisterService {

	Map<String, Object> getStatisticsMileStoneFilter(String projectId, String subProjectNo, String subProductLine,
			String docOwner, String docType, String docLevel);

	Map<String, Object> getStatisticsOTDChartData(String projectId, String subProjectNo, String subProductLine,
			String docOwner, String docType, String docLevel, String milestoneCode);

	Map<String, Object> getStatisticsOTDChartPopupDetails(String projectId, String subProjectNo, String subProductLine,
			String docOwner, String docType, String docLevel, String milestoneCode, String milestoneStatus);

	byte[] downloadStatisticsOTDExcelDetails(String projectId, String subProjectNo, String subProductLine,
			String docOwner, String docType, String docLevel, String milestoneCode);
}