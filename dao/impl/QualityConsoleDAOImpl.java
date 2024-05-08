package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IQualityConsoleDAO;
import com.bh.realtrack.dto.PCBomDetailsChartDTO;
import com.bh.realtrack.dto.PCPackingDetailsChartDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessBomDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessPackingDetailsDTO;
import com.bh.realtrack.dto.QualityConsoleInspectionDTO;
import com.bh.realtrack.dto.QualityConsoleMDRDTO;
import com.bh.realtrack.dto.QualityConsoleNonConformancesDTO;
import com.bh.realtrack.dto.QualityConsoleProjectDetailsDTO;
import com.bh.realtrack.dto.QualityConsoleProjectStatusDTO;
import com.bh.realtrack.dto.QualityConsolePunchListDTO;
import com.bh.realtrack.dto.QualityConsoleQCPDTO;
import com.bh.realtrack.dto.QualityConsoleTRSBookDTO;
import com.bh.realtrack.dto.QualityConsoleTRSMeetingDTO;
import com.bh.realtrack.util.QualityConsoleConstants;

@Repository
public class QualityConsoleDAOImpl implements IQualityConsoleDAO {

	private static final Logger log = LoggerFactory.getLogger(QualityConsoleDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public QualityConsoleProjectDetailsDTO getQualityConsoleProjectDetails(String projectId) {
		QualityConsoleProjectDetailsDTO dto = new QualityConsoleProjectDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_PROJECTS_DETAILS)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setProjectName(null != rs.getString("project_name_out") ? rs.getString("project_name_out") : "");
				dto.setProjectId(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				dto.setContractCustomer(
						null != rs.getString("contract_customer_out") ? rs.getString("contract_customer_out") : "");
				dto.setEndUser(null != rs.getString("end_user_out") ? rs.getString("end_user_out") : "");
				dto.setQpm(null != rs.getString("qpm_out") ? rs.getString("qpm_out") : "");
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setProjectScope(null != rs.getString("project_scope_out") ? rs.getString("project_scope_out") : "");
				dto.setContractValue(
						null != rs.getString("contract_value_out") ? rs.getString("contract_value_out") : "");
				dto.setActualProgress(
						null != rs.getString("actual_progress_out") ? rs.getString("actual_progress_out") : "");
			}
		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleProjectDetails() :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public QualityConsoleProjectStatusDTO getQualityConsoleProjectStatus(String projectId) {
		QualityConsoleProjectStatusDTO dto = new QualityConsoleProjectStatusDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_PROJECTS_STATUS)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setQcp(null != rs.getString("qcp_out") ? rs.getString("qcp_out") : "");
				dto.setPunchList(null != rs.getString("punchlist_out") ? rs.getString("punchlist_out") : "");
				dto.setCustNC(null != rs.getString("customernc_out") ? rs.getString("customernc_out") : "");
				dto.setMdr(null != rs.getString("mdr_out") ? rs.getString("mdr_out") : "");
				dto.setInspection(null != rs.getString("inspection_out") ? rs.getString("inspection_out") : "");
				dto.setTrs(null != rs.getString("trs_out") ? rs.getString("trs_out") : "");
				dto.setHighlights(null != rs.getString("qpm_highlights_out") ? rs.getString("qpm_highlights_out") : "");
			}
		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleProjectStatus() :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public QualityConsoleInspectionDTO getQualityConsoleInspectionDetails(String projectId) {
		QualityConsoleInspectionDTO dto = new QualityConsoleInspectionDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_INSPECTION_DETAILS)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setTotal(null != rs.getString("total_cnt") ? rs.getString("total_cnt") : "");
				dto.setExecuted(null != rs.getString("executed_cnt") ? rs.getString("executed_cnt") : "");
				dto.setNotified(null != rs.getString("notified_cnt") ? rs.getString("notified_cnt") : "");
				dto.setNotNotifiedWithFcstDt(
						null != rs.getString("not_notify_w_fcst_cnt") ? rs.getString("not_notify_w_fcst_cnt") : "");
				dto.setNotNotifiedWithOutFcstDt(
						null != rs.getString("not_notify_wo_fcst_cnt") ? rs.getString("not_notify_wo_fcst_cnt") : "");
				dto.setInspectionExecutionPer(
						null != rs.getString("execution_percent") ? rs.getString("execution_percent") : "");
				dto.setSourceInfo(null != rs.getString("source_info_out") ? rs.getString("source_info_out") : "");
				dto.setSourceMsg(null != rs.getString("source_msg_out") ? rs.getString("source_msg_out") : "");

			}

		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleInspection() :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public String getQualityConsoleQIRadarDetails(String projectId) {
		String count = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_QIRADAR_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				count = rs.getString("total_count");
			}
		} catch (SQLException e) {
			log.error("Exception in QualityConsoleDAOImpl :: getProjectConsoleQIRadarDetails() :: " + e.getMessage());
		}
		return count;
	}

	@Override
	public String getQualityConsoleDPUDetails(String projectId) {
		String dpu = "";
		try {
			dpu = "800";
		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleDPUDetails() :: " + e.getMessage());
		}
		return dpu;
	}

	@Override
	public QualityConsolePunchListDTO getQualityConsolePunchListDetails(String projectId) {
		QualityConsolePunchListDTO dto = new QualityConsolePunchListDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_PUNCH_LIST_DETAILS)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {

				dto.setTotal(null != rs.getString("total_cnt_out") ? rs.getString("total_cnt_out") : "");
				dto.setOpen(null != rs.getString("open_cnt_out") ? rs.getString("open_cnt_out") : "");
				dto.setSou(null != rs.getString("sou_cnt_out") ? rs.getString("sou_cnt_out") : "");
				dto.setShop(null != rs.getString("shop_cnt_out") ? rs.getString("shop_cnt_out") : "");
				dto.setSite(null != rs.getString("site_cnt_out") ? rs.getString("site_cnt_out") : "");
			}
		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsolePunchListDetails() :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public ProjectConsoleCompletenessBomDetailsDTO getQualityConsoleBomDetails(String projectId) {
		ProjectConsoleCompletenessBomDetailsDTO list = new ProjectConsoleCompletenessBomDetailsDTO();
		PCBomDetailsChartDTO data = new PCBomDetailsChartDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_COMPLETENESS_BOM_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				list.setTotalBomLines(null != rs.getString("totalbom_lines") ? rs.getString("totalbom_lines") : "");
				list.setBomActualPercentage(
						null != rs.getString("bomactual_percnt") ? rs.getString("bomactual_percnt") : "");
				data.setBomLineNotReceived(
						null != rs.getString("bomline_not_received") ? rs.getString("bomline_not_received") : "");
				data.setBomLineReceived(
						null != rs.getString("bomline_received") ? rs.getString("bomline_received") : "");
				list.setBomDetailsChart(data);
			}
		} catch (SQLException e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleBomDetails() :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public ProjectConsoleCompletenessPackingDetailsDTO getQualityConsolePackingDetails(String projectId,
			boolean isModuleProject) {
		ProjectConsoleCompletenessPackingDetailsDTO list = new ProjectConsoleCompletenessPackingDetailsDTO();
		PCPackingDetailsChartDTO data = new PCPackingDetailsChartDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_PROJECT_QUALITY_COMPLETENESS_PACKING_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (!isModuleProject) {
					list.setPkgLineOfReceivedMaterial(null != rs.getString("packinglines_of_received_material")
							? rs.getString("packinglines_of_received_material")
							: "");
					list.setPkgActualPercent(
							null != rs.getString("pkgactual_percent") ? rs.getString("pkgactual_percent") : "");
					data.setPkgLineCompleted(
							null != rs.getString("pkgline_completed") ? rs.getString("pkgline_completed") : "");
					data.setPkgLineOpened(null != rs.getString("pkgline_opened") ? rs.getString("pkgline_opened") : "");
					list.setPackingDetailsChart(data);
				}
			}
		} catch (SQLException e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsolePackingDetails() :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<String> getModuleProjects(String projectId) {
		List<String> projectIdList = new ArrayList<String>();
		String moduleProjectId = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(QualityConsoleConstants.GET_MODULE_PROJECT_LIST)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				moduleProjectId = rs.getString(1);
				if (null != moduleProjectId) {
					projectIdList.add(moduleProjectId);
				}
			}
		} catch (Exception exception) {
			log.error("Error in getting Project Console Module Projects  ::  " + exception.getMessage());
		}
		return projectIdList;
	}

	@Override
	public QualityConsoleNonConformancesDTO getQualityConsoleNonConformancesDetails(String projectId) {
		QualityConsoleNonConformancesDTO dto = new QualityConsoleNonConformancesDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_NON_CONFORMANCES_DETAILS)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setTotal(null != rs.getString("total_cnt") ? rs.getString("total_cnt") : "");
				dto.setOpen(null != rs.getString("open_cnt") ? rs.getString("open_cnt") : "");
				dto.setMfg(null != rs.getString("mfg_cnt") ? rs.getString("mfg_cnt") : "");
				dto.setVendor(null != rs.getString("vendor_cnt") ? rs.getString("vendor_cnt") : "");
				dto.setSite(null != rs.getString("site_cnt") ? rs.getString("site_cnt") : "");
			}
		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleNonConformancesDetails() :: "
					+ e.getMessage());
		}
		return dto;
	}

	@Override
	public QualityConsoleQCPDTO getQualityConsoleQCPDetails(String projectId) {
		QualityConsoleQCPDTO dto = new QualityConsoleQCPDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_QCP_DETAILS)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setTotal(null != rs.getString("total_out") ? rs.getString("total_out") : "");
				dto.setFinalizationPer(
						null != rs.getString("finalization_percent_out") ? rs.getString("finalization_percent_out")
								: "");
			}
		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleQCPDetails() :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public QualityConsoleMDRDTO getQualityConsoleMDRDetails(String projectId) {
		QualityConsoleMDRDTO dto = new QualityConsoleMDRDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_MDR_DETAILS)) {
			pstm.setString(1, projectId);
			System.out.println(pstm);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setTotal(null != rs.getString("total_out") ? rs.getString("total_out") : "");
				dto.setIssuancePer(
						null != rs.getString("issuance_percent_out") ? rs.getString("issuance_percent_out") : "");
				// dto.setCompletenessPer(null != rs.getString("total_out") ?
				// rs.getString("total_out") : "");
			}
		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleMDRDetails() :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public QualityConsoleTRSBookDTO getQualityConsoleTRSBookDetails(String projectId) {
		QualityConsoleTRSBookDTO dto = new QualityConsoleTRSBookDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_TRS_BOOK_DETAILS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dto.setCompletenessPer(
						null != rs.getString("completeness_percent") ? rs.getString("completeness_percent") : "");
				dto.setTotalLines(null != rs.getString("total") ? rs.getString("total") : "");
			}
		} catch (Exception e) {
			log.error("Exception in QualityConsoleDAOImpl :: getQualityConsoleTRSBookDetails() :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public QualityConsoleTRSMeetingDTO getQualityConsoleTRSMeetingDetails(String projectId) {
		QualityConsoleTRSMeetingDTO dto = new QualityConsoleTRSMeetingDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(QualityConsoleConstants.GET_QUALITY_CONSOLE_TRS_MEETING_DETAILS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dto.setTotalAction(null != rs.getString("total") ? rs.getString("total") : "");
				dto.setOpen(null != rs.getString("open") ? rs.getString("open") : "");
				dto.setOverdue(null != rs.getString("overdue") ? rs.getString("overdue") : "");
				dto.setFutureDue(null != rs.getString("future_due") ? rs.getString("future_due") : "");
			}
		} catch (Exception e) {
			log.error(
					"Exception in QualityConsoleDAOImpl :: getQualityConsoleTRSMeetingDetails() :: " + e.getMessage());
		}
		return dto;
	}

}
