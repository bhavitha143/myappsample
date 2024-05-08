package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ManufacturingPopupDetailsDTO;
import com.bh.realtrack.dto.ManufacturingSummaryPopUpDetailsDTO;

/**
 * @author Shweta D. Sawant
 */
public interface IManufacturingDAO {

	public String getRole(String sso);

	public List<ManufacturingPopupDetailsDTO> getManufacturingAgingStatus(String projectIds, String companyId,
			String subProject, String ncrtype, String criticality, String organizationName, String role);

	public List<ManufacturingPopupDetailsDTO> getManufacturingAgingStatusForTPS(String projectIds, String companyId,
			String subProject, String ncrtype, String criticality, String role);

	public String getLastUpdatedDate(String projectIds);

	public List<ManufacturingPopupDetailsDTO> getManufacturingCreationCurrentDetails(String projectIds,
			String companyId, String subProject, String ncrtype, String criticality, String organizationName,
			String fromdate, String todate, String role);

	public List<ManufacturingPopupDetailsDTO> getManufacturingCreationCurrentDetailsForTPS(String projectIds,
			String companyId, String subProject, String ncrtype, String criticality, String fromdate, String todate,
			String role);

	public List<ManufacturingPopupDetailsDTO> getManufacturingCreationClosureDetails(String projectIds,
			String companyId, String subProject, String ncrtype, String criticality, String organizationName,
			String fromdate, String todate, String role);

	public List<ManufacturingPopupDetailsDTO> getManufacturingCreationClosureDetailsForTPS(String projectIds,
			String companyId, String subProject, String ncrtype, String criticality, String fromdate, String todate,
			String role);

	List<String> getSubProject(String projectId, int companyId, String role);

	List<String> getNcrType(String projectId, int companyId, String role);

	List<DropDownDTO> getCriticality(String projectId, int companyId, String role);

	List<String> getOrganizationName(String projectId, int companyId, String role);

	List<ManufacturingPopupDetailsDTO> downloadExcelForNCAgingStatusPopup(String projectId, String companyId,
			String subProject, String ncrType, String criticality, String organizationName, int barStartRange,
			int barEndRange, String barStatus, String role);

	List<ManufacturingPopupDetailsDTO> downloadExcelForNCCreationByCurrentStatusPopup(String projectId,
			String companyId, String subProject, String ncrType, String criticality, String fromDate, String toDate,
			String organizationName, String barStartDate, String barEndDate, String barStatus, String role);

	List<ManufacturingPopupDetailsDTO> downloadExcelForNCRsClosedPopup(String projectId, String companyId,
			String subProject, String ncrType, String criticality, String fromDate, String toDate,
			String organizationName, String barStartDate, String barEndDate, String role);

	List<ManufacturingPopupDetailsDTO> downloadExcelForNCRsCreatedPopup(String projectId, String companyId,
			String subProject, String ncrType, String criticality, String fromDate, String toDate,
			String organizationName, String barStartDate, String barEndDate, String role);

	List<ManufacturingSummaryPopUpDetailsDTO> getManufacturingSummaryPopUpDetails(String projectId, String companyId,
			String subProject, String ncrType, String criticality, String status);
}
