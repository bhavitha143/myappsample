package com.bh.realtrack.dao;

import java.util.List;

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

public interface IExecutionDao {
	public List<ExecutionDTO> getExecutionChart(String projectId, String department, String subProject,
			String floatType, String weight, String milestone, String otd, String ippActivityType, String functionGroup);

	public List<ExecutionStartDTO> getExecutionStartChart(String projectId, String department, String subProject,
			String floatType, String weight, String milestone, String otd, String ippActivityType, String functionGroup);

	public List<ExecutionFinishDTO> getExecutionFinishChart(String projectId, String department, String subProject,
			String floatType, String weight, String milestone, String otd, String ippActivityType, String functionGroup);

	public List<ExecutionDropDown> getSubProjectFilter(String projectId, String department);

	public List<String> getFloatTypeFilter(String projectId);

	public List<ScurveProgressDTO> getExecutionProgress(String projectId, String department, String subProject,
			String dataDate, String weekDate);

	public List<ProgressDates> getProgressDates(String projectId);

	public List<String> getWeightFilter(String projectId);

	public List<ExecutionDates> getExecutionDates(String projectId);

	public String getRole(String sso);

	public List<ExecutionNCDTO> getExecutionForNC(String projectId, String role, String companyId, String ncrType,
			String criticality, String subProject, String organizationName);
	public List<ExecutionNCDTO> getExecutionForNCForTPS(String projectId, String role, String companyId, String ncrType,
		  String criticality, String subProject);
	public List<ExecutionFunctionFilter> getFunctionFilter(String projectId);

	public List<String> getMilestone();

	public List<String> getOtd();

	public List<KpiDTO> getKpi(String projectId, String department, String subProject, String floatType,
			String weightType, String milestone, String otd, String ippActivityType, String functionGroup);

	List<OTDTrendChartDetailsDTO> getOTDTrendChartData(String projectId, String department, String subProject,
			String floatType, String weight, String milestone, String otd, String ippActivityType, String functionGroup);

	public List<String> getIppActivityType(String projectId);

	public List<DropDownDTO> getMilestoneList(String projectId);

	public List<String> getFunctionGroup(String projectId);

	List<ExecutionStartDTO> downloadExecutionStartChartPopupExcel(String projectId, String department, String subProject, String floatType,
												 String weightType, String milestone,
												 String otd, String ippActivityType, String functionGroup,
												 String otdStart, String timingStart );

	List<ExecutionFinishDTO> downloadExecutionFinishChartPopupExcel(String projectId, String department, String subProject, String floatType,
												  String weightType, String milestone,
												  String otd, String ippActivityType, String functionGroup,
												  String otdFinish, String timingFinish );



}
