package com.bh.realtrack.controller;

import java.text.ParseException;
import java.util.Map;

import javax.ws.rs.ClientErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.CalculateNEComplexityDTO;
import com.bh.realtrack.dto.CalculateSRVComplexityDTO;
import com.bh.realtrack.dto.ComplexityFormTemplateDTO;
import com.bh.realtrack.dto.ITOComplexityTabDTO;
import com.bh.realtrack.dto.NEComplexityTabDTO;
import com.bh.realtrack.dto.NEProjectComplexityDTO;
import com.bh.realtrack.dto.NEProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.ProjectComplexityLogDetailsDTO;
import com.bh.realtrack.dto.SRVPPProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SRVProjectComplexityDTO;
import com.bh.realtrack.dto.SRVProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SaveNEComplexityDTO;
import com.bh.realtrack.dto.SaveNEComplexityResponseDTO;
import com.bh.realtrack.dto.SaveProjectComplexityDTO;
import com.bh.realtrack.dto.SaveProjectComplexityResponseDTO;
import com.bh.realtrack.dto.saveSRVComplexityDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.exception.RealTrackException;
import com.bh.realtrack.service.IProjectCompleixtyService;
import com.bh.realtrack.util.AssertUtils;

/**
 *
 * @author Anand Kumar
 *
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/projectcomplexity")
public class ProjectComplexityController {

	@Autowired
	private IProjectCompleixtyService iProjectConplexityService;

	@RequestMapping(value = "/getProjectComplexity", method = RequestMethod.GET)
	public ProjectComplexityLogDetailsDTO getProjectComplexity(@RequestParam final String projectId) {
		try {
			String projId = AssertUtils.validateString(projectId);
			return iProjectConplexityService.getProjectComplexityDetails(projId);
		} catch (RealTrackException e) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}

	}

	@RequestMapping(value = "/saveProjectComplexity", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> saveProjectComplexity(@RequestHeader final HttpHeaders headers,
			@RequestBody final SaveProjectComplexityDTO saveProjectComplexityDTO) {
		if (saveProjectComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<SaveProjectComplexityResponseDTO>(
				iProjectConplexityService.saveProjectComplexity(saveProjectComplexityDTO), HttpStatus.OK);
	}

	@RequestMapping(value = "/getPCDropdownlist", method = RequestMethod.GET)
	public Map<String, Object> getProjectComplexityDropdownList(@RequestParam final String attributeGroup) {
		if (attributeGroup == null || attributeGroup.isEmpty()) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return iProjectConplexityService.getProjectComplexityDropdownList(attributeGroup);

	}

	@GetMapping("/getNEProjectComplexityData")
	public NEProjectComplexityDTO getNEProjectComplexityData(@RequestParam String projectId) {
		return iProjectConplexityService.getNEProjectComplexityData(projectId);
	}

	@GetMapping("/getNEComplexityProjectTabData")
	public Map<String, Object> getNEComplexityProjectTabData(@RequestParam String projectId) {
		return iProjectConplexityService.getNEComplexityProjectTabData(projectId);
	}

	@GetMapping("/getComplexityFormTemplate")
	public ComplexityFormTemplateDTO getComplexityFormTemplate(@RequestParam String projectId) {
		return iProjectConplexityService.getComplexityFormTemplate(projectId);
	}

	@RequestMapping(value = "/saveNEComplexityData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> saveNEComplexityData(@RequestHeader final HttpHeaders headers,
			@RequestBody final SaveNEComplexityDTO saveNEComplexityDTO) {
		if (saveNEComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<SaveNEComplexityResponseDTO>(
				iProjectConplexityService.saveNEComplexityData(saveNEComplexityDTO), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculateNEProjectComplexityData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> calculateNEProjectComplexityData(@RequestHeader final HttpHeaders headers,
			@RequestBody final CalculateNEComplexityDTO calculateNEComplexityDTO) {
		if (calculateNEComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<Map<String, Object>>(
				iProjectConplexityService.calculateNEProjectComplexityData(calculateNEComplexityDTO), HttpStatus.OK);
	}

	@RequestMapping(value = "/getNEProjectComplexityDropDownData", method = RequestMethod.GET)
	public NEProjectComplexityDropDownDTO getNEProjectComplexityDropDownData(@RequestParam String projectId)
			throws Exception {
		return iProjectConplexityService.getNEProjectComplexityDropDownData(projectId);
	}

	@RequestMapping(value = "/getNEPPProjectComplexityDropDownData", method = RequestMethod.GET)
	public NEProjectComplexityDropDownDTO getNEPPProjectComplexityDropDownData(@RequestParam String projectId)
			throws Exception {
		return iProjectConplexityService.getNEPPProjectComplexityDropDownData(projectId);
	}

	@RequestMapping(value = "/getNEQPMProjectComplexityDropDownData", method = RequestMethod.GET)
	public NEProjectComplexityDropDownDTO getNEQPMProjectComplexityDropDownData(@RequestParam String projectId)
			throws Exception {
		return iProjectConplexityService.getNEQPMProjectComplexityDropDownData(projectId);
	}

	@GetMapping("/getNEProjectComplexityITOData")
	public NEProjectComplexityDTO getNEProjectComplexityITOData(@RequestParam String projectId) {
		return iProjectConplexityService.getNEProjectComplexityITOData(projectId);
	}

	@GetMapping("/getNEComplexityProjectITOTabData")
	public Map<String, Object> getNEComplexityProjectITOTabData(@RequestParam String projectId) {
		return iProjectConplexityService.getNEComplexityProjectITOTabData(projectId);
	}

	@RequestMapping(value = "/saveNEComplexityITOData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> saveNEComplexityITOData(@RequestHeader final HttpHeaders headers,
			@RequestBody final SaveNEComplexityDTO saveNEComplexityDTO) {
		if (saveNEComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<SaveNEComplexityResponseDTO>(
				iProjectConplexityService.saveNEComplexityITOData(saveNEComplexityDTO), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculateNEProjectComplexityITOData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> calculateNEProjectComplexityITOData(@RequestHeader final HttpHeaders headers,
			@RequestBody final CalculateNEComplexityDTO calculateNEComplexityDTO) {
		if (calculateNEComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<Map<String, Object>>(
				iProjectConplexityService.calculateNEProjectComplexityITOData(calculateNEComplexityDTO), HttpStatus.OK);
	}

	@RequestMapping(value = "/saveITOComplexityDataQPM", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> saveITOComplexityDataQPM(@RequestHeader final HttpHeaders headers,
			@RequestBody final SaveNEComplexityDTO saveNEComplexityDTO) {
		if (saveNEComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<SaveNEComplexityResponseDTO>(
				iProjectConplexityService.saveITOComplexityDataQPM(saveNEComplexityDTO), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculateITOProjectComplexityDataQPM", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> calculateITOProjectComplexityDataQPM(@RequestHeader final HttpHeaders headers,
			@RequestBody final CalculateNEComplexityDTO calculateNEComplexityDTO) {
		if (calculateNEComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<Map<String, Object>>(
				iProjectConplexityService.calculateITOProjectComplexityDataQPM(calculateNEComplexityDTO),
				HttpStatus.OK);
	}
	
	@GetMapping("/getProjectComplexitySRVData")
	public SRVProjectComplexityDTO getProjectComplexitySRVData(@RequestParam String projectId) {
		return iProjectConplexityService.getProjectComplexitySRVData(projectId);
	}

	@RequestMapping(value = "/getSRVPMProjectComplexityDropDownData", method = RequestMethod.GET)
	public SRVProjectComplexityDropDownDTO getSRVPMProjectComplexityDropDownData(@RequestParam String projectId)
			throws Exception {
		return iProjectConplexityService.getSRVPMProjectComplexityDropDownData(projectId);
	}

	@RequestMapping(value = "/getSRVPPProjectComplexityDropDownData", method = RequestMethod.GET)
	public SRVPPProjectComplexityDropDownDTO getSRVPPProjectComplexityDropDownData(@RequestParam String projectId)
			throws Exception {
		return iProjectConplexityService.getSRVPPProjectComplexityDropDownData(projectId);
	}

	@RequestMapping(value = "/calculateSRVProjectComplexityData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> calculateSRVProjectComplexityData(@RequestHeader final HttpHeaders headers,
			@RequestBody final CalculateSRVComplexityDTO calculateSRVComplexityDTO) {
		if (calculateSRVComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<Map<String, Object>>(
				iProjectConplexityService.calculateSRVProjectComplexityData(calculateSRVComplexityDTO), HttpStatus.OK);
	}

	@RequestMapping(value = "/saveSRVComplexityData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> saveSRVComplexityData(@RequestHeader final HttpHeaders headers,
			@RequestBody final saveSRVComplexityDTO saveSRVComplexityDTO) {
		if (saveSRVComplexityDTO == null) {
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
		return new ResponseEntity<SaveNEComplexityResponseDTO>(
				iProjectConplexityService.saveSRVComplexityData(saveSRVComplexityDTO), HttpStatus.OK);
	}

	@RequestMapping(value = "/getProjectSummaryDetails", method = RequestMethod.GET)
	public Map<String, Object> getProjectSummaryDetails(@RequestParam String projectId) throws ParseException {
		return iProjectConplexityService.getProjectSummaryDetails(projectId);
	}

	@RequestMapping(value = "/saveNEComplexityProjectTabData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveNEComplexityProjectTabData(@RequestBody NEComplexityTabDTO tabDTO) {
		return iProjectConplexityService.saveNEComplexityProjectTabData(tabDTO);
	}

	@RequestMapping(value = "/saveITOComplexityProjectTabData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveITOComplexityProjectTabData(@RequestBody ITOComplexityTabDTO tabDTO) {
		return iProjectConplexityService.saveITOComplexityProjectTabData(tabDTO);
	}
}
