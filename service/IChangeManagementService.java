package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ActionOwnerDetailsDTO;
import com.bh.realtrack.dto.ChangeActionDTO;
import com.bh.realtrack.dto.ChangeManagementDTO;
import com.bh.realtrack.dto.ChangeSummaryRequestDTO;
import com.bh.realtrack.dto.EditChangeActionDTO;

public interface IChangeManagementService {

	public Map<String, Object> getChangeManagementFilter(String projectId);

	List<ChangeManagementDTO> getSummary(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	List<ChangeActionDTO> getChangeRequest(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	List<ChangeActionDTO> getChangeDataforEcr(String projectId, String ecrCode);

	Map<String, String> deleteChangeRequest(String changeActionId, String projectId);

	Map<String, String> saveChangeRequestData(EditChangeActionDTO editChangeActionDTO);

	byte[] downloadChangeManagementDetails(String projectId, String jobNumber, String phase);

	String getChangeActionLastUpdateDate(String projectId);

	String getChangeSummaryLastUpdateDate(String projectId);

	ActionOwnerDetailsDTO getActionOwnerDetail(String projectId);

	ChangeActionDTO getECRDetails(String projectId, String ecrCode);

	Map<String, Object> getChangeRequestActionData(String projectId, String jobNumber, String changeActionId);

	Map<String, String> saveChangeRequestAction(ChangeActionDTO editChangeRequestActionDTO);
	
	Map<String, String> getChangeRequestSumaryCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);
	
	Map<String, String> getChangeSummaryAssessedImpact(ChangeSummaryRequestDTO changeSummaryRequestDTO);
	
	Map<String, String> getChangeSummaryAgingECR(ChangeSummaryRequestDTO changeSummaryRequestDTO);
	
	List<ChangeManagementDTO> getSummaryPopupDetails(ChangeSummaryRequestDTO changeSummaryRequestDTO) ;
	
	Map<String, String> getChangeRequestCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);
	
	Map<String, String> getChangeRequestSayDOChart(ChangeSummaryRequestDTO changeSummaryRequestDTO);
	
	Map<String, Map<String, String>> getPendingActionLookHead(ChangeSummaryRequestDTO changeSummaryRequestDTO);
	

}
