package com.bh.realtrack.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IProjectControlDAO;
import com.bh.realtrack.dto.ProjectConsoleApplicableFlagDTO;
import com.bh.realtrack.dto.ProjectConsoleBillingStatusDTO;
import com.bh.realtrack.dto.ProjectConsoleCMDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessBomDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessPackingDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleConfiguratorDTO;
import com.bh.realtrack.dto.ProjectConsoleDeckContractualDeliveryDTO;
import com.bh.realtrack.dto.ProjectConsoleDeckStatusDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleEngDocStatusDTO;
import com.bh.realtrack.dto.ProjectConsoleFeaturesDTO;
import com.bh.realtrack.dto.ProjectConsoleFinancialDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleInspectionDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleNextToBillDTO;
import com.bh.realtrack.dto.ProjectConsoleProcurementDeliveryDataDTO;
import com.bh.realtrack.dto.ProjectConsoleProcurementPlacementDataDTO;
import com.bh.realtrack.dto.ProjectConsoleProjectDetailsDTO;
import com.bh.realtrack.dto.ProjectConsolePunchListDTO;
import com.bh.realtrack.dto.ProjectConsoleQIRadarDTO;
import com.bh.realtrack.dto.ProjectConsoleScurveDataDTO;
import com.bh.realtrack.dto.ProjectConsoleScurveTableDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleSelectedConfigDTO;
import com.bh.realtrack.dto.ProjectConsoleShipmentDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleStaticDataDTO;
import com.bh.realtrack.dto.ProjectConsoleUpdateCtrlPrimaryDTO;
import com.bh.realtrack.dto.ProjectConsoleViewsDTO;
import com.bh.realtrack.dto.ProjectConsoleWeightDataDTO;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IProjectControlService;
import com.bh.realtrack.service.helper.ProjectConsoleSCurveServiceHelper;

@Service
public class ProjectControlServiceImpl implements IProjectControlService {

	@Autowired
	IProjectControlDAO iProjectControlDAO;
	CallerContext callerContext;
	ProjectConsoleSCurveServiceHelper serviceHelper;

	private static final Logger log = LoggerFactory.getLogger(ProjectControlServiceImpl.class);

	@Autowired
	public ProjectControlServiceImpl(final IProjectControlDAO iProjectControlDAO, final CallerContext callerCxt,
			final ProjectConsoleSCurveServiceHelper serviceHelper) {
		this.iProjectControlDAO = iProjectControlDAO;
		this.callerContext = callerCxt;
		this.serviceHelper = serviceHelper;
	}

	@Override
	public ProjectConsoleApplicableFlagDTO checkProjectConsoleApplicableFlag(String projectId) {
		ProjectConsoleApplicableFlagDTO dto = new ProjectConsoleApplicableFlagDTO();
		String sso = callerContext.getName();
		try {
			dto = iProjectControlDAO.checkProjectConsoleApplicableFlag(projectId, sso);
		} catch (Exception e) {
			log.error("checkProjectConsoleApplicableFlag(): Exception occurred : " + e.getMessage());
		}
		return dto;

	}

	@Override
	public Map<String, Object> getProjectConsoleProjectDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleProjectDetailsDTO data = new ProjectConsoleProjectDetailsDTO();
		ProjectConsoleQIRadarDTO dto = new ProjectConsoleQIRadarDTO();
		String businessUnit = "";
		try {
			businessUnit = iProjectControlDAO.getProjectConsoleCheckBusinessUnit(projectId);
			data = iProjectControlDAO.getProjectConsoleProjectDetails(projectId, businessUnit);
			if (null != businessUnit && !businessUnit.isEmpty() && businessUnit.equalsIgnoreCase("SRV")) {
				dto.setProjectBusinessUnit(businessUnit);
				dto.setWidgetFlag("Y");
			} else {
				dto.setProjectBusinessUnit(businessUnit);
				dto.setWidgetFlag("N");
			}
			responseMap.put("data", data);
			responseMap.put("flagDetails", dto);
		} catch (Exception e) {
			log.error("getProjectConsoleProjectDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleCMAnalysisDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleCMDetailsDTO data = new ProjectConsoleCMDetailsDTO();
		try {
			data = iProjectControlDAO.getProjectConsoleCMAnalysisDetails(projectId);
			responseMap.put("data", data);
		} catch (Exception e) {
			log.error("getProjectConsoleCMAnalysisDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleFinancialSummaryDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleFinancialDetailsDTO data = new ProjectConsoleFinancialDetailsDTO();
		try {
			data = iProjectControlDAO.getProjectConsoleFinancialSummaryDetails(projectId);
			responseMap.put("data", data);
		} catch (Exception e) {
			log.error("getProjectConsoleFinancialSummaryDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleCashCollectionDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> chartMap = new HashMap<String, Object>();
		try {
			if (projectId != null) {
				chartMap = iProjectControlDAO.getProjectConsoleCashCollectionDetails(projectId);
				responseMap.put("data", chartMap);
			} else {
				throw new Exception("getProjectConsoleCashCollectionDetails(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getProjectConsoleCashCollectionDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleEngineeringDocStatus(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> pendingCountMap = new HashMap<String, String>();
		ProjectConsoleEngDocStatusDTO list = new ProjectConsoleEngDocStatusDTO();
		try {
			if (projectId != null) {
				list = iProjectControlDAO.getProjectConsoleEngineeringDocStatus(projectId);
				pendingCountMap = iProjectControlDAO.getProjectConsoleEngineeringDocPendingStatus(projectId);
				responseMap.put("data", list);
				responseMap.put("pendingStatus", pendingCountMap);
			} else {
				throw new Exception("getProjectConsoleEngineeringDocStatus(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getProjectConsoleEngineeringDocStatus(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsolePunchListDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsolePunchListDTO list = new ProjectConsolePunchListDTO();
		try {
			if (projectId != null) {
				list = iProjectControlDAO.getProjectConsolePunchListDetails(projectId);
				responseMap.put("data", list);
			} else {
				throw new Exception("getProjectConsolePunchListDetails(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getProjectConsolePunchListDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleTotalRisks(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> totalRisks = new HashMap<String, String>();
		String lessonLearnedRiskCnt = "", lessonLearnedCnt = "";
		String insertLessonLearnedUrl = "", searchLessonLearnedUrl = "";
		String sso = callerContext.getName();
		String businessUnit = "";
		try {
			totalRisks = iProjectControlDAO.getProjectConsoleTotalRisks(projectId, sso);
			insertLessonLearnedUrl = "https://apps.powerapps.com/play/b3190cd2-5ea2-4ebb-9b1a-6babe73f3643?tenantId=d584a4b7-b1f2-4714-a578-fd4d43c146a6";
			searchLessonLearnedUrl = "https://bakerhughes.sharepoint.com/:x:/r/sites/BHTPSLessonLearned/LL%20Validation%20WF/Shared%20Documents/Lesson%20Learned%20Validated%20.xlsx?d=w5387a58c7192434f82109b91195c469e&csf=1&web=1";
			businessUnit = iProjectControlDAO.getProjectConsoleCheckBusinessUnit(projectId);
			if (null != businessUnit && !businessUnit.isEmpty() && businessUnit.equalsIgnoreCase("SRV")) {
				lessonLearnedCnt = iProjectControlDAO.getProjectConsoleLessonLearnedCnt(projectId);
				lessonLearnedRiskCnt = iProjectControlDAO.getProjectConsoleLessonLearnedRiskCnt(projectId);
				responseMap.put("lessonLearnedCnt", lessonLearnedCnt);
				responseMap.put("lessonLearnedRiskCnt", lessonLearnedRiskCnt);
			}
			responseMap.put("RiskSummary", totalRisks);
			responseMap.put("InsertLessonsLearnedURL", insertLessonLearnedUrl);
			responseMap.put("SearchLessonsLearnedURL", searchLessonLearnedUrl);
		} catch (Exception e) {
			log.error("getProjectConsoleTotalRisks(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleShipmentDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleShipmentDetailsDTO responseDTO = new ProjectConsoleShipmentDetailsDTO();
		try {
			if (projectId != null) {
				responseDTO = iProjectControlDAO.getProjectConsoleShipmentDetails(projectId);
				if (responseDTO.getMaterialToBePacked() > 0 || responseDTO.getMaterialToBeRecived() > 0
						|| responseDTO.getBoxesPacked() > 0 || responseDTO.getBoxesShipped() > 0
						|| responseDTO.getReadyToShip() > 0) {
					responseMap.put("data", responseDTO);
				} else {
					responseMap.put("data", "");
				}
			} else {
				throw new Exception("getProjectConsoleShipmentDetails(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getProjectConsoleShipmentDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleCompletenessWidget(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleCompletenessBomDetailsDTO bomDetails = new ProjectConsoleCompletenessBomDetailsDTO();
		ProjectConsoleCompletenessPackingDetailsDTO packingDetails = new ProjectConsoleCompletenessPackingDetailsDTO();
		boolean isModuleProject = checkModuleProject(projectId);
		String pkgActPercentString = null;
		double pkgActPercent = 0;
		try {
			bomDetails = iProjectControlDAO.getProjectConsoleCompletenessBomDetails(projectId);
			packingDetails = iProjectControlDAO.getProjectConsoleCompletenessPackingDetails(projectId, isModuleProject);
			responseMap.put("bomDetails", bomDetails);
			responseMap.put("packingDetails", packingDetails);
			if (null != packingDetails && null != packingDetails.getPkgActualPercent()
					&& !"".equalsIgnoreCase(packingDetails.getPkgActualPercent())) {
				pkgActPercentString = packingDetails.getPkgActualPercent();
				pkgActPercent = Double.parseDouble(pkgActPercentString);
				if (pkgActPercent < 0 || pkgActPercent > 100) {
					responseMap.put("dataErrorMessage", "Please check BOM Structure vs Shipping Sales Order creation");
				}
			}
		} catch (Exception e) {
			log.error("getProjectConsoleCompletenessWidget(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	private boolean checkModuleProject(String projectId) {
		boolean isModuleProject = false;
		List<String> projectIdList = iProjectControlDAO.getModuleProjects(projectId);
		if (null != projectIdList && projectIdList.contains(projectId)) {
			isModuleProject = true;
		}
		return isModuleProject;
	}

	@Override
	public Map<String, Object> getProjectConsoleProcurementDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleProcurementPlacementDataDTO placementDetails = new ProjectConsoleProcurementPlacementDataDTO();
		ProjectConsoleProcurementDeliveryDataDTO deliveryDetails = new ProjectConsoleProcurementDeliveryDataDTO();
		try {
			String sso = callerContext.getName();
			boolean customizedFlag = iProjectControlDAO.checkProjectConsoleExceptionProjects(projectId, sso);
			log.info("projectId :: " + projectId + " :: customizedFlag :: " + customizedFlag);
			placementDetails = iProjectControlDAO.getProjectConsoleProcurementPlacementDetails(projectId,
					customizedFlag);
			deliveryDetails = iProjectControlDAO.getProjectConsoleProcurementDeliveryDetails(projectId, customizedFlag);
			responseMap.put("placementData", placementDetails);
			responseMap.put("deliveryData", deliveryDetails);
		} catch (Exception e) {
			log.error("getProjectConsoleProcurementDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleInspectionDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleInspectionDetailsDTO dto = new ProjectConsoleInspectionDetailsDTO();
		try {
			dto = iProjectControlDAO.getProjectConsoleInspectionDetails(projectId);
			responseMap.put("data", dto);
		} catch (Exception e) {
			log.error("getProjectConsoleInspectionDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleBillingStatusDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ProjectConsoleBillingStatusDTO> itoBaselineCurve = new ArrayList<ProjectConsoleBillingStatusDTO>();
		List<ProjectConsoleBillingStatusDTO> actualCurve = new ArrayList<ProjectConsoleBillingStatusDTO>();
		List<ProjectConsoleBillingStatusDTO> baselineCurve = new ArrayList<ProjectConsoleBillingStatusDTO>();
		List<ProjectConsoleBillingStatusDTO> blankForecast = new ArrayList<ProjectConsoleBillingStatusDTO>();
		List<ProjectConsoleBillingStatusDTO> financialBLCurve = new ArrayList<ProjectConsoleBillingStatusDTO>();
		List<ProjectConsoleBillingStatusDTO> forecastCurve = new ArrayList<ProjectConsoleBillingStatusDTO>();
		List<ProjectConsoleNextToBillDTO> nextToBillList = new ArrayList<ProjectConsoleNextToBillDTO>();
		String nextToBill = "", inDays = "";
		try {
			if (projectId != null) {
				itoBaselineCurve = iProjectControlDAO.getProjectConsoleBillingITOBaselineCurve(projectId);
				actualCurve = iProjectControlDAO.getProjectConsoleBillingActualCurve(projectId);
				baselineCurve = iProjectControlDAO.getProjectConsoleBillingBaselineCurve(projectId);
				financialBLCurve = iProjectControlDAO.getProjectConsoleBillingFinancialBLCurve(projectId);
				forecastCurve = iProjectControlDAO.getProjectConsoleBillingForecastCurve(projectId);
				nextToBill = iProjectControlDAO.getProjectConsoleBillingNextToBillDetails(projectId);
				inDays = iProjectControlDAO.getProjectConsoleBillingNextToBillInDays(projectId);
				nextToBillList = iProjectControlDAO.getProjectConsoleBillingNextToBillList(projectId);
				if (actualCurve.size() > 1 || forecastCurve.size() > 1 || baselineCurve.size() > 1
						|| financialBLCurve.size() > 1 || itoBaselineCurve.size() > 1) {
					blankForecast = iProjectControlDAO.getProjectConsoleBillingBlankForecast(projectId);
				}
			}
			responseMap.put("itoBaselineCurve", itoBaselineCurve);
			responseMap.put("actualCurve", actualCurve);
			responseMap.put("baselineCurve", baselineCurve);
			responseMap.put("blankForecast", blankForecast);
			responseMap.put("financialBLCurve", financialBLCurve);
			responseMap.put("forecastCurve", forecastCurve);
			responseMap.put("nextToBill", nextToBill);
			responseMap.put("inDays", inDays);
			responseMap.put("nextToBillList", nextToBillList);
		} catch (Exception e) {
			log.error("getProjectConsoleBillingStatusDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleScurveDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> selectedConfigResponseMap = new HashMap<String, Object>();
		List<ProjectConsoleScurveDataDTO> lastUpdateDailyPercentage = new ArrayList<ProjectConsoleScurveDataDTO>();
		List<ProjectConsoleUpdateCtrlPrimaryDTO> dataList = new ArrayList<ProjectConsoleUpdateCtrlPrimaryDTO>();
		List<ProjectConsoleScurveTableDetailsDTO> tableData = new ArrayList<ProjectConsoleScurveTableDetailsDTO>();
		List<String> projectDisplayInfoList = new ArrayList<String>();
		List<String> scurveToShowList = new ArrayList<String>();
		ProjectConsoleSelectedConfigDTO selectedConfigResponseDTO = new ProjectConsoleSelectedConfigDTO();
		ProjectConsoleWeightDataDTO weightData = new ProjectConsoleWeightDataDTO();
		String epsContractId = "", customWeight = "", jobs = "", wbsCustomWt = "", wbsOverallWt = "";
		try {
			String sso = callerContext.getName();
			boolean customizedFlag = iProjectControlDAO.checkProjectConsoleExceptionProjects(projectId, sso);
			log.info("projectId :: " + projectId + " :: customizedFlag :: " + customizedFlag);
			if (customizedFlag) {
				jobs = iProjectControlDAO.getProjectConsoleScurveSubProjectDetails(projectId);
				weightData = iProjectControlDAO.getProjectConsoleScurveWeightDetails(projectId);
				customWeight = iProjectControlDAO.getProjectConsoleScurveCustomWeightDetails(projectId);
				projectDisplayInfoList = iProjectControlDAO.getProjectConsoleScurveToShowDetails(projectId);
				epsContractId = iProjectControlDAO.getProjectConsoleScurveEPSContractDetails(projectId);
				wbsCustomWt = weightData.getWbsCustomWeight();
				wbsOverallWt = weightData.getWbsOverallWeight();
				log.info("projectId :: " + projectId + " :: epsContractId :: " + epsContractId + " :: subProjects :: "
						+ jobs);
				log.info("customWeight :: " + customWeight + " :: wbsCustomWt :: " + wbsCustomWt
						+ " :: wbsOverallWt :: " + wbsOverallWt);
				if (wbsCustomWt.equalsIgnoreCase("yes") && wbsOverallWt.equalsIgnoreCase("yes")) {
					lastUpdateDailyPercentage = iProjectControlDAO.getProjectConsoleCustomizedWeightedSCurveDetails(
							projectId, jobs, epsContractId, customWeight, projectDisplayInfoList);
					tableData = iProjectControlDAO.getProjectConsoleCustomizedWeightedSCurveTableDetails(projectId,
							jobs, epsContractId, customWeight);
				} else if (wbsCustomWt.equalsIgnoreCase("yes") && wbsOverallWt.equalsIgnoreCase("no")) {
					lastUpdateDailyPercentage = iProjectControlDAO.getProjectConsoleCustomizedWeightedSCurveDetails(
							projectId, jobs, epsContractId, customWeight, projectDisplayInfoList);
					tableData = iProjectControlDAO.getProjectConsoleCustomizedWeightedSCurveTableDetails(projectId,
							jobs, epsContractId, customWeight);
				} else {
					lastUpdateDailyPercentage = iProjectControlDAO.getProjectConsoleCustomizedSCurveDetails(projectId,
							jobs, epsContractId, customWeight, projectDisplayInfoList);
					tableData = iProjectControlDAO.getProjectConsoleCustomizedSCurveTableDetails(projectId, jobs,
							epsContractId, customWeight);
				}
			} else {
				selectedConfigResponseMap = getProjectConsoleSCurveSelectedDetails(projectId, "Internal");
				selectedConfigResponseDTO = (ProjectConsoleSelectedConfigDTO) selectedConfigResponseMap.get("data");
				dataList = selectedConfigResponseDTO.selectedData;
				weightData = selectedConfigResponseDTO.weightData;
				customWeight = selectedConfigResponseDTO.staticData.getCustomWeight();
				scurveToShowList = selectedConfigResponseDTO.staticData.getsCurvesToShow();
				epsContractId = selectedConfigResponseDTO.selectedData.get(0).getEpsContractId();
				wbsCustomWt = weightData.wbsCustomWeight;
				wbsOverallWt = weightData.wbsOverallWeight;
				for (ProjectConsoleUpdateCtrlPrimaryDTO obj : dataList) {
					if (jobs.equalsIgnoreCase("")) {
						jobs = obj.getSubProject();
					} else {
						jobs = jobs + ";" + obj.getSubProject();
					}
					if (wbsCustomWt.equalsIgnoreCase("yes") && wbsOverallWt.equalsIgnoreCase("yes")) {
						jobs = jobs + ";" + obj.getSubProject() + "-ENG";
					}
				}
				for (String curve : scurveToShowList) {
					String scurveToShow = "";
					scurveToShow = curve;
					if (curve.equalsIgnoreCase("At Complete")) {
						scurveToShow = "Forecast";
					} else if (curve.equalsIgnoreCase("At Complete Late")) {
						scurveToShow = "Late Forecast";
					}
					projectDisplayInfoList.add(scurveToShow);
				}
				projectDisplayInfoList.add("SPI");
				projectDisplayInfoList.add("TFI");
				log.info("projectId :: " + projectId + " :: epsContractId :: " + epsContractId + " :: subProjects :: "
						+ jobs);
				log.info("customWeight :: " + customWeight + " :: wbsCustomWt :: " + wbsCustomWt
						+ " :: wbsOverallWt :: " + wbsOverallWt);
				if (wbsCustomWt.equalsIgnoreCase("yes") && wbsOverallWt.equalsIgnoreCase("yes")) {
					lastUpdateDailyPercentage = iProjectControlDAO.getProjectConsoleWeightedSCurveDetails(projectId,
							jobs, epsContractId, customWeight, projectDisplayInfoList);
					tableData = iProjectControlDAO.getProjectConsoleWeightedSCurveTableDetails(projectId, jobs,
							epsContractId, customWeight);
				} else if (wbsCustomWt.equalsIgnoreCase("yes") && wbsOverallWt.equalsIgnoreCase("no")) {
					lastUpdateDailyPercentage = iProjectControlDAO.getProjectConsoleWeightedSCurveDetails(projectId,
							jobs, epsContractId, customWeight, projectDisplayInfoList);
					tableData = iProjectControlDAO.getProjectConsoleWeightedSCurveTableDetails(projectId, jobs,
							epsContractId, customWeight);
				} else {
					lastUpdateDailyPercentage = iProjectControlDAO.getProjectConsoleSCurveDetails(projectId, jobs,
							epsContractId, customWeight, projectDisplayInfoList);
					tableData = iProjectControlDAO.getProjectConsoleSCurveTableDetails(projectId, jobs, epsContractId,
							customWeight);
				}
			}
			responseMap.put("data", lastUpdateDailyPercentage);
			responseMap.put("displayList", projectDisplayInfoList);
			responseMap.put("tableData", tableData);
		} catch (Exception e) {
			responseMap.put("data", lastUpdateDailyPercentage);
			log.error("getProjectConsoleScurveDetails :: " + e.getMessage());
		}
		return responseMap;
	}

	private Map<String, Object> getProjectConsoleSCurveSelectedDetails(String projectId, String viewConsideration) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> selectedContractIdResponse = new HashMap<String, Object>();
		List<ProjectConsoleUpdateCtrlPrimaryDTO> dataList = new ArrayList<ProjectConsoleUpdateCtrlPrimaryDTO>();
		ProjectConsoleSelectedConfigDTO selectedConfigResponseDTO = new ProjectConsoleSelectedConfigDTO();
		ProjectConsoleConfiguratorDTO data = new ProjectConsoleConfiguratorDTO();
		ProjectConsoleFeaturesDTO feature = new ProjectConsoleFeaturesDTO();
		ProjectConsoleViewsDTO view = new ProjectConsoleViewsDTO();
		ProjectConsoleStaticDataDTO staticData = new ProjectConsoleStaticDataDTO();
		ProjectConsoleWeightDataDTO weightData = new ProjectConsoleWeightDataDTO();
		String selectedNode = "", internalSelectedNode = "", externalSelectedNode = "";
		try {
			data = iProjectControlDAO.getProjectConsoleSCurveSelectedDetails(projectId);
			if (null != data) {
				selectedContractIdResponse = serviceHelper.getSelectedContractId(data.getFeatures());
				internalSelectedNode = (String) selectedContractIdResponse.get("internalSelectedNode");
				externalSelectedNode = (String) selectedContractIdResponse.get("externalSelectedNode");
				feature = serviceHelper.getFeature(data.getFeatures(), "PROJECTS & BASELINES");
				if (viewConsideration.equalsIgnoreCase("Internal")) {
					selectedNode = internalSelectedNode;
					view = serviceHelper.getView(feature, "INTERNAL");
				} else {
					selectedNode = externalSelectedNode;
					view = serviceHelper.getView(feature, "EXTERNAL");
				}
				dataList = serviceHelper.getSelectedUpdateControlV2(view, selectedNode);
				staticData = serviceHelper.getStaticInformation(data, viewConsideration);
				weightData = serviceHelper.getWeightInformation(data, viewConsideration);
				selectedConfigResponseDTO.setSelectedData(dataList);
				selectedConfigResponseDTO.setStaticData(staticData);
				selectedConfigResponseDTO.setWeightData(weightData);
				responseMap.put("data", selectedConfigResponseDTO);
			}
		} catch (Exception e) {
			log.error("getProjectConsoleSCurveSelectedDetails :: " + e.getMessage());
		}
		return responseMap;
	}

	public Map<String, Object> getProjectConsoleDeckStatusDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectConsoleDeckStatusDetailsDTO dto = new ProjectConsoleDeckStatusDetailsDTO();
		try {
			dto = iProjectControlDAO.getProjectConsoleDeckStatusDetails(projectId);
			responseMap.put("data", dto);
		} catch (Exception e) {
			log.error("ProjectConsoleDeckStatusDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleDeckContractualDeliveryDates(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ProjectConsoleDeckContractualDeliveryDTO> dto = new ArrayList<ProjectConsoleDeckContractualDeliveryDTO>();
		try {
			dto = iProjectControlDAO.getProjectConsoleDeckContractualDeliveryDates(projectId);
			responseMap.put("data", dto);
		} catch (Exception e) {
			log.error("getProjectConsoleDeckContractualDeliveryDates(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getProjectConsoleQIRadarDetails(String projectId) {
		log.debug("INIT- getProjectConsoleQIRadarDetails for projectId : {}", projectId);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			String redFlagInspectionsCnt = iProjectControlDAO.getProjectConsoleQIRadarDetails(projectId);
			responseMap.put("Red Flag Inspections Count", redFlagInspectionsCnt);
		} catch (Exception e) {
			log.error("getProjectConsoleQIRadarDetails(): Exception occurred : " + e.getMessage());
		}
		log.debug("END- getProjectConsoleQIRadarDetails for projectId : {}", projectId);
		return responseMap;
	}

}
