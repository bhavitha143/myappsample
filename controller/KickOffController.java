package com.bh.realtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.KickOffLogDetailsDTO;
import com.bh.realtrack.dto.SaveKickOffDTO;
import com.bh.realtrack.dto.SaveKickOffResponseDTO;
import com.bh.realtrack.service.IKickOffService;

/**
 *
 * @author Anand Kumar
 *
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/kickoff")
public class KickOffController {

	@Autowired
	private IKickOffService iKickOffService;

	@RequestMapping(value = "/getKickOffDetails", method = RequestMethod.GET)
	public KickOffLogDetailsDTO getProjectComplexity(
			@RequestParam final String projectId) {
		return iKickOffService.getKickOffDetails(projectId);
	}

	@RequestMapping(value = "/saveKickOffDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> saveProjectComplexity(
			@RequestHeader final HttpHeaders headers,
			@RequestBody final SaveKickOffDTO saveKickOffDTO) {
		return new ResponseEntity<SaveKickOffResponseDTO>(
				iKickOffService.saveKickOffDetails(saveKickOffDTO),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/downloadKickOffExcel", method = RequestMethod.GET)
	public ResponseEntity<?> exportKickOffExcel(
			@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers) {
		return iKickOffService.exportKickOffExcel(projectId);
	}

	@RequestMapping(value = "/downloadKickOffPDF", method = RequestMethod.GET)
	public ResponseEntity<?> exportKickOffPDF(
			@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers) throws Exception {
		return iKickOffService.exportKickOffPDF(projectId);
	}
}
