package com.bh.realtrack.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IQualityConsoleDAO;
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
import com.bh.realtrack.service.IQualityConsoleService;

@Service
public class QualityConsoleServiceImpl implements IQualityConsoleService {

	private static final Logger log = LoggerFactory.getLogger(QualityConsoleServiceImpl.class);

	@Autowired
	IQualityConsoleDAO iQualityConsoleDAO;

	@Override
	public Map<String, Object> getQualityConsoleProjectDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsoleProjectDetailsDTO projectDetailsDTO = new QualityConsoleProjectDetailsDTO();
		try {
			projectDetailsDTO = iQualityConsoleDAO.getQualityConsoleProjectDetails(projectId);
			responseMap.put("projectDetails", projectDetailsDTO);
		} catch (Exception e) {
			log.error(
					"Exception in QualityConsoleServiceImpl :: getQualityConsoleProjectDetails() :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleProjectStatus(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsoleProjectStatusDTO dto = new QualityConsoleProjectStatusDTO();
		try {
			dto = iQualityConsoleDAO.getQualityConsoleProjectStatus(projectId);
			responseMap.put("projectStatusDetails", dto);
		} catch (Exception e) {
			log.error(
					"Exception in QualityConsoleServiceImpl :: getQualityConsoleProjectStatus() :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleInspectionDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsoleInspectionDTO dto = new QualityConsoleInspectionDTO();
		try {
			dto = iQualityConsoleDAO.getQualityConsoleInspectionDetails(projectId);
			responseMap.put("inspectionDetails", dto);
		} catch (Exception e) {
			log.error("Exception in QualityConsoleServiceImpl :: getQualityConsoleInspectionDetails() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleQIRadarDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			String redFlagInspectionsCnt = iQualityConsoleDAO.getQualityConsoleQIRadarDetails(projectId);
			responseMap.put("redFlagInspectionsCnt", redFlagInspectionsCnt);
		} catch (Exception e) {
			log.error(
					"Exception in QualityConsoleServiceImpl :: getQualityConsoleQIRadarDetails() :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleDPUDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String dpu = "";
		try {
			dpu = iQualityConsoleDAO.getQualityConsoleDPUDetails(projectId);
			responseMap.put("dpuDetails", dpu);
		} catch (Exception e) {
			log.error("Exception in QualityConsoleServiceImpl :: getQualityConsoleDPUDetails() :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsolePunchListDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsolePunchListDTO dto = new QualityConsolePunchListDTO();
		try {
			dto = iQualityConsoleDAO.getQualityConsolePunchListDetails(projectId);
			responseMap.put("punchListDetails", dto);
		} catch (Exception e) {
			log.error("Exception in QualityConsoleServiceImpl :: getQualityConsolePunchListDetails() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleCompletenessDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleCompletenessBomDetailsDTO bomDetails = new ProjectConsoleCompletenessBomDetailsDTO();
		ProjectConsoleCompletenessPackingDetailsDTO packingDetails = new ProjectConsoleCompletenessPackingDetailsDTO();
		boolean isModuleProject = checkModuleProject(projectId);
		try {
			bomDetails = iQualityConsoleDAO.getQualityConsoleBomDetails(projectId);
			packingDetails = iQualityConsoleDAO.getQualityConsolePackingDetails(projectId, isModuleProject);
			responseMap.put("bomDetails", bomDetails);
			responseMap.put("packingDetails", packingDetails);
		} catch (Exception e) {
			log.error("Exception in QualityConsoleServiceImpl :: getQualityConsoleCompletenessDetails() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	private boolean checkModuleProject(String projectId) {
		boolean isModuleProject = false;
		List<String> projectIdList = iQualityConsoleDAO.getModuleProjects(projectId);
		if (null != projectIdList && projectIdList.contains(projectId)) {
			isModuleProject = true;
		}
		return isModuleProject;
	}

	@Override
	public Map<String, Object> getQualityConsoleNonConformancesDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsoleNonConformancesDTO dto = new QualityConsoleNonConformancesDTO();
		try {
			dto = iQualityConsoleDAO.getQualityConsoleNonConformancesDetails(projectId);
			responseMap.put("nonConformancesDetails", dto);
		} catch (Exception e) {
			log.error("Exception in QualityConsoleServiceImpl :: getQualityConsoleNonConformancesDetails() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleQCPDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsoleQCPDTO dto = new QualityConsoleQCPDTO();
		try {
			dto = iQualityConsoleDAO.getQualityConsoleQCPDetails(projectId);
			responseMap.put("qcpDetails", dto);
		} catch (Exception e) {
			log.error("Exception in QualityConsoleServiceImpl :: getQualityConsoleQCPDetails() :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleMDRDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsoleMDRDTO dto = new QualityConsoleMDRDTO();
		try {
			dto = iQualityConsoleDAO.getQualityConsoleMDRDetails(projectId);
			responseMap.put("mdrDetails", dto);

		} catch (Exception e) {
			log.error("Exception in QualityConsoleServiceImpl :: getQualityConsoleMDRDetails() :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleTRSBookDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsoleTRSBookDTO dto = new QualityConsoleTRSBookDTO();
		try {
			dto = iQualityConsoleDAO.getQualityConsoleTRSBookDetails(projectId);
			responseMap.put("trsBookDetails", dto);
		} catch (Exception e) {
			log.error(
					"Exception in QualityConsoleServiceImpl :: getQualityConsoleTRSBookDetails() :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQualityConsoleTRSMeetingDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		QualityConsoleTRSMeetingDTO dto = new QualityConsoleTRSMeetingDTO();
		try {
			dto = iQualityConsoleDAO.getQualityConsoleTRSMeetingDetails(projectId);
			responseMap.put("trsMeetingDetails", dto);
		} catch (Exception e) {
			log.error("Exception in QualityConsoleServiceImpl :: getQualityConsoleTRSMeetingDetails() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

}
