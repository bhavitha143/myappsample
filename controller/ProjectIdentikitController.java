package com.bh.realtrack.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.ProjectDataformSummaryDTO;
import com.bh.realtrack.service.IProjectIdentikitService;
import com.bh.realtrack.util.AssertUtils;

@RestController
@CrossOrigin
public class ProjectIdentikitController {

	@Autowired
	IProjectIdentikitService iProjectIdentikitService;

	private static final Logger log = LoggerFactory.getLogger(ProjectIdentikitController.class);

	@RequestMapping(value = "/getIdentikitProjectDetails", method = RequestMethod.GET)
	public Map<String, Object> getIdentikitProjectDetails(@RequestParam String projectId) {
		return iProjectIdentikitService.getIdentikitProjectDetails(projectId);
	}

	@RequestMapping(value = "/getIdentikitProjectCoreTeamDetails", method = RequestMethod.GET)
	public Map<String, Object> getIdentikitProjectCoreTeamDetails(@RequestParam String projectId) {
		return iProjectIdentikitService.getIdentikitProjectCoreTeamDetails(projectId);
	}

	@RequestMapping(value = "/getIdentikitProjectGeographyDetails", method = RequestMethod.GET)
	public Map<String, Object> getIdentikitProjectGeographyDetails(@RequestParam String projectId) {
		return iProjectIdentikitService.getIdentikitProjectGeographyDetails(projectId);
	}

	@RequestMapping(value = "/getIdentikitProjectScopeDetails", method = RequestMethod.GET)
	public Map<String, Object> getIdentikitProjectScopeDetails(@RequestParam String projectId) {
		return iProjectIdentikitService.getIdentikitProjectScopeDetails(projectId);
	}

	@RequestMapping(value = "/saveIdentikitProjectDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveIdentikitProjectDetails(@RequestBody ProjectDataformSummaryDTO summaryDTO) {
		return iProjectIdentikitService.saveIdentikitProjectDetails(summaryDTO);
	}

	@RequestMapping(value = "/getIdentikitPMAddNotesHistory", method = RequestMethod.GET)
	public Map<String, Object> getIdentikitPMAddNotesHistory(@RequestParam String projectId) {
		return iProjectIdentikitService.getIdentikitPMAddNotesHistory(projectId);
	}

	@RequestMapping(value = "/downloadIdentikitProjectScopeDetails", method = RequestMethod.GET)
	public void downloadIdentikitProjectScopeDetails(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "IDENTIKIT_PROJECT_SCOPE_DETAILS_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iProjectIdentikitService.downloadIdentikitProjectScopeDetails(projectId);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading identikit project scope excel file :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading identikit project scope excel file :: " + e.getMessage());
			}
		}
	}

}
