package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.INPIProjectDetailsDAO;
import com.bh.realtrack.dto.KeyValueDTO;
import com.bh.realtrack.dto.NPIDetailsPopupDataResponse;
import com.bh.realtrack.dto.NPIDropdownResponseDTO;
import com.bh.realtrack.dto.NPIOtdTrendDTO;
import com.bh.realtrack.dto.NPIProjectDetailsDTO;
import com.bh.realtrack.dto.NPISummaryDetailsResponseDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.NPIProjectDetailsConstants;

@Repository
public class NPIProjectDetailsDAOImpl implements INPIProjectDetailsDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger log = LoggerFactory.getLogger(NPIProjectDetailsDAOImpl.class);

	@Override
	public NPIDropdownResponseDTO getNPIDropDownDetails(String projectId) throws Exception {
		List<KeyValueDTO> subProjectList = getListOfSubProjects(projectId);
		List<KeyValueDTO> ownerList = getListOfOwners(projectId);
		List<KeyValueDTO> activityGroupList = getListOfActivityGroups(projectId);

		NPIDropdownResponseDTO dropdownResponse = new NPIDropdownResponseDTO();
		dropdownResponse.setSubProject(subProjectList);
		dropdownResponse.setOwner(ownerList);
		dropdownResponse.setActivityGroup(activityGroupList);
		dropdownResponse.setDefaultActivityGroup("BOM");
		return dropdownResponse;

	}

	public List<KeyValueDTO> getListOfSubProjects(String projectId) {
		List<KeyValueDTO> listOfSubProjects = new ArrayList<KeyValueDTO>();
		return jdbcTemplate.query(NPIProjectDetailsConstants.GET_NPI_SUB_PROJECT, new Object[] { projectId },
				new ResultSetExtractor<List<KeyValueDTO>>() {

					@Override
					public List<KeyValueDTO> extractData(ResultSet rs) throws SQLException {

						while (rs.next()) {
							KeyValueDTO subProject = new KeyValueDTO();
							subProject.setKey(rs.getString(1));
							subProject.setVal(rs.getString(1));
							listOfSubProjects.add(subProject);
						}
						return listOfSubProjects;
					}
				});

	}

	public List<KeyValueDTO> getListOfOwners(String projectId) {
		List<KeyValueDTO> listOfOwners = new ArrayList<KeyValueDTO>();
		return jdbcTemplate.query(NPIProjectDetailsConstants.GET_NPI_OWNERSHIP, new Object[] { projectId },
				new ResultSetExtractor<List<KeyValueDTO>>() {

					@Override
					public List<KeyValueDTO> extractData(ResultSet rs) throws SQLException {

						while (rs.next()) {
							KeyValueDTO owner = new KeyValueDTO();
							owner.setKey(rs.getString(1));
							owner.setVal(rs.getString(1));
							listOfOwners.add(owner);
						}
						return listOfOwners;
					}
				});

	}

	public List<KeyValueDTO> getListOfActivityGroups(String projectId) {
		List<KeyValueDTO> listOfActivityGroups = new ArrayList<KeyValueDTO>();
		return jdbcTemplate.query(NPIProjectDetailsConstants.GET_NPI_ACTIVITY_GROUP, new Object[] { projectId },
				new ResultSetExtractor<List<KeyValueDTO>>() {

					@Override
					public List<KeyValueDTO> extractData(ResultSet rs) throws SQLException {

						while (rs.next()) {
							KeyValueDTO activityGroup = new KeyValueDTO();
							activityGroup.setKey(rs.getString(1));
							activityGroup.setVal(rs.getString(1));
							listOfActivityGroups.add(activityGroup);
						}
						return listOfActivityGroups;
					}
				});

	}

	@Override
	public NPISummaryDetailsResponseDTO getNPISummaryDetails(String projectId, List<String> subProjects,
			List<String> owners, List<String> activityGroups) throws Exception {
		NPISummaryDetailsResponseDTO summaryDetailsDTO = new NPISummaryDetailsResponseDTO();

		String total = getSummaryCountDetails(projectId, subProjects, owners, activityGroups,
				NPIProjectDetailsConstants.GET_SUMMARY_TOTAL);
		String backlog = getSummaryCountDetails(projectId, subProjects, owners, activityGroups,
				NPIProjectDetailsConstants.GET_SUMMARY_BACKLOG);
		String toGo = getSummaryCountDetails(projectId, subProjects, owners, activityGroups,
				NPIProjectDetailsConstants.GET_SUMMARY_TO_GO);
		String released = getSummaryCountDetails(projectId, subProjects, owners, activityGroups,
				NPIProjectDetailsConstants.GET_SUMMARY_RELEASED);
		String actualOTD = getSummaryCountDetails(projectId, subProjects, owners, activityGroups,
				NPIProjectDetailsConstants.GET_SUMMARY_ACTUAL_OTD);

		summaryDetailsDTO.setTotal(total != null ? total : "0");
		summaryDetailsDTO.setBackLog(backlog != null ? backlog : "0");
		summaryDetailsDTO.setToGo(toGo != null ? toGo : "0");
		summaryDetailsDTO.setReleased(released != null ? released : "0");
		summaryDetailsDTO.setActualOTD(actualOTD != null ? actualOTD : "0");
		return summaryDetailsDTO;

	}

	public String getSummaryCountDetails(String projectId, List<String> subProjects, List<String> owners,
			List<String> activityGroups, String query) {
		Connection con = null;
		String count = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();

			Array subProjectStrArr = con.createArrayOf("varchar", subProjects.toArray());
			Array ownersStrArr = con.createArrayOf("varchar", owners.toArray());
			Array activityGroupsStrArr = con.createArrayOf("varchar", activityGroups.toArray());
			PreparedStatement pstm = con.prepareStatement(query);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, ownersStrArr);
			pstm.setArray(5, ownersStrArr);
			pstm.setArray(6, activityGroupsStrArr);
			pstm.setArray(7, activityGroupsStrArr);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				count = String.valueOf(rs.getString(1));
			}
			return count;
		} catch (SQLException e) {
			log.error("something went wrong while getting summary details:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while getting summary details:" + e.getMessage());
				}
			}
		}

	}

	@Override
	public List<NPIDetailsPopupDataResponse> getNPIDetailsPopupData(String projectId, String chartType, String status,
			String fwWeek, List<String> subProjects, List<String> owners, List<String> activityGroups)
			throws Exception {

		List<NPIDetailsPopupDataResponse> popUpDetailsList = new ArrayList<NPIDetailsPopupDataResponse>();
		String query = null;

		if (chartType.equalsIgnoreCase("SUMMARY") && status.equalsIgnoreCase("TOTAL")) {
			query = NPIProjectDetailsConstants.GET_POPUP_SUMMARY_TOTAL;
		} else if (chartType.equalsIgnoreCase("BAR") && status.equalsIgnoreCase("BACKLOG")) {
			query = NPIProjectDetailsConstants.GET_POPUP_BAR_BACKLOG;
		} else if (chartType.equalsIgnoreCase("BAR") && status.equalsIgnoreCase("TOGO")) {
			query = NPIProjectDetailsConstants.GET_POPUP_BAR_TOGO;
		} else if (chartType.equalsIgnoreCase("BAR") && status.equalsIgnoreCase("RELEASED")) {
			query = NPIProjectDetailsConstants.GET_POPUP_BAR_RELEASED;
		}
		else if (chartType.equalsIgnoreCase("BAR") && status.equalsIgnoreCase("NOTPLANNED")) {
			query = NPIProjectDetailsConstants.GET_POPUP_BAR_NOT_PLANNED;
		}
		else {
			log.error("Incorrect chart type and status passed in input params");
		}

		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();

			Array subProjectStrArr = con.createArrayOf("varchar", subProjects.toArray());
			Array ownersStrArr = con.createArrayOf("varchar", owners.toArray());
			Array activityGroupsStrArr = con.createArrayOf("varchar", activityGroups.toArray());
			PreparedStatement pstm = con.prepareStatement(query);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, ownersStrArr);
			pstm.setArray(5, ownersStrArr);
			pstm.setArray(6, activityGroupsStrArr);
			pstm.setArray(7, activityGroupsStrArr);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				NPIDetailsPopupDataResponse popupDetailsDTO = new NPIDetailsPopupDataResponse();

				popupDetailsDTO.setProjectId(rs.getString(1) != null ? rs.getString(1) : "");
				popupDetailsDTO.setJob(rs.getString(2) != null ? rs.getString(2) : "");
				popupDetailsDTO.setDummyCode(rs.getString(3) != null ? rs.getString(3) : "");
				popupDetailsDTO.setActivityName(rs.getString(4) != null ? rs.getString(4) : "");
				popupDetailsDTO.setStatus(rs.getString(5) != null ? rs.getString(5) : "");
				popupDetailsDTO.setActType(rs.getString(6) != null ? rs.getString(6) : "");
				popupDetailsDTO.setDueDate(rs.getString(7) != null ? rs.getString(7) : "");
				popupDetailsDTO.setActualDate(rs.getString(8) != null ? rs.getString(8) : "");
				popupDetailsDTO.setForecastDate(rs.getString(9) != null ? rs.getString(9) : "");
				popupDetailsDTO.setOwnership(rs.getString(10) != null ? rs.getString(10) : "");
				popupDetailsDTO.setActivityGroup(rs.getString(11) != null ? rs.getString(11) : "");

				popUpDetailsList.add(popupDetailsDTO);
			}
			return popUpDetailsList;
		} catch (SQLException e) {
			log.error("something went wrong while getting pop up data details:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while getting pop up data details:" + e.getMessage());
				}
			}
		}

	}

	@Override
	public List<NPIOtdTrendDTO> getNPIOTDTrendDetails(String projectId, List<String> subProject, List<String> owner,
			List<String> activityGroup, String startDate, String endDate) throws Exception {

		String[] startmonthInParts = startDate.split("-");
		String startYear = startmonthInParts[1];
		String[] endmonthInParts = endDate.split("-");
		String endYear = endmonthInParts[1];
		String currentWeek = null;
		String agingApplicable = null;

		String startDt = startYear + "-" + getMonth(startDate) + "-01";
		String endDt = endYear + "-" + getMonth(endDate) + "-" + getLastDayOfMonth(endDate);

		List<NPIOtdTrendDTO> otdTrendDetailsList = new ArrayList<NPIOtdTrendDTO>();

		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();

			Array subProjectStrArr = con.createArrayOf("varchar", subProject.toArray());
			Array ownersStrArr = con.createArrayOf("varchar", owner.toArray());
			Array activityGroupsStrArr = con.createArrayOf("varchar", activityGroup.toArray());
			PreparedStatement pstm = con.prepareStatement(NPIProjectDetailsConstants.GET_OTD_GRAPH);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, ownersStrArr);
			pstm.setArray(5, ownersStrArr);
			pstm.setArray(6, activityGroupsStrArr);
			pstm.setArray(7, activityGroupsStrArr);
			pstm.setString(8, startDt);
			pstm.setString(9, endDt);
			pstm.setString(10, projectId);
			pstm.setArray(11, subProjectStrArr);
			pstm.setArray(12, subProjectStrArr);
			pstm.setArray(13, ownersStrArr);
			pstm.setArray(14, ownersStrArr);
			pstm.setArray(15, activityGroupsStrArr);
			pstm.setArray(16, activityGroupsStrArr);
			pstm.setString(17, startDt);
			pstm.setString(18, endDt);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				NPIOtdTrendDTO otdTrendDTO = new NPIOtdTrendDTO();
				currentWeek= rs.getString("current_week");
				agingApplicable = rs.getString("aging_90_applicable");
				otdTrendDTO.setTotalBacklog(rs.getString("total_backlog") != null ? rs.getString("total_backlog") : "0");
				otdTrendDTO.setTotalOpen(rs.getString("total_open") != null ? rs.getString("total_open") : "0");
				otdTrendDTO.setTotalReleased(rs.getString("total_released") != null ? rs.getString("total_released") : "0");
				otdTrendDTO.setAgingP90(rs.getString("aging_p90") != null ? rs.getString("aging_p90") : "0");
				if("N".equalsIgnoreCase(agingApplicable) || ("N".equalsIgnoreCase(currentWeek) && setNAAgingP90(subProject, owner,activityGroup)))
				{
					otdTrendDTO.setAgingP90("NA");
				}
				
				otdTrendDTO.setTotalOTD(rs.getString("total_otd") != null ? rs.getString("total_otd") : "0");
				otdTrendDTO.setWeeklyOTD(rs.getString("weekly_otd") != null ? rs.getString("weekly_otd") : "0");
				otdTrendDTO.setFwWeek(rs.getString("fw_week"));
				otdTrendDTO.setNotPlanned(rs.getString("total_not_planned") != null ? rs.getString("total_not_planned") : "0");

				otdTrendDetailsList.add(otdTrendDTO);
			}
			return otdTrendDetailsList;
		} catch (SQLException e) {
			log.error("something went wrong while getting otd trend details:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while getting otd trend details:" + e.getMessage());
				}
			}
		}

	}
	
	private boolean setNAAgingP90(List<String> subProject, List<String> owner, List<String> activityGroup) {

		boolean agingP90NA = false;

		if (subProject.contains("OVERALL") && owner.contains("OVERALL") && activityGroup.size() == 1) {
			agingP90NA = false;
		} else if ((!subProject.contains("OVERALL") && subProject.size() == 1)
				&& ( activityGroup.size() == 1) && owner.size() == 1) {
			agingP90NA = false;
		} else if (subProject.contains("OVERALL") || subProject.size() > 1) {
			agingP90NA = true;
		} else if (owner.contains("OVERALL") || owner.size() > 1) {
			agingP90NA = true;
		} else if (activityGroup.contains("OVERALL") || activityGroup.size() > 1) {
			agingP90NA = true;
		}

		return agingP90NA;
	}

	public int getMonth(String date) {
		Date monthInDate;
		SimpleDateFormat sf = new SimpleDateFormat("MMM");
		String[] dateParts = date.split("-");
		try {
			monthInDate = sf.parse(dateParts[0]);
			Calendar cal = Calendar.getInstance();
			cal.setTime(monthInDate);
			int month = cal.get(Calendar.MONTH) + 1;
			return month;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

	public int getLastDayOfMonth(String date) {
		SimpleDateFormat sf = new SimpleDateFormat("MMM-yyyy");
		try {
			Date convertedDate = sf.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(convertedDate);
			int lastDayOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			return lastDayOfMonth;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}
	
	@Override
	public String getUpdatedOnForChartData(String projectId) {
		Connection con = null;
		String updatedOn = null;
		try {

			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(NPIProjectDetailsConstants.GET_LAST_REFRESH_DATE);
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while(rs.next())
			{
				updatedOn= rs.getString(1);
			}
			return updatedOn;
		}
		catch (SQLException e) {
			log.error("something went wrong while getting last refresh date:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while getting last refresh date::" + e.getMessage());
				}
			}
		}

	}
	
	@Override
	public String getFiscalWeek() {
		Connection con = null;
		String fiscalWeek = null;
		try {

			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(NPIProjectDetailsConstants.GET_FISCAL_WEEK);
			ResultSet rs = pstm.executeQuery();
			while(rs.next())
			{
				fiscalWeek= rs.getString(1);
			}
			return fiscalWeek;
		}
		catch (SQLException e) {
			log.error("something went wrong while getting fiscal week" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while getting fiscal week" + e.getMessage());
				}
			}
		}

	}

	@Override
	public List<NPIProjectDetailsDTO> getNPIDetailsExcel(String projectId, String subProject, String owner,
			String activityGroup) {
		Connection con = null;
		List<NPIProjectDetailsDTO> excelDetails = new ArrayList<NPIProjectDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(",");
			String[] ownerStr = owner.split(",");
			String[] activityGroupStr = activityGroup.split(",");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(NPIProjectDetailsConstants.GET_NPI_PROJECT_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array ownerStrArr = con.createArrayOf("varchar", ownerStr);
			Array activityGroupStrArr = con.createArrayOf("varchar", activityGroupStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, ownerStrArr);
			pstm.setArray(5, ownerStrArr);
			pstm.setArray(6, activityGroupStrArr);
			pstm.setArray(7, activityGroupStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NPIProjectDetailsDTO excelDetailsDTO = new NPIProjectDetailsDTO();
				excelDetailsDTO.setProjectID(null != rs.getString("project_id") ? rs.getString("project_id")
						: NPIProjectDetailsConstants.EMPTY);
				excelDetailsDTO
						.setJob(null != rs.getString("sub_project") ? rs.getString("sub_project") : NPIProjectDetailsConstants.EMPTY);
				excelDetailsDTO.setDummyCode(null != rs.getString("dummy_code") ? rs.getString("dummy_code")
						: NPIProjectDetailsConstants.EMPTY);
				excelDetailsDTO.setActivityName(null != rs.getString("activity_name") ? rs.getString("activity_name")
						: NPIProjectDetailsConstants.EMPTY);
				excelDetailsDTO.setStatus(
						null != rs.getString("status") ? rs.getString("status") : NPIProjectDetailsConstants.EMPTY);
				excelDetailsDTO.setActType(
						null != rs.getString("act_type") ? rs.getString("act_type") : NPIProjectDetailsConstants.EMPTY);
				excelDetailsDTO.setDueDate(
						null != rs.getString("due_date") ? rs.getString("due_date") : NPIProjectDetailsConstants.EMPTY);
				excelDetailsDTO.setActualDate(null != rs.getString("actual_date") ? rs.getString("actual_date")
						: NPIProjectDetailsConstants.EMPTY);
				excelDetailsDTO.setForecastDate(null != rs.getString("forecast_date") ? rs.getString("forecast_date") : "");
				excelDetailsDTO.setOwnership(null != rs.getString("owner_ship") ? rs.getString("owner_ship") : "");
				excelDetailsDTO.setActivityGroup(null != rs.getString("activity_group") ? rs.getString("activity_group") : "");
				excelDetailsDTO.setActivityGroupDisplay(null != rs.getString("activity_group_display") ? rs.getString("activity_group_display") : "");
				excelDetails.add(excelDetailsDTO);
			}
		} catch (SQLException e) {
			log.error("Error occured while downloading NPI Project details   " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error occured while downloading NPI Project details " + e.getMessage());
				}
			}
		}
		return excelDetails;
	}

}
