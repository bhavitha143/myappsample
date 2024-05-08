package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IProjectControlDAO;
import com.bh.realtrack.dto.PCBomDetailsChartDTO;
import com.bh.realtrack.dto.PCPackingDetailsChartDTO;
import com.bh.realtrack.dto.ProjectConsoleApplicableFlagDTO;
import com.bh.realtrack.dto.ProjectConsoleBillingStatusDTO;
import com.bh.realtrack.dto.ProjectConsoleCMDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleCashCollectionDTO;
import com.bh.realtrack.dto.ProjectConsoleCashDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessBomDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleCompletenessPackingDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleConfiguratorDTO;
import com.bh.realtrack.dto.ProjectConsoleDeckContractualDeliveryDTO;
import com.bh.realtrack.dto.ProjectConsoleDeckStatusDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleEngDocStatusDTO;
import com.bh.realtrack.dto.ProjectConsoleFinancialDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleInspectionDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleNextToBillDTO;
import com.bh.realtrack.dto.ProjectConsolePendingStatusDTO;
import com.bh.realtrack.dto.ProjectConsoleProcurementDeliveryDataDTO;
import com.bh.realtrack.dto.ProjectConsoleProcurementPlacementDataDTO;
import com.bh.realtrack.dto.ProjectConsoleProjectDetailsDTO;
import com.bh.realtrack.dto.ProjectConsolePunchListDTO;
import com.bh.realtrack.dto.ProjectConsoleScurveDataDTO;
import com.bh.realtrack.dto.ProjectConsoleScurveTableDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleShipmentDetailsDTO;
import com.bh.realtrack.dto.ProjectConsoleTotalRisksDTO;
import com.bh.realtrack.dto.ProjectConsoleWeightDataDTO;
import com.bh.realtrack.util.ProjectControlConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ProjectControlDAOImpl implements IProjectControlDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger log = LoggerFactory.getLogger(ProjectControlDAOImpl.class);

	@Override
	public ProjectConsoleApplicableFlagDTO checkProjectConsoleApplicableFlag(String projectId, String sso) {
		ProjectConsoleApplicableFlagDTO dto = new ProjectConsoleApplicableFlagDTO();
		String flag = "N";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.CHECK_PROJECT_CONSOLE_APPLICABLE_FLAG)) {
			pstm.setString(1, sso);
			pstm.setString(2, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				flag = null != rs.getString("flag") ? rs.getString("flag") : "N";
			}
		} catch (Exception e) {
			log.error("Exception :: checkProjectConsoleApplicableFlag :: " + e.getMessage());
		}
		dto.setApplicable(flag);
		return dto;
	}

	@Override
	public ProjectConsoleProjectDetailsDTO getProjectConsoleProjectDetails(String projectId, String businessUnit) {
		ProjectConsoleProjectDetailsDTO responseDto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(ProjectControlConstants.GET_CONSOLE_PROJECT_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseDto = new ProjectConsoleProjectDetailsDTO();
				responseDto.setProjectId(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				responseDto.setProjectName(
						null != rs.getString("project_name_out") ? rs.getString("project_name_out") : "");
				responseDto.setProjectManager(
						null != rs.getString("project_manager_out") ? rs.getString("project_manager_out") : "");
				if (null != businessUnit && !businessUnit.isEmpty() && businessUnit.equalsIgnoreCase("SRV")) {
					responseDto.setExecutionModel(
							null != rs.getString("execution_model_out") ? rs.getString("execution_model_out") : "");
				} else {
					responseDto.setExecutionModel("");
				}
			}

		} catch (SQLException e) {
			log.error("Exception while getting Project Console Details :: " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public ProjectConsoleCMDetailsDTO getProjectConsoleCMAnalysisDetails(String projectId) {
		ProjectConsoleCMDetailsDTO responseDto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_CM_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseDto = new ProjectConsoleCMDetailsDTO();
				responseDto.setAsSoldCmPer(null != rs.getString("cm_per_as_out") ? rs.getString("cm_per_as_out") : "");
				responseDto
						.setLastBCECmPer(null != rs.getString("cm_per_bce_out") ? rs.getString("cm_per_bce_out") : "");
				responseDto.setEacCmPer(null != rs.getString("cm_per_eac_out") ? rs.getString("cm_per_eac_out") : "");
				responseDto.setCmIndicator(
						null != rs.getString("cm_indicator_out") ? rs.getString("cm_indicator_out") : "");
				responseDto.setWarningFlag(
						null != rs.getString("warning_flag_out") ? rs.getString("warning_flag_out") : "");
				if (responseDto.getWarningFlag().equalsIgnoreCase("Y")) {
					responseDto.setWarningMessage("Existing Non-NP scope not accounted in CM Analysis");
				} else {
					responseDto.setWarningMessage("");
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console CM Analysis Details :: " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public ProjectConsoleFinancialDetailsDTO getProjectConsoleFinancialSummaryDetails(String projectId) {
		ProjectConsoleFinancialDetailsDTO responseDto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_FINANCIAL_SUMMARY);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseDto = new ProjectConsoleFinancialDetailsDTO();
				responseDto.setContractValue(
						null != rs.getString("contract_value_out") ? rs.getString("contract_value_out") : "");
				responseDto.setInvoicedAmount(
						null != rs.getString("invoice_amt_out") ? rs.getString("invoice_amt_out") : "");
				responseDto.setCashCollected(
						null != rs.getString("cash_collected_out") ? rs.getString("cash_collected_out") : "");
				responseDto.setPastDue(null != rs.getString("past_due_out") ? rs.getString("past_due_out") : "");
			}

		} catch (SQLException e) {
			log.error("Exception while getting Project Console Financial Summary Details :: " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public Map<String, Object> getProjectConsoleCashCollectionDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ProjectConsoleCashDTO> pastDueList = new ArrayList<ProjectConsoleCashDTO>();
		List<ProjectConsoleCashDTO> currentQuarterList = new ArrayList<ProjectConsoleCashDTO>();
		List<ProjectConsoleCashDTO> futureDueList = new ArrayList<ProjectConsoleCashDTO>();
		ProjectConsoleCashCollectionDTO pastDueDTO = new ProjectConsoleCashCollectionDTO();
		ProjectConsoleCashCollectionDTO currentQuarterDTO = new ProjectConsoleCashCollectionDTO();
		ProjectConsoleCashCollectionDTO futureDueDTO = new ProjectConsoleCashCollectionDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_CASH_COLLECTION);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleCashDTO dto = new ProjectConsoleCashDTO();
				dto.setTypes(null != rs.getString("types_out") ? rs.getString("types_out") : "");
				dto.setDisputeColor(null != rs.getString("dispute_color_out") ? rs.getString("dispute_color_out") : "");
				dto.setCount(null != rs.getString("count_out") ? rs.getString("count_out") : "");
				dto.setSum(null != rs.getString("amount_out") ? rs.getString("amount_out") : "");
				if (dto.getTypes().equalsIgnoreCase(ProjectControlConstants.PAST_DUE_CHART_TYPE)) {
					pastDueList.add(dto);
				} else if (dto.getTypes().equalsIgnoreCase(ProjectControlConstants.CURRENT_DUE_CHART_TYPE)) {
					currentQuarterList.add(dto);
				} else if (dto.getTypes().equalsIgnoreCase(ProjectControlConstants.FUTURE_DUE_CHART_TYPE)) {
					futureDueList.add(dto);
				}
			}
			pastDueDTO = getProjectControlCashResponse(pastDueList);
			currentQuarterDTO = getProjectControlCashResponse(currentQuarterList);
			futureDueDTO = getProjectControlCashResponse(futureDueList);
			responseMap.put("pastDue", pastDueDTO);
			responseMap.put("currentQuarter", currentQuarterDTO);
			responseMap.put("futureDue", futureDueDTO);
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Cash Collection Details :: " + e.getMessage());
		}
		return responseMap;
	}

	public ProjectConsoleCashCollectionDTO getProjectControlCashResponse(List<ProjectConsoleCashDTO> list) {
		ProjectConsoleCashCollectionDTO dto = new ProjectConsoleCashCollectionDTO();
		int totalCount = 0;
		Double totalAmount = 0.0;
		for (ProjectConsoleCashDTO obj : list) {
			if (obj.getDisputeColor().equalsIgnoreCase("Black")) {
				dto.setDisputedAmount(obj.getSum());
				dto.setDisputedCount(obj.getCount());
			} else if (obj.getDisputeColor().equalsIgnoreCase("Red")) {
				dto.setCollectionEscalatedAmount(obj.getSum());
				dto.setCollectionEscalatedCount(obj.getCount());
			} else if (obj.getDisputeColor().equalsIgnoreCase("Orange")) {
				dto.setNotDisputedAmount(obj.getSum());
				dto.setNotDisputedCount(obj.getCount());
			} else if (obj.getDisputeColor().equalsIgnoreCase("Green")) {
				dto.setCommitToPayAmount(obj.getSum());
				dto.setCommitToPayCount(obj.getCount());
			} else if (obj.getDisputeColor().equalsIgnoreCase("Blue")) {
				dto.setCreditNoteAmount(obj.getSum());
				dto.setCreditNoteCount(obj.getCount());
			}
			totalCount = totalCount + Integer.parseInt(obj.getCount());
			totalAmount = totalAmount + Double.parseDouble(obj.getSum());
			dto.setTotalCount(String.valueOf(totalCount));
			dto.setTotalAmount(Double.toString(Math.round(totalAmount * 100.0) / 100.0));
		}
		return dto;
	}

	@Override
	public ProjectConsoleEngDocStatusDTO getProjectConsoleEngineeringDocStatus(String projectId) {
		ProjectConsoleEngDocStatusDTO responseDto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_ENGINEERING_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseDto = new ProjectConsoleEngDocStatusDTO();
				responseDto.setDocFirstIssueOTD(
						null != rs.getString("completed_on_time") ? rs.getString("completed_on_time") : "");
				responseDto
						.setDocFirstIssueOTDColor(null != rs.getString("color_code") ? rs.getString("color_code") : "");
				responseDto
						.setTotalNoOfDocs(null != rs.getString("totaldoccount") ? rs.getString("totaldoccount") : "");
				responseDto.setFinalizedDocs(
						null != rs.getString("finalizeddoccount") ? rs.getString("finalizeddoccount") : "");
				responseDto.setPendingDocs(null != rs.getString("pendingstatus") ? rs.getString("pendingstatus") : "");
				responseDto.setFinalizationPer(
						null != rs.getString("finalizationper") ? rs.getString("finalizationper") : "");
			}

		} catch (SQLException e) {
			log.error("Exception while getting Project Console Engineering Doc Status Details :: " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public Map<String, String> getProjectConsoleEngineeringDocPendingStatus(String projectId) {
		Map<String, String> map = new HashMap<String, String>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_PENDING_STATUS_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsolePendingStatusDTO dto = new ProjectConsolePendingStatusDTO();
				dto.setDocStatus(null != rs.getString("doc_status_out") ? rs.getString("doc_status_out") : "");
				dto.setCount(rs.getInt("cnt_out"));
				map.put(dto.getDocStatus(), Integer.toString(dto.getCount()));
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Total Risks :: " + e.getMessage());
		}
		return map;
	}

	@Override
	public ProjectConsolePunchListDTO getProjectConsolePunchListDetails(String projectId) {
		ProjectConsolePunchListDTO responseDto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_PUNCHLIST_DETAILS)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseDto = new ProjectConsolePunchListDTO();
				responseDto.setOpen(rs.getInt("open_count"));
				responseDto.setShop(rs.getInt("open_shop_phase"));
				responseDto.setSourcing(rs.getInt("open_sourcing_phase"));
				responseDto.setTotal(rs.getInt("total_count"));
			}

		} catch (SQLException e) {
			log.error("Exception while getting Project Console PunchList Details :: " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public Map<String, String> getProjectConsoleTotalRisks(String projectId, String sso) {
		Map<String, String> map = new HashMap<String, String>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_TOTAL_RISKS);) {
			pstm.setString(1, projectId);
			pstm.setString(2, sso);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleTotalRisksDTO dto = new ProjectConsoleTotalRisksDTO();
				dto.setDescription(
						null != rs.getString("attribute_value_out") ? rs.getString("attribute_value_out") : "");
				dto.setCnt(rs.getInt("cnt_out"));
				map.put(dto.getDescription(), Integer.toString(dto.getCnt()));
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Total Risks :: " + e.getMessage());
		}
		return map;
	}

	@Override
	public ProjectConsoleShipmentDetailsDTO getProjectConsoleShipmentDetails(String projectId) {
		ProjectConsoleShipmentDetailsDTO dto = new ProjectConsoleShipmentDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SHIPMENT_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setMaterialToBeRecived(rs.getInt("mat_to_be_received"));
				dto.setMaterialToBePacked(rs.getInt("mat_to_be_packed"));
				dto.setBoxesPacked(rs.getInt("boxes_packed"));
				dto.setReadyToShip(rs.getInt("ready_to_ship"));
				dto.setBoxesShipped(rs.getInt("boxes_shipped"));
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Shipment Details :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public ProjectConsoleCompletenessBomDetailsDTO getProjectConsoleCompletenessBomDetails(String projectId) {
		ProjectConsoleCompletenessBomDetailsDTO list = new ProjectConsoleCompletenessBomDetailsDTO();
		PCBomDetailsChartDTO data = new PCBomDetailsChartDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_COMPLETENESS_BOM_DETAILS);) {
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
			log.error("Exception while getting Project Console Completeness Bom Details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public ProjectConsoleCompletenessPackingDetailsDTO getProjectConsoleCompletenessPackingDetails(String projectId,
			boolean isModuleProject) {
		ProjectConsoleCompletenessPackingDetailsDTO list = new ProjectConsoleCompletenessPackingDetailsDTO();
		PCPackingDetailsChartDTO data = new PCPackingDetailsChartDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_COMPLETENESS_PACKING_DETAILS);) {
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
			log.error("Exception while getting Project Console Completeness Packing Details :: " + e.getMessage());
		}

		return list;
	}

	@Override
	public List<String> getModuleProjects(String projectId) {
		List<String> projectIdList = new ArrayList<String>();
		String moduleProjectId = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(ProjectControlConstants.GET_MODULE_PROJECT_LIST)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				moduleProjectId = rs.getString(1);
				if (null != moduleProjectId) {
					projectIdList.add(moduleProjectId);
				}
			}
		} catch (Exception exception) {
			log.error("Error in getting Project Console Module Projects :: " + exception.getMessage());
		}

		return projectIdList;

	}

	@Override
	public boolean checkProjectConsoleExceptionProjects(String projectId, String sso) {
		String expProjectId = null;
		boolean flag = false;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_EXCEPTION_PROJECT_LIST)) {
			ps.setString(1, sso);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				expProjectId = rs.getString(1);
				if (null != expProjectId && !expProjectId.isEmpty() && expProjectId.equalsIgnoreCase(projectId)) {
					flag = true;
				}
			}
		} catch (Exception e) {
			flag = false;
			log.error("Error in ProjectControlDAOImpl :: checkProjectConsoleExceptionProjects :: " + e.getMessage());
		}
		return flag;
	}

	@Override
	public ProjectConsoleProcurementPlacementDataDTO getProjectConsoleProcurementPlacementDetails(String projectId,
			boolean customizedFlag) {
		ProjectConsoleProcurementPlacementDataDTO dto = new ProjectConsoleProcurementPlacementDataDTO();
		String query = "";
		if (customizedFlag) {
			query = ProjectControlConstants.GET_PROJECT_CONSOLE_PROCUREMENT_PLACEMENT_CUSTOM_DETAILS;

		} else {
			query = ProjectControlConstants.GET_PROJECT_CONSOLE_PROCUREMENT_PLACEMENT_DETAILS;
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setTotal(null != rs.getString("total") ? rs.getString("total") : "");
				dto.setPlaced(null != rs.getString("placed") ? rs.getString("placed") : "");
				dto.setPlacementPer(
						null != rs.getString("placement_percentage") ? rs.getString("placement_percentage") : "");
				dto.setPlacementOTDEarly(null != rs.getString("otd_early") ? rs.getString("otd_early") : "");
				dto.setPlacementOTDLate(null != rs.getString("otd_late") ? rs.getString("otd_late") : "");
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Procurement Placement Details :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public ProjectConsoleProcurementDeliveryDataDTO getProjectConsoleProcurementDeliveryDetails(String projectId,
			boolean customizedFlag) {
		ProjectConsoleProcurementDeliveryDataDTO dto = new ProjectConsoleProcurementDeliveryDataDTO();
		String query = "";
		if (customizedFlag) {
			query = ProjectControlConstants.GET_PROJECT_CONSOLE_PROCUREMENT_DELIVERY_CUSTOM_DETAILS;

		} else {
			query = ProjectControlConstants.GET_PROJECT_CONSOLE_PROCUREMENT_DELIVERY_DETAILS;
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setTotal(null != rs.getString("total") ? rs.getString("total") : "");
				dto.setDelivered(null != rs.getString("delivered") ? rs.getString("delivered") : "");
				dto.setDeliveredPer(
						null != rs.getString("delivery_percentage") ? rs.getString("delivery_percentage") : "");
				dto.setDeliveryOTD(null != rs.getString("po_delivery_otd") ? rs.getString("po_delivery_otd") : "");
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Procurement Delivery Details :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public ProjectConsoleInspectionDetailsDTO getProjectConsoleInspectionDetails(String projectId) {
		ProjectConsoleInspectionDetailsDTO dto = new ProjectConsoleInspectionDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_INSPECTION_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String source = "";
				dto.setTotal(null != rs.getString("total_out") ? rs.getString("total_out") : "");
				dto.setCustObservation(
						null != rs.getString("total_cust_observation_out") ? rs.getString("total_cust_observation_out")
								: "");
				dto.setCustWitness(
						null != rs.getString("total_cust_witness_out") ? rs.getString("total_cust_witness_out") : "");
				dto.setExecutedCustObservation(
						null != rs.getString("exec_cust_observation_out") ? rs.getString("exec_cust_observation_out")
								: "");
				dto.setExecutedCustWitness(
						null != rs.getString("exec_cust_witness_out") ? rs.getString("exec_cust_witness_out") : "");
				dto.setExecutionPer(
						null != rs.getString("exec_percentage_out") ? rs.getString("exec_percentage_out") : "");
				source = null != rs.getString("source_info_out") ? rs.getString("source_info_out") : "";
				dto.setSourceMsg(null != rs.getString("source_msg_out") ? rs.getString("source_msg_out") : "");
				if (null != source && !source.equalsIgnoreCase("")) {
					if (source.equalsIgnoreCase("last published")) {
						dto.setSource("Last Published View");
					} else if (source.equalsIgnoreCase("last updated external")) {
						dto.setSource("Last Updated External View");
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Inspection Details :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingITOBaselineCurve(String projectId) {
		List<ProjectConsoleBillingStatusDTO> list = new ArrayList<ProjectConsoleBillingStatusDTO>();
		ProjectConsoleBillingStatusDTO dto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_BILLING_ITO_BASELINE_CURVE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new ProjectConsoleBillingStatusDTO();
				dto.setDisplayDate(null != rs.getString("epoch_date_out") ? rs.getString("epoch_date_out") : "");
				dto.setDisplayAmount(
						null != rs.getString("cum_disp_amount_out") ? rs.getString("cum_disp_amount_out") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error(
					"Exception while getting Project Console Billing ITO Baseline Curve Details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingActualCurve(String projectId) {
		List<ProjectConsoleBillingStatusDTO> list = new ArrayList<ProjectConsoleBillingStatusDTO>();
		ProjectConsoleBillingStatusDTO dto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_BILLING_ACTUAL_CURVE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new ProjectConsoleBillingStatusDTO();
				dto.setDisplayDate(null != rs.getString("epoch_date_out") ? rs.getString("epoch_date_out") : "");
				dto.setDisplayAmount(
						null != rs.getString("cum_disp_amount_out") ? rs.getString("cum_disp_amount_out") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Billing Actual Curve Details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingBaselineCurve(String projectId) {
		List<ProjectConsoleBillingStatusDTO> list = new ArrayList<ProjectConsoleBillingStatusDTO>();
		ProjectConsoleBillingStatusDTO dto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_BILLING_BASELINE_CURVE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new ProjectConsoleBillingStatusDTO();
				dto.setDisplayDate(null != rs.getString("epoch_date_out") ? rs.getString("epoch_date_out") : "");
				dto.setDisplayAmount(
						null != rs.getString("cum_disp_amount_out") ? rs.getString("cum_disp_amount_out") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Billing Baseline Curve Details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingBlankForecast(String projectId) {
		List<ProjectConsoleBillingStatusDTO> list = new ArrayList<ProjectConsoleBillingStatusDTO>();
		ProjectConsoleBillingStatusDTO dto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_BILLING_BLANK_FORECAST);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new ProjectConsoleBillingStatusDTO();
				dto.setDisplayDate(null != rs.getString("epoch_date_out") ? rs.getString("epoch_date_out") : "");
				dto.setDisplayAmount(
						null != rs.getString("cum_disp_amount_out") ? rs.getString("cum_disp_amount_out") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error(
					"Exception while getting Project Console Billing Blank Forecast Date Details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingFinancialBLCurve(String projectId) {
		List<ProjectConsoleBillingStatusDTO> list = new ArrayList<ProjectConsoleBillingStatusDTO>();
		ProjectConsoleBillingStatusDTO dto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_BILLING_FINANCIAL_CURVE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new ProjectConsoleBillingStatusDTO();
				dto.setDisplayDate(null != rs.getString("epoch_date_out") ? rs.getString("epoch_date_out") : "");
				dto.setDisplayAmount(
						null != rs.getString("cum_disp_amount_out") ? rs.getString("cum_disp_amount_out") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error(
					"Exception while getting Project Console Billing Financial BL Curve Details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public List<ProjectConsoleBillingStatusDTO> getProjectConsoleBillingForecastCurve(String projectId) {
		List<ProjectConsoleBillingStatusDTO> list = new ArrayList<ProjectConsoleBillingStatusDTO>();
		ProjectConsoleBillingStatusDTO dto = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_BILLING_FORECAST_CURVE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new ProjectConsoleBillingStatusDTO();
				dto.setDisplayDate(null != rs.getString("epoch_date_out") ? rs.getString("epoch_date_out") : "");
				dto.setDisplayAmount(
						null != rs.getString("cum_disp_amount_out") ? rs.getString("cum_disp_amount_out") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Billing Forecast Curve Details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public String getProjectConsoleBillingNextToBillDetails(String projectId) {
		String nextToBill = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_NEXT_TO_BILL_AMOUNT)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				nextToBill = rs.getString(1);
			}
		} catch (Exception exception) {
			log.error("Exception while getting Project Console Billing Next To Bill Details :: "
					+ exception.getMessage());
		}
		return nextToBill;
	}

	@Override
	public String getProjectConsoleBillingNextToBillInDays(String projectId) {
		String inDays = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_NEXT_TO_BILL_IN_DAYS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				inDays = rs.getString(1);
			}
		} catch (Exception exception) {
			log.error("Exception while getting Project Console Billing Next To Bill In Days  :: "
					+ exception.getMessage());
		}
		return inDays;
	}

	@Override
	public List<ProjectConsoleNextToBillDTO> getProjectConsoleBillingNextToBillList(String projectId) {
		List<ProjectConsoleNextToBillDTO> list = new ArrayList<ProjectConsoleNextToBillDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_NEXT_TO_BILL_DETAILS);) {
			pstm.setString(1, projectId);
			pstm.setString(2, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleNextToBillDTO dto = new ProjectConsoleNextToBillDTO();
				dto.setMilestoneId(null != rs.getString(1) ? rs.getString(1) : "");
				dto.setMilestoneDesc(null != rs.getString(2) ? rs.getString(2) : "");
				dto.setOrigAmount(null != rs.getString(3) ? rs.getString(3) : "");
				dto.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
				dto.setConvertedAmount(null != rs.getString(5) ? rs.getString(5) : "");
				dto.setStatus(null != rs.getString(6) ? rs.getString(6) : "");
				dto.setForecastDate(null != rs.getString(7) ? rs.getString(7) : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Billing Next To Bill Details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public ProjectConsoleConfiguratorDTO getProjectConsoleSCurveSelectedDetails(String projectId) {
		String projectConfig = null;
		ObjectMapper mapper = new ObjectMapper();
		ProjectConsoleConfiguratorDTO configDTO = new ProjectConsoleConfiguratorDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_SELECTED_DETAILS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				projectConfig = rs.getString("config_object");
			}
			configDTO = mapper.readValue(projectConfig, ProjectConsoleConfiguratorDTO.class);
		} catch (Exception exception) {
			log.error("Error in getting Project Console Scurve Selected Details :: " + exception.getMessage());
		}
		return configDTO;
	}

	@Override
	public List<ProjectConsoleScurveDataDTO> getProjectConsoleSCurveDetails(String projectId, String jobs,
			String epsContractId, String customWeight, List<String> projectDisplayInfoList) {
		List<ProjectConsoleScurveDataDTO> list = new ArrayList<ProjectConsoleScurveDataDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_DETAILS)) {
			String[] subProjectStr = jobs.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setString(3, customWeight);
			pstm.setString(4, epsContractId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleScurveDataDTO dto = new ProjectConsoleScurveDataDTO();
				dto.setProjectDate(null != rs.getString("projectdate_out") ? rs.getString("projectdate_out") : "");
				if (projectDisplayInfoList.contains("Actual")) {
					dto.setActual(null != rs.getString("actual_out") ? rs.getFloat("actual_out") : null);
				}
				if (projectDisplayInfoList.contains("Forecast")) {
					dto.setForecast(null != rs.getString("forecast_out") ? rs.getFloat("forecast_out") : null);
				}
				if (projectDisplayInfoList.contains("Late Forecast")) {
					dto.setLateForecast(
							null != rs.getString("lateforecast_out") ? rs.getFloat("lateforecast_out") : null);
				}
				if (projectDisplayInfoList.contains("Baseline")) {
					dto.setBaseline(null != rs.getString("baseline_out") ? rs.getFloat("baseline_out") : null);
				}
				if (projectDisplayInfoList.contains("Baseline Late")) {
					dto.setBaselineLate(
							null != rs.getString("baselinelate_out") ? rs.getFloat("baselinelate_out") : null);
				}
				dto.setSpi(null != rs.getString("spi_out") ? rs.getString("spi_out") : "");
				dto.setTfi(null != rs.getString("tfi_out") ? rs.getString("tfi_out") : "");
				list.add(dto);
			}
		} catch (Exception exception) {
			log.error("Error in getting Project Console Scurve Details :: " + exception.getMessage());
		}
		return list;
	}

	@Override
	public List<ProjectConsoleScurveTableDetailsDTO> getProjectConsoleSCurveTableDetails(String projectId, String jobs,
			String epsContractId, String customWeight) {
		List<ProjectConsoleScurveTableDetailsDTO> list = new ArrayList<ProjectConsoleScurveTableDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_TABLE_DETAILS)) {
			String[] subProjectStr = jobs.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setString(3, customWeight);
			pstm.setString(4, epsContractId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleScurveTableDetailsDTO dto = new ProjectConsoleScurveTableDetailsDTO();
				String departmentName = "";
				departmentName = null != rs.getString("department_name_out") ? rs.getString("department_name_out") : "";
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("ENG")) {
					dto.setDepartmentName("Engineering");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(1);
					list.add(dto);
				}
				if (null != departmentName && !departmentName.isEmpty()
						&& departmentName.equalsIgnoreCase("PO Issue")) {
					dto.setDepartmentName("PO Placement");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(2);
					list.add(dto);
				}
				if (null != departmentName && !departmentName.isEmpty()
						&& departmentName.equalsIgnoreCase("Procurement")) {
					dto.setDepartmentName("PO Delivery");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(3);
					list.add(dto);
				}
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("MFG")) {
					dto.setDepartmentName("Manufacturing");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(4);
					list.add(dto);
				}
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("OVERALL")) {
					dto.setDepartmentName("OVERALL");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(5);
					list.add(dto);
				}
			}
			list.sort(Comparator.comparing(ProjectConsoleScurveTableDetailsDTO::getOrderNo));
		} catch (Exception exception) {
			log.error("Error in getting Project Console Scurve Table Details :: " + exception.getMessage());
		}
		return list;
	}

	@Override
	public List<ProjectConsoleScurveDataDTO> getProjectConsoleWeightedSCurveDetails(String projectId, String jobs,
			String epsContractId, String customWeight, List<String> projectDisplayInfoList) {
		List<ProjectConsoleScurveDataDTO> list = new ArrayList<ProjectConsoleScurveDataDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_WEIGHTED_SCURVE_DETAILS)) {
			String[] subProjectStr = jobs.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setString(3, customWeight);
			pstm.setString(4, epsContractId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleScurveDataDTO dto = new ProjectConsoleScurveDataDTO();
				dto.setProjectDate(null != rs.getString("projectdate_out") ? rs.getString("projectdate_out") : "");
				if (projectDisplayInfoList.contains("Actual")) {
					dto.setActual(null != rs.getString("actual_out") ? rs.getFloat("actual_out") : null);
				}
				if (projectDisplayInfoList.contains("Forecast")) {
					dto.setForecast(null != rs.getString("forecast_out") ? rs.getFloat("forecast_out") : null);
				}
				if (projectDisplayInfoList.contains("Late Forecast")) {
					dto.setLateForecast(
							null != rs.getString("late_forecast_out") ? rs.getFloat("late_forecast_out") : null);
				}
				if (projectDisplayInfoList.contains("Baseline")) {
					dto.setBaseline(
							null != rs.getString("baseline_data_out") ? rs.getFloat("baseline_data_out") : null);
				}
				if (projectDisplayInfoList.contains("Baseline Late")) {
					dto.setBaselineLate(
							null != rs.getString("baseline_late_out") ? rs.getFloat("baseline_late_out") : null);
				}
				dto.setSpi(null != rs.getString("spi_out") ? rs.getString("spi_out") : "");
				dto.setTfi(null != rs.getString("tfi_out") ? rs.getString("tfi_out") : "");
				list.add(dto);
			}
		} catch (Exception exception) {
			log.error("Error in getting Project Console Weighted Scurve Details :: " + exception.getMessage());
		}
		return list;
	}

	@Override
	public List<ProjectConsoleScurveTableDetailsDTO> getProjectConsoleWeightedSCurveTableDetails(String projectId,
			String jobs, String epsContractId, String customWeight) {
		List<ProjectConsoleScurveTableDetailsDTO> list = new ArrayList<ProjectConsoleScurveTableDetailsDTO>();
		try {
			String[] departmentList = { "ENG", "PO Issue", "Procurement", "MFG", "OVERALL" };
			for (String department : departmentList) {
				ProjectConsoleScurveTableDetailsDTO dto = new ProjectConsoleScurveTableDetailsDTO();
				dto = getProjectConsoleWeightedSCurveTableDetails(projectId, jobs, epsContractId, customWeight,
						department);
				list.add(dto);
			}
			list.sort(Comparator.comparing(ProjectConsoleScurveTableDetailsDTO::getOrderNo));
		} catch (Exception exception) {
			log.error("Error in getting Project Console Weighted Scurve Table Details :: " + exception.getMessage());
		}
		return list;
	}

	public ProjectConsoleScurveTableDetailsDTO getProjectConsoleWeightedSCurveTableDetails(String projectId,
			String jobs, String epsContractId, String customWeight, String department) {
		ProjectConsoleScurveTableDetailsDTO dto = new ProjectConsoleScurveTableDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_WEIGHTED_SCURVE_TABLE_DETAILS)) {
			String[] subProjectStr = jobs.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			String[] departmentStr = department.split(";");
			Array departmentStrArr = con.createArrayOf("varchar", departmentStr);

			if (null != department && !department.isEmpty() && department.equalsIgnoreCase("OVERALL")) {
				department = "overall";
			}

			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setString(3, customWeight);
			pstm.setString(4, epsContractId);
			pstm.setArray(5, departmentStrArr);
			pstm.setString(6, department);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String departmentName = "";
				departmentName = null != rs.getString("department_name_out") ? rs.getString("department_name_out") : "";
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("ENG")) {
					dto.setDepartmentName("Engineering");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(1);
				}
				if (null != departmentName && !departmentName.isEmpty()
						&& departmentName.equalsIgnoreCase("PO Issue")) {
					dto.setDepartmentName("PO Placement");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(2);
				}
				if (null != departmentName && !departmentName.isEmpty()
						&& departmentName.equalsIgnoreCase("Procurement")) {
					dto.setDepartmentName("PO Delivery");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(3);
				}
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("MFG")) {
					dto.setDepartmentName("Manufacturing");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(4);
				}
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("OVERALL")) {
					dto.setDepartmentName("OVERALL");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(5);
				}
			}
		} catch (Exception exception) {
			log.error("Error in getting Project Console Weighted Scurve Table Details :: " + exception.getMessage());
		}
		return dto;
	}

	@Override
	public String getProjectConsoleScurveEPSContractDetails(String projectId) {
		String epsContract = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_CUSTOMIZED_DETAILS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				epsContract = rs.getString("eps_contract_id");
			}
		} catch (Exception e) {
			log.error(
					"Error in ProjectControlDAOImpl :: getProjectConsoleScurveEPSContractDetails :: " + e.getMessage());
		}
		return epsContract;
	}

	@Override
	public String getProjectConsoleScurveSubProjectDetails(String projectId) {
		String subProjectList = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(
						ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_CUSTOMIZED_SUB_PROJECT_DETAILS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String subProject = rs.getString("sub_project");
				if (subProject != null && !subProject.equalsIgnoreCase("") && !subProjectList.equalsIgnoreCase("")
						&& !subProjectList.equalsIgnoreCase(subProject)) {
					subProjectList = subProjectList + ";" + subProject;
				} else {
					subProjectList = subProject;
				}
			}
		} catch (Exception e) {
			log.error(
					"Error in ProjectControlDAOImpl :: getProjectConsoleScurveSubProjectDetails :: " + e.getMessage());
		}
		return subProjectList;
	}

	@Override
	public List<String> getProjectConsoleScurveToShowDetails(String projectId) {
		List<String> projectDisplayInfoList = new ArrayList<String>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_CUSTOMIZED_DETAILS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String scurveToShow = rs.getString("selected_scurve");
				String[] scurveToShowStr = scurveToShow.split(",");
				for (String curve : scurveToShowStr) {
					scurveToShow = curve;
					if (curve.equalsIgnoreCase("At Complete")) {
						scurveToShow = "Forecast";
					} else if (curve.equalsIgnoreCase("At Complete Late")) {
						scurveToShow = "Late Forecast";
					}
					projectDisplayInfoList.add(scurveToShow);
				}
				projectDisplayInfoList.add("SPI");
				projectDisplayInfoList.add("TFI");
			}
		} catch (Exception e) {
			log.error("Error in ProjectControlDAOImpl :: getProjectConsoleScurveToShowDetails :: " + e.getMessage());
		}
		return projectDisplayInfoList;
	}

	@Override
	public String getProjectConsoleScurveCustomWeightDetails(String projectId) {
		String customWeight = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_CUSTOMIZED_DETAILS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				customWeight = rs.getString("weight");
			}
		} catch (Exception e) {
			log.error("Error in ProjectControlDAOImpl :: getProjectConsoleScurveCustomWeightDetails :: "
					+ e.getMessage());
		}
		return customWeight;
	}

	@Override
	public ProjectConsoleWeightDataDTO getProjectConsoleScurveWeightDetails(String projectId) {
		ProjectConsoleWeightDataDTO weightData = new ProjectConsoleWeightDataDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_CUSTOMIZED_DETAILS)) {
			ps.setString(1, projectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				weightData
						.setWbsCustomWeight(null != rs.getString("wbs_custom_wt") ? rs.getString("wbs_custom_wt") : "");
				weightData.setWbsOverallWeight(
						null != rs.getString("wbs_overall_dept") ? rs.getString("wbs_overall_dept") : "");
				weightData.setWbsEngDeptAsTrain(
						null != rs.getString("eng_dept_at_train") ? rs.getString("eng_dept_at_train") : "");
			}
		} catch (Exception e) {
			log.error("Error in ProjectControlDAOImpl :: getProjectConsoleScurveWeightDetails :: " + e.getMessage());
		}
		return weightData;
	}

	@Override
	public List<ProjectConsoleScurveDataDTO> getProjectConsoleCustomizedSCurveDetails(String projectId, String jobs,
			String epsContractId, String customWeight, List<String> projectDisplayInfoList) {
		List<ProjectConsoleScurveDataDTO> list = new ArrayList<ProjectConsoleScurveDataDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_CUSTOM_DETAILS)) {
			String[] subProjectStr = jobs.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setString(3, customWeight);
			pstm.setString(4, epsContractId);

			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleScurveDataDTO dto = new ProjectConsoleScurveDataDTO();
				dto.setProjectDate(null != rs.getString("projectdate_out") ? rs.getString("projectdate_out") : "");
				if (projectDisplayInfoList.contains("Actual")) {
					dto.setActual(null != rs.getString("actual_out") ? rs.getFloat("actual_out") : null);
				}
				if (projectDisplayInfoList.contains("Forecast")) {
					dto.setForecast(null != rs.getString("forecast_out") ? rs.getFloat("forecast_out") : null);
				}
				if (projectDisplayInfoList.contains("Late Forecast")) {
					dto.setLateForecast(
							null != rs.getString("lateforecast_out") ? rs.getFloat("lateforecast_out") : null);
				}
				if (projectDisplayInfoList.contains("Baseline")) {
					dto.setBaseline(null != rs.getString("baseline_out") ? rs.getFloat("baseline_out") : null);
				}
				if (projectDisplayInfoList.contains("Baseline Late")) {
					dto.setBaselineLate(
							null != rs.getString("baselinelate_out") ? rs.getFloat("baselinelate_out") : null);
				}
				dto.setSpi(null != rs.getString("spi_out") ? rs.getString("spi_out") : "");
				dto.setTfi(null != rs.getString("tfi_out") ? rs.getString("tfi_out") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error(
					"Error in ProjectControlDAOImpl :: getProjectConsoleCustomizedSCurveDetails :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<ProjectConsoleScurveTableDetailsDTO> getProjectConsoleCustomizedSCurveTableDetails(String projectId,
			String jobs, String epsContractId, String customWeight) {
		List<ProjectConsoleScurveTableDetailsDTO> list = new ArrayList<ProjectConsoleScurveTableDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_SCURVE_TABLE_CUSTOM_DETAILS)) {
			String[] subProjectStr = jobs.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setString(3, customWeight);
			pstm.setString(4, epsContractId);

			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleScurveTableDetailsDTO dto = new ProjectConsoleScurveTableDetailsDTO();
				String departmentName = "";
				departmentName = null != rs.getString("department_name_out") ? rs.getString("department_name_out") : "";
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("ENG")) {
					dto.setDepartmentName("Engineering");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(1);
					list.add(dto);
				}
				if (null != departmentName && !departmentName.isEmpty()
						&& departmentName.equalsIgnoreCase("PO Issue")) {
					dto.setDepartmentName("PO Placement");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(2);
					list.add(dto);
				}
				if (null != departmentName && !departmentName.isEmpty()
						&& departmentName.equalsIgnoreCase("Procurement")) {
					dto.setDepartmentName("PO Delivery");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(3);
					list.add(dto);
				}
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("MFG")) {
					dto.setDepartmentName("Manufacturing");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(4);
					list.add(dto);
				}
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("OVERALL")) {
					dto.setDepartmentName("OVERALL");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(null != rs.getString("baseline_out") ? rs.getString("baseline_out") : null);
					dto.setOrderNo(5);
					list.add(dto);
				}
			}
			list.sort(Comparator.comparing(ProjectConsoleScurveTableDetailsDTO::getOrderNo));
		} catch (Exception e) {
			log.error("Error in ProjectControlDAOImpl :: getProjectConsoleCustomizedSCurveTableDetails :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<ProjectConsoleScurveDataDTO> getProjectConsoleCustomizedWeightedSCurveDetails(String projectId,
			String jobs, String epsContractId, String customWeight, List<String> projectDisplayInfoList) {
		List<ProjectConsoleScurveDataDTO> list = new ArrayList<ProjectConsoleScurveDataDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_WEIGHTED_SCURVE_CUSTOM_DETAILS)) {
			String[] subProjectStr = jobs.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setString(3, customWeight);
			pstm.setString(4, epsContractId);

			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleScurveDataDTO dto = new ProjectConsoleScurveDataDTO();
				dto.setProjectDate(null != rs.getString("projectdate_out") ? rs.getString("projectdate_out") : "");
				if (projectDisplayInfoList.contains("Actual")) {
					dto.setActual(null != rs.getString("actual_out") ? rs.getFloat("actual_out") : null);
				}
				if (projectDisplayInfoList.contains("Forecast")) {
					dto.setForecast(null != rs.getString("forecast_out") ? rs.getFloat("forecast_out") : null);
				}
				if (projectDisplayInfoList.contains("Late Forecast")) {
					dto.setLateForecast(
							null != rs.getString("late_forecast_out") ? rs.getFloat("late_forecast_out") : null);
				}
				if (projectDisplayInfoList.contains("Baseline")) {
					dto.setBaseline(
							null != rs.getString("baseline_data_out") ? rs.getFloat("baseline_data_out") : null);
				}
				if (projectDisplayInfoList.contains("Baseline Late")) {
					dto.setBaselineLate(
							null != rs.getString("baseline_late_out") ? rs.getFloat("baseline_late_out") : null);
				}
				dto.setSpi(null != rs.getString("spi_out") ? rs.getString("spi_out") : "");
				dto.setTfi(null != rs.getString("tfi_out") ? rs.getString("tfi_out") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Error in ProjectControlDAOImpl :: getProjectConsoleCustomizedWeightedSCurveDetails :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<ProjectConsoleScurveTableDetailsDTO> getProjectConsoleCustomizedWeightedSCurveTableDetails(
			String projectId, String jobs, String epsContractId, String customWeight) {
		List<ProjectConsoleScurveTableDetailsDTO> list = new ArrayList<ProjectConsoleScurveTableDetailsDTO>();
		try {
			String[] departmentList = { "ENG", "PO Issue", "Procurement", "MFG", "OVERALL" };
			for (String department : departmentList) {
				ProjectConsoleScurveTableDetailsDTO dto = new ProjectConsoleScurveTableDetailsDTO();
				dto = getProjectConsoleCustomizedWeightedSCurveTableDetails(projectId, jobs, epsContractId,
						customWeight, department);
				list.add(dto);
			}
			list.sort(Comparator.comparing(ProjectConsoleScurveTableDetailsDTO::getOrderNo));
		} catch (Exception e) {
			log.error("Error in ProjectControlDAOImpl :: getProjectConsoleCustomizedWeightedSCurveTableDetails :: "
					+ e.getMessage());
		}
		return list;
	}

	public ProjectConsoleScurveTableDetailsDTO getProjectConsoleCustomizedWeightedSCurveTableDetails(String projectId,
			String jobs, String epsContractId, String customWeight, String department) {
		ProjectConsoleScurveTableDetailsDTO dto = new ProjectConsoleScurveTableDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						ProjectControlConstants.GET_PROJECT_CONSOLE_WEIGHTED_SCURVE_TABLE_CUSTOM_DETAILS)) {
			String[] subProjectStr = jobs.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			String[] departmentStr = department.split(";");
			Array departmentStrArr = con.createArrayOf("varchar", departmentStr);

			if (null != department && !department.isEmpty() && department.equalsIgnoreCase("OVERALL")) {
				department = "overall";
			}

			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setString(3, customWeight);
			pstm.setString(4, epsContractId);
			pstm.setArray(5, departmentStrArr);
			pstm.setString(6, department);

			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String departmentName = "";
				departmentName = null != rs.getString("department_name_out") ? rs.getString("department_name_out") : "";
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("ENG")) {
					dto.setDepartmentName("Engineering");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(1);
				}
				if (null != departmentName && !departmentName.isEmpty()
						&& departmentName.equalsIgnoreCase("PO Issue")) {
					dto.setDepartmentName("PO Placement");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(2);
				}
				if (null != departmentName && !departmentName.isEmpty()
						&& departmentName.equalsIgnoreCase("Procurement")) {
					dto.setDepartmentName("PO Delivery");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(3);
				}
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("MFG")) {
					dto.setDepartmentName("Manufacturing");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(4);
				}
				if (null != departmentName && !departmentName.isEmpty() && departmentName.equalsIgnoreCase("OVERALL")) {
					dto.setDepartmentName("OVERALL");
					dto.setActualPer(null != rs.getString("actual_out") ? rs.getString("actual_out") : null);
					dto.setBaselinePer(
							null != rs.getString("baseline_data_out") ? rs.getString("baseline_data_out") : null);
					dto.setOrderNo(5);
				}
			}
		} catch (Exception e) {
			log.error("Error in ProjectControlDAOImpl :: getProjectConsoleCustomizedWeightedSCurveTableDetails :: "
					+ e.getMessage());
		}
		return dto;
	}

	@Override
	public ProjectConsoleDeckStatusDetailsDTO getProjectConsoleDeckStatusDetails(String projectId) {
		ProjectConsoleDeckStatusDetailsDTO dto = new ProjectConsoleDeckStatusDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_DECK_STATUS_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setQuality(null != rs.getString("o_quality_color") ? rs.getString("o_quality_color") : "");
				dto.setSchedule(null != rs.getString("o_schedule_color") ? rs.getString("o_schedule_color") : "");
				dto.setCost(null != rs.getString("o_cost_color") ? rs.getString("o_cost_color") : "");

			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Deck Status Details :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public List<ProjectConsoleDeckContractualDeliveryDTO> getProjectConsoleDeckContractualDeliveryDates(
			String projectId) {
		List<ProjectConsoleDeckContractualDeliveryDTO> list = new ArrayList<ProjectConsoleDeckContractualDeliveryDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_DECK_CONTRACTUAL_DATES);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectConsoleDeckContractualDeliveryDTO dto = new ProjectConsoleDeckContractualDeliveryDTO();
				dto.setDelivery(null != rs.getString("del_date_label") ? rs.getString("del_date_label") : "");
				dto.setDeliveryDate(null != rs.getString("delivery_date") ? rs.getString("delivery_date") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting Project Console Deck Contractual Delivery Dates :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public String getProjectConsoleCheckBusinessUnit(String projectId) {
		String business = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_BUSINESS_UNIT);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				business = rs.getString("business_unit");
			}
		} catch (SQLException e) {
			log.error("Exception while getting business unit for project :: " + e.getMessage());
		}
		return business;

	}

	@Override
	public String getProjectConsoleQIRadarDetails(String projectId) {
		log.debug("INIT- getProjectConsoleQIRadarDetails for projectId : {}", projectId);
		String count = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_QIRADAR_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				count = rs.getString("total_count");
			}

		} catch (SQLException e) {
			log.error("Exception while getting getProjectConsoleQIRadarDetails project :: " + e.getMessage());
		}
		log.debug("END- getProjectConsoleQIRadarDetails for projectId : {}", projectId);
		return count;
	}

	@Override
	public String getProjectConsoleLessonLearnedCnt(String projectId) {
		String lessonLearnedCnt = "0";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_LESSON_LEARNED_CNT);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				lessonLearnedCnt = rs.getString("lesson_learned");
			}
		} catch (Exception e) {
			log.error("Exception in ProjectControlDAOImpl :: getProjectConsoleLessonLearnedCnt :: " + e.getMessage());
		}
		return lessonLearnedCnt;
	}

	@Override
	public String getProjectConsoleLessonLearnedRiskCnt(String projectId) {
		String lessonLearnedRiskCnt = "0";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProjectControlConstants.GET_PROJECT_CONSOLE_LESSON_LEARNED_RISK_CNT);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				lessonLearnedRiskCnt = rs.getString("lesson_learned_risk");
			}
		} catch (SQLException e) {
			log.error(
					"Exception in ProjectControlDAOImpl :: getProjectConsoleLessonLearnedRiskCnt :: " + e.getMessage());
		}
		return lessonLearnedRiskCnt;
	}

}
