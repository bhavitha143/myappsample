package com.bh.realtrack.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.realtrack.service.VarOrderReportService;

/**
 * 
 * @author tchavda
 * @since 2019-03-01
 * @version 1.0
 */
@Controller
@CrossOrigin
@RequestMapping("api/v1/varOrderReport")
public class VarOrderReportController {

	Logger logger = LoggerFactory.getLogger(VarOrderReportController.class);

	@Autowired
	private VarOrderReportService varOrderReportService;

	@RequestMapping(value = "/downloadOrderReport", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> downloadVarOrderReportExcel(@RequestHeader HttpHeaders headers,
			@RequestParam("projectId") String projectId, @RequestParam("roleId") int roleId,
			@RequestParam("companyId") int companyId) {

		return varOrderReportService.createExcelBytes(projectId, roleId, companyId);
	}
}
