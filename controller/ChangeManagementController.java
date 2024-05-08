package com.bh.realtrack.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.ChangeActionDTO;
import com.bh.realtrack.dto.ChangeSummaryRequestDTO;
import com.bh.realtrack.dto.EditChangeActionDTO;
import com.bh.realtrack.service.IChangeManagementService;
import com.bh.realtrack.util.AssertUtils;

@RestController
@CrossOrigin
@RequestMapping("api/v1/changemanagement")

public class ChangeManagementController {

	private static Logger log = LoggerFactory.getLogger(ChangeManagementController.class.getName());

	@Autowired
	IChangeManagementService ichangemanagementservice;

	@RequestMapping(value = "/getChangeManagementFilter", method = RequestMethod.GET)
	public Map<String, Object> getChangeManagementFilter(@RequestHeader HttpHeaders headers,
			@RequestParam("projectId") String projectId) {
		return ichangemanagementservice.getChangeManagementFilter(projectId);
	}

	@RequestMapping(value = "/getSummary", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getSummary(@RequestBody ChangeSummaryRequestDTO changeSummaryRequestDTO) {

		Map<String, Object> summaryData = new HashMap<String, Object>();
		summaryData.put("summary", ichangemanagementservice.getSummary(changeSummaryRequestDTO));
		summaryData.put("changeSummaryRefreshDate",
				ichangemanagementservice.getChangeSummaryLastUpdateDate(changeSummaryRequestDTO.getProjectId()));
		summaryData.put("changeRequestSummary",
				ichangemanagementservice.getChangeRequestSumaryCount(changeSummaryRequestDTO));
		summaryData.put("assessedImpact",
				ichangemanagementservice.getChangeSummaryAssessedImpact(changeSummaryRequestDTO));
		summaryData.put("agingECR", ichangemanagementservice.getChangeSummaryAgingECR(changeSummaryRequestDTO));

		return summaryData;
	}

	@RequestMapping(value = "/getChangeSummaryDetailsPopup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getSummaryDetailsPopup(@RequestBody ChangeSummaryRequestDTO changeSummaryRequestDTO) {

		Map<String, Object> summaryData = new HashMap<String, Object>();
		changeSummaryRequestDTO.setLevel("SUMMARY");
		summaryData.put("chartData", ichangemanagementservice.getSummaryPopupDetails(changeSummaryRequestDTO));

		return summaryData;
	}

	@RequestMapping(value = "/getChangerequest", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getChangeRequestAction(@RequestBody ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		Map<String, Object> changeRequest = new HashMap<String, Object>();

		changeRequest.put("changeRequest", ichangemanagementservice.getChangeRequest(changeSummaryRequestDTO));
		changeRequest.put("changeRequestRefreshDate",
				ichangemanagementservice.getChangeActionLastUpdateDate(changeSummaryRequestDTO.getProjectId()));
		changeRequest.put("changeRequestCount",
				ichangemanagementservice.getChangeRequestCount(changeSummaryRequestDTO));
		changeRequest.put("sayDOChart", ichangemanagementservice.getChangeRequestSayDOChart(changeSummaryRequestDTO));
		changeRequest.put("pendingActionLookHead",
				ichangemanagementservice.getPendingActionLookHead(changeSummaryRequestDTO));

		return changeRequest;
	}

	@RequestMapping(value = "/getChangeRequestDetailsPopup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getChangeRequestDetailsPopup(
			@RequestBody ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		Map<String, Object> changeRequest = new HashMap<String, Object>();

		changeSummaryRequestDTO.setLevel("ACTION");
		changeRequest.put("chartData", ichangemanagementservice.getChangeRequest(changeSummaryRequestDTO));

		return changeRequest;
	}

	@RequestMapping(value = "/getChangerequestByEcr", method = RequestMethod.GET)
	public Map<String, Object> getChangeactiondata(@RequestHeader HttpHeaders headers,
			@RequestParam("projectId") String projectId, @RequestParam("ecrCode") String ecrCode) {
		Map<String, Object> changeRequestData = new HashMap<String, Object>();
		changeRequestData.put("changeRequestData", ichangemanagementservice.getChangeDataforEcr(projectId, ecrCode));
		changeRequestData.put("hideEngAction", "No");
		return changeRequestData;
	}

	@RequestMapping(value = "/getChangeRequestEngAction", method = RequestMethod.GET)
	public ChangeActionDTO getChangeRequestEngAction(@RequestHeader HttpHeaders headers,
			@RequestParam("projectId") String projectId, @RequestParam("ecrCode") String ecrCode) {
		ChangeActionDTO changeActionDTO = ichangemanagementservice.getECRDetails(projectId, ecrCode);
		return changeActionDTO;
	}

	@RequestMapping(value = "/deleteRequestaction", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> deleteRequestaction(@RequestParam("changeActionId") String changeActionId,
			@RequestParam("projectId") String projectId) {
		Map<String, String> responseMap = new HashMap<String, String>();
		if (null != changeActionId && !changeActionId.contains("AD_")) {
			responseMap = ichangemanagementservice.deleteChangeRequest(changeActionId, projectId);
		} else {
			responseMap.put("status", "success");
		}
		return responseMap;
	}

	@RequestMapping(value = "/saveChangerequest", method = RequestMethod.POST, consumes = "application/json", headers = "Accept=application/json")
	public @ResponseBody Map<String, String> saveChangeRequestData(
			@RequestBody EditChangeActionDTO editChangeActionDTO) {
		return ichangemanagementservice.saveChangeRequestData(editChangeActionDTO);
	}

	@RequestMapping(value = "/getActionOwnerDetail", method = RequestMethod.GET)
	public Map<String, Object> getActionOwnerDetail(@RequestHeader HttpHeaders headers,
			@RequestParam("projectId") String projectId) {
		Map<String, Object> changeRequest = new HashMap<String, Object>();
		changeRequest.put("actionOwnerDetail", ichangemanagementservice.getActionOwnerDetail(projectId));
		return changeRequest;
	}

	@RequestMapping(value = "/downloadChangeManagementDetails", method = RequestMethod.GET)
	public void downloadChangeManagementDetails(@RequestParam final String projectId,
			@RequestParam("jobNumber") String jobNumber, @RequestParam final String phase,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "CHANGE_MANAGEMENT_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] changeManagementDetails = ichangemanagementservice.downloadChangeManagementDetails(projectId,
					jobNumber, phase);
			IOUtils.write(changeManagementDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading Change Management Excel file" + e.getMessage());
		}
	}

	@RequestMapping(value = "/getChangeRequestActionData", method = RequestMethod.GET)
	public Map<String, Object> getChangeRequestActionData(@RequestHeader HttpHeaders headers,
			@RequestParam("projectId") String projectId, @RequestParam("jobNumber") String jobNumber,
			@RequestParam("changeActionId") String changeActionId) {
		return ichangemanagementservice.getChangeRequestActionData(projectId, jobNumber, changeActionId);
	}

	@RequestMapping(value = "/saveChangeRequestAction", method = RequestMethod.POST, consumes = "application/json", headers = "Accept=application/json")
	public @ResponseBody Map<String, String> saveChangeRequestAction(
			@RequestBody ChangeActionDTO editChangeRequestActionDTO) {
		return ichangemanagementservice.saveChangeRequestAction(editChangeRequestActionDTO);
	}
}
