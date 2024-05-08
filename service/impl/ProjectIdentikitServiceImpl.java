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

import com.bh.realtrack.dao.IProjectIdentikitDAO;
import com.bh.realtrack.dao.impl.ProjectIdentikitDAOImpl;
import com.bh.realtrack.dto.IdentikitBhMfgShopLocationDetailsDTO;
import com.bh.realtrack.dto.IdentikitContractLELocationDetailsDTO;
import com.bh.realtrack.dto.IdentikitMainSupplierLocationDetailsDTO;
import com.bh.realtrack.dto.IdentikitPMAddNotesHistoryDTO;
import com.bh.realtrack.dto.IdentikitProjectCoreTeamDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectGeographyDBDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectGeographyDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectLocationDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectScopeDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectScopeTableDetailsDTO;
import com.bh.realtrack.dto.ProjectDataformSummaryDTO;
import com.bh.realtrack.excel.ExportIdentikitDetailsExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IProjectIdentikitService;

@Service
public class ProjectIdentikitServiceImpl implements IProjectIdentikitService {

	private static final Logger log = LoggerFactory.getLogger(ProjectIdentikitServiceImpl.class);

	@Autowired
	IProjectIdentikitDAO iProjectIdentikitDAO;
	CallerContext callerContext;

	@Autowired
	public ProjectIdentikitServiceImpl(ProjectIdentikitDAOImpl projectIdentikitDAOImpl, CallerContext callerContext) {
		this.iProjectIdentikitDAO = projectIdentikitDAOImpl;
		this.callerContext = callerContext;
	}

	@Override
	public Map<String, Object> getIdentikitProjectDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		IdentikitProjectDetailsDTO data = new IdentikitProjectDetailsDTO();
		String sso = callerContext.getName();
		try {
			data = iProjectIdentikitDAO.getIdentikitProjectDetails(projectId, sso);
			responseMap.put("data", data);
		} catch (Exception e) {
			log.error("getIdentikitProjectDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getIdentikitProjectCoreTeamDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> teamDetailsMap = new HashMap<String, Object>();
		IdentikitProjectCoreTeamDetailsDTO data = new IdentikitProjectCoreTeamDetailsDTO();
		try {
			data = iProjectIdentikitDAO.getIdentikitProjectCoreTeamDetails(projectId);
			teamDetailsMap = iProjectIdentikitDAO.getIdentikitProjectTeamDetails(projectId);
			responseMap.put("data", data);
			responseMap.put("teamDetails", teamDetailsMap);
		} catch (Exception e) {
			log.error("getIdentikitProjectCoreTeamDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getIdentikitProjectGeographyDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, IdentikitProjectLocationDetailsDTO> projectLocationMap = new HashMap<String, IdentikitProjectLocationDetailsDTO>();
		Map<String, IdentikitBhMfgShopLocationDetailsDTO> bhMFGShopLocationMap = new HashMap<String, IdentikitBhMfgShopLocationDetailsDTO>();
		Map<String, IdentikitMainSupplierLocationDetailsDTO> mainSupplierLocationMap = new HashMap<String, IdentikitMainSupplierLocationDetailsDTO>();
		Map<String, IdentikitContractLELocationDetailsDTO> contractLELocationMap = new HashMap<String, IdentikitContractLELocationDetailsDTO>();
		List<IdentikitProjectGeographyDBDetailsDTO> list = new ArrayList<IdentikitProjectGeographyDBDetailsDTO>();
		List<IdentikitProjectLocationDetailsDTO> projectLocationList = new ArrayList<IdentikitProjectLocationDetailsDTO>();
		List<IdentikitBhMfgShopLocationDetailsDTO> bhMFGShopList = new ArrayList<IdentikitBhMfgShopLocationDetailsDTO>();
		List<IdentikitMainSupplierLocationDetailsDTO> mainSupplierList = new ArrayList<IdentikitMainSupplierLocationDetailsDTO>();
		List<IdentikitContractLELocationDetailsDTO> contractLEList = new ArrayList<IdentikitContractLELocationDetailsDTO>();
		IdentikitProjectGeographyDetailsDTO data = new IdentikitProjectGeographyDetailsDTO();
		IdentikitProjectLocationDetailsDTO projectLocationDTO = new IdentikitProjectLocationDetailsDTO();
		IdentikitBhMfgShopLocationDetailsDTO bhMFGShopLocationDTO = new IdentikitBhMfgShopLocationDetailsDTO();
		IdentikitMainSupplierLocationDetailsDTO mainSupplierLocationDTO = new IdentikitMainSupplierLocationDetailsDTO();
		IdentikitContractLELocationDetailsDTO contractLELocationDTO = new IdentikitContractLELocationDetailsDTO();
		try {
			list = iProjectIdentikitDAO.getIdentikitProjectGeographyDetails(projectId);
			for (IdentikitProjectGeographyDBDetailsDTO dto : list) {
				projectLocationDTO = new IdentikitProjectLocationDetailsDTO();
				bhMFGShopLocationDTO = new IdentikitBhMfgShopLocationDetailsDTO();
				mainSupplierLocationDTO = new IdentikitMainSupplierLocationDetailsDTO();
				contractLELocationDTO = new IdentikitContractLELocationDetailsDTO();

				data.setRegion(dto.getRegion());
				data.setCountry(dto.getCountry());
				data.setPmAddNotes(dto.getPmAddNotes());

				if (null != dto.getProjLocLattitude() && !dto.getProjLocLattitude().isEmpty()
						&& null != dto.getProjLocLongitude() && !dto.getProjLocLongitude().isEmpty()) {
					projectLocationDTO.setProjectId(dto.getProjectId());
					projectLocationDTO.setLatitude(dto.getProjLocLattitude());
					projectLocationDTO.setLongitude(dto.getProjLocLongitude());
					projectLocationDTO.setInstallationCountry(dto.getCountry());
					projectLocationMap.put(dto.getProjectId(), projectLocationDTO);
				}

				if (null != dto.getBhShopLattitude() && !dto.getBhShopLattitude().isEmpty()
						&& null != dto.getBhShopLongitude() && !dto.getBhShopLongitude().isEmpty()) {
					bhMFGShopLocationDTO.setProjectId(dto.getBhShopCountryName());
					bhMFGShopLocationDTO.setLatitude(dto.getBhShopLattitude());
					bhMFGShopLocationDTO.setLongitude(dto.getBhShopLongitude());
					bhMFGShopLocationMap.put(dto.getBhShopCountryName(), bhMFGShopLocationDTO);
				}

				if (null != dto.getMainSupplierLattitude() && !dto.getMainSupplierLattitude().isEmpty()
						&& null != dto.getMainSupplierLongitude() && !dto.getMainSupplierLongitude().isEmpty()) {
					mainSupplierLocationDTO.setProjectId(dto.getMainSupplierName());
					mainSupplierLocationDTO.setLatitude(dto.getMainSupplierLattitude());
					mainSupplierLocationDTO.setLongitude(dto.getMainSupplierLongitude());
					mainSupplierLocationDTO.setPo(dto.getMainSupplierPoVal());
					mainSupplierLocationMap.put(dto.getMainSupplierName(), mainSupplierLocationDTO);
				}

				if (null != dto.getContractLeLattitude() && !dto.getContractLeLattitude().isEmpty()
						&& null != dto.getContractLeLongitude() && !dto.getContractLeLongitude().isEmpty()) {
					contractLELocationDTO.setProjectId(dto.getContractLeName());
					contractLELocationDTO.setLatitude(dto.getContractLeLattitude());
					contractLELocationDTO.setLongitude(dto.getContractLeLongitude());
					contractLELocationMap.put(dto.getContractLeName(), contractLELocationDTO);
				}
			}
			projectLocationList.addAll(projectLocationMap.values());
			bhMFGShopList.addAll(bhMFGShopLocationMap.values());
			mainSupplierList.addAll(mainSupplierLocationMap.values());
			contractLEList.addAll(contractLELocationMap.values());
			responseMap.put("data", data);
			responseMap.put("projectLocationList", projectLocationList);
			responseMap.put("bhMFGShopList", bhMFGShopList);
			responseMap.put("directShoppingList", mainSupplierList);
			responseMap.put("contractLEList", contractLEList);
		} catch (Exception e) {
			log.error("getIdentikitProjectGeographyDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getIdentikitProjectScopeDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		IdentikitProjectScopeDetailsDTO data = new IdentikitProjectScopeDetailsDTO();
		List<IdentikitProjectScopeTableDetailsDTO> list = new ArrayList<IdentikitProjectScopeTableDetailsDTO>();
		try {
			data = iProjectIdentikitDAO.getIdentikitProjectScopeDetails(projectId);
			list = iProjectIdentikitDAO.getIdentikitProjectScopeTableDetails(projectId);
			responseMap.put("data", data);
			responseMap.put("list", list);
		} catch (Exception e) {
			log.error("getIdentikitProjectScopeDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveIdentikitProjectDetails(ProjectDataformSummaryDTO summaryDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		boolean updateFlag = false;
		try {
			String sso = callerContext.getName();
			updateFlag = iProjectIdentikitDAO.saveIdentikitProjectDetails(summaryDTO, sso);
			if (updateFlag) {
				responseMap.put("status", "Success");
				responseMap.put("message", "Data saved successfully.");
			} else {
				responseMap.put("status", "Error");
				responseMap.put("message", "Error while saving data.");
			}
		} catch (Exception e) {
			log.error("saveIdentikitProjectDetails() :: Exception occurred :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getIdentikitPMAddNotesHistory(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<IdentikitPMAddNotesHistoryDTO> data = new ArrayList<IdentikitPMAddNotesHistoryDTO>();
		try {
			data = iProjectIdentikitDAO.getIdentikitPMAddNotesHistory(projectId);
			responseMap.put("data", data);
		} catch (Exception e) {
			log.error("getIdentikitPMAddNotesHistory(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadIdentikitProjectScopeDetails(String projectId) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportIdentikitDetailsExcel excelDTO = new ExportIdentikitDetailsExcel();
		List<IdentikitProjectScopeTableDetailsDTO> list = new ArrayList<IdentikitProjectScopeTableDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			list = iProjectIdentikitDAO.getIdentikitProjectScopeTableDetails(projectId);
			log.info("Creating Identikit Project Scope Details Sheet with " + list.size() + " rows.");
			excelDTO.exportIdentikitProjectScopeDetailsExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Identikit Project Scope Details :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Identikit Project Scope Details :: " + e.getMessage());
			}
		}
		return excelData;
	}

}
