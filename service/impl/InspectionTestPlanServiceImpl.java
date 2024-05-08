/**
 * 
 */
package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bh.realtrack.dao.IInspectionTestPlanDAO;
import com.bh.realtrack.dto.ChartAxisColorDTO;
import com.bh.realtrack.dto.CustomerInspectionTestPlanDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.InspectionCalendarDetailsDTO;
import com.bh.realtrack.dto.InspectionCalendarLogDetailsDTO;
import com.bh.realtrack.dto.InspectionCommentsDTO;
import com.bh.realtrack.dto.InspectionConfiguratorDTO;
import com.bh.realtrack.dto.InspectionExecutionDetailsDTO;
import com.bh.realtrack.dto.InspectionExecutionLogDetailsDTO;
import com.bh.realtrack.dto.InspectionFileUploadDTO;
import com.bh.realtrack.dto.InspectionLookAheadDTO;
import com.bh.realtrack.dto.InspectionLookAheadLogDTO;
import com.bh.realtrack.dto.InspectionStatusDTO;
import com.bh.realtrack.dto.InspectionStatusLogDetailsDTO;
import com.bh.realtrack.dto.InspectionTestPlanDTO;
import com.bh.realtrack.dto.InspectionTestPlanLogDetailsDTO;
import com.bh.realtrack.dto.KeyValueDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.MCCChartDetailsDTO;
import com.bh.realtrack.dto.MCCChartDetailsXAxisDTO;
import com.bh.realtrack.dto.MCCChartDetailsYAxisDTO;
import com.bh.realtrack.dto.MCCCheckApplicableDTO;
import com.bh.realtrack.dto.MCCDetailsDTO;
import com.bh.realtrack.dto.MCCDropdownResponseDTO;
import com.bh.realtrack.dto.MCCSummaryDTO;
import com.bh.realtrack.dto.StatusUtilDTO;
import com.bh.realtrack.dto.UpdateDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.excel.ExportInspectionTestPlanToExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.pdf.ExportIAPToPdf;
import com.bh.realtrack.service.IInspectionTestPlanService;
import com.bh.realtrack.service.helper.InspectionTestPlanServiceHelper;
import com.bh.realtrack.util.InspectionTestPlanConstants;

/**
 * @author Anand Kumar
 *
 */
@Service
public class InspectionTestPlanServiceImpl implements IInspectionTestPlanService {
	private static Logger log = LoggerFactory.getLogger(InspectionTestPlanServiceImpl.class.getName());

	DataFormatter dataFormatter = new DataFormatter();
	private IInspectionTestPlanDAO iInspectionTestPlanDAO;
	private CallerContext callerContext;

	@Autowired
	public InspectionTestPlanServiceImpl(IInspectionTestPlanDAO iInspectionTestPlanDAO, CallerContext callerContext) {
		this.iInspectionTestPlanDAO = iInspectionTestPlanDAO;
		this.callerContext = callerContext;
	}

	@Autowired
	private InspectionTestPlanServiceHelper inspectionTestPlanServiceHelper;

	/***
	 * This service method is used to fetch the Inspection status details total,
	 * executed and percentage of executed details for the given project ID and
	 * based on the user type
	 * 
	 */
	@Override
	public InspectionExecutionLogDetailsDTO getExecutionDetails(String projectId, String userType) {
		List<InspectionExecutionDetailsDTO> inspectionExecutionList = new ArrayList<InspectionExecutionDetailsDTO>();
		InspectionExecutionLogDetailsDTO inspectionExecutionDetailsDto = new InspectionExecutionLogDetailsDTO();
		InspectionConfiguratorDTO inspectionConfiguratorDTO = null;
		InspectionCommentsDTO inspectionCommentsDTO = null;

		/***
		 * If the user type is INTERNAL then fetch the plain value from the data base
		 * 
		 */
		if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			inspectionExecutionList = iInspectionTestPlanDAO.getInternalExecutionData(projectId);
		}

		/***
		 * IF the user type is EXTERNAL then apply the filter condition as defined in
		 * the CONFIGURATOR on the table data and fetch the result
		 * 
		 */
		else if (InspectionTestPlanConstants.USER_EXTERNAL.equalsIgnoreCase(userType)) {

			/***
			 * Fetch the inspection configuration details from the project id
			 */
			inspectionConfiguratorDTO = getInspectionConfiguratorDetails(projectId, false);

			/**
			 * Based on the fetched inspection configuration details apply the filter
			 * condition on the plain data and fetch the details
			 */
			inspectionExecutionList = iInspectionTestPlanDAO.getExternalExecutionData(projectId,
					inspectionConfiguratorDTO);
		}

		/***
		 * IF the user type is CUSTOMER then fetch the data from the CUSTOMER view
		 */
		else {
			inspectionExecutionList = iInspectionTestPlanDAO.getCustomerExecutionData(projectId);
		}

		/***
		 * For External and Customer the comments entered by the user when publish
		 * customer data needs to be fetched
		 * 
		 */
		if (!InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			inspectionCommentsDTO = iInspectionTestPlanDAO.fetchUserComments(projectId);
		}

		/***
		 * Fetch the user details who inserted the Inspection details
		 */
		StatusUtilDTO statusUtilDTO = iInspectionTestPlanDAO.getLogDetails(projectId);

		inspectionExecutionDetailsDto.setInspectionCommentsDTO(inspectionCommentsDTO);
		inspectionExecutionDetailsDto.setInspectionExecutionDetailsList(inspectionExecutionList);
		inspectionExecutionDetailsDto.setUserLogDetails(statusUtilDTO);

		return inspectionExecutionDetailsDto;
	}

	/***
	 * This service method is used to fetch the inspection status -- Customer
	 * Presence details
	 * 
	 */
	@Override
	public InspectionStatusLogDetailsDTO getInspectionStatus(String projectId, String userType) {
		List<InspectionStatusDTO> inspectionStatusList = new ArrayList<InspectionStatusDTO>();

		InspectionStatusLogDetailsDTO inspectionStatusLogDetailsDTO = new InspectionStatusLogDetailsDTO();

		InspectionConfiguratorDTO inspectionConfiguratorDTO = null;

		/***
		 * If the user type is INTERNAL then fetch the plain value from the data base
		 * 
		 */
		if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			inspectionStatusList = iInspectionTestPlanDAO.getInternalInspectionStatusData(projectId);
		}

		/***
		 * IF the user type is EXTERNAL then apply the filter condition as defined in
		 * the CONFIGURATOR on the table data and fetch the result
		 * 
		 */
		else if (InspectionTestPlanConstants.USER_EXTERNAL.equalsIgnoreCase(userType)) {

			/***
			 * Fetch the inspection configuration details from the project id
			 */
			inspectionConfiguratorDTO = getInspectionConfiguratorDetails(projectId, false);

			/**
			 * Based on the fetched inspection configuration details apply the filter
			 * condition on the plain data and fetch the details
			 */
			inspectionStatusList = iInspectionTestPlanDAO.getExternalInspectionStatusData(projectId,
					inspectionConfiguratorDTO);

		}
		/***
		 * IF the user type is CUSTOMER then fetch the data from customer table
		 * 
		 */
		else if (InspectionTestPlanConstants.USER_CUSTOMER.equalsIgnoreCase(userType)) {

			inspectionConfiguratorDTO = iInspectionTestPlanDAO.getInspectionConfiguratorDetails(projectId,
					InspectionTestPlanConstants.USER_CUSTOMER);

			inspectionStatusList = iInspectionTestPlanDAO.getCustomerInspectionStatusData(projectId,
					inspectionConfiguratorDTO);
		}

		StatusUtilDTO statusUtilDTO = iInspectionTestPlanDAO.getLogDetails(projectId);
		List<String> xAxisMasterDTOList = iInspectionTestPlanDAO.getInspectionXAxisMasterData();
		List<ChartAxisColorDTO> yAxisMasterDTOList = iInspectionTestPlanDAO.getInspectionYAxisMasterData();
		inspectionStatusLogDetailsDTO.setInspectionStatusDetailsList(inspectionStatusList);
		inspectionStatusLogDetailsDTO.setxAxisMasterDetails(xAxisMasterDTOList);
		inspectionStatusLogDetailsDTO.setyAxisMasterDetails(yAxisMasterDTOList);
		inspectionStatusLogDetailsDTO.setUserLogDetails(statusUtilDTO);
		return inspectionStatusLogDetailsDTO;
	}

	/***
	 * 
	 * This service method is used to fetch the calendar details for the given
	 * project id
	 */
	@Override
	public InspectionCalendarLogDetailsDTO getInspectionCalendarDetails(String projectId, String userType) {
		InspectionCalendarLogDetailsDTO calendarLogDetailsDTO = new InspectionCalendarLogDetailsDTO();
		List<InspectionCalendarDetailsDTO> calendarDetailsList = null;
		String defaultDate = null;

		InspectionConfiguratorDTO inspectionConfiguratorDTO = null;

		/***
		 * If the user type is INTERNAL then fetch the plain value from the data base
		 * 
		 */
		if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			calendarDetailsList = iInspectionTestPlanDAO.getInternalInspectionCalendarDetailsData(projectId);
			defaultDate = iInspectionTestPlanDAO.getInternalDefaultDate(projectId);
		}

		/***
		 * IF the user type is EXTERNAL then apply the filter condition as defined in
		 * the CONFIGURATOR on the table data and fetch the result
		 * 
		 */
		else if (InspectionTestPlanConstants.USER_EXTERNAL.equalsIgnoreCase(userType)) {

			/***
			 * Fetch the inspection configuration details from the project id
			 */
			inspectionConfiguratorDTO = getInspectionConfiguratorDetails(projectId, false);

			/**
			 * Based on the fetched inspection configuration details apply the filter
			 * condition on the plain data and fetch the details
			 */
			calendarDetailsList = iInspectionTestPlanDAO.getExternalInspectionCalendarDetailsData(projectId,
					inspectionConfiguratorDTO);
			defaultDate = iInspectionTestPlanDAO.getExternalDefaultDate(projectId, inspectionConfiguratorDTO);
		}

		StatusUtilDTO statusUtilDTO = iInspectionTestPlanDAO.getLogDetails(projectId);
		calendarLogDetailsDTO.setCalendarDetails(calendarDetailsList);
		calendarLogDetailsDTO.setDefaultDate(defaultDate);
		calendarLogDetailsDTO.setUserLogDetails(statusUtilDTO);
		return calendarLogDetailsDTO;
	}

	/***
	 * This service method is used to fetch the inspection look a head details
	 */
	@Override
	public InspectionLookAheadLogDTO getInspectionLookAheadDetails(String projectId, String userType) {
		List<InspectionLookAheadDTO> inspectionLookAheadList = new ArrayList<InspectionLookAheadDTO>();
		InspectionLookAheadLogDTO inspectionLookAheadLogDTO = new InspectionLookAheadLogDTO();

		InspectionConfiguratorDTO inspectionConfiguratorDTO = null;

		/***
		 * If the user type is INTERNAL then fetch the plain value from the data base
		 * 
		 */
		if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			inspectionLookAheadList = iInspectionTestPlanDAO.getInternalInspectionLookAheadData(projectId);
		}

		/***
		 * IF the user type is EXTERNAL then apply the filter condition as defined in
		 * the CONFIGURATOR on the table data and fetch the result
		 * 
		 */
		else if (InspectionTestPlanConstants.USER_EXTERNAL.equalsIgnoreCase(userType)) {

			/***
			 * Fetch the inspection configuration details from the project id
			 */
			inspectionConfiguratorDTO = getInspectionConfiguratorDetails(projectId, false);

			/**
			 * Based on the fetched inspection configuration details apply the filter
			 * condition on the plain data and fetch the details
			 */
			inspectionLookAheadList = iInspectionTestPlanDAO.getExternalInspectionLookAheadData(projectId,
					inspectionConfiguratorDTO);
		}

		/***
		 * IF the user type is CUSTOMER then fetch the data from the CUSTOMER view
		 */
		else {
			inspectionConfiguratorDTO = iInspectionTestPlanDAO.getInspectionConfiguratorDetails(projectId,
					InspectionTestPlanConstants.USER_CUSTOMER);
			inspectionLookAheadList = iInspectionTestPlanDAO.getCustomerInspectionLookAheadData(projectId,
					inspectionConfiguratorDTO);
		}

		StatusUtilDTO statusUtilDTO = iInspectionTestPlanDAO.getLogDetails(projectId);
		List<String> xAxisMasterDTOList = iInspectionTestPlanDAO.getLookAheadXAxisMasterData();
		List<ChartAxisColorDTO> yAxisMasterDTOList = iInspectionTestPlanDAO.getLookAheadYAxisMasterData();
		inspectionLookAheadLogDTO.setInspectionLookAheadDetailsList(inspectionLookAheadList);
		inspectionLookAheadLogDTO.setxAxisMasterDetails(xAxisMasterDTOList);
		inspectionLookAheadLogDTO.setyAxisMasterDetails(yAxisMasterDTOList);
		inspectionLookAheadLogDTO.setUserLogDetails(statusUtilDTO);
		return inspectionLookAheadLogDTO;
	}

	/***
	 * This service method is used to fetch the Inspection test plan details for the
	 * INTERNAL or EXTERNAL user based on the user type
	 * 
	 */
	@Override
	public InspectionTestPlanLogDetailsDTO getInspectionTestPlanDetails(InspectionTestPlanDTO inspectionTestPlanDTO,
			String userType) {

		List<InspectionTestPlanDTO> inspectionTestPlanList = new ArrayList<InspectionTestPlanDTO>();
		InspectionTestPlanLogDetailsDTO logDetailsDTO = new InspectionTestPlanLogDetailsDTO();
		InspectionConfiguratorDTO inspectionConfiguratorDTO = null;

		/***
		 * If the user type is INTERNAL then fetch the plain value from the data base
		 * 
		 */
		if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			inspectionTestPlanList = iInspectionTestPlanDAO.getInternalInspectionTestPlanData(inspectionTestPlanDTO);
		}

		/***
		 * IF the user type is EXTERNAL then apply the filter condition as defined in
		 * the CONFIGURATOR on the table data and fetch the result
		 * 
		 */
		else if (InspectionTestPlanConstants.USER_EXTERNAL.equalsIgnoreCase(userType)) {

			/***
			 * Fetch the inspection configuration details from the project id
			 */
			inspectionConfiguratorDTO = getInspectionConfiguratorDetails(inspectionTestPlanDTO.getProjectId(), false);

			/**
			 * Based on the fetched inspection configuration details apply the filter
			 * condition on the plain data and fetch the details
			 */
			inspectionTestPlanList = iInspectionTestPlanDAO.getExternalInspectionTestPlanData(inspectionTestPlanDTO,
					inspectionConfiguratorDTO);
		}

		StatusUtilDTO statusUtilDTO = iInspectionTestPlanDAO.getLogDetails(inspectionTestPlanDTO.getProjectId());
		logDetailsDTO.setInspectionTestPlanList(inspectionTestPlanList);
		logDetailsDTO.setUserLogDetails(statusUtilDTO);

		return logDetailsDTO;
	}

	/***
	 * This service method is used to fetch the Inspection test plan details for the
	 * CUSTOMER
	 * 
	 */
	@Override
	public InspectionTestPlanLogDetailsDTO getCustomerInspectionTestPlanDetails(
			InspectionTestPlanDTO inspectionTestPlanDTO) {

		List<CustomerInspectionTestPlanDTO> inspectionTestPlanList = new ArrayList<CustomerInspectionTestPlanDTO>();
		InspectionTestPlanLogDetailsDTO logDetailsDTO = new InspectionTestPlanLogDetailsDTO();
		InspectionConfiguratorDTO inspectionConfiguratorDTO = iInspectionTestPlanDAO.getInspectionConfiguratorDetails(
				inspectionTestPlanDTO.getProjectId(), InspectionTestPlanConstants.USER_CUSTOMER);
		inspectionTestPlanList = iInspectionTestPlanDAO.getCustomerInspectionTestPlanData(inspectionTestPlanDTO,
				inspectionConfiguratorDTO);

		StatusUtilDTO statusUtilDTO = iInspectionTestPlanDAO.getLogDetails(inspectionTestPlanDTO.getProjectId());
		logDetailsDTO.setCustomerInspectionTestPlanList(inspectionTestPlanList);
		logDetailsDTO.setUserLogDetails(statusUtilDTO);

		return logDetailsDTO;
	}

	@Override
	public String publishCustomerData(String projectId, String pqmComments) {

		String publishMessage = null;
		boolean deleteStatus = false;
		InspectionCommentsDTO inspectionCommentsDTO = null;
		InspectionConfiguratorDTO inspectionConfiguratorDTO = null;

		if (null == pqmComments || InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(pqmComments)) {
			publishMessage = "Comments is Required";
		} else {
			inspectionCommentsDTO = new InspectionCommentsDTO();
			inspectionCommentsDTO.setProjectId(projectId);
			inspectionCommentsDTO.setPqmComments(pqmComments);
			inspectionCommentsDTO.setSsoId(callerContext.getName());

			iInspectionTestPlanDAO.saveUserComments(inspectionCommentsDTO);
			deleteStatus = iInspectionTestPlanDAO.deleteCustomerData(projectId);
			if (deleteStatus) {
				inspectionConfiguratorDTO = getInspectionConfiguratorDetails(projectId, false);
				publishMessage = iInspectionTestPlanDAO.publishCustomerData(projectId, inspectionConfiguratorDTO);

				inspectionConfiguratorDTO.setCustomerDataFilter(null);
				inspectionConfiguratorDTO.setTpDataFilter(null);
				inspectionConfiguratorDTO.setEndUserDataFilter(null);
				inspectionConfiguratorDTO.setQcpPageNameFilter(null);
				inspectionTestPlanServiceHelper.populateUserSelectedConfigurationValue(inspectionConfiguratorDTO);
				iInspectionTestPlanDAO.updateInspectionConfiguratorDetails(inspectionConfiguratorDTO,
						InspectionTestPlanConstants.USER_CUSTOMER);
			}
		}
		triggerQIRadar(projectId);
		return publishMessage;
	}

	/***
	 * 
	 * This method is used to fetch the Inspection Configuration Details from the
	 * table rt_app.rt_cat_iap_config_update_control for the given project id
	 * 
	 */
	@Override
	public InspectionConfiguratorDTO getInspectionConfiguratorDetails(String projectId, boolean fetchMasterValue) {

		/***
		 * Fetch the inspection configuration details for the project id from the data
		 * base
		 */
		InspectionConfiguratorDTO inspectionConfiguratorDTO = iInspectionTestPlanDAO
				.getInspectionConfiguratorDetails(projectId, InspectionTestPlanConstants.USER_EXTERNAL);

		InspectionConfiguratorDTO customerIAPConfig = null;

		/***
		 * If the inspection configuration details are not present in the data base then
		 * populate the static default values and persist it in the data base
		 */
		if (null == inspectionConfiguratorDTO) {
			customerIAPConfig = new InspectionConfiguratorDTO();
			inspectionConfiguratorDTO = inspectionTestPlanServiceHelper.populateConfigurationDefaultValue(projectId);
			iInspectionTestPlanDAO.insertsInspectionConfiguratorDetails(inspectionConfiguratorDTO,
					InspectionTestPlanConstants.USER_EXTERNAL);

			customerIAPConfig.setProjectId(projectId);
			customerIAPConfig.setPeriodFilter("90");
			iInspectionTestPlanDAO.insertsInspectionConfiguratorDetails(customerIAPConfig,
					InspectionTestPlanConstants.USER_CUSTOMER);
		}

		if (fetchMasterValue) {
			inspectionTestPlanServiceHelper.fetchInspectionConfigurationMasterData(inspectionConfiguratorDTO);
			iInspectionTestPlanDAO.fetchQCPPageNameMasterData(inspectionConfiguratorDTO);
			inspectionTestPlanServiceHelper.populateSelectedConfigurationValue(inspectionConfiguratorDTO);
		}

		return inspectionConfiguratorDTO;
	}

	/***
	 * 
	 * This method is used to update the Inspection Configuration details edited by
	 * the user to the table rt_app.rt_cat_iap_config_update_control for the given
	 * project id
	 * 
	 */
	@Override
	public InspectionConfiguratorDTO updateInspectionConfiguratorDetails(
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {
		boolean valid = inspectionTestPlanServiceHelper
				.populateUserSelectedConfigurationValue(inspectionConfiguratorDTO);

		if (valid) {
			iInspectionTestPlanDAO.updateInspectionConfiguratorDetails(inspectionConfiguratorDTO,
					InspectionTestPlanConstants.USER_EXTERNAL);
		}

		return inspectionConfiguratorDTO;
	}

	/***
	 * This service method is used to fetch the inspection test plan details and
	 * frame the the details in Excel format for the Internal and External user
	 * 
	 */
	@SuppressWarnings("resource")
	@Override
	public String downloadIAPData(String projectId, String userType) {

		InspectionConfiguratorDTO inspectionConfiguratorDTO = null;
		String iapData = null;

		/***
		 * If the user type is INTERNAL then fetch the plain value from the data base
		 * 
		 */
		if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
			iapData = iInspectionTestPlanDAO.downloadInternalIAPCSVData(projectId);
		}

		/***
		 * IF the user type is EXTERNAL then apply the filter condition as defined in
		 * the CONFIGURATOR on the table data and fetch the result
		 * 
		 */
		else if (InspectionTestPlanConstants.USER_EXTERNAL.equalsIgnoreCase(userType)) {

			/***
			 * Fetch the inspection configuration details from the project id
			 */
			inspectionConfiguratorDTO = getInspectionConfiguratorDetails(projectId, false);

			/**
			 * Based on the fetched inspection configuration details apply the filter
			 * condition on the plain data and fetch the details
			 */
			iapData = iInspectionTestPlanDAO.downloadExternalInspectionTestPlanDataCSV(projectId,
					inspectionConfiguratorDTO);
		}

		return iapData;

	}

	/***
	 * This service method is used to fetch the inspection test plan details and
	 * frame the the details in Excel format for the customer
	 * 
	 */
	@SuppressWarnings("resource")
	@Override
	public String downloadCustomerInspectionTestPlanExcel(String projectId) {

		String testPlanDetails = iInspectionTestPlanDAO.downloadCustomerInspectionTestPlanDataCSV(projectId);

		return testPlanDetails;
	}

	/***
	 * This service method is used to fetch the inspection test plan details from
	 * the uploaded EXCEL file and save the details in to stage table.
	 * 
	 * Once the stage table is populated, the details will be processed and save in
	 * to customer view
	 * 
	 */
	@SuppressWarnings("resource")
	@Override
	public Map<String, String> importInspectionTestPlanExcel(String projectId, MultipartFile excelFile,
			String comments) {

		String ssoId = callerContext.getName();
		String fileName = null;
		Map<Long, String> columnNameMap = null;
		StringBuilder validationMessage = new StringBuilder();
		boolean validationStatus = true;
		Map<String, String> fileUploadStatusMap = new HashMap<String, String>();
		InspectionFileUploadDTO inspectionFileUploadDTO = new InspectionFileUploadDTO();
		Integer trackId = null;
		byte[] bytes = null;
		String iapData = null;
		String[] rowData = null;
		String[] excelHeader = null;
		try {
			fileName = excelFile.getOriginalFilename();
			if (null == comments || InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(comments)) {
				validationMessage.append("Please enter the comments");
				validationStatus = false;
				fileUploadStatusMap.put("error", validationMessage.toString());
			}

			/***
			 * Check whether already an file upload is in-progress
			 * 
			 */
			if (validationStatus && checkInprogressFileUpload(projectId)) {
				validationMessage.append("Already an upload in progress for this project");
				validationStatus = false;
				fileUploadStatusMap.put("error", validationMessage.toString());
			}
			if (validationStatus) {
				bytes = excelFile.getBytes();

				iapData = new String(bytes);

				rowData = iapData.split("\n");

				/***
				 * Check whether the uploaded file is having header and columns and whether the
				 * header columns are present in the specified order
				 * 
				 */
				if (rowData.length < 2) {
					validationMessage.append("The upload file has  no content");
					validationStatus = false;
				}

				if (validationStatus) {
					excelHeader = rowData[0].split(",");

					if (excelHeader.length <= 1) {
						validationMessage.append("The uploaded file has no header");
						validationStatus = false;
					}
				}

				if (validationStatus) {
					columnNameMap = iInspectionTestPlanDAO.fetchIAPExcelColumnNameList();
					validationStatus = inspectionTestPlanServiceHelper.validateExcelColumnDetails(columnNameMap,
							rowData[0], validationMessage);
				}

				if (!validationStatus && projectId != null && !projectId.isEmpty() && !projectId.equalsIgnoreCase("")) {
					trackId = iInspectionTestPlanDAO.insertCustomerDataTrackingDetails(projectId, ssoId, null,
							fileName);
					inspectionFileUploadDTO.setProjectId(projectId);
					inspectionFileUploadDTO.setErrorMessage(validationMessage.toString());
					inspectionFileUploadDTO.setTrackId(trackId);
					iInspectionTestPlanDAO.saveFileUploadErrorDetails(inspectionFileUploadDTO);
				}
			}

		} catch (IOException e) {
			log.error("something went wrong while uploading inspection test plan excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		}

		if (validationStatus && projectId != null && !projectId.isEmpty() && !projectId.equalsIgnoreCase("")) {
			trackId = iInspectionTestPlanDAO.insertCustomerDataTrackingDetails(projectId, ssoId, comments, fileName);
			processExcelFileUpload(projectId, iapData, ssoId, trackId);
		}
		return fileUploadStatusMap;

	}

	public boolean checkInprogressFileUpload(String projectId) {
		Map<String, Integer> fileUploadStatus = iInspectionTestPlanDAO.getFileUploadStatus(projectId);
		boolean inProgressFileUpload = false;
		if (fileUploadStatus.get("status") != 0) {
			inProgressFileUpload = true;
		}
		return inProgressFileUpload;
	}

	public void processExcelFileUpload(String projectId, String excelData, String ssoId, Integer trackId) {
		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				InspectionFileUploadDTO inspectionFileUploadDTO = new InspectionFileUploadDTO();
				try {
					boolean sucessfullUpload = true;
					InspectionConfiguratorDTO customerConfiguration = new InspectionConfiguratorDTO();

					iInspectionTestPlanDAO.deleteFileUploadStageData(projectId);
					boolean specialCharacterPresent = iInspectionTestPlanDAO.saveExcelDataIntoStg(projectId, excelData,
							ssoId);

					if (!specialCharacterPresent) {
						Map<String, String> fileProcessResponse = iInspectionTestPlanDAO
								.processExcelUploadedData(projectId, trackId);

						if (null != fileProcessResponse && null != fileProcessResponse.get("status")
								&& InspectionTestPlanConstants.ERROR
										.equalsIgnoreCase(fileProcessResponse.get("status"))) {
							sucessfullUpload = false;
						}

						if (sucessfullUpload) {
							customerConfiguration.setProjectId(projectId);
							customerConfiguration.setPeriodFilter(null);
							iInspectionTestPlanDAO.updateInspectionConfiguratorDetails(customerConfiguration,
									InspectionTestPlanConstants.USER_CUSTOMER);
						}
					} else {
						inspectionFileUploadDTO.setProjectId(projectId);
						inspectionFileUploadDTO.setErrorMessage(
								"Invalid characters present in the upload file. Applicable characters are a-zA-Z0-9_;:$%./- ");
						inspectionFileUploadDTO.setTrackId(trackId);
						iInspectionTestPlanDAO.saveFileUploadErrorDetails(inspectionFileUploadDTO);
					}

				} catch (Exception exception) {
					inspectionFileUploadDTO.setProjectId(projectId);
					inspectionFileUploadDTO.setErrorMessage("Error occured when processing the file upload process");
					inspectionFileUploadDTO.setTrackId(trackId);
					iInspectionTestPlanDAO.saveFileUploadErrorDetails(inspectionFileUploadDTO);
					log.error("Error occured when processing the file upload process" + exception.getStackTrace());
				}

			}
		};
		new Thread(caller1).start();
	}

	@Override
	public Map<String, String> importIAPExcel(String projectId, MultipartFile excelFile, String comments) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Map<Long, String> headerIndexMap = new HashMap<Long, String>();
		InspectionFileUploadDTO statusDTO = new InspectionFileUploadDTO();
		boolean validationStatus = true;
		String sso = "", fileName = "";
		StringBuilder validationMessage = new StringBuilder();
		Integer trackId = null;
		try {

			sso = callerContext.getName();

			if (null == comments || InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(comments)) {
				validationMessage.append("Please enter the comments");
				validationStatus = false;
				responseMap.put("error", validationMessage.toString());
			}

			if (excelFile != null && !excelFile.isEmpty()) {

				fileName = excelFile.getOriginalFilename();

				// Check whether already an file upload is in-progress
				if (validationStatus && checkInprogressFileUpload(projectId)) {
					validationMessage.append("Already an upload in progress for this project");
					validationStatus = false;
					responseMap.put("error", validationMessage.toString());
				}

				if (validationStatus) {
					try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());) {

						headerIndexMap = iInspectionTestPlanDAO.fetchIAPExcelColumnNameList();

						// validate excel
						validationStatus = validateIAPExcelColumnDetails(projectId, workbook, headerIndexMap,
								validationMessage);

						// on validation failure, update error
						if (!validationStatus && projectId != null && !projectId.isEmpty()
								&& !projectId.equalsIgnoreCase("")) {
							trackId = iInspectionTestPlanDAO.insertCustomerDataTrackingDetails(projectId, sso, null,
									fileName);
							statusDTO.setProjectId(projectId);
							statusDTO.setErrorMessage(validationMessage.toString());
							statusDTO.setTrackId(trackId);
							iInspectionTestPlanDAO.saveFileUploadErrorDetails(statusDTO);
						}

						// on validation success, process file
						if (validationStatus && projectId != null && !projectId.isEmpty()
								&& !projectId.equalsIgnoreCase("")) {
							trackId = iInspectionTestPlanDAO.insertCustomerDataTrackingDetails(projectId, sso, comments,
									fileName);
							statusDTO.setTrackId(trackId);
							processIAPExcelFile(projectId, workbook, sso, headerIndexMap, statusDTO);
						}
					}
				}
			}
		} catch (IOException e) {
			log.error("something went wrong while uploading inspection test plan excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		}
		return responseMap;
	}

	private boolean validateIAPExcelColumnDetails(String projectId, Workbook workbook, Map<Long, String> headerIndexMap,
			StringBuilder validationMessage) {
		boolean validationStatus = true;
		boolean headerColumnsErrorFlag = false;
		StringBuilder errorHeaderColumns = new StringBuilder();
		Long headerMapColumnIx;
		short minColIx = 0, maxColIx = 0, colIx = 0;
		Row headerRow = null;
		try {
			log.info("Validating IAP Excel Column Details for " + projectId);

			Sheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			log.info("totalRows :: " + totalRows);

			// check if the excel contains at least one row with header
			if (validationStatus) {
				Row dataRow = sheet.getRow(0);
				if (isRowEmpty(dataRow)) {
					validationMessage.append("The upload file has no headers");
					validationStatus = false;
				}
			}

			// check if the excel contains at least one row with data
			if (validationStatus) {
				if (totalRows < 2) {
					validationMessage.append("The upload file has no content");
					validationStatus = false;
				}
			}

			// check if the excel contains correct no of columns
			if (validationStatus) {
				headerRow = sheet.getRow(0);
				maxColIx = headerRow.getLastCellNum();
				log.info("headerIndexMap Size :: " + headerIndexMap.size());
				log.info("maxColIx :: " + maxColIx);
				if (headerIndexMap.size() < 38 || headerIndexMap.size() > 39) {
					validationMessage.append("Incorrect number of columns are present in excel file");
					validationStatus = false;
				}
			}

			// check if the excel contains header with correct column names and order
			if (validationStatus) {
				headerRow = sheet.getRow(0);
				minColIx = headerRow.getFirstCellNum();
				maxColIx = 38;
				log.info("minColIx :: " + minColIx);
				if (minColIx >= 0 && maxColIx != 0 && colIx >= 0) {
					for (colIx = minColIx; colIx < maxColIx; colIx++) {
						Cell cell = headerRow.getCell(colIx);
						String headerCellValue = null != cell.getStringCellValue() ? cell.getStringCellValue() : "";
						headerMapColumnIx = Long.valueOf(colIx) + 1;
						String headerMapValue = headerIndexMap.get(headerMapColumnIx);
						if (!headerMapValue.equalsIgnoreCase(headerCellValue)) {
							validationStatus = false;
							if (headerColumnsErrorFlag) {
								errorHeaderColumns.append(", ");
							}
							errorHeaderColumns.append(headerMapValue);
							headerColumnsErrorFlag = true;
						}
					}
				}
				if (headerColumnsErrorFlag) {
					validationMessage.append(
							"Column(s) " + errorHeaderColumns.toString() + " are either misplaced or misspelled");
				}
			}

			log.info("validationStatus :: " + validationStatus);
			log.info("validationMessage :: " + validationMessage);

		} catch (Exception e) {
			log.error("Error while validating excel column details :: " + e.getMessage());
		}
		return validationStatus;
	}

	private void processIAPExcelFile(String projectId, XSSFWorkbook workbook, String sso,
			Map<Long, String> headerIndexMap, InspectionFileUploadDTO statusDTO) {
		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				List<InspectionTestPlanDTO> list = new ArrayList<InspectionTestPlanDTO>();
				InspectionConfiguratorDTO customerConfiguration = new InspectionConfiguratorDTO();
				boolean validationStatus = true, successfullUpload = true, stageFlag = false;
				try {
					int trackId = statusDTO.getTrackId();
					if (validationStatus) {
						list = readIAPDetailsExcelFile(projectId, workbook, headerIndexMap, statusDTO);
						if (!list.isEmpty()) {
							iInspectionTestPlanDAO.deleteFileUploadStageData(projectId);
							stageFlag = iInspectionTestPlanDAO.insertIAPDataStageData(projectId, list, sso);
							if (stageFlag) {
								Map<String, String> fileProcessResponse = iInspectionTestPlanDAO
										.processExcelUploadedData(projectId, trackId);

								if (null != fileProcessResponse && null != fileProcessResponse.get("status")
										&& InspectionTestPlanConstants.ERROR
												.equalsIgnoreCase(fileProcessResponse.get("status"))) {
									successfullUpload = false;
									statusDTO.setErrorMessage("Error while inserting data into target table");
									iInspectionTestPlanDAO.saveFileUploadErrorDetails(statusDTO);
								}

								if (successfullUpload) {
									customerConfiguration.setProjectId(projectId);
									customerConfiguration.setPeriodFilter(null);
									iInspectionTestPlanDAO.updateInspectionConfiguratorDetails(customerConfiguration,
											InspectionTestPlanConstants.USER_CUSTOMER);
								}
							} else {
								statusDTO.setProjectId(projectId);
								statusDTO.setErrorMessage("Error while inserting data into stage table");
								statusDTO.setTrackId(trackId);
								iInspectionTestPlanDAO.saveFileUploadErrorDetails(statusDTO);
							}

						} else {
							statusDTO.setProjectId(projectId);
							statusDTO
									.setErrorMessage("Error while reading data from excel file. Excel has no content.");
							statusDTO.setTrackId(trackId);
							iInspectionTestPlanDAO.saveFileUploadErrorDetails(statusDTO);
						}
					}
				} catch (Exception e) {
					log.error("Error while processing IAP Details excel file :: " + e.getMessage());
					statusDTO.setErrorMessage("Error occured when processing the file upload process");
					iInspectionTestPlanDAO.saveFileUploadErrorDetails(statusDTO);
				}
			}
		};
		new Thread(caller1).start();
	}

	private List<InspectionTestPlanDTO> readIAPDetailsExcelFile(String projectId, XSSFWorkbook workbook,
			Map<Long, String> headerIndexMap, InspectionFileUploadDTO statusDTO) {
		List<InspectionTestPlanDTO> list = new ArrayList<InspectionTestPlanDTO>();
		try {
			Sheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();

			for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
				InspectionTestPlanDTO dto = new InspectionTestPlanDTO();
				int colIx = 0;
				String cellValue = "";
				Cell cell = null;
				Row dataRow = sheet.getRow(rowNum);

				if (isRowEmpty(dataRow)) {
					continue;
				}

				dto.setProjectId(projectId);

				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setOmDescription(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setQcpPageName(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCostingProject(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setFunctionalUnit(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setFunctionalUnitDescription(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setPeiCode(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setLongDummyCode(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setItemCode(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setItemDescription(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setQcpDoc(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRevQCP(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRequirementId(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRequirementDescription(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setStatus(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRefDocs(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setProcedure(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setAcceptance(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setGe(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCustomer(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setEndUser(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setTp(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSupplier(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSupplierLocation(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSubSupplierLocation(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setPoWip(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setPoLine(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setExpectedInspectionStartDate(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCustNotificationNumber(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setNotificationNumber(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setNotificationStatus(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setNotificationRevision(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInspectionDate(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInspectionDuration(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInspectionType(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setNotificationToCustomer(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRc1Reference(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setTestResult(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInspectionNotes(cellValue);

				list.add(dto);
			}
			log.info("IAP Details list size :: " + list.size());
		} catch (Exception e) {
			log.error("Error while processing IAP Details excel file :: " + e.getMessage());
		}
		return list;
	}

	private static boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();
		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}

	private String getCellValueByType(Cell cell) {
		String cellValue = "";
		if (null != cell.getCellType()) {
			switch (cell.getCellType()) {
			case STRING:
				cellValue = null != cell.getStringCellValue() ? cell.getStringCellValue() : "";
				break;
			case NUMERIC:
				cellValue = String.valueOf((Object) cell.getNumericCellValue());
				if (DateUtil.isCellDateFormatted(cell)) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = cell.getDateCellValue();
					cellValue = df.format(date);
				}
				break;
			case BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case BLANK:
				cellValue = "";
				break;
			case ERROR:
				cellValue = "";
				break;
			case FORMULA:
				cellValue = "";
				break;
			case _NONE:
				cellValue = "";
				break;
			default:
				cellValue = null != cell.getStringCellValue() ? cell.getStringCellValue() : "";
				break;
			}
		}
		return cellValue;
	}

	@Override
	public UpdateDetailsDTO getUpdateDetails(String projectId) {
		UpdateDetailsDTO updateDetailsDTO = new UpdateDetailsDTO();
		List<LastSuccessfulUpdateDetailsDTO> lastSuccessfulUpdateDetailsDTO = iInspectionTestPlanDAO
				.getLastSuccessfulUpdateData(projectId);
		List<LastUpdateDetailsDTO> lastUpdateDetailsDTO = iInspectionTestPlanDAO.getLastUpdateData(projectId);
		List<ErrorDetailsDTO> errorDetails = iInspectionTestPlanDAO.getErrorDetailsData(projectId);
		updateDetailsDTO.setLastSuccessfulUpdateDetails(lastSuccessfulUpdateDetailsDTO);
		updateDetailsDTO.setLastUpdateDetails(lastUpdateDetailsDTO);
		updateDetailsDTO.setErrorDetails(errorDetails);
		return updateDetailsDTO;
	}

	@Override
	public InspectionConfiguratorDTO getCustomerInspectionConfiguration(String projectId) {
		InspectionConfiguratorDTO inspectionConfiguratorDTO = iInspectionTestPlanDAO
				.getInspectionConfiguratorDetails(projectId, InspectionTestPlanConstants.USER_CUSTOMER);

		return inspectionConfiguratorDTO;
	}

	private void triggerQIRadar(String projectId) {
		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				try {
					iInspectionTestPlanDAO.triggerQIRadar(projectId);
				} catch (Exception e) {
					log.error("Error occured when triggered QI radar" + e.getStackTrace());
				}
			}
		};
		new Thread(caller1).start();
	}

	@Override
	public ByteArrayOutputStream downloadCustomerInspectionTestPlanPdf(String projectId) {
		String projectName = null;
		String updatedDate = null;
		List<CustomerInspectionTestPlanDTO> list = iInspectionTestPlanDAO
				.downloadCustomerInspectionTestPlanDataPDF(projectId);
		projectName = iInspectionTestPlanDAO.fetchProjectName(projectId);
		InspectionCommentsDTO inspectionCommentsDTO = iInspectionTestPlanDAO.fetchUserComments(projectId);
		if (null != inspectionCommentsDTO) {
			updatedDate = inspectionCommentsDTO.getInsertedDate();
		}
		return ExportIAPToPdf.generateIAPReport(projectId, list, projectName, updatedDate);
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] downloadIAPExcel(String projectId, String userType) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ExportInspectionTestPlanToExcel exportIAPExcel = new ExportInspectionTestPlanToExcel();
		List<InspectionTestPlanDTO> list = new ArrayList<InspectionTestPlanDTO>();
		InspectionConfiguratorDTO inspectionConfiguratorDTO = null;
		XSSFWorkbook workbook = null;
		byte[] excelData = null;
		try {
			if (InspectionTestPlanConstants.USER_EXTERNAL.equalsIgnoreCase(userType)) {
				inspectionConfiguratorDTO = getInspectionConfiguratorDetails(projectId, false);
				list = iInspectionTestPlanDAO.downloadIAPExternalExcel(projectId, inspectionConfiguratorDTO);
			}
			if (InspectionTestPlanConstants.USER_INTERNAL.equalsIgnoreCase(userType)) {
				list = iInspectionTestPlanDAO.downloadIAPInternalExcel(projectId);
			}

			workbook = new XSSFWorkbook();
			log.info("Creating IAP Details Sheet with " + list.size() + " rows.");
			workbook = exportIAPExcel.exportInspectionTestPlanExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error while downloading IAP Details :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error while downloading IAP Details :: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public MCCDropdownResponseDTO getMCCDropDownDetails(String projectId) {
		MCCDropdownResponseDTO dropdownResponse = new MCCDropdownResponseDTO();
		List<KeyValueDTO> subProjectList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> disciplineList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> moduleList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> showByList = new ArrayList<KeyValueDTO>();
		String moduleDefault = "OVERALL", showByDefault = "OVERALL";
		try {
			subProjectList = iInspectionTestPlanDAO.getMCCSubProjectFilter(projectId);
			disciplineList = iInspectionTestPlanDAO.getMCCDisciplineFilter(projectId);
			moduleList = iInspectionTestPlanDAO.getMCCModuleFilter(projectId);
			showByList = iInspectionTestPlanDAO.getMCCShowByFilter(projectId);
			if (null != moduleList && moduleList.size() > 0) {
				moduleDefault = moduleList.get(0).getKey();
			}
	
				showByDefault ="Mechanical Completion";
			dropdownResponse.setSubProject(subProjectList);
			dropdownResponse.setDiscipline(disciplineList);
			dropdownResponse.setModule(moduleList);
			dropdownResponse.setShowByModule(moduleDefault);
			dropdownResponse.setShowBy(showByList);
			dropdownResponse.setShowByDefault(showByDefault);
		} catch (Exception e) {
			log.error("getMCCDropDownDetails(): Exception occurred : " + e.getMessage());
		}
		return dropdownResponse;
	}

	@Override
	public MCCSummaryDTO getMCCSummary(String projectId, String subProject, String discipline, String module,
			String showBy) {
		MCCSummaryDTO summaryDTO = new MCCSummaryDTO();
		try {
			summaryDTO = iInspectionTestPlanDAO.getMCCSummary(projectId, subProject, discipline, module, showBy);
		} catch (Exception e) {
			log.error("getMCCSummary(): Exception occurred : " + e.getMessage());
		}
		return summaryDTO;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] getMCCDetailsExcel(String projectId, String subProject, String discipline, String module,
			String showBy) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = null;
		ExportInspectionTestPlanToExcel exportMCCDetailsExcel = new ExportInspectionTestPlanToExcel();
		List<MCCDetailsDTO> excelDetails = new ArrayList<MCCDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new XSSFWorkbook();
			excelDetails = iInspectionTestPlanDAO.getMCCDetailsExcel(projectId, subProject, discipline, module, showBy);
			log.info("Creating Excel with " + excelDetails.size() + " rows for project id " + projectId);
			workbook = exportMCCDetailsExcel.exportMCCDetailsExcel(workbook, excelDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading MCC Details:: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading MCC Details:: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getMCCChartPopupData(String projectId, String subProject, String discipline,
			String module, String showBy, String type, String status, String subStatus) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<MCCDetailsDTO> popupDetails = new ArrayList<MCCDetailsDTO>();
		try {
			popupDetails = iInspectionTestPlanDAO.getMCCChartPopupData(projectId, subProject, discipline, module,
					showBy, type, status, subStatus);
			responseMap.put("popup", popupDetails);
		} catch (Exception e) {
			log.error("getMCCPopupDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public MCCCheckApplicableDTO checkMCCApplicable(String projectId) {
		MCCCheckApplicableDTO dto = new MCCCheckApplicableDTO();
		try {
			dto = iInspectionTestPlanDAO.checkMCCApplicable(projectId);
		} catch (Exception e) {
			log.error("checkMCCApplicable(): Exception occurred : " + e.getMessage());
		}
		return dto;
	}

	@Override
	public Map<String, Object> getMCCChartDetails(String projectId, String subProject, String discipline, String module,
			String showBy) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<MCCChartDetailsYAxisDTO> yChart = new ArrayList<MCCChartDetailsYAxisDTO>();
		List<MCCChartDetailsXAxisDTO> xChart = new ArrayList<MCCChartDetailsXAxisDTO>();
		Map<String, MCCChartDetailsDTO> chartData = new LinkedHashMap<String, MCCChartDetailsDTO>();
		String updateOnDate = "";
		try {
			yChart = iInspectionTestPlanDAO.getMCCChartYAxisDetails(projectId, subProject, discipline, module, showBy);
			xChart = iInspectionTestPlanDAO.getMCCChartXAxisDetails(projectId, subProject, discipline, module, showBy);
			chartData = iInspectionTestPlanDAO.getMCCChartDataDetails(projectId, subProject, discipline, module,
					showBy);
			updateOnDate = iInspectionTestPlanDAO.getLastUpdatedDate(projectId);
			responseMap.put("yAxisConfig", yChart);
			responseMap.put("xAxisConfig", xChart);
			responseMap.put("chartData", chartData);
			responseMap.put("lastUpdatedOn", updateOnDate);
		} catch (Exception e) {
			log.error("getMCCChartDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

}