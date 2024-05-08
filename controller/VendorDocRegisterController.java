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

import com.bh.realtrack.service.IVendorDocRegisterService;
import com.bh.realtrack.util.AssertUtils;

@RestController
@CrossOrigin
public class VendorDocRegisterController {

	private static final Logger log = LoggerFactory.getLogger(VendorDocRegisterController.class);

	@Autowired
	IVendorDocRegisterService iVendorDocRegisterService;

	@RequestMapping(value = "/getStatisticsMileStoneFilter", method = RequestMethod.GET)
	public Map<String, Object> getStatisticsMileStoneFilter(@RequestParam(name = "project-id") String projectId,
			@RequestParam(name = "sub-project-number") String subProjectNo,
			@RequestParam(name = "sub-product-line") String subProductLine,
			@RequestParam(name = "doc-owner") String docOwner, @RequestParam(name = "doc-type") String docType,
			@RequestParam(name = "doc-level") String docLevel) {
		return iVendorDocRegisterService.getStatisticsMileStoneFilter(projectId, subProjectNo, subProductLine, docOwner,
				docType, docLevel);
	}

	@RequestMapping(value = "/getStatisticsOTDChartData", method = RequestMethod.GET)
	public Map<String, Object> getStatisticsOTDChartData(@RequestParam(name = "project-id") String projectId,
			@RequestParam(name = "sub-project-number") String subProjectNo,
			@RequestParam(name = "sub-product-line") String subProductLine,
			@RequestParam(name = "doc-owner") String docOwner, @RequestParam(name = "doc-type") String docType,
			@RequestParam(name = "doc-level") String docLevel,
			@RequestParam(name = "milestone-code") String milestoneCode) {
		return iVendorDocRegisterService.getStatisticsOTDChartData(projectId, subProjectNo, subProductLine, docOwner,
				docType, docLevel, milestoneCode);
	}

	@RequestMapping(value = "/getStatisticsOTDChartPopupDetails", method = RequestMethod.GET)
	public Map<String, Object> getStatisticsOTDChartPopupDetails(@RequestParam(name = "project-id") String projectId,
			@RequestParam(name = "sub-project-number") String subProjectNo,
			@RequestParam(name = "sub-product-line") String subProductLine,
			@RequestParam(name = "doc-owner") String docOwner, @RequestParam(name = "doc-type") String docType,
			@RequestParam(name = "doc-level") String docLevel,
			@RequestParam(name = "milestone-code") String milestoneCode,
			@RequestParam(name = "milestone-status") String milestoneStatus) {
		return iVendorDocRegisterService.getStatisticsOTDChartPopupDetails(projectId, subProjectNo, subProductLine,
				docOwner, docType, docLevel, milestoneCode, milestoneStatus);
	}

	@RequestMapping(value = "/downloadStatisticsOTDExcelDetails", method = RequestMethod.GET)
	public void downloadStatisticsOTDExcelDetails(@RequestParam(name = "project-id") String projectId,
			@RequestParam(name = "sub-project-number") String subProjectNo,
			@RequestParam(name = "sub-product-line") String subProductLine,
			@RequestParam(name = "doc-owner") String docOwner, @RequestParam(name = "doc-type") String docType,
			@RequestParam(name = "doc-level") String docLevel,
			@RequestParam(name = "milestone-code") String milestoneCode, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "STATISTICS_OTD_EXCEL_DETAILS_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iVendorDocRegisterService.downloadStatisticsOTDExcelDetails(projectId, subProjectNo,
					subProductLine, docOwner, docType, docLevel, milestoneCode);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading statistics otd excel file :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading statistics otd excel file :: " + e.getMessage());
			}
		}
	}

}
