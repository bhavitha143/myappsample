package com.bh.realtrack.service;

import java.util.Map;

public interface IProcurementService {

	Map<String, Object> getItemBuyControlFlowDetails(String jobNumber, String dummyCode, String activityFilter);

	byte[] downloadMaterialListDetails(String projectId, String viewConsideration, String train, String subProject,
			String componentCode, String activityFilter, String viewName);

}
