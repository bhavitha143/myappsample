/**
 * 
 */
package com.bh.realtrack.controller;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.FormParam;

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
import org.springframework.web.multipart.MultipartFile;

import com.bh.realtrack.dto.InspectionCalendarLogDetailsDTO;
import com.bh.realtrack.dto.InspectionConfiguratorDTO;
import com.bh.realtrack.dto.InspectionExecutionLogDetailsDTO;
import com.bh.realtrack.dto.InspectionLookAheadLogDTO;
import com.bh.realtrack.dto.InspectionStatusLogDetailsDTO;
import com.bh.realtrack.dto.InspectionTestPlanDTO;
import com.bh.realtrack.dto.InspectionTestPlanLogDetailsDTO;
import com.bh.realtrack.dto.MCCCheckApplicableDTO;
import com.bh.realtrack.dto.MCCDropdownResponseDTO;
import com.bh.realtrack.dto.MCCSummaryDTO;
import com.bh.realtrack.dto.UpdateDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.exception.RealTrackException;
import com.bh.realtrack.service.IInspectionTestPlanService;
import com.bh.realtrack.util.AssertUtils;
import com.bh.realtrack.util.InspectionTestPlanConstants;

/**
 * @author Anand Kumar
 *
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/inspection")
public class InspectionTestPlanController {

	private static Logger log = LoggerFactory.getLogger(InspectionTestPlanController.class.getName());

	@Autowired
	private IInspectionTestPlanService iInspectionTestPlanService;

	/***
	 * This controller method is used to fetch the inspection execution details
	 * based on the received project id and user type
	 * 
	 * Response data will have total, executed and percentage of executed values
	 * 
	 * For Internal User -- Plain table data will be fetched External User --
	 * Configuration filter condition will be applied on the table data excluding
	 * Period condition Customer -- Data will be fetched from the separate Customer
	 * table
	 * 
	 */
	@RequestMapping(value = "/getInspectionExecutionDetails", method = RequestMethod.GET)
	public InspectionExecutionLogDetailsDTO getInspectionExecutionDetails(@RequestParam final String projectId,
			@RequestParam final String userType) {
		return iInspectionTestPlanService.getExecutionDetails(projectId, userType);
	}

	/***
	 * This controller method is used to fetch the inspection status customer
	 * presence details based on the project id for the given user type
	 * 
	 * For Internal User -- Plain table data will be fetched External User --
	 * Configuration filter condition will be applied on the table data
	 * 
	 */
	@RequestMapping(value = "/getInspectionStatus", method = RequestMethod.GET)
	public InspectionStatusLogDetailsDTO getInspectionStatus(@RequestParam final String projectId,
			@RequestParam final String userType) {
		return iInspectionTestPlanService.getInspectionStatus(projectId, userType);
	}

	/***
	 * This controller method is used to fetch calendar details For Internal User --
	 * Plain table data will be fetched External User -- Configuration filter
	 * condition will be applied on the table data
	 */
	@RequestMapping(value = "/getCalendarDetails", method = RequestMethod.GET)
	public InspectionCalendarLogDetailsDTO getInspectionCalendarDetails(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, @RequestParam final String userType) {
		return iInspectionTestPlanService.getInspectionCalendarDetails(projectId, userType);
	}

	/***
	 * This controller method is used to fetch the inspection look ahead details
	 * 
	 * For Internal User -- Plain table data will be fetched External User --
	 * Configuration filter condition will be applied on the table data
	 */
	@RequestMapping(value = "/getInspectionLookAheadDetails", method = RequestMethod.GET)
	public InspectionLookAheadLogDTO getInspectionLookAheadDetails(@RequestParam final String projectId,
			@RequestParam final String userType) {
		return iInspectionTestPlanService.getInspectionLookAheadDetails(projectId, userType);
	}

	/***
	 * This controller method is used to fetch the inspection test plan details
	 * 
	 * For Internal User -- Plain table data will be fetched External User --
	 * Configuration filter condition will be applied on the table data
	 */
	@RequestMapping(value = "/getInspectionTestPlanDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public InspectionTestPlanLogDetailsDTO getInspectionTestPlan(
			@RequestBody InspectionTestPlanDTO inspectionTestPlanDTO) {
		return iInspectionTestPlanService.getInspectionTestPlanDetails(inspectionTestPlanDTO,
				inspectionTestPlanDTO.getUserType());
	}

	/***
	 * This controller method is used to fetch the inspection test plan details for
	 * the customer
	 * 
	 */
	@RequestMapping(value = "/getCustomerInspectionTestPlanDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public InspectionTestPlanLogDetailsDTO getCustomerInspectionTestPlan(
			@RequestBody InspectionTestPlanDTO inspectionTestPlanDTO) {
		return iInspectionTestPlanService.getCustomerInspectionTestPlanDetails(inspectionTestPlanDTO);
	}

	/***
	 * This controller method is used to publish the inspection details to the
	 * customer view table
	 * 
	 * @throws RealTrackException
	 * 
	 */
	@RequestMapping(value = "/publishInspectionDetails", method = RequestMethod.POST)
	public String publishInspectionDetails(@RequestParam final String projectId, @RequestParam final String pqmComments)
			throws RealTrackException {

		String proId = AssertUtils.validateString(projectId);
		String comments = AssertUtils.validateString(pqmComments);
		return iInspectionTestPlanService.publishCustomerData(proId, comments);
	}

	/***
	 * This controller method is used to fetch the inspection configuration details
	 * 
	 */
	@RequestMapping(value = "/getInspectionConfiguratorDetails", method = RequestMethod.GET)
	public InspectionConfiguratorDTO getInspectionConfiguratorDetails(@RequestParam final String projectId) {
		return iInspectionTestPlanService.getInspectionConfiguratorDetails(projectId, true);
	}

	/***
	 * This controller method is used to fetch the inspection configuration details
	 * 
	 */
	@RequestMapping(value = "/getCustomerInspectionConfiguratorDetails", method = RequestMethod.GET)
	public InspectionConfiguratorDTO getCustomerInspectionConfiguratorDetails(@RequestParam final String projectId) {
		return iInspectionTestPlanService.getCustomerInspectionConfiguration(projectId);
	}

	/***
	 * This controller method is used to save the inspection configuration details
	 * 
	 */
	@RequestMapping(value = "/saveInspectionConfiguratorDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public InspectionConfiguratorDTO saveInspectionConfiguratorDetails(
			@RequestBody final InspectionConfiguratorDTO inspectionConfiguratorDTO) {
		return iInspectionTestPlanService.updateInspectionConfiguratorDetails(inspectionConfiguratorDTO);
	}

	/***
	 * This controller method is used to download the inspection test plan in excel
	 * for the Internal and External
	 * 
	 * @throws RealTrackException
	 * 
	 */
	@RequestMapping(value = "/downloadInspectionTestPlanExcel", method = RequestMethod.GET)
	public void downloadInspectionTestPlanExcelCSV(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, @RequestParam final String userType, HttpServletRequest request,
			HttpServletResponse response) throws RealTrackException {

		String proId = AssertUtils.sanitizeString(projectId);

		String fileName = "IAP-Last_Updated_Ext_" + proId + ".csv";

		if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			fileName = "IAP-Last_Updated_Int_" + proId + ".csv";
		}

		// OutputStream outputStream = null;
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			// outputStream = response.getOutputStream();
			String iapData = iInspectionTestPlanService.downloadIAPData(proId, userType);
			IOUtils.write(iapData, response.getOutputStream());
			// outputStream.write(iapData.getBytes());
			// outputStream.flush();
		} catch (Exception e) {
			log.error("Error occured when downloading IAP details" + e.getStackTrace());
		} finally {
			try {
				// outputStream.close();
			} catch (Exception e) {
				log.error("Error occured when downloading IAP details" + e.getStackTrace());
			}
		}
	}

	/***
	 * This controller method is used to download the inspection test plan in excel
	 * for the Customer
	 * 
	 * @throws RealTrackException
	 * 
	 */
	@RequestMapping(value = "/downloadCustomerInspectionTestPlanExcel", method = RequestMethod.GET)
	public void downloadCustomerInspectionTestPlanCSV(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws RealTrackException {

		String proId = AssertUtils.sanitizeString(projectId);

		String fileName = "IAP-Last_Published_" + proId + ".csv";
		// OutputStream outputStream = null;
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			// outputStream = response.getOutputStream();
			String iapData = iInspectionTestPlanService.downloadCustomerInspectionTestPlanExcel(proId);
			IOUtils.write(iapData, response.getOutputStream());
			// outputStream.write(iapData.getBytes());
			// outputStream.flush();
		} catch (Exception e) {
			log.error("Error occured when downloading Customer IAP details" + e.getStackTrace());
		} finally {
			try {
				// outputStream.close();
			} catch (Exception e) {
				log.error("Error occured when downloading Customer IAP details" + e.getStackTrace());
			}
		}
	}

	/***
	 * This controller method is used to download the inspection test plan in excel
	 * 
	 * @throws RealTrackException
	 * 
	 */
	@RequestMapping(value = "/downloadIAPExcel", method = RequestMethod.GET)
	public void downloadIAPExcel(@RequestParam final String projectId, @RequestParam final String userType,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws RealTrackException {

		String proId = AssertUtils.sanitizeString(projectId);

		String fileName = "IAP-Last_Updated_Ext_" + proId + ".xlsx";
		if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			fileName = "IAP-Last_Updated_Int_" + proId + ".xlsx";
		}
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {

			byte[] iapData = iInspectionTestPlanService.downloadIAPExcel(proId, userType);
			IOUtils.write(iapData, response.getOutputStream());

		} catch (Exception e) {
			log.error("Error occured when downloading IAP Excel details" + e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading IAP Excel details" + e.getMessage());
			}
		}
	}

	/***
	 * This controller method is used to upload the inspection test plan in excel
	 * file and publish the details to the customer view
	 * 
	 */
	@RequestMapping(value = "/uploadIAPExcel", method = RequestMethod.POST)
	public Map<String, String> importIAPExcel(@RequestParam final String projectId,
			@FormParam("comments") final String comments, @FormParam("excelFile") final MultipartFile excelFile,
			@RequestHeader final HttpHeaders headers) {
		try {
			String projId = AssertUtils.sanitizeString(projectId);
			MultipartFile excel = AssertUtils.validateExcel(excelFile);
			return iInspectionTestPlanService.importIAPExcel(projId, excel, comments);
		} catch (RealTrackException e) {
			log.error("uploadIAPExcel :: " + e.getMessage());
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}

	}

	/***
	 * This controller method is used to upload the inspection test plan in excel
	 * file and publish the details to the customer view
	 * 
	 */
	@RequestMapping(value = "/uploadInspectionTestPlanExcel", method = RequestMethod.POST)
	public Map<String, String> importInspectionTestPlanExcel(@RequestParam final String projectId,
			@FormParam("comments") final String comments, @FormParam("excelFile") final MultipartFile excelFile,
			@RequestHeader final HttpHeaders headers) {
		try {
			String projId = AssertUtils.sanitizeString(projectId);
			MultipartFile excel = AssertUtils.validateCSV(excelFile);
			return iInspectionTestPlanService.importInspectionTestPlanExcel(projId, excel, comments);
		} catch (RealTrackException e) {
			log.error("Printing error logs" + e.getMessage());
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}

	}

	/***
	 * This controller method is used to fetch the status of the uploaded file
	 * 
	 */
	@RequestMapping(value = "/updateDetails", method = RequestMethod.GET)
	public UpdateDetailsDTO updateDetials(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers) {
		return iInspectionTestPlanService.getUpdateDetails(projectId);
	}

	/***
	 * This controller method is used to download the inspection test plan in PDF
	 * for the Customer
	 */
	@RequestMapping(value = "/downloadCustomerInspectionTestPlanPdf", method = RequestMethod.GET)
	public void downloadCustomerInspectionTestPlanPDF(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws RealTrackException {

		String proId = AssertUtils.sanitizeString(projectId);

		String fileName = "IAP-Last_Published_" + proId + ".pdf";
		// OutputStream outputStream = null;
		response.setContentType("text/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			// outputStream = response.getOutputStream();
			ByteArrayOutputStream pdfDoc = iInspectionTestPlanService.downloadCustomerInspectionTestPlanPdf(projectId);
			IOUtils.write(pdfDoc.toByteArray(), response.getOutputStream());
			// outputStream.write(pdfDoc.toByteArray());
			// outputStream.flush();
		} catch (Exception e) {
			log.error("Error occured when downloading Customer IAP details" + e.getStackTrace());
		} finally {
			try {
				// outputStream.close();
			} catch (Exception e) {
				log.error("Error occured when downloading Customer IAP details" + e.getStackTrace());
			}
		}
	}

	@RequestMapping(value = "/getMCCDropDownDetails", method = RequestMethod.GET)
	public MCCDropdownResponseDTO getMCCDropDownDetails(@RequestParam String projectId) throws Exception {
		return iInspectionTestPlanService.getMCCDropDownDetails(projectId);
	}

	@RequestMapping(value = "/getMCCSummary", method = RequestMethod.GET)
	public MCCSummaryDTO getMCCSummary(@RequestParam String projectId, @RequestParam String subProject,
			@RequestParam String discipline, @RequestParam String module, @RequestParam String showBy)
			throws Exception {
		return iInspectionTestPlanService.getMCCSummary(projectId, subProject, discipline, module, showBy);
	}

	@GetMapping("/downloadMCCDetails")
	public void downloadMCCDetailsExcel(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam String discipline, @RequestParam String module, @RequestParam String showBy,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "MCC_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] excelDetails = iInspectionTestPlanService.getMCCDetailsExcel(projectId, subProject, discipline,
					module, showBy);
			IOUtils.write(excelDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured while downloading MCC Details :: " + e.getMessage());
		}
	}

	@RequestMapping(value = "/getMCCChartPopupData", method = RequestMethod.GET)
	public Map<String, Object> getMCCChartPopupData(@RequestParam String projectId, @RequestParam String subProject,
			@RequestParam String discipline, @RequestParam String module, @RequestParam String showBy,
			@RequestParam String type, @RequestParam String status, @RequestParam(required = false) String subStatus) {
		return iInspectionTestPlanService.getMCCChartPopupData(projectId, subProject, discipline, module, showBy, type,
				status, subStatus);
	}

	@RequestMapping(value = "/checkMCCApplicable", method = RequestMethod.GET)
	public MCCCheckApplicableDTO checkMCCApplicable(@RequestParam String projectId) throws Exception {
		return iInspectionTestPlanService.checkMCCApplicable(projectId);
	}

	@RequestMapping(value = "/getMCCChartDetails", method = RequestMethod.GET)
	public Map<String, Object> getMCCChartDetails(@RequestParam String projectId, @RequestParam String subProject,
			@RequestParam String discipline, @RequestParam String module, @RequestParam String showBy) {
		return iInspectionTestPlanService.getMCCChartDetails(projectId, subProject, discipline, module, showBy);
	}

}