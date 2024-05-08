package com.bh.realtrack.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.LCDashboardBPSDTO;
import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisFilterDTO;
import com.bh.realtrack.dto.LCDashboardDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisFilterDTO;
import com.bh.realtrack.service.LogisticCostDashboardService;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/logistic")
public class LogisticCostDashboardController {
	private static final Logger log = LoggerFactory.getLogger(LogisticCostDashboardController.class);

	@Autowired
	private LogisticCostDashboardService logisticService;

	@RequestMapping(value = "/getLCDashboardDropDowns", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getLCDashboardDropDowns(@RequestBody LCDashboardDTO paramList) {
		return logisticService.getLCDashboardDropDowns(paramList);
	}

	@RequestMapping(value = "/getLCDashboardBoxPackingSummary", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getLCDashboardBoxPackingSummary(@RequestBody LCDashboardBPSDTO paramList) {
		return logisticService.getLCDashboardBoxPackingSummary(paramList);
	}

	@RequestMapping(value = "/getLCDashboardTransportationSummary", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getLCDashboardTransportationSummary(@RequestBody LCDashboardBPSDTO paramList) {
		return logisticService.getLCDashboardTransportationSummary(paramList);
	}

	@RequestMapping(value = "/getLCDashboardWorkloadDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getLCDashboardWorkloadDetails(@RequestBody LCDashboardBPSDTO paramList) {
		return logisticService.getLCDashboardWorkloadDetails(paramList);
	}

	@RequestMapping(value = "/downloadLCDashboardWorkloadDetails", method = RequestMethod.GET)
	public void downloadLCDashboardWorkloadDetails(@RequestParam int customerId, @RequestParam int companyId,
			@RequestParam String businessUnit, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			@RequestParam String shippingManager, HttpServletResponse response) throws Exception {
		LCDashboardBPSDTO paramList = new LCDashboardBPSDTO();
		String fileName = "LC_WORKLOAD_DETAILS.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			paramList.setCustomerId(customerId);
			paramList.setCompanyId(companyId);
			paramList.setBusinessUnit(businessUnit);
			paramList.setSegment(segment);
			paramList.setRegion(region);
			paramList.setPmLeader(pmLeader);
			paramList.setSpm(spm);
			paramList.setFinancialSegment(financialSegment);
			paramList.setShippingManager(shippingManager);
			byte[] plData = logisticService.downloadLCDashboardWorkloadDetails(paramList);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading LC Dashboard workload excel file :: " + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading LC Dashboard workload excel file :: " + e.getMessage());
			}
		}
	}

	@RequestMapping(value = "/getLCDashboardBPSiteAnalysisFilters", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getLCDashboardBPSiteAnalysisFilters(@RequestBody LCDashboardDTO paramList) {
		return logisticService.getLCDashboardBPSiteAnalysisFilters(paramList);
	}

	@RequestMapping(value = "/getLCDashboardBPSiteAnalysisSummary", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getLCDashboardBPSiteAnalysisSummary(
			@RequestBody LCDashboardBPSiteAnalysisFilterDTO paramList) {
		return logisticService.getLCDashboardBPSiteAnalysisSummary(paramList);
	}

	@RequestMapping(value = "/downloadLCDashboardBPSiteAnalysisDetails", method = RequestMethod.GET)
	public void downloadLCDashboardWorkloadDetails(@RequestParam int customerId, @RequestParam int companyId,
			@RequestParam String businessUnit, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			@RequestParam String shippingManager, @RequestParam String site, @RequestParam String packType,
			@RequestParam String startDate, @RequestParam String endDate, HttpServletResponse response)
			throws Exception {
		LCDashboardBPSiteAnalysisFilterDTO paramList = new LCDashboardBPSiteAnalysisFilterDTO();
		String fileName = "LC_BP_SITE_ANALYSIS_DETAILS.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			paramList.setCustomerId(customerId);
			paramList.setCompanyId(companyId);
			paramList.setBusinessUnit(businessUnit);
			paramList.setSegment(segment);
			paramList.setRegion(region);
			paramList.setPmLeader(pmLeader);
			paramList.setSpm(spm);
			paramList.setFinancialSegment(financialSegment);
			paramList.setShippingManager(shippingManager);
			paramList.setSite(site);
			paramList.setPackType(packType);
			paramList.setStartDate(startDate);
			paramList.setEndDate(endDate);
			byte[] plData = logisticService.downloadLCDashboardBPSiteAnalysisDetails(paramList);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardController :: downloadLCDashboardBPSiteAnalysisDetails :: "
					+ e.getMessage());
		}
	}

	@RequestMapping(value = "/getLCDashboardTransportationAnalysisFilters", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getLCDashboardTransportationAnalysisFilters(@RequestBody LCDashboardDTO paramList) {
		return logisticService.getLCDashboardTransportationAnalysisFilters(paramList);
	}

	@RequestMapping(value = "/getLCDashboardTransportationAnalysisSummary", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getLCDashboardTransportationAnalysisSummary(
			@RequestBody LCDashboardTransportationAnalysisFilterDTO paramList) {
		return logisticService.getLCDashboardTransportationAnalysisSummary(paramList);
	}

	@RequestMapping(value = "/downloadLCDashboardTransportationAnalysisDetails", method = RequestMethod.GET)
	public void downloadLCDashboardTransportationAnalysisDetails(@RequestParam int customerId,
			@RequestParam int companyId, @RequestParam String businessUnit, @RequestParam String segment,
			@RequestParam String region, @RequestParam String pmLeader, @RequestParam String spm,
			@RequestParam String financialSegment, @RequestParam String shippingManager,
			@RequestParam String transportationRegion, @RequestParam String incoterms, @RequestParam String startDate,
			@RequestParam String endDate, HttpServletResponse response) throws Exception {
		LCDashboardTransportationAnalysisFilterDTO paramList = new LCDashboardTransportationAnalysisFilterDTO();
		String fileName = "LC_TRANSPORTATION_ANALYSIS_DETAILS.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			paramList.setCustomerId(customerId);
			paramList.setCompanyId(companyId);
			paramList.setBusinessUnit(businessUnit);
			paramList.setSegment(segment);
			paramList.setRegion(region);
			paramList.setPmLeader(pmLeader);
			paramList.setSpm(spm);
			paramList.setFinancialSegment(financialSegment);
			paramList.setShippingManager(shippingManager);
			paramList.setTransportationRegion(transportationRegion);
			paramList.setIncoterms(incoterms);
			paramList.setStartDate(startDate);
			paramList.setEndDate(endDate);
			byte[] plData = logisticService.downloadLCDashboardTransportationAnalysisDetails(paramList);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error(
					"Exception in LogisticCostDashboardController :: downloadLCDashboardTransportationeAnalysisDetails :: "
							+ e.getMessage());
		}
	}

}
