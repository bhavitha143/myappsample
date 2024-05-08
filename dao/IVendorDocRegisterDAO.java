package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.VDRStatisticsOTDDetailsDTO;

public interface IVendorDocRegisterDAO {

	Map<String, Integer> getStatisticsOTDDetails(String projectId, String subProjectNo, String docOwner,
			String docType);

	String getStatisticsOTDLastUpdateDate(String projectId);

	List<VDRStatisticsOTDDetailsDTO> getStatisticsOTDChartPopupDetails(String projectId, String subProjectNo,
			String docOwner, String docType, String milestoneStatus);

	List<VDRStatisticsOTDDetailsDTO> downloadStatisticsOTDExcelDetails(String projectId, String subProjectNo,
			String docOwner, String docType);

}
