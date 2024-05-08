package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ProjectConsoleApplicableFlagDTO;
import com.bh.realtrack.dto.ProjectConsoleBillingStatusDTO;
import com.bh.realtrack.dto.ProjectConsoleCMDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessBomDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessPackingDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleConfiguratorDTO;
import com.bh.realtrack.dto.ProjectConsoleDeckContractualDeliveryDTO;
import com.bh.realtrack.dto.ProjectConsoleDeckStatusDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleEngDocStatusDTO;
import com.bh.realtrack.dto.ProjectConsoleFinancialDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleInspectionDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleNextToBillDTO;
import com.bh.realtrack.dto.ProjectConsoleProcurementDeliveryDataDTO;
import com.bh.realtrack.dto.ProjectConsoleProcurementPlacementDataDTO;
import com.bh.realtrack.dto.ProjectConsoleProjectDetailsDTO;
import com.bh.realtrack.dto.ProjectConsolePunchListDTO;
import com.bh.realtrack.dto.ProjectConsoleScurveDataDTO;
import com.bh.realtrack.dto.ProjectConsoleScurveTableDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleShipmentDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleWeightDataDTO;

public interface IProjectControlDAO {

	ProjectConsoleApplicableFlagDTO checkProjectConsoleApplicableFlag(String projectId, String sso);

	ProjectConsoleProjectDetailsDTO getProjectConsoleProjectDetails(String projectId, String businessUnit);

	ProjectConsoleCMDetailsDTO getProjectConsoleCMAnalysisDetails(String projectId);

	ProjectConsoleFinancialDetailsDTO getProjectConsoleFinancialSummaryDetails(String projectId);

	Map<String, Object> getProjectConsoleCashCollectionDetails(String projectId);

	Map<String, String> getProjectConsoleEngineeringDocPendingStatus(String projectId);

	ProjectConsoleEngDocStatusDTO getProjectConsoleEngineeringDocStatus(String projectId);

	ProjectConsolePunchListDTO getProjectConsolePunchListDetails(String projectId);

	ProjectConsoleShipmentDetailsDTO getProjectConsoleShipmentDetails(String projectId);

	Map<String, String> getProjectConsoleTotalRisks(String projectId, String sso);

	ProjectConsoleCompletenessBomDetailsDTO getProjectConsoleCompletenessBomDetails(String projectId);

	ProjectConsoleCompletenessPackingDetailsDTO getProjectConsoleCompletenessPackingDetails(String projectId,
			boolean isModuleProject);

	List<String> getModuleProjects(String projectId);

	boolean checkProjectConsoleExceptionProjects(String projectId, String sso);

	ProjectConsoleProcurementPlacementDataDTO getProjectConsoleProcurementPlacementDetails(String projectId,
			boolean customizedFlag);

	ProjectConsoleProcurementDeliveryDataDTO getProjectConsoleProcurementDeliveryDetails(String projectId,
			boolean customizedFlag);

	ProjectConsoleInspectionDetailsDTO getProjectConsoleInspectionDetails(String projectId);

	List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingITOBaselineCurve(String projectId);

	List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingActualCurve(String projectId);

	List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingBaselineCurve(String projectId);

	List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingBlankForecast(String projectId);

	List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingFinancialBLCurve(String projectId);

	List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingForecastCurve(String projectId);

	String getProjectConsoleBillingNextToBillDetails(String projectId);

	String getProjectConsoleBillingNextToBillInDays(String projectId);

	List<ProjectConsoleNextToBillDTO> getProjectConsoleBillingNextToBillList(String projectId);

	ProjectConsoleConfiguratorDTO getProjectConsoleSCurveSelectedDetails(String projectId);

	List<ProjectConsoleScurveDataDTO> getProjectConsoleSCurveDetails(String projectId, String jobs,
			String epsContractId, String customWeight, List<String> projectDisplayInfoList);

	List<ProjectConsoleScurveTableDetailsDTO> getProjectConsoleSCurveTableDetails(String projectId, String jobs,
			String epsContractId, String customWeight);

	List<ProjectConsoleScurveDataDTO> getProjectConsoleWeightedSCurveDetails(String projectId, String jobs,
			String epsContractId, String customWeight, List<String> projectDisplayInfoList);

	List<ProjectConsoleScurveTableDetailsDTO> getProjectConsoleWeightedSCurveTableDetails(String projectId, String jobs,
			String epsContractId, String customWeight);

	String getProjectConsoleScurveEPSContractDetails(String projectId);

	String getProjectConsoleScurveSubProjectDetails(String projectId);

	String getProjectConsoleScurveCustomWeightDetails(String projectId);

	ProjectConsoleWeightDataDTO getProjectConsoleScurveWeightDetails(String projectId);

	List<String> getProjectConsoleScurveToShowDetails(String projectId);

	List<ProjectConsoleScurveDataDTO> getProjectConsoleCustomizedSCurveDetails(String projectId, String jobs,
			String epsContractId, String customWeight, List<String> projectDisplayInfoList);

	List<ProjectConsoleScurveTableDetailsDTO> getProjectConsoleCustomizedSCurveTableDetails(String projectId,
			String jobs, String epsContractId, String customWeight);

	List<ProjectConsoleScurveDataDTO> getProjectConsoleCustomizedWeightedSCurveDetails(String projectId, String jobs,
			String epsContractId, String customWeight, List<String> projectDisplayInfoList);

	List<ProjectConsoleScurveTableDetailsDTO> getProjectConsoleCustomizedWeightedSCurveTableDetails(String projectId,
			String jobs, String epsContractId, String customWeight);

	ProjectConsoleDeckStatusDetailsDTO getProjectConsoleDeckStatusDetails(String projectId);

	List<ProjectConsoleDeckContractualDeliveryDTO> getProjectConsoleDeckContractualDeliveryDates(String projectId);

	String getProjectConsoleCheckBusinessUnit(String projectId);

	String getProjectConsoleQIRadarDetails(String projectId);

	String getProjectConsoleLessonLearnedCnt(String projectId);

	String getProjectConsoleLessonLearnedRiskCnt(String projectId);

}
