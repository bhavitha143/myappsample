package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IChangeManagementDAO;
import com.bh.realtrack.dto.ActionOwnerDetailsDTO;
import com.bh.realtrack.dto.ChangeActionDTO;
import com.bh.realtrack.dto.ChangeManagementDTO;
import com.bh.realtrack.dto.ChangeSummaryRequestDTO;
import com.bh.realtrack.dto.EditChangeActionDTO;
import com.bh.realtrack.excel.ExportChangeManagementExcel;
import com.bh.realtrack.service.IChangeManagementService;

@Service
public class ChangeRequestServiceImpl implements IChangeManagementService {

	private static Logger log = LoggerFactory.getLogger(ChangeRequestServiceImpl.class.getName());

	@Autowired
	private IChangeManagementDAO ichangemanagementdao;

	@Override
	public Map<String, Object> getChangeManagementFilter(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<String> jobList = new ArrayList<String>();
		try {
			jobList = ichangemanagementdao.getJobFilter(projectId);
		} catch (Exception e) {
			log.error("getChangeManagementFilter(): Exception occurred : " + e.getMessage());
		}
		responseMap.put("jobNumber", jobList);
		return responseMap;
	}

	@Override
	public List<ChangeManagementDTO> getSummary(ChangeSummaryRequestDTO changeSummaryRequestDTO) {

		return ichangemanagementdao.getSummary(changeSummaryRequestDTO);
	}

	@Override
	public String getChangeSummaryLastUpdateDate(String projectId) {
		return ichangemanagementdao.getChangeSummaryLastUpdateDate(projectId);
	}

	@Override
	public List<ChangeActionDTO> getChangeRequest(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		return ichangemanagementdao.getChangeRequest(changeSummaryRequestDTO);
	}

	@Override
	public String getChangeActionLastUpdateDate(String projectId) {
		return ichangemanagementdao.getChangeActionLastUpdateDate(projectId);
	}

	@Override
	public List<ChangeActionDTO> getChangeDataforEcr(String projectId, String ecrCode) {
		return ichangemanagementdao.getChangeDataforEcr(projectId, ecrCode);
	}

	@Override
	public Map<String, String> deleteChangeRequest(String changeActionId, String projectId) {
		Map<String, String> responseMap = new HashMap<String, String>();
		ichangemanagementdao.deleteChangeRequest(changeActionId);
		ichangemanagementdao.triggerCMUserNotification(projectId);
		responseMap.put("status", "success");
		return responseMap;
	}

	@Override
	public Map<String, String> saveChangeRequestData(EditChangeActionDTO editChangeActionDTO) {
		Map<String, String> validationMap = valdiateCRActionData(editChangeActionDTO.getEditedCRActionList());
		String projectId = null;
		if (validationMap.isEmpty()) {
			projectId = editChangeActionDTO.getEditedCRActionList().get(0).getProjectId();
			ichangemanagementdao.saveChangeRequestData(editChangeActionDTO.getEditedCRActionList());
			ichangemanagementdao.triggerCMUserNotification(projectId);
			validationMap.put("sucess", "Change Request Actions save successfully");
		}
		return validationMap;
	}


	private Map<String, String> valdiateCRActionData(List<ChangeActionDTO> changeActionDTOList) {
		Map<String, String> validationMap = new HashMap<String, String>();
		String ownerDetail = null;
		if (null != changeActionDTOList && !changeActionDTOList.isEmpty()) {
			for (ChangeActionDTO changeActionDTO : changeActionDTOList) {
				if (null == changeActionDTO.getActions() || "".equalsIgnoreCase(changeActionDTO.getActions().trim())) {
					validationMap.put("failure", "Please enter Action value");
					break;
				} else if (null == changeActionDTO.getOwner()
						|| "".equalsIgnoreCase(changeActionDTO.getOwner().trim())) {
					validationMap.put("failure", "Please enter Owner value");
					break;
				} else if (null == changeActionDTO.getDueDate()
						|| "".equalsIgnoreCase(changeActionDTO.getDueDate().trim())) {
					validationMap.put("failure", "Please enter Due Date value");
					break;
				} else if (!validDateFormat(changeActionDTO.getDueDate())) {
					validationMap.put("failure", "Please enter Due Date in DD-MMM-YYYY format");
					break;
				} else if ((null == changeActionDTO.getStatus()
						|| "".equalsIgnoreCase(changeActionDTO.getStatus().trim()))
						&& (null == changeActionDTO.getActualClosureDate()
								|| "".equalsIgnoreCase(changeActionDTO.getActualClosureDate().trim()))) {
					validationMap.put("failure", "Please enter Status value");
					break;
				} else if (null != changeActionDTO.getStatus()
						&& "Completed".equalsIgnoreCase(changeActionDTO.getStatus())) {

					if (null == changeActionDTO.getActualClosureDate()
							|| "".equalsIgnoreCase(changeActionDTO.getActualClosureDate().trim())) {
						validationMap.put("failure", "Actual closure Date is mandatory when  Status is Completed");
						break;
					} else if (!validDateFormat(changeActionDTO.getActualClosureDate())) {
						validationMap.put("failure", "Please enter Actual closure Date in DD-MMM-YYYY format");
						break;
					}
				}
				ownerDetail = changeActionDTO.getOwner();
				if (ownerDetail.contains("(") && ownerDetail.contains(")")) {
					changeActionDTO.setOwner(ownerDetail.substring(0, ownerDetail.indexOf("(")));
					changeActionDTO
							.setOwnerSSO(ownerDetail.substring(ownerDetail.indexOf("(") + 1, ownerDetail.indexOf(")")));
				}
			}
		} else {
			validationMap.put("failure", "No Change Summary Action Present");
		}
		return validationMap;
	}

	private boolean validDateFormat(String inputDateString) {
		boolean valid = true;
		try {
			new SimpleDateFormat("dd-MMM-yyyy").parse(inputDateString);
		} catch (Exception e) {
			valid = false;
		}
		return valid;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] downloadChangeManagementDetails(String projectId, String jobNumber, String phase) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExportChangeManagementExcel exportChangeManagementExcel = new ExportChangeManagementExcel();
		byte[] excelData = null;
		try {
			List<ChangeActionDTO> changeActionDTOList = ichangemanagementdao.downloadChangeManagementDetails(projectId,
					jobNumber, phase);
			workbook = exportChangeManagementExcel.exportChangeManagementExcel(workbook, changeActionDTOList);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (IOException e) {
			log.error("Error occured when downloading Change Management Excel file" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading Change Management Excel file" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public ActionOwnerDetailsDTO getActionOwnerDetail(String projectId) {

		ActionOwnerDetailsDTO actionOwnerDetailsDTO = ichangemanagementdao.getProjectTeamMembersDetails(projectId);
		if (null == actionOwnerDetailsDTO) {
			actionOwnerDetailsDTO = new ActionOwnerDetailsDTO();
		}
		actionOwnerDetailsDTO.setAvailableUser(ichangemanagementdao.getRealTeackUserDetails(projectId));
		return actionOwnerDetailsDTO;
	}

	@Override
	public ChangeActionDTO getECRDetails(String projectId, String ecrCode) {
		ChangeActionDTO changeActionDTO = new ChangeActionDTO();
		ChangeManagementDTO changeManagementDTO = ichangemanagementdao.getECRDetails(projectId, ecrCode);
		changeActionDTO.setSubProject(changeManagementDTO.getSubProject());
		changeActionDTO.setEcrCode(changeManagementDTO.getEcrCode());
		changeActionDTO.setActions("Complete revision in TCE");
		changeActionDTO.setOwnerSSO(changeManagementDTO.getEcrIssuerId());
		changeActionDTO.setOwner(changeManagementDTO.getEcrIssuerName());
		changeActionDTO.setDueDate(changeManagementDTO.getApproverDescDate());
		changeActionDTO.setStatus("In Progress");
		changeActionDTO.setEngAction("Y");
		changeActionDTO.setAssessmentDescription(changeManagementDTO.getAssementDescription());
		if (null != changeManagementDTO.getClosure() && "Closed".equalsIgnoreCase(changeManagementDTO.getClosure())) {
			changeActionDTO.setStatus("Completed");
			changeActionDTO.setActualClosureDate(changeManagementDTO.getTodayDate());
		}
		return changeActionDTO;
	}

	@Override
	public Map<String, Object> getChangeRequestActionData(String projectId, String jobNumber, String changeActionId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ChangeActionDTO changeRequestActionDTO = new ChangeActionDTO();
		changeRequestActionDTO = ichangemanagementdao.getChangeRequestActionData(projectId, jobNumber, changeActionId);
		if (null != changeRequestActionDTO) {
			responseMap.put("changeRequestActionDetails", changeRequestActionDTO);
		}
		return responseMap;
	}

	@Override
	public Map<String, String> saveChangeRequestAction(ChangeActionDTO editChangeRequestActionDTO) {
		Map<String, String> responseMap = valdiateChangeRequestAction(editChangeRequestActionDTO);
		boolean saveFlag = false;
		if (responseMap.isEmpty()) {
			saveFlag = ichangemanagementdao.saveChangeRequestAction(editChangeRequestActionDTO);
			if (saveFlag) {
				responseMap.put("status", "success");
				responseMap.put("message", "Change request action saved successfully");
			} else if (!saveFlag) {
				responseMap.put("status", "error");
				responseMap.put("message", "Error while saving change request action");
			}
		}
		return responseMap;
	}

	private Map<String, String> valdiateChangeRequestAction(ChangeActionDTO editChangeRequestActionDTO) {
		Map<String, String> validationMap = new HashMap<String, String>();
		String ownerDetail = null;
		if (null != editChangeRequestActionDTO) {
			if (null == editChangeRequestActionDTO.getActions()
					|| "".equalsIgnoreCase(editChangeRequestActionDTO.getActions().trim())) {
				validationMap.put("failure", "Please enter Action value");
				return validationMap;
			} else if (null == editChangeRequestActionDTO.getOwner()
					|| "".equalsIgnoreCase(editChangeRequestActionDTO.getOwner().trim())) {
				validationMap.put("failure", "Please enter Owner value");
				return validationMap;
			} else if (null == editChangeRequestActionDTO.getDueDate()
					|| "".equalsIgnoreCase(editChangeRequestActionDTO.getDueDate().trim())) {
				validationMap.put("failure", "Please enter Due Date value");
				return validationMap;
			} else if (!validDateFormat(editChangeRequestActionDTO.getDueDate())) {
				validationMap.put("failure", "Please enter Due Date in DD-MMM-YYYY format");
				return validationMap;
			} else if ((null == editChangeRequestActionDTO.getStatus()
					|| "".equalsIgnoreCase(editChangeRequestActionDTO.getStatus().trim()))
					&& (null == editChangeRequestActionDTO.getActualClosureDate()
							|| "".equalsIgnoreCase(editChangeRequestActionDTO.getActualClosureDate().trim()))) {
				validationMap.put("failure", "Please enter Status value");
				return validationMap;
			} else if (null != editChangeRequestActionDTO.getStatus()
					&& "Completed".equalsIgnoreCase(editChangeRequestActionDTO.getStatus())) {
				if (null == editChangeRequestActionDTO.getActualClosureDate()
						|| "".equalsIgnoreCase(editChangeRequestActionDTO.getActualClosureDate().trim())) {
					validationMap.put("failure", "Actual closure Date is mandatory when  Status is Completed");
					return validationMap;
				} else if (!validDateFormat(editChangeRequestActionDTO.getActualClosureDate())) {
					validationMap.put("failure", "Please enter Actual closure Date in DD-MMM-YYYY format");
					return validationMap;
				}
			}
			ownerDetail = editChangeRequestActionDTO.getOwner();
			if (ownerDetail.contains("(") && ownerDetail.contains(")")) {
				editChangeRequestActionDTO.setOwner(ownerDetail.substring(0, ownerDetail.indexOf("(")));
				editChangeRequestActionDTO
						.setOwnerSSO(ownerDetail.substring(ownerDetail.indexOf("(") + 1, ownerDetail.indexOf(")")));
			}
		} else {
			validationMap.put("failure", "Change Request Action Is Empty");
		}
		return validationMap;
	}

	public Map<String, String> getChangeRequestSumaryCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		Map<String, String> responseMap = new HashMap<String, String>();

		responseMap.put("totalECR", ichangemanagementdao.getTotalECRCount(changeSummaryRequestDTO));
		responseMap.put("noAction", ichangemanagementdao.getTotalNoActionECRCount(changeSummaryRequestDTO));
		responseMap.put("inprogressAction",
				ichangemanagementdao.getTotalInProgressActionECRCount(changeSummaryRequestDTO));
		responseMap.put("allClosedAction",
				ichangemanagementdao.getTotalAllCompletedActionECRCount(changeSummaryRequestDTO));

		return responseMap;
	}

	public Map<String, String> getChangeSummaryAssessedImpact(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		Map<String, String> responseMap = ichangemanagementdao.getECRAssessedImpactDetails(changeSummaryRequestDTO);

		return responseMap;
	}

	public Map<String, String> getChangeSummaryAgingECR(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		Map<String, String> responseMap = ichangemanagementdao.getECRAgingDetails(changeSummaryRequestDTO);

		return responseMap;
	}

	public List<ChangeManagementDTO> getSummaryPopupDetails(ChangeSummaryRequestDTO changeSummaryRequestDTO) {

		return ichangemanagementdao.getSummary(changeSummaryRequestDTO);
	}

	public Map<String, String> getChangeRequestCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		Map<String, String> responseMap = new HashMap<String, String>();

		responseMap.put("totalAction", ichangemanagementdao.getTotalActionCount(changeSummaryRequestDTO));
		responseMap.put("completed", ichangemanagementdao.getTotalCompletedActionCount(changeSummaryRequestDTO));
		responseMap.put("pending", ichangemanagementdao.getTotalInProgressActionCount(changeSummaryRequestDTO));

		return responseMap;
	}

	public Map<String, String> getChangeRequestSayDOChart(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		Map<String, String> responseMap = ichangemanagementdao.getActionSayDODetails(changeSummaryRequestDTO);

		return responseMap;
	}

	public Map<String, Map<String, String>> getPendingActionLookHead(ChangeSummaryRequestDTO changeSummaryRequestDTO) {

		Map<String, Map<String, String>> responseMap = ichangemanagementdao
				.getPendingActionLookaheadDetails(changeSummaryRequestDTO);

		return responseMap;
	}

}
