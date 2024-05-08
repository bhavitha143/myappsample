package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ServerErrorException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IInspectionTestPlanDAO;
import com.bh.realtrack.dao.helper.InspectionTestPlanDAOHelper;
import com.bh.realtrack.dao.helper.MCCDAOHelper;
import com.bh.realtrack.dto.ChartAxisColorDTO;
import com.bh.realtrack.dto.CustomerInspectionTestPlanDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.InspectionCalendarDetailsDTO;
import com.bh.realtrack.dto.InspectionCommentsDTO;
import com.bh.realtrack.dto.InspectionConfigurationDropDownDTO;
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
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.InspectionTestPlanConstants;
import com.bh.realtrack.util.MCCConstants;
import com.bh.realtrack.util.MaterialManagementConstants;

/**
 * @author Anand Kumar
 *
 */
@Repository
public class InspectionTestPlanDAOImpl implements IInspectionTestPlanDAO {
	private static Logger log = LoggerFactory.getLogger(InspectionTestPlanDAOImpl.class.getName());
	DataFormatter dataFormatter = new DataFormatter();
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private InspectionTestPlanDAOHelper inspectionTestPlanDAOHelper;

	/***
	 * 
	 * This DAO method is used to fetch the Inspection execution details from the
	 * rt_app.rt_cat_itp_report_data table for the given project id
	 * 
	 * The output will contain total count, execution count and percentage of
	 * execution count
	 * 
	 */
	@Override
	public List<InspectionExecutionDetailsDTO> getInternalExecutionData(String projectId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_EXECUTION_DETAILS,
				new Object[] { projectId }, new ResultSetExtractor<List<InspectionExecutionDetailsDTO>>() {
					@Override
					public List<InspectionExecutionDetailsDTO> extractData(final ResultSet rs) {
						List<InspectionExecutionDetailsDTO> list = new ArrayList<InspectionExecutionDetailsDTO>();
						try {
							while (rs.next()) {
								InspectionExecutionDetailsDTO inspectionExecutionDetailsDTO = new InspectionExecutionDetailsDTO();
								inspectionExecutionDetailsDTO.setTotalCount(rs.getLong(1));
								inspectionExecutionDetailsDTO.setExecutionStatusCount(rs.getLong(2));
								inspectionExecutionDetailsDTO.setBhPresence(rs.getLong(3));
								inspectionExecutionDetailsDTO.setExecutionPercentage(rs.getDouble(4));
								list.add(inspectionExecutionDetailsDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection execution details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/**
	 * 
	 * This DAO method is used to fetch the inspection execution status based on the
	 * filter condition as defined in the configuration. The PERIOD filter will not
	 * be applied for fetching the Inspection execution details
	 * 
	 */
	@Override
	public List<InspectionExecutionDetailsDTO> getExternalExecutionData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionDetails = new StringBuilder(
				InspectionTestPlanConstants.GET_INSPECTION_EXECUTION_DETAILS_EXTERNAL_ALL);

		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionDetails, false, false);

		inspectionDetails.append(InspectionTestPlanConstants.GET_INSPECTION_EXECUTION_DETAILS_EXTERNAL_EXECUTED);
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionDetails, false, true);

		inspectionDetails
				.append(InspectionTestPlanConstants.GET_INSPECTION_EXECUTION_DETAILS_EXTERNAL_ALL_PARTIES_REVIEW);
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionDetails, false, true);

		inspectionDetails.append(InspectionTestPlanConstants.GET_INSPECTION_EXECUTION_DETAILS_EXTERNAL_WHERE_CLAUSE);
		queryData.add(projectId);

		return jdbcTemplate.query(inspectionDetails.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionExecutionDetailsDTO>>() {
					@Override
					public List<InspectionExecutionDetailsDTO> extractData(final ResultSet rs) {
						List<InspectionExecutionDetailsDTO> list = new ArrayList<InspectionExecutionDetailsDTO>();
						try {
							while (rs.next()) {
								InspectionExecutionDetailsDTO inspectionExecutionDetailsDTO = new InspectionExecutionDetailsDTO();
								inspectionExecutionDetailsDTO.setTotalCount(rs.getLong(1));
								inspectionExecutionDetailsDTO.setExecutionStatusCount(rs.getLong(2));
								inspectionExecutionDetailsDTO.setBhPresence(rs.getLong(3));
								inspectionExecutionDetailsDTO.setExecutionPercentage(rs.getDouble(4));
								list.add(inspectionExecutionDetailsDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection execution details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/***
	 * This DAO method is used to fetch the inspection execution details from the
	 * customer view table
	 */
	@Override
	public List<InspectionExecutionDetailsDTO> getCustomerExecutionData(String projectId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_EXECUTION_DETAILS_CUSTOMER,
				new Object[] { projectId }, new ResultSetExtractor<List<InspectionExecutionDetailsDTO>>() {
					@Override
					public List<InspectionExecutionDetailsDTO> extractData(final ResultSet rs) {
						List<InspectionExecutionDetailsDTO> list = new ArrayList<InspectionExecutionDetailsDTO>();
						try {
							while (rs.next()) {
								InspectionExecutionDetailsDTO inspectionExecutionDetailsDTO = new InspectionExecutionDetailsDTO();
								inspectionExecutionDetailsDTO.setTotalCount(rs.getLong(1));
								inspectionExecutionDetailsDTO.setExecutionStatusCount(rs.getLong(2));
								inspectionExecutionDetailsDTO.setBhPresence(rs.getLong(3));
								inspectionExecutionDetailsDTO.setExecutionPercentage(rs.getDouble(4));
								list.add(inspectionExecutionDetailsDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection execution details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/****
	 * This DAO method is used to fetch the inspection status customer presence
	 * details based on the given project id
	 **/
	@Override
	public List<InspectionStatusDTO> getInternalInspectionStatusData(String projectId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_STATUS, new Object[] { projectId },
				new ResultSetExtractor<List<InspectionStatusDTO>>() {
					@Override
					public List<InspectionStatusDTO> extractData(final ResultSet rs) {
						List<InspectionStatusDTO> list = new ArrayList<InspectionStatusDTO>();
						try {
							while (rs.next()) {
								InspectionStatusDTO inspectionStatusDTO = new InspectionStatusDTO();
								inspectionStatusDTO.setCustomer(rs.getString(1));
								inspectionStatusDTO.setRtiStatus(rs.getString(2));
								inspectionStatusDTO.setRtiStatusColor(rs.getString(3));
								inspectionStatusDTO.setStatusFlagCount(rs.getLong(4));
								list.add(inspectionStatusDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection status details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/**
	 * 
	 * This DAO method is used to fetch the inspection status for the project id
	 * based on the filter condition defined in the inspection configuration
	 * 
	 */
	@Override
	public List<InspectionStatusDTO> getExternalInspectionStatusData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionStatusDetails = new StringBuilder(
				InspectionTestPlanConstants.GET_EXTERNAL_INSPECTION_STATUS);

		queryData.add(projectId);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration
		 */
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionStatusDetails, true, true);

		inspectionStatusDetails.append(" group by customer_status,rti_status,rti_status_color order by 1 ");

		return jdbcTemplate.query(inspectionStatusDetails.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionStatusDTO>>() {
					@Override
					public List<InspectionStatusDTO> extractData(final ResultSet rs) {
						List<InspectionStatusDTO> list = new ArrayList<InspectionStatusDTO>();
						try {
							while (rs.next()) {
								InspectionStatusDTO inspectionStatusDTO = new InspectionStatusDTO();
								inspectionStatusDTO.setCustomer(rs.getString(1));
								inspectionStatusDTO.setRtiStatus(rs.getString(2));
								inspectionStatusDTO.setRtiStatusColor(rs.getString(3));
								inspectionStatusDTO.setStatusFlagCount(rs.getLong(4));
								list.add(inspectionStatusDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection status details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/****
	 * This DAO method is used to fetch the inspection status customer presence
	 * details based on the given project id
	 **/
	@Override
	public List<InspectionStatusDTO> getCustomerInspectionStatusData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionStatusDetails = new StringBuilder(
				InspectionTestPlanConstants.GET_CUSTOMER_INSPECTION_STATUS);
		queryData.add(projectId);

		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionStatusDetails, true, true);
		inspectionStatusDetails.append(" group by customer_status,rti_status,rti_status_color order by 1 ");

		return jdbcTemplate.query(inspectionStatusDetails.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionStatusDTO>>() {
					@Override
					public List<InspectionStatusDTO> extractData(final ResultSet rs) {
						List<InspectionStatusDTO> list = new ArrayList<InspectionStatusDTO>();
						try {
							while (rs.next()) {
								InspectionStatusDTO inspectionStatusDTO = new InspectionStatusDTO();
								inspectionStatusDTO.setCustomer(rs.getString(1));
								inspectionStatusDTO.setRtiStatus(rs.getString(2));
								inspectionStatusDTO.setRtiStatusColor(rs.getString(3));
								inspectionStatusDTO.setStatusFlagCount(rs.getLong(4));
								list.add(inspectionStatusDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection status details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/**
	 * This DAO method is used to fetch the customer presence -- inspection status
	 * chart's X-Axis details
	 */
	@Override
	public List<String> getInspectionXAxisMasterData() {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_STATUS_XAXIS_MASTER_DETAILS,
				new ResultSetExtractor<List<String>>() {

					@Override
					public List<String> extractData(ResultSet rs) {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								list.add(rs.getString(1));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection xaxis master details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}

				});
	}

	/**
	 * This DAO method is used to fetch the customer presence -- inspection status
	 * chart's Y-Axis details
	 */
	@Override
	public List<ChartAxisColorDTO> getInspectionYAxisMasterData() {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_STATUS_YAXIS_MASTER_DETAILS,
				new ResultSetExtractor<List<ChartAxisColorDTO>>() {
					@Override
					public List<ChartAxisColorDTO> extractData(ResultSet rs) {
						List<ChartAxisColorDTO> list = new ArrayList<ChartAxisColorDTO>();
						try {
							while (rs.next()) {
								ChartAxisColorDTO chartAxisColorDTO = new ChartAxisColorDTO();
								chartAxisColorDTO.setAxisName(rs.getString(1));
								chartAxisColorDTO.setAxisColor(rs.getString(2));
								list.add(chartAxisColorDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection yaxis master details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}

				});
	}

	/***
	 * This DAO method is used to fetch the calendar details for the Internal user
	 * for the given project id
	 * 
	 */
	@Override
	public List<InspectionCalendarDetailsDTO> getInternalInspectionCalendarDetailsData(String projectId) {
		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_CALENDAR_DETAILS,
				new Object[] { projectId }, new ResultSetExtractor<List<InspectionCalendarDetailsDTO>>() {
					@Override
					public List<InspectionCalendarDetailsDTO> extractData(final ResultSet rs) {
						List<InspectionCalendarDetailsDTO> list = new ArrayList<InspectionCalendarDetailsDTO>();
						try {
							while (rs.next()) {

								InspectionCalendarDetailsDTO calendarDetails = new InspectionCalendarDetailsDTO();
								calendarDetails.setCostingProject(rs.getString("costing_project"));
								calendarDetails.setItemCode(rs.getString("item_code"));
								calendarDetails.setItemDescription(rs.getString("item_description"));
								calendarDetails.setRequirementId(rs.getString("requirement_id"));
								calendarDetails.setRequirementDescription(rs.getString("requirement_description"));
								calendarDetails.setRtiStatus(rs.getString("rti_status"));
								calendarDetails.setSupplier(rs.getString("supplier"));
								calendarDetails.setExpInspectionStartDt(rs.getString("expected_inspection_start_dt"));
								calendarDetails.setNotificationNumber(rs.getString("notification_number"));
								calendarDetails.setFunctionalUnit(rs.getString("functional_unit"));
								calendarDetails.setCalendarCustColor(rs.getString("calender_cust_color"));
								calendarDetails.setCalendarSymbol(rs.getString("calender_symbol"));
								list.add(calendarDetails);
							}
						} catch (SQLException e) {
							log.error(
									"something went wrong while getting inspection calendar details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/***
	 * This DAO method is used to fetch the calendar details for the External user
	 * for the given project id based on the filter condition defined in the
	 * inspection configuration
	 * 
	 */
	@Override
	public List<InspectionCalendarDetailsDTO> getExternalInspectionCalendarDetailsData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder calendarDetails = new StringBuilder(
				InspectionTestPlanConstants.GET_EXTERNAL_INSPECTION_CALENDAR_DETAILS);

		queryData.add(projectId);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration
		 */
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				calendarDetails, true, true);

		return jdbcTemplate.query(calendarDetails.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionCalendarDetailsDTO>>() {
					@Override
					public List<InspectionCalendarDetailsDTO> extractData(final ResultSet rs) {
						List<InspectionCalendarDetailsDTO> list = new ArrayList<InspectionCalendarDetailsDTO>();
						try {
							while (rs.next()) {

								InspectionCalendarDetailsDTO calendarDetails = new InspectionCalendarDetailsDTO();
								calendarDetails.setCostingProject(rs.getString("costing_project"));
								calendarDetails.setItemCode(rs.getString("item_code"));
								calendarDetails.setItemDescription(rs.getString("item_description"));
								calendarDetails.setRequirementId(rs.getString("requirement_id"));
								calendarDetails.setRequirementDescription(rs.getString("requirement_description"));
								calendarDetails.setRtiStatus(rs.getString("rti_status"));
								calendarDetails.setSupplier(rs.getString("supplier"));
								calendarDetails.setExpInspectionStartDt(rs.getString("expected_inspection_start_dt"));
								calendarDetails.setNotificationNumber(rs.getString("notification_number"));
								calendarDetails.setFunctionalUnit(rs.getString("functional_unit"));
								calendarDetails.setCalendarCustColor(rs.getString("calender_cust_color"));
								calendarDetails.setCalendarSymbol(rs.getString("calender_symbol_ext"));
								list.add(calendarDetails);
							}
						} catch (SQLException e) {
							log.error(
									"something went wrong while getting inspection calendar details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/***
	 * This DAO method is used to fetch the default date for the internal user based
	 * on the given project id
	 */
	@Override
	public String getInternalDefaultDate(String projectId) {
		return jdbcTemplate.query(InspectionTestPlanConstants.GET_DEFAULT_DATE, new Object[] { projectId },
				new ResultSetExtractor<String>() {

					@Override
					public String extractData(ResultSet rs) {
						String defaultDate = "";
						try {
							while (rs.next()) {
								defaultDate = rs.getString(1);
							}

						} catch (SQLException e) {
							log.error("something went wrong while getting default date:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return defaultDate;
					}

				});
	}

	/***
	 * This DAO method is used to fetch the default date for the External user for
	 * the given project id based on the filter condition defined in the Inspection
	 * configuration
	 * 
	 */
	@Override
	public String getExternalDefaultDate(String projectId, InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder defaultDateQuery = new StringBuilder(InspectionTestPlanConstants.GET_EXTERNAL_DEFAULT_DATE);

		queryData.add(projectId);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration
		 */
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				defaultDateQuery, true, true);

		return jdbcTemplate.query(defaultDateQuery.toString(), queryData.toArray(), new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) {
				String defaultDate = "";
				try {
					while (rs.next()) {
						defaultDate = rs.getString(1);
					}

				} catch (SQLException e) {
					log.error("something went wrong while getting default date:" + e.getMessage());
					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}

				return defaultDate;
			}

		});
	}

	/***
	 * This DAO method is used to fetch the inspection look a head data for for the
	 * internal user for the given project id
	 * 
	 */
	@Override
	public List<InspectionLookAheadDTO> getInternalInspectionLookAheadData(String projectId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_LOOK_AHEAD_DETAILS,
				new Object[] { projectId }, new ResultSetExtractor<List<InspectionLookAheadDTO>>() {
					@Override
					public List<InspectionLookAheadDTO> extractData(final ResultSet rs) {
						List<InspectionLookAheadDTO> list = new ArrayList<InspectionLookAheadDTO>();
						try {
							while (rs.next()) {
								InspectionLookAheadDTO inspectionLookAheadDTO = new InspectionLookAheadDTO();
								inspectionLookAheadDTO.setxAxisColor(rs.getString(1));
								inspectionLookAheadDTO.setyAxisColor(rs.getString(2));
								inspectionLookAheadDTO.setyAxisStatus(rs.getString(3));
								inspectionLookAheadDTO.setStatusFlagCount(rs.getLong(4));
								list.add(inspectionLookAheadDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection look ahead details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/***
	 * This DAO method is used to fetch the inspection look a head data for for the
	 * external user for the given project id based on the filter condition defined
	 * in the inspection configuration
	 * 
	 */
	@Override
	public List<InspectionLookAheadDTO> getExternalInspectionLookAheadData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionLookQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_EXTERNAL_INSPECTION_LOOK_AHEAD_DETAILS);
		queryData.add(projectId);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration
		 */
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionLookQuery, true, true);

		inspectionLookQuery.append("group by x_axis_ext,y_axis_ext,y_axis_status_ext order by 1");

		return jdbcTemplate.query(inspectionLookQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionLookAheadDTO>>() {
					@Override
					public List<InspectionLookAheadDTO> extractData(final ResultSet rs) {
						List<InspectionLookAheadDTO> list = new ArrayList<InspectionLookAheadDTO>();
						try {
							while (rs.next()) {
								InspectionLookAheadDTO inspectionLookAheadDTO = new InspectionLookAheadDTO();
								inspectionLookAheadDTO.setxAxisColor(rs.getString(1));
								inspectionLookAheadDTO.setyAxisColor(rs.getString(2));
								inspectionLookAheadDTO.setyAxisStatus(rs.getString(3));
								inspectionLookAheadDTO.setStatusFlagCount(rs.getLong(4));
								list.add(inspectionLookAheadDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection look ahead details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/***
	 * This DAO method is used to fetch the inspection look a head data for for the
	 * customer for the given project id
	 * 
	 */
	@Override
	public List<InspectionLookAheadDTO> getCustomerInspectionLookAheadData(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionLookQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_CUSTOMER_INSPECTION_LOOK_AHEAD_DETAILS);
		queryData.add(projectId);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration
		 */
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionLookQuery, true, true);

		inspectionLookQuery.append(" group by x_axis_ext,y_axis_ext,y_axis_status_ext order by 1");

		return jdbcTemplate.query(inspectionLookQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionLookAheadDTO>>() {
					@Override
					public List<InspectionLookAheadDTO> extractData(final ResultSet rs) {
						List<InspectionLookAheadDTO> list = new ArrayList<InspectionLookAheadDTO>();
						try {
							while (rs.next()) {
								InspectionLookAheadDTO inspectionLookAheadDTO = new InspectionLookAheadDTO();
								inspectionLookAheadDTO.setxAxisColor(rs.getString(1));
								inspectionLookAheadDTO.setyAxisColor(rs.getString(2));
								inspectionLookAheadDTO.setyAxisStatus(rs.getString(3));
								inspectionLookAheadDTO.setStatusFlagCount(rs.getLong(4));
								list.add(inspectionLookAheadDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection look ahead details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/***
	 * This DAO method is used to fetch the X-Axis Master data for the inspection
	 * look a head chart
	 * 
	 */
	@Override
	public List<String> getLookAheadXAxisMasterData() {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_LOOK_AHEAD_XAXIS_MASTER_DETAILS,
				new ResultSetExtractor<List<String>>() {

					@Override
					public List<String> extractData(ResultSet rs) {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								list.add(rs.getString(1));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting look ahead xaxis master details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}

				});
	}

	/***
	 * This DAO method is used to fetch the Y-Axis Master data for the inspection
	 * look a head chart
	 * 
	 */
	@Override
	public List<ChartAxisColorDTO> getLookAheadYAxisMasterData() {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_LOOK_AHEAD_YAXIS_MASTER_DETAILS,
				new ResultSetExtractor<List<ChartAxisColorDTO>>() {

					@Override
					public List<ChartAxisColorDTO> extractData(ResultSet rs) {
						List<ChartAxisColorDTO> list = new ArrayList<ChartAxisColorDTO>();
						try {
							while (rs.next()) {
								ChartAxisColorDTO chartAxisColorDTO = new ChartAxisColorDTO();
								chartAxisColorDTO.setAxisName(rs.getString(1));
								chartAxisColorDTO.setAxisColor(rs.getString(2));
								list.add(chartAxisColorDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting look ahead yaxis master details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}

				});
	}

	/***
	 * This DAO method is used to fetch the inspection test plan details for the
	 * given project id for the internal user
	 * 
	 */
	@Override
	public List<InspectionTestPlanDTO> getInternalInspectionTestPlanData(InspectionTestPlanDTO inspectionTestPlanDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_INSPECTION_TEST_PLAN_DETAILS);
		queryData.add(inspectionTestPlanDTO.getProjectId());

		inspectionTestPlanDAOHelper.frameInternalITPDetials(inspectionTestPlanDTO, inspectionTestPlanQuery, queryData);

		return jdbcTemplate.query(inspectionTestPlanQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionTestPlanDTO>>() {
					@Override
					public List<InspectionTestPlanDTO> extractData(final ResultSet rs) {
						List<InspectionTestPlanDTO> list = new ArrayList<InspectionTestPlanDTO>();
						try {
							inspectionTestPlanDAOHelper.populateInspectionTestPlanDetails(rs, list);
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection test plan details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/***
	 * This DAO method is used to fetch the inspection test plan details for the
	 * given project id for the internal user
	 * 
	 */
	@Override
	public String downloadInternalIAPCSVData(String projectId) {

		StringBuilder resultData = new StringBuilder();
		inspectionTestPlanDAOHelper.frameIAPDownloadFileHeader(resultData);

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_TEST_PLAN_DETAILS,
				new Object[] { projectId }, new ResultSetExtractor<String>() {
					@Override
					public String extractData(final ResultSet rs) {
						try {
							inspectionTestPlanDAOHelper.downloadInspectionTestPlanDetails(rs, resultData);
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection test plan details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return resultData.toString();
					}
				});
	}

	/**
	 * This DAO method is used to fetch the inspection test plan details for the
	 * external user for the given project id based on the filter condition defined
	 * in the inspection configuration
	 * 
	 */
	@Override
	public String downloadExternalInspectionTestPlanDataCSV(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		StringBuilder resultData = new StringBuilder();
		inspectionTestPlanDAOHelper.frameExternalIAPDownloadFileHeader(resultData);

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_EXTERNAL_INSPECTION_TEST_PLAN_DETAILS);
		queryData.add(projectId);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration
		 */
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionTestPlanQuery, false, true);

		return jdbcTemplate.query(inspectionTestPlanQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(final ResultSet rs) {
						try {
							inspectionTestPlanDAOHelper.downloadExternalInspectionTestPlanDetails(rs, resultData);
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection test plan details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return resultData.toString();
					}
				});
	}

	/**
	 * This DAO method is used to fetch the inspection test plan details for the
	 * external user for the given project id based on the filter condition defined
	 * in the inspection configuration
	 * 
	 */
	@Override
	public List<InspectionTestPlanDTO> getExternalInspectionTestPlanData(InspectionTestPlanDTO inspectionTestPlanDTO,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_EXTERNAL_INSPECTION_TEST_PLAN_DETAILS);
		queryData.add(inspectionTestPlanDTO.getProjectId());

		inspectionTestPlanDAOHelper.frameIAPExternalDetails(inspectionTestPlanDTO, inspectionTestPlanQuery, queryData);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration
		 */
		inspectionTestPlanDAOHelper.applyExternalConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionTestPlanQuery, true, true);

		return jdbcTemplate.query(inspectionTestPlanQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionTestPlanDTO>>() {
					@Override
					public List<InspectionTestPlanDTO> extractData(final ResultSet rs) {
						List<InspectionTestPlanDTO> list = new ArrayList<InspectionTestPlanDTO>();
						try {
							inspectionTestPlanDAOHelper.populateExternalInspectionTestPlanDetails(rs, list);
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection test plan details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/**
	 * This DAO method is used to fetch the inspection test plan details for the
	 * customer for the given project id
	 * 
	 */
	@Override
	public List<CustomerInspectionTestPlanDTO> getCustomerInspectionTestPlanData(
			InspectionTestPlanDTO inspectionTestPlanDTO, InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_CUSTOMER_INSPECTION_TEST_PLAN_DETAILS);
		queryData.add(inspectionTestPlanDTO.getProjectId());

		inspectionTestPlanDAOHelper.frameExternalITPDetials(inspectionTestPlanDTO, inspectionTestPlanQuery, queryData);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration for the customer
		 */
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionTestPlanQuery, true, true);

		return jdbcTemplate.query(inspectionTestPlanQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<List<CustomerInspectionTestPlanDTO>>() {

					@Override
					public List<CustomerInspectionTestPlanDTO> extractData(final ResultSet rs) {
						List<CustomerInspectionTestPlanDTO> list = new ArrayList<CustomerInspectionTestPlanDTO>();
						try {
							inspectionTestPlanDAOHelper.populateCustomerInspectionTestPlanDetails(rs, list);
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection test plan details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}

				});
	}

	@Override
	public StatusUtilDTO getLogDetails(String projectId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_TEST_PLAN_LOG_DETAILS,
				new Object[] { projectId }, new ResultSetExtractor<StatusUtilDTO>() {
					@Override
					public StatusUtilDTO extractData(final ResultSet rs) {
						StatusUtilDTO statusUtilDTODTO = new StatusUtilDTO();
						try {
							while (rs.next()) {
								statusUtilDTODTO.setFirstName(rs.getString(1));
								statusUtilDTODTO.setLastUpdatedDate(rs.getString(2));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection log details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return statusUtilDTODTO;
					}
				});
	}

	@Override
	public boolean deleteCustomerData(String projectId) {
		boolean deletedSucessfully = true;

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(InspectionTestPlanConstants.DELETE_CUSTOMER_INSPECTION_DATA);) {
			ps.setString(1, projectId);
			ps.execute();

		} catch (SQLException e) {
			deletedSucessfully = false;
			log.error("something went wrong while deleting  inspection configuration details: " + e.getMessage());
		}
		return deletedSucessfully;
	}

	@Override
	public String publishCustomerData(String projectId, InspectionConfiguratorDTO inspectionConfiguratorDTO) {
		String publishStatus = null;
		int objectIndex = 1;

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.PUBLISH_CUSTOMER_INSPECTION_DATA);
		queryData.add(projectId);

		/***
		 * apply the filter condition based on the value set in the inspection
		 * configuration
		 */
		inspectionTestPlanDAOHelper.applyConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionTestPlanQuery, false, true);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(inspectionTestPlanQuery.toString());

		) {
			for (Object queryString : queryData) {
				ps.setString(objectIndex, queryString.toString());
				objectIndex = objectIndex + 1;
			}

			ps.execute();
			publishStatus = "Project " + projectId + " is published successfully";

		} catch (SQLException e) {
			publishStatus = "Error Occured when the project " + projectId + " is published";
			log.error("something went wrong while publishing inspection configuration details: " + e.getMessage());
		}
		return publishStatus;
	}

	@Override
	public InspectionCommentsDTO saveUserComments(InspectionCommentsDTO inspectionCommentsDTO) {

		Integer trackId = insertCustomerDataTrackingDetails(inspectionCommentsDTO.getProjectId(),
				inspectionCommentsDTO.getSsoId(), inspectionCommentsDTO.getPqmComments(), "NA");

		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement updateTrackingDataStatusPstm = con
					.prepareStatement(InspectionTestPlanConstants.UPDATE_TRACKING_DATA_STATUS);
			updateTrackingDataStatusPstm.setString(1, "Completed");
			updateTrackingDataStatusPstm.setString(2, "PUBLISHED");
			updateTrackingDataStatusPstm.setInt(3, trackId);
			updateTrackingDataStatusPstm.setString(4, inspectionCommentsDTO.getProjectId());

			updateTrackingDataStatusPstm.executeUpdate();
		} catch (Exception e) {
			log.error("something went wrong while saving track id and updating tracking data status:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while saving track id and updating tracking data status:"
							+ e.getMessage());
				}
			}
		}

		return inspectionCommentsDTO;
	}

	@Override
	public InspectionCommentsDTO fetchUserComments(String projectId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_USER_COMMENTS_AND_SOURCE,
				new Object[] { projectId, projectId, "Completed" }, new ResultSetExtractor<InspectionCommentsDTO>() {

					@Override
					public InspectionCommentsDTO extractData(ResultSet rs) {
						InspectionCommentsDTO inspectionCommentsDTO = null;
						try {
							while (rs.next()) {
								inspectionCommentsDTO = new InspectionCommentsDTO();
								inspectionCommentsDTO.setProjectId(projectId);
								inspectionCommentsDTO.setPqmComments(rs.getString("comments"));
								inspectionCommentsDTO.setCommentsSource(rs.getString("data_source"));
								inspectionCommentsDTO.setInsertedBy(rs.getString("user_name"));
								inspectionCommentsDTO.setInsertedDate(rs.getString("insert_date"));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting last successful update details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return inspectionCommentsDTO;
					}

				});
	}

	/***
	 * 
	 * This method is used to fetch the Inspection Configuration Details from the
	 * table rt_app.rt_cat_iap_config_update_control for the given project id
	 * 
	 */
	@Override
	public InspectionConfiguratorDTO getInspectionConfiguratorDetails(String projectId, String sourceType) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_INSPECTION_CONFIGURATION_DETAILS,
				new Object[] { projectId, sourceType }, new ResultSetExtractor<InspectionConfiguratorDTO>() {

					@Override
					public InspectionConfiguratorDTO extractData(ResultSet rs) {
						InspectionConfiguratorDTO inspectionConfiguratorDTO = null;
						try {

							while (rs.next()) {
								inspectionConfiguratorDTO = new InspectionConfiguratorDTO();
								inspectionConfiguratorDTO.setProjectId(projectId);
								inspectionConfiguratorDTO.setCustomerDataFilter(rs.getString(1));
								inspectionConfiguratorDTO.setTpDataFilter(rs.getString(2));
								inspectionConfiguratorDTO.setEndUserDataFilter(rs.getString(3));
								inspectionConfiguratorDTO.setPeriodFilter(rs.getString(4));
								inspectionConfiguratorDTO.setQcpPageNameFilter(rs.getString(5));
							}

						} catch (SQLException e) {
							log.error("something went wrong while getting inspection configuration details: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return inspectionConfiguratorDTO;
					}

				});
	}

	/***
	 * 
	 * This method is used to update the Inspection Configuration details edited by
	 * the user to the table rt_app.rt_cat_iap_config_update_control for the given
	 * project id
	 * 
	 */
	@Override
	public int updateInspectionConfiguratorDetails(InspectionConfiguratorDTO inspectionConfiguratorDTO,
			String sourceType) {

		int rowsAffected = jdbcTemplate.update(InspectionTestPlanConstants.UPDATE_INSPECTION_CONFIGURATION_DETAILS,
				new Object[] { inspectionConfiguratorDTO.getCustomerDataFilter(),
						inspectionConfiguratorDTO.getTpDataFilter(), inspectionConfiguratorDTO.getEndUserDataFilter(),
						null == inspectionConfiguratorDTO.getPeriodFilter() ? null
								: Integer.valueOf(inspectionConfiguratorDTO.getPeriodFilter()).intValue(),
						inspectionConfiguratorDTO.getQcpPageNameFilter(), inspectionConfiguratorDTO.getProjectId(),
						sourceType });

		return rowsAffected;
	}

	/***
	 * 
	 * This method is used to insert the default Inspection Configuration details in
	 * to the table rt_app.rt_cat_iap_config_update_control for the given project id
	 * 
	 */
	@Override
	public void insertsInspectionConfiguratorDetails(InspectionConfiguratorDTO inspectionConfiguratorDTO,
			String sourceType) {

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(InspectionTestPlanConstants.INSERT_INSPECTION_CONFIGURATION_DETAILS);

		) {

			ps.setString(1, inspectionConfiguratorDTO.getProjectId());
			ps.setString(2, inspectionConfiguratorDTO.getCustomerDataFilter());
			ps.setString(3, inspectionConfiguratorDTO.getTpDataFilter());
			ps.setInt(4, Integer.valueOf(inspectionConfiguratorDTO.getPeriodFilter()).intValue());
			ps.setString(5, "System");
			ps.setString(6, sourceType);
			ps.execute();

		} catch (SQLException e) {
			log.error("something went wrong while inserting inspection configuration details: " + e.getMessage());
		}

	}

	@Override
	public InspectionConfiguratorDTO fetchQCPPageNameMasterData(InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		inspectionConfiguratorDTO.setQcpPageNameMasterList(new ArrayList<InspectionConfigurationDropDownDTO>());

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_QCP_PAGE_NAME_MASTER_VALUE,
				new Object[] { inspectionConfiguratorDTO.getProjectId() },
				new ResultSetExtractor<InspectionConfiguratorDTO>() {

					String qcpPageFilter = null;

					@Override
					public InspectionConfiguratorDTO extractData(ResultSet rs) {
						InspectionConfigurationDropDownDTO inspectionConfigurationDropDownDTO = null;
						Set<String> qcpPageFilterSet = new HashSet<String>();

						try {
							while (rs.next()) {
								qcpPageFilter = rs.getString(1);

								if (null != qcpPageFilter
										&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(qcpPageFilter)
										&& !qcpPageFilterSet.contains(qcpPageFilter)) {
									qcpPageFilterSet.add(qcpPageFilter);
									inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
									inspectionConfigurationDropDownDTO.setKey(qcpPageFilter);
									inspectionConfigurationDropDownDTO.setVal(qcpPageFilter);
									inspectionConfiguratorDTO.getQcpPageNameMasterList()
											.add(inspectionConfigurationDropDownDTO);
								}

							}

						} catch (SQLException e) {
							log.error("something went wrong while getting inspection configuration details: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return inspectionConfiguratorDTO;
					}

				});
	}

	@Override
	public Map<String, Integer> getFileUploadStatus(String projectId) {
		return jdbcTemplate.query(InspectionTestPlanConstants.GET_FILE_UPLOAD_STATUS, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Integer>>() {

					@Override
					public Map<String, Integer> extractData(ResultSet rs) {
						HashMap<String, Integer> status = new HashMap<String, Integer>();
						try {
							while (rs.next()) {
								status.put("status", rs.getInt(1));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting file upload status:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return status;
					}

				});
	}

	@Override
	public Map<Long, String> fetchIAPExcelColumnNameList() {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_IAP_EXCEL_COLUMN_LIST, new Object[] {},
				new ResultSetExtractor<Map<Long, String>>() {
					Map<Long, String> columnNameMap = null;

					@Override
					public Map<Long, String> extractData(ResultSet rs) {
						columnNameMap = new HashMap<Long, String>();
						try {
							while (rs.next()) {
								columnNameMap.put(rs.getLong(1), rs.getString(2));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting column name details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return columnNameMap;
					}

				});
	}

	@Override
	public Integer insertCustomerDataTrackingDetails(String projectId, String ssoId, String comments, String fileName) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_TRACK_ID,
				new Object[] { projectId, ssoId, comments, fileName }, new ResultSetExtractor<Integer>() {

					@Override
					public Integer extractData(ResultSet rs) {
						Integer trackId = null;
						try {
							while (rs.next()) {
								trackId = rs.getInt(1);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting track id:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return trackId;
					}

				});

	}

	public void saveFileUploadErrorDetails(InspectionFileUploadDTO inspectionFileUploadDTO) {

		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement saveTrackIdPstm = con.prepareStatement(InspectionTestPlanConstants.SAVE_TRACK_ID);
			PreparedStatement updateTrackingDataStatusPstm = con
					.prepareStatement(InspectionTestPlanConstants.UPDATE_TRACKING_DATA_STATUS);

			saveTrackIdPstm.setString(1, inspectionFileUploadDTO.getProjectId());
			saveTrackIdPstm.setString(2, inspectionFileUploadDTO.getErrorMessage());
			saveTrackIdPstm.setInt(3, inspectionFileUploadDTO.getTrackId());

			updateTrackingDataStatusPstm.setString(1, "Error");
			updateTrackingDataStatusPstm.setString(2, "EXCEL_UPDATE");
			updateTrackingDataStatusPstm.setInt(3, inspectionFileUploadDTO.getTrackId());
			updateTrackingDataStatusPstm.setString(4, inspectionFileUploadDTO.getProjectId());

			saveTrackIdPstm.executeUpdate();
			updateTrackingDataStatusPstm.executeUpdate();
		} catch (Exception e) {
			log.error("something went wrong while saving track id and updating tracking data status:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public boolean deleteFileUploadStageData(String projectId) {
		boolean deletedSucessfully = true;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(InspectionTestPlanConstants.DELETE_FILE_UPLOAD_STAGE_TABLE);) {
			ps.setString(1, projectId);
			ps.execute();
		} catch (SQLException e) {
			deletedSucessfully = false;
			log.error("something went wrong while deleting  file upload stage   details: " + e.getMessage());
		}
		return deletedSucessfully;
	}

	public boolean saveExcelDataIntoStg(String projectId, String excelData, String ssoId) {
		boolean specialCharacterPresent = false;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(InspectionTestPlanConstants.SAVE_INSPECTION_TEST_PLAN_EXCEL_DATA);

		) {
			String[] rowData = excelData.split(InspectionTestPlanConstants.END_OF_LINE_DELIMITER);
			String[] columnData = null;
			int batchIndex = 0;
			int cellIndex = 0;
			boolean firstHeaderRow = true;
			for (String row : rowData) {
				cellIndex = 2;
				columnData = row.split(InspectionTestPlanConstants.COMMA_SEPERATOR);
				if (columnData.length == 38) {
					if (firstHeaderRow) {
						firstHeaderRow = false;
						continue;
					}
					ps.setString(1, projectId);
					for (String cellData : columnData) {
						cellData = cellData.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
								InspectionTestPlanConstants.WHITE_SPACE);
						ps.setString(cellIndex, cellData.trim());
						cellIndex++;
					}
					ps.setString(40, ssoId);
					ps.addBatch();
					batchIndex++;
					if (batchIndex % 1000 == 0) {
						ps.executeBatch();
					}
				} else {
					specialCharacterPresent = true;
				}
			}
			ps.executeBatch();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return specialCharacterPresent;
	}

	public Map<String, String> processExcelUploadedData(String projectId, long trackId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_EXCEL_UPLOAD_STATUS,
				new Object[] { projectId, trackId }, new ResultSetExtractor<Map<String, String>>() {

					@Override
					public Map<String, String> extractData(ResultSet rs) {
						HashMap<String, String> status = new HashMap<String, String>();
						try {
							while (rs.next()) {
								status.put("status", rs.getString(1));
							}
						} catch (SQLException e) {
							log.error("something went wrong while uploading excel data into targate table:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return status;
					}

				});
	}

	@Override
	public List<LastSuccessfulUpdateDetailsDTO> getLastSuccessfulUpdateData(String projectId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_LAST_SUCCESSFUL_UPDATE_DETAILS,
				new Object[] { projectId, projectId, "Completed" },
				new ResultSetExtractor<List<LastSuccessfulUpdateDetailsDTO>>() {

					@Override
					public List<LastSuccessfulUpdateDetailsDTO> extractData(ResultSet rs) {
						List<LastSuccessfulUpdateDetailsDTO> list = new ArrayList<LastSuccessfulUpdateDetailsDTO>();
						try {
							while (rs.next()) {
								//		
								LastSuccessfulUpdateDetailsDTO successfulDTO = new LastSuccessfulUpdateDetailsDTO();
								successfulDTO.setSsoId(rs.getString("user_name"));
								successfulDTO.setStatus(rs.getString("status"));
								successfulDTO.setInsertDate(rs.getString("insert_date"));
								successfulDTO.setFileName(rs.getString("file_name"));
								list.add(successfulDTO);

							}
						} catch (SQLException e) {
							log.error("something went wrong while getting last successful update details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}

				});
	}

	@Override
	public List<LastUpdateDetailsDTO> getLastUpdateData(String projectId) {

		return jdbcTemplate.query(InspectionTestPlanConstants.GET_LAST_UPDATE_DETAILS,
				new Object[] { projectId, projectId }, new ResultSetExtractor<List<LastUpdateDetailsDTO>>() {

					@Override
					public List<LastUpdateDetailsDTO> extractData(ResultSet rs) {
						List<LastUpdateDetailsDTO> list = new ArrayList<LastUpdateDetailsDTO>();
						try {
							while (rs.next()) {
								LastUpdateDetailsDTO lastUpdateDTO = new LastUpdateDetailsDTO();
								lastUpdateDTO.setSsoId(rs.getString("user_name"));
								lastUpdateDTO.setStatus(rs.getString("status"));
								lastUpdateDTO.setInsertDate(rs.getString("insert_date"));
								lastUpdateDTO.setFileName(rs.getString("file_name"));
								list.add(lastUpdateDTO);

							}
						} catch (SQLException e) {
							log.error("something went wrong while getting last update details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}

				});
	}

	@Override
	public List<ErrorDetailsDTO> getErrorDetailsData(String projectId) {
		return jdbcTemplate.query(InspectionTestPlanConstants.GET_ERROR_DETAILS, new Object[] { projectId },
				new ResultSetExtractor<List<ErrorDetailsDTO>>() {
					@Override
					public List<ErrorDetailsDTO> extractData(final ResultSet rs) {
						List<ErrorDetailsDTO> list = new ArrayList<ErrorDetailsDTO>();
						try {
							while (rs.next()) {
								ErrorDetailsDTO errorDetailsDTO = new ErrorDetailsDTO();
								errorDetailsDTO.setProjectId(rs.getString(1));
								errorDetailsDTO.setErrorDetails(rs.getString(2));
								list.add(errorDetailsDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting error details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	/**
	 * This DAO method is used to fetch the inspection test plan details for the
	 * customer for the given project id
	 * 
	 */
	@Override
	public String downloadCustomerInspectionTestPlanDataCSV(String projectId) {

		ArrayList<Object> queryData = new ArrayList<Object>();

		StringBuilder resultData = new StringBuilder();
		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_CUSTOMER_INSPECTION_TEST_PLAN_DETAILS);
		queryData.add(projectId);

		inspectionTestPlanDAOHelper.frameCustomerDownloadFileHeader(resultData);
		return jdbcTemplate.query(inspectionTestPlanQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<String>() {

					@Override
					public String extractData(final ResultSet rs) {
						String resultString = null;
						try {
							resultString = inspectionTestPlanDAOHelper.downloadCustomerInspectionTestPlanDetails(rs,
									resultData);
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection test plan details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return resultString;
					}

				});
	}

	@Override
	public void triggerQIRadar(String projectId) {

		jdbcTemplate.update(InspectionTestPlanConstants.TRIGGER_QI_RADAR, new Object[] { projectId, projectId });

	}

	@Override
	public List<CustomerInspectionTestPlanDTO> downloadCustomerInspectionTestPlanDataPDF(String projectId) {

		ArrayList<Object> queryData = new ArrayList<Object>();
		ArrayList<CustomerInspectionTestPlanDTO> list = new ArrayList<CustomerInspectionTestPlanDTO>();
		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_CUSTOMER_INSPECTION_TEST_PLAN_DETAILS);
		queryData.add(projectId);

		return jdbcTemplate.query(inspectionTestPlanQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<List<CustomerInspectionTestPlanDTO>>() {
					CustomerInspectionTestPlanDTO dto = null;
					String customer = null;
					String endUser = null;
					String tp = null;
					String qcpPageName = null;
					String costingProject = null;
					String qcpDoc = null;
					String revQCP = null;
					String itemDescription = null;
					String requirementId = null;
					String requirementDescription = null;
					String refDocs = null;
					String procedure = null;
					String acceptance = null;
					String ge = null;
					String supplier = null;
					String supplierLocation = null;
					String subSupplierLocation = null;
					String custNotificationNo = null;
					String notificationStatus = null;
					String notificationRev = null;

					@Override
					public List<CustomerInspectionTestPlanDTO> extractData(final ResultSet rs) {
						try {
							while (rs.next()) {
								dto = new CustomerInspectionTestPlanDTO();

								qcpPageName = rs.getString(1);
								if (null != qcpPageName) {
									qcpPageName = qcpPageName.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									qcpPageName = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setQcpPageName(qcpPageName);

								costingProject = rs.getString(2);
								if (null != costingProject) {
									costingProject = costingProject.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									costingProject = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setCostingProject(costingProject);

								qcpDoc = rs.getString(3);
								if (null != qcpDoc) {
									qcpDoc = qcpDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									qcpDoc = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setQcpDoc(qcpDoc);

								revQCP = rs.getString(4);
								if (null != revQCP) {
									revQCP = revQCP.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									revQCP = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRevQCP(revQCP);

								itemDescription = rs.getString(5);
								if (null != itemDescription) {
									itemDescription = itemDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									itemDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setItemDescription(itemDescription);

								requirementId = rs.getString(6);
								if (null != requirementId) {
									requirementId = requirementId.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									requirementId = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRequirementId(requirementId);

								requirementDescription = rs.getString(7);
								if (null != requirementDescription) {
									requirementDescription = requirementDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									requirementDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRequirementDescription(requirementDescription);

								refDocs = rs.getString(8);
								if (null != refDocs) {
									refDocs = refDocs.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									refDocs = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRefDocs(refDocs);

								procedure = rs.getString(9);
								if (null != procedure) {
									procedure = procedure.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									procedure = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setProcedure(procedure);

								acceptance = rs.getString(10);
								if (null != acceptance) {
									acceptance = acceptance.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									acceptance = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setAcceptance(acceptance);

								ge = rs.getString(11);
								if (null != ge) {
									ge = ge.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									ge = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setGe(ge);

								customer = rs.getString(12);
								if (null == customer
										|| InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(customer)) {
									customer = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setCustomer(customer);

								endUser = rs.getString(13);
								if (null == endUser
										|| InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(endUser)) {
									endUser = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setEndUser(endUser);

								tp = rs.getString(14);
								if (null == tp || InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(tp)) {
									tp = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setTp(tp);

								supplier = rs.getString(15);
								if (null != supplier) {
									supplier = supplier.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									supplier = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSupplier(supplier);

								supplierLocation = rs.getString(16);
								if (null != supplierLocation) {
									supplierLocation = supplierLocation.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									supplierLocation = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSupplierLocation(supplierLocation);

								subSupplierLocation = rs.getString(17);
								if (null != subSupplierLocation) {
									subSupplierLocation = subSupplierLocation.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									subSupplierLocation = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSubSupplierLocation(subSupplierLocation);

								dto.setExpectedInspectionStartDate((null != rs.getString(18) ? rs.getString(18) : ""));

								custNotificationNo = rs.getString(19);
								if (null != custNotificationNo) {
									custNotificationNo = custNotificationNo.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									custNotificationNo = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setCustNotificationNumber(custNotificationNo);

								notificationStatus = rs.getString(20);
								if (null != notificationStatus) {
									notificationStatus = notificationStatus.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationStatus = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationStatus(notificationStatus);

								notificationRev = rs.getString(21);
								if (null != notificationRev) {
									notificationRev = notificationRev.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationRev = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationRevision(notificationRev);
								dto.setInspectionDate((null != rs.getString(22) ? rs.getString(22) : ""));
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting inspection test plan details for pdf:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}

				});
	}

	@Override
	public List<InspectionTestPlanDTO> downloadIAPExternalExcel(String projectId,
			InspectionConfiguratorDTO inspectionConfiguratorDTO) {
		ArrayList<Object> queryData = new ArrayList<Object>();
		ArrayList<InspectionTestPlanDTO> list = new ArrayList<InspectionTestPlanDTO>();
		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_EXTERNAL_INSPECTION_TEST_PLAN_DETAILS);
		queryData.add(projectId);
		inspectionTestPlanDAOHelper.applyExternalConfiguratorFilterCondition(inspectionConfiguratorDTO, queryData,
				inspectionTestPlanQuery, false, true);

		return jdbcTemplate.query(inspectionTestPlanQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionTestPlanDTO>>() {
					InspectionTestPlanDTO dto = null;
					String customer = null;
					String endUser = null;
					String tp = null;
					String qcpPageName = null;
					String omDescription = null;
					String costingProject = null;
					String functionalUnit = null;
					String functionalUnitDescription = null;
					String peiCode = null;
					String longDummyCode = null;
					String itemCode = null;
					String qcpDoc = null;
					String revQCP = null;
					String itemDescription = null;
					String requirementId = null;
					String requirementDescription = null;
					String status = null;
					String refDocs = null;
					String procedure = null;
					String acceptance = null;
					String ge = null;
					String supplier = null;
					String supplierLocation = null;
					String subSupplierLocation = null;
					String poWip = null;
					String poLine = null;
					String custNotificationNo = null;
					String notificationNumber = null;
					String notificationStatus = null;
					String notificationRev = null;
					String inspectionDuration = null;
					String inspectionType = null;
					String notificationToCustomer = null;
					String rc1Reference = null;
					String testResult = null;
					String inspectionNotes = null;

					@Override
					public List<InspectionTestPlanDTO> extractData(final ResultSet rs) {
						try {
							while (rs.next()) {
								dto = new InspectionTestPlanDTO();

								omDescription = rs.getString(1);
								if (null != omDescription) {
									omDescription = omDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									omDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setOmDescription(omDescription);

								qcpPageName = rs.getString(2);
								if (null != qcpPageName) {
									qcpPageName = qcpPageName.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									qcpPageName = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setQcpPageName(qcpPageName);

								costingProject = rs.getString(3);
								if (null != costingProject) {
									costingProject = costingProject.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									costingProject = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setCostingProject(costingProject);

								functionalUnit = rs.getString(4);
								if (null != functionalUnit) {
									functionalUnit = functionalUnit.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									functionalUnit = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setFunctionalUnit(functionalUnit);

								functionalUnitDescription = rs.getString(5);
								if (null != functionalUnitDescription) {
									functionalUnitDescription = functionalUnitDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									functionalUnitDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setFunctionalUnitDescription(functionalUnitDescription);

								peiCode = rs.getString(6);
								if (null != peiCode) {
									peiCode = peiCode.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									peiCode = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setPeiCode(peiCode);

								longDummyCode = rs.getString(7);
								if (null != longDummyCode) {
									longDummyCode = longDummyCode.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									longDummyCode = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setLongDummyCode(longDummyCode);

								itemCode = rs.getString(8);
								if (null != itemCode) {
									itemCode = itemCode.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									itemCode = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setItemCode(itemCode);

								itemDescription = rs.getString(9);
								if (null != itemDescription) {
									itemDescription = itemDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									itemDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setItemDescription(itemDescription);

								qcpDoc = rs.getString(10);
								if (null != qcpDoc) {
									qcpDoc = qcpDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									qcpDoc = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setQcpDoc(qcpDoc);

								revQCP = rs.getString(11);
								if (null != revQCP) {
									revQCP = revQCP.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									revQCP = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRevQCP(revQCP);

								requirementId = rs.getString(12);
								if (null != requirementId) {
									requirementId = requirementId.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									requirementId = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRequirementId(requirementId);

								requirementDescription = rs.getString(13);
								if (null != requirementDescription) {
									requirementDescription = requirementDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									requirementDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRequirementDescription(requirementDescription);

								status = rs.getString(14);
								if (null != status) {
									status = status.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									status = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setStatus(status);

								refDocs = rs.getString(15);
								if (null != refDocs) {
									refDocs = refDocs.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									refDocs = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRefDocs(refDocs);

								procedure = rs.getString(16);
								if (null != procedure) {
									procedure = procedure.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									procedure = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setProcedure(procedure);

								acceptance = rs.getString(17);
								if (null != acceptance) {
									acceptance = acceptance.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									acceptance = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setAcceptance(acceptance);

								ge = rs.getString(18);
								if (null != ge) {
									ge = ge.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									ge = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setGe(ge);

								customer = rs.getString(19);
								if (null == customer
										|| InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(customer)) {
									customer = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setCustomer(customer);

								endUser = rs.getString(20);
								if (null == endUser
										|| InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(endUser)) {
									endUser = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setEndUser(endUser);

								tp = rs.getString(21);
								if (null == tp || InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(tp)) {
									tp = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setTp(tp);

								supplier = rs.getString(22);
								if (null != supplier) {
									supplier = supplier.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									supplier = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSupplier(supplier);

								supplierLocation = rs.getString(23);
								if (null != supplierLocation) {
									supplierLocation = supplierLocation.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									supplierLocation = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSupplierLocation(supplierLocation);

								subSupplierLocation = rs.getString(24);
								if (null != subSupplierLocation) {
									subSupplierLocation = subSupplierLocation.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									subSupplierLocation = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSubSupplierLocation(subSupplierLocation);

								poWip = rs.getString(25);
								if (null != poWip) {
									poWip = poWip.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									poWip = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setPoWip(poWip);

								poLine = rs.getString(26);
								if (null != poLine) {
									poLine = poLine.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									poLine = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setPoLine(poLine);

								dto.setExpectedInspectionStartDate((null != rs.getString(27) ? rs.getString(27) : ""));

								custNotificationNo = rs.getString(28);
								if (null != custNotificationNo) {
									custNotificationNo = custNotificationNo.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									custNotificationNo = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setCustNotificationNumber(custNotificationNo);

								notificationNumber = rs.getString(29);
								if (null != notificationNumber) {
									notificationNumber = notificationNumber.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationNumber = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationNumber(notificationNumber);

								notificationStatus = rs.getString(30);
								if (null != notificationStatus) {
									notificationStatus = notificationStatus.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationStatus = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationStatus(notificationStatus);

								notificationRev = rs.getString(31);
								if (null != notificationRev) {
									notificationRev = notificationRev.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationRev = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationRevision(notificationRev);

								dto.setInspectionDate((null != rs.getString(32) ? rs.getString(32) : ""));

								inspectionDuration = rs.getString(33);
								if (null != inspectionDuration) {
									inspectionDuration = inspectionDuration.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									inspectionDuration = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setInspectionDuration(inspectionDuration);

								inspectionType = rs.getString(34);
								if (null != inspectionType) {
									inspectionType = inspectionType.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									inspectionType = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setInspectionType(inspectionType);

								notificationToCustomer = rs.getString(35);
								if (null != notificationToCustomer) {
									notificationToCustomer = notificationToCustomer.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationToCustomer = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationToCustomer(notificationToCustomer);

								rc1Reference = rs.getString(36);
								if (null != rc1Reference) {
									rc1Reference = rc1Reference.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									rc1Reference = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRc1Reference(rc1Reference);

								testResult = rs.getString(37);
								if (null != testResult) {
									testResult = testResult.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									testResult = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setTestResult(testResult);

								inspectionNotes = rs.getString(38);
								if (null != inspectionNotes) {
									inspectionNotes = inspectionNotes.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									inspectionNotes = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setInspectionNotes(inspectionNotes);
								dto.setUsrExpectedInspectionStartDt(null != rs.getString("usr_expected_inspection_start_dt")
										? rs.getString("usr_expected_inspection_start_dt")
										: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting IAP External Details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}

				});

	}

	@Override
	public List<InspectionTestPlanDTO> downloadIAPInternalExcel(String projectId) {
		ArrayList<Object> queryData = new ArrayList<Object>();
		ArrayList<InspectionTestPlanDTO> list = new ArrayList<InspectionTestPlanDTO>();
		StringBuilder inspectionTestPlanQuery = new StringBuilder(
				InspectionTestPlanConstants.GET_INSPECTION_TEST_PLAN_DETAILS);
		queryData.add(projectId);
		return jdbcTemplate.query(inspectionTestPlanQuery.toString(), queryData.toArray(),
				new ResultSetExtractor<List<InspectionTestPlanDTO>>() {
					InspectionTestPlanDTO dto = null;
					String customer = null;
					String endUser = null;
					String tp = null;
					String qcpPageName = null;
					String omDescription = null;
					String costingProject = null;
					String functionalUnit = null;
					String functionalUnitDescription = null;
					String peiCode = null;
					String longDummyCode = null;
					String itemCode = null;
					String qcpDoc = null;
					String revQCP = null;
					String itemDescription = null;
					String requirementId = null;
					String requirementDescription = null;
					String status = null;
					String refDocs = null;
					String procedure = null;
					String acceptance = null;
					String ge = null;
					String supplier = null;
					String supplierLocation = null;
					String subSupplierLocation = null;
					String poWip = null;
					String poLine = null;
					String custNotificationNo = null;
					String notificationNumber = null;
					String notificationStatus = null;
					String notificationRev = null;
					String inspectionDuration = null;
					String inspectionType = null;
					String notificationToCustomer = null;
					String rc1Reference = null;
					String testResult = null;
					String inspectionNotes = null;

					@Override
					public List<InspectionTestPlanDTO> extractData(final ResultSet rs) {
						try {
							while (rs.next()) {
								dto = new InspectionTestPlanDTO();

								omDescription = rs.getString(1);
								if (null != omDescription) {
									omDescription = omDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									omDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setOmDescription(omDescription);

								qcpPageName = rs.getString(2);
								if (null != qcpPageName) {
									qcpPageName = qcpPageName.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									qcpPageName = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setQcpPageName(qcpPageName);

								costingProject = rs.getString(3);
								if (null != costingProject) {
									costingProject = costingProject.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									costingProject = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setCostingProject(costingProject);

								functionalUnit = rs.getString(4);
								if (null != functionalUnit) {
									functionalUnit = functionalUnit.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									functionalUnit = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setFunctionalUnit(functionalUnit);

								functionalUnitDescription = rs.getString(5);
								if (null != functionalUnitDescription) {
									functionalUnitDescription = functionalUnitDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									functionalUnitDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setFunctionalUnitDescription(functionalUnitDescription);

								peiCode = rs.getString(6);
								if (null != peiCode) {
									peiCode = peiCode.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									peiCode = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setPeiCode(peiCode);

								longDummyCode = rs.getString(7);
								if (null != longDummyCode) {
									longDummyCode = longDummyCode.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									longDummyCode = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setLongDummyCode(longDummyCode);

								itemCode = rs.getString(8);
								if (null != itemCode) {
									itemCode = itemCode.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									itemCode = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setItemCode(itemCode);

								itemDescription = rs.getString(9);
								if (null != itemDescription) {
									itemDescription = itemDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									itemDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setItemDescription(itemDescription);

								qcpDoc = rs.getString(10);
								if (null != qcpDoc) {
									qcpDoc = qcpDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									qcpDoc = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setQcpDoc(qcpDoc);

								revQCP = rs.getString(11);
								if (null != revQCP) {
									revQCP = revQCP.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									revQCP = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRevQCP(revQCP);

								requirementId = rs.getString(12);
								if (null != requirementId) {
									requirementId = requirementId.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									requirementId = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRequirementId(requirementId);

								requirementDescription = rs.getString(13);
								if (null != requirementDescription) {
									requirementDescription = requirementDescription.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									requirementDescription = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRequirementDescription(requirementDescription);

								status = rs.getString(14);
								if (null != status) {
									status = status.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									status = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setStatus(status);

								refDocs = rs.getString(15);
								if (null != refDocs) {
									refDocs = refDocs.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									refDocs = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRefDocs(refDocs);

								procedure = rs.getString(16);
								if (null != procedure) {
									procedure = procedure.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									procedure = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setProcedure(procedure);

								acceptance = rs.getString(17);
								if (null != acceptance) {
									acceptance = acceptance.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									acceptance = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setAcceptance(acceptance);

								ge = rs.getString(18);
								if (null != ge) {
									ge = ge.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									ge = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setGe(ge);

								customer = rs.getString(19);
								if (null == customer
										|| InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(customer)) {
									customer = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setCustomer(customer);

								endUser = rs.getString(20);
								if (null == endUser
										|| InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(endUser)) {
									endUser = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setEndUser(endUser);

								tp = rs.getString(21);
								if (null == tp || InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(tp)) {
									tp = InspectionTestPlanConstants.EMPTY_STRING;
								}
								dto.setTp(tp);

								supplier = rs.getString(22);
								if (null != supplier) {
									supplier = supplier.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									supplier = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSupplier(supplier);

								supplierLocation = rs.getString(23);
								if (null != supplierLocation) {
									supplierLocation = supplierLocation.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									supplierLocation = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSupplierLocation(supplierLocation);

								subSupplierLocation = rs.getString(24);
								if (null != subSupplierLocation) {
									subSupplierLocation = subSupplierLocation.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									subSupplierLocation = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setSubSupplierLocation(subSupplierLocation);

								poWip = rs.getString(25);
								if (null != poWip) {
									poWip = poWip.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									poWip = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setPoWip(poWip);

								poLine = rs.getString(26);
								if (null != poLine) {
									poLine = poLine.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									poLine = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setPoLine(poLine);

								dto.setExpectedInspectionStartDate((null != rs.getString(27) ? rs.getString(27) : ""));

								custNotificationNo = rs.getString(28);
								if (null != custNotificationNo) {
									custNotificationNo = custNotificationNo.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									custNotificationNo = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setCustNotificationNumber(custNotificationNo);

								notificationNumber = rs.getString(29);
								if (null != notificationNumber) {
									notificationNumber = notificationNumber.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationNumber = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationNumber(notificationNumber);

								notificationStatus = rs.getString(30);
								if (null != notificationStatus) {
									notificationStatus = notificationStatus.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationStatus = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationStatus(notificationStatus);

								notificationRev = rs.getString(31);
								if (null != notificationRev) {
									notificationRev = notificationRev.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationRev = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationRevision(notificationRev);

								dto.setInspectionDate((null != rs.getString(32) ? rs.getString(32) : ""));

								inspectionDuration = rs.getString(33);
								if (null != inspectionDuration) {
									inspectionDuration = inspectionDuration.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									inspectionDuration = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setInspectionDuration(inspectionDuration);

								inspectionType = rs.getString(34);
								if (null != inspectionType) {
									inspectionType = inspectionType.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									inspectionType = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setInspectionType(inspectionType);

								notificationToCustomer = rs.getString(35);
								if (null != notificationToCustomer) {
									notificationToCustomer = notificationToCustomer.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									notificationToCustomer = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setNotificationToCustomer(notificationToCustomer);

								rc1Reference = rs.getString(36);
								if (null != rc1Reference) {
									rc1Reference = rc1Reference.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									rc1Reference = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setRc1Reference(rc1Reference);

								testResult = rs.getString(37);
								if (null != testResult) {
									testResult = testResult.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									testResult = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setTestResult(testResult);

								inspectionNotes = rs.getString(38);
								if (null != inspectionNotes) {
									inspectionNotes = inspectionNotes.replaceAll(
											InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
											InspectionTestPlanConstants.WHITE_SPACE);
								} else {
									inspectionNotes = InspectionTestPlanConstants.WHITE_SPACE;
								}
								dto.setInspectionNotes(inspectionNotes);
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting IAP Internal Details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}

				});

	}

	@Override
	public String fetchProjectName(String projectId) {

		ArrayList<Object> queryData = new ArrayList<Object>();
		queryData.add(projectId);

		return jdbcTemplate.query(InspectionTestPlanConstants.FETCH_MASTER_PROJECT_NAME, queryData.toArray(),
				new ResultSetExtractor<String>() {
					String projectName = null;

					@Override
					public String extractData(final ResultSet rs) {
						try {
							while (rs.next()) {
								projectName = rs.getString("master_project_name");
							}
						} catch (SQLException e) {
							log.error("something went wrong while fetching project name:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return projectName;
					}

				});
	}

	@Override
	public boolean insertIAPDataStageData(String projectId, List<InspectionTestPlanDTO> list, String sso) {
		int batchIndex = 0;
		boolean flag = false;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(InspectionTestPlanConstants.SAVE_INSPECTION_TEST_PLAN_EXCEL_DATA);) {
			for (InspectionTestPlanDTO dto : list) {
				pstm.setString(1, dto.getProjectId());
				pstm.setString(2, dto.getOmDescription());
				pstm.setString(3, dto.getQcpPageName());
				pstm.setString(4, dto.getCostingProject());
				pstm.setString(5, dto.getFunctionalUnit());
				pstm.setString(6, dto.getFunctionalUnitDescription());
				pstm.setString(7, dto.getPeiCode());
				pstm.setString(8, dto.getLongDummyCode());
				pstm.setString(9, dto.getItemCode());
				pstm.setString(10, dto.getItemDescription());
				pstm.setString(11, dto.getQcpDoc());
				pstm.setString(12, dto.getRevQCP());
				pstm.setString(13, dto.getRequirementId());
				pstm.setString(14, dto.getRequirementDescription());
				pstm.setString(15, dto.getStatus());
				pstm.setString(16, dto.getRefDocs());
				pstm.setString(17, dto.getProcedure());
				pstm.setString(18, dto.getAcceptance());
				pstm.setString(19, dto.getGe());
				pstm.setString(20, dto.getCustomer());
				pstm.setString(21, dto.getEndUser());
				pstm.setString(22, dto.getTp());
				pstm.setString(23, dto.getSupplier());
				pstm.setString(24, dto.getSupplierLocation());
				pstm.setString(25, dto.getSubSupplierLocation());
				pstm.setString(26, dto.getPoWip());
				pstm.setString(27, dto.getPoLine());
				pstm.setString(28, dto.getExpectedInspectionStartDate());
				pstm.setString(29, dto.getCustNotificationNumber());
				pstm.setString(30, dto.getNotificationNumber());
				pstm.setString(31, dto.getNotificationStatus());
				pstm.setString(32, dto.getNotificationRevision());
				pstm.setString(33, dto.getInspectionDate());
				pstm.setString(34, dto.getInspectionDuration());
				pstm.setString(35, dto.getInspectionType());
				pstm.setString(36, dto.getNotificationToCustomer());
				pstm.setString(37, dto.getRc1Reference());
				pstm.setString(38, dto.getTestResult());
				pstm.setString(39, dto.getInspectionNotes());
				pstm.setString(40, sso);
				pstm.addBatch();
				batchIndex++;
				if (batchIndex % 500 == 0) {
					log.info("Inserting " + batchIndex + " rows into Stage table.");
					pstm.executeBatch();
					flag = true;
				}
			}
			if (batchIndex % 500 != 0) {
				log.info("Inserting " + batchIndex + " rows into Stage table.");
				pstm.executeBatch();
				flag = true;
			}

			log.info("Insert Stage Data Status :: " + flag);

		} catch (Exception e) {
			log.error("Exception while inserting data in IAP excel stage table :: " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<KeyValueDTO> getMCCSubProjectFilter(String projectId) {
		Connection con = null;
		List<KeyValueDTO> subProjectFilterList = new ArrayList<KeyValueDTO>();
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.GET_MCC_SUB_PROJECT_FILTER);
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				KeyValueDTO dto = new KeyValueDTO();
				String subProject = rs.getString("sub_project");
				if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
					dto.setVal(subProject);
					dto.setKey(subProject);
					subProjectFilterList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Sub-Project Filter :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Sub-Project Filter :: " + e.getMessage());
				}
			}
		}
		return subProjectFilterList;
	}

	@Override
	public List<KeyValueDTO> getMCCDisciplineFilter(String projectId) {
		Connection con = null;
		List<KeyValueDTO> disciplineFilterList = new ArrayList<KeyValueDTO>();
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.GET_MCC_DISCIPLINE_FILTER);
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				KeyValueDTO dto = new KeyValueDTO();
				String discipline = rs.getString("discipline");
				if (null != discipline && !discipline.isEmpty() && !discipline.equalsIgnoreCase("")) {
					dto.setVal(discipline);
					dto.setKey(discipline);
					disciplineFilterList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Discipline Filter :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Discipline Filter :: " + e.getMessage());
				}
			}
		}
		return disciplineFilterList;
	}

	@Override
	public List<KeyValueDTO> getMCCModuleFilter(String projectId) {
		Connection con = null;
		List<KeyValueDTO> moduleFilterList = new ArrayList<KeyValueDTO>();
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.GET_MCC_MODULE_FILTER);
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				KeyValueDTO dto = new KeyValueDTO();
				String module = rs.getString("module_descrip");
				if (null != module && !module.isEmpty() && !module.equalsIgnoreCase("")) {
					dto.setVal(module);
					dto.setKey(module);
					moduleFilterList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Module Filter :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Module Filter :: " + e.getMessage());
				}
			}
		}
		return moduleFilterList;
	}

	@Override
	public List<KeyValueDTO> getMCCShowByFilter(String projectId) {
		Connection con = null;
		List<KeyValueDTO> showByFilterList = new ArrayList<KeyValueDTO>();
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.GET_MCC_SHOW_BY_FILTER);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				KeyValueDTO dto = new KeyValueDTO();
				String showBy = rs.getString("show_by");
				if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
					dto.setVal(showBy);
					dto.setKey(showBy);
					showByFilterList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Show By Filter :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Show By Filter :: " + e.getMessage());
				}
			}
		}
		return showByFilterList;
	}

	@Override
	public MCCSummaryDTO getMCCSummary(String projectId, String subProject, String discipline, String module,
			String showBy) {
		Connection con = null;
		MCCSummaryDTO summaryDTO = new MCCSummaryDTO();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] disciplineStr = discipline.split(";");
			String[] moduleStr = module.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.GET_MCC_SUMMARY_CNT);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array disciplineStrArr = con.createArrayOf("varchar", disciplineStr);
			Array moduleStrArr = con.createArrayOf("varchar", moduleStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, disciplineStrArr);
			pstm.setArray(5, disciplineStrArr);
			pstm.setString(6, showBy);
			pstm.setArray(7, moduleStrArr);
			pstm.setArray(8, moduleStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				summaryDTO.setTotal(null != rs.getString("total_cnt") ? rs.getString("total_cnt") : "");
				summaryDTO.setExecuted(null != rs.getString("executed_cnt") ? rs.getString("executed_cnt") : "");
				summaryDTO.setPercentage(null != rs.getString("executed_per") ? rs.getString("executed_per") : "");
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Summary Counts :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Summary Counts :: " + e.getMessage());
				}
			}
		}
		return summaryDTO;
	}

	@Override
	public List<MCCDetailsDTO> getMCCChartPopupData(String projectId, String subProject, String discipline,
			String module, String showBy, String type, String status, String subStatus) {
		Connection con = null;
		List<MCCDetailsDTO> list = new ArrayList<MCCDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] disciplineStr = discipline.split(";");
			String[] moduleStr = module.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			String queryString = MCCDAOHelper.getMCCChartPopupDetailsQuery(type, status, subStatus);
			PreparedStatement pstm = con.prepareStatement(queryString);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array disciplineStrArr = con.createArrayOf("varchar", disciplineStr);
			Array moduleStrArr = con.createArrayOf("varchar", moduleStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, disciplineStrArr);
			pstm.setArray(5, disciplineStrArr);
			pstm.setString(6, showBy);
			pstm.setArray(7, moduleStrArr);
			pstm.setArray(8, moduleStrArr);
			if (type.equalsIgnoreCase("CHART")) {
				pstm.setString(9, status);
			}
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MCCDetailsDTO dto = new MCCDetailsDTO();
				dto.setJob(null != rs.getString("job") ? rs.getString("job") : "");
				dto.setModule(null != rs.getString("module1") ? rs.getString("module1") : "");
				dto.setModuleDesc(null != rs.getString("module_descrip") ? rs.getString("module_descrip") : "");
				dto.setSystem(null != rs.getString("system1") ? rs.getString("system1") : "");
				dto.setSysDesc(null != rs.getString("sys_descrip") ? rs.getString("sys_descrip") : "");
				dto.setSubSystem(null != rs.getString("sub_system") ? rs.getString("sub_system") : "");
				dto.setSubSysDesc(null != rs.getString("sub_system_desc") ? rs.getString("sub_system_desc") : "");
				dto.setDiscipline(null != rs.getString("discipline") ? rs.getString("discipline") : "");
				dto.setTag(null != rs.getString("tag") ? rs.getString("tag") : "");
				dto.setTagDesc(null != rs.getString("tag_descrip") ? rs.getString("tag_descrip") : "");
				dto.setItr(null != rs.getString("itr") ? rs.getString("itr") : "");
				dto.setItrDesc(null != rs.getString("itr_descip") ? rs.getString("itr_descip") : "");
				dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Popup Details :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Popup Details :: " + e.getMessage());
				}
			}
		}
		return list;
	}

	@Override
	public MCCCheckApplicableDTO checkMCCApplicable(String projectId) {
		Connection con = null;
		MCCCheckApplicableDTO dto = new MCCCheckApplicableDTO();
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.CHECK_IF_PROJECT_IS_MODULE);
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String flag = rs.getString("flag");
				if (null != flag && !flag.isEmpty() && !flag.equalsIgnoreCase("")) {
					dto.setApplicable(flag);
					dto.setErrorMsg("");
					if (flag.equalsIgnoreCase("N")) {
						dto.setErrorMsg("This section is only applicable for module projects");
					}
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Applicable Flag :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Applicable Flag :: " + e.getMessage());
				}
			}
		}
		return dto;
	}

	@Override
	public List<MCCDetailsDTO> getMCCDetailsExcel(String projectId, String subProject, String discipline, String module,
			String showBy) {
		Connection con = null;
		List<MCCDetailsDTO> list = new ArrayList<MCCDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] disciplineStr = discipline.split(";");
			String[] moduleStr = module.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.GET_MCC_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array disciplineStrArr = con.createArrayOf("varchar", disciplineStr);
			Array moduleStrArr = con.createArrayOf("varchar", moduleStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, disciplineStrArr);
			pstm.setArray(5, disciplineStrArr);
			pstm.setString(6, showBy);
			pstm.setArray(7, moduleStrArr);
			pstm.setArray(8, moduleStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MCCDetailsDTO dto = new MCCDetailsDTO();
				dto.setJob(null != rs.getString("job") ? rs.getString("job") : "");
				dto.setModule(null != rs.getString("module1") ? rs.getString("module1") : "");
				dto.setModuleDesc(null != rs.getString("module_descrip") ? rs.getString("module_descrip") : "");
				dto.setSystem(null != rs.getString("system1") ? rs.getString("system1") : "");
				dto.setSysDesc(null != rs.getString("sys_descrip") ? rs.getString("sys_descrip") : "");
				dto.setSubSystem(null != rs.getString("sub_system") ? rs.getString("sub_system") : "");
				dto.setSubSysDesc(null != rs.getString("sub_system_desc") ? rs.getString("sub_system_desc") : "");
				dto.setDiscipline(null != rs.getString("discipline") ? rs.getString("discipline") : "");
				dto.setTag(null != rs.getString("tag") ? rs.getString("tag") : "");
				dto.setTagDesc(null != rs.getString("tag_descrip") ? rs.getString("tag_descrip") : "");
				dto.setItr(null != rs.getString("itr") ? rs.getString("itr") : "");
				dto.setItrDesc(null != rs.getString("itr_descip") ? rs.getString("itr_descip") : "");
				dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Excel Details :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Excel Details :: " + e.getMessage());
				}
			}
		}
		return list;
	}

	@Override
	public List<MCCChartDetailsYAxisDTO> getMCCChartYAxisDetails(String projectId, String subProject, String discipline,
			String module, String showBy) {
		List<MCCChartDetailsYAxisDTO> list = new ArrayList<MCCChartDetailsYAxisDTO>();

		MCCChartDetailsYAxisDTO open = new MCCChartDetailsYAxisDTO();
		open.setLegendName("Open");
		open.setKey("open");
		open.setColor("#42cbf5");
		list.add(open);

		MCCChartDetailsYAxisDTO executed = new MCCChartDetailsYAxisDTO();
		executed.setLegendName("Executed");
		executed.setKey("executed");
		executed.setColor("#31a802");
		list.add(executed);

		return list;
	}

	@Override
	public List<MCCChartDetailsXAxisDTO> getMCCChartXAxisDetails(String projectId, String subProject, String discipline,
			String module, String showBy) {
		List<MCCChartDetailsXAxisDTO> list = new ArrayList<MCCChartDetailsXAxisDTO>();
		Connection con = null;
		try {
			String[] subProjectStr = subProject.split(";");
			String[] disciplineStr = discipline.split(";");
			String[] moduleStr = module.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.GET_MCC_CHART_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array disciplineStrArr = con.createArrayOf("varchar", disciplineStr);
			Array moduleStrArr = con.createArrayOf("varchar", moduleStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, disciplineStrArr);
			pstm.setArray(5, disciplineStrArr);
			pstm.setString(6, showBy);
			pstm.setArray(7, moduleStrArr);
			pstm.setArray(8, moduleStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MCCChartDetailsXAxisDTO dto = new MCCChartDetailsXAxisDTO();
				String subSystem = rs.getString("sub_system");
				if (null != subSystem && !subSystem.isEmpty() && !subSystem.equalsIgnoreCase("")) {
					dto.setLegendName(subSystem);
					dto.setKey(subSystem);
					list.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Chart X-Axis Details :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Chart X-Axis Details :: " + e.getMessage());
				}
			}
		}
		return list;
	}

	@Override
	public Map<String, MCCChartDetailsDTO> getMCCChartDataDetails(String projectId, String subProject,
			String discipline, String module, String showBy) {
		Map<String, MCCChartDetailsDTO> map = new LinkedHashMap<String, MCCChartDetailsDTO>();
		Connection con = null;
		try {
			String[] subProjectStr = subProject.split(";");
			String[] disciplineStr = discipline.split(";");
			String[] moduleStr = module.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MCCConstants.GET_MCC_CHART_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array disciplineStrArr = con.createArrayOf("varchar", disciplineStr);
			Array moduleStrArr = con.createArrayOf("varchar", moduleStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, disciplineStrArr);
			pstm.setArray(5, disciplineStrArr);
			pstm.setString(6, showBy);
			pstm.setArray(7, moduleStrArr);
			pstm.setArray(8, moduleStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MCCChartDetailsDTO dto = new MCCChartDetailsDTO();
				String subSystem = null != rs.getString("sub_system") ? rs.getString("sub_system") : "";
				dto.setExecuted(null != rs.getString("executed_cnt") ? rs.getString("executed_cnt") : "");
				dto.setOpen(null != rs.getString("open_cnt") ? rs.getString("open_cnt") : "");
				map.put(subSystem, dto);
			}
		} catch (SQLException e) {
			log.error("Error in getting MCC Chart Details :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting MCC Chart Details :: " + e.getMessage());
				}
			}
		}
		return map;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getLastUpdatedDate(String projectId) {
		return jdbcTemplate.query(MCCConstants.GET_MCC_LAST_REFRESHED_DATE, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = "";
						while (rs.next()) {
							date = rs.getString(1);
						}
						return date;
					}
				});
	}

}
