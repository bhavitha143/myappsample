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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IProjectIdentikitDAO;
import com.bh.realtrack.dto.IdentikitPMAddNotesHistoryDTO;
import com.bh.realtrack.dto.IdentikitProjectCoreTeamDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectGeographyDBDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectScopeDetailsDTO;
import com.bh.realtrack.dto.IdentikitProjectScopeTableDetailsDTO;
import com.bh.realtrack.dto.LCVolumeAnalysisDetailsDTO;
import com.bh.realtrack.dto.ProjectDataformSummaryDTO;
import com.bh.realtrack.dto.ProjectTeamDetailsDTO;
import com.bh.realtrack.util.IdentikitProjectConstants;
import com.bh.realtrack.util.ShippingPreservationConstants;

@Repository
public class ProjectIdentikitDAOImpl implements IProjectIdentikitDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger log = LoggerFactory.getLogger(ProjectIdentikitDAOImpl.class);

	@Override
	public IdentikitProjectDetailsDTO getIdentikitProjectDetails(String projectId, String sso) {
		IdentikitProjectDetailsDTO responseDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(IdentikitProjectConstants.GET_IDENTIKIT_PROJECT_DETAILS);) {
			pstm.setString(1, sso);
			pstm.setString(2, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseDTO = new IdentikitProjectDetailsDTO();
				responseDTO.setProjectId(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				responseDTO.setProjectName(
						null != rs.getString("project_name_out") ? rs.getString("project_name_out") : "");
				responseDTO.setContractCustomer(
						null != rs.getString("contract_customer_out") ? rs.getString("contract_customer_out") : "");
				responseDTO.setEndUser(null != rs.getString("end_user_out") ? rs.getString("end_user_out") : "");
			}
		} catch (SQLException e) {
			log.error("Exception while getting Identikit Project Details :: " + e.getMessage());
		}
		return responseDTO;
	}

	@Override
	public IdentikitProjectCoreTeamDetailsDTO getIdentikitProjectCoreTeamDetails(String projectId) {
		IdentikitProjectCoreTeamDetailsDTO responseDto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(IdentikitProjectConstants.GET_IDENTIKIT_PROJECT_CORE_TEAM_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseDto = new IdentikitProjectCoreTeamDetailsDTO();
				responseDto.setProjectManager(
						null != rs.getString("project_owner_nm_out") ? rs.getString("project_owner_nm_out") : "");
				responseDto.setProjectEngineer(
						null != rs.getString("project_engineer_out") ? rs.getString("project_engineer_out") : "");
				responseDto.setInstallationManager(
						null != rs.getString("installation_manager_out") ? rs.getString("installation_manager_out")
								: "");
				responseDto.setProjectPlanner(
						null != rs.getString("project_planner_out") ? rs.getString("project_planner_out") : "");
				responseDto.setProjectQualityManager(
						null != rs.getString("quality_engineer_out") ? rs.getString("quality_engineer_out") : "");
			}

		} catch (SQLException e) {
			log.error("Exception while getting Identikit Project Core Team Details :: " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public Map<String, Object> getIdentikitProjectTeamDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ProjectTeamDetailsDTO> pmList = new ArrayList<ProjectTeamDetailsDTO>();
		List<ProjectTeamDetailsDTO> peList = new ArrayList<ProjectTeamDetailsDTO>();
		List<ProjectTeamDetailsDTO> imList = new ArrayList<ProjectTeamDetailsDTO>();
		List<ProjectTeamDetailsDTO> ppList = new ArrayList<ProjectTeamDetailsDTO>();
		List<ProjectTeamDetailsDTO> qpmList = new ArrayList<ProjectTeamDetailsDTO>();
		ProjectTeamDetailsDTO responseDTO = new ProjectTeamDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(IdentikitProjectConstants.GET_IDENTIKIT_PROJECT_TEAM_EMAIL_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String role = null != rs.getString("roles_out") ? rs.getString("roles_out") : "";
				responseDTO = new ProjectTeamDetailsDTO();
				responseDTO.setName(null != rs.getString("p_name_out") ? rs.getString("p_name_out") : "");
				responseDTO.setEmail(null != rs.getString("email_out") ? rs.getString("email_out") : "");
				if (null != role && !role.isEmpty() && null != responseDTO.getName()
						&& !responseDTO.getName().isEmpty()) {
					if (role.equalsIgnoreCase("PM")) {
						pmList.add(responseDTO);
					} else if (role.equalsIgnoreCase("PE")) {
						peList.add(responseDTO);
					} else if (role.equalsIgnoreCase("PP")) {
						ppList.add(responseDTO);
					} else if (role.equalsIgnoreCase("QE")) {
						qpmList.add(responseDTO);
					} else if (role.equalsIgnoreCase("IM")) {
						imList.add(responseDTO);
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting Identikit Project Team Email Details :: " + e.getMessage());
		}
		responseMap.put("projectManager", pmList);
		responseMap.put("projectEngineer", peList);
		responseMap.put("installationManager", imList);
		responseMap.put("projectPlanner", ppList);
		responseMap.put("projectQualityManager", qpmList);
		return responseMap;
	}

	@Override
	public List<IdentikitProjectGeographyDBDetailsDTO> getIdentikitProjectGeographyDetails(String projectId) {
		List<IdentikitProjectGeographyDBDetailsDTO> list = new ArrayList<IdentikitProjectGeographyDBDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(IdentikitProjectConstants.GET_IDENTIKIT_PROJECT_GEOGRAPHY_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				IdentikitProjectGeographyDBDetailsDTO dto = new IdentikitProjectGeographyDBDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setCountry(null != rs.getString("country_out") ? rs.getString("country_out") : "");
				dto.setPmAddNotes(null != rs.getString("pm_add_notes_out") ? rs.getString("pm_add_notes_out") : "");
				dto.setProjLocLattitude(
						null != rs.getString("prj_latitude_out") ? rs.getString("prj_latitude_out") : "");
				dto.setProjLocLongitude(
						null != rs.getString("prj_longitude_out") ? rs.getString("prj_longitude_out") : "");

				dto.setBhShopLattitude(
						null != rs.getString("bh_mfg_shop_latitude_out") ? rs.getString("bh_mfg_shop_latitude_out")
								: "");
				dto.setBhShopLongitude(
						null != rs.getString("bh_mfg_shop_longitude_out") ? rs.getString("bh_mfg_shop_longitude_out")
								: "");
				dto.setBhShopCountryName(
						null != rs.getString("bh_shop_country_out") ? rs.getString("bh_shop_country_out") : "");
				dto.setMainSupplierLattitude(
						null != rs.getString("main_supplier_latitude_out") ? rs.getString("main_supplier_latitude_out")
								: "");
				dto.setMainSupplierLongitude(null != rs.getString("main_supplier_longitude_out")
						? rs.getString("main_supplier_longitude_out")
						: "");
				dto.setMainSupplierName(
						null != rs.getString("supplier_name_out") ? rs.getString("supplier_name_out") : "");
				dto.setMainSupplierPoVal(null != rs.getString("po_val_out") ? rs.getString("po_val_out") : "");
				dto.setContractLeLattitude(null != rs.getString("contract_legal_entitites_latitude_out")
						? rs.getString("contract_legal_entitites_latitude_out")
						: "");
				dto.setContractLeLongitude(null != rs.getString("contract_legal_entitites_longitude_out")
						? rs.getString("contract_legal_entitites_longitude_out")
						: "");
				dto.setContractLeName(null != rs.getString("le_name_out") ? rs.getString("le_name_out") : "");
				list.add(dto);
			}

		} catch (SQLException e) {
			log.error("Exception while getting Identikit Project Core Team Details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public IdentikitProjectScopeDetailsDTO getIdentikitProjectScopeDetails(String projectId) {
		IdentikitProjectScopeDetailsDTO responseDto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(IdentikitProjectConstants.GET_IDENTIKIT_PROJECT_SCOPE_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseDto = new IdentikitProjectScopeDetailsDTO();
				responseDto.setSegment(null != rs.getString("segment_out") ? rs.getString("segment_out") : "");
				responseDto.setExecutionModel(
						null != rs.getString("execution_model_out") ? rs.getString("execution_model_out") : "");
				responseDto.setScopeType(null != rs.getString("scope_type_out") ? rs.getString("scope_type_out") : "");
				responseDto.setTurnKey(null != rs.getString("turnkey_out") ? rs.getString("turnkey_out") : "");
				responseDto.setProjectScope(
						null != rs.getString("scope_of_work_out") ? rs.getString("scope_of_work_out") : "");
			}

		} catch (SQLException e) {
			log.error("Exception while getting Identikit Project Scope Details :: " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public List<IdentikitProjectScopeTableDetailsDTO> getIdentikitProjectScopeTableDetails(String projectId) {
		List<IdentikitProjectScopeTableDetailsDTO> list = new ArrayList<IdentikitProjectScopeTableDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(IdentikitProjectConstants.GET_IDENTIKIT_PROJECT_SCOPE_TABLE_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				IdentikitProjectScopeTableDetailsDTO responseDto = new IdentikitProjectScopeTableDetailsDTO();
				responseDto.setJobNumber(null != rs.getString("job_number_out") ? rs.getString("job_number_out") : "");
				responseDto.setProduct(null != rs.getString("product_out") ? rs.getString("product_out") : "");
				responseDto.setProductFamily(
						null != rs.getString("product_family_out") ? rs.getString("product_family_out") : "");
				list.add(responseDto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting Identikit Project PM Add Notes History Details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public boolean saveIdentikitProjectDetails(ProjectDataformSummaryDTO summaryDTO, String sso) {
		boolean updateFlag = true;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(IdentikitProjectConstants.INSERT_SAVE_IDENTIKIT_PROJECT_DETAILS);) {
			pstm.setString(1, summaryDTO.getProjectId());
			pstm.setString(2, summaryDTO.getPmAddNotes());
			pstm.setString(3, sso);
			if (pstm.executeUpdate() > 0) {
				updateFlag = true;
			}
		} catch (SQLException e) {
			updateFlag = false;
			log.error("insertIdentikitProjectDetails() :: Exception occurred :: " + e.getMessage());
		}
		return updateFlag;
	}

	@Override
	public List<IdentikitPMAddNotesHistoryDTO> getIdentikitPMAddNotesHistory(String projectId) {
		List<IdentikitPMAddNotesHistoryDTO> list = new ArrayList<IdentikitPMAddNotesHistoryDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(IdentikitProjectConstants.GET_IDENTIKIT_PM_ADD_NOTES_HISTORY);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				IdentikitPMAddNotesHistoryDTO responseDto = new IdentikitPMAddNotesHistoryDTO();
				responseDto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				responseDto.setNotes(null != rs.getString("notes") ? rs.getString("notes") : "");
				responseDto.setInsertBy(null != rs.getString("inserted_by") ? rs.getString("inserted_by") : "");
				responseDto.setInsertDt(null != rs.getString("inserted_on") ? rs.getString("inserted_on") : "");
				list.add(responseDto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting Identikit Project PM Add Notes History Details :: " + e.getMessage());
		}
		return list;
	}

	
		
		
	}


