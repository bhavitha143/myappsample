/**
 * @author Shweta Sawant
 *
 */
package com.bh.realtrack.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.exception.RealTrackException;
import com.bh.realtrack.service.IManufacturingService;

@RestController
@CrossOrigin
@RequestMapping("api/v1/manufacturing")
public class ManufacturingController {

	private static final Logger log = LoggerFactory.getLogger(ManufacturingController.class);

	@Autowired
	IManufacturingService iManufacturingService;

	@GetMapping("/manufacturingAgingStatus")
	public Map<String, Object> getManufacturingAgingStatus(@RequestParam(name = "project-ids") String projectIds,
			@RequestParam(name = "company-id") String companyId, @RequestParam String subproject,
			@RequestParam String ncrtype, @RequestParam String criticality,
			@RequestParam(required = false) String organizationName) {
		return iManufacturingService.getManufacturingAgingStatus(projectIds, companyId, subproject, ncrtype,
				criticality, organizationName);
	}

	@GetMapping("/manufacturingCreationCurrent")
	public Map<String, Object> getManufacturingCreationCurrent(@RequestParam(name = "project-ids") String projectIds,
			@RequestParam(name = "company-id") String companyId, @RequestParam String subproject,
			@RequestParam String ncrtype, @RequestParam String criticality,
			@RequestParam(required = false) String organizationName, @RequestParam String fromdate,
			@RequestParam String todate) {
		return iManufacturingService.getManufacturingCreationCurrent(projectIds, companyId, subproject, ncrtype,
				criticality, organizationName, fromdate, todate);
	}

	@GetMapping("/manufacturingCreationClosure")
	public Map<String, Object> getManufacturingCreationClosure(@RequestParam(name = "project-ids") String projectIds,
			@RequestParam(name = "company-id") String companyId, @RequestParam String subproject,
			@RequestParam String ncrtype, @RequestParam String criticality,
			@RequestParam(required = false) String organizationName, @RequestParam String fromdate,
			@RequestParam String todate) {
		return iManufacturingService.getManufacturingCreationClosure(projectIds, companyId, subproject, ncrtype,
				criticality, organizationName, fromdate, todate);
	}

	@GetMapping(value = "/getManufacturingFilters")
	public Map<String, Object> getManufacturingFilters(@RequestParam String projectId, @RequestParam int companyId) {
		return iManufacturingService.getManufacturingFilters(projectId, companyId);
	}

	@GetMapping(value = "/downloadExcelForNCAgingStatusPopup")
	public void downloadExcelForNCAgingStatusPopup(@RequestParam String projectId, @RequestParam String companyId,
			@RequestParam String subProject, @RequestParam String ncrType, @RequestParam String criticality,
			@RequestParam(required = false) String organizationName, @RequestParam int barStartRange,
			@RequestParam int barEndRange, @RequestParam String barStatus, @RequestHeader HttpHeaders headers,
			HttpServletResponse response) throws RealTrackException {

		String fileName = "NC_Aging_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		try {
			byte[] plData = iManufacturingService.downloadExcelForNCAgingStatusPopup(projectId, companyId, subProject,
					ncrType, criticality, organizationName, barStartRange, barEndRange, barStatus);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error(
					"Error occured when downloading Nc Aging Status status popup list excel file :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading Nc Aging Status popup list excel file  :: " + e.getMessage());
			}
		}

	}

	@GetMapping(value = "/downloadExcelForNCCreationByCurrentStatusPopup")
	public void downloadExcelForNCCreationByCurrentStatusPopup(@RequestParam String projectId,
			@RequestParam String companyId, @RequestParam String subProject, @RequestParam String ncrType,
			@RequestParam String criticality, @RequestParam String fromDate, @RequestParam String toDate,
			@RequestParam(required = false) String organizationName, @RequestParam String barStartDate,
			@RequestParam String barEndDate, @RequestParam String barStatus, @RequestHeader HttpHeaders headers,
			HttpServletResponse response) throws RealTrackException {

		String fileName = "NC_Creation_Current_Status_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		try {
			byte[] plData = iManufacturingService.downloadExcelForNCCreationByCurrentStatusPopup(projectId, companyId,
					subProject, ncrType, criticality, fromDate, toDate, organizationName, barStartDate, barEndDate,
					barStatus);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading creation by current status popup list excel file :: "
					+ e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading dcreation by current status popup list excel file  :: "
						+ e.getMessage());
			}
		}

	}

	@GetMapping(value = "/downloadExcelForNCCreationClosureStatusPopup")
	public void downloadExcelForNCCreationClosureStatusPopup(@RequestParam String projectId,
			@RequestParam String companyId, @RequestParam String subProject, @RequestParam String ncrType,
			@RequestParam String criticality, @RequestParam String fromDate, @RequestParam String toDate,
			@RequestParam(required = false) String organizationName, @RequestParam String barStartDate,
			@RequestParam String barEndDate, @RequestParam String chartType, @RequestHeader HttpHeaders headers,
			HttpServletResponse response) throws RealTrackException {

		String fileName = "NC_" + chartType + "_Status_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		try {
			byte[] plData = iManufacturingService.downloadExcelForNCCreationClosureStatusPopup(projectId, companyId,
					subProject, ncrType, criticality, fromDate, toDate, organizationName, barStartDate, barEndDate,
					chartType);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading creation and closure status popup list excel file :: "
					+ e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading creation and closure status popup list excel file  :: "
						+ e.getMessage());
			}
		}

	}

	@GetMapping(value = "/getManufacturingSummaryPopUpDetails")
	public Map<String, Object> getManufacturingSummaryPopUpDetails(@RequestParam(name = "projectId") String projectId,
			@RequestParam(name = "companyId") String companyId, @RequestParam String subProject,
			@RequestParam String ncrType, @RequestParam String criticality, @RequestParam String status) {
		return iManufacturingService.getManufacturingSummaryPopUpDetails(projectId, companyId, subProject, ncrType,
				criticality, status);
	}

}
