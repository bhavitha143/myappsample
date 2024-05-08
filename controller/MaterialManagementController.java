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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.service.IMaterialManagementService;
import com.bh.realtrack.util.AssertUtils;

/**
 * @author Shweta D. Sawant
 */
@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/materialmanagement")
public class MaterialManagementController {

	@Autowired
	IMaterialManagementService iMaterialManagementService;

	private static final Logger log = LoggerFactory.getLogger(MaterialManagementController.class);

	/* Material Management Receiving Tab */

	@GetMapping("/getMMReceivingDropdownDetails")
	public Map<String, Object> getMMReceivingDropdownDetails(@RequestParam String projectId) {
		return iMaterialManagementService.getMMReceivingDropdownDetails(projectId);
	}

	@GetMapping("/getMMReceivingSummaryDetails")
	public Map<String, Object> getMMReceivingSummaryDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String organization, @RequestParam String location) {
		return iMaterialManagementService.getMMReceivingSummaryDetails(projectId, subProject, organization, location);
	}

	@GetMapping("/getMMReceivingPieChartDetails")
	public Map<String, Object> getMMReceivingPieChartDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String organization, @RequestParam String location) {
		return iMaterialManagementService.getMMReceivingPieChartDetails(projectId, subProject, organization, location);
	}

	@GetMapping("/getMMReceivingDetailsPopupData")
	public Map<String, Object> getMMReceivingDetailsPopupData(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String organization, @RequestParam String location,
			@RequestParam String type, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate, @RequestParam(required = false) String fiscalWeek) {
		return iMaterialManagementService.getMMReceivingDetailsPopupData(projectId, subProject, organization, location,
				type, startDate, endDate, fiscalWeek);
	}

	@GetMapping("/getMMReceivingChartDetails")
	public Map<String, Object> getMMReceivingChartDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String organization, @RequestParam String location,
			@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam(required = false) String weekOrMonth) {
		return iMaterialManagementService.getMMReceivingChartDetails(projectId, subProject, organization, location,
				startDate, endDate, weekOrMonth);
	}

	@GetMapping("/downloadMMReceivingDetailsExcel")
	public void exportMMReceivingDetailsExcel(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam String organization, @RequestParam String location, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "Material_Management_Receiving_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] excelDetails = iMaterialManagementService.getMMReceivingDetailsExcel(projectId, subProject,
					organization, location);
			IOUtils.write(excelDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : Receiving Tab - Excel file :: "
					+ e.getMessage());
		}
	}

	/* Material Management Picking Tab */

	@GetMapping("/getMMCheckApplicableFlag")
	public Map<String, Object> getMMCheckApplicableFlag(@RequestParam String projectId) {
		return iMaterialManagementService.getMMCheckApplicableFlag(projectId);
	}

	@GetMapping("/getMMPickingDropdownDetails")
	public Map<String, Object> getMMPickingDropdownDetails(@RequestParam String projectId) {
		return iMaterialManagementService.getMMPickingDropdownDetails(projectId);
	}

	@GetMapping("/getMMPickingSummaryDetails")
	public Map<String, Object> getMMPickingSummaryDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String subInvFrom, @RequestParam String subInvTo) {
		return iMaterialManagementService.getMMPickingSummaryDetails(projectId, subProject, subInvFrom, subInvTo);
	}

	@GetMapping("/getMMPickingBacklogTrendChartDetails")
	public Map<String, Object> getMMPickingBacklogTrendChartDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String subInvFrom, @RequestParam String subInvTo,
			@RequestParam String startDate, @RequestParam String endDate) {
		return iMaterialManagementService.getMMPickingBacklogTrendChartDetails(projectId, subProject, subInvFrom,
				subInvTo, startDate, endDate);
	}

	@GetMapping("/getMMPickingPopupDetails")
	public Map<String, Object> getMMPickingPopupDetails(@RequestParam String projectId, @RequestParam String subProject,
			@RequestParam String subInvFrom, @RequestParam String subInvTo, @RequestParam String type,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
			@RequestParam(required = false) String fiscalWeek) {
		return iMaterialManagementService.getMMPickingPopupDetails(projectId, subProject, subInvFrom, subInvTo, type,
				startDate, endDate, fiscalWeek);
	}

	@GetMapping("/downloadMMPickingDetailsExcel")
	public void exportMMPickingDetailsExcel(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam String subInvFrom, @RequestParam String subInvTo, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "Material_Management_Picking_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] excelDetails = iMaterialManagementService.exportMMPickingDetailsExcel(projectId, subProject,
					subInvFrom, subInvTo);
			IOUtils.write(excelDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : Picking Tab - Excel file :: "
					+ e.getMessage());
		}
	}

	/* Material Management Mover Order Transfer Tab */

	@GetMapping("/getMMMoveOrderDropdownDetails")
	public Map<String, Object> getMMMoveOrderDropdownDetails(@RequestParam String projectId) {
		return iMaterialManagementService.getMMMoveOrderDropdownDetails(projectId);
	}

	@GetMapping("/getMMMoveOrderSummaryDetails")
	public Map<String, Object> getMMMoveOrderSummaryDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String subInvFrom, @RequestParam String subInvTo) {
		return iMaterialManagementService.getMMMoveOrderSummaryDetails(projectId, subProject, subInvFrom, subInvTo);
	}

	@GetMapping("/getMMMoveOrderBacklogTrendChartDetails")
	public Map<String, Object> getMMMoveOrderBacklogTrendChartDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String subInvFrom, @RequestParam String subInvTo,
			@RequestParam String startDate, @RequestParam String endDate) {
		return iMaterialManagementService.getMMMoveOrderBacklogTrendChartDetails(projectId, subProject, subInvFrom,
				subInvTo, startDate, endDate);
	}

	@GetMapping("/getMMMoveOrderPopupDetails")
	public Map<String, Object> getMMMoveOrderPopupDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String subInvFrom, @RequestParam String subInvTo,
			@RequestParam String type, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate, @RequestParam(required = false) String fiscalWeek) {
		return iMaterialManagementService.getMMMoveOrderPopupDetails(projectId, subProject, subInvFrom, subInvTo, type,
				startDate, endDate, fiscalWeek);
	}

	@GetMapping("/downloadMMMoveOrderDetailsExcel")
	public void exportMMMoveOrderDetailsExcel(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam String subInvFrom, @RequestParam String subInvTo, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "Material_Management_Move_Order_Transfer_Details_" + AssertUtils.sanitizeString(projectId)
				+ ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] excelDetails = iMaterialManagementService.exportMMMoveOrderDetailsExcel(projectId, subProject,
					subInvFrom, subInvTo);
			IOUtils.write(excelDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : Move Order Transfer Tab - Excel file :: "
					+ e.getMessage());
		}
	}

	/* Material Management WF Anomaly Tab */

	@GetMapping("/getWFAnoDropDownDetails")
	public Map<String, Object> getWFAnoDropDownDetails(@RequestParam String projectId) {
		return iMaterialManagementService.getWFAnoDropDownDetails(projectId);
	}

	@GetMapping("/getWFAnoSummaryDetails")
	public Map<String, Object> getWFAnoSummaryDetails(@RequestParam String projectId, @RequestParam String ownership) {
		return iMaterialManagementService.getWFAnoSummaryDetails(projectId, ownership);
	}

	@GetMapping("/getWFAnoChartDetails")
	public Map<String, Object> getWFAnoChartDetails(@RequestParam String projectId, @RequestParam String ownership) {
		return iMaterialManagementService.getWFAnoChartDetails(projectId, ownership);
	}

	@GetMapping("/getWFAnoPopupDetails")
	public Map<String, Object> getWFAnoPopupDetails(@RequestParam String projectId, @RequestParam String ownership,
			@RequestParam String chartType, @RequestParam String status) {
		return iMaterialManagementService.getWFAnoPopupDetails(projectId, ownership, chartType, status);
	}

	@GetMapping("/downloadWFAnoExcelDetails")
	public void exportWFAnoExcelDetailsExcel(@RequestParam final String projectId, @RequestParam String ownership,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "Material_Management_WF_Anomaly_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] excelDetails = iMaterialManagementService.exportWFAnoExcelDetailsExcel(projectId, ownership);
			IOUtils.write(excelDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : WF Anomaly Tab - Excel file :: "
					+ e.getMessage());
		}
	}

	/* Material Management Packing Tab */

	@GetMapping("/getPackingFDDropdownDetails")
	public Map<String, Object> getPackingFDDropdownDetails(@RequestParam String projectId) {
		return iMaterialManagementService.getPackingFDDropdownDetails(projectId);
	}

	@GetMapping("/getMMPackingFDSummaryDetails")
	public Map<String, Object> getMMPackingFDSummaryDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String subInvFrom, @RequestParam String subInvTo,
			@RequestParam String ownership) {
		return iMaterialManagementService.getMMPackingFDSummaryDetails(projectId, subProject, subInvFrom, subInvTo,
				ownership);
	}

	@GetMapping("/getMMPackingFDChartDetails")
	public Map<String, Object> getMMPackingFDChartDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String subInvFrom, @RequestParam String subInvTo,
			@RequestParam String ownership, @RequestParam String startDate, @RequestParam String endDate) {
		return iMaterialManagementService.getMMPackingFDChartDetails(projectId, subProject, subInvFrom, subInvTo,
				ownership, startDate, endDate);
	}

	@GetMapping("/getMMPackingFDPopupDetails")
	public Map<String, Object> getMMPackingFDPopupDetails(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String subInvFrom, @RequestParam String subInvTo,
			@RequestParam String ownership, @RequestParam String chartType, @RequestParam String status,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
			@RequestParam(required = false) String fiscalWeek) {
		return iMaterialManagementService.getMMPackingFDPopupDetails(projectId, subProject, subInvFrom, subInvTo,
				ownership, chartType, status, startDate, endDate, fiscalWeek);
	}

	@GetMapping("/downloadMMPackingFDDetailsExcel")
	public void exportMMPackingFDDetailsExcel(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam String subInvFrom, @RequestParam String subInvTo, @RequestParam String ownership,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "Material_Management_Packing_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] excelDetails = iMaterialManagementService.exportMMPackingFDDetailsExcel(projectId, subProject,
					subInvFrom, subInvTo, ownership);
			IOUtils.write(excelDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured while downloading Material Management : Packing Tab - Excel file :: "
					+ e.getMessage());
		}
	}
}
