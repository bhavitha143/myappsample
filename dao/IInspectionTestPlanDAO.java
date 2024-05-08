/**
 * 
 */
package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ChartAxisColorDTO;
import com.bh.realtrack.dto.CustomerInspectionTestPlanDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.InspectionCalendarDetailsDTO;
import com.bh.realtrack.dto.InspectionCommentsDTO;
import com.bh.realtrack.dto.InspectionConfiguratorDTO;
import com.bh.realtrack.dto.InspectionExecutionDetailsDTO;
import com.bh.realtrack.dto.InspectionFileUploadDTO;
import com.bh.realtrack.dto.InspectionLookAheadDTO;
import com.bh.realtrack.dto.InspectionStatusDTO;
import com.bh.realtrack.dto.InspectionTestPlanDTO;
import com.bh.realtrack.dto.KeyValueDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.MCCChartDetailsDTO;
import com.bh.realtrack.dto.MCCChartDetailsXAxisDTO;
import com.bh.realtrack.dto.MCCChartDetailsYAxisDTO;
import com.bh.realtrack.dto.MCCCheckApplicableDTO;
import com.bh.realtrack.dto.MCCDetailsDTO;
import com.bh.realtrack.dto.MCCSummaryDTO;
import com.bh.realtrack.dto.StatusUtilDTO;

/**
 * @author Anand Kumar
 *
 */
public interface IInspectionTestPlanDAO {

	/***
	 * 
	 * This DAO method is used to fetch the Inspection execution details from the
	 * rt_app.rt_cat_itp_report_data table for the given project id
	 * 
	 * The output will contain total count, execution count and percentage of
	 * execution count
	 * 
	 */
	List<InspectionExecutionDetailsDTO> getInternalExecutionData(String projectId);

	/**
	 * 
	 * This DAO method is used to fetch the inspection execution status based on the
	 * filter condition as defined in the configuration. The PERIOD filter will not
	 * be applied for fetching the Inspection execution details
	 * 
	 */
	List<InspectionExecutionDetailsDTO> getExternalExecutionData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	/***
	 * This DAO method is used to fetch the inspection execution details from the
	 * customer view table
	 */
	List<InspectionExecutionDetailsDTO> getCustomerExecutionData(String projectId);

	/****
	 * This DAO method is used to fetch the inspection status customer presence
	 * details based on the given project id
	 **/
	List<InspectionStatusDTO> getInternalInspectionStatusData(String projectId);

	/**
	 * 
	 * This DAO method is used to fetch the inspection status for the project id
	 * based on the filter condition defined in the inspection configuration
	 * 
	 */
	List<InspectionStatusDTO> getExternalInspectionStatusData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	List<InspectionStatusDTO> getCustomerInspectionStatusData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	/**
	 * This DAO method is used to fetch the customer presence -- inspection status
	 * chart's X-Axis details
	 */
	List<String> getInspectionXAxisMasterData();

	/**
	 * This DAO method is used to fetch the customer presence -- inspection status
	 * chart's Y-Axis details
	 */
	List<ChartAxisColorDTO> getInspectionYAxisMasterData();

	/***
	 * This DAO method is used to fetch the calendar details for the Internal user
	 * for the given project id
	 * 
	 */
	List<InspectionCalendarDetailsDTO> getInternalInspectionCalendarDetailsData(String projectId);

	/***
	 * This DAO method is used to fetch the calendar details for the External user
	 * for the given project id based on the filter condition defined in the
	 * inspection configuration
	 * 
	 */
	List<InspectionCalendarDetailsDTO> getExternalInspectionCalendarDetailsData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	/***
	 * This DAO method is used to fetch the default date for the internal user based
	 * on the given project id
	 */
	String getInternalDefaultDate(String projectId);

	/***
	 * This DAO method is used to fetch the default date for the External user for
	 * the given project id based on the filter condition defined in the Inspection
	 * configuration
	 * 
	 */
	String getExternalDefaultDate(String projectId, InspectionConfiguratorDTO inspectionConfiguratorDTO);

	/***
	 * This DAO method is used to fetch the inspection look a head data for for the
	 * internal user for the given project id
	 * 
	 */
	List<InspectionLookAheadDTO> getInternalInspectionLookAheadData(String projectId);

	/***
	 * This DAO method is used to fetch the inspection look a head data for for the
	 * external user for the given project id based on the filter condition defined
	 * in the inspection configuration
	 * 
	 */
	List<InspectionLookAheadDTO> getExternalInspectionLookAheadData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	/***
	 * This DAO method is used to fetch the inspection look a head data for for the
	 * customer for the given project id
	 * 
	 */
	List<InspectionLookAheadDTO> getCustomerInspectionLookAheadData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	/***
	 * This DAO method is used to fetch the X-Axis Master data for the inspection
	 * look a head chart
	 * 
	 */
	List<String> getLookAheadXAxisMasterData();

	/***
	 * This DAO method is used to fetch the Y-Axis Master data for the inspection
	 * look a head chart
	 * 
	 */
	List<ChartAxisColorDTO> getLookAheadYAxisMasterData();

	/***
	 * This DAO method is used to fetch the inspection test plan details for the
	 * given project id for the internal user
	 * 
	 */
	List<InspectionTestPlanDTO> getInternalInspectionTestPlanData(InspectionTestPlanDTO inspectionTestPlanDTO);

	/**
	 * This DAO method is used to fetch the inspection test plan details for the
	 * external user for the given project id based on the filter condition defined
	 * in the inspection configuration
	 * 
	 */
	List<InspectionTestPlanDTO> getExternalInspectionTestPlanData(InspectionTestPlanDTO inspectionTestPlanDTO,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	/**
	 * This DAO method is used to fetch the inspection test plan details for the
	 * external user for the given project id based on the filter condition defined
	 * in the inspection configuration
	 * 
	 */
	List<CustomerInspectionTestPlanDTO> getCustomerInspectionTestPlanData(InspectionTestPlanDTO inspectionTestPlanDTO,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	StatusUtilDTO getLogDetails(String projectId);

	/***
	 * 
	 * This method is used to fetch the Inspection Configuration Details from the
	 * table rt_app.rt_cat_iap_config_update_control for the given project id
	 * 
	 */
	InspectionConfiguratorDTO getInspectionConfiguratorDetails(String projectId, String sourceType);

	/***
	 * 
	 * This method is used to update the Inspection Configuration details edited by
	 * the user to the table rt_app.rt_cat_iap_config_update_control for the given
	 * project id
	 * 
	 */
	int updateInspectionConfiguratorDetails(InspectionConfiguratorDTO inspectionConfiguratorDTO, String sourceType);

	InspectionConfiguratorDTO fetchQCPPageNameMasterData(InspectionConfiguratorDTO inspectionConfiguratorDTO);

	/***
	 * 
	 * This method is used to insert the default Inspection Configuration details in
	 * to the table rt_app.rt_cat_iap_config_update_control for the given project id
	 * 
	 */
	void insertsInspectionConfiguratorDetails(InspectionConfiguratorDTO inspectionConfiguratorDTO, String sourceType);

	List<LastSuccessfulUpdateDetailsDTO> getLastSuccessfulUpdateData(String projectId);

	List<LastUpdateDetailsDTO> getLastUpdateData(String projectId);

	List<ErrorDetailsDTO> getErrorDetailsData(String projectId);

	String publishCustomerData(String projectId, InspectionConfiguratorDTO inspectionConfiguratorDTO);

	boolean deleteCustomerData(String projectId);

	boolean deleteFileUploadStageData(String projectId);

	boolean saveExcelDataIntoStg(String projectId, String excelData, String ssoId);

	public Map<String, String> processExcelUploadedData(String projectId, long trackId);

	InspectionCommentsDTO saveUserComments(InspectionCommentsDTO inspectionCommentsDTO);

	InspectionCommentsDTO fetchUserComments(String projectId);

	Map<Long, String> fetchIAPExcelColumnNameList();

	Map<String, Integer> getFileUploadStatus(String projectId);

	Integer insertCustomerDataTrackingDetails(String projectId, String ssoId, String comments, String fileName);

	void saveFileUploadErrorDetails(InspectionFileUploadDTO inspectionFileUploadDTO);

	String downloadCustomerInspectionTestPlanDataCSV(String projectId);

	String downloadInternalIAPCSVData(String projectId);

	String downloadExternalInspectionTestPlanDataCSV(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	void triggerQIRadar(String projectId);

	List<CustomerInspectionTestPlanDTO> downloadCustomerInspectionTestPlanDataPDF(String projectId);

	List<InspectionTestPlanDTO> downloadIAPExternalExcel(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO);

	String fetchProjectName(String projectId);

	boolean insertIAPDataStageData(String projectId, List<InspectionTestPlanDTO> list, String sso);

	List<InspectionTestPlanDTO> downloadIAPInternalExcel(String projectId);

	List<KeyValueDTO> getMCCSubProjectFilter(String projectId);

	List<KeyValueDTO> getMCCDisciplineFilter(String projectId);

	List<KeyValueDTO> getMCCModuleFilter(String projectId);

	List<KeyValueDTO> getMCCShowByFilter(String projectId);

	MCCSummaryDTO getMCCSummary(String projectId, String subProject, String discipline, String module, String showBy);

	List<MCCDetailsDTO> getMCCChartPopupData(String projectId, String subProject, String discipline, String module,
			String showBy, String type, String status, String subStatus);

	MCCCheckApplicableDTO checkMCCApplicable(String projectId);

	List<MCCDetailsDTO> getMCCDetailsExcel(String projectId, String subProject, String discipline, String module,
			String showBy);

	List<MCCChartDetailsYAxisDTO> getMCCChartYAxisDetails(String projectId, String subProject, String discipline,
			String module, String showBy);

	List<MCCChartDetailsXAxisDTO> getMCCChartXAxisDetails(String projectId, String subProject, String discipline,
			String module, String showBy);

	Map<String, MCCChartDetailsDTO> getMCCChartDataDetails(String projectId, String subProject, String discipline,
			String module, String showBy);

	String getLastUpdatedDate(String projectId);

}
