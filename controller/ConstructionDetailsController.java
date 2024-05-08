package com.bh.realtrack.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.ClientErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.ActivityDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.exception.RealTrackException;
import com.bh.realtrack.service.IConstructionDetailsService;
import com.bh.realtrack.util.AssertUtils;

/**
 * @author Anand Kumar
 *
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/construction")
public class ConstructionDetailsController {
	@Autowired
	private IConstructionDetailsService iConstructionDetailsService;

	@RequestMapping(value = "/getConstructionDetails", method = RequestMethod.GET)
	public Map<String, Object> getConstructionDetails(
			@RequestParam final String projectId,
			@RequestParam final String trains,
			@RequestParam final String departments,
			@RequestParam final String filterFlag,
			@RequestParam final String intExtFlag,
			@RequestParam final boolean publish) {

		return iConstructionDetailsService.getConstructionDetails(projectId,
				trains, departments, filterFlag, intExtFlag, publish);
	}

	@RequestMapping(value = "/getActivityDetails", method = RequestMethod.GET)
	public Map<String, List<ActivityDetailsDTO>> getActivityDetails(
			@RequestParam final String projectId,
			@RequestParam final String epsContractId,
			@RequestParam final String jobNumber,
			@RequestParam final String filterFlag,
			@RequestParam final String weightFlag,
			@RequestParam final String intExtFlag,
			@RequestParam final String department,
			@RequestParam final String train,
			@RequestParam final String activityTypeDesc,
			@RequestParam final boolean publish) {

		return iConstructionDetailsService.getActivityDetails(projectId,
				epsContractId, jobNumber, filterFlag, weightFlag, intExtFlag,
				department, train, activityTypeDesc, publish);

	}

	@RequestMapping(value = "/getConstructionFilters", method = RequestMethod.GET)
	public Map<String, Object> getConstructionFilterDetails(
			@RequestParam final String projectId,
			@RequestParam final String intExtFlag,
			@RequestParam final boolean publish) {
		return iConstructionDetailsService.getConstructionFilterDetails(
				projectId, intExtFlag, publish);
	}

	@RequestMapping(value = "/saveLatestPublish", method = RequestMethod.POST)
	public Map<String, String> saveLatestPublishDetails(
			@RequestParam final String projectId,
			@RequestParam final String intExtFlag) {
		try {
			String projId = AssertUtils.validateString(projectId);
			String flag = AssertUtils.validateString(intExtFlag);
			return iConstructionDetailsService.saveLatestPublishDetails(projId,
					flag);
		} catch (RealTrackException e) {
			throw new ClientErrorException(
					ErrorCode.BAD_REQUEST.getResponseStatus());
		}

	}

}
