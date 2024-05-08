package com.bh.realtrack.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IProjectComplexityDAO;
import com.bh.realtrack.dto.CalculateNEComplexityDTO;
import com.bh.realtrack.dto.CalculateNEComplexityResponseDTO;
import com.bh.realtrack.dto.CalculateSRVComplexityDTO;
import com.bh.realtrack.dto.ComplexityFormTemplateDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ITOComplexityTabDTO;
import com.bh.realtrack.dto.ITOProjectComplexityTabDTO;
import com.bh.realtrack.dto.ITOSlotCCTDates;
import com.bh.realtrack.dto.NEComplexityDTO;
import com.bh.realtrack.dto.NEComplexityTabDTO;
import com.bh.realtrack.dto.NEProjectComplexityDTO;
import com.bh.realtrack.dto.NEProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.NEProjectComplexityTabDTO;
import com.bh.realtrack.dto.ProjectComplexityDTO;
import com.bh.realtrack.dto.ProjectComplexityDropdownDTO;
import com.bh.realtrack.dto.ProjectComplexityLogDetailsDTO;
import com.bh.realtrack.dto.ProjectManagerDTO;
import com.bh.realtrack.dto.ProjectManagerSRVDTO;
import com.bh.realtrack.dto.ProjectPlannerDTO;
import com.bh.realtrack.dto.ProjectPlannerSRVDTO;
import com.bh.realtrack.dto.ProjectsSummaryDetailsDTO;
import com.bh.realtrack.dto.QualityProjectManagerDTO;
import com.bh.realtrack.dto.SRVComplexityDTO;
import com.bh.realtrack.dto.SRVPPProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SRVProjectComplexityDTO;
import com.bh.realtrack.dto.SRVProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SaveNEComplexityDTO;
import com.bh.realtrack.dto.SaveNEComplexityResponseDTO;
import com.bh.realtrack.dto.SaveProjectComplexityDTO;
import com.bh.realtrack.dto.SaveProjectComplexityResponseDTO;
import com.bh.realtrack.dto.saveSRVComplexityDTO;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IProjectCompleixtyService;
import com.bh.realtrack.service.IProjectControlService;

/**
 *
 * @author Anand Kumar
 *
 */
@Service
public class ProjectComplexityServiceImpl implements IProjectCompleixtyService {
	private static Logger log = LoggerFactory.getLogger(ProjectComplexityServiceImpl.class.getName());

	private IProjectComplexityDAO iProjectComplexityDAO;

	private CallerContext callerContext;

	@Autowired
	IProjectControlService iProjectControlService;

	@Autowired
	public ProjectComplexityServiceImpl(final IProjectComplexityDAO iPrjComplexityDAO, final CallerContext callerCxt) {
		this.iProjectComplexityDAO = iPrjComplexityDAO;
		this.callerContext = callerCxt;
	}

	@Override
	public ProjectComplexityLogDetailsDTO getProjectComplexityDetails(final String projectId) {
		List<ProjectComplexityDTO> projectComplexityDTOList = new ArrayList<ProjectComplexityDTO>();
		ProjectComplexityLogDetailsDTO logDetailsDTO = new ProjectComplexityLogDetailsDTO();
		projectComplexityDTOList = iProjectComplexityDAO.getProjectComplexity(projectId);
		logDetailsDTO = iProjectComplexityDAO.getProjectComplexityLogDetails(projectId);
		logDetailsDTO.setProjectComplexityList(projectComplexityDTOList);
		return logDetailsDTO;
	}

	@Override
	public SaveProjectComplexityResponseDTO saveProjectComplexity(
			final SaveProjectComplexityDTO saveProjectComplexityDTO) {
		List<Map<String, Object>> projectComplexityDetailsList = saveProjectComplexityDTO
				.getProjectComplexityDetailList();
		String sso = callerContext.getName();
		int result = 0;
		SaveProjectComplexityResponseDTO saveProjectComplexityResponseDTO = new SaveProjectComplexityResponseDTO();
		try {
			for (Map<String, Object> projectComplexityDetailsMap : projectComplexityDetailsList) {
				result = iProjectComplexityDAO.saveProjectComplexityList(saveProjectComplexityDTO.getProjectId(),
						projectComplexityDetailsMap, sso);
				result = 1;
			}
			saveProjectComplexityResponseDTO = iProjectComplexityDAO
					.getProjectComplexitySaveLogDetails(saveProjectComplexityDTO.getProjectId());
			if (result == 1) {
				saveProjectComplexityResponseDTO.setStatus("Success");
			} else {
				saveProjectComplexityResponseDTO.setStatus("Error");
			}
		} catch (Exception e) {
			log.error("Failed to save data into db:", e);
			saveProjectComplexityResponseDTO.setStatus("Error");
		}
		return saveProjectComplexityResponseDTO;
	}

	@Override
	public Map<String, Object> getProjectComplexityDropdownList(final String attributeGroup) {
		List<ProjectComplexityDropdownDTO> projectProductDropdownList = new ArrayList<ProjectComplexityDropdownDTO>();
		List<ProjectComplexityDropdownDTO> list = new ArrayList<ProjectComplexityDropdownDTO>();
		Map<String, Object> map = new HashMap<String, Object>();
		HashSet<String> hashSet = new HashSet<String>();
		int countDropdownList = 0;
		int count = 0;
		boolean flag = false;

		projectProductDropdownList = iProjectComplexityDAO.getPCDropdownDetails(attributeGroup);

		for (ProjectComplexityDropdownDTO pcDto : projectProductDropdownList) {
			countDropdownList++;
			list.add(pcDto);
			flag = hashSet.add(pcDto.getAttributeName());
			if (projectProductDropdownList.size() == countDropdownList) {
				flag = true;
				count = 1;
			}
			if (flag) {
				if (count > 0) {
					pcDto = list.get(list.size() - 1);
					if (!(projectProductDropdownList.size() == countDropdownList)) {
						list.remove(list.size() - 1);
					}
					map.put(list.get(0).getAttributeName(), list);
					list = new ArrayList<ProjectComplexityDropdownDTO>();
					list.add(pcDto);
				}
				count = 0;
			} else {
				count++;
			}

		}
		return map;
	}

	@Override
	public NEProjectComplexityDTO getNEProjectComplexityData(String projectId) {
		NEProjectComplexityDTO responseDTO = new NEProjectComplexityDTO();
		List<ProjectManagerDTO> pmList = new ArrayList<ProjectManagerDTO>();
		List<ProjectPlannerDTO> ppList = new ArrayList<ProjectPlannerDTO>();
		List<QualityProjectManagerDTO> qpmUnitsScopeList = new ArrayList<QualityProjectManagerDTO>();
		List<QualityProjectManagerDTO> qpmModuleScopeList = new ArrayList<QualityProjectManagerDTO>();
		List<NEComplexityDTO> list = new ArrayList<NEComplexityDTO>();
		DropDownDTO overallPerDTO = new DropDownDTO();
		String role = "", scope = "", showQPMModuleFlag = "N", overallProgress = "", dataDate = "";
		try {
			overallPerDTO = getOverallProgressPercent(projectId);
			overallProgress = null != overallPerDTO.getKey() ? overallPerDTO.getKey() : "";
			dataDate = null != overallPerDTO.getVal() ? overallPerDTO.getVal() : "";
			list = iProjectComplexityDAO.getNEProjectComplexityData(projectId);
			for (NEComplexityDTO complexityDTO : list) {
				role = complexityDTO.getRole();
				scope = complexityDTO.getScope();
				if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PM")) {
					ProjectManagerDTO pm = new ProjectManagerDTO();
					pm.setScopeProduct(complexityDTO.getScopeProduct());
					pm.setScopeUnitTrain(complexityDTO.getScopeUnitTrain());
					pm.setmDollar(complexityDTO.getmDollar());
					pm.setTestQuality(complexityDTO.getTestQuality());
					pm.setSchedule(complexityDTO.getSchedule());
					pm.setCustomization(complexityDTO.getCustomization());
					pm.setNoOfUnits(complexityDTO.getNoOfUnits());
					pm.setNoOfCostingProject(complexityDTO.getNoOfCostingProject());
					pm.setNoOfTrain(complexityDTO.getNoOfTrain());
					pm.setNoOfModules(complexityDTO.getNoOfModules());
					if (null != complexityDTO.getNoOfModules() && !complexityDTO.getNoOfModules().isEmpty()
							&& !complexityDTO.getNoOfModules().equalsIgnoreCase("")) {
						double moduleCnt = Double.parseDouble(complexityDTO.getNoOfModules());
						if (moduleCnt > 0.0) {
							showQPMModuleFlag = "Y";
						}
					}
					pm.setInitialScoreForPMComplex(complexityDTO.getInitialScoreForPMComplex());
					pm.setActualScoreForPMComplex(complexityDTO.getActualScoreForPMComplex());
					pm.setActualScoreForFTEPM(complexityDTO.getActualScoreForFTEPM());
					pm.setCustomeScoreForFTEPMShop(complexityDTO.getCustomeScoreForFTEPMShop());
					pm.setCustomeScoreForFTEPMInstal(complexityDTO.getCustomeScoreForFTEPMInstal());
					pm.setNotesForCustomScore(complexityDTO.getNotesForCustomScore());
					pmList.add(pm);
					responseDTO.setUpdatedBy(complexityDTO.getUpdatedBy());
					responseDTO.setUpdatedOn(complexityDTO.getUpdatedOn());
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PP")) {
					ProjectPlannerDTO pp = new ProjectPlannerDTO();
					pp.setScopeProduct(complexityDTO.getScopeProduct());
					pp.setScopeUnitTrain(complexityDTO.getScopeUnitTrain());
					pp.setmDollar(complexityDTO.getmDollar());
					pp.setTestQuality(complexityDTO.getTestQuality());
					pp.setSchedule(complexityDTO.getSchedule());
					pp.setCustomization(complexityDTO.getCustomization());
					pp.setProjectWSPPOutsourcing(complexityDTO.getPpOutSourcing());
					pp.setInitialScoreForPMComplex(complexityDTO.getInitialScoreForPMComplex());
					pp.setActualScoreForPMComplex(complexityDTO.getActualScoreForPMComplex());
					pp.setActualScoreForFTEPM(complexityDTO.getActualScoreForFTEPM());
					pp.setCustomeScoreForFTEPMShop(complexityDTO.getCustomeScoreForFTEPMShop());
					pp.setCustomeScoreForFTEPMInstal(complexityDTO.getCustomeScoreForFTEPMInstal());
					pp.setNotesForCustomScore(complexityDTO.getNotesForCustomScore());
					pp.setActualScoreForPPOutsourcing(complexityDTO.getActualScoreForPPOutsourcing());
					pp.setCustomScoreForFTEPPShopOutsourcing(complexityDTO.getCustomScoreForFTEPPShopOutsourcing());
					pp.setCustomScoreForPPFTE(complexityDTO.getCustomScoreForPPFTE());
					pp.setActualScoreForPP(complexityDTO.getActualScoreForPP());
					ppList.add(pp);
					responseDTO.setUpdatedBy(complexityDTO.getUpdatedBy());
					responseDTO.setUpdatedOn(complexityDTO.getUpdatedOn());
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("QPM") && null != scope
						&& !scope.isEmpty() && scope.equalsIgnoreCase("unit")) {
					QualityProjectManagerDTO qpm = new QualityProjectManagerDTO();
					qpm.setScopeProduct(complexityDTO.getScopeProduct());
					qpm.setScopeUnitTrain(complexityDTO.getScopeUnitTrain());
					qpm.setmDollar(complexityDTO.getmDollar());
					qpm.setTestQuality(complexityDTO.getTestQuality());
					qpm.setSchedule(complexityDTO.getSchedule());
					qpm.setCustomization(complexityDTO.getCustomization());
					qpm.setProjectWSPPOutsourcing(complexityDTO.getPpOutSourcing());
					qpm.setInitialScoreForPMComplex(complexityDTO.getInitialScoreForPMComplex());
					qpm.setActualScoreForPMComplex(complexityDTO.getActualScoreForPMComplex());
					qpm.setActualScoreForFTEPM(complexityDTO.getActualScoreForFTEPM());
					qpm.setCustomScoreForPPFTE(complexityDTO.getCustomScoreForPPFTE());
					qpm.setActualScoreForPP(complexityDTO.getActualScoreForPP());
					qpm.setActualScoreForPPOutsourcing(complexityDTO.getActualScoreForPPOutsourcing());
					qpm.setCustomeScoreForFTEPMShop(complexityDTO.getCustomeScoreForFTEPMShop());
					qpm.setCustomScoreForFTEPPShopOutsourcing(complexityDTO.getCustomScoreForFTEPPShopOutsourcing());
					qpm.setCustomeScoreForFTEPMInstal(complexityDTO.getCustomeScoreForFTEPMInstal());
					qpm.setCustomScoreForFTEPMInstallOutsourcing(complexityDTO.getCustomScoreFTEInstallOutsourcing());
					qpm.setNotesForCustomScore(complexityDTO.getNotesForCustomScore());
					qpmUnitsScopeList.add(qpm);
					responseDTO.setUpdatedBy(complexityDTO.getUpdatedBy());
					responseDTO.setUpdatedOn(complexityDTO.getUpdatedOn());
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("QPM") && null != scope
						&& !scope.isEmpty() && scope.equalsIgnoreCase("module")) {
					QualityProjectManagerDTO qpm = new QualityProjectManagerDTO();
					qpm.setScopeProduct(complexityDTO.getScopeProduct());
					qpm.setScopeUnitTrain(complexityDTO.getScopeUnitTrain());
					qpm.setmDollar(complexityDTO.getmDollar());
					qpm.setTestQuality(complexityDTO.getTestQuality());
					qpm.setSchedule(complexityDTO.getSchedule());
					qpm.setCustomization(complexityDTO.getCustomization());
					qpm.setProjectWSPPOutsourcing(complexityDTO.getPpOutSourcing());
					qpm.setInitialScoreForPMComplex(complexityDTO.getInitialScoreForPMComplex());
					qpm.setActualScoreForPMComplex(complexityDTO.getActualScoreForPMComplex());
					qpm.setActualScoreForFTEPM(complexityDTO.getActualScoreForFTEPM());
					qpm.setCustomScoreForPPFTE(complexityDTO.getCustomScoreForPPFTE());
					qpm.setActualScoreForPP(complexityDTO.getActualScoreForPP());
					qpm.setActualScoreForPPOutsourcing(complexityDTO.getActualScoreForPPOutsourcing());
					qpm.setCustomeScoreForFTEPMShop(complexityDTO.getCustomeScoreForFTEPMShop());
					qpm.setCustomScoreForFTEPPShopOutsourcing(complexityDTO.getCustomScoreForFTEPPShopOutsourcing());
					qpm.setCustomeScoreForFTEPMInstal(complexityDTO.getCustomeScoreForFTEPMInstal());
					qpm.setCustomScoreForFTEPMInstallOutsourcing(complexityDTO.getCustomScoreFTEInstallOutsourcing());
					qpm.setNotesForCustomScore(complexityDTO.getNotesForCustomScore());
					qpmModuleScopeList.add(qpm);
					responseDTO.setUpdatedBy(complexityDTO.getUpdatedBy());
					responseDTO.setUpdatedOn(complexityDTO.getUpdatedOn());
				}
			}

			if (null != dataDate && !dataDate.isEmpty()) {
				if (null != qpmUnitsScopeList && !qpmUnitsScopeList.isEmpty()) {
					QualityProjectManagerDTO qpm = new QualityProjectManagerDTO();
					qpm = qpmUnitsScopeList.get(0);
					qpm.setActualProgress(overallProgress);
					qpm.setDataDate(dataDate);
				} else {
					QualityProjectManagerDTO qpm = new QualityProjectManagerDTO();
					overallPerDTO = getOverallProgressPercent(projectId);
					qpm.setActualProgress(overallProgress);
					qpm.setDataDate(dataDate);
					qpmUnitsScopeList.add(qpm);
				}
			}
			responseDTO.setPm(pmList);
			responseDTO.setPp(ppList);
			responseDTO.setQpm(qpmUnitsScopeList);
			responseDTO.setQpmModule(qpmModuleScopeList);
			responseDTO.setShowQPMModuleFlag(showQPMModuleFlag);
		} catch (Exception e) {
			log.error("getNEProjectComplexityData :: ", e.getMessage());
		}
		return responseDTO;
	}

	@Override
	public Map<String, Object> getNEComplexityProjectTabData(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		NEProjectComplexityTabDTO dto = new NEProjectComplexityTabDTO();
		List<DropDownDTO> dropDown1 = new ArrayList<DropDownDTO>();
		List<DropDownDTO> dropDown2 = new ArrayList<DropDownDTO>();
		List<DropDownDTO> dropDown3 = new ArrayList<DropDownDTO>();
		try {
			dropDown1 = iProjectComplexityDAO.getWLSegmentDropDown(projectId);
			dropDown2 = iProjectComplexityDAO.getWLProjectCategoryDropDown(projectId);
			dropDown3 = iProjectComplexityDAO.getOrderTypeDropDown(projectId);
			dto = iProjectComplexityDAO.getNEComplexityProjectTabData(projectId);
			dto.setDefaultWlProjectCategory("CORE");
			responseMap.put("WLSegment", dropDown1);
			responseMap.put("WLProjectCategory", dropDown2);
			responseMap.put("orderType", dropDown3);
			responseMap.put("ProjectTabData", dto);
		} catch (Exception e) {
			log.error("getNEComplexityProjectTabData(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public ComplexityFormTemplateDTO getComplexityFormTemplate(String projectId) {
		return iProjectComplexityDAO.getComplexityFormTemplate(projectId);
	}

	@Override
	public SaveNEComplexityResponseDTO saveNEComplexityData(SaveNEComplexityDTO saveNEComplexityDTO) {
		SaveNEComplexityResponseDTO saveNEComplexityResponseDTO = new SaveNEComplexityResponseDTO();
		Map<String, Object> pmMap = new HashMap<String, Object>();
		Map<String, Object> ppMap = new HashMap<String, Object>();
		Map<String, Object> qpmMap = new HashMap<String, Object>();
		Map<String, Object> qpmModuleMap = new HashMap<String, Object>();
		int pmResult = 0;
		int ppResult = 0;
		int qpmResult = 0;
		int qpmModuleResult = 1;
		String sso = callerContext.getName();
		try {
			pmMap = saveNEComplexityDTO.getPm();
			pmMap = validateNEComplexityData(saveNEComplexityDTO.getProjectId(), pmMap, "PM");
			if (null != pmMap && !pmMap.isEmpty()) {
				if (null != pmMap.get("status").toString() && !pmMap.get("status").toString().isEmpty()
						&& pmMap.get("status").toString().equalsIgnoreCase("E")) {
					saveNEComplexityResponseDTO.setStatus("Error");
					if (null != pmMap.get("errorMsg").toString() && !pmMap.get("errorMsg").toString().isEmpty()) {
						saveNEComplexityResponseDTO.setErrorMsg(pmMap.get("errorMsg").toString());
					}
					return saveNEComplexityResponseDTO;
				} else if (null != pmMap.get("status").toString() && !pmMap.get("status").toString().isEmpty()
						&& pmMap.get("status").toString().equalsIgnoreCase("S")) {
					pmResult = iProjectComplexityDAO.saveNEComplexityPMData(saveNEComplexityDTO.getProjectId(), pmMap,
							sso);
				}
			}

			ppMap = saveNEComplexityDTO.getPp();
			ppMap = validateNEComplexityData(saveNEComplexityDTO.getProjectId(), ppMap, "PP");
			if (null != ppMap && !ppMap.isEmpty()) {
				if (null != ppMap.get("status").toString() && !ppMap.get("status").toString().isEmpty()
						&& ppMap.get("status").toString().equalsIgnoreCase("E")) {
					saveNEComplexityResponseDTO.setStatus("Error");
					if (null != ppMap.get("errorMsg").toString() && !ppMap.get("errorMsg").toString().isEmpty()) {
						saveNEComplexityResponseDTO.setErrorMsg(ppMap.get("errorMsg").toString());
					}
					return saveNEComplexityResponseDTO;
				} else if (null != ppMap.get("status").toString() && !ppMap.get("status").toString().isEmpty()
						&& ppMap.get("status").toString().equalsIgnoreCase("S")) {
					ppResult = iProjectComplexityDAO.saveNEComplexityPPData(saveNEComplexityDTO.getProjectId(), ppMap,
							sso);
				}
			}

			qpmMap = saveNEComplexityDTO.getQpm();
			qpmMap = validateNEComplexityData(saveNEComplexityDTO.getProjectId(), qpmMap, "QPM");
			if (null != qpmMap && !qpmMap.isEmpty()) {
				if (null != qpmMap.get("status").toString() && !qpmMap.get("status").toString().isEmpty()
						&& qpmMap.get("status").toString().equalsIgnoreCase("E")) {
					saveNEComplexityResponseDTO.setStatus("Error");
					if (null != qpmMap.get("errorMsg").toString() && !qpmMap.get("errorMsg").toString().isEmpty()) {
						saveNEComplexityResponseDTO.setErrorMsg(qpmMap.get("errorMsg").toString());
					}
					return saveNEComplexityResponseDTO;
				} else if (null != qpmMap.get("status").toString() && !qpmMap.get("status").toString().isEmpty()
						&& qpmMap.get("status").toString().equalsIgnoreCase("S")) {
					qpmResult = iProjectComplexityDAO.saveNEComplexityQPMData(saveNEComplexityDTO.getProjectId(),
							qpmMap, sso);
				}
			}

			qpmModuleMap = saveNEComplexityDTO.getQpmModule();
			qpmModuleMap = validateNEComplexityData(saveNEComplexityDTO.getProjectId(), qpmModuleMap, "qpmModule");
			if (null != qpmModuleMap && !qpmModuleMap.isEmpty()) {
				if (null != qpmModuleMap.get("status").toString() && !qpmModuleMap.get("status").toString().isEmpty()
						&& qpmModuleMap.get("status").toString().equalsIgnoreCase("E")) {
					saveNEComplexityResponseDTO.setStatus("Error");
					if (null != qpmModuleMap.get("errorMsg").toString()
							&& !qpmModuleMap.get("errorMsg").toString().isEmpty()) {
						saveNEComplexityResponseDTO.setErrorMsg(qpmModuleMap.get("errorMsg").toString());
					}
					return saveNEComplexityResponseDTO;
				} else if (null != qpmModuleMap.get("status").toString()
						&& !qpmModuleMap.get("status").toString().isEmpty()
						&& qpmMap.get("status").toString().equalsIgnoreCase("S")) {
					qpmModuleResult = iProjectComplexityDAO
							.saveNEComplexityQPMModuleData(saveNEComplexityDTO.getProjectId(), qpmModuleMap, sso);
				}
			}
			if (pmResult == 1 && ppResult == 1 && qpmResult == 1 && qpmModuleResult == 1) {
				saveNEComplexityResponseDTO.setStatus("Success");
				saveNEComplexityResponseDTO.setErrorMsg("");
			} else {
				saveNEComplexityResponseDTO.setStatus("Error");
				saveNEComplexityResponseDTO.setErrorMsg("Error");
			}
		} catch (Exception e) {
			log.error("Failed to save NE Complexity Data into db:", e.getMessage());
			saveNEComplexityResponseDTO.setStatus("Error");
			saveNEComplexityResponseDTO.setErrorMsg("Error");
			return saveNEComplexityResponseDTO;
		}
		return saveNEComplexityResponseDTO;
	}

	private Map<String, Object> validateNEComplexityData(String projectId, Map<String, Object> map, String role) {
		Map<String, Object> calculateMap = new HashMap<String, Object>();
		CalculateNEComplexityResponseDTO calculateResponseDTO = new CalculateNEComplexityResponseDTO();
		CalculateNEComplexityDTO calculateNEComplexityDTO = new CalculateNEComplexityDTO();
		calculateNEComplexityDTO = new CalculateNEComplexityDTO();
		try {
			if (null != map && !map.isEmpty()) {
				log.info("Saving NE Complexity data for project :::: " + projectId);
				log.info("Before Calculation :: actualScoreForPMComplex :: "
						+ map.get("actualScoreForPMComplex").toString() + " :: actualScoreForFTEPM :: "
						+ map.get("actualScoreForFTEPM").toString());
				calculateNEComplexityDTO.setProjectId(projectId);
				calculateNEComplexityDTO.setRole(role);
				calculateNEComplexityDTO.setData(map);
				calculateMap = calculateNEProjectComplexityData(calculateNEComplexityDTO);
				if (null != calculateMap && !calculateMap.isEmpty()) {
					if (null != calculateMap.get("status").toString()
							&& !calculateMap.get("status").toString().isEmpty()
							&& calculateMap.get("status").toString().equalsIgnoreCase("E")) {
						map.put("status", "E");
						if (null != calculateMap.get("errorMsg").toString()
								&& !calculateMap.get("errorMsg").toString().isEmpty()) {
							log.info("Inside loop errorMsg");
							map.put("errorMsg", calculateMap.get("errorMsg").toString());
						}
					} else if (null != calculateMap.get("status").toString()
							&& !calculateMap.get("status").toString().isEmpty()
							&& calculateMap.get("status").toString().equalsIgnoreCase("S")) {
						map.put("status", "S");
						if (null != calculateMap.get(role)) {
							calculateResponseDTO = (CalculateNEComplexityResponseDTO) calculateMap.get(role);
							map.put("actualScoreForPMComplex", calculateResponseDTO.getActualScoreForPMComplex());
							map.put("actualScoreForPP", calculateResponseDTO.getActualScoreForPP());
							map.put("actualScoreForFTEPM", calculateResponseDTO.getActualScoreForFTEPM());
							map.put("actualScoreForPPOutsourcing",
									calculateResponseDTO.getActualScoreForPPOutsourcing());
						}
					}
				}
				log.info("After Calculation :: actualScoreForPMComplex :: "
						+ map.get("actualScoreForPMComplex").toString() + " :: actualScoreForFTEPM :: "
						+ map.get("actualScoreForFTEPM").toString());
			}
		} catch (Exception e) {
			log.error("validateNEComplexityData :: ", e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> calculateNEProjectComplexityData(CalculateNEComplexityDTO calculateNEComplexityDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		CalculateNEComplexityResponseDTO saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		StringBuilder validationMessage = new StringBuilder();
		boolean validationStatus = true, ppFlag = false;
		String role = "";
		Double scopeUnitTrain, totalActualScoreFTEPP = null, actualScoreFTEPPShop = null,
				actualScoreFTEPPShopOut = null, customScorePPFTE = null, customScoreFTEPPShop = null,
				customScoreFTEPPShopOut = null, customScoreFTEInstall = null, customScoreFTEInstallOut = null;
		try {
			Map<String, Object> map = calculateNEComplexityDTO.getData();
			role = calculateNEComplexityDTO.getRole();
			if (validationStatus && null != map.get("scopeUnitTrain") && !map.get("scopeUnitTrain").toString().isEmpty()
					&& null != role && !role.isEmpty()
					&& (role.equalsIgnoreCase("PM") || role.equalsIgnoreCase("PP"))) {
				scopeUnitTrain = Double.parseDouble(map.get("scopeUnitTrain").toString());
				if (scopeUnitTrain > 40) {
					validationMessage.append("Scope (Unit & Train) should be less than 40");
					validationStatus = false;
				}
			}
			if (validationStatus) {
				if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PM")) {
					log.info("***** Validating Complexity values for " + role + " Role *****");
					saveNEComplexityResponseDTO = calculateNEComplexityData(map, "PM");
					responseMap.put("status", "S");
					responseMap.put("errorMsg", "");
					responseMap.put("PM", saveNEComplexityResponseDTO);
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PP")) {
					if (null != map.get("actionType") && !map.get("actionType").toString().isEmpty()) {
						String actionType = map.get("actionType").toString();
						if (actionType.equalsIgnoreCase("CHECK")) {
							map.put("customeScoreForFTEPMShop", "");
							map.put("customScoreForFTEPPShopOutsourcing", "");
						}
						log.info("actionType ::: " + actionType);
					}
					saveNEComplexityResponseDTO = calculateNEComplexityData(map, "PP");
					if (validationStatus) {
						log.info("***** Validating Complexity values for " + role + " Role *****");

						if (null != map.get("projectWSPPOutsourcing")
								&& !map.get("projectWSPPOutsourcing").toString().isEmpty()) {
							ppFlag = Boolean.parseBoolean(map.get("projectWSPPOutsourcing").toString());
							log.info("PP_OUTSOURCING ::: " + ppFlag);
						}
						if (null != saveNEComplexityResponseDTO.getActualScoreForPP()
								&& !saveNEComplexityResponseDTO.getActualScoreForPP().isEmpty()) {
							totalActualScoreFTEPP = Double
									.parseDouble(saveNEComplexityResponseDTO.getActualScoreForPP());
							log.info("TOTAL_ACTUAL_SCORE_PP ::: " + totalActualScoreFTEPP);
						}
						if (null != saveNEComplexityResponseDTO.getActualScoreForFTEPM()
								&& !saveNEComplexityResponseDTO.getActualScoreForFTEPM().isEmpty()) {
							actualScoreFTEPPShop = Double
									.parseDouble(saveNEComplexityResponseDTO.getActualScoreForFTEPM());
							log.info("ACTUAL_SCORE_FTE_PP ::: " + actualScoreFTEPPShop);
						}
						if (null != saveNEComplexityResponseDTO.getActualScoreForPPOutsourcing()
								&& !saveNEComplexityResponseDTO.getActualScoreForPPOutsourcing().isEmpty()) {
							actualScoreFTEPPShopOut = Double
									.parseDouble(saveNEComplexityResponseDTO.getActualScoreForPPOutsourcing());
							log.info("ACTUAL_SCORE_FTE_PP_OUT ::: " + actualScoreFTEPPShopOut);
						}
						if (null != map.get("customScoreForPPFTE")
								&& !map.get("customScoreForPPFTE").toString().isEmpty()) {
							customScorePPFTE = Double.parseDouble(map.get("customScoreForPPFTE").toString());
							log.info("CUSTOM_SCORE_PP ::: " + customScorePPFTE);
						}
						if (null != map.get("customeScoreForFTEPMShop")
								&& !map.get("customeScoreForFTEPMShop").toString().isEmpty()) {
							customScoreFTEPPShop = Double.parseDouble(map.get("customeScoreForFTEPMShop").toString());
							log.info("CUSTOM_SCORE_FTE_PP ::: " + customScoreFTEPPShop);
						}
						if (null != map.get("customScoreForFTEPPShopOutsourcing")
								&& !map.get("customScoreForFTEPPShopOutsourcing").toString().isEmpty()) {
							customScoreFTEPPShopOut = Double
									.parseDouble(map.get("customScoreForFTEPPShopOutsourcing").toString());
							log.info("CUSTOM_SCORE_FTE_PP_OUT ::: " + customScoreFTEPPShopOut);
						}

						if (validationStatus && ppFlag && null != customScoreFTEPPShop
								&& null == customScoreFTEPPShopOut) {
							validationMessage.append("Both FTE PP and FTE PP Outsourcing need to be Filled");
							validationStatus = false;
						} else if (validationStatus && ppFlag && null != customScoreFTEPPShopOut
								&& null == customScoreFTEPPShop) {
							validationMessage.append("Both FTE PP and FTE PP Outsourcing need to be Filled");
							validationStatus = false;
						} else if (validationStatus && ppFlag && null != customScoreFTEPPShopOut
								&& null != customScoreFTEPPShop) {
							if (null != customScorePPFTE) {
								if (customScorePPFTE != roundTwoDecimals(
										(customScoreFTEPPShop + customScoreFTEPPShopOut))) {
									validationMessage.append(
											"Sum of FTE PP and FTE PP OUTSOURCING needs to match with project total");
									validationStatus = false;
								}
							} else {
								if (totalActualScoreFTEPP != roundTwoDecimals(
										(customScoreFTEPPShop + customScoreFTEPPShopOut))) {
									validationMessage.append(
											"Sum of FTE PP and FTE PP OUTSOURCING needs to match with project total");
									validationStatus = false;
								}
							}
						}
						if (validationStatus && !ppFlag) {
							map.put("customeScoreForFTEPMShop",
									saveNEComplexityResponseDTO.getCustomeScoreForFTEPMShop());
						}
					}
					if (validationStatus) {
						responseMap.put("status", "S");
						responseMap.put("errorMsg", "");
						responseMap.put("PP", saveNEComplexityResponseDTO);
					} else {
						saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
						responseMap.put("status", "E");
						responseMap.put("errorMsg", validationMessage);
						responseMap.put("data", saveNEComplexityResponseDTO);
					}
				} else if (null != role && !role.isEmpty()
						&& (role.equalsIgnoreCase("QPM") || role.equalsIgnoreCase("qpmModule"))) {
					String actionType = "";
					if (null != map.get("actionType") && !map.get("actionType").toString().isEmpty()) {
						actionType = map.get("actionType").toString();
						log.info("actionType ::: " + actionType);
						// if (actionType.equalsIgnoreCase("CHECK")) {
						// map.put("customScoreForPPFTE", "");
						// map.put("customeScoreForFTEPMShop", "");
						// map.put("customScoreForFTEPPShopOutsourcing", "");
						// map.put("customeScoreForFTEPMInstal", "");
						// map.put("customScoreForFTEPMInstallOutsourcing", "");
						// }
					}
					saveNEComplexityResponseDTO = calculateNEComplexityDataForQPM(map, role);
					if (validationStatus) {
						log.info("***** Validating Complexity values for " + role + " Role *****");

						if (null != map.get("projectWSPPOutsourcing")
								&& !map.get("projectWSPPOutsourcing").toString().isEmpty()) {
							ppFlag = Boolean.parseBoolean(map.get("projectWSPPOutsourcing").toString());
							log.info("QPM_OUTSOURCING ::: " + ppFlag);
						}
						if (null != saveNEComplexityResponseDTO.getActualScoreForPP()
								&& !saveNEComplexityResponseDTO.getActualScoreForPP().isEmpty()) {
							totalActualScoreFTEPP = Double
									.parseDouble(saveNEComplexityResponseDTO.getActualScoreForPP());
							log.info("TOTAL_ACTUAL_SCORE_PP ::: " + totalActualScoreFTEPP);
						}
						if (null != saveNEComplexityResponseDTO.getActualScoreForFTEPM()
								&& !saveNEComplexityResponseDTO.getActualScoreForFTEPM().isEmpty()) {
							actualScoreFTEPPShop = Double
									.parseDouble(saveNEComplexityResponseDTO.getActualScoreForFTEPM());
							log.info("ACTUAL_SCORE_FTE_PP ::: " + actualScoreFTEPPShop);
						}
						if (null != saveNEComplexityResponseDTO.getActualScoreForPPOutsourcing()
								&& !saveNEComplexityResponseDTO.getActualScoreForPPOutsourcing().isEmpty()) {
							actualScoreFTEPPShopOut = Double
									.parseDouble(saveNEComplexityResponseDTO.getActualScoreForPPOutsourcing());
							log.info("ACTUAL_SCORE_FTE_PP_OUT ::: " + actualScoreFTEPPShopOut);
						}
						if (null != map.get("customScoreForPPFTE")
								&& !map.get("customScoreForPPFTE").toString().isEmpty()) {
							customScorePPFTE = Double.parseDouble(map.get("customScoreForPPFTE").toString());
							log.info("CUSTOM_SCORE_PP ::: " + customScorePPFTE);
						}
						if (null != map.get("customeScoreForFTEPMShop")
								&& !map.get("customeScoreForFTEPMShop").toString().isEmpty()) {
							customScoreFTEPPShop = Double.parseDouble(map.get("customeScoreForFTEPMShop").toString());
							log.info("CUSTOM_SCORE_FTE_PP ::: " + customScoreFTEPPShop);
						}
						if (null != map.get("customScoreForFTEPPShopOutsourcing")
								&& !map.get("customScoreForFTEPPShopOutsourcing").toString().isEmpty()) {
							customScoreFTEPPShopOut = Double
									.parseDouble(map.get("customScoreForFTEPPShopOutsourcing").toString());
							log.info("CUSTOM_SCORE_FTE_PP_OUT ::: " + customScoreFTEPPShopOut);
						}
						if (null != map.get("customeScoreForFTEPMInstal")
								&& !map.get("customeScoreForFTEPMInstal").toString().isEmpty()) {
							customScoreFTEInstall = Double
									.parseDouble(map.get("customeScoreForFTEPMInstal").toString());
							log.info("CUSTOM_SCORE_FTE_INSTALL ::: " + customScoreFTEInstall);
						}
						if (null != map.get("customScoreForFTEPMInstallOutsourcing")
								&& !map.get("customScoreForFTEPMInstallOutsourcing").toString().isEmpty()) {
							customScoreFTEInstallOut = Double
									.parseDouble(map.get("customScoreForFTEPMInstallOutsourcing").toString());
							log.info("CUSTOM_SCORE_FTE_INSTALL_OUT ::: " + customScoreFTEInstallOut);
						}

						if (actionType.equalsIgnoreCase("CALCULATE")) {
							if (validationStatus && ppFlag && null != customScoreFTEPPShop
									&& null == customScoreFTEPPShopOut) {
								validationMessage.append("Both FTE QPM and FTE QPM Outsourcing need to be Filled");
								validationStatus = false;
							} else if (validationStatus && ppFlag && null != customScoreFTEPPShopOut
									&& null == customScoreFTEPPShop) {
								validationMessage.append("Both FTE QPM and FTE QPM Outsourcing need to be Filled");
								validationStatus = false;
							} else if (validationStatus && ppFlag && null != customScoreFTEInstall
									&& null == customScoreFTEInstallOut) {
								validationMessage.append("Both FTE QPM and FTE QPM Outsourcing need to be Filled");
								validationStatus = false;
							} else if (validationStatus && ppFlag && null != customScoreFTEInstallOut
									&& null == customScoreFTEInstall) {
								validationMessage.append("Both FTE QPM and FTE QPM Outsourcing need to be Filled");
								validationStatus = false;
							}
						}
						if (validationStatus && !ppFlag) {
							map.put("customeScoreForFTEPMShop",
									saveNEComplexityResponseDTO.getCustomeScoreForFTEPMShop());
						}
					}
					if (validationStatus) {
						responseMap.put("status", "S");
						responseMap.put("errorMsg", "");
						responseMap.put(role, saveNEComplexityResponseDTO);
					} else {
						saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
						responseMap.put("status", "E");
						responseMap.put("errorMsg", validationMessage);
						responseMap.put("data", saveNEComplexityResponseDTO);
					}
				}
			} else {
				saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
				responseMap.put("status", "E");
				responseMap.put("errorMsg", validationMessage);
				responseMap.put("data", saveNEComplexityResponseDTO);
			}
		} catch (Exception e) {
			log.error("calculateNEProjectComplexityData :: " + e.getMessage());
		}
		return responseMap;
	}

	private CalculateNEComplexityResponseDTO calculateNEComplexityDataForQPM(Map<String, Object> map, String role) {
		CalculateNEComplexityResponseDTO saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		Double scopeUnitTrain = 0.0, scopeProduct = 0.0, testQuality = 0.0, schedule = 0.0, customization = 0.0,
				actualScoreProjectComplexity = null, actualScoreFTEPP = null, actualScoreFTEPPOut = null,
				totalActualScoreFTEPP = null, customScorePPFTE = null, customScoreForFTEPMShop = null,
				customScoreFTEPPShopOut = null, customScoreForFTEPMInstall = null,
				customScoreForPMInstallOutSourcing = null;
		String actionType = "", actualScoreProjectComplexityStr = "", totalActualScoreFTEPPStr = "",
				actualScoreFTEPPStr = "", actualScoreFTEPPOutStr = "", customScoreForFTEPMShopStr = "",
				customScoreForPPOutSourcingStr = "", customScoreForFTEPMInstallStr = "",
				customScoreForPMInstallOutSourcingStr = "", customScoreForPPFTEStr = "";
		boolean ppFlag = false;
		try {
			log.info("***** Getting Complexity values for " + role + " Role *****");
			if (role.equalsIgnoreCase("QPM") || role.equalsIgnoreCase("qpmModule")) {
				if (null != map.get("scopeProduct") && !map.get("scopeProduct").toString().isEmpty()) {
					if (role.equalsIgnoreCase("QPM")) {
						scopeProduct = iProjectComplexityDAO.getNEComplexityValues("PROJECT_PRODUCT_SCOPE",
								map.get("scopeProduct").toString(), role);
						log.info("PROJECT_PRODUCT_SCOPE ::: " + scopeProduct);
					} else if (role.equalsIgnoreCase("qpmModule")) {
						scopeProduct = Double.parseDouble(map.get("scopeProduct").toString());
						log.info("PROJECT_PRODUCT_SCOPE ::: " + scopeProduct);
					}
				}
				if (null != map.get("scopeUnitTrain") && !map.get("scopeUnitTrain").toString().isEmpty()) {
					scopeUnitTrain = Double.parseDouble(map.get("scopeUnitTrain").toString());
					log.info("SCOPE_UNIT_TRAIN ::: " + scopeUnitTrain);
				}
				if (null != map.get("testQuality") && !map.get("testQuality").toString().isEmpty()) {
					testQuality = Double.parseDouble(map.get("testQuality").toString());
					log.info("TESTING_QUALITY_REQUIREMENTS ::: " + testQuality);
				}
				if (null != map.get("schedule") && !map.get("schedule").toString().isEmpty()) {
					schedule = iProjectComplexityDAO.getNEComplexityValues("PROJECT_SCHEDULE_AS_PLANNED",
							map.get("schedule").toString(), role);
					log.info("PROJECT_SCHEDULE_AS_PLANNED ::: " + schedule);
				}
				if (null != map.get("customization") && !map.get("customization").toString().isEmpty()) {
					customization = Double.parseDouble(map.get("customization").toString());
					log.info("LEVEL_OF_CUSTOMIZATION ::: " + customization);
				}
				if (null != map.get("projectWSPPOutsourcing")
						&& !map.get("projectWSPPOutsourcing").toString().isEmpty()) {
					ppFlag = Boolean.parseBoolean(map.get("projectWSPPOutsourcing").toString());
					log.info("QPM_OUTSOURCING ::: " + ppFlag);
				}
				if (null != map.get("actionType") && !map.get("actionType").toString().isEmpty()) {
					actionType = map.get("actionType").toString();
					log.info("actionType ::: " + actionType);
				}
				if (null != map.get("customScoreForPPFTE") && !map.get("customScoreForPPFTE").toString().isEmpty()) {
					customScorePPFTE = Double.parseDouble(map.get("customScoreForPPFTE").toString());
					log.info("CUSTOM_SCORE_PP ::: " + customScorePPFTE);
				}
				if (null != map.get("customeScoreForFTEPMShop")
						&& !map.get("customeScoreForFTEPMShop").toString().isEmpty()) {
					customScoreForFTEPMShop = Double.parseDouble(map.get("customeScoreForFTEPMShop").toString());
					log.info("CUSTOM_SCORE_FTE_PP_SHOP ::: " + customScoreForFTEPMShop);
				}
				if (null != map.get("customScoreForFTEPPShopOutsourcing")
						&& !map.get("customScoreForFTEPPShopOutsourcing").toString().isEmpty()) {
					customScoreFTEPPShopOut = Double
							.parseDouble(map.get("customScoreForFTEPPShopOutsourcing").toString());
					log.info("CUSTOM_SCORE_FTE_PP_SHOP_OUT ::: " + customScoreFTEPPShopOut);
				}
				if (null != map.get("customeScoreForFTEPMInstal")
						&& !map.get("customeScoreForFTEPMInstal").toString().isEmpty()) {
					customScoreForFTEPMInstall = Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString());
					log.info("CUSTOM_SCORE_FTE_INSTALL_PP ::: " + customScoreForFTEPMShop);
				}
				if (null != map.get("customScoreForFTEPMInstallOutsourcing")
						&& !map.get("customScoreForFTEPMInstallOutsourcing").toString().isEmpty()) {
					customScoreForPMInstallOutSourcing = Double
							.parseDouble(map.get("customScoreForFTEPMInstallOutsourcing").toString());
					log.info("CUSTOM_SCORE_FTE_PM_INSTALL_OUT ::: " + customScoreForPMInstallOutSourcing);
				}
				if (null != scopeUnitTrain || null != scopeProduct || null != testQuality || null != schedule
						|| null != customization) {
					actualScoreProjectComplexity = roundTwoDecimals(
							(scopeUnitTrain + scopeProduct + testQuality + schedule + customization));
					actualScoreProjectComplexityStr = String.valueOf(actualScoreProjectComplexity);
					totalActualScoreFTEPP = roundTwoDecimals(actualScoreProjectComplexity * 0.01);
				}
				actualScoreFTEPP = roundTwoDecimals(actualScoreProjectComplexity * 0.01);

				totalActualScoreFTEPPStr = null != totalActualScoreFTEPP ? String.valueOf(totalActualScoreFTEPP) : "";
				actualScoreFTEPPStr = null != actualScoreFTEPP ? String.valueOf(actualScoreFTEPP) : "";
				actualScoreFTEPPOutStr = null != actualScoreFTEPPOut ? String.valueOf(actualScoreFTEPPOut) : "";
				customScoreForPPFTEStr = null != customScorePPFTE ? String.valueOf(customScorePPFTE) : "";
				if (null == customScorePPFTE) {
					if (null != customScoreForFTEPMShop) {
						customScoreForPPFTEStr = String.valueOf(customScoreForFTEPMShop);
					} else if (null != customScoreForFTEPMInstall) {
						customScoreForPPFTEStr = String.valueOf(customScoreForFTEPMInstall);
					}
				}
				customScoreForFTEPMShopStr = null != customScoreForFTEPMShop ? String.valueOf(customScoreForFTEPMShop)
						: "";
				customScoreForPPOutSourcingStr = null != customScoreFTEPPShopOut
						? String.valueOf(customScoreFTEPPShopOut)
						: "";
				customScoreForFTEPMInstallStr = null != customScoreForFTEPMInstall
						? String.valueOf(customScoreForFTEPMInstall)
						: "";
				customScoreForPMInstallOutSourcingStr = null != customScoreForPMInstallOutSourcing
						? String.valueOf(customScoreForPMInstallOutSourcing)
						: "";
				saveNEComplexityResponseDTO.setActualScoreForPMComplex(actualScoreProjectComplexityStr);
				saveNEComplexityResponseDTO.setActualScoreForPP(totalActualScoreFTEPPStr);
				saveNEComplexityResponseDTO.setActualScoreForFTEPM(actualScoreFTEPPStr);
				saveNEComplexityResponseDTO.setActualScoreForPPOutsourcing(actualScoreFTEPPOutStr);
				saveNEComplexityResponseDTO.setCustomScoreForPPFTE(customScoreForPPFTEStr);
				saveNEComplexityResponseDTO.setCustomeScoreForFTEPMShop(customScoreForFTEPMShopStr);
				saveNEComplexityResponseDTO.setCustomScoreForFTEPPShopOutsourcing(customScoreForPPOutSourcingStr);
				saveNEComplexityResponseDTO.setCustomeScoreForFTEPMInstal(customScoreForFTEPMInstallStr);
				saveNEComplexityResponseDTO
						.setCustomScoreForFTEPMInstallOutsourcing(customScoreForPMInstallOutSourcingStr);
				return saveNEComplexityResponseDTO;
			}
		} catch (Exception e) {
			log.error("calculateNEComplexityDataForQPM :: ", e.getMessage());
		}
		return saveNEComplexityResponseDTO;
	}

	private CalculateNEComplexityResponseDTO calculateNEComplexityData(Map<String, Object> map, String role) {
		CalculateNEComplexityResponseDTO saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		Double mDollar = 0.0, scopeUnitTrain = 0.0, scopeProduct = 0.0, testQuality = 0.0, schedule = 0.0,
				customization = 0.0, actualScoreProjectComplexity = null, actualScoreFTEPP = null,
				actualScoreFTEPPOut = null, totalActualScoreFTEPP = null, customScorePPFTE = null,
				customScoreForFTEPMShop = null, customScoreFTEPPShopOut = null;
		String actionType = "", actualScoreProjectComplexityStr = "", totalActualScoreFTEPPStr = "",
				actualScoreFTEPPStr = "", actualScoreFTEPPOutStr = "", customScoreForFTEPMShopStr = "",
				customScoreForPPOutSourcingStr = "", customScoreForPPFTEStr = "";
		boolean ppFlag = false;
		try {
			log.info("***** Getting Complexity values for " + role + " Role *****");

			if (null != map.get("scopeProduct") && !map.get("scopeProduct").toString().isEmpty()) {
				scopeProduct = iProjectComplexityDAO.getNEComplexityValues("PROJECT_PRODUCT_SCOPE",
						map.get("scopeProduct").toString(), role);
				log.info("PROJECT_PRODUCT_SCOPE ::: " + scopeProduct);
			}
			if (null != map.get("scopeUnitTrain") && !map.get("scopeUnitTrain").toString().isEmpty()) {
				scopeUnitTrain = Double.parseDouble(map.get("scopeUnitTrain").toString());
				log.info("SCOPE_UNIT_TRAIN ::: " + scopeUnitTrain);
			}
			if (null != map.get("mDollar") && !map.get("mDollar").toString().isEmpty()) {
				mDollar = Double.parseDouble(map.get("mDollar").toString());
				log.info("mDOLLAR ::: " + mDollar);
			}
			if (null != map.get("testQuality") && !map.get("testQuality").toString().isEmpty()) {
				testQuality = iProjectComplexityDAO.getNEComplexityValues("TESTING_QUALITY_REQUIREMENTS",
						map.get("testQuality").toString(), role);
				log.info("TESTING_QUALITY_REQUIREMENTS ::: " + testQuality);
			}
			if (null != map.get("schedule") && !map.get("schedule").toString().isEmpty()) {
				schedule = iProjectComplexityDAO.getNEComplexityValues("PROJECT_SCHEDULE_AS_PLANNED",
						map.get("schedule").toString(), role);
				log.info("PROJECT_SCHEDULE_AS_PLANNED ::: " + schedule);
			}
			if (null != map.get("customization") && !map.get("customization").toString().isEmpty()) {
				customization = iProjectComplexityDAO.getNEComplexityValues("LEVEL_OF_CUSTOMIZATION",
						map.get("customization").toString(), role);
				log.info("LEVEL_OF_CUSTOMIZATION ::: " + customization);
			}
			if (null != map.get("projectWSPPOutsourcing") && !map.get("projectWSPPOutsourcing").toString().isEmpty()) {
				ppFlag = Boolean.parseBoolean(map.get("projectWSPPOutsourcing").toString());
				log.info("PP_OUTSOURCING ::: " + ppFlag);
			}
			if (null != map.get("customScoreForPPFTE") && !map.get("customScoreForPPFTE").toString().isEmpty()) {
				customScorePPFTE = Double.parseDouble(map.get("customScoreForPPFTE").toString());
				log.info("CUSTOM_SCORE_PP ::: " + customScorePPFTE);
			}
			if (null != map.get("customeScoreForFTEPMShop")
					&& !map.get("customeScoreForFTEPMShop").toString().isEmpty()) {
				customScoreForFTEPMShop = Double.parseDouble(map.get("customeScoreForFTEPMShop").toString());
				log.info("CUSTOM_SCORE_FTE_PP ::: " + customScoreForFTEPMShop);
			}
			if (null != map.get("customScoreForFTEPPShopOutsourcing")
					&& !map.get("customScoreForFTEPPShopOutsourcing").toString().isEmpty()) {
				customScoreFTEPPShopOut = Double.parseDouble(map.get("customScoreForFTEPPShopOutsourcing").toString());
				log.info("CUSTOM_SCORE_FTE_PP_OUT ::: " + customScoreFTEPPShopOut);
			}
			if (null != map.get("actionType") && !map.get("actionType").toString().isEmpty()) {
				actionType = map.get("actionType").toString();
				log.info("actionType ::: " + actionType);
			}

			if (null != mDollar || null != scopeUnitTrain || null != scopeProduct || null != testQuality
					|| null != schedule || null != customization || null != customScorePPFTE) {
				actualScoreProjectComplexity = roundTwoDecimals(
						(mDollar + scopeUnitTrain + scopeProduct + testQuality + schedule + customization));
				actualScoreProjectComplexityStr = String.valueOf(actualScoreProjectComplexity);
				totalActualScoreFTEPP = roundTwoDecimals(actualScoreProjectComplexity * 0.01);
				if (ppFlag) {
					if (null != customScorePPFTE && actionType.equalsIgnoreCase("CALCULATE")) {
						customScoreForFTEPMShop = roundTwoDecimals(customScorePPFTE * 0.2);
						customScoreFTEPPShopOut = roundTwoDecimals(customScorePPFTE * 0.8);
					}
					actualScoreFTEPP = roundTwoDecimals(totalActualScoreFTEPP * 0.2);
					actualScoreFTEPPOut = roundTwoDecimals(totalActualScoreFTEPP * 0.8);
				} else {
					actualScoreFTEPP = roundTwoDecimals(actualScoreProjectComplexity * 0.01);
					if (null != customScorePPFTE) {
						customScoreForFTEPMShop = customScorePPFTE;
					}
				}
			}
			totalActualScoreFTEPPStr = null != totalActualScoreFTEPP ? String.valueOf(totalActualScoreFTEPP) : "";
			actualScoreFTEPPStr = null != actualScoreFTEPP ? String.valueOf(actualScoreFTEPP) : "";
			actualScoreFTEPPOutStr = null != actualScoreFTEPPOut ? String.valueOf(actualScoreFTEPPOut) : "";
			customScoreForFTEPMShopStr = null != customScoreForFTEPMShop ? String.valueOf(customScoreForFTEPMShop) : "";
			customScoreForPPFTEStr = null != customScorePPFTE ? String.valueOf(customScorePPFTE) : "";
			customScoreForPPOutSourcingStr = null != customScoreFTEPPShopOut ? String.valueOf(customScoreFTEPPShopOut)
					: "";
			saveNEComplexityResponseDTO.setActualScoreForPMComplex(actualScoreProjectComplexityStr);
			saveNEComplexityResponseDTO.setActualScoreForPP(totalActualScoreFTEPPStr);
			saveNEComplexityResponseDTO.setActualScoreForFTEPM(actualScoreFTEPPStr);
			saveNEComplexityResponseDTO.setActualScoreForPPOutsourcing(actualScoreFTEPPOutStr);
			saveNEComplexityResponseDTO.setCustomeScoreForFTEPMShop(customScoreForFTEPMShopStr);
			saveNEComplexityResponseDTO.setCustomScoreForPPFTE(customScoreForPPFTEStr);
			saveNEComplexityResponseDTO.setCustomScoreForFTEPPShopOutsourcing(customScoreForPPOutSourcingStr);
		} catch (Exception e) {
			log.error("calculateNEComplexityData :: ", e.getMessage());
		}
		return saveNEComplexityResponseDTO;
	}

	public double roundTwoDecimals(double value) {
		return (double) Math.round(value * 100) / 100;
	}

	@Override
	public NEProjectComplexityDropDownDTO getNEProjectComplexityDropDownData(String projectId) {
		return iProjectComplexityDAO.getNEProjectComplexityDropDownData(projectId);
	}

	@Override
	public NEProjectComplexityDropDownDTO getNEPPProjectComplexityDropDownData(String projectId) {
		return iProjectComplexityDAO.getNEPPProjectComplexityDropDownData(projectId);
	}

	@Override
	public NEProjectComplexityDropDownDTO getNEQPMProjectComplexityDropDownData(String projectId) {
		return iProjectComplexityDAO.getNEQPMProjectComplexityDropDownData(projectId);
	}

	@Override
	public NEProjectComplexityDTO getNEProjectComplexityITOData(String projectId) {
		NEProjectComplexityDTO responseDTO = new NEProjectComplexityDTO();
		List<ProjectManagerDTO> pm = new ArrayList<ProjectManagerDTO>();
		List<ProjectPlannerDTO> pp = new ArrayList<ProjectPlannerDTO>();
		List<QualityProjectManagerDTO> qpm = new ArrayList<QualityProjectManagerDTO>();
		List<QualityProjectManagerDTO> qpmModule = new ArrayList<QualityProjectManagerDTO>();
		List<NEComplexityDTO> list = new ArrayList<NEComplexityDTO>();
		String role = "", scope = "", showQPMModuleFlag = "N";
		try {
			list = iProjectComplexityDAO.getNEProjectComplexityITOData(projectId);
			for (NEComplexityDTO complexityDTO : list) {
				role = complexityDTO.getRole();
				scope = complexityDTO.getScope();
				if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PM")) {
					ProjectManagerDTO pm1 = new ProjectManagerDTO();
					pm1.setScopeProduct(complexityDTO.getScopeProduct());
					pm1.setScopeUnitTrain(complexityDTO.getScopeUnitTrain());
					pm1.setmDollar(complexityDTO.getmDollar());
					pm1.setTestQuality(complexityDTO.getTestQuality());
					pm1.setSchedule(complexityDTO.getSchedule());
					pm1.setCustomization(complexityDTO.getCustomization());
					pm1.setNoOfUnits(complexityDTO.getNoOfUnits());
					pm1.setNoOfCostingProject(complexityDTO.getNoOfCostingProject());
					pm1.setNoOfTrain(complexityDTO.getNoOfTrain());
					pm1.setNoOfModules(complexityDTO.getNoOfModules());
					if (null != complexityDTO.getNoOfModules() && !complexityDTO.getNoOfModules().isEmpty()
							&& !complexityDTO.getNoOfModules().equalsIgnoreCase("")) {
						double moduleCnt = Double.parseDouble(complexityDTO.getNoOfModules());
						if (moduleCnt > 0.0) {
							showQPMModuleFlag = "Y";
						}
					}
					pm1.setInitialScoreForPMComplex(complexityDTO.getInitialScoreForPMComplex());
					pm1.setActualScoreForPMComplex(complexityDTO.getActualScoreForPMComplex());
					pm1.setActualScoreForFTEPM(complexityDTO.getActualScoreForFTEPM());
					pm1.setCustomeScoreForFTEPMShop(complexityDTO.getCustomeScoreForFTEPMShop());
					pm1.setCustomeScoreForFTEPMInstal(complexityDTO.getCustomeScoreForFTEPMInstal());
					pm1.setNotesForCustomScore(complexityDTO.getNotesForCustomScore());
					pm.add(pm1);
					responseDTO.setShowQPMModuleFlag(showQPMModuleFlag);
					responseDTO.setUpdatedBy(complexityDTO.getUpdatedBy());
					responseDTO.setUpdatedOn(complexityDTO.getUpdatedOn());
					responseDTO.setPm(pm);
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PP")) {
					ProjectPlannerDTO pp1 = new ProjectPlannerDTO();
					pp1.setScopeProduct(complexityDTO.getScopeProduct());
					pp1.setScopeUnitTrain(complexityDTO.getScopeUnitTrain());
					pp1.setmDollar(complexityDTO.getmDollar());
					pp1.setTestQuality(complexityDTO.getTestQuality());
					pp1.setSchedule(complexityDTO.getSchedule());
					pp1.setCustomization(complexityDTO.getCustomization());
					pp1.setProjectWSPPOutsourcing(complexityDTO.getPpOutSourcing());
					pp1.setInitialScoreForPMComplex(complexityDTO.getInitialScoreForPMComplex());
					pp1.setActualScoreForPMComplex(complexityDTO.getActualScoreForPMComplex());
					pp1.setActualScoreForFTEPM(complexityDTO.getActualScoreForFTEPM());
					pp1.setCustomeScoreForFTEPMShop(complexityDTO.getCustomeScoreForFTEPMShop());
					pp1.setCustomeScoreForFTEPMInstal(complexityDTO.getCustomeScoreForFTEPMInstal());
					pp1.setNotesForCustomScore(complexityDTO.getNotesForCustomScore());
					pp.add(pp1);
					responseDTO.setUpdatedBy(complexityDTO.getUpdatedBy());
					responseDTO.setUpdatedOn(complexityDTO.getUpdatedOn());
					responseDTO.setPp(pp);
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("QPM") && null != scope
						&& !scope.isEmpty() && scope.equalsIgnoreCase("unit")) {
					QualityProjectManagerDTO qpm1 = new QualityProjectManagerDTO();
					qpm1.setScopeProduct(complexityDTO.getScopeProduct());
					qpm1.setScopeUnitTrain(complexityDTO.getScopeUnitTrain());
					qpm1.setmDollar(complexityDTO.getmDollar());
					qpm1.setTestQuality(complexityDTO.getTestQuality());
					qpm1.setSchedule(complexityDTO.getSchedule());
					qpm1.setCustomization(complexityDTO.getCustomization());
					qpm1.setProjectWSPPOutsourcing(complexityDTO.getPpOutSourcing());
					qpm1.setInitialScoreForPMComplex(complexityDTO.getInitialScoreForPMComplex());
					qpm1.setActualScoreForPMComplex(complexityDTO.getActualScoreForPMComplex());
					qpm1.setActualScoreForFTEPM(complexityDTO.getActualScoreForFTEPM());
					qpm1.setCustomeScoreForFTEPMShop(complexityDTO.getCustomeScoreForFTEPMShop());
					qpm1.setCustomeScoreForFTEPMInstal(complexityDTO.getCustomeScoreForFTEPMInstal());
					qpm1.setNotesForCustomScore(complexityDTO.getNotesForCustomScore());
					qpm.add(qpm1);
					responseDTO.setUpdatedBy(complexityDTO.getUpdatedBy());
					responseDTO.setUpdatedOn(complexityDTO.getUpdatedOn());
					responseDTO.setQpm(qpm);
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("QPM") && null != scope
						&& !scope.isEmpty() && scope.equalsIgnoreCase("module")) {
					QualityProjectManagerDTO qpm1 = new QualityProjectManagerDTO();
					qpm1.setScopeProduct(complexityDTO.getScopeProduct());
					qpm1.setScopeUnitTrain(complexityDTO.getScopeUnitTrain());
					qpm1.setmDollar(complexityDTO.getmDollar());
					qpm1.setTestQuality(complexityDTO.getTestQuality());
					qpm1.setSchedule(complexityDTO.getSchedule());
					qpm1.setCustomization(complexityDTO.getCustomization());
					qpm1.setProjectWSPPOutsourcing(complexityDTO.getPpOutSourcing());
					qpm1.setInitialScoreForPMComplex(complexityDTO.getInitialScoreForPMComplex());
					qpm1.setActualScoreForPMComplex(complexityDTO.getActualScoreForPMComplex());
					qpm1.setActualScoreForFTEPM(complexityDTO.getActualScoreForFTEPM());
					qpm1.setCustomeScoreForFTEPMShop(complexityDTO.getCustomeScoreForFTEPMShop());
					qpm1.setCustomeScoreForFTEPMInstal(complexityDTO.getCustomeScoreForFTEPMInstal());
					qpm1.setNotesForCustomScore(complexityDTO.getNotesForCustomScore());
					qpmModule.add(qpm1);
					responseDTO.setUpdatedBy(complexityDTO.getUpdatedBy());
					responseDTO.setUpdatedOn(complexityDTO.getUpdatedOn());
					responseDTO.setQpmModule(qpmModule);
				}
			}
		} catch (Exception e) {
			log.error("getNEProjectComplexityITOData :: ", e.getMessage());
		}
		return responseDTO;
	}

	@Override
	public Map<String, Object> getNEComplexityProjectITOTabData(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ITOProjectComplexityTabDTO dto = new ITOProjectComplexityTabDTO();
		List<ITOSlotCCTDates> itoSlotDatesList = new ArrayList<ITOSlotCCTDates>();
		List<DropDownDTO> wlSegmentList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> wlProjectCategoryList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> orderTypeList = new ArrayList<DropDownDTO>();
		try {
			wlSegmentList = iProjectComplexityDAO.getWLSegmentDropDown(projectId);
			wlProjectCategoryList = iProjectComplexityDAO.getWLProjectCategoryDropDown(projectId);
			orderTypeList = iProjectComplexityDAO.getOrderTypeDropDown(projectId);
			itoSlotDatesList = iProjectComplexityDAO.getITOSlotWFDatesList(projectId);
			dto = iProjectComplexityDAO.getNEComplexityProjectITOTabData(projectId);
			dto.setDefaultWlProjectCategory("CORE");
			responseMap.put("WLSegment", wlSegmentList);
			responseMap.put("WLProjectCategory", wlProjectCategoryList);
			responseMap.put("orderType", orderTypeList);
			responseMap.put("itoSlotDatesList", itoSlotDatesList);
			responseMap.put("ProjectTabData", dto);
		} catch (Exception e) {
			log.error("getNEComplexityProjectITOTabData(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public SaveNEComplexityResponseDTO saveNEComplexityITOData(SaveNEComplexityDTO saveNEComplexityDTO) {
		SaveNEComplexityResponseDTO saveNEComplexityResponseDTO = new SaveNEComplexityResponseDTO();
		Map<String, Object> pmMap = new HashMap<String, Object>();
		Map<String, Object> ppMap = new HashMap<String, Object>();
		int pmResult = 0;
		int ppResult = 0;
		String sso = callerContext.getName();
		try {
			pmMap = saveNEComplexityDTO.getPm();
			pmMap = validateNEComplexityITOData(saveNEComplexityDTO.getProjectId(), pmMap, "PM");
			if (null != pmMap && !pmMap.isEmpty()) {
				if (null != pmMap.get("status").toString() && !pmMap.get("status").toString().isEmpty()
						&& pmMap.get("status").toString().equalsIgnoreCase("E")) {
					saveNEComplexityResponseDTO.setStatus("Error");
					if (null != pmMap.get("errorMsg").toString() && !pmMap.get("errorMsg").toString().isEmpty()) {
						saveNEComplexityResponseDTO.setErrorMsg(pmMap.get("errorMsg").toString());
					}
					return saveNEComplexityResponseDTO;
				} else if (null != pmMap.get("status").toString() && !pmMap.get("status").toString().isEmpty()
						&& pmMap.get("status").toString().equalsIgnoreCase("S")) {
					pmResult = iProjectComplexityDAO.saveNEComplexityPMITOData(saveNEComplexityDTO.getProjectId(),
							pmMap, sso);
				}
			}

			ppMap = saveNEComplexityDTO.getPp();
			ppMap = validateNEComplexityITOData(saveNEComplexityDTO.getProjectId(), ppMap, "PP");
			if (null != ppMap && !ppMap.isEmpty()) {
				if (null != ppMap.get("status").toString() && !ppMap.get("status").toString().isEmpty()
						&& ppMap.get("status").toString().equalsIgnoreCase("E")) {
					saveNEComplexityResponseDTO.setStatus("Error");
					if (null != ppMap.get("errorMsg").toString() && !ppMap.get("errorMsg").toString().isEmpty()) {
						saveNEComplexityResponseDTO.setErrorMsg(ppMap.get("errorMsg").toString());
					}
					return saveNEComplexityResponseDTO;
				} else if (null != ppMap.get("status").toString() && !ppMap.get("status").toString().isEmpty()
						&& ppMap.get("status").toString().equalsIgnoreCase("S")) {
					ppResult = iProjectComplexityDAO.saveNEComplexityPPITOData(saveNEComplexityDTO.getProjectId(),
							ppMap, sso);
				}
			}
			if (pmResult == 1 && ppResult == 1) {
				saveNEComplexityResponseDTO.setStatus("Success");
				saveNEComplexityResponseDTO.setErrorMsg("");
			} else {
				saveNEComplexityResponseDTO.setStatus("Error");
				saveNEComplexityResponseDTO.setErrorMsg("Error");
			}
		} catch (Exception e) {
			log.error("Failed to save NE Complexity ITO Data into db:", e.getMessage());
			saveNEComplexityResponseDTO.setStatus("Error");
			saveNEComplexityResponseDTO.setErrorMsg("Error");
			return saveNEComplexityResponseDTO;
		}
		return saveNEComplexityResponseDTO;

	}

	private Map<String, Object> validateNEComplexityITOData(String projectId, Map<String, Object> map, String role) {
		Map<String, Object> calculateMap = new HashMap<String, Object>();
		CalculateNEComplexityResponseDTO calculateResponseDTO = new CalculateNEComplexityResponseDTO();
		CalculateNEComplexityDTO calculateNEComplexityDTO = new CalculateNEComplexityDTO();
		calculateNEComplexityDTO = new CalculateNEComplexityDTO();
		try {
			if (null != map && !map.isEmpty()) {
				log.info("Saving NE Complexity ITO data for project :::: " + projectId);
				log.info("Before Calculation :: actualScoreForPMComplex :: "
						+ map.get("actualScoreForPMComplex").toString() + " :: actualScoreForFTEPM :: "
						+ map.get("actualScoreForFTEPM").toString());
				calculateNEComplexityDTO.setProjectId(projectId);
				calculateNEComplexityDTO.setRole(role);
				calculateNEComplexityDTO.setData(map);
				calculateMap = calculateNEProjectComplexityITOData(calculateNEComplexityDTO);
				if (null != calculateMap && !calculateMap.isEmpty()) {
					if (null != calculateMap.get("status").toString()
							&& !calculateMap.get("status").toString().isEmpty()
							&& calculateMap.get("status").toString().equalsIgnoreCase("E")) {
						map.put("status", "E");
						if (null != calculateMap.get("errorMsg").toString()
								&& !calculateMap.get("errorMsg").toString().isEmpty()) {
							log.info("Inside loop errorMsg");
							map.put("errorMsg", calculateMap.get("errorMsg").toString());
						}
					} else if (null != calculateMap.get("status").toString()
							&& !calculateMap.get("status").toString().isEmpty()
							&& calculateMap.get("status").toString().equalsIgnoreCase("S")) {
						map.put("status", "S");
						if (null != calculateMap.get(role)) {
							calculateResponseDTO = (CalculateNEComplexityResponseDTO) calculateMap.get(role);
							map.put("actualScoreForPMComplex", calculateResponseDTO.getActualScoreForPMComplex());
							map.put("actualScoreForFTEPM", calculateResponseDTO.getActualScoreForFTEPM());
						}
					}
				}
				log.info("After Calculation :: actualScoreForPMComplex :: "
						+ map.get("actualScoreForPMComplex").toString() + " :: actualScoreForFTEPM :: "
						+ map.get("actualScoreForFTEPM").toString());
			}
		} catch (Exception e) {
			log.error("validateNEComplexityITOData :: ", e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> calculateNEProjectComplexityITOData(CalculateNEComplexityDTO calculateNEComplexityDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		CalculateNEComplexityResponseDTO saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		StringBuilder validationMessage = new StringBuilder();
		boolean validationStatus = true;
		String role = "";
		Double scopeUnitTrain;
		try {
			Map<String, Object> map = calculateNEComplexityDTO.getData();
			role = calculateNEComplexityDTO.getRole();
			if (validationStatus && null != map.get("scopeUnitTrain")
					&& !map.get("scopeUnitTrain").toString().isEmpty()) {
				scopeUnitTrain = Double.parseDouble(map.get("scopeUnitTrain").toString());
				if (scopeUnitTrain > 40) {
					validationMessage.append("Scope (Unit & Train) should be less than 40");
					validationStatus = false;
				}
			}
			if (validationStatus) {
				if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PM")) {
					saveNEComplexityResponseDTO = calculateNEComplexityITOData(map, "PM");
					responseMap.put("status", "S");
					responseMap.put("errorMsg", "");
					responseMap.put("PM", saveNEComplexityResponseDTO);
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PP")) {
					saveNEComplexityResponseDTO = calculateNEComplexityITOData(map, "PP");
					responseMap.put("status", "S");
					responseMap.put("errorMsg", "");
					responseMap.put("PP", saveNEComplexityResponseDTO);
				}
			} else {
				responseMap.put("status", "E");
				responseMap.put("errorMsg", validationMessage);
				responseMap.put("data", saveNEComplexityResponseDTO);
			}
		} catch (Exception e) {
			log.error("calculateNEProjectComplexityITOData :: " + e.getMessage());
		}
		return responseMap;

	}

	private CalculateNEComplexityResponseDTO calculateNEComplexityITOData(Map<String, Object> map, String role) {
		CalculateNEComplexityResponseDTO saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		Double mDollar = 0.0, scopeUnitTrain = 0.0, scopeProduct = 0.0, testQuality = 0.0, schedule = 0.0,
				customization = 0.0, actualScorePM = 0.0, actualScoreFT = 0.0;
		String actualScoreForPMComplex = "", actualScoreForFTEPM = "";
		try {
			log.info("***** Getting Complexity values for " + role + " Role *****");
			if (null != map.get("scopeProduct") && !map.get("scopeProduct").toString().isEmpty()) {
				scopeProduct = Double.parseDouble(map.get("scopeProduct").toString());
			}
			if (null != map.get("scopeUnitTrain") && !map.get("scopeUnitTrain").toString().isEmpty()) {
				scopeUnitTrain = Double.parseDouble(map.get("scopeUnitTrain").toString());
				log.info("SCOPE_UNIT_TRAIN ::: " + scopeUnitTrain);
			}
			if (null != map.get("mDollar") && !map.get("mDollar").toString().isEmpty()) {
				mDollar = Double.parseDouble(map.get("mDollar").toString());
				log.info("mDOLLAR ::: " + mDollar);
			}
			if (null != map.get("testQuality") && !map.get("testQuality").toString().isEmpty()) {
				testQuality = Double.parseDouble(map.get("testQuality").toString());
			}
			if (null != map.get("schedule") && !map.get("schedule").toString().isEmpty()) {
				schedule = Double.parseDouble(map.get("schedule").toString());
			}
			if (null != map.get("customization") && !map.get("customization").toString().isEmpty()) {
				customization = Double.parseDouble(map.get("customization").toString());
			}
			if (mDollar != 0.0 || scopeUnitTrain != 0.0 || scopeProduct != 0.0 || testQuality != 0.0 || schedule != 0.0
					|| customization != 0.0) {
				actualScorePM = (mDollar + scopeUnitTrain + scopeProduct + testQuality + schedule + customization);
				actualScoreForPMComplex = String.valueOf(actualScorePM);
				actualScoreFT = (actualScorePM / 100);
				actualScoreForFTEPM = String.valueOf(actualScoreFT);
			}
			saveNEComplexityResponseDTO.setActualScoreForPMComplex(actualScoreForPMComplex);
			saveNEComplexityResponseDTO.setActualScoreForFTEPM(actualScoreForFTEPM);

		} catch (Exception e) {
			log.error("calculateNEComplexityITOData :: ", e.getMessage());
		}
		return saveNEComplexityResponseDTO;
	}

	@Override
	public SaveNEComplexityResponseDTO saveITOComplexityDataQPM(SaveNEComplexityDTO saveNEComplexityDTO) {
		SaveNEComplexityResponseDTO saveNEComplexityResponseDTO = new SaveNEComplexityResponseDTO();
		Map<String, Object> qpmMap = new HashMap<String, Object>();
		Map<String, Object> qpmModuleMap = new HashMap<String, Object>();
		int result = 0;
		try {
			String sso = callerContext.getName();
			if (null != saveNEComplexityDTO.getQpm()) {
				qpmMap = saveNEComplexityDTO.getQpm();
				qpmMap = validateITOComplexityDataQPM(saveNEComplexityDTO.getProjectId(), qpmMap, "QPM");
				if (null != qpmMap && !qpmMap.isEmpty()) {
					if (null != qpmMap.get("status").toString() && !qpmMap.get("status").toString().isEmpty()
							&& qpmMap.get("status").toString().equalsIgnoreCase("E")) {
						saveNEComplexityResponseDTO.setStatus("Error");
						if (null != qpmMap.get("errorMsg").toString() && !qpmMap.get("errorMsg").toString().isEmpty()) {
							saveNEComplexityResponseDTO.setErrorMsg(qpmMap.get("errorMsg").toString());
						}
						return saveNEComplexityResponseDTO;
					} else if (null != qpmMap.get("status").toString() && !qpmMap.get("status").toString().isEmpty()
							&& qpmMap.get("status").toString().equalsIgnoreCase("S")) {
						result = iProjectComplexityDAO.saveITOComplexityDataQPM(saveNEComplexityDTO.getProjectId(),
								qpmMap, sso);
					}
				}
			}
			if (null != saveNEComplexityDTO.getQpmModule()) {
				qpmModuleMap = saveNEComplexityDTO.getQpmModule();
				qpmModuleMap = validateITOComplexityDataQPM(saveNEComplexityDTO.getProjectId(), qpmModuleMap,
						"qpmModule");
				if (null != qpmModuleMap && !qpmModuleMap.isEmpty()) {
					if (null != qpmModuleMap.get("status").toString()
							&& !qpmModuleMap.get("status").toString().isEmpty()
							&& qpmModuleMap.get("status").toString().equalsIgnoreCase("E")) {
						saveNEComplexityResponseDTO.setStatus("Error");
						if (null != qpmModuleMap.get("errorMsg").toString()
								&& !qpmModuleMap.get("errorMsg").toString().isEmpty()) {
							saveNEComplexityResponseDTO.setErrorMsg(qpmModuleMap.get("errorMsg").toString());
						}
						return saveNEComplexityResponseDTO;
					} else if (null != qpmModuleMap.get("status").toString()
							&& !qpmModuleMap.get("status").toString().isEmpty()
							&& qpmModuleMap.get("status").toString().equalsIgnoreCase("S")) {
						result = iProjectComplexityDAO
								.saveITOComplexityDataQPMModule(saveNEComplexityDTO.getProjectId(), qpmModuleMap, sso);
					}
				}
			}
			if (result == 1) {
				saveNEComplexityResponseDTO.setStatus("Success");
				saveNEComplexityResponseDTO.setErrorMsg("");
			} else {
				saveNEComplexityResponseDTO.setStatus("Error");
				saveNEComplexityResponseDTO.setErrorMsg("Error");
			}
		} catch (Exception e) {
			log.error("Failed to save NE Complexity ITO Data into db:", e.getMessage());
			saveNEComplexityResponseDTO.setStatus("Error");
			saveNEComplexityResponseDTO.setErrorMsg("Error");
			return saveNEComplexityResponseDTO;
		}
		return saveNEComplexityResponseDTO;
	}

	private Map<String, Object> validateITOComplexityDataQPM(String projectId, Map<String, Object> map, String role) {
		Map<String, Object> calculateMap = new HashMap<String, Object>();
		CalculateNEComplexityResponseDTO calculateResponseDTO = new CalculateNEComplexityResponseDTO();
		CalculateNEComplexityDTO calculateNEComplexityDTO = new CalculateNEComplexityDTO();
		calculateNEComplexityDTO = new CalculateNEComplexityDTO();
		try {
			if (null != map && !map.isEmpty()) {
				log.info("Saving NE Complexity ITO data for project :::: " + projectId);
				log.info("Before Calculation :: actualScoreForPMComplex :: "
						+ map.get("actualScoreForPMComplex").toString() + " :: actualScoreForFTEPM :: "
						+ map.get("actualScoreForFTEPM").toString());
				calculateNEComplexityDTO.setProjectId(projectId);
				calculateNEComplexityDTO.setRole(role);
				calculateNEComplexityDTO.setData(map);
				calculateMap = calculateITOProjectComplexityDataQPM(calculateNEComplexityDTO);
				if (null != calculateMap && !calculateMap.isEmpty()) {
					if (null != calculateMap.get("status").toString()
							&& !calculateMap.get("status").toString().isEmpty()
							&& calculateMap.get("status").toString().equalsIgnoreCase("E")) {
						map.put("status", "E");
						if (null != calculateMap.get("errorMsg").toString()
								&& !calculateMap.get("errorMsg").toString().isEmpty()) {
							log.info("Inside loop errorMsg");
							map.put("errorMsg", calculateMap.get("errorMsg").toString());
						}
					} else if (null != calculateMap.get("status").toString()
							&& !calculateMap.get("status").toString().isEmpty()
							&& calculateMap.get("status").toString().equalsIgnoreCase("S")) {
						map.put("status", "S");
						if (null != calculateMap.get(role)) {
							calculateResponseDTO = (CalculateNEComplexityResponseDTO) calculateMap.get(role);
							map.put("actualScoreForPMComplex", calculateResponseDTO.getActualScoreForPMComplex());
							map.put("actualScoreForFTEPM", calculateResponseDTO.getActualScoreForFTEPM());
						}
					}
				}
				log.info("After Calculation :: actualScoreForPMComplex :: "
						+ map.get("actualScoreForPMComplex").toString() + " :: actualScoreForFTEPM :: "
						+ map.get("actualScoreForFTEPM").toString());
			}
		} catch (Exception e) {
			log.error("validateNEComplexityITOData :: ", e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> calculateITOProjectComplexityDataQPM(CalculateNEComplexityDTO calculateNEComplexityDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		CalculateNEComplexityResponseDTO saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		StringBuilder validationMessage = new StringBuilder();
		boolean validationStatus = true;
		String role = "";
		try {
			Map<String, Object> map = calculateNEComplexityDTO.getData();
			role = calculateNEComplexityDTO.getRole();
			if (validationStatus) {
				if (null != role && !role.isEmpty()) {
					saveNEComplexityResponseDTO = calculateITOProjectComplexityDataQPM(map, role);
					responseMap.put("status", "S");
					responseMap.put("errorMsg", "");
					responseMap.put(role, saveNEComplexityResponseDTO);
				}
			} else {
				responseMap.put("status", "E");
				responseMap.put("errorMsg", validationMessage);
				responseMap.put("data", saveNEComplexityResponseDTO);
			}
		} catch (Exception e) {
			log.error("calculateITOProjectComplexityDataQPM :: " + e.getMessage());
		}
		return responseMap;
	}

	private CalculateNEComplexityResponseDTO calculateITOProjectComplexityDataQPM(Map<String, Object> map,
			String role) {
		CalculateNEComplexityResponseDTO saveNEComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		Double scopeUnitTrain = 0.0, scopeProduct = 0.0, testQuality = 0.0, schedule = 0.0, customization = 0.0,
				actualScorePM = 0.0, actualScoreFT = 0.0;
		String actualScoreForPMComplex = "", actualScoreForFTEPM = "";
		try {
			log.info("***** Getting Complexity values for " + role + " Role *****");
			if (null != map.get("scopeProduct") && !map.get("scopeProduct").toString().isEmpty()) {
				scopeProduct = Double.parseDouble(map.get("scopeProduct").toString());
				log.info("PROJECT_PRODUCT_SCOPE ::: " + scopeProduct);
			}
			if (null != map.get("scopeUnitTrain") && !map.get("scopeUnitTrain").toString().isEmpty()) {
				scopeUnitTrain = Double.parseDouble(map.get("scopeUnitTrain").toString());
				log.info("SCOPE_UNIT_TRAIN ::: " + scopeUnitTrain);
			}
			if (null != map.get("testQuality") && !map.get("testQuality").toString().isEmpty()) {
				testQuality = Double.parseDouble(map.get("testQuality").toString());
				log.info("TESTING_QUALITY_REQUIREMENTS ::: " + testQuality);
			}
			if (null != map.get("schedule") && !map.get("schedule").toString().isEmpty()) {
				schedule = Double.parseDouble(map.get("schedule").toString());
				log.info("PROJECT_SCHEDULE_AS_PLANNED ::: " + schedule);
			}
			if (null != map.get("customization") && !map.get("customization").toString().isEmpty()) {
				customization = Double.parseDouble(map.get("customization").toString());
				log.info("LEVEL_OF_CUSTOMIZATION ::: " + customization);
			}
			if (scopeUnitTrain != 0.0 || scopeProduct != 0.0 || testQuality != 0.0 || schedule != 0.0
					|| customization != 0.0) {
				actualScorePM = (scopeUnitTrain + scopeProduct + testQuality + schedule + customization);
				actualScoreForPMComplex = String.valueOf(actualScorePM);
				actualScoreFT = (actualScorePM / 100);
				actualScoreForFTEPM = String.valueOf(actualScoreFT);
			}
			saveNEComplexityResponseDTO.setActualScoreForPMComplex(actualScoreForPMComplex);
			saveNEComplexityResponseDTO.setActualScoreForFTEPM(actualScoreForFTEPM);
		} catch (Exception e) {
			log.error("calculateITOProjectComplexityDataQPM :: ", e.getMessage());
		}
		return saveNEComplexityResponseDTO;
	}

	@Override
	public SRVProjectComplexityDTO getProjectComplexitySRVData(String projectId) {
		SRVProjectComplexityDTO responseDto = new SRVProjectComplexityDTO();
		List<ProjectManagerSRVDTO> pm = new ArrayList<ProjectManagerSRVDTO>();
		List<ProjectPlannerSRVDTO> pp = new ArrayList<ProjectPlannerSRVDTO>();
		List<SRVComplexityDTO> list = new ArrayList<SRVComplexityDTO>();
		String role = "";
		try {
			list = iProjectComplexityDAO.getSRVProjectComplexityData(projectId);
			for (SRVComplexityDTO complexityDto : list) {
				role = complexityDto.getRole();
				if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PM")) {
					ProjectManagerSRVDTO pm1 = new ProjectManagerSRVDTO();
					pm1.setScopeProduct(complexityDto.getScopeProduct());
					pm1.setOtherLegalEntity(complexityDto.getOtherLegalEntity());
					pm1.setmDollar(complexityDto.getmDollar());
					pm1.setPassTrough(complexityDto.getPassTrough());
					pm1.setCustomerReporting(complexityDto.getCustomerReporting());
					pm1.setLocation(complexityDto.getLocation());
					pm1.setInitialScoreForPMComplex(complexityDto.getInitialScoreForPMComplex());
					pm1.setActualScoreForPMComplex(complexityDto.getActualScoreForPMComplex());
					pm1.setActualScoreForFTEPM(complexityDto.getActualScoreForFTEPM());
					pm1.setNotesForCustomScore(complexityDto.getNotesForCustomScore());
					pm.add(pm1);
					responseDto.setUpdatedBy(complexityDto.getUpdatedBy());
					responseDto.setUpdatedOn(complexityDto.getUpdatedOn());
					responseDto.setPm(pm);
				} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PP")) {
					ProjectPlannerSRVDTO pp1 = new ProjectPlannerSRVDTO();
					pp1.setScopeProduct(complexityDto.getScopeProduct());
					pp1.setOmContribution(complexityDto.getOmContribution());
					pp1.setActualScoreForPMComplex(complexityDto.getActualScoreForPMComplex());
					pp1.setActualScoreForFTEPM(complexityDto.getActualScoreForFTEPM());
					pp.add(pp1);
					responseDto.setUpdatedBy(complexityDto.getUpdatedBy());
					responseDto.setUpdatedOn(complexityDto.getUpdatedOn());
					responseDto.setPp(pp);
				}
			}
		} catch (Exception e) {
			log.error("getSRVProjectComplexityData :: ", e.getMessage());
		}
		return responseDto;
	}

	@Override
	public SRVProjectComplexityDropDownDTO getSRVPMProjectComplexityDropDownData(String projectId) {
		return iProjectComplexityDAO.getSRVPMProjectComplexityDropDownData(projectId);
	}

	@Override
	public SRVPPProjectComplexityDropDownDTO getSRVPPProjectComplexityDropDownData(String projectId) {
		return iProjectComplexityDAO.getSRVPPProjectComplexityDropDownData(projectId);
	}

	@Override
	public Map<String, Object> calculateSRVProjectComplexityData(CalculateSRVComplexityDTO calculateSRVComplexityDTO) {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		CalculateNEComplexityResponseDTO saveSRVComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		StringBuilder validationMessage = new StringBuilder();
		String role = "";
		try {
			Map<String, Object> map = calculateSRVComplexityDTO.getData();
			role = calculateSRVComplexityDTO.getRole();
			if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PM")) {
				saveSRVComplexityResponseDTO = calculateSRVComplexityData(map, "PM");
				responseMap.put("status", "S");
				responseMap.put("errorMsg", "");
				responseMap.put("PM", saveSRVComplexityResponseDTO);
			} else {
				responseMap.put("status", "E");
				responseMap.put("errorMsg", validationMessage);
				responseMap.put("data", saveSRVComplexityResponseDTO);
			}
		} catch (Exception e) {
			log.error("calculateSRVProjectComplexityData :: " + e.getMessage());
		}
		return responseMap;

	}

	private CalculateNEComplexityResponseDTO calculateSRVComplexityData(Map<String, Object> map, String role) {
		CalculateNEComplexityResponseDTO saveSRVComplexityResponseDTO = new CalculateNEComplexityResponseDTO();
		Double mDollar = 0.0, otherLegalEntity = 0.0, scopeProduct = 0.0, passTrough = 0.0, customerReporting = 0.0,
				location = 0.0, actualScorePM = 0.0, actualScoreFT = 0.0;
		String actualScoreForPMComplex = "", actualScoreForFTEPM = "";
		try {
			log.info("***** Getting Complexity values for " + role + " Role *****");
			if (null != map.get("scopeProduct").toString() && !map.get("scopeProduct").toString().isEmpty()) {
				scopeProduct = Double.parseDouble(map.get("scopeProduct").toString());
			}
			if (null != map.get("otherLegalEntity").toString() && !map.get("otherLegalEntity").toString().isEmpty()) {
				otherLegalEntity = Double.parseDouble(map.get("otherLegalEntity").toString());
				log.info("OTHER_LEGAL_ENTITY ::: " + otherLegalEntity);
			}
			if (null != map.get("passTrough").toString() && !map.get("passTrough").toString().isEmpty()) {
				passTrough = Double.parseDouble(map.get("passTrough").toString());
				log.info("PASS_TROUGHT ::: " + passTrough);
			}
			if (null != map.get("customerReporting").toString() && !map.get("customerReporting").toString().isEmpty()) {
				customerReporting = Double.parseDouble(map.get("customerReporting").toString());
			}
			if (null != map.get("location").toString() && !map.get("location").toString().isEmpty()) {
				location = Double.parseDouble(map.get("location").toString());
			}
			if (null != map.get("mDollar") && !map.get("mDollar").toString().isEmpty()) {
				mDollar = Double.parseDouble(map.get("mDollar").toString());
			}
			if (mDollar != 0.0 || location != 0.0 || scopeProduct != 0.0 || customerReporting != 0.0
					|| passTrough != 0.0 || otherLegalEntity != 0.0) {
				actualScorePM = (mDollar + location + scopeProduct + customerReporting + passTrough + otherLegalEntity);
				actualScoreForPMComplex = String.valueOf(actualScorePM);
				actualScoreFT = (actualScorePM / 100);
				actualScoreForFTEPM = String.valueOf(actualScoreFT);
			}
			saveSRVComplexityResponseDTO.setActualScoreForPMComplex(actualScoreForPMComplex);
			saveSRVComplexityResponseDTO.setActualScoreForFTEPM(actualScoreForFTEPM);

		} catch (Exception e) {
			log.error("calculateSRVComplexityData :: ", e.getMessage());
		}
		return saveSRVComplexityResponseDTO;
	}

	@Override
	public SaveNEComplexityResponseDTO saveSRVComplexityData(saveSRVComplexityDTO saveSRVComplexityDTO) {

		SaveNEComplexityResponseDTO saveNEComplexityResponseDTO = new SaveNEComplexityResponseDTO();
		Map<String, Object> pmMap = new HashMap<String, Object>();
		int pmResult = 0;
		String sso = callerContext.getName();
		try {
			pmMap = saveSRVComplexityDTO.getPm();
			pmMap = validateSRVComplexityData(saveSRVComplexityDTO.getProjectId(), pmMap, "PM");
			if (null != pmMap && !pmMap.isEmpty()) {
				if (null != pmMap.get("status").toString() && !pmMap.get("status").toString().isEmpty()
						&& pmMap.get("status").toString().equalsIgnoreCase("E")) {
					saveNEComplexityResponseDTO.setStatus("Error");
					if (null != pmMap.get("errorMsg").toString() && !pmMap.get("errorMsg").toString().isEmpty()) {
						saveNEComplexityResponseDTO.setErrorMsg(pmMap.get("errorMsg").toString());
					}
					return saveNEComplexityResponseDTO;
				} else if (null != pmMap.get("status").toString() && !pmMap.get("status").toString().isEmpty()
						&& pmMap.get("status").toString().equalsIgnoreCase("S")) {
					pmResult = iProjectComplexityDAO.saveSRVComplexityPMData(saveSRVComplexityDTO.getProjectId(), pmMap,
							sso);
				}
			}

			if (pmResult == 1) {
				saveNEComplexityResponseDTO.setStatus("Success");
				saveNEComplexityResponseDTO.setErrorMsg("");
			} else {
				saveNEComplexityResponseDTO.setStatus("Error");
				saveNEComplexityResponseDTO.setErrorMsg("Error");
			}
		} catch (Exception e) {
			log.error("Failed to save SRV Complexity Data into db:", e.getMessage());
			saveNEComplexityResponseDTO.setStatus("Error");
			saveNEComplexityResponseDTO.setErrorMsg("Error");
			return saveNEComplexityResponseDTO;
		}
		return saveNEComplexityResponseDTO;
	}

	private Map<String, Object> validateSRVComplexityData(String projectId, Map<String, Object> map, String role) {
		Map<String, Object> calculateMap = new HashMap<String, Object>();
		CalculateNEComplexityResponseDTO calculateResponseDTO = new CalculateNEComplexityResponseDTO();
		CalculateSRVComplexityDTO calculateSRVComplexityDTO = new CalculateSRVComplexityDTO();
		calculateSRVComplexityDTO = new CalculateSRVComplexityDTO();
		try {
			if (null != map && !map.isEmpty()) {
				log.info("Saving SRV Complexity  data for project :::: " + projectId);
				log.info("Before Calculation :: actualScoreForPMComplex :: "
						+ map.get("actualScoreForPMComplex").toString() + " :: actualScoreForFTEPM :: "
						+ map.get("actualScoreForFTEPM").toString());
				calculateSRVComplexityDTO.setProjectId(projectId);
				calculateSRVComplexityDTO.setRole(role);
				calculateSRVComplexityDTO.setData(map);
				calculateMap = calculateSRVProjectComplexityData(calculateSRVComplexityDTO);
				if (null != calculateMap && !calculateMap.isEmpty()) {
					if (null != calculateMap.get("status").toString()
							&& !calculateMap.get("status").toString().isEmpty()
							&& calculateMap.get("status").toString().equalsIgnoreCase("E")) {
						map.put("status", "E");
						if (null != calculateMap.get("errorMsg").toString()
								&& !calculateMap.get("errorMsg").toString().isEmpty()) {
							log.info("Inside loop errorMsg");
							map.put("errorMsg", calculateMap.get("errorMsg").toString());
						}
					} else if (null != calculateMap.get("status").toString()
							&& !calculateMap.get("status").toString().isEmpty()
							&& calculateMap.get("status").toString().equalsIgnoreCase("S")) {
						map.put("status", "S");
						if (null != calculateMap.get(role)) {
							calculateResponseDTO = (CalculateNEComplexityResponseDTO) calculateMap.get(role);
							map.put("actualScoreForPMComplex", calculateResponseDTO.getActualScoreForPMComplex());
							map.put("actualScoreForFTEPM", calculateResponseDTO.getActualScoreForFTEPM());
						}
					}
				}
				log.info("After Calculation :: actualScoreForPMComplex :: "
						+ map.get("actualScoreForPMComplex").toString() + " :: actualScoreForFTEPM :: "
						+ map.get("actualScoreForFTEPM").toString());
			}
		} catch (Exception e) {
			log.error("validateSRVComplexityData :: ", e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getProjectSummaryDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ProjectsSummaryDetailsDTO projectSummary = new ProjectsSummaryDetailsDTO();
		try {
			projectSummary = iProjectComplexityDAO.getProjectSummaryDetails(projectId);
			responseMap.put("data", projectSummary);
		} catch (Exception e) {
			log.error("getProjectSummaryDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveNEComplexityProjectTabData(NEComplexityTabDTO tabDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		int result = 0;
		try {
			String sso = callerContext.getName();
			result = iProjectComplexityDAO.saveNEComplexityProjectTabData(tabDTO, sso);
			if (null != tabDTO.getOrderTypeUpdateFlag() && !tabDTO.getOrderTypeUpdateFlag().isEmpty()
					&& tabDTO.getOrderTypeUpdateFlag().equalsIgnoreCase("Y")) {
				result = iProjectComplexityDAO.saveNEComplexityProjectTabOrderDetails(tabDTO, sso);
			}
			if (result == 1) {
				responseMap.put("status", "Success");
				responseMap.put("errorMsg", "");
			} else {
				responseMap.put("status", "Error");
				responseMap.put("errorMsg", "Error");
			}
		} catch (Exception e) {
			responseMap.put("status", "Error");
			log.error("saveNEComplexityProjectTabData(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveITOComplexityProjectTabData(ITOComplexityTabDTO tabDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		int result = 0;
		try {
			String sso = callerContext.getName();
			result = iProjectComplexityDAO.saveITOComplexityProjectTabData(tabDTO, sso);
			if (null != tabDTO.getOrderTypeUpdateFlag() && !tabDTO.getOrderTypeUpdateFlag().isEmpty()
					&& tabDTO.getOrderTypeUpdateFlag().equalsIgnoreCase("Y")) {
				result = iProjectComplexityDAO.saveITOComplexityProjectTabOrderDetails(tabDTO, sso);
			}
			if (result == 1) {
				responseMap.put("status", "Success");
				responseMap.put("errorMsg", "");
			} else {
				responseMap.put("status", "Error");
				responseMap.put("errorMsg", "Error");
			}
		} catch (Exception e) {
			responseMap.put("status", "Error");
			log.error("saveITOComplexityProjectTabData(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	public DropDownDTO getOverallProgressPercent(String projectId) {
		DropDownDTO dto = new DropDownDTO();
		try {
			dto = iProjectComplexityDAO.getOverallProgressPercent(projectId);
		} catch (Exception e) {
			log.error("getOverallProgressPercent() :: Exception occurred :: " + e.getMessage());
		}
		return dto;
	}

}
