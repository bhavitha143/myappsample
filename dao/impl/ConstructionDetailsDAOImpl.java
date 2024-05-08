package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IConstructionDetailsDAO;
import com.bh.realtrack.dto.ActivityDetailsDTO;
import com.bh.realtrack.dto.ActivityDetailsParamsDTO;
import com.bh.realtrack.dto.ActivityListDTO;
import com.bh.realtrack.dto.ConstructionDetailsDTO;
import com.bh.realtrack.dto.DepartmentDetailsDTO;
import com.bh.realtrack.dto.JobDetailsDTO;
import com.bh.realtrack.dto.PubWeightDTO;
import com.bh.realtrack.dto.TrainDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.ConstructionConstants;

/**
 * @author Anand Kumar
 *
 */
@Repository
public class ConstructionDetailsDAOImpl implements IConstructionDetailsDAO {
	private static Logger log = LoggerFactory
			.getLogger(ConstructionDetailsDAOImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ConstructionDetailsDTO> getConstructionDetailsData(
			String projectId, String trains, String departments,
			String filterFlag, String intExtFlag,
			Map<String, Object> selectedMap) {

		@SuppressWarnings("unchecked")
		List<String> jobNumberList = (List<String>) selectedMap
				.get("jobNumberList");

		String epsContractId = selectedMap.get("contractId").toString();
		String weightFlag = selectedMap.get("weightFlag").toString();
		String wbsCustomWtFlag = selectedMap.get("wbsCustomWt").toString();
		String wbsOberallDeptFlag = selectedMap.get("wbsOverallDeptWt")
				.toString();
		String wbsSelectedDeptFlag = "Y";
		if (departments.contains(",") || departments.equals("OVERALL")) {
			wbsSelectedDeptFlag = "N";
		}
		String[] jobNumArray = getJobNumbers(jobNumberList);
		String[] epsArray = new String[] { selectedMap.get("contractId")
				.toString() };
		String[] deptArray = departments.split(",");
		String[] trainArray = trains.split(",");

		List<ConstructionDetailsDTO> constructionDetails = new ArrayList<ConstructionDetailsDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_PROJECT_LEVEL_DETAILS);
			Array jobNums = con.createArrayOf("varchar", jobNumArray);
			Array tr = con.createArrayOf("varchar", trainArray);
			Array dept = con.createArrayOf("varchar", deptArray);
			Array epsId = con.createArrayOf("varchar", epsArray);

			pstm.setString(1, projectId);
			pstm.setArray(2, epsId);
			pstm.setArray(3, jobNums);
			pstm.setString(4, filterFlag);
			pstm.setString(5, weightFlag);
			pstm.setString(6, intExtFlag);
			pstm.setArray(7, tr);
			pstm.setArray(8, dept);
			pstm.setString(9, wbsCustomWtFlag);
			pstm.setString(10, wbsOberallDeptFlag);
			pstm.setString(11, wbsSelectedDeptFlag);
			ResultSet rs = pstm.executeQuery();

			List<TrainDetailsDTO> trainList = getTrainDetailsData(projectId,
					trainArray, deptArray, epsArray, jobNumArray, filterFlag,
					weightFlag, intExtFlag, wbsCustomWtFlag,
					wbsOberallDeptFlag, wbsSelectedDeptFlag);
			List<JobDetailsDTO> jobList = getJobDetailList(projectId,
					trainArray, deptArray, epsArray, jobNumArray, filterFlag,
					weightFlag, intExtFlag, wbsCustomWtFlag,
					wbsOberallDeptFlag, wbsSelectedDeptFlag);
			List<DepartmentDetailsDTO> departmentList = getDepartmentDetailList(
					projectId, trainArray, deptArray, epsArray, jobNumArray,
					filterFlag, weightFlag, intExtFlag, wbsCustomWtFlag,
					wbsOberallDeptFlag, wbsSelectedDeptFlag);
			List<ActivityListDTO> activityList = getActivityTypeData(projectId,
					epsArray, jobNumArray, filterFlag, weightFlag, intExtFlag,
					trainArray, deptArray);

			while (rs.next()) {
				ConstructionDetailsDTO constructionDetailsDTO = new ConstructionDetailsDTO();
				constructionDetailsDTO.setContractId(rs.getString(1));
				constructionDetailsDTO.setProgress(Math.round(rs.getDouble(2)));

				List<TrainDetailsDTO> trainResponse = new ArrayList<TrainDetailsDTO>();
				for (int i = 0; i < trainList.size(); i++) {
					String train = trainList.get(i).getTrain();

					TrainDetailsDTO trainDto = new TrainDetailsDTO();
					trainDto.setTrain(train);
					trainDto.setStartDate(trainList.get(i).getStartDate());
					trainDto.setFinishDate(trainList.get(i).getFinishDate());
					trainDto.setProgress(trainList.get(i).getProgress());

					List<JobDetailsDTO> jobResponse = new ArrayList<JobDetailsDTO>();
					for (int j = 0; j < jobList.size(); j++) {
						if (train.equals(jobList.get(j).getTrain())) {
							String jobNumber = jobList.get(j).getJobNumber();

							JobDetailsDTO jobDto = new JobDetailsDTO();
							jobDto.setTrain(jobList.get(j).getTrain());
							jobDto.setJobNumber(jobNumber);
							jobDto.setStartDate(jobList.get(j).getStartDate());
							jobDto.setFinishDate(jobList.get(j).getFinishDate());
							jobDto.setProgress(jobList.get(j).getProgress());

							List<DepartmentDetailsDTO> departmentResponse = new ArrayList<DepartmentDetailsDTO>();
							for (int k = 0; k < departmentList.size(); k++) {
								if (jobNumber.equals(departmentList.get(k)
										.getJobNumber())) {
									String department = departmentList.get(k)
											.getDepartment();

									DepartmentDetailsDTO deptDto = new DepartmentDetailsDTO();
									deptDto.setTrain(departmentList.get(k)
											.getTrain());
									deptDto.setJobNumber(departmentList.get(k)
											.getJobNumber());
									deptDto.setDepartment(departmentList.get(k)
											.getDepartment());
									deptDto.setStartDate(departmentList.get(k)
											.getStartDate());
									deptDto.setFinishDate(departmentList.get(k)
											.getFinishDate());
									deptDto.setProgress(departmentList.get(k)
											.getProgress());

									List<ActivityListDTO> activityResponse = new ArrayList<ActivityListDTO>();
									for (int l = 0; l < activityList.size(); l++) {
										if (department.equals(activityList
												.get(l)
												.getActivityDetailsParams()
												.getDepartment())
												&& departmentList
														.get(k)
														.getTrain()
														.equals(activityList
																.get(l)
																.getActivityDetailsParams()
																.getTrain())
												&& departmentList
														.get(k)
														.getJobNumber()
														.equals(activityList
																.get(l)
																.getActivityDetailsParams()
																.getJobNumber())) {

											ActivityListDTO alDto = new ActivityListDTO();
											ActivityDetailsParamsDTO adpDto = new ActivityDetailsParamsDTO();

											alDto.setActivityTypeDescription(activityList
													.get(l)
													.getActivityTypeDescription());
											alDto.setStartDate(activityList
													.get(l).getStartDate());
											alDto.setFinishDate(activityList
													.get(l).getFinishDate());
											alDto.setWeightActType(activityList
													.get(l).getWeightActType());
											alDto.setWeightOnOverall(activityList
													.get(l)
													.getWeightOnOverall());
											alDto.setProgress(activityList.get(
													l).getProgress());
											alDto.setOverallProgress(activityList
													.get(l)
													.getOverallProgress());

											adpDto.setEpsContractId(epsContractId);
											adpDto.setTrain(activityList.get(l)
													.getActivityDetailsParams()
													.getTrain());
											adpDto.setJobNumber(activityList
													.get(l)
													.getActivityDetailsParams()
													.getJobNumber());
											adpDto.setDepartment(activityList
													.get(l)
													.getActivityDetailsParams()
													.getDepartment());
											adpDto.setWeightFlag(weightFlag);

											alDto.setActivityDetailsParams(adpDto);
											activityResponse.add(alDto);
										}

									}
									deptDto.setActivityList(activityResponse);
									departmentResponse.add(deptDto);

								}
							}
							jobDto.setDepartmentList(departmentResponse);
							jobResponse.add(jobDto);

						}
					}
					trainDto.setJobList(jobResponse);
					trainResponse.add(trainDto);

				}

				constructionDetailsDTO.setTrainList(trainResponse);
				constructionDetails.add(constructionDetailsDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting project details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return constructionDetails;
	}

	private List<TrainDetailsDTO> getTrainDetailsData(String projectId,
			String[] train, String[] department, String[] epsContractId,
			String[] jobNumbers, String filterFlag, String weightFlag,
			String intExtFlag, String wbsCustomWtFlag,
			String wbsOberallDeptFlag, String wbsSelectedDeptFlag) {

		List<TrainDetailsDTO> list = new ArrayList<TrainDetailsDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_TRAIN_LEVEL_DETAILS);
			Array jobNums = con.createArrayOf("varchar", jobNumbers);
			Array tr = con.createArrayOf("varchar", train);
			Array dept = con.createArrayOf("varchar", department);
			Array eps = con.createArrayOf("varchar", epsContractId);

			pstm.setString(1, projectId);
			pstm.setArray(2, eps);
			pstm.setArray(3, jobNums);
			pstm.setString(4, filterFlag);
			pstm.setString(5, weightFlag);
			pstm.setString(6, intExtFlag);
			pstm.setArray(7, tr);
			pstm.setArray(8, dept);
			pstm.setString(9, wbsCustomWtFlag);
			pstm.setString(10, wbsOberallDeptFlag);
			pstm.setString(11, wbsSelectedDeptFlag);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				TrainDetailsDTO trainDetailsDTO = new TrainDetailsDTO();
				trainDetailsDTO.setTrain(rs.getString(1));
				trainDetailsDTO.setStartDate(rs.getString(2));
				trainDetailsDTO.setFinishDate(rs.getString(3));
				trainDetailsDTO.setProgress(Math.round(rs.getDouble(4)));
				list.add(trainDetailsDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting train details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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

	private List<JobDetailsDTO> getJobDetailList(String projectId,
			String[] train, String[] department, String[] epsContractId,
			String[] jobNumbers, String filterFlag, String weightFlag,
			String intExtFlag, String wbsCustomWtFlag,
			String wbsOberallDeptFlag, String wbsSelectedDeptFlag) {

		List<JobDetailsDTO> list = new ArrayList<JobDetailsDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_JOB_LEVEL_DETAILS);
			Array jobNums = con.createArrayOf("varchar", jobNumbers);
			Array tr = con.createArrayOf("varchar", train);
			Array dept = con.createArrayOf("varchar", department);
			Array eps = con.createArrayOf("varchar", epsContractId);

			pstm.setString(1, projectId);
			pstm.setArray(2, eps);
			pstm.setArray(3, jobNums);
			pstm.setString(4, filterFlag);
			pstm.setString(5, weightFlag);
			pstm.setString(6, intExtFlag);
			pstm.setArray(7, tr);
			pstm.setArray(8, dept);
			pstm.setString(9, wbsCustomWtFlag);
			pstm.setString(10, wbsOberallDeptFlag);
			pstm.setString(11, wbsSelectedDeptFlag);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				JobDetailsDTO jobDetailsDTO = new JobDetailsDTO();
				jobDetailsDTO.setTrain(rs.getString(1));
				jobDetailsDTO.setJobNumber(rs.getString(2));
				jobDetailsDTO.setStartDate(rs.getString(3));
				jobDetailsDTO.setFinishDate(rs.getString(4));
				jobDetailsDTO.setProgress(Math.round(rs.getDouble(5)));
				list.add(jobDetailsDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting job details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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

	private List<DepartmentDetailsDTO> getDepartmentDetailList(
			String projectId, String[] train, String[] department,
			String[] epsContractId, String[] jobNumbers, String filterFlag,
			String weightFlag, String intExtFlag, String wbsCustomWtFlag,
			String wbsOberallDeptFlag, String wbsSelectedDeptFlag) {

		List<DepartmentDetailsDTO> list = new ArrayList<DepartmentDetailsDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_DEPARTMENT_LEVEL_DETAILS);
			Array jobNums = con.createArrayOf("varchar", jobNumbers);
			Array tr = con.createArrayOf("varchar", train);
			Array dept = con.createArrayOf("varchar", department);
			Array eps = con.createArrayOf("varchar", epsContractId);

			pstm.setString(1, projectId);
			pstm.setArray(2, eps);
			pstm.setArray(3, jobNums);
			pstm.setString(4, filterFlag);
			pstm.setString(5, weightFlag);
			pstm.setString(6, intExtFlag);
			pstm.setArray(7, tr);
			pstm.setArray(8, dept);
			pstm.setString(9, wbsCustomWtFlag);
			pstm.setString(10, wbsOberallDeptFlag);
			pstm.setString(11, wbsSelectedDeptFlag);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				DepartmentDetailsDTO departmentDetailsDTO = new DepartmentDetailsDTO();
				departmentDetailsDTO.setTrain(rs.getString(1));
				departmentDetailsDTO.setJobNumber(rs.getString(2));
				departmentDetailsDTO.setDepartment(rs.getString(3));
				departmentDetailsDTO.setStartDate(rs.getString(4));
				departmentDetailsDTO.setFinishDate(rs.getString(5));
				departmentDetailsDTO.setProgress(Math.round(rs.getDouble(6)));
				list.add(departmentDetailsDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting department details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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

	private List<ActivityListDTO> getActivityTypeData(String projectId,
			String[] epsContractId, String[] jobNumbers, String filterFlag,
			String weightFlag, String intExtFlag, String[] train,
			String[] department) {

		List<ActivityListDTO> list = new ArrayList<ActivityListDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_ACTIVITY_TYPE_DETAILS);
			Array jobNums = con.createArrayOf("varchar", jobNumbers);
			Array tr = con.createArrayOf("varchar", train);
			Array dept = con.createArrayOf("varchar", department);
			Array eps = con.createArrayOf("varchar", epsContractId);

			pstm.setString(1, projectId);
			pstm.setArray(2, eps);
			pstm.setArray(3, jobNums);
			pstm.setString(4, filterFlag);
			pstm.setString(5, weightFlag);
			pstm.setString(6, intExtFlag);
			pstm.setArray(7, tr);
			pstm.setArray(8, tr);
			pstm.setArray(9, dept);
			pstm.setArray(10, dept);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				ActivityListDTO activityListDTO = new ActivityListDTO();
				ActivityDetailsParamsDTO activityDetailsParamsDTO = new ActivityDetailsParamsDTO();
				activityDetailsParamsDTO.setTrain(rs.getString(1));
				activityDetailsParamsDTO.setJobNumber(rs.getString(2));
				activityDetailsParamsDTO.setDepartment(rs.getString(3));

				activityListDTO
						.setActivityDetailsParams(activityDetailsParamsDTO);
				activityListDTO.setActivityTypeDescription(rs.getString(4));
				activityListDTO.setStartDate(rs.getString(5));
				activityListDTO.setFinishDate(rs.getString(6));
				activityListDTO.setWeightActType(rs.getLong(7));
				activityListDTO.setWeightOnOverall(rs.getDouble(8));
				activityListDTO.setProgress(Math.round(rs.getDouble(9)));
				activityListDTO.setOverallProgress(rs.getDouble(10));

				list.add(activityListDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting activity type details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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
	public List<ActivityDetailsDTO> getActivityDetailsData(String projectId,
			String epsContractId, String jobNumber, String filterFlag,
			String weightFlag, String intExtFlag, String department,
			String train, String activityTypeDesc) {

		return jdbcTemplate.query(
				ConstructionConstants.GET_ACTIVITY_POP_UP_DETAILS,
				new Object[] { filterFlag, weightFlag, intExtFlag, department,
						projectId, jobNumber, train, epsContractId,
						activityTypeDesc },
				new ResultSetExtractor<List<ActivityDetailsDTO>>() {
					@Override
					public List<ActivityDetailsDTO> extractData(
							final ResultSet rs) {
						List<ActivityDetailsDTO> list = new ArrayList<ActivityDetailsDTO>();
						try {
							while (rs.next()) {
								ActivityDetailsDTO activityDetailsATDTO = new ActivityDetailsDTO();
								activityDetailsATDTO.setJobNumber(rs
										.getString(1));
								activityDetailsATDTO.setActivityId(rs
										.getString(2));
								activityDetailsATDTO.setActivityName(rs
										.getString(3));
								activityDetailsATDTO.setOrigDuration(rs
										.getString(4));
								activityDetailsATDTO.setStartDate(rs
										.getString(5));
								activityDetailsATDTO.setFinishDate(rs
										.getString(6));
								activityDetailsATDTO.setTotalFloat(rs
										.getString(7));
								activityDetailsATDTO.setWeight(rs.getDouble(8));
								activityDetailsATDTO
										.setActivityPercntComplete(rs
												.getString(9));
								activityDetailsATDTO.setActivityStatus(rs
										.getString(10));
								activityDetailsATDTO.setStatusFlag(rs
										.getString(11));

								list.add(activityDetailsATDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting activity details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL
									.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public Map<String, Object> getConstructionFiltersData(String projectId,
			String intExtFlag, Map<String, Object> selectedMap) {
		Map<String, Object> constructionFiltersMap = new HashMap<String, Object>();
		List<String> jobNumberList = (List<String>) selectedMap
				.get("jobNumberList");
		List<String> filterFlagList = (List<String>) selectedMap
				.get("filterFlag");

		String[] jobNumbers = getJobNumbers(jobNumberList);
		String[] epsContractId = new String[] { selectedMap.get("contractId")
				.toString() };
		String weightFlag = selectedMap.get("weightFlag").toString();
		String filterFlag = filterFlagList.get(0);

		if (filterFlag.equalsIgnoreCase("Main Items+Internal Item")) {
			filterFlag = "MI";
		} else if (filterFlag.equalsIgnoreCase("All")) {
			filterFlag = "A";
		} else if (filterFlag.equalsIgnoreCase("Main Items")) {
			filterFlag = "M";
		}
		List<String> departmentFilterList = getDepartmentFiltersData(projectId,
				epsContractId, jobNumbers, filterFlag, weightFlag, intExtFlag);

		List<String> trainFilterList = getTrainFiltersData(projectId,
				epsContractId, jobNumbers, filterFlag, weightFlag, intExtFlag);
		constructionFiltersMap.put("departmentFilters", departmentFilterList);
		constructionFiltersMap.put("trainFilters", trainFilterList);
		constructionFiltersMap.put("filterFlagFilters", filterFlagList);
		return constructionFiltersMap;
	}

	private String[] getJobNumbers(List<String> jobNumberList) {
		String[] jobNumbers = new String[jobNumberList.size()];
		for (int i = 0; i < jobNumbers.length; i++) {
			jobNumbers[i] = jobNumberList.get(i);
		}

		return jobNumbers;
	}

	private List<String> getDepartmentFiltersData(String projectId,
			String[] epsContractId, String[] jobNumbers, String filterFlag,
			String weightFlag, String intExtFlag) {

		List<String> list = new ArrayList<String>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_DEPARTMENT_FILTERS_DETAILS);
			Array jobNums = con.createArrayOf("varchar", jobNumbers);
			Array eps = con.createArrayOf("varchar", epsContractId);

			pstm.setString(1, projectId);
			pstm.setArray(2, eps);
			pstm.setArray(3, jobNums);
			pstm.setString(4, filterFlag);
			pstm.setString(5, weightFlag);
			pstm.setString(6, intExtFlag);

			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				list.add(rs.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting department filters details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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

	private List<String> getTrainFiltersData(String projectId,
			String[] epsContractId, String[] jobNumbers, String filterFlag,
			String weightFlag, String intExtFlag) {

		List<String> list = new ArrayList<String>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_TRAIN_FILTERS_DETAILS);
			Array jobNums = con.createArrayOf("varchar", jobNumbers);
			Array eps = con.createArrayOf("varchar", epsContractId);

			pstm.setString(1, projectId);
			pstm.setArray(2, eps);
			pstm.setArray(3, jobNums);
			pstm.setString(4, filterFlag);
			pstm.setString(5, weightFlag);
			pstm.setString(6, intExtFlag);

			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				list.add(rs.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting train filters details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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
	public List<String> getConstructionParamsData(String projectId) {
		return jdbcTemplate.query(
				ConstructionConstants.GET_CONSTRUCTION_PARAMS,
				new Object[] { projectId },
				new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(final ResultSet rs) {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								list.add(rs.getString(1));
							}
						} catch (SQLException e) {
							log.error("something went wrong while gettting construction params details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL
									.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public Map<String, Object> getPublishFiltersData(String projectId,
			String intExtFlag) {
		// TODO Auto-generated method stub
		Map<String, Object> publishFiltersMap = new HashMap<String, Object>();
		List<String> departmentPublishFilters = getPublishDepartmentFilters(
				projectId, intExtFlag);
		List<String> apiPublishFilters = getPublishAPIFilters(projectId,
				intExtFlag);
		List<String> trainPublishFilters = getPublishTrainFilters(projectId,
				intExtFlag);
		publishFiltersMap.put("departmentFilters", departmentPublishFilters);
		publishFiltersMap.put("filterFlagFilters", apiPublishFilters);
		publishFiltersMap.put("trainFilters", trainPublishFilters);
		return publishFiltersMap;
	}

	private List<String> getPublishAPIFilters(String projectId,
			String intExtFlag) {
		return jdbcTemplate.query(ConstructionConstants.GET_PUBLISH_API_FILTER,
				new Object[] { projectId, intExtFlag },
				new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(final ResultSet rs) {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								list.add(rs.getString(1));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting api filters:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL
									.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	private List<String> getPublishDepartmentFilters(String projectId,
			String intExtFlag) {
		return jdbcTemplate.query(
				ConstructionConstants.GET_PUBLISH_DEPARTMENT_FILTER,
				new Object[] { projectId, intExtFlag },
				new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(final ResultSet rs) {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								list.add(rs.getString(1));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting publish department filters:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL
									.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	public List<String> getPublishTrainFilters(String projectId,
			String intExtFlag) {
		return jdbcTemplate.query(
				ConstructionConstants.GET_PUBLISH_TRAIN_FILTER, new Object[] {
						projectId, intExtFlag },
				new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(final ResultSet rs) {
						List<String> list = new ArrayList<String>();
						try {
							while (rs.next()) {
								list.add(rs.getString(1));
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting publish train filters:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL
									.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ConstructionDetailsDTO> getPublishDetailsData(String projectId,
			String apiFilter, String intExtFlag, String trains,
			String departments) {

		PubWeightDTO pubWeight = getPubWeightDetails(projectId, intExtFlag);

		String wbsCustomWt = pubWeight.getWbsCustomWt();
		String wbsOverallDetp = pubWeight.getWbsOverallDept();
		String epsContractId = pubWeight.getEpsContractId();
		String weightFlag = pubWeight.getActivityCustomWeight();

		String wbsSelectedDeptFlag = "Y";
		if (departments.contains(",") || departments.equals("OVERALL")) {
			wbsSelectedDeptFlag = "N";
		}

		String[] deptArray = departments.split(",");
		String[] trainArray = trains.split(",");
		String[] epsArray = new String[] { epsContractId };

		List<ConstructionDetailsDTO> constructionDetails = new ArrayList<ConstructionDetailsDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_PUBLISH_PROJECT_LEVEL_DETAILS);
			Array tr = con.createArrayOf("varchar", trainArray);
			Array dept = con.createArrayOf("varchar", deptArray);

			pstm.setString(1, projectId);
			pstm.setString(2, apiFilter);
			pstm.setString(3, intExtFlag);
			pstm.setArray(4, tr);
			pstm.setArray(5, dept);
			pstm.setString(6, wbsCustomWt);
			pstm.setString(7, wbsOverallDetp);
			pstm.setString(8, wbsSelectedDeptFlag);
			ResultSet rs = pstm.executeQuery();

			List<TrainDetailsDTO> trainList = getPublishTrainDetails(projectId,
					apiFilter, intExtFlag, trainArray, deptArray, wbsCustomWt,
					wbsOverallDetp, wbsSelectedDeptFlag);
			List<JobDetailsDTO> jobList = getPublishJobDetails(projectId,
					apiFilter, intExtFlag, trainArray, deptArray, wbsCustomWt,
					wbsOverallDetp, wbsSelectedDeptFlag);
			List<DepartmentDetailsDTO> departmentList = getPublishDepartmentDetail(
					projectId, apiFilter, intExtFlag, trainArray, deptArray,
					wbsCustomWt, wbsOverallDetp, wbsSelectedDeptFlag);
			List<ActivityListDTO> activityList = getPublishActivityTypeDetails(
					projectId, epsArray, apiFilter, weightFlag, intExtFlag,
					trainArray, deptArray);

			while (rs.next()) {
				ConstructionDetailsDTO constructionDetailsDTO = new ConstructionDetailsDTO();
				constructionDetailsDTO.setContractId(rs.getString(1));
				constructionDetailsDTO.setProgress(Math.round(rs.getDouble(2)));

				List<TrainDetailsDTO> trainResponse = new ArrayList<TrainDetailsDTO>();
				for (int i = 0; i < trainList.size(); i++) {
					String train = trainList.get(i).getTrain();

					TrainDetailsDTO trainDto = new TrainDetailsDTO();
					trainDto.setTrain(train);
					trainDto.setStartDate(trainList.get(i).getStartDate());
					trainDto.setFinishDate(trainList.get(i).getFinishDate());
					trainDto.setProgress(trainList.get(i).getProgress());

					List<JobDetailsDTO> jobResponse = new ArrayList<JobDetailsDTO>();
					for (int j = 0; j < jobList.size(); j++) {
						if (train.equals(jobList.get(j).getTrain())) {
							String jobNumber = jobList.get(j).getJobNumber();

							JobDetailsDTO jobDto = new JobDetailsDTO();
							jobDto.setTrain(jobList.get(j).getTrain());
							jobDto.setJobNumber(jobNumber);
							jobDto.setStartDate(jobList.get(j).getStartDate());
							jobDto.setFinishDate(jobList.get(j).getFinishDate());
							jobDto.setProgress(jobList.get(j).getProgress());

							List<DepartmentDetailsDTO> departmentResponse = new ArrayList<DepartmentDetailsDTO>();
							for (int k = 0; k < departmentList.size(); k++) {
								if (jobNumber.equals(departmentList.get(k)
										.getJobNumber())) {
									String department = departmentList.get(k)
											.getDepartment();

									DepartmentDetailsDTO deptDto = new DepartmentDetailsDTO();
									deptDto.setTrain(departmentList.get(k)
											.getTrain());
									deptDto.setJobNumber(departmentList.get(k)
											.getJobNumber());
									deptDto.setDepartment(departmentList.get(k)
											.getDepartment());
									deptDto.setStartDate(departmentList.get(k)
											.getStartDate());
									deptDto.setFinishDate(departmentList.get(k)
											.getFinishDate());
									deptDto.setProgress(departmentList.get(k)
											.getProgress());

									List<ActivityListDTO> activityResponse = new ArrayList<ActivityListDTO>();
									for (int l = 0; l < activityList.size(); l++) {
										if (department.equals(activityList
												.get(l)
												.getActivityDetailsParams()
												.getDepartment())
												&& departmentList
														.get(k)
														.getTrain()
														.equals(activityList
																.get(l)
																.getActivityDetailsParams()
																.getTrain())
												&& departmentList
														.get(k)
														.getJobNumber()
														.equals(activityList
																.get(l)
																.getActivityDetailsParams()
																.getJobNumber())) {

											ActivityListDTO alDto = new ActivityListDTO();
											ActivityDetailsParamsDTO adpDto = new ActivityDetailsParamsDTO();

											alDto.setActivityTypeDescription(activityList
													.get(l)
													.getActivityTypeDescription());
											alDto.setStartDate(activityList
													.get(l).getStartDate());
											alDto.setFinishDate(activityList
													.get(l).getFinishDate());
											alDto.setWeightActType(activityList
													.get(l).getWeightActType());
											alDto.setWeightOnOverall(activityList
													.get(l)
													.getWeightOnOverall());
											alDto.setProgress(activityList.get(
													l).getProgress());
											alDto.setOverallProgress(activityList
													.get(l)
													.getOverallProgress());

											adpDto.setEpsContractId(epsContractId);
											adpDto.setTrain(activityList.get(l)
													.getActivityDetailsParams()
													.getTrain());
											adpDto.setJobNumber(activityList
													.get(l)
													.getActivityDetailsParams()
													.getJobNumber());
											adpDto.setDepartment(activityList
													.get(l)
													.getActivityDetailsParams()
													.getDepartment());
											adpDto.setWeightFlag(weightFlag);

											alDto.setActivityDetailsParams(adpDto);
											activityResponse.add(alDto);
										}

									}
									deptDto.setActivityList(activityResponse);
									departmentResponse.add(deptDto);

								}
							}
							jobDto.setDepartmentList(departmentResponse);
							jobResponse.add(jobDto);

						}
					}
					trainDto.setJobList(jobResponse);
					trainResponse.add(trainDto);

				}

				constructionDetailsDTO.setTrainList(trainResponse);
				constructionDetails.add(constructionDetailsDTO);
			}

		} catch (SQLException e) {
			log.error("something went wrong while getting publish project details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return constructionDetails;
	}

	private List<TrainDetailsDTO> getPublishTrainDetails(String projectId,
			String apiFilter, String intExtFlag, String[] train,
			String[] department, String wbsCustomWt, String wbsOverallDetp,
			String wbsSelectedDeptFlag) {

		List<TrainDetailsDTO> list = new ArrayList<TrainDetailsDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_PUBLISH_TRAIN_LEVEL_DETAILS);
			Array tr = con.createArrayOf("varchar", train);
			Array dept = con.createArrayOf("varchar", department);

			pstm.setString(1, projectId);
			pstm.setString(2, apiFilter);
			pstm.setString(3, intExtFlag);
			pstm.setArray(4, tr);
			pstm.setArray(5, dept);
			pstm.setString(6, wbsCustomWt);
			pstm.setString(7, wbsOverallDetp);
			pstm.setString(8, wbsSelectedDeptFlag);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				TrainDetailsDTO trainDetailsDTO = new TrainDetailsDTO();
				trainDetailsDTO.setTrain(rs.getString(1));
				trainDetailsDTO.setStartDate(rs.getString(2));
				trainDetailsDTO.setFinishDate(rs.getString(3));
				trainDetailsDTO.setProgress(Math.round(rs.getDouble(4)));
				list.add(trainDetailsDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting publish train details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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

	private List<JobDetailsDTO> getPublishJobDetails(String projectId,
			String apiFilter, String intExtFlag, String[] train,
			String[] department, String wbsCustomWt, String wbsOverallDetp,
			String wbsSelectedDeptFlag) {

		List<JobDetailsDTO> list = new ArrayList<JobDetailsDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_PUBLISH_JOB_LEVEL_DETAILS);
			Array tr = con.createArrayOf("varchar", train);
			Array dept = con.createArrayOf("varchar", department);

			pstm.setString(1, projectId);
			pstm.setString(2, apiFilter);
			pstm.setString(3, intExtFlag);
			pstm.setArray(4, tr);
			pstm.setArray(5, dept);
			pstm.setString(6, wbsCustomWt);
			pstm.setString(7, wbsOverallDetp);
			pstm.setString(8, wbsSelectedDeptFlag);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				JobDetailsDTO jobDetailsDTO = new JobDetailsDTO();
				jobDetailsDTO.setTrain(rs.getString(1));
				jobDetailsDTO.setJobNumber(rs.getString(2));
				jobDetailsDTO.setStartDate(rs.getString(3));
				jobDetailsDTO.setFinishDate(rs.getString(4));
				jobDetailsDTO.setProgress(Math.round(rs.getDouble(5)));
				list.add(jobDetailsDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting publish job details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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

	private List<DepartmentDetailsDTO> getPublishDepartmentDetail(
			String projectId, String apiFilter, String intExtFlag,
			String[] train, String[] department, String wbsCustomWt,
			String wbsOverallDetp, String wbsSelectedDeptFlag) {

		List<DepartmentDetailsDTO> list = new ArrayList<DepartmentDetailsDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_PUBLISH_DEPARTMENT_LEVEL_DETAILS);
			Array tr = con.createArrayOf("varchar", train);
			Array dept = con.createArrayOf("varchar", department);

			pstm.setString(1, projectId);
			pstm.setString(2, apiFilter);
			pstm.setString(3, intExtFlag);
			pstm.setArray(4, tr);
			pstm.setArray(5, dept);
			pstm.setString(6, wbsCustomWt);
			pstm.setString(7, wbsOverallDetp);
			pstm.setString(8, wbsSelectedDeptFlag);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				DepartmentDetailsDTO departmentDetailsDTO = new DepartmentDetailsDTO();
				departmentDetailsDTO.setTrain(rs.getString(1));
				departmentDetailsDTO.setJobNumber(rs.getString(2));
				departmentDetailsDTO.setDepartment(rs.getString(3));
				departmentDetailsDTO.setStartDate(rs.getString(4));
				departmentDetailsDTO.setFinishDate(rs.getString(5));
				departmentDetailsDTO.setProgress(Math.round(rs.getDouble(6)));
				list.add(departmentDetailsDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting publish department details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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

	private List<ActivityListDTO> getPublishActivityTypeDetails(
			String projectId, String[] epsContractId, String filterFlag,
			String weightFlag, String intExtFlag, String[] train,
			String[] department) {

		List<ActivityListDTO> list = new ArrayList<ActivityListDTO>();
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ConstructionConstants.GET_PUBLISH_ACTIVITY_TYPE_DETAILS);
			Array tr = con.createArrayOf("varchar", train);
			Array dept = con.createArrayOf("varchar", department);
			Array eps = con.createArrayOf("varchar", epsContractId);

			pstm.setString(1, projectId);
			pstm.setArray(2, eps);
			pstm.setString(3, filterFlag);
			pstm.setString(4, weightFlag);
			pstm.setString(5, intExtFlag);
			pstm.setArray(6, tr);
			pstm.setArray(7, tr);
			pstm.setArray(8, dept);
			pstm.setArray(9, dept);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				ActivityListDTO activityListDTO = new ActivityListDTO();
				ActivityDetailsParamsDTO activityDetailsParamsDTO = new ActivityDetailsParamsDTO();
				activityDetailsParamsDTO.setTrain(rs.getString(1));
				activityDetailsParamsDTO.setJobNumber(rs.getString(2));
				activityDetailsParamsDTO.setDepartment(rs.getString(3));

				activityListDTO
						.setActivityDetailsParams(activityDetailsParamsDTO);
				activityListDTO.setActivityTypeDescription(rs.getString(4));
				activityListDTO.setStartDate(rs.getString(5));
				activityListDTO.setFinishDate(rs.getString(6));
				activityListDTO.setWeightActType(rs.getLong(7));
				activityListDTO.setWeightOnOverall(rs.getDouble(8));
				activityListDTO.setProgress(Math.round(rs.getDouble(9)));
				activityListDTO.setOverallProgress(rs.getDouble(10));

				list.add(activityListDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while getting publish activity type details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
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
	public List<ActivityDetailsDTO> getPublishActivityDetailsData(
			String projectId, String epsContractId, String jobNumber,
			String apiFilter, String weightFlag, String intExtFlag,
			String department, String train, String activityTypeDesc) {

		return jdbcTemplate.query(
				ConstructionConstants.GET_PUBLISH_ACTIVITY_POP_UP_DETAILS,
				new Object[] { apiFilter, weightFlag, intExtFlag, department,
						projectId, jobNumber, train, epsContractId,
						activityTypeDesc },
				new ResultSetExtractor<List<ActivityDetailsDTO>>() {
					@Override
					public List<ActivityDetailsDTO> extractData(
							final ResultSet rs) {
						List<ActivityDetailsDTO> list = new ArrayList<ActivityDetailsDTO>();
						try {
							while (rs.next()) {
								ActivityDetailsDTO activityDetailsATDTO = new ActivityDetailsDTO();
								activityDetailsATDTO.setJobNumber(rs
										.getString(1));
								activityDetailsATDTO.setActivityId(rs
										.getString(2));
								activityDetailsATDTO.setActivityName(rs
										.getString(3));
								activityDetailsATDTO.setOrigDuration(rs
										.getString(4));
								activityDetailsATDTO.setStartDate(rs
										.getString(5));
								activityDetailsATDTO.setFinishDate(rs
										.getString(6));
								activityDetailsATDTO.setTotalFloat(rs
										.getString(7));
								activityDetailsATDTO.setWeight(rs.getDouble(8));
								activityDetailsATDTO
										.setActivityPercntComplete(rs
												.getString(9));
								activityDetailsATDTO.setActivityStatus(rs
										.getString(10));
								activityDetailsATDTO.setStatusFlag(rs
										.getString(11));

								list.add(activityDetailsATDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting activity publish details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL
									.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public Map<String, String> saveLatestPublish(String ssoId,
			String projectId, String intExtFlag, Map<String, Object> selectedMap) {
		Map<String, String> map = new HashMap<String, String>();
		List<String> jobNumberList = (List<String>) selectedMap
				.get("jobNumberList");
		List<String> filterFlagList = (List<String>) selectedMap
				.get("filterFlag");
		String wbsCustomWtFlag = selectedMap.get("wbsCustomWt").toString();
		String wbsOberallDeptFlag = selectedMap.get("wbsOverallDeptWt")
				.toString();

		String[] jobNumbers = getJobNumbers(jobNumberList);
		String epsContractId = selectedMap.get("contractId").toString();
		String weightFlag = selectedMap.get("weightFlag").toString();
		String apiFilter = filterFlagList.get(0);
		if (apiFilter.equalsIgnoreCase("Main Items+Internal Item")) {
			apiFilter = "MI";
		} else if (apiFilter.equalsIgnoreCase("All")) {
			apiFilter = "A";
		} else if (apiFilter.equalsIgnoreCase("Main Items")) {
			apiFilter = "M";
		}

		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement savePstm = con
					.prepareStatement(ConstructionConstants.SAVE_LATEST_PUBLISH_DETAILS);
			PreparedStatement deletePstm = con
					.prepareStatement(ConstructionConstants.DELETE_LAST_PUBLISH_DETAILS);

			Array jobNums = con.createArrayOf("varchar", jobNumbers);

			savePstm.setString(1, ssoId);
			savePstm.setString(2, wbsCustomWtFlag);
			savePstm.setString(3, wbsOberallDeptFlag);
			savePstm.setString(4, projectId);
			savePstm.setString(5, epsContractId);
			savePstm.setArray(6, jobNums);
			savePstm.setString(7, apiFilter);
			savePstm.setString(8, weightFlag);
			savePstm.setString(9, intExtFlag);

			deletePstm.setString(1, projectId);
			deletePstm.setString(2, intExtFlag);

			deletePstm.executeUpdate();
			savePstm.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("something went wrong while inserting latest publish details:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		map.put("status", "completed");
		return map;
	}

	private PubWeightDTO getPubWeightDetails(String projectId, String intExt) {
		return jdbcTemplate.query(ConstructionConstants.GET_PUB_WEIGHT_DETAILS,
				new Object[] { projectId, intExt },
				new ResultSetExtractor<PubWeightDTO>() {
					@Override
					public PubWeightDTO extractData(final ResultSet rs) {
						PubWeightDTO dto = new PubWeightDTO();
						try {
							while (rs.next()) {
								dto.setWbsCustomWt(rs.getString(1));
								dto.setWbsOverallDept(rs.getString(2));
								dto.setEpsContractId(rs.getString(3));
								dto.setActivityCustomWeight(rs.getString(4));

							}
						} catch (SQLException e) {
							log.error("something went wrong while getting publish weight details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL
									.getResponseStatus(), e);
						}
						return dto;
					}
				});
	}
}