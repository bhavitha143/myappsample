package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.ProjectConsoleCompletenessBomDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessPackingDetailsDTO;
import com.bh.realtrack.dto.QualityConsoleInspectionDTO;
import com.bh.realtrack.dto.QualityConsoleMDRDTO;
import com.bh.realtrack.dto.QualityConsoleNonConformancesDTO;
import com.bh.realtrack.dto.QualityConsoleProjectDetailsDTO;
import com.bh.realtrack.dto.QualityConsoleProjectStatusDTO;
import com.bh.realtrack.dto.QualityConsolePunchListDTO;
import com.bh.realtrack.dto.QualityConsoleQCPDTO;
import com.bh.realtrack.dto.QualityConsoleTRSBookDTO;
import com.bh.realtrack.dto.QualityConsoleTRSMeetingDTO;

public interface IQualityConsoleDAO {

	QualityConsoleProjectDetailsDTO getQualityConsoleProjectDetails(String projectId);

	QualityConsoleProjectStatusDTO getQualityConsoleProjectStatus(String projectId);

	QualityConsoleInspectionDTO getQualityConsoleInspectionDetails(String projectId);

	String getQualityConsoleQIRadarDetails(String projectId);

	String getQualityConsoleDPUDetails(String projectId);

	QualityConsolePunchListDTO getQualityConsolePunchListDetails(String projectId);

	ProjectConsoleCompletenessBomDetailsDTO getQualityConsoleBomDetails(String projectId);

	ProjectConsoleCompletenessPackingDetailsDTO getQualityConsolePackingDetails(String projectId,
			boolean isModuleProject);

	QualityConsoleNonConformancesDTO getQualityConsoleNonConformancesDetails(String projectId);

	QualityConsoleQCPDTO getQualityConsoleQCPDetails(String projectId);

	QualityConsoleMDRDTO getQualityConsoleMDRDetails(String projectId);

	QualityConsoleTRSBookDTO getQualityConsoleTRSBookDetails(String projectId);

	QualityConsoleTRSMeetingDTO getQualityConsoleTRSMeetingDetails(String projectId);

	List<String> getModuleProjects(String projectId);

}
