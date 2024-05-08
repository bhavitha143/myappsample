package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ActivityDetailsDTO;
import com.bh.realtrack.dto.ConstructionDetailsDTO;

/**
 * @author Anand Kumar
 *
 */
public interface IConstructionDetailsDAO {
	List<ConstructionDetailsDTO> getConstructionDetailsData(String projectId, String train, String department,
			String filterFlag, String intExtFlag, Map<String, Object> selectedData);

	List<ConstructionDetailsDTO> getPublishDetailsData(String projectId, String apiFilter, String intExtFlag,
			String trains, String departments);

	List<ActivityDetailsDTO> getActivityDetailsData(String projectId, String epsContractId, String jobNumber,
			String filterFlag, String weightFlag, String intExtFlag, String department, String train,
			String activityTypeDesc);

	List<ActivityDetailsDTO> getPublishActivityDetailsData(String projectId, String epsContractId, String jobNumber,
			String apiFilter, String weightFlag, String intExtFlag, String department, String train,
			String activityTypeDesc);

	Map<String, Object> getConstructionFiltersData(String projectId, String intExtFlag,
			Map<String, Object> selectedMap);

	Map<String, Object> getPublishFiltersData(String projectId, String intExtFlag);

	Map<String, String> saveLatestPublish(String ssoId, String projectId, String intExtFlag,
			Map<String, Object> selectedData);

	List<String> getConstructionParamsData(String projectId);

}
