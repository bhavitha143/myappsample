package com.bh.realtrack.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.ProjectConsoleApplicableFlagDTO;
import com.bh.realtrack.service.IProjectControlService;

@RestController
@CrossOrigin
public class ProjectControlController {

	@Autowired
	IProjectControlService iProjectControlService;

	private static final Logger log = LoggerFactory.getLogger(ProjectControlController.class);

	@RequestMapping(value = "/checkProjectConsoleApplicableFlag", method = RequestMethod.GET)
	public ProjectConsoleApplicableFlagDTO checkProjectConsoleApplicableFlag(@RequestParam String projectId)
			throws Exception {
		return iProjectControlService.checkProjectConsoleApplicableFlag(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleProjectDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleProjectDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleProjectDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleCMAnalysisDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleCMAnalysisDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleCMAnalysisDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleFinancialSummaryDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleFinancialSummaryDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleFinancialSummaryDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleCashCollectionDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleCashCollectionDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleCashCollectionDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleTotalRisks", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleTotalRisks(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleTotalRisks(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleEngineeringDocStatus", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleEngineeringDocStatus(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleEngineeringDocStatus(projectId);
	}

	@RequestMapping(value = "/getProjectConsolePunchListDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsolePunchListDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsolePunchListDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleShipmentDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleShipmentDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleShipmentDetails(projectId);
	}

	@GetMapping("/getProjectConsoleCompletenessWidget")
	public Map<String, Object> getProjectConsoleCompletenessWidget(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleCompletenessWidget(projectId);
	}

	@GetMapping("/getProjectConsoleProcurementDetails")
	public Map<String, Object> getProjectConsoleProcurementDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleProcurementDetails(projectId);
	}

	@GetMapping("/getProjectConsoleInspectionDetails")
	public Map<String, Object> getProjectConsoleInspectionDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleInspectionDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleBillingStatusDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleBillingStatusDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleBillingStatusDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleScurveDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleScurveDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleScurveDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleDeckStatusDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleDeckStatusDetails(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleDeckStatusDetails(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleDeckContractualDeliveryDates", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleDeckContractualDeliveryDates(@RequestParam String projectId) {
		return iProjectControlService.getProjectConsoleDeckContractualDeliveryDates(projectId);
	}

	@RequestMapping(value = "/getProjectConsoleQIRadarDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectConsoleQIRadarDetails(@RequestParam String projectId) {
		log.debug("INIT- getProjectConsoleQIRadarDetails for projectId : {}", projectId);
		return iProjectControlService.getProjectConsoleQIRadarDetails(projectId);
	}

}
