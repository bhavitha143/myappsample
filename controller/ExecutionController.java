/**
 * @author Shweta Sawant
 *
 */
package com.bh.realtrack.controller;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.exception.RealTrackException;
import com.bh.realtrack.util.AssertUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bh.realtrack.dto.OTDTrendChartDetailsDTO;
import com.bh.realtrack.service.IExecutionService;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping("api/v1/execution")
public class ExecutionController {

	private static final Logger log = LoggerFactory.getLogger(ExecutionController.class);

	@Autowired
	private IExecutionService iExecutionService;

	@RequestMapping(value = "/getExecutionChart", method = RequestMethod.GET)
	public Map<String, Object> getExecutionChart(
			@RequestHeader HttpHeaders headers, @RequestParam String projectId,
			String department, String subProject, String floatType,
			String weightType, String chartType, String milestone, String otd,String ippActivityType,String functionGroup) {
		return iExecutionService.getExecutionChart(projectId, department,
				subProject, floatType, weightType, chartType, milestone, otd,ippActivityType,functionGroup);
	}

	@RequestMapping(value = "/downloadExecutionExcel", method = RequestMethod.GET)
	public ResponseEntity<?> exportExecutionExcel(
			@RequestParam final String projectId, String department,
			String subProject, String floatType, String weightType,
			String milestone, String otd,String ippActivityType,String functionGroup,
			@RequestHeader final HttpHeaders headers) {
		return iExecutionService.exportExecutionExcel(projectId, department,
				subProject, floatType, weightType, milestone, otd,ippActivityType,functionGroup);
	}

	@RequestMapping(value = "/downloadExecutionNCExcel", method = RequestMethod.GET)
	public ResponseEntity<?> downloadExecutionNCExcel(
			@RequestParam final String projectId, String companyId,
			String subProject, String ncrType, String criticality, @RequestParam(required = false) String organizationName,
			@RequestHeader final HttpHeaders headers) {
		return iExecutionService.exportExecutionNCExcel(projectId, companyId,
				ncrType, criticality, subProject,organizationName);
	}

	@RequestMapping(value = "/getExecutionFilters", method = RequestMethod.GET)
	public Map<String, Object> getExecutionFilters(
			@RequestParam String projectId, String department) {
		return iExecutionService.getExecutionFilters(projectId, department);
	}

	@RequestMapping(value = "/getKpi", method = RequestMethod.GET)
	public Map<String, Object> getKpi(@RequestParam String projectId,
			String department, String subProject, String floatType,
			String weightType, String milestone, String otd,String ippActivityType,String functionGroup) {
		return iExecutionService.getKpi(projectId, department, subProject,
				floatType, weightType, milestone, otd,ippActivityType,functionGroup);
	}

	@RequestMapping(value = "/getExecutionProgress", method = RequestMethod.GET)
	public Map<String, Object> getExecutionProgress(
			@RequestHeader HttpHeaders headers, @RequestParam String projectId,
			String department, String subProject) {
		return iExecutionService.getExecutionProgress(projectId, department,
				subProject);
	}

	@RequestMapping(value = "/getExecutionDates", method = RequestMethod.GET)
	public Map<String, Object> getExecutionDates(@RequestParam String projectId) {
		return iExecutionService.getExecutionDates(projectId);
	}
	
	@RequestMapping(value = "/getOTDTrendChart", method = RequestMethod.GET)
	public Map<String, List<OTDTrendChartDetailsDTO>> getOTDTrendChart(
			@RequestHeader HttpHeaders headers, @RequestParam String projectId,
			String department, String subProject, String floatType,
			String weightType, String chartType, String milestone, String otd,String ippActivityType,String functionGroup) {
		return iExecutionService.getOTDTrendChart(projectId, department,
				subProject, floatType, weightType, chartType, milestone, otd,ippActivityType,functionGroup);
	}




    //popup excel download
	@GetMapping(value = "/downloadExecutionStartChartPopupExcel")
	public void downloadExecutionStartChartPopupExcel(
			@RequestHeader HttpHeaders headers, @RequestParam final String  projectId,
			@RequestParam final String  department, @RequestParam final String subProject, @RequestParam final String floatType,
			@RequestParam final String weightType, @RequestParam final String chartType, @RequestParam final String milestone,
			@RequestParam final String otd, @RequestParam final String ippActivityType, @RequestParam final String functionGroup,
			@RequestParam final String otdStart, @RequestParam final String timingStart, HttpServletResponse response) throws RealTrackException {

		String fileName = "Execution_"+chartType+"_List.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		try {
			byte[] plData = iExecutionService.downloadExecutionStartChartPopupExcel(projectId, department, subProject,
					floatType, weightType, milestone, otd, ippActivityType, functionGroup, otdStart, timingStart);
			IOUtils.write(plData,response.getOutputStream());
		}catch (Exception e) {
			log.error("Error occured when downloading Execution start popup list excel file :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading Execution start popup list excel file  :: " + e.getMessage());
			}
		}


	}


	//popup excel download
	@GetMapping(value = "/downloadExecutionFinishChartPopupExcel")
	public void downloadExecutionFinishChartPopupExcel(
		    @RequestParam final String  projectId,
			@RequestParam final String  department, @RequestParam final String subProject, @RequestParam final String floatType,
			@RequestParam final String weightType, @RequestParam final String chartType, @RequestParam final String milestone,
			@RequestParam final String otd, @RequestParam final String ippActivityType, @RequestParam final String functionGroup,
			@RequestParam final String otdFinish, @RequestParam final String timingFinish, @RequestHeader HttpHeaders headers, HttpServletResponse response) throws RealTrackException {

		String fileName ="Execution_"+chartType+"_List.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		try {
			byte[] plData = iExecutionService.downloadExecutionFinishChartPopupExcel(projectId, department, subProject,
					floatType, weightType, milestone, otd, ippActivityType, functionGroup, otdFinish, timingFinish);
			IOUtils.write(plData,response.getOutputStream());
		}catch (Exception e) {
			log.error("Error occured when downloading Execution finish popup list excel file :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading Execution finsh popup list excel file  :: " + e.getMessage());
			}
		}


	}




}
