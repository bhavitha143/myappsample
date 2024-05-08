package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IManufacturingDAO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ManufacturingAgingDetailsDTO;
import com.bh.realtrack.dto.ManufacturingCreationClosureDTO;
import com.bh.realtrack.dto.ManufacturingPopupDetailsDTO;
import com.bh.realtrack.dto.ManufacturingSummaryPopUpDetailsDTO;
import com.bh.realtrack.excel.ExportNonConformancesPopupExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IManufacturingService;

/**
 * @author Shweta D. Sawant
 */
@Service
public class ManufacturingServiceImpl implements IManufacturingService {

	@Autowired
	private IManufacturingDAO iManufacturingDAO;

	@Autowired
	private CallerContext callerContext;

	private static final Logger log = LoggerFactory.getLogger(ManufacturingServiceImpl.class.getName());

	@Override
	public Map<String, Object> getManufacturingAgingStatus(String projectIds, String companyId, String subProject,
			String ncrtype, String criticality, String organizationName) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
		ManufacturingAgingDetailsDTO responseDTO = new ManufacturingAgingDetailsDTO();
		String updateOnDate = "";
		try {
			String sso = callerContext.getName();
			String role = iManufacturingDAO.getRole(sso);
			if (organizationName == null) {
				log.info("Inside TPS");
				list = iManufacturingDAO.getManufacturingAgingStatusForTPS(projectIds, companyId, subProject, ncrtype,
						criticality, role);
			} else {
				log.info("Inside OFE");
				list = iManufacturingDAO.getManufacturingAgingStatus(projectIds, companyId, subProject, ncrtype,
						criticality, organizationName, role);
			}
			updateOnDate = iManufacturingDAO.getLastUpdatedDate(projectIds);
			responseDTO.setLastUpdateDate(updateOnDate);
			responseDTO.setManufacturingDetails(list);
			responseMap.put("data", responseDTO);
		} catch (Exception e) {
			log.error("getManufacturingAgingStatus(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getManufacturingCreationCurrent(String projectIds, String companyId, String subProject,
			String ncrtype, String criticality, String organizationName, String fromdate, String todate) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
		ManufacturingAgingDetailsDTO responseDTO = new ManufacturingAgingDetailsDTO();
		String updateOnDate = "";
		try {
			String sso = callerContext.getName();
			String role = iManufacturingDAO.getRole(sso);
			// For TPS
			if (organizationName == null) {
				list = iManufacturingDAO.getManufacturingCreationCurrentDetailsForTPS(projectIds, companyId, subProject,
						ncrtype, criticality, fromdate, todate, role);
			}
			// For OFE
			else {
				list = iManufacturingDAO.getManufacturingCreationCurrentDetails(projectIds, companyId, subProject,
						ncrtype, criticality, organizationName, fromdate, todate, role);
			}
			updateOnDate = iManufacturingDAO.getLastUpdatedDate(projectIds);
			responseDTO.setLastUpdateDate(updateOnDate);
			responseDTO.setManufacturingDetails(list);
			responseMap.put("data", responseDTO);
		} catch (Exception e) {
			log.error("getManufacturingCreationCurrent(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getManufacturingCreationClosure(String projectIds, String companyId, String subProject,
			String ncrtype, String criticality, String organizationName, String fromdate, String todate) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ManufacturingPopupDetailsDTO> currentList = new ArrayList<ManufacturingPopupDetailsDTO>();
		List<ManufacturingPopupDetailsDTO> closureList = new ArrayList<ManufacturingPopupDetailsDTO>();
		ManufacturingCreationClosureDTO responseDTO = new ManufacturingCreationClosureDTO();
		String updateOnDate = "";
		try {
			String sso = callerContext.getName();
			String role = iManufacturingDAO.getRole(sso);
			// For TPS
			if (organizationName == null) {
				currentList = iManufacturingDAO.getManufacturingCreationCurrentDetailsForTPS(projectIds, companyId,
						subProject, ncrtype, criticality, fromdate, todate, role);
				closureList = iManufacturingDAO.getManufacturingCreationClosureDetailsForTPS(projectIds, companyId,
						subProject, ncrtype, criticality, fromdate, todate, role);
			}
			// For OFE
			else {
				currentList = iManufacturingDAO.getManufacturingCreationCurrentDetails(projectIds, companyId,
						subProject, ncrtype, criticality, organizationName, fromdate, todate, role);
				closureList = iManufacturingDAO.getManufacturingCreationClosureDetails(projectIds, companyId,
						subProject, ncrtype, criticality, organizationName, fromdate, todate, role);
			}
			updateOnDate = iManufacturingDAO.getLastUpdatedDate(projectIds);
			responseDTO.setLastUpdateDate(updateOnDate);
			responseDTO.setManufacturingCreation(currentList);
			responseDTO.setManufacturingClosure(closureList);
			responseMap.put("data", responseDTO);
		} catch (Exception e) {
			log.error("getManufacturingCreationClosure(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getManufacturingFilters(String projectId, int companyId) {
		Map<String, Object> response = new HashMap<>();
		String sso = callerContext.getName();
		String role = iManufacturingDAO.getRole(sso);
		List<String> subProject = iManufacturingDAO.getSubProject(projectId, companyId, role);
		List<String> ncrType = iManufacturingDAO.getNcrType(projectId, companyId, role);
		List<DropDownDTO> criticality = iManufacturingDAO.getCriticality(projectId, companyId, role);
		List<String> organizationName = iManufacturingDAO.getOrganizationName(projectId, companyId, role);

		response.put("subProject", subProject);
		response.put("ncrType", ncrType);
		response.put("criticality", criticality);
		response.put("organizationName", organizationName);
		return response;

	}

	@Override
	public byte[] downloadExcelForNCAgingStatusPopup(String projectId, String companyId, String subProject,
			String ncrType, String criticality, String organizationName, int barStartRange, int barEndRange,
			String barStatus) {
		String sheetName = barStatus;
		String sso = callerContext.getName();
		String role = iManufacturingDAO.getRole(sso);
		ExportNonConformancesPopupExcel exportNonConformancesPopupExcel = new ExportNonConformancesPopupExcel();
		List<ManufacturingPopupDetailsDTO> manufacturingPopupDetailsDTOList = new ArrayList<ManufacturingPopupDetailsDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			manufacturingPopupDetailsDTOList = iManufacturingDAO.downloadExcelForNCAgingStatusPopup(projectId,
					companyId, subProject, ncrType, criticality, organizationName, barStartRange, barEndRange,
					barStatus, role);
			workbook = exportNonConformancesPopupExcel.getNonConformancesPopupExcel(workbook,
					manufacturingPopupDetailsDTOList, sheetName);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error(
					"Error occured when downloading Nc Aging Status status popup project details :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading Nc Aging Status status popup project details :: "
						+ e.getMessage());
			}
		}
		return excelData;

	}

	@Override
	public byte[] downloadExcelForNCCreationByCurrentStatusPopup(String projectId, String companyId, String subProject,
			String ncrType, String criticality, String fromDate, String toDate, String organizationName,
			String barStartDate, String barEndDate, String barStatus) {
		String sheetName = barStatus;
		String sso = callerContext.getName();
		String role = iManufacturingDAO.getRole(sso);
		ExportNonConformancesPopupExcel exportNonConformancesPopupExcel = new ExportNonConformancesPopupExcel();
		List<ManufacturingPopupDetailsDTO> manufacturingPopupDetailsDTOList = new ArrayList<ManufacturingPopupDetailsDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			manufacturingPopupDetailsDTOList = iManufacturingDAO.downloadExcelForNCCreationByCurrentStatusPopup(
					projectId, companyId, subProject, ncrType, criticality, fromDate, toDate, organizationName,
					barStartDate, barEndDate, barStatus, role);
			workbook = exportNonConformancesPopupExcel.getNonConformancesPopupExcel(workbook,
					manufacturingPopupDetailsDTOList, sheetName);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading creation by current status popup project details :: "
					+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading creation by current status popup project details :: "
						+ e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public byte[] downloadExcelForNCCreationClosureStatusPopup(String projectId, String companyId, String subProject,
			String ncrType, String criticality, String fromDate, String toDate, String organizationName,
			String barStartDate, String barEndDate, String chartType) {
		String sheetName = chartType + " NCRs";
		String sso = callerContext.getName();
		String role = iManufacturingDAO.getRole(sso);
		ExportNonConformancesPopupExcel exportNonConformancesPopupExcel = new ExportNonConformancesPopupExcel();
		List<ManufacturingPopupDetailsDTO> manufacturingPopupDetailsDTOList = new ArrayList<ManufacturingPopupDetailsDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			if (chartType.equalsIgnoreCase("Creation")) {
				manufacturingPopupDetailsDTOList = iManufacturingDAO.downloadExcelForNCRsCreatedPopup(projectId,
						companyId, subProject, ncrType, criticality, fromDate, toDate, organizationName, barStartDate,
						barEndDate, role);
			} else {
				manufacturingPopupDetailsDTOList = iManufacturingDAO.downloadExcelForNCRsClosedPopup(projectId,
						companyId, subProject, ncrType, criticality, fromDate, toDate, organizationName, barStartDate,
						barEndDate, role);
			}
			workbook = exportNonConformancesPopupExcel.getNonConformancesPopupExcel(workbook,
					manufacturingPopupDetailsDTOList, sheetName);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading creation and closure status popup project details :: "
					+ e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading creation and closure status popup project details :: "
						+ e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getManufacturingSummaryPopUpDetails(String projectId, String companyId,
			String subProject, String ncrType, String criticality, String status) {
		Map<String, Object> repsonseMap = new HashMap<String, Object>();
		List<ManufacturingSummaryPopUpDetailsDTO> manufacturingPopUpList = new ArrayList<ManufacturingSummaryPopUpDetailsDTO>();
		try {
			manufacturingPopUpList = iManufacturingDAO.getManufacturingSummaryPopUpDetails(projectId, companyId,
					subProject, ncrType, criticality, status);
			repsonseMap.put("popup", manufacturingPopUpList);
		} catch (Exception e) {
			log.error("getManufacturingSummaryPopUpDetails(): Exception occurred : " + e.getMessage());
		}
		return repsonseMap;
	}

}
