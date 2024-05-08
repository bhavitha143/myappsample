package com.bh.realtrack.service;

import java.util.Map;

public interface IQualityConsoleService {

	Map<String, Object> getQualityConsoleProjectDetails(String projectId);

	Map<String, Object> getQualityConsoleProjectStatus(String projectId);

	Map<String, Object> getQualityConsoleInspectionDetails(String projectId);

	Map<String, Object> getQualityConsoleQIRadarDetails(String projectId);

	Map<String, Object> getQualityConsoleDPUDetails(String projectId);

	Map<String, Object> getQualityConsolePunchListDetails(String projectId);

	Map<String, Object> getQualityConsoleCompletenessDetails(String projectId);

	Map<String, Object> getQualityConsoleNonConformancesDetails(String projectId);

	Map<String, Object> getQualityConsoleQCPDetails(String projectId);

	Map<String, Object> getQualityConsoleMDRDetails(String projectId);

	Map<String, Object> getQualityConsoleTRSBookDetails(String projectId);

	Map<String, Object> getQualityConsoleTRSMeetingDetails(String projectId);

}
