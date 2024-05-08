/**
 * @author Shweta Sawant
 *
 */
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.LCSOSCellDTO;
import com.bh.realtrack.dto.LogisticCostDropdownDTO;
import com.bh.realtrack.dto.ShowExpiryMessageDTO;
import com.bh.realtrack.service.IShippingPreservationService;
import com.bh.realtrack.util.AssertUtils;

@RestController
@CrossOrigin
@RequestMapping("api/v1/shipping")
public class ShippingPreservationController {

	private static final Logger log = LoggerFactory.getLogger(ShippingPreservationController.class);

	@Autowired
	IShippingPreservationService iShippingPreservationService;

	// Shipping/Preservation Widget

	@GetMapping("/showExpiryMessageFlag")
	public ShowExpiryMessageDTO showExpiryMessageFlag(@RequestParam(name = "project-id") String projectId,
			@RequestParam String status) {
		return iShippingPreservationService.showExpiryMessageFlag(projectId, status);
	}

	@RequestMapping(value = "/downloadShippingReportDetails", method = RequestMethod.GET)
	public void downloadShippingReportDetails(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("INIT- downloadShippingReportDetails for projectId : {}", projectId);
		String fileName = "SHIPPING_DETAILS_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iShippingPreservationService.downloadShippingReportDetails(projectId);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading Shipping Report Details :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading Shipping Report Details :: " + e.getMessage());
			}
		}
	}

	// Logistic Cost Widget

	@RequestMapping(value = "/getLogisticCostDropdowns", method = RequestMethod.GET)
	public LogisticCostDropdownDTO getLogisticCostDropdowns(@RequestParam String projectId) throws Exception {
		return iShippingPreservationService.getLogisticCostDropdowns(projectId);
	}

	@RequestMapping(value = "/getLCAgainstVolumeSummary", method = RequestMethod.GET)
	public Map<String, Object> getLCAgainstVolumeSummary(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String showBy, @RequestParam String viewType) {
		return iShippingPreservationService.getLCAgainstVolumeSummary(projectId, subProject, showBy, viewType);
	}

	@RequestMapping(value = "/getLCVolumeAnalysisSummary", method = RequestMethod.GET)
	public Map<String, Object> getLCVolumeAnalysisSummary(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String viewType) {
		return iShippingPreservationService.getLCVolumeAnalysisSummary(projectId, subProject, viewType);
	}

	@RequestMapping(value = "/getLCAgainstVolumeDetailsPopup", method = RequestMethod.GET)
	public Map<String, Object> getLCAgainstVolumeDetailsPopup(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String showBy, @RequestParam String viewType,
			@RequestParam(required = false) String selectedPeriod) {
		return iShippingPreservationService.getLCAgainstVolumeDetailsPopup(projectId, subProject, showBy, viewType,
				selectedPeriod);
	}

	@RequestMapping(value = "/getLCVolumeAnalysisDetailsPopup", method = RequestMethod.GET)
	public Map<String, Object> getLCVolumeAnalysisDetailsPopup(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam(required = false) String viewType, String chartType,
			@RequestParam String category) {
		return iShippingPreservationService.getLCVolumeAnalysisDetailsPopup(projectId, subProject, viewType, chartType,
				category);
	}

	@RequestMapping(value = "/downloadLCAgainstVolumeDetails", method = RequestMethod.GET)
	public void downloadLCAgainstVolumeDetails(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam String showBy, @RequestParam String viewType, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "LC_AGAINST_VOLUME_DETAILS_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iShippingPreservationService.downloadLCAgainstVolumeDetails(projectId, subProject, showBy,
					viewType);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading lc against volume excel file :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading lc against volume excel file :: " + e.getMessage());
			}
		}
	}

	@RequestMapping(value = "/downloadLCVolumeAnalysisDetails", method = RequestMethod.GET)
	public void downloadLCVolumeAnalysisDetails(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam(required = false) String viewType, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "LC_VOLUME_ANALYSIS_DETAILS_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iShippingPreservationService.downloadLCVolumeAnalysisDetails(projectId, subProject,
					viewType);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading lc volume analysis excel file :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading lc volume analysis excel file :: " + e.getMessage());
			}
		}
	}

	@RequestMapping(value = "/getLCSOSCellData", method = RequestMethod.GET)
	public Map<String, Object> getLCSOSCellData(@RequestParam String projectId) throws Exception {
		return iShippingPreservationService.getLCSOSCellData(projectId);
	}

	@RequestMapping(value = "/saveLCSOSCellData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveLCSOSCellData(@RequestBody LCSOSCellDTO sosCellDTO) {
		return iShippingPreservationService.saveLCSOSCellData(sosCellDTO);
	}
}
