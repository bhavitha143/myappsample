package com.bh.realtrack.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.service.IQualityConsoleService;

@RestController
@CrossOrigin
public class QualityConsoleController {

	private static final Logger log = LoggerFactory.getLogger(QualityConsoleController.class);

	@Autowired
	IQualityConsoleService iQualityConsoleService;

	@RequestMapping(value = "/getQualityConsoleProjectDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleProjectDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleProjectDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleProjectStatus", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleProjectStatus(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleProjectStatus(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleInspectionDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleInspectionDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleInspectionDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleQIRadarDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleQIRadarDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleQIRadarDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleDPUDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleDPUDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleDPUDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsolePunchListDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsolePunchListDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsolePunchListDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleCompletenessDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleCompletenessDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleCompletenessDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleNonConformancesDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleNonConformancesDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleNonConformancesDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleQCPDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleQCPDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleQCPDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleMDRDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleMDRDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleMDRDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleTRSBookDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleTRSBookDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleTRSBookDetails(projectId);
	}

	@RequestMapping(value = "/getQualityConsoleTRSMeetingDetails", method = RequestMethod.GET)
	public Map<String, Object> getQualityConsoleTRSMeetingDetails(@RequestParam String projectId) {
		return iQualityConsoleService.getQualityConsoleTRSMeetingDetails(projectId);
	}

}
