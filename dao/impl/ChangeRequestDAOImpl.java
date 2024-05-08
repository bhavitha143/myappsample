package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IChangeManagementDAO;
import com.bh.realtrack.dao.helper.ChangeManagmentDAOHelper;
import com.bh.realtrack.dto.ActionOwnerDetailsDTO;
import com.bh.realtrack.dto.ChangeActionDTO;
import com.bh.realtrack.dto.ChangeManagementDTO;
import com.bh.realtrack.dto.ChangeSummaryRequestDTO;
import com.bh.realtrack.dto.UserDetailsDTO;
import com.bh.realtrack.util.ChangeRequestConstants;
import com.bh.realtrack.util.InspectionTestPlanConstants;

@Repository
public class ChangeRequestDAOImpl implements IChangeManagementDAO {

	private static Logger log = LoggerFactory.getLogger(ChangeRequestDAOImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ChangeManagmentDAOHelper changeManagmentDAOHelper;

	@Override
	public List<String> getJobFilter(String projectId) {
		return jdbcTemplate.query(ChangeRequestConstants.GET_JOB_FILTER, new Object[] { projectId },
				new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<String> jobsFilterList = new ArrayList<String>();
						String jobNumber = null;
						while (rs.next()) {
							jobNumber = rs.getString("sub_project");
							if (!jobNumber.isEmpty() && !jobNumber.equalsIgnoreCase("")) {
								jobsFilterList.add(jobNumber);
							}
						}
						return jobsFilterList;
					}
				});
	}

	@Override
	public List<ChangeManagementDTO> getSummary(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder summaryQuery = new StringBuilder();
		summaryQuery.append(ChangeRequestConstants.GET_SUMMARY_DATA);
		ResultSet rs = null;
		String subProject = null;
		ChangeManagementDTO changesummarydto = null;
		List<ChangeManagementDTO> listOfSubprojects = new ArrayList<ChangeManagementDTO>();

		changeSummaryRequestDTO.setEndOfShipDate(fetchShipEndOfShopDate(changeSummaryRequestDTO.getProjectId()));
		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, summaryQuery);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(summaryQuery.toString());) {

			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);
			rs = ps.executeQuery();

			while (rs.next()) {
				changesummarydto = new ChangeManagementDTO();
				changesummarydto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				subProject = rs.getString("sub_project");
				if (null != subProject) {
					subProject = subProject.substring(0, Math.min(subProject.length(), 23));
				}
				changesummarydto.setSubProject(null != subProject ? subProject : "");
				changesummarydto.setEcrCode(null != rs.getString("ecr_code") ? rs.getString("ecr_code") : "");
				changesummarydto.setEcrCretionDate(
						null != rs.getString("ECR_CREATION_DATE") ? rs.getString("ECR_CREATION_DATE") : "");
				changesummarydto.setChangeDescription(
						null != rs.getString("CHANGE_DESCRIPTION") ? rs.getString("CHANGE_DESCRIPTION") : "");
				changesummarydto.setAssementDescription(
						null != rs.getString("ASSESSMENT_DESCRIPTION") ? rs.getString("ASSESSMENT_DESCRIPTION") : "");
				changesummarydto
						.setReferenceFile(null != rs.getString("REFERENCE_FILE") ? rs.getString("REFERENCE_FILE") : "");
				changesummarydto.setAssementResult(
						null != rs.getString("XX_ASSESSMENT_RESULT") ? rs.getString("XX_ASSESSMENT_RESULT") : "");
				changesummarydto.setAssessorId(null != rs.getString("ASSESSOR_ID") ? rs.getString("ASSESSOR_ID") : "");
				changesummarydto
						.setEcrIssuerId(null != rs.getString("ecr_issuer_id") ? rs.getString("ecr_issuer_id") : "");
				changesummarydto.setEcrIssuerName(
						null != rs.getString("ecr_issuer_name") ? rs.getString("ecr_issuer_name") : "");
				changesummarydto.setClosure(null != rs.getString("closure") ? rs.getString("closure") : "");
				changesummarydto.setApproverDescDate(
						null != rs.getString("approver_desc_date") ? rs.getString("approver_desc_date") : "");
				changesummarydto.setNoOfAction(null != rs.getString("count") ? rs.getString("count") : "");
				changesummarydto.setApproverName(null != rs.getString("approver") ? rs.getString("approver") : "");
				listOfSubprojects.add(changesummarydto);
			}
		} catch (Exception exception) {
			log.error("Error occured when fetching popup detals");
		}

		return listOfSubprojects;
	}

	@Override
	public String getChangeSummaryLastUpdateDate(String projectId) {
		return jdbcTemplate.query(ChangeRequestConstants.GET_CHANGE_SUMMARY_LAST_UPDATE_DATE,
				new Object[] { projectId }, new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						String lastRefreshDate = null;
						while (rs.next()) {
							lastRefreshDate = rs.getString(1);
						}
						return lastRefreshDate;
					}
				});
	}

	@Override
	public List<ChangeActionDTO> getChangeRequest(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder crActionQuery = new StringBuilder();
		crActionQuery.append(ChangeRequestConstants.GET_CHANGE_REQUEST_DATA);
		List<ChangeActionDTO> listOfActionRequests = new ArrayList<ChangeActionDTO>();
		ResultSet rs = null;
		String ownerSSO = null;
		String ownerName = null;

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, crActionQuery);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(crActionQuery.toString());) {

			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);
			rs = ps.executeQuery();

			while (rs.next()) {
				ChangeActionDTO changeActionData = new ChangeActionDTO();
				changeActionData.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				changeActionData.setSubProject(null != rs.getString("sub_project") ? rs.getString("sub_project") : "");
				changeActionData.setEcrCode(null != rs.getString("ecr_code") ? rs.getString("ecr_code") : "");
				changeActionData.setActions(null != rs.getString("actions") ? rs.getString("actions") : "");
				ownerSSO = rs.getString("OWERN_SSO");
				ownerName = rs.getString("owner");
				if (null != ownerSSO) {
					if (null != ownerName) {
						ownerName = ownerName + "(";
					} else {
						ownerName = "(";
					}
					ownerName = ownerName + ownerSSO + ")";
				}
				changeActionData.setOwner(ownerName);
				changeActionData.setDueDate(null != rs.getString("due_date") ? rs.getString("due_date") : "");
				changeActionData.setActualClosureDate(
						null != rs.getString("actual_closure_date") ? rs.getString("actual_closure_date") : "");
				changeActionData.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				changeActionData.setComments(null != rs.getString("comments") ? rs.getString("comments") : "");
				changeActionData.setChangeActionId(
						null != rs.getString("change_action_id") ? rs.getString("change_action_id") : "");
				changeActionData
						.setEngAction(null != rs.getString("is_eng_action") ? rs.getString("is_eng_action") : "N");

				changeActionData.setEditable(rs.getString("editable"));
				changeActionData.setAutoComplete(rs.getString("auto_complete"));
				changeActionData.setActionTypeCode(rs.getString("action_type_code"));

				listOfActionRequests.add(changeActionData);
			}
		} catch (Exception exception) {
			log.error("Exception occurred in fetching changes request details" + exception.getMessage());
		}
		return listOfActionRequests;
	}

	@Override
	public String getChangeActionLastUpdateDate(String projectId) {
		return jdbcTemplate.query(ChangeRequestConstants.GET_CHANGE_ACTION_LAST_UPDATE_DATE, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						String lastRefreshDate = null;
						while (rs.next()) {
							lastRefreshDate = rs.getString(1);
						}
						return lastRefreshDate;
					}
				});
	}

	@Override
	public List<ChangeActionDTO> getChangeDataforEcr(String projectId, String ecrCode) {
		return jdbcTemplate.query(ChangeRequestConstants.GET_CHANGE_DATA_ECR, new Object[] { projectId, ecrCode },
				new ResultSetExtractor<List<ChangeActionDTO>>() {
					@Override
					public List<ChangeActionDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<ChangeActionDTO> listOfActionRequests = new ArrayList<ChangeActionDTO>();
						String ownerSSO = null;
						String ownerName = null;
						String assessmentDescription = "";
						assessmentDescription = getAssessmentDescription(projectId, ecrCode);
						while (rs.next()) {
							ChangeActionDTO changeActionData = new ChangeActionDTO();
							changeActionData
									.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
							changeActionData.setSubProject(
									null != rs.getString("sub_project") ? rs.getString("sub_project") : "");
							changeActionData
									.setEcrCode(null != rs.getString("ecr_code") ? rs.getString("ecr_code") : "");
							changeActionData.setActions(null != rs.getString("actions") ? rs.getString("actions") : "");
							ownerSSO = rs.getString("OWERN_SSO");
							ownerName = rs.getString("owner");
							if (null != ownerSSO) {
								if (null != ownerName) {
									ownerName = ownerName + "(";
								} else {
									ownerName = "(";
								}
								ownerName = ownerName + ownerSSO + ")";
							}
							changeActionData.setOwner(ownerName);
							changeActionData
									.setDueDate(null != rs.getString("due_date") ? rs.getString("due_date") : "");
							changeActionData.setActualClosureDate(
									null != rs.getString("actual_closure_date") ? rs.getString("actual_closure_date")
											: "");
							changeActionData.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
							changeActionData
									.setComments(null != rs.getString("comments") ? rs.getString("comments") : "");
							changeActionData.setChangeActionId(
									null != rs.getString("change_action_id") ? rs.getString("change_action_id") : "");
							changeActionData.setEngAction(
									null != rs.getString("is_eng_action") ? rs.getString("is_eng_action") : "N");

							changeActionData.setEditable(rs.getString("editable"));
							changeActionData.setAutoComplete(rs.getString("auto_complete"));
							changeActionData.setActionTypeCode(rs.getString("action_type_code"));

							changeActionData.setAssessmentDescription(assessmentDescription);
							listOfActionRequests.add(changeActionData);
						}
						return listOfActionRequests;
					}
				});
	}

	public String getAssessmentDescription(String projectId, String ecrCode) {
		return jdbcTemplate.query(ChangeRequestConstants.GET_ASSESSMENT_DESC_ECR, new Object[] { projectId, ecrCode },
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						String assessmentDescription = "";
						while (rs.next()) {
							assessmentDescription = rs.getString(1);
						}
						return assessmentDescription;
					}
				});
	}

	@Override
	public Boolean deleteChangeRequest(String changeActionId) {
		Connection con = null;
		int noOfRowsDeleted = 0;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String deleteQuery = ChangeRequestConstants.DELETE_CHANGE_REQUEST_BY_ACTION_ID;
			PreparedStatement pstmDeleteRequest = con.prepareStatement(deleteQuery);
			pstmDeleteRequest.setInt(1, Integer.parseInt(changeActionId));
			noOfRowsDeleted = pstmDeleteRequest.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (noOfRowsDeleted > 0)
			return true;
		return false;
	}

	public Boolean deleteChangeRequestByEcr(String projectId, String ecrCode) {
		String deleteQuery = ChangeRequestConstants.DELETE_CHANGE_REQUEST_BY_ECR;
		int noOfRowsDeleted = jdbcTemplate.update(deleteQuery, new Object[] { projectId, ecrCode });
		if (noOfRowsDeleted > 0)
			return true;
		return false;
	}

	@Override
	public void saveChangeRequestData(List<ChangeActionDTO> changeActionDTOList) {
		Date actualClosureDate = null, dueDate = null;
		String insertQuery = ChangeRequestConstants.INSERT_REQUEST_ACTION_DATA;
		Date actualClosureSQLDate = null;
		Date dueSQLDate = null;
		ChangeManagementDTO changeManagementDTO = getECRDetails(changeActionDTOList.get(0).getProjectId(),
				changeActionDTOList.get(0).getEcrCode());
		deleteChangeRequestByEcr(changeActionDTOList.get(0).getProjectId(), changeActionDTOList.get(0).getEcrCode());
		for (ChangeActionDTO changeActionDTO : changeActionDTOList) {
			actualClosureDate = null;
			dueDate = null;
			actualClosureSQLDate = null;
			dueSQLDate = null;
			try {
				if (null != changeActionDTO.getActualClosureDate()
						&& !"".equalsIgnoreCase(changeActionDTO.getActualClosureDate())) {
					actualClosureDate = new SimpleDateFormat("dd-MMM-yyyy")
							.parse(changeActionDTO.getActualClosureDate());
					actualClosureSQLDate = new java.sql.Date(actualClosureDate.getTime());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				dueDate = new SimpleDateFormat("dd-MMM-yyyy").parse(changeActionDTO.getDueDate());
				dueSQLDate = new java.sql.Date(dueDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (null != actualClosureSQLDate) {
				changeActionDTO.setStatus("Completed");
			}
			jdbcTemplate.update(insertQuery,
					new Object[] { changeActionDTO.getProjectId(), changeManagementDTO.getSubProject(),
							changeActionDTO.getEcrCode(), changeActionDTO.getActions(), changeActionDTO.getOwner(),
							changeActionDTO.getOwnerSSO(), dueSQLDate, actualClosureSQLDate,
							changeActionDTO.getStatus(), changeActionDTO.getComments(), changeActionDTO.getEngAction(),
							Integer.parseInt(changeActionDTO.getCompanyId()), changeActionDTO.getEditable(),
							changeActionDTO.getAutoComplete(), changeActionDTO.getActionTypeCode() });
		}
	}

	@Override
	public List<UserDetailsDTO> getRealTeackUserDetails(String projectId) {
		return jdbcTemplate.query(ChangeRequestConstants.GET_ACTIVE_REAL_TRACK_USERS, new Object[] {},
				new ResultSetExtractor<List<UserDetailsDTO>>() {
					@Override
					public List<UserDetailsDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<UserDetailsDTO> listOfuserDetails = new ArrayList<UserDetailsDTO>();
						UserDetailsDTO userDetailsDTO = null;
						String userName = null;
						while (rs.next()) {
							userDetailsDTO = new UserDetailsDTO();
							userDetailsDTO.setSso(rs.getString("user_sso"));
							userName = rs.getString("user_name");
							if (null != userName) {
								userDetailsDTO.setUserName(
										userName.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
												InspectionTestPlanConstants.WHITE_SPACE));
							}
							listOfuserDetails.add(userDetailsDTO);
						}
						return listOfuserDetails;
					}
				});
	}

	@Override
	public ActionOwnerDetailsDTO getProjectTeamMembersDetails(String projectId) {

		ActionOwnerDetailsDTO actionOwnerDetailsDTO = new ActionOwnerDetailsDTO();
		ResultSet projectTeamRS = null;
		ResultSet installMgrRS = null;
		UserDetailsDTO userDetailsDTO = null;
		List<UserDetailsDTO> userDetailsDTOList = new ArrayList<UserDetailsDTO>();
		List<UserDetailsDTO> pmDetailsDTOList = new ArrayList<UserDetailsDTO>();
		List<UserDetailsDTO> ppDetailsDTOList = new ArrayList<UserDetailsDTO>();
		List<UserDetailsDTO> peDetailsDTOList = new ArrayList<UserDetailsDTO>();
		List<UserDetailsDTO> pqmDetailsDTOList = new ArrayList<UserDetailsDTO>();
		String mgrNm = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement projectTeamPS = con
						.prepareStatement(ChangeRequestConstants.GET_PROJECT_TEAM_MEMBER_DETAILS);
				PreparedStatement installMgrPS = con
						.prepareStatement(ChangeRequestConstants.GET_PROJECT_INSTALLATION_MANAGER_DETAILS)) {
			projectTeamPS.setString(1, projectId);
			projectTeamPS.setString(2, projectId);
			projectTeamPS.setString(3, projectId);
			projectTeamPS.setString(4, projectId);
			projectTeamRS = projectTeamPS.executeQuery();
			while (projectTeamRS.next()) {
				userDetailsDTO = new UserDetailsDTO();
				String role = null, userName = null, userSSO = null;
				role = projectTeamRS.getString("role1");
				userName = projectTeamRS.getString("name1");
				userSSO = projectTeamRS.getString("user_sso");
				if (null != role && null != userName && null != userSSO) {
					if (role.equalsIgnoreCase("project_manager")) {
						userDetailsDTO.setUserName(userName);
						userDetailsDTO.setSso(userSSO);
						pmDetailsDTOList.add(userDetailsDTO);
						actionOwnerDetailsDTO.setPmName(pmDetailsDTOList);
					}
					if (role.equalsIgnoreCase("project_engineer")) {
						userDetailsDTO.setUserName(userName);
						userDetailsDTO.setSso(userSSO);
						ppDetailsDTOList.add(userDetailsDTO);
						actionOwnerDetailsDTO.setPeName(ppDetailsDTOList);
					}
					if (role.equalsIgnoreCase("project_planner")) {
						userDetailsDTO.setUserName(userName);
						userDetailsDTO.setSso(userSSO);
						peDetailsDTOList.add(userDetailsDTO);
						actionOwnerDetailsDTO.setPpName(peDetailsDTOList);
					}
					if (role.equalsIgnoreCase("quality_engineer")) {
						userDetailsDTO.setUserName(userName);
						userDetailsDTO.setSso(userSSO);
						pqmDetailsDTOList.add(userDetailsDTO);
						actionOwnerDetailsDTO.setPqmName(pqmDetailsDTOList);
					}
				}
			}
			installMgrPS.setString(1, projectId);
			installMgrRS = installMgrPS.executeQuery();
			while (installMgrRS.next()) {
				userDetailsDTO = new UserDetailsDTO();
				userDetailsDTO.setSso(installMgrRS.getString("instal_mgr_sso"));
				mgrNm = installMgrRS.getString("instal_mgr_nm");
				if (null != mgrNm) {
					userDetailsDTO.setUserName(mgrNm.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
							InspectionTestPlanConstants.WHITE_SPACE));
				}
				userDetailsDTOList.add(userDetailsDTO);
				actionOwnerDetailsDTO.setImName(userDetailsDTOList);
			}
		} catch (Exception exception) {
			log.error("Exception occured when fetching project team members details");
		}
		return actionOwnerDetailsDTO;
	}

	private List<UserDetailsDTO> fetchUserDetailsList(String userDetail) {
		List<UserDetailsDTO> userDetailsDTOList = new ArrayList<UserDetailsDTO>();
		String[] nameSSOList = null;
		UserDetailsDTO userDetailsDTO = null;
		String userName = null;
		if (null != userDetail) {
			nameSSOList = userDetail.split(",");
			for (String nameSSO : nameSSOList) {
				userDetailsDTO = new UserDetailsDTO();
				userName = nameSSO.substring(0, nameSSO.indexOf("("));
				if (null != userName) {
					userDetailsDTO
							.setUserName(userName.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
									InspectionTestPlanConstants.WHITE_SPACE));
				}
				userDetailsDTO.setSso(nameSSO.substring(nameSSO.indexOf("(") + 1, nameSSO.indexOf(")")));
				userDetailsDTOList.add(userDetailsDTO);
			}
		}
		return userDetailsDTOList;
	}

	private Date fetchShipEndOfShopDate(String projectId) {
		return jdbcTemplate.query(ChangeRequestConstants.GET_SHIP_ENDOF_SHOP_DATE, new Object[] { projectId },
				new ResultSetExtractor<Date>() {
					@Override
					public Date extractData(ResultSet rs) throws SQLException, DataAccessException {
						Date shipDate = null;
						while (rs.next()) {
							shipDate = rs.getDate(1);
						}
						return shipDate;
					}
				});
	}

	@Override
	public void triggerCMUserNotification(String projectId) {
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(ChangeRequestConstants.TRIGGER_CM_USER_NOTIFIACTION);) {
			ps.setString(1, projectId);
			ps.execute();
		} catch (SQLException e) {
			log.error("something went wrong while triggering CM user notification:  " + e.getMessage());
		}

	}

	public List<ChangeActionDTO> downloadChangeManagementDetails(String projectId, String jobNumber, String phase) {
		StringBuilder summaryQuery = new StringBuilder();
		summaryQuery.append(ChangeRequestConstants.DOWNLOAD_CM_DETAILS);
		List<Object> paramList = new ArrayList<Object>();
		Date endOfShipDate = fetchShipEndOfShopDate(projectId);
		paramList.add(endOfShipDate);
		paramList.add(projectId);
		if (null != jobNumber && !"".equalsIgnoreCase(jobNumber) && !"OVERALL".equalsIgnoreCase(jobNumber)) {
			summaryQuery.append(" and chMng.sub_project like ? ");
			paramList.add("%" + jobNumber + "%");
		}
		if (null != phase && !"".equalsIgnoreCase(phase) && !"OVERALL".equalsIgnoreCase(phase)) {
			if (null != endOfShipDate) {
				if ("Site Phase".equalsIgnoreCase(phase)) {
					summaryQuery.append(" and ECR_CREATION_DATE > ? ");
				} else {
					summaryQuery.append(" and ECR_CREATION_DATE <= ? ");
				}
				paramList.add(endOfShipDate);
			}
		}
		return jdbcTemplate.query(summaryQuery.toString(), paramList.toArray(),
				new ResultSetExtractor<List<ChangeActionDTO>>() {
					@Override
					public List<ChangeActionDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<ChangeActionDTO> cmDetails = new ArrayList<ChangeActionDTO>();
						ChangeActionDTO changeActionDTO = null;
						String ownerSSO = null;
						String ownerName = null;
						while (rs.next()) {
							changeActionDTO = new ChangeActionDTO();
							changeActionDTO.setSubProject(
									null != rs.getString("sub_project") ? rs.getString("sub_project") : "");
							changeActionDTO
									.setEcrCode(null != rs.getString("ecr_code") ? rs.getString("ecr_code") : "");
							changeActionDTO.setEcrCretionDate(
									null != rs.getString("ECR_CREATION_DATE") ? rs.getString("ECR_CREATION_DATE") : "");
							changeActionDTO.setChangeDescription(
									null != rs.getString("CHANGE_DESCRIPTION") ? rs.getString("CHANGE_DESCRIPTION")
											: "");
							changeActionDTO.setAssessmentDescription(null != rs.getString("ASSESSMENT_DESCRIPTION")
									? rs.getString("ASSESSMENT_DESCRIPTION")
									: "");
							changeActionDTO.setReferenceFile(
									null != rs.getString("REFERENCE_FILE") ? rs.getString("REFERENCE_FILE") : "");
							changeActionDTO.setAssementResult(
									null != rs.getString("XX_ASSESSMENT_RESULT") ? rs.getString("XX_ASSESSMENT_RESULT")
											: "");
							changeActionDTO.setAssessorId(
									null != rs.getString("ASSESSOR_ID") ? rs.getString("ASSESSOR_ID") : "");
							changeActionDTO.setEcrIssuerId(
									null != rs.getString("ecr_issuer_id") ? rs.getString("ecr_issuer_id") : "");
							changeActionDTO.setEcrIssuerName(
									null != rs.getString("ecr_issuer_name") ? rs.getString("ecr_issuer_name") : "");
							changeActionDTO.setClosure(null != rs.getString("closure") ? rs.getString("closure") : "");
							changeActionDTO.setApproverDescDate(
									null != rs.getString("approver_desc_date") ? rs.getString("approver_desc_date")
											: "");
							changeActionDTO.setActions(null != rs.getString("actions") ? rs.getString("actions") : "");
							ownerSSO = rs.getString("OWERN_SSO");
							ownerName = rs.getString("owner");
							if (null != ownerSSO) {
								if (null != ownerName) {
									ownerName = ownerName + "(";
								} else {
									ownerName = "(";
								}
								ownerName = ownerName + ownerSSO + ")";
							}
							changeActionDTO.setOwner(ownerName);
							changeActionDTO
									.setDueDate(null != rs.getString("due_date") ? rs.getString("due_date") : "");
							changeActionDTO.setActualClosureDate(
									null != rs.getString("actual_closure_date") ? rs.getString("actual_closure_date")
											: "");
							changeActionDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
							changeActionDTO
									.setComments(null != rs.getString("comments") ? rs.getString("comments") : "");
							changeActionDTO.setPmSSO(
									null != rs.getString("project_manager_sso") ? rs.getString("project_manager_sso")
											: "");
							changeActionDTO.setPhase(null != rs.getString("phase") ? rs.getString("phase") : "");
							changeActionDTO
									.setApproverName(null != rs.getString("approver") ? rs.getString("approver") : "");
							cmDetails.add(changeActionDTO);
						}
						return cmDetails;
					}
				});
	}

	@Override
	public ChangeManagementDTO getECRDetails(String projectId, String ecrCode) {
		ChangeManagementDTO changesummarydto = new ChangeManagementDTO();
		ResultSet rs = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(ChangeRequestConstants.GET_ECR_DETAILS);) {
			ps.setString(1, projectId);
			ps.setString(2, ecrCode);
			rs = ps.executeQuery();
			while (rs.next()) {
				changesummarydto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				changesummarydto.setSubProject(null != rs.getString("sub_project") ? rs.getString("sub_project") : "");
				changesummarydto.setEcrCode(null != rs.getString("ecr_code") ? rs.getString("ecr_code") : "");
				changesummarydto.setEcrCretionDate(
						null != rs.getString("ECR_CREATION_DATE") ? rs.getString("ECR_CREATION_DATE") : "");
				changesummarydto.setChangeDescription(
						null != rs.getString("CHANGE_DESCRIPTION") ? rs.getString("CHANGE_DESCRIPTION") : "");
				changesummarydto.setAssementDescription(
						null != rs.getString("ASSESSMENT_DESCRIPTION") ? rs.getString("ASSESSMENT_DESCRIPTION") : "");
				changesummarydto
						.setReferenceFile(null != rs.getString("REFERENCE_FILE") ? rs.getString("REFERENCE_FILE") : "");
				changesummarydto.setAssementResult(
						null != rs.getString("XX_ASSESSMENT_RESULT") ? rs.getString("XX_ASSESSMENT_RESULT") : "");
				changesummarydto.setAssessorId(null != rs.getString("ASSESSOR_ID") ? rs.getString("ASSESSOR_ID") : "");
				changesummarydto
						.setEcrIssuerId(null != rs.getString("ecr_issuer_id") ? rs.getString("ecr_issuer_id") : "");
				changesummarydto.setEcrIssuerName(
						null != rs.getString("ecr_issuer_name") ? rs.getString("ecr_issuer_name") : "");
				changesummarydto.setClosure(null != rs.getString("closure") ? rs.getString("closure") : "");
				changesummarydto.setApproverDescDate(
						null != rs.getString("approver_desc_date") ? rs.getString("approver_desc_date") : "");
				changesummarydto.setTodayDate(null != rs.getString("todayDate") ? rs.getString("todayDate") : "");
			}
		} catch (SQLException e) {
			log.error("something went wrong while fetching ECR details:  " + e.getMessage());
		}
		return changesummarydto;
	}

	@Override
	public ChangeActionDTO getChangeRequestActionData(String projectId, String jobNumber, String changeActionId) {
		StringBuilder crActionQuery = new StringBuilder();
		crActionQuery.append(ChangeRequestConstants.GET_CHANGE_REQUEST_ACTION_DATA);
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(projectId);
		paramList.add(Integer.parseInt(changeActionId));
		return jdbcTemplate.query(crActionQuery.toString(), paramList.toArray(),
				new ResultSetExtractor<ChangeActionDTO>() {
					@Override
					public ChangeActionDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
						ChangeActionDTO changeActionData = new ChangeActionDTO();
						String assessmentDescription = "";
						while (rs.next()) {
							String ownerSSO = null;
							String ownerName = null;
							changeActionData
									.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
							changeActionData.setSubProject(
									null != rs.getString("sub_project") ? rs.getString("sub_project") : "");
							changeActionData
									.setEcrCode(null != rs.getString("ecr_code") ? rs.getString("ecr_code") : "");
							changeActionData.setActions(null != rs.getString("actions") ? rs.getString("actions") : "");
							ownerSSO = rs.getString("OWERN_SSO");
							ownerName = rs.getString("owner");
							if (null != ownerSSO) {
								if (null != ownerName) {
									ownerName = ownerName + "(";
								} else {
									ownerName = "(";
								}
								ownerName = ownerName + ownerSSO + ")";
							}
							changeActionData.setOwner(ownerName);
							changeActionData
									.setDueDate(null != rs.getString("due_date") ? rs.getString("due_date") : "");
							changeActionData.setActualClosureDate(
									null != rs.getString("actual_closure_date") ? rs.getString("actual_closure_date")
											: "");
							changeActionData.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
							changeActionData
									.setComments(null != rs.getString("comments") ? rs.getString("comments") : "");
							changeActionData.setChangeActionId(
									null != rs.getString("change_action_id") ? rs.getString("change_action_id") : "");
							changeActionData.setEngAction(
									null != rs.getString("is_eng_action") ? rs.getString("is_eng_action") : "N");
							assessmentDescription = getAssessmentDescription(projectId, changeActionData.getEcrCode());
							changeActionData.setAssessmentDescription(assessmentDescription);
						}
						return changeActionData;
					}
				});
	}

	@Override
	public boolean saveChangeRequestAction(ChangeActionDTO editChangeRequestActionDTO) {
		Date actualClosureDate = null, dueDate = null, actualClosureSQLDate = null, dueSQLDate = null;
		boolean updateFlag = false;
		try {
			String updateQuery = ChangeRequestConstants.UPDATE_CHANGE_REQUEST_ACTION_DATA;
			if (null != editChangeRequestActionDTO.getActualClosureDate()
					&& !"".equalsIgnoreCase(editChangeRequestActionDTO.getActualClosureDate())) {
				actualClosureDate = new SimpleDateFormat("dd-MMM-yyyy")
						.parse(editChangeRequestActionDTO.getActualClosureDate());
				actualClosureSQLDate = new java.sql.Date(actualClosureDate.getTime());
			}
			if (null != actualClosureSQLDate) {
				editChangeRequestActionDTO.setStatus("Completed");
			}
			dueDate = new SimpleDateFormat("dd-MMM-yyyy").parse(editChangeRequestActionDTO.getDueDate());
			dueSQLDate = new java.sql.Date(dueDate.getTime());
			final Object[] params = new Object[] { editChangeRequestActionDTO.getActions(),
					editChangeRequestActionDTO.getOwner(), editChangeRequestActionDTO.getOwnerSSO(), dueSQLDate,
					actualClosureSQLDate, editChangeRequestActionDTO.getStatus(),
					editChangeRequestActionDTO.getComments(), editChangeRequestActionDTO.getProjectId(),
					editChangeRequestActionDTO.getSubProject(),
					Integer.parseInt(editChangeRequestActionDTO.getChangeActionId()) };
			if (jdbcTemplate.update(updateQuery, params) > 0) {
				updateFlag = true;
			}
		} catch (Exception e) {
			log.error("something went wrong while saving change request action: " + e.getMessage());
		}
		return updateFlag;
	}

	public String getTotalECRCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_TOTAL_ECR_COUNT);
		String ecrCount = "0";
		ResultSet rs = null;

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				ecrCount = rs.getString(1);
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total ECR count" + exception.getStackTrace());
		}

		return ecrCount;
	}

	public String getTotalNoActionECRCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_TOTAL_ECR_WITH_NO_ACTION_COUNT);
		String ecrCount = "0";
		ResultSet rs = null;

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				ecrCount = rs.getString(1);
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total No Action ECR count" + exception.getStackTrace());
		}

		return ecrCount;
	}

	public String getTotalInProgressActionECRCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(
				ChangeRequestConstants.GET_TOTAL_ECR_WITH_IN_PROGRESS_ACTION_COUNT);
		String ecrCount = "0";
		ResultSet rs = null;

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				ecrCount = rs.getString(1);
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total In Progress Action ECR count" + exception.getStackTrace());
		}

		return ecrCount;
	}

	public String getTotalAllCompletedActionECRCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(
				ChangeRequestConstants.GET_TOTAL_ECR_WITH_ALL_COMPLETED_ACTION_COUNT);
		String ecrCount = "0";
		ResultSet rs = null;

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				ecrCount = rs.getString(1);
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total All completed  Action ECR count"
					+ exception.getStackTrace());
		}

		return ecrCount;
	}

	public Map<String, String> getECRAgingDetails(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_ECR_NO_ACTION_AGING_DETAILS);
		String ecrCount = "0";
		String bucket = "0";
		ResultSet rs = null;

		Map<String, String> agingMap = new HashMap<String, String>();
		agingMap.put("lessThan7Days", "0");
		agingMap.put("above7lessThan30", "0");
		agingMap.put("above30", "0");

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		queryString.append(" group by aging ");

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				bucket = rs.getString(1);
				ecrCount = rs.getString(2);

				if ("7_DAYS".equals(bucket)) {
					agingMap.put("lessThan7Days", ecrCount);
				} else if ("30_DAYS".equals(bucket)) {
					agingMap.put("above7lessThan30", ecrCount);
				} else if ("31_DAYS".equals(bucket)) {
					agingMap.put("above30", ecrCount);
				}
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total All completed  Action ECR count"
					+ exception.getStackTrace());
		}

		return agingMap;
	}

	public Map<String, String> getECRAssessedImpactDetails(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_ECR_ASSESSED_IMPACT_DETAILS);
		String ecrCount = "0";
		String bucket = "0";
		ResultSet rs = null;

		Map<String, String> agingMap = new HashMap<String, String>();
		agingMap.put("noImpact", "0");
		agingMap.put("deliveryImpact", "0");
		agingMap.put("costImpact", "0");
		agingMap.put("costDeliveryImpact", "0");

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		queryString.append(" group by assessed_impact ");

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				bucket = rs.getString(1);
				ecrCount = rs.getString(2);

				if ("NO_IMPACT".equals(bucket)) {
					agingMap.put("noImpact", ecrCount);
				} else if ("DELIVERY_IMPACT".equals(bucket)) {
					agingMap.put("deliveryImpact", ecrCount);
				} else if ("COST_IMPACT".equals(bucket)) {
					agingMap.put("costImpact", ecrCount);
				} else if ("COST_DELIVERY_IMPACT".equals(bucket)) {
					agingMap.put("costDeliveryImpact", ecrCount);
				}
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total All completed  Action ECR count"
					+ exception.getStackTrace());
		}

		return agingMap;
	}

	public String getTotalActionCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_TOTAL_ACTION_COUNT);
		String ecrCount = "0";
		ResultSet rs = null;

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				ecrCount = rs.getString(1);
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total action count" + exception.getStackTrace());
		}

		return ecrCount;
	}

	public String getTotalInProgressActionCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_TOTAL_IN_PROGRESS_ACTION_COUNT);
		String ecrCount = "0";
		ResultSet rs = null;

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				ecrCount = rs.getString(1);
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total action count" + exception.getStackTrace());
		}

		return ecrCount;
	}

	public String getTotalCompletedActionCount(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_TOTAL_COMPLETED_ACTION_COUNT);
		String ecrCount = "0";
		ResultSet rs = null;

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				ecrCount = rs.getString(1);
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total action count" + exception.getStackTrace());
		}

		return ecrCount;
	}

	public Map<String, Map<String, String>> getPendingActionLookaheadDetails(
			ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_ACTION_PENDING_IMPACT_DETAILS);
		String ecrCount = "0";
		String bucket = "0";
		String agingBucket = "0";
		ResultSet rs = null;
		Map<String, String> agingMap = null;
		Map<String, Map<String, String>> responseMap = new HashMap<String, Map<String, String>>();

		agingMap = new HashMap<String, String>();
		agingMap.put("costImpact", "0");
		agingMap.put("deliveryImpact", "0");
		agingMap.put("costDeliveryImpact", "0");
		agingMap.put("noImpact", "0");
		responseMap.put("0_1_WEEKS", agingMap);

		agingMap = new HashMap<String, String>();
		agingMap.put("costImpact", "0");
		agingMap.put("deliveryImpact", "0");
		agingMap.put("costDeliveryImpact", "0");
		agingMap.put("noImpact", "0");
		responseMap.put("1_2_WEEKS", agingMap);

		agingMap = new HashMap<String, String>();
		agingMap.put("costImpact", "0");
		agingMap.put("deliveryImpact", "0");
		agingMap.put("costDeliveryImpact", "0");
		agingMap.put("noImpact", "0");
		responseMap.put("2_3_WEEKS", agingMap);

		agingMap = new HashMap<String, String>();
		agingMap.put("costImpact", "0");
		agingMap.put("deliveryImpact", "0");
		agingMap.put("costDeliveryImpact", "0");
		agingMap.put("noImpact", "0");
		responseMap.put("3_4_WEEKS", agingMap);

		agingMap = new HashMap<String, String>();
		agingMap.put("costImpact", "0");
		agingMap.put("deliveryImpact", "0");
		agingMap.put("costDeliveryImpact", "0");
		agingMap.put("noImpact", "0");
		responseMap.put("4_5_WEEKS", agingMap);

		agingMap = new HashMap<String, String>();
		agingMap.put("costImpact", "0");
		agingMap.put("deliveryImpact", "0");
		agingMap.put("costDeliveryImpact", "0");
		agingMap.put("noImpact", "0");
		responseMap.put("ALREADY_DUE", agingMap);

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		queryString.append(" group by assessed_impact,due_in_weeks ");

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {

				bucket = rs.getString(1);
				agingBucket = rs.getString(2);
				ecrCount = rs.getString(3);

				agingMap = responseMap.get(agingBucket);

				if (null != agingMap) {

					if ("NO_IMPACT".equals(bucket)) {
						agingMap.put("noImpact", ecrCount);
					} else if ("DELIVERY_IMPACT".equals(bucket)) {
						agingMap.put("deliveryImpact", ecrCount);
					} else if ("COST_IMPACT".equals(bucket)) {
						agingMap.put("costImpact", ecrCount);
					} else if ("COST_DELIVERY_IMPACT".equals(bucket)) {
						agingMap.put("costDeliveryImpact", ecrCount);
					}
				}

			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total All completed  Action ECR count"
					+ exception.getStackTrace());
		}

		return responseMap;
	}

	public Map<String, String> getActionSayDODetails(ChangeSummaryRequestDTO changeSummaryRequestDTO) {
		StringBuilder queryString = new StringBuilder(ChangeRequestConstants.GET_ACTION_SAY_DO_DETAILS);
		String ecrCount = "0";
		String bucket = "0";
		ResultSet rs = null;
		float completedOnTime = 0;
		float completedLate = 0;
		float overDue = 0;
		float aging = 0;

		Map<String, String> agingMap = new HashMap<String, String>();
		agingMap.put("completedOnTime", "0");
		agingMap.put("completedLate", "0");
		agingMap.put("overDue", "0");
		agingMap.put("aging", "0");

		changeManagmentDAOHelper.frameDynamicQuery(changeSummaryRequestDTO, queryString);

		queryString.append(" group by say_do ");

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(queryString.toString());) {
			changeManagmentDAOHelper.setDynamicParam(changeSummaryRequestDTO, ps, 0);

			rs = ps.executeQuery();

			while (rs.next()) {
				bucket = rs.getString(1);
				ecrCount = rs.getString(2);

				if ("COMPLETED_ON_TIME".equals(bucket)) {
					if (null != ecrCount) {
						completedOnTime = Integer.valueOf(ecrCount).intValue();
					}
					agingMap.put("completedOnTime", ecrCount);
				} else if ("COMPLETED_LATE".equals(bucket)) {
					if (null != ecrCount) {
						completedLate = Integer.valueOf(ecrCount).intValue();
					}
					agingMap.put("completedLate", ecrCount);
				} else if ("OVER_DUE".equals(bucket)) {
					if (null != ecrCount) {
						overDue = Integer.valueOf(ecrCount).intValue();
					}
					agingMap.put("overDue", ecrCount);
				}
			}

			aging = completedOnTime + completedLate + overDue;

			if (aging > 0 && completedOnTime > 0) {
				aging = (completedOnTime / aging) * 100;
				agingMap.put("aging", String.format("%.2f", aging));
			}

		} catch (Exception exception) {
			log.error("Exception occured when fetching total All completed  Action ECR count"
					+ exception.getStackTrace());
		}

		return agingMap;
	}

}