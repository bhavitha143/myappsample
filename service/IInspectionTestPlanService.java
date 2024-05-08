/**
 * 
 */
package com.bh.realtrack.service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

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

/**
 * @author Anand Kumar
 *
 */
public interface IInspectionTestPlanService {

	InspectionTestPlanLogDetailsDTO getInspectionTestPlanDetails(InspectionTestPlanDTO inspectionTestPlanDTO,
			String userType);

	InspectionExecutionLogDetailsDTO getExecutionDetails(String projectId, String userType);

	InspectionStatusLogDetailsDTO getInspectionStatus(String projectId, String userType);

	InspectionLookAheadLogDTO getInspectionLookAheadDetails(String projectId, String userType);

	/***
	 * 
	 * This method is used to fetch the Inspection Configuration Details from the
	 * table rt_app.rt_cat_iap_config_update_control for the given project id
	 * 
	 */
	InspectionConfiguratorDTO getInspectionConfiguratorDetails(String projectId, boolean fetchMasterValue);

	/***
	 * 
	 * This method is used to update the Inspection Configuration details edited by
	 * the user to the table rt_app.rt_cat_iap_config_update_control for the given
	 * project id
	 * 
	 */
	InspectionConfiguratorDTO updateInspectionConfiguratorDetails(InspectionConfiguratorDTO inspectionConfiguratorDTO);

	Map<String, String> importInspectionTestPlanExcel(String projectId, MultipartFile excelFile, String comments);

	Map<String, String> importIAPExcel(String projectId, MultipartFile excelFile, String comments);

	UpdateDetailsDTO getUpdateDetails(String projectId);

	InspectionCalendarLogDetailsDTO getInspectionCalendarDetails(String projectId, String userType);

	InspectionTestPlanLogDetailsDTO getCustomerInspectionTestPlanDetails(InspectionTestPlanDTO inspectionTestPlanDTO);

	String publishCustomerData(String projectId, String pqmComments);

	InspectionConfiguratorDTO getCustomerInspectionConfiguration(String projectId);

	String downloadCustomerInspectionTestPlanExcel(String projectId);

	String downloadIAPData(String projectId, String userType);

	ByteArrayOutputStream downloadCustomerInspectionTestPlanPdf(String projectId);

	byte[] downloadIAPExcel(String projectId, String userType);

	MCCDropdownResponseDTO getMCCDropDownDetails(String projectId);

	MCCSummaryDTO getMCCSummary(String projectId, String subProject, String discipline, String module, String showBy);

	byte[] getMCCDetailsExcel(String projectId, String subProject, String discipline, String module, String showBy);

	Map<String, Object> getMCCChartPopupData(String projectId, String subProject, String discipline, String module,
			String showBy, String type, String status, String subStatus);

	MCCCheckApplicableDTO checkMCCApplicable(String projectId);

	Map<String, Object> getMCCChartDetails(String projectId, String subProject, String discipline, String module,
			String showBy);

}