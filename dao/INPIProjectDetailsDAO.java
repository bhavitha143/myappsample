package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.NPIDetailsPopupDataResponse;
import com.bh.realtrack.dto.NPIDropdownResponseDTO;
import com.bh.realtrack.dto.NPIOtdTrendDTO;
import com.bh.realtrack.dto.NPIProjectDetailsDTO;
import com.bh.realtrack.dto.NPISummaryDetailsResponseDTO;

public interface INPIProjectDetailsDAO {

	public NPIDropdownResponseDTO getNPIDropDownDetails(String projectId) throws Exception;

	public NPISummaryDetailsResponseDTO getNPISummaryDetails(String projectId, List<String> subProjects,
			List<String> owners, List<String> activityGroups) throws Exception;

	public List<NPIDetailsPopupDataResponse> getNPIDetailsPopupData(String projectId, String chartType, String status,
			String fwWeek, List<String> subProjects, List<String> owners,
			List<String> activityGroups) throws Exception;

	public List<NPIOtdTrendDTO> getNPIOTDTrendDetails(String projectId, List<String> subProject, List<String> owner,
			List<String> activityGroup,  String startDate, String endDate) throws Exception;


	public List<NPIProjectDetailsDTO> getNPIDetailsExcel(String projectId, String subProject, String owner,
			String activityGroup);

	public String getUpdatedOnForChartData(String projectId);

	public String getFiscalWeek();

}
