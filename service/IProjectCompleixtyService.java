package com.bh.realtrack.service;

import java.util.Map;

import com.bh.realtrack.dto.CalculateNEComplexityDTO;
import com.bh.realtrack.dto.CalculateSRVComplexityDTO;
import com.bh.realtrack.dto.ComplexityFormTemplateDTO;
import com.bh.realtrack.dto.ITOComplexityTabDTO;
import com.bh.realtrack.dto.NEComplexityTabDTO;
import com.bh.realtrack.dto.NEProjectComplexityDTO;
import com.bh.realtrack.dto.NEProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.ProjectComplexityLogDetailsDTO;
import com.bh.realtrack.dto.SRVPPProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SRVProjectComplexityDTO;
import com.bh.realtrack.dto.SRVProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SaveNEComplexityDTO;
import com.bh.realtrack.dto.SaveNEComplexityResponseDTO;
import com.bh.realtrack.dto.SaveProjectComplexityDTO;
import com.bh.realtrack.dto.SaveProjectComplexityResponseDTO;
import com.bh.realtrack.dto.saveSRVComplexityDTO;

/**
 *
 * @author Anand Kumar
 *
 */
public interface IProjectCompleixtyService {
	ProjectComplexityLogDetailsDTO getProjectComplexityDetails(String projectId);

	SaveProjectComplexityResponseDTO saveProjectComplexity(SaveProjectComplexityDTO projectComplexityDTO);

	Map<String, Object> getProjectComplexityDropdownList(String attributeGroup);

	Map<String, Object> getNEComplexityProjectTabData(String projectId);

	ComplexityFormTemplateDTO getComplexityFormTemplate(String projectId);

	SaveNEComplexityResponseDTO saveNEComplexityData(SaveNEComplexityDTO saveNEComplexityDTO);

	Map<String, Object> calculateNEProjectComplexityData(CalculateNEComplexityDTO calculateNEComplexityDTO);

	NEProjectComplexityDropDownDTO getNEProjectComplexityDropDownData(String projectId);

	NEProjectComplexityDropDownDTO getNEPPProjectComplexityDropDownData(String projectId);

	NEProjectComplexityDropDownDTO getNEQPMProjectComplexityDropDownData(String projectId);

	NEProjectComplexityDTO getNEProjectComplexityData(String projectId);

	NEProjectComplexityDTO getNEProjectComplexityITOData(String projectId);

	Map<String, Object> getNEComplexityProjectITOTabData(String projectId);

	SaveNEComplexityResponseDTO saveNEComplexityITOData(SaveNEComplexityDTO saveNEComplexityDTO);

	Map<String, Object> calculateNEProjectComplexityITOData(CalculateNEComplexityDTO calculateNEComplexityDTO);

	SaveNEComplexityResponseDTO saveITOComplexityDataQPM(SaveNEComplexityDTO saveNEComplexityDTO);

	Map<String, Object> calculateITOProjectComplexityDataQPM(CalculateNEComplexityDTO calculateNEComplexityDTO);

	SRVProjectComplexityDTO getProjectComplexitySRVData(String projectId);

	SRVProjectComplexityDropDownDTO getSRVPMProjectComplexityDropDownData(String projectId);

	SRVPPProjectComplexityDropDownDTO getSRVPPProjectComplexityDropDownData(String projectId);

	Map<String, Object> calculateSRVProjectComplexityData(CalculateSRVComplexityDTO calculateSRVComplexityDTO);

	SaveNEComplexityResponseDTO saveSRVComplexityData(saveSRVComplexityDTO saveSRVComplexityDTO);

	Map<String, Object> getProjectSummaryDetails(String projectId);

	Map<String, Object> saveNEComplexityProjectTabData(NEComplexityTabDTO tabDTO);

	Map<String, Object> saveITOComplexityProjectTabData(ITOComplexityTabDTO tabDTO);

}
