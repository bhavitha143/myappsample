package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ActionOwnerDetailsDTO;
import com.bh.realtrack.dto.ChangeActionDTO;
import com.bh.realtrack.dto.ChangeManagementDTO;
import com.bh.realtrack.dto.ChangeSummaryRequestDTO;
import com.bh.realtrack.dto.UserDetailsDTO;

public interface IChangeManagementDAO {

	List<String> getJobFilter(String projectId);

	List<ChangeManagementDTO> getSummary(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	List<ChangeActionDTO> getChangeRequest(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	List<ChangeActionDTO> getChangeDataforEcr(String projectId, String ecrCode);

	Boolean deleteChangeRequest(String changeActionId);

	void saveChangeRequestData(List<ChangeActionDTO> changeActionDTOList);

	String getChangeActionLastUpdateDate(String projectId);

	String getChangeSummaryLastUpdateDate(String projectId);

	List<UserDetailsDTO> getRealTeackUserDetails(String projectId);

	ActionOwnerDetailsDTO getProjectTeamMembersDetails(String projectId);

	void triggerCMUserNotification(String projectId);

	String getTotalECRCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	List<ChangeActionDTO> downloadChangeManagementDetails(String projectId, String jobNumber, String phase);

	ChangeManagementDTO getECRDetails(String projectId, String ecrCode);

	Map<String, String> getECRAssessedImpactDetails(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	Map<String, String> getECRAgingDetails(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	ChangeActionDTO getChangeRequestActionData(String projectId, String jobNumber, String changeActionId);

	boolean saveChangeRequestAction(ChangeActionDTO editChangeRequestActionDTO);

	String getTotalAllCompletedActionECRCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	String getTotalInProgressActionECRCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	String getTotalNoActionECRCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	Map<String, String> getActionSayDODetails(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	Map<String, Map<String, String>> getPendingActionLookaheadDetails(ChangeSummaryRequestDTO changeSummaryRequestDTO);

	String getTotalCompletedActionCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);
	
	String getTotalInProgressActionCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);
	
	String getTotalActionCount(ChangeSummaryRequestDTO changeSummaryRequestDTO);
}
