package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IExecutionDao;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ExecutionDTO;
import com.bh.realtrack.dto.ExecutionDates;
import com.bh.realtrack.dto.ExecutionDropDown;
import com.bh.realtrack.dto.ExecutionFinishDTO;
import com.bh.realtrack.dto.ExecutionFunctionFilter;
import com.bh.realtrack.dto.ExecutionNCDTO;
import com.bh.realtrack.dto.ExecutionStartDTO;
import com.bh.realtrack.dto.KpiDTO;
import com.bh.realtrack.dto.OTDTrendChartDetailsDTO;
import com.bh.realtrack.dto.ProgressDates;
import com.bh.realtrack.dto.ScurveProgressDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.ExecutionConstants;

/**
 *
 * @author Shweta Sawant
 *
 */
@Repository
@SuppressWarnings("deprecation")
public class ExecutionDAOImpl implements IExecutionDao {
	private static Logger log = LoggerFactory.getLogger(ExecutionDAOImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ExecutionDTO> getExecutionChart(String projectId, String department, String subProject,
			String floatType, String weight, String milestone, String otd, String ippActivityType,
			String functionGroup) {
		List<ExecutionDTO> executionList = new ArrayList<ExecutionDTO>();
		return jdbcTemplate.query(ExecutionConstants.GET_EXECUTION_DETAILS,
				new Object[] {projectId,milestone,subProject,department,functionGroup,floatType,weight,ippActivityType,otd},
				new ResultSetExtractor<List<ExecutionDTO>>() {
					public List<ExecutionDTO> extractData(ResultSet rs) throws SQLException {
						
						try {

							while (rs.next()) {
								ExecutionDTO executionDTO = new ExecutionDTO();
								executionDTO.setPl(null != rs.getString(1) ? rs.getString(1) : "");
								executionDTO.setSubpl(null != rs.getString(2) ? rs.getString(2) : "");
								executionDTO.setProductLine(null != rs.getString(3) ? rs.getString(3) : "");
								executionDTO.setMasterProjectName(null != rs.getString(4) ? rs.getString(4) : "");
								executionDTO.setProjectId(null != rs.getString(5) ? rs.getString(5) : "");
								executionDTO.setSubProjectName(null != rs.getString(6) ? rs.getString(6) : "");
								executionDTO.setDepartment(null != rs.getString(7) ? rs.getString(7) : "");
								executionDTO.setActivityType(null != rs.getString(8) ? rs.getString(8) : "");
								executionDTO.setWbsCbs(null != rs.getString(9) ? rs.getString(9) : "");
								executionDTO.setWeightUsd(rs.getDouble(10));
								executionDTO.setTotalFloatDays(rs.getDouble(11));
								executionDTO.setFloatType(null != rs.getString(12) ? rs.getString(12) : "");
								executionDTO.setActivityId(null != rs.getString(13) ? rs.getString(13) : "");
								executionDTO.setActivityName(null != rs.getString(14) ? rs.getString(14) : "");
								executionDTO.setBaselineStartDate(null != rs.getString(15) ? rs.getString(15) : "");
								executionDTO.setForecastStartDate(null != rs.getString(16) ? rs.getString(16) : "");
								executionDTO.setActualStartDate(null != rs.getString(17) ? rs.getString(17) : "");
								executionDTO.setBaselineFinishDate(null != rs.getString(18) ? rs.getString(18) : "");
								executionDTO.setForecastFinishDate(null != rs.getString(19) ? rs.getString(19) : "");
								executionDTO.setActualFinishDate(null != rs.getString(20) ? rs.getString(20) : "");
								executionDTO.setTimingStart(null != rs.getString(21) ? rs.getString(21) : "");
								executionDTO.setDaysLateStart(rs.getInt(22));
								executionDTO.setOtdStart(null != rs.getString(23) ? rs.getString(23) : "");
								executionDTO.setDueStart(null != rs.getString(24) ? rs.getString(24) : "");
								executionDTO.setTimingFinish(null != rs.getString(25) ? rs.getString(25) : "");
								executionDTO.setDaysLateFinish(rs.getInt(26));
								executionDTO.setOtdFinish(null != rs.getString(27) ? rs.getString(27) : "");
								executionDTO.setDueFinish(null != rs.getString(28) ? rs.getString(28) : "");
								executionDTO.setWeight(null != rs.getString(29) ? rs.getString(29) : "");
								executionDTO.setWeightpercentage(null != rs.getString(30) ? rs.getString(30) : "");
								executionDTO.setEngLobCodes(null != rs.getString(31) ? rs.getString(31) : "");
								executionDTO.setMilestoneType(null != rs.getString(32) ? rs.getString(32) : "");
								executionDTO.setFunctionGroup(null != rs.getString(33) ? rs.getString(33) : "");
								executionDTO.setDataDate(null != rs.getString(34) ? rs.getString(34) : "");
								executionList.add(executionDTO);

							}
						} catch (SQLException e) {
							log.error("something went wrong while getting execution details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return executionList;
					}
				});
	}

	@Override
	public List<ExecutionStartDTO> getExecutionStartChart(String projectId, String department, String subProject,
			String floatType, String weight, String milestone, String otd, String ippActivityType,
			String functionGroup) {

		return jdbcTemplate.query(ExecutionConstants.GET_EXECUTION_START_DETAILS,
				new Object[] {projectId,milestone,subProject,department,functionGroup,floatType,weight,ippActivityType,otd },
				new ResultSetExtractor<List<ExecutionStartDTO>>() {
					public List<ExecutionStartDTO> extractData(ResultSet rs) throws SQLException {
						List<ExecutionStartDTO> executionList = new ArrayList<ExecutionStartDTO>();
						try {
							while (rs.next()) {
								ExecutionStartDTO executionDTO = new ExecutionStartDTO();

								executionDTO.setPl(null != rs.getString(1) ? rs.getString(1) : "");
								executionDTO.setSubpl(null != rs.getString(2) ? rs.getString(2) : "");
								executionDTO.setProductLine(null != rs.getString(3) ? rs.getString(3) : "");
								executionDTO.setMasterProjectName(null != rs.getString(4) ? rs.getString(4) : "");
								executionDTO.setProjectId(null != rs.getString(5) ? rs.getString(5) : "");
								executionDTO.setSubProjectName(null != rs.getString(6) ? rs.getString(6) : "");
								executionDTO.setDepartment(null != rs.getString(7) ? rs.getString(7) : "");
								executionDTO.setActivityType(null != rs.getString(8) ? rs.getString(8) : "");
								executionDTO.setWbsCbs(null != rs.getString(9) ? rs.getString(9) : "");
								executionDTO.setWeightUsd(rs.getDouble(10));
								executionDTO.setTotalFloatDays(rs.getDouble(11));
								executionDTO.setFloatType(null != rs.getString(12) ? rs.getString(12) : "");
								executionDTO.setActivityId(null != rs.getString(13) ? rs.getString(13) : "");
								executionDTO.setActivityName(null != rs.getString(14) ? rs.getString(14) : "");
								executionDTO.setBaselineStartDate(null != rs.getString(15) ? rs.getString(15) : "");
								executionDTO.setForecastStartDate(null != rs.getString(16) ? rs.getString(16) : "");
								executionDTO.setActualStartDate(null != rs.getString(17) ? rs.getString(17) : "");
								executionDTO.setTimingStart(null != rs.getString(18) ? rs.getString(18) : "");
								executionDTO.setDaysLateStart(rs.getInt(19));
								executionDTO.setOtdStart(null != rs.getString(20) ? rs.getString(20) : "");
								executionDTO.setDueStart(null != rs.getString(21) ? rs.getString(21) : "");
								executionDTO.setWeight(null != rs.getString(22) ? rs.getString(22) : "");
								executionDTO.setWeightpercentage(rs.getDouble(23));
								executionDTO.setEngLobCodes(null != rs.getString(24) ? rs.getString(24) : "");
								executionDTO.setDataDate(null != rs.getString(25) ? rs.getString(25) : "");
								executionList.add(executionDTO);

							}
						} catch (SQLException e) {
							log.error("something went wrong while getting execution details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return executionList;
					}
				});
	}

	@Override
	public List<ExecutionFinishDTO> getExecutionFinishChart(String projectId, String department, String subProject,
			String floatType, String weight, String milestone, String otd, String ippActivityType,
			String functionGroup) {
		List<ExecutionFinishDTO> executionList = new ArrayList<ExecutionFinishDTO>();
		return jdbcTemplate.query(ExecutionConstants.GET_EXECUTION_FINISH_DETAILS,
				new Object[] {projectId,milestone,subProject,department,functionGroup,floatType,weight,ippActivityType,otd},
				new ResultSetExtractor<List<ExecutionFinishDTO>>() {
					public List<ExecutionFinishDTO> extractData(ResultSet rs) throws SQLException {
						
						try {
							while (rs.next()) {
								ExecutionFinishDTO executionDTO = new ExecutionFinishDTO();

								executionDTO.setPl(null != rs.getString(1) ? rs.getString(1) : "");
								executionDTO.setSubpl(null != rs.getString(2) ? rs.getString(2) : "");
								executionDTO.setProductLine(null != rs.getString(3) ? rs.getString(3) : "");
								executionDTO.setMasterProjectName(null != rs.getString(4) ? rs.getString(4) : "");
								executionDTO.setProjectId(null != rs.getString(5) ? rs.getString(5) : "");
								executionDTO.setSubProjectName(null != rs.getString(6) ? rs.getString(6) : "");
								executionDTO.setDepartment(null != rs.getString(7) ? rs.getString(7) : "");
								executionDTO.setActivityType(null != rs.getString(8) ? rs.getString(8) : "");
								executionDTO.setWbsCbs(null != rs.getString(9) ? rs.getString(9) : "");
								executionDTO.setWeightUsd(rs.getDouble(10));
								executionDTO.setTotalFloatDays(rs.getDouble(11));
								executionDTO.setFloatType(null != rs.getString(12) ? rs.getString(12) : "");
								executionDTO.setActivityId(null != rs.getString(13) ? rs.getString(13) : "");
								executionDTO.setActivityName(null != rs.getString(14) ? rs.getString(14) : "");
								executionDTO.setBaselineFinishDate(null != rs.getString(15) ? rs.getString(15) : "");
								executionDTO.setForecastFinishDate(null != rs.getString(16) ? rs.getString(16) : "");
								executionDTO.setActualFinishDate(null != rs.getString(17) ? rs.getString(17) : "");
								executionDTO.setTimingFinish(null != rs.getString(18) ? rs.getString(18) : "");
								executionDTO.setDaysLateFinish(rs.getInt(19));
								executionDTO.setOtdFinish(null != rs.getString(20) ? rs.getString(20) : "");
								executionDTO.setDueFinish(null != rs.getString(21) ? rs.getString(21) : "");
								executionDTO.setWeight(null != rs.getString(22) ? rs.getString(22) : "");
								executionDTO.setWeightpercentage(rs.getDouble(23));
								executionDTO.setEngLobCodes(null != rs.getString(24) ? rs.getString(24) : "");
								executionDTO.setDataDate(null != rs.getString(25) ? rs.getString(25) : "");
								executionList.add(executionDTO);

							}
						} catch (SQLException e) {
							log.error("something went wrong while getting execution details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return executionList;
					}
				});
	}

	@Override
	public List<ExecutionDropDown> getSubProjectFilter(String projectId, String department) {
		return jdbcTemplate.query(ExecutionConstants.GET_SUB_PROJECT_FILTER,
				new Object[] { projectId, department, department }, new ResultSetExtractor<List<ExecutionDropDown>>() {
					public List<ExecutionDropDown> extractData(ResultSet rs) throws SQLException {
						List<ExecutionDropDown> list = new ArrayList<ExecutionDropDown>();
						try {
							while (rs.next()) {
								ExecutionDropDown obj = new ExecutionDropDown();
								obj.setProjectId(null != rs.getString(1) ? rs.getString(1) : "");
								obj.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
								list.add(obj);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting sub project filter:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<String> getFloatTypeFilter(String projectId) {
		return jdbcTemplate.query(ExecutionConstants.GET_FLOAT_FILTER, new Object[] { projectId },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								String obj = (null != rs.getString(1) ? rs.getString(1) : "");
								list.add(obj);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting float type filter:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<ProgressDates> getProgressDates(String projectId) {
		return jdbcTemplate.query(ExecutionConstants.GET_PROGRESS_DATES, new Object[] { projectId },
				new ResultSetExtractor<List<ProgressDates>>() {
					public List<ProgressDates> extractData(ResultSet rs) throws SQLException {
						List<ProgressDates> list = new ArrayList<ProgressDates>();
						try {
							while (rs.next()) {
								ProgressDates dto = new ProgressDates();
								dto.setDataDt(null != rs.getString(1) ? rs.getString(1) : "");
								dto.setProgressDt(null != rs.getString(2) ? rs.getString(2) : "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting progress Dates:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<ScurveProgressDTO> getExecutionProgress(String projectId, String department, String subProject,
			String dataDate, String weekDate) {

		List<ScurveProgressDTO> list = new ArrayList<ScurveProgressDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(ExecutionConstants.GET_EXECUTION_PROGRESS);
			Array subProjectArray = con.createArrayOf("varchar", subProject.split(","));
			pstm.setString(1, dataDate);
			pstm.setString(2, dataDate);
			pstm.setString(3, dataDate);
			pstm.setString(4, dataDate);
			pstm.setString(5, projectId);
			pstm.setArray(6, subProjectArray);
			pstm.setArray(7, subProjectArray);
			pstm.setString(8, projectId);
			pstm.setArray(9, subProjectArray);
			pstm.setArray(10, subProjectArray);
			pstm.setString(11, projectId);
			pstm.setArray(12, subProjectArray);
			pstm.setArray(13, subProjectArray);
			pstm.setString(14, dataDate);
			pstm.setString(15, weekDate);
			pstm.setString(16, department);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ScurveProgressDTO dto = new ScurveProgressDTO();
				dto.setPlannedProgress(null != rs.getString(1) ? rs.getString(1) : "");
				dto.setActualProgress(null != rs.getString(2) ? rs.getString(2) : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("something went wrong while getting activity type details:" + e.getMessage());
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
		return list;
	}

	@Override
	public List<String> getWeightFilter(String projectId) {
		return jdbcTemplate.query(ExecutionConstants.GET_WEIGHT_FILTER, new Object[] { projectId },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								String obj = (null != rs.getString(1) ? rs.getString(1) : "");
								list.add(obj);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting weight filter:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<ExecutionDates> getExecutionDates(String projectId) {
		return jdbcTemplate.query(ExecutionConstants.GET_EXECUTION_DATES, new Object[] { projectId, projectId },
				new ResultSetExtractor<List<ExecutionDates>>() {
					public List<ExecutionDates> extractData(ResultSet rs) throws SQLException {
						List<ExecutionDates> list = new ArrayList<ExecutionDates>();
						try {
							while (rs.next()) {
								ExecutionDates dto = new ExecutionDates();
								dto.setLastRefreshDate(null != rs.getString(1) ? rs.getString(1) : "");
								dto.setDataDate(null != rs.getString(2) ? rs.getString(2) : "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting execution Dates:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<ExecutionNCDTO> getExecutionForNC(String projectId, String role, String companyId, String ncrType,
			String criticality, String subProject, String organizationName) {
		int compId = 0;
		if (companyId != null && !companyId.isEmpty()) {
			compId = Integer.parseInt(companyId);
		}
		return jdbcTemplate.query(
				ExecutionConstants.GET_EXECUTION_NC_DETAILS, new Object[] { projectId, projectId, compId, role, role,
						subProject, subProject, ncrType, ncrType, criticality, criticality, organizationName,organizationName },
				new ResultSetExtractor<List<ExecutionNCDTO>>() {
					public List<ExecutionNCDTO> extractData(ResultSet rs) throws SQLException {
						List<ExecutionNCDTO> executionList = new ArrayList<ExecutionNCDTO>();
						try {
							while (rs.next()) {
								ExecutionNCDTO executionDTO = new ExecutionNCDTO();
								executionDTO.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								executionDTO.setSubProject(
										null != rs.getString("subproject") ? rs.getString("subproject") : "");
								executionDTO.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								executionDTO.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								executionDTO.setNcrNumber(
										null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								executionDTO.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								executionDTO.setnCRType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								executionDTO.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								executionDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								executionDTO.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								executionDTO.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								executionDTO.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								executionDTO.setPartNumber(
										null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								executionDTO.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								executionDTO.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								executionDTO.setDisposition(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								executionDTO.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								executionDTO.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								executionDTO.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								executionDTO.setCauseCode(
										null != rs.getString("causecode") ? rs.getString("causecode") : "");
								executionDTO.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								executionDTO.setPeiCode(null != rs.getString("peicode") ? rs.getString("peicode") : "");
								executionDTO.setOmId(null != rs.getString("omid") ? rs.getString("omid") : "");
								executionDTO.setOmDescription(
										null != rs.getString("omdescription") ? rs.getString("omdescription") : "");
								executionDTO.setLocation(
										null != rs.getString("location") ? rs.getString("location") : "");
								executionDTO.setSubLocation(
										null != rs.getString("sub_location") ? rs.getString("sub_location") : "");	
								executionDTO.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname") : "");
								executionList.add(executionDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting execution details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return executionList;
					}
				});
	}

	@Override
	public List<ExecutionNCDTO> getExecutionForNCForTPS(String projectId, String role, String companyId, String ncrType, String criticality, String subProject) {
		int compId = 0;
		if (companyId != null && !companyId.isEmpty()) {
			compId = Integer.parseInt(companyId);
		}
		return jdbcTemplate.query(
				ExecutionConstants.GET_EXECUTION_NC_DETAILS_FOR_TPS, new Object[] { projectId, projectId, compId, role, role,
						subProject, subProject, ncrType, ncrType, criticality, criticality },
				new ResultSetExtractor<List<ExecutionNCDTO>>() {
					public List<ExecutionNCDTO> extractData(ResultSet rs) throws SQLException {
						List<ExecutionNCDTO> executionList = new ArrayList<ExecutionNCDTO>();
						try {
							while (rs.next()) {
								ExecutionNCDTO executionDTO = new ExecutionNCDTO();
								executionDTO.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								executionDTO.setSubProject(
										null != rs.getString("subproject") ? rs.getString("subproject") : "");
								executionDTO.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								executionDTO.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								executionDTO.setNcrNumber(
										null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								executionDTO.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								executionDTO.setnCRType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								executionDTO.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								executionDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								executionDTO.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								executionDTO.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								executionDTO.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								executionDTO.setPartNumber(
										null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								executionDTO.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								executionDTO.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								executionDTO.setDisposition(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								executionDTO.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								executionDTO.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								executionDTO.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								executionDTO.setCauseCode(
										null != rs.getString("causecode") ? rs.getString("causecode") : "");
								executionDTO.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								executionDTO.setPeiCode(null != rs.getString("peicode") ? rs.getString("peicode") : "");
								executionDTO.setOmId(null != rs.getString("omid") ? rs.getString("omid") : "");
								executionDTO.setOmDescription(
										null != rs.getString("omdescription") ? rs.getString("omdescription") : "");
								executionDTO.setLocation(
										null != rs.getString("location") ? rs.getString("location") : "");
								executionDTO.setSubLocation(
										null != rs.getString("sub_location") ? rs.getString("sub_location") : "");
								executionDTO.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname") : "");
								executionList.add(executionDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting execution details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return executionList;
					}
				});
	}

	@Override
	public String getRole(String sso) {
		return jdbcTemplate.query(ExecutionConstants.GET_ROLE, new Object[] { sso }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = new String();
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {
					log.error("something went wrong while getting inspection log details:" + e.getMessage());
					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public List<ExecutionFunctionFilter> getFunctionFilter(String projectId) {
		return jdbcTemplate.query(ExecutionConstants.GET_FUNCTION_FILTER, new Object[] { projectId },
				new ResultSetExtractor<List<ExecutionFunctionFilter>>() {
					public List<ExecutionFunctionFilter> extractData(ResultSet rs) throws SQLException {
						List<ExecutionFunctionFilter> list = new ArrayList<ExecutionFunctionFilter>();
						try {
							while (rs.next()) {
								ExecutionFunctionFilter obj = new ExecutionFunctionFilter();
								obj.setDepartment(null != rs.getString(1) ? rs.getString(1) : "");
								obj.setActivityDep(null != rs.getString(2) ? rs.getString(2) : "");
								list.add(obj);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting function filter:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<String> getMilestone() {
		return jdbcTemplate.query(ExecutionConstants.GET_MILESTONE_FILTER, new Object[] {},
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								String obj = (null != rs.getString(1) ? rs.getString(1) : "");
								list.add(obj);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting Milestone filter:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<String> getOtd() {
		return jdbcTemplate.query(ExecutionConstants.GET_OTD_FILTER, new Object[] {},
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								String obj = (null != rs.getString(1) ? rs.getString(1) : "");
								list.add(obj);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting Milestone filter:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<KpiDTO> getKpi(String projectId, String department, String subProject, String floatType,
			String weightType, String milestone, String otd, String ippActivityType, String functionGroup) {

		return jdbcTemplate.query(ExecutionConstants.GET_KPI,
				new Object[] { milestone, milestone, milestone, milestone, milestone, milestone, milestone, milestone,
						milestone, functionGroup, functionGroup, functionGroup, projectId, department, department,
						department, department, subProject, projectId, subProject, floatType, floatType, weightType,
						weightType, ippActivityType, ippActivityType },

				new ResultSetExtractor<List<KpiDTO>>() {

					public List<KpiDTO> extractData(ResultSet rs) throws SQLException {

						List<KpiDTO> list = new ArrayList<KpiDTO>();
						try {
							while (rs.next()) {
								KpiDTO kpi = new KpiDTO();
								kpi.setActualOtd(null != rs.getString(1) ? rs.getString(1) : "");
								kpi.setEtcOtd(null != rs.getString(2) ? rs.getString(2) : "");
								kpi.setEacOtd(null != rs.getString(3) ? rs.getString(3) : "");

								list.add(kpi);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting 3 KPi:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<OTDTrendChartDetailsDTO> getOTDTrendChartData(String projectId, String department, String subProject,
			String floatType, String weightType, String milestone, String otd, String ippActivityType,
			String functionGroup) {

		return jdbcTemplate.query(ExecutionConstants.GET_OTD_TREND_MILISTONE_DATA,
				new Object[] { milestone, milestone, milestone, milestone, milestone, milestone, milestone, milestone,
						milestone, functionGroup, functionGroup, functionGroup, projectId, department, department,
						subProject, projectId, subProject, floatType, floatType, weightType,
						weightType, otd, otd, otd, otd, ippActivityType, ippActivityType },
				new ResultSetExtractor<List<OTDTrendChartDetailsDTO>>() {
					public List<OTDTrendChartDetailsDTO> extractData(ResultSet rs) throws SQLException {

						List<OTDTrendChartDetailsDTO> otdTrendChartDetailsDTOList = new ArrayList<OTDTrendChartDetailsDTO>();
						OTDTrendChartDetailsDTO otdTrendChartDetailsDTO = null;

						try {

							while (rs.next()) {

								otdTrendChartDetailsDTO = new OTDTrendChartDetailsDTO();
								otdTrendChartDetailsDTO.setWeekDate(rs.getString("hist_date"));

								otdTrendChartDetailsDTO.setActualOnTime(rs.getString("ontime_and_due"));
								otdTrendChartDetailsDTO.setEacOnTime(rs.getString("ontime_milistone"));
								otdTrendChartDetailsDTO.setEtcOnTime(rs.getString("ontime_and_notdue"));

								otdTrendChartDetailsDTO.setOntimeMilistone(rs.getString("ontime_milistone"));
								otdTrendChartDetailsDTO.setTotalMilistone(rs.getString("total_milistone"));
								otdTrendChartDetailsDTO
										.setOntimeTotalPercentage(rs.getString("on_time_milistone_perc"));
								otdTrendChartDetailsDTO.setTotalDueMilistone(rs.getString("due_milistone"));
								otdTrendChartDetailsDTO.setActualOTD(rs.getString("Actual OTD"));
								otdTrendChartDetailsDTO.setEtcOTD(rs.getString("ETC OTD"));
								otdTrendChartDetailsDTO.setEacOTD(rs.getString("EAC OTD"));

								otdTrendChartDetailsDTOList.add(otdTrendChartDetailsDTO);

							}

						} catch (SQLException e) {

							log.error("something went wrong while getting OTD Trend chart:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return otdTrendChartDetailsDTOList;
					}
				});
	}

	@Override
	public List<String> getIppActivityType(String projectId) {
		return jdbcTemplate.query(ExecutionConstants.GET_IPP_ACTIVITY_TYPE_FILTER, new Object[] { projectId },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								String obj = (null != rs.getString(1) ? rs.getString(1) : "");
								list.add(obj);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting IppActivityType:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<String> getFunctionGroup(String projectId) {
		return jdbcTemplate.query(ExecutionConstants.GET_FUNCTION_GROUP_FILTER, new Object[] { projectId },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								String obj = (null != rs.getString(1) ? rs.getString(1) : "");
								list.add(obj);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting IppActivityType:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<DropDownDTO> getMilestoneList(String projectId) {
		return jdbcTemplate.query(ExecutionConstants.GET_MILESTONE_LIST_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						try {
							while (rs.next()) {
								DropDownDTO dto = new DropDownDTO();
								dto.setVal(null != rs.getString(1) ? rs.getString(1) : "");
								dto.setKey(null != rs.getString(2) ? rs.getString(2) : "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting IppActivityType:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<ExecutionStartDTO> downloadExecutionStartChartPopupExcel(String projectId, String department, String subProject, String floatType,
																		 String weightType, String milestone,
																		 String otd, String ippActivityType, String functionGroup,
																		 String otdStart, String timingStart ) {

		return jdbcTemplate.query(ExecutionConstants.GET_EXECUTION_START_POPUP_EXCEL_DOWNLOAD,
				new Object[] {projectId,milestone,subProject,department,functionGroup,floatType,weightType,ippActivityType,otd,otdStart,timingStart },
				new ResultSetExtractor<List<ExecutionStartDTO>>() {
					public List<ExecutionStartDTO> extractData(ResultSet rs) throws SQLException {
						List<ExecutionStartDTO> executionList = new ArrayList<ExecutionStartDTO>();
						try {
							while (rs.next()) {
								ExecutionStartDTO executionDTO = new ExecutionStartDTO();

								executionDTO.setMasterProjectName(null!=rs.getString("master_project_name_out") ? rs.getString("master_project_name_out") : "" );
								executionDTO.setProjectId(null!=rs.getString("project_id_out") ? rs.getString("project_id_out") : "" );
								executionDTO.setSubProjectName(null!=rs.getString("sub_project_name_out") ? rs.getString("sub_project_name_out") : "" );
								executionDTO.setActivityId(null!=rs.getString("activity_id_out") ? rs.getString("activity_id_out") : "" );
								executionDTO.setActivityName(null!=rs.getString("activity_name_out") ? rs.getString("activity_name_out") : "" );
								executionDTO.setProductLine(null!=rs.getString("product_line_out") ? rs.getString("product_line_out") : "" );
								executionDTO.setDepartment(null!=rs.getString("department_out") ? rs.getString("department_out") : "" );
								executionDTO.setWeight(null!=rs.getString("weight_out") ? rs.getString("weight_out") : "" );
								executionDTO.setFloatType(null!=rs.getString("float_type_out") ? rs.getString("float_type_out") : "" );
								executionDTO.setTotalFloatDays(rs.getDouble("total_float_days_out"));
								executionDTO.setWeightpercentage(rs.getDouble("weightpercentage_out"));
								executionDTO.setBaselineStartDate(null!=rs.getString("baseline_finish_date_out") ? rs.getString("baseline_finish_date_out") : "" );
								executionDTO.setForecastStartDate(null!=rs.getString("forecast_finish_date_out") ? rs.getString("forecast_finish_date_out") : "" );
								executionDTO.setActualStartDate(null!=rs.getString("actual_finish_date_out") ? rs.getString("actual_finish_date_out") : "" );
								executionDTO.setDaysLateStart(rs.getInt("days_late_finish_out"));
								executionDTO.setOtdStart(null!=rs.getString("otd_finish_out") ? rs.getString("otd_finish_out") : "" );
								executionDTO.setTimingStart(null!=rs.getString("timing_finish_out") ? rs.getString("timing_finish_out") : "" );
								executionDTO.setWbsCbs(null!=rs.getString("wbs_cbs_out") ? rs.getString("wbs_cbs_out") : "" );
								executionDTO.setActivityType(null!=rs.getString("activity_type_out") ? rs.getString("activity_type_out") : "" );
								executionDTO.setEngLobCodes(null!=rs.getString("eng_lob_codes_out") ? rs.getString("eng_lob_codes_out") : "" );
								executionDTO.setDataDate(null!=rs.getString("data_date_out") ? rs.getString("data_date_out"):"");
								executionList.add(executionDTO);

							}
						} catch (SQLException e) {
							log.error("something went wrong while getting execution popup details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return executionList;
					}
				});
	}

	@Override
	public List<ExecutionFinishDTO> downloadExecutionFinishChartPopupExcel(String projectId, String department, String subProject, String floatType,
																		   String weightType, String milestone,
																		   String otd, String ippActivityType, String functionGroup,
																		   String otdFinish, String timingFinish) {

		return jdbcTemplate.query(ExecutionConstants.GET_EXECUTION_FINISH_POPUP_EXCEL_DOWNLOAD,
				new Object[] {projectId,milestone,subProject,department,functionGroup,floatType,weightType,ippActivityType,otd,otdFinish,timingFinish },
				new ResultSetExtractor<List<ExecutionFinishDTO>>() {
					public List<ExecutionFinishDTO> extractData(ResultSet rs) throws SQLException {
						List<ExecutionFinishDTO> executionList = new ArrayList<ExecutionFinishDTO>();
						try {
							while (rs.next()) {
								ExecutionFinishDTO executionDTO = new ExecutionFinishDTO();

								executionDTO.setMasterProjectName(null!=rs.getString("master_project_name_out") ? rs.getString("master_project_name_out") : "" );
								executionDTO.setProjectId(null!=rs.getString("project_id_out") ? rs.getString("project_id_out") : "" );
								executionDTO.setSubProjectName(null!=rs.getString("sub_project_name_out") ? rs.getString("sub_project_name_out") : "" );
								executionDTO.setActivityId(null!=rs.getString("activity_id_out") ? rs.getString("activity_id_out") : "" );
								executionDTO.setActivityName(null!=rs.getString("activity_name_out") ? rs.getString("activity_name_out") : "" );
								executionDTO.setProductLine(null!=rs.getString("product_line_out") ? rs.getString("product_line_out") : "" );
								executionDTO.setDepartment(null!=rs.getString("department_out") ? rs.getString("department_out") : "" );
								executionDTO.setWeight(null!=rs.getString("weight_out") ? rs.getString("weight_out") : "" );
								executionDTO.setFloatType(null!=rs.getString("float_type_out") ? rs.getString("float_type_out") : "" );
								executionDTO.setTotalFloatDays(rs.getDouble("total_float_days_out"));
								executionDTO.setWeightpercentage(rs.getDouble("weightpercentage_out"));
								executionDTO.setBaselineFinishDate(null!=rs.getString("baseline_finish_date_out") ? rs.getString("baseline_finish_date_out") : "" );
								executionDTO.setForecastFinishDate(null!=rs.getString("forecast_finish_date_out") ? rs.getString("forecast_finish_date_out") : "" );
								executionDTO.setActualFinishDate(null!=rs.getString("actual_finish_date_out") ? rs.getString("actual_finish_date_out") : "" );
								executionDTO.setDaysLateFinish(rs.getInt("days_late_finish_out"));
								executionDTO.setOtdFinish(null!=rs.getString("otd_finish_out") ? rs.getString("otd_finish_out") : "" );
								executionDTO.setTimingFinish(null!=rs.getString("timing_finish_out") ? rs.getString("timing_finish_out") : "" );
								executionDTO.setWbsCbs(null!=rs.getString("wbs_cbs_out") ? rs.getString("wbs_cbs_out") : "" );
								executionDTO.setActivityType(null!=rs.getString("activity_type_out") ? rs.getString("activity_type_out") : "" );
								executionDTO.setEngLobCodes(null!=rs.getString("eng_lob_codes_out") ? rs.getString("eng_lob_codes_out") : "" );
								executionDTO.setDataDate(null!=rs.getString("data_date_out") ? rs.getString("data_date_out"):"");
								executionList.add(executionDTO);

							}
						} catch (SQLException e) {
							log.error("something went wrong while getting execution popup details:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return executionList;
					}
				});
	}



}