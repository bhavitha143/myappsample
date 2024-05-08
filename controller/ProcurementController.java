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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.service.IProcurementService;
import com.bh.realtrack.util.AssertUtils;

@RestController
@CrossOrigin
public class ProcurementController {

	@Autowired
	IProcurementService iProcurementService;

	private static final Logger log = LoggerFactory.getLogger(ProcurementController.class);

	@RequestMapping(value = "/getItemBuyControlFlowDetails", method = RequestMethod.GET)
	public Map<String, Object> getItemBuyControlFlowDetails(@RequestParam String jobNumber,
			@RequestParam String dummyCode, @RequestParam String activityFilter) {
		return iProcurementService.getItemBuyControlFlowDetails(jobNumber, dummyCode, activityFilter);
	}

	@RequestMapping(value = "/downloadMaterialListDetails", method = RequestMethod.GET)
	public void downloadMaterialListDetails(@RequestParam(name = "project-id") String projectId,
			@RequestParam(name = "view-consideration") String viewConsideration,
			@RequestParam(name = "train") String train, @RequestParam(name = "sub-project") String subProject,
			@RequestParam(name = "component-code") String componentCode,
			@RequestParam(name = "activity-filter") String activityFilter, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = null != viewConsideration && viewConsideration.equalsIgnoreCase("Internal") ? "Live Plan View"
				: "Custom View";
		String fileName = "Procurement-" + viewName + "-" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iProcurementService.downloadMaterialListDetails(projectId, viewConsideration, train,
					subProject, componentCode, activityFilter, viewName);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Exception in ProcurementController :: downloadMaterialListDetails :: " + e.getMessage());
		}
	}

}
