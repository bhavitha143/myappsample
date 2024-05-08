package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IProjectComplexityDAO;
import com.bh.realtrack.dto.ComplexityFormTemplateDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ITOComplexityTabDTO;
import com.bh.realtrack.dto.ITOProjectComplexityTabDTO;
import com.bh.realtrack.dto.ITOProjectDataDTO;
import com.bh.realtrack.dto.ITOSlotCCTDates;
import com.bh.realtrack.dto.KeyValueDTO;
import com.bh.realtrack.dto.NEComplexityDTO;
import com.bh.realtrack.dto.NEComplexityTabDTO;
import com.bh.realtrack.dto.NEProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.NEProjectComplexityTabDTO;
import com.bh.realtrack.dto.NEProjectDataDTO;
import com.bh.realtrack.dto.ProjectComplexityDTO;
import com.bh.realtrack.dto.ProjectComplexityDropdownDTO;
import com.bh.realtrack.dto.ProjectComplexityLogDetailsDTO;
import com.bh.realtrack.dto.ProjectsSummaryDetailsDTO;
import com.bh.realtrack.dto.SRVComplexityDTO;
import com.bh.realtrack.dto.SRVPPProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SRVProjectComplexityDropDownDTO;
import com.bh.realtrack.dto.SaveProjectComplexityResponseDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.service.impl.ProjectComplexityServiceImpl;
import com.bh.realtrack.util.ProjectComplexityConstants;

/**
 *
 * @author Anand Kumar
 *
 */
@Repository
public class ProjectComplexityDAOImpl implements IProjectComplexityDAO {
	private static Logger log = LoggerFactory.getLogger(ProjectComplexityServiceImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int saveProjectComplexityList(final String projectId, final Map<String, Object> map, final String sso) {
		int count = getExistingProjectComplexityCount(projectId);
		int result = 0;
		Connection con = null;
		if (count == 0) {
			try {
				con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(ProjectComplexityConstants.SAVE_PROJECT_COMPLEXITY_LIST);
				pstm.setString(1, projectId);
				pstm.setInt(2, Integer.parseInt(map.get("companyId").toString()));
				pstm.setInt(3, Integer.parseInt(map.get("projectProductScope").toString()));
				pstm.setInt(4, Integer.parseInt(map.get("projectServicesScope").toString()));
				pstm.setInt(5, Integer.parseInt(map.get("testingQualityRequirements").toString()));
				pstm.setInt(6, Integer.parseInt(map.get("projectScheduleAsPlanned").toString()));
				pstm.setInt(7, Integer.parseInt(map.get("levelOfCustomization").toString()));
				pstm.setInt(8, Integer.parseInt(map.get("prjLocForTranInstlPur").toString()));
				pstm.setDouble(9, Double.parseDouble(map.get("totalComplexity").toString()));
				pstm.setInt(10, Integer.parseInt(map.get("projectStatus").toString()));
				pstm.setInt(11, Integer.parseInt(map.get("newComplexity").toString()));
				Double ppComplexity = StringUtils.isNotBlank(map.get("ppComplexity").toString())
						? Double.parseDouble(map.get("ppComplexity").toString())
						: 0;
				pstm.setDouble(12, ppComplexity);
				pstm.setString(13, sso);
				pstm.setString(14, sso);
				if (pstm.executeUpdate() > 0) {
					result = 1;
				}
			} catch (Exception e) {
				log.error("something went wrong while saving project complexity list:" + e.getMessage());
				throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
			} finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						log.error("something went wrong while saving project complexity list:" + e.getMessage());
					}
				}
			}
		} else {
			try {
				con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.UPDATE_PROJECT_COMPLEXITY_LIST);
				pstm.setInt(1, Integer.parseInt(map.get("projectProductScope").toString()));
				pstm.setInt(2, Integer.parseInt(map.get("projectServicesScope").toString()));
				pstm.setInt(3, Integer.parseInt(map.get("testingQualityRequirements").toString()));
				pstm.setInt(4, Integer.parseInt(map.get("projectScheduleAsPlanned").toString()));
				pstm.setInt(5, Integer.parseInt(map.get("levelOfCustomization").toString()));
				pstm.setInt(6, Integer.parseInt(map.get("prjLocForTranInstlPur").toString()));
				pstm.setDouble(7, Double.parseDouble(map.get("totalComplexity").toString()));
				pstm.setInt(8, Integer.parseInt(map.get("projectStatus").toString()));
				pstm.setInt(9, Integer.parseInt(map.get("newComplexity").toString()));
				Double ppComplexity = StringUtils.isNotBlank(map.get("ppComplexity").toString())
						? Double.parseDouble(map.get("ppComplexity").toString())
						: 0;
				pstm.setDouble(10, ppComplexity);
				pstm.setString(11, sso);
				pstm.setString(12, projectId);
				if (pstm.executeUpdate() > 0) {
					result = 1;
				}
			} catch (Exception e) {
				log.error("something went wrong while updating project complexity list:" + e.getMessage());
				throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
			} finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						log.error("something went wrong while updating project complexity list:" + e.getMessage());
					}
				}
			}
		}
		return result;
	}

	@Override
	public ProjectComplexityLogDetailsDTO getProjectComplexityLogDetails(final String projectId) {
		return jdbcTemplate.query(ProjectComplexityConstants.SAVE_PROJECT_COMPLEXITY_LOG_DETAILS,
				new Object[] { projectId }, new ResultSetExtractor<ProjectComplexityLogDetailsDTO>() {
					@Override
					public ProjectComplexityLogDetailsDTO extractData(final ResultSet rs) {
						ProjectComplexityLogDetailsDTO projectConplexityLogDetailsDTO = new ProjectComplexityLogDetailsDTO();
						try {
							while (rs.next()) {
								projectConplexityLogDetailsDTO.setFirstName(rs.getString(1));
								projectConplexityLogDetailsDTO.setLastName(rs.getString(2));
								projectConplexityLogDetailsDTO.setLastUpdatedDate(rs.getString(3));
							}
						} catch (SQLException e) {
							log.error("something went wrong while iterating project complexity log details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return projectConplexityLogDetailsDTO;
					}
				});
	}

	@Override
	public SaveProjectComplexityResponseDTO getProjectComplexitySaveLogDetails(final String projectId) {
		return jdbcTemplate.query(ProjectComplexityConstants.SAVE_PROJECT_COMPLEXITY_LOG_DETAILS,
				new Object[] { projectId }, new ResultSetExtractor<SaveProjectComplexityResponseDTO>() {
					@Override
					public SaveProjectComplexityResponseDTO extractData(final ResultSet rs) {
						SaveProjectComplexityResponseDTO saveProjectComplexityResponseDTO = new SaveProjectComplexityResponseDTO();
						try {
							while (rs.next()) {
								saveProjectComplexityResponseDTO.setFirstName(rs.getString(1));
								saveProjectComplexityResponseDTO.setLastName(rs.getString(2));
								saveProjectComplexityResponseDTO.setLastSaveDate(rs.getString(3));
							}
						} catch (SQLException e) {
							log.error("something went wrong while iterating project complexity log details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return saveProjectComplexityResponseDTO;
					}
				});
	}

	private int getExistingProjectComplexityCount(final String projectId) {
		return jdbcTemplate.query(ProjectComplexityConstants.EXISTING_PROJECT_COMPLEXITY_LIST,
				new Object[] { projectId }, new ResultSetExtractor<Integer>() {
					@Override
					public Integer extractData(final ResultSet rs) {
						int count = 0;
						try {
							while (rs.next()) {
								count = rs.getInt(1);
							}
						} catch (SQLException e) {
							log.error("something went wrong while checking existing project complexity count:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return count;
					}
				});
	}

	@Override
	public List<ProjectComplexityDTO> getProjectComplexity(final String projectId) {
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(ProjectComplexityConstants.UPDATE_PROJECT_COMPLEXITY);
			pstm.setString(1, projectId);
			pstm.executeUpdate();
		} catch (Exception e) {
			log.error("something went wrong while updating project complexity:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while updating project complexity:" + e.getMessage());
				}
			}
		}
		return jdbcTemplate.query(ProjectComplexityConstants.PROJECT_COMPLEXITY_DETAILS, new Object[] { projectId },
				new ResultSetExtractor<List<ProjectComplexityDTO>>() {
					@Override
					public List<ProjectComplexityDTO> extractData(final ResultSet rs) {
						List<ProjectComplexityDTO> list = new ArrayList<ProjectComplexityDTO>();
						String ppComplexity = "";
						try {
							while (rs.next()) {
								ProjectComplexityDTO projectComplexityDTO = new ProjectComplexityDTO();
								projectComplexityDTO.setProjectId(
										null != rs.getString("l2_project_id") ? rs.getString("l2_project_id") : "");
								projectComplexityDTO.setCompanyId(rs.getLong("company_id"));
								projectComplexityDTO.setProjectProductScope(rs.getLong("project_product_scope"));
								projectComplexityDTO.setProjectServicesScope(rs.getLong("project_services_scope"));
								projectComplexityDTO
										.setTestingQualityRequirements(rs.getLong("testing_quality_requirements"));
								projectComplexityDTO
										.setProjectScheduleAsPlanned(rs.getLong("project_schedule_as_planned"));
								projectComplexityDTO.setLevelOfCustomization(rs.getLong("level_of_customization"));
								projectComplexityDTO.setPrjLocForTranInstlPur(rs.getLong("prj_loc_for_tran_instl_pur"));
								projectComplexityDTO.setTotNoUnits(
										null != rs.getString("tot_no_units") ? rs.getString("tot_no_units") : "");
								projectComplexityDTO
										.setProjectContractualAmount(null != rs.getString("project_contractual_amount")
												? rs.getString("project_contractual_amount")
												: "");
								projectComplexityDTO.setTotComplexity(
										null != rs.getString("tot_complexity") ? rs.getString("tot_complexity") : "");
								projectComplexityDTO.setProjectStatus(rs.getLong("project_status"));
								projectComplexityDTO.setNewComplexity(rs.getLong("new_complexity"));
								projectComplexityDTO.setPmComplexity(
										null != rs.getString("tot_complexity") ? rs.getString("tot_complexity") : "");
								ppComplexity = getPPProjectComplexity(projectId);
								projectComplexityDTO.setPpComplexity(ppComplexity);
								list.add(projectComplexityDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while iterating project complexity:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	public String getPPProjectComplexity(String projectId) {
		return jdbcTemplate.query(ProjectComplexityConstants.GET_PP_PROJECT_COMPLEXITY,
				new Object[] { projectId, projectId }, new ResultSetExtractor<String>() {
					@Override
					public String extractData(final ResultSet rs) {
						String ppComplexity = "";
						try {
							while (rs.next()) {
								ppComplexity = rs.getString("pp_complexity");
							}
						} catch (SQLException e) {
							log.error(
									"something went wrong while getting pp project complexity value:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return ppComplexity;
					}
				});
	}

	@Override
	public List<ProjectComplexityDropdownDTO> getPCDropdownDetails(final String attributeGroup) {
		return jdbcTemplate.query(ProjectComplexityConstants.GET_PRJECT_COMPLEXITY_DROPDOWN_LIST,
				new Object[] { attributeGroup }, new ResultSetExtractor<List<ProjectComplexityDropdownDTO>>() {
					@Override
					public List<ProjectComplexityDropdownDTO> extractData(final ResultSet rs) {
						List<ProjectComplexityDropdownDTO> list = new ArrayList<ProjectComplexityDropdownDTO>();
						try {
							while (rs.next()) {
								ProjectComplexityDropdownDTO ppDropdownDTO = new ProjectComplexityDropdownDTO();
								ppDropdownDTO.setAttributeId(rs.getLong(1));
								ppDropdownDTO.setAttributeGroup(rs.getString(2));
								ppDropdownDTO.setAttributeName(rs.getString(3));
								ppDropdownDTO.setAttributeValue(rs.getString(4));
								ppDropdownDTO.setWidgetId(rs.getLong(5));
								ppDropdownDTO.setCompanyId(rs.getLong(6));
								list.add(ppDropdownDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while iterating project complexity dropdown details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return list;
					}
				});
	}

	@Override
	public List<NEComplexityDTO> getNEProjectComplexityData(String projectId) {
		List<NEComplexityDTO> list = new ArrayList<NEComplexityDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_NE_PROJECT_COMPLEXITY_FORM);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NEComplexityDTO responseDto = new NEComplexityDTO();
				responseDto.setRole(null != rs.getString("o_user_role") ? rs.getString("o_user_role") : "");
				responseDto.setScopeProduct(
						null != rs.getString("o_scope_product") ? rs.getString("o_scope_product") : "");
				responseDto.setScopeUnitTrain(
						null != rs.getString("o_scope_unit_train") ? rs.getString("o_scope_unit_train") : "");
				responseDto.setmDollar(null != rs.getString("o_m_dollar") ? rs.getString("o_m_dollar") : "");
				responseDto
						.setTestQuality(null != rs.getString("o_test_quality") ? rs.getString("o_test_quality") : "");
				responseDto.setSchedule(null != rs.getString("o_schedule") ? rs.getString("o_schedule") : "");
				responseDto.setCustomization(
						null != rs.getString("o_customization") ? rs.getString("o_customization") : "");
				responseDto.setNoOfUnits(null != rs.getString("o_no_of_units") ? rs.getString("o_no_of_units") : "");
				responseDto.setNoOfCostingProject(
						null != rs.getString("o_no_of_costing_project") ? rs.getString("o_no_of_costing_project") : "");
				responseDto.setNoOfTrain(null != rs.getString("o_no_of_train") ? rs.getString("o_no_of_train") : "");
				responseDto
						.setNoOfModules(null != rs.getString("o_no_of_modules") ? rs.getString("o_no_of_modules") : "");
				responseDto.setInitialScoreForPMComplex(
						null != rs.getString("o_initial_score_for_pc") ? rs.getString("o_initial_score_for_pc") : "");
				responseDto.setActualScoreForPMComplex(
						null != rs.getString("o_actual_score_for_pc") ? rs.getString("o_actual_score_for_pc") : "");
				responseDto.setActualScoreForFTEPM(
						null != rs.getString("o_actual_score_for_fte") ? rs.getString("o_actual_score_for_fte") : "");
				responseDto.setCustomeScoreForFTEPMShop(
						null != rs.getString("o_custom_score_for_shop") ? rs.getString("o_custom_score_for_shop") : "");
				responseDto.setCustomeScoreForFTEPMInstal(
						null != rs.getString("o_custom_score_for_install") ? rs.getString("o_custom_score_for_install")
								: "");
				responseDto.setCustomScoreFTEInstallOutsourcing(
						null != rs.getString("o_custom_score_for_fte_pm_install_outsourcing")
								? rs.getString("o_custom_score_for_fte_pm_install_outsourcing")
								: "");
				responseDto.setNotesForCustomScore(
						null != rs.getString("o_note_for_custom_score") ? rs.getString("o_note_for_custom_score") : "");
				responseDto.setPpOutSourcing(
						null != rs.getString("o_pp_out_sourcing") ? rs.getString("o_pp_out_sourcing") : "");
				responseDto.setActualScoreForPPOutsourcing(null != rs.getString("o_actual_score_for_pp_outsourcing")
						? rs.getString("o_actual_score_for_pp_outsourcing")
						: "");
				responseDto.setCustomScoreForFTEPPShopOutsourcing(
						null != rs.getString("o_custom_score_for_fte_pp_shop_outsourcing")
								? rs.getString("o_custom_score_for_fte_pp_shop_outsourcing")
								: "");
				responseDto.setCustomScoreForPPFTE(
						null != rs.getString("o_custom_score_for_pp_fte") ? rs.getString("o_custom_score_for_pp_fte")
								: "");
				responseDto.setActualScoreForPP(
						null != rs.getString("o_actual_score_for_pp") ? rs.getString("o_actual_score_for_pp") : "");
				responseDto.setScope(null != rs.getString("o_p_scope") ? rs.getString("o_p_scope") : "");
				responseDto.setUpdatedBy(null != rs.getString("o_user_name") ? rs.getString("o_user_name") : "");
				responseDto.setUpdatedOn(null != rs.getString("o_insert_dt") ? rs.getString("o_insert_dt") : "");
				list.add(responseDto);
			}
		} catch (Exception e) {
			log.error("Exception while getting NE Project Complexity details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public NEProjectComplexityTabDTO getNEComplexityProjectTabData(String projectId) {
		List<NEProjectDataDTO> list1 = new ArrayList<NEProjectDataDTO>();
		NEProjectComplexityTabDTO list = new NEProjectComplexityTabDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_NE_PROJECT_COMPLEXITY_TAB_DATA);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NEProjectDataDTO tabDTO = new NEProjectDataDTO();
				tabDTO.setOpptyId(
						null != rs.getString("o_rt_adm_project_oppty_id") ? rs.getString("o_rt_adm_project_oppty_id")
								: "");
				tabDTO.setStageCategory(
						null != rs.getString("o_stage_category") ? rs.getString("o_stage_category") : "");
				tabDTO.setBr(null != rs.getString("o_business_release") ? rs.getString("o_business_release") : "");
				tabDTO.setBrStartDate(null != rs.getString("o_br_start_date") ? rs.getString("o_br_start_date") : "");
				tabDTO.setBrEndDate(null != rs.getString("o_br_end_date") ? rs.getString("o_br_end_date") : "");
				tabDTO.setRac(null != rs.getString("o_rac") ? rs.getString("o_rac") : "");
				tabDTO.setMaxP6ContDelDate(
						null != rs.getString("o_max_p6_contract_date") ? rs.getString("o_max_p6_contract_date") : "");
				tabDTO.setShipOrEndOFShop(
						null != rs.getString("o_ship_end_shop") ? rs.getString("o_ship_end_shop") : "");
				tabDTO.setInstallationId(
						null != rs.getString("o_installation_contract_id") ? rs.getString("o_installation_contract_id")
								: "");
				tabDTO.setInstallationStartDate(
						null != rs.getString("o_installation_start") ? rs.getString("o_installation_start") : "");
				tabDTO.setEndOfInstallationPhase(null != rs.getString("o_cod") ? rs.getString("o_cod") : "");
				tabDTO.setEndOfWarraenty(
						null != rs.getString("o_end_of_warranty") ? rs.getString("o_end_of_warranty") : "");
				tabDTO.setP6ProjectPhase(
						null != rs.getString("o_project_phase_map") ? rs.getString("o_project_phase_map") : "");
				tabDTO.setProjectPhase(null != rs.getString("o_project_phase") ? rs.getString("o_project_phase") : "");
				tabDTO.setProjectStatus(
						null != rs.getString("o_project_status") ? rs.getString("o_project_status") : "");
				tabDTO.setShowWarrantyFlag(
						null != rs.getString("o_show_warrenty_data") ? rs.getString("o_show_warrenty_data") : "");
				tabDTO.setP6Contractualdelivery(null != rs.getString("o_contractual_delivery_terms")
						? rs.getString("o_contractual_delivery_terms")
						: "");
				tabDTO.setInstallationStatus(
						null != rs.getString("o_installation_status") ? rs.getString("o_installation_status") : "");
				tabDTO.setBrExpirationDate(
						null != rs.getString("o_br_expiration_date") ? rs.getString("o_br_expiration_date") : "");
				tabDTO.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				tabDTO.setWlSegment(null != rs.getString("o_wl_segment") ? rs.getString("o_wl_segment") : "");
				tabDTO.setNotesWlSegment(
						null != rs.getString("o_notes_for_wl_segment") ? rs.getString("o_notes_for_wl_segment") : "");
				tabDTO.setWlProjectCategory(
						null != rs.getString("o_wl_project_category") ? rs.getString("o_wl_project_category") : "");
				tabDTO.setNotesProjectCategory(null != rs.getString("o_notes_for_project_category")
						? rs.getString("o_notes_for_project_category")
						: "");
				tabDTO.setOrderType(null != rs.getString("o_order_type") ? rs.getString("o_order_type") : "");
				tabDTO.setOrderTypeNotes(
						null != rs.getString("o_order_type_notes") ? rs.getString("o_order_type_notes") : "");
				tabDTO.setCountry(null != rs.getString("o_country") ? rs.getString("o_country") : "");
				tabDTO.setRegion(null != rs.getString("o_region") ? rs.getString("o_region") : "");
				tabDTO.setInstallationCountry(
						null != rs.getString("o_installation_country") ? rs.getString("o_installation_country") : "");
				tabDTO.setInstallationRegion(
						null != rs.getString("o_installation_region") ? rs.getString("o_installation_region") : "");
				list.setUpdatedBy(null != rs.getString("o_updated_by") ? rs.getString("o_updated_by") : "");
				list.setUpdatedOn(null != rs.getString("o_updated_date") ? rs.getString("o_updated_date") : "");
				list1.add(tabDTO);
			}
			list.setData(list1);
		} catch (SQLException e) {
			log.error("Exception while getting NE Project Complexity Project Tab details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<DropDownDTO> getWLSegmentDropDown(String projectId) {
		String[] list = { "IND", "LNG", "OOP", "PGP", "REP", "OTHERS" };
		List<DropDownDTO> list1 = new ArrayList<DropDownDTO>();
		for (String obj : list) {
			DropDownDTO dto = new DropDownDTO();
			dto.setKey(obj);
			dto.setVal(obj);
			list1.add(dto);
		}
		return list1;
	}

	@Override
	public List<DropDownDTO> getWLProjectCategoryDropDown(String projectId) {
		String[] list = { "CORE", "SPECIAL" };
		List<DropDownDTO> list1 = new ArrayList<DropDownDTO>();
		for (String obj : list) {
			DropDownDTO dto = new DropDownDTO();
			dto.setKey(obj);
			dto.setVal(obj);
			list1.add(dto);
		}
		return list1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ComplexityFormTemplateDTO getComplexityFormTemplate(String projectId) {
		return jdbcTemplate.query(ProjectComplexityConstants.GET_PROJECT_COMPLEXITY_FORM, new Object[] { projectId },
				new ResultSetExtractor<ComplexityFormTemplateDTO>() {
					@Override
					public ComplexityFormTemplateDTO extractData(final ResultSet rs) {
						ComplexityFormTemplateDTO projectComplexityform = new ComplexityFormTemplateDTO();
						try {
							while (rs.next()) {
								projectComplexityform.setTemplate(
										null != rs.getString("business_unit") ? rs.getString("business_unit") : "");
							}
						} catch (SQLException e) {
							log.error("something went wrong while iterating project complexity form template:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}

						return projectComplexityform;
					}
				});
	}

	@Override
	public NEProjectComplexityDropDownDTO getNEProjectComplexityDropDownData(String projectId) {
		NEProjectComplexityDropDownDTO dropdownResponse = new NEProjectComplexityDropDownDTO();
		List<KeyValueDTO> scopeProductList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> testQualityList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> scheduleList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> customizationList = new ArrayList<KeyValueDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_NE_PM_PROJECT_DROP_DOWN);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String mapKey = rs.getString("use_as_map");
				KeyValueDTO keyValDTO1 = new KeyValueDTO();
				keyValDTO1.setKey(rs.getString("use_as_key"));
				if (projectId.contains("M_")) {
					keyValDTO1.setVal(rs.getString("use_as_val"));
				} else {
					keyValDTO1.setVal(rs.getString("use_as_key"));
				}

				if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_PRODUCT_SCOPE")) {
					scopeProductList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty()
						&& mapKey.equalsIgnoreCase("TESTING_QUALITY_REQUIREMENTS")) {
					testQualityList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("LEVEL_OF_CUSTOMIZATION")) {
					customizationList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty()
						&& mapKey.equalsIgnoreCase("PROJECT_SCHEDULE_AS_PLANNED")) {
					scheduleList.add(keyValDTO1);
				}
			}
			dropdownResponse.setScopeProduct(scopeProductList);
			dropdownResponse.setTestQuality(testQualityList);
			dropdownResponse.setSchedule(scheduleList);
			dropdownResponse.setCustomization(customizationList);
		} catch (SQLException e) {
			log.error("Exception while getting NE PM Project Complexity Dropdown details :: " + e.getMessage());
		}
		return dropdownResponse;
	}

	@Override
	public NEProjectComplexityDropDownDTO getNEPPProjectComplexityDropDownData(String projectId) {
		NEProjectComplexityDropDownDTO dropdownResponse = new NEProjectComplexityDropDownDTO();
		List<KeyValueDTO> scopeProductList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> testQualityList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> scheduleList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> customizationList = new ArrayList<KeyValueDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_NE_PP_PROJECT_DROP_DOWN);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String mapKey = rs.getString("use_as_map");
				KeyValueDTO keyValDTO1 = new KeyValueDTO();
				keyValDTO1.setKey(rs.getString("use_as_key"));
				if (projectId.contains("M_")) {
					keyValDTO1.setVal(rs.getString("use_as_val"));
				} else {
					keyValDTO1.setVal(rs.getString("use_as_key"));
				}
				if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_PRODUCT_SCOPE")) {
					scopeProductList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty()
						&& mapKey.equalsIgnoreCase("TESTING_QUALITY_REQUIREMENTS")) {
					testQualityList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("LEVEL_OF_CUSTOMIZATION")) {
					customizationList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty()
						&& mapKey.equalsIgnoreCase("PROJECT_SCHEDULE_AS_PLANNED")) {
					scheduleList.add(keyValDTO1);
				}
			}
			dropdownResponse.setScopeProduct(scopeProductList);
			dropdownResponse.setTestQuality(testQualityList);
			dropdownResponse.setSchedule(scheduleList);
			dropdownResponse.setCustomization(customizationList);
		} catch (SQLException e) {
			log.error("Exception while getting NE PP Project Complexity Dropdown details :: " + e.getMessage());
		}
		return dropdownResponse;
	}

	@Override
	public NEProjectComplexityDropDownDTO getNEQPMProjectComplexityDropDownData(String projectId) {
		NEProjectComplexityDropDownDTO dropdownResponse = new NEProjectComplexityDropDownDTO();
		List<KeyValueDTO> scopeProductList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> scheduleList = new ArrayList<KeyValueDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_NE_QPM_PROJECT_DROP_DOWN);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String mapKey = rs.getString("use_as_map");
				KeyValueDTO keyValDTO1 = new KeyValueDTO();
				keyValDTO1.setKey(rs.getString("use_as_key"));
				if (projectId.contains("M_")) {
					keyValDTO1.setVal(rs.getString("use_as_val"));
				} else {
					keyValDTO1.setVal(rs.getString("use_as_key"));
				}
				if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_PRODUCT_SCOPE")) {
					scopeProductList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty()
						&& mapKey.equalsIgnoreCase("PROJECT_SCHEDULE_AS_PLANNED")) {
					scheduleList.add(keyValDTO1);
				}
			}
			dropdownResponse.setScopeProduct(scopeProductList);
			dropdownResponse.setSchedule(scheduleList);
		} catch (SQLException e) {
			log.error("Exception while getting NE QPM Project Complexity Dropdown details :: " + e.getMessage());
		}
		return dropdownResponse;
	}

	@Override
	public int saveNEComplexityPMData(String projectId, Map<String, Object> map, String sso) {
		int result = 0;
		boolean deleteData = false;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			deleteData = deleteNEComplexityData(projectId, "PM");
			PreparedStatement pstm = con.prepareStatement(ProjectComplexityConstants.SAVE_NE_PROJECT_COMPLEXITY_LIST);
			pstm.setString(1, projectId);
			if (StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
				pstm.setDouble(2, Double.parseDouble(map.get("scopeProduct").toString()));
			} else {
				pstm.setNull(2, Types.NULL);
			}
			if (null != map.get("scopeUnitTrain") && StringUtils.isNotBlank(map.get("scopeUnitTrain").toString())) {
				pstm.setDouble(3, Double.parseDouble(map.get("scopeUnitTrain").toString()));
			} else {
				pstm.setNull(3, Types.NULL);
			}
			if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
				pstm.setDouble(4, Double.parseDouble(map.get("mDollar").toString()));
			} else {
				pstm.setNull(4, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("testQuality").toString())) {
				pstm.setDouble(5, Double.parseDouble(map.get("testQuality").toString()));
			} else {
				pstm.setNull(5, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("schedule").toString())) {
				pstm.setDouble(6, Double.parseDouble(map.get("schedule").toString()));
			} else {
				pstm.setNull(6, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("customization").toString())) {
				pstm.setDouble(7, Double.parseDouble(map.get("customization").toString()));
			} else {
				pstm.setNull(7, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("noOfUnits").toString())) {
				pstm.setDouble(8, Double.parseDouble(map.get("noOfUnits").toString()));
			} else {
				pstm.setNull(8, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("noOfCostingProject").toString())) {
				pstm.setDouble(9, Double.parseDouble(map.get("noOfCostingProject").toString()));
			} else {
				pstm.setNull(9, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("noOfTrain").toString())) {
				pstm.setDouble(10, Double.parseDouble(map.get("noOfTrain").toString()));
			} else {
				pstm.setNull(10, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("initialScoreForPMComplex").toString())) {
				pstm.setDouble(11, Double.parseDouble(map.get("initialScoreForPMComplex").toString()));
			} else {
				pstm.setNull(11, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
				pstm.setDouble(12, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
			} else {
				pstm.setNull(12, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
				pstm.setDouble(13, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
			} else {
				pstm.setNull(13, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMShop")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMShop").toString())) {
				pstm.setDouble(14, Double.parseDouble(map.get("customeScoreForFTEPMShop").toString()));
			} else {
				pstm.setNull(14, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMInstal")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMInstal").toString())) {
				pstm.setDouble(15, Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString()));
			} else {
				pstm.setNull(15, Types.NULL);
			}
			if (null != map.get("notesForCustomScore")
					&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
				pstm.setString(16, map.get("notesForCustomScore").toString());
			} else {
				pstm.setNull(16, Types.NULL);
			}
			pstm.setString(17, "PM");
			pstm.setString(18, "");
			pstm.setNull(19, Types.NULL);
			pstm.setNull(20, Types.NULL);
			pstm.setString(21, sso);
			pstm.setNull(22, Types.NULL);
			pstm.setNull(23, Types.NULL);
			pstm.setNull(24, Types.NULL);
			if (null != map.get("noOfModules") && StringUtils.isNotBlank(map.get("noOfModules").toString())) {
				pstm.setDouble(25, Double.parseDouble(map.get("noOfModules").toString()));
			} else {
				pstm.setNull(25, Types.NULL);
			}
			pstm.setNull(26, Types.NULL);
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Data saved successfully for PM Complexity");
			}
		} catch (Exception e) {
			log.error("something went wrong while saving PM complexity list:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while saving PM complexity list:" + e.getMessage());
				}
			}
		}
		return result;
	}

	@Override
	public int saveNEComplexityPPData(String projectId, Map<String, Object> map, String sso) {
		int result = 0;
		boolean deleteData = false;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			deleteData = deleteNEComplexityData(projectId, "PP");
			PreparedStatement pstm = con.prepareStatement(ProjectComplexityConstants.SAVE_NE_PROJECT_COMPLEXITY_LIST);
			pstm.setString(1, projectId);
			if (StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
				pstm.setDouble(2, Double.parseDouble(map.get("scopeProduct").toString()));
			} else {
				pstm.setNull(2, Types.NULL);
			}
			if (null != map.get("scopeUnitTrain") && StringUtils.isNotBlank(map.get("scopeUnitTrain").toString())) {
				pstm.setDouble(3, Double.parseDouble(map.get("scopeUnitTrain").toString()));
			} else {
				pstm.setNull(3, Types.NULL);
			}
			if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
				pstm.setDouble(4, Double.parseDouble(map.get("mDollar").toString()));
			} else {
				pstm.setNull(4, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("testQuality").toString())) {
				pstm.setDouble(5, Double.parseDouble(map.get("testQuality").toString()));
			} else {
				pstm.setNull(5, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("schedule").toString())) {
				pstm.setDouble(6, Double.parseDouble(map.get("schedule").toString()));
			} else {
				pstm.setNull(6, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("customization").toString())) {
				pstm.setDouble(7, Double.parseDouble(map.get("customization").toString()));
			} else {
				pstm.setNull(7, Types.NULL);
			}
			pstm.setNull(8, Types.NULL);
			pstm.setNull(9, Types.NULL);
			pstm.setNull(10, Types.NULL);
			if (StringUtils.isNotBlank(map.get("initialScoreForPMComplex").toString())) {
				pstm.setDouble(11, Double.parseDouble(map.get("initialScoreForPMComplex").toString()));
			} else {
				pstm.setNull(11, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
				pstm.setDouble(12, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
			} else {
				pstm.setNull(12, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
				pstm.setDouble(13, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
			} else {
				pstm.setNull(13, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMShop")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMShop").toString())) {
				pstm.setDouble(14, Double.parseDouble(map.get("customeScoreForFTEPMShop").toString()));
			} else {
				pstm.setNull(14, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMInstal")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMInstal").toString())) {
				pstm.setDouble(15, Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString()));
			} else {
				pstm.setNull(15, Types.NULL);
			}
			if (null != map.get("notesForCustomScore")
					&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
				pstm.setString(16, map.get("notesForCustomScore").toString());
			} else {
				pstm.setNull(16, Types.NULL);
			}
			pstm.setString(17, "PP");
			pstm.setString(18, map.get("projectWSPPOutsourcing").toString());

			if (StringUtils.isNotBlank(map.get("actualScoreForPPOutsourcing").toString())) {
				pstm.setDouble(19, Double.parseDouble(map.get("actualScoreForPPOutsourcing").toString()));
			} else {
				pstm.setNull(19, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("customScoreForFTEPPShopOutsourcing").toString())) {
				pstm.setDouble(20, Double.parseDouble(map.get("customScoreForFTEPPShopOutsourcing").toString()));
			} else {
				pstm.setNull(20, Types.NULL);
			}
			pstm.setString(21, sso);
			if (StringUtils.isNotBlank(map.get("customScoreForPPFTE").toString())) {
				pstm.setDouble(22, Double.parseDouble(map.get("customScoreForPPFTE").toString()));
			} else {
				pstm.setNull(22, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForPP").toString())) {
				pstm.setDouble(23, Double.parseDouble(map.get("actualScoreForPP").toString()));
			} else {
				pstm.setNull(23, Types.NULL);
			}
			pstm.setNull(24, Types.NULL);
			pstm.setNull(25, Types.NULL);
			pstm.setNull(26, Types.NULL);
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Data saved successfully for PP Complexity");
			}
		} catch (Exception e) {
			log.error("something went wrong while saving PP complexity list:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					log.error("something went wrong while saving PP complexity list:" + e.getMessage());
				}
			}
		}
		return result;
	}

	@Override
	public int saveNEComplexityQPMData(String projectId, Map<String, Object> map, String sso) {
		int result = 0;
		try {
			deleteNEComplexityData(projectId, "QPM");
			try (Connection con = jdbcTemplate.getDataSource().getConnection();
					PreparedStatement pstm = con
							.prepareStatement(ProjectComplexityConstants.SAVE_NE_PROJECT_COMPLEXITY_LIST);) {
				pstm.setString(1, projectId);
				if (null != map.get("scopeProduct") && StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
					pstm.setDouble(2, Double.parseDouble(map.get("scopeProduct").toString()));
				} else {
					pstm.setNull(2, Types.NULL);
				}
				if (null != map.get("scopeUnitTrain") && StringUtils.isNotBlank(map.get("scopeUnitTrain").toString())) {
					pstm.setDouble(3, Double.parseDouble(map.get("scopeUnitTrain").toString()));
				} else {
					pstm.setNull(3, Types.NULL);
				}
				if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
					pstm.setDouble(4, Double.parseDouble(map.get("mDollar").toString()));
				} else {
					pstm.setNull(4, Types.NULL);
				}
				if (null != map.get("testQuality") && StringUtils.isNotBlank(map.get("testQuality").toString())) {
					pstm.setDouble(5, Double.parseDouble(map.get("testQuality").toString()));
				} else {
					pstm.setNull(5, Types.NULL);
				}
				if (null != map.get("schedule") && StringUtils.isNotBlank(map.get("schedule").toString())) {
					pstm.setDouble(6, Double.parseDouble(map.get("schedule").toString()));
				} else {
					pstm.setNull(6, Types.NULL);
				}
				if (null != map.get("customization") && StringUtils.isNotBlank(map.get("customization").toString())) {
					pstm.setDouble(7, Double.parseDouble(map.get("customization").toString()));
				} else {
					pstm.setNull(7, Types.NULL);
				}
				pstm.setNull(8, Types.NULL);
				pstm.setNull(9, Types.NULL);
				pstm.setNull(10, Types.NULL);
				if (null != map.get("initialScoreForPMComplex")
						&& StringUtils.isNotBlank(map.get("initialScoreForPMComplex").toString())) {
					pstm.setDouble(11, Double.parseDouble(map.get("initialScoreForPMComplex").toString()));
				} else {
					pstm.setNull(11, Types.NULL);
				}
				if (null != map.get("actualScoreForPMComplex")
						&& StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
					pstm.setDouble(12, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
				} else {
					pstm.setNull(12, Types.NULL);
				}
				if (null != map.get("actualScoreForFTEPM")
						&& StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
					pstm.setDouble(13, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
				} else {
					pstm.setNull(13, Types.NULL);
				}
				if (null != map.get("customeScoreForFTEPMShop")
						&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMShop").toString())) {
					pstm.setDouble(14, Double.parseDouble(map.get("customeScoreForFTEPMShop").toString()));
				} else {
					pstm.setNull(14, Types.NULL);
				}
				if (null != map.get("customeScoreForFTEPMInstal")
						&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMInstal").toString())) {
					pstm.setDouble(15, Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString()));
				} else {
					pstm.setNull(15, Types.NULL);
				}
				if (null != map.get("notesForCustomScore")
						&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
					pstm.setString(16, map.get("notesForCustomScore").toString());
				} else {
					pstm.setNull(16, Types.NULL);
				}
				pstm.setString(17, "QPM");
				pstm.setString(18, map.get("projectWSPPOutsourcing").toString());

				if (null != map.get("actualScoreForPPOutsourcing")
						&& StringUtils.isNotBlank(map.get("actualScoreForPPOutsourcing").toString())) {
					pstm.setDouble(19, Double.parseDouble(map.get("actualScoreForPPOutsourcing").toString()));
				} else {
					pstm.setNull(19, Types.NULL);
				}
				if (null != map.get("customScoreForFTEPPShopOutsourcing")
						&& StringUtils.isNotBlank(map.get("customScoreForFTEPPShopOutsourcing").toString())) {
					pstm.setDouble(20, Double.parseDouble(map.get("customScoreForFTEPPShopOutsourcing").toString()));
				} else {
					pstm.setNull(20, Types.NULL);
				}
				pstm.setString(21, sso);
				if (null != map.get("customScoreForPPFTE")
						&& StringUtils.isNotBlank(map.get("customScoreForPPFTE").toString())) {
					pstm.setDouble(22, Double.parseDouble(map.get("customScoreForPPFTE").toString()));
				} else {
					pstm.setNull(22, Types.NULL);
				}
				if (null != map.get("actualScoreForPP")
						&& StringUtils.isNotBlank(map.get("actualScoreForPP").toString())) {
					pstm.setDouble(23, Double.parseDouble(map.get("actualScoreForPP").toString()));
				} else {
					pstm.setNull(23, Types.NULL);
				}
				pstm.setString(24, "unit");
				pstm.setNull(25, Types.NULL);
				if (null != map.get("customScoreForFTEPMInstallOutsourcing")
						&& StringUtils.isNotBlank(map.get("customScoreForFTEPMInstallOutsourcing").toString())) {
					pstm.setDouble(26, Double.parseDouble(map.get("customScoreForFTEPMInstallOutsourcing").toString()));
				} else {
					pstm.setNull(26, Types.NULL);
				}
				if (pstm.executeUpdate() > 0) {
					result = 1;
					log.info("Data saved successfully for QPM Complexity");
				}
			}
		} catch (Exception e) {
			log.error("Error in saveNEComplexityQPMData :: " + e.getMessage());
		}
		return result;
	}

	@Override
	public int saveNEComplexityQPMModuleData(String projectId, Map<String, Object> map, String sso) {
		int result = 0;
		try {
			deleteNEComplexityData(projectId, "qpmModule");
			try (Connection con = jdbcTemplate.getDataSource().getConnection();
					PreparedStatement pstm = con
							.prepareStatement(ProjectComplexityConstants.SAVE_NE_PROJECT_COMPLEXITY_LIST);) {
				pstm.setString(1, projectId);
				if (null != map.get("scopeProduct") && StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
					pstm.setDouble(2, Double.parseDouble(map.get("scopeProduct").toString()));
				} else {
					pstm.setNull(2, Types.NULL);
				}
				if (null != map.get("scopeUnitTrain") && StringUtils.isNotBlank(map.get("scopeUnitTrain").toString())) {
					pstm.setDouble(3, Double.parseDouble(map.get("scopeUnitTrain").toString()));
				} else {
					pstm.setNull(3, Types.NULL);
				}
				if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
					pstm.setDouble(4, Double.parseDouble(map.get("mDollar").toString()));
				} else {
					pstm.setNull(4, Types.NULL);
				}
				if (null != map.get("testQuality") && StringUtils.isNotBlank(map.get("testQuality").toString())) {
					pstm.setDouble(5, Double.parseDouble(map.get("testQuality").toString()));
				} else {
					pstm.setNull(5, Types.NULL);
				}
				if (null != map.get("schedule") && StringUtils.isNotBlank(map.get("schedule").toString())) {
					pstm.setDouble(6, Double.parseDouble(map.get("schedule").toString()));
				} else {
					pstm.setNull(6, Types.NULL);
				}
				if (null != map.get("customization") && StringUtils.isNotBlank(map.get("customization").toString())) {
					pstm.setDouble(7, Double.parseDouble(map.get("customization").toString()));
				} else {
					pstm.setNull(7, Types.NULL);
				}
				pstm.setNull(8, Types.NULL);
				pstm.setNull(9, Types.NULL);
				pstm.setNull(10, Types.NULL);
				if (null != map.get("initialScoreForPMComplex")
						&& StringUtils.isNotBlank(map.get("initialScoreForPMComplex").toString())) {
					pstm.setDouble(11, Double.parseDouble(map.get("initialScoreForPMComplex").toString()));
				} else {
					pstm.setNull(11, Types.NULL);
				}
				if (null != map.get("actualScoreForPMComplex")
						&& StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
					pstm.setDouble(12, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
				} else {
					pstm.setNull(12, Types.NULL);
				}
				if (null != map.get("actualScoreForFTEPM")
						&& StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
					pstm.setDouble(13, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
				} else {
					pstm.setNull(13, Types.NULL);
				}
				if (null != map.get("customeScoreForFTEPMShop")
						&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMShop").toString())) {
					pstm.setDouble(14, Double.parseDouble(map.get("customeScoreForFTEPMShop").toString()));
				} else {
					pstm.setNull(14, Types.NULL);
				}
				if (null != map.get("customeScoreForFTEPMInstal")
						&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMInstal").toString())) {
					pstm.setDouble(15, Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString()));
				} else {
					pstm.setNull(15, Types.NULL);
				}
				if (null != map.get("notesForCustomScore")
						&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
					pstm.setString(16, map.get("notesForCustomScore").toString());
				} else {
					pstm.setNull(16, Types.NULL);
				}
				pstm.setString(17, "QPM");
				pstm.setString(18, map.get("projectWSPPOutsourcing").toString());

				if (null != map.get("actualScoreForPPOutsourcing")
						&& StringUtils.isNotBlank(map.get("actualScoreForPPOutsourcing").toString())) {
					pstm.setDouble(19, Double.parseDouble(map.get("actualScoreForPPOutsourcing").toString()));
				} else {
					pstm.setNull(19, Types.NULL);
				}
				if (null != map.get("customScoreForFTEPPShopOutsourcing")
						&& StringUtils.isNotBlank(map.get("customScoreForFTEPPShopOutsourcing").toString())) {
					pstm.setDouble(20, Double.parseDouble(map.get("customScoreForFTEPPShopOutsourcing").toString()));
				} else {
					pstm.setNull(20, Types.NULL);
				}
				pstm.setString(21, sso);
				if (null != map.get("customScoreForPPFTE")
						&& StringUtils.isNotBlank(map.get("customScoreForPPFTE").toString())) {
					pstm.setDouble(22, Double.parseDouble(map.get("customScoreForPPFTE").toString()));
				} else {
					pstm.setNull(22, Types.NULL);
				}
				if (null != map.get("actualScoreForPP")
						&& StringUtils.isNotBlank(map.get("actualScoreForPP").toString())) {
					pstm.setDouble(23, Double.parseDouble(map.get("actualScoreForPP").toString()));
				} else {
					pstm.setNull(23, Types.NULL);
				}
				pstm.setString(24, "module");
				pstm.setNull(25, Types.NULL);
				if (null != map.get("customScoreForFTEPMInstallOutsourcing")
						&& StringUtils.isNotBlank(map.get("customScoreForFTEPMInstallOutsourcing").toString())) {
					pstm.setDouble(26, Double.parseDouble(map.get("customScoreForFTEPMInstallOutsourcing").toString()));
				} else {
					pstm.setNull(26, Types.NULL);
				}
				if (pstm.executeUpdate() > 0) {
					result = 1;
					log.info("Data saved successfully for QPM Complexity");
				}
			}
		} catch (Exception e) {
			log.error("Error in saveNEComplexityQPMModuleData :: " + e.getMessage());
		}
		return result;
	}

	private boolean deleteNEComplexityData(String projectId, String role) {
		boolean resultFlag = false;
		String count = "", scope = "";
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			if (projectId != null && !projectId.isEmpty() && !projectId.equalsIgnoreCase("") && role != null
					&& !role.isEmpty() && !role.equalsIgnoreCase("")) {
				if (role.equalsIgnoreCase("PM") || role.equalsIgnoreCase("PP")) {
					con = jdbcTemplate.getDataSource().getConnection();
					pstm = con.prepareStatement(ProjectComplexityConstants.DELETE_NE_COMPLEXITY_DATA);
					pstm.setString(1, projectId);
					pstm.setString(2, role);
					int result = pstm.executeUpdate();
					if (result > 0) {
						log.info("Deleted " + count + " rows for project id :: " + projectId);
						resultFlag = true;
					}
				} else if (role.equalsIgnoreCase("QPM") || role.equalsIgnoreCase("qpmModule")) {
					if (role.equalsIgnoreCase("QPM")) {
						scope = "unit";
					} else if (role.equalsIgnoreCase("qpmModule")) {
						scope = "module";
						role = "QPM";
					}
					con = jdbcTemplate.getDataSource().getConnection();
					pstm = con.prepareStatement(ProjectComplexityConstants.DELETE_QPM_COMPLEXITY_DATA);
					pstm.setString(1, projectId);
					pstm.setString(2, role);
					pstm.setString(3, scope);
					int result = pstm.executeUpdate();
					if (result > 0) {
						log.info("Deleted " + count + " rows for project id :: " + projectId);
						resultFlag = true;
					}
				}
			}
		} catch (Exception e) {
			log.error("something went wrong while deleting NE Complexity details:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while deleting NE Complexity details:" + e.getMessage());
				}
			}
		}
		return resultFlag;
	}

	@Override
	public Double getNEComplexityValues(String mapKey, String keyVal, String role) {
		NEProjectComplexityDropDownDTO dropdownResponse = new NEProjectComplexityDropDownDTO();
		List<KeyValueDTO> list = new ArrayList<KeyValueDTO>();
		Double value = null;
		try {
			if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PM")) {
				dropdownResponse = getNEProjectComplexityDropDownData("M_");
			} else if (null != role && !role.isEmpty() && role.equalsIgnoreCase("PP")) {
				dropdownResponse = getNEPPProjectComplexityDropDownData("M_");
			} else if (null != role && !role.isEmpty() && (role.equalsIgnoreCase("QPM"))) {
				dropdownResponse = getNEQPMProjectComplexityDropDownData("M_");
			} else if (null != role && !role.isEmpty() && (role.equalsIgnoreCase("qpmModule"))) {
				dropdownResponse = getNEQPMProjectComplexityDropDownData("M_");
			}
			if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_PRODUCT_SCOPE")) {
				list = dropdownResponse.getScopeProduct();
			} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("TESTING_QUALITY_REQUIREMENTS")) {
				list = dropdownResponse.getTestQuality();
			} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_SCHEDULE_AS_PLANNED")) {
				list = dropdownResponse.getSchedule();
			} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("LEVEL_OF_CUSTOMIZATION")) {
				list = dropdownResponse.getCustomization();
			}
			for (KeyValueDTO obj : list) {
				if (obj.getVal().equalsIgnoreCase(keyVal)) {
					log.info(mapKey + " ::: " + obj.getVal() + " ::: " + obj.getKey());
					value = Double.parseDouble(obj.getKey());
				}
			}
		} catch (Exception e) {
			log.error("something went wrong while getting NE Complexity values ::" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		}
		return value;
	}

	@Override
	public List<NEComplexityDTO> getNEProjectComplexityITOData(String projectId) {
		List<NEComplexityDTO> list = new ArrayList<NEComplexityDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_NE_PROJECT_COMPLEXITY_ITO_FORM);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NEComplexityDTO responseDto = new NEComplexityDTO();
				responseDto.setRole(null != rs.getString("o_user_role") ? rs.getString("o_user_role") : "");
				responseDto.setScopeProduct(
						null != rs.getString("o_scope_product") ? rs.getString("o_scope_product") : "");
				responseDto.setScopeUnitTrain(
						null != rs.getString("o_scope_unit_train") ? rs.getString("o_scope_unit_train") : "");
				responseDto.setmDollar(null != rs.getString("o_m_dollar") ? rs.getString("o_m_dollar") : "");
				responseDto
						.setTestQuality(null != rs.getString("o_test_quality") ? rs.getString("o_test_quality") : "");
				responseDto.setSchedule(null != rs.getString("o_schedule") ? rs.getString("o_schedule") : "");
				responseDto.setCustomization(
						null != rs.getString("o_customization") ? rs.getString("o_customization") : "");
				responseDto.setNoOfUnits(null != rs.getString("o_no_of_units") ? rs.getString("o_no_of_units") : "");
				responseDto.setNoOfCostingProject(
						null != rs.getString("o_no_of_costing_project") ? rs.getString("o_no_of_costing_project") : "");
				responseDto.setNoOfTrain(null != rs.getString("o_no_of_train") ? rs.getString("o_no_of_train") : "");
				responseDto
						.setNoOfModules(null != rs.getString("o_no_of_modules") ? rs.getString("o_no_of_modules") : "");
				responseDto.setInitialScoreForPMComplex(
						null != rs.getString("o_initial_score_for_pc") ? rs.getString("o_initial_score_for_pc") : "");
				responseDto.setActualScoreForPMComplex(
						null != rs.getString("o_actual_score_for_pc") ? rs.getString("o_actual_score_for_pc") : "");
				responseDto.setActualScoreForFTEPM(
						null != rs.getString("o_actual_score_for_fte") ? rs.getString("o_actual_score_for_fte") : "");
				responseDto.setCustomeScoreForFTEPMShop(
						null != rs.getString("o_custom_score_for_shop") ? rs.getString("o_custom_score_for_shop") : "");
				responseDto.setCustomeScoreForFTEPMInstal(
						null != rs.getString("o_custom_score_for_install") ? rs.getString("o_custom_score_for_install")
								: "");
				responseDto.setNotesForCustomScore(
						null != rs.getString("o_note_for_custom_score") ? rs.getString("o_note_for_custom_score") : "");
				responseDto.setPpOutSourcing(
						null != rs.getString("o_pp_out_sourcing") ? rs.getString("o_pp_out_sourcing") : "");
				responseDto.setScope(null != rs.getString("o_p_scope") ? rs.getString("o_p_scope") : "");
				responseDto.setUpdatedBy(null != rs.getString("o_user_name") ? rs.getString("o_user_name") : "");
				responseDto.setUpdatedOn(null != rs.getString("o_insert_dt") ? rs.getString("o_insert_dt") : "");
				list.add(responseDto);
			}
		} catch (Exception e) {
			log.error("Exception while getting NE Project Complexity  ITO details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public ITOProjectComplexityTabDTO getNEComplexityProjectITOTabData(String projectId) {
		List<ITOProjectDataDTO> list1 = new ArrayList<ITOProjectDataDTO>();
		ITOProjectComplexityTabDTO list = new ITOProjectComplexityTabDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_NE_PROJECT_COMPLEXITY_ITO_TAB_DATA);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ITOProjectDataDTO tabDTO = new ITOProjectDataDTO();
				tabDTO.setOpptyId(
						null != rs.getString("o_rt_adm_project_oppty_id") ? rs.getString("o_rt_adm_project_oppty_id")
								: "");
				tabDTO.setStageCategory(
						null != rs.getString("o_stage_category") ? rs.getString("o_stage_category") : "");
				tabDTO.setBr(null != rs.getString("o_business_release") ? rs.getString("o_business_release") : "");
				tabDTO.setBrStartDate(null != rs.getString("o_br_start_date") ? rs.getString("o_br_start_date") : "");
				tabDTO.setBrEndDate(null != rs.getString("o_br_end_date") ? rs.getString("o_br_end_date") : "");
				tabDTO.setRac(null != rs.getString("o_rac") ? rs.getString("o_rac") : "");
				tabDTO.setMaxP6ContDelDate(
						null != rs.getString("o_max_p6_contract_date") ? rs.getString("o_max_p6_contract_date") : "");
				tabDTO.setShipOrEndOFShop(
						null != rs.getString("o_ship_end_shop") ? rs.getString("o_ship_end_shop") : "");
				tabDTO.setInstallationId(
						null != rs.getString("o_installation_contract_id") ? rs.getString("o_installation_contract_id")
								: "");
				tabDTO.setInstallationStartDate(
						null != rs.getString("o_installation_start") ? rs.getString("o_installation_start") : "");
				tabDTO.setEndOfInstallationPhase(null != rs.getString("o_cod") ? rs.getString("o_cod") : "");
				tabDTO.setEndOfWarraenty(
						null != rs.getString("o_end_of_warranty") ? rs.getString("o_end_of_warranty") : "");
				tabDTO.setP6ProjectPhase(
						null != rs.getString("o_project_phase_map") ? rs.getString("o_project_phase_map") : "");
				tabDTO.setProjectPhase(null != rs.getString("o_project_phase") ? rs.getString("o_project_phase") : "");
				tabDTO.setProjectStatus(
						null != rs.getString("o_project_status") ? rs.getString("o_project_status") : "");
				tabDTO.setShowWarrantyFlag(
						null != rs.getString("o_show_warrenty_data") ? rs.getString("o_show_warrenty_data") : "");
				tabDTO.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				tabDTO.setWlSegment(null != rs.getString("o_wl_segment") ? rs.getString("o_wl_segment") : "");
				tabDTO.setNotesWlSegment(
						null != rs.getString("o_notes_for_wl_segment") ? rs.getString("o_notes_for_wl_segment") : "");
				tabDTO.setWlProjectCategory(
						null != rs.getString("o_wl_project_category") ? rs.getString("o_wl_project_category") : "");
				tabDTO.setNotesProjectCategory(null != rs.getString("o_notes_for_project_category")
						? rs.getString("o_notes_for_project_category")
						: "");
				tabDTO.setOrderType(null != rs.getString("o_order_type") ? rs.getString("o_order_type") : "");
				tabDTO.setOrderTypeNotes(
						null != rs.getString("o_order_type_notes") ? rs.getString("o_order_type_notes") : "");
				tabDTO.setCountry(null != rs.getString("o_country") ? rs.getString("o_country") : "");
				tabDTO.setRegion(null != rs.getString("o_region") ? rs.getString("o_region") : "");
				list.setUpdatedBy(null != rs.getString("o_updated_by") ? rs.getString("o_updated_by") : "");
				list.setUpdatedOn(null != rs.getString("o_updated_date") ? rs.getString("o_updated_date") : "");
				list1.add(tabDTO);
			}
			list.setData(list1);
		} catch (SQLException e) {
			log.error("Exception while getting NE Project Complexity Project ITO Tab details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public int saveNEComplexityPMITOData(String projectId, Map<String, Object> map, String sso) {
		int result = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ProjectComplexityConstants.SAVE_NE_PROJECT_COMPLEXITY_ITO_LIST);
			if (null != map.get("scopeProduct") && StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
				pstm.setDouble(1, Double.parseDouble(map.get("scopeProduct").toString()));
			} else {
				pstm.setNull(1, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("testQuality").toString())) {
				pstm.setDouble(2, Double.parseDouble(map.get("testQuality").toString()));
			} else {
				pstm.setNull(2, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("schedule").toString())) {
				pstm.setDouble(3, Double.parseDouble(map.get("schedule").toString()));
			} else {
				pstm.setNull(3, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("customization").toString())) {
				pstm.setDouble(4, Double.parseDouble(map.get("customization").toString()));
			} else {
				pstm.setNull(4, Types.NULL);
			}
			if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
				pstm.setDouble(5, Double.parseDouble(map.get("mDollar").toString()));
			} else {
				pstm.setNull(5, Types.NULL);
			}
			if (null != map.get("scopeUnitTrain") && StringUtils.isNotBlank(map.get("scopeUnitTrain").toString())) {
				pstm.setDouble(6, Double.parseDouble(map.get("scopeUnitTrain").toString()));
			} else {
				pstm.setNull(6, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
				pstm.setDouble(7, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
			} else {
				pstm.setNull(7, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
				pstm.setDouble(8, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
			} else {
				pstm.setNull(8, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMInstal")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMInstal").toString())) {
				pstm.setDouble(9, Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString()));
			} else {
				pstm.setNull(9, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMShop")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMShop").toString())) {
				pstm.setDouble(10, Double.parseDouble(map.get("customeScoreForFTEPMShop").toString()));
			} else {
				pstm.setNull(10, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("initialScoreForPMComplex").toString())) {
				pstm.setDouble(11, Double.parseDouble(map.get("initialScoreForPMComplex").toString()));
			} else {
				pstm.setNull(11, Types.NULL);
			}
			if (null != map.get("notesForCustomScore")
					&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
				pstm.setString(12, map.get("notesForCustomScore").toString());
			} else {
				pstm.setNull(12, Types.NULL);
			}
			
			pstm.setString(13, "");

			if (StringUtils.isNotBlank(map.get("noOfModules").toString())) {
				pstm.setInt(14, Integer.parseInt(map.get("noOfModules").toString()));
			} else {
				pstm.setNull(14, Types.NULL);
			}

			pstm.setString(15, sso);
			pstm.setString(16, projectId);
			pstm.setString(17, "PM");
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Data saved successfully for PM Complexity");
			}
		} catch (Exception e) {
			log.error("something went wrong while saving PM complexity ITO list:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while saving PM complexity ITO list:" + e.getMessage());
				}
			}
		}
		return result;

	}

	@Override
	public int saveNEComplexityPPITOData(String projectId, Map<String, Object> map, String sso) {

		int result = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ProjectComplexityConstants.SAVE_NE_PROJECT_COMPLEXITY_ITO_LIST);
			if (null != map.get("scopeProduct") && StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
				pstm.setDouble(1, Double.parseDouble(map.get("scopeProduct").toString()));
			} else {
				pstm.setNull(1, Types.NULL);
			}
			if (null != map.get("testQuality") && StringUtils.isNotBlank(map.get("testQuality").toString())) {
				pstm.setDouble(2, Double.parseDouble(map.get("testQuality").toString()));
			} else {
				pstm.setNull(2, Types.NULL);
			}
			if (null != map.get("schedule") && StringUtils.isNotBlank(map.get("schedule").toString())) {
				pstm.setDouble(3, Double.parseDouble(map.get("schedule").toString()));
			} else {
				pstm.setNull(3, Types.NULL);
			}
			if (null != map.get("customization") && StringUtils.isNotBlank(map.get("customization").toString())) {
				pstm.setDouble(4, Double.parseDouble(map.get("customization").toString()));
			} else {
				pstm.setNull(4, Types.NULL);
			}
			if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
				pstm.setDouble(5, Double.parseDouble(map.get("mDollar").toString()));
			} else {
				pstm.setNull(5, Types.NULL);
			}
			if (null != map.get("scopeUnitTrain") && StringUtils.isNotBlank(map.get("scopeUnitTrain").toString())) {
				pstm.setDouble(6, Double.parseDouble(map.get("scopeUnitTrain").toString()));
			} else {
				pstm.setNull(6, Types.NULL);
			}
			if (null != map.get("actualScoreForPMComplex")
					&& StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
				pstm.setDouble(7, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
			} else {
				pstm.setNull(7, Types.NULL);
			}
			if (null != map.get("actualScoreForFTEPM")
					&& StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
				pstm.setDouble(8, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
			} else {
				pstm.setNull(8, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMInstal")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMInstal").toString())) {
				pstm.setDouble(9, Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString()));
			} else {
				pstm.setNull(9, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMShop")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMShop").toString())) {
				pstm.setDouble(10, Double.parseDouble(map.get("customeScoreForFTEPMShop").toString()));
			} else {
				pstm.setNull(10, Types.NULL);
			}
			if (null != map.get("initialScoreForPMComplex")
					&& StringUtils.isNotBlank(map.get("initialScoreForPMComplex").toString())) {
				pstm.setDouble(11, Double.parseDouble(map.get("initialScoreForPMComplex").toString()));
			} else {
				pstm.setNull(11, Types.NULL);
			}
			if (null != map.get("notesForCustomScore")
					&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
				pstm.setString(12, map.get("notesForCustomScore").toString());
			} else {
				pstm.setNull(12, Types.NULL);
			}
			pstm.setString(13, map.get("projectWSPPOutsourcing").toString());
			if (null != map.get("noOfModules") && StringUtils.isNotBlank(map.get("noOfModules").toString())) {
				pstm.setString(14, map.get("noOfModules").toString());
			} else {
				pstm.setNull(14, Types.NULL);
			}
			pstm.setString(15, sso);
			pstm.setString(16, projectId);
			pstm.setString(17, "PP");
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Data saved successfully for PP Complexity");
			}
		} catch (Exception e) {
			log.error("something went wrong while saving PP complexity ITO list:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while saving PP complexity ITO list:" + e.getMessage());
				}
			}
		}
		return result;

	}

	@Override
	public int saveITOComplexityDataQPM(String projectId, Map<String, Object> map, String sso) {
		int result = 0;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.SAVE_NE_QPM_PROJECT_COMPLEXITY_ITO_LIST);) {
			if (null != map.get("scopeProduct") && StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
				pstm.setDouble(1, Double.parseDouble(map.get("scopeProduct").toString()));
			} else {
				pstm.setNull(1, Types.NULL);
			}
			if (null != map.get("testQuality") && StringUtils.isNotBlank(map.get("testQuality").toString())) {
				pstm.setDouble(2, Double.parseDouble(map.get("testQuality").toString()));
			} else {
				pstm.setNull(2, Types.NULL);
			}
			if (null != map.get("schedule") && StringUtils.isNotBlank(map.get("schedule").toString())) {
				pstm.setDouble(3, Double.parseDouble(map.get("schedule").toString()));
			} else {
				pstm.setNull(3, Types.NULL);
			}
			if (null != map.get("customization") && StringUtils.isNotBlank(map.get("customization").toString())) {
				pstm.setDouble(4, Double.parseDouble(map.get("customization").toString()));
			} else {
				pstm.setNull(4, Types.NULL);
			}
			if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
				pstm.setDouble(5, Double.parseDouble(map.get("mDollar").toString()));
			} else {
				pstm.setNull(5, Types.NULL);
			}
			if (null != map.get("scopeUnitTrain") && StringUtils.isNotBlank(map.get("scopeUnitTrain").toString())) {
				pstm.setDouble(6, Double.parseDouble(map.get("scopeUnitTrain").toString()));
			} else {
				pstm.setNull(6, Types.NULL);
			}
			if (null != map.get("actualScoreForPMComplex")
					&& StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
				pstm.setDouble(7, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
			} else {
				pstm.setNull(7, Types.NULL);
			}
			if (null != map.get("actualScoreForFTEPM")
					&& StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
				pstm.setDouble(8, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
			} else {
				pstm.setNull(8, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMInstal")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMInstal").toString())) {
				pstm.setDouble(9, Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString()));
			} else {
				pstm.setNull(9, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMShop")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMShop").toString())) {
				pstm.setDouble(10, Double.parseDouble(map.get("customeScoreForFTEPMShop").toString()));
			} else {
				pstm.setNull(10, Types.NULL);
			}
			if (null != map.get("initialScoreForPMComplex")
					&& StringUtils.isNotBlank(map.get("initialScoreForPMComplex").toString())) {
				pstm.setDouble(11, Double.parseDouble(map.get("initialScoreForPMComplex").toString()));
			} else {
				pstm.setNull(11, Types.NULL);
			}
			if (null != map.get("notesForCustomScore")
					&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
				pstm.setString(12, map.get("notesForCustomScore").toString());
			} else {
				pstm.setNull(12, Types.NULL);
			}
			pstm.setString(13, map.get("projectWSPPOutsourcing").toString());
			pstm.setString(14, sso);
			pstm.setString(15, projectId);
			pstm.setString(16, "QPM");
			pstm.setString(17, "unit");
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Data saved successfully for QPM Complexity");
			}
		} catch (Exception e) {
			log.error("Error in saveITOComplexityDataQPM :: " + e.getMessage());
		}
		return result;
	}

	@Override
	public int saveITOComplexityDataQPMModule(String projectId, Map<String, Object> map, String sso) {
		int result = 0;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.SAVE_NE_QPM_PROJECT_COMPLEXITY_ITO_LIST);) {
			if (null != map.get("scopeProduct") && StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
				pstm.setDouble(1, Double.parseDouble(map.get("scopeProduct").toString()));
			} else {
				pstm.setNull(1, Types.NULL);
			}
			if (null != map.get("testQuality") && StringUtils.isNotBlank(map.get("testQuality").toString())) {
				pstm.setDouble(2, Double.parseDouble(map.get("testQuality").toString()));
			} else {
				pstm.setNull(2, Types.NULL);
			}
			if (null != map.get("schedule") && StringUtils.isNotBlank(map.get("schedule").toString())) {
				pstm.setDouble(3, Double.parseDouble(map.get("schedule").toString()));
			} else {
				pstm.setNull(3, Types.NULL);
			}
			if (null != map.get("customization") && StringUtils.isNotBlank(map.get("customization").toString())) {
				pstm.setDouble(4, Double.parseDouble(map.get("customization").toString()));
			} else {
				pstm.setNull(4, Types.NULL);
			}
			if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
				pstm.setDouble(5, Double.parseDouble(map.get("mDollar").toString()));
			} else {
				pstm.setNull(5, Types.NULL);
			}
			if (null != map.get("scopeUnitTrain") && StringUtils.isNotBlank(map.get("scopeUnitTrain").toString())) {
				pstm.setDouble(6, Double.parseDouble(map.get("scopeUnitTrain").toString()));
			} else {
				pstm.setNull(6, Types.NULL);
			}
			if (null != map.get("actualScoreForPMComplex")
					&& StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
				pstm.setDouble(7, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
			} else {
				pstm.setNull(7, Types.NULL);
			}
			if (null != map.get("actualScoreForFTEPM")
					&& StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
				pstm.setDouble(8, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
			} else {
				pstm.setNull(8, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMInstal")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMInstal").toString())) {
				pstm.setDouble(9, Double.parseDouble(map.get("customeScoreForFTEPMInstal").toString()));
			} else {
				pstm.setNull(9, Types.NULL);
			}
			if (null != map.get("customeScoreForFTEPMShop")
					&& StringUtils.isNotBlank(map.get("customeScoreForFTEPMShop").toString())) {
				pstm.setDouble(10, Double.parseDouble(map.get("customeScoreForFTEPMShop").toString()));
			} else {
				pstm.setNull(10, Types.NULL);
			}
			if (null != map.get("initialScoreForPMComplex")
					&& StringUtils.isNotBlank(map.get("initialScoreForPMComplex").toString())) {
				pstm.setDouble(11, Double.parseDouble(map.get("initialScoreForPMComplex").toString()));
			} else {
				pstm.setNull(11, Types.NULL);
			}
			if (null != map.get("notesForCustomScore")
					&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
				pstm.setString(12, map.get("notesForCustomScore").toString());
			} else {
				pstm.setNull(12, Types.NULL);
			}
			pstm.setString(13, map.get("projectWSPPOutsourcing").toString());
			pstm.setString(14, sso);
			pstm.setString(15, projectId);
			pstm.setString(16, "QPM");
			pstm.setString(17, "module");
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Data saved successfully for QPM-Module Complexity");
			}
		} catch (Exception e) {
			log.error("Error in saveITOComplexityDataQPMModule :: " + e.getMessage());
		}
		return result;
	}

	@Override
	public List<SRVComplexityDTO> getSRVProjectComplexityData(String projectId) {
		List<SRVComplexityDTO> list = new ArrayList<SRVComplexityDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_SRV_PROJECT_COMPLEXITY_FORM);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				SRVComplexityDTO responseDto = new SRVComplexityDTO();
				responseDto.setRole(null != rs.getString("user_role") ? rs.getString("user_role") : "");
				responseDto.setScopeProduct(null != rs.getString("scope_project") ? rs.getString("scope_project") : "");
				responseDto.setOtherLegalEntity(
						null != rs.getString("other_leagal_entity") ? rs.getString("other_leagal_entity") : "");
				responseDto.setPassTrough(null != rs.getString("pass_through") ? rs.getString("pass_through") : "");
				responseDto.setCustomerReporting(
						null != rs.getString("customer_reporting") ? rs.getString("customer_reporting") : "");
				responseDto.setLocation(null != rs.getString("location_value") ? rs.getString("location_value") : "");
				responseDto.setmDollar(null != rs.getString("m_dollar") ? rs.getString("m_dollar") : "");
				responseDto.setInitialScoreForPMComplex(null != rs.getString("intial_sorce_project_complex")
						? rs.getString("intial_sorce_project_complex")
						: "");
				responseDto.setActualScoreForPMComplex(null != rs.getString("actual_sorce_project_complex")
						? rs.getString("actual_sorce_project_complex")
						: "");
				responseDto.setActualScoreForFTEPM(
						null != rs.getString("actual_sorce_fte") ? rs.getString("actual_sorce_fte") : "");
				responseDto.setNotesForCustomScore(
						null != rs.getString("user_comments") ? rs.getString("user_comments") : "");
				responseDto.setOmContribution(
						null != rs.getString("om_contibution") ? rs.getString("om_contibution") : "");
				responseDto.setUpdatedBy(null != rs.getString("user_name") ? rs.getString("user_name") : "");
				responseDto.setUpdatedOn(null != rs.getString("updated_on") ? rs.getString("updated_on") : "");
				list.add(responseDto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting SRV Project Complexity details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public SRVProjectComplexityDropDownDTO getSRVPMProjectComplexityDropDownData(String projectId) {
		SRVProjectComplexityDropDownDTO dropdownResponse = new SRVProjectComplexityDropDownDTO();
		List<KeyValueDTO> scopeProductList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> customerReportingList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> locationList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> legalEntityList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> passTroughList = new ArrayList<KeyValueDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_SRV_PM_PROJECT_DROP_DOWN);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String mapKey = rs.getString("use_as_map");
				KeyValueDTO keyValDTO1 = new KeyValueDTO();
				keyValDTO1.setKey(rs.getString("use_as_key"));
				keyValDTO1.setVal(rs.getString("use_as_key"));

				if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_PRODUCT_SCOPE")) {
					scopeProductList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("CUSTOMER_REPORTING")) {
					customerReportingList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("TRANSPORTATION")) {
					locationList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_LEGAL_ENTITY")) {
					legalEntityList.add(keyValDTO1);
				} else if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_PASS_TROUGH")) {
					passTroughList.add(keyValDTO1);
				}
			}
			dropdownResponse.setScopeProduct(scopeProductList);
			dropdownResponse.setCustomerReporting(customerReportingList);
			dropdownResponse.setLocation(locationList);
			dropdownResponse.setLegalEntity(legalEntityList);
			dropdownResponse.setPassTrough(passTroughList);
		} catch (SQLException e) {
			log.error("Exception while getting SRV PM Project Complexity Dropdown details :: " + e.getMessage());
		}
		return dropdownResponse;

	}

	@Override
	public SRVPPProjectComplexityDropDownDTO getSRVPPProjectComplexityDropDownData(String projectId) {
		SRVPPProjectComplexityDropDownDTO dropdownResponse = new SRVPPProjectComplexityDropDownDTO();
		List<KeyValueDTO> scopeProductList = new ArrayList<KeyValueDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_SRV_PP_PROJECT_DROP_DOWN);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String mapKey = rs.getString("use_as_map");
				KeyValueDTO keyValDTO1 = new KeyValueDTO();
				keyValDTO1.setKey(rs.getString("use_as_key"));
				keyValDTO1.setVal(rs.getString("use_as_key"));
				if (null != mapKey && !mapKey.isEmpty() && mapKey.equalsIgnoreCase("PROJECT_PRODUCT_SCOPE")) {
					scopeProductList.add(keyValDTO1);
				}
			}
			dropdownResponse.setScopeProduct(scopeProductList);
		} catch (SQLException e) {
			log.error("Exception while getting SRV PP Project Complexity Dropdown details :: " + e.getMessage());
		}
		return dropdownResponse;

	}

	@Override
	public int saveSRVComplexityPMData(String projectId, Map<String, Object> map, String sso) {
		int result = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(ProjectComplexityConstants.SAVE_SRV_PROJECT_COMPLEXITY_LIST);
			if (StringUtils.isNotBlank(map.get("scopeProduct").toString())) {
				pstm.setDouble(1, Double.parseDouble(map.get("scopeProduct").toString()));
			} else {
				pstm.setNull(1, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("otherLegalEntity").toString())) {
				pstm.setDouble(2, Double.parseDouble(map.get("otherLegalEntity").toString()));
			} else {
				pstm.setNull(2, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("passTrough").toString())) {
				pstm.setDouble(3, Double.parseDouble(map.get("passTrough").toString()));
			} else {
				pstm.setNull(3, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("customerReporting").toString())) {
				pstm.setDouble(4, Double.parseDouble(map.get("customerReporting").toString()));
			} else {
				pstm.setNull(4, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("location").toString())) {
				pstm.setDouble(5, Double.parseDouble(map.get("location").toString()));
			} else {
				pstm.setNull(5, Types.NULL);
			}
			if (null != map.get("mDollar") && StringUtils.isNotBlank(map.get("mDollar").toString())) {
				pstm.setDouble(6, Double.parseDouble(map.get("mDollar").toString()));
			} else {
				pstm.setNull(6, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForPMComplex").toString())) {
				pstm.setDouble(7, Double.parseDouble(map.get("actualScoreForPMComplex").toString()));
			} else {
				pstm.setNull(7, Types.NULL);
			}
			if (StringUtils.isNotBlank(map.get("actualScoreForFTEPM").toString())) {
				pstm.setDouble(8, Double.parseDouble(map.get("actualScoreForFTEPM").toString()));
			} else {
				pstm.setNull(8, Types.NULL);
			}
			if (null != map.get("notesForCustomScore")
					&& StringUtils.isNotBlank(map.get("notesForCustomScore").toString())) {
				pstm.setString(9, map.get("notesForCustomScore").toString());
			} else {
				pstm.setNull(9, Types.NULL);
			}
			pstm.setString(10, sso);
			pstm.setString(11, projectId);
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Data saved successfully for PM Complexity");
			}
		} catch (Exception e) {
			log.error("something went wrong while saving PM complexity SRV list:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while saving PM complexity SRV list:" + e.getMessage());
				}
			}
		}
		return result;

	}

	@Override
	public ProjectsSummaryDetailsDTO getProjectSummaryDetails(String projectId) {
		ProjectsSummaryDetailsDTO dto = new ProjectsSummaryDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_PROJECT_SUMMARY_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setPmName(null != rs.getString("pm_name") ? rs.getString("pm_name") : "");
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Summary details :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public int saveNEComplexityProjectTabData(NEComplexityTabDTO tabDTO, String sso) {
		int result = 0;
		deleteNEComplexityProjectTabData(tabDTO.getProjectId());
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(ProjectComplexityConstants.INSERT_NE_PROJECT_TAB_DATA);) {
			pstm.setString(1, tabDTO.getProjectId());
			pstm.setString(2, tabDTO.getSegment());
			pstm.setString(3, tabDTO.getWlSegment());
			pstm.setString(4, tabDTO.getNotesWlSegment());
			pstm.setString(5, tabDTO.getWlProjectCategory());
			pstm.setString(6, tabDTO.getNotesProjectCategory());
			pstm.setString(7, sso);
			pstm.setString(8, sso);
			if (pstm.executeUpdate() > 0) {
				result = 1;
			}
		} catch (SQLException e) {
			log.error("Error occured while saving NE Complexity Project Tab Data: " + e.getMessage());
		}
		return result;

	}

	private void deleteNEComplexityProjectTabData(String projectId) {
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(ProjectComplexityConstants.DELETE_NE_PROJECT_TAB_DATA);) {
			pstm.setString(1, projectId);
			int result = pstm.executeUpdate();
			if (result > 0) {
				log.info("Deleted NE Complexity Project Tab Data for Project Id :: " + projectId);
			}
		} catch (SQLException e) {
			log.error("Error occured while deleting NE Complexity Project Tab Data: " + e.getMessage());
		}

	}

	@Override
	public int saveITOComplexityProjectTabData(ITOComplexityTabDTO tabDTO, String sso) {
		int result = 0;
		deleteITOComplexityProjectTabData(tabDTO.getProjectId());
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.INSERT_ITO_PROJECT_TAB_DATA);) {
			pstm.setString(1, tabDTO.getProjectId());
			pstm.setString(2, tabDTO.getSegment());
			pstm.setString(3, tabDTO.getWlSegment());
			pstm.setString(4, tabDTO.getNotesWlSegment());
			pstm.setString(5, tabDTO.getWlProjectCategory());
			pstm.setString(6, tabDTO.getNotesProjectCategory());
			pstm.setString(7, sso);
			pstm.setString(8, sso);
			if (pstm.executeUpdate() > 0) {
				result = 1;
			}
		} catch (SQLException e) {
			log.error("Error occured while saving ITO Complexity Project Tab Data: " + e.getMessage());
		}
		return result;

	}

	private void deleteITOComplexityProjectTabData(String projectId) {
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.DELETE_ITO_PROJECT_TAB_DATA);) {
			pstm.setString(1, projectId);
			int result = pstm.executeUpdate();
			if (result > 0) {
				log.info("Deleted ITO Complexity Project Tab Data for Project Id :: " + projectId);
			}
		} catch (SQLException e) {
			log.error("Error occured while deleting ITO Complexity Project Tab Data: " + e.getMessage());
		}

	}

	@Override
	public List<DropDownDTO> getOrderTypeDropDown(String projectId) {
		String[] list = { "BR", "BR EXPIRED", "PO", "PO_ONLY ENG", "PO_LNTP", "PO x SPARE PARTS" };
		List<DropDownDTO> list1 = new ArrayList<DropDownDTO>();
		for (String obj : list) {
			DropDownDTO dto = new DropDownDTO();
			dto.setKey(obj);
			dto.setVal(obj);
			list1.add(dto);
		}
		return list1;

	}

	@Override
	public int saveNEComplexityProjectTabOrderDetails(NEComplexityTabDTO tabDTO, String sso) {
		int result = 0;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.UPDATE_NE_PROJECT_TAB_ORDER_DATA);) {
			pstm.setString(1, tabDTO.getOrderType());
			pstm.setString(2, tabDTO.getOrderTypeNotes());
			pstm.setString(3, sso);
			pstm.setString(4, tabDTO.getProjectId());
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Project Tab Order details updated successfully");
			}
		} catch (SQLException e) {
			log.error("Error occured while saving NE Complexity Project Tab Order Data: " + e.getMessage());
		}
		return result;

	}

	@Override
	public int saveITOComplexityProjectTabOrderDetails(ITOComplexityTabDTO tabDTO, String sso) {
		int result = 0;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.UPDATE_ITO_PROJECT_TAB_ORDER_DATA);) {
			pstm.setString(1, tabDTO.getOrderType());
			pstm.setString(2, tabDTO.getOrderTypeNotes());
			pstm.setString(3, sso);
			pstm.setString(4, tabDTO.getProjectId());
			if (pstm.executeUpdate() > 0) {
				result = 1;
				log.info("Project Tab Order details updated successfully");
			}
		} catch (SQLException e) {
			log.error("Error occured while saving ITO Complexity Project Tab Order Data: " + e.getMessage());
		}
		return result;

	}

	@Override
	public List<ITOSlotCCTDates> getITOSlotWFDatesList(String projectId) {
		List<ITOSlotCCTDates> list = new ArrayList<ITOSlotCCTDates>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_ITO_SLOT_WF_DELIVERY_DATES);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ITOSlotCCTDates dto = new ITOSlotCCTDates();
				dto.setTrnMfgComDt(null != rs.getString("o_trn_mfg_com_dt") ? rs.getString("o_trn_mfg_com_dt") : "");
				dto.setTrnConExwDt(null != rs.getString("o_trn_con_exw_dt") ? rs.getString("o_trn_con_exw_dt") : "");
				dto.setModMfgComDt(null != rs.getString("o_mod_mfg_com_dt") ? rs.getString("o_mod_mfg_com_dt") : "");
				dto.setModConExwDt(null != rs.getString("o_mod_con_exw_dt") ? rs.getString("o_mod_con_exw_dt") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Error in getITOSlotWFDatesList :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public DropDownDTO getOverallProgressPercent(String projectId) {
		DropDownDTO dto = new DropDownDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectComplexityConstants.GET_NE_PROJECT_COMPLEXITY_PERCENT_PROGRESS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setKey(null != rs.getString("o_overall_per") ? rs.getString("o_overall_per") : "");
				dto.setVal(null != rs.getString("o_project_date") ? rs.getString("o_project_date") : "");
			}
		} catch (Exception e) {
			log.error("Error in getOverallProgressPercent :: " + e.getMessage());
		}
		return dto;
	}
}
