package com.bh.realtrack.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.NPIDropdownResponseDTO;
import com.bh.realtrack.dto.NPISummaryDetailsResponseDTO;
import com.bh.realtrack.service.INPIProjectDetailsService;
import com.bh.realtrack.util.AssertUtils;

@RestController
@CrossOrigin
public class NPIProjectDetailsController {

	@Autowired
	INPIProjectDetailsService npiProjectDetailsService;
	private static final Logger log = LoggerFactory.getLogger(NPIProjectDetailsController.class);

	@RequestMapping(method = RequestMethod.GET, value = "/getNPIDropDownDetails")
	public ResponseEntity<?> getNPIDropDownDetails(@RequestParam String projectId) throws Exception {
		if (null != projectId && !projectId.isEmpty()) {
			NPIDropdownResponseDTO npiDropdownResponse = npiProjectDetailsService.getNPIDropDownDetails(projectId);
			return new ResponseEntity<NPIDropdownResponseDTO>(npiDropdownResponse, HttpStatus.OK);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getNPISummaryDetails")
	public ResponseEntity<?> getNPISummaryDetails(@RequestParam String projectId,
			@RequestParam List<String> subProject, @RequestParam List<String> owner,
			@RequestParam List<String> activityGroup) throws Exception {
		if (null != projectId && !projectId.isEmpty()) {
			NPISummaryDetailsResponseDTO summaryDetailsresponse = npiProjectDetailsService
					.getNPISummaryDetails(projectId, subProject, owner, activityGroup);
			return new ResponseEntity<NPISummaryDetailsResponseDTO>(summaryDetailsresponse, HttpStatus.OK);
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getNPIDetailsPopupData")
	public ResponseEntity<?> getNPIDetailsPopupData(@RequestParam String projectId,
			@RequestParam String chartType, @RequestParam String status,
			@QueryParam(value = "fwWeek") String fwWeek, @RequestParam List<String> subProject, @RequestParam List<String> owner, 
			@RequestParam List<String> activityGroup) throws Exception {
		if (null != projectId && !projectId.isEmpty()) {
			Map<String, Object> popUpDataResponse = npiProjectDetailsService
					.getNPIDetailsPopupData(projectId, chartType, status, fwWeek, subProject, owner, activityGroup);
			return new ResponseEntity<Map<String, Object>>(popUpDataResponse, HttpStatus.OK);
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getNPIOTDTrendDetails")
	public ResponseEntity<?> getNPIOTDTrendDetails(@RequestParam String projectId,
			@RequestParam List<String> subProject, @RequestParam List<String> owner,
			@RequestParam List<String> activityGroup, @RequestParam String startDate, @RequestParam String endDate) throws Exception {
		if (null != projectId && !projectId.isEmpty()) {
			Map<String, Object> otdTrendDetailsResponse = npiProjectDetailsService
					.getNPIOTDTrendDetails(projectId, subProject, owner, activityGroup, startDate, endDate);
			return new ResponseEntity<Map<String, Object>>(otdTrendDetailsResponse, HttpStatus.OK);
		}
		return null;
	}

	@GetMapping("/downloadNPIDetails")
	public void downloadNPIDetails(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam String owner, @RequestParam String activityGroup, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "NPI_Project_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] excelDetails = npiProjectDetailsService.getNPIDetailsExcel(projectId, subProject, owner,
					activityGroup);
			IOUtils.write(excelDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured while downloading NPI Project Details " + e.getMessage());
		}
	}


}
