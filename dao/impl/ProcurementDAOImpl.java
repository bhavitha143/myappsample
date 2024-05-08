package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IProcurementDAO;
import com.bh.realtrack.dto.ItemBuyControlDTO;
import com.bh.realtrack.dto.MaterialListDetailsDTO;
import com.bh.realtrack.dto.ProjectTeamDetailsDTO;
import com.bh.realtrack.util.ProcurementConstants;

@Repository
public class ProcurementDAOImpl implements IProcurementDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final Logger log = LoggerFactory.getLogger(ProcurementDAOImpl.class);

	@Override
	public List<ItemBuyControlDTO> getItemBuyControlFlowDetails(String jobNumber, String dummyCode,
			String activityFilter) {
		Map<String, ItemBuyControlDTO> compareMap = new LinkedHashMap<String, ItemBuyControlDTO>();
		List<ItemBuyControlDTO> list = new ArrayList<ItemBuyControlDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProcurementConstants.GET_ITEM_BUY_CONTROL_FLOW_DETAILS);) {
			pstm.setString(1, activityFilter);
			pstm.setString(2, jobNumber);
			pstm.setString(3, dummyCode);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ItemBuyControlDTO responseDTO = new ItemBuyControlDTO();
				responseDTO.setKey(null != rs.getString("key1_out") ? rs.getString("key1_out") : "");
				responseDTO.setItemDescription(
						null != rs.getString("item_description_out") ? rs.getString("item_description_out") : "");
				responseDTO.setRealCode(null != rs.getString("real_code_out") ? rs.getString("real_code_out") : "");
				responseDTO.setPorNumber(null != rs.getString("por_number_out") ? rs.getString("por_number_out") : "");
				responseDTO.setPoNumber(null != rs.getString("po_number_out") ? rs.getString("po_number_out") : "");
				responseDTO.setStatus(null != rs.getString("status_out") ? rs.getString("status_out") : "");
				responseDTO.setColor(null != rs.getString("color_out") ? rs.getString("color_out") : "");
				responseDTO.setForecastDate(
						null != rs.getString("forecast_date_out") ? rs.getString("forecast_date_out") : "");
				responseDTO.setActualFinishDate(
						null != rs.getString("actual_finish_date_out") ? rs.getString("actual_finish_date_out") : "");
				responseDTO.setBaselineEarlyFinish(
						null != rs.getString("bl_early_date") ? rs.getString("bl_early_date") : "");
				responseDTO.setBaselinelateFinish(
						null != rs.getString("bl_late_date") ? rs.getString("bl_late_date") : "");
				responseDTO.setDeName(null != rs.getString("de_out") ? rs.getString("de_out") : "");
				responseDTO.setBuyerName(null != rs.getString("buyer_name_out") ? rs.getString("buyer_name_out") : "");
				responseDTO.setSfmName(null != rs.getString("sfm_name_out") ? rs.getString("sfm_name_out") : "");
				responseDTO.setNote("");
				responseDTO.setWorkflowId(null != rs.getString("o_request_id") ? rs.getString("o_request_id") : "");
				responseDTO.setTaWorkflowLink(null != rs.getString("o_wf_link") ? rs.getString("o_wf_link") : "");
				responseDTO.setCreatedDate(
						null != rs.getString("created_date_out") ? rs.getString("created_date_out") : "");
				compareMap.put(responseDTO.getKey(), responseDTO);
			}
			list.addAll(compareMap.values());
			if (!compareMap.containsKey("Real_Code_BoM_Release")) {
				ItemBuyControlDTO bomRealCodeDTO = new ItemBuyControlDTO();
				ItemBuyControlDTO poIssueDTO = new ItemBuyControlDTO();
				poIssueDTO = compareMap.get("PO_Issue");
				bomRealCodeDTO.setKey("Real_Code_BoM_Release");
				bomRealCodeDTO.setColor(
						poIssueDTO.getColor().equalsIgnoreCase("#018374") ? poIssueDTO.getColor() : "#00A5B8");
				bomRealCodeDTO.setItemDescription(poIssueDTO.getItemDescription());
				bomRealCodeDTO.setNote("ENG Activity missing in Primavera");
				list.add(1, bomRealCodeDTO);
			}
		} catch (SQLException e) {
			log.error("Exception in ProcurementDAOImpl :: getItemBuyControlFlowDetails :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<ProjectTeamDetailsDTO> getItemBuyControlFlowChatDetails(String jobNumber, String dummyCode,
			String activityFilter) {
		List<ProjectTeamDetailsDTO> list = new ArrayList<ProjectTeamDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ProcurementConstants.GET_ITEM_BUY_CONTROL_FLOW_CHAT_DETAILS);) {
			pstm.setString(1, activityFilter);
			pstm.setString(2, jobNumber);
			pstm.setString(3, dummyCode);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ProjectTeamDetailsDTO dto = new ProjectTeamDetailsDTO();
				dto.setRole(null != rs.getString("key1_out") ? rs.getString("key1_out") : "");
				dto.setName((null != rs.getString("username_out") ? rs.getString("username_out") : ""));
				;
				dto.setEmail(null != rs.getString("sso_out") ? rs.getString("sso_out") : "");
				list.add(dto);
			}

		} catch (SQLException e) {
			log.error("Exception in ProcurementDAOImpl :: getItemBuyControlFlowChatDetails :: " + e.getMessage());
		}

		return list;

	}

	@Override
	public List<MaterialListDetailsDTO> getMaterialListDetails(String projectId, String viewConsideration, String train,
			String subProject, String componentCode, String activityFilter) {
		List<MaterialListDetailsDTO> list = new ArrayList<MaterialListDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(ProcurementConstants.GET_MATERIAL_LIST_DETAILS);) {
			pstm.setString(1, projectId);
			pstm.setString(2, train);
			pstm.setString(3, subProject);
			pstm.setString(4, componentCode);
			pstm.setString(5, activityFilter);
			pstm.setString(6, viewConsideration);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MaterialListDetailsDTO responseDTO = new MaterialListDetailsDTO();
				responseDTO.setProjectNumber(
						null != rs.getString("projectnumber_out") ? rs.getString("projectnumber_out") : "");
				responseDTO.setTrain(null != rs.getString("train_out") ? rs.getString("train_out") : "");
				responseDTO.setSubProject(null != rs.getString("subproject_out") ? rs.getString("subproject_out") : "");
				responseDTO.setComponentCode(
						null != rs.getString("componentcode_out") ? rs.getString("componentcode_out") : "");
				responseDTO.setItemCode(null != rs.getString("itemcode_out") ? rs.getString("itemcode_out") : "");
				responseDTO.setItemDescription(
						null != rs.getString("itemdescription_out") ? rs.getString("itemdescription_out") : "");
				responseDTO.setPlannedPoIssueDate(
						null != rs.getString("plannedpoissuedate_out") ? rs.getString("plannedpoissuedate_out") : "");
				responseDTO.setActualPoIssueDate(
						null != rs.getString("actualpoissuedate_out") ? rs.getString("actualpoissuedate_out") : "");
				responseDTO.setNeedPoDeliveryDate(
						null != rs.getString("needpodeliverydate_out") ? rs.getString("needpodeliverydate_out") : "");
				responseDTO.setActualPoDeliveryDate(
						null != rs.getString("actualpodeliverydate_out") ? rs.getString("actualpodeliverydate_out")
								: "");
				responseDTO.setExpectedPoDeliveryDate(
						null != rs.getString("expectedpodeliverydate_out") ? rs.getString("expectedpodeliverydate_out")
								: "");
				responseDTO.setContractualPoDeliveryDate(null != rs.getString("contractualpodeliverydate_out")
						? rs.getString("contractualpodeliverydate_out")
						: "");
				responseDTO.setSupplierName(
						null != rs.getString("suppliername_out") ? rs.getString("suppliername_out") : "");
				responseDTO.setPoNumber(null != rs.getString("ponumber_out") ? rs.getString("ponumber_out") : "");
				responseDTO.setComponentDesc(
						null != rs.getString("componentdesc_out") ? rs.getString("componentdesc_out") : "");
				responseDTO.setItemStatus(null != rs.getString("itemstatus_out") ? rs.getString("itemstatus_out") : "");
				responseDTO.setViewConsideration(
						null != rs.getString("viewconsideration_out") ? rs.getString("viewconsideration_out") : "");
				responseDTO.setActivityFilter(
						null != rs.getString("activityfilter_out") ? rs.getString("activityfilter_out") : "");
				responseDTO.setRealCode(null != rs.getString("realcode_out") ? rs.getString("realcode_out") : "");
				responseDTO.setDummyCode(null != rs.getString("dummycode_out") ? rs.getString("dummycode_out") : "");
				responseDTO.setActivityId(null != rs.getString("activityid_out") ? rs.getString("activityid_out") : "");
				responseDTO.setBlLatePoIssueDt(
						null != rs.getString("bllatepoissuedt_out") ? rs.getString("bllatepoissuedt_out") : "");
				responseDTO
						.setStatusColor(null != rs.getString("statuscolor_out") ? rs.getString("statuscolor_out") : "");
				responseDTO.setStatusColorLabel(
						null != rs.getString("statuscolorlabel_out") ? rs.getString("statuscolorlabel_out") : "");
				responseDTO.setMainItem(
						null != rs.getString("main_item_flag_out") ? rs.getString("main_item_flag_out") : "");
				responseDTO.setInternalItem(
						null != rs.getString("internal_item_flag_out") ? rs.getString("internal_item_flag_out") : "");
				responseDTO.setDeName(null != rs.getString("de_name_out") ? rs.getString("de_name_out") : "");
				responseDTO.setBuyerName(null != rs.getString("buyer_name_out") ? rs.getString("buyer_name_out") : "");
				responseDTO.setSfmName(null != rs.getString("sfm_name_out") ? rs.getString("sfm_name_out") : "");
				responseDTO.setWorkflowId(null != rs.getString("request_id_out") ? rs.getString("request_id_out") : "");
				responseDTO.setTaWorkflowLink(null != rs.getString("wf_link_out") ? rs.getString("wf_link_out") : "");
				list.add(responseDTO);
			}

		} catch (Exception e) {
			log.error("Exception in ProcurementDAOImpl :: getMaterialListDetails :: " + e.getMessage());
		}
		return list;
	}

}
