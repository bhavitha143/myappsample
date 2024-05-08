package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.NPIDropdownResponseDTO;
import com.bh.realtrack.dto.NPISummaryDetailsResponseDTO;

public interface INPIProjectDetailsService {

	public NPIDropdownResponseDTO getNPIDropDownDetails(String projectId) throws Exception;

	public NPISummaryDetailsResponseDTO getNPISummaryDetails(String projectId, List<String> subProjects,
			List<String> owners, List<String> activityGroups) throws Exception;

	public Map<String, Object> getNPIDetailsPopupData(String projectId, String chartType,
			String status, String fwWeek, List<String> subProjects, List<String> owners, List<String> activityGroups) throws Exception;

	public Map<String, Object> getNPIOTDTrendDetails(String projectId, List<String> subProject,
			List<String> owner, List<String> activityGroup,  String startDate, String endDate) throws Exception;

	public byte[] getNPIDetailsExcel(String projectId, String subProject, String owner, String activityGroup);

}
