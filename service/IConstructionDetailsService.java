package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ActivityDetailsDTO;
import com.bh.realtrack.dto.ConstructionDetailsDTO;

public interface IConstructionDetailsService {

	/*Map<String, List<ConstructionDetailsDTO>> getConstructionDetails(String projectId, String trains,
			String departments, String filterFlag, String intExtFlag, boolean publish);*/
	Map<String, Object> getConstructionDetails(String projectId, String trains,
			String departments, String filterFlag, String intExtFlag, boolean publish);

	Map<String, List<ActivityDetailsDTO>> getActivityDetails(String projectId, String epsContractId, String jobNumber,
			String filterFlag, String weightFlag, String intExtFlag, String department, String train,
			String activityTypeDesc, boolean publish);

	Map<String, Object> getConstructionFilterDetails(String projectId, String intExtFlag, boolean publish);

	Map<String, String> saveLatestPublishDetails(String projectId, String intExtFlag);

}
