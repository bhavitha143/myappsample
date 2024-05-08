package com.bh.realtrack.service.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bh.realtrack.dto.ProjectConsoleBaselineDTO;
import com.bh.realtrack.dto.ProjectConsoleDataDTO;
import com.bh.realtrack.dto.ProjectConsoleFeaturesDTO;
import com.bh.realtrack.dto.ProjectConsoleConfiguratorDTO;
import com.bh.realtrack.dto.ProjectConsoleProjectsDTO;
import com.bh.realtrack.dto.ProjectConsoleStaticDataDTO;
import com.bh.realtrack.dto.ProjectConsoleUpdateCtrlPrimaryDTO;
import com.bh.realtrack.dto.ProjectConsoleViewsDTO;
import com.bh.realtrack.dto.ProjectConsoleWeightDataDTO;

@Component
public class ProjectConsoleSCurveServiceHelper {
	static Logger log = LoggerFactory.getLogger(ProjectConsoleSCurveServiceHelper.class.getName());

	public ProjectConsoleFeaturesDTO getFeature(List<ProjectConsoleFeaturesDTO> features, String featureType) {
		ProjectConsoleFeaturesDTO feature = new ProjectConsoleFeaturesDTO();
		try {
			for (ProjectConsoleFeaturesDTO obj : features) {
				if (obj.getFeature().equalsIgnoreCase(featureType)) {
					feature = obj;
				}
			}
		} catch (Exception e) {
			log.error("getFeature :: " + e.getMessage());
		}
		return feature;
	}

	public ProjectConsoleViewsDTO getView(ProjectConsoleFeaturesDTO features, String viewType) {
		ProjectConsoleViewsDTO view = new ProjectConsoleViewsDTO();
		List<ProjectConsoleViewsDTO> viewsList = new ArrayList<ProjectConsoleViewsDTO>();
		try {
			viewsList = features.getViews();
			for (ProjectConsoleViewsDTO obj : viewsList) {
				if (obj.getViewName().equalsIgnoreCase(viewType)) {
					view = obj;
				}
			}
		} catch (Exception e) {
			log.error("getFeature :: " + e.getMessage());
		}
		return view;
	}

	public Map<String, Object> getSelectedContractId(List<ProjectConsoleFeaturesDTO> features) {
		Map<String, Object> response = new HashMap<String, Object>();
		String internalSelectedNode = "", externalSelectedNode = "";

		try {
			for (ProjectConsoleFeaturesDTO feature : features) {
				if (feature.getFeature().equalsIgnoreCase("EPSNode")) {
					List<ProjectConsoleViewsDTO> views = new ArrayList<ProjectConsoleViewsDTO>();
					views = feature.getViews();
					for (ProjectConsoleViewsDTO view : views) {
						List<ProjectConsoleDataDTO> dataList = new ArrayList<ProjectConsoleDataDTO>();
						if (view.getViewName().equalsIgnoreCase("External")) {
							if (view.getDataList() != null) {
								dataList = view.getDataList();
								for (ProjectConsoleDataDTO data : dataList)
									if (data.getIsSelected()) {
										externalSelectedNode = data.getContract();
									}
							}
						} else if (view.getViewName().equalsIgnoreCase("Internal")) {
							if (view.getDataList() != null) {
								dataList = view.getDataList();
								for (ProjectConsoleDataDTO data : dataList) {
									if (data.getIsSelected()) {
										internalSelectedNode = data.getContract();
									}
								}
							}
						}
					}
				}
			}
			response.put("internalSelectedNode", internalSelectedNode);
			response.put("externalSelectedNode", externalSelectedNode);
		} catch (Exception e) {
			log.error("getSelectedContractId :: " + e.getMessage());
		}
		return response;
	}

	public List<ProjectConsoleUpdateCtrlPrimaryDTO> getSelectedUpdateControlV2(ProjectConsoleViewsDTO view,
			String contractSelected) {
		List<ProjectConsoleUpdateCtrlPrimaryDTO> rtCatConfigUpdateCtrlPrimaryList = new ArrayList<ProjectConsoleUpdateCtrlPrimaryDTO>();
		try {
			List<ProjectConsoleDataDTO> dataList = new ArrayList<ProjectConsoleDataDTO>();
			if (view.getDataList() != null) {
				dataList = view.getDataList();
				for (ProjectConsoleDataDTO dataObj : dataList) {
					if (dataObj.getContract().equalsIgnoreCase(contractSelected)) {
						List<ProjectConsoleProjectsDTO> projectList = new ArrayList<ProjectConsoleProjectsDTO>();
						projectList = dataObj.getProjects();
						for (ProjectConsoleProjectsDTO projectObj : projectList) {
							if (projectObj.getIsSelected()) {
								List<ProjectConsoleBaselineDTO> baselineList = new ArrayList<ProjectConsoleBaselineDTO>();
								baselineList = projectObj.getBaselines();
								if (baselineList.size() == 1
										&& baselineList.get(0).getBaseline().equalsIgnoreCase("")) {
									ProjectConsoleUpdateCtrlPrimaryDTO dto = new ProjectConsoleUpdateCtrlPrimaryDTO();
									dto.setEpsContractId(contractSelected);
									dto.setSubProject(projectObj.getProject());
									dto.setBaselineId("NOT_PRESENT");
									dto.setIntExtFlag(view.getViewName());
									rtCatConfigUpdateCtrlPrimaryList.add(dto);
								} else {
									for (ProjectConsoleBaselineDTO baselineObj : baselineList) {
										if (baselineObj.getCurrentBaselineFlag().equalsIgnoreCase("Y")) {
											ProjectConsoleUpdateCtrlPrimaryDTO dto = new ProjectConsoleUpdateCtrlPrimaryDTO();
											dto.setEpsContractId(contractSelected);
											dto.setSubProject(projectObj.getProject());
											dto.setBaselineId(baselineObj.getBaseline());
											dto.setIntExtFlag(view.getViewName());
											rtCatConfigUpdateCtrlPrimaryList.add(dto);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("getSelectedUpdateControlV2 :: " + e.getMessage());
		}
		return rtCatConfigUpdateCtrlPrimaryList;
	}

	public ProjectConsoleStaticDataDTO getStaticInformation(ProjectConsoleConfiguratorDTO data,
			String viewConsideration) {
		ProjectConsoleStaticDataDTO staticDataDTO = new ProjectConsoleStaticDataDTO();
		ProjectConsoleFeaturesDTO otherCriteriafeature = new ProjectConsoleFeaturesDTO();
		ProjectConsoleViewsDTO view = new ProjectConsoleViewsDTO();
		List<ProjectConsoleDataDTO> dataList = new ArrayList<ProjectConsoleDataDTO>();
		List<String> filterBy = new ArrayList<String>();
		List<String> sCurvesToShow = new ArrayList<String>();
		String customWeight = "", activityWeightBudgetCost = "no";
		try {
			otherCriteriafeature = getFeature(data.getFeatures(), "OTHER CRITERIA");
			view = getView(otherCriteriafeature, viewConsideration);
			dataList = view.getDataList();
			for (ProjectConsoleDataDTO dataObj : dataList) {
				if (dataObj.getCategory().equalsIgnoreCase("FILTER_BY") && dataObj.isSelectedOption()) {
					filterBy.add(dataObj.getVal());
				} else if (dataObj.getCategory().equalsIgnoreCase("CUSTOM_WEIGHT") && dataObj.isSelectedOption()) {
					customWeight = dataObj.getVal();
				} else if (dataObj.getCategory().equalsIgnoreCase("ACTIVITY_WEIGHT_BUDGET_COST")
						&& dataObj.isSelectedOption()) {
					activityWeightBudgetCost = dataObj.getVal();
				} else if (dataObj.getCategory().equalsIgnoreCase("S_CURVES_TO_SHOW") && dataObj.isSelectedOption()) {
					sCurvesToShow.add(dataObj.getVal());
				}
			}
			staticDataDTO.setCustomWeight(customWeight);
			staticDataDTO.setActivityWeightBudgetCost(activityWeightBudgetCost);
			staticDataDTO.setFilterBy(filterBy);
			staticDataDTO.setsCurvesToShow(sCurvesToShow);
		} catch (Exception e) {
			log.error("getStaticInformation :: " + e.getMessage());
		}
		return staticDataDTO;
	}

	public ProjectConsoleWeightDataDTO getWeightInformation(ProjectConsoleConfiguratorDTO data,
			String viewConsideration) {
		ProjectConsoleWeightDataDTO weightDataDTO = new ProjectConsoleWeightDataDTO();
		ProjectConsoleFeaturesDTO otherCriteriafeature = new ProjectConsoleFeaturesDTO();
		ProjectConsoleViewsDTO view = new ProjectConsoleViewsDTO();
		List<ProjectConsoleDataDTO> dataList = new ArrayList<ProjectConsoleDataDTO>();
		String wbsCustomWeight = "", wbsOverallWeight = "", wbsEngDeptAsTrain = "";
		try {
			otherCriteriafeature = getFeature(data.getFeatures(), "OTHER CRITERIA");
			view = getView(otherCriteriafeature, "Internal");
			dataList = view.getDataList();
			for (ProjectConsoleDataDTO dataObj : dataList) {
				if (dataObj.getCategory().equalsIgnoreCase("WBS_CUSTOM_WEIGHT") && dataObj.isSelectedOption()) {
					wbsCustomWeight = dataObj.getVal();
				} else if (dataObj.getCategory().equalsIgnoreCase("WBS_OVERALL_DEPT") && dataObj.isSelectedOption()) {
					wbsOverallWeight = dataObj.getVal();
				} else if (dataObj.getCategory().equalsIgnoreCase("WBS_ENG_DEPT_AS_TRAIN")
						&& dataObj.isSelectedOption()) {
					wbsEngDeptAsTrain = dataObj.getVal();
				}
			}
			weightDataDTO.setWbsCustomWeight(wbsCustomWeight);
			weightDataDTO.setWbsOverallWeight(wbsOverallWeight);
			weightDataDTO.setWbsEngDeptAsTrain(wbsEngDeptAsTrain);
		} catch (Exception e) {
			log.error("getWeightInformation :: " + e.getMessage());
		}
		return weightDataDTO;
	}

	public List<String> resolveFilterBy(ProjectConsoleStaticDataDTO data) {
		List<String> filterBy = new ArrayList<String>();
		try {
			if (data.getFilterBy().size() == 1 && data.getFilterBy().get(0).equalsIgnoreCase("All")) {
				filterBy.add("A");
			} else if (data.getFilterBy().size() == 1
					&& data.getFilterBy().get(0).equalsIgnoreCase("Main Items+Internal Item")) {
				filterBy.add("MI");
			} else if (data.getFilterBy().size() == 1 && data.getFilterBy().get(0).equalsIgnoreCase("Main Items")) {
				filterBy.add("M");
			} else if (data.getFilterBy().size() == 2 && data.getFilterBy().contains("Main Items+Internal Item")
					&& data.getFilterBy().contains("All")) {
				filterBy.add("MIA_MI");
				filterBy.add("MIA_A");
			}
		} catch (Exception e) {
			log.error("resolveFilterBy :: " + e.getMessage());
		}
		return filterBy;
	}

	public List<String> getSelectedSubProjects(List<ProjectConsoleUpdateCtrlPrimaryDTO> selectedData) {
		List<String> subProjectList = new ArrayList<String>();
		try {
			for (ProjectConsoleUpdateCtrlPrimaryDTO dataObj : selectedData) {
				String engDataObj = "";
				engDataObj = dataObj + "-ENG";
				if (!engDataObj.equalsIgnoreCase("")) {
					subProjectList.add(engDataObj);
				}
				subProjectList.add(dataObj.getSubProject());
			}
		} catch (Exception e) {
			log.error("getSelectedSubProjects :: " + e.getMessage());
		}
		return subProjectList;
	}
}
