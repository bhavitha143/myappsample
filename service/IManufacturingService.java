package com.bh.realtrack.service;

import java.util.Map;

public interface IManufacturingService {

	Map<String, Object> getManufacturingAgingStatus(String projectIds, String companyId, String subProject,
			String ncrtype, String criticality, String organizationName);

	Map<String, Object> getManufacturingCreationCurrent(String projectIds, String companyId, String subProject,
			String ncrtype, String criticality, String organizationName, String fromdate, String todate);

	Map<String, Object> getManufacturingCreationClosure(String projectIds, String companyId, String subProject,
			String ncrtype, String criticality, String organizationName, String fromdate, String todate);

	Map<String, Object> getManufacturingFilters(String projectId, int companyId);

	byte[] downloadExcelForNCAgingStatusPopup(String projectId, String companyId, String subproject, String ncrType,
			String criticality, String organizationName, int barStartRange, int barEndRange, String barStatus);

	byte[] downloadExcelForNCCreationByCurrentStatusPopup(String projectId, String companyId, String subProject,
			String ncrType, String criticality, String fromDate, String toDate, String organizationName,
			String barStartDate, String barEndDate, String barStatus);

	byte[] downloadExcelForNCCreationClosureStatusPopup(String projectId, String companyId, String subProject,
			String ncrType, String criticality, String fromDate, String toDate, String organizationName,
			String barStartDate, String barEndDate, String chartType);

	Map<String, Object> getManufacturingSummaryPopUpDetails(String projectId, String companyId, String subProject,
			String ncrType, String criticality, String status);
}