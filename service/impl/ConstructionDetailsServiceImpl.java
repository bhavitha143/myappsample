package com.bh.realtrack.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IConstructionDetailsDAO;
import com.bh.realtrack.dto.ActivityDetailsDTO;
import com.bh.realtrack.dto.ConstructionDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IConstructionDetailsService;

@Service
public class ConstructionDetailsServiceImpl implements IConstructionDetailsService {
	private static Logger log = LoggerFactory.getLogger(ConstructionDetailsServiceImpl.class.getName());

	private IConstructionDetailsDAO iConstructionDetailsDAO;
	private CallerContext callerContext;

	@Autowired
	public ConstructionDetailsServiceImpl(IConstructionDetailsDAO iConstructionDetailsDAO,
			CallerContext callerContext) {
		this.iConstructionDetailsDAO = iConstructionDetailsDAO;
		this.callerContext = callerContext;
	}

	@Override
	public Map<String, Object> getConstructionDetails(String projectId, String trains, String departments,
			String filterFlag, String intExtFlag, boolean publish) {

		Map<String, Object> constructionDetailsMap = new HashMap<String, Object>();

		Map<String, Object> selectedData = getConstructionParamsDetails(projectId, intExtFlag);

		if (publish) {
			List<ConstructionDetailsDTO> list = iConstructionDetailsDAO.getPublishDetailsData(projectId, filterFlag,
					intExtFlag, trains, departments);
			constructionDetailsMap.put("constructionDetails", list);

			return constructionDetailsMap;
		} else {
			List<ConstructionDetailsDTO> list = iConstructionDetailsDAO.getConstructionDetailsData(projectId, trains,
					departments, filterFlag, intExtFlag, selectedData);
			constructionDetailsMap.put("constructionDetails", list);
			return constructionDetailsMap;

		}

	}

	@Override
	public Map<String, List<ActivityDetailsDTO>> getActivityDetails(String projectId, String epsContractId,
			String jobNumber, String filterFlag, String weightFlag, String intExtFlag, String department, String train,
			String activityTypeDesc, boolean publish) {
		Map<String, List<ActivityDetailsDTO>> constructionDetailsMap = new HashMap<String, List<ActivityDetailsDTO>>();
		if (publish) {
			List<ActivityDetailsDTO> list = iConstructionDetailsDAO.getPublishActivityDetailsData(projectId,
					epsContractId, jobNumber, filterFlag, weightFlag, intExtFlag, department, train, activityTypeDesc);
			constructionDetailsMap.put("activityDetails", list);
			return constructionDetailsMap;

		} else {
			List<ActivityDetailsDTO> list = iConstructionDetailsDAO.getActivityDetailsData(projectId, epsContractId,
					jobNumber, filterFlag, weightFlag, intExtFlag, department, train, activityTypeDesc);
			constructionDetailsMap.put("activityDetails", list);
			return constructionDetailsMap;
		}

	}

	@Override
	public Map<String, Object> getConstructionFilterDetails(String projectId, String intExtFlag, boolean publish) {

		if (publish) {
			return iConstructionDetailsDAO.getPublishFiltersData(projectId, intExtFlag);
		} else {
			Map<String, Object> selectedData = getConstructionParamsDetails(projectId, intExtFlag);
			return iConstructionDetailsDAO.getConstructionFiltersData(projectId, intExtFlag, selectedData);

		}

	}

	private Map<String, Object> getConstructionParamsDetails(String projectId, String intExtFlag) {

		List<String> json = iConstructionDetailsDAO.getConstructionParamsData(projectId);
		try {
			Map<String, Object> constructionParam = new HashMap<String, Object>();
			JSONObject featuresJsonObject = new JSONObject(json.get(0));
			JSONArray featuresArray = featuresJsonObject.getJSONArray("features");
			for (int i = 0; i < featuresArray.length(); i++) {
				if (i == 0) {
					JSONObject viewsJsonObject = featuresArray.getJSONObject(i);
					JSONArray viewsArray = viewsJsonObject.getJSONArray("views");
					for (int j = 0; j < viewsArray.length(); j++) {
						JSONObject viewNameObject = viewsArray.getJSONObject(j);
						String viewName = viewNameObject.getString("viewName");
						if (intExtFlag.equals(viewName)) {
							JSONObject dataListObject = viewsArray.getJSONObject(j);
							JSONArray dataListArray = dataListObject.getJSONArray("dataList");
							for (int k = 0; k < dataListArray.length(); k++) {
								JSONObject contractIsSelectedObject = dataListArray.getJSONObject(k);
								boolean isSelected = contractIsSelectedObject.getBoolean("isSelected");
								if (isSelected) {
									String contractId = contractIsSelectedObject.getString("contract");
									JSONArray projectArray = contractIsSelectedObject.getJSONArray("projects");
									List<String> jobNumberList = new ArrayList<String>();
									for (int l = 0; l < projectArray.length(); l++) {
										JSONObject projectObject = projectArray.getJSONObject(l);
										boolean isSelectedJobNumber = projectObject.getBoolean("isSelected");
										if (isSelectedJobNumber) {
											String jobNumber = projectObject.getString("project");
											jobNumberList.add(jobNumber);
										}

									}
									constructionParam.put("contractId", contractId);
									constructionParam.put("jobNumberList", jobNumberList);
								}
							}
						}

					}
				} else if (i == 2) {
					setConfiguratorFlag(featuresArray.getJSONObject(i), constructionParam);
					JSONObject viewsJsonObject = featuresArray.getJSONObject(i);
					JSONArray viewsArray = viewsJsonObject.getJSONArray("views");
					for (int j = 0; j < viewsArray.length(); j++) {
						JSONObject viewNameObject = viewsArray.getJSONObject(j);
						String viewName = viewNameObject.getString("viewName");
						if (intExtFlag.equals(viewName)) {
							JSONObject dataListObject = viewsArray.getJSONObject(j);
							JSONArray dataListArray = dataListObject.getJSONArray("dataList");
							List<String> filterFlag = new ArrayList<String>();
							int count = 0;
							for (int k = 0; k < dataListArray.length(); k++) {
								JSONObject filterAndWeightObject = dataListArray.getJSONObject(k);
								Iterator<String> keys = filterAndWeightObject.keys();
								while (keys.hasNext()) {
									String key = keys.next().toString();
									if (key.equals("selectedOption")) {
										String category = filterAndWeightObject.getString("category");
										if (category.equals("FILTER_BY")) {
											count++;
											String fFlag = filterAndWeightObject.getString("val");

											if (fFlag.equals("Main Items")) {
												fFlag = "M";
											} else if (fFlag.equals("Main Items+Internal Item")) {
												fFlag = "MI";
											} else if (fFlag.equals("All")) {
												fFlag = "A";
											}
											filterFlag.add(fFlag);
											constructionParam.put("filterFlag", filterFlag);
										} else if (category.equals("CUSTOM_WEIGHT")) {
											String weightFlag = filterAndWeightObject.getString("val");
											constructionParam.put("weightFlag", weightFlag);
										}
									}
								}

							}
							if (count == 2) {
								filterFlag.removeAll(filterFlag);
								filterFlag.add("MIA_MI");
								filterFlag.add("MIA_A");
								constructionParam.put("filterFlag", filterFlag);
							}
						}
					}
				}
			}
			return constructionParam;
		} catch (JSONException e) {

			log.error("something went wrong while parsing construction json:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		}

	}

	private void setConfiguratorFlag(JSONObject viewsJsonObject, Map<String, Object> constructionParam) {
		try {
			JSONArray viewsArray = viewsJsonObject.getJSONArray("views");
			for (int i = 0; i < viewsArray.length(); i++) {
				JSONObject viewNameObject = viewsArray.getJSONObject(i);
				String viewName = viewNameObject.getString("viewName");
				if (viewName.equals("Internal")) {
					JSONObject dataListObject = viewsArray.getJSONObject(i);
					JSONArray dataListArray = dataListObject.getJSONArray("dataList");
					for (int j = 0; j < dataListArray.length(); j++) {
						JSONObject wbsConfiguratorObject = dataListArray.getJSONObject(j);
						Iterator<String> keys = wbsConfiguratorObject.keys();
						while (keys.hasNext()) {
							String key = keys.next().toString();
							if (key.equals("selectedOption")) {
								String category = wbsConfiguratorObject.getString("category");
								if (category.equals("WBS_CUSTOM_WEIGHT")) {
									String wbsCustomWt = wbsConfiguratorObject.getString("val");
									if (wbsCustomWt.equals("yes")) {
										wbsCustomWt = "Y";
									} else {
										wbsCustomWt = "N";
									}
									constructionParam.put("wbsCustomWt", wbsCustomWt);
								}
								if (category.equals("WBS_OVERALL_DEPT")) {
									String wbsOverallDeptWt = wbsConfiguratorObject.getString("val");
									if (wbsOverallDeptWt.equals("yes")) {
										wbsOverallDeptWt = "Y";
									} else {
										wbsOverallDeptWt = "N";
									}
									constructionParam.put("wbsOverallDeptWt", wbsOverallDeptWt);
								}
							}

						}
					}
				}
			}

		} catch (JSONException e) {
			log.error("something went wrong while parsing construction json for configurator flag:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		}

	}

	@Override
	public Map<String, String> saveLatestPublishDetails(String projectId, String intExtFlag) {
		Map<String, Object> selectedData = getConstructionParamsDetails(projectId, intExtFlag);
		String ssoId = callerContext.getName();
		return iConstructionDetailsDAO.saveLatestPublish(ssoId, projectId, intExtFlag, selectedData);

	}
}
