package com.bh.realtrack.service;

import java.util.Map;

import com.bh.realtrack.dto.ProjectConsoleApplicableFlagDTO;

public interface IProjectControlService {

	ProjectConsoleApplicableFlagDTO checkProjectConsoleApplicableFlag(String projectId);

	Map<String, Object> getProjectConsoleProjectDetails(String projectId);

	Map<String, Object> getProjectConsoleCMAnalysisDetails(String projectId);

	Map<String, Object> getProjectConsoleFinancialSummaryDetails(String projectId);

	Map<String, Object> getProjectConsoleCashCollectionDetails(String projectId);

	Map<String, Object> getProjectConsoleTotalRisks(String projectId);

	Map<String, Object> getProjectConsoleEngineeringDocStatus(String projectId);

	Map<String, Object> getProjectConsolePunchListDetails(String projectId);

	Map<String, Object> getProjectConsoleShipmentDetails(String projectId);

	Map<String, Object> getProjectConsoleCompletenessWidget(String projectId);

	Map<String, Object> getProjectConsoleProcurementDetails(String projectId);

	Map<String, Object> getProjectConsoleInspectionDetails(String projectId);

	Map<String, Object> getProjectConsoleBillingStatusDetails(String projectId);

	Map<String, Object> getProjectConsoleScurveDetails(String projectId);

	Map<String, Object> getProjectConsoleDeckStatusDetails(String projectId);

	Map<String, Object> getProjectConsoleDeckContractualDeliveryDates(String projectId);

	Map<String, Object> getProjectConsoleQIRadarDetails(String projectId);

}
