package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.IdentikitPMAddNotesHistoryDTO;
import com.bh.realtrack.dto.IdentikitProjectCoreTeamDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectGeographyDBDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectScopeDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectScopeTableDetailsDTO;
import com.bh.realtrack.dto.ProjectDataformSummaryDTO;

public interface IProjectIdentikitDAO {

	IdentikitProjectDetailsDTO getIdentikitProjectDetails(String projectId, String sso);

	IdentikitProjectCoreTeamDetailsDTO getIdentikitProjectCoreTeamDetails(String projectId);

	Map<String, Object> getIdentikitProjectTeamDetails(String projectId);

	List<IdentikitProjectGeographyDBDetailsDTO> getIdentikitProjectGeographyDetails(String projectId);

	IdentikitProjectScopeDetailsDTO getIdentikitProjectScopeDetails(String projectId);

	List<IdentikitProjectScopeTableDetailsDTO> getIdentikitProjectScopeTableDetails(String projectId);

	boolean saveIdentikitProjectDetails(ProjectDataformSummaryDTO summaryDTO, String sso);

	List<IdentikitPMAddNotesHistoryDTO> getIdentikitPMAddNotesHistory(String projectId);


}
