package com.bh.realtrack.service;

import java.util.Map;

import com.bh.realtrack.dto.ProjectDataformSummaryDTO;

public interface IProjectIdentikitService {

	Map<String, Object> getIdentikitProjectDetails(String projectId);

	Map<String, Object> getIdentikitProjectCoreTeamDetails(String projectId);

	Map<String, Object> getIdentikitProjectGeographyDetails(String projectId);

	Map<String, Object> getIdentikitProjectScopeDetails(String projectId);

	Map<String, Object> saveIdentikitProjectDetails(ProjectDataformSummaryDTO summaryDTO);

	Map<String, Object> getIdentikitPMAddNotesHistory(String projectId);

	byte[] downloadIdentikitProjectScopeDetails(String projectId);

}
