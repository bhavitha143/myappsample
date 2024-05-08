package com.bh.realtrack.service.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IExecutionDao;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ExecutionDTO;
import com.bh.realtrack.dto.ExecutionDates;
import com.bh.realtrack.dto.ExecutionDropDown;
import com.bh.realtrack.dto.ExecutionFinishDTO;
import com.bh.realtrack.dto.ExecutionFunctionFilter;
import com.bh.realtrack.dto.ExecutionNCDTO;
import com.bh.realtrack.dto.ExecutionStartDTO;
import com.bh.realtrack.dto.KpiDTO;
import com.bh.realtrack.dto.OTDTrendChartDetailsDTO;
import com.bh.realtrack.dto.ProgressDates;
import com.bh.realtrack.dto.ScurveProgressDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.excel.ExportExecutionToExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IExecutionService;

@Service
public class ExecutionServiceImpl implements IExecutionService {

	private IExecutionDao iExecutionDAO;
	private static Logger log = LoggerFactory.getLogger(ExecutionServiceImpl.class.getName());

	private CallerContext callerContext;

	@Autowired
	public ExecutionServiceImpl(IExecutionDao iExecutionDAO, CallerContext callerContext) {
		this.iExecutionDAO = iExecutionDAO;
		this.callerContext = callerContext;
	}

	@Override
	public Map<String, Object> getExecutionChart(String projectId, String department, String subProject,
			String floatType, String weight, String chartType, String milestone, String otd,String ippActivityType,String functionGroup) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ExecutionStartDTO> executionStartList = new ArrayList<ExecutionStartDTO>();
		List<ExecutionFinishDTO> executionFinshList = new ArrayList<ExecutionFinishDTO>();
		if (chartType.equalsIgnoreCase("Start")) {
			executionStartList = iExecutionDAO.getExecutionStartChart(projectId, department, subProject, floatType,
					weight, milestone, otd,ippActivityType,functionGroup);
			responseMap.put("executionSummary", executionStartList);
		} else{
			executionFinshList = iExecutionDAO.getExecutionFinishChart(projectId, department, subProject, floatType,
					weight, milestone, otd,ippActivityType,functionGroup);
			responseMap.put("executionSummary", executionFinshList);
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<InputStreamResource> exportExecutionExcel(String projectId, String department,
			String subProject, String floatType, String weight, String milestone, String otd,String ippActivityType,String functionGroup) {
		
		List<ExecutionDTO> executionDTOList = new ArrayList<ExecutionDTO>();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		ExportExecutionToExcel excelObj = new ExportExecutionToExcel();
		executionDTOList = iExecutionDAO.getExecutionChart(projectId, department, subProject, floatType, weight,
				milestone, otd,ippActivityType,functionGroup);
		
		ResponseEntity<InputStreamResource> responseEntity = null;

		String fileName = "Execution_Details.xlsx";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(fileName);
			
			workbook = excelObj.exportExecutionExcel(workbook, executionDTOList);
			workbook.write(fileOut);
			
			fileOut.flush();
			File file = new File(fileName);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/text")).body(resource);
		} catch (IOException e) {
			log.error("something went wrong while downloading execution excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} catch (Exception e) {
			log.error("something went wrong while downloading execution excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {
				log.error("something went wrong while downloading execution excel:" + e.getMessage());
			} catch (Exception e) {
				log.error("something went wrong while downloading execution excel:" + e.getMessage());
			}
		}
		return responseEntity;
	}

	@Override
	public Map<String, Object> getExecutionFilters(String projectId, String department) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ExecutionDropDown> subProjectList = new ArrayList<ExecutionDropDown>();
		List<String> floatTypeList = new ArrayList<String>();
		List<String> weightList = new ArrayList<String>();
		List<ExecutionFunctionFilter> functionList = new ArrayList<ExecutionFunctionFilter>();
		List<ExecutionFunctionFilter> funcList = new ArrayList<ExecutionFunctionFilter>();
		List<String> functionGroupList = new ArrayList<String>();
		List<String> ippActivityTypeList = new ArrayList<String>();
		List<String> otd = new ArrayList<String>();

		subProjectList = iExecutionDAO.getSubProjectFilter(projectId, department);
		floatTypeList = iExecutionDAO.getFloatTypeFilter(projectId);
		weightList = iExecutionDAO.getWeightFilter(projectId);
		functionList = iExecutionDAO.getFunctionFilter(projectId);
		functionList.forEach(dto -> {
			if ("Blank".equalsIgnoreCase(dto.getDepartment())) {
				dto.setActivityDep("BLANK");
			} else {
				funcList.add(dto);
			}
		});

		otd = iExecutionDAO.getOtd();
		List<DropDownDTO> milestoneList = new ArrayList<DropDownDTO>();

		milestoneList = iExecutionDAO.getMilestoneList(projectId);
				responseMap.put("Milestone", milestoneList);
		ippActivityTypeList = iExecutionDAO.getIppActivityType(projectId);
		
		functionGroupList= iExecutionDAO.getFunctionGroup(projectId);
		
		responseMap.put("Milestone", milestoneList);
		responseMap.put("Otd", otd);
		responseMap.put("SubProject", subProjectList);
		responseMap.put("FloatType", floatTypeList);
		responseMap.put("Weight", weightList);
		responseMap.put("Function", functionList);
		responseMap.put("functionGroupList", functionGroupList);
		responseMap.put("IppActivityType", ippActivityTypeList);
		return responseMap;
	}

	/*
	 * private List<DropDownDTO> getMileStoneDropDownData(String department) {
	 * List<DropDownDTO> dropDownDTOList = new ArrayList<DropDownDTO>();
	 * 
	 * DropDownDTO dropDownDTO = new DropDownDTO(); dropDownDTO.setKey("CONMS");
	 * dropDownDTO.setVal("Contract Milestones"); dropDownDTOList.add(dropDownDTO);
	 * 
	 * dropDownDTO = new DropDownDTO(); dropDownDTO.setKey("CP");
	 * dropDownDTO.setVal("Control Points"); dropDownDTOList.add(dropDownDTO);
	 * 
	 * dropDownDTO = new DropDownDTO(); dropDownDTO.setKey("CP_550");
	 * dropDownDTO.setVal("CP_550s"); dropDownDTOList.add(dropDownDTO);
	 * 
	 * return dropDownDTOList; }
	 */

	@Override
	public Map<String, Object> getExecutionProgress(String projectId, String department, String subProject) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ScurveProgressDTO> progressDTOList = new ArrayList<ScurveProgressDTO>();
		List<ProgressDates> progressDatesDTOList = new ArrayList<ProgressDates>();
		progressDatesDTOList = iExecutionDAO.getProgressDates(projectId);
		if (subProject.equalsIgnoreCase("0")) {
			subProject = "overall";
		}
		if (department.equalsIgnoreCase("0")) {
			department = "overall";
		}
		progressDTOList = iExecutionDAO.getExecutionProgress(projectId, department, subProject,
				progressDatesDTOList.get(0).getDataDt(), progressDatesDTOList.get(0).getProgressDt());
		responseMap.put("progressList", progressDTOList);
		return responseMap;
	}

	@Override
	public Map<String, Object> getExecutionDates(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String p6DataDate = "", refreshDate = "";
		List<ExecutionDates> executionDatesList = new ArrayList<ExecutionDates>();
		executionDatesList = iExecutionDAO.getExecutionDates(projectId);
		if (executionDatesList.isEmpty() == false) {
			p6DataDate = executionDatesList.get(0).getDataDate();
			refreshDate = executionDatesList.get(0).getLastRefreshDate();
		}
		responseMap.put("p6DataDate", p6DataDate);
		responseMap.put("refreshDate", refreshDate);
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<InputStreamResource> exportExecutionNCExcel(String projectId, String companyId,
			String ncrType, String criticality, String subProject, String organizationName) {
		
		List<ExecutionNCDTO> executionNCDTOList = new ArrayList<ExecutionNCDTO>();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		ExportExecutionToExcel excelObj = new ExportExecutionToExcel();
		String sso = callerContext.getName();
		String role = iExecutionDAO.getRole(sso);
		if(organizationName==null){
			executionNCDTOList = iExecutionDAO.getExecutionForNCForTPS(projectId, role, companyId, ncrType, criticality,
					subProject);
		}
		else {
			executionNCDTOList = iExecutionDAO.getExecutionForNC(projectId, role, companyId, ncrType, criticality,
					subProject, organizationName);
		}
		ResponseEntity<InputStreamResource> responseEntity = null;
		String fileName = "Execution_NC_Details_" + projectId + ".xlsx";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(fileName);
			workbook = excelObj.exportExecutionNCExcel(workbook, executionNCDTOList,organizationName);
			workbook.write(fileOut);
			
			fileOut.flush();
			File file = new File(fileName);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/text")).body(resource);
		} catch (IOException e) {
			log.error("something went wrong while downloading exportExecutionNCExcel excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {

				log.error("something went wrong while downloading execution excel:" + e.getMessage());
			}
		}
		return responseEntity;
	}

	@Override
	public Map<String, Object> getKpi(String projectId, String department, String subProject, String floatType,
			String weightType, String milestone, String otd,String ippActivityType,String functionGroup ) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<KpiDTO> kpi = new ArrayList<KpiDTO>();
		
		
		kpi = iExecutionDAO.getKpi(projectId, department, subProject, floatType, weightType, milestone, otd,ippActivityType,functionGroup);

		responseMap.put("kpi", kpi);

		return responseMap;
	}

	@Override
	public Map<String, List<OTDTrendChartDetailsDTO>> getOTDTrendChart(String projectId, String department,
			String subProject, String floatType, String weight, String chartType, String milestone, String otd,String ippActivityType,String functionGroup) {

		Map<String, List<OTDTrendChartDetailsDTO>> responseMap = new HashMap<String, List<OTDTrendChartDetailsDTO>>();
		List<OTDTrendChartDetailsDTO> otdTrendChartDetailsDTOList = iExecutionDAO.getOTDTrendChartData(projectId,
				department, subProject, floatType, weight, milestone, otd,ippActivityType,functionGroup);

		responseMap.put("otdTrendList", otdTrendChartDetailsDTOList);
		return responseMap;
	}

	@Override
	public byte[] downloadExecutionStartChartPopupExcel(String projectId, String department, String subProject, String floatType, String weightType, String milestone, String otd, String ippActivityType, String functionGroup, String otdStart, String timingStart) {
		String sheetName = otdStart+" - "+timingStart;
		ExportExecutionToExcel exportExecutionToExcel = new ExportExecutionToExcel();
		List<ExecutionStartDTO> executionStartDTOList = new ArrayList<>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			executionStartDTOList = iExecutionDAO.downloadExecutionStartChartPopupExcel(projectId, department, subProject,
					floatType, weightType, milestone, otd, ippActivityType, functionGroup, otdStart, timingStart);
			workbook = exportExecutionToExcel.exportExecutionStartPopupExcel(workbook,executionStartDTOList, sheetName);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading execution start popup project details :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading execution start popup project details :: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public byte[] downloadExecutionFinishChartPopupExcel(String projectId, String department, String subProject, String floatType, String weightType, String milestone, String otd, String ippActivityType, String functionGroup, String otdFinish, String timingFinish) {
		String sheetName = otdFinish+" - "+timingFinish;
		ExportExecutionToExcel exportExecutionToExcel = new ExportExecutionToExcel();
		List<ExecutionFinishDTO> executionFinishDTOList = new ArrayList<>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			executionFinishDTOList = iExecutionDAO.downloadExecutionFinishChartPopupExcel(projectId, department, subProject,
					floatType, weightType, milestone, otd, ippActivityType, functionGroup, otdFinish, timingFinish);
			workbook = exportExecutionToExcel.exportExecutionFinishPopupExcel(workbook,executionFinishDTOList,sheetName);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading execution finish popup project details :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading execution finish popup project details :: " + e.getMessage());
			}
		}
		return excelData;
	}



}
