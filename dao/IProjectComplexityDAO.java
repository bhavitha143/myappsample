package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ComplexityFormTemplateDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ITOComplexityTabDTO;
import com.bh.realtrack.dto.ITOProjectComplexityTabDTO;
import com.bh.realtrack.dto.ITOSlotCCTDates;
import com.bh.realtrack.dto.NEComplexityDTO;
import com.bh.realtrack.dto.NEComplexityTabDTO;
import com.bh.realtrack.dto.NEProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.NEProjectComplexityTabDTO;
import com.bh.realtrack.dto.ProjectComplexityDTO;
import com.bh.realtrack.dto.ProjectComplexityDropdownDTO;
import com.bh.realtrack.dto.ProjectComplexityLogDetailsDTO;
import com.bh.realtrack.dto.ProjectsSummaryDetailsDTO;
import com.bh.realtrack.dto.SRVComplexityDTO;
import com.bh.realtrack.dto.SRVPPProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SRVProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SaveProjectComplexityResponseDTO;

/**
 *
 * @author Anand Kumar
 *
 */
public interface IProjectComplexityDAO {
	int saveProjectComplexityList(String projectId, Map<String, Object> map, String sso);

	ProjectComplexityLogDetailsDTO getProjectComplexityLogDetails(String projectId);

	SaveProjectComplexityResponseDTO getProjectComplexitySaveLogDetails(String projectId);

	List<ProjectComplexityDTO> getProjectComplexity(String projectId);

	List<ProjectComplexityDropdownDTO> getPCDropdownDetails(String attributeGroup);

	List<NEComplexityDTO> getNEProjectComplexityData(String projectId);

	NEProjectComplexityTabDTO getNEComplexityProjectTabData(String projectId);

	ComplexityFormTemplateDTO getComplexityFormTemplate(String projectId);

	NEProjectComplexityDropDownDTO getNEProjectComplexityDropDownData(String projectId);

	NEProjectComplexityDropDownDTO getNEPPProjectComplexityDropDownData(String projectId);

	NEProjectComplexityDropDownDTO getNEQPMProjectComplexityDropDownData(String projectId);

	int saveNEComplexityPMData(String projectId, Map<String, Object> map, String sso);

	int saveNEComplexityPPData(String projectId, Map<String, Object> map, String sso);

	int saveNEComplexityQPMData(String projectId, Map<String, Object> ppMap, String sso);

	int saveNEComplexityQPMModuleData(String projectId, Map<String, Object> qpmMap, String sso);

	Double getNEComplexityValues(String mapKey, String keyVal, String role);

	List<NEComplexityDTO> getNEProjectComplexityITOData(String projectId);

	ITOProjectComplexityTabDTO getNEComplexityProjectITOTabData(String projectId);

	int saveNEComplexityPMITOData(String projectId, Map<String, Object> map, String sso);

	int saveNEComplexityPPITOData(String projectId, Map<String, Object> map, String sso);

	int saveITOComplexityDataQPM(String projectId, Map<String, Object> map, String sso);

	int saveITOComplexityDataQPMModule(String projectId, Map<String, Object> map, String sso);

	List<SRVComplexityDTO> getSRVProjectComplexityData(String projectId);

	SRVProjectComplexityDropDownDTO getSRVPMProjectComplexityDropDownData(String projectId);

	SRVPPProjectComplexityDropDownDTO getSRVPPProjectComplexityDropDownData(String projectId);

	int saveSRVComplexityPMData(String projectId, Map<String, Object> map, String sso);

	ProjectsSummaryDetailsDTO getProjectSummaryDetails(String projectId);

	List<DropDownDTO> getWLSegmentDropDown(String projectId);

	List<DropDownDTO> getWLProjectCategoryDropDown(String projectId);

	int saveNEComplexityProjectTabData(NEComplexityTabDTO tabDTO, String sso);

	int saveITOComplexityProjectTabData(ITOComplexityTabDTO tabDTO, String sso);

	List<DropDownDTO> getOrderTypeDropDown(String projectId);

	int saveNEComplexityProjectTabOrderDetails(NEComplexityTabDTO tabDTO, String sso);

	int saveITOComplexityProjectTabOrderDetails(ITOComplexityTabDTO tabDTO, String sso);

	List<ITOSlotCCTDates> getITOSlotWFDatesList(String projectId);

	DropDownDTO getOverallProgressPercent(String projectId);

}
