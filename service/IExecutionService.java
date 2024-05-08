package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.bh.realtrack.dto.OTDTrendChartDetailsDTO;
import org.springframework.web.bind.annotation.RequestParam;

public interface IExecutionService {
	public Map<String, Object> getExecutionChart(String projectId,
			String department, String subProject, String floatType,
			String weight, String chartType, String milestone, String otd, String ippActivityType, String functionGroup);

	ResponseEntity<InputStreamResource> exportExecutionExcel(
			final String projectId, String department, String subProject,
			String floatType, String weight, String milestone, String otd, String ippActivityType, String functionGroup);

	public Map<String, Object> getExecutionFilters(String projectId,
			String department);

	public Map<String, Object> getExecutionProgress(String projectId,
			String department, String subProject);

	public Map<String, Object> getExecutionDates(String projectId);

	public ResponseEntity<InputStreamResource> exportExecutionNCExcel(
			String projectId, String companyId, String ncrType,
			String criticality, String subProject, String organizationName);

	public Map<String, Object> getKpi(String projectId, String department,
			String subProject, String floatType, String weightType,
			String milestone, String otd, String ippActivityType, String functionGroup);
	
	Map<String, List<OTDTrendChartDetailsDTO>> getOTDTrendChart(String projectId,
			String department, String subProject, String floatType,
			String weight, String chartType, String milestone, String otd, String ippActivityType, String functionGroup);

	byte[] downloadExecutionStartChartPopupExcel(String projectId, String department, String subProject, String floatType,
												 String weightType, String milestone,
												 String otd, String ippActivityType, String functionGroup,
												 String otdStart, String timingStart );

	byte[] downloadExecutionFinishChartPopupExcel(String projectId, String department, String subProject, String floatType,
												 String weightType, String milestone,
												 String otd, String ippActivityType, String functionGroup,
												 String otdFinish, String timingFinish );

}