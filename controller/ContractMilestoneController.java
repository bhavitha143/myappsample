/**
 * 
 */
package com.bh.realtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bh.realtrack.service.IContractMilestoneService;

/**
 * @author Anand Kumar
 *
 */
@Controller
@CrossOrigin
@RequestMapping("api/v1/contractmilestone")
public class ContractMilestoneController {
	@Autowired
	private IContractMilestoneService iContractMilestoneService;

	@RequestMapping(value = "/downloadContractMilestoneExcel", method = RequestMethod.GET)
	public ResponseEntity<?> exportRiskRegisterExcel(
			@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers) {
		return iContractMilestoneService
				.exportContractMilestoneExcel(projectId);
	}
}
