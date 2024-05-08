package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IShippingPreservationDAO;
import com.bh.realtrack.dto.KeyValueDTO;
import com.bh.realtrack.dto.LCSOSCellDTO;
import com.bh.realtrack.dto.LCVolumeAnalysisDetailsDTO;
import com.bh.realtrack.dto.LCVolumeAnalysisPiechart;
import com.bh.realtrack.dto.LcAgainstVolumeActualCostDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeBoxPackingDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeDetailsTransportationDTO;
import com.bh.realtrack.dto.LcVolumeAnalysisDTO;
import com.bh.realtrack.dto.LcVolumeBarchartDTO;
import com.bh.realtrack.dto.LcVolumeGaugeChartDTO;
import com.bh.realtrack.dto.ShippingReportDTO;
import com.bh.realtrack.dto.ShowExpiryMessageDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.ShippingPreservationConstants;

/**
 * @author Shweta D. Sawant
 */
@Repository
public class ShippingPreservationDAOImpl implements IShippingPreservationDAO {

	private static final Logger log = LoggerFactory.getLogger(ShippingPreservationDAOImpl.class.getName());

	@Autowired
	JdbcTemplate jdbcTemplate;

	// Shipping/Preservation Widget

	@Override
	public ShowExpiryMessageDTO showExpiryMessageFlag(String projectId) {
		Connection con = null;
		ShowExpiryMessageDTO dto = new ShowExpiryMessageDTO();
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(ShippingPreservationConstants.SHOW_EXPIRY_MESSAGE_FLAG);
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String attributeGrp = "", attributeVal = "";
				attributeGrp = rs.getString("attribute_group");
				attributeVal = rs.getString("attribute_value");
				if (null != attributeGrp && !attributeGrp.isEmpty()
						&& attributeGrp.equalsIgnoreCase("SHIPPING_PRESEVATION_MSG_APP")) {
					dto.setShowExpiryMsg(attributeVal);
				}
				if (null != attributeGrp && !attributeGrp.isEmpty()
						&& attributeGrp.equalsIgnoreCase("SHIPPING_PRESEVATION_MSG")) {
					dto.setExpiryMsg(attributeVal);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting About to Expiry Message Flag :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting About to Expiry Message Flag :: " + e.getMessage());
				}
			}
		}
		return dto;
	}

	@Override
	public ShowExpiryMessageDTO showExpiryMessageExpiredFlag(String projectId) {
		Connection con = null;
		ShowExpiryMessageDTO dto = new ShowExpiryMessageDTO();
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(ShippingPreservationConstants.SHOW_EXPIRY_MESSAGE_EXPIRED_FLAG);
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String attributeGrp = "", attributeVal = "";
				attributeGrp = rs.getString("attribute_group");
				attributeVal = rs.getString("attribute_value");
				if (null != attributeGrp && !attributeGrp.isEmpty()
						&& attributeGrp.equalsIgnoreCase("SHIPPING_PRESEVATION_EXPIRED_MSG_APP")) {
					dto.setShowExpiryMsg(attributeVal);
				}
				if (null != attributeGrp && !attributeGrp.isEmpty()
						&& attributeGrp.equalsIgnoreCase("SHIPPING_PRESEVATION_EXPIRED_MSG")) {
					dto.setExpiryMsg(attributeVal);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting Expired Message Flag :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Expired Message Flag :: " + e.getMessage());
				}
			}
		}
		return dto;

	}

	@Override
	public List<ShippingReportDTO> getShippingReportDetails(String projectId) {
		log.debug("INIT- getShippingReportDetails for projectId : {}", projectId);
		List<ShippingReportDTO> list = new ArrayList<ShippingReportDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_SHIPPING_REPORT_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ShippingReportDTO dto = new ShippingReportDTO();
				dto.setSalesOrderType(null != rs.getString("order_type") ? rs.getString("order_type") : "");
				dto.setSalesOrder(null != rs.getString("order_number") ? rs.getString("order_number") : "");
				dto.setProject(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setCustomerName(null != rs.getString("contract_customer") ? rs.getString("contract_customer") : "");
				dto.setJobNumber(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				dto.setPm(null != rs.getString("pm_name") ? rs.getString("pm_name") : "");
				dto.setBox(null != rs.getString("box") ? rs.getString("box") : "");
				dto.setBoxStatus(null != rs.getString("box_status") ? rs.getString("box_status") : "");
				dto.setBoxClosureDate(null != rs.getString("box_closure_dt") ? rs.getString("box_closure_dt") : "");
				dto.setBoxPlace(null != rs.getString("box_place") ? rs.getString("box_place") : "");
				dto.setBoxLength(null != rs.getString("box_length") ? rs.getString("box_length") : "");
				dto.setBoxWidth(null != rs.getString("box_width") ? rs.getString("box_width") : "");
				dto.setBoxHeight(null != rs.getString("box_height") ? rs.getString("box_height") : "");
				dto.setGrossWeight(null != rs.getString("gross_weight") ? rs.getString("gross_weight") : "");
				dto.setNetWeight(null != rs.getString("net_weight") ? rs.getString("net_weight") : "");
				dto.setPackType(null != rs.getString("pack_type") ? rs.getString("pack_type") : "");
				dto.setPackingIdentification(
						null != rs.getString("pack_identification") ? rs.getString("pack_identification") : "");
				dto.setStoragedescription(null != rs.getString("storage_desc") ? rs.getString("storage_desc") : "");
				dto.setPackingExpirationDate(
						null != rs.getString("pack_expiration_dt") ? rs.getString("pack_expiration_dt") : "");
				dto.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				dto.setItemDescription(
						null != rs.getString("item_description") ? rs.getString("item_description") : "");
				dto.setExwDate(null != rs.getString("exw_date") ? rs.getString("exw_date") : "");
				dto.setPercievedExwDate(null != rs.getString("prec_exw_date") ? rs.getString("prec_exw_date") : "");
				dto.setPeiDescription(null != rs.getString("pei_description") ? rs.getString("pei_description") : "");
				dto.setBarrierBag(null != rs.getString("barrier_bag") ? rs.getString("barrier_bag") : "");
				dto.setFirstBoxClosureDate(
						null != rs.getString("first_box_closure_date") ? rs.getString("first_box_closure_date") : "");
				dto.setBoxValue(null != rs.getString("box_value") ? rs.getString("box_value") : "");
				dto.setAdditionalCoo(null != rs.getString("additional_coo") ? rs.getString("additional_coo") : "");
				dto.setArgolAttribute(null != rs.getString("argol_attribute") ? rs.getString("argol_attribute") : "");
				dto.setBeaconNumber(null != rs.getString("beacon_number") ? rs.getString("beacon_number") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting Shipping Report excel details :: " + e.getMessage());
		}
		log.debug("END- getShippingReportDetails for projectId : {}", projectId);
		return list;
	}

	// Logistic Cost Widget

	@Override
	public List<KeyValueDTO> getLCSubProjectDropdown(String projectId) {
		List<KeyValueDTO> list = new ArrayList<KeyValueDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_SUB_PROJECT_FILTER);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				KeyValueDTO dto = new KeyValueDTO();
				String subProject = rs.getString(1);
				if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
					dto.setKey(subProject);
					dto.setVal(subProject);
					list.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc subProject filter :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<KeyValueDTO> getLCShowByDropdown(String projectId) {
		List<KeyValueDTO> showByList = new ArrayList<KeyValueDTO>();

		KeyValueDTO showBy = new KeyValueDTO();
		showBy.setKey("Box & Packing");
		showBy.setVal("Box & Packing");
		showByList.add(showBy);

		KeyValueDTO showBy1 = new KeyValueDTO();
		showBy1.setKey("Transportation");
		showBy1.setVal("Transportation");
		showByList.add(showBy1);

		return showByList;
	}

	@Override
	public Map<String, Object> getLCAgainstVolumeSummaryQuarterlyBarChart(String projectId, String subProject,
			String showBy, String viewType) {
		Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
		Map<String, LcVolumeBarchartDTO> barChartMap = new TreeMap<String, LcVolumeBarchartDTO>();
		LcVolumeBarchartDTO dto = new LcVolumeBarchartDTO();
		String lastBudget = "", subProjectStr = "", showByVal = "";
		if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
			if (showBy.equalsIgnoreCase("Box & Packing")) {
				showByVal = "BP_COMPETENZE";
			} else if (showBy.equalsIgnoreCase("Transportation")) {
				showByVal = "TRANSPORTATION";
			}
		}
		lastBudget = getLCAgainstVolumeLastBudget(projectId, subProject, showBy, viewType);
		getLCAgainstVolumeMinRFS(barChartMap, projectId, subProject, viewType);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_QUARTERLY_GRAPH);) {
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (subProject.equalsIgnoreCase("OVERALL")) {
					subProjectStr = "0";
				} else {
					subProjectStr = subProject.replace(";", ",");
				}
			}
			log.info("subProject :: " + subProjectStr);
			pstm.setString(1, projectId);
			pstm.setString(2, subProjectStr);
			pstm.setString(3, showByVal);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String yearQuarter = "";
				yearQuarter = rs.getString("period");
				if (null != yearQuarter && !yearQuarter.isEmpty() && !yearQuarter.equalsIgnoreCase("")) {
					dto = barChartMap.get(yearQuarter);
					if (null == dto) {
						dto = new LcVolumeBarchartDTO();
						dto.setVolumePacked(null != rs.getString("volume_packed") ? rs.getString("volume_packed") : "");
						dto.setVolumeShipped(
								null != rs.getString("volume_shipped") ? rs.getString("volume_shipped") : "");
						dto.setVolumePackedPer(
								null != rs.getString("volume_packed_per_out") ? rs.getString("volume_packed_per_out")
										: "");
						dto.setVolumeShippedPer(
								null != rs.getString("volume_shipped_per_out") ? rs.getString("volume_shipped_per_out")
										: "");
						dto.setActualCost(
								null != rs.getString("actual_cost_out") ? rs.getString("actual_cost_out") : "");
						dto.setLastBDG(lastBudget);
						barChartMap.put(yearQuarter, dto);
					} else {
						dto.setVolumePacked(null != rs.getString("volume_packed") ? rs.getString("volume_packed") : "");
						dto.setVolumeShipped(
								null != rs.getString("volume_shipped") ? rs.getString("volume_shipped") : "");
						dto.setVolumePackedPer(
								null != rs.getString("volume_packed_per_out") ? rs.getString("volume_packed_per_out")
										: "");
						dto.setVolumeShippedPer(
								null != rs.getString("volume_shipped_per_out") ? rs.getString("volume_shipped_per_out")
										: "");
						dto.setActualCost(
								null != rs.getString("actual_cost_out") ? rs.getString("actual_cost_out") : "");
						dto.setLastBDG(lastBudget);
					}
				}
			}
			responseMap.put("lcVolumeBarchart", barChartMap);
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume quarterly graph summary :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLCAgainstVolumeSummaryMonthlyBarChart(String projectId, String subProject,
			String showBy, String viewType) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, LcVolumeBarchartDTO> barChartMap = new TreeMap<String, LcVolumeBarchartDTO>();
		Map<String, LcVolumeBarchartDTO> sortedBarChartMap = new TreeMap<String, LcVolumeBarchartDTO>();
		LcVolumeBarchartDTO dto = new LcVolumeBarchartDTO();
		String lastBudget = "", subProjectStr = "", showByVal = "";
		if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
			if (showBy.equalsIgnoreCase("Box & Packing")) {
				showByVal = "BP_COMPETENZE";
			} else if (showBy.equalsIgnoreCase("Transportation")) {
				showByVal = "TRANSPORTATION";
			}
		}
		lastBudget = getLCAgainstVolumeLastBudget(projectId, subProject, showBy, viewType);
		getLCAgainstVolumeMinRFS(barChartMap, projectId, subProject, viewType);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_MONTHLY_GRAPH);) {
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (subProject.equalsIgnoreCase("OVERALL")) {
					subProjectStr = "0";
				} else {
					subProjectStr = subProject.replace(";", ",");
				}
			}
			log.info("subProject :: " + subProjectStr);
			pstm.setString(1, projectId);
			pstm.setString(2, subProjectStr);
			pstm.setString(3, showByVal);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new LcVolumeBarchartDTO();
				String monthlyQuarter = "";
				monthlyQuarter = rs.getString("period");
				if (null != monthlyQuarter && !monthlyQuarter.isEmpty() && !monthlyQuarter.equalsIgnoreCase("")) {
					dto = barChartMap.get(monthlyQuarter);
					if (null == dto) {
						dto = new LcVolumeBarchartDTO();
						dto.setVolumePacked(null != rs.getString("volume_packed") ? rs.getString("volume_packed") : "");
						dto.setVolumeShipped(
								null != rs.getString("volume_shipped") ? rs.getString("volume_shipped") : "");
						dto.setVolumePackedPer(
								null != rs.getString("volume_packed_per_out") ? rs.getString("volume_packed_per_out")
										: "");
						dto.setVolumeShippedPer(
								null != rs.getString("volume_shipped_per_out") ? rs.getString("volume_shipped_per_out")
										: "");
						dto.setActualCost(
								null != rs.getString("actual_cost_out") ? rs.getString("actual_cost_out") : "");
						dto.setLastBDG(lastBudget);
						barChartMap.put(monthlyQuarter, dto);
					} else {
						dto.setVolumePacked(null != rs.getString("volume_packed") ? rs.getString("volume_packed") : "");
						dto.setVolumeShipped(
								null != rs.getString("volume_shipped") ? rs.getString("volume_shipped") : "");
						dto.setVolumePackedPer(
								null != rs.getString("volume_packed_per_out") ? rs.getString("volume_packed_per_out")
										: "");
						dto.setVolumeShippedPer(
								null != rs.getString("volume_shipped_per_out") ? rs.getString("volume_shipped_per_out")
										: "");
						dto.setActualCost(
								null != rs.getString("actual_cost_out") ? rs.getString("actual_cost_out") : "");
						dto.setLastBDG(lastBudget);
					}
				}
			}
			sortedBarChartMap = getLCAgainstVolumeMonthlySortedGraph(barChartMap);
			responseMap.put("lcVolumeBarchart", sortedBarChartMap);
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume monthly graph summary :: " + e.getMessage());
		}
		return responseMap;
	}

	private Map<String, LcVolumeBarchartDTO> getLCAgainstVolumeMonthlySortedGraph(
			Map<String, LcVolumeBarchartDTO> barChartMap) {
		Map<String, LcVolumeBarchartDTO> sortedbarChartMap = new LinkedHashMap<String, LcVolumeBarchartDTO>();
		LcVolumeBarchartDTO dto = new LcVolumeBarchartDTO();
		List<String> keyList = new ArrayList<String>(barChartMap.keySet());
		Collections.sort(keyList, new Comparator<String>() {
			@Override
			public int compare(String p1, String p2) {
				try {
					return getDateParsed(p1).compareTo(getDateParsed(p2));
				} catch (ParseException e) {
					log.error("Exception while getting lc against volume monthly graph summary :: " + e.getMessage());
				}
				return 0;
			}
		});
		for (String key : keyList) {
			dto = new LcVolumeBarchartDTO();
			dto = barChartMap.get(key);
			if (null != dto) {
				sortedbarChartMap.put(key, dto);
			}
		}
		return sortedbarChartMap;
	}

	private String getLCAgainstVolumeLastBudget(String projectId, String subProject, String showBy, String viewType) {
		String lastBudget = "", showByVal = "";
		if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
			if (showBy.equalsIgnoreCase("Box & Packing")) {
				showByVal = "y";
			} else if (showBy.equalsIgnoreCase("Transportation")) {
				showByVal = "n";
			}
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_LAST_BUDGET);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, showByVal);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				lastBudget = null != rs.getString("last_budget") ? rs.getString("last_budget") : "";
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume last budget :: " + e.getMessage());
		}
		return lastBudget;
	}

	public String getLCAgainstVolumeBudgetAsSold(String projectId, String subProject, String showBy, String viewType) {
		String budgetAsSold = "", showByVal = "";
		if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
			if (showBy.equalsIgnoreCase("Box & Packing")) {
				showByVal = "y";
			} else if (showBy.equalsIgnoreCase("Transportation")) {
				showByVal = "n";
			}
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_BUDGET_AS_SOLD);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, showByVal);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				budgetAsSold = null != rs.getString("budget_as_sold") ? rs.getString("budget_as_sold") : "";
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume budget as sold:: " + e.getMessage());
		}
		return budgetAsSold;
	}

	public String getLCAgainstVolumeVolumeOTR(String projectId, String subProject) {
		String volumeOTR = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_VOLUME_OTR);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				volumeOTR = null != rs.getString("volume_otr") ? rs.getString("volume_otr") : "";
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume - volume_otr:: " + e.getMessage());
		}
		return volumeOTR;
	}

	private void getLCAgainstVolumeMinRFS(Map<String, LcVolumeBarchartDTO> barChartMap, String projectId,
			String subProject, String viewType) {
		String minRFS = "", minRFSMonth = "", minRFSQuarter = "";
		LcVolumeBarchartDTO dto = new LcVolumeBarchartDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_MIN_RFS);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				minRFS = null != rs.getString("min_rfs") ? rs.getString("min_rfs") : "";
				minRFSMonth = null != rs.getString("min_rfs_month") ? rs.getString("min_rfs_month") : "";
				minRFSQuarter = null != rs.getString("min_rfs_quarter") ? rs.getString("min_rfs_quarter") : "";
			}
			if (null != minRFS && !minRFS.isEmpty() && !minRFS.equalsIgnoreCase("")) {
				if (null != viewType && !viewType.isEmpty() && !viewType.equalsIgnoreCase("")) {
					if (viewType.equalsIgnoreCase("Y")) {
						dto = barChartMap.get(minRFSMonth);
						if (null == dto) {
							dto = new LcVolumeBarchartDTO();
							dto.setMinRFS(minRFS);
							barChartMap.put(minRFSMonth, dto);
						} else {
							dto.setMinRFS(minRFS);
						}
					} else {
						dto = barChartMap.get(minRFSQuarter);
						if (null == dto) {
							dto = new LcVolumeBarchartDTO();
							dto.setMinRFS(minRFS);
							barChartMap.put(minRFSQuarter, dto);
						} else {
							dto.setMinRFS(minRFS);
						}
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume min rfs:: " + e.getMessage());
		}
	}

	@Override
	public LcVolumeGaugeChartDTO getLCAgainstVolumeSummaryGaugeChart(String projectId, String subProject, String showBy,
			String viewType) {
		LcVolumeGaugeChartDTO dto = new LcVolumeGaugeChartDTO();
		String lastBudget = "", volumeOTR = "", showByVal = "";
		if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
			if (showBy.equalsIgnoreCase("Box & Packing")) {
				showByVal = "y";
			} else if (showBy.equalsIgnoreCase("Transportation")) {
				showByVal = "n";
			}
		}
		lastBudget = getLCAgainstVolumeLastBudget(projectId, subProject, showBy, viewType);
		volumeOTR = getLCAgainstVolumeVolumeOTR(projectId, subProject);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_GUAGE_CHART);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, showByVal);
			pstm.setString(5, projectId);
			pstm.setArray(6, subProjectStrArr);
			pstm.setArray(7, subProjectStrArr);
			pstm.setString(8, projectId);
			pstm.setArray(9, subProjectStrArr);
			pstm.setArray(10, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new LcVolumeGaugeChartDTO();
				dto.setLastBDG(lastBudget);
				dto.setVolumeOTR(volumeOTR);
				dto.setVolumePacked(null != rs.getString("cte_volume_packed") ? rs.getString("cte_volume_packed") : "");
				dto.setVolumeShipped(
						null != rs.getString("cte_volume_shipped") ? rs.getString("cte_volume_shipped") : "");
				dto.setActualCost(null != rs.getString("actual_cost") ? rs.getString("actual_cost") : "");
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against guage chart :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public String getLCAgainstVolumeSummaryUpdatedOn(String projectId, String subProject) {
		String updatedOn = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_UPDATED_ON);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				updatedOn = null != rs.getString("last_updated_dt") ? rs.getString("last_updated_dt") : "";
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume updated on date :: " + e.getMessage());
		}
		return updatedOn;
	}

	@Override
	public Map<String, Object> getLCVolumeAnalysisQuarterlyGraphSummary(String projectId, String subProject,
			String viewType) {
		Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
		Map<String, LcVolumeAnalysisDTO> volumeMap = new TreeMap<String, LcVolumeAnalysisDTO>();
		LcVolumeAnalysisDTO dto = new LcVolumeAnalysisDTO();
		String volumeOTR = "", subProjectStr = "";
		getLCVolumeAnalysisQuarterlyGraphDates(volumeMap, projectId, subProject);
		volumeOTR = getLCAgainstVolumeVolumeOTR(projectId, subProject);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_QUARTERLY_GRAPH);) {
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (subProject.equalsIgnoreCase("OVERALL")) {
					subProjectStr = "0";
				} else {
					subProjectStr = subProject.replace(";", ",");
				}
			}
			log.info("subProject :: " + subProjectStr);
			pstm.setString(1, projectId);
			pstm.setString(2, subProjectStr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String yearQuarter = "";
				yearQuarter = rs.getString("period_out");
				if (null != yearQuarter && !yearQuarter.isEmpty() && !yearQuarter.equalsIgnoreCase("")) {
					dto = volumeMap.get(yearQuarter);
					if (null == dto) {
						dto = new LcVolumeAnalysisDTO();
						dto.setVolumePacked(
								null != rs.getString("volume_packed_out") ? rs.getString("volume_packed_out") : "");
						dto.setVolumeShipped(
								null != rs.getString("volume_shipped_out") ? rs.getString("volume_shipped_out") : "");
						dto.setVolumeOTR(volumeOTR);
						volumeMap.put(yearQuarter, dto);
					} else {
						dto.setVolumePacked(
								null != rs.getString("volume_packed_out") ? rs.getString("volume_packed_out") : "");
						dto.setVolumeShipped(
								null != rs.getString("volume_shipped_out") ? rs.getString("volume_shipped_out") : "");
						dto.setVolumeOTR(volumeOTR);
					}
				}
			}
			responseMap.put("volumeGraphMap", volumeMap);
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis quarterly graph summary :: " + e.getMessage());
		}
		return responseMap;
	}

	private void getLCVolumeAnalysisQuarterlyGraphDates(Map<String, LcVolumeAnalysisDTO> volumeMap, String projectId,
			String subProject) {
		LcVolumeAnalysisDTO dto = new LcVolumeAnalysisDTO();
		String minRFS = "", maxRFS = "", rfs = "", minBoxExpiration = "", minRFSQuarter = "", maxRFSQuarter = "",
				rfsQuarter = "", minBoxExpirationQuarter = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_GRAPH_DATES);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				minRFS = null != rs.getString("min_rfs") ? rs.getString("min_rfs") : "";
				minRFSQuarter = null != rs.getString("min_rfs_quarter") ? rs.getString("min_rfs_quarter") : "";
				maxRFS = null != rs.getString("max_rfs") ? rs.getString("max_rfs") : "";
				maxRFSQuarter = null != rs.getString("max_rfs_quarter") ? rs.getString("max_rfs_quarter") : "";
				minBoxExpiration = null != rs.getString("min_box_expiration_date")
						? rs.getString("min_box_expiration_date")
						: "";
				minBoxExpirationQuarter = null != rs.getString("min_box_expiration_quarter")
						? rs.getString("min_box_expiration_quarter")
						: "";
				rfs = null != rs.getString("min_rfs") ? rs.getString("min_rfs") : "";
				rfsQuarter = null != rs.getString("min_rfs_quarter") ? rs.getString("min_rfs_quarter") : "";

			}
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				// min box expiration
				if (null != minBoxExpiration && !minBoxExpiration.isEmpty() && !minBoxExpiration.equalsIgnoreCase("")) {
					dto = volumeMap.get(minBoxExpirationQuarter);
					if (null == dto) {
						dto = new LcVolumeAnalysisDTO();
						dto.setMinBoxExpiration(minBoxExpiration);
						volumeMap.put(minBoxExpirationQuarter, dto);
					} else {
						dto.setMinBoxExpiration(minBoxExpiration);
					}
				}
				if (!subProject.equalsIgnoreCase("OVERALL") && subProjectStr.length <= 1) {
					// rfs
					if (null != rfs && !rfs.isEmpty() && !rfs.equalsIgnoreCase("")) {
						dto = volumeMap.get(rfsQuarter);
						if (null == dto) {
							dto = new LcVolumeAnalysisDTO();
							dto.setRfs(rfs);
							volumeMap.put(rfsQuarter, dto);
						} else {
							dto.setRfs(rfs);
						}
					}
				} else {
					if (null != minRFS && !minRFS.isEmpty() && !minRFS.equalsIgnoreCase("")) {
						dto = volumeMap.get(minRFSQuarter);
						if (null == dto) {
							dto = new LcVolumeAnalysisDTO();
							dto.setMinRFS(minRFS);
							volumeMap.put(minRFSQuarter, dto);
						} else {
							dto.setMinRFS(minRFS);
						}
					}
					if (null != maxRFS && !maxRFS.isEmpty() && !maxRFS.equalsIgnoreCase("")) {
						dto = volumeMap.get(maxRFSQuarter);
						if (null == dto) {
							dto = new LcVolumeAnalysisDTO();
							dto.setMaxRFS(maxRFS);
							volumeMap.put(maxRFSQuarter, dto);
						} else {
							dto.setMaxRFS(maxRFS);
						}
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis graph dates :: " + e.getMessage());
		}
	}

	@Override
	public Map<String, Object> getLCVolumeAnalysisMonthlyGraphSummary(String projectId, String subProject,
			String viewType) {
		Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
		Map<String, LcVolumeAnalysisDTO> volumeMap = new LinkedHashMap<String, LcVolumeAnalysisDTO>();
		Map<String, LcVolumeAnalysisDTO> sortedVolumeMap = new LinkedHashMap<String, LcVolumeAnalysisDTO>();
		LcVolumeAnalysisDTO dto = new LcVolumeAnalysisDTO();
		String volumeOTR = "", subProjectStr = "";
		getLCVolumeAnalysisMonthlyGraphDates(volumeMap, projectId, subProject);
		volumeOTR = getLCAgainstVolumeVolumeOTR(projectId, subProject);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_MONTHLY_GRAPH);) {
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (subProject.equalsIgnoreCase("OVERALL")) {
					subProjectStr = "0";
				} else {
					subProjectStr = subProject.replace(";", ",");
				}
			}
			log.info("subProject :: " + subProjectStr);
			pstm.setString(1, projectId);
			pstm.setString(2, subProjectStr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new LcVolumeAnalysisDTO();
				String monthlyQuarter = "";
				monthlyQuarter = rs.getString("period_out");
				if (null != monthlyQuarter && !monthlyQuarter.isEmpty() && !monthlyQuarter.equalsIgnoreCase("")) {
					dto = volumeMap.get(monthlyQuarter);
					if (null == dto) {
						dto = new LcVolumeAnalysisDTO();
						dto.setVolumePacked(
								null != rs.getString("volume_packed_out") ? rs.getString("volume_packed_out") : "");
						dto.setVolumeShipped(
								null != rs.getString("volume_shipped_out") ? rs.getString("volume_shipped_out") : "");
						dto.setVolumeOTR(volumeOTR);
						volumeMap.put(monthlyQuarter, dto);
					} else {
						dto.setVolumePacked(
								null != rs.getString("volume_packed_out") ? rs.getString("volume_packed_out") : "");
						dto.setVolumeShipped(
								null != rs.getString("volume_shipped_out") ? rs.getString("volume_shipped_out") : "");
						dto.setVolumeOTR(volumeOTR);
					}
				}
			}
			sortedVolumeMap = getLCVolumeAnalysisMonthlySortedGraph(volumeMap);
			responseMap.put("volumeGraphMap", sortedVolumeMap);
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis monthly graph summary :: " + e.getMessage());
		}
		return responseMap;
	}

	private Map<String, LcVolumeAnalysisDTO> getLCVolumeAnalysisMonthlySortedGraph(
			Map<String, LcVolumeAnalysisDTO> volumeMap) {
		Map<String, LcVolumeAnalysisDTO> sortedVolumeMap = new LinkedHashMap<String, LcVolumeAnalysisDTO>();
		LcVolumeAnalysisDTO dto = new LcVolumeAnalysisDTO();
		List<String> keyList = new ArrayList<String>(volumeMap.keySet());
		Collections.sort(keyList, new Comparator<String>() {
			@Override
			public int compare(String p1, String p2) {
				try {
					return getDateParsed(p1).compareTo(getDateParsed(p2));
				} catch (ParseException e) {
					log.error("Exception while getting lc volume analysis monthly graph summary :: " + e.getMessage());
				}
				return 0;
			}
		});
		for (String key : keyList) {
			dto = new LcVolumeAnalysisDTO();
			dto = volumeMap.get(key);
			if (null != dto) {
				sortedVolumeMap.put(key, dto);
			}

		}
		return sortedVolumeMap;
	}

	public Date getDateParsed(String date) throws ParseException {
		return new SimpleDateFormat("MMM-yy", Locale.ENGLISH).parse(date);
	}

	private void getLCVolumeAnalysisMonthlyGraphDates(Map<String, LcVolumeAnalysisDTO> volumeMap, String projectId,
			String subProject) {
		LcVolumeAnalysisDTO dto = new LcVolumeAnalysisDTO();
		String minRFS = "", maxRFS = "", rfs = "", minBoxExpiration = "", minRFSMonth = "", maxRFSMonth = "",
				rfsMonth = "", minBoxExpirationMonth = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_GRAPH_DATES);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				minRFS = null != rs.getString("min_rfs") ? rs.getString("min_rfs") : "";
				minRFSMonth = null != rs.getString("min_rfs_month") ? rs.getString("min_rfs_month") : "";
				maxRFS = null != rs.getString("max_rfs") ? rs.getString("max_rfs") : "";
				maxRFSMonth = null != rs.getString("max_rfs_month") ? rs.getString("max_rfs_month") : "";
				minBoxExpiration = null != rs.getString("min_box_expiration_date")
						? rs.getString("min_box_expiration_date")
						: "";
				minBoxExpirationMonth = null != rs.getString("min_box_expiration_month")
						? rs.getString("min_box_expiration_month")
						: "";
				rfs = null != rs.getString("min_rfs") ? rs.getString("min_rfs") : "";
				rfsMonth = null != rs.getString("min_rfs_month") ? rs.getString("min_rfs_month") : "";
			}
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				// min box expiration
				if (null != minBoxExpiration && !minBoxExpiration.isEmpty() && !minBoxExpiration.equalsIgnoreCase("")) {
					dto = volumeMap.get(minBoxExpirationMonth);
					if (null == dto) {
						dto = new LcVolumeAnalysisDTO();
						dto.setMinBoxExpiration(minBoxExpiration);
						volumeMap.put(minBoxExpirationMonth, dto);
					} else {
						dto.setMinBoxExpiration(minBoxExpiration);
					}
				}
				if (!subProject.equalsIgnoreCase("OVERALL") && subProjectStr.length <= 1) {
					// rfs
					if (null != rfs && !rfs.isEmpty() && !rfs.equalsIgnoreCase("")) {
						dto = volumeMap.get(rfsMonth);
						if (null == dto) {
							dto = new LcVolumeAnalysisDTO();
							dto.setRfs(rfs);
							volumeMap.put(rfsMonth, dto);
						} else {
							dto.setRfs(rfs);
						}
					}
				} else {
					if (null != minRFS && !minRFS.isEmpty() && !minRFS.equalsIgnoreCase("")) {
						dto = volumeMap.get(minRFSMonth);
						if (null == dto) {
							dto = new LcVolumeAnalysisDTO();
							dto.setMinRFS(minRFS);
							volumeMap.put(minRFSMonth, dto);
						} else {
							dto.setMinRFS(minRFS);
						}
					}
					if (null != maxRFS && !maxRFS.isEmpty() && !maxRFS.equalsIgnoreCase("")) {
						dto = volumeMap.get(maxRFSMonth);
						if (null == dto) {
							dto = new LcVolumeAnalysisDTO();
							dto.setMaxRFS(maxRFS);
							volumeMap.put(maxRFSMonth, dto);
						} else {
							dto.setMaxRFS(maxRFS);
						}
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis graph dates :: " + e.getMessage());
		}
	}

	@Override
	public Map<String, Object> getLCVolumeAnalysisVolumePackedPiechartSummary(String projectId, String subProject,
			String viewType) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		LCVolumeAnalysisPiechart dto = new LCVolumeAnalysisPiechart();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_VOLUME_PACKED_PIE_CHART);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, projectId);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new LCVolumeAnalysisPiechart();
				String category = "";
				category = rs.getString("category");
				if (null != category && !category.isEmpty() && !category.equalsIgnoreCase("")) {
					dto.setVol(null != rs.getString("volume") ? rs.getString("volume") : "");
					dto.setPer(null != rs.getString("volume_per") ? rs.getString("volume_per") : "");
					responseMap.put(category, dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis summary volume packed pie chart summary :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLCVolumeAnalysisVolumeShippedPiechartSummary(String projectId, String subProject,
			String viewType) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		LCVolumeAnalysisPiechart dto = new LCVolumeAnalysisPiechart();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_VOLUME_SHIPPED_PIE_CHART);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, projectId);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto = new LCVolumeAnalysisPiechart();
				String category = "";
				category = rs.getString("category");
				if (null != category && !category.isEmpty() && !category.equalsIgnoreCase("")) {
					dto.setVol(null != rs.getString("volume") ? rs.getString("volume") : "");
					dto.setPer(null != rs.getString("volume_per") ? rs.getString("volume_per") : "");
					responseMap.put(category, dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis summary volume shipped pie chart summary :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public String getLCVolumeAnalysisSummaryUpdatedOn(String projectId, String subProject) {
		String updatedOn = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_UPDATED_ON);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				updatedOn = null != rs.getString("last_updated_dt") ? rs.getString("last_updated_dt") : "";
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis summary updated on date :: " + e.getMessage());
		}
		return updatedOn;
	}

	@Override
	public List<LcAgainstVolumeDetailsTransportationDTO> getLCAgainstVolumeTransportationDetails(String projectId,
			String subProject) {
		List<LcAgainstVolumeDetailsTransportationDTO> list = new ArrayList<LcAgainstVolumeDetailsTransportationDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_TRANSPORTATION_DETAILS);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, projectId);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subProjectStrArr);
			pstm.setString(7, projectId);
			pstm.setArray(8, subProjectStrArr);
			pstm.setArray(9, subProjectStrArr);
			pstm.setString(10, projectId);
			pstm.setArray(11, subProjectStrArr);
			pstm.setArray(12, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LcAgainstVolumeDetailsTransportationDTO dto = new LcAgainstVolumeDetailsTransportationDTO();
				dto.setSubProject(null != rs.getString("sub_project") ? rs.getString("sub_project") : "");
				dto.setInstallationCountry(
						null != rs.getString("installation_country") ? rs.getString("installation_country") : "");
				dto.setRacDate(null != rs.getString("rac_date") ? rs.getString("rac_date") : "");
				dto.setShipDate(null != rs.getString("ship_date") ? rs.getString("ship_date") : "");
				dto.setFreightTermsTitleTransfer(null != rs.getString("freight_terms_title_transfer")
						? rs.getString("freight_terms_title_transfer")
						: "");
				dto.setIncotermsCategory(
						null != rs.getString("incoterms_category") ? rs.getString("incoterms_category") : "");
				dto.setTransportationBdg(
						null != rs.getString("transportation_bdg") ? rs.getString("transportation_bdg") : "");
				dto.setTransportationLastBdg(
						null != rs.getString("transportation_last_bdg") ? rs.getString("transportation_last_bdg") : "");
				dto.setTransportationAct(
						null != rs.getString("transportation_act") ? rs.getString("transportation_act") : "");
				dto.setPerTranspBdgUsed(
						null != rs.getString("per_transp_bdg_used") ? rs.getString("per_transp_bdg_used") : "");
				dto.setVolumeOtr(null != rs.getString("volume_otr") ? rs.getString("volume_otr") : "");
				dto.setVolumeShipped(null != rs.getString("volume_shipped") ? rs.getString("volume_shipped") : "");
				dto.setVolumeShippedPer(
						null != rs.getString("volume_shipped_per") ? rs.getString("volume_shipped_per") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume transportation details popup :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<LcAgainstVolumeBoxPackingDetailsDTO> getLCAgainstVolumeBoxPackingDetails(String projectId,
			String subProject) {
		List<LcAgainstVolumeBoxPackingDetailsDTO> list = new ArrayList<LcAgainstVolumeBoxPackingDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_BOX_PACKING_DETAILS);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, projectId);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subProjectStrArr);
			pstm.setString(7, projectId);
			pstm.setArray(8, subProjectStrArr);
			pstm.setArray(9, subProjectStrArr);
			pstm.setString(10, projectId);
			pstm.setArray(11, subProjectStrArr);
			pstm.setArray(12, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LcAgainstVolumeBoxPackingDetailsDTO dto = new LcAgainstVolumeBoxPackingDetailsDTO();
				dto.setSubProject(null != rs.getString("sub_project") ? rs.getString("sub_project") : "");
				dto.setInstallationCountry(
						null != rs.getString("installation_country") ? rs.getString("installation_country") : "");
				dto.setRacDate(null != rs.getString("rac_date") ? rs.getString("rac_date") : "");
				dto.setReadyForShipment(
						null != rs.getString("ready_for_shipment") ? rs.getString("ready_for_shipment") : "");
				dto.setBpBdg(null != rs.getString("bp_bdg") ? rs.getString("bp_bdg") : "");
				dto.setBpLastBdg(null != rs.getString("bp_last_bdg") ? rs.getString("bp_last_bdg") : "");
				dto.setBpAct(null != rs.getString("bp_act") ? rs.getString("bp_act") : "");
				dto.setProductRate(null != rs.getString("product_rate") ? rs.getString("product_rate") : "");
				dto.setVolumeOtr(null != rs.getString("volume_otr") ? rs.getString("volume_otr") : "");
				dto.setVolumePacked(null != rs.getString("volume_packed") ? rs.getString("volume_packed") : "");
				dto.setVolumePackedPer(
						null != rs.getString("volume_packed_per") ? rs.getString("volume_packed_per") : "");
				dto.setVolumePackedLastMonth(
						null != rs.getString("volume_packed_last_month") ? rs.getString("volume_packed_last_month")
								: "");
				dto.setWood(null != rs.getString("wood") ? rs.getString("wood") : "");
				dto.setThermo(null != rs.getString("thermo") ? rs.getString("thermo") : "");
				dto.setBpNotNeeded(null != rs.getString("bp_not_needed") ? rs.getString("bp_not_needed") : "");
				dto.setExpirationDate(null != rs.getString("expiration_date") ? rs.getString("expiration_date") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume box and packing details popup :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<LcAgainstVolumeActualCostDetailsDTO> getLCAgainstVolumeActualCostDetails(String projectId,
			String subProject, String viewType, String showBy) {
		List<LcAgainstVolumeActualCostDetailsDTO> list = new ArrayList<LcAgainstVolumeActualCostDetailsDTO>();
		String showByVal = "";
		StringBuilder queryString = new StringBuilder();
		queryString.append(ShippingPreservationConstants.GET_LC_AGAINST_VOLUME_ANALYSIS_ACTUAL_COST_DETAILS);
		if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
			if (showBy.equalsIgnoreCase("Box & Packing")) {
				showByVal = "y";
			} else if (showBy.equalsIgnoreCase("Transportation")) {
				showByVal = "n";
			}
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(queryString.toString());) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, showByVal);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LcAgainstVolumeActualCostDetailsDTO dto = new LcAgainstVolumeActualCostDetailsDTO();
				dto.setJob(null != rs.getString("job") ? rs.getString("job") : "");
				dto.setDestinationOrganization(
						null != rs.getString("destination_organization") ? rs.getString("destination_organization")
								: "");
				dto.setRdiDestinationType(
						null != rs.getString("rdi_destination_type") ? rs.getString("rdi_destination_type") : "");
				dto.setVendorNum(null != rs.getString("vendor_num") ? rs.getString("vendor_num") : "");
				dto.setVendorName(null != rs.getString("vendor_name") ? rs.getString("vendor_name") : "");
				dto.setPoApprovalStatus(
						null != rs.getString("po_approval_status") ? rs.getString("po_approval_status") : "");
				dto.setPoBuyerName(null != rs.getString("po_buyer_name") ? rs.getString("po_buyer_name") : "");
				dto.setPoReleaseBuyerName(
						null != rs.getString("po_release_buyer_name") ? rs.getString("po_release_buyer_name") : "");
				dto.setPoNumber(null != rs.getString("po_number") ? rs.getString("po_number") : "");
				dto.setPoRelease(null != rs.getString("po_release") ? rs.getString("po_release") : "");
				dto.setPoShipment(null != rs.getString("po_shipment") ? rs.getString("po_shipment") : "");
				dto.setPoLine(null != rs.getString("po_line") ? rs.getString("po_line") : "");
				dto.setPoDistributionLine(
						null != rs.getString("po_distribution_line") ? rs.getString("po_distribution_line") : "");
				dto.setPoItemCode(null != rs.getString("po_item_code") ? rs.getString("po_item_code") : "");
				dto.setPoItemDescription(
						null != rs.getString("po_item_description") ? rs.getString("po_item_description") : "");
				dto.setItemDescription(
						null != rs.getString("item_description") ? rs.getString("item_description") : "");
				dto.setPoLineCreationDate(
						null != rs.getString("po_line_creation_date") ? rs.getString("po_line_creation_date") : "");
				dto.setNeedByDate(null != rs.getString("need_by_date") ? rs.getString("need_by_date") : "");
				dto.setPromisedDate(null != rs.getString("promised_date") ? rs.getString("promised_date") : "");
				dto.setPoDistributionQtyOrdered(null != rs.getString("po_distribution_qty_ordered")
						? rs.getString("po_distribution_qty_ordered")
						: "");
				dto.setUnitPriceCurr(null != rs.getString("unit_price_curr") ? rs.getString("unit_price_curr") : "");
				dto.setCurr(null != rs.getString("curr") ? rs.getString("curr") : "");
				dto.setPoEurConversionRate(
						null != rs.getString("po_eur_conversion_rate") ? rs.getString("po_eur_conversion_rate") : "");
				dto.setPoUnitValue(null != rs.getString("po_unit_value") ? rs.getString("po_unit_value") : "");
				dto.setPoDistributionValue(
						null != rs.getString("po_distribution_value") ? rs.getString("po_distribution_value") : "");
				dto.setPoValueReceived(
						null != rs.getString("po_value_received") ? rs.getString("po_value_received") : "");
				dto.setPoDistributionBilled(
						null != rs.getString("po_distribution_billed") ? rs.getString("po_distribution_billed") : "");
				dto.setPoDistributionToBill(
						null != rs.getString("po_distribution_to_bill") ? rs.getString("po_distribution_to_bill") : "");
				dto.setAccountNumber(null != rs.getString("account_number") ? rs.getString("account_number") : "");
				dto.setNoteToReceiver(null != rs.getString("note_to_receiver") ? rs.getString("note_to_receiver") : "");
				dto.setOrgId(null != rs.getString("org_id") ? rs.getString("org_id") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc against volume actual cost details popup :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<LCVolumeAnalysisDetailsDTO> getLCVolumeAnalysisPopupDetails(String projectId, String subProject,
			String viewType, String chartType, String category) {
		List<LCVolumeAnalysisDetailsDTO> list = new ArrayList<LCVolumeAnalysisDetailsDTO>();
		StringBuilder queryString = new StringBuilder();
		if (null != chartType && !chartType.isEmpty() && !chartType.equalsIgnoreCase("")) {
			if (chartType.equalsIgnoreCase("VOLUME_PACKED")) {
				queryString
						.append(ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_VOLUME_PACKED_PIE_CHART_DETAILS);
			} else if (chartType.equalsIgnoreCase("VOLUME_SHIPPED")) {
				queryString
						.append(ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_VOLUME_SHIPPED_PIE_CHART_DETAILS);
			}
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(queryString.toString());) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, category);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCVolumeAnalysisDetailsDTO dto = new LCVolumeAnalysisDetailsDTO();
				dto.setProject(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setSubProject(null != rs.getString("job") ? rs.getString("job") : "");
				dto.setSso(null != rs.getString("sso") ? rs.getString("sso") : "");
				dto.setBoxId(null != rs.getString("outer_most_box") ? rs.getString("outer_most_box") : "");
				dto.setBoxDescription(null != rs.getString("box_desc") ? rs.getString("box_desc") : "");
				dto.setPeiDescription(null != rs.getString("pei_desc") ? rs.getString("pei_desc") : "");
				dto.setBoxClosureDate(
						null != rs.getString("box_clouser_date1") ? rs.getString("box_clouser_date1") : "");
				dto.setPackType(null != rs.getString("pack_type") ? rs.getString("pack_type") : "");
				dto.setBarrierBag(null != rs.getString("barrier_bag") ? rs.getString("barrier_bag") : "");
				dto.setStorageCode(null != rs.getString("storage_code") ? rs.getString("storage_code") : "");
				dto.setPackagingId(null != rs.getString("packaging_id") ? rs.getString("packaging_id") : "");
				dto.setNetWeight(null != rs.getString("net_weight") ? rs.getString("net_weight") : "");
				dto.setGrossWeight(null != rs.getString("gross_weight") ? rs.getString("gross_weight") : "");
				dto.setLength(null != rs.getString("length1") ? rs.getString("length1") : "");
				dto.setWidth(null != rs.getString("width1") ? rs.getString("width1") : "");
				dto.setHeight(null != rs.getString("height1") ? rs.getString("height1") : "");
				dto.setVolume(null != rs.getString("volume") ? rs.getString("volume") : "");
				dto.setArrivalDate(null != rs.getString("arrival_date1") ? rs.getString("arrival_date1") : "");
				dto.setArrivalPlace(null != rs.getString("arrival_place") ? rs.getString("arrival_place") : "");
				dto.setExpirationDate(null != rs.getString("expiration_date1") ? rs.getString("expiration_date1") : "");
				dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				dto.setConfirmDate(null != rs.getString("confirm_date1") ? rs.getString("confirm_date1") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis popup details :: " + e.getMessage());
		}
		return list;

	}

	@Override
	public List<LCVolumeAnalysisDetailsDTO> getLCVolumeAnalysisExcelDetails(String projectId, String subProject) {
		List<LCVolumeAnalysisDetailsDTO> list = new ArrayList<LCVolumeAnalysisDetailsDTO>();
		StringBuilder queryString = new StringBuilder();
		queryString.append(ShippingPreservationConstants.GET_LC_VOLUME_ANALYSIS_PIE_CHART_DETAILS);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(queryString.toString());) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCVolumeAnalysisDetailsDTO dto = new LCVolumeAnalysisDetailsDTO();
				dto.setProject(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setSubProject(null != rs.getString("job") ? rs.getString("job") : "");
				dto.setSso(null != rs.getString("sso") ? rs.getString("sso") : "");
				dto.setBoxId(null != rs.getString("outer_most_box") ? rs.getString("outer_most_box") : "");
				dto.setBoxDescription(null != rs.getString("box_desc") ? rs.getString("box_desc") : "");
				dto.setPeiDescription(null != rs.getString("pei_desc") ? rs.getString("pei_desc") : "");
				dto.setBoxClosureDate(
						null != rs.getString("box_clouser_date1") ? rs.getString("box_clouser_date1") : "");
				dto.setPackType(null != rs.getString("pack_type") ? rs.getString("pack_type") : "");
				dto.setBarrierBag(null != rs.getString("barrier_bag") ? rs.getString("barrier_bag") : "");
				dto.setStorageCode(null != rs.getString("storage_code") ? rs.getString("storage_code") : "");
				dto.setPackagingId(null != rs.getString("packaging_id") ? rs.getString("packaging_id") : "");
				dto.setNetWeight(null != rs.getString("net_weight") ? rs.getString("net_weight") : "");
				dto.setGrossWeight(null != rs.getString("gross_weight") ? rs.getString("gross_weight") : "");
				dto.setLength(null != rs.getString("length1") ? rs.getString("length1") : "");
				dto.setWidth(null != rs.getString("width1") ? rs.getString("width1") : "");
				dto.setHeight(null != rs.getString("height1") ? rs.getString("height1") : "");
				dto.setVolume(null != rs.getString("volume") ? rs.getString("volume") : "");
				dto.setArrivalDate(null != rs.getString("arrival_date1") ? rs.getString("arrival_date1") : "");
				dto.setArrivalPlace(null != rs.getString("arrival_place") ? rs.getString("arrival_place") : "");
				dto.setExpirationDate(null != rs.getString("expiration_date1") ? rs.getString("expiration_date1") : "");
				dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				dto.setConfirmDate(null != rs.getString("confirm_date1") ? rs.getString("confirm_date1") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc volume analysis excel details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public String getLCSOSCellData(String projectId) {
		String sosCheckFlag = "N";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(ShippingPreservationConstants.GET_LC_SOS_CELL_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				sosCheckFlag = null != rs.getString("sos_cell") ? rs.getString("sos_cell") : "N";
			}
		} catch (SQLException e) {
			log.error("Exception while getting lc sos cell data :: " + e.getMessage());
		}
		return sosCheckFlag;
	}

	@Override
	public int saveLCSOSCellData(LCSOSCellDTO sosCellDTO, String sso) {
		int result = 0;
		deleteLCSOSCellData(sosCellDTO.getProjectId());
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.INSERT_LC_SOS_CELL_DETAILS);) {
			pstm.setString(1, sosCellDTO.getProjectId());
			pstm.setString(2, sosCellDTO.getSosData());
			pstm.setString(3, sso);
			if (pstm.executeUpdate() > 0) {
				result = 1;
			}
		} catch (SQLException e) {
			log.error("Error occured while saving sos cell details: " + e.getMessage());
		}
		return result;
	}

	private void deleteLCSOSCellData(String projectId) {
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(ShippingPreservationConstants.DELETE_LC_SOS_CELL_DETAILS);) {
			pstm.setString(1, projectId);
			int result = pstm.executeUpdate();
			if (result > 0) {
				log.info("Deleted SOS Cell data for project id :: " + projectId);
			}
		} catch (SQLException e) {
			log.error("Error occured while deleting sos cell details: " + e.getMessage());
		}

	}

}